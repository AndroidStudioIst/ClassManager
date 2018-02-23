package com.cls.manager.iview

import android.view.LayoutInflater
import com.angcyo.uiview.container.ContentLayout
import com.cls.manager.R
import com.cls.manager.base.BaseContentUIView

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

    override fun inflateContentLayout(baseContentLayout: ContentLayout?, inflater: LayoutInflater?) {
        inflate(R.layout.activity_main)
    }

}