package com.zfy.component.basic.mvx.mvvm.app;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.common.exts.LogX;
import com.zfy.component.basic.ComponentX;
import com.zfy.component.basic.app.AppDelegate;
import com.zfy.component.basic.app.view.IViewInit;
import com.zfy.component.basic.app.view.ViewConfig;
import com.zfy.component.basic.mvx.mvvm.BaseViewModel;
import com.zfy.component.basic.mvx.mvvm.IBindingView;

/**
 * CreateAt : 2018/9/12
 * Describe :
 *
 * @author chendong
 */
public class MvvmDelegate<VideoModel extends BaseViewModel, VDB extends ViewDataBinding> extends AppDelegate implements IBindingView<VideoModel, VDB> {

    public static final String TAG = MvvmDelegate.class.getSimpleName();

    private VideoModel mViewModel;
    private VDB        mBinding;

    private <T extends LifecycleOwner> void attach(T obj) {
        ComponentX.inject(obj);
        mHost = obj;
        mLifecycleOwner = obj;
        mLifecycleRegistry = new LifecycleRegistry(mLifecycleOwner);
        if (obj instanceof IViewInit && ((IViewInit) obj).getViewConfig() != null) {
            mViewConfig = ((IViewInit) obj).getViewConfig();
        } else {
            VM annotation = mHost.getClass().getAnnotation(VM.class);
            if (annotation != null) {
                int layout = annotation.layout();
                Class vm = annotation.vm();
                int vmId = annotation.vmId();
                if (layout != 0) {
                    mViewConfig = ViewConfig.makeMvvm(layout, vmId, vm);
                }
            }
        }
        if (mViewConfig == null) {
            throw new IllegalStateException("require ViewConfig");
        }
    }

    @Override
    public View bindFragmentDispatch(LifecycleOwner owner, LayoutInflater inflater, ViewGroup container) {
        attach(owner);
        mBinding = DataBindingUtil.inflate(inflater, mViewConfig.getLayout(), container, false);
        bindView(mHost, mBinding.getRoot());
        bindEvent();
        init();
        return mBinding.getRoot();
    }

    @Override
    public void bindActivityDispatch(LifecycleOwner owner) {
        attach(owner);
        mBinding = DataBindingUtil.setContentView(((Activity) owner), mViewConfig.getLayout());
        bindView(mHost, null);
        init();
    }

    @Override
    public void bindNoLayoutViewDispatch(LifecycleOwner owner, Object host) {
        super.bindNoLayoutViewDispatch(owner, host);
    }

    private void init() {
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
    private VideoModel makeViewModel(Class clazz) {
        if (mHost instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) mHost;
            VideoModel viewModel = (VideoModel) ViewModelProviders.of(activity).get(clazz);
            addObserver(viewModel);
            viewModel.init();
            return viewModel;
        }
        return null;
    }


    @Override
    public VideoModel viewModel() {
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

}
