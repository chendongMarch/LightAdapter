package com.zfy.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.adapter.data.Sectionable;
import com.zfy.adapter.data.Typeable;
import com.zfy.adapter.collections.LightList;
import com.zfy.adapter.common.AdapterException;
import com.zfy.adapter.common.ItemType;
import com.zfy.adapter.common.LightValues;
import com.zfy.adapter.common.SpanSize;
import com.zfy.adapter.contract.IAdapter;
import com.zfy.adapter.contract.IEventContract;
import com.zfy.adapter.contract.ItemBinder;
import com.zfy.adapter.delegate.DelegateRegistry;
import com.zfy.adapter.delegate.IDelegate;
import com.zfy.adapter.delegate.impl.AnimatorDelegate;
import com.zfy.adapter.delegate.refs.AnimatorRef;
import com.zfy.adapter.delegate.refs.DragSwipeRef;
import com.zfy.adapter.delegate.refs.EmptyViewRef;
import com.zfy.adapter.delegate.refs.FakeRef;
import com.zfy.adapter.delegate.refs.FooterRef;
import com.zfy.adapter.delegate.refs.HeaderRef;
import com.zfy.adapter.delegate.refs.LoadMoreRef;
import com.zfy.adapter.delegate.refs.LoadingViewRef;
import com.zfy.adapter.delegate.refs.NotifyRef;
import com.zfy.adapter.delegate.refs.SectionRef;
import com.zfy.adapter.delegate.refs.SelectorRef;
import com.zfy.adapter.delegate.refs.TopMoreRef;
import com.zfy.adapter.callback.BindCallback;
import com.zfy.adapter.callback.EventCallback;
import com.zfy.adapter.callback.ModelTypeConfigCallback;
import com.zfy.adapter.model.Extra;
import com.zfy.adapter.model.ModelType;

import java.util.ArrayList;
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
public class LightAdapter<D> extends RecyclerView.Adapter<LightHolder>
        implements IEventContract<D>, IAdapter<D> {

    // View
    private RecyclerView                  mView;
    // 上下文
    private Context                       mContext;
    // 数据源
    private List<D>                       mDatas;
    // 布局加载
    private LayoutInflater                mLayoutInflater;
    // 类型和 TypeOptions 配置
    private SparseArray<ModelType>        mModelTypeCache;
    // 更新类型配置
    private List<ModelTypeConfigCallback> mModelTypeConfigCallbacks;
    // 代理注册表
    private DelegateRegistry              mDelegateRegistry;
    private BindCallback<D>               mBindCallback;

    /**
     * 事件部分，负责完成事件的初始化和触发
     */
    /*package*/ LightEvent<D> mLightEvent;
    private EventCallback<D> mClickCallback;
    private EventCallback<D> mLongPressCallback;
    private EventCallback<D> mDbClickCallback;
    private EventCallback<D> mChildViewClickCallback;
    private EventCallback<D> mChildViewLongPressCallback;

    /**
     * ItemAdapter 部分，可以将一个 Adapter 拆分为多个可复用的 ItemAdapter
     */
    private SparseArray<ItemBinder<D>> mItemBinderArray;


    /**
     * 简单、单类型适配器构造函数
     *
     * @param datas    数据源
     * @param layoutId 布局
     */
    public LightAdapter(List<D> datas, @LayoutRes int layoutId) {
        init(datas, modelType -> modelType.layoutId = layoutId);
    }

    /**
     * 扩展，单类型适配器构造函数
     *
     * @param datas     数据源
     * @param modelType 扩展类型
     */
    public LightAdapter(List<D> datas, ModelType modelType) {
        init(datas, type -> type.update(modelType));
    }


    /**
     * 多类型适配器构造
     *
     * @param datas             数据源
     * @param modelTypeRegistry ModelTypeRegistry
     */
    public LightAdapter(List<D> datas, ModelTypeRegistry modelTypeRegistry) {
        for (ItemBinder part : modelTypeRegistry.getItemBinders()) {
            addItemBinder(part);
        }
        init(datas, modelType -> {
            SparseArray<ModelType> array = modelTypeRegistry.getTypeSparseArray();
            ModelType type = array.get(modelType.type);
            if (type != null) {
                modelType.update(type);
            }
        });
    }


    // 通用初始化方法
    private void init(List<D> datas, ModelTypeConfigCallback callback) {
        if (datas instanceof LightList) {
            ((LightList) datas).getCallback().register(this);
        }
        mDatas = datas;
        mModelTypeCache = new SparseArray<>();
        if (mItemBinderArray == null) {
            mItemBinderArray = new SparseArray<>();
        }
        // 代理注册表
        mDelegateRegistry = new DelegateRegistry();
        mDelegateRegistry.onAttachAdapter(this);
        // 事件处理
        mLightEvent = new LightEvent<>(this, (eventType, holder, extra, data) -> {
            ItemBinder<D> itemAdapter = mItemBinderArray.get(getItemViewType(extra.layoutIndex));
            if (itemAdapter != null) {
                itemAdapter.onEventDispatch(eventType, holder, extra, data);
                return;
            }
            switch (eventType) {
                case LightEvent.TYPE_ITEM_CLICK:
                    if (mClickCallback != null) {
                        mClickCallback.call(holder, data, extra);
                    }
                    break;
                case LightEvent.TYPE_ITEM_LONG_PRESS:
                    if (mLongPressCallback != null) {
                        mLongPressCallback.call(holder, data, extra);
                    }
                    break;
                case LightEvent.TYPE_ITEM_DB_CLICK:
                    if (mDbClickCallback != null) {
                        mDbClickCallback.call(holder, data, extra);
                    }
                    break;
                case LightEvent.TYPE_CHILD_CLICK:
                    if (mChildViewClickCallback != null) {
                        mChildViewClickCallback.call(holder, data, extra);
                    }
                    break;
                case LightEvent.TYPE_CHILD_LONG_PRESS:
                    if (mChildViewLongPressCallback != null) {
                        mChildViewLongPressCallback.call(holder, data, extra);
                    }
                    break;
                default:
            }
        });
        mModelTypeConfigCallbacks = new ArrayList<>();
        // 内置类型参数构建
        addModelTypeConfigCallback(type -> {
            if (type.type == ItemType.TYPE_FOOTER
                    || type.type == ItemType.TYPE_SECTION
                    || type.type == ItemType.TYPE_HEADER
                    || type.type == ItemType.TYPE_LOADING
                    || type.type == ItemType.TYPE_EMPTY) {
                type.setSpanSize(SpanSize.SPAN_SIZE_ALL);
            }
        });
        // 外部类型参数构建
        if (callback != null) {
            addModelTypeConfigCallback(callback);
        }
        // ItemAdapter 类型参数构建
        addModelTypeConfigCallback(modelType -> {
            if (mItemBinderArray == null) {
                return;
            }
            ItemBinder itemAdapter = mItemBinderArray.get(modelType.type);
            if (itemAdapter != null) {
                modelType.update(itemAdapter.getModelType());
            }
        });
    }

    @Override
    public final LightHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LightHolder holder = mDelegateRegistry.onCreateViewHolder(parent, viewType);
        if (holder == null) {
            View view;
            ModelType type = getModelType(viewType);
            if (type != null) {
                if(type.layoutId <= 0){
                    throw new AdapterException("ModelType No LayoutId viewType = " + viewType);
                }
                view = mLayoutInflater.inflate(type.layoutId, parent, false);
                if (view != null) {
                    holder = new LightHolder(this, viewType, view);
                    mLightEvent.initEvent(holder, type);
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
    public final void onBindViewHolder(@NonNull LightHolder holder, int layoutIndex) {
        if (!mDelegateRegistry.onBindViewHolder(holder, layoutIndex)) {
            Extra extra = obtainExtraByLayoutIndex(layoutIndex);
            extra.byPayload = false;
            D data = getItem(extra.modelIndex);
            ItemBinder<D> itemAdapter = mItemBinderArray.get(getItemViewType(layoutIndex));
            if (itemAdapter != null) {
                itemAdapter.onBindViewUseItemBinder(holder, data, extra);
            } else {
                onBindView(holder, data, extra);
            }
        }
    }

    @Override
    public final void onBindViewHolder(@NonNull LightHolder holder, int layoutIndex, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, layoutIndex, payloads);
        } else {
            Extra extra = obtainExtraByLayoutIndex(layoutIndex);
            D data = getItem(extra.modelIndex);
            for (Object payload : payloads) {
                if (!(payload instanceof Set) || ((Set) payload).isEmpty()) {
                    continue;
                }
                Set msgSet = (Set) payload;
                for (Object o : msgSet) {
                    if (o instanceof String) {
                        extra.payloadMsg = (String) o;
                        extra.byPayload = true;
                        onBindView(holder, data, extra);
                    }
                }
            }
        }
    }

    @Override
    public final void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mView = recyclerView;
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

    @Override
    public void onBindView(LightHolder holder, D data, Extra extra) {
        if (mBindCallback != null) {
            mBindCallback.bind(holder, data, extra);
        }
    }

    @Override
    public void setBindCallback(BindCallback<D> bindCallback) {
        mBindCallback = bindCallback;
    }

    @Override
    public void setClickEvent(EventCallback<D> clickCallback) {
        mClickCallback = clickCallback;
    }

    @Override
    public void setLongPressEvent(EventCallback<D> longPressCallback) {
        mLongPressCallback = longPressCallback;
    }

    @Override
    public void setDbClickEvent(EventCallback<D> dbClickCallback) {
        mDbClickCallback = dbClickCallback;
    }

    @Override
    public void setChildViewClickEvent(EventCallback<D> childViewClickEvent) {
        mChildViewClickCallback = childViewClickEvent;
    }

    @Override
    public void setChildViewLongPressEvent(EventCallback<D> childViewLongPressEvent) {
        mChildViewLongPressCallback = childViewLongPressEvent;
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
    public RecyclerView getView() {
        return mView;
    }

    public LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
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


    public Extra obtainExtraByLayoutIndex(int layoutIndex) {
        Extra extra = Extra.extra();
        extra.layoutIndex = layoutIndex;
        extra.modelIndex = toModelIndex(layoutIndex);
        return extra;
    }

    public Extra obtainExtraByModelIndex(int modelIndex) {
        Extra extra = Extra.extra();
        extra.layoutIndex = toLayoutIndex(modelIndex);
        extra.modelIndex = modelIndex;
        return extra;
    }

    /**
     * 添加类型更新器
     *
     * @param updater ModelTypeConfigCallback
     */
    public void addModelTypeConfigCallback(ModelTypeConfigCallback updater) {
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
     * @return {@link AnimatorDelegate}
     */
    public AnimatorRef animator() {
        return getDelegate(IDelegate.ANIMATOR);
    }

    /**
     * @return {@link com.zfy.adapter.delegate.impl.FakeDelegate}
     */
    public FakeRef<D> fake() {
        return getDelegate(IDelegate.FAKE);
    }

    private void addItemBinder(ItemBinder binder) {
        if (mItemBinderArray == null) {
            mItemBinderArray = new SparseArray<>();
        }
        int type = binder.getItemType();
        if (mItemBinderArray.indexOfKey(type) > 0) {
            throw new AdapterException("ItemAdapter Type 重复");
        }
        mItemBinderArray.put(type, binder);
    }
}
