package com.zfy.lxadapter.helper;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zfy.lxadapter.LxAdapter;
import com.zfy.lxadapter.LxList;
import com.zfy.lxadapter.R;
import com.zfy.lxadapter.data.LxModel;

import java.util.List;

/**
 * CreateAt : 2019-09-17
 * Describe :
 * <p>
 * 嵌套滑动专用，记住上次滑动的位置，以便恢复状态
 * <p>
 * only support LinearLayoutManager
 *
 * @author chendong
 */
public class LxNesting {

    private static final String KEY_POS    = "KEY_POS";
    private static final String KEY_OFFSET = "KEY_OFFSET";


    public interface OnNoAdapterCallback {
        void set(RecyclerView view, LxList list);
    }

    private OnNoAdapterCallback onNoAdapterCallback;

    public LxNesting(OnNoAdapterCallback onNoAdapterCallback) {
        this.onNoAdapterCallback = onNoAdapterCallback;
    }

    public void setup(RecyclerView childRecyclerView, LxModel model, List<LxModel> datas) {
        RecyclerView.Adapter adapter = childRecyclerView.getAdapter();
        backup(childRecyclerView, model);
        LxList data;
        if (adapter != null) {
            data = ((LxAdapter) adapter).getData();
        } else {
            data = new LxList();
            onNoAdapterCallback.set(childRecyclerView, data);
        }
        data.update(datas);
    }

    private void backup(RecyclerView view, LxModel model) {
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager)) {
            return;
        }
        RecyclerView.OnScrollListener listener = (RecyclerView.OnScrollListener) view.getTag(R.id.tag_listener);
        if (listener != null) {
            view.removeOnScrollListener(listener);
        }
        view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    getPositionAndOffset(recyclerView, model);
                }
            }
        });
        scrollToPosition(view, model);
    }

    private void getPositionAndOffset(RecyclerView view, LxModel model) {
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if (layoutManager == null) {
            return;
        }
        // 获取可视的第一个view
        View topView = layoutManager.getChildAt(0);
        if (topView != null) {
            // 获取与该view的顶部的偏移量
            int lastOffset = LxUtil.getRecyclerViewOrientation(view) == RecyclerView.VERTICAL ? topView.getTop() : topView.getLeft();
            // 得到该View的数组位置
            int lastPosition = layoutManager.getPosition(topView);
            model.getExtra().putInt(KEY_OFFSET, lastOffset);
            model.getExtra().putInt(KEY_POS, lastPosition);
        }
    }

    private void scrollToPosition(RecyclerView view, LxModel model) {
        LinearLayoutManager manager = (LinearLayoutManager) view.getLayoutManager();
        int offset = model.getExtra().getInt(KEY_OFFSET, 0);
        int pos = model.getExtra().getInt(KEY_POS, 0);
        if (manager != null) {
            manager.scrollToPositionWithOffset(pos, offset);
        }
    }

}
