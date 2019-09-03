package com.zfy.adapter.data;

import com.zfy.adapter.Lx;

import java.util.HashSet;
import java.util.Set;

/**
 * CreateAt : 2019-08-30
 * Describe :
 *
 * @author chendong
 */
public class LxModel implements Diffable<LxModel>, Typeable, Selectable, Idable {

    private static final Set<String> EMPTY_SET = new HashSet<>();
    private static       int         ID        = 0;


    private int     incrementId;
    private Object  data;
    private int     type = Lx.VIEW_TYPE_DEFAULT;
    private int     moduleId; // 模块ID
    private boolean selected;

    public LxModel(Object data) {
        this.data = data;
        this.incrementId = ID++;
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
        Object objId1 = getObjId();
        Object objId2 = newItem.getObjId();
        if (objId1 != null && objId2 != null) {
            return objId1.equals(objId2);
        }
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
        return false;
    }

    @Override
    public Set<String> getChangePayload(LxModel newItem) {
        if (canCompare(newItem, this)) {
            return ((Diffable) data).getChangePayload(newItem.data);
        }
        return EMPTY_SET;
    }

    @Override
    public int getItemType() {
        if (data instanceof Typeable) {
            return ((Typeable) data).getItemType();
        }
        return type;
    }

//    @Override
//    public LxModel copyNewOne() {
//        LxModel lxModel = new LxModel(LxUtil.copy(data));
//        lxModel.moduleId = moduleId;
//        lxModel.type = type;
//        return lxModel;
//    }


    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public Object getObjId() {
        if (data instanceof Idable) {
            return ((Idable) data).getObjId();
        }
        return incrementId;
    }
}
