package com.mytian.lb.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mytian.lb.AbsFragment;
import com.mytian.lb.R;
import com.onlylemi.mapview.BitmapLayerTestActivity;
import com.onlylemi.mapview.LocationLayerTestActivity;
import com.onlylemi.mapview.MapLayerTestActivity;
import com.onlylemi.mapview.MarkLayerTestActivity;
import com.onlylemi.mapview.RouteLayerTestActivity;

import butterknife.BindView;

public class KindleFragment extends AbsFragment {

    @BindView(R.id.mapview_lv)
    ListView maplayerListView;

    private static final String TAG = "KindleFragment";


    private ArrayAdapter<String> mAdapter;


     private Class[] classes = {MapLayerTestActivity.class, BitmapLayerTestActivity.class,
     LocationLayerTestActivity.class, MarkLayerTestActivity.class, RouteLayerTestActivity
     .class};
//        private Class[] classes = {RouteLayerTestActivity.class};

    @Override
    public int getContentView() {
        return R.layout.activity_list;
    }

    @Override
    public void EInit() {
        super.EInit();
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.maplayer_name));
        maplayerListView.setAdapter(mAdapter);
        maplayerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, classes[position].getSimpleName());
                startActivity(new Intent(getActivity(), classes[position]));
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

}
