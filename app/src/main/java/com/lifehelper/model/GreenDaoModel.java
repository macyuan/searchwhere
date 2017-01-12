package com.lifehelper.model;

import android.content.Context;

import com.mytian.lb.App;

/**
 * Created by jsion on 16/3/22.
 */
public class GreenDaoModel {
    private Context context;

    public GreenDaoModel(Context context) {
        this.context = context;
    }

    public com.mytian.lb.DaoSession getGreenDaoSession() {
        return App.getDaoSession();
    }
}
