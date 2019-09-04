package com.zfy.adapter.function;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.zfy.adapter.Lx;
import com.zfy.adapter.LxAdapter;
import com.zfy.adapter.data.LxModel;
import com.zfy.adapter.list.LxList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * CreateAt : 2019-09-03
 * Describe :
 *
 * @author chendong
 */
public class LxTypeSplit {

    private static final LxList EMPTY = new LxList() {

        private List<LxModel> list = new ArrayList<>();

        @Override
        public List<LxModel> list() {
            return list;
        }

        @Override
        public void update(@NonNull List<LxModel> newItems) {

        }
    };

    private LxAdapter     adapter;
    private Set<Integer>  contentTypes;
    private LxBlockedList blockedList;

    public void setAdapter(LxAdapter adapter, Set<Integer> contentTypes) {
        this.adapter = adapter;
        this.contentTypes = contentTypes;
        this.blockedList = new LxBlockedList();
    }

    public @NonNull
    LxList getContentTypeData() {
        if (this.contentTypes == null || this.contentTypes.isEmpty()) {
            return adapter.getData();
        }
        LxList contentList = blockedList.getContentList();
        return contentList == null ? EMPTY : contentList;
    }

    public @NonNull
    LxList getCustomTypeData(int viewType) {
        if (this.contentTypes == null || this.contentTypes.isEmpty()) {
            return EMPTY;
        }
        LxList typedList = blockedList.getTypedList(viewType);
        return typedList == null ? EMPTY : typedList;
    }

    private boolean isContentType(int viewType) {
        if (contentTypes == null || contentTypes.isEmpty()) {
            // 没有多类型，没有自定义类型的时候均为 内容类型
            // throw new IllegalStateException("期望获取类型是否是内容类型，但是没有设置 contentType");
            LxUtil.log("期望获取类型是否是内容类型，但是没有设置 contentType");
            return true;
        }
        return contentTypes.contains(viewType);
    }

    private class LxBlockedList extends LxList {

        private SparseArray<HandleUpdateLxList> array;
        private List<Integer>                   blockIds;

        private LxBlockedList() {
            super();
            this.array = new SparseArray<>();
            this.blockIds = new ArrayList<>();
            this.trySplitList();
        }

        private List<LxModel> mergeList() {
            List<LxModel> list = new ArrayList<>();
            for (Integer id : blockIds) {
                List<LxModel> es = array.get(id);
                list.addAll(es);
            }
            return list;
        }

        private void trySplitList() {
            array.clear();
            blockIds.clear();
            List<LxModel> list = adapter.getData();
            for (LxModel lxModel : list) {
                boolean isContentType = isContentType(lxModel.getItemType());
                int blockId = isContentType ? Lx.DEFAULT_BLOCK_ID : lxModel.getItemType();
                LxBlockedList.HandleUpdateLxList handleUpdateLxList = array.get(blockId);
                if (handleUpdateLxList == null) {
                    handleUpdateLxList = new HandleUpdateLxList();
                    array.append(blockId, handleUpdateLxList);
                    blockIds.add(blockId);
                }
                handleUpdateLxList.models.add(lxModel);
            }
        }

        @Override
        public List<LxModel> list() {
            return mergeList();
        }

        @Override
        public void update(@NonNull List<LxModel> newItems) {
            LxList list = adapter.getData();
            list.update(mergeList());
        }

        @Nullable
        LxList getTypedList(int blockId) {
            trySplitList();
            return array.get(blockId);
        }

        LxList getContentList() {
            trySplitList();
            return getTypedList(Lx.DEFAULT_BLOCK_ID);
        }


        class HandleUpdateLxList extends LxList {

            private List<LxModel> models = new ArrayList<>();

            @Override
            public List<LxModel> list() {
                return models;
            }

            @Override
            public void update(@NonNull List<LxModel> newItems) {
                models.clear();
                models.addAll(newItems);
                LxBlockedList.this.update(mergeList());
            }
        }

    }


}
