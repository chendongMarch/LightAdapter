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
        List<LxModel> lxModels = new ArrayList<>();
        for (E e : list) {
            lxModels.add(new LxModel(e));
        }
        return lxModels;
    }
}
