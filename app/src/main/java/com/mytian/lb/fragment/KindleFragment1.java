package com.mytian.lb.fragment;

import android.view.View;

import com.mytian.lb.AbsFragment;
import com.mytian.lb.R;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import butterknife.BindView;

public class KindleFragment1 extends AbsFragment {

    @BindView(R.id.webview)
    XWalkView xWalkView;

    @BindView(R.id.progress)
    ProgressWheel progress;

    @Override
    public int getContentView() {
        return R.layout.fragment_kindle;
    }

    @Override
    public void EInit() {
        super.EInit();
        //https://snapdrop.net
        webviewSetInit("https://snapdrop.net");
        //webviewSetInit("https://en.wikipedia.org/wiki/Android_(operating_system)");
    }

    //webview 属性 设置
    private void webviewSetInit(String url) {
        xWalkView.setUIClient(new XWalkUIClient(xWalkView) {
            @Override
            public void onPageLoadStarted(XWalkView view, String url) {
                super.onPageLoadStarted(view, url);
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageLoadStopped(XWalkView view, String url, LoadStatus status) {
                super.onPageLoadStopped(view, url, status);
                progress.setVisibility(View.GONE);
            }
        });
        xWalkView.load(url, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (xWalkView != null) {
            xWalkView.onDestroy();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (xWalkView != null) {
            xWalkView.pauseTimers();
            xWalkView.onHide();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (xWalkView != null) {
            xWalkView.resumeTimers();
            xWalkView.onShow();
        }
    }


    @Override
    public boolean onBackPressed() {
        // Go backward
        if (xWalkView != null && xWalkView.getNavigationHistory().canGoBack()) {
            xWalkView.getNavigationHistory().navigate(
                    XWalkNavigationHistory.Direction.BACKWARD, 1);
            return true;
        } else {
            return false;
        }
    }

}
