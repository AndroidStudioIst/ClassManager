package com.cls.manager.bean;

import android.text.TextUtils;

import com.angcyo.uiview.utils.RUtils;

import java.util.ArrayList;

/**
 * Created by angcyo on 2018-02-25.
 */

public class RequestClassBean extends BaseBmob {
    private String request = "";//正在申请的教室
    private String success = "";//申请成功的教室
    private String field = "";//申请失败
    private boolean needNotify = false; //审批成功后, 是否需要弹出通知

    //正在申请课室的老师信息
    public transient TeacherBean mTeacherBean;

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

    public void addSuccess(String value) {
        request = request.replaceAll(value, "");
        field = field.replaceAll(value, "");
        success = success + "," + value;
    }

    public void addRequest(String value) {
        field = field.replaceAll(value, "");
        success = success.replaceAll(value, "");
        request = request + "," + value;
    }

    public boolean isNeedNotify() {
        return needNotify;
    }

    public void setNeedNotify(boolean needNotify) {
        this.needNotify = needNotify;
    }

    /**备注列表*/
    private String w1Remark = ",,,,,,,,,";
    private String w2Remark = ",,,,,,,,,";
    private String w3Remark = ",,,,,,,,,";
    private String w4Remark = ",,,,,,,,,";
    private String w5Remark = ",,,,,,,,,";

    public ArrayList<String> w1RemarkList() {
        return RUtils.split(w1Remark, ",", true);
    }

    public ArrayList<String> w2RemarkList() {
        return RUtils.split(w2Remark, ",", true);
    }

    public ArrayList<String> w3RemarkList() {
        return RUtils.split(w3Remark, ",", true);
    }

    public ArrayList<String> w4RemarkList() {
        return RUtils.split(w4Remark, ",", true);
    }

    public ArrayList<String> w5RemarkList() {
        return RUtils.split(w5Remark, ",", true);
    }

    public boolean w1ListHave(int index) {
        return !TextUtils.isEmpty(RUtils.split(w1Remark, ",", true).get(index));
    }

    public String getW1Remark() {
        return w1Remark;
    }

    public void setW1Remark(String w1Remark) {
        this.w1Remark = w1Remark;
    }

    public String getW2Remark() {
        return w2Remark;
    }

    public void setW2Remark(String w2Remark) {
        this.w2Remark = w2Remark;
    }

    public String getW3Remark() {
        return w3Remark;
    }

    public void setW3Remark(String w3Remark) {
        this.w3Remark = w3Remark;
    }

    public String getW4Remark() {
        return w4Remark;
    }

    public void setW4Remark(String w4Remark) {
        this.w4Remark = w4Remark;
    }

    public String getW5Remark() {
        return w5Remark;
    }

    public void setW5Remark(String w5Remark) {
        this.w5Remark = w5Remark;
    }
}
