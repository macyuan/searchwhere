package com.mytian.lb.bean.push;

import com.core.openapi.OpenApiBaseRequestAdapter;
import com.core.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class UpdateChannelidParam extends OpenApiBaseRequestAdapter {

    private String uid;

    private String token;

    private String channelId;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public boolean validate() {
        if (StringUtil.isBlank(this.uid)) return false;
        if (StringUtil.isBlank(this.token)) return false;
        if (StringUtil.isBlank(this.channelId)) return false;
        return true;
    }

    @Override
    public void fill2Map(Map<String, Object> param, boolean includeEmptyAttr) {
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(uid)))
            param.put("uid", uid);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(token)))
            param.put("token", token);
        if (includeEmptyAttr || (!includeEmptyAttr && StringUtil.isNotBlank(channelId)))
            param.put("parent.channelId", channelId);

    }

    @Override
    public String toString() {
        return "UpdateChannelidParam{" +
                "uid='" + uid + '\'' +
                ", token='" + token + '\'' +
                ", channelId='" + channelId + '\'' +
                '}';
    }
}
