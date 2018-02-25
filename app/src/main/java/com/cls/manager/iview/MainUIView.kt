package com.cls.manager.iview

import android.content.pm.ActivityInfo
import android.view.LayoutInflater
import com.angcyo.uiview.container.ContentLayout
import com.angcyo.uiview.kotlin.clickIt
import com.cls.manager.R
import com.cls.manager.base.BaseContentUIView
import com.cls.manager.control.UserControl
import com.orhanobut.hawk.Hawk

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2018/02/23 11:12
 * 修改人员：Robi
 * 修改时间：2018/02/23 11:12
 * 修改备注：
 * Version: 1.0.0
 */
class MainUIView : BaseContentUIView() {

//    override fun getTitleBar(): TitleBarPattern {
//        return super.getTitleBar()
//                .setTitleBarBGColor(Color.RED)
//                .addRightItem(TitleBarPattern.TitleBarItem("+") {
//                    startIView(MainUIView())
//                })
//    }

    override fun isHaveTitleBar(): Boolean {
        return false
    }

    override fun getDefaultRequestedOrientation(): Int {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun inflateContentLayout(baseContentLayout: ContentLayout?, inflater: LayoutInflater?) {
        inflate(R.layout.activity_main)
    }

    override fun initOnShowContentLayout() {
        super.initOnShowContentLayout()
        UserControl.loginUserBean?.let {
            when (it.type) {
                1 -> {
                    //学生
                    mViewHolder.tv(R.id.add_teacher).text = "添加课表"
                    click(R.id.add_teacher) {
                        startIView(AddTeacherUIView(false))
                    }

                    //添加课程
                    mViewHolder.visible(R.id.add_lesson).clickIt {
                        startIView(AddLessonUIView())
                    }
                }
                2 -> {
                    //老师
                    mViewHolder.tv(R.id.add_teacher).text = "添加课表"
                    click(R.id.add_teacher) {
                        startIView(AddTeacherUIView())
                    }

                    //申请实验课室
                    mViewHolder.visible(R.id.request_class).clickIt {
                        startIView(RequestClassUIView())
                    }
                }
                3 -> {
                    //管理
                    mViewHolder.gone(R.id.add_teacher)
                }
                4 -> {
                    //超级
                    mViewHolder.gone(R.id.add_teacher)

                }
            }
        }

        //退出登录
        click(R.id.quit_button) {
            UserControl.loginUserBean = null
            Hawk.put("AUTO_LOGIN", false)
            replaceIView(LoginUIView())
        }
    }
}
