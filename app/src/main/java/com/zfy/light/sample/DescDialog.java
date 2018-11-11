package com.zfy.light.sample;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.zfy.component.basic.app.AppDialog;

import butterknife.BindView;

/**
 * CreateAt : 2018/11/9
 * Describe :
 *
 * @author chendong
 */
public class DescDialog extends AppDialog {

    @BindView(R.id.content_tv) TextView mContentTv;
    @BindView(R.id.title_tv)   TextView mTitleTv;

    private String mContent;
    private String mTitle;


    public static void show(Context context, String title, String msg) {
        DescDialog descDialog = new DescDialog(context);
        descDialog.setContent(msg);
        descDialog.setTitle(title);
        descDialog.show();
    }

    public DescDialog(Context context) {
        super(context);
    }

    @Override
    protected void initViewOnCreate() {
        mContentTv.setText(mContent);
        mTitleTv.setText(mTitle);
    }

    public void setContent(String content) {
        mContent = content;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.desc_dialog;
    }

    @Override
    protected void setWindowParams() {
        setDialogAttributes(MATCH, WRAP, Gravity.CENTER);
        setAnimationBottomToCenter();
    }
}
