package com.zfy.lxadapter.helper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zfy.lxadapter.LxAdapter;
import com.zfy.lxadapter.LxItemBinder;
import com.zfy.lxadapter.LxList;
import com.zfy.lxadapter.LxRecyclerView;
import com.zfy.lxadapter.component.LxPickerComponent;
import com.zfy.lxadapter.data.LxModel;
import com.zfy.lxadapter.function._Consumer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * CreateAt : 2019-09-30
 * Describe :
 * 添加无用的占位
 * 多级联动
 *
 * @author chendong
 */
public class LxPicker<D> {

    public interface PickerDataFetcher<D> {
        List<D> resp(@Nullable D pickValue, _Consumer<List<D>> callback);
    }

    public interface OnPickerDataUpdateFinishListener {
        void finish();
    }

    private static class PickerNode<D> {
        int                    defaultPickPosition = 0;
        int                    currentPickPosition;
        int                    index;
        int                    viewType;
        LxAdapter              adapter;
        LxPickerComponent.Opts opts;
        PickerDataFetcher<D>   fetcher;
    }

    private LinkedList<PickerNode<D>>        pickerNodeList;
    private ViewGroup                        pickerContainer;
    private OnPickerDataUpdateFinishListener finishListener;

    public LxPicker(LinearLayout pickerContainer) {
        this.pickerContainer = pickerContainer;
        pickerNodeList = new LinkedList<>();
    }


    // 前后追加隐藏数据
    private List<LxModel> addFakeData(List<LxModel> snapshot, int viewType, LxPickerComponent.Opts opts) {
        if (opts.infinite) {
            return snapshot;
        }
        int size = (opts.exposeViewCount - 1) / 2;
        // 前面追加
        for (int i = 0; i < size; i++) {
            LxModel element = new LxModel(null);
            element.setType(viewType);
            snapshot.add(0, element);
        }
        // 后面追加
        for (int i = 0; i < size; i++) {
            LxModel element = new LxModel(null);
            element.setType(viewType);
            snapshot.add(element);
        }
        return snapshot;
    }


    public LxAdapter addPicker(
            LxPickerComponent.Opts opts,
            LxItemBinder<D> itemBinder,
            PickerDataFetcher<D> pickerDataFetcher) {
        RecyclerView recyclerView = new LxRecyclerView(pickerContainer.getContext());
        pickerContainer.addView(recyclerView);
        return addPicker(recyclerView, opts, itemBinder, pickerDataFetcher);
    }


    public LxAdapter addPicker(RecyclerView recyclerView,
                               LxPickerComponent.Opts opts,
                               LxItemBinder<D> itemBinder,
                               PickerDataFetcher<D> pickerDataFetcher) {
        LxList lxList = new LxList();
        LxAdapter adapter = LxAdapter.of(lxList)
                .bindItem(itemBinder)
                .compose(value -> {
                    if (opts.infinite) {
                        return value.infinite();
                    }
                    return value;
                })
                .component(new LxPickerComponent(opts))
                .attachTo(recyclerView, LxManager.linear(recyclerView.getContext()));
        addPicker(itemBinder.getTypeOpts().viewType, adapter, pickerDataFetcher);
        return adapter;
    }


    // 添加一个 Picker
    public void addPicker(int viewType, LxAdapter adapter, PickerDataFetcher<D> pickerDataFetcher) {
        LxPickerComponent.Opts opts = getPickerComponent(adapter).getOpts();
        if (opts.exposeViewCount % 2 == 0) {
            throw new IllegalArgumentException("必须是奇数");
        }
        // 当前数据前后添加假数据
        LxList data = adapter.getData();
        List<LxModel> lxModels = addFakeData(data.snapshot(), viewType, opts);
        data.update(lxModels);

        // 构造选择器节点
        PickerNode<D> pickerNode = new PickerNode<>();
        pickerNode.index = pickerNodeList.size();
        pickerNode.adapter = adapter;
        pickerNode.fetcher = pickerDataFetcher;
        pickerNode.viewType = viewType;
        pickerNode.opts = opts;

        // 为节点增加监听
        LxPickerComponent component = getPickerComponent(pickerNode.adapter);
        component.setOnPickerListener(position -> {
            pickerNode.currentPickPosition = position;
            // 查找触发下一个
            int findIndex = pickerNode.index + 1;
            if (findIndex >= pickerNodeList.size()) {
                // 选中监听
                Log.e("chendong", "最后一个选中监听");
                if (finishListener != null) {
                    finishListener.finish();
                }
            } else {
                D pickValue = pickerNode.adapter.getData().get(position).unpack();
                PickerNode<D> nextNode = pickerNodeList.get(findIndex);
                updatePickerNode(pickValue, nextNode);
            }
        });
        pickerNodeList.addLast(pickerNode);
    }

    private void updatePickerNode(D pickValue, PickerNode<D> node) {
        LxAdapter adapter = node.adapter;
        LxPickerComponent component = getPickerComponent(adapter);
        LxPickerComponent.Opts opts = component.getOpts();
        _Consumer<List<D>> consumer = respData -> {
            List<LxModel> pack = LxTransformations.pack(node.viewType, respData);
            List<LxModel> models = addFakeData(pack, node.viewType, opts);
            adapter.getData().updateDataSetChanged(models);
            int pos = calcDefaultPosition(node.defaultPickPosition, node.opts, adapter.getData().size());
            component.selectItem(pos, false);
            node.defaultPickPosition = 0;
        };
        List<D> resp = node.fetcher.resp(pickValue, consumer);
        if (resp != null) {
            consumer.accept(resp);
        }
    }

    // 获取内部的 component
    private @NonNull
    LxPickerComponent getPickerComponent(LxAdapter adapter) {
        LxPickerComponent component = adapter.getComponent(LxPickerComponent.class);
        if (component == null) {
            throw new IllegalStateException("PickerComponent Not Found");
        }
        return component;
    }

    private int calcDefaultPosition(int pos, LxPickerComponent.Opts opts, int dataSize) {
        if (opts.infinite) {
            return pos + dataSize * 10;
        } else {
            return pos;
        }
    }

    public void setFinishListener(OnPickerDataUpdateFinishListener finishListener) {
        this.finishListener = finishListener;
    }

    public List<D> getResult() {
        List<D> l = new ArrayList<>();
        for (PickerNode<D> node : pickerNodeList) {
            l.add(node.adapter.getData().get(node.currentPickPosition).unpack());
        }
        return l;
    }


    // 激活选择器并且会触发选中第一个
    public void active() {
        if (!pickerNodeList.isEmpty()) {
            PickerNode<D> first = pickerNodeList.getFirst();
            updatePickerNode(null, first);
        }
    }

    public void select(int... poss) {
        if (poss.length != pickerNodeList.size()) {
            return;
        }
        for (int i = 0; i < pickerNodeList.size(); i++) {
            pickerNodeList.get(i).defaultPickPosition = poss[i];
        }
        active();
    }

}
