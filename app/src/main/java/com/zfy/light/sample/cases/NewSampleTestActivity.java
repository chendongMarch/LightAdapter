package com.zfy.light.sample.cases;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.march.common.exts.ListX;
import com.march.common.exts.SizeX;
import com.march.common.exts.ToastX;
import com.march.common.pool.ExecutorsPool;
import com.zfy.adapter.Lx;
import com.zfy.adapter.LxAdapter;
import com.zfy.adapter.LxGlobal;
import com.zfy.adapter.LxItemBind;
import com.zfy.adapter.LxList;
import com.zfy.adapter.LxVh;
import com.zfy.adapter.animation.BindScaleAnimator;
import com.zfy.adapter.component.LxDragSwipeComponent;
import com.zfy.adapter.component.LxEndEdgeLoadMoreComponent;
import com.zfy.adapter.component.LxFixedComponent;
import com.zfy.adapter.component.LxSelectComponent;
import com.zfy.adapter.component.LxSnapComponent;
import com.zfy.adapter.component.LxStartEdgeLoadMoreComponent;
import com.zfy.adapter.data.Copyable;
import com.zfy.adapter.data.Diffable;
import com.zfy.adapter.data.LxContext;
import com.zfy.adapter.data.LxModel;
import com.zfy.adapter.data.TypeOpts;
import com.zfy.adapter.data.Typeable;
import com.zfy.adapter.decoration.LxSlidingSelectLayout;
import com.zfy.adapter.helper.LxExpandable;
import com.zfy.adapter.helper.LxManager;
import com.zfy.adapter.helper.LxNesting;
import com.zfy.adapter.helper.LxTransformations;
import com.zfy.adapter.listener.EventHandler;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.R;
import com.zfy.light.sample.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * CreateAt : 2019-08-31
 * Describe :
 *
 * @author chendong
 */
@MvpV(layout = R.layout.new_sample_activity)
public class NewSampleTestActivity extends MvpActivity {

    public static final String HIDE_LOADING = "HIDE_LOADING";

    public static final int TYPE_STUDENT              = Lx.contentTypeOf(); // 业务类型
    public static final int TYPE_TEACHER              = Lx.contentTypeOf();
    public static final int TYPE_SELECT               = Lx.contentTypeOf();
    public static final int TYPE_PAGER                = Lx.contentTypeOf();
    public static final int TYPE_VERTICAL_IMG         = Lx.contentTypeOf();
    public static final int TYPE_HORIZONTAL_IMG       = Lx.contentTypeOf();
    public static final int TYPE_HORIZONTAL_CONTAINER = Lx.contentTypeOf();

    @BindView(R.id.debug_tv)      TextView              mDebugTv;
    @BindView(R.id.content_rv)    RecyclerView          mContentRv;
    @BindView(R.id.fix_container) ViewGroup             mFixContainerFl;
    @BindView(R.id.select_layout) LxSlidingSelectLayout mLxSlidingSelectLayout;

    private LxList mLxModels = new LxList();
//    private LxModelList mLxModels = new LxModelList(true);


    public static final String CLEAR_ALL_DATA = "CLEAR_ALL_DATA";
    private void test() {

        LxGlobal.addEventHandler(CLEAR_ALL_DATA, (event, adapter, extra) -> {
            adapter.getData().updateClear();
        });


        LxItemBind.of(Student.class)
                .opts(TypeOpts.make(R.layout.item_section))
                .onViewBind((context, holder, data) -> {

                })
                .onEventBind((context, data, eventType) -> {

                })
                .build();

        LxList models = new LxList();
        models.publishEvent("HIDE_LOADING");
        LxAdapter.of(models)
                // 这里指定了 5 种类型的数据绑定
                .bindItem(new StudentItemBind(), new TeacherItemBind())
                .bindItem(new HeaderItemBind(), new FooterItemBind(), new EmptyItemBind())
                .attachTo(mContentRv, LxManager.grid(getContext(), 3));

        LxDragSwipeComponent.DragSwipeOptions options = new LxDragSwipeComponent.DragSwipeOptions();
        // 关闭触摸自动触发侧滑
        options.touchItemView4Swipe = false;
        // 关闭长按自动触发拖拽
        options.longPressItemView4Drag = false;

        // 定义事件拦截器
        EventHandler handler = (event, adapter, extra) -> {
            LxList lxModels = adapter.getData();
            LxList extTypeData = lxModels.getExtTypeData(Lx.VIEW_TYPE_LOADING);
            extTypeData.updateClear();
        };
        // 全局注入，会对所有 Adapter 生效
        LxGlobal.addEventHandler(HIDE_LOADING, handler);
        // 对 Adapter 注入，仅对当前 Adapter 生效
        LxAdapter.of(models)
                .bindItem(new StudentItemBind())
                .onEvent(HIDE_LOADING, handler)
                .attachTo(mContentRv, LxManager.linear(getContext()));
        // 直接在数据层注入，会对该数据作为数据源的 Adapter 生效
        models.addEventHandler(HIDE_LOADING, handler);


//        List snapshot = models.snapshot();
//        // 添加两个 header
//        snapshot.add(LxTransformations.pack(Lx.VIEW_TYPE_HEADER, new CustomTypeData("header1")));
//        snapshot.add(LxTransformations.pack(Lx.VIEW_TYPE_HEADER, new CustomTypeData("header2")));
//        // 交替添加 10 个学生和老师
//        List<Student> students = ListX.range(10, index -> new Student());
//        List<Teacher> teachers = ListX.range(10, index -> new Teacher());
//        for (int i = 0; i < 10; i++) {
//            snapshot.add(LxTransformations.pack(TYPE_STUDENT, students.get(i)));
//            snapshot.add(LxTransformations.pack(TYPE_TEACHER, teachers.get(i)));
//        }
//        // 添加两个 footer
//        snapshot.add(LxTransformations.pack(Lx.VIEW_TYPE_FOOTER, new CustomTypeData("footer1")));
//        snapshot.add(LxTransformations.pack(Lx.VIEW_TYPE_FOOTER, new CustomTypeData("footer2")));
//        // 发布数据更新
//        models.update(snapshot);


    }


    @Override
    public void init() {
        LxGlobal.setImgUrlLoader((view, url, extra) -> {
            Glide.with(view).load(url).into(view);
        });


        mDebugTv.setText("演示：拖拽排序，侧滑删除");
        initImgTest();
    }

    @OnClick({R.id.test_pager_btn, R.id.test_drag_swipe_btn,
            R.id.test_load_more_btn, R.id.test_expandable_btn,
            R.id.test_select_btn, R.id.test_img_btn})
    public void clickTestView(View view) {
        mLxModels = new LxList();
        mLxSlidingSelectLayout.setEnabled(false);
        switch (view.getId()) {
            case R.id.test_img_btn:
                mDebugTv.setText("演示：图片");
                initImgTest();
                break;
            case R.id.test_drag_swipe_btn:
                mDebugTv.setText("演示：拖拽排序，侧滑删除");
                initDragSwipeTest();
                break;
            case R.id.test_load_more_btn:
                mDebugTv.setText("演示：加载更多");
                initLoadMoreTest();
                break;
            case R.id.test_expandable_btn:
                mDebugTv.setText("演示：分组列表");
                initGroupListTest();
                break;
            case R.id.test_select_btn:
                mDebugTv.setText("演示：选择器，滑动选中");
                initSelectTest();
                break;
            case R.id.test_pager_btn:
                mDebugTv.setText("演示：ViewPager 效果");
                initPagerTest();
                break;
            default:
                break;
        }
    }
    private void initLoadMoreTest() {
        LxItemBind<NoNameData> loadingBind = LxItemBind.of(NoNameData.class)
                .opts(TypeOpts.make(opts -> {
                    opts.viewType = Lx.VIEW_TYPE_LOADING;
                    opts.layoutId = R.layout.loading_view;
                    opts.spanSize = Lx.SPAN_SIZE_ALL;
                }))
                .onViewBind((context, holder, data) -> {
                    holder.setText(R.id.content_tv, data.desc);

                    if (data.status == -1) {
                        holder.setGone(R.id.pb);
                    }
                })
                .build();

        LxAdapter.of(mLxModels)
                .bindItem(new StudentItemBind(), new TeacherItemBind(),
                        new SectionItemBind(), new SelectItemBind(),
                        new HeaderItemBind(), new FooterItemBind(), new EmptyItemBind(),
                        loadingBind,
                        new GroupItemBind(), new ChildItemBind())
                .component(new LxFixedComponent())
                .component(new LxStartEdgeLoadMoreComponent((component) -> {
                    ToastX.show("顶部加载更多");
                    ExecutorsPool.ui(() -> {
                        mLxModels.publishEvent(Lx.EVENT_FINISH_START_EDGE_LOAD_MORE, null);
                    }, 2000);
                }))
                .component(new LxEndEdgeLoadMoreComponent(10, (component) -> { // 加载回调
                    ToastX.show("底部加载更多");
                    mLxModels.updateAdd(LxTransformations.pack(Lx.VIEW_TYPE_LOADING, new NoNameData("加载中～")));
                    ExecutorsPool.ui(() -> {
                        if (mLxModels.size() > 70) {
                            LxList customTypeData = mLxModels.getExtTypeData(Lx.VIEW_TYPE_LOADING);
                            customTypeData.updateSet(0, new LxList.UnpackConsumer<NoNameData>() {
                                @Override
                                protected void onAccept(NoNameData noNameData) {
                                    noNameData.desc = "加载完成～";
                                    noNameData.status = -1;
                                }
                            });
                            mLxModels.publishEvent(Lx.EVENT_LOAD_MORE_ENABLE, false);
                        } else {
                            LxList contentTypeData = mLxModels.getContentTypeData();
                            contentTypeData.updateAddAll(loadData(10));
                            LxList customTypeData = mLxModels.getExtTypeData(Lx.VIEW_TYPE_LOADING);
                            customTypeData.updateClear();
                            mLxModels.publishEvent(Lx.EVENT_FINISH_LOAD_MORE, null);
                        }
                    }, 1000);
                }))
                .layoutManager(new GridLayoutManager(getContext(), 3))
                .attachTo(mContentRv, LxManager.grid(getContext(), 3));
        setData();
    }


    private void initNormalTest() {
        LxDragSwipeComponent.DragSwipeOptions dragSwipeOptions = new LxDragSwipeComponent.DragSwipeOptions();
        dragSwipeOptions.longPressItemView4Drag = true;
        dragSwipeOptions.touchItemView4Swipe = true;

        LxItemBind<NoNameData> loadingBind = LxItemBind.of(NoNameData.class)
                .opts(TypeOpts.make(opts -> {
                    opts.viewType = Lx.VIEW_TYPE_LOADING;
                    opts.layoutId = R.layout.loading_view;
                    opts.spanSize = Lx.SPAN_SIZE_ALL;
                }))
                .onViewBind((context, holder, data) -> {
                    holder.setText(R.id.content_tv, data.desc);

                    if (data.status == -1) {
                        holder.setGone(R.id.pb);
                    }
                })
                .build();

        LxAdapter.of(mLxModels)
                .bindItem(new StudentItemBind(), new TeacherItemBind(),
                        new SectionItemBind(), new SelectItemBind(),
                        new HeaderItemBind(), new FooterItemBind(),
                        new EmptyItemBind(), loadingBind,
                        new GroupItemBind(), new ChildItemBind())
                //                .component(new LxSnapComponent(Lx.SNAP_MODE_PAGER))
                .component(new LxFixedComponent())
                //                .component(new LxBindAnimatorComponent())
                //                .component(new LxItemAnimatorComponent(new ScaleInLeftAnimator()))
                .component(new LxSelectComponent(Lx.SELECT_MULTI, (data, toSelect) -> {
                    data.getExtra().putBoolean("change_now", true);
                    return false;
                }))
//                .component(new LxStartEdgeLoadMoreComponent((component) -> {
//                    ToastX.show("顶部加载更多");
//                    ExecutorsPool.ui(() -> {
//                        mLxModels.publishEvent(Lx.EVENT_FINISH_START_EDGE_LOAD_MORE, null);
//                    }, 2000);
//                }))
//                .component(new LxEndEdgeLoadMoreComponent(10, (component) -> { // 加载回调
//                    ToastX.show("底部加载更多");
//                    mLxModels.updateAdd(LxTransformations.pack(Lx.VIEW_TYPE_LOADING, new NoNameData("加载中～")));
//                    ExecutorsPool.ui(() -> {
//                        if (mLxModels.size() > 130) {
//                            LxList customTypeData = mLxModels.getExtTypeData(Lx.VIEW_TYPE_LOADING);
//                            customTypeData.updateSet(0, new LxList.UnpackConsumer<NoNameData>() {
//                                @Override
//                                protected void onAccept(NoNameData noNameData) {
//                                    noNameData.desc = "加载完成～";
//                                    noNameData.status = -1;
//                                }
//                            });
//                            mLxModels.publishEvent(Lx.EVENT_LOAD_MORE_ENABLE, false);
//                        } else {
//                            LxList contentTypeData = mLxModels.getContentTypeData();
//                            contentTypeData.updateAddAll(loadData(10));
//                            LxList customTypeData = mLxModels.getExtTypeData(Lx.VIEW_TYPE_LOADING);
//                            customTypeData.updateClear();
//                            mLxModels.publishEvent(Lx.EVENT_FINISH_LOAD_MORE, null);
//                        }
//                    }, 2000);
//                }))
                .component(new LxDragSwipeComponent(dragSwipeOptions, (state, holder, context) -> {
                    switch (state) {
                        case Lx.DRAG_SWIPE_STATE_NONE:
                            break;
                        case Lx.DRAG_STATE_ACTIVE:
                            holder.itemView.animate().scaleX(1.13f).scaleY(1.13f).setDuration(300).start();
                            break;
                        case Lx.DRAG_STATE_RELEASE:
                            holder.itemView.animate().scaleX(1f).scaleY(1f).setDuration(300).start();
                            break;
                        case Lx.SWIPE_STATE_ACTIVE:
                            holder.itemView.setBackgroundColor(Color.GRAY);
                            break;
                        case Lx.SWIPE_STATE_RELEASE:
                            holder.itemView.setBackgroundColor(Color.WHITE);
                            break;
                    }
                }))
                .attachTo(mContentRv, LxManager.grid(getContext(), 3));

        setData();
    }


    private void initDragSwipeTest() {
        LxDragSwipeComponent.DragSwipeOptions dragSwipeOptions = new LxDragSwipeComponent.DragSwipeOptions();
        dragSwipeOptions.longPressItemView4Drag = true;
        dragSwipeOptions.touchItemView4Swipe = true;

        LxAdapter.of(mLxModels)
                .bindItem(new StudentItemBind(), new TeacherItemBind(),
                        new SectionItemBind(), new SelectItemBind(),
                        new HeaderItemBind(), new FooterItemBind(),
                        new EmptyItemBind())
                .component(new LxDragSwipeComponent(dragSwipeOptions, (state, holder, context) -> {
                    switch (state) {
                        case Lx.DRAG_SWIPE_STATE_NONE:
                            break;
                        case Lx.DRAG_STATE_ACTIVE:
                            holder.itemView.animate().scaleX(1.13f).scaleY(1.13f).setDuration(300).start();
                            break;
                        case Lx.DRAG_STATE_RELEASE:
                            holder.itemView.animate().scaleX(1f).scaleY(1f).setDuration(300).start();
                            break;
                        case Lx.SWIPE_STATE_ACTIVE:
                            holder.itemView.setBackgroundColor(Color.GRAY);
                            break;
                        case Lx.SWIPE_STATE_RELEASE:
                            holder.itemView.setBackgroundColor(Color.WHITE);
                            break;
                    }
                }))
                .layoutManager(new GridLayoutManager(getContext(), 3))
                .attachTo(mContentRv, LxManager.grid(getContext(), 3));
        setData();
    }

    private void initSelectTest() {
        mLxSlidingSelectLayout.setEnabled(true);

        LxAdapter.of(mLxModels)
                .bindItem(new StudentItemBind(), new TeacherItemBind(),
                        new SectionItemBind(), new SelectItemBind(),
                        new HeaderItemBind(), new FooterItemBind(),
                        new EmptyItemBind(),
                        new GroupItemBind(), new ChildItemBind())
                .component(new LxSelectComponent(Lx.SELECT_MULTI, (data, toSelect) -> {
                    data.getExtra().putBoolean("change_now", true);
                    return false;
                }))
                .attachTo(mContentRv, LxManager.grid(getContext(), 3));

        setAllStudent();
    }


    public void initGroupListTest() {
        LxAdapter.of(mLxModels)
                .bindItem(new HeaderItemBind(), new FooterItemBind(), new EmptyItemBind(),
                        new GroupItemBind(), new ChildItemBind())
                .component(new LxFixedComponent())
                .attachTo(mContentRv, LxManager.grid(getContext(), 3));
        setGroupChildData();
    }


    public void initPagerTest() {
        LxAdapter.of(mLxModels)
                .bindItem(new HeaderItemBind(), new FooterItemBind(), new EmptyItemBind(),
                        new PagerItemBind())
                .component(new LxSnapComponent(Lx.SNAP_MODE_PAGER, new LxSnapComponent.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int lastPosition, int position) {
                        RecyclerView.ViewHolder holder = mContentRv.findViewHolderForAdapterPosition(position);
                        RecyclerView.ViewHolder lastHolder = mContentRv.findViewHolderForAdapterPosition(lastPosition);
                        holder.itemView.animate().scaleX(1.13f).scaleY(1.13f).setDuration(300).start();
                        if (lastHolder != null && !lastHolder.equals(holder)) {
                            lastHolder.itemView.animate().scaleX(1f).scaleY(1f).setDuration(300).start();
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }))
                .attachTo(mContentRv, LxManager.linear(getContext(), true));
        List<NoNameData> sections = ListX.range(10, index -> new NoNameData(index + " "));
        mLxModels.update(LxTransformations.pack(TYPE_PAGER, sections));
    }


    public void initImgTest() {
        // mContentRv.addItemDecoration(new GridSpaceItemDecoration(2, 20, true));
        LxAdapter.of(mLxModels)
                .bindItem(new VerticalImgItemBind(), new HorizontalImgContainerItemBind())
                .attachTo(mContentRv, LxManager.grid(getContext(), 2));

        List<LxModel> snapshot = mLxModels.snapshot();
        for (int i = 0; i < 100; i++) {
            if (i % 3 == 0) {
                NoNameData data = new NoNameData(Utils.randomImage(), i + "");
                data.datas = ListX.range(((int) (Math.random() * 10)) + 5, integer -> new NoNameData(Utils.randomImage(), integer + ": 横向"));
                snapshot.add(LxTransformations.pack(TYPE_HORIZONTAL_CONTAINER, data));
            } else {
                snapshot.add(LxTransformations.pack(TYPE_VERTICAL_IMG, new NoNameData(Utils.randomImage(), i + "：竖向")));
            }
        }
        mLxModels.update(snapshot);

    }


    @OnClick({R.id.add_header_btn, R.id.add_footer_btn, R.id.empty_btn})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.add_header_btn:
                mDebugTv.setText("演示：添加Header");
                LxModel header = LxTransformations.pack(Lx.VIEW_TYPE_HEADER, new NoNameData(Utils.randomImage(), String.valueOf(System.currentTimeMillis())));
                if (mLxModels.hasType(Lx.VIEW_TYPE_HEADER)) {
                    LxList headerData = mLxModels.getExtTypeData(Lx.VIEW_TYPE_HEADER);
                    headerData.updateAdd(0, header);
                } else {
                    mLxModels.updateAdd(0, header);
                }
                mContentRv.smoothScrollToPosition(0);
                break;
            case R.id.add_footer_btn:
                mDebugTv.setText("演示：添加Footer");
                LxModel footer = LxTransformations.pack(Lx.VIEW_TYPE_FOOTER, new NoNameData(Utils.randomImage(), String.valueOf(System.currentTimeMillis())));
                if (mLxModels.hasType(Lx.VIEW_TYPE_FOOTER)) {
                    LxList footerData = mLxModels.getExtTypeData(Lx.VIEW_TYPE_FOOTER);
                    footerData.updateAdd(footer);
                } else {
                    mLxModels.updateAddLast(footer);
                }
                mContentRv.smoothScrollToPosition(mLxModels.size() - 1);
                break;
            case R.id.empty_btn:
                mDebugTv.setText("演示：空载页");
                showEmpty();
                break;
            default:
                break;
        }
    }

    private void showEmpty() {
        mLxModels.updateClear();
        mLxModels.updateAdd(LxTransformations.pack(Lx.VIEW_TYPE_EMPTY, new NoNameData("", String.valueOf(System.currentTimeMillis()))));
    }


    private void setData() {
        int count = 10;

        LinkedList<LxModel> lxModels = loadData(count);

        LxModel header = LxTransformations.pack(Lx.VIEW_TYPE_HEADER, new NoNameData(Utils.randomImage(), String.valueOf(System.currentTimeMillis())));
        lxModels.addFirst(header);

        LxModel footer = LxTransformations.pack(Lx.VIEW_TYPE_FOOTER, new NoNameData(Utils.randomImage(), String.valueOf(System.currentTimeMillis())));
        lxModels.addLast(footer);

        mLxModels.update(lxModels);
    }


    private void setGroupChildData() {
        List<GroupData> groupDataList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            GroupData groupData = new GroupData("group -> " + i);
            groupData.groupId = i;
            groupDataList.add(groupData);
            List<ChildData> childDataList = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                ChildData childData = new ChildData("child -> " + j + " ,group -> " + i);
                childData.childId = j;
                childData.groupId = i;
                childData.groupData = groupData;
                childDataList.add(childData);
            }
            groupData.children = childDataList;
        }

        List<LxModel> lxModels = LxTransformations.pack(Lx.VIEW_TYPE_EXPANDABLE_GROUP, groupDataList);
        mLxModels.update(lxModels);
    }


    private void setAllStudent() {
        int count = 100;
        List<NoNameData> students = ListX.range(count, index -> new NoNameData(index + " " + System.currentTimeMillis()));
        List<LxModel> models = LxTransformations.pack(TYPE_SELECT, students);
        mLxModels.update(models);
    }

    @NonNull
    private LinkedList<LxModel> loadData(int count) {
        List<NoNameData> sections = ListX.range(count, index -> new NoNameData(index + " " + System.currentTimeMillis()));
        List<Student> students = ListX.range(count, index -> new Student(index + " " + System.currentTimeMillis()));
        List<Teacher> teachers = ListX.range(count, index -> new Teacher(index + " " + System.currentTimeMillis()));
        LinkedList<LxModel> lxModels = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            lxModels.add(LxTransformations.pack(Lx.VIEW_TYPE_SECTION, sections.get(i)));
            lxModels.add(LxTransformations.pack(TYPE_STUDENT, students.get(i)));
            lxModels.add(LxTransformations.pack(TYPE_TEACHER, teachers.get(i)));
        }
        return lxModels;
    }


    // 使用一个自增 ID
    public static int ID = 10;

    static class Student implements Diffable<Student>, Copyable<Student> {

        int    id = ID++;
        String name;

        Student(String name) {
            this.name = name;
        }

        @Override
        public Student copyNewOne() {
            Student student = new Student(name);
            student.id = id;
            return student;
        }

        @Override
        public boolean areContentsTheSame(Student newItem) {
            return name.equals(newItem.name);
        }

        @Override
        public Set<String> getChangePayload(Student newItem) {
            Set<String> strings = new HashSet<>();
            if (!name.equals(newItem.name)) {
                strings.add("name_change");
            }
            return strings;
        }
    }

    static class Teacher {

        String name;

        Teacher(String name) {
            this.name = name;
        }
    }


    static class InnerTypeData implements Typeable {

        int type;

        @Override
        public int getItemType() {
            return type;
        }
    }

    static class NoNameData {
        int    status;
        String url;
        String desc;

        int pos;
        int offset;

        List<NoNameData> datas;

        public NoNameData() {
        }

        NoNameData(String desc) {
            this.desc = desc;
        }

        NoNameData(String url, String desc) {
            this.url = url;
            this.desc = desc;
        }
    }

    static class GroupData implements LxExpandable.ExpandableGroup<GroupData, ChildData> {
        public List<ChildData> children;
        public String          title;
        public boolean         expand;
        public int             groupId;

        public GroupData(String title) {
            this.title = title;
        }

        @Override
        public List<ChildData> getChildren() {
            return children;
        }

        @Override
        public boolean isExpand() {
            return expand;
        }

        @Override
        public void setExpand(boolean expand) {
            this.expand = expand;
        }

        @Override
        public int getGroupId() {
            return groupId;
        }

    }

    static class ChildData implements LxExpandable.ExpandableChild<GroupData, ChildData> {

        public String    title;
        public int       childId;
        public int       groupId;
        public GroupData groupData;

        public ChildData(String title) {
            this.title = title;
        }

        @Override
        public int getGroupId() {
            return groupId;
        }


        @Override
        public GroupData getGroupData() {
            return groupData;
        }
    }


    static class PagerItemBind extends LxItemBind<NoNameData> {

        public PagerItemBind() {
            super(TypeOpts.make(opts -> {
                opts.layoutId = R.layout.item_pager;
                opts.viewType = TYPE_PAGER;
            }));
        }

        @Override
        public void onBindView(LxContext context, LxVh holder, NoNameData listItem) {
            holder.setText(R.id.content_tv, listItem.desc);
        }
    }
    static class GroupItemBind extends LxItemBind<GroupData> {

        GroupItemBind() {
            super(TypeOpts.make(opts -> {
                opts.spanSize = Lx.SPAN_SIZE_ALL;
                opts.viewType = Lx.VIEW_TYPE_EXPANDABLE_GROUP;
                opts.layoutId = R.layout.item_section;
                opts.enableFixed = true;
            }));
        }

        @Override
        public void onBindView(LxContext context, LxVh holder, GroupData data) {
            holder.setText(R.id.section_tv, data.title + " " + (data.expand ? "展开" : "关闭"));
        }

        @Override
        public void onEvent(LxContext context, GroupData listItem, int eventType) {
            LxExpandable.toggleExpand(adapter, context, listItem);
        }
    }

    static class ChildItemBind extends LxItemBind<ChildData> {

        ChildItemBind() {
            super(TypeOpts.make(opts -> {
                opts.spanSize = Lx.SPAN_SIZE_ALL;
                opts.viewType = Lx.VIEW_TYPE_EXPANDABLE_CHILD;
                opts.layoutId = R.layout.item_simple;
            }));
        }

        @Override
        public void onBindView(LxContext context, LxVh holder, ChildData data) {
            holder.setText(R.id.sample_tv, data.title + " ，点击删除");
        }

        @Override
        public void onEvent(LxContext context, ChildData data, int eventType) {
            // 点击删除子项
            LxExpandable.removeChild(adapter, context, data);
        }
    }

    static class SectionItemBind extends LxItemBind<NoNameData> {

        public SectionItemBind() {
            super(TypeOpts.make(opts -> {
                opts.layoutId = R.layout.item_section;
                opts.viewType = Lx.VIEW_TYPE_SECTION;
                opts.enableFixed = true;
                opts.spanSize = Lx.SPAN_SIZE_ALL;
            }));
        }

        @Override
        public void onBindView(LxContext context, LxVh holder, NoNameData data) {
            holder.setText(R.id.section_tv, data.desc);
        }

        @Override
        public void onEvent(LxContext context, NoNameData data, int eventType) {
            ToastX.show("click section => " + data.desc);
        }
    }

    static class StudentItemBind extends LxItemBind<Student> {

        StudentItemBind() {
            super(TypeOpts.make(opts -> {
                opts.layoutId = R.layout.item_squire1;
                opts.enableLongPress = false;
                opts.enableDbClick = false;
                opts.enableClick = true;
                opts.viewType = TYPE_STUDENT;
                opts.spanSize = Lx.SPAN_SIZE_ALL;
                opts.enableSwipe = true;
//                opts.enableFixed = true;
            }));
        }

        @Override
        public void onBindView(LxContext context, LxVh holder, Student data) {
            Bundle condition = context.condition;
            if (!condition.isEmpty()) {
                boolean needUpdate = condition.getBoolean("update_name", false);
                String updateNameContent = condition.getString("update_name_content");
                if (needUpdate) {
                    holder.setText(R.id.title_tv, updateNameContent + "," + data.name);
                }
                return;
            }
            if (context.payloads.isEmpty()) {
                holder.setText(R.id.title_tv, "学：" + data.name)
                        .setText(R.id.desc_tv, "支持Swipe，pos = " + context.position + " ,type =" + TYPE_STUDENT + ", 点击触发payloads更新, 悬停在页面顶部");
            } else {
                for (String payload : context.payloads) {
                    if (payload.equals("name_change")) {
                        holder.setText(R.id.title_tv, "payloads：" + data.name);
                    }
                }
            }
        }

        @Override
        public void onEvent(LxContext context, Student listItem, int eventType) {
            ToastX.show("点击学生 position = " + context.position + " data = " + listItem.name + " eventType = " + eventType);
            switch (eventType) {
                case Lx.EVENT_CLICK:
                    // 获取内容类型，这里面只包括了学生和老师的数据
                    // 这样我们就可以愉快的操作业务类型数据了，不用管什么 Header/Footer
//                    LxList contentTypeData = getData().getContentTypeData();
                    // 删除第一个吧
//                    contentTypeData.updateRemove(0);
                    adapter.getData().updateSet(context.position, data -> {
                        Bundle condition = data.getCondition();
                        condition.putBoolean("update_name", true);
                        condition.putString("update_name_content", "条件更新");
                    });
                    break;
                case Lx.EVENT_LONG_PRESS:
                    // 获取 header，会把顶部的两个 header 单独获取出来
                    LxList headerData = getData().getExtTypeData(Lx.VIEW_TYPE_HEADER);
                    // 更新 header
                    headerData.updateSet(0, d -> {
                        NoNameData firstData = d.unpack();
                        firstData.desc = "新设置的";
                    });

                    // 获取 footer，会把底部的两个 footer 单独获取出来
                    LxList footerData = getData().getExtTypeData(Lx.VIEW_TYPE_FOOTER);
                    // 清空 footer
                    footerData.updateClear();
                    break;
                case Lx.EVENT_DOUBLE_CLICK:
                    // 更新当前这一个数据
                    LxList list = getData();
                    list.updateSet(context.position, d -> {
                        Student unpack = d.unpack();
                        unpack.name = String.valueOf(System.currentTimeMillis());
                    });
                    break;
            }
        }
    }

    static class TeacherItemBind extends LxItemBind<Teacher> {

        TeacherItemBind() {
            super(TypeOpts.make(opts -> {
                opts.layoutId = R.layout.item_squire2;
                opts.viewType = TYPE_TEACHER;
                opts.spanSize = 1;
                opts.enableDrag = true;
                opts.enableClick = false;
                opts.bindAnimator = new BindScaleAnimator();
            }));
        }

        @Override
        public void onBindView(LxContext context, LxVh holder, Teacher data) {
            holder.setText(R.id.title_tv, "师：" + data.name)
                    .setText(R.id.desc_tv, "支持Drag，pos = " + context.position + " ,type =" + TYPE_TEACHER)
                    .setTextColor(R.id.title_tv, context.model.isSelected() ? Color.RED : Color.BLACK);
        }

        @Override
        public void onEvent(LxContext context, Teacher data, int eventType) {
            ToastX.show("点击老师 position = " + context.position + " data = " + data.name + " eventType = " + eventType);
            // 点击更新 header
            LxList list = getData().getExtTypeData(Lx.VIEW_TYPE_HEADER);
            list.updateSet(0, m -> {
                NoNameData header = m.unpack();
                header.desc = String.valueOf(System.currentTimeMillis());
            });
            LxSelectComponent component = adapter.getComponent(LxSelectComponent.class);
            if (component != null) {
                component.select(context.model);
            }
        }
    }

    static class HeaderItemBind extends LxItemBind<NoNameData> {

        HeaderItemBind() {
            super(TypeOpts.make(opts -> {
                opts.layoutId = R.layout.desc_header;
                opts.spanSize = Lx.SPAN_SIZE_ALL;
                opts.viewType = Lx.VIEW_TYPE_HEADER;
                opts.enableClick = true;
                opts.enableDbClick = true;
                opts.enableLongPress = true;
            }));
        }

        @Override
        public void onBindView(LxContext context, LxVh holder, NoNameData data) {
            holder.setText(R.id.desc_tv, data.desc)
                    .setImage(R.id.cover_iv, data.url, null);
        }

        @Override
        public void onEvent(LxContext context, NoNameData data, int eventType) {

            if (eventType == Lx.EVENT_LONG_PRESS) {
                // 长按删除 header
                LxList list = getData().getExtTypeData(Lx.VIEW_TYPE_HEADER);
                list.updateRemove(0);
            }
            if (eventType == Lx.EVENT_CLICK) {
                // 点击删除内容第一个
                LxList list = getData().getContentTypeData();
                list.updateClear();
            }
            if (eventType == Lx.EVENT_DOUBLE_CLICK) {
                // 双击更新第一个数据
                LxList list = getData().getContentTypeData();
                list.updateSet(0, m -> {
                    if (m.getItemType() == TYPE_STUDENT) {
                        Student stu = m.unpack();
                        stu.name = "new stu " + System.currentTimeMillis();
                    } else if (m.getItemType() == TYPE_TEACHER) {
                        Teacher teacher = m.unpack();
                        teacher.name = "new teacher " + System.currentTimeMillis();
                    }
                });
            }
        }
    }


    static class FooterItemBind extends LxItemBind<NoNameData> {

        FooterItemBind() {
            super(TypeOpts.make(opts -> {
                opts.layoutId = R.layout.item_footer;
                opts.spanSize = Lx.SPAN_SIZE_ALL;
                opts.viewType = Lx.VIEW_TYPE_FOOTER;
                opts.enableClick = true;
                opts.enableLongPress = true;
                opts.enableDbClick = true;
            }));
        }

        @Override
        public void onBindView(LxContext context, LxVh holder, NoNameData data) {
            holder.setText(R.id.desc_tv, data.desc);
        }

        @Override
        public void onEvent(LxContext context, NoNameData data, int eventType) {
            if (eventType == Lx.EVENT_LONG_PRESS) {
                // 长按删除 footer
                LxList list = getData().getExtTypeData(Lx.VIEW_TYPE_FOOTER);
                list.updateRemove(0);
            }
            if (eventType == Lx.EVENT_CLICK) {
                // 点击删除内容第一个
                LxList list = getData().getContentTypeData();
                list.updateClear();
            }
            if (eventType == Lx.EVENT_DOUBLE_CLICK) {
                // 双击再加一个 footer
                LxList list = getData().getExtTypeData(Lx.VIEW_TYPE_FOOTER);
                list.updateAdd(LxTransformations.pack(Lx.VIEW_TYPE_FOOTER, new NoNameData("", String.valueOf(System.currentTimeMillis()))));
            }
        }
    }

    class EmptyItemBind extends LxItemBind<NoNameData> {

        EmptyItemBind() {
            super(TypeOpts.make(opts -> {
                opts.layoutId = R.layout.empty_view;
                opts.spanSize = Lx.SPAN_SIZE_ALL;
                opts.viewType = Lx.VIEW_TYPE_EMPTY;
                opts.enableClick = true;
                opts.enableLongPress = true;
                opts.enableDbClick = true;
            }));
        }

        @Override
        public void onBindView(LxContext context, LxVh holder, NoNameData data) {
            holder.setClick(R.id.refresh_tv, v -> setData());
        }
    }


    static class VerticalImgItemBind extends LxItemBind<NoNameData> {

        public VerticalImgItemBind() {
            super(TypeOpts.make(opts -> {
                opts.viewType = TYPE_VERTICAL_IMG;
                opts.layoutId = R.layout.item_img;
                opts.spanSize = 1;
            }));
        }

        @Override
        public void onBindView(LxContext context, LxVh holder, NoNameData listItem) {
            holder.setImage(R.id.cover_iv, listItem.url)
                    .setText(R.id.title_tv, listItem.desc)
                    .setLayoutParams(SizeX.WIDTH / 2, SizeX.WIDTH / 2);
        }
    }

    static class HorizontalImgItemBind extends LxItemBind<NoNameData> {

        public HorizontalImgItemBind() {
            super(TypeOpts.make(TYPE_HORIZONTAL_IMG, R.layout.item_img));
        }

        @Override
        public void onBindView(LxContext context, LxVh holder, NoNameData listItem) {
            holder.setImage(R.id.cover_iv, listItem.url);
        }
    }


    static class HorizontalImgContainerItemBind extends LxItemBind<NoNameData> {

        public HorizontalImgContainerItemBind() {
            super(TypeOpts.make(opts -> {
                opts.viewType = TYPE_HORIZONTAL_CONTAINER;
                opts.layoutId = R.layout.item_horizontal_container;
                opts.spanSize = Lx.SPAN_SIZE_ALL;
            }));
        }

        // 初始化没有 adapter 时的 callback，放在这里是避免多次创建造成性能问题
        // 使用 list 创建一个 Adapter 绑定到 view 上
        private LxNesting.OnNoAdapterCallback callback = (view, list) -> {
            LxAdapter.of(list)
                    .bindItem(new HorizontalImgItemBind())
                    .attachTo(view, LxManager.linear(view.getContext(), true));
        };

        @Override
        public void onBindView(LxContext context, LxVh holder, NoNameData listItem) {
            holder.setText(R.id.title_tv, listItem.desc + " , offset = " + listItem.offset + " pos = " + listItem.pos);
            // 获取到控件
            RecyclerView contentRv = holder.getView(R.id.content_rv);
            // 打包横向滑动的数据，这个 type 可自定义
            List<LxModel> packDatas = LxTransformations.pack(TYPE_HORIZONTAL_IMG, listItem.datas);
            // 设置，这里会尝试恢复上次的位置，并计算接下来滑动的位置
            LxNesting.setup(contentRv, context.model, packDatas, callback);
        }
    }


    static class SelectItemBind extends LxItemBind<NoNameData> {

        SelectItemBind() {
            super(TypeOpts.make(opts -> {
                opts.layoutId = R.layout.item_squire1;
                opts.enableClick = true;
                opts.enableLongPress = true;
                opts.enableDbClick = false;
                opts.viewType = TYPE_SELECT;
            }));
        }

        @Override
        public void onBindView(LxContext context, LxVh holder, NoNameData data) {
            LxModel model = context.model;

            holder
                    .setLayoutParams(SizeX.WIDTH / 3, SizeX.WIDTH / 3)
                    // 选中时，更改文字和颜色
                    .setText(R.id.title_tv, model.isSelected() ? "我被选中" : "我没有被选中")
                    .setTextColor(R.id.title_tv, model.isSelected() ? Color.RED : Color.BLACK);

            boolean changeNow = model.getExtra().getBoolean("change_now", false);
            // 选中时，执行缩放动画，提醒用户
            View view = holder.getView(R.id.container_cl);
            if (changeNow) {
                if (model.isSelected()) {
                    if (view.getScaleX() == 1) {
                        view.animate().scaleX(0.8f).scaleY(0.8f).setDuration(300).start();
                    }
                } else {
                    if (view.getScaleX() != 1) {
                        view.animate().scaleX(1f).scaleY(1f).setDuration(300).start();
                    }
                }
                model.getExtra().putBoolean("change_now", false);
            } else {
                if (model.isSelected()) {
                    if (view.getScaleX() == 1) {
                        view.setScaleX(0.8f);
                        view.setScaleY(0.8f);
                    }
                } else {
                    if (view.getScaleX() != 1) {
                        view.setScaleX(1f);
                        view.setScaleY(1f);
                    }
                }
            }
        }

        @Override
        public void onEvent(LxContext context, NoNameData data, int eventType) {
            // 点击选中
            LxSelectComponent component = adapter.getComponent(LxSelectComponent.class);
            if (component != null) {
                component.select(context.model);
            }
        }
    }

}
