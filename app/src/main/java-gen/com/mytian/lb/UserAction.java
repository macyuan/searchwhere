package com.mytian.lb;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import java.io.Serializable;
import java.util.Date;

/**
 * Entity mapped to table "USER_ACTION".
 */
public class UserAction implements Serializable {

    /**
     * 扩展
     */
    public final static String GREAT = "1";
    public final static String BAD = "0";
    private String record;

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    private Long id;
    private String type;
    private String title;
    private String appointId;
    private String icon;
    private String icon_disabled;
    private Date date;

    public UserAction() {
    }

    public UserAction(Long id) {
        this.id = id;
    }

    public UserAction(Long id, String type, String title, String appointId, String icon, String icon_disabled, Date date) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.appointId = appointId;
        this.icon = icon;
        this.icon_disabled = icon_disabled;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAppointId() {
        return appointId;
    }

    public void setAppointId(String appointId) {
        this.appointId = appointId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon_disabled() {
        return icon_disabled;
    }

    public void setIcon_disabled(String icon_disabled) {
        this.icon_disabled = icon_disabled;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
