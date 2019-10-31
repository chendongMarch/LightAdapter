package com.zfy.light.sample;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.zfy.component.basic.app.AppActivity;
import com.zfy.component.basic.app.Layout;
import com.zfy.light.sample.cases.NewSampleTestActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * CreateAt : 2018/11/21
 * Describe :
 *
 * @author chendong
 */
@Layout(R.layout.entry_activity)
public class EntryActivity extends AppActivity {


    @BindView(R.id.iv) AwesomeImageView mIv;

    @Override
    public void init() {
    }

    @OnClick({R.id.entry, R.id.test})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.entry:
                startActivity(new Intent(getContext(), NewSampleTestActivity.class));
                break;
            case R.id.test:
                ViewGroup.LayoutParams layoutParams = mIv.getLayoutParams();
                layoutParams.width = (int) (300 + Math.random() * 300);
                layoutParams.height = (int) (300 + Math.random() * 300);
                mIv.setLayoutParams(layoutParams);

                break;
            default:
                break;
        }
    }
}
