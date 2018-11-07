package com.zfy.adapter.collections;

import android.databinding.ListChangeRegistry;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;

import java.util.AbstractList;
import java.util.List;
import java.util.ListIterator;

/**
 * CreateAt : 2018/11/2
 * Describe :
 *
 * @author chendong
 */
public class AsyncLightDiffList<T> extends AbstractList<T> implements ObservableList<T> {

    private final AsyncListDiffer<T> differ;
    private final ListChangeRegistry listeners = new ListChangeRegistry();

    /**
     * Creates a new DiffObservableList of type T, which runs the diff on a background thread using
     * {@link AsyncListDiffer}.
     *
     * @param callback The callback that controls the behavior of the DiffObservableList.
     */
    public AsyncLightDiffList(@NonNull DiffUtil.ItemCallback<T> callback) {
        this(new AsyncDifferConfig.Builder<>(callback).build());
    }

    /**
     * Creates a new AsyncLightDiffList of type T, which runs the diff on a background thread using
     * {@link AsyncListDiffer}.
     *
     * @param config The config passed to {@code AsyncListDiffer}.
     */
    public AsyncLightDiffList(@NonNull AsyncDifferConfig<T> config) {
        differ = new AsyncListDiffer<>(new ObservableListUpdateCallback(), config);
    }

    /**
     * Updates the list to the given items. A diff will run in a background thread then this
     * collection will be updated.
     */
    public void update(@Nullable List<T> newItems) {
        differ.submitList(newItems);
    }

    @Override
    public void addOnListChangedCallback(@NonNull OnListChangedCallback<? extends ObservableList<T>> callback) {
        listeners.add(callback);
    }

    @Override
    public void removeOnListChangedCallback(@NonNull OnListChangedCallback<? extends ObservableList<T>> callback) {
        listeners.remove(callback);
    }

    @Override
    public T get(int index) {
        return differ.getCurrentList().get(index);
    }

    @Override
    public int size() {
        return differ.getCurrentList().size();
    }

    @Override
    public int indexOf(Object o) {
        return differ.getCurrentList().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return differ.getCurrentList().lastIndexOf(o);
    }

    @NonNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return differ.getCurrentList().subList(fromIndex, toIndex);
    }

    @Override
    public int hashCode() {
        return differ.getCurrentList().hashCode();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (!(o instanceof AsyncLightDiffList)) {
            return false;
        }
        return differ.getCurrentList().equals(((AsyncLightDiffList) o).differ.getCurrentList());
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(final int index) {
        return differ.getCurrentList().listIterator(index);
    }

    class ObservableListUpdateCallback implements ListUpdateCallback {

        @Override
        public void onChanged(int position, int count, Object payload) {
            listeners.notifyChanged(AsyncLightDiffList.this, position, count);
        }

        @Override
        public void onInserted(int position, int count) {
            listeners.notifyInserted(AsyncLightDiffList.this, position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            listeners.notifyRemoved(AsyncLightDiffList.this, position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            listeners.notifyMoved(AsyncLightDiffList.this, fromPosition, toPosition, 1);
        }
    }
}
