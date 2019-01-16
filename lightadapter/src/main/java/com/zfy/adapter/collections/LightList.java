package com.zfy.adapter.collections;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntRange;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.util.ListUpdateCallback;

import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.data.Diffable;
import com.zfy.adapter.function.LightConsumer;
import com.zfy.adapter.function.LightPredicate;

import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
    class LightAdapterUpdateCallback implements ListUpdateCallback {

        @Override
        public void onInserted(int position, int count) {
            LightAdapter adapter;
            Iterator<WeakReference<LightAdapter>> iterator = mAdapters.iterator();
            while (iterator.hasNext()) {
                adapter = iterator.next().get();
                if (adapter != null) {
                    adapter.notifyItem().insert(adapter.toLayoutIndex(position), count);
                    // adapter.notifyItemRangeInserted(adapter.toLayoutIndex(position), count);
                } else {
                    iterator.remove();
                }
            }
        }

        @Override
        public void onRemoved(int position, int count) {
            LightAdapter adapter;
            Iterator<WeakReference<LightAdapter>> iterator = mAdapters.iterator();
            while (iterator.hasNext()) {
                adapter = iterator.next().get();
                if (adapter != null) {
                    adapter.notifyItem().remove(adapter.toLayoutIndex(position), count);
                    // adapter.notifyItemRangeRemoved(adapter.toLayoutIndex(position), count);
                } else {
                    iterator.remove();
                }
            }
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            LightAdapter adapter;
            Iterator<WeakReference<LightAdapter>> iterator = mAdapters.iterator();
            while (iterator.hasNext()) {
                adapter = iterator.next().get();
                if (adapter != null) {
                    adapter.notifyItem().move(adapter.toLayoutIndex(fromPosition), adapter.toLayoutIndex(toPosition));
                    // adapter.notifyItemMoved(adapter.toLayoutIndex(fromPosition), adapter.toLayoutIndex(toPosition));
                } else {
                    iterator.remove();
                }
            }
        }

        @Override
        public void onChanged(int position, int count, Object payload) {
            LightAdapter adapter;
            Iterator<WeakReference<LightAdapter>> iterator = mAdapters.iterator();
            while (iterator.hasNext()) {
                adapter = iterator.next().get();
                if (adapter != null) {
                    adapter.notifyItem().change(adapter.toLayoutIndex(position), count, payload);
                    // adapter.notifyItemRangeChanged(adapter.toLayoutIndex(position), count, payload);
                } else {
                    iterator.remove();
                }
            }
        }
    }

    private LightAdapterUpdateCallback        mCallback;
    private List<WeakReference<LightAdapter>> mAdapters;

    LightList() {
        mAdapters = new ArrayList<>();
        mCallback = new LightAdapterUpdateCallback();
    }

    public void register(LightAdapter adapter) {
        unregister(adapter);
        mAdapters.add(new WeakReference<>(adapter));
    }

    public void unregister(LightAdapter adapter) {
        LightAdapter item;
        Iterator<WeakReference<LightAdapter>> iterator = mAdapters.iterator();
        while (iterator.hasNext()) {
            item = iterator.next().get();
            if (item.equals(adapter)) {
                iterator.remove();
            }
        }
    }

    public LightAdapterUpdateCallback getCallback() {
        return mCallback;
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
    public boolean add(T t) {
        return getList().add(t);
    }

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

    @Override
    public Iterator<T> iterator() {
        return getList().iterator();
    }

    /**
     * 更新为新的数据
     *
     * @param newItems 新的数据源
     */
    @MainThread
    public abstract void update(@NonNull List<T> newItems);


    /**
     * 获取数据快照
     *
     * @return 快照
     */
    public List<T> snapshot() {
        return new ArrayList<>(getList());
    }

    /**
     * 更爱你清空列表
     */
    @MainThread
    public void updateClear() {
        List<T> snapshot = snapshot();
        snapshot.clear();
        update(snapshot);
    }

    /**
     * 在原有数据基础上面追加数据
     *
     * @param newItems 新的数据源
     * @see List#addAll(Collection)
     * @return 添加是否成功
     */
    @MainThread
    public boolean updateAddAll(@NonNull List<T> newItems) {
        List<T> snapshot = snapshot();
        boolean result = snapshot.addAll(newItems);
        if (result) {
            update(snapshot);
        }
        return result;
    }

    /**
     * 在原有数据基础上面追加数据
     *
     * @param newItem 新的单个数据源
     * @see List#add(Object)
     * @return 添加是否成功
     */
    @MainThread
    public boolean updateAdd(@NonNull T newItem) {
        List<T> snapshot = snapshot();
        boolean result = snapshot.add(newItem);
        if (result) {
            update(snapshot);
        }
        return result;
    }

    /**
     * 在原有数据基础上面追加数据
     *
     * @param newItems 新的数据源
     * @param index 开始索引
     * @see List#addAll(int, Collection)
     * @return 添加是否成功
     */
    @MainThread
    public boolean updateAddAll(@IntRange(from = 0) int index, @NonNull List<T> newItems) {
        List<T> snapshot = snapshot();
        boolean result = snapshot.addAll(index, newItems);
        if (result) {
            update(snapshot);
        }
        return result;
    }

    /**
     * 在原有数据基础上面追加数据
     *
     * @param newItem 新的单个数据源
     * @see List#add(int, Object)
     * @param index 开始索引
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
     * @return 删除的那个元素
     */
    @MainThread
    public T updateRemove(@IntRange(from = 0) int index) {
        List<T> snapshot = snapshot();
        T remove = snapshot.remove(index);
        if (remove != null) {
            update(snapshot);
        }
        return remove;
    }

    /**
     * 删除满足条件的元素
     *
     * @param removeCount  删除的个数
     * @param fromEnd      从列表尾部开始删除？
     * @param shouldRemove 是否应该删除的条件
     * @return 删除了多少个元素
     */
    @MainThread
    public int updateRemove(int removeCount, boolean fromEnd, LightPredicate<T> shouldRemove) {
        List<T> snapshot = snapshot();
        int count = 0;
        if (fromEnd) {
            ListIterator<T> iterator = snapshot.listIterator(snapshot.size() - 1);
            T previous;
            while (iterator.hasPrevious()) {
                if (removeCount >= 0 && count >= removeCount) {
                    break;
                }
                previous = iterator.previous();
                if (previous != null && shouldRemove.test(previous)) {
                    iterator.remove();
                    count++;
                }
            }
        } else {
            Iterator<T> iterator = snapshot.iterator();
            T next;
            while (iterator.hasNext()) {
                if (removeCount >= 0 && count >= removeCount) {
                    break;
                }
                next = iterator.next();
                if (next != null && shouldRemove.test(next)) {
                    iterator.remove();
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 从头开始，删除全部满足条件的元素
     *
     * @param shouldRemove 是否应该删除
     * @return 删除元素的个数
     * @see LightList#updateRemove(int, boolean, LightPredicate)
     */
    @MainThread
    public int updateRemove(LightPredicate<T> shouldRemove) {
        return updateRemove(-1, false, shouldRemove);

    }

    /**
     * 删除指定位置的数据
     *
     * @param item 数据
     * @see List#remove(Object)
     * @return 是否删除了元素
     */
    @MainThread
    public boolean updateRemove(@NonNull T item) {
        List<T> snapshot = snapshot();
        boolean remove = snapshot.remove(item);
        if (remove) {
            update(snapshot);
        }
        return remove;
    }


    /**
     * 更新某个位置的数据
     *
     * @param index              开始索引
     * @param howToUpdateConsumer 如何更新数据
     * @see List#set(int, Object)
     * @return 更改的数据
     */
    @MainThread
    public T updateSet(@IntRange(from = 0) int index, @NonNull LightConsumer<T> howToUpdateConsumer) {
        List<T> snapshot = snapshot();
        return setItem(snapshot, index, howToUpdateConsumer);
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

    @MainThread
    public void updateForEach(@NonNull LightConsumer<T> howToUpdateConsumer) {
        List<T> ts = foreach(item -> true, howToUpdateConsumer);
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
    private T setItem(List<T> list, int pos, LightConsumer<T> consumer) {
        T item = list.get(pos);
        T copy = copy(item);
        consumer.accept(copy);
        return list.set(pos, copy);
    }

    // 使用 Parcelable 复制一份新的数据
    @SuppressWarnings("unchecked")
    private T copy(T input) {
        if (!(input instanceof Parcelable)) {
            throw new IllegalStateException("model should impl Parcelable to use set operate;");
        }
        Parcelable parcelable = (Parcelable) input;
        Parcel parcel = null;
        try {
            parcel = Parcel.obtain();
            parcel.writeParcelable(parcelable, 0);
            parcel.setDataPosition(0);
            Parcelable copy = parcel.readParcelable(input.getClass().getClassLoader());
            return (T) copy;
        } finally {
            if (parcel != null) {
                parcel.recycle();
            }
        }
    }
}
