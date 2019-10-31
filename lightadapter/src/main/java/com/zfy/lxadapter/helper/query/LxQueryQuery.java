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

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2019-10-18
 * Describe :
 * <p>
 * add      index
 * remove   class, type, index, item, predicate, loopPredicate
 * set      class, type, index, item, predicate, loopPredicate, consumer
 * find     class, type, predicate, loopPredicate
 *
 * @author chendong
 */
public class LxQueryQuery {

    public static void test() {
        LxQueryQuery queryY = new LxQueryQuery();

        Query query = Query.add(LxSource.just(""));

        Query.remove(String.class, 100, new _Predicate<String>() {
            @Override
            public boolean test(String data) {
                return false;
            }
        });
        queryY.add(query);
    }

    public static final int ADD    = 1;
    public static final int REMOVE = 2;
    public static final int SET    = 3;
    public static final int FIND   = 4;

    private LxList list;

    public static class Query<E> {

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

    public void execute(Query query) {
        switch (query.operate) {
            case ADD:
                add(query);
                break;
            case REMOVE:
                remove(query);
                break;
            case SET:
                set(query);
                break;
        }
    }


    @Nullable
    public <E> E findOne(Query<E> query) {
        List<E> impl = findImpl(query.clazz, query.itemType, query.predicate, 1);
        return impl.isEmpty() ? null : impl.get(0);
    }

    @NonNull
    public <E> List<E> find(Query<E> query) {
        return findImpl(query.clazz, query.itemType, query.predicate, -1);
    }

    public <E> void set(Query<E> query) {
        if (query.index != -1) {
            setImpl(query.clazz, query.itemType, query.index, query.consumer);
        } else if (query.item != null) {
            setImpl(query.clazz, query.itemType, query.item, query.consumer);
        } else if (query.predicate != null) {
            setImpl(query.clazz, query.itemType, query.predicate, query.consumer);
        } else if (query.loopPredicate != null) {
            setImpl(query.clazz, query.itemType, query.loopPredicate, query.consumer);
        }
    }

    public <E> void remove(Query<E> query) {
        if (query.index != -1) {
            list.updateRemove(query.index);
        } else if (query.item != null) {
            list.updateRemove(query.item);
        } else if (query.predicate != null) {
            removeImpl(query.clazz, query.itemType, query.predicate);
        } else if (query.loopPredicate != null) {
            removeImpl(query.clazz, query.itemType, query.loopPredicate);
        }
    }

    public boolean add(Query query) {
        if (query.index != -1) {
            return list.updateAddAll(query.index, query.source.asModels());
        } else {
            return list.updateAddAll(query.source.asModels());
        }
    }

    @NonNull
    private <E> List<E> findImpl(Class<E> clazz, int type, _Predicate<E> test, int count) {
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

    private <E> void setImpl(Class<E> clazz, int type, _Predicate<E> predicate, _Consumer<E> consumer) {
        list.updateSet(new ClazzPredicate<>(clazz, type, predicate), new ClazzConsumer<>(clazz, type, consumer));
    }

    private <E> void setImpl(Class<E> clazz, int type, int index, _Consumer<E> consumer) {
        list.updateSet(index, new ClazzConsumer<>(clazz, type, consumer));
    }

    private <E> void setImpl(Class<E> clazz, int type, LxModel model, _Consumer<E> consumer) {
        list.updateSet(model, new ClazzConsumer<>(clazz, type, consumer));
    }

    private <E> void setImpl(Class<E> clazz, int type, _LoopPredicate<E> predicate, _Consumer<E> consumer) {
        list.updateSetX(new ClazzLoopPredicate<>(clazz, type, predicate), new ClazzConsumer<>(clazz, type, consumer));
    }

    private <E> void removeImpl(Class<E> clazz, int type, _Predicate<E> predicate) {
        list.updateRemove(new ClazzPredicate<>(clazz, type, predicate));
    }

    private <E> void removeImpl(Class<E> clazz, int type, _LoopPredicate<E> predicate) {
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
            if (!clazz.equals(data.unpack().getClass())) {
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
            if (!clazz.equals(data.unpack().getClass())) {
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
            if (!clazz.equals(data.unpack().getClass())) {
                return;
            }
            consumer.accept(data.unpack());
        }
    }

}
