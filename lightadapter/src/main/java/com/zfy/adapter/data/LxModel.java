package com.zfy.adapter.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.zfy.adapter.Lx;

import java.util.Set;

/**
 * CreateAt : 2019-08-30
 * Describe :
 *
 * @author chendong
 */
public class LxModel implements Diffable<LxModel>, Typeable, Copyable<LxModel>, Selectable {

    private Object  data;
    private int     type = Lx.VIEW_TYPE_DEFAULT;
    private int     moduleId; // 模块ID
    private boolean selected;

    public LxModel(Object data) {
        this.data = data;
    }

    public void setType(int type) {
        this.type = type;
    }

    public <T> T unpack() {
        return (T) data;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
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
            return ((Diffable) data).areItemsTheSame(newItem.data);
        }
        return this.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(LxModel newItem) {
        if (canCompare(newItem, this)) {
            return ((Diffable) data).areContentsTheSame(newItem.data);
        }
        return true;
    }

    @Override
    public Set<String> getChangePayload(LxModel newItem) {
        if (canCompare(newItem, this)) {
            return ((Diffable) data).getChangePayload(newItem.data);
        }
        return null;
    }

    @Override
    public int getItemType() {
        if (data instanceof Typeable) {
            return ((Typeable) data).getItemType();
        }
        return type;
    }

    @Override
    public LxModel copyNewOne() {
        LxModel lxModel = new LxModel(copy(data));
        lxModel.moduleId = moduleId;
        lxModel.type = type;
        return lxModel;
    }


    private Object copy(Object input) {
        Object newOne = null;
        if (input instanceof Copyable) {
            try {
                newOne = ((Copyable) input).copyNewOne();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (input instanceof Parcelable) {
            Parcelable parcelable = (Parcelable) input;
            Parcel parcel = null;
            try {
                parcel = Parcel.obtain();
                parcel.writeParcelable(parcelable, 0);
                parcel.setDataPosition(0);
                Parcelable copy = parcel.readParcelable(input.getClass().getClassLoader());
                newOne = copy;
            } finally {
                if (parcel != null) {
                    parcel.recycle();
                }
            }
        }
        if (newOne == null) {
            newOne = input;
        }
        return newOne;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }
}
