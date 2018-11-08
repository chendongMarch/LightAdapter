package com.zfy.light.sample;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.march.common.exts.ListX;
import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.LightHolder;
import com.zfy.adapter.decoration.LinearDividerDecoration;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;

import java.util.List;

import butterknife.BindView;

/**
 * CreateAt : 2018/11/8
 * Describe :
 *
 * @author chendong
 */
@MvpV(layout = R.layout.main_activity)
public class MainActivity extends MvpActivity {


    @BindView(R.id.content_rv) RecyclerView mRecyclerView;


    @Override
    public void init() {
        List<String> strings = ListX.range(100, String::valueOf);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new LinearDividerDecoration(getContext(), LinearDividerDecoration.VERTICAL, R.drawable.comm_divider);
        mRecyclerView.setAdapter(new LightAdapter<String>(getContext(), strings, R.layout.item_simple) {
            @Override
            public void onBindView(LightHolder holder, String data, int pos) {
                holder.setText(R.id.sample_tv, data);
            }
        });
    }
}
