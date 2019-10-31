package com.zfy.lxadapter.helper;

import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.LxList;
import com.zfy.lxadapter.data.LxModel;
import com.zfy.lxadapter.function._Consumer;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2019-10-19
 * Describe :
 * 解决数据每次都要打包的问题
 *
 * @author chendong
 */
public class LxSource {

    private List<LxModel> internalList;

    private LxSource() {

    }

    public static LxSource empty() {
        LxSource r = new LxSource();
        r.internalList = new ArrayList<>();
        return r;
    }

    public static LxSource snapshot(LxList list) {
        LxSource r = new LxSource();
        r.internalList = list.snapshot();
        return r;
    }

    public static <E> LxSource just(E data) {
        LxSource source = LxSource.empty();
        source.add(Lx.ViewType.DEFAULT, data);
        return source;
    }

    public static <E> LxSource just(int type, E data) {
        LxSource source = LxSource.empty();
        source.add(type, data, null);
        return source;
    }

    public static <E> LxSource just(List<E> list) {
        LxSource source = LxSource.empty();
        source.addAll(Lx.ViewType.DEFAULT, list);
        return source;
    }

    public static <E> LxSource just(int type, List<E> list) {
        LxSource source = LxSource.empty();
        source.addAll(type, list, null);
        return source;
    }

    public <E> LxModel add(E data) {
        return add(Lx.ViewType.DEFAULT, data);
    }

    public <E> LxModel add(int type, E data) {
        return add(type, data, null);
    }

    public <E> LxModel add(int type, E data, _Consumer<LxModel> consumer) {
        LxModel lxModel = wrap(type, data, consumer);
        internalList.add(lxModel);
        return lxModel;
    }

    public <E> LxModel addOnIndex(int index, E data) {
        return addOnIndex(index, Lx.ViewType.DEFAULT, data);
    }

    public <E> LxModel addOnIndex(int index, int type, E data) {
        return addOnIndex(index, type, data, null);
    }

    public <E> LxModel addOnIndex(int index, int type, E data, _Consumer<LxModel> consumer) {
        LxModel lxModel = wrap(type, data, consumer);
        internalList.add(index, lxModel);
        return lxModel;
    }

    public <E> List<LxModel> addAll(List<E> list) {
        return addAll(Lx.ViewType.DEFAULT, list);
    }

    public <E> List<LxModel> addAll(int type, List<E> list) {
        return addAll(type, list, null);
    }

    public <E> List<LxModel> addAll(int type, List<E> list, _Consumer<LxModel> consumer) {
        List<LxModel> lxModels = wrap(type, list, consumer);
        internalList.addAll(lxModels);
        return lxModels;
    }

    public <E> List<LxModel> addAllOnIndex(int index, List<E> list) {
        return addAllOnIndex(index, Lx.ViewType.DEFAULT, list);
    }

    public <E> List<LxModel> addAllOnIndex(int index, int type, List<E> list) {
        return addAllOnIndex(index, type, list, null);
    }

    public <E> List<LxModel> addAllOnIndex(int index, int type, List<E> list, _Consumer<LxModel> consumer) {
        List<LxModel> lxModels = wrap(type, list, consumer);
        internalList.addAll(index, lxModels);
        return lxModels;
    }

    private <E> LxModel wrap(int type, E data, _Consumer<LxModel> consumer) {
        LxModel lxModel = new LxModel(data);
        lxModel.setType(type);
        if (consumer != null) {
            consumer.accept(lxModel);
        }
        return lxModel;
    }

    private <E> List<LxModel> wrap(int type, List<E> datas, _Consumer<LxModel> consumer) {
        List<LxModel> lxModels = new ArrayList<>();
        for (E e : datas) {
            lxModels.add(wrap(type, e, consumer));
        }
        return lxModels;
    }

    public List<LxModel> asModels() {
        return internalList;
    }
}
