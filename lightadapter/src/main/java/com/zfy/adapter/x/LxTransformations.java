package com.zfy.adapter.x;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2019-08-31
 * Describe :
 *
 * @author chendong
 */
public class LxTransformations {

    public static <E> List<LxModel> map(List<E> list) {
        return map(Lx.VIEW_TYPE_DEFAULT, list);
    }

    public static <E> List<LxModel> map(int type, List<E> list) {
        List<LxModel> lxModels = new ArrayList<>();
        for (E e : list) {
            LxModel lxModel = new LxModel(e);
            lxModel.setType(type);
            lxModels.add(lxModel);
        }
        return lxModels;
    }

    public static <E> LxModel map(E data) {
        return map(Lx.VIEW_TYPE_DEFAULT, data);
    }

    public static <E> LxModel map(int type, E data) {
        LxModel lxModel = new LxModel(data);
        lxModel.setType(type);
        return lxModel;
    }
}
