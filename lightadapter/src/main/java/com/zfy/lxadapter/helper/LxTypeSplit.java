package com.zfy.lxadapter.helper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.LxAdapter;
import com.zfy.lxadapter.list.LxTypedList;
import com.zfy.lxadapter.LxList;
import com.zfy.lxadapter.data.LxModel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * CreateAt : 2019-09-03
 * Describe :
 *
 * @author chendong
 */
public class LxTypeSplit {

    private static final LxTypedList EMPTY = new LxTypedList() {

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
    private LxBlockedList blockedList;
    private boolean       hasExtType;

    public void setAdapter(LxAdapter adapter, boolean hasExtType) {
        this.adapter = adapter;
        this.hasExtType = hasExtType;
        this.blockedList = new LxBlockedList();
    }

    public @NonNull
    LxList getContentTypeData() {
        if (hasExtType) {
            LxTypedList contentList = blockedList.getContentTypeList();
            return contentList == null ? EMPTY : contentList;
        }
        return adapter.getData();
    }

    public @NonNull
    LxList getExtTypeData(int viewType) {
        if (hasExtType) {
            LxTypedList typedList = blockedList.getExtTypeList(viewType);
            return typedList == null ? EMPTY : typedList;
        }
        return EMPTY;

    }

    private class LxBlockedList extends LxTypedList {

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
                boolean isContentType = Lx.isContentType(lxModel.getItemType());
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
        LxTypedList getExtTypeList(int blockId) {
            trySplitList();
            return array.get(blockId);
        }

        LxTypedList getContentTypeList() {
            trySplitList();
            return getExtTypeList(Lx.DEFAULT_BLOCK_ID);
        }


        class HandleUpdateLxList extends LxTypedList {

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
