package com.zfy.lxadapter.helper.query;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.LxList;
import com.zfy.lxadapter.data.Idable;
import com.zfy.lxadapter.data.LxModel;
import com.zfy.lxadapter.function._Consumer;
import com.zfy.lxadapter.function._LoopPredicate;
import com.zfy.lxadapter.function._Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2019-10-30
 * Describe :
 *
 * @author chendong
 */
public class QueryExecutor {

    private LxList list;

    public QueryExecutor(LxList list) {
        this.list = list;
    }

    public void setList(LxList list) {
        this.list = list;
    }

    @Nullable
    public <E> E findOneById(Class<E> clazz, Object id) {
        if (id == null) {
            return null;
        }
        E result = null;
        for (LxModel t : list) {
            if (!t.unpack().getClass().equals(clazz)) {
                continue;
            }
            E unpack = t.unpack();
            if (unpack instanceof Idable && id.equals(((Idable) unpack).getObjId())) {
                result = unpack;
                break;
            }
        }
        return result;
    }

    // 查找
    @NonNull
    public <E> List<E> findImpl(Class<E> clazz, int type, _Predicate<E> test, int count) {
        List<E> l = new ArrayList<>();
        for (LxModel t : list) {
            if (count > 0 && list.size() >= count) {
                break;
            }
            if (t.getItemType() != type) {
                continue;
            }
            if (!t.unpack().getClass().equals(clazz)) {
                continue;
            }
            E unpack = t.unpack();
            if (test.test(unpack)) {
                l.add(unpack);
            }
        }
        return l;
    }

    // 设置，使用普通条件
    public <E> void setImpl(Class<E> clazz, int type, _Predicate<E> predicate, _Consumer<E> consumer) {
        list.updateSet(new ClazzPredicate<>(clazz, type, predicate), new ClazzConsumer<>(clazz, type, consumer));
    }

    // 设置，使用下标
    public <E> void setImpl(Class<E> clazz, int type, int index, _Consumer<E> consumer) {
        list.updateSet(index, new ClazzConsumer<>(clazz, type, consumer));
    }

    // 设置，使用数据
    public <E> void setImpl(Class<E> clazz, int type, LxModel model, _Consumer<E> consumer) {
        list.updateSet(model, new ClazzConsumer<>(clazz, type, consumer));
    }

    // 设置，使用复杂条件
    public <E> void setImplX(Class<E> clazz, int type, _LoopPredicate<E> predicate, _Consumer<E> consumer) {
        list.updateSetX(new ClazzLoopPredicate<>(clazz, type, predicate), new ClazzConsumer<>(clazz, type, consumer));
    }

    // 删除，使用普通条件
    public <E> void removeImpl(Class<E> clazz, int type, int index) {
        list.updateRemove(index);
    }

    // 删除使用复杂条件
    public <E> void removeImpl(Class<E> clazz, int type, LxModel model) {
        list.updateRemove(model);
    }

    // 删除，使用普通条件
    public <E> void removeImpl(Class<E> clazz, int type, _Predicate<E> predicate) {
        list.updateRemove(new ClazzPredicate<>(clazz, type, predicate));
    }

    // 删除使用复杂条件
    public <E> void removeImplX(Class<E> clazz, int type, _LoopPredicate<E> predicate) {
        list.updateRemoveX(new ClazzLoopPredicate<>(clazz, type, predicate));
    }

    static class ClazzPredicate<E> implements _Predicate<LxModel> {

        private Class         clazz;
        private _Predicate<E> predicate;
        private int           type;

        ClazzPredicate(Class clazz, int type, _Predicate<E> predicate) {
            this.clazz = clazz;
            this.predicate = predicate;
            this.type = type;
        }

        @Override
        public boolean test(LxModel data) {
            if (data.getItemType() != type) {
                return false;
            }
            if (clazz != null && !clazz.equals(data.unpack().getClass())) {
                return false;
            }
            if (predicate == null) {
                return true;
            }
            return predicate.test(data.unpack());
        }
    }

    static class ClazzLoopPredicate<E> implements _LoopPredicate<LxModel> {

        private Class             clazz;
        private _LoopPredicate<E> predicate;
        private int               type;

        ClazzLoopPredicate(Class clazz, int type, _LoopPredicate<E> predicate) {
            this.clazz = clazz;
            this.predicate = predicate;
            this.type = type;
        }

        @Override
        public int test(LxModel data) {
            if (data.getItemType() != type) {
                return Lx.Loop.FALSE_NOT_BREAK;
            }
            if (clazz != null && !clazz.equals(data.unpack().getClass())) {
                return Lx.Loop.FALSE_NOT_BREAK;
            }
            if (predicate == null) {
                return Lx.Loop.TRUE_NOT_BREAK;
            }
            return predicate.test(data.unpack());
        }
    }

    static class ClazzConsumer<E> implements _Consumer<LxModel> {

        private Class        clazz;
        private int          type;
        private _Consumer<E> consumer;

        public ClazzConsumer(Class clazz, int type, _Consumer<E> consumer) {
            this.clazz = clazz;
            this.type = type;
            this.consumer = consumer;
        }

        @Override
        public void accept(LxModel data) {
            if (type != data.getItemType()) {
                return;
            }
            if (clazz != null && !clazz.equals(data.unpack().getClass())) {
                return;
            }
            consumer.accept(data.unpack());
        }
    }
}
