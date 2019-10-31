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
import com.zfy.lxadapter.helper.LxSource;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2019-10-18
 * Describe : 一个类型属于一个 class,一个 class 可能有多个 type
 *
 * @author chendong
 */
public class LxQuerySimple {

    private LxList list;


    public LxQuerySimple(LxList list) {
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


    @Nullable
    public <E> E findOne(Class<E> clazz, _Predicate<E> test) {
        List<E> list = find(clazz, test);
        return list.isEmpty() ? null : list.get(0);
    }

    @Nullable
    public <E> E findOne(Class<E> clazz, int type) {
        List<E> list = find(clazz, type);
        return list.isEmpty() ? null : list.get(0);
    }


    @NonNull
    public <E> List<E> find(Class<E> clazz, _Predicate<E> test) {
        List<E> l = new ArrayList<>();
        for (LxModel t : list) {
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


    @NonNull
    public <E> List<E> find(Class<E> clazz, int type) {
        List<E> l = new ArrayList<>();
        for (LxModel t : list) {
            if (!t.unpack().getClass().equals(clazz)) {
                continue;
            }
            E unpack = t.unpack();
            if (t.getItemType() == type) {
                l.add(unpack);
            }
        }
        return l;
    }

    public <E> void updateSet4Type(Class<E> clazz, int type, _Consumer<E> consumer) {
        list.updateSet(data -> data.getItemType() == type, new ClazzConsumer<>(clazz, consumer));
    }

    public void updateRemove4Type(int type) {
        list.updateRemove(data -> data.getItemType() == type);
    }

    public <E> void updateSet(Class<E> clazz, _Predicate<E> predicate, _Consumer<E> consumer) {
        list.updateSet(new ClazzPredicate<>(clazz, predicate), new ClazzConsumer<>(clazz, consumer));
    }

    public <E> void updateSetX(Class<E> clazz, _LoopPredicate<E> predicate, _Consumer<E> consumer) {
        list.updateSetX(new ClazzLoopPredicate<>(clazz, predicate), new ClazzConsumer<>(clazz, consumer));
    }

    public <E> void updateSet(Class<E> clazz, int index, _Consumer<E> consumer) {
        list.updateSet(index, new ClazzConsumer<>(clazz, consumer));
    }

    public <E> void updateSet(Class<E> clazz, LxModel model, _Consumer<E> consumer) {
        list.updateSet(model, new ClazzConsumer<>(clazz, consumer));
    }


    public <E> void updateRemove(Class<E> clazz, _Predicate<E> predicate) {
        list.updateRemove(new ClazzPredicate<>(clazz, predicate));
    }

    public <E> void updateRemoveX(Class<E> clazz, _LoopPredicate<E> predicate) {
        list.updateRemoveX(new ClazzLoopPredicate<>(clazz, predicate));
    }

    public <E> void updateRemoveLast(Class<E> clazz, _Predicate<E> predicate) {
        list.updateRemove(new ClazzPredicate<>(clazz, predicate));
    }

    public <E> void updateRemoveLastX(Class<E> clazz, _LoopPredicate<E> predicate) {
        list.updateRemoveX(new ClazzLoopPredicate<>(clazz, predicate));
    }

    public boolean updateAdd(LxSource source) {
        return list.updateAddAll(source.asModels());
    }

    public boolean updateAdd(int index, LxSource source) {
        return list.updateAddAll(index, source.asModels());
    }


    static class ClazzPredicate<E> implements _Predicate<LxModel> {

        private Class         clazz;
        private _Predicate<E> predicate;

        ClazzPredicate(Class clazz, _Predicate<E> predicate) {
            this.clazz = clazz;
            this.predicate = predicate;
        }

        @Override
        public boolean test(LxModel data) {
            if (!clazz.equals(data.unpack().getClass())) {
                return false;
            }
            return predicate.test(data.unpack());
        }
    }

    static class ClazzLoopPredicate<E> implements _LoopPredicate<LxModel> {

        private Class             clazz;
        private _LoopPredicate<E> predicate;

        ClazzLoopPredicate(Class clazz, _LoopPredicate<E> predicate) {
            this.clazz = clazz;
            this.predicate = predicate;
        }

        @Override
        public int test(LxModel data) {
            if (!clazz.equals(data.unpack().getClass())) {
                return Lx.Loop.FALSE_NOT_BREAK;
            }
            return predicate.test(data.unpack());
        }
    }

    static class ClazzConsumer<E> implements _Consumer<LxModel> {

        private Class        clazz;
        private _Consumer<E> consumer;

        ClazzConsumer(Class clazz, _Consumer<E> consumer) {
            this.clazz = clazz;
            this.consumer = consumer;
        }

        @Override
        public void accept(LxModel data) {
            if (!clazz.equals(data.unpack().getClass())) {
                return;
            }
            consumer.accept(data.unpack());
        }
    }

}
