package com.zfy.component.basic.mvvm.app;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.common.exts.LogX;
import com.zfy.component.basic.ComponentX;
import com.zfy.component.basic.mvvm.BaseViewModel;

/**
 * CreateAt : 2018/9/12
 * Describe :
 *
 * @author chendong
 */
public class MvvmDelegate<VM extends BaseViewModel, VDB extends ViewDataBinding> implements IBindingView<VM, VDB> {

    public static final String TAG = MvvmDelegate.class.getSimpleName();

    public interface IViewInit {
        ViewConfig getViewConfig();
    }

    private Object mHost;
    private LifecycleOwner mLifecycleOwner;
    private ViewConfig mViewConfig;
    private VM mViewModel;
    private VDB mBinding;
    private LifecycleRegistry mLifecycleRegistry;

    private <T extends LifecycleOwner> void attach(T obj) {
        ComponentX.inject(obj);
        mHost = obj;
        mLifecycleOwner = obj;
        mLifecycleRegistry = new LifecycleRegistry(mLifecycleOwner);
        if (obj instanceof IViewInit && ((IViewInit) obj).getViewConfig() != null) {
            mViewConfig = ((IViewInit) obj).getViewConfig();
        } else {
            com.zfy.component.basic.mvvm.app.VM annotation = mHost.getClass().getAnnotation(com.zfy.component.basic.mvvm.app.VM.class);
            int layout = annotation.layout();
            Class vm = annotation.vm();
            int vmId = annotation.vmId();
            if (layout != 0) {
                mViewConfig = ViewConfig.make(layout, vmId, vm);
            }
        }
    }

    public View bindFragment(LifecycleOwner owner, LayoutInflater inflater, ViewGroup container) {
        attach(owner);
        mBinding = DataBindingUtil.inflate(inflater, mViewConfig.getLayout(), container, false);
        init();
        return mBinding.getRoot();
    }

    public void bindActivity(LifecycleOwner owner) {
        attach(owner);
        mBinding = DataBindingUtil.setContentView(((Activity) owner), mViewConfig.getLayout());
        init();
    }

    @Override
    public void init() {
        // binding 绑定到生命周期
        mBinding.setLifecycleOwner(mLifecycleOwner);
        // 生成 view model
        if (mViewConfig.getVmClazz() != null) {
            mViewModel = makeViewModel(mViewConfig.getVmClazz());
        } else {
            LogX.e(TAG, "viewModel class is null (" + mViewConfig.getVmId() + ")");
        }
        // 绑定到 binding
        if (mViewModel != null && mViewConfig.getVmId() != 0) {
            mBinding.setVariable(mViewConfig.getVmId(), mViewModel);
        } else {
            LogX.e(TAG, "viewModel is null or vmId is 0 (" + mViewConfig.getVmId() + "," + mViewConfig.getVmClazz() + ")");
        }
    }

    // 创建 ViewModel 并绑定到生命周期
    @SuppressWarnings("unchecked")
    private VM makeViewModel(Class clazz) {
        if (mHost instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) mHost;
            VM viewModel = (VM) ViewModelProviders.of(activity).get(clazz);
            addObserver(viewModel);
            viewModel.init();
            return viewModel;
        }
        return null;
    }

    private void addObserver(@NonNull LifecycleObserver observer) {
        mLifecycleRegistry.addObserver(observer);
    }

    @Override
    public VM viewModel() {
        return mViewModel;
    }

    @Override
    public VDB binding() {
        return mBinding;
    }

    @Override
    public <E extends BaseViewModel> E provideViewModel(Class<E> clazz) {
        if (mHost instanceof FragmentActivity) {
            return ViewModelProviders.of((FragmentActivity) mHost).get(clazz);
        } else if (mHost instanceof Fragment) {
            Fragment fragment = (Fragment) mHost;
            if (fragment.getActivity() != null) {
                return ViewModelProviders.of(fragment.getActivity()).get(clazz);
            }
        }
        return null;
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

}
