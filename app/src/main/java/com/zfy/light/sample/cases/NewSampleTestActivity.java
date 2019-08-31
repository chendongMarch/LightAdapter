package com.zfy.light.sample.cases;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

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
import com.zfy.adapter.x.component.LxLoadMoreComponent;
import com.zfy.adapter.x.function.LxTransformations;
import com.zfy.adapter.x.list.LxDiffList;
import com.zfy.adapter.x.list.LxList;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.R;

import java.util.ArrayList;
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

    @BindView(R.id.content_rv) RecyclerView mRecyclerView;

    private LxList<LxModel> mLxModels = new LxDiffList<>();

    @Override
    public void init() {
        LxAdapter.of(mLxModels)
                .binder(new StudentItemBind(), new TeacherItemBind())
                .component(new LxLoadMoreComponent(
                        Lx.LOAD_MORE_END_EDGE, // edge
                        LxLoadMoreComponent.DEFAULT_START_LOAD_COUNT, // 预加载个数
                        (component) -> { // 加载回调
                            ToastX.show("加在更多");
                            ExecutorsPool.ui(component::finishLoadMore, 2000);
                        }))
                .attachTo(mRecyclerView, new GridLayoutManager(getContext(), 3));

        List<Student> students = ListX.range(10, index -> new Student(index + " " + System.currentTimeMillis()));
        List<Teacher> teachers = ListX.range(10, index -> new Teacher(index + " " + System.currentTimeMillis()));
        List<LxModel> lxModels = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            lxModels.add(LxTransformations.pack(TYPE_STUDENT, students.get(i)));
            lxModels.add(LxTransformations.pack(TYPE_TEACHER, teachers.get(i)));
        }
        mLxModels.update(lxModels);
    }


    static class Student {

        public String name;

        public Student(String name) {
            this.name = name;
        }
    }

    static class Teacher {

        public String name;

        public Teacher(String name) {
            this.name = name;
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
            }));
        }

        @Override
        public void onBindView(LxVh holder, int position, Student data, @NonNull List<Object> payloads) {
            holder.setText(R.id.title_tv, "学：" + data.name).setText(R.id.desc_tv, position + " " + TYPE_STUDENT);
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
            }));
        }

        @Override
        public void onBindView(LxVh holder, int position, Teacher data, @NonNull List<Object> payloads) {
            holder.setText(R.id.title_tv, "师：" + data.name).setText(R.id.desc_tv, position + " " + TYPE_TEACHER);
        }

        @Override
        public void onEvent(LxContext context, Teacher data, int eventType) {
            ToastX.show("点击老师 position = " + context.position + " data = " + data.name + " eventType = " + eventType);

            LxList<LxModel> list = getAdapter().getData();
            list.updateRemove(0);
        }
    }
}
