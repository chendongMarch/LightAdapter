package com.zfy.lxadapter.helper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zfy.lxadapter.LxList;
import com.zfy.lxadapter.data.LxModel;
import com.zfy.lxadapter.function._Consumer;
import com.zfy.lxadapter.function._LoopPredicate;
import com.zfy.lxadapter.function._Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2019-10-18
 * Describe :
 *
 * @author chendong
 */
public class LxTypedHelper {

    private LxList list;

    public LxTypedHelper(LxList list) {
        this.list = list;
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
            if (t.getItemType() == type) {
                l.add(t.unpack());
            }
        }
        return l;
    }

    public <E> void updateSet4Type(Class<E> clazz, int type, _Consumer<E> consumer) {
        list.updateSet(data -> data.getItemType() == type, data -> consumer.accept(data.unpack()));
    }

    public void updateRemove4Type(int type) {
        list.updateRemove(data -> data.getItemType() == type);
    }

    public <E> void updateSet(Class<E> clazz, _Predicate<E> predicate, _Consumer<E> consumer) {
        list.updateSet(data -> predicate.test(data.unpack()), data -> consumer.accept(data.unpack()));
    }

    public <E> void updateSetX(Class<E> clazz, _LoopPredicate<E> predicate, _Consumer<E> consumer) {
        list.updateSetX(data -> predicate.test(data.unpack()), data -> consumer.accept(data.unpack()));
    }

    public <E> void updateSet(Class<E> clazz, int index, _Consumer<E> consumer) {
        list.updateSet(index, data -> consumer.accept(data.unpack()));
    }

    public <E> void updateSet(Class<E> clazz, LxModel model, _Consumer<E> consumer) {
        list.updateSet(model, data -> consumer.accept(data.unpack()));
    }


    public <E> void updateRemove(Class<E> clazz, _Predicate<E> predicate) {
        list.updateRemove(data -> predicate.test(data.unpack()));
    }

    public <E> void updateRemoveX(Class<E> clazz, _LoopPredicate<E> predicate) {
        list.updateRemoveX(data -> predicate.test(data.unpack()));
    }

    public <E> void updateRemoveLast(Class<E> clazz, _Predicate<E> predicate) {
        list.updateRemove(data -> predicate.test(data.unpack()));
    }

    public <E> void updateRemoveLastX(Class<E> clazz, _LoopPredicate<E> predicate) {
        list.updateRemoveX(data -> predicate.test(data.unpack()));
    }

    public boolean updateAdd(LxSource source) {
        return list.updateAddAll(source.asModels());
    }

    public boolean updateAdd(int index, LxSource source) {
        return list.updateAddAll(index, source.asModels());
    }


}
