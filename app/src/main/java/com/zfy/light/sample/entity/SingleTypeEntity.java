package com.zfy.light.sample.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.zfy.adapter.data.Diffable;

/**
 * CreateAt : 2018/11/9
 * Describe :
 *
 * @author chendong
 */
public class SingleTypeEntity implements Diffable<SingleTypeEntity>, Parcelable {

    public String desc;
    public String title;
    public int id;

    public SingleTypeEntity(int id, String desc, String title) {
        this.desc = desc;
        this.title = title;
        this.id = id;
    }

    public SingleTypeEntity(String title, String desc) {
        this.desc = desc;
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.desc);
        dest.writeString(this.title);
    }

    protected SingleTypeEntity(Parcel in) {
        this.desc = in.readString();
        this.title = in.readString();
    }

    public static final Creator<SingleTypeEntity> CREATOR = new Creator<SingleTypeEntity>() {
        @Override
        public SingleTypeEntity createFromParcel(Parcel source) {
            return new SingleTypeEntity(source);
        }

        @Override
        public SingleTypeEntity[] newArray(int size) {
            return new SingleTypeEntity[size];
        }
    };

    @Override
    public boolean areContentsTheSame(SingleTypeEntity newItem) {
        return false;
    }

}
