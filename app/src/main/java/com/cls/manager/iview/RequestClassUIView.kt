package com.cls.manager.iview

import android.graphics.Color
import android.widget.TextView
import com.angcyo.bmob.RBmob
import com.angcyo.uiview.dialog.UILoading
import com.angcyo.uiview.kotlin.have
import com.angcyo.uiview.model.TitleBarPattern
import com.angcyo.uiview.recycler.RBaseViewHolder
import com.angcyo.uiview.utils.RUtils
import com.angcyo.uiview.utils.Tip
import com.cls.manager.R
import com.cls.manager.bean.RequestClassBean
import com.cls.manager.control.UserControl

/**
 * Created by angcyo on 2018-02-25.
 */

open class RequestClassUIView : AddTeacherUIView(true) {

    var requestClassBean = RequestClassBean().apply {
        name = UserControl.loginUserBean!!.name
    }

    override fun getTitleBar(): TitleBarPattern {
        return super.getTitleBar().setTitleString("申请实验课室")
    }

    override fun onShowContentData() {
        RBmob.query<RequestClassBean>(RequestClassBean::class.java, "name:${requestClassBean.name}") {
            if (!RUtils.isListEmpty(it)) {
                requestClassBean = it.first()
            }
            showContentLayout()
            uiTitleBarContainer.getRightView<TextView>(0).text = "申请"
        }
    }

    override fun initTeacher(holder: RBaseViewHolder, position: Int) {
        //super.initTeacher(holder, position)
        //val row = position / 6  //第几行
        val column = position.rem(6) //第几列

        when (column) {
            1 -> {//周一
                setTeacherItem(holder, teacherBean.w1, position)
            }
            2 -> {
                setTeacherItem(holder, teacherBean.w2, position)
            }
            3 -> {
                setTeacherItem(holder, teacherBean.w3, position)
            }
            4 -> {
                setTeacherItem(holder, teacherBean.w4, position)
            }
            5 -> {
                setTeacherItem(holder, teacherBean.w5, position)
            }
        }
    }

    override fun saveTeacher() {
        //super.saveTeacher()
        UILoading.progress(mParentILayout)
        RBmob.update<RequestClassBean>(RequestClassBean::class.java, requestClassBean, "name:${requestClassBean.name}") {
            if (it.isEmpty()) {
                Tip.tip("保存失败")
            } else {
                finishIView()
            }
            UILoading.hide()
        }
    }

    override fun setTeacherItem(holder: RBaseViewHolder, w: Int, position: Int) {
        //super.setTeacherItem(holder, w, position)

        val row = position / 6  //第几行
        val column = position.rem(6) //第几列

        val rowShl = 0b1.shl(row - 1)

        holder.clickItem { }

        if (w.have(rowShl)) {
            holder.itemView.setBackgroundColor(getColor(R.color.base_color_disable))
            holder.tv(R.id.text_view).text = "×"
            holder.tv(R.id.text_view).setTextColor(Color.WHITE)
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            holder.tv(R.id.text_view).text = "可申请"

            if (requestClassBean.success.contains("$row:$column")) {
                holder.tv(R.id.text_view).setTextColor(Color.WHITE)
                holder.itemView.setBackgroundColor(getColor(R.color.theme_color_accent))
                holder.tv(R.id.text_view).text = "已申请"
            } else if (requestClassBean.request.contains("$row:$column")) {
                holder.itemView.setBackgroundColor(getColor(R.color.base_chat_bg_color))
                holder.tv(R.id.text_view).text = "申请中"
            } else {
                if (requestClassBean.field.contains("$row:$column")) {
                    holder.tv(R.id.text_view).setTextColor(Color.WHITE)
                    holder.itemView.setBackgroundColor(Color.RED)
                    holder.tv(R.id.text_view).text = "申请失败"
                }
                holder.clickItem {
                    requestClassBean.addRequest("$row:$column")
                    mExBaseAdapter.notifyItemChanged(position)
                }
            }
        }
    }
}
