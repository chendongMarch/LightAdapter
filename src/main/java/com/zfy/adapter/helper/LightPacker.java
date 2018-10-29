package com.zfy.adapter.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2017/6/19
 * Describe : 打包数据辅助
 *
 * @author chendong
 */
public class LightPacker<Wrap, D> {

    public static abstract class OnPackHandler<Wrap, D> {

        public abstract void onPack(List<Wrap> list, int index, D preObj, D currentObj, D nextObj);

        public D getData(Wrap wrap) {
            return null;
        }
    }


    private List<Wrap> preList;
    private List<D>    currentList;
    private List<Wrap> nextList;

    public LightPacker(List<D> currentList) {
        this.currentList = currentList;
    }

    public LightPacker<Wrap, D> setPreList(List<Wrap> preList) {
        this.preList = preList;
        return this;
    }

    public LightPacker<Wrap, D> setNextList(List<Wrap> nextList) {
        this.nextList = nextList;
        return this;
    }



    public List<Wrap> pack(OnPackHandler<Wrap, D> onPackHandler) {
        List<Wrap> datas = new ArrayList<>();
        int index = 0;
        D preObj;
        D nextObj;
        D currentObj;
        for (; index < currentList.size(); index++) {
            // 计算 pre obj
            if (index == 0) {
                preObj = isNotEmpty(preList)
                        ? onPackHandler.getData(preList.get(preList.size() - 1))
                        : null;
            } else {
                preObj = currentList.get(index - 1);
            }

            // 计算 next obj
            if (index == currentList.size() - 1) {
                nextObj = isNotEmpty(nextList)
                        ? onPackHandler.getData(nextList.get(0))
                        : null;
            } else {
                nextObj = currentList.get(index + 1);
            }

            // 计算 current obj
            currentObj = currentList.get(index);

            // 打包数据
            onPackHandler.onPack(datas, index, preObj, currentObj, nextObj);
        }
        return datas;
    }

    private boolean isNotEmpty(List list) {
        return list != null && list.size() > 0;
    }
}
