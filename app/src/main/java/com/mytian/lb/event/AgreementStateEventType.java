package com.mytian.lb.event;

/**
 * 约定状态
 * Created by bin on 15/7/23.
 */
public class AgreementStateEventType {

    public final static String AGREEMENT_END = "0";
    public final static String AGREEMENT_ING = "1";
    public String appointing;
    public String appointer;
    public String babyUid;
    public String appoint_time;

    public AgreementStateEventType(String babyUid, String appointing,String appointer,String appoint_time) {
        this.babyUid = babyUid;
        this.appointing = appointing;
        this.appointer = appointer;
        this.appoint_time = appoint_time;
    }
}
