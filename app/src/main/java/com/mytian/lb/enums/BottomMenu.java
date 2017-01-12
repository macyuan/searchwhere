package com.mytian.lb.enums;


import com.mytian.lb.R;

/**
 * tab菜单枚举类.
 *
 * @author
 */
public enum BottomMenu {

    //    主界面
    //动态
    DYNAMIC(R.string.menu_dynamic,R.string.menu_dynamic, R.mipmap.main_tab_news_down, R.mipmap.main_tab_news_normal, 0xFFFFD704, 0xFF333333),
    //爱的约定
    AGREEMENT(R.string.menu_follow,R.string.menu_follow, R.mipmap.main_tab_love_down, R.mipmap.main_tab_love_normal, 0xFFFFD704, 0xFF333333),
    //其他
    KINDLE(R.string.menu_kindle,R.string.menu_kindle, R.mipmap.main_tab_kindle_down, R.mipmap.main_tab_kindle_normal, 0xFFFFD704, 0xFF333333),
    //帐号管理
    USER(R.string.menu_user, R.string.menu_user,R.mipmap.main_tab_me_down, R.mipmap.main_tab_me_normal, 0xFFFFD704, 0xFF333333);


    private int title;
    private int title_press;
    private int resid_press;
    private int resid_normal;
    private int title_colos_press;
    private int title_colos_normal;

    BottomMenu(int title,int title_press ,int resid_press, int resid_normal, int title_colos_press, int title_colos_normal) {
        this.title = title;
        this.title_press = title_press;
        this.resid_press = resid_press;
        this.resid_normal = resid_normal;
        this.title_colos_press = title_colos_press;
        this.title_colos_normal = title_colos_normal;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getTitle_press() {
        return title_press;
    }

    public void setTitle_press(int title_press) {
        this.title_press = title_press;
    }

    public int getResid_press() {
        return resid_press;
    }

    public void setResid_press(int resid_press) {
        this.resid_press = resid_press;
    }

    public int getResid_normal() {
        return resid_normal;
    }

    public void setResid_normal(int resid_normal) {
        this.resid_normal = resid_normal;
    }

    public int getTitle_colos_press() {
        return title_colos_press;
    }

    public void setTitle_colos_press(int title_colos_press) {
        this.title_colos_press = title_colos_press;
    }

    public int getTitle_colos_normal() {
        return title_colos_normal;
    }

    public void setTitle_colos_normal(int title_colos_normal) {
        this.title_colos_normal = title_colos_normal;
    }

    @Override
    public String toString() {
        return "BottomMenu{" +
                "title=" + title +
                ", title_press=" + title_press +
                ", resid_press=" + resid_press +
                ", resid_normal=" + resid_normal +
                ", title_colos_press=" + title_colos_press +
                ", title_colos_normal=" + title_colos_normal +
                '}';
    }
}
