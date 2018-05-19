package com.cls.manager.iview

import com.angcyo.uiview.model.TitleBarPattern
import com.angcyo.uiview.recycler.RBaseViewHolder
import com.angcyo.uiview.utils.RUtils
import com.cls.manager.R
import com.cls.manager.bean.TeacherBean

/**
 * Created by angcyo on 2018/05/19 09:05
 */
class SeeVerifyClassUIView : VerifyClassUIView() {
    override fun getTitleBar(): TitleBarPattern {
        return super.getTitleBar().setTitleString("实验课室情况")
    }

    override fun onShowContentData() {
        super.onShowContentData()
        uiTitleBarContainer.hideRightItem(0) //不需要按钮
    }

    override fun setTeacherItem(holder: RBaseViewHolder, w: Int, position: Int) {
        //super.setTeacherItem(holder, w, position)
        val row = position / 6  //第几行
        val column = position.rem(6) //第几列

        if (RUtils.isListEmpty(mExBaseAdapter.allDatas)) {
            mExBaseAdapter.allDatas = MutableList(mExBaseAdapter.itemCount) {
                TeacherBean()
            }
        }

        val teacherBean = mExBaseAdapter.allDatas[position]

        requestClassList.mapIndexed { _, requestClassBean ->
            if (requestClassBean.success.contains("$row:$column")) {
                teacherBean.selectorRequest = requestClassBean
            }
        }

        holder.clickItem { }
        holder.tv(R.id.text_view).text = teacherBean.selectorRequest.name
    }
}