package com.zfy.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.adapter.able.ModelTypeable;
import com.zfy.adapter.collections.AbstractLightList;
import com.zfy.adapter.collections.LightDiffList;
import com.zfy.adapter.common.AdapterException;
import com.zfy.adapter.common.LightValues;
import com.zfy.adapter.delegate.DelegateRegistry;
import com.zfy.adapter.delegate.IDelegate;
import com.zfy.adapter.delegate.impl.DragSwipeDelegate;
import com.zfy.adapter.delegate.impl.EmptyViewDelegate;
import com.zfy.adapter.delegate.impl.HFViewDelegate;
import com.zfy.adapter.delegate.impl.LoadMoreDelegate;
import com.zfy.adapter.delegate.impl.LoadingViewDelegate;
import com.zfy.adapter.delegate.impl.NotifyDelegate;
import com.zfy.adapter.delegate.impl.SectionDelegate;
import com.zfy.adapter.delegate.impl.SelectorDelegate;
import com.zfy.adapter.delegate.impl.SpanDelegate;
import com.zfy.adapter.delegate.impl.TopMoreDelegate;
import com.zfy.adapter.listener.EventCallback;
import com.zfy.adapter.listener.ModelTypeUpdater;
import com.zfy.adapter.model.Ids;
import com.zfy.adapter.model.ModelType;
import com.zfy.adapter.model.SingleTypeUpdater;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * CreateAt : 2016/19/7
 * Describe : adapter 基类
 * D 数据基于范型定义
 * ViewHolder 统一使用 LightHolder
 * Adapter 负责内容数据的加载，这部分也可以抽离成 Delegate
 * 其他各个功能由 Delegate 实现
 * @author chendong
 */
public abstract class LightAdapter<D> extends RecyclerView.Adapter<LightHolder>
        implements LightEvent.EventSetting<D> {

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
    // 类型和 TypeOptions 配置
    private SparseArray<ModelType> mModelTypeCache;
    // 更新类型配置
    private List<ModelTypeUpdater> mModelTypeUpdaters;
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
        this(context, datas, new SingleTypeUpdater(LightValues.TYPE_CONTENT, data -> {
            data.layoutId = layoutId;
        }));
    }

    /**
     * 多类型适配器构造函数
     *
     * @param context 上下文
     * @param datas   数据源
     * @param updater 类型构造工厂
     */
    public LightAdapter(Context context, List<D> datas, ModelTypeUpdater updater) {
        init(context, datas);
        addModelUpdater(updater);
    }

    // 通用初始化方法
    private void init(Context context, List<D> datas) {
        if (datas instanceof AbstractLightList) {
            ((LightDiffList) datas).setAdapter(this);
        }
        mContext = context;
        mHolderCache = new HashSet<>();
        mLayoutInflater = LayoutInflater.from(context);
        mDatas = datas;
        mModelTypeCache = new SparseArray<>();
        mHolderCache = new HashSet<>();
        // 代理注册表
        mDelegateRegistry = new DelegateRegistry();
        mDelegateRegistry.register(new SpanDelegate());
        mDelegateRegistry.register(new NotifyDelegate());
        mDelegateRegistry.register(IDelegate.HF, HFViewDelegate::new);
        mDelegateRegistry.register(IDelegate.TOP_MORE, TopMoreDelegate::new);
        mDelegateRegistry.register(IDelegate.LOAD_MORE, LoadMoreDelegate::new);
        mDelegateRegistry.register(IDelegate.SELECTOR, SelectorDelegate::new);
        mDelegateRegistry.register(IDelegate.LOADING, LoadingViewDelegate::new);
        mDelegateRegistry.register(IDelegate.EMPTY, EmptyViewDelegate::new);
        mDelegateRegistry.register(IDelegate.DRAG_SWIPE, DragSwipeDelegate::new);
        mDelegateRegistry.register(IDelegate.SECTION, SectionDelegate::new);
        mDelegateRegistry.onAttachAdapter(this);
        // 事件处理
        mLightEvent = new LightEvent<>(this);
        mModelTypeUpdaters = new ArrayList<>();
        // 内置类型参数构建
        addModelUpdater(type -> {
            if (type.getType() == LightValues.TYPE_FOOTER
                    || type.getType() == LightValues.TYPE_HEADER
                    || type.getType() == LightValues.TYPE_LOADING
                    || type.getType() == LightValues.TYPE_EMPTY) {
                type.setSpanSize(LightValues.SPAN_SIZE_ALL);
            }
        });
    }

    @Override
    public LightHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LightHolder holder = mDelegateRegistry.onCreateViewHolder(parent, viewType);
        if (holder == null) {
            View view = null;
            ModelType type = getType(viewType);
            if (type != null && type.getLayoutId() > 0) {
                view = mLayoutInflater.inflate(type.getLayoutId(), parent, false);
                if (view != null) {
                    holder = new LightHolder(this, viewType, view);
                    mLightEvent.initEvent(holder, type);
                    mHolderCache.add(holder);
                }
                if (holder == null) {
                    throw new AdapterException("holder is null layout = " + type.layoutId + " , type = " + type.type);
                }
            } else {
                throw new AdapterException("can not find type viewType = " + viewType);
            }

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
        int itemViewType = mDelegateRegistry.getItemViewType(position);
        if (itemViewType != LightValues.NONE) {
            return itemViewType;
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
        return position - mDelegateRegistry.getAboveItemCount(LightValues.FLOW_LEVEL_CONTENT);
    }

    /**
     * 数据位置 转换为 布局中的位置
     *
     * @param position 数据中的位置
     * @return 布局中的位置
     */
    public int toLayoutIndex(int position) {
        return position + mDelegateRegistry.getAboveItemCount(LightValues.FLOW_LEVEL_CONTENT);
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


    @Override
    public void setClickCallback(EventCallback<D> clickCallback) {
        mLightEvent.setClickCallback(clickCallback);
    }

    @Override
    public void setLongPressCallback(EventCallback<D> longPressCallback) {
        mLightEvent.setLongPressCallback(longPressCallback);
    }

    @Override
    public void setDbClickCallback(EventCallback<D> dbClickCallback) {
        mLightEvent.setDbClickCallback(dbClickCallback);
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
     * 添加类型更新器
     *
     * @param updater ModelTypeUpdater
     */
    public void addModelUpdater(ModelTypeUpdater updater) {
        mModelTypeUpdaters.add(updater);
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
            for (ModelTypeUpdater updater : mModelTypeUpdaters) {
                updater.update(modelType);
            }
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
    public @Nullable ModelType getType(D data) {
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
    public HFViewDelegate header() {
        return getDelegate(IDelegate.HF);
    }

    /**
     * @return HFDelegate 执行 footer 相关功能
     */
    public HFViewDelegate footer() {
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
    public SelectorDelegate<D> selector() {
        return getDelegate(IDelegate.SELECTOR);
    }

    /**
     * @return LoadingViewDelegate 加载动画功能
     */
    public LoadingViewDelegate loadingView() {
        return getDelegate(IDelegate.LOADING);
    }

    /**
     * @return EmptyViewDelegate 空白页面功能
     */
    public EmptyViewDelegate emptyView() {
        return getDelegate(IDelegate.EMPTY);
    }

    /**
     * @return DragSwipeDelegate 拖动和滑动 功能
     */
    public DragSwipeDelegate dragSwipe() {
        return getDelegate(IDelegate.DRAG_SWIPE);
    }

    /**
     * @return SectionDelegate 隔断效果
     */
    public SectionDelegate<D> section() {
        return getDelegate(IDelegate.SECTION);
    }
}
