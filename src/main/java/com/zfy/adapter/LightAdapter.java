package com.zfy.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.adapter.delegate.DelegateRegistry;
import com.zfy.adapter.delegate.IDelegate;
import com.zfy.adapter.delegate.impl.HFDelegate;
import com.zfy.adapter.listener.OnItemListener;
import com.zfy.adapter.listener.SimpleItemListener;
import com.zfy.adapter.model.ITypeModel;

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

    public static final String TAG = LightAdapter.class.getSimpleName();

    public static final int UNSET        = -100;
    public static final int TYPE_HEADER  = -1;
    public static final int TYPE_FOOTER  = -2;
    public static final int TYPE_DEFAULT = -3;
    public static final int TYPE_EMPTY   = -4;

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


    // 构造一个 ModelType
    public ModelType getType(D data) {
        if (data == null) {
            return null;
        }
        int type;
        if (data instanceof ITypeModel) {
            type = ((ITypeModel) data).getModelType();
        } else {
            type = VALUE.TYPE_CONTENT;
        }
        return getType(type);
    }

    // 单类型构造
    public LightAdapter(Context context, List<D> datas, int layoutId) {
        this(context, datas, modelType -> modelType.setLayout(layoutId));
    }

    // 多类型构造
    public LightAdapter(Context context, List<D> datas, ModelTypeFactory factory) {
        this(context, datas);
        mModelTypeFactory = factory;
    }

    private LightAdapter(Context context, List<D> datas) {
        mContext = context;
        mHolderCache = new HashSet<>();
        mLayoutInflater = LayoutInflater.from(context);
        mDatas = datas;
        mModelTypeCache = new SparseArray<>();
        mHolderCache = new HashSet<>();
        mDelegateRegistry = new DelegateRegistry();
        mDelegateRegistry.onAttachAdapter(this);
        mBuildInModelTypeFactory = type -> {
            if (type.getType() == VALUE.TYPE_FOOTER || type.getType() == VALUE.TYPE_HEADER) {
                type.setSpanSize(VALUE.SPAN_SIZE_ALL);
            }
        };
    }

    public Set<LightHolder> getHolderCache() {
        return mHolderCache;
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

    // 创建 ViewHolder
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
            initItemEvent(holder);
            mHolderCache.add(holder);
        }
        return holder;
    }

    // 绑定数据
    @Override
    public void onBindViewHolder(@NonNull LightHolder holder, int position) {
        if (!mDelegateRegistry.onBindViewHolder(holder, position)) {
            int pos = toModelIndex(position);
            D data = getItem(pos);
            onBindView(holder, data, pos);
        }
    }

    // 绑定到 RecyclerView
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
        if (hfType != VALUE.NONE) {
            return hfType;
        }
        D d = getItem(toModelIndex(position));
        if (d instanceof ITypeModel) {
            ITypeModel model = (ITypeModel) d;
            return model.getModelType();
        } else {
            return VALUE.TYPE_CONTENT;
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull LightHolder holder) {
        super.onViewAttachedToWindow(holder);

    }

    /**
     * 布局中的位置 转换为 数据里面的 位置
     *
     * @param position 布局中的位置
     * @return 数据中的位置
     */
    public int toModelIndex(int position) {
        HFDelegate delegate = mDelegateRegistry.get(IDelegate.HF, HFDelegate.class);
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
        HFDelegate delegate = mDelegateRegistry.get(IDelegate.HF, HFDelegate.class);
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

    public <Delegate extends IDelegate> Delegate getDelegate(int key, Class<Delegate> clazz) {
        return mDelegateRegistry.get(key, clazz);
    }

    /**
     * 绑定数据
     *
     * @param holder holder
     * @param data   数据
     * @param pos    数据中的位置
     */
    public abstract void onBindView(LightHolder holder, D data, int pos);


    public void setOnItemListener(final OnItemListener<D> onItemListener) {
        this.mOnItemListener = new SimpleItemListener<D>() {
            @Override
            public void onClick(int pos, LightHolder holder, D data) {
                int position = toModelIndex(holder.getAdapterPosition());
                ModelType type = getType(getItem(position));
                if (type != null && type.isEnableClick()) {
                    onItemListener.onClick(position, holder, data);
                }
            }

            @Override
            public void onLongPress(int pos, LightHolder holder, D data) {
                int position = toModelIndex(holder.getAdapterPosition());
                ModelType type = getType(getItem(position));
                if (type != null && type.isEnableClick()) {
                    onItemListener.onLongPress(position, holder, data);
                }
            }

            @Override
            public void onDoubleClick(int pos, LightHolder holder, D data) {
                int position = toModelIndex(holder.getAdapterPosition());
                ModelType type = getType(getItem(position));
                if (type != null && type.isEnableClick()) {
                    onItemListener.onDoubleClick(position, holder, data);
                }
            }
        };
    }


    private void initItemEvent(final LightHolder holder) {
        View itemView = holder.getItemView();
//        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
//            @Override
//            public boolean onSingleTapConfirmed(MotionEvent e) {
//                if (mOnItemListener != null && mAdapterConfig.isDbClick()) {
//                    mOnItemListener.onClick(0, holder, null);
//                }
//                return super.onSingleTapConfirmed(e);
//            }
//
//            @Override
//            public boolean onSingleTapUp(MotionEvent e) {
//                if (mOnItemListener != null && !mAdapterConfig.isDbClick()) {
//                    mOnItemListener.onClick(0, holder, null);
//                }
//                return super.onSingleTapUp(e);
//            }
//
//            @Override
//            public boolean onDoubleTap(MotionEvent e) {
//                if (mOnItemListener != null) {
//                    mOnItemListener.onDoubleClick(0, holder, null);
//                }
//                return super.onDoubleTap(e);
//            }
//
//            @Override
//            public void onLongPress(MotionEvent e) {
//                if (mOnItemListener != null) {
//                    mOnItemListener.onLongPress(0, holder, null);
//                }
//            }
//        };
//        final GestureDetectorCompat gestureDetector = new GestureDetectorCompat(mContext, gestureListener);
//        itemView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (mOnItemListener != null && mAdapterConfig.isDbClick()) {
//                    gestureDetector.onTouchEvent(motionEvent);
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });
        // 不支持双击的话还是用原来的，因为这样可以支持控件点击的背景变化
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemListener != null) {
                    mOnItemListener.onClick(0, holder, null);
                }
            }
        });
        // 不支持双击的话还是用原来的
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mOnItemListener != null) {
                    mOnItemListener.onLongPress(0, holder, null);
                }
                return true;
            }
        });
    }


    public int all(int... ids) {

    }
}
