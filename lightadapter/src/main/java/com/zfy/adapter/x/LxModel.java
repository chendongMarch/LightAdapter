package com.zfy.adapter.x;

import com.zfy.adapter.common.ItemType;
import com.zfy.adapter.data.Diffable;
import com.zfy.adapter.data.Typeable;

import java.util.Set;

/**
 * CreateAt : 2019-08-30
 * Describe :
 *
 * @author chendong
 */
public class LxModel implements Diffable<LxModel>, Typeable {

    private Object data;

    public LxModel(Object data) {
        this.data = data;
    }

    public <T> T unpack() {
        return (T) data;
    }

    private boolean canCompare(LxModel newItem, LxModel current) {
        Object newData = newItem.data;
        Object currentData = current.data;
        if (newData.getClass().equals(currentData.getClass())
                && newData instanceof Diffable
                && currentData instanceof Diffable) {
            return true;
        }
        return false;
    }

    @Override
    public boolean areItemsTheSame(LxModel newItem) {
        if (canCompare(newItem, this)) {
            return ((Diffable) newItem.data).areItemsTheSame(newItem.data);
        }
        return this.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(LxModel newItem) {
        if (canCompare(newItem, this)) {
            return ((Diffable) newItem.data).areContentsTheSame(newItem.data);
        }
        return true;
    }

    @Override
    public Set<String> getChangePayload(LxModel newItem) {
        if (canCompare(newItem, this)) {
            return ((Diffable) newItem.data).getChangePayload(newItem.data);
        }
        return null;
    }

    @Override
    public int getItemType() {
        if (data instanceof Typeable) {
            return ((Typeable) data).getItemType();
        }
        return  ItemType.TYPE_DEFAULT;
    }
}
