package com.zfy.adapter.function;

import com.zfy.adapter.Lx;
import com.zfy.adapter.data.LxModel;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2019-08-31
 * Describe : 普通数据和 LxModel 的数据快速转换
 *
 * @author chendong
 */
public class LxTransformations {

    // 打包成 LxModel 列表
    public static <E> List<LxModel> pack(List<E> list) {
        return pack(Lx.VIEW_TYPE_DEFAULT, list);
    }

    // 打包成 LxModel 列表，并指定类型
    public static <E> List<LxModel> pack(int type, List<E> list) {
        List<LxModel> lxModels = new ArrayList<>();
        for (E e : list) {
            lxModels.add(pack(type, e));
        }
        return lxModels;
    }

    // 打包成单个 LxModel
    public static <E> LxModel pack(E data) {
        return pack(Lx.VIEW_TYPE_DEFAULT, data);
    }

    // 打包单个 LxModel，并制定类型
    public static <E> LxModel pack(int type, E data) {
        LxModel lxModel = new LxModel(data);
        lxModel.setType(type);
        return lxModel;
    }

    // 解包 LxModel 列表
    public static <E> List<E> unpack(List<LxModel> lxModels) {
        List<E> list = new ArrayList<>();
        for (LxModel lxModel : lxModels) {
            list.add(lxModel.unpack());
        }
        return list;
    }


}
