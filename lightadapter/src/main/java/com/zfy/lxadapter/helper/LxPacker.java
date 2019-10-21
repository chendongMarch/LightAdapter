package com.zfy.lxadapter.helper;

import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.data.LxModel;
import com.zfy.lxadapter.function._BiFunction;
import com.zfy.lxadapter.function._Consumer;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2019-08-31
 * Describe : 普通数据和 LxModel 的数据快速转换
 *
 * @author chendong
 */
@Deprecated
public class LxPacker {

    // 打包成单个 LxModel
    public static <E> LxModel pack(E data) {
        return pack(Lx.ViewType.DEFAULT, data);
    }

    // 打包单个 LxModel，并制定类型
    public static <E> LxModel pack(int type, E data) {
        return pack(type, data, null);
    }

    // 打包单个 LxModel，并制定类型
    public static <E> LxModel pack(int type, E data, _Consumer<LxModel> consumer) {
        LxModel lxModel = new LxModel(data);
        lxModel.setType(type);
        if (consumer != null) {
            consumer.accept(lxModel);
        }
        return lxModel;
    }

    // 打包成 LxModel 列表
    public static <E> List<LxModel> pack(List<E> list) {
        return pack(Lx.ViewType.DEFAULT, list);
    }

    // 打包成 LxModel 列表，并指定类型
    public static <E> List<LxModel> pack(int type, List<E> list) {
        return pack(type, list, null);
    }

    // 打包成 LxModel 列表，并指定类型
    public static <E> List<LxModel> pack(int type, List<E> list, _Consumer<LxModel> consumer) {
        List<LxModel> lxModels = new ArrayList<>();
        for (E e : list) {
            lxModels.add(pack(type, e, consumer));
        }
        return lxModels;
    }


    public static <DATA, SECTION> List<LxModel> packSection(List<LxModel> originList, List<DATA> list, _BiFunction<DATA, DATA, SECTION> function) {
        List<LxModel> lxModels = new ArrayList<>();
        DATA lastData = null;
        if (originList != null && !originList.isEmpty()) {
            LxModel lastLxModel = originList.get(originList.size() - 1);
            if (lastLxModel.getItemType() != Lx.ViewType.SECTION) {
                lastData = lastLxModel.unpack();
            }
        }
        DATA current;
        for (DATA data : list) {
            current = data;
            SECTION section = function.apply(lastData, current);
            if (section != null) {
                lxModels.add(pack(Lx.ViewType.SECTION, section));
            }
            lxModels.add(pack(Lx.ViewType.DEFAULT, data));
            lastData = current;
        }
        return lxModels;
    }

}
