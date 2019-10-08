package com.zfy.lxadapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
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

import com.zfy.lxadapter.component.LxDragSwipeComponent;
import com.zfy.lxadapter.data.Ids;
import com.zfy.lxadapter.data.LxContext;

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
public class LxViewHolder extends RecyclerView.ViewHolder {

    public static int UNSET = -100;

    private SparseArray<View> cacheViews;
    private int               itemViewType;

    public LxViewHolder(View itemView) {
        super(itemView);
        this.cacheViews = new SparseArray<>(5);
    }

    public void setLxContext(Object tag) {
        itemView.setTag(R.id.item_context, tag);
    }

    public @Nullable
    LxContext getLxContext() {
        Object viewTag = itemView.getTag(R.id.item_context);
        if (viewTag != null) {
            LxContext context = (LxContext) viewTag;
            context.layoutPosition = getAdapterPosition();
            context.viewType = getItemViewType();
            return context;
        }
        return null;
    }

    public void setItemViewType(int itemViewType) {
        this.itemViewType = itemViewType;
    }

    public int getViewType() {
        return this.itemViewType;
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

    public LxViewHolder setVisibility(Ids ids, @Visibility int visibility) {
        for (int id : ids.ids()) {
            setVisibility(getView(id), visibility);
        }
        return this;
    }

    public LxViewHolder setVisibility(@IdRes int resId, @Visibility int visibility) {
        return setVisibility(all(resId), visibility);
    }

    public LxViewHolder setGone(@IdRes int... resIds) {
        return setVisibility(all(resIds), View.GONE);
    }

    public LxViewHolder setVisible(@IdRes int... resIds) {
        return setVisibility(all(resIds), View.VISIBLE);
    }


    public LxViewHolder setInVisible(@IdRes int... resIds) {
        return setVisibility(all(resIds), View.INVISIBLE);
    }

    public LxViewHolder setVisibleGone(@IdRes int resId, boolean isVisible) {
        return setVisibility(all(resId), isVisible ? View.VISIBLE : View.GONE);
    }

    public LxViewHolder setVisibleGone(Ids ids, boolean isVisible) {
        return setVisibility(ids, isVisible ? View.VISIBLE : View.GONE);
    }

    public LxViewHolder setVisibleInVisible(@IdRes int resId, boolean isVisible) {
        return setVisibility(all(resId), isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    public LxViewHolder setVisibleInVisible(Ids ids, boolean isVisible) {
        return setVisibility(ids, isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    //////////////////////////////  -- View.setSelect --  //////////////////////////////

    public LxViewHolder setSelect(Ids ids, boolean isSelect) {
        for (int id : ids.ids()) {
            View view = getView(id);
            if (view == null) {
                continue;
            }
            view.setSelected(isSelect);
        }
        return this;
    }

    public LxViewHolder setSelect(@IdRes int resId, boolean isSelect) {
        return setSelect(all(resId), isSelect);
    }

    public LxViewHolder setSelectYes(@IdRes int... resIds) {
        return setSelect(all(resIds), true);
    }

    public LxViewHolder setSelectNo(@IdRes int... resIds) {
        return setSelect(all(resIds), false);
    }

    //////////////////////////////  -- View.setChecked --  //////////////////////////////

    public LxViewHolder setChecked(Ids ids, boolean isCheck) {
        for (int id : ids.ids()) {
            View view = getView(id);
            if (view == null || !(view instanceof CompoundButton)) {
                continue;
            }
            ((CompoundButton) view).setChecked(isCheck);
        }
        return this;
    }

    public LxViewHolder setChecked(@IdRes int resId, boolean isCheck) {
        return setChecked(all(resId), isCheck);
    }

    public LxViewHolder setCheckedYes(@IdRes int... resIds) {
        return setChecked(all(resIds), true);
    }

    public LxViewHolder setCheckedNo(@IdRes int... resIds) {
        return setChecked(all(resIds), false);
    }

    //////////////////////////////  -- View.bg --  //////////////////////////////

    public LxViewHolder setBgDrawable(Ids ids, Drawable drawable) {
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

    public LxViewHolder setBgDrawable(@IdRes int resId, Drawable drawable) {
        return setBgDrawable(all(resId), drawable);
    }

    public LxViewHolder setBgRes(Ids ids, @DrawableRes int bgRes) {
        for (int id : ids.ids()) {
            View view = getView(id);
            if (view == null) {
                continue;
            }
            view.setBackgroundResource(bgRes);
        }
        return this;
    }

    public LxViewHolder setBgRes(@IdRes int resId, @DrawableRes int bgRes) {
        return setBgRes(all(resId), bgRes);
    }

    public LxViewHolder setBgColor(Ids ids, int color) {
        for (int id : ids.ids()) {
            View view = getView(id);
            if (view == null) {
                continue;
            }
            view.setBackgroundColor(color);
        }
        return this;
    }

    public LxViewHolder setBgColor(@IdRes int resId, int color) {
        return setBgColor(all(resId), color);
    }

    public LxViewHolder setBgColorRes(Ids ids, @ColorRes int colorRes) {
        for (int id : ids.ids()) {
            View view = getView(id);
            if (view == null) {
                continue;
            }
            view.setBackgroundColor(getColor(colorRes));
        }
        return this;
    }

    public LxViewHolder setBgColorRes(@IdRes int resId, @ColorRes int colorRes) {
        return setBgColorRes(all(resId), colorRes);
    }


    //////////////////////////////  -- TextView 文本颜色 --  //////////////////////////////

    public LxViewHolder setTextColor(Ids ids, int color) {
        for (int id : ids.ids()) {
            View view = getView(id);
            if (view == null || !(view instanceof TextView)) {
                continue;
            }
            ((TextView) view).setTextColor(color);
        }
        return this;
    }

    public LxViewHolder setTextColor(@IdRes int resId, int color) {
        return setTextColor(all(resId), color);
    }

    public LxViewHolder setTextColorRes(@IdRes int resId, @ColorRes int colorRes) {
        return setTextColor(all(resId), getColor(colorRes));
    }

    public LxViewHolder setTextColorRes(Ids ids, @ColorRes int colorRes) {
        return setTextColor(ids, getColor(colorRes));
    }

    //////////////////////////////  -- TextView 文本 --  //////////////////////////////

    public LxViewHolder setText(@IdRes int resId, CharSequence txt, boolean goneIfEmpty) {
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

    public LxViewHolder setText(@IdRes int resId, CharSequence txt) {
        if (txt == null) {
            return this;
        }
        return setText(resId, txt, false);
    }

    public LxViewHolder setTextRes(@IdRes int resId, @StringRes int txtRes) {
        if (txtRes == 0) {
            return this;
        }
        String txt = itemView.getContext().getResources().getString(txtRes);
        return setText(resId, txt, false);
    }

    //////////////////////////////  -- ImageView --  //////////////////////////////

    public LxViewHolder setImage(@IdRes int resId, Drawable imgResId) {
        ImageView iv = getView(resId);
        if (iv == null) {
            return this;
        }
        iv.setImageDrawable(imgResId);
        return this;
    }


    public LxViewHolder setImage(@IdRes int resId, @DrawableRes int imgResId) {
        ImageView iv = getView(resId);
        if (iv == null) {
            return this;
        }
        iv.setImageResource(imgResId);
        return this;
    }

    public LxViewHolder setImage(@IdRes int resId, Bitmap bitmap) {
        ImageView iv = getView(resId);
        if (iv == null) {
            return this;
        }
        iv.setImageBitmap(bitmap);
        return this;
    }

    public LxViewHolder setImage(@IdRes int resId, String url, Object extra) {
        ImageView iv = getView(resId);
        if (iv == null) {
            return this;
        }
        LxGlobal.ImgUrlLoader loader = LxGlobal.imgUrlLoader;
        if (loader != null) {
            loader.load(iv, url, extra);
        }
        return this;
    }

    public LxViewHolder setImage(@IdRes int resId, String url) {
        setImage(resId, url, null);
        return this;
    }

    public LxViewHolder setElevation(@IdRes int resId, int sizeInPx) {
        View view = getView(resId);
        ViewCompat.setElevation(view, sizeInPx);
        return this;
    }


    //////////////////////////////  -- View.Event Click --  //////////////////////////////


    public LxViewHolder linkClick(@IdRes int sourceId, @IdRes int targetId) {
        setClick(sourceId, v -> {
            View view = getView(targetId);
            if (view != null) {
                view.performClick();
            }
        });
        return this;
    }

    public LxViewHolder linkClick(@IdRes int sourceId) {
        setClick(sourceId, v -> {
            itemView.performClick();
        });
        return this;
    }

    /**
     * 设置 View 点击监听
     *
     * @param ids view id 数组
     * @return holder
     */
    public LxViewHolder setClick(int... ids) {
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
    public LxViewHolder setClick(Ids ids, View.OnClickListener listener) {
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
    public LxViewHolder setClick(int resId, View.OnClickListener listener) {
        return setClick(all(resId), listener);
    }

    /**
     * 给 ItemView 设置监听
     *
     * @param listener 监听事件
     * @return holder
     */
    public LxViewHolder setClick(View.OnClickListener listener) {
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
    public LxViewHolder setLongClick(int... ids) {
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
    public LxViewHolder setLongClick(Ids ids, View.OnLongClickListener listener) {
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
    public LxViewHolder setLongClick(int resId, View.OnLongClickListener listener) {
        return setLongClick(all(resId), listener);
    }

    public LxViewHolder setLongClick(View.OnLongClickListener listener) {
        itemView.setLongClickable(true);
        itemView.setOnLongClickListener(listener);
        return this;
    }


    //////////////////////////////  -- View LayoutParams --  //////////////////////////////

    public LxViewHolder setLayoutParams(Ids ids, int width, int height) {
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

    public LxViewHolder setLayoutParams(int resId, int width, int height) {
        return setLayoutParams(all(resId), width, height);
    }

    public LxViewHolder setLayoutParams(int width, int height) {
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

    //////////////////////////////  -- drag & swipe --  //////////////////////////////

    public LxViewHolder dragOnTouch(LxAdapter adapter, int... ids) {
        LxDragSwipeComponent component = adapter.getComponent(LxDragSwipeComponent.class);
        if (component == null) {
            return this;
        }
        if (ids.length == 0) {
            component.dragOnTouch(itemView, this);
            return this;
        }
        for (int id : ids) {
            View view = getView(id);
            if (view != null) {
                component.dragOnTouch(view, this);
            }
        }
        return this;
    }

    public LxViewHolder dragOnLongPress(LxAdapter adapter, int... ids) {
        LxDragSwipeComponent component = adapter.getComponent(LxDragSwipeComponent.class);
        if (component == null) {
            return this;
        }
        if (ids.length == 0) {
            component.dragOnLongPress(itemView, this);
            return this;
        }
        for (int id : ids) {
            View view = getView(id);
            if (view != null) {
                component.dragOnLongPress(view, this);
            }
        }
        return this;
    }

    public LxViewHolder swipeOnTouch(LxAdapter adapter, int... ids) {
        LxDragSwipeComponent component = adapter.getComponent(LxDragSwipeComponent.class);
        if (component == null) {
            return this;
        }
        if (ids.length == 0) {
            component.swipeOnTouch(itemView, this);
            return this;
        }
        for (int id : ids) {
            View view = getView(id);
            if (view != null) {
                component.swipeOnTouch(view, this);
            }
        }
        return this;
    }

    public LxViewHolder swipeOnLongPress(LxAdapter adapter, int... ids) {
        LxDragSwipeComponent component = adapter.getComponent(LxDragSwipeComponent.class);
        if (component == null) {
            return this;
        }
        if (ids.length == 0) {
            component.swipeOnLongPress(itemView, this);
            return this;
        }
        for (int id : ids) {
            View view = getView(id);
            if (view != null) {
                component.swipeOnLongPress(view, this);
            }
        }
        return this;
    }


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
