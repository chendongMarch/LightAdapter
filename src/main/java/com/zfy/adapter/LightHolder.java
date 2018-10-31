package com.zfy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfy.adapter.model.Ids;

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

    public static final String TAG = LightHolder.class.getSimpleName();
    public static final  int    UNSET = -100;

    private SparseArray<View> cacheViews;
    private ModelType modelType;
    private LightAdapter adapter;

    public LightHolder(LightAdapter adapter, int type, View itemView) {
        super(itemView);
        this.cacheViews = new SparseArray<>(5);
        this.adapter = adapter;
        this.modelType = adapter.getType(type);
    }

    public View getItemView() {
        return itemView;
    }

    public Context getContext() {
        return itemView.getContext();
    }

    /**
     * 获取view列表 ArrayList
     *
     * @param resIds id列表
     * @param <T>    范型
     * @return List
     */
    public <T extends View> List<T> getViews(int... resIds) {
        List<T> views = new ArrayList<>();
        for (int resId : resIds) {
            T view = getView(resId);
            views.add(view);
        }
        return views;
    }


    /**
     * 使用资源id找到view
     *
     * @param resId 资源id
     * @param <T>   泛型,View的子类
     * @return 返回泛型类
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int resId) {
        View v = cacheViews.get(resId);
        if (v == null) {
            v = itemView.findViewById(resId);
            if (v != null) {
                cacheViews.put(resId, v);
            }
        }
        return (T) v;
    }

    /**
     * 使用类反射找到字符串id代表的view
     *
     * @param idName String类型ID
     * @param <T>    范型
     * @return 返回
     */
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

    private boolean checkVisibilityParam(int visibility) {
        boolean result = visibility == View.VISIBLE
                || visibility == View.GONE
                || visibility == View.INVISIBLE;
        return result;
    }

    public LightHolder setVisibility(Ids ids, final int visibility) {
        forEachView(new Callback<View>() {
            @Override
            public void bind(LightHolder binder, View view, int pos) {
                if (view.getVisibility() != visibility && checkVisibilityParam(visibility))
                    view.setVisibility(visibility);
            }
        }, ids.getViewIds());
        return this;
    }

    public LightHolder setVisibility(int resId, int visibility) {
        return setVisibility(new Ids(resId), visibility);
    }

    public LightHolder setGone(int... resIds) {
        return setVisibility(new Ids(resIds), View.GONE);
    }

    public LightHolder setVisible(int... resIds) {
        return setVisibility(new Ids(resIds), View.VISIBLE);
    }

    public LightHolder setInVisible(int... resIds) {
        return setVisibility(new Ids(resIds), View.INVISIBLE);
    }

    public LightHolder setVisibleGone(int resId, boolean isVisible) {
        return setVisibility(new Ids(resId), isVisible ? View.VISIBLE : View.GONE);
    }

    public LightHolder setVisibleGone(Ids ids, boolean isVisible) {
        return setVisibility(ids, isVisible ? View.VISIBLE : View.GONE);
    }

    public LightHolder setVisibleInVisible(int resId, boolean isVisible) {
        return setVisibility(new Ids(resId), isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    public LightHolder setVisibleInVisible(Ids ids, boolean isVisible) {
        return setVisibility(ids, isVisible ? View.VISIBLE : View.INVISIBLE);
    }


    //////////////////////////////  -- View.setSelect --  //////////////////////////////

    public LightHolder setSelect(Ids ids, final boolean isSelect) {
        forEachView(new Callback() {
            @Override
            public void bind(LightHolder binder, View view, int pos) {
                view.setSelected(isSelect);
            }
        }, ids.getViewIds());
        return this;
    }

    public LightHolder setSelect(int resId, boolean isSelect) {
        return setSelect(new Ids(resId), isSelect);
    }

    public LightHolder setSelectYes(int... resIds) {
        return setSelect(new Ids(resIds), true);
    }

    public LightHolder setSelectNo(int... resIds) {
        return setSelect(new Ids(resIds), false);
    }

    //////////////////////////////  -- View.setChecked --  //////////////////////////////

    public LightHolder setChecked(Ids ids, final boolean isCheck) {
        forEachView(new Callback() {
            @Override
            public void bind(LightHolder holder, View view, int pos) {
                if (view instanceof CompoundButton) {
                    ((CompoundButton) view).setChecked(isCheck);
                }
            }
        }, ids.getViewIds());
        return this;
    }

    public LightHolder setChecked(int resId, final boolean isCheck) {
        return setChecked(new Ids(resId), isCheck);
    }

    public LightHolder setCheckedYes(int... resIds) {
        return setChecked(new Ids(resIds), true);
    }

    public LightHolder setCheckedNo(int... resIds) {
        return setChecked(new Ids(resIds), false);
    }


    //////////////////////////////  -- View.bg --  //////////////////////////////

    public LightHolder setBgDrawable(Ids ids, final Drawable drawable) {
        forEachView(new Callback<View>() {
            @Override
            public void bind(LightHolder holder, View view, int pos) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(drawable);
                } else {
                    view.setBackgroundDrawable(drawable);
                }

            }
        }, ids.getViewIds());
        return this;
    }

    public LightHolder setBgDrawable(int resId, final Drawable drawable) {
        return setBgDrawable(new Ids(resId), drawable);
    }

    public LightHolder setBgRes(Ids ids, final int bgRes) {
        forEachView(new Callback<View>() {
            @Override
            public void bind(LightHolder holder, View view, int pos) {
                view.setBackgroundResource(bgRes);
            }
        }, ids.getViewIds());
        return this;
    }

    public LightHolder setBgRes(int resId, final int bgRes) {
        return setBgRes(new Ids(resId), bgRes);
    }

    public LightHolder setBgColor(int resId, int color) {
        return setBgColor(new Ids(resId), color);
    }

    public LightHolder setBgColor(Ids ids, final int color) {
        forEachView(new Callback() {
            @Override
            public void bind(LightHolder holder, View view, int pos) {
                view.setBackgroundColor(color);
            }
        }, ids.getViewIds());
        return this;
    }

    public LightHolder setBgColorRes(int resId, int colorRes) {
        return setBgDrawable(new Ids(resId), new ColorDrawable(getColor(colorRes)));
    }

    public LightHolder setBgColorRes(Ids ids, final int colorRes) {
        forEachView(new Callback() {
            @Override
            public void bind(LightHolder holder, View view, int pos) {
                view.setBackgroundColor(getColor(colorRes));
            }
        }, ids.getViewIds());
        return this;
    }

    //////////////////////////////  -- TextView 文本颜色 --  //////////////////////////////

    public LightHolder setTextColor(Ids ids, final int color) {
        forEachView(new Callback<TextView>() {
            @Override
            public void bind(LightHolder holder, TextView view, int pos) {
                view.setTextColor(color);
            }
        }, ids.getViewIds());
        return this;
    }

    public LightHolder setTextColor(int resId, int color) {
        return setTextColor(new Ids(resId), color);
    }

    public LightHolder setTextColorRes(int resId, int colorRes) {
        return setTextColor(new Ids(resId), getColor(colorRes));
    }

    public LightHolder setTextColorRes(Ids ids, int colorRes) {
        return setTextColor(ids, getColor(colorRes));
    }

    //////////////////////////////  -- TextView 文本 --  //////////////////////////////

    public LightHolder setText(int resId, final CharSequence txt, final boolean goneIfEmpty) {
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

    public LightHolder setText(int resId, Object txt) {
        if (txt == null) return this;
        return setText(resId, txt.toString(), false);
    }

    public LightHolder setStyleText(int resId, CharSequence txt) {
        if (txt == null) return this;
        return setText(resId, txt, false);
    }


    public LightHolder setTextRes(int resId, int txtRes) {
        if (txtRes == 0) return this;
        String txt = itemView.getContext().getResources().getString(txtRes);
        return setText(resId, txt, false);
    }

    //////////////////////////////  -- ImageView --  //////////////////////////////

    public LightHolder setImage(int resId, final int imgResId) {
        ImageView iv = getView(resId);
        iv.setImageResource(imgResId);
        return this;
    }

    public LightHolder setImage(int resId, Bitmap bitmap) {
        ImageView iv = getView(resId);
        iv.setImageBitmap(bitmap);
        return this;
    }


    //////////////////////////////  -- View.Event Click --  //////////////////////////////

    public <T extends View> LightHolder setClick(Ids ids, final View.OnClickListener listener) {
        forEachView(new Callback<T>() {
            @Override
            public void bind(final LightHolder holder, final T view, final int pos) {
                view.setOnClickListener(listener);
            }
        }, ids.getViewIds());
        return this;
    }

    public <T extends View> LightHolder setClick(int resId, final View.OnClickListener listener) {
        return setClick(new Ids(resId), listener);
    }


    //////////////////////////////  -- View.Event Long Click --  //////////////////////////////


    public <T extends View> LightHolder setLongClick(Ids ids, final View.OnLongClickListener listener) {

        forEachView(new Callback<T>() {
            @Override
            public void bind(final LightHolder holder, final T view, final int pos) {
                view.setOnLongClickListener(listener);
            }
        }, ids.getViewIds());
        return this;
    }

    public <T extends View> LightHolder setLongClick(int resId, View.OnLongClickListener listener) {
        return setLongClick(new Ids(resId), listener);
    }

    //////////////////////////////  -- View LayoutParams --  //////////////////////////////

    public LightHolder setLayoutParams(Ids ids, final int width, final int height) {
        forEachView(new Callback<View>() {
            @Override
            public void bind(LightHolder holder, View view, int pos) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if (width != UNSET && width > 0)
                    layoutParams.width = width;
                if (width != UNSET && height > 0)
                    layoutParams.height = height;
                view.setLayoutParams(layoutParams);
            }
        }, ids.getViewIds());
        return this;
    }

    public LightHolder setLayoutParams(int resId, int width, int height) {
        return setLayoutParams(new Ids(resId), width, height);
    }

    public LightHolder setLayoutParams(int width, int height) {
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        if (width != UNSET && width > 0)
            layoutParams.width = width;
        if (height != UNSET && height > 0)
            layoutParams.height = height;
        itemView.setLayoutParams(layoutParams);
        return this;
    }

    //////////////////////////////  -- 自己定义的回调绑定 --  //////////////////////////////

    public <T extends View> LightHolder setCallback(int resId, Callback<T> callback) {
        forEachView(callback, resId);
        return this;
    }

    public <T extends View> LightHolder setCallback(Ids ids, Callback<T> callback) {
        forEachView(callback, ids.getViewIds());

        set(100, IMAGE, imageView -> {

        });
        return this;
    }

    public static final Class<ImageView> IMAGE = ImageView.class;


    //////////////////////////////  -- 公共方法 --  //////////////////////////////

    public <V extends View> void set(int id, Class<V> clazz, Callback2<V> callback) {

    }

    public interface Callback2<T extends View> {
        void bind(T view);
    }


    public interface Callback<T extends View> {
        void bind(LightHolder holder, T view, int pos);
    }

    private <T extends View> void forEachView(Callback<T> forEach, int... resIds) {
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
        return ContextCompat.getColor(getContext(), colorRes);
    }


}
