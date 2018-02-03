package com.march.lightadapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.march.lightadapter.helper.LightLogger;
import com.march.lightadapter.listener.OnItemListener;
import com.march.lightadapter.model.ITypeModel;
import com.march.lightadapter.model.TypeConfig;
import com.march.lightadapter.module.HFModule;
import com.march.lightadapter.module.LoadMoreModule;
import com.march.lightadapter.module.SelectorModule;
import com.march.lightadapter.module.TopLoadMoreModule;
import com.march.lightadapter.module.UpdateModule;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * CreateAt : 2016/19/7
 * Describe : adapter基类，主要负责数据适配的相关逻辑，事件，module
 *
 * @author chendong
 */
public abstract class LightAdapter<D>
        extends RecyclerView.Adapter<LightHolder>
        implements ILightAdapter {

    public static final String TAG = LightAdapter.class.getSimpleName();
    private String mDebugTag;

    // 上下文
    private Context mContext;
    // 数据源
    private List<D> mDatas;
    // 布局加载
    private LayoutInflater mLayoutInflater;
    // 用来存储创建的所有holder，你可以使用holder来直接更新item，而不必调用 notify
    private Set<LightHolder<D>> mHolderSet;
    // 点击监听时间
    private OnItemListener<D> mOnItemListener;
    // 类型和layout资源文件配置
    private SparseArray<TypeConfig> mLayoutResIdArray;


    // header+footer
    private HFModule mHFModule;
    // 选择器模块
    private SelectorModule<D> mSelectorModule;
    // 数据更新模块
    private UpdateModule<D> mUpdateModule;


    public LightAdapter(Context context, List<D> datas) {
        this(context, datas, -1);
    }

    public LightAdapter(Context context, List<D> datas, int layoutRes) {
        Annotation[] annotations = this.getClass().getAnnotations();
        Log.e("chendong", Arrays.toString(annotations));
        this.mContext = context;
        this.mHolderSet = new HashSet<>();
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.mHolderSet = new HashSet<>();
        if (layoutRes > 0) {
            _addType(TYPE_DEFAULT, layoutRes);
        }
        mUpdateModule = new UpdateModule<>();
        mUpdateModule.onAttachAdapter(this);
        mDebugTag = hashCode() + "";
    }

    public Set<LightHolder<D>> getHolderSet() {
        return mHolderSet;
    }

    public List<D> getDatas() {
        return mDatas;
    }

    public void setDatas(List<D> datas) {
        mDatas = datas;
    }

    public void setDebugTag(String debugTag) {
        mDebugTag = debugTag;
    }

    public Context getContext() {
        return mContext;
    }

    public void setOnItemListener(OnItemListener<D> onItemListener) {
        this.mOnItemListener = onItemListener;
    }

    public void bindRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null) {
            LightLogger.e(mDebugTag, "recyclerView has no LayoutManager");
            return;
        }
        recyclerView.setAdapter(this);
    }

    public void bindRecyclerView(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        this.bindRecyclerView(recyclerView);
    }


    private View getInflateView(int viewType, ViewGroup parent) {
        TypeConfig typeConfig = mLayoutResIdArray.get(viewType);
        if (typeConfig != null && typeConfig.getResId() > 0) {
            return mLayoutInflater.inflate(typeConfig.getResId(), parent, false);
        } else if (viewType != TYPE_DEFAULT) {
            LightLogger.e(mDebugTag, "viewType = " + viewType + ",no layout res,use defLayoutRes instead,please check implements ITypeModel");
            return getInflateView(TYPE_DEFAULT, parent);
        } else {
            LightLogger.e(mDebugTag, "viewType = " + viewType + ",no layoutRes,defType no layoutRes too !");
            return null;
        }
    }

    int calPositionInDatas(int pos) {
        if (mHFModule != null && mHFModule.isHeaderEnable()) {
            return pos - 1;
        } else {
            return pos;
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // 模块部分，添加和获取模块
    ///////////////////////////////////////////////////////////////////////////

    public UpdateModule<D> getUpdateModule() {
        return mUpdateModule;
    }


    public HFModule getHFModule() {
        return mHFModule;
    }


    public LoadMoreModule getLoadMoreModule() {
        return mLoadMoreModule;
    }

    public TopLoadMoreModule getTopLoadMoreModule() {
        return mTopLoadMoreModule;
    }

    public SelectorModule<D> getSelectorModule() {
        return mSelectorModule;
    }


    public void addHFModule(HFModule hfModule) {
        this.mHFModule = hfModule;
        mHFModule.onAttachAdapter(LightAdapter.this);
    }

    public void addLoadMoreModule(LoadMoreModule loadMoreModule) {
        this.mLoadMoreModule = loadMoreModule;
        mLoadMoreModule.onAttachAdapter(LightAdapter.this);
    }

    public void addTopLoadMoreModule(TopLoadMoreModule topLoadMoreModule) {
        this.mTopLoadMoreModule = topLoadMoreModule;
        mTopLoadMoreModule.onAttachAdapter(LightAdapter.this);
    }

    public void addSelectorModule(SelectorModule<D> selectorModule, int... ignoreType) {
        this.mSelectorModule = selectorModule;
        mSelectorModule.onAttachAdapter(LightAdapter.this);
        mSelectorModule.ignoreType(ignoreType);
    }


    ///////////////////////////////////////////////////////////////////////////
    // 重载Adapter的方法
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public LightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LightHolder holder = null;
        if (mHFModule != null)
            holder = mHFModule.getHFViewHolder(viewType);
        if (holder == null) {
            holder = new LightHolder<>(mContext, getInflateView(viewType, parent), viewType);
            holder.setAttachAdapter(this);
            holder.setOnItemListener(null);
            if (!ignoreItemListener(holder, viewType))
                holder.setOnItemListener(mOnItemListener);
        }
        mHolderSet.add(holder);
        return holder;
    }


    @Override
    public void onBindViewHolder(LightHolder holder, int position) {
        if (mHFModule == null) {
            onBindViewHolderWrap(holder, position);
        } else if (mHFModule.isFooterEnable() && position == getItemCount() - 1) {
            onBindFooter(holder);
        } else if (mHFModule.isHeaderEnable() && position == 0) {
            onBindHeader(holder);
        } else {
            onBindViewHolderWrap(holder, position);
        }
    }

    private void onBindViewHolderWrap(LightHolder<D> holder, int position) {
        int pos = calPositionInDatas(position);
        D data = mDatas.get(pos);
        holder.setData(data);
        onBindView(holder, data, pos, getItemViewType(position));
        if (mSelectorModule != null) {
            mSelectorModule.onBindView(holder, data, pos, getItemViewType(position));
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (mLoadMoreModule != null)
            mLoadMoreModule.onAttachedToRecyclerView(recyclerView);
        if (mTopLoadMoreModule != null)
            mTopLoadMoreModule.onAttachedToRecyclerView(recyclerView);
        if (mSelectorModule != null)
            mSelectorModule.onAttachedToRecyclerView(recyclerView);
        if (mHFModule != null)
            mHFModule.onAttachedToRecyclerView(recyclerView);
        if (mUpdateModule != null)
            mUpdateModule.onAttachedToRecyclerView(recyclerView);

        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (!(layoutManager instanceof GridLayoutManager))
            return;
        // 针对GridLayoutManager处理
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


    // 忽略某种类型的点击事件
    protected boolean ignoreItemListener(LightHolder holder, int viewType) {
        return false;
    }

    // 子类决定哪一种类型需要跨越整行
    protected boolean isFullSpanType(int viewType) {
        return false;
    }

    @Override
    public int getItemCount() {
        int pos = this.mDatas.size();
        if (mHFModule == null)
            return pos;
        if (mHFModule.isHeaderEnable())
            pos++;
        if (mHFModule.isFooterEnable())
            pos++;
        return pos;
    }


    @Override
    public int getItemViewType(int position) {

        if (mHFModule == null)
            return getOriginItemType(position);

        // 如果没有header没有footer直接返回
        if (!mHFModule.isHeaderEnable() && !mHFModule.isFooterEnable())
            return getOriginItemType(position);

        // 有header且位置0
        if (mHFModule.isHeaderEnable() && position == 0)
            return TYPE_HEADER;

        // pos超出
        if (mHFModule.isFooterEnable() && position == getItemCount() - 1)
            return TYPE_FOOTER;

        // 如果有header,下标减一个
        if (mHFModule.isHeaderEnable())
            return getOriginItemType(position - 1);
        else
            //没有header 按照原来的
            return getOriginItemType(position);
    }


    ///////////////////////////////////////////////////////////////////////////
    // 关于数据类型的相关方法
    ///////////////////////////////////////////////////////////////////////////

    public LightAdapter<D> addType(int type, int resId) {
        if (type == TYPE_HEADER || type == TYPE_FOOTER || type == TYPE_DEFAULT) {
            throw new IllegalArgumentException(mDebugTag + " type can not be (" + TYPE_HEADER + "," + TYPE_FOOTER + "," + TYPE_DEFAULT + ")");
        }
        _addType(type, resId);
        return this;
    }

    private void _addType(int type, int resId) {
        if (this.mLayoutResIdArray == null)
            this.mLayoutResIdArray = new SparseArray<>();
        this.mLayoutResIdArray.put(type, new TypeConfig(type, resId));
    }


    private int getOriginItemType(int pos) {
        D d = mDatas.get(pos);
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

    /**
     * 绑定header的数据 和  监听
     *
     * @param header header holder
     */
    public void onBindHeader(LightHolder<D> header) {

    }

    /**
     * 绑定footer的数据和监听
     *
     * @param footer footer holder
     */
    public void onBindFooter(LightHolder<D> footer) {

    }

    ///////////////////////////////////////////////////////////////////////////
    // Header+Footer
    ///////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////
    // LoadMore
    ///////////////////////////////////////////////////////////////////////////

    // 底部加载更多模块
    private LoadMoreModule mLoadMoreModule;
    // 顶部加载更多
    private TopLoadMoreModule mTopLoadMoreModule;

    public void finishBottomLoad() {
        mLoadMoreModule.finishLoad();
    }

    public void finishTopLoad() {
        mTopLoadMoreModule.finishLoad();
    }
}
