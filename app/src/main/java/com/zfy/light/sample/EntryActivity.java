package com.zfy.light.sample;

import android.content.Intent;
import android.view.View;

import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.cases.MainActivity;

import butterknife.OnClick;

/**
 * CreateAt : 2018/11/21
 * Describe :
 *
 * @author chendong
 */
@MvpV(layout = R.layout.entry_activity)
public class EntryActivity extends MvpActivity {
    @Override
    public void init() {

    }

    @OnClick({R.id.entry})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.entry:
                launchActivity(new Intent(getContext(), MainActivity.class), 0);
                break;
            default:
                break;
        }
    }
}
