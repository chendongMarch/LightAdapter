package com.zfy.lxadapter.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.LxList;
import com.zfy.lxadapter.data.LxModel;
import com.zfy.lxadapter.diff.IDiffDispatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2018/11/8
 * Describe :
 *
 * 多类型使用，分区块更新数据
 *
 * @author chendong
 */
public class LxTypedList extends LxList {

    private static final int CONTENT_BLOCK = Lx.contentTypeOf();


    private static class NoAdapterDiffDispatcher implements IDiffDispatcher<LxModel> {

        private List<LxModel> list = new ArrayList<>();

        @Override
        public void update(@Nullable List<LxModel> newItems) {
            if (newItems != null) {
                list = newItems;
            }
        }

        @Override
        public List<LxModel> list() {
            return list;
        }
    }

    // 放某种类型的数据集，没有绑定到 adapter，不能直接发布更新
    private static class BlockItemList extends LxList {

        private LxTypedList updateDispatcher;

        BlockItemList(boolean async) {
            super(async);
            dispatcher = new NoAdapterDiffDispatcher();
        }

        public void onlyUpdateContent(List<LxModel> newItems) {
            dispatcher.update(newItems);
        }

        @Override
        public void update(@NonNull List<LxModel> newItems, int flag) {
            // 这里其实只会更新数据
            super.update(newItems, flag);
            updateDispatcher.update(newItems, FLAG_INTERNAL);
        }
    }

    // 列表中的某一块
    private static class TypeLxListBlock {
        int           block;
        BlockItemList list;

        TypeLxListBlock(int block, BlockItemList list) {
            this.block = block;
            this.list = list;
        }
    }

    // 分区块的数据源
    private List<TypeLxListBlock> blockList;
    // 内容区块
    private BlockItemList         contentBlockList;

    public LxTypedList(boolean async) {
        super(async);
        this.async = async;
        blockList = new ArrayList<>(); // 寻址次数较多，使用 ArrayList
        initContentBlock();
    }

    public LxTypedList() {
        this(false);
    }

    // 添加内容区块
    private void initContentBlock() {
        contentBlockList = new BlockItemList(async);
        contentBlockList.updateDispatcher = this;
        blockList.add(new TypeLxListBlock(CONTENT_BLOCK, contentBlockList));
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
        calcContentStartPosition();
    }

    @NonNull
    @Override
    public LxList getContentTypeData() {
        return contentBlockList;
    }

    @NonNull
    @Override
    public LxList getExtTypeData(int viewType) {
        for (TypeLxListBlock node : blockList) {
            if (node.block == viewType) {
                return node.list;
            }
        }
        // 无法找到该区块，添加新的
        BlockItemList lxModels = new BlockItemList(async);
        lxModels.updateDispatcher = this;
        int index = 0;
        if (viewType < CONTENT_BLOCK) {
            for (int i = 0; i < blockList.size(); i++) {
                if (blockList.get(i).block == viewType) {
                    index = i;
                }
            }
        } else if (viewType > CONTENT_BLOCK) {
            for (int i = blockList.size() - 1; i >= 0; i--) {
                if (blockList.get(i).block == CONTENT_BLOCK) {
                    index = i + 1;
                }
            }
        }
        blockList.add(index, new TypeLxListBlock(viewType, lxModels));
        calcContentStartPosition();
        return lxModels;
    }


    private void calcContentStartPosition() {
        if (adapter == null) {
            return;
        }
        boolean hasExtType = adapterHasExtType();
        if (!hasExtType) {
            contentStartPosition = 0;
            return;
        }
        int count = 0;
        for (TypeLxListBlock node : blockList) {
            if (node.block == CONTENT_BLOCK) {
                break;
            }
            count += node.list.size();
        }
        contentStartPosition = count;
    }

    private List<LxModel> getMergeList() {
        List<LxModel> mergedList = new ArrayList<>();
        for (TypeLxListBlock node : blockList) {
            mergedList.addAll(node.list.list());
        }
        return mergedList;
    }

    @Override
    public void update(@NonNull List<LxModel> newItems) {
        this.update(newItems, FLAG_NORMAL);
    }

    @Override
    public void update(@NonNull List<LxModel> newItems, int flag) {
        if (flag == FLAG_NORMAL) {
            super.update(newItems, FLAG_NORMAL);
        } else if (flag == FLAG_INTERNAL) {
            // 内部更新，拼接数据发布更新，直接发布更新
            this.update(getMergeList(), FLAG_NORMAL);
        } else if (flag == FLAG_ONLY_CONTENT) {
            // 直接更新的话，更新的是 content
            contentBlockList.onlyUpdateContent(newItems);
            this.update(getMergeList(), FLAG_NORMAL);
        }
        calcContentStartPosition();
    }

}