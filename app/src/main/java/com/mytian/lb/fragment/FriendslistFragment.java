package com.mytian.lb.fragment;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.android.gms.analytics.Tracker;
import com.lifehelper.baidumap.MyOrientationListener;
import com.lifehelper.baidumap.MyTransitRouteOverlay;
import com.lifehelper.baidumap.PoiOverlay;
import com.lifehelper.baidumap.TransitRouteOverlay;
import com.lifehelper.entity.BottomSheetEntity;
import com.lifehelper.entity.MyPoiInfoEntity;
import com.lifehelper.entity.MyPoiInfroWithLocationEntity;
import com.lifehelper.entity.NavMenuDetailEntity;
import com.lifehelper.entity.NavMenuEntity;
import com.lifehelper.presenter.impl.NavMenuDetailPresenterImpl;
import com.lifehelper.presenter.impl.NavMenuPresenterImpl;
import com.lifehelper.tools.T;
import com.lifehelper.tools.Tools;
import com.lifehelper.tools.ViewUtils;
import com.lifehelper.ui.RouteLineActivity;
import com.lifehelper.ui.WhoActivity;
import com.lifehelper.ui.customwidget.BottomSheetDialogView;
import com.lifehelper.ui.customwidget.LoadingDialog;
import com.lifehelper.ui.customwidget.MapStateView;
import com.lifehelper.view.NavMenuDetailView;
import com.lifehelper.view.NavMenuView;
import com.mytian.lb.AbsFragment;
import com.mytian.lb.R;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关注的人界面
 */
public class FriendslistFragment extends AbsFragment implements BaiduMap.OnMapStatusChangeListener, NavMenuView, NavMenuDetailView {
    public static final String FROM_BOTTOM_SHEET = "FROM_BOTTOM_SHEET";
    @BindView(R.id.bmapView)
    MapView mMapView;
    @BindView(R.id.msv_map_state_view)
    MapStateView mMapStateView;
    @BindView(R.id.msv_map_state_traffic)
    MapStateView mMapStateViewTraffic;
    @BindView(R.id.msv_map_state_satellite)
    MapStateView mMapStateViewSatellite;
    @BindView(R.id.iv_route_line)
    ImageView mRouteLine;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

//    @BindView(R.id.rlv_nav_menu_container)
//    RecyclerView mRecyclerNavMenuContainer;

//    private View view;
//    private MapView mMapView = null;
//    private MapStateView mMapStateView = null;
//    private MapStateView mMapStateViewTraffic = null;
//    private MapStateView mMapStateViewSatellite = null;
//    private RecyclerView mRecyclerNavMenuContainer = null;


    public LayoutInflater mInflater;

    private Tracker mTracker;

    public Context mContext;

    private ViewGroup mRootView;

    private NavMenuAdapter mNavMenuAdapter;
    private NavMenuPresenterImpl mNavMenuPresenter;
    private NavMenuDetailPresenterImpl mNavMenuDetailPresenter;
    private ActionBarDrawerToggle mToggle;
    private LatLng mCurrentCenpt;
    private MyOrientationListener mOrientationListener;
    private SensorManager sensorManager;
    private Sensor sensor;
    private NavDetailPopWindow mNavPopupWindow;
    private OnNavPopClickListener mOnNavPopClickListener;
    private View popView;
    private List<NavMenuDetailEntity> mNavMenuDetails;
    private LoadingDialog mLoadingDialog;

    public final static String SHAREDPREFERENCES_TIME = "START_TIME";
    /**
     * when init has load page 0,
     * so load more from 1
     */
    private int bottomIndex = 1;
    private BottomSheetDialogView mBottomSheetDialogView;
    private LoadingDialog mLoadMoreDialog;

    @OnClick(R.id.iv_route_line)
    void routeLine() {
        ViewUtils.changeActivity(getActivity(), RouteLineActivity.class, mCurrentBDLocation);
    }

    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private BDLocationListener mBdLocationListener;
    private BDLocation mCurrentBDLocation;
    private boolean isFirstEnter;
    private int mXDirection;
    private List<MyPoiInfoEntity> mPoiInfoEntities;
    /**
     * poi兴趣点检索
     */
    private PoiSearch mPoiSearch;
    private PoiSearch mLoadMorePoiSearch;
    private SuggestionSearch mSuggestionSearch;
    private SuggestionSearch mLoadMoreSuggestionSearch;
    private ArrayList<String> suggest;
    private ArrayList<String> mLoadMoreSuggest;

    /**
     * 公交地铁检索
     */
    private BusLineSearch busLineSearch;
    private String busLineId;


    /**
     * 路线规划
     */
    private RoutePlanSearch routePlanSearch;
    /**
     * 是否用默认icon
     */
    private boolean useDefaultIcon;

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.activity_map, container, false);
//
//        return view;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActivity().setContentView(R.layout.activity_main_drawer);

//        LayoutInflater  ll = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        view = ll.inflate(R.layout.activity_map1, null);

        initData();
        initView();
        initEvent();
    }

    @Override
    public void EInit() {
        super.EInit();
        initBaiduClient();
        initOritationListener();
        initSensorManager();

//        mMapStateView = (MapStateView) view.findViewById(R.id.msv_map_state_view);
        mMapStateView.setmOnMapStateViewClickListener(new MapStateView.OnMapStateViewClickListener() {
            @Override
            public void mapStateViewClick(int currentState) {


                if (currentState == MapStateView.MAP_STATE.NORMAL) {
                    modifyMapOverLay(mCurrentCenpt, mBaiduMap, -60.f);
                    MyLocationConfiguration.LocationMode locationMode = MyLocationConfiguration.LocationMode.COMPASS;
                    mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(locationMode, true, null));
                    mBaiduMap.getUiSettings().setCompassEnabled(true);
                    mMapStateView.setmCurrentState(MapStateView.MAP_STATE.STEREO);
                } else {
                    modifyMapOverLay(mCurrentCenpt, mBaiduMap, 0);
                    MyLocationConfiguration.LocationMode locationMode = MyLocationConfiguration.LocationMode.NORMAL;
                    mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(locationMode, true, null));
                    mBaiduMap.getUiSettings().setCompassEnabled(false);
                    mMapStateView.setmCurrentState(MapStateView.MAP_STATE.NORMAL);
                }

                //定位后是否恢复原始的缩放比例
//                MapStatus mMapStatus = new MapStatus.Builder()
//                        .target(mCurrentCenpt)
//                        .zoom(17)
//                        .build();
//                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//                mBaiduMap.animateMapStatus(mMapStatusUpdate);
            }
        });

//        mMapStateViewTraffic = (MapStateView) view.findViewById(R.id.msv_map_state_traffic);

        mMapStateViewTraffic.setmOnMapIconAndTextClickListener(new MapStateView.OnMapIconAndTextClickListener() {
            @Override
            public void mapIconAndTextClick(int iconAndTextSate) {
                if (iconAndTextSate == MapStateView.MAP_TEXT_STATE.MAP_ICON_ON) {
                    mBaiduMap.setTrafficEnabled(false);
                    mMapStateViewTraffic.setmCurrentIconAndTextState(MapStateView.MAP_TEXT_STATE.MAP_ICON_OFF);
                    T.show(getActivity(), getResources().getString(R.string.traffic_off), 0);
                } else {
                    mBaiduMap.setTrafficEnabled(true);
                    mMapStateViewTraffic.setmCurrentIconAndTextState(MapStateView.MAP_TEXT_STATE.MAP_ICON_ON);
                    T.show(getActivity(), getResources().getString(R.string.traffic_on), 0);
                }
            }
        });

//        mMapStateViewSatellite = (MapStateView) view.findViewById(R.id.msv_map_state_satellite);

        mMapStateViewSatellite.setmOnMapIconAndTextClickListener(new MapStateView.OnMapIconAndTextClickListener() {
            @Override
            public void mapIconAndTextClick(int iconAndTextSate) {
                if (iconAndTextSate == MapStateView.MAP_TEXT_STATE.MAP_ICON_ON) {
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                    mMapStateViewSatellite.setmCurrentIconAndTextState(MapStateView.MAP_TEXT_STATE.MAP_ICON_OFF);
                    T.show(getActivity(), getResources().getString(R.string.satellite_on), 0);

                } else {
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                    mMapStateViewSatellite.setmCurrentIconAndTextState(MapStateView.MAP_TEXT_STATE.MAP_ICON_ON);
                    T.show(getActivity(), getResources().getString(R.string.satellite_off), 0);
                }
            }
        });


        mMapStateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do samething in onTouchEvent
            }
        });

        mMapStateViewTraffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do sameting in onTouchEvent
            }
        });

        mMapStateViewSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do sameting in onTouchEvent
            }
        });

    }

    /**
     * init Baidu location and Baidu map
     */
    private void initBaiduClient() {

//        mMapView = (MapView) view.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setTrafficEnabled(true);
        mBaiduMap.getUiSettings().setCompassEnabled(true);

        mMapView.showZoomControls(false);
        mMapView.showScaleControl(false);

        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mBdLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mBdLocationListener);
        initLocation(mLocationClient);
        mLocationClient.start();
    }

    //    @Override
    protected void initData() {
        mPoiInfoEntities = new ArrayList<>();
        mNavMenuAdapter = new NavMenuAdapter();
        mNavMenuPresenter = new NavMenuPresenterImpl(this);
        mNavMenuDetailPresenter = new NavMenuDetailPresenterImpl(this);
        mNavMenuAdapter.setOnRecylerItemClickListener(new OnRecylerItemClickListener() {
            @Override
            public void onRecylerItemClick(int clickType) {
                drawerOpenOrClose();
                if (clickType == NAV_MENU_CLICK._WHO) {
                    ViewUtils.changeActivity(getActivity(), WhoActivity.class, mCurrentBDLocation);
                } else {
                    showNavDetailPop(clickType);
                }

            }

            @Override
            public void onRecylerItemLongClick(int clickType) {

            }
        });

        mOnNavPopClickListener = new OnNavPopClickListener() {
            @Override
            public void onPopHeaderClick(NavMenuDetailEntity popItemDesc) {
                mLoadingDialog.show();
                testPOI(mCurrentCenpt, popItemDesc.getNavMenuDetailDesc(), 0, true, popItemDesc);
            }

            @Override
            public void onPopDeItemClick(String popItemName, NavMenuDetailEntity forGetUI) {
                mLoadingDialog.show();
                testPOI(mCurrentCenpt, popItemName, 0, false, forGetUI);
            }
        };
    }

    /**
     * for get UI property
     *
     * @param popItemDesc
     */
    private void addUI2MyPoiEntity(NavMenuDetailEntity popItemDesc) {
        for (int i = 0; i < mPoiInfoEntities.size(); i++) {
            mPoiInfoEntities.get(i).setNavMenuDetailEntity(popItemDesc);
        }
    }

    /**
     * showpopwindow
     *
     * @param clickType
     */
    private void showNavDetailPop(int clickType) {
        for (int i = 0; i < mNavMenuDetails.size(); i++) {
            NavMenuDetailEntity currentNavMD = mNavMenuDetails.get(i);
            if (clickType == currentNavMD.getNavMenuDetaType()) {
                mNavPopupWindow = new NavDetailPopWindow(getActivity());
                mNavPopupWindow.setNavMenuDetailEntity(currentNavMD);
                mNavPopupWindow.setOnNavPopClickListener(mOnNavPopClickListener);
                mNavPopupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                return;
            }
        }
    }

    /**
     * the interface for popwindow
     */
    public interface OnNavPopClickListener {
        void onPopHeaderClick(NavMenuDetailEntity popItemDesc);

        void onPopDeItemClick(String popItemName, NavMenuDetailEntity forGetUI);
    }

    /**
     * if you want use butterknife in popwindow
     * you may do like this.
     * because it only support local variable
     */
    class NavDetailPopWindow extends PopupWindow {
        private NavMenuDetailEntity navMenuDetailEntity;
        private Context context;
        private OnNavPopClickListener onNavPopClickListener;

        public void setOnNavPopClickListener(final OnNavPopClickListener onNavPopClickListener) {
            this.onNavPopClickListener = onNavPopClickListener;
            mPopHeaderIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onNavPopClickListener != null) {
                        onNavPopClickListener.onPopHeaderClick(navMenuDetailEntity);
                    }
                }
            });
        }

        public void setNavMenuDetailEntity(NavMenuDetailEntity navMenuDetailEntity) {
            this.navMenuDetailEntity = navMenuDetailEntity;
            mPopHeaderIcon.setImageResource(navMenuDetailEntity.getNavMenuDetailIcon());
            mPopHeaderDesc.setBackgroundColor(context.getResources().getColor(navMenuDetailEntity.getNavMenuDetailColor()));
            mPopHeaderDesc.setText(navMenuDetailEntity.getNavMenuDetailTitle());

            mPopRecyclerViewDiff.setLayoutManager(new GridLayoutManager(context, 2));
            PopNavMenuDeAdapter popNavMenuDeAdapter = new PopNavMenuDeAdapter();
            mPopRecyclerViewDiff.setAdapter(popNavMenuDeAdapter);
        }

        @BindView(R.id.iv_pop_header_icon)
        ImageView mPopHeaderIcon;
        @BindView(R.id.tv_nav_menu_detail_title)
        TextView mPopHeaderDesc;
        @BindView(R.id.rlv_pop_menu_detail_different)
        RecyclerView mPopRecyclerViewDiff;
        @BindView(R.id.ll_pop_header)
        LinearLayout mPopIconContainer;
        @BindView(R.id.ll_pop_body)
        LinearLayout mPopBody;

        public NavDetailPopWindow(Context context) {
            super(context);
            this.context = context;
            popView = LayoutInflater
                    .from(context)
                    .inflate(R.layout.pop_nav_menu_detail, null, false);
            ButterKnife.bind(this, popView);
            setContentView(popView);

            ColorDrawable dw = new ColorDrawable(0x00000000);
            setBackgroundDrawable(dw);
            setOutsideTouchable(true);
            setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

            ObjectAnimator popHeaderAnim = ObjectAnimator.ofFloat(mPopIconContainer, "translationY", -310, 0);
            popHeaderAnim.setInterpolator(new BounceInterpolator());
            popHeaderAnim.setDuration(1300);

            ObjectAnimator popBodyAnim = ObjectAnimator.ofFloat(mPopBody, "translationY", 310, 0);
            popBodyAnim.setInterpolator(new BounceInterpolator());
            popBodyAnim.setDuration(1300);

            popHeaderAnim.start();
            popBodyAnim.start();

        }

        class PopNavMenuDeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                PopNavMenuVH popNavMenuVH = new PopNavMenuVH(LayoutInflater.from(context).inflate(R.layout.item_pop_menu_detail, parent, false));
                return popNavMenuVH;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                final String currentItemName = navMenuDetailEntity.getNavMenuDetailList().get(position);
                PopNavMenuVH popNavMenuVH = (PopNavMenuVH) holder;
                popNavMenuVH.popMenuName.setText(currentItemName);

                if (onNavPopClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNavPopClickListener.onPopDeItemClick(currentItemName, navMenuDetailEntity);
                        }
                    });
                }
            }

            @Override
            public int getItemCount() {
                return navMenuDetailEntity.getNavMenuDetailList().size();
            }

            class PopNavMenuVH extends RecyclerView.ViewHolder {
                @BindView(R.id.tv_item_pop_diff)
                TextView popMenuName;

                public PopNavMenuVH(View itemView) {
                    super(itemView);
                    ButterKnife.bind(this, itemView);
                }
            }

        }
    }


    //    @Override
    protected void initView() {
        mLoadingDialog = new LoadingDialog(getActivity(), false);
        ButterKnife.bind(getActivity());
    }

    //    @Override
    protected void initEvent() {

        mNavMenuPresenter.getNavMenuData();
        mNavMenuDetailPresenter.getNavMenuDetail();

        toggleAndDrawer();

    }

    /**
     * init Sensor
     */
    private void initOritationListener() {
        mOrientationListener = new MyOrientationListener(getActivity().getApplicationContext());
        mOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mXDirection = (int) x;
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(mCurrentBDLocation.getRadius())
                        .direction(mXDirection)
                        .latitude(mCurrentBDLocation.getLatitude())
                        .longitude(mCurrentBDLocation.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);
                // change mapview compass degree
            }
        });
    }

    /**
     * init SensorManager
     */
    private void initSensorManager() {
        // get Sensor manager
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            // get Orientation Sensor
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
    }

    /**
     * modify Baidu map overlay
     *
     * @param mCurrentCenpt
     * @param mBaiduMap
     * @param overLay
     */
    private static void modifyMapOverLay(LatLng mCurrentCenpt, BaiduMap mBaiduMap, float overLay) {
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(mCurrentCenpt)
                .overlook(overLay)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.animateMapStatus(mMapStatusUpdate);
    }

    /**
     * make drawer link toolbar
     */
    private void toggleAndDrawer() {
//        setSupportActionBar(mToolbar);
//        mToggle = new ActionBarDrawerToggle(
//                getActivity(), mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mDrawerLayout.setDrawerListener(mToggle);
//        mToggle.syncState();
    }

    /**
     * 兴趣点测试
     */
    private void testPOI(final LatLng latLng, final String nearByPoiName, int pageNum, final boolean isAllCity, final NavMenuDetailEntity forGetUI) {
        mPoiSearch = PoiSearch.newInstance();
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
                    return;
                }
                suggest = new ArrayList<String>();
                for (SuggestionResult.SuggestionInfo info : suggestionResult.getAllSuggestions()) {
                    if (info.key != null) {
                        suggest.add(info.key);
                    }
                }

                Logger.e(suggest.toString());
            }
        });
        OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
            public void onGetPoiResult(PoiResult result) {
                mLoadingDialog.dismiss();
                //获取POI检索结果
                if (result == null
                        || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    Toast.makeText(getActivity(), "未找到结果", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    List<PoiInfo> allPoi = result.getAllPoi();
                    mPoiInfoEntities.clear();
                    for (PoiInfo poi : allPoi) {
                        MyPoiInfoEntity tem = new MyPoiInfoEntity();
                        tem.setPoiInfo(poi);
                        tem.setDistance2MyLocation(DistanceUtil.getDistance(mCurrentCenpt, poi.location));
                        mPoiInfoEntities.add(tem);
                    }

                    mBaiduMap.clear();
                    PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
                    mBaiduMap.setOnMarkerClickListener(overlay);
                    overlay.setData(result);
                    overlay.addToMap();
                    overlay.zoomToSpan();


                    //定义地图状态
                    MapStatus mMapStatus = new MapStatus.Builder()
                            .target(latLng)
//                            .zoom(17)
                            .build();
                    //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                    //改变地图状态
                    mBaiduMap.animateMapStatus(mMapStatusUpdate);

                    // deep clone
                    NavMenuDetailEntity temp = forGetUI.clone();
                    addUI2MyPoiEntity(temp);
                    BottomSheetEntity bottomSheetEntity = new BottomSheetEntity();
                    if (!isAllCity) {
                        temp.setNavMenuDetailTitle(nearByPoiName);
                    }
                    bottomSheetEntity.setNavMenuDetailEntity(temp);
                    bottomSheetEntity.setPoiInfoEntities(mPoiInfoEntities);

                    mBottomSheetDialogView = BottomSheetDialogView.bottomSheetShow(getActivity(), bottomSheetEntity, new BottomSheetDialogView.OnRecyclerScrollBottomListener() {
                        @Override
                        public void recyclerScrollBottom() {
                            if (mLoadMoreDialog == null) {
                                mLoadMoreDialog = new LoadingDialog(getActivity(), false, "加载更多...");
                            }
                            mLoadMoreDialog.show();
                            loadMorePOI(mPoiInfoEntities.get(0).getNavMenuDetailEntity(), nearByPoiName, isAllCity, latLng);
                            bottomIndex++;
                        }
                    });

                    mBottomSheetDialogView.setOnRecyClickListener(new BottomSheetDialogView.OnRecyClickListener() {
                        @Override
                        public void onReClick(MyPoiInfoEntity poiInfoEntity) {
                            MyPoiInfroWithLocationEntity tempPoiAndLocation = new MyPoiInfroWithLocationEntity();
                            tempPoiAndLocation.setmLocation(mCurrentBDLocation);
                            tempPoiAndLocation.setmPoiInfoEntity(poiInfoEntity);
                            ViewUtils.changeActivity(getActivity(), RouteLineActivity.class, tempPoiAndLocation, FROM_BOTTOM_SHEET);
                        }
                    });
//                    BottomSheetDialogView.bottomSheetShow(getActivity(), bottomSheetEntity);
                    return;
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

                    // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
                    String strInfo = "在";
                    for (CityInfo cityInfo : result.getSuggestCityList()) {
                        strInfo += cityInfo.city;
                        strInfo += ",";
                    }
                    strInfo += "找到结果";
                    Toast.makeText(getActivity(), strInfo, Toast.LENGTH_LONG)
                            .show();
                }
            }

            public void onGetPoiDetailResult(PoiDetailResult result) {
                //获取Place详情页检索结果
                if (result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getActivity(), result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        };

        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);

        if (isAllCity) {
            mPoiSearch.searchInCity(new PoiCitySearchOption()
                    .city(mCurrentBDLocation.getCity())
                    .keyword(nearByPoiName)
                    .pageNum(pageNum)
                    .pageCapacity(10));

        } else {
            mPoiSearch.searchNearby(new PoiNearbySearchOption().keyword(nearByPoiName)
                    .location(latLng)
                    .pageCapacity(10)
                    .pageNum(pageNum)
                    .radius(5000));
        }
    }

    /**
     * bottom sheet load more poi
     *
     * @param navMenuDetailEntity
     * @param isAllCity
     */
    private void loadMorePOI(final NavMenuDetailEntity navMenuDetailEntity, String nearByPoiName, boolean isAllCity, LatLng latLng) {
        if (mLoadMorePoiSearch == null) {
            mLoadMorePoiSearch = PoiSearch.newInstance();
        }
        mLoadMoreSuggestionSearch = SuggestionSearch.newInstance();
        mLoadMoreSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
                    return;
                }
                mLoadMoreSuggest = new ArrayList<String>();
                for (SuggestionResult.SuggestionInfo info : suggestionResult.getAllSuggestions()) {
                    if (info.key != null) {
                        mLoadMoreSuggest.add(info.key);
                    }
                }

                Logger.e(mLoadMoreSuggest.toString());
            }
        });
        OnGetPoiSearchResultListener loadMorePoiListener = new OnGetPoiSearchResultListener() {
            public void onGetPoiResult(PoiResult result) {
                mLoadMoreDialog.dismiss();
                //获取POI检索结果
                if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    T.show(getActivity(), getResources().getString(R.string.no_more), 0);
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    List<PoiInfo> allPoi = result.getAllPoi();
                    for (PoiInfo poi : allPoi) {
                        MyPoiInfoEntity tem = new MyPoiInfoEntity();
                        tem.setPoiInfo(poi);
                        tem.setNavMenuDetailEntity(navMenuDetailEntity);
                        tem.setDistance2MyLocation(DistanceUtil.getDistance(mCurrentCenpt, poi.location));
                        mPoiInfoEntities.add(tem);
                    }
                    // notify data
                    mBottomSheetDialogView.notifyBottomSheetData(mPoiInfoEntities);
                    return;
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

                    // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
                    String strInfo = "在";
                    for (CityInfo cityInfo : result.getSuggestCityList()) {
                        strInfo += cityInfo.city;
                        strInfo += ",";
                    }
                    strInfo += "找到结果";
                    Toast.makeText(getActivity(), strInfo, Toast.LENGTH_LONG)
                            .show();
                }
            }

            public void onGetPoiDetailResult(PoiDetailResult result) {
                //获取Place详情页检索结果
                if (result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getActivity(), result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        };

        mLoadMorePoiSearch.setOnGetPoiSearchResultListener(loadMorePoiListener);

        if (isAllCity) {
            mLoadMorePoiSearch.searchInCity(new PoiCitySearchOption()
                    .city(mCurrentBDLocation.getCity())
                    .keyword(nearByPoiName)
                    .pageNum(bottomIndex)
                    .pageCapacity(10));

        } else {
            mLoadMorePoiSearch.searchNearby(new PoiNearbySearchOption().keyword(nearByPoiName)
                    .location(latLng)
                    .pageCapacity(10)
                    .pageNum(bottomIndex)
                    .radius(5000));
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Variables may be null
        if (mPoiSearch != null) {
            mPoiSearch.destroy();
        }
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        if (mSuggestionSearch != null) {
            mSuggestionSearch.destroy();
        }
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mBdLocationListener);
        }

        if (mOrientationListener != null) {
            mOrientationListener.stop();
            sensorManager.unregisterListener(mOrientationListener);
        }
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationEnabled(false);
        }


    }

    @Override
    public int getContentView() {
        return R.layout.activity_main_drawer;
    }

    /**
     * init location
     */
    private void initLocation(LocationClient locationClient) {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        locationClient.setLocOption(option);
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {
        mMapStateView.setmCurrentState(MapStateView.MAP_STATE.NO_CURRENT_LOCATION);
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {

    }

    @Override
    public void bindNavMenus(List<NavMenuEntity> navMenus) {

//        mRecyclerNavMenuContainer = (RecyclerView) view.findViewById(R.id.rlv_nav_menu_container);
//
//        mRecyclerNavMenuContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mNavMenuAdapter.setMenuEntities(navMenus);
//        mRecyclerNavMenuContainer.setAdapter(mNavMenuAdapter);
    }

    @Override
    public void bindNavMenusDetail(List<NavMenuDetailEntity> navMenuDetails) {
        mNavMenuDetails = navMenuDetails;
    }


    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            mCurrentBDLocation = location;
            if (sensor != null && sensorManager != null) {
                sensorManager.registerListener(mOrientationListener, sensor, SensorManager.SENSOR_DELAY_UI);
            }

            if (!isFirstEnter) {
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                // 设置定位数据
                mBaiduMap.setMyLocationData(locData);
                // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
//                BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
//                        .fromResource(R.mipmap.bg_location);

                MyLocationConfiguration.LocationMode locationMode = MyLocationConfiguration.LocationMode.NORMAL;
                MyLocationConfiguration config = new MyLocationConfiguration(locationMode,
                        true, null);
                mBaiduMap.setMyLocationConfigeration(config);

                //设定中心点坐标
                mCurrentCenpt = new LatLng(location.getLatitude(), location.getLongitude());

                //定义地图状态
                MapStatus mMapStatus = new MapStatus.Builder()
                        .target(mCurrentCenpt)
                        .zoom(18)
                        .build();
                //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                //改变地图状态
                mBaiduMap.animateMapStatus(mMapStatusUpdate);
                mBaiduMap.setOnMapStatusChangeListener(FriendslistFragment.this);
                isFirstEnter = true;

//                testPOI(cenpt);
//                testBusLine(cenpt);
//                testRoutePlan(cenpt);


            }

        }
    }

    /**
     * 测试路线规划
     *
     * @param cenpt
     */
    private void testRoutePlan(LatLng cenpt) {
        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    //result.getSuggestAddrInfo()
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap, useDefaultIcon);
                    mBaiduMap.setOnMarkerClickListener(overlay);
                    overlay.setData(result.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }
            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        });


        final PlanNode stNode = PlanNode.withLocation(cenpt);
//        PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京", "西二旗");
//        PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", "西三旗大地影城");

        /**
         * 输入要去地方检索路线
         */
        if (mPoiSearch == null) {
            mPoiSearch = PoiSearch.newInstance();
        }
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    List<PoiInfo> poiInfos = result.getAllPoi();

                    LatLng temp = poiInfos.get(0).location;


                    routePlanSearch.transitSearch((new TransitRoutePlanOption())
                            .from(stNode)
                            .city("北京")
                            .to(PlanNode.withLocation(temp)));

                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }
        });

        mPoiSearch.searchInCity(new PoiCitySearchOption().city("北京").keyword("大地影城"));


//        routePlanSearch.transitSearch((new TransitRoutePlanOption())
//                .from(stNode)
//                .city("北京")
//                .to(enNode));

    }


    /**
     * 测试公交检索
     *
     * @param cenpt
     */
    private void testBusLine(LatLng cenpt) {
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.searchInCity(new PoiCitySearchOption().city("北京").keyword("717"));
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    return;
                }
                //遍历所有POI，找到类型为公交线路的POI
                for (PoiInfo poi : result.getAllPoi()) {
                    if (poi.type == PoiInfo.POITYPE.BUS_LINE || poi.type == PoiInfo.POITYPE.SUBWAY_LINE) {
                        //说明该条POI为公交信息，获取该条POI的UID
                        busLineId = poi.uid;

                        // 在难道buslineiD后去调用搜索的方法
                        busLineSearch = BusLineSearch.newInstance();
                        busLineSearch.searchBusLine(new BusLineSearchOption().city("北京").uid(busLineId));
                        busLineSearch.setOnGetBusLineSearchResultListener(new OnGetBusLineSearchResultListener() {
                            @Override
                            public void onGetBusLineResult(BusLineResult busLineResult) {
                                Logger.e(busLineResult.getBusCompany() + busLineResult.getBusLineName() + busLineResult.getSteps().toString());
                            }
                        });


                        break;
                    }
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }
        });

    }


    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            // if (poi.hasCaterDetails) {
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poi.uid));
            // }
            return true;
        }
    }


    @Override
    public boolean onBackPressed() {
        if (mNavPopupWindow != null && mNavPopupWindow.isShowing()) {
            mNavPopupWindow.dismiss();
            return true;
        } else {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                Tools.doublePressExit(getActivity());
            }
            return false;
        }
    }

    /**
     * the method for drawer state
     */
    private void drawerOpenOrClose() {
        if (mDrawerLayout != null) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        }
    }

    /**
     * if need we can set different click type
     */
    private interface OnRecylerItemClickListener {
        void onRecylerItemClick(int clickType);

        void onRecylerItemLongClick(int clickType);
    }

    class NavMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<NavMenuEntity> menuEntities;
        private OnRecylerItemClickListener onRecylerItemClickListener;


        public void setMenuEntities(List<NavMenuEntity> menuEntities) {
            this.menuEntities = menuEntities;
        }

        public void setOnRecylerItemClickListener(OnRecylerItemClickListener onRecylerItemClickListener) {
            this.onRecylerItemClickListener = onRecylerItemClickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            NavMenuHolder navMenuHolder = new NavMenuHolder(LayoutInflater
                    .from(getActivity())
                    .inflate(R.layout.item_nav_menu, parent, false));
            return navMenuHolder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            final NavMenuEntity currentNavMenuEntity = menuEntities.get(position);
            NavMenuHolder navMenuHolder = (NavMenuHolder) holder;
            navMenuHolder.navMenuIcon.setImageResource(currentNavMenuEntity.getNavMenuIcon());
            navMenuHolder.navMenuDesc.setText(currentNavMenuEntity.getNavMenuName());
            if (currentNavMenuEntity.isShowBottomLine()) {
                navMenuHolder.navMenuBottomLine.setVisibility(View.VISIBLE);
            }
            if (currentNavMenuEntity.isBoldText()) {
                navMenuHolder.navMenuDesc.setTextColor(getResources().getColor(R.color.skin_colorPrimary_tea));
                navMenuHolder.navMenuDesc.setTypeface(Typeface.DEFAULT_BOLD);
            }

            if (onRecylerItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRecylerItemClickListener.onRecylerItemClick(currentNavMenuEntity.getClickType());
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onRecylerItemClickListener.onRecylerItemLongClick(currentNavMenuEntity.getClickType());
                        return false;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return menuEntities.size();
        }

        class NavMenuHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.iv_nav_item_icon)
            ImageView navMenuIcon;
            @BindView(R.id.tv_nav_item_desc)
            TextView navMenuDesc;
            @BindView(R.id.iv_nav_item_bottom_line)
            ImageView navMenuBottomLine;

            public NavMenuHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }

    /**
     * nav menu click type
     */
    public static class NAV_MENU_CLICK {
        public static final int _EAT = 33;
        public static final int _PA = 44;
        public static final int _PLAY = 55;
        public static final int _HAPPY = 66;
        public static final int _WHO = 77;
    }

    // TODO: 16/3/29   在activity初始化完毕后调用, 不要在oncreate中直接调用,必须在mainactivity中
    class NoUIThread extends Thread {

        @Override

        public void run() {


            Looper.prepare();

            TextView tx = new TextView(getActivity());
            tx.setBackgroundColor(getResources().getColor(R.color.common_red_color));
            tx.setTextColor(getResources().getColor(R.color.demo_title1));
            tx.setText("子线程增加自己的ViewRoot,即可刷新UI");


            WindowManager wm = getActivity().getWindowManager();

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(

                    250, 250, 200, 200, WindowManager.LayoutParams.FIRST_SUB_WINDOW,

                    WindowManager.LayoutParams.TYPE_TOAST, PixelFormat.OPAQUE);

            wm.addView(tx, params);
            Looper.loop();

        }

    }



}
