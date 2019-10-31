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
 *
 * add      index
 * remove   class, type, index, item, predicate, loopPredicate
 * set      class, type, index, item, predicate, loopPredicate
 * find     class, type, predicate, loopPredicate
 * @author chendong
 */
public class LxQueryWhere<E> {

    public static void test() {
        LxList list = new LxList();

        int type = 100;


    }

    private LxList   list;
    private Class<E> clazz;
    private int      itemType;

    private LxQueryWhere() {
    }

    public static <E> LxQueryWhere<E> query(int itemType, Class<E> clazz, LxList list) {
        LxQueryWhere<E> query = new LxQueryWhere<>();
        query.clazz = clazz;
        query.itemType = itemType;
        query.list = list;
        return query;
    }

    public Executor<E> where(Where<E> where) {
        return new Executor<>(where, list, clazz, itemType);
    }

    public Executor<E> where(_Consumer<Where<E>> consumer) {
        Where<E> where = new Where<>();
        consumer.accept(where);
        return where(where);
    }

    public static class Where<E> {
        public int               index = -1;
        public LxModel           item;
        public _Predicate<E>     predicate;
        public _LoopPredicate<E> loopPredicate;
    }

    public static class Executor<E> {

        private Where<E> where;
        private LxList   list;
        private Class<E> clazz;
        private int      itemType;


        public Executor(Where<E> where, LxList list, Class<E> clazz, int itemType) {
            this.where = where;
            this.list = list;
            this.clazz = clazz;
            this.itemType = itemType;
        }

        @Nullable
        public E findOne() {
            List<E> impl = findImpl(clazz, itemType, where.predicate);
            return impl.isEmpty() ? null : impl.get(0);
        }

        @NonNull
        public List<E> find() {
            return findImpl(clazz, itemType, where.predicate);
        }

        public void set(_Consumer<E> consumer) {
            if (where.index != -1) {
                setImpl(clazz, itemType, where.index, consumer);
            } else if (where.item != null) {
                setImpl(clazz, itemType, where.item, consumer);
            } else if (where.predicate != null) {
                setImpl(clazz, itemType, where.predicate, consumer);
            } else if (where.loopPredicate != null) {
                setImpl(clazz, itemType, where.loopPredicate, consumer);
            }
        }

        public void remove() {
            if (where.index != -1) {
                list.updateRemove(where.index);
            } else if (where.item != null) {
                list.updateRemove(where.item);
            } else if (where.predicate != null) {
                removeImpl(clazz, itemType, where.predicate);
            } else if (where.loopPredicate != null) {
                removeImpl(clazz, itemType, where.loopPredicate);
            }
        }

        public boolean add(LxSource source) {
            if (where.index != -1) {
                return list.updateAddAll(where.index, source.asModels());
            } else {
                return list.updateAddAll(source.asModels());
            }
        }

        @NonNull
        private List<E> findImpl(Class<E> clazz, int type, _Predicate<E> test) {
            List<E> l = new ArrayList<>();
            for (LxModel t : list) {
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

        private void setImpl(Class<E> clazz, int type, _Predicate<E> predicate, _Consumer<E> consumer) {
            list.updateSet(new ClazzPredicate<>(clazz, type, predicate), new ClazzConsumer<>(clazz, type, consumer));
        }

        private void setImpl(Class<E> clazz, int type, int index, _Consumer<E> consumer) {
            list.updateSet(index, new ClazzConsumer<>(clazz, type, consumer));
        }

        private void setImpl(Class<E> clazz, int type, LxModel model, _Consumer<E> consumer) {
            list.updateSet(model, new ClazzConsumer<>(clazz, type, consumer));
        }

        private void setImpl(Class<E> clazz, int type, _LoopPredicate<E> predicate, _Consumer<E> consumer) {
            list.updateSetX(new ClazzLoopPredicate<>(clazz, type, predicate), new ClazzConsumer<>(clazz, type, consumer));
        }

        private void removeImpl(Class<E> clazz, int type, _Predicate<E> predicate) {
            list.updateRemove(new ClazzPredicate<>(clazz, type, predicate));
        }

        private void removeImpl(Class<E> clazz, int type, _LoopPredicate<E> predicate) {
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

}
