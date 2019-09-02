package com.zfy.light.sample.cases;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.march.common.exts.ListX;
import com.march.common.exts.ToastX;
import com.march.common.pool.ExecutorsPool;
import com.zfy.adapter.x.Lx;
import com.zfy.adapter.x.LxAdapter;
import com.zfy.adapter.x.LxContext;
import com.zfy.adapter.x.LxItemBind;
import com.zfy.adapter.x.LxModel;
import com.zfy.adapter.x.LxVh;
import com.zfy.adapter.x.TypeOpts;
import com.zfy.adapter.x.component.LxDragSwipeComponent;
import com.zfy.adapter.x.component.LxEndEdgeLoadMoreComponent;
import com.zfy.adapter.x.component.LxFixedComponent;
import com.zfy.adapter.x.component.LxStartEdgeLoadMoreComponent;
import com.zfy.adapter.x.function.LxTransformations;
import com.zfy.adapter.x.list.LxDiffList;
import com.zfy.adapter.x.list.LxList;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.R;
import com.zfy.light.sample.Utils;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

/**
 * CreateAt : 2019-08-31
 * Describe :
 *
 * @author chendong
 */
@MvpV(layout = R.layout.new_sample_activity)
public class NewSampleTestActivity extends MvpActivity {

    public static final int TYPE_STUDENT = Lx.incrementViewType();
    public static final int TYPE_TEACHER = Lx.incrementViewType();
    public static final int TYPE_HEADER  = Lx.incrementViewType();
    public static final int TYPE_FOOTER  = Lx.incrementViewType();
    public static final int TYPE_EMPTY   = Lx.incrementViewType();

    @BindView(R.id.content_rv) RecyclerView mRecyclerView;

    private LxList<LxModel> mLxModels = new LxDiffList<>();

    @Override
    public void init() {
        LxDragSwipeComponent.DragSwipeOptions dragSwipeOptions = new LxDragSwipeComponent.DragSwipeOptions();
        dragSwipeOptions.longPressItemView4Drag = false;
        dragSwipeOptions.touchItemView4Swipe = false;

        LxAdapter.of(mLxModels)
                .contentType(TYPE_STUDENT, TYPE_TEACHER)
                .binder(new StudentItemBind(), new TeacherItemBind(), new HeaderItemBind(), new FooterItemBind(), new EmptyItemBind())
                .component(new LxFixedComponent())
                .component(new LxStartEdgeLoadMoreComponent((component) -> {
                    ToastX.show("顶部加载更多");
                    ExecutorsPool.ui(component::finishLoadMore, 2000);
                }))
                .component(new LxEndEdgeLoadMoreComponent((component) -> { // 加载回调
                    ToastX.show("底部加载更多");
                    ExecutorsPool.ui(component::finishLoadMore, 2000);
                }))
                .component(new LxDragSwipeComponent(dragSwipeOptions, (state, holder, context) -> {
                    switch (state) {
                        case Lx.DRAG_SWIPE_STATE_NONE:
                            break;
                        case Lx.DRAG_SWIPE_STATE_ACTIVE_DRAG:
                            holder.itemView.animate().scaleX(1.13f).scaleY(1.13f).setDuration(300).start();
                            break;
                        case Lx.DRAG_SWIPE_STATE_RELEASE_DRAG:
                            holder.itemView.animate().scaleX(1f).scaleY(1f).setDuration(300).start();
                            break;
                        case Lx.DRAG_SWIPE_STATE_ACTIVE_SWIPE:
                            holder.itemView.setBackgroundColor(Color.GRAY);
                            break;
                        case Lx.DRAG_SWIPE_STATE_RELEASE_SWIPE:
                            holder.itemView.setBackgroundColor(Color.WHITE);
                            break;
                    }
                }))
                .attachTo(mRecyclerView, new GridLayoutManager(getContext(), 3));

        mLxModels.updateAdd(LxTransformations.pack(TYPE_EMPTY, new BlockTestData("", String.valueOf(System.currentTimeMillis()))));
    }


    private void setData() {
        List<Student> students = ListX.range(10, index -> new Student(index + " " + System.currentTimeMillis()));
        List<Teacher> teachers = ListX.range(10, index -> new Teacher(index + " " + System.currentTimeMillis()));
        LinkedList<LxModel> lxModels = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            lxModels.add(LxTransformations.pack(TYPE_STUDENT, students.get(i)));
            lxModels.add(LxTransformations.pack(TYPE_TEACHER, teachers.get(i)));
        }
        LxModel header = LxTransformations.pack(TYPE_HEADER, new BlockTestData(Utils.randomImage(), String.valueOf(System.currentTimeMillis())));
        lxModels.addFirst(header);

        LxModel footer = LxTransformations.pack(TYPE_FOOTER, new BlockTestData(Utils.randomImage(), String.valueOf(System.currentTimeMillis())));
        lxModels.addLast(footer);

        mLxModels.update(lxModels);
    }


    static class Student {

        String name;

        Student(String name) {
            this.name = name;
        }
    }

    static class Teacher {

        String name;

        Teacher(String name) {
            this.name = name;
        }
    }

    static class BlockTestData {
        String url;
        String desc;

        BlockTestData(String url, String desc) {
            this.url = url;
            this.desc = desc;
        }
    }


    static class StudentItemBind extends LxItemBind<Student> {

        StudentItemBind() {
            super(TypeOpts.make(opts -> {
                opts.layoutId = R.layout.item_squire1;
                opts.enableClick = true;
                opts.enableLongPress = true;
                opts.enableDbClick = true;
                opts.viewType = TYPE_STUDENT;
                opts.spanSize = Lx.SPAN_SIZE_ALL;
                // opts.enableSwipe = true;
            }));
        }

        @Override
        public void onBindView(LxVh holder, int position, Student data, @NonNull List<Object> payloads) {
            holder.setText(R.id.title_tv, "学：" + data.name)
                    .setText(R.id.desc_tv, "支持Swipe，pos = " + position + " ,type =" + TYPE_STUDENT)
                    .swipeOnLongPress(adapter, R.id.title_tv);
        }

        @Override
        public void onEvent(LxContext context, Student data, int eventType) {
            ToastX.show("点击学生 position = " + context.position + " data = " + data.name + " eventType = " + eventType);

            LxList<LxModel> list = getAdapter().getData();
            list.updateRemove(0);
        }
    }

    static class TeacherItemBind extends LxItemBind<Teacher> {

        TeacherItemBind() {
            super(TypeOpts.make(opts -> {
                opts.layoutId = R.layout.item_squire2;
                opts.viewType = TYPE_TEACHER;
                opts.spanSize = 1;
                opts.enableFixed = true;
                // opts.enableDrag = true;
            }));
        }

        @Override
        public void onBindView(LxVh holder, int position, Teacher data, @NonNull List<Object> payloads) {
            holder.setText(R.id.title_tv, "师：" + data.name)
                    .setText(R.id.desc_tv, "支持Drag，pos = " + position + " ,type =" + TYPE_TEACHER)
                    .dragOnTouch(adapter, R.id.title_tv);
        }

        @Override
        public void onEvent(LxContext context, Teacher data, int eventType) {
            ToastX.show("点击老师 position = " + context.position + " data = " + data.name + " eventType = " + eventType);
            // 点击更新 header
            LxList<LxModel> list = adapter.getCustomTypeData(TYPE_HEADER);
            if (list != null) {
                list.updateSet(0, m -> {
                    BlockTestData header = m.unpack();
                    header.desc = String.valueOf(System.currentTimeMillis());
                });
            }

        }
    }

    static class HeaderItemBind extends LxItemBind<BlockTestData> {

        HeaderItemBind() {
            super(TypeOpts.make(opts -> {
                opts.layoutId = R.layout.desc_header;
                opts.spanSize = Lx.SPAN_SIZE_ALL;
                opts.viewType = TYPE_HEADER;
                opts.enableClick = true;
                opts.enableDbClick = true;
                opts.enableLongPress = true;
            }));
        }

        @Override
        public void onBindView(LxVh holder, int position, BlockTestData data, @NonNull List<Object> payloads) {
            holder.setText(R.id.desc_tv, data.desc)
                    .setCallback(R.id.cover_iv, (LxVh.Callback<ImageView>) view -> Glide.with(view.getContext()).load(data.url).into(view));
        }

        @Override
        public void onEvent(LxContext context, BlockTestData data, int eventType) {

            if (eventType == Lx.EVENT_LONG_PRESS) {
                // 长按删除 header
                LxList<LxModel> list = adapter.getCustomTypeData(TYPE_HEADER);
                if (list != null) {
                    list.updateRemove(0);
                }
            }
            if (eventType == Lx.EVENT_CLICK) {
                // 点击删除内容第一个
                LxList<LxModel> list = adapter.getContentTypeData();
                list.updateClear();
            }
            if (eventType == Lx.EVENT_DOUBLE_CLICK) {
                // 双击更新第一个数据
                LxList<LxModel> list = adapter.getContentTypeData();
                if (list != null) {
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
    }


    static class FooterItemBind extends LxItemBind<BlockTestData> {

        FooterItemBind() {
            super(TypeOpts.make(opts -> {
                opts.layoutId = R.layout.item_footer;
                opts.spanSize = Lx.SPAN_SIZE_ALL;
                opts.viewType = TYPE_FOOTER;
                opts.enableClick = true;
                opts.enableLongPress = true;
                opts.enableDbClick = true;
            }));
        }

        @Override
        public void onBindView(LxVh holder, int position, BlockTestData data, @NonNull List<Object> payloads) {
            holder.setText(R.id.desc_tv, data.desc);
        }

        @Override
        public void onEvent(LxContext context, BlockTestData data, int eventType) {
            if (eventType == Lx.EVENT_LONG_PRESS) {
                // 长按删除 footer
                LxList<LxModel> list = adapter.getCustomTypeData(TYPE_FOOTER);
                if (list != null) {
                    list.updateRemove(0);
                }
            }
            if (eventType == Lx.EVENT_CLICK) {
                // 点击删除内容第一个
                LxList<LxModel> list = adapter.getContentTypeData();
                if (list != null) {
                    list.updateClear();
                }
            }
            if (eventType == Lx.EVENT_DOUBLE_CLICK) {
                // 双击再加一个 footer
                LxList<LxModel> list = adapter.getCustomTypeData(TYPE_FOOTER);
                if (list != null) {
                    list.updateAdd(LxTransformations.pack(TYPE_FOOTER, new BlockTestData("", String.valueOf(System.currentTimeMillis()))));
                }
            }
        }
    }

    class EmptyItemBind extends LxItemBind<BlockTestData> {

        EmptyItemBind() {
            super(TypeOpts.make(opts -> {
                opts.layoutId = R.layout.empty_view;
                opts.spanSize = Lx.SPAN_SIZE_ALL;
                opts.viewType = TYPE_EMPTY;
                opts.enableClick = true;
                opts.enableLongPress = true;
                opts.enableDbClick = true;
            }));
        }

        @Override
        public void onBindView(LxVh holder, int position, BlockTestData data, @NonNull List<Object> payloads) {
            holder.setClick(R.id.refresh_tv, v -> {
                setData();
            });
        }

        @Override
        public void onEvent(LxContext context, BlockTestData data, int eventType) {

        }
    }
}
