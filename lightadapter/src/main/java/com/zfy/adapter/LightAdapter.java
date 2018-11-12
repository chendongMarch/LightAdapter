package com.zfy.adapter;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.adapter.able.Typeable;
import com.zfy.adapter.able.Sectionable;
import com.zfy.adapter.annotations.ModelIndex;
import com.zfy.adapter.collections.AbstractLightList;
import com.zfy.adapter.collections.LightDiffList;
import com.zfy.adapter.common.AdapterException;
import com.zfy.adapter.common.ItemType;
import com.zfy.adapter.common.LightUtils;
import com.zfy.adapter.common.LightValues;
import com.zfy.adapter.common.SpanSize;
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
import com.zfy.adapter.delegate.refs.DragSwipeRef;
import com.zfy.adapter.delegate.refs.EmptyViewRef;
import com.zfy.adapter.delegate.refs.FooterRef;
import com.zfy.adapter.delegate.refs.HeaderRef;
import com.zfy.adapter.delegate.refs.LoadMoreRef;
import com.zfy.adapter.delegate.refs.LoadingViewRef;
import com.zfy.adapter.delegate.refs.NotifyRef;
import com.zfy.adapter.delegate.refs.SectionRef;
import com.zfy.adapter.delegate.refs.SelectorRef;
import com.zfy.adapter.delegate.refs.TopMoreRef;
import com.zfy.adapter.items.ItemAdapter;
import com.zfy.adapter.listener.EventCallback;
import com.zfy.adapter.listener.ModelTypeConfigCallback;
import com.zfy.adapter.model.Ids;
import com.zfy.adapter.model.ModelType;
import com.zfy.adapter.model.SingleTypeConfigCallback;

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
    private RecyclerView                  mRecyclerView;
    // 上下文
    private Context                       mContext;
    // 数据源
    private List<D>                       mDatas;
    // 布局加载
    private LayoutInflater                mLayoutInflater;
    // 用来存储创建的所有holder，你可以使用holder来直接更新item，而不必调用 notify
    private Set<LightHolder>              mHolderCache;
    // 类型和 TypeOptions 配置
    private SparseArray<ModelType>        mModelTypeCache;
    // 更新类型配置
    private List<ModelTypeConfigCallback> mModelTypeConfigCallbacks;
    // 代理注册表
    private DelegateRegistry              mDelegateRegistry;
    // 负责完成事件的初始化和触发
    private LightEvent<D>                 mLightEvent;
    // 多 ID 绑定
    private Ids                           mIds;

    /**
     * 单类型适配器构造函数
     *
     * @param datas    数据源
     * @param layoutId 布局
     */
    public LightAdapter(List<D> datas, @LayoutRes int layoutId) {
        this(datas, new SingleTypeConfigCallback(data -> {
            data.layoutId = layoutId;
        }).setSingleType(ItemType.TYPE_CONTENT));
    }

    /**
     * 多类型适配器构造函数
     *
     * @param datas   数据源
     * @param updater 类型构造工厂
     */
    public LightAdapter(List<D> datas, ModelTypeConfigCallback updater) {
        init(datas);
        addModelUpdater(updater);
    }

    // 通用初始化方法
    private void init(List<D> datas) {
        if (datas instanceof AbstractLightList) {
            ((LightDiffList) datas).setAdapter(this);
        }
        mHolderCache = new HashSet<>();
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
        mModelTypeConfigCallbacks = new ArrayList<>();
        // 内置类型参数构建
        addModelUpdater(type -> {
            if (type.getType() == ItemType.TYPE_FOOTER
                    || type.getType() == ItemType.TYPE_HEADER
                    || type.getType() == ItemType.TYPE_LOADING
                    || type.getType() == ItemType.TYPE_EMPTY) {
                type.setSpanSize(SpanSize.SPAN_SIZE_ALL);
            }
        });
    }

    @Override
    public final LightHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LightHolder holder = mDelegateRegistry.onCreateViewHolder(parent, viewType);
        if (holder == null) {
            View view = null;
            ModelType type = getModelType(viewType);
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
    public final void onBindViewHolder(@NonNull LightHolder holder, int position) {
        if (!mDelegateRegistry.onBindViewHolder(holder, position)) {
            int pos = toModelIndex(position);
            D data = getItem(pos);
            onBindView(holder, data, pos);
        }
    }

    @Override
    public final void onBindViewHolder(@NonNull LightHolder holder, int position, @NonNull List<Object> payloads) {
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
    public final void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mContext = recyclerView.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        mDelegateRegistry.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public final int getItemCount() {
        return this.mDatas.size() + mDelegateRegistry.getItemCount();
    }

    @Override
    public final int getItemViewType(int position) {
        int itemViewType = mDelegateRegistry.getItemViewType(position);
        if (itemViewType != ItemType.TYPE_NONE) {
            return itemViewType;
        }
        D d = getItem(toModelIndex(position));
        if (d instanceof Sectionable && ((Sectionable) d).isSection()) {
            return ItemType.TYPE_SECTION;
        } else if (d instanceof Typeable) {
            Typeable model = (Typeable) d;
            return model.getItemType();
        } else {
            return ItemType.TYPE_CONTENT;
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
    public abstract void onBindView(LightHolder holder, D data, @ModelIndex int pos);

    /**
     * 使用 payload 绑定数据
     *
     * @param holder LightHolder
     * @param data   数据
     * @param pos    位置
     * @param msg    消息
     */
    public void onBindViewUsePayload(LightHolder holder, D data, @ModelIndex int pos, String msg) {
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
     * @param updater ModelTypeConfigCallback
     */
    public void addModelUpdater(ModelTypeConfigCallback updater) {
        mModelTypeConfigCallbacks.add(updater);
    }

    /**
     * 根据类型获取 ModelType
     *
     * @param type 类型
     * @return 数据的类型
     */
    public ModelType getModelType(int type) {
        ModelType modelType = mModelTypeCache.get(type);
        if (modelType == null) {
            modelType = new ModelType(type);
            for (ModelTypeConfigCallback updater : mModelTypeConfigCallbacks) {
                updater.call(modelType);
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
    public @Nullable ModelType getModelType(D data) {
        if (data == null) {
            return null;
        }
        int type;
        if (data instanceof Typeable) {
            type = ((Typeable) data).getItemType();
        } else {
            type = ItemType.TYPE_CONTENT;
        }
        return getModelType(type);
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
    public HeaderRef header() {
        return getDelegate(IDelegate.HF);
    }

    /**
     * @return HFDelegate 执行 footer 相关功能
     */
    public FooterRef footer() {
        return getDelegate(IDelegate.HF);
    }

    /**
     * @return NotifyDelegate 执行 数据更新 相关功能
     */
    public NotifyRef notifyItem() {
        return getDelegate(IDelegate.NOTIFY);
    }

    /**
     * @return LoadMoreDelegate 执行底部加载更多功能
     */
    public LoadMoreRef loadMore() {
        return getDelegate(IDelegate.LOAD_MORE);
    }

    /**
     * @return TopMoreDelegate 执行顶部加载更多功能
     */
    public TopMoreRef topMore() {
        return getDelegate(IDelegate.TOP_MORE);
    }

    /**
     * @return SelectorDelegate 负责选择器功能
     */
    public SelectorRef<D> selector() {
        return getDelegate(IDelegate.SELECTOR);
    }

    /**
     * @return LoadingViewDelegate 加载动画功能
     */
    public LoadingViewRef loadingView() {
        return getDelegate(IDelegate.LOADING);
    }

    /**
     * @return EmptyViewDelegate 空白页面功能
     */
    public EmptyViewRef emptyView() {
        return getDelegate(IDelegate.EMPTY);
    }

    /**
     * @return DragSwipeDelegate 拖动和滑动 功能
     */
    public DragSwipeRef dragSwipe() {
        return getDelegate(IDelegate.DRAG_SWIPE);
    }

    /**
     * @return SectionDelegate 隔断效果
     */
    public SectionRef<D> section() {
        return getDelegate(IDelegate.SECTION);
    }


    /**
     * 使用 ItemAdapter 构建 LightAdapter，将每个类型进行隔离，逻辑更清晰
     *
     * @param list     数据源
     * @param adapters ItemAdapter 列表
     * @param <DATA>   数据范型
     * @return LightAdapter
     * @see ItemAdapter
     * @see com.zfy.adapter.items.LightItemAdapter
     */
    @CheckResult
    public static <DATA> LightAdapter<DATA> of(List<DATA> list, ItemAdapter<DATA>... adapters) {
        SparseArray<ItemAdapter<DATA>> array = new SparseArray<>();
        for (ItemAdapter<DATA> itemAdapter : adapters) {
            int type = itemAdapter.getItemType();
            if (LightUtils.isBuildInType(type)) {
                throw new AdapterException(AdapterException.USE_BUILD_IN_TYPE);
            }
            if (array.indexOfKey(type) > 0) {
                throw new AdapterException("ItemAdapter Type 重复");
            }
            array.put(type, itemAdapter);
        }
        ModelTypeConfigCallback callback = modelType -> {
            ItemAdapter itemAdapter = array.get(modelType.type);
            if (itemAdapter != null) {
                itemAdapter.configModelType(modelType);
            }
        };
        return new LightMixAdapter<>(list, array, callback);
    }

}
