package com.zfy.adapter.x;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfy.adapter.R;
import com.zfy.adapter.model.Ids;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2019-08-30
 * Describe :
 *
 * @author chendong
 */
public class LxVh extends RecyclerView.ViewHolder {

    public static int UNSET = -100;

    private SparseArray<View> cacheViews;

    public LxVh(View itemView) {
        super(itemView);
        this.cacheViews = new SparseArray<>(5);
    }

    public void setLxContext(Object tag) {
        itemView.setTag(R.id.item_context, tag);
    }

    // 获取 view 列表 ArrayList
    public <T extends View> List<T> getViews(int... resIds) {
        List<T> views = new ArrayList<>();
        for (int resId : resIds) {
            T view = getView(resId);
            views.add(view);
        }
        return views;
    }

    // 使用资源 id 找到 view
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int resId) {
        if (resId == R.id.item_view) {
            return (T) itemView;
        }
        View v = cacheViews.get(resId);
        if (v == null) {
            v = itemView.findViewById(resId);
            if (v != null) {
                cacheViews.put(resId, v);
            }
        }
        return (T) v;
    }

    // 使用类反射找到字符串 id 代表的 view
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(String idName) {
        View view = null;
        if (idName != null) {
            Class<R.id> idClass = R.id.class;
            try {
                Field field = idClass.getDeclaredField(idName);
                field.setAccessible(true);
                int id = field.getInt(idClass);
                view = getView(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (T) view;
    }


    //////////////////////////////  -- View.Visibility --  //////////////////////////////


    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Visibility {
    }

    private void setVisibility(View view, @Visibility int visibility) {
        if (view == null) {
            return;
        }
        if (view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
    }

    public LxVh setVisibility(Ids ids, @Visibility int visibility) {
        for (int id : ids.ids()) {
            setVisibility(getView(id), visibility);
        }
        return this;
    }

    public LxVh setVisibility(@IdRes int resId, @Visibility int visibility) {
        return setVisibility(all(resId), visibility);
    }

    public LxVh setGone(@IdRes int... resIds) {
        return setVisibility(all(resIds), View.GONE);
    }

    public LxVh setVisible(@IdRes int... resIds) {
        return setVisibility(all(resIds), View.VISIBLE);
    }


    public LxVh setInVisible(@IdRes int... resIds) {
        return setVisibility(all(resIds), View.INVISIBLE);
    }

    public LxVh setVisibleGone(@IdRes int resId, boolean isVisible) {
        return setVisibility(all(resId), isVisible ? View.VISIBLE : View.GONE);
    }

    public LxVh setVisibleGone(Ids ids, boolean isVisible) {
        return setVisibility(ids, isVisible ? View.VISIBLE : View.GONE);
    }

    public LxVh setVisibleInVisible(@IdRes int resId, boolean isVisible) {
        return setVisibility(all(resId), isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    public LxVh setVisibleInVisible(Ids ids, boolean isVisible) {
        return setVisibility(ids, isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    //////////////////////////////  -- View.setSelect --  //////////////////////////////

    public LxVh setSelect(Ids ids, boolean isSelect) {
        for (int id : ids.ids()) {
            View view = getView(id);
            if (view == null) {
                continue;
            }
            view.setSelected(isSelect);
        }
        return this;
    }

    public LxVh setSelect(@IdRes int resId, boolean isSelect) {
        return setSelect(all(resId), isSelect);
    }

    public LxVh setSelectYes(@IdRes int... resIds) {
        return setSelect(all(resIds), true);
    }

    public LxVh setSelectNo(@IdRes int... resIds) {
        return setSelect(all(resIds), false);
    }

    //////////////////////////////  -- View.setChecked --  //////////////////////////////

    public LxVh setChecked(Ids ids, boolean isCheck) {
        for (int id : ids.ids()) {
            View view = getView(id);
            if (view == null || !(view instanceof CompoundButton)) {
                continue;
            }
            ((CompoundButton) view).setChecked(isCheck);
        }
        return this;
    }

    public LxVh setChecked(@IdRes int resId, boolean isCheck) {
        return setChecked(all(resId), isCheck);
    }

    public LxVh setCheckedYes(@IdRes int... resIds) {
        return setChecked(all(resIds), true);
    }

    public LxVh setCheckedNo(@IdRes int... resIds) {
        return setChecked(all(resIds), false);
    }

    //////////////////////////////  -- View.bg --  //////////////////////////////

    public LxVh setBgDrawable(Ids ids, Drawable drawable) {
        for (int id : ids.ids()) {
            View view = getView(id);
            if (view == null) {
                continue;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(drawable);
            } else {
                view.setBackgroundDrawable(drawable);
            }
        }
        return this;
    }

    public LxVh setBgDrawable(@IdRes int resId, Drawable drawable) {
        return setBgDrawable(all(resId), drawable);
    }

    public LxVh setBgRes(Ids ids, @DrawableRes int bgRes) {
        for (int id : ids.ids()) {
            View view = getView(id);
            if (view == null) {
                continue;
            }
            view.setBackgroundResource(bgRes);
        }
        return this;
    }

    public LxVh setBgRes(@IdRes int resId, @DrawableRes int bgRes) {
        return setBgRes(all(resId), bgRes);
    }

    public LxVh setBgColor(Ids ids, int color) {
        for (int id : ids.ids()) {
            View view = getView(id);
            if (view == null) {
                continue;
            }
            view.setBackgroundColor(color);
        }
        return this;
    }

    public LxVh setBgColor(@IdRes int resId, int color) {
        return setBgColor(all(resId), color);
    }

    public LxVh setBgColorRes(Ids ids, @ColorRes int colorRes) {
        for (int id : ids.ids()) {
            View view = getView(id);
            if (view == null) {
                continue;
            }
            view.setBackgroundColor(getColor(colorRes));
        }
        return this;
    }

    public LxVh setBgColorRes(@IdRes int resId, @ColorRes int colorRes) {
        return setBgColorRes(all(resId), colorRes);
    }


    //////////////////////////////  -- TextView 文本颜色 --  //////////////////////////////

    public LxVh setTextColor(Ids ids, int color) {
        for (int id : ids.ids()) {
            View view = getView(id);
            if (view == null || !(view instanceof TextView)) {
                continue;
            }
            ((TextView) view).setTextColor(color);
        }
        return this;
    }

    public LxVh setTextColor(@IdRes int resId, int color) {
        return setTextColor(all(resId), color);
    }

    public LxVh setTextColorRes(@IdRes int resId, @ColorRes int colorRes) {
        return setTextColor(all(resId), getColor(colorRes));
    }

    public LxVh setTextColorRes(Ids ids, @ColorRes int colorRes) {
        return setTextColor(ids, getColor(colorRes));
    }

    //////////////////////////////  -- TextView 文本 --  //////////////////////////////

    public LxVh setText(@IdRes int resId, CharSequence txt, boolean goneIfEmpty) {
        TextView view = getView(resId);
        if (view == null) {
            return this;
        }
        if (goneIfEmpty) {
            if (TextUtils.isEmpty(txt)) {
                setVisibility(view, View.GONE);
            }
        }
        view.setText(txt);
        if (view instanceof EditText) {
            ((EditText) view).setSelection(view.getText().toString().trim().length());
        }
        return this;
    }

    public LxVh setText(@IdRes int resId, CharSequence txt) {
        if (txt == null) {
            return this;
        }
        return setText(resId, txt, false);
    }

    public LxVh setTextRes(@IdRes int resId, @StringRes int txtRes) {
        if (txtRes == 0) {
            return this;
        }
        String txt = itemView.getContext().getResources().getString(txtRes);
        return setText(resId, txt, false);
    }

    //////////////////////////////  -- ImageView --  //////////////////////////////

    public LxVh setImage(@IdRes int resId, Drawable imgResId) {
        ImageView iv = getView(resId);
        if (iv == null) {
            return this;
        }
        iv.setImageDrawable(imgResId);
        return this;
    }


    public LxVh setImage(@IdRes int resId, @DrawableRes int imgResId) {
        ImageView iv = getView(resId);
        if (iv == null) {
            return this;
        }
        iv.setImageResource(imgResId);
        return this;
    }

    public LxVh setImage(@IdRes int resId, Bitmap bitmap) {
        ImageView iv = getView(resId);
        if (iv == null) {
            return this;
        }
        iv.setImageBitmap(bitmap);
        return this;
    }

    public LxVh setElevation(@IdRes int resId, int sizeInPx) {
        View view = getView(resId);
        ViewCompat.setElevation(view, sizeInPx);
        return this;
    }


    //////////////////////////////  -- View.Event Click --  //////////////////////////////

    /**
     * 设置 View 点击监听
     *
     * @param ids view id 数组
     * @return holder
     */
    public LxVh setClick(int... ids) {
        for (int id : ids) {
            View view = getView(id);
            if (view == null) {
                continue;
            }
            // adapter.mLightEvent.setChildViewClickListener(this, view);
        }
        return this;
    }


    /**
     * 设置 View 点击监听
     *
     * @param ids      Ids
     * @param listener 点击事件
     * @return holder
     */
    public LxVh setClick(Ids ids, View.OnClickListener listener) {
        if (listener == null) {
            return this;
        }
        for (int id : ids.ids()) {
            View view = getView(id);
            if (view == null) {
                continue;
            }
            view.setClickable(true);
            view.setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 设置 View 点击监听
     *
     * @param resId    view id
     * @param listener 点击事件
     * @return holder
     */
    public LxVh setClick(int resId, View.OnClickListener listener) {
        return setClick(all(resId), listener);
    }

    /**
     * 给 ItemView 设置监听
     *
     * @param listener 监听事件
     * @return holder
     */
    public LxVh setClick(View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
        return this;
    }


    //////////////////////////////  -- View.Event Long Click --  //////////////////////////////

    /**
     * 设置长按监听
     *
     * @param ids view id 数组
     * @return holder
     */
    public LxVh setLongClick(int... ids) {
        for (int id : ids) {
            View view = getView(id);
            if (view == null) {
                continue;
            }
            // adapter.mLightEvent.setChildViewLongPressListener(this, view);
        }
        return this;
    }

    /**
     * 设置长按监听
     *
     * @param ids      view id 数组
     * @param listener 监听事件
     * @return holder
     */
    public LxVh setLongClick(Ids ids, View.OnLongClickListener listener) {
        for (int id : ids.ids()) {
            View view = getView(id);
            if (view == null) {
                continue;
            }
            view.setLongClickable(true);
            view.setOnLongClickListener(listener);
        }
        return this;
    }

    /**
     * 设置长按监听
     *
     * @param resId    view id
     * @param listener 监听
     * @return holder
     */
    public LxVh setLongClick(int resId, View.OnLongClickListener listener) {
        return setLongClick(all(resId), listener);
    }

    public LxVh setLongClick(View.OnLongClickListener listener) {
        itemView.setLongClickable(true);
        itemView.setOnLongClickListener(listener);
        return this;
    }


    //////////////////////////////  -- View LayoutParams --  //////////////////////////////

    public LxVh setLayoutParams(Ids ids, int width, int height) {
        for (int id : ids.ids()) {
            View view = getView(id);
            if (view == null) {
                continue;
            }
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (width != UNSET && width > 0) {
                layoutParams.width = width;
            }
            if (width != UNSET && height > 0) {
                layoutParams.height = height;
            }
            view.setLayoutParams(layoutParams);
        }
        return this;
    }

    public LxVh setLayoutParams(int resId, int width, int height) {
        return setLayoutParams(all(resId), width, height);
    }

    public LxVh setLayoutParams(int width, int height) {
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        if (width != UNSET && width > 0) {
            layoutParams.width = width;
        }
        if (height != UNSET && height > 0) {
            layoutParams.height = height;
        }
        itemView.setLayoutParams(layoutParams);
        return this;
    }

    //////////////////////////////  -- 自己定义的回调绑定 --  //////////////////////////////

    public <V extends View> LxVh setCallback(int resId, Class<V> clazz, Callback<V> callback) {
        setCallback(all(resId), clazz, callback);
        return this;
    }

    public <V extends View> LxVh setCallback(int resId, Callback<V> callback) {
        setCallback(all(resId), callback);
        return this;
    }


    public <V extends View> LxVh setCallback(Ids ids, Callback<V> callback) {
        for (int id : ids.ids()) {
            V view = getView(id);
            callback.bind(view);
        }
        return this;
    }

    public <V extends View> LxVh setCallback(Ids ids, Class<V> clazz, Callback<V> callback) {
        for (int id : ids.ids()) {
            V view = getView(id);
            if (view == null) {
                continue;
            }
            callback.bind(view);
        }
        return this;
    }

    //////////////////////////////  -- drag & swipe --  //////////////////////////////

//    public LxVh dragOnTouch(int... ids) {
//        if (ids.length == 0) {
//            adapter.dragSwipe().dragOnTouch(itemView, this);
//            return this;
//        }
//        for (int id : ids) {
//            View view = getView(id);
//            if (view != null) {
//                adapter.dragSwipe().dragOnTouch(view, this);
//            }
//        }
//        return this;
//    }
//
//    public LxVh dragOnLongPress(int... ids) {
//        if (ids.length == 0) {
//            adapter.dragSwipe().dragOnLongPress(itemView, this);
//            return this;
//        }
//        for (int id : ids) {
//            View view = getView(id);
//            if (view != null) {
//                adapter.dragSwipe().dragOnLongPress(view, this);
//            }
//        }
//        return this;
//    }
//
//    public LxVh swipeOnTouch(int... ids) {
//        if (ids.length == 0) {
//            adapter.dragSwipe().swipeOnTouch(itemView, this);
//            return this;
//        }
//        for (int id : ids) {
//            View view = getView(id);
//            if (view != null) {
//                adapter.dragSwipe().swipeOnTouch(view, this);
//            }
//        }
//        return this;
//    }
//
//    public LxVh swipeOnLongPress(int... ids) {
//        if (ids.length == 0) {
//            adapter.dragSwipe().swipeOnLongPress(itemView, this);
//            return this;
//        }
//        for (int id : ids) {
//            View view = getView(id);
//            if (view != null) {
//                adapter.dragSwipe().swipeOnLongPress(view, this);
//            }
//        }
//        return this;
//    }


    //////////////////////////////  -- 公共方法 --  //////////////////////////////


    private Ids all(@IdRes int... resIds) {
        return Ids.all(resIds);
    }

    public interface Callback<T extends View> {
        void bind(T view);
    }

    private int getColor(int colorRes) {
        return ContextCompat.getColor(itemView.getContext(), colorRes);
    }
}
