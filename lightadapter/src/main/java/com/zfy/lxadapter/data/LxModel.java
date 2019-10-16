package com.zfy.lxadapter.data;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.helper.LxUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * CreateAt : 2019-08-30
 * Describe :
 *
 * @author chendong
 */
public class LxModel implements Diffable<LxModel>, Typeable, Selectable, Idable, Copyable<LxModel> {

    public static final  Object      EMPTY_OBJ = new Object();
    private static final Set<String> EMPTY_SET = new HashSet<>();
    private static       int         ID        = 0;

    private int     incrementId;
    private Object  data;
    private int     type = Lx.ViewType.DEFAULT;
    private int     moduleId; // 模块ID
    private boolean selected;

    private Bundle extra; // 数据扩展

    @NonNull
    public Bundle getExtra() {
        if (extra == null) {
            extra = new Bundle();
        }
        return extra;
    }


    // 条件更新

    public void setCondition(String condition) {
        setCondition(condition, null);
    }

    public void setCondition(String condition, Bundle bundle) {
        getExtra().putString(Lx.Condition.KEY, condition);
        getExtra().putBundle(Lx.Condition.VALUE, bundle);
    }

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
        if (newData == null || currentData == null) {
            return false;
        }
        if (newData.getClass().equals(currentData.getClass())
                && newData instanceof Diffable
                && currentData instanceof Diffable) {
            return true;
        }
        return false;
    }

    @Override
    public boolean areItemsTheSame(LxModel newItem) {
        // 如果考虑使用 payloads，则根据条件返回 false
        if (canCompare(newItem, this)) {
            return ((Diffable) data).areItemsTheSame(newItem.data);
        }
        // 使用 ID 比较，避免调用 insert/remove
        Object objId1 = getObjId();
        Object objId2 = newItem.getObjId();
        if (objId1 != null && objId2 != null) {
            return objId1.equals(objId2);
        }
        return this.equals(newItem);
    }


    @Override
    public boolean areContentsTheSame(LxModel newItem) {
        // 相同地址，则一定完全相同
        if (this.equals(newItem)) {
            return true;
        }
        // 有条件更新，直接调用 bind
        String condition = getExtra().getString(Lx.Condition.KEY, "");
        if (condition != null && !condition.isEmpty()) {
            return false;
        }
        // 如果考虑使用 payloads，则根据条件返回 false
        if (canCompare(newItem, this)) {
            return ((Diffable) data).areContentsTheSame(newItem.data);
        }
        return false;
    }

    @Override
    public Set<String> getChangePayload(LxModel newItem) {
        // 此时一定会触发 change，根据条件返回 payloads
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

    @Override
    public LxModel copyNewOne() {
        LxModel lxModel = new LxModel(LxUtil.copy(data));
        lxModel.moduleId = moduleId;
        lxModel.type = type;
        lxModel.selected = selected;
        lxModel.incrementId = incrementId;
        lxModel.extra = extra;
        return lxModel;
    }


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
