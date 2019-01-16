package com.zfy.light.sample.cases;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.march.common.exts.ListX;
import com.march.common.exts.ToastX;
import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.LightHolder;
import com.zfy.adapter.LightItemBinder;
import com.zfy.adapter.collections.LightDiffList;
import com.zfy.adapter.collections.LightList;
import com.zfy.adapter.common.SpanSize;
import com.zfy.adapter.model.Extra;
import com.zfy.adapter.model.LightView;
import com.zfy.adapter.ModelTypeRegistry;
import com.zfy.adapter.model.ModelType;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.GlideCallback;
import com.zfy.light.sample.R;
import com.zfy.light.sample.Utils;
import com.zfy.light.sample.Values;
import com.zfy.light.sample.entity.Data;

import java.util.List;

import butterknife.BindView;

/**
 * CreateAt : 2018/11/13
 * Describe : 简单的多类型适配器
 *
 * @author chendong
 */
@MvpV(layout = R.layout.sample_activity)
public class SampleTestActivity extends MvpActivity {

    @BindView(R.id.content_rv) RecyclerView mContentRv;

    private LightAdapter<Data> mAdapter;
    private LightList<Data>    list;




//    class Student implements Diffable<Student> {
//
//        public static final String MSG_NAME_CHANGED = "MSG_NAME_CHANGED";
//
//        int    id;
//        String name;
//
//        @Override
//        public boolean areItemsTheSame(Student newItem) {
//            return this.equals(newItem) && id == newItem.id;
//        }
//
//        @Override
//        public boolean areContentsTheSame(Student newItem) {
//            return name.equals(newItem.name);
//        }
//
//        @Override
//        public Set<String> getChangePayload(Student newItem) {
//            Set<String> set = new HashSet<>();
//            if (!name.equals(newItem.name)) {
//                set.add(MSG_NAME_CHANGED);
//            }
//            return set;
//        }
//    }

//    // 视频类型
//    class VideoItemAdapter extends LightItemBinder<Data> {
//
//        @Override
//        public ModelType newModelType() {
//            return ModelType.singleType(Data.TYPE_VIDEO, R.layout.item_video);
//        }
//        @Override
//        public void onBindView(LightHolder holder, Data data, Extra extra) {
//            // bind video data
//        }
//    }

//    // 音频类型
//    class AudioItemAdapter extends LightItemBinder<Data> {
//        @Override
//        public ModelType newModelType() {
//            return ModelType.singleType(Data.TYPE_AUDIO, R.layout.item_audio);
//        }
//        @Override
//        public void onBindView(LightHolder holder, Data data, Extra extra) {
//            // bind audio data
//        }
//    }


    @Override
    public void init() {
        list = new LightDiffList<>();
//
//        // ModelType 注册表
//        ModelTypeRegistry registry = new ModelTypeRegistry();
//        // 添加一种普通类型
//        registry.add(Data.TYPE_CONTENT, R.layout.item_content);
//        // 添加可被复用视频类型
//        registry.add(new VideoItemAdapter());
//        // 添加可被复用音频类型
//        registry.add(new AudioItemAdapter());
//        // 使用 ModelTypeRegistry 构建 Adapter
//        LightAdapter<Data> adapter = new LightAdapter<>(list, registry);
//
//        // 列表项单击事件
//        adapter.setClickEvent((holder, data, extra) -> {
//        });
//        // 列表项长按事件
//        adapter.setLongPressEvent((holder, data, extra) -> {
//        });
//        // 列表项双击事件
//        adapter.setDbClickEvent((holder, data, extra) -> {
//        });
//        // 子 View 单击事件，需要配合 LightHolder 绑定
//        adapter.setChildViewClickEvent((holder, data, extra) -> {
//        });
//        // 子 View 长按事件，需要配合 LightHolder 绑定
//        adapter.setChildViewLongPressEvent((holder, data, extra) -> {
//        });


        // type callback
        ModelTypeRegistry registry = ModelTypeRegistry.create();
        registry.add(Data.TYPE_DELEGATE, R.layout.item_deleate, SpanSize.SPAN_SIZE_HALF);
        registry.add(Data.TYPE_PROJECT, R.layout.item_cover, SpanSize.SPAN_SIZE_HALF);
        // adapter
        mAdapter = new LightAdapter<>(list, registry);
        mAdapter.setBindCallback((holder, data, extra) -> {
            holder.setText(R.id.title_tv, data.title)
                    .setText(R.id.desc_tv, data.desc);
            switch (data.type) {
                case Data.TYPE_DELEGATE:
                    holder.setText(R.id.subtitle_tv, "子标题");
                    break;
                case Data.TYPE_PROJECT:
                    holder.setCallback(R.id.cover_iv, new GlideCallback(Utils.randomImage()));
                    break;
            }
        });
        mAdapter.setClickEvent((holder, data, extra) -> {
            ToastX.show("click item");
        });
        // header
        mAdapter.header().addHeaderView(LightView.from(R.layout.desc_header), (holder) -> {
            holder.setText(R.id.desc_tv, Values.getAnimatorDesc())
                    .setCallback(R.id.cover_iv, new GlideCallback(Utils.randomImage()))
                    .setClick(R.id.action_fab, v -> {
                    });
        });
        // loadMore
        mAdapter.loadMore().setLoadMoreListener(adapter -> {
            post(() -> {
                appendData();
                mAdapter.loadMore().finishLoadMore();
            }, 2000);
        });
        // animator
        mAdapter.animator().setAnimatorEnable(true);
        mContentRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mContentRv.setAdapter(mAdapter);
        appendData();
    }


    private void appendData() {
        List<Data> list = ListX.range(10, index -> {
            Data entity = new Data(index % 3 == 0 ? Data.TYPE_DELEGATE : Data.TYPE_PROJECT);
            entity.title = "Title " + index;
            entity.desc = "Desc " + index;
            entity.subTitle = "SubTitle " + index;
            return entity;
        });
        this.list.updateAddAll(list);
    }


    private void test(LightHolder holder2) {
//        holder2
//                // 设置 visibility
//                .setVisibility(R.id.tv, View.VISIBLE)
//                // 同时对多个控件设置 visibility
//                .setVisibility(Ids.all(R.id.tv, R.id.tv_count), View.GONE)
//                // 对多个控件设置某种显示状态
//                .setVisible(R.id.tv, R.id.tv_count)
//                .setGone(R.id.tv, R.id.tv_count)
//                .setInVisible(R.id.tv, R.id.tv_count)
//                // 通过 bool 值切换两种显示状态
//                .setVisibleGone(R.id.test_tv, true)
//                .setVisibleInVisible(R.id.test_tv, false)
//                // 设置 select
//                .setSelect(R.id.tv, true)
//                .setSelectYes(R.id.tv_count, R.id.test_tv)
//                .setSelectNo(R.id.tv_count, R.id.test_tv)
//                // 设置 checked
//                .setChecked(R.id.tv, true)
//                .setCheckedNo(R.id.tv_count, R.id.test_tv)
//                .setCheckedYes(R.id.tv_count, R.id.test_tv)
//                // 设置背景
//                .setBgColor(R.id.test_tv, Color.RED)
//                .setBgColorRes(R.id.test_tv, R.color.colorPrimary)
//                .setBgDrawable(R.id.test_tv, new ColorDrawable(Color.RED))
//                .setBgRes(R.id.test_tv, R.drawable.wx_logo)
//                // 设置文字颜色
//                .setTextColor(R.id.test_tv, Color.RED)
//                .setTextColorRes(R.id.test_tv, R.color.colorPrimary)
//                // 设置文字
//                .setText(R.id.test_tv, "test", true)
//                .setTextRes(R.id.test_tv, R.string.app_name)
//                // 设置图片
//                .setImage(R.id.test_tv, R.drawable.wx_logo)
//                .setImage(R.id.test_tv, new ColorDrawable(Color.RED))
//                .setImage(R.id.test_tv, BitmapFactory.decodeFile("test"))
//                // 给 itemView 设置 LayoutParams
//                .setLayoutParams(100, 100)
//                // 给指定控件设置 LayoutParams
//                .setLayoutParams(R.id.test_tv, 100, 100)
//                // 点击事件，会发送到 Adapter#ChildViewClickEvent
//                .setClick(R.id.test_tv)
//                // 点击事件，直接设置 listener
//                .setClick(R.id.test_tv, view -> {
//                    ToastX.show("点击事件");
//                })
//                // 长按事件，会发送到 Adapter#ChildViewLongPressEvent
//                .setLongClick(R.id.test_tv)
//                // 长按事件，直接设置 listener
//                .setLongClick(R.id.test_tv, view -> {
//                    ToastX.show("长按事件");
//                    return true;
//                })
//                // 设置长按触发拖拽事件
//                .dragOnLongPress(R.id.tv)
//                // 设置触摸触发拖拽事件
//                .dragOnTouch(R.id.tv)
//                // 设置长按触发侧滑事件
//                .swipeOnLongPress(R.id.tv)
//                // 设置触摸触发侧滑事件
//                .swipeOnTouch(R.id.tv)
//                // 使用回调风格，LightHolder.IMAGE 用来声明范型类型
//                .setCallback(R.id.tv, LightHolder.IMAGE, imgView -> {
//                    Glide.with(imgView.getContext()).load("url").into(imgView);
//                })
//                // 将 glide 加载封装成单独的 callback，直接使用
//                .setCallback(R.id.tv, new GlideCallback("url"));
//
//
//        LightAdapter<String> mMyAdapter = null;
//        // 获取 Header 功能代理对象
//        HeaderRef header = mMyAdapter.header();
//        // 使用布局文件创建 LightView
//        LightView lightView = LightView.from(R.layout.adapter_item_header);
//        // OR 同样也支持使用 View 对象创建 LightView
//        LightView lightView = LightView.from(new ImageView(context));
//        // 添加一个 Header，并在回调中绑定数据显示
//        header.addHeaderView(lightView, holder -> {
//            holder.setText(R.id.header_tv, headerDesc);
//        });
//        // 更新 Header 数据绑定
//        header.notifyHeaderUpdate();
//        // 显示 / 隐藏 Header
//        header.setHeaderEnable(true);
//        // 清除添加的所有 Header
//        header.removeAllHeaderViews();
//        // 删除指定的某个 Header
//        header.removeHeaderView(lightView);
//        // 获取 Header 布局的父容器
//        ViewGroup headerView = header.getHeaderView();
//        // header 当前的状态
//        boolean headerEnable = header.isHeaderEnable();
//
//        // 获取 Header 功能代理对象
//        FooterRef footer = mMyAdapter.footer();
//        // 使用布局文件创建 LightView
//        LightView lightView = LightView.from(R.layout.adapter_item_footer);
//        // OR 同样也支持使用 View 对象创建 LightView
//        LightView lightView = LightView.from(new ImageView(context));
//        // 添加一个 Header，并在回调中绑定数据显示
//        footer.addFooterView(lightView, holder -> {
//            holder.setText(R.id.footer_tv, footerDesc);
//        });
//        // 更新 Header 数据绑定
//        footer.notifyFooterUpdate();
//        // 显示 / 隐藏 Footer
//        footer.setFooterEnable(true);
//        // 清除添加的所有 Footer
//        footer.removeAllFooterViews();
//        // 删除指定的某个 Footer
//        footer.removeFooterView(lightView);
//        // 获取 Footer 布局的父容器
//        ViewGroup footerView = footer.getFooterView();
//        // header 当前的状态
//        boolean footerEnable = footer.isFooterEnable();
//
//        // 获取 loadMore 代理对象
//        LoadMoreRef loadMore = mMyAdapter.loadMore();
//        // 设置加载更多监听，提前预加载 10 个，默认 3 个
//        loadMore.setLoadMoreListener(10, adapter -> {
//            // 加载数据
//            loadDatas();
//            // 结束加载更多，开启下次检测
//            loadMore.finishLoadMore();
//        });
//        // 设置是否加载加载更多
//        loadMore.setLoadMoreEnable(false);
//
//        // 获取 topMore 代理对象
//        TopMoreRef topMore = mMyAdapter.topMore();
//        topMore.setTopMoreListener(10, adapter -> {
//            // 加载数据
//            loadDatas();
//            // 结束加载，开启下次检测
//            topMore.finishTopMore();
//        });
//        // 设置是否支持顶部加载更多
//        topMore.setTopMoreEnable(false);
//
//        // 获取 selector 代理实现
//        SelectorRef<String> selector = mMyAdapter.selector();
//        // 设置单选模式，数据绑定
//        selector.setSingleSelector((holder, data, extra) -> {
//            holder.setText(R.id.tv, extra.selected ? "选中" : "不选中");
//        });
//        // 设置多选模式，数据绑定
//        selector.setMultiSelector((holder, data, extra) -> {
//            holder.setText(R.id.tv, extra.selected ? "选中" : "不选中");
//        });
//        // 获取单选的结果
//        String result = selector.getResult("default value");
//        // 获取多选的结果
//        List<String> results = selector.getResults();
//        // 该数据是否被选中
//        boolean isSelect = selector.isSelect(data);
//        // 取消选中该元素
//        selector.releaseItem(data);
//        // 选中该元素
//        selector.selectItem(data);
//        // 切换状态，选中改为不选中，不选中改为选中
//        selector.toggleItem(data);
//        // 设置选择监听，返回 false 将阻止选择操作
//        selector.setOnSelectListener((data, toSelect) -> {
//            // 返回 false 将阻止这次选择操作，用于最多选择 10 个这种场景
//            return true;
//        });
//
//        // 获取动画代理实现
//        AnimatorRef animator = mMyAdapter.animator();
//        // 关闭动画效果
//        animator.setAnimatorEnable(false);
//        // 使用 BindAnimator
//        // 创建一个缩放动画
//        BindAnimator scaleAnimator = new ScaleAnimator(0.5f)
//                .interceptor(new OvershootInterpolator())
//                .duration(500);
//        // 针对某个类型设置动画效果
//        modelType.animator = scaleAnimator;
//        // 设置动画效果，所有 Item 都会执行该动画
//        animator.setBindAnimator(scaleAnimator);
//        // 使用 ItemAnimator
//        // 设置 ItemAnimator,
//        animator.setItemAnimator(new ScaleInBottomAnimator());
//
//        // 设置数据绑定回调
//        mMyAdapter.setBindCallback((holder, data, extra) -> {
//            // 判读是否使用 payloads 局部更新数据
//            if (extra.byPayload) {
//                switch (extra.payloadMsg) {
//                    // 获取到仅更新 name 的消息
//                    case NAME_CHANGED:
//                        // 仅重新绑定 name 即可
//                        holder.setText(R.id.tv, data.name);
//                        break;
//                }
//                return;
//            }
//            // 绑定整个条目的数据
//            holder.setText(R.id.tv, data);
//        });
//
//        NotifyRef notifyRef = mMyAdapter.notifyItem();
//        // 同 adapter.notifyDataSetChanged();
//        notifyRef.change(2);
//        // 同 adapter.notifyItemRangeChanged(2, 20);
//        notifyRef.change(2, 20);
//        // 同 adapter.notifyItemRangeChanged(2, 20, null);
//        notifyRef.change(2, 20, null);
//        // 同 adapter.notifyItemInserted(2);
//        notifyRef.insert(2);
//        // 同 adapter.notifyItemRangeInserted(2, 20);
//        notifyRef.insert(2, 20);
//        // 同 adapter.notifyItemRemoved(2);
//        notifyRef.remove(2);
//        // 同 adapter.notifyItemRangeRemoved(2, 20);
//        notifyRef.remove(2, 20);
//        // 同 adapter.notifyItemMoved(10, 20);
//        notifyRef.move(10, 20);
//
//
//        LoadingViewRef loadingView = mMyAdapter.loadingView();
//        // 获取 loadingView 状态
//        loadingView.isLoadingEnable();
//        // 设置 loadingView 状态
//        loadingView.setLoadingEnable(true);
//        // 设置 loadingView 布局和数据绑定
//        LightView view = LightView.from(R.layout.loading_view);
//        loadingView.setLoadingView(view, (holder, data, extra) -> {
//            switch (data.state) {
//                case LoadingState.INIT: // 初始化
//                    break;
//                case LoadingState.FINISH: // 结束加载
//                    break;
//                case LoadingState.LOADING: // 加载中
//                    break;
//                case LoadingState.NO_DATA: // 无数据
//                    break;
//                case CUSTOM_STATE: // 自定义的状态
//                    break;
//            }
//        });
//        // 手动设置状态
//        loadingView.setLoadingState(LoadingState.NO_DATA);
//        loadingView.setLoadingState(CUSTOM_STATE);
//
//        // 获取 empty view 功能代理实现
//        EmptyViewRef emptyViewRef = mMyAdapter.emptyView();
//        // 获取 empty view 当前状态
//        boolean emptyEnable = emptyViewRef.isEmptyEnable();
//        LightView view = LightView.from(R.layout.empty_view);
//        emptyViewRef.setEmptyView(view, (holder, data, extra) -> {
//            switch (data.state) {
//                case EmptyState.NONE: // 隐藏空白页
//                    break;
//                case EmptyState.ERROR: // 错误
//                    break;
//                case EmptyState.SUCCESS: // 成功
//                    break;
//                case EmptyState.NO_DATA: // 无数据
//                    break;
//                case CUSTOM_STATE: // 自定义的状态
//                    break;
//            }
//        });
//        // 隐藏空白页
//        emptyViewRef.setEmptyState(EmptyState.NONE);
//        // 设置为自定义状态
//        emptyViewRef.setEmptyState(CUSTOM_STATE);
//
//        SectionRef<String> section = mMyAdapter.section();
//        // 是否支持悬挂
//        section.setPinEnable(true);
//        // 设置隔断的布局和数据绑定
//        section.setOptions(R.layout.item_section, true, (holder, data, extra) -> {
//            holder.setText(R.id.title_tv, data);
//        });
//
//        DragSwipeRef dragSwipeRef = mMyAdapter.dragSwipe();
//        DragSwipeOptions options = new DragSwipeOptions();
//        // 设置仅支持左右拖动
//        options.dragFlags = ItemTouchHelper.START | ItemTouchHelper.END;
//        // 滑动超过 0.7 触发 swipe 事件
//        options.swipeThreshold = 0.7f;
//        dragSwipeRef.setOptions(options);
//        dragSwipeRef.setDragSwipeCallback((holder, data, extra) -> {
//            switch (data.state) {
//                case DragSwipeState.ACTIVE_DRAG:
//                    // 开始拖动，更改显示
//                    break;
//                case DragSwipeState.RELEASE_DRAG:
//                    // 结束拖动，更改显示
//                    break;
//                case DragSwipeState.ACTIVE_SWIPE:
//                    // 开始侧滑，更改显示
//                    break;
//                case DragSwipeState.RELEASE_SWIPE:
//                    // 结束侧滑，更改显示
//                    break;
//            }
//        });
//
//        mMyAdapter.setBindCallback((holder, data, extra) -> {
//            holder
//                    // 设置触发触发拖拽
//                    .dragOnTouch(R.id.title_tv, R.id.desc_tv)
//                    // 设置长按触发拖拽
//                    .dragOnLongPress(R.id.title_tv, R.id.desc_tv)
//                    // 设置触摸触发侧滑
//                    .swipeOnTouch(R.id.title_tv, R.id.desc_tv)
//                    // 设置长按触发侧滑
//                    .swipeOnLongPress(R.id.title_tv, R.id.desc_tv);
//        });
//
//
//        FakeRef<String> fake = mMyAdapter.fake();
//        // 显示假数据，指定假数据个数，布局和数据绑定
//        fake.showFake(10, R.layout.item_fake, (holder, data, extra) -> {
//
//        });
//        // 隐藏假数据显示
//        fake.hideFake();
//
//        LightList<Data> list = new LightDiffList<>();
//        Data item = null;
//        List<Data> newList = null;
//        // 添加元素
//        list.updateAdd(item);
//        list.updateAdd(0, item);
//        // 添加列表
//        list.updateAddAll(newList);
//        list.updateAddAll(0, newList);
//        // 设置元素
//        list.updateSet(0, data -> {
//            data.title = "new title";
//        });
//        // 清空列表
//        list.updateClear();
//        // 删除元素
//        list.updateRemove(item);
//        list.updateRemove(0);
//        // 删除符合规则的元素
//        list.updateRemove(data -> {
//            return data.id > 0;
//        });
//        list.updateRemove(10, true, data -> {
//            return data.id > 10;
//        });
//        // 遍历列表，找到符合规则的元素，执行 set 操作
//        list.updateForEach(data -> {
//            return data.id > 10;
//        }, data -> {
//            data.title = "new title";
//        });
//        // 遍历列表，执行 set 操作
//        list.updateForEach(data -> {
//            data.title = "new title";
//        });
//        // 获取列表快照
//        List<Data> snapshot = list.snapshot();
//        // 删除第一个元素
//        snapshot.remove(0);
//        // 发布更新
//        list.update(newList);
    }
}
