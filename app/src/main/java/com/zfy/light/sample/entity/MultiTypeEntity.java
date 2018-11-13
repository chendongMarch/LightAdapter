package com.zfy.light.sample.entity;

import android.os.Parcel;

import com.zfy.adapter.able.Diffable;
import com.zfy.adapter.able.Typeable;
import com.zfy.adapter.able.Sectionable;

/**
 * CreateAt : 2018/11/9
 * Describe :
 *
 * @author chendong
 */
public class MultiTypeEntity implements Typeable, Sectionable, Diffable<MultiTypeEntity> {

    public static final int TYPE_DESC      = 0; // 文本描述类型
    public static final int TYPE_BASIC     = 1; // 数据适配部分，单类型/多类型/Typeable/ModelTypeConfigCallback
    public static final int TYPE_LIST      = 2; // 内置集合类数据更新，DiffUtil/Diffable/payload/LightDiffList/LightAsyncDiffList
    public static final int TYPE_EVENT     = 3; // 事件。单击、双击、长按
    public static final int TYPE_HOLDER    = 4; // LightHolder
    public static final int TYPE_DELEGATE  = 5; // 功能代理
    public static final int TYPE_ASSISTANT = 6; // 辅助，分割线，滑动选中
    public static final int TYPE_FUTURE    = 7; // 分页器，Expandable,Animation
    public static final int TYPE_PROJECT   = 8; // 项目
    public static final int TYPE_LINK      = 9; // 超链

    public static final int TYPE_CAN_DRAG       = 10; // 长按拖拽，view 拖拽
    public static final int TYPE_CAN_SWIPE      = 12; // 自动开启滑动的

    public int      type;
    public boolean  isSection;
    public String   sectionTitle;
    public String   desc;
    public String   url;
    public String   title;
    public String   subTitle;
    public String   cover;
    public Class<?> targetClazz;
    public String   msg;
    public int      id;

    static int ID = 100;


    public MultiTypeEntity() {
    }

    public MultiTypeEntity(int type) {
        this.type = type;
        this.id = ID++;
    }


    public MultiTypeEntity(int type, String sectionTitle) {
        this.type = type;
        this.sectionTitle = sectionTitle;
        this.id = ID++;
    }

    @Override
    public int getItemType() {
        return type;
    }

    @Override
    public boolean isSection() {
        return sectionTitle != null;
    }

    @Override
    public boolean areItemsTheSame(MultiTypeEntity newItem) {
        return id == newItem.id;
    }

    @Override
    public boolean areContentsTheSame(MultiTypeEntity newItem) {
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeByte(this.isSection ? (byte) 1 : (byte) 0);
    }

    protected MultiTypeEntity(Parcel in) {
        this.type = in.readInt();
        this.isSection = in.readByte() != 0;
    }

    public static final Creator<MultiTypeEntity> CREATOR = new Creator<MultiTypeEntity>() {
        @Override
        public MultiTypeEntity createFromParcel(Parcel source) {
            return new MultiTypeEntity(source);
        }

        @Override
        public MultiTypeEntity[] newArray(int size) {
            return new MultiTypeEntity[size];
        }
    };


}
