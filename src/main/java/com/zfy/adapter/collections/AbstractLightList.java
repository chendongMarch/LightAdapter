package com.zfy.adapter.collections;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.util.ListUpdateCallback;

import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.able.Diffable;
import com.zfy.adapter.function.LightConsumer;
import com.zfy.adapter.function.LightPredicate;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * CreateAt : 2018/11/8
 * Describe :
 *
 * @author chendong
 */
public abstract class AbstractLightList<T extends Diffable<T>> extends AbstractList<T> {

    /**
     * 发送 DiffResult 到 LightAdapter 更新
     */
    static class LightAdapterUpdateCallback implements ListUpdateCallback {

        private LightAdapter adapter;

        public void setAdapter(LightAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onInserted(int position, int count) {
            if (adapter != null) {
                adapter.notifyItem().insert(adapter.toLayoutIndex(position), count);
                // adapter.notifyItemRangeInserted(adapter.toLayoutIndex(position), count);
            }
        }

        @Override
        public void onRemoved(int position, int count) {
            if (adapter != null) {
                adapter.notifyItem().remove(adapter.toLayoutIndex(position), count);
                // adapter.notifyItemRangeRemoved(adapter.toLayoutIndex(position), count);
            }
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            if (adapter != null) {
                adapter.notifyItem().move(adapter.toLayoutIndex(fromPosition), adapter.toLayoutIndex(toPosition));
                // adapter.notifyItemMoved(adapter.toLayoutIndex(fromPosition), adapter.toLayoutIndex(toPosition));
            }
        }

        @Override
        public void onChanged(int position, int count, Object payload) {
            if (adapter != null) {
                adapter.notifyItem().change(adapter.toLayoutIndex(position), count, payload);
                // adapter.notifyItemRangeChanged(adapter.toLayoutIndex(position), count, payload);
            }
        }
    }

    protected LightAdapter mAdapter;
    protected LightAdapterUpdateCallback mCallback;


    public void setAdapter(LightAdapter adapter) {
        mAdapter = adapter;
        mCallback.setAdapter(mAdapter);
    }

    /**
     * 获取内部真正的 list
     *
     * @return List
     */
    public abstract List<T> getList();


    @Override
    public T get(int i) {
        return getList().get(i);
    }

    @Override
    public int size() {
        return getList().size();
    }

    @Override
    public T set(int index, T element) {
        return getList().set(index, element);
    }

    @Override
    public boolean remove(Object o) {
        return getList().remove(o);
    }

    @Override
    public T remove(int index) {
        return getList().remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return getList().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return getList().lastIndexOf(o);
    }

    @NonNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return getList().subList(fromIndex, toIndex);
    }

    @Override
    public int hashCode() {
        return getList().hashCode();
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(final int index) {
        return getList().listIterator(index);
    }

    /**
     * 获取数据快照
     *
     * @return 快照
     */
    public List<T> snapshot() {
        return new ArrayList<>(getList());
    }

    /**
     * 更新为新的数据
     *
     * @param newItems 新的数据源
     */
    @MainThread
    public abstract void update(List<T> newItems);


    /**
     * 在原有数据基础上面追加数据
     *
     * @param newItems 新的数据源
     */
    @MainThread
    public void append(List<T> newItems) {
        List<T> newList = new ArrayList<>();
        newList.addAll(getList());
        newList.addAll(newItems);
        update(newList);
    }


    /**
     * 循环检测数据更新
     *
     * @param shouldUpdate 返回是否需要更新这一项
     * @param howToUpdate  如何更新该数据
     */
    @MainThread
    public void update(LightPredicate<T> shouldUpdate, LightConsumer<T> howToUpdate) {
        List<T> ts = foreach(shouldUpdate, howToUpdate);
        update(ts);
    }

    /**
     * 更新某一项指定的
     *
     * @param pos         位置
     * @param howToUpdate 如何更新该数据
     */
    @MainThread
    public void update(int pos, LightConsumer<T> howToUpdate) {
        List<T> ts = snapshot();
        setItem(ts, pos, howToUpdate);
        update(ts);
    }


    // 循环数据执行操作
    private List<T> foreach(LightPredicate<T> needUpdate, LightConsumer<T> consumer) {
        List<T> newItems = snapshot();
        T t;
        for (int i = 0; i < newItems.size(); i++) {
            t = newItems.get(i);
            if (needUpdate.test(t)) {
                setItem(newItems, i, consumer);
            }
        }
        return newItems;
    }

    // 设置某项
    private void setItem(List<T> items, int pos, LightConsumer<T> consumer) {
        T item = items.get(pos);
        Parcelable newItem = copy(((Parcelable) item));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            consumer.accept((T) newItem);
        }
        items.set(pos, (T) newItem);
    }

    // 复制新的数据
    private <P extends Parcelable> P copy(P input) {
        Parcel parcel = null;
        try {
            parcel = Parcel.obtain();
            parcel.writeParcelable(input, 0);
            parcel.setDataPosition(0);
            return parcel.readParcelable(input.getClass().getClassLoader());
        } finally {
            if (parcel != null) {
                parcel.recycle();
            }
        }
    }
}
