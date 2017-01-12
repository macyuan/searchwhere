package com.mytian.lb.fragment;


import android.content.Intent;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.core.CommonResponse;
import com.handmark.pulltorefresh.PullToRefreshBase;
import com.handmark.pulltorefresh.PullToRefreshListView;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.AbsFragment;
import com.mytian.lb.R;
import com.mytian.lb.activity.UserDetailActivity;
import com.mytian.lb.adapter.FriendslistAdapter;
import com.mytian.lb.bean.follow.FollowListResult;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.event.AgreementStateEventType;
import com.mytian.lb.event.PushStateEventType;
import com.mytian.lb.event.UpdateBabyAliasEventType;
import com.mytian.lb.manager.FollowManager;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * 关注的人界面
 */
public class FriendslistFragment1 extends AbsFragment {

    @BindView(R.id.listview_pr)
    PullToRefreshListView listview;
    @BindView(R.id.ll_listEmpty)
    LinearLayout llListEmpty;

    private ListView mActualListView;
    private FriendslistAdapter mAdapter;
    private int currentPager = 1;
    private FollowManager manager = new FollowManager();

    private ArrayMap<String, FollowUser> arrayList;

    @Override
    public void EInit() {
        super.EInit();
        initListView();
        dialogShow();
        getListData(INIT_LIST);
    }

    private void initListView() {

        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getListData(INIT_LIST);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getListData(LOAD_DATA);
            }
        });

        mActualListView = listview.getRefreshableView();

        // Need to use the Actual ListView when registering for Context Menu
        registerForContextMenu(mActualListView);

        mAdapter = new FriendslistAdapter((AbsActivity) getActivity(), arrayList);

        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);

        animationAdapter.setAbsListView(mActualListView);

        mActualListView.setAdapter(animationAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FollowUser followUser = arrayList.valueAt(position - 1);
                toUserDetailActivity(followUser);
            }
        });
    }

    private void toUserDetailActivity(FollowUser user) {
        Intent intent = new Intent(getActivity(), UserDetailActivity.class);
        intent.putExtra(UserDetailActivity.USER, user);
        startActivity(intent);
    }

    private void getListData(int state) {
        if (state == INIT_LIST) {
            currentPager = 1;
            listview.setMode(PullToRefreshBase.Mode.BOTH);
            arrayList = null;
        }
        manager.followList(getContext(), "" + currentPager, "1", mHandler, state);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_friends_list;
    }

    private static final int INIT_LIST = 0x01;//初始化数据处理
    private static final int LOAD_DATA = 0x02;//加载数据处理
    private static final int COUNT_MAX = 12;//加载数据最大值

    @Override
    public void handlerCallBack(Message msg) {
        super.handlerCallBack(msg);
        int what = msg.what;
        switch (what) {
            case INIT_LIST:
            case LOAD_DATA:
                loadData((CommonResponse) msg.obj);
                break;
            default:
                break;
        }
    }

    /**
     * 线上状态更新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PushStateEventType event) {
        String babyUid = event.babyUid;
        if (arrayList != null && arrayList.containsKey(babyUid)) {
            FollowUser followUser = arrayList.get(babyUid);
            followUser.setIs_online(event.is_online);
            arrayList.put(babyUid, followUser);
            mAdapter.refresh(babyUid, followUser);
        }
    }

    /**
     * 更新关注对象备注昵称
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(UpdateBabyAliasEventType event) {
        String babyUid = event.babyUid;
        if (arrayList != null && arrayList.containsKey(babyUid)) {
            FollowUser followUser = arrayList.get(babyUid);
            followUser.setBaby_alias(event.baby_alias);
            arrayList.put(babyUid, followUser);
        }
    }

    /**
     * 约定状态更新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AgreementStateEventType event) {
        String babyUid = event.babyUid;
        if (arrayList != null && arrayList.containsKey(babyUid)) {
            FollowUser followUser = arrayList.get(babyUid);
            followUser.setAppointing(event.appointing);
            arrayList.put(babyUid, followUser);
            mAdapter.refresh(babyUid, followUser);
        }
    }

    private void loadData(CommonResponse resposne) {
        dialogDismiss();
        listview.onRefreshComplete();
        if (resposne.isSuccess()) {
            FollowListResult result = (FollowListResult) resposne.getData();
            List<FollowUser> list = result.getList();
            int size = list == null ? 0 : list.size();
            if (arrayList == null) {
                arrayList = new ArrayMap<>();
            }
            if (size > 0) {
                for (FollowUser followUser : list) {
                    arrayList.put(followUser.getUid(), followUser);
                }
            }
            mAdapter.refresh(arrayList);
            if (size >= COUNT_MAX) {
                currentPager++;
            } else {
                listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
        }
        if (arrayList == null || arrayList.size() <= 0) {
            llListEmpty.setVisibility(View.VISIBLE);
        } else {
            llListEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

}
