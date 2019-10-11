package com.zfy.lxadapter.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.LxList;
import com.zfy.lxadapter.data.LxModel;

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
public class LxMergedList extends LxList {

    private static final int CONTENT_BLOCK = -1;

    // 放某种类型的数据集，没有绑定到 adapter，不能直接发布更新
    private static class BlockItemList extends LxList {

        private LxMergedList updateDispatcher;

        BlockItemList(boolean async) {
            super(async);
        }

        @Override
        public void update(@NonNull List<LxModel> newItems) {
            super.update(newItems);
            updateDispatcher.updateByInternal();
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
    // 内容类型开始的位置
    private int                   contentStartPosition;
    // 组装数据临时数据集，避免多次创建
    private List<LxModel>         tempList;
    // 内容区块
    private BlockItemList         contentBlockList;


    public LxMergedList(boolean async) {
        super(async);
        this.async = async;
        tempList = new ArrayList<>();
        blockList = new ArrayList<>(); // 寻址次数较多，使用 ArrayList
        initContentBlock();
    }

    public LxMergedList() {
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
        int index = 0;
        for (int i = 0; i < blockList.size(); i++) {
            if (blockList.get(i).block > viewType) {
                index = i;
            }
        }
        lxModels.updateDispatcher = this;
        blockList.add(index, new TypeLxListBlock(viewType, lxModels));
        calcContentStartPosition();
        return lxModels;
    }

    /*default*/ int getContentStartPosition() {
        return contentStartPosition;
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
            if (Lx.isContentType(node.block)) {
                break;
            }
            count += node.list.size();
        }
        contentStartPosition = count;
    }

    private void updateByInternal() {
        tempList.clear();
        for (TypeLxListBlock node : blockList) {
            tempList.addAll(node.list);
        }
        update(tempList);
    }

    @Override
    public void update(@NonNull List<LxModel> newItems) {
        super.update(newItems);
        calcContentStartPosition();
    }

} 