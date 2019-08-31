package com.zfy.light.sample.cases;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.march.common.exts.ListX;
import com.zfy.adapter.x.LxAdapter;
import com.zfy.adapter.x.LxItemBind;
import com.zfy.adapter.x.LxModel;
import com.zfy.adapter.x.LxTransformations;
import com.zfy.adapter.x.LxVh;
import com.zfy.adapter.x.TypeOpts;
import com.zfy.adapter.x.list.LxDiffList;
import com.zfy.adapter.x.list.LxList;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.R;

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

    @BindView(R.id.content_rv) RecyclerView mRecyclerView;

    private LxList<LxModel> mLxModels = new LxDiffList<>();

    @Override
    public void init() {
        LxAdapter.of(mLxModels).bind(new MyItemBind())
                .attachTo(mRecyclerView, new LinearLayoutManager(getContext()));

        List<String> list = ListX.range(20, v -> v + " " + System.currentTimeMillis());
        List<LxModel> models = LxTransformations.map(list);
        mLxModels.update(models);
    }

    static class MyItemBind extends LxItemBind<String> {

        MyItemBind() {
            super(TypeOpts.make(R.layout.item_basic));
        }

        @Override
        public void onBindView(LxVh holder, int position, String data, @NonNull List<Object> payloads) {
            holder.setText(R.id.title_tv, data);
        }
    }
}
