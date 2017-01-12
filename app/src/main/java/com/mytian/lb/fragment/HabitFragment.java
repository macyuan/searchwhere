package com.mytian.lb.fragment;

import android.widget.ListView;

import com.mytian.lb.AbsActivity;
import com.mytian.lb.AbsFragment;
import com.mytian.lb.R;
import com.mytian.lb.UserAction;
import com.mytian.lb.activity.UserDetailActivity;
import com.mytian.lb.adapter.HabitAdapter;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.manager.UserActionDOManager;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 习惯界面
 */
public class HabitFragment extends AbsFragment {

    @BindView(R.id.listview_pr)
    ListView listview;

    private FollowUser cureentParent;

    private ArrayList arrayList;

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_habit;
    }

    @Override
    public void EInit() {
        super.EInit();
        cureentParent = (FollowUser) getArguments().getSerializable(UserDetailActivity.USER);
        try {
            arrayList = deepCopy(UserActionDOManager.getInstance().getArrayListHabit());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        initListViewData(arrayList);
    }

    /**
     * 深度拷贝 行为习惯
     * 拷贝对象 需要 序列号
     * implements Serializable
     *
     * @param src
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ArrayList deepCopy(List src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        return (ArrayList) in.readObject();
    }

    private void initListViewData(ArrayList<UserAction> _arrayList) {

        HabitAdapter mAdapter = new HabitAdapter((AbsActivity) getActivity(), cureentParent, _arrayList);

        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);

        animationAdapter.setAbsListView(listview);

        listview.setAdapter(animationAdapter);
    }

}
