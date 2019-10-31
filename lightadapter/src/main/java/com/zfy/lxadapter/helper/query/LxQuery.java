package com.zfy.lxadapter.helper.query;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.LxList;
import com.zfy.lxadapter.data.LxModel;
import com.zfy.lxadapter.function._Consumer;
import com.zfy.lxadapter.function._LoopPredicate;
import com.zfy.lxadapter.function._Predicate;
import com.zfy.lxadapter.helper.LxSource;
import com.zfy.lxadapter.helper.query.QueryExecutor;

import java.util.List;

/**
 * CreateAt : 2019-10-18
 * Describe : 一个类型属于一个 class,一个 class 可能有多个 type
 *
 * @author chendong
 */
public class LxQuery {

    private LxList        list;
    private QueryExecutor executor;

    public LxQuery(LxList list) {
        this.list = list;
        executor = new QueryExecutor(list);
    }

    /************ find ************/

    @Nullable
    public <E> E findOneById(Class<E> clazz, Object id) {
        return executor.findOneById(clazz, id);
    }

    @Nullable
    public <E> E findOne(Class<E> clazz, int type, _Predicate<E> test) {
        List<E> list = executor.findImpl(clazz, type, test, 1);
        return list.isEmpty() ? null : list.get(0);
    }

    @NonNull
    public <E> List<E> find(Class<E> clazz, int type, _Predicate<E> test) {
        return executor.findImpl(clazz, type, test, -1);
    }

    @NonNull
    public <E> List<E> find(Class<E> clazz, int type) {
        return executor.findImpl(clazz, type, m -> true, -1);
    }


    /************ set ************/

    public <E> void set(Class<E> clazz, int type, _Predicate<E> predicate, _Consumer<E> consumer) {
        executor.setImpl(clazz, type, predicate, consumer);
    }

    public <E> void set(Class<E> clazz, int type, _Consumer<E> consumer) {
        executor.setImpl(clazz, type, m -> true, consumer);
    }

    public <E> void setX(Class<E> clazz, int type, _LoopPredicate<E> predicate, _Consumer<E> consumer) {
        executor.setImplX(clazz, type, predicate, consumer);
    }

    public <E> void set(Class<E> clazz, int type, int index, _Consumer<E> consumer) {
        executor.setImpl(clazz, type, index, consumer);
    }

    public <E> void set(Class<E> clazz, int type, LxModel model, _Consumer<E> consumer) {
        executor.setImpl(clazz, type, model, consumer);
    }

    /************ remove ************/

    public <E> void remove(int type) {
        executor.removeImpl(null, type, m -> true);
    }

    public <E> void remove(Class<E> clazz, int type, _Predicate<E> predicate) {
        executor.removeImpl(clazz, type, predicate);
    }

    public <E> void removeX(Class<E> clazz, int type, _LoopPredicate<E> predicate) {
        executor.removeImplX(clazz, type, predicate);
    }

    /************ add ************/

    public boolean add(LxSource source) {
        return list.updateAddAll(source.asModels());
    }

    public boolean add(int index, LxSource source) {
        return list.updateAddAll(index, source.asModels());
    }


}
