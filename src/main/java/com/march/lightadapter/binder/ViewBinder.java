package com.march.lightadapter.binder;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.march.lightadapter.core.ViewHolder;
import com.march.lightadapter.helper.LightLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2017.09.28
 * Describe : 绑定管理器
 *
 * @author chendong
 */

public class ViewBinder {

    private static final String TAG = ViewBinder.class.getSimpleName();
    public static final int UNSET = -100;

    private SparseArray<View> mCacheViews;
    private View mItemView;
    private ViewFinder mViewFinder;

    private ViewBinder(final View itemView) {
        this(itemView, new ViewFinder() {
            @Override
            public View find(int resId) {
                return itemView.findViewById(resId);
            }
        });
    }

    private ViewBinder(View itemView, ViewFinder viewFinder) {
        this.mCacheViews = new SparseArray<>(5);
        this.mItemView = itemView;
        this.mViewFinder = viewFinder;
    }


    public static ViewBinder from(ViewHolder holder) {
        return new ViewBinder(holder.itemView);
    }

    public static ViewBinder from(View parentView) {
        return new ViewBinder(parentView);
    }

    public static ViewBinder from(final Activity activity) {
        return new ViewBinder(activity.findViewById(android.R.id.content),
                new ViewFinder() {
                    @Override
                    public View find(int resId) {
                        return activity.findViewById(resId);
                    }
                });
    }

    public View getItemView() {
        return mItemView;
    }

    public <T extends View> T getView(int resId) {
        View v = mCacheViews.get(resId);
        if (v == null) {
            v = mViewFinder.find(resId);
            if (v != null) {
                mCacheViews.put(resId, v);
            }
        }
        return (T) v;
    }


    public <T extends View> List<T> getViews(int... resIds) {
        List<T> views = new ArrayList<>();
        for (int resId : resIds) {
            T view = getView(resId);
            views.add(view);
        }
        return views;
    }


    ///////////////////////////////////////////////////////////////////////////
    // View.Visibility
    ///////////////////////////////////////////////////////////////////////////

    private boolean checkVisibilityParam(int visibility) {
        boolean result = visibility == View.VISIBLE
                || visibility == View.GONE
                || visibility == View.INVISIBLE;
        if (!result) {
            LightLogger.e(TAG, "checkVisibilityParam fail");
        }
        return result;
    }

    public ViewBinder setVisibility(Ids ids, final int visibility) {
        forEachView(new BindCallback<View>() {
            @Override
            public void bind(ViewBinder binder, View view, int pos) {
                if (view.getVisibility() != visibility && checkVisibilityParam(visibility))
                    view.setVisibility(visibility);
            }
        }, ids.getViewIds());
        return this;
    }

    public ViewBinder setVisibility(int resId, int visibility) {
        return setVisibility(new Ids(resId), visibility);
    }

    public ViewBinder setGone(int... resIds) {
        return setVisibility(new Ids(resIds), View.GONE);
    }

    public ViewBinder setVisible(int... resIds) {
        return setVisibility(new Ids(resIds), View.VISIBLE);
    }

    public ViewBinder setInVisible(int... resIds) {
        return setVisibility(new Ids(resIds), View.INVISIBLE);
    }

    public ViewBinder setVisibleGone(int resId, boolean isVisible) {
        return setVisibility(new Ids(resId), isVisible ? View.VISIBLE : View.GONE);
    }

    public ViewBinder setVisibleGone(Ids ids, boolean isVisible) {
        return setVisibility(ids, isVisible ? View.VISIBLE : View.GONE);
    }

    public ViewBinder setVisibleInVisible(int resId, boolean isVisible) {
        return setVisibility(new Ids(resId), isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    public ViewBinder setVisibleInVisible(Ids ids, boolean isVisible) {
        return setVisibility(ids, isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    ///////////////////////////////////////////////////////////////////////////
    // View.setSelect
    ///////////////////////////////////////////////////////////////////////////

    public ViewBinder setSelect(Ids ids, final boolean isSelect) {
        forEachView(new BindCallback<View>() {
            @Override
            public void bind(ViewBinder binder, View view, int pos) {
                view.setSelected(isSelect);
            }
        }, ids.getViewIds());
        return this;
    }

    public ViewBinder setSelect(int resId, boolean isSelect) {
        return setSelect(new Ids(resId), isSelect);
    }

    public ViewBinder setSelectYes(int... resIds) {
        return setSelect(new Ids(resIds), true);
    }

    public ViewBinder setSelectNo(int... resIds) {
        return setSelect(new Ids(resIds), false);
    }

    ///////////////////////////////////////////////////////////////////////////
    // View.setChecked
    ///////////////////////////////////////////////////////////////////////////

    public ViewBinder setChecked(Ids ids, final boolean isCheck) {
        forEachView(new BindCallback<View>() {
            @Override
            public void bind(ViewBinder binder, View view, int pos) {
                if (view instanceof CompoundButton) {
                    ((CompoundButton) view).setChecked(isCheck);
                }
            }
        }, ids.getViewIds());
        return this;
    }

    public ViewBinder setChecked(int resId, final boolean isCheck) {
        return setChecked(new Ids(resId), isCheck);
    }

    public ViewBinder setCheckedYes(int... resIds) {
        return setChecked(new Ids(resIds), true);
    }

    public ViewBinder setCheckedNo(int... resIds) {
        return setChecked(new Ids(resIds), false);
    }


    ///////////////////////////////////////////////////////////////////////////
    // View.bg
    ///////////////////////////////////////////////////////////////////////////
    public ViewBinder setBgDrawable(Ids ids, final Drawable drawable) {
        forEachView(new BindCallback<View>() {
            @Override
            public void bind(ViewBinder binder, View view, int pos) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(drawable);
                } else {
                    view.setBackgroundDrawable(drawable);
                }

            }
        }, ids.getViewIds());
        return this;
    }

    public ViewBinder setBgDrawable(int resId, final Drawable drawable) {
        return setBgDrawable(new Ids(resId), drawable);
    }

    public ViewBinder setBgRes(Ids ids, final int bgRes) {
        forEachView(new BindCallback<View>() {
            @Override
            public void bind(ViewBinder binder, View view, int pos) {
                view.setBackgroundResource(bgRes);
            }
        }, ids.getViewIds());
        return this;
    }

    public ViewBinder setBgRes(int resId, final int bgRes) {
        return setBgRes(new Ids(resId), bgRes);
    }

    public ViewBinder setBgColor(int resId, int color) {
        return setBgColor(new Ids(resId), color);
    }

    public ViewBinder setBgColor(Ids ids, final int color) {
        forEachView(new BindCallback<View>() {
            @Override
            public void bind(ViewBinder binder, View view, int pos) {
                view.setBackgroundColor(color);
            }
        }, ids.getViewIds());
        return this;
    }

    public ViewBinder setBgColorRes(int resId, int colorRes) {
        return setBgDrawable(new Ids(resId), new ColorDrawable(getColor(colorRes)));
    }

    public ViewBinder setBgColorRes(Ids ids, final int colorRes) {
        forEachView(new BindCallback<View>() {
            @Override
            public void bind(ViewBinder binder, View view, int pos) {
                view.setBackgroundColor(getColor(colorRes));
            }
        }, ids.getViewIds());
        return this;
    }


    ///////////////////////////////////////////////////////////////////////////
    // TextView 文本颜色
    ///////////////////////////////////////////////////////////////////////////

    public ViewBinder setTextColor(Ids ids, final int color) {
        forEachView(new BindCallback<View>() {
            @Override
            public void bind(ViewBinder binder, View view, int pos) {
                if (view instanceof TextView)
                    ((TextView) view).setTextColor(color);
            }
        }, ids.getViewIds());
        return this;
    }

    public ViewBinder setTextColor(int resId, int color) {
        return setTextColor(new Ids(resId), color);
    }

    public ViewBinder setTextColorRes(int resId, int colorRes) {
        return setTextColor(new Ids(resId), getColor(colorRes));
    }

    public ViewBinder setTextColorRes(Ids ids, int colorRes) {
        return setTextColor(ids, getColor(colorRes));
    }

    ///////////////////////////////////////////////////////////////////////////
    // TextView 文本
    ///////////////////////////////////////////////////////////////////////////

    public ViewBinder setText(int resId, final CharSequence txt, final boolean goneIfEmpty) {
        TextView view = getView(resId);
        if (goneIfEmpty) {
            if (TextUtils.isEmpty(txt)) {
                view.setVisibility(View.GONE);
            }
        }
        view.setText(txt);
        if (view instanceof EditText)
            ((EditText) view).setSelection(view.getText().toString().trim().length());
        return this;
    }

    public ViewBinder setText(int resId, Object txt) {
        if (txt == null) return this;
        return setText(resId, txt.toString(), false);
    }

    public ViewBinder setStyleText(int resId, CharSequence txt) {
        if (txt == null) return this;
        return setText(resId, txt, false);
    }


    public ViewBinder setTextRes(int resId, int txtRes) {
        if (txtRes == 0) return this;
        String txt = mItemView.getContext().getResources().getString(txtRes);
        return setText(resId, txt, false);
    }

    ///////////////////////////////////////////////////////////////////////////
    // ImageView
    ///////////////////////////////////////////////////////////////////////////

    public ViewBinder setImage(int resId, final int imgResId) {
        ImageView iv = getView(resId);
        iv.setImageResource(imgResId);
        return this;
    }

    public ViewBinder setImage(int resId, Bitmap bitmap) {
        ImageView iv = getView(resId);
        iv.setImageBitmap(bitmap);
        return this;
    }


    ///////////////////////////////////////////////////////////////////////////
    // View.Event Click
    ///////////////////////////////////////////////////////////////////////////

    public <T extends View> ViewBinder setClick(Ids ids, final BindCallback<T> bindCallback) {
        forEachView(new BindCallback<T>() {
            @Override
            public void bind(final ViewBinder binder, final T view, final int pos) {
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bindCallback.bind(binder, view, pos);
                    }
                };
                view.setOnClickListener(listener);
            }
        }, ids.getViewIds());
        return this;
    }

    public <T extends View> ViewBinder setClick(int resId, BindCallback<T> bindCallback) {
        return setClick(new Ids(resId), bindCallback);
    }


    ///////////////////////////////////////////////////////////////////////////
    // View.Event Long Click
    ///////////////////////////////////////////////////////////////////////////


    public <T extends View> ViewBinder setLongClick(Ids ids, final BindCallback<T> bindCallback) {

        forEachView(new BindCallback<T>() {
            @Override
            public void bind(final ViewBinder binder, final T view, final int pos) {
                View.OnLongClickListener listener = new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        bindCallback.bind(binder, view, pos);
                        return true;
                    }
                };
                view.setOnLongClickListener(listener);
            }
        }, ids.getViewIds());
        return this;
    }

    public <T extends View> ViewBinder setLongClick(int resId, final BindCallback<T> bindCallback) {
        return setLongClick(new Ids(resId), bindCallback);
    }


    ///////////////////////////////////////////////////////////////////////////
    // View LayoutParams
    ///////////////////////////////////////////////////////////////////////////

    public ViewBinder setLayoutParams(Ids ids, final int width, final int height) {
        forEachView(new BindCallback<View>() {
            @Override
            public void bind(ViewBinder binder, View view, int pos) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if (width != UNSET && width > 0)
                    layoutParams.width = width;
                if (height != UNSET && height > 0)
                    layoutParams.height = height;
                view.setLayoutParams(layoutParams);
            }
        }, ids.getViewIds());
        return this;
    }

    public ViewBinder setLayoutParams(int resId, int width, int height) {
        return setLayoutParams(new Ids(resId), width, height);
    }

    public ViewBinder setLayoutParams(int width, int height) {
        ViewGroup.LayoutParams layoutParams = mItemView.getLayoutParams();
        if (width != UNSET && width > 0)
            layoutParams.width = width;
        if (height != UNSET && height > 0)
            layoutParams.height = height;
        mItemView.setLayoutParams(layoutParams);

        return this;
    }


    ///////////////////////////////////////////////////////////////////////////
    // 自己定义的回调绑定
    ///////////////////////////////////////////////////////////////////////////

    public <T extends View> ViewBinder setCallback(int resId, BindCallback<T> callback) {
        forEachView(callback, resId);
        return this;
    }

    public <T extends View> ViewBinder setCallback(Ids ids, BindCallback<T> callback) {
        forEachView(callback, ids.getViewIds());
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 公共方法
    ///////////////////////////////////////////////////////////////////////////


    public interface BindCallback<T extends View> {
        void bind(ViewBinder binder, T view, int pos);
    }

    private <T extends View> void forEachView(BindCallback<T> forEach, int... resIds) {
        T view;
        for (int i = 0; i < resIds.length; i++) {
            int resId = resIds[i];
            view = getView(resId);
            if (view != null && forEach != null) {
                forEach.bind(this, view, i);
            }
        }
    }

    private int getColor(int colorRes) {
        return ContextCompat.getColor(mItemView.getContext(), colorRes);
    }
}
