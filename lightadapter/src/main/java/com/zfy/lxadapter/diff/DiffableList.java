package com.zfy.lxadapter.diff;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.zfy.lxadapter.data.Diffable;
import com.zfy.lxadapter.function._Consumer;
import com.zfy.lxadapter.function._Predicate;
import com.zfy.lxadapter.helper.LxUtil;

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
public class DiffableList<E extends Diffable<E>> extends AbstractList<E> {

    public static final int FLAG_NORMAL       = -1;
    public static final int FLAG_INTERNAL     = 0;
    public static final int FLAG_ONLY_CONTENT = 1;


    public interface ListUpdateObserver<E> {
        void onChange(List<E> list);
    }

    private final List<ListUpdateObserver<E>> updateObservers;
    private final AdapterUpdateCallback       updateCallback;
    protected     IDiffDispatcher<E>          dispatcher;

    public DiffableList(boolean async) {
        updateCallback = new AdapterUpdateCallback();
        updateObservers = new ArrayList<>();
        dispatcher = async
                ? new AsyncDiffDispatcher<>(updateCallback)
                : new SyncDiffDispatcher<>(updateCallback);
    }

    public DiffableList() {
        this(false);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        updateCallback.setAdapter(adapter);
    }

    public void addUpdateObserver(ListUpdateObserver<E> updateObserver) {
        this.updateObservers.add(updateObserver);
    }

    public void update(@NonNull List<E> newItems) {
        this.update(newItems, FLAG_NORMAL);
    }

    public void update(@NonNull List<E> newItems, int flag) {
        dispatcher.update(newItems);
    }


    public List<E> list() {
        return dispatcher.list();
    }

    /******************************************读方法*********************************************/

    @Override
    public E get(int i) {
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
    public List<E> subList(@IntRange(from = 0) int fromIndex, @IntRange(from = 0) int toIndex) {
        return list().subList(fromIndex, toIndex);
    }


    /******************************************写方法*********************************************/

    @Override
    public boolean add(E t) {
        return list().add(t);
    }

    @Override
    public E set(@IntRange(from = 0) int index, @NonNull E element) {
        return list().set(index, element);
    }

    @Override
    public boolean remove(@NonNull Object o) {
        return list().remove(o);
    }

    @Override
    public E remove(@IntRange(from = 0) int index) {
        return list().remove(index);
    }

    @Override
    public int hashCode() {
        return list().hashCode();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return list().addAll(c);
    }

    @Override
    public void clear() {
        list().clear();
    }

    @NonNull
    @Override
    public ListIterator<E> listIterator(final int index) {
        return list().listIterator(index);
    }

    @Override
    public Iterator<E> iterator() {
        return list().iterator();
    }


    public List<E> updateDataSetChanged(List<E> newItems) {
        dispatchUpdate(newItems);
        updateCallback.adapter.notifyDataSetChanged();
        return newItems;
    }


    // 发布数据更新
    private void dispatchUpdate(@NonNull List<E> newItems) {
        update(newItems);
        for (ListUpdateObserver<E> updateObserver : updateObservers) {
            updateObserver.onChange(newItems);
        }
    }

    /**
     * 获取数据快照
     *
     * @return 快照
     */
    public List<E> snapshot() {
        return new ArrayList<>(list());
    }


    /**
     * 清空列表
     *
     * @return 列表
     */
    public List<E> updateClear() {
        List<E> snapshot = snapshot();
        snapshot.clear();
        dispatchUpdate(snapshot);
        return snapshot;
    }

    /**
     * 在原有数据基础上面追加数据
     *
     * @param newItems 新的数据源
     * @return 添加是否成功
     * @see List#addAll(Collection)
     */
    public boolean updateAddAll(@NonNull List<E> newItems) {
        List<E> snapshot = snapshot();
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
    public boolean updateAdd(@NonNull E newItem) {
        List<E> snapshot = snapshot();
        boolean result = snapshot.add(newItem);
        if (result) {
            dispatchUpdate(snapshot);
        }
        return result;
    }

    /**
     * 在原有数据基础上面追加数据
     *
     * @param newItem 新的单个数据源
     * @see List#add(int, Object)
     */
    public void updateAddLast(@NonNull E newItem) {
        updateAdd(size(), newItem);
    }

    /**
     * 在原有数据基础上面追加数据
     *
     * @param index    下标
     * @param newItems 新的数据源
     * @return 添加是否成功
     * @see List#addAll(int, Collection)
     */
    public boolean updateAddAll(@IntRange(from = 0) int index, @NonNull List<E> newItems) {
        List<E> snapshot = snapshot();
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
    public void updateAdd(@IntRange(from = 0) int index, @NonNull E newItem) {
        List<E> snapshot = snapshot();
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
    public E updateRemove(@IntRange(from = 0) int index) {
        List<E> snapshot = snapshot();
        E remove = snapshot.remove(index);
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
    public int updateRemove(int removeCount, boolean fromEnd, _Predicate<E> shouldRemove) {
        List<E> snapshot = snapshot();
        int count = 0;
        if (fromEnd) {
            ListIterator<E> iterator = snapshot.listIterator(snapshot.size() - 1);
            E previous;
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
            Iterator<E> iterator = snapshot.iterator();
            E next;
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
     * @see DiffableList#updateRemove(int, boolean, _Predicate)
     */
    public int updateRemove(_Predicate<E> shouldRemove) {
        return updateRemove(-1, false, shouldRemove);

    }

    /**
     * 删除指定位置的数据
     *
     * @param item 数据
     * @return 是否删除了元素
     * @see List#remove(Object)
     */
    public boolean updateRemove(@NonNull E item) {
        List<E> snapshot = snapshot();
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
    public E updateSet(@IntRange(from = 0) int index, @NonNull _Consumer<E> howToUpdateConsumer) {
        List<E> snapshot = snapshot();
        E t = setItem(snapshot, index, howToUpdateConsumer);
        dispatchUpdate(snapshot);
        return t;
    }


    /**
     * 更新某个位置的数据
     *
     * @param model               对象
     * @param howToUpdateConsumer 如何更新数据
     * @return 设置的元素
     * @see List#set(int, Object)
     */
    public E updateSet(E model, @NonNull _Consumer<E> howToUpdateConsumer) {
        List<E> snapshot = snapshot();
        E t = setItem(snapshot, indexOf(model), howToUpdateConsumer);
        dispatchUpdate(snapshot);
        return t;
    }

    /**
     * 循环更新列表中满足条件的所有数据时
     *
     * @param shouldUpdate        返回是否需要更新这一项
     * @param howToUpdateConsumer 如何更新该数据
     */
    public void updateSet(@NonNull _Predicate<E> shouldUpdate, @NonNull _Consumer<E> howToUpdateConsumer) {
        List<E> ts = foreach(shouldUpdate, howToUpdateConsumer);
        dispatchUpdate(ts);
    }


    public void updateSet(@NonNull _Consumer<E> howToUpdateConsumer) {
        List<E> ts = foreach(item -> true, howToUpdateConsumer);
        dispatchUpdate(ts);
    }


    // 循环数据执行操作
    private List<E> foreach(_Predicate<E> needUpdate, _Consumer<E> consumer) {
        List<E> snapshot = snapshot();
        E t;
        for (int i = 0; i < snapshot.size(); i++) {
            t = snapshot.get(i);
            if (needUpdate.test(t)) {
                setItem(snapshot, i, consumer);
            }
        }
        return snapshot;
    }

    // 复制数据后实现 set(index, item) 功能
    private E setItem(List<E> list, int pos, _Consumer<E> consumer) {
        if (pos < 0 || pos > size()) {
            return null;
        }
        E item = list.get(pos);
        E copy = (E) LxUtil.copy(item);
        consumer.accept(copy);
        return list.set(pos, copy);
    }


}
