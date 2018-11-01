package com.zfy.adapter.extend;

import android.support.v7.widget.RecyclerView;

import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.LightHolder;
import com.zfy.adapter.listener.AdapterViewBinder;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2018/2/3
 * Describe :
 * <p>
 * 需要的参数：
 * adapter，绑定到的 adapter
 * 类型，单选|多选
 * 初始选择项，列表
 * 数据绑定器
 *
 * @author chendong
 */
public class SelectManager<D> {

    public static final String TAG = SelectManager.class.getSimpleName();

    public static final int TYPE_SINGLE = 1;
    public static final int TYPE_MULTI = 2;

    private int mType;
    private List<D> mSelectDatas;
    private LightAdapter<D> mAdapter;
    private AdapterViewBinder<D> mBinder;
    private OnSelectListener<D> mSelectListener;

    public interface OnSelectListener<D> {

        boolean onBeforeSelect(boolean toSelect, D data);

        void onAfterSelect(boolean toSelect, D data);
    }

    public SelectManager(LightAdapter<D> adapter, int type, AdapterViewBinder<D> binder) {
        mAdapter = adapter;
        mSelectDatas = new ArrayList<>();
        mType = type;
        mBinder = binder;
//        adapter.addViewBinder(mBinder);
    }

    public void setSelectListener(OnSelectListener<D> selectListener) {
        mSelectListener = selectListener;
    }

    public void initSelect(int... initItems) {
        if (initItems.length == 0) {
            return;
        }
        if (mType == TYPE_SINGLE) {
            mSelectDatas.add(mAdapter.getItem(initItems[0]));
        } else {
            for (int pos : initItems) {
                D item = mAdapter.getItem(pos);
                if (item != null) {
                    mSelectDatas.add(item);
                }
            }
        }
    }

    private boolean checkPosition(int pos) {
        return pos >= 0 && pos < getDatas().size();
    }

    private List<D> getDatas() {
        return mAdapter.getDatas();
    }

    // 不选中一个
    private void unSelect(int pos) {
        if (checkPosition(pos)) {
            D data = getDatas().get(pos);
            if (mSelectListener != null && !mSelectListener.onBeforeSelect(false, data)) {
                return;
            }
            if (mType == TYPE_MULTI) {
                mSelectDatas.remove(data);
            } else if (mType == TYPE_SINGLE) {
                mSelectDatas.clear();
            }
            updatePos(pos);
            if (mSelectListener != null) {
                mSelectListener.onAfterSelect(false, data);
            }
        }
    }

    // 选中一个
    private void doSelect(int pos) {
        if (checkPosition(pos)) {
            D data = getDatas().get(pos);
            if (mSelectListener != null && !mSelectListener.onBeforeSelect(true, data)) {
                return;
            }
            if (mType == TYPE_MULTI) {
                mSelectDatas.add(data);
            } else if (mType == TYPE_SINGLE) {
                mSelectDatas.clear();
                mSelectDatas.add(data);
            }
            updatePos(pos);
            if (mSelectListener != null) {
                mSelectListener.onAfterSelect(true, data);
            }
        }
    }

    // 切换，单选时去掉原来的，选中现在的，多选时，存在就去掉，不存在就添加
    public void select(int pos) {
        if (!checkPosition(pos)) {
            return;
        }
        D data = getDatas().get(pos);
        // 多选模式，存在就删除，不存在就添加，更新当前项
        if (mType == TYPE_MULTI) {
            if (mSelectDatas.contains(data)) {
                unSelect(pos);
            } else {
                doSelect(pos);
            }
        } else if (mType == TYPE_SINGLE) {
            // 单选情况就是去掉原来的，添加现在的
            D lastData = mSelectDatas.size() > 0 ? mSelectDatas.get(0) : null;
            // 上一个不为空
            if (lastData != null) {
                // 当前选择和上一个选择相同不进行数据更新
                if (lastData.equals(data))
                    return;
                unSelect(getDatas().indexOf(lastData));
            }
            // 更新现在的
            doSelect(pos);
        }
    }

    public boolean isSelect(D data) {
        return mSelectDatas.contains(data);
    }

    public int size() {
        return mSelectDatas.size();
    }

    public int indexOf(D data) {
        return mSelectDatas.indexOf(data);
    }

    public void clear() {
        for (D selectData : mSelectDatas) {
            unSelect(getDatas().indexOf(selectData));
        }
        mSelectDatas.clear();
    }

    public void selectAll() {
        mSelectDatas.addAll(getDatas());
        for (D selectData : mSelectDatas) {
            doSelect(getDatas().indexOf(selectData));
        }
    }

    public D getResult() {
        if (mSelectDatas != null && mSelectDatas.size() > 0) {
            return mSelectDatas.get(0);
        }
        return null;
    }

    public void setSelectDatas(List<D> selectDatas) {
        mSelectDatas = selectDatas;
    }

    public List<D> getResults() {
        if (mSelectDatas != null) {
            return mSelectDatas;
        }
        return new ArrayList<>();
    }

    private boolean updatePosUseHolder(int pos) {
        if (mAdapter == null || !checkPosition(pos))
            return false;
        RecyclerView.ViewHolder holder = null;
//        RecyclerView.ViewHolder holder = mAdapter.findViewHolderForAdapterPosition(mAdapter.toLayoutIndex(pos));
        try {
            if (holder != null) {
                LightHolder viewHolder = (LightHolder) holder;
                if (mBinder != null && mAdapter.getItem(pos) != null) {
                    mBinder.onBindViewHolder(viewHolder, mAdapter.getItem(pos), pos, mAdapter.getItemViewType(pos));
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void updatePos(final int pos) {
        if (!updatePosUseHolder(pos)) {
//            mAdapter.notifyItem().notifyItemChanged(pos);
        }
    }
}
