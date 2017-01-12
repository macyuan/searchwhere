package com.mytian.lb.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.core.CommonResponse;
import com.core.util.CommonUtil;
import com.core.util.StringUtil;
import com.gitonway.lee.niftymodaldialogeffects.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.NiftyDialogBuilder;
import com.mytian.lb.App;
import com.mytian.lb.BuildConfig;
import com.mytian.lb.ParentDao;
import com.mytian.lb.R;
import com.mytian.lb.activityexpand.activity.AnimatedRectActivity;
import com.mytian.lb.bean.user.UserResult;
import com.mytian.lb.helper.AnimationHelper;
import com.mytian.lb.manager.LoginManager;
import com.mytian.lb.mview.EditTextWithDelete;
import com.rey.material.widget.CheckBox;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AnimatedRectActivity {

    public NiftyDialogBuilder dialogBuilder;
    public LayoutInflater mInflater;
    private LoginManager loginManager = new LoginManager();
    @BindView(R.id.toolbar_left_btn)
    TextView toolbarLeftBtn;
    @BindView(R.id.toolbar_intermediate_tv)
    TextView toolbarIntermediateTv;
    @BindView(R.id.phone_et)
    EditTextWithDelete phoneEt;
    @BindView(R.id.password_et)
    EditTextWithDelete passwordEt;
    @BindView(R.id.isnoaccount)
    CheckBox isNoAccount;

    private boolean isCancle;

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void animationStartEnd() {
        App.getInstance().activityManager.popOneActivityExcept(MainActivity.class);
    }

    @OnClick(R.id.register_bt)
    void toRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        String phone = phoneEt.getText().toString();
        if (StringUtil.isNotBlank(phone) && StringUtil.checkMobile(phone)) {
            intent.putExtra("phone", phone);
        }
        startActivity(intent);
    }

    @OnClick(R.id.reset_password_bt)
    void toResetPassword() {
        Intent intent = new Intent(this, ResetPassWordActivity.class);
        String phone = phoneEt.getText().toString();
        if (StringUtil.isNotBlank(phone) && StringUtil.checkMobile(phone)) {
            intent.putExtra("phone", phone);
        }
        startActivity(intent);
    }

    @OnClick(R.id.login_bt)
    void login() {
        String phone = phoneEt.getText().toString();
        String password = passwordEt.getText().toString();
        if (App.getInstance().isNoAccount()) {
            phone = "18217612175";
            password = "111111";
        }
        if (StringUtil.isBlank(phone) || !StringUtil.checkMobile(phone)) {
            AnimationHelper.getInstance().viewAnimationQuiver(phoneEt);
            return;
        }
        if (StringUtil.isBlank(password)) {
            AnimationHelper.getInstance().viewAnimationQuiver(passwordEt);
            return;
        }
        dialogShow(R.string.logining);
        loginManager.login(this, phone, password, activityHandler, LOGIN_ING);
    }

    private void toMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCancle = getIntent().getBooleanExtra("login", false);
        mInflater = LayoutInflater.from(this);
        ButterKnife.bind(this);
        setStatusBar();
        if (App.getInstance().getUserResult().getParent() != null) {
            String phone = App.getInstance().getUserResult().getParent().getPhone();
            if (StringUtil.isNotBlank(phone)) {
                phoneEt.setText(phone);
                phoneEt.setSelection(phone.length());
            }
        }
        if (!BuildConfig.BUILD_TYPE.equals("release")) {
            isNoAccount.setVisibility(View.VISIBLE);
            isNoAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    App.getInstance().setIsNoAccount(b);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (isCancle) {
            App.getInstance().exit();
        }
        super.onBackPressed();
    }

    /**
     * 设置statusbar全透明
     */
    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        toolbarIntermediateTv.setText(R.string.login);
        toolbarLeftBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
    }

    public void dialogShow(int title) {
        dialogDismiss();
        LinearLayout convertView = (LinearLayout) mInflater.inflate(R.layout.loading_view, null);
        TextView dialog_confirm_content = (TextView) convertView.findViewById(R.id.dialog_confirm_content);
        dialog_confirm_content.setText(title);
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0) {
                    return true;
                }
                return false;
            }
        });
        dialogBuilder.withDuration(700) // def
                .isCancelableOnTouchOutside(false) // def | isCancelable(true)
                .withEffect(Effectstype.Fadein) // def Effectstype.Slidetop
                .setCustomView(convertView, this); // .setCustomView(View
        activityHandler.sendEmptyMessage(DIALOGSHOW);

    }

    public void dialogDismiss() {
        if (null != dialogBuilder && dialogBuilder.isShowing()) {
            activityHandler.sendEmptyMessage(DIALOGDISMISS);
        }
    }

    private void loadLogin(CommonResponse resposne) {
        dialogDismiss();
        if (resposne.isSuccess()) {
            UserResult result = (UserResult) resposne.getData();
            loginSuccess(result);
        } else {
            if (App.getInstance().isNoAccount()) {
                loginSuccess(UserResult.testData());
                return;
            }
            CommonUtil.showToast(resposne.getMsg());
        }
    }

    private void loginSuccess(UserResult result) {
        App.getInstance().setUserResult(result);
        ParentDao dao = App.getDaoSession().getParentDao();
        dao.deleteAll();
        dao.insertInTx(result.getParent());
        toMain();
    }

    private final static int LOGIN_ING = 3;
    private final static int DIALOGSHOW = 1;
    private final static int DIALOGDISMISS = 0;


    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DIALOGSHOW:
                    dialogBuilder.show();
                    break;
                case DIALOGDISMISS:
                    dialogBuilder.dismiss();
                    break;
                case LOGIN_ING:
                    loadLogin((CommonResponse) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

}
