package com.zfy.light.sample.cases;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.collections.LightAsyncDiffList;
import com.zfy.adapter.collections.LightDiffList;
import com.zfy.adapter.collections.LightList;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.R;
import com.zfy.light.sample.entity.Data;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * CreateAt : 2019/4/26
 * Describe :
 *
 * @author chendong
 */
@MvpV(layout = R.layout.light_list_test_activity)
public class LightListTestActivity extends MvpActivity {

    @BindView(R.id.content_rv) RecyclerView    mContentRv;
    private                    LightList<Data> mList;

    @Override
    public void init() {
        mList = new LightAsyncDiffList<>();
        List<Data> snapshot = mList.snapshot();
        for (int i = 0; i < 100; i++) {
            snapshot.add(Data.sample("==" + i + "=="));
        }
        mList.updateAddAll(snapshot);
        LightAdapter<Data> adapter = new LightAdapter<>(mList, R.layout.item_simple);
        adapter.setBindCallback((holder, data, extra) -> {
            holder.setText(R.id.sample_tv, data.title);
        });
        mContentRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mContentRv.setAdapter(adapter);
    }

    /*

    <Button
        android:id="@+id/btn1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="在5插入2个"
        app:layout_constraintTop_toTopOf="parent" />

    <Button
        android:id="@+id/btn2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="删除10位置"
        app:layout_constraintTop_toBottomOf="@+id/btn1" />

    <Button
        android:id="@+id/btn3"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="删除包含0的"
        app:layout_constraintTop_toBottomOf="@+id/btn2" />

    <Button
        android:id="@+id/btn4"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="更改第15个"
        app:layout_constraintTop_toBottomOf="@+id/btn3" />
     */

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                List<Data> list = new ArrayList<>();
                list.add(Data.sample("2新的" + System.currentTimeMillis()));
                list.add(Data.sample("1新的" + System.currentTimeMillis()));
                mList.updateAddAll(5, list);
                break;
            case R.id.btn2:
                mList.updateRemove(10);
                break;
            case R.id.btn3:
                mList.updateRemove(item -> item.title.contains("0"));
                break;
            case R.id.btn4:
                mList.updateSet(15, item -> {
                    item.id = (int) System.currentTimeMillis();
                    item.title = "我是新的啊";
                });
                break;
            default:
                break;
        }
    }
}
