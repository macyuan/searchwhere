package com.lifehelper.ui.customwidget;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lifehelper.entity.BottomSheetEntity;
import com.lifehelper.entity.MyPoiInfoEntity;
import com.mytian.lb.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jsion on 16/3/14.
 */
public class BottomSheetDialogView {
    private Context context;
    private BottomSheetEntity bottomSheetEntity;
    private List<MyPoiInfoEntity> bottomPoiData;
    @BindView(R.id.tv_bottom_desc)
    TextView bottomDesc;
    @BindView(R.id.rlv_bottom_all)
    RecyclerView bottomAll;
    private BottomSheetDeAdapter bottomSheetDeAdapter;

    public interface OnRecyclerScrollBottomListener {
        void recyclerScrollBottom();
    }

    public BottomSheetDialogView(Context context, BottomSheetEntity bottomSheetEntity) {
        this.context = context;
        this.bottomSheetEntity = bottomSheetEntity;
        View bottomView = LayoutInflater.from(context).inflate(R.layout.layout_bottom_sheet, null);
        ButterKnife.bind(this, bottomView);
        initEvent(bottomSheetEntity);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(bottomView);
        bottomSheetDialog.show();
    }


    public BottomSheetDialogView(Context context, BottomSheetEntity bottomSheetEntity, OnRecyclerScrollBottomListener onRecyclerScrollBottomListener) {
        this.context = context;
        this.bottomSheetEntity = bottomSheetEntity;
        View bottomView = LayoutInflater.from(context).inflate(R.layout.layout_bottom_sheet, null);
        ButterKnife.bind(this, bottomView);
        initEvent(bottomSheetEntity, onRecyclerScrollBottomListener);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(bottomView);
        bottomSheetDialog.show();
    }

    private void initEvent(BottomSheetEntity bottomSheetEntity) {
        this.bottomPoiData = bottomSheetEntity.getPoiInfoEntities();
        bottomDesc.setText(bottomSheetEntity.getNavMenuDetailEntity().getNavMenuDetailTitle());
        bottomDesc.setBackgroundColor(context.getResources().getColor(bottomSheetEntity.getNavMenuDetailEntity().getNavMenuDetailColor()));

        bottomAll.setLayoutManager(new LinearLayoutManager(context));
        bottomSheetDeAdapter = new BottomSheetDeAdapter();
//        bottomSheetDeAdapter = new BottomSheetDeAdapter(bottomPoiData);
        bottomAll.setAdapter(bottomSheetDeAdapter);

    }


    private void initEvent(BottomSheetEntity bottomSheetEntity, final OnRecyclerScrollBottomListener onRecyclerScrollBottomListener) {
        this.bottomPoiData = bottomSheetEntity.getPoiInfoEntities();
        bottomDesc.setText(bottomSheetEntity.getNavMenuDetailEntity().getNavMenuDetailTitle());
        bottomDesc.setBackgroundColor(context.getResources().getColor(bottomSheetEntity.getNavMenuDetailEntity().getNavMenuDetailColor()));
        bottomAll.setLayoutManager(new LinearLayoutManager(context));
        bottomSheetDeAdapter = new BottomSheetDeAdapter();
//        bottomSheetDeAdapter = new BottomSheetDeAdapter(bottomPoiData);
        bottomAll.setAdapter(bottomSheetDeAdapter);

        bottomAll.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int itemCount = linearLayoutManager.getItemCount();
                    int lastVisiblePostion = linearLayoutManager.findLastVisibleItemPosition();
                    if (itemCount - 1 == lastVisiblePostion) {
                        // recylerview has scroll the end ,should load more
                        if (onRecyclerScrollBottomListener != null) {
                            onRecyclerScrollBottomListener.recyclerScrollBottom();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }


    public static void bottomSheetShow(Context context, BottomSheetEntity bottomSheetEntity) {
        new BottomSheetDialogView(context, bottomSheetEntity);
    }

    public static BottomSheetDialogView bottomSheetShow(Context context, BottomSheetEntity bottomSheetEntity, OnRecyclerScrollBottomListener onRecyclerScrollBottomListener) {
        return new BottomSheetDialogView(context, bottomSheetEntity, onRecyclerScrollBottomListener);
    }

    public interface OnRecyClickListener {
        void onReClick(MyPoiInfoEntity poiInfoEntity);
    }

    private OnRecyClickListener onRecyClickListener;

    public void setOnRecyClickListener(OnRecyClickListener onRecyClickListener) {
        this.onRecyClickListener = onRecyClickListener;
    }

    class BottomSheetDeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//        private List<MyPoiInfoEntity> recyclerData;
//
//        public BottomSheetDeAdapter(List<MyPoiInfoEntity> recyclerData) {
//            this.recyclerData = recyclerData;
//        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            BottomVH bottomVH = new BottomVH(LayoutInflater.from(context).inflate(R.layout.item_bottom_sheet, parent, false));
            return bottomVH;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final MyPoiInfoEntity currentEntity = bottomPoiData.get(position);
//            MyPoiInfoEntity currentEntity = recyclerData.get(position);
            BottomVH bottomVH = (BottomVH) holder;
            bottomVH.bottomIcon.setImageResource(currentEntity.getNavMenuDetailEntity().getNavMenuDetailIcon());
            bottomVH.bottomName.setText(currentEntity.getPoiInfo().name);
            bottomVH.bottomAddress.setText(currentEntity.getPoiInfo().address);
            bottomVH.bottomDistance.setText(String.format("%.1f", currentEntity.getDistance2MyLocation() / 1000) + "KM");
            bottomVH.bottomDistance.setTextColor(context.getResources().getColor(currentEntity.getNavMenuDetailEntity().getNavMenuDetailColor()));

            if (onRecyClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRecyClickListener.onReClick(currentEntity);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return bottomPoiData.size();
//            return recyclerData.size();
        }

        class BottomVH extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_item_bottom_name)
            TextView bottomName;
            @BindView(R.id.tv_item_bottom_address)
            TextView bottomAddress;
            @BindView(R.id.tv_item_bottom_distance)
            TextView bottomDistance;
            @BindView(R.id.iv_item_bottom_sheet)
            ImageView bottomIcon;

            public BottomVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public void notifyBottomSheetData(List<MyPoiInfoEntity> myPoiInfoEntities) {
        this.bottomPoiData = myPoiInfoEntities;
        bottomSheetDeAdapter.notifyDataSetChanged();
    }

}
