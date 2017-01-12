package com.lifehelper.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by jsion on 16/3/13.
 * Prototype deep copy
 */
public class NavMenuDetailEntity implements Cloneable, Parcelable {
    private String navMenuDetailTitle;
    private String navMenuDetailDesc;
    private int navMenuDetailIcon;
    private int navMenuDetailColor;
    private ArrayList<String> navMenuDetailList;
    private int navMenuDetaType;

    public int getNavMenuDetaType() {
        return navMenuDetaType;
    }

    public void setNavMenuDetaType(int navMenuDetaType) {
        this.navMenuDetaType = navMenuDetaType;
    }

    public String getNavMenuDetailTitle() {
        return navMenuDetailTitle;
    }

    public void setNavMenuDetailTitle(String navMenuDetailTitle) {
        this.navMenuDetailTitle = navMenuDetailTitle;
    }

    public String getNavMenuDetailDesc() {
        return navMenuDetailDesc;
    }

    public void setNavMenuDetailDesc(String navMenuDetailDesc) {
        this.navMenuDetailDesc = navMenuDetailDesc;
    }

    public int getNavMenuDetailIcon() {
        return navMenuDetailIcon;
    }

    public void setNavMenuDetailIcon(int navMenuDetailIcon) {
        this.navMenuDetailIcon = navMenuDetailIcon;
    }

    public int getNavMenuDetailColor() {
        return navMenuDetailColor;
    }

    public void setNavMenuDetailColor(int navMenuDetailColor) {
        this.navMenuDetailColor = navMenuDetailColor;
    }

    public ArrayList<String> getNavMenuDetailList() {
        return navMenuDetailList;
    }

    public void setNavMenuDetailList(ArrayList<String> navMenuDetailList) {
        this.navMenuDetailList = navMenuDetailList;
    }

    @Override
    public NavMenuDetailEntity clone() {
        try {
            NavMenuDetailEntity cloneEntity = (NavMenuDetailEntity) super.clone();
            cloneEntity.navMenuDetailTitle = this.navMenuDetailTitle;
            cloneEntity.navMenuDetailDesc = this.navMenuDetailDesc;
            cloneEntity.navMenuDetailIcon = this.navMenuDetailIcon;
            cloneEntity.navMenuDetailColor = this.navMenuDetailColor;
            cloneEntity.navMenuDetaType = this.navMenuDetaType;

            // if property is object,so need deep copy

            cloneEntity.navMenuDetailList = (ArrayList<String>) this.navMenuDetailList.clone();
            return cloneEntity;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.navMenuDetailTitle);
        dest.writeString(this.navMenuDetailDesc);
        dest.writeInt(this.navMenuDetailIcon);
        dest.writeInt(this.navMenuDetailColor);
        dest.writeStringList(this.navMenuDetailList);
        dest.writeInt(this.navMenuDetaType);
    }

    public NavMenuDetailEntity() {
    }

    protected NavMenuDetailEntity(Parcel in) {
        this.navMenuDetailTitle = in.readString();
        this.navMenuDetailDesc = in.readString();
        this.navMenuDetailIcon = in.readInt();
        this.navMenuDetailColor = in.readInt();
        this.navMenuDetailList = in.createStringArrayList();
        this.navMenuDetaType = in.readInt();
    }

    public static final Creator<NavMenuDetailEntity> CREATOR = new Creator<NavMenuDetailEntity>() {
        @Override
        public NavMenuDetailEntity createFromParcel(Parcel source) {
            return new NavMenuDetailEntity(source);
        }

        @Override
        public NavMenuDetailEntity[] newArray(int size) {
            return new NavMenuDetailEntity[size];
        }
    };
}
