package com.zfy.light.sample.cases;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.march.common.pool.ExecutorsPool;
import com.march.common.x.ListX;
import com.march.common.x.LogX;
import com.march.common.x.SizeX;
import com.march.common.x.ToastX;
import com.zfy.component.basic.app.AppActivity;
import com.zfy.component.basic.app.Layout;
import com.zfy.light.sample.R;
import com.zfy.light.sample.Utils;
import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.LxAdapter;
import com.zfy.lxadapter.LxGlobal;
import com.zfy.lxadapter.LxItemBinder;
import com.zfy.lxadapter.LxList;
import com.zfy.lxadapter.LxViewHolder;
import com.zfy.lxadapter.animation.BindScaleAnimator;
import com.zfy.lxadapter.component.LxDragSwipeComponent;
import com.zfy.lxadapter.component.LxEndEdgeLoadMoreComponent;
import com.zfy.lxadapter.component.LxFixedComponent;
import com.zfy.lxadapter.component.LxSelectComponent;
import com.zfy.lxadapter.component.LxSnapComponent;
import com.zfy.lxadapter.component.LxStartEdgeLoadMoreComponent;
import com.zfy.lxadapter.data.Copyable;
import com.zfy.lxadapter.data.Diffable;
import com.zfy.lxadapter.data.LxContext;
import com.zfy.lxadapter.data.LxModel;
import com.zfy.lxadapter.data.TypeOpts;
import com.zfy.lxadapter.data.Typeable;
import com.zfy.lxadapter.decoration.LxSlidingSelectLayout;
import com.zfy.lxadapter.function._Consumer;
import com.zfy.lxadapter.helper.LxExpandable;
import com.zfy.lxadapter.helper.LxManager;
import com.zfy.lxadapter.helper.LxNesting;
import com.zfy.lxadapter.helper.LxPacker;
import com.zfy.lxadapter.helper.LxPicker;
import com.zfy.lxadapter.helper.LxSource;
import com.zfy.lxadapter.helper.LxTypedHelper;
import com.zfy.lxadapter.list.LxTypedList;
import com.zfy.lxadapter.listener.EventSubscriber;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

//import com.zfy.lxadapter.helper.LxPicker;

/**
 * CreateAt : 2019-08-31
 * Describe :
 *
 * @author chendong
 */
@Layout(R.layout.new_sample_activity)
public class NewSampleTestActivity extends AppActivity {

    public static final String HIDE_LOADING = "HIDE_LOADING";

    public static final int TYPE_STUDENT              = Lx.contentTypeOf(); // 业务类型
    public static final int TYPE_TEACHER              = Lx.contentTypeOf();
    public static final int TYPE_SELECT               = Lx.contentTypeOf();
    public static final int TYPE_PAGER                = Lx.contentTypeOf();
    public static final int TYPE_PICKER               = Lx.contentTypeOf();
    public static final int TYPE_VERTICAL_IMG         = Lx.contentTypeOf();
    public static final int TYPE_HORIZONTAL_IMG       = Lx.contentTypeOf();
    public static final int TYPE_HORIZONTAL_CONTAINER = Lx.contentTypeOf();

    public static final int TYPE_LOADING = Lx.extTypeAfterContentOf();
    public static final int TYPE_EMPTY   = Lx.extTypeAfterContentOf();
    public static final int TYPE_HEADER  = Lx.extTypeBeforeContentOf();
    public static final int TYPE_FOOTER  = Lx.extTypeBeforeContentOf();

    @BindView(R.id.debug_tv)      TextView              mDebugTv;
    @BindView(R.id.content_rv)    RecyclerView          mContentRv;
    @BindView(R.id.fix_container) ViewGroup             mFixContainerFl;
    @BindView(R.id.select_layout) LxSlidingSelectLayout mLxSlidingSelectLayout;

    private LxList mLxModels = new LxTypedList();
//    private LxModelList mLxModels = new LxModelList(true);


    public static final String               CLEAR_ALL_DATA = "CLEAR_ALL_DATA";
    private             LxPicker<NoNameData> mLxPicker;


    public static final int SPAN_SIZE_FIFTH = Lx.SpanSize.QUARTER - 1;

    private void test() {

        LxList list = new LxList();


        list.updateRemoveLast(model -> model.getItemType() == TYPE_STUDENT);
        list.updateRemoveLastX(model -> {
            if (model.getItemType() == TYPE_STUDENT) {
                return Lx.Loop.TRUE_BREAK;
            }
            return Lx.Loop.FALSE_NOT_BREAK;
        });

        list.updateRemove(model -> model.getItemType() == TYPE_STUDENT);
        list.updateRemoveX(model -> {
            if (model.getItemType() == TYPE_STUDENT) {
                return Lx.Loop.TRUE_BREAK;
            }
            return Lx.Loop.FALSE_NOT_BREAK;
        });


        // 初始化
        Student student = new Student("Job");
        List<Student> studentList = new ArrayList<>();
        studentList.add(student);

        // LxSource
        LxSource source = null;
        source = LxSource.just(student);
        source = LxSource.just(TYPE_STUDENT, student);
        source = LxSource.just(studentList);
        source = LxSource.just(TYPE_STUDENT, studentList);
        source = LxSource.empty();
        source = LxSource.snapshot(list);

        source.add(student);
        source.add(TYPE_STUDENT, student);
        source.add(TYPE_STUDENT, student, model -> model.setModuleId(100));
        source.addOnIndex(10, student);
        source.addOnIndex(10, TYPE_STUDENT, student);
        source.addOnIndex(10, TYPE_STUDENT, student, model -> model.setModuleId(100));

        source.addAll(studentList);
        source.addAll(TYPE_STUDENT, studentList);
        source.addAll(TYPE_STUDENT, studentList, model -> model.setModuleId(100));
        source.addAllOnIndex(10, studentList);
        source.addAllOnIndex(10, TYPE_STUDENT, studentList);
        source.addAllOnIndex(10, TYPE_STUDENT, studentList, model -> model.setModuleId(100));


        // 使用 source 更新数据
        list.update(source);

        // 数据更新辅助类
        LxTypedHelper helper = list.getHelper();

        // 增
        list.update(source);
        helper.updateAdd(LxSource.just(student));

        // 删
        helper.updateRemove(Student.class, stu -> stu.id > 10);
        helper.updateRemove4Type(TYPE_STUDENT);
        helper.updateRemoveX(Student.class, stu -> {
            if (stu.id == 10) {
                return Lx.Loop.TRUE_BREAK;
            }
            return Lx.Loop.FALSE_NOT_BREAK;
        });

        // 改
        int index = 10;
        helper.updateSet(Student.class, stu -> stu.id == 10, stu -> stu.name = "NEW_NAME");
        helper.updateSet(Student.class, index, stu -> stu.name = "NEW_NAME");
        helper.updateSet4Type(Student.class, TYPE_STUDENT, stu -> {
            stu.name = "NEW_NAME";
        });
        helper.updateSetX(Student.class, stu -> {
            if (stu.id == 10) {
                // 返回 true，停止循环
                return Lx.Loop.TRUE_BREAK;
            }
            return Lx.Loop.FALSE_NOT_BREAK;
        }, data -> data.name = "NEW_NAME");

        // 查
        List<Student> students1 = helper.find(Student.class, stu -> stu.id > 10);
        List<Student> students2 = helper.find(Student.class, TYPE_STUDENT);
        Student one1 = helper.findOne(Student.class, stu -> stu.id > 10);
        Student one2 = helper.findOne(Student.class, TYPE_STUDENT);

        LxGlobal.setSpanSizeAdapter((spanCount, spanSize) -> {
            if (spanSize == SPAN_SIZE_FIFTH && spanCount % 5 == 0) {
                return spanCount / 5;
            }
            return spanSize;
        });


        LxItemBinder.of(Student.class, TypeOpts.make(R.layout.item_section))
                .onEventBind((binder, context, holder, data) -> {

                })
                .onViewBind((binder, context, data, eventType) -> {

                })
                .build();

        LxList models = new LxList();
        models.postEvent("HIDE_LOADING");
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
        EventSubscriber subscriber = (event, adapter, extra) -> {
            LxList lxModels = adapter.getData();
            LxList extTypeData = lxModels.getExtTypeData(TYPE_LOADING);
            extTypeData.updateClear();
        };
        // 全局注入，会对所有 Adapter 生效
        LxGlobal.subscribe(HIDE_LOADING, subscriber);
        // 对 Adapter 注入，仅对当前 Adapter 生效
        LxAdapter.of(models)
                .bindItem(new StudentItemBind())
                .subscribe(HIDE_LOADING, subscriber)
                .attachTo(mContentRv, LxManager.linear(getContext()));
        // 直接在数据层注入，会对该数据作为数据源的 Adapter 生效
        models.subscribe(HIDE_LOADING, subscriber);


//        List snapshot = models.snapshot();
//        // 添加两个 header
//        snapshot.add(LxPacker.pack(TYPE_HEADER, new CustomTypeData("header1")));
//        snapshot.add(LxPacker.pack(TYPE_HEADER, new CustomTypeData("header2")));
//        // 交替添加 10 个学生和老师
//        List<Student> students = ListX.range(10, index -> new Student());
//        List<Teacher> teachers = ListX.range(10, index -> new Teacher());
//        for (int i = 0; i < 10; i++) {
//            snapshot.add(LxPacker.pack(TYPE_STUDENT, students.get(i)));
//            snapshot.add(LxPacker.pack(TYPE_TEACHER, teachers.get(i)));
//        }
//        // 添加两个 footer
//        snapshot.add(LxPacker.pack(TYPE_FOOTER, new CustomTypeData("footer1")));
//        snapshot.add(LxPacker.pack(TYPE_FOOTER, new CustomTypeData("footer2")));
//        // 发布数据更新
//        models.update(snapshot);


    }


    @Override
    public void init() {
        LxGlobal.setImgUrlLoader((view, url, extra) -> {
            Glide.with(view).load(url).into(view);
        });

//         initPickerTest();
//        initLoadMoreTest();
        initSelectTest();

    }

    @OnClick({R.id.test_pager_btn, R.id.test_drag_swipe_btn,
            R.id.test_load_more_btn, R.id.test_expandable_btn,
            R.id.test_select_btn, R.id.test_img_btn,
            R.id.test_picker_btn, R.id.test_now_btn})
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
            case R.id.test_picker_btn:
                mDebugTv.setText("演示：滚轮效果");
                initPickerTest();
                break;
            case R.id.test_now_btn:
                mLxPicker.select(4, 5, 6);
//                degree--;
//                Log.e("chendong", "degree = " + degree);
//                view.setRotationX(degree);

                break;
            default:
                break;
        }
    }

    int degree = 0;

    private void initLoadMoreTest() {

        TypeOpts typeOpts = TypeOpts.make(opts -> {
            opts.viewType = TYPE_LOADING;
            opts.layoutId = R.layout.loading_view;
            opts.spanSize = Lx.SpanSize.ALL;
        });
        LxItemBinder<NoNameData> loadingBind = LxItemBinder.of(NoNameData.class, typeOpts)
                .onViewBind((binder, context, holder, data) -> {
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
                        mLxModels.postEvent(Lx.Event.FINISH_START_EDGE_LOAD_MORE, null);
                    }, 2000);
                }))
                .component(new LxEndEdgeLoadMoreComponent(10, (component) -> { // 加载回调
                    ToastX.show("底部加载更多");

                    LxSource snapshot = LxSource.snapshot(mLxModels);
                    snapshot.add(TYPE_LOADING, new NoNameData("加载中～"));
                    mLxModels.update(snapshot.asModels());

                    mLxModels.updateAdd(LxPacker.pack(TYPE_LOADING, new NoNameData("加载中～")));

                    ExecutorsPool.ui(() -> {
                        if (mLxModels.size() > 70) {
                            LxList customTypeData = mLxModels.getExtTypeData(TYPE_LOADING);
                            customTypeData.updateSet(0, new _Consumer<LxModel>() {
                                @Override
                                public void accept(LxModel data) {
                                    NoNameData noNameData = data.unpack();
                                    noNameData.desc = "加载完成～";
                                    noNameData.status = -1;
                                }
                            });
                            mLxModels.postEvent(Lx.Event.LOAD_MORE_ENABLE, false);
                        } else {
                            LxList contentTypeData = mLxModels.getContentTypeData();
                            contentTypeData.updateAddAll(loadData(10));
                            LxList customTypeData = mLxModels.getExtTypeData(TYPE_LOADING);
                            customTypeData.updateClear();
                            mLxModels.postEvent(Lx.Event.FINISH_LOAD_MORE, null);
                        }
                    }, 1000);
                }))
                .attachTo(mContentRv, LxManager.grid(getContext(), 3, false));
        setData();

        List<Student> students = mLxModels.filterTo(data -> data.getItemType() == TYPE_PAGER, value -> value.unpack());
    }


    private void initNormalTest() {
        LxDragSwipeComponent.DragSwipeOptions dragSwipeOptions = new LxDragSwipeComponent.DragSwipeOptions();
        dragSwipeOptions.longPressItemView4Drag = true;
        dragSwipeOptions.touchItemView4Swipe = true;

        TypeOpts typeOpts = TypeOpts.make(opts -> {
            opts.viewType = TYPE_LOADING;
            opts.layoutId = R.layout.loading_view;
            opts.spanSize = Lx.SpanSize.ALL;
        });
        LxItemBinder<NoNameData> loadingBind = LxItemBinder.of(NoNameData.class, typeOpts)
                .onViewBind((binder, context, holder, data) -> {
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
                .component(new LxSelectComponent(Lx.SelectMode.MULTI, (data, toSelect) -> {
                    data.getExtra().putBoolean("change_now", true);
                    return false;
                }))
//                .component(new LxStartEdgeLoadMoreComponent((component) -> {
//                    ToastX.show("顶部加载更多");
//                    ExecutorsPool.ui(() -> {
//                        mLxModels.postEvent(Lx.Event.FINISH_START_EDGE_LOAD_MORE, null);
//                    }, 2000);
//                }))
//                .component(new LxEndEdgeLoadMoreComponent(10, (component) -> { // 加载回调
//                    ToastX.show("底部加载更多");
//                    mLxModels.updateAdd(LxPacker.pack(TYPE_LOADING, new NoNameData("加载中～")));
//                    ExecutorsPool.ui(() -> {
//                        if (mLxModels.size() > 130) {
//                            LxList customTypeData = mLxModels.getExtTypeData(TYPE_LOADING);
//                            customTypeData.updateSet(0, new LxList.UnpackConsumer<NoNameData>() {
//                                @Override
//                                protected void onAccept(NoNameData noNameData) {
//                                    noNameData.desc = "加载完成～";
//                                    noNameData.status = -1;
//                                }
//                            });
//                            mLxModels.postEvent(Lx.Event.LOAD_MORE_ENABLE, false);
//                        } else {
//                            LxList contentTypeData = mLxModels.getContentTypeData();
//                            contentTypeData.updateAddAll(loadData(10));
//                            LxList customTypeData = mLxModels.getExtTypeData(TYPE_LOADING);
//                            customTypeData.updateClear();
//                            mLxModels.postEvent(Lx.Event.FINISH_LOAD_MORE, null);
//                        }
//                    }, 2000);
//                }))
                .component(new LxDragSwipeComponent(dragSwipeOptions, (state, holder, context) -> {
                    switch (state) {
                        case Lx.DragState.NONE:
                        case Lx.SwipeState.NONE:
                            break;
                        case Lx.DragState.ACTIVE:
                            holder.itemView.animate().scaleX(1.13f).scaleY(1.13f).setDuration(300).start();
                            break;
                        case Lx.DragState.RELEASE:
                            holder.itemView.animate().scaleX(1f).scaleY(1f).setDuration(300).start();
                            break;
                        case Lx.SwipeState.ACTIVE:
                            holder.itemView.setBackgroundColor(Color.GRAY);
                            break;
                        case Lx.SwipeState.RELEASE:
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
                        case Lx.DragState.NONE:
                        case Lx.SwipeState.NONE:
                            break;
                        case Lx.DragState.ACTIVE:
                            holder.itemView.animate().scaleX(1.13f).scaleY(1.13f).setDuration(300).start();
                            break;
                        case Lx.DragState.RELEASE:
                            holder.itemView.animate().scaleX(1f).scaleY(1f).setDuration(300).start();
                            break;
                        case Lx.SwipeState.ACTIVE:
                            holder.itemView.setBackgroundColor(Color.GRAY);
                            break;
                        case Lx.SwipeState.RELEASE:
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
                .component(new LxSelectComponent(Lx.SelectMode.MULTI, (data, toSelect) -> {
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
        LxAdapter lxAdapter = LxAdapter.of(mLxModels)
                .bindItem(new HeaderItemBind(), new FooterItemBind(), new EmptyItemBind(),
                        new PagerItemBind())
                .component(new LxSnapComponent(Lx.SnapMode.PAGER, new LxSnapComponent.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int lastPosition, int position) {
                        RecyclerView.ViewHolder holder = mContentRv.findViewHolderForAdapterPosition(position);
                        RecyclerView.ViewHolder lastHolder = mContentRv.findViewHolderForAdapterPosition(lastPosition);
                        holder.itemView.animate().scaleX(1.13f).scaleY(1.13f).setDuration(300).start();
                        if (lastHolder != null && !lastHolder.equals(holder)) {
                            lastHolder.itemView.animate().scaleX(1f).scaleY(1f).setDuration(300).start();
                        }

                        LogX.e("chendong", "选中了 -> " + position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }))
                .attachTo(mContentRv, LxManager.linear(getContext(), true));
        List<NoNameData> sections = ListX.range(10, index -> new NoNameData(index + " "));
        mLxModels.update(LxPacker.pack(TYPE_PAGER, sections));

        LxSnapComponent component = lxAdapter.getComponent(LxSnapComponent.class);
        if (component != null) {
            component.selectItem(3, true);
        }
    }


    public void initPickerTest() {
        LinearLayout pickerContainer = findViewById(R.id.picker_container);

        LxPicker.Opts opts = new LxPicker.Opts();
        opts.maxScaleValue = 1.3f;
        opts.listViewWidth = SizeX.WIDTH / 3;
        opts.itemViewHeight = SizeX.dp2px(60);
        opts.exposeViewCount = 5;
        opts.infinite = false;

        mLxPicker = new LxPicker<>(pickerContainer);

        mLxPicker.addPicker(opts, new PickerItemBind(), new LxPicker.PickerDataFetcher<NoNameData>() {
            @Override
            public List<NoNameData> resp(int index, NoNameData pickValue, _Consumer<List<NoNameData>> callback) {
                ExecutorsPool.ui(() -> {
                    callback.accept(ListX.range(100, i -> new NoNameData("" + i)));
                }, 1000);
                return null;
            }
        });

        mLxPicker.addPicker(opts, new PickerItemBind(), new LxPicker.PickerDataFetcher<NoNameData>() {
            @Override
            public List<NoNameData> resp(int index, NoNameData pickValue, _Consumer<List<NoNameData>> callback) {
                ExecutorsPool.ui(() -> {
                    callback.accept(ListX.range(((int) (Math.random() * 20 + 10)), integer -> new NoNameData(pickValue.desc + "," + integer)));
                }, 1000);
                return null;
            }
        });

        mLxPicker.addPicker(opts, new PickerItemBind(), new LxPicker.PickerDataFetcher<NoNameData>() {
            @Override
            public List<NoNameData> resp(int index, NoNameData pickValue, _Consumer<List<NoNameData>> callback) {
                ExecutorsPool.ui(() -> {
                    callback.accept(ListX.range(((int) (Math.random() * 20 + 10)), integer -> new NoNameData(pickValue.desc + "," + integer)));
                }, 1000);
                return null;
            }
        });

        // 第一个自发产生数据
//        mLxPicker.addPicker1(adapter1, new LxPicker.PickerDataFetcher<NoNameData>() {
//            @Override
//            public List<NoNameData> resp(NoNameData pickValue, _Consumer<List<NoNameData>> callback) {
//                return ListX.range(100, index -> new NoNameData("" + index));
//            }
//        });
//        mLxPicker.addPicker1(adapter2, new LxPicker.PickerDataFetcher<NoNameData>() {
//            @Override
//            public List<NoNameData> resp(NoNameData pickValue, _Consumer<List<NoNameData>> callback) {
//                return ListX.range(((int) (Math.random() * 20 + 10)), integer -> new NoNameData(pickValue.desc + "," + integer));
//            }
//        });
//        mLxPicker.addPicker1(adapter3, new LxPicker.PickerDataFetcher<NoNameData>() {
//            @Override
//            public List<NoNameData> resp(NoNameData pickValue, _Consumer<List<NoNameData>> callback) {
//                return ListX.range(((int) (Math.random() * 20 + 10)), integer -> new NoNameData(pickValue.desc + "," + integer));
//            }
//        });

        mLxPicker.active();

//        LxPickerComponent component = adapter1.getComponent(LxPickerComponent.class);
//        if (component != null) {
//            component.selectItem(0, false);
//        }

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
                snapshot.add(LxPacker.pack(TYPE_HORIZONTAL_CONTAINER, data));
            } else {
                snapshot.add(LxPacker.pack(TYPE_VERTICAL_IMG, new NoNameData(Utils.randomImage(), i + "：竖向")));
            }
        }
        mLxModels.update(snapshot);

    }


    @OnClick({R.id.add_header_btn, R.id.add_footer_btn, R.id.empty_btn})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.add_header_btn:
                mDebugTv.setText("演示：添加Header");
                LxModel header = LxPacker.pack(TYPE_HEADER, new NoNameData(Utils.randomImage(), String.valueOf(System.currentTimeMillis())));
                LxList headerData = mLxModels.getExtTypeData(TYPE_HEADER);
                headerData.updateAdd(0, header);
                mContentRv.smoothScrollToPosition(0);
                break;
            case R.id.add_footer_btn:
                mDebugTv.setText("演示：添加Footer");
                LxModel footer = LxPacker.pack(TYPE_FOOTER, new NoNameData(Utils.randomImage(), String.valueOf(System.currentTimeMillis())));
                LxList footerData = mLxModels.getExtTypeData(TYPE_FOOTER);
                footerData.updateAdd(footer);
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
        mLxModels.updateAdd(LxPacker.pack(TYPE_EMPTY, new NoNameData("", String.valueOf(System.currentTimeMillis()))));
    }


    private void setData() {
        int count = 10;

        LinkedList<LxModel> lxModels = loadData(count);

        LxModel header = LxPacker.pack(TYPE_HEADER, new NoNameData(Utils.randomImage(), String.valueOf(System.currentTimeMillis())));
        lxModels.addFirst(header);

        LxModel footer = LxPacker.pack(TYPE_FOOTER, new NoNameData(Utils.randomImage(), String.valueOf(System.currentTimeMillis())));
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

        List<LxModel> lxModels = LxPacker.pack(Lx.ViewType.EXPANDABLE_GROUP, groupDataList);
        mLxModels.update(lxModels);
    }


    private void setAllStudent() {
        int count = 100;
        List<NoNameData> students = ListX.range(count, index -> new NoNameData(index + " " + System.currentTimeMillis()));
        List<LxModel> models = LxPacker.pack(TYPE_SELECT, students);
        mLxModels.update(models);
    }

    @NonNull
    private LinkedList<LxModel> loadData(int count) {
        List<NoNameData> sections = ListX.range(count, index -> new NoNameData(index + " " + System.currentTimeMillis()));
        List<Student> students = ListX.range(count, index -> new Student(index + " " + System.currentTimeMillis()));
        List<Teacher> teachers = ListX.range(count, index -> new Teacher(index + " " + System.currentTimeMillis()));
        LinkedList<LxModel> lxModels = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            lxModels.add(LxPacker.pack(Lx.ViewType.SECTION, sections.get(i)));
            lxModels.add(LxPacker.pack(TYPE_STUDENT, students.get(i)));
            lxModels.add(LxPacker.pack(TYPE_TEACHER, teachers.get(i)));
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


    static class PagerItemBind extends LxItemBinder<NoNameData> {

        @Override
        protected TypeOpts newTypeOpts() {
            return TypeOpts.make(opts -> {
                opts.layoutId = R.layout.item_pager;
                opts.viewType = TYPE_PAGER;
            });
        }

        @Override
        public void onBindView(LxContext context, LxViewHolder holder, NoNameData listItem) {
            holder.setText(R.id.content_tv, listItem.desc);
        }

    }


    static class PickerItemBind extends LxItemBinder<NoNameData> {

        @Override
        protected TypeOpts newTypeOpts() {
            return TypeOpts.make(opts -> {
                opts.layoutId = R.layout.item_whell;
                opts.viewType = TYPE_PICKER;
            });
        }

        @Override
        public void onBindView(LxContext context, LxViewHolder holder, NoNameData listItem) {
            holder.setLayoutParams(SizeX.WIDTH / 3, SizeX.dp2px(60));
            if (listItem == null) {
                holder.setText(R.id.content_tv, "");
                return;
            }
            holder.setText(R.id.content_tv, listItem.desc);
        }
    }

    static class GroupItemBind extends LxItemBinder<GroupData> {

        @Override
        protected TypeOpts newTypeOpts() {
            return TypeOpts.make(opts -> {
                opts.spanSize = Lx.SpanSize.ALL;
                opts.viewType = Lx.ViewType.EXPANDABLE_GROUP;
                opts.layoutId = R.layout.item_section;
                opts.enableFixed = true;
            });
        }

        @Override
        public void onBindView(LxContext context, LxViewHolder holder, GroupData data) {
            holder.setText(R.id.section_tv, data.title + " " + (data.expand ? "展开" : "关闭"));
        }

        @Override
        public void onBindEvent(LxContext context, GroupData listItem, int eventType) {
            LxExpandable.toggleExpand(adapter, context, listItem);
        }
    }

    static class ChildItemBind extends LxItemBinder<ChildData> {


        @Override
        protected TypeOpts newTypeOpts() {
            return TypeOpts.make(opts -> {
                opts.spanSize = Lx.SpanSize.ALL;
                opts.viewType = Lx.ViewType.EXPANDABLE_CHILD;
                opts.layoutId = R.layout.item_simple;
            });
        }

        @Override
        public void onBindView(LxContext context, LxViewHolder holder, ChildData data) {
            holder.setText(R.id.sample_tv, data.title + " ，点击删除");
        }

        @Override
        public void onBindEvent(LxContext context, ChildData data, int eventType) {
            // 点击删除子项
            LxExpandable.removeChild(adapter, context, data);
        }
    }

    static class SectionItemBind extends LxItemBinder<NoNameData> {

        @Override
        protected TypeOpts newTypeOpts() {
            return TypeOpts.make(opts -> {
                opts.layoutId = R.layout.item_section;
                opts.viewType = Lx.ViewType.SECTION;
                opts.enableFixed = true;
                opts.spanSize = Lx.SpanSize.ALL;
            });
        }

        @Override
        public void onBindView(LxContext context, LxViewHolder holder, NoNameData data) {
            holder.setText(R.id.section_tv, data.desc);
        }

        @Override
        public void onBindEvent(LxContext context, NoNameData data, int eventType) {
            ToastX.show("click section => " + data.desc);
        }
    }

    public static final String KEY_NEW_CONTENT       = "KEY_NEW_CONTENT";
    public static final String CONDITION_UPDATE_NAME = "CONDITION_UPDATE_NAME";

    static class StudentItemBind extends LxItemBinder<Student> {

        @Override
        protected TypeOpts newTypeOpts() {
            return TypeOpts.make(opts -> {
                opts.layoutId = R.layout.item_squire1;
                opts.enableLongPress = false;
                opts.enableDbClick = false;
                opts.enableClick = true;
                opts.viewType = TYPE_STUDENT;
                opts.spanSize = Lx.SpanSize.ALL;
                opts.enableSwipe = true;
//              opts.enableFixed = true;
                opts.enableFocusChange = true;
            });
        }

        @Override
        public void onBindView(LxContext context, LxViewHolder holder, Student data) {
            if (context.bindMode == Lx.BindMode.CONDITION) {
                // 条件更新
                Bundle conditionValue = context.conditionValue;
                if (CONDITION_UPDATE_NAME.equals(context.conditionKey)) {
                    String value = conditionValue.getString(KEY_NEW_CONTENT, "no content");
                    holder.setText(R.id.title_tv, value + "," + data.name);
                }
            } else if (context.bindMode == Lx.BindMode.PAYLOADS) {
                // payloads 更新
                for (String payload : context.payloads) {
                    if ("name_change".equals(payload)) {
                        holder.setText(R.id.title_tv, data.name);
                    }
                }
            } else {
                // 正常更新
                holder.setText(R.id.title_tv, "学：" + data.name)
                        .setText(R.id.desc_tv, "支持Swipe，pos = " + context.layoutPosition + " ,type =" + TYPE_STUDENT + ", 点击触发payloads更新, 悬停在页面顶部");
            }
        }

        @Override
        public void onBindEvent(LxContext context, Student listItem, int eventType) {
            ToastX.show("点击学生 position = " + context.layoutPosition + " data = " + listItem.name + " eventType = " + eventType);
            switch (eventType) {
                case Lx.ViewEvent.CLICK:
                    // 获取内容类型，这里面只包括了学生和老师的数据
                    // 这样我们就可以愉快的操作业务类型数据了，不用管什么 Header/Footer
//                    LxList contentTypeData = getData().getContentTypeData();
                    // 删除第一个吧
//                    contentTypeData.updateRemove(0);
                    LxList models = adapter.getData();
                    models.updateSet(context.layoutPosition, data -> {
                        Bundle bundle = new Bundle();
                        bundle.putString(KEY_NEW_CONTENT, "I AM NEW CONTENT");
                        data.setCondition(CONDITION_UPDATE_NAME);
                    });
                    break;
                case Lx.ViewEvent.LONG_PRESS:
                    // 获取 header，会把顶部的两个 header 单独获取出来
                    LxList headerData = getData().getExtTypeData(TYPE_HEADER);
                    // 更新 header
                    headerData.updateSet(0, d -> {
                        NoNameData firstData = d.unpack();
                        firstData.desc = "新设置的";
                    });

                    // 获取 footer，会把底部的两个 footer 单独获取出来
                    LxList footerData = getData().getExtTypeData(TYPE_FOOTER);
                    // 清空 footer
                    footerData.updateClear();
                    break;
                case Lx.ViewEvent.DOUBLE_CLICK:
                    // 更新当前这一个数据
                    LxList list = getData();
                    list.updateSet(context.layoutPosition, d -> {
                        Student unpack = d.unpack();
                        unpack.name = String.valueOf(System.currentTimeMillis());
                    });
                    break;
            }
        }
    }

    static class TeacherItemBind extends LxItemBinder<Teacher> {
        @Override
        protected TypeOpts newTypeOpts() {
            return TypeOpts.make(opts -> {
                opts.layoutId = R.layout.item_squire2;
                opts.viewType = TYPE_TEACHER;
                opts.spanSize = 1;
                opts.enableDrag = true;
                opts.enableClick = false;
                opts.bindAnimator = new BindScaleAnimator();
            });
        }

        @Override
        public void onBindView(LxContext context, LxViewHolder holder, Teacher data) {
            holder.setText(R.id.title_tv, "师：" + data.name)
                    .setText(R.id.desc_tv, "支持Drag，pos = " + context.layoutPosition + " ,type =" + TYPE_TEACHER)
                    .setTextColor(R.id.title_tv, context.model.isSelected() ? Color.RED : Color.BLACK);
        }

        @Override
        public void onBindEvent(LxContext context, Teacher data, int eventType) {
            ToastX.show("点击老师 position = " + context.layoutPosition + " data = " + data.name + " eventType = " + eventType);
            // 点击更新 header
            LxList list = getData().getExtTypeData(TYPE_HEADER);
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

    static class HeaderItemBind extends LxItemBinder<NoNameData> {

        @Override
        protected TypeOpts newTypeOpts() {
            return TypeOpts.make(opts -> {
                opts.layoutId = R.layout.desc_header;
                opts.spanSize = Lx.SpanSize.ALL;
                opts.viewType = TYPE_HEADER;
                opts.enableClick = true;
                opts.enableDbClick = true;
                opts.enableLongPress = true;
            });
        }

        @Override
        public void onBindView(LxContext context, LxViewHolder holder, NoNameData data) {
            holder.setText(R.id.desc_tv, data.desc)
                    .setImage(R.id.cover_iv, data.url, null);
        }

        @Override
        public void onBindEvent(LxContext context, NoNameData data, int eventType) {

            if (eventType == Lx.ViewEvent.LONG_PRESS) {
                // 长按删除 header
                LxList list = getData().getExtTypeData(TYPE_HEADER);
                list.updateRemove(0);
            }
            if (eventType == Lx.ViewEvent.CLICK) {
                // 点击删除内容第一个
                LxList list = getData().getContentTypeData();
                list.updateClear();
            }
            if (eventType == Lx.ViewEvent.DOUBLE_CLICK) {
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


    static class FooterItemBind extends LxItemBinder<NoNameData> {

        @Override
        protected TypeOpts newTypeOpts() {
            return TypeOpts.make(opts -> {
                opts.layoutId = R.layout.item_footer;
                opts.spanSize = Lx.SpanSize.ALL;
                opts.viewType = TYPE_FOOTER;
                opts.enableClick = true;
                opts.enableLongPress = true;
                opts.enableDbClick = true;
            });
        }

        @Override
        public void onBindView(LxContext context, LxViewHolder holder, NoNameData data) {
            holder.setText(R.id.desc_tv, data.desc);
        }

        @Override
        public void onBindEvent(LxContext context, NoNameData data, int eventType) {
            if (eventType == Lx.ViewEvent.LONG_PRESS) {
                // 长按删除 footer
                LxList list = getData().getExtTypeData(TYPE_FOOTER);
                list.updateRemove(0);
            }
            if (eventType == Lx.ViewEvent.CLICK) {
                // 点击删除内容第一个
                LxList list = getData().getContentTypeData();
                list.updateClear();
            }
            if (eventType == Lx.ViewEvent.DOUBLE_CLICK) {
                // 双击再加一个 footer
                LxList list = getData().getExtTypeData(TYPE_FOOTER);
                list.updateAdd(LxPacker.pack(TYPE_FOOTER, new NoNameData("", String.valueOf(System.currentTimeMillis()))));
            }
        }
    }

    class EmptyItemBind extends LxItemBinder<NoNameData> {

        @Override
        protected TypeOpts newTypeOpts() {
            return TypeOpts.make(opts -> {
                opts.layoutId = R.layout.empty_view;
                opts.spanSize = Lx.SpanSize.ALL;
                opts.viewType = TYPE_EMPTY;
                opts.enableClick = true;
                opts.enableLongPress = true;
                opts.enableDbClick = true;
            });
        }

        @Override
        public void onBindView(LxContext context, LxViewHolder holder, NoNameData data) {
            holder.setClick(R.id.refresh_tv, v -> setData());
        }
    }


    static class VerticalImgItemBind extends LxItemBinder<NoNameData> {

        @Override
        protected TypeOpts newTypeOpts() {
            return TypeOpts.make(opts -> {
                opts.viewType = TYPE_VERTICAL_IMG;
                opts.layoutId = R.layout.item_img;
                opts.spanSize = 1;
            });
        }

        @Override
        public void onBindView(LxContext context, LxViewHolder holder, NoNameData listItem) {
            holder.setImage(R.id.cover_iv, listItem.url)
                    .setText(R.id.title_tv, listItem.desc)
                    .setLayoutParams(SizeX.WIDTH / 2, SizeX.WIDTH / 2);
        }
    }

    static class HorizontalImgItemBind extends LxItemBinder<NoNameData> {

        @Override
        protected TypeOpts newTypeOpts() {
            return TypeOpts.make(TYPE_HORIZONTAL_IMG, R.layout.item_img);
        }

        @Override
        public void onBindView(LxContext context, LxViewHolder holder, NoNameData listItem) {
            holder.setImage(R.id.cover_iv, listItem.url);
        }
    }


    static class HorizontalImgContainerItemBind extends LxItemBinder<NoNameData> {

        @Override
        protected TypeOpts newTypeOpts() {
            return TypeOpts.make(opts -> {
                opts.viewType = TYPE_HORIZONTAL_CONTAINER;
                opts.layoutId = R.layout.item_horizontal_container;
                opts.spanSize = Lx.SpanSize.ALL;
            });
        }


        // 初始化没有 adapter 时的 callback，放在这里是避免多次创建造成性能问题
        // 使用 list 创建一个 Adapter 绑定到 view 上
        private LxNesting mLxNesting = new LxNesting((view, list) -> {
            LxAdapter.of(list)
                    .bindItem(new HorizontalImgItemBind())
                    .attachTo(view, LxManager.linear(view.getContext(), true));
        });



        @Override
        public void onBindView(LxContext context, LxViewHolder holder, NoNameData listItem) {
            holder.setText(R.id.title_tv, listItem.desc + " , offset = " + listItem.offset + " pos = " + listItem.pos);
            // 获取到控件
            RecyclerView contentRv = holder.getView(R.id.content_rv);
            // 数据源
            LxSource source = LxSource.just(TYPE_HORIZONTAL_IMG, listItem.datas);
            // 设置，这里会尝试恢复上次的位置，并计算接下来滑动的位置
            mLxNesting.setup(contentRv, context.model, source.asModels());
        }
    }


    static class SelectItemBind extends LxItemBinder<NoNameData> {

        @Override
        protected TypeOpts newTypeOpts() {
            return TypeOpts.make(opts -> {
                opts.layoutId = R.layout.item_squire1;
                opts.enableClick = true;
                opts.enableLongPress = true;
                opts.enableDbClick = false;
                opts.viewType = TYPE_SELECT;
            });
        }

        @Override
        public void onBindView(LxContext context, LxViewHolder holder, NoNameData data) {
            LxModel model = context.model;
            View view = holder.getView(R.id.container_cl);

            if (context.bindMode == Lx.BindMode.CONDITION) {
                // 被选中时会触发条件更新
                if (Lx.Condition.CONDITION_SELECTOR.equals(context.conditionKey)) {
                    holder.setText(R.id.title_tv, model.isSelected() ? "条件：我被选中" : "条件我没有被选中")
                            .setTextColor(R.id.title_tv, model.isSelected() ? Color.RED : Color.BLACK);
                    if (model.isSelected()) {
                        if (view.getScaleX() == 1) {
                            view.animate().scaleX(0.8f).scaleY(0.8f).setDuration(300).start();
                        }
                    } else {
                        if (view.getScaleX() != 1) {
                            view.animate().scaleX(1f).scaleY(1f).setDuration(300).start();
                        }
                    }
                    return;
                }
            }

            holder.setLayoutParams(SizeX.WIDTH / 3, SizeX.WIDTH / 3)
                    .setText(R.id.title_tv, model.isSelected() ? "正常：我被选中" : "正常：我没有被选中")
                    .setTextColor(R.id.title_tv, model.isSelected() ? Color.RED : Color.BLACK);
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

        @Override
        public void onBindEvent(LxContext context, NoNameData data, int eventType) {
            // 点击选中
            LxSelectComponent component = adapter.getComponent(LxSelectComponent.class);
            if (component != null) {
                component.select(context.model);
            }
        }
    }

}
