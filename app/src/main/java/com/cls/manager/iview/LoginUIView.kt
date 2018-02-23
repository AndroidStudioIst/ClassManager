package com.cls.manager.iview

import android.view.Gravity
import com.angcyo.uiview.base.Item
import com.angcyo.uiview.base.SingleItem
import com.angcyo.uiview.model.TitleBarPattern
import com.angcyo.uiview.recycler.RBaseViewHolder
import com.cls.manager.R
import com.cls.manager.base.BaseItemUIView

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2018/02/23 18:23
 * 修改人员：Robi
 * 修改时间：2018/02/23 18:23
 * 修改备注：
 * Version: 1.0.0
 */
class LoginUIView : BaseItemUIView() {

    override fun getTitleBar(): TitleBarPattern {
        return super.getTitleBar().setTitleGravity(Gravity.LEFT).setTitleString("请登录")
    }

    override fun createItems(items: MutableList<SingleItem>) {
        items.add(object : SingleItem() {
            override fun onBindView(holder: RBaseViewHolder, posInData: Int, dataBean: Item?) {

            }

            override fun getItemLayoutId(): Int {
                return R.layout.item_login
            }

        })
    }

}