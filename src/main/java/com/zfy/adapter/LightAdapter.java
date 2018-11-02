package com.zfy.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.adapter.able.ModelTypeable;
import com.zfy.adapter.collections.LightDiffList;
import com.zfy.adapter.common.LightValues;
import com.zfy.adapter.delegate.DelegateRegistry;
import com.zfy.adapter.delegate.IDelegate;
import com.zfy.adapter.delegate.impl.HFDelegate;
import com.zfy.adapter.delegate.impl.LoadMoreDelegate;
import com.zfy.adapter.delegate.impl.NotifyDelegate;
import com.zfy.adapter.delegate.impl.SelectorDelegate;
import com.zfy.adapter.delegate.impl.TopMoreDelegate;
import com.zfy.adapter.listener.ModelTypeFactory;
import com.zfy.adapter.listener.OnItemListener;
import com.zfy.adapter.model.Ids;
import com.zfy.adapter.model.ModelType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * CreateAt : 2016/19/7
 * Describe : adapter基类，主要负责数据适配的相关逻辑，事件，module
 *
 * @author chendong
 */
public abstract class LightAdapter<D> extends RecyclerView.Adapter<LightHolder> {

    // View
    private RecyclerView mRecyclerView;
    // 上下文
    private Context mContext;
    // 数据源
    private List<D> mDatas;
    // 布局加载
    private LayoutInflater mLayoutInflater;
    // 用来存储创建的所有holder，你可以使用holder来直接更新item，而不必调用 notify
    private Set<LightHolder> mHolderCache;
    // 点击监听时间
    private OnItemListener<D> mOnItemListener;
    // 类型和 TypeOptions 配置
    private SparseArray<ModelType> mModelTypeCache;
    // 类型构造器
    private ModelTypeFactory mModelTypeFactory;
    private ModelTypeFactory mBuildInModelTypeFactory;
    // 代理注册表
    private DelegateRegistry mDelegateRegistry;
    // 负责完成事件的初始化和触发
    private LightEvent<D> mLightEvent;
    // 多 ID 绑定
    private Ids mIds;

    /**
     * 单类型适配器构造函数
     *
     * @param context  上下文
     * @param datas    数据源
     * @param layoutId 布局
     */
    public LightAdapter(Context context, List<D> datas, int layoutId) {
        this(context, datas, modelType -> modelType.setLayout(layoutId));
    }

    /**
     * 多类型适配器构造函数
     *
     * @param context 上下文
     * @param datas   数据源
     * @param factory 类型构造工厂
     */
    public LightAdapter(Context context, List<D> datas, ModelTypeFactory factory) {
        init(context, datas);
        mModelTypeFactory = factory;
    }

    // 通用初始化方法
    private void init(Context context, List<D> datas) {
        if (datas instanceof LightDiffList) {
            ((LightDiffList) datas).setLightAdapter(this);
        }
        mContext = context;
        mHolderCache = new HashSet<>();
        mLayoutInflater = LayoutInflater.from(context);
        mDatas = datas;
        mModelTypeCache = new SparseArray<>();
        mHolderCache = new HashSet<>();
        mDelegateRegistry = new DelegateRegistry();
        mDelegateRegistry.onAttachAdapter(this);
        mLightEvent = new LightEvent<>(this);
        mBuildInModelTypeFactory = type -> {
            if (type.getType() == LightValues.TYPE_FOOTER || type.getType() == LightValues.TYPE_HEADER) {
                type.setSpanSize(LightValues.SPAN_SIZE_ALL);
            }
        };
    }

    @Override
    public LightHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LightHolder holder = mDelegateRegistry.onCreateViewHolder(parent, viewType);
        if (holder == null) {
            View view = null;
            ModelType type = getType(viewType);
            if (type != null && type.getLayout() > 0) {
                view = mLayoutInflater.inflate(type.getLayout(), parent, false);
            }
            holder = new LightHolder(this, viewType, view);
            mLightEvent.initEvent(holder, type);
            mHolderCache.add(holder);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LightHolder holder, int position) {
        if (!mDelegateRegistry.onBindViewHolder(holder, position)) {
            int pos = toModelIndex(position);
            D data = getItem(pos);
            onBindView(holder, data, pos);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull LightHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            int pos = toModelIndex(position);
            D data = getItem(pos);
            onBindView(holder, data, pos);
            for (Object payload : payloads) {
                if (payload instanceof Set && !((Set) payload).isEmpty()) {
                    Set msgSet = (Set) payload;
                    for (Object o : msgSet) {
                        if (o instanceof String) {
                            onBindViewUsePayload(holder, data, pos, (String) o);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mDelegateRegistry.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return this.mDatas.size() + mDelegateRegistry.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        int hfType = mDelegateRegistry.getItemViewType(position);
        if (hfType != LightValues.NONE) {
            return hfType;
        }
        D d = getItem(toModelIndex(position));
        if (d instanceof ModelTypeable) {
            ModelTypeable model = (ModelTypeable) d;
            return model.getModelType();
        } else {
            return LightValues.TYPE_CONTENT;
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull LightHolder holder) {
        super.onViewAttachedToWindow(holder);
        mDelegateRegistry.onViewAttachedToWindow(holder);
    }

    /**
     * 布局中的位置 转换为 数据里面的 位置
     *
     * @param position 布局中的位置
     * @return 数据中的位置
     */
    public int toModelIndex(int position) {
        HFDelegate delegate = mDelegateRegistry.get(IDelegate.HF);
        if (delegate.isHeaderEnable()) {
            return position - 1;
        }
        return position;
    }

    /**
     * 数据位置 转换为 布局中的位置
     *
     * @param position 数据中的位置
     * @return 布局中的位置
     */
    public int toLayoutIndex(int position) {
        HFDelegate delegate = mDelegateRegistry.get(IDelegate.HF);
        if (delegate.isHeaderEnable()) {
            return position + 1;
        }
        return position;
    }


    /**
     * 获取一个 Item
     *
     * @param pos position
     * @return data
     */
    public D getItem(int pos) {
        if (pos >= 0 && pos < mDatas.size()) {
            return mDatas.get(pos);
        } else {
            return null;
        }
    }

    /**
     * 一般绑定数据
     *
     * @param holder LightHolder
     * @param data   数据
     * @param pos    位置
     */
    public abstract void onBindView(LightHolder holder, D data, int pos);

    /**
     * 使用 payload 绑定数据
     *
     * @param holder LightHolder
     * @param data   数据
     * @param pos    位置
     * @param msg    消息
     */
    public void onBindViewUsePayload(LightHolder holder, D data, int pos, String msg) {
    }


    /**
     * 设置点击事件
     *
     * @param onItemListener
     */
    public void setOnItemListener(final OnItemListener<D> onItemListener) {
        mLightEvent.setOnItemListener(onItemListener);
    }

    /**
     * 获取创建的 所有 holder
     *
     * @return holder set
     */
    public Set<LightHolder> getHolderCache() {
        return mHolderCache;
    }

    /**
     * @return 数据源
     */
    public List<D> getDatas() {
        return mDatas;
    }

    /**
     * @param datas 切换数据源
     */
    public void setDatas(List<D> datas) {
        mDatas = datas;
    }

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 获取 RecyclerView
     *
     * @return RecyclerView
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }


    /**
     * 复用 id 集合
     *
     * @param ids id 集合
     * @return Ids
     */
    public Ids all(int... ids) {
        if (mIds == null) {
            mIds = Ids.all();
        }
        return mIds.obtain(ids);
    }

    /**
     * 根据类型获取 ModelType
     *
     * @param type 类型
     * @return 数据的类型
     */
    public ModelType getType(int type) {
        ModelType modelType = mModelTypeCache.get(type);
        if (modelType == null) {
            modelType = new ModelType(type);
            mBuildInModelTypeFactory.update(modelType);
            mModelTypeFactory.update(modelType);
            mModelTypeCache.put(type, modelType);
        }
        return modelType;
    }

    /**
     * 根据数据获取 ModelType
     *
     * @param data 数据
     * @return 数据的类型
     */
    public ModelType getType(D data) {
        if (data == null) {
            return null;
        }
        int type;
        if (data instanceof ModelTypeable) {
            type = ((ModelTypeable) data).getModelType();
        } else {
            type = LightValues.TYPE_CONTENT;
        }
        return getType(type);
    }

    /**
     * @return DelegateRegistry 获取注册表，注册和获取
     */
    public DelegateRegistry getDelegateRegistry() {
        return mDelegateRegistry;
    }


    /**
     * 从注册中心获取委托实例
     *
     * @param key        委托类的 key
     * @param <Delegate> 范型
     * @return 功能代理
     */
    public <Delegate extends IDelegate> Delegate getDelegate(int key) {
        return mDelegateRegistry.get(key);
    }


    /**
     * @return HFDelegate 执行 header 相关功能
     */
    public HFDelegate header() {
        return getDelegate(IDelegate.HF);
    }

    /**
     * @return HFDelegate 执行 footer 相关功能
     */
    public HFDelegate footer() {
        return getDelegate(IDelegate.HF);
    }

    /**
     * @return NotifyDelegate 执行 数据更新 相关功能
     */
    public NotifyDelegate notifyItem() {
        return getDelegate(IDelegate.NOTIFY);
    }

    /**
     * @return LoadMoreDelegate 执行底部加载更多功能
     */
    public LoadMoreDelegate loadMore() {
        return getDelegate(IDelegate.LOAD_MORE);
    }

    /**
     * @return TopMoreDelegate 执行顶部加载更多功能
     */
    public TopMoreDelegate topMore() {
        return getDelegate(IDelegate.TOP_MORE);
    }

    /**
     * @return SelectorDelegate 负责选择器功能
     */
    public SelectorDelegate selector() {
        return getDelegate(IDelegate.SELECTOR);
    }
}