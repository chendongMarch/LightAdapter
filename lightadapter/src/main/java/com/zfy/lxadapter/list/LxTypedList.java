package com.zfy.lxadapter.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.LxList;
import com.zfy.lxadapter.data.LxModel;
import com.zfy.lxadapter.helper.LxTypeSplit;

import java.util.List;

/**
 * CreateAt : 2018/11/8
 * Describe : 对外支持更新的 List
 *
 * @author chendong
 */
public class LxTypedList extends LxList {

    private LxTypeSplit typeSplit;

    public LxTypedList(boolean async) {
        super(async);
        typeSplit = new LxTypeSplit();
    }

    public LxTypedList() {
        this(false);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
        typeSplit.setAdapter(this.adapter, adapterHasExtType());
        calcDataStartPosition();
    }

    @Override
    @NonNull
    public LxList getContentTypeData() {
        return typeSplit.getContentTypeData();
    }

    @Override
    @NonNull
    public LxList getExtTypeData(int viewType) {
        return typeSplit.getExtTypeData(viewType);
    }


    private void calcDataStartPosition() {
        if (adapter == null) {
            return;
        }
        boolean hasExtType = adapterHasExtType();
        if (hasExtType) {
            LxModel lxModel;
            int count = size();
            for (int i = 0; i < count; i++) {
                lxModel = get(i);
                if (Lx.isContentType(lxModel.getItemType())) {
                    dataStartPosition = i;
                    break;
                }
            }
        } else {
            dataStartPosition = 0;
        }
    }


    @Override
    public void update(@NonNull List<LxModel> newItems) {
        super.update(newItems);
        calcDataStartPosition();
    }

}
