package com.march.lightadapter;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.march.lightadapter.helper.LightLogger;
import com.march.lightadapter.listener.OnItemListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * CreateAt : 2016/11/8
 * Describe : custom view holder
 *
 * @author chendong
 */
public class LightHolder<D> extends RecyclerView.ViewHolder {

    private static final String TAG = LightHolder.class.getSimpleName();

    public static final int UNSET = -0x123;

    private OnItemListener<D> itemListener;
    private SparseArray<View> cacheViews;

    private D                data;
    private int              viewType;
    private LightAdapter<D>  attachAdapter;


    public LightHolder(Context context, final View itemView) {
        super(itemView);
        this.cacheViews = new SparseArray<>(5);
        initItemEvent(context, itemView);
    }

    public LightHolder(Context context, final View itemView, int viewType) {
        super(itemView);
        this.cacheViews = new SparseArray<>(5);
        this.viewType = viewType;
        initItemEvent(context, itemView);
    }

    public View getItemView() {
        return itemView;
    }

    public void setData(D data) {
        this.data = data;
    }

    public void setAttachAdapter(LightAdapter<D> attachAdapter) {
        this.attachAdapter = attachAdapter;
    }


    /**
     * 初始化事件
     *
     * @param context  上下文
     * @param itemView parent view
     */
    private void initItemEvent(Context context, final View itemView) {
        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (itemListener != null && itemListener.isSupportDoubleClick()) {
                    itemListener.onClick(attachAdapter.calPositionInDatas(getAdapterPosition()), LightHolder.this, data);
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (itemListener != null && !itemListener.isSupportDoubleClick()) {
                    itemListener.onClick(attachAdapter.calPositionInDatas(getAdapterPosition()), LightHolder.this, data);
                }
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (itemListener != null) {
                    itemListener.onDoubleClick(attachAdapter.calPositionInDatas(getAdapterPosition()), LightHolder.this, data);
                }
                return super.onDoubleTap(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (itemListener != null) {
                    itemListener.onLongPress(attachAdapter.calPositionInDatas(getAdapterPosition()), LightHolder.this, data);
                }
            }
        };
        final GestureDetectorCompat gestureDetector = new GestureDetectorCompat(context, gestureListener);
        itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (itemListener != null && itemListener.isSupportDoubleClick()) {
                    gestureDetector.onTouchEvent(motionEvent);
                    return true;
                } else {
                    return false;
                }
            }
        });

        // 不支持双击的话还是用原来的，因为这样可以支持控件点击的背景变化
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemListener != null && !itemListener.isSupportDoubleClick()) {
                    itemListener.onClick(attachAdapter.calPositionInDatas(getAdapterPosition()), LightHolder.this, data);
                }
            }
        });
        // 不支持双击的话还是用原来的
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (itemListener != null && !itemListener.isSupportDoubleClick())
                    itemListener.onLongPress(attachAdapter.calPositionInDatas(getAdapterPosition()), LightHolder.this, data);
                return true;
            }
        });
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

    void setOnItemListener(OnItemListener<D> itemListener) {
        this.itemListener = itemListener;
    }

    public int getViewType() {
        return viewType;
    }

    public D getData() {
        return data;
    }


    ///////////////////////////////////////////////////////////////////////////
    // 简化View操作
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

    private LightHolder setVisibility(final int visibility, int... resIds) {
        forEachView(new OnEachViewListener<View>() {
            @Override
            public void onEachView(View view) {
                if (view.getVisibility() != visibility && checkVisibilityParam(visibility))
                    view.setVisibility(visibility);
            }
        }, resIds);
        return this;
    }

    public LightHolder visibility(int resId, int visibility) {
        setVisibility(visibility, resId);
        return this;
    }

    public LightHolder gone(int... resIds) {
        setVisibility(View.GONE, resIds);
        return this;
    }

    public LightHolder visible(int... resIds) {
        setVisibility(View.VISIBLE, resIds);
        return this;
    }

    public LightHolder inVisible(int... resIds) {
        setVisibility(View.INVISIBLE, resIds);
        return this;
    }

    public LightHolder visibleOrGone(final boolean isVisible, int... resIds) {
        forEachView(new OnEachViewListener<View>() {
            @Override
            public void onEachView(View view) {
                view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            }
        }, resIds);
        return this;
    }

    public LightHolder textColor(int resId, final int color) {
        TextView view = getView(resId);
        view.setTextColor(color);
        return this;
    }

    public LightHolder text(int resId, final CharSequence txt, final boolean goneWhenEmpty) {
        TextView view = getView(resId);
        if (goneWhenEmpty) {
            if (TextUtils.isEmpty(txt)) {
                view.setVisibility(View.GONE);
            }
        }
        view.setText(txt);
        if (view instanceof EditText)
            ((EditText) view).setSelection(view.getText().toString().trim().length());
        return this;
    }

    public LightHolder text(int resId, String txt) {
        text(resId, txt, false);
        return this;
    }

    public LightHolder text(int resId, int txtRes) {
        String txt = itemView.getContext().getResources().getString(txtRes);
        text(resId, txt, false);
        return this;
    }

    public LightHolder image(int resId, final int imgResId) {
        ImageView iv = getView(resId);
        iv.setImageResource(imgResId);
        return this;
    }


    public LightHolder selectAll(final boolean isSelect, int... resIds) {
        forEachView(new OnEachViewListener<View>() {
            @Override
            public void onEachView(View view) {
                view.setSelected(isSelect);
            }
        }, resIds);
        return this;
    }

    public LightHolder doSelect(int... resIds) {
        selectAll(true, resIds);
        return this;
    }

    public LightHolder unSelect(int... resIds) {
        selectAll(false, resIds);
        return this;
    }


    public LightHolder click(final View.OnClickListener listener, int... resIds) {
        if (listener == null) return this;
        forEachView(new OnEachViewListener<View>() {
            @Override
            public void onEachView(View view) {
                view.setOnClickListener(listener);
            }
        }, resIds);
        return this;
    }

    /**
     * 设置LayoutParams
     *
     * @param width  宽度 -1:MATCH_PARENT, -2:WRAP_CONTENT,-0x123:UNSET
     * @param height 宽度 -1:MATCH_PARENT, -2:WRAP_CONTENT,-0x123:UNSET
     * @param resIds id列表
     * @return this
     */
    public LightHolder layoutParams(final int width, final int height, int... resIds) {
        forEachView(new OnEachViewListener<View>() {
            @Override
            public void onEachView(View view) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if (width != UNSET && width > 0)
                    layoutParams.width = width;
                if (height != UNSET && height > 0)
                    layoutParams.height = height;
                view.setLayoutParams(layoutParams);
            }
        }, resIds);
        return this;
    }

    /**
     * 给父控件设置layout params
     *
     * @param width  宽度 -1:MATCH_PARENT, -2:WRAP_CONTENT,-0x123:UNSET
     * @param height 宽度 -1:MATCH_PARENT, -2:WRAP_CONTENT,-0x123:UNSET
     * @return this
     */
    public LightHolder layoutParams(int width, int height) {
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        if (width != UNSET && width > 0)
            layoutParams.width = width;
        if (height != UNSET && height > 0)
            layoutParams.height = height;
        itemView.setLayoutParams(layoutParams);
        return this;
    }


    interface OnEachViewListener<T> {
        void onEachView(T view);
    }

    private <T extends View> void forEachView(OnEachViewListener<T> forEach, int... resIds) {
        T view;
        for (int resId : resIds) {
            view = getView(resId);
            if (view != null && forEach != null) {
                forEach.onEachView(view);
            }
        }
    }

}
