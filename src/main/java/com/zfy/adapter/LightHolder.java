package com.zfy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfy.adapter.model.Ids;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * CreateAt : 2016/11/8
 * Describe : custom view holder
 *
 * @author chendong
 */
public class LightHolder extends RecyclerView.ViewHolder {

    public static Class<ImageView> IMAGE = ImageView.class;
    public static Class<TextView> TEXT = TextView.class;
    public static Class<View> VIEW = View.class;
    public static Class<EditText> EDIT = EditText.class;
    public static Class<CompoundButton> COMPOUND_BTN = CompoundButton.class;
    public static Class<Button> BTN = Button.class;

    public static int UNSET = -100;

    private SparseArray<View> mCacheViews;
    private ModelType mModelType;
    private LightAdapter mAdapter;
    private Ids mIds;


    public LightHolder(LightAdapter adapter, int type, View itemView) {
        super(itemView);
        this.mCacheViews = new SparseArray<>(5);
        this.mAdapter = adapter;
        this.mModelType = adapter.getType(type);
        this.mIds = Ids.all();
    }

    public View getItemView() {
        return itemView;
    }

    public Context getContext() {
        return itemView.getContext();
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
        View v = mCacheViews.get(resId);
        if (v == null) {
            v = itemView.findViewById(resId);
            if (v != null) {
                mCacheViews.put(resId, v);
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
        if (view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
    }

    public LightHolder setVisibility(Ids ids, @Visibility int visibility) {
        for (int id : ids.ids()) {
            setVisibility(getView(id), visibility);
        }
        return this;
    }

    public LightHolder setVisibility(@IdRes int resId, @Visibility int visibility) {
        return setVisibility(mIds.obtain(resId), visibility);
    }

    public LightHolder setGone(@IdRes int... resIds) {
        return setVisibility(mIds.obtain(resIds), View.GONE);
    }

    public LightHolder setVisible(@IdRes int... resIds) {
        return setVisibility(mIds.obtain(resIds), View.VISIBLE);
    }

    public LightHolder setInVisible(@IdRes int... resIds) {
        return setVisibility(mIds.obtain(resIds), View.INVISIBLE);
    }

    public LightHolder setVisibleGone(@IdRes int resId, boolean isVisible) {
        return setVisibility(mIds.obtain(resId), isVisible ? View.VISIBLE : View.GONE);
    }

    public LightHolder setVisibleGone(Ids ids, boolean isVisible) {
        return setVisibility(ids, isVisible ? View.VISIBLE : View.GONE);
    }

    public LightHolder setVisibleInVisible(@IdRes int resId, boolean isVisible) {
        return setVisibility(mIds.obtain(resId), isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    public LightHolder setVisibleInVisible(Ids ids, boolean isVisible) {
        return setVisibility(ids, isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    //////////////////////////////  -- View.setSelect --  //////////////////////////////

    public LightHolder setSelect(Ids ids, boolean isSelect) {
        for (int id : ids.ids()) {
            getView(id).setSelected(isSelect);
        }
        return this;
    }

    public LightHolder setSelect(@IdRes int resId, boolean isSelect) {
        return setSelect(mIds.obtain(resId), isSelect);
    }

    public LightHolder setSelectYes(@IdRes int... resIds) {
        return setSelect(mIds.obtain(resIds), true);
    }

    public LightHolder setSelectNo(@IdRes int... resIds) {
        return setSelect(mIds.obtain(resIds), false);
    }

    //////////////////////////////  -- View.setChecked --  //////////////////////////////

    public LightHolder setChecked(Ids ids, boolean isCheck) {
        for (int id : ids.ids()) {
            ((CompoundButton) getView(id)).setChecked(isCheck);
        }
        return this;
    }

    public LightHolder setChecked(@IdRes int resId, boolean isCheck) {
        return setChecked(mIds.obtain(resId), isCheck);
    }

    public LightHolder setCheckedYes(@IdRes int... resIds) {
        return setChecked(mIds.obtain(resIds), true);
    }

    public LightHolder setCheckedNo(@IdRes int... resIds) {
        return setChecked(mIds.obtain(resIds), false);
    }

    //////////////////////////////  -- View.bg --  //////////////////////////////

    public LightHolder setBgDrawable(Ids ids, Drawable drawable) {
        for (int id : ids.ids()) {
            View view = getView(id);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(drawable);
            } else {
                view.setBackgroundDrawable(drawable);
            }
        }
        return this;
    }

    public LightHolder setBgDrawable(@IdRes int resId, Drawable drawable) {
        return setBgDrawable(mIds.obtain(resId), drawable);
    }

    public LightHolder setBgRes(Ids ids, @DrawableRes int bgRes) {
        for (int id : ids.ids()) {
            getView(id).setBackgroundResource(bgRes);
        }
        return this;
    }

    public LightHolder setBgRes(@IdRes int resId, @DrawableRes int bgRes) {
        return setBgRes(mIds.obtain(resId), bgRes);
    }

    public LightHolder setBgColor(Ids ids, int color) {
        for (int id : ids.ids()) {
            getView(id).setBackgroundColor(color);
        }
        return this;
    }

    public LightHolder setBgColor(@IdRes int resId, int color) {
        return setBgColor(mIds.obtain(resId), color);
    }

    public LightHolder setBgColorRes(Ids ids, @ColorRes int colorRes) {
        for (int id : ids.ids()) {
            getView(id).setBackgroundColor(getColor(colorRes));
        }
        return this;
    }

    public LightHolder setBgColorRes(@IdRes int resId, @ColorRes int colorRes) {
        return setBgColorRes(mIds.obtain(resId), colorRes);
    }


    //////////////////////////////  -- TextView 文本颜色 --  //////////////////////////////

    public LightHolder setTextColor(Ids ids, int color) {
        for (int id : ids.ids()) {
            ((TextView) getView(id)).setTextColor(color);
        }
        return this;
    }

    public LightHolder setTextColor(@IdRes int resId, int color) {
        return setTextColor(mIds.obtain(resId), color);
    }

    public LightHolder setTextColorRes(@IdRes int resId, @ColorRes int colorRes) {
        return setTextColor(mIds.obtain(resId), getColor(colorRes));
    }

    public LightHolder setTextColorRes(Ids ids, @ColorRes int colorRes) {
        return setTextColor(ids, getColor(colorRes));
    }

    //////////////////////////////  -- TextView 文本 --  //////////////////////////////

    public LightHolder setText(@IdRes int resId, CharSequence txt, boolean goneIfEmpty) {
        TextView view = getView(resId);
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

    public LightHolder setText(@IdRes int resId, Object txt) {
        if (txt == null) return this;
        return setText(resId, txt.toString(), false);
    }

    public LightHolder setText(@IdRes int resId, CharSequence txt) {
        if (txt == null) return this;
        return setText(resId, txt, false);
    }

    public LightHolder setText(@IdRes int resId, @StringRes int txtRes) {
        if (txtRes == 0) return this;
        String txt = itemView.getContext().getResources().getString(txtRes);
        return setText(resId, txt, false);
    }

    //////////////////////////////  -- ImageView --  //////////////////////////////

    public LightHolder setImage(@IdRes int resId, @DrawableRes int imgResId) {
        ImageView iv = getView(resId);
        iv.setImageResource(imgResId);
        return this;
    }

    public LightHolder setImage(@IdRes int resId, Bitmap bitmap) {
        ImageView iv = getView(resId);
        iv.setImageBitmap(bitmap);
        return this;
    }


    //////////////////////////////  -- View.Event Click --  //////////////////////////////

    public LightHolder setClick(Ids ids, View.OnClickListener listener) {
        for (int id : ids.ids()) {
            getView(id).setOnClickListener(listener);
        }
        return this;
    }

    public LightHolder setClick(int resId, View.OnClickListener listener) {
        return setClick(mIds.obtain(resId), listener);
    }


    //////////////////////////////  -- View.Event Long Click --  //////////////////////////////


    public LightHolder setLongClick(Ids ids, View.OnLongClickListener listener) {
        for (int id : ids.ids()) {
            getView(id).setOnLongClickListener(listener);
        }
        return this;
    }

    public LightHolder setLongClick(int resId, View.OnLongClickListener listener) {
        return setLongClick(mIds.obtain(resId), listener);
    }

    //////////////////////////////  -- View LayoutParams --  //////////////////////////////

    public LightHolder setLayoutParams(Ids ids, int width, int height) {
        for (int id : ids.ids()) {
            View view = getView(id);
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

    public LightHolder setLayoutParams(int resId, int width, int height) {
        return setLayoutParams(mIds.obtain(resId), width, height);
    }

    public LightHolder setLayoutParams(int width, int height) {
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

    public <V extends View> LightHolder setCallback(int resId, Class<V> clazz, Callback<V> callback) {
        setCallback(mIds.obtain(resId), clazz, callback);
        return this;
    }

    public <V extends View> LightHolder setCallback(Ids ids, Class<V> clazz, Callback<V> callback) {
        for (int id : ids.ids()) {
            V view = getView(id);
            if (view.getClass() == clazz) {
                callback.bind(view);
            }
        }
        return this;
    }

    //////////////////////////////  -- 公共方法 --  //////////////////////////////


    public interface Callback<T extends View> {
        void bind(T view);
    }

    private int getColor(int colorRes) {
        return ContextCompat.getColor(getContext(), colorRes);
    }
}
