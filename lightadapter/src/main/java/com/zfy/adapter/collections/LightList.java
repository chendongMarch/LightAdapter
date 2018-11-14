package com.zfy.adapter.collections;

import android.os.Parcel;
import android.support.annotation.IntRange;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.util.ListUpdateCallback;

import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.able.Diffable;
import com.zfy.adapter.function.LightConsumer;
import com.zfy.adapter.function.LightPredicate;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * CreateAt : 2018/11/8
 * Describe : 对外支持更新的 List
 *
 * @author chendong
 */
public abstract class LightList<T extends Diffable<T>> extends AbstractList<T> {

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

    protected LightAdapter               mAdapter;
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

    /******************************************读方法*********************************************/

    @Override
    public T get(int i) {
        return getList().get(i);
    }

    @Override
    public int size() {
        return getList().size();
    }

    @Override
    public int indexOf(@NonNull Object o) {
        return getList().indexOf(o);
    }

    @Override
    public int lastIndexOf(@NonNull Object o) {
        return getList().lastIndexOf(o);
    }

    @NonNull
    @Override
    public List<T> subList(@IntRange(from = 0) int fromIndex, @IntRange(from = 0) int toIndex) {
        return getList().subList(fromIndex, toIndex);
    }

    /******************************************写方法*********************************************/

    @Override
    public T set(@IntRange(from = 0) int index, @NonNull T element) {
        return getList().set(index, element);
    }

    @Override
    public boolean remove(@NonNull Object o) {
        return getList().remove(o);
    }

    @Override
    public T remove(@IntRange(from = 0) int index) {
        return getList().remove(index);
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
    public abstract void update(@NonNull List<T> newItems);


    /**
     * 在原有数据基础上面追加数据
     *
     * @param newItems 新的数据源
     * @see List#addAll(Collection)
     */
    @MainThread
    public void updateAddAll(@NonNull List<T> newItems) {
        List<T> snapshot = snapshot();
        snapshot.addAll(newItems);
        update(snapshot);
    }

    /**
     * 在原有数据基础上面追加数据
     *
     * @param newItem 新的单个数据源
     * @see List#add(Object)
     */
    @MainThread
    public void updateAdd(@NonNull T newItem) {
        List<T> snapshot = snapshot();
        snapshot.add(newItem);
        update(snapshot);
    }

    /**
     * 在原有数据基础上面追加数据
     *
     * @param newItems 新的数据源
     * @see List#addAll(int, Collection)
     */
    @MainThread
    public void updateAddAll(@IntRange(from = 0) int index, @NonNull List<T> newItems) {
        List<T> snapshot = snapshot();
        snapshot.addAll(index, newItems);
        update(snapshot);
    }

    /**
     * 在原有数据基础上面追加数据
     *
     * @param newItem 新的单个数据源
     * @see List#add(int, Object)
     */
    @MainThread
    public void updateAdd(@IntRange(from = 0) int index, @NonNull T newItem) {
        List<T> snapshot = snapshot();
        snapshot.add(index, newItem);
        update(snapshot);
    }


    /**
     * 删除指定位置的数据
     *
     * @param index 下标
     * @see List#remove(int)
     */
    @MainThread
    public void updateRemove(@IntRange(from = 0) int index) {
        List<T> snapshot = snapshot();
        if (snapshot.remove(index) != null) {
            update(snapshot);
        }
    }

    /**
     * 删除指定位置的数据
     *
     * @param item 数据
     * @see List#remove(Object)
     */
    @MainThread
    public void updateRemove(@NonNull T item) {
        List<T> snapshot = snapshot();
        if (snapshot.remove(item)) {
            update(snapshot);
        }
    }


    /**
     * 更新某个位置的数据
     *
     * @param index               下标
     * @param howToUpdateConsumer 如何更新数据
     * @see List#set(int, Object)
     */
    @MainThread
    public void updateSet(@IntRange(from = 0) int index, @NonNull LightConsumer<T> howToUpdateConsumer) {
        List<T> snapshot = snapshot();
        setItem(snapshot, index, howToUpdateConsumer);
    }

    /**
     * 循环更新列表中满足条件的所有数据时
     *
     * @param shouldUpdate        返回是否需要更新这一项
     * @param howToUpdateConsumer 如何更新该数据
     */
    @MainThread
    public void updateForEach(@NonNull LightPredicate<T> shouldUpdate, @NonNull LightConsumer<T> howToUpdateConsumer) {
        List<T> ts = foreach(shouldUpdate, howToUpdateConsumer);
        update(ts);
    }


    // 循环数据执行操作
    private List<T> foreach(LightPredicate<T> needUpdate, LightConsumer<T> consumer) {
        List<T> snapshot = snapshot();
        T t;
        for (int i = 0; i < snapshot.size(); i++) {
            t = snapshot.get(i);
            if (needUpdate.test(t)) {
                setItem(snapshot, i, consumer);
            }
        }
        return snapshot;
    }

    // 复制数据后实现 set(index, item) 功能
    private void setItem(List<T> list, int pos, LightConsumer<T> consumer) {
        T item = list.get(pos);
        T copy = copy(item);
        consumer.accept(copy);
        list.set(pos, copy);
    }

    // 使用 Parcelable 复制一份新的数据
    private T copy(T input) {
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
