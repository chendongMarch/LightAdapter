package com.zfy.light.sample;

import android.content.Intent;
import android.view.View;

import com.zfy.component.basic.app.AppActivity;
import com.zfy.component.basic.app.Layout;
import com.zfy.light.sample.cases.NewSampleTestActivity;

import butterknife.OnClick;

/**
 * CreateAt : 2018/11/21
 * Describe :
 *
 * @author chendong
 */
@Layout(R.layout.entry_activity)
public class EntryActivity extends AppActivity {

    @Override
    public void init() {
    }

    @OnClick({R.id.entry})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.entry:
                startActivity(new Intent(getContext(), NewSampleTestActivity.class));
                break;
            default:
                break;
        }
    }
}
