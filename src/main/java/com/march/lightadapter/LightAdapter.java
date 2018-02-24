package com.march.lightadapter;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.march.lightadapter.event.SimpleItemListener;
import com.march.lightadapter.helper.LightLogger;
import com.march.lightadapter.event.OnItemListener;
import com.march.lightadapter.listener.AdapterViewBinder;
import com.march.lightadapter.model.ITypeModel;
import com.march.lightadapter.model.TypeConfig;
import com.march.lightadapter.module.AbstractModule;
import com.march.lightadapter.module.HFModule;
import com.march.lightadapter.module.LoadMoreModule;
import com.march.lightadapter.module.TopLoadMoreModule;
import com.march.lightadapter.module.UpdateModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * CreateAt : 2016/19/7
 * Describe : adapter基类，主要负责数据适配的相关逻辑，事件，module
 *
 * @author chendong
 */
public abstract class LightAdapter<D> extends RecyclerView.Adapter<LightHolder> {

    public static final String TAG = LightAdapter.class.getSimpleName();
    public static final int UNSET = -100;
    public static final int TYPE_HEADER = -1;
    public static final int TYPE_FOOTER = -2;
    public static final int TYPE_DEFAULT = 0x123;

    private RecyclerView mRecyclerView;
    // 上下文
    private Context mContext;
    // 数据源
    private List<D> mDatas;
    // 布局加载
    private LayoutInflater mLayoutInflater;
    // 用来存储创建的所有holder，你可以使用holder来直接更新item，而不必调用 notify
    private Set<LightHolder> mHolderSet;
    // 点击监听时间
    private OnItemListener<D> mOnItemListener;
    // 类型和layout资源文件配置
    private SparseArray<TypeConfig> mLayoutResIdArray;
    // 模块列表
    private Map<Class, AbstractModule> mModuleMap;

    public LightAdapter(Context context, List<D> datas) {
        this(context, datas, -1);
    }

    public LightAdapter(Context context, List<D> datas, int layoutRes) {
        this.mContext = context;
        this.mHolderSet = new HashSet<>();
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.mHolderSet = new HashSet<>();
        if (layoutRes > 0) {
            addTypeInternal(TYPE_DEFAULT, layoutRes);
        }
        addModule(new UpdateModule<>());
    }

    public Set<LightHolder> getHolderSet() {
        return mHolderSet;
    }

    public List<D> getDatas() {
        return mDatas;
    }

    public void setDatas(List<D> datas) {
        mDatas = datas;
    }

    public Context getContext() {
        return mContext;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public void bindRecyclerView(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(this);
    }

    private View getInflateView(int viewType, ViewGroup parent) {
        TypeConfig typeConfig = mLayoutResIdArray.get(viewType);
        if (typeConfig != null && typeConfig.getResId() > 0) {
            return mLayoutInflater.inflate(typeConfig.getResId(), parent, false);
        }
        return null;
    }

    int mapPosition(int pos) {
        return isHasHeader() ? pos - 1 : pos;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 模块部分，添加模块支持
    ///////////////////////////////////////////////////////////////////////////


    public void addModule(AbstractModule module) {
        module.onAttachAdapter(this);
        if (mModuleMap == null) {
            mModuleMap = new HashMap<>();
        }
        mModuleMap.put(module.getClass(), module);
        if (module instanceof HFModule) {
            mHFModule = (HFModule) module;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 重载Adapter的方法
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public LightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LightHolder holder = null;
        if (mHFModule != null) {
            holder = mHFModule.onCreateViewHolder(parent, viewType);
        }
        if (holder == null) {
            holder = new LightHolder(mContext, getInflateView(viewType, parent));
            initItemEvent(holder);
        }
        mHolderSet.add(holder);
        return holder;
    }


    @Override
    public void onBindViewHolder(LightHolder holder, int position) {
        if (mHFModule == null || !mHFModule.onBindViewHolder(holder, position)) {
            int pos = mapPosition(position);
            D data = getItem(pos);
            onBindView(holder, data, pos, getItemViewType(position));
            for (AdapterViewBinder<D> binder : mAdapterViewBinders) {
                binder.onBindViewHolder(holder, data, pos, getItemViewType(position));
            }
        }
    }


    public D getItem(int pos) {
        if (pos >= 0 && pos < mDatas.size()) {
            return mDatas.get(pos);
        } else {
            LightLogger.e(TAG, "IndexOutBounds & pos = " + pos);
            return null;
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        for (AbstractModule module : mModuleMap.values()) {
            module.onAttachedToRecyclerView(recyclerView);
        }

        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    if ((mHFModule != null && mHFModule.isFullSpan(type)) || isFullSpanType(type)) {
                        return gridLayoutManager.getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    // 子类决定哪一种类型需要跨越整行
    protected boolean isFullSpanType(int viewType) {
        return false;
    }

    @Override
    public int getItemCount() {
        int count = this.mDatas.size();
        if (mHFModule != null) {
            count += mHFModule.getItemCount4HF();
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (mHFModule != null && ((type = mHFModule.getItemViewType4HF(position)) != 0)) {
            return type;
        }
        // 如果有header,下标减一个
        if (mHFModule != null && mHFModule.isHeaderEnable())
            return getModelItemType(position - 1);
        else
            //没有header 按照原来的
            return getModelItemType(position);
    }


    ///////////////////////////////////////////////////////////////////////////
    // 关于数据类型的相关方法
    ///////////////////////////////////////////////////////////////////////////

    public LightAdapter<D> addType(int type, int resId) {
        if (type == TYPE_HEADER || type == TYPE_FOOTER || type == TYPE_DEFAULT) {
            throw new IllegalArgumentException(TAG + " type can not be (" + TYPE_HEADER + "," + TYPE_FOOTER + "," + TYPE_DEFAULT + ")");
        }
        addTypeInternal(type, resId);
        return this;
    }

    private void addTypeInternal(int type, int resId) {
        if (this.mLayoutResIdArray == null)
            this.mLayoutResIdArray = new SparseArray<>();
        this.mLayoutResIdArray.put(type, new TypeConfig(type, resId));
    }


    private int getModelItemType(int pos) {
        D d = getItem(pos);
        if (d instanceof ITypeModel) {
            ITypeModel model = (ITypeModel) d;
            return model.getModelType();
        } else {
            return TYPE_DEFAULT;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 子类实现的进行数据绑定的方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 绑定数据
     *
     * @param holder ViewHolder数据持有者
     * @param data   数据集
     * @param pos    数据集中的位置
     * @param type   类型
     */
    public abstract void onBindView(LightHolder holder, D data, int pos, int type);

    public void addViewBinder(AdapterViewBinder<D> binder) {
        if (mAdapterViewBinders == null) {
            mAdapterViewBinders = new ArrayList<>();
        }
        mAdapterViewBinders.add(binder);
    }

    @SuppressWarnings("unchecked")
    public <C extends AbstractModule> C getComp(Class<C> clz) {
        return (C) mModuleMap.get(clz);
    }

    ///////////////////////////////////////////////////////////////////////////
    // update
    ///////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("unchecked")
    public UpdateModule<D> update() {
        return getComp(UpdateModule.class);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Header+Footer
    ///////////////////////////////////////////////////////////////////////////

    private HFModule mHFModule;

    public HFModule getHFModule() {
        return mHFModule;
    }

    public boolean isHasHeader() {
        return mHFModule != null && mHFModule.isHeaderEnable();
    }

    public boolean isHasFooter() {
        return mHFModule != null && mHFModule.isHeaderEnable();
    }

    public void setFooterEnable(boolean footerEnable) {
        if (mHFModule != null) {
            mHFModule.setFooterEnable(false);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // LoadMore
    ///////////////////////////////////////////////////////////////////////////

    public void finishLoading() {
        getComp(LoadMoreModule.class).finishLoad();
    }

    public void finishTopLoading() {
        getComp(TopLoadMoreModule.class).finishLoad();
    }

    ///////////////////////////////////////////////////////////////////////////
    // 事件
    ///////////////////////////////////////////////////////////////////////////

    public void setOnItemListener(final OnItemListener<D> onItemListener) {
        this.mOnItemListener = new SimpleItemListener<D>() {

            @Override
            public void onClick(int pos, LightHolder holder, D data) {
                int position = mapPosition(holder.getAdapterPosition());
                D item = getItem(position);
                if (isClickable(item)) {
                    onItemListener.onClick(pos, holder, item);
                }
            }

            @Override
            public void onLongPress(int pos, LightHolder holder, D data) {
                int position = mapPosition(holder.getAdapterPosition());
                D item = getItem(position);
                if (isClickable(item)) {
                    onItemListener.onLongPress(pos, holder, data);
                }
            }

            @Override
            public void onDoubleClick(int pos, LightHolder holder, D data) {
                int position = mapPosition(holder.getAdapterPosition());
                D item = getItem(position);
                if (isClickable(item)) {
                    onItemListener.onDoubleClick(pos, holder, data);
                }
            }

            @Override
            public boolean isSupportDoubleClick() {
                return onItemListener.isSupportDoubleClick();
            }

            @Override
            public boolean isClickable(D data) {
                return onItemListener.isClickable(data);
            }
        };
    }


    private void initItemEvent(final LightHolder holder) {
        View itemView = holder.getItemView();
        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (mOnItemListener != null && mOnItemListener.isSupportDoubleClick()) {
                    mOnItemListener.onClick(0, holder, null);
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (mOnItemListener != null && !mOnItemListener.isSupportDoubleClick()) {
                    mOnItemListener.onClick(0, holder, null);
                }
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (mOnItemListener != null) {
                    mOnItemListener.onDoubleClick(0, holder, null);
                }
                return super.onDoubleTap(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (mOnItemListener != null) {
                    mOnItemListener.onLongPress(0, holder, null);
                }
            }
        };
        final GestureDetectorCompat gestureDetector = new GestureDetectorCompat(mContext, gestureListener);
        itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mOnItemListener != null && mOnItemListener.isSupportDoubleClick()) {
                    gestureDetector.onTouchEvent(motionEvent);
                    return true;
                } else {
                    return false;
                }
            }
        });
        // 不支持双击的话还是用原来的，因为这样可以支持控件点击的背景变化
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemListener != null && !mOnItemListener.isSupportDoubleClick()) {
                    mOnItemListener.onClick(0, holder, null);
                }
            }
        });
        // 不支持双击的话还是用原来的
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mOnItemListener != null && !mOnItemListener.isSupportDoubleClick()) {
                    mOnItemListener.onLongPress(0, holder, null);
                }
                return true;
            }
        });
    }

}
