package com.mytian.lb.manager;

import android.content.Context;
import android.os.Handler;

import com.alibaba.fastjson.TypeReference;
import com.core.CommonDataLoader;
import com.core.CommonRequest;
import com.core.openapi.OpenApiMethodEnum;
import com.core.openapi.OpenApiSimpleResult;
import com.mytian.lb.App;
import com.mytian.lb.Parent;
import com.mytian.lb.bean.push.UpdateChannelidParam;

/**
 * 推送业务类.
 *
 * @author bin.teng
 */
public class PushMManager {

    /**
     * 上传channelId
     *
     * @param context        上下文
     * @param channelId      channelId
     * @param handler        在Activity中处理返回结果的Handler
     * @param handlerMsgCode 返回结果的Handler的Msg代码
     */
    public void updateChannelId(Context context, String channelId, final Handler handler, final int handlerMsgCode) {
        UpdateChannelidParam param = new UpdateChannelidParam();
        Parent parent = App.getInstance().getUserResult().getParent();

        param.setUid(parent.getUid());
        param.setToken(parent.getToken());
        param.setChannelId(channelId);

        // 接口参数
        param.setMethod(OpenApiMethodEnum.LOAD_UPDATECHANNELID);
        param.setParseTokenType(new TypeReference<OpenApiSimpleResult>() {
        });
        // 请求对象
        CommonRequest request = new CommonRequest(param, handler, handlerMsgCode);
        // 开始执行加载
        CommonDataLoader.getInstance(context).load(request);
    }


}
