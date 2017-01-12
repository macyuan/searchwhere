package com.lifehelper.view;


import com.mytian.lb.PlanNodeTable;
import com.mytian.lb.RouteLineNodeTable;

import java.util.List;

/**
 * Created by jsion on 16/3/22.
 */
public interface GreenDaoView {
    void bindRoutePlanNodes(List<RouteLineNodeTable> routeLineNodeTable);
    void bindPlanNode(List<PlanNodeTable> planNodeTable);
    void insertPlanNodes(PlanNodeTable planNodeTable);
    void insertRoutePlanNodes(RouteLineNodeTable routeLineNodeTable);
    void clearTable();
}
