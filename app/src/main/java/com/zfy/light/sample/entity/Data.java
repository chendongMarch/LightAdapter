package com.zfy.light.sample.entity;

import android.os.Parcel;

import com.zfy.adapter.able.Diffable;
import com.zfy.adapter.able.Sectionable;
import com.zfy.adapter.able.Typeable;

import java.util.HashSet;
import java.util.Set;

/**
 * CreateAt : 2018/11/9
 * Describe :
 *
 * @author chendong
 */
public class Data implements Typeable, Sectionable, Diffable<Data> {

    public static final String TITLE_CHANGED = "TITLE_CHANGED";
    public static final String DESC_CHANGED  = "DESC_CHANGED";

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

    public static final int TYPE_PAYLOAD1 = 13; // payload 测试1
    public static final int TYPE_PAYLOAD2 = 14; // payload 测试2

    public static final int TYPE_CONTENT = 15; // payload 测试1

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


    public Data() {
    }

    public Data(int type) {
        this.type = type;
        this.id = ID++;
    }

    public Data(int type, String sectionTitle) {
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
    public boolean areItemsTheSame(Data newItem) {
        return id == newItem.id && type == newItem.type;
    }

    @Override
    public boolean areContentsTheSame(Data newItem) {
        if (type == TYPE_PAYLOAD1 && newItem.type == TYPE_PAYLOAD1 || type == TYPE_PAYLOAD2 && newItem.type == TYPE_PAYLOAD2) {
            if (!title.equals(newItem.title) || !desc.equals(newItem.desc)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Set<String> getChangePayload(Data newItem) {
        Set<String> set = new HashSet<>();
        if (!title.equals(newItem.title)) {
            set.add(TITLE_CHANGED);
        }
        if (!desc.equals(newItem.desc)) {
            set.add(DESC_CHANGED);
        }
        return set;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeByte(this.isSection ? (byte) 1 : (byte) 0);
        dest.writeString(this.sectionTitle);
        dest.writeString(this.desc);
        dest.writeString(this.url);
        dest.writeString(this.title);
        dest.writeString(this.subTitle);
        dest.writeString(this.cover);
        dest.writeSerializable(this.targetClazz);
        dest.writeString(this.msg);
        dest.writeInt(this.id);
    }

    protected Data(Parcel in) {
        this.type = in.readInt();
        this.isSection = in.readByte() != 0;
        this.sectionTitle = in.readString();
        this.desc = in.readString();
        this.url = in.readString();
        this.title = in.readString();
        this.subTitle = in.readString();
        this.cover = in.readString();
        this.targetClazz = (Class<?>) in.readSerializable();
        this.msg = in.readString();
        this.id = in.readInt();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel source) {
            return new Data(source);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };
}
