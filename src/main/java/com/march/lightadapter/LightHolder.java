package com.march.lightadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.march.lightadapter.binder.ViewBinder;

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

    private SparseArray<View> cacheViews;

    public LightHolder(Context context, final View itemView) {
        super(itemView);
        this.cacheViews = new SparseArray<>(5);
    }

    public View getItemView() {
        return itemView;
    }

    public ViewBinder bind() {
        return ViewBinder.from(this);
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

}
