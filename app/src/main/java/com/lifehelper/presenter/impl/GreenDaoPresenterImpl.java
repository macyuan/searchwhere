package com.lifehelper.presenter.impl;

import android.content.Context;

import com.lifehelper.model.GreenDaoModel;
import com.lifehelper.presenter.GreenDaoPresenter;
import com.lifehelper.view.GreenDaoView;
import com.mytian.lb.DaoSession;
import com.mytian.lb.PlanNodeTable;
import com.mytian.lb.PlanNodeTableDao;
import com.mytian.lb.RouteLineNodeTable;
import com.mytian.lb.RouteLineNodeTableDao;


/**
 * Created by jsion on 16/3/22.
 */
public class GreenDaoPresenterImpl implements GreenDaoPresenter {
    private GreenDaoModel greenDaoModel;
    private GreenDaoView greenDaoView;
    private DaoSession daoSession;
    private PlanNodeTableDao planNodeTableDao;
    private RouteLineNodeTableDao routeLineNodeTableDao;

    public GreenDaoPresenterImpl(GreenDaoView greenDaoView, Context context) {
        greenDaoModel = new GreenDaoModel(context);
        this.greenDaoView = greenDaoView;
        daoSession = greenDaoModel.getGreenDaoSession();
        planNodeTableDao = daoSession.getPlanNodeTableDao();
        routeLineNodeTableDao = daoSession.getRouteLineNodeTableDao();
    }

    @Override
    public void insertPlanNode(PlanNodeTable planNodeTable) {
        planNodeTableDao.insert(planNodeTable);
        greenDaoView.insertPlanNodes(planNodeTable);
    }

    @Override
    public void insertRoutePlanNodes(RouteLineNodeTable routeLineNodeTable) {
        routeLineNodeTableDao.insert(routeLineNodeTable);
        greenDaoView.insertRoutePlanNodes(routeLineNodeTable);
    }

    @Override
    public void clearPlanNode() {
        planNodeTableDao.deleteAll();
        greenDaoView.clearTable();
    }

    @Override
    public void clearRoutePlanNodes() {
        routeLineNodeTableDao.deleteAll();
        greenDaoView.clearTable();
    }

    @Override
    public void queryRoutePlanNodes() {
        greenDaoView.bindRoutePlanNodes(routeLineNodeTableDao.queryBuilder().list());
    }

    @Override
    public void queryPlanNodes() {
        greenDaoView.bindPlanNode(planNodeTableDao.queryBuilder().list());
    }
}
