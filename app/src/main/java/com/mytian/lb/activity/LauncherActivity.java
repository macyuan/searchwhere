package com.mytian.lb.activity;


import android.content.Intent;
import android.os.Message;
import android.widget.RelativeLayout;

import com.core.util.CommonUtil;
import com.core.util.StringUtil;
import com.debug.ChannelUtil;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.App;
import com.mytian.lb.BuildConfig;
import com.mytian.lb.Parent;
import com.mytian.lb.R;
import com.mytian.lb.activityexpand.activity.AnimatedRectLayout;
import com.mytian.lb.helper.ShapeRevealSample;
import com.mytian.lb.imp.ECallOnClick;
import com.mytian.lb.manager.AppManager;
import com.mytian.lb.push.PushHelper;

import butterknife.BindView;
import butterknife.OnClick;
import su.levenetc.android.textsurface.TextSurface;

/**
 * 启动界面
 */
public class LauncherActivity extends AbsActivity {

    private final static int TO_LOGIN = 0;
    private final static int TO_GUIDE = 1;

    private int statue;

    @BindView(R.id.launcher_ly)
    RelativeLayout launcherLy;

    @BindView(R.id.text_surface)
    TextSurface textSurface;

    @Override
    public void EInit() {
        if (BuildConfig.CHANNEL_DEBUG) {
            String channel_id = ChannelUtil.getChannel(this);
            if (StringUtil.isNotBlank(channel_id)) {
                CommonUtil.showToast(channel_id);
            }
        }
        PushHelper.getInstance().initPush();
        super.EInit();
        setSwipeBackEnable(false);
        statue = TO_GUIDE;
        long time = 2000;
        mHandler.sendEmptyMessageDelayed(statue, time);
    }

    private void showGuideText() {
        textSurface.reset();
        ShapeRevealSample.play(textSurface, new ECallOnClick() {
            @Override
            public void callOnClick() {
                toLogining();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        launcherLy.setEnabled(true);
    }

    @OnClick(R.id.launcher_ly)
    void OnClickActivity() {
        launcherLy.setEnabled(false);
        textSurface.clearAnimation();
        mHandler.removeMessages(statue);
        statue = TO_LOGIN;
        mHandler.sendEmptyMessage(statue);
    }

    @Override
    public void onBackPressed() {
        mHandler.removeMessages(statue);
        App.getInstance().exit();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_launcher;
    }

    private void toLogining() {
        Parent parent = App.getInstance().getUserResult().getParent();
        if (null != parent) {
            toMain();
        } else {
            toLoginActivity();
        }
    }

    private void toLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("animation_type", AnimatedRectLayout.ANIMATION_WAVE_TR);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void toGuide() {
        textSurface.post(new Runnable() {
            @Override
            public void run() {
                showGuideText();
            }
        });
    }

    private void toMain() {
        if (AppManager.getInstance().isOUT()) {
            return;
        }
        Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void EDestroy() {
        super.EDestroy();
        mHandler.removeMessages(statue);
        textSurface.clearAnimation();
        textSurface = null;
    }

    @Override
    public void handlerCallBack(Message msg) {
        super.handlerCallBack(msg);
        int statue = msg.what;
        switch (statue) {
            case TO_LOGIN:
                toLogining();
                break;
            case TO_GUIDE:
                toGuide();
                break;
            default:
                break;
        }
    }

}
