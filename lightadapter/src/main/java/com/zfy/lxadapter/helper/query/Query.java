package com.zfy.lxadapter.helper.query;

import com.zfy.lxadapter.data.LxModel;
import com.zfy.lxadapter.function._Consumer;
import com.zfy.lxadapter.function._LoopPredicate;
import com.zfy.lxadapter.function._Predicate;
import com.zfy.lxadapter.helper.LxSource;

/**
 * CreateAt : 2019-10-30
 * Describe :
 *
 * @author chendong
 */
public class Query<E> {

    private int               operate;
    private Class<E>          clazz;
    private int               itemType;
    private int               index = -1;
    private LxModel           item;
    private _Predicate<E>     predicate;
    private _LoopPredicate<E> loopPredicate;
    private _Consumer<E>      consumer;
    private LxSource          source;

    public static Query add(LxSource source) {
        Query<Object> query = new Query<>();
        query.operate = 1;
        query.source = source;
        return query;
    }

    public static Query add(int index, LxSource source) {
        Query<Object> query = new Query<>();
        query.operate = 1;
        query.source = source;
        query.index = index;
        return query;
    }

    public static Query remove(int index) {
        Query<Object> query = new Query<>();
        query.index = index;
        return query;
    }

    public static Query remove(LxModel model) {
        Query<Object> query = new Query<>();
        query.item = model;
        return query;
    }

    public static <T> Query<T> remove(Class<T> clazz, int type, _Predicate<T> predicate) {
        Query<T> query = new Query<>();
        query.clazz = clazz;
        query.itemType = type;
        query.predicate = predicate;
        return query;
    }

    public static <T> Query<T> remove(Class<T> clazz, int type, _LoopPredicate<T> predicate) {
        Query<T> query = new Query<>();
        query.clazz = clazz;
        query.itemType = type;
        query.loopPredicate = predicate;
        return query;
    }

    public static <T> Query<T> set(int index, _Consumer<T> consumer) {
        Query<T> query = new Query<>();
        query.index = index;
        query.consumer = consumer;
        return query;
    }

    public static <T> Query<T> set(LxModel model, _Consumer<T> consumer) {
        Query<T> query = new Query<>();
        query.item = model;
        query.consumer = consumer;
        return query;
    }

    public static <T> Query<T> set(Class<T> clazz, int type, _Predicate<T> predicate, _Consumer<T> consumer) {
        Query<T> query = new Query<>();
        query.clazz = clazz;
        query.itemType = type;
        query.predicate = predicate;
        query.consumer = consumer;
        return query;
    }

    public static <T> Query<T> set(Class<T> clazz, int type, _LoopPredicate<T> predicate, _Consumer<T> consumer) {
        Query<T> query = new Query<>();
        query.clazz = clazz;
        query.itemType = type;
        query.loopPredicate = predicate;
        query.consumer = consumer;
        return query;
    }
}
