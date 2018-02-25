package com.cls.manager.bean;

/**
 * Created by angcyo on 2018-02-25.
 */

public class RequestClassBean extends BaseBmob {
    private String request = "";//正在申请的教室
    private String success = "";//申请成功的教室
    private String field = "";//申请失败

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
