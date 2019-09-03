package com.zfy.adapter.list;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.zfy.adapter.data.Diffable;
import com.zfy.adapter.function.LxUtil;

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
public abstract class LxList<T extends Diffable<T>> extends AbstractList<T> {

    public interface ListUpdateObserver<T> {
        void onChange(List<T> list);
    }

    private   List<ListUpdateObserver<T>> updateObservers;
    protected AdapterUpdateCallback       updateCallback;

    public LxList() {
        updateCallback = new AdapterUpdateCallback();
        updateObservers = new ArrayList<>();
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        updateCallback.setAdapter(adapter);
    }

    public void addUpdateObserver(ListUpdateObserver<T> updateObserver) {
        this.updateObservers.add(updateObserver);
    }

    /**
     * 获取内部真正的 list
     *
     * @return List
     */
    public abstract List<T> list();



    /******************************************读方法*********************************************/

    @Override
    public T get(int i) {
        return list().get(i);
    }

    @Override
    public int size() {
        return list().size();
    }

    @Override
    public int indexOf(@NonNull Object o) {
        return list().indexOf(o);
    }

    @Override
    public int lastIndexOf(@NonNull Object o) {
        return list().lastIndexOf(o);
    }

    @NonNull
    @Override
    public List<T> subList(@IntRange(from = 0) int fromIndex, @IntRange(from = 0) int toIndex) {
        return list().subList(fromIndex, toIndex);
    }


    /******************************************写方法*********************************************/

    @Override
    public boolean add(T t) {
        return list().add(t);
    }

    @Override
    public T set(@IntRange(from = 0) int index, @NonNull T element) {
        return list().set(index, element);
    }

    @Override
    public boolean remove(@NonNull Object o) {
        return list().remove(o);
    }

    @Override
    public T remove(@IntRange(from = 0) int index) {
        return list().remove(index);
    }

    @Override
    public int hashCode() {
        return list().hashCode();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return list().addAll(c);
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(final int index) {
        return list().listIterator(index);
    }

    @Override
    public Iterator<T> iterator() {
        return list().iterator();
    }


    // 发布数据更新
    private void dispatchUpdate(@NonNull List<T> newItems) {
        update(newItems);
        for (ListUpdateObserver<T> updateObserver : updateObservers) {
            updateObserver.onChange(newItems);
        }
    }

    /**
     * 更新为新的数据
     *
     * @param newItems 新的数据源
     */
    public abstract void update(@NonNull List<T> newItems);


    public List<T> filter(_Predicate<T> test) {
        List<T> l = new ArrayList<>();
        for (T t : this) {
            if (test.test(t)) {
                l.add(t);
            }
        }
        return l;
    }

    public <R> List<R> filterTo(_Predicate<T> test, _Function<T, R> function) {
        List<R> l = new ArrayList<>();
        for (T t : this) {
            if (test.test(t)) {
                l.add(function.map(t));
            }
        }
        return l;
    }

    /**
     * 获取数据快照
     *
     * @return 快照
     */
    public List<T> snapshot() {
        return new ArrayList<>(list());
    }

    /**
     * 清空列表
     */
    public void updateClear() {
        List<T> snapshot = snapshot();
        snapshot.clear();
        dispatchUpdate(snapshot);
    }

    /**
     * 在原有数据基础上面追加数据
     *
     * @param newItems 新的数据源
     * @return 添加是否成功
     * @see List#addAll(Collection)
     */
    public boolean updateAddAll(@NonNull List<T> newItems) {
        List<T> snapshot = snapshot();
        boolean result = snapshot.addAll(newItems);
        if (result) {
            dispatchUpdate(snapshot);
        }
        return result;
    }

    /**
     * 在原有数据基础上面追加数据
     *
     * @param newItem 新的单个数据源
     * @return 添加是否成功
     * @see List#add(Object)
     */
    public boolean updateAdd(@NonNull T newItem) {
        List<T> snapshot = snapshot();
        boolean result = snapshot.add(newItem);
        if (result) {
            dispatchUpdate(snapshot);
        }
        return result;
    }


    /**
     * 在原有数据基础上面追加数据
     *
     * @param index    下标
     * @param newItems 新的数据源
     * @return 添加是否成功
     * @see List#addAll(int, Collection)
     */
    public boolean updateAddAll(@IntRange(from = 0) int index, @NonNull List<T> newItems) {
        List<T> snapshot = snapshot();
        boolean result = snapshot.addAll(index, newItems);
        if (result) {
            dispatchUpdate(snapshot);
        }
        return result;
    }

    /**
     * 在原有数据基础上面追加数据
     *
     * @param index   下标
     * @param newItem 新的单个数据源
     * @see List#add(int, Object)
     */
    public void updateAdd(@IntRange(from = 0) int index, @NonNull T newItem) {
        List<T> snapshot = snapshot();
        snapshot.add(index, newItem);
        dispatchUpdate(snapshot);
    }


    /**
     * 删除指定位置的数据
     *
     * @param index 下标
     * @return 删除的那个元素
     * @see List#remove(int)
     */
    public T updateRemove(@IntRange(from = 0) int index) {
        List<T> snapshot = snapshot();
        T remove = snapshot.remove(index);
        if (remove != null) {
            dispatchUpdate(snapshot);
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
    public int updateRemove(int removeCount, boolean fromEnd, _Predicate<T> shouldRemove) {
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
        dispatchUpdate(snapshot);
        return count;
    }

    /**
     * 从头开始，删除全部满足条件的元素
     *
     * @param shouldRemove 是否应该删除
     * @return 删除元素的个数
     * @see LxList#updateRemove(int, boolean, _Predicate)
     */
    public int updateRemove(_Predicate<T> shouldRemove) {
        return updateRemove(-1, false, shouldRemove);

    }

    /**
     * 删除指定位置的数据
     *
     * @param item 数据
     * @return 是否删除了元素
     * @see List#remove(Object)
     */
    public boolean updateRemove(@NonNull T item) {
        List<T> snapshot = snapshot();
        boolean remove = snapshot.remove(item);
        if (remove) {
            dispatchUpdate(snapshot);
        }
        return remove;
    }


    /**
     * 更新某个位置的数据
     *
     * @param index               下标
     * @param howToUpdateConsumer 如何更新数据
     * @return 设置的元素
     * @see List#set(int, Object)
     */
    public T updateSet(@IntRange(from = 0) int index, @NonNull _Consumer<T> howToUpdateConsumer) {
        List<T> snapshot = snapshot();
        T t = setItem(snapshot, index, howToUpdateConsumer);
        dispatchUpdate(snapshot);
        return t;
    }


    /**
     * 循环更新列表中满足条件的所有数据时
     *
     * @param shouldUpdate        返回是否需要更新这一项
     * @param howToUpdateConsumer 如何更新该数据
     */
    public void updateSet(@NonNull _Predicate<T> shouldUpdate, @NonNull _Consumer<T> howToUpdateConsumer) {
        List<T> ts = foreach(shouldUpdate, howToUpdateConsumer);
        dispatchUpdate(ts);
    }


    public void updateSet(@NonNull _Consumer<T> howToUpdateConsumer) {
        List<T> ts = foreach(item -> true, howToUpdateConsumer);
        dispatchUpdate(ts);
    }


    // 循环数据执行操作
    private List<T> foreach(_Predicate<T> needUpdate, _Consumer<T> consumer) {
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
    private T setItem(List<T> list, int pos, _Consumer<T> consumer) {
        T item = list.get(pos);
        T copy = (T) LxUtil.copy(item);
        consumer.accept(copy);
        return list.set(pos, copy);
    }

}
