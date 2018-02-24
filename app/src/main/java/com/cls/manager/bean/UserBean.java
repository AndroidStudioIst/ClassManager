package com.cls.manager.bean;

import cn.bmob.v3.BmobObject;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2018/02/24 15:46
 * 修改人员：Robi
 * 修改时间：2018/02/24 15:46
 * 修改备注：
 * Version: 1.0.0
 */
public class UserBean extends BmobObject {
    private String name = "";
    private String pw = "";
    private int level = 1; //用户等级, 用来做权限控制
    private int type = 1; //用户类型, 角色区分   1:学生 2:老师 3:管理 4:超级
    private String ex1 = "";
    private String ex2 = "";
    private String ex3 = "";

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getEx1() {
        return ex1;
    }

    public void setEx1(String ex1) {
        this.ex1 = ex1;
    }

    public String getEx2() {
        return ex2;
    }

    public void setEx2(String ex2) {
        this.ex2 = ex2;
    }

    public String getEx3() {
        return ex3;
    }

    public void setEx3(String ex3) {
        this.ex3 = ex3;
    }
}
