package com.zfy.component.basic.app.view;

import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * CreateAt : 2017/12/7
 * Describe :
 *
 * @author chendong
 */
public interface IOnResultView {

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
