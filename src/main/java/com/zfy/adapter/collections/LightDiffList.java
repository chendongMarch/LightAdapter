package com.zfy.adapter.collections;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;

import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.able.Diffable;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * CreateAt : 2018/11/1
 * Describe : 使用 DiffUtil 更新数据
 *
 * @author chendong
 */
public class LightDiffList<T extends Diffable<T>> extends AbstractList<T> {

    private final Object LIST_LOCK = new Object();
    private List<T> list = Collections.emptyList();
    private final boolean detectMoves;
    private final ObservableListUpdateCallback listCallback = new ObservableListUpdateCallback();
    private LightAdapter adapter;

    public LightDiffList() {
        this(true);
    }

    public LightDiffList(boolean detectMoves) {
        this.detectMoves = detectMoves;
    }

    public void setLightAdapter(LightAdapter lightAdapter) {
        adapter = lightAdapter;
    }

    /**
     * 根据给出的新集合计算 DiffResult
     *
     * @param newItems 新的集合
     * @return 返回 DiffResult
     */
    public DiffUtil.DiffResult calculateDiff(final List<T> newItems) {
        final ArrayList<T> frozenList;
        synchronized (LIST_LOCK) {
            frozenList = new ArrayList<>(list);
        }
        return doCalculateDiff(frozenList, newItems);
    }

    // 计算 DiffResult
    private DiffUtil.DiffResult doCalculateDiff(final List<T> oldItems, final List<T> newItems) {
        return DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldItems.size();
            }

            @Override
            public int getNewListSize() {
                return newItems != null ? newItems.size() : 0;
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                T oldItem = oldItems.get(oldItemPosition);
                T newItem = newItems.get(newItemPosition);
                return oldItem.areItemsTheSame(newItem);
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                T oldItem = oldItems.get(oldItemPosition);
                T newItem = newItems.get(newItemPosition);
                return oldItem.areContentsTheSame(newItem);
            }

            @Nullable
            @Override
            public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                T oldItem = oldItems.get(oldItemPosition);
                T newItem = newItems.get(newItemPosition);
                return oldItem.getChangePayload(newItem);
            }

        }, detectMoves);
    }

    private void dispatchUpdatesTo(DiffUtil.DiffResult diffResult) {
        diffResult.dispatchUpdatesTo(listCallback);
    }

    public List<T> snapshot() {
        return new ArrayList<>(list);
    }

    /**
     * 在原有数据基础上面追加数据
     *
     * @param newItems 新的数据源
     */
    @MainThread
    public void append(List<T> newItems) {
        List<T> ts = new ArrayList<>();
        ts.addAll(list);
        ts.addAll(newItems);
        update(ts);
    }


    /**
     * 更新为新的数据
     *
     * @param newItems 新的数据源
     */
    @MainThread
    public void update(List<T> newItems) {
        DiffUtil.DiffResult diffResult = doCalculateDiff(list, newItems);
        list = newItems;
        dispatchUpdatesTo(diffResult);
    }

    /**
     * 循环检测数据更新
     *
     * @param shouldUpdate 返回是否需要更新这一项
     * @param howToUpdate  如何更新该数据
     */
    @MainThread
    public void update(Predicate<T> shouldUpdate, Consumer<T> howToUpdate) {
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
    public void update(int pos, Consumer<T> howToUpdate) {
        List<T> ts = snapshot();
        setItem(ts, pos, howToUpdate);
        update(ts);
    }

    // 循环数据执行操作
    @TargetApi(Build.VERSION_CODES.N)
    private List<T> foreach(Predicate<T> needUpdate, Consumer<T> consumer) {
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
    private void setItem(List<T> items, int pos, Consumer<T> consumer) {
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

    @Override
    public T get(int i) {
        return list.get(i);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public T set(int index, T element) {
        return list.set(index, element);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public T remove(int index) {
        return list.remove(index);
    }

    class ObservableListUpdateCallback implements ListUpdateCallback {

        @Override
        public void onInserted(int position, int count) {
            if (adapter != null) {
                adapter.notifyItem().insert(adapter.toLayoutIndex(position), count);
//                adapter.notifyItemRangeInserted(adapter.toLayoutIndex(position), count);
            }
            modCount += 1;
        }

        @Override
        public void onRemoved(int position, int count) {
            if (adapter != null) {
                adapter.notifyItem().remove(adapter.toLayoutIndex(position), count);
    //                adapter.notifyItemRangeRemoved(adapter.toLayoutIndex(position), count);
            }
            modCount += 1;
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            if (adapter != null) {
                adapter.notifyItem().move(adapter.toLayoutIndex(fromPosition), adapter.toLayoutIndex(toPosition));
//                adapter.notifyItemMoved(adapter.toLayoutIndex(fromPosition), adapter.toLayoutIndex(toPosition));
            }
        }

        @Override
        public void onChanged(int position, int count, Object payload) {
            if (adapter != null) {
                adapter.notifyItem().change(adapter.toLayoutIndex(position), count, payload);
//                adapter.notifyItemRangeChanged(adapter.toLayoutIndex(position), count, payload);
            }
        }
    }
}
