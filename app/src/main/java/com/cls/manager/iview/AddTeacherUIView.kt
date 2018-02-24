package com.cls.manager.iview

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.angcyo.bmob.RBmob
import com.angcyo.uiview.container.ContentLayout
import com.angcyo.uiview.dialog.UIItemSelectorDialog
import com.angcyo.uiview.dialog.UILoading
import com.angcyo.uiview.kotlin.add
import com.angcyo.uiview.kotlin.have
import com.angcyo.uiview.kotlin.onSizeChanged
import com.angcyo.uiview.kotlin.remove
import com.angcyo.uiview.model.TitleBarPattern
import com.angcyo.uiview.net.RLoadingSubscriber
import com.angcyo.uiview.recycler.RBaseItemDecoration
import com.angcyo.uiview.recycler.RBaseViewHolder
import com.angcyo.uiview.recycler.RRecyclerView
import com.angcyo.uiview.recycler.adapter.RExBaseAdapter
import com.angcyo.uiview.rsen.RefreshLayout
import com.angcyo.uiview.skin.SkinHelper
import com.angcyo.uiview.utils.RUtils
import com.angcyo.uiview.utils.Tip
import com.angcyo.uiview.utils.UI
import com.cls.manager.R
import com.cls.manager.base.BaseSingleRecyclerUIView
import com.cls.manager.bean.LessonBean
import com.cls.manager.bean.TeacherBean
import com.cls.manager.control.UserControl

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：老师课程安排, 周一 到 周五, 第一节课 到 第十节课
 * 创建人员：Robi
 * 创建时间：2018/02/23 15:09
 * 修改人员：Robi
 * 修改时间：2018/02/23 15:09
 * 修改备注：
 * Version: 1.0.0
 */
class AddTeacherUIView(val isTeacher: Boolean = true) : BaseSingleRecyclerUIView<TeacherBean>() {

    var teacherBean = TeacherBean().apply {
        name = UserControl.loginUserBean!!.name
    }

    private val name: String
        get() {
            return if (isTeacher) "老师名称" else "学生名称"
        }
    private val titleName: String
        get() {
            return if (isTeacher) "添加老师课表" else "添加学生课表"
        }

    private val titleNameTip: String
        get() {
            return if (isTeacher) "请输入老师名称" else "请输入学生名称"
        }

    private val existTip: String
        get() {
            return if (isTeacher) "老师已存在, 是否更新数据?" else "学生已存在, 是否更新数据?"
        }


    override fun getTitleBar(): TitleBarPattern {
        return super.getTitleBar()
                .setTitleString(titleName)
                .addRightItem(TitleBarPattern.TitleBarItem("保存") {
                    if (isTeacher) {
                        saveTeacher()
                    } else {
                        saveStudent()
                    }
                })
    }

    private fun saveTeacher() {
        if (teacherBean.name.isEmpty()) {
            Tip.tip(titleNameTip)
        } else {
            UILoading.show2(mParentILayout)
            val query = BmobQuery<TeacherBean>()
            query.addWhereEqualTo("name", teacherBean.name)
            query.findObjects(object : FindListener<TeacherBean>() {
                override fun done(p0: MutableList<TeacherBean>?, p1: BmobException?) {
                    if (p0?.isNotEmpty() != false) {
                        //Tip.tip("老师已存在, 是否更新数据?")
//                        UILoading.hide()
//                        UIDialog.build()
//                                .setDialogContent(existTip)
//                                .setOkListener {
//                                    teacherBean.updateObservable(p0!![0].objectId)
//                                            .subscribe(object : RLoadingSubscriber<Void>(mParentILayout) {
//
//                                                override fun onSucceed(bean: Void?) {
//                                                    super.onSucceed(bean)
//                                                    Tip.tip("更新课表成功")
//                                                    finishIView()
//                                                }
//
//                                                override fun onError(code: Int, msg: String?) {
//                                                    super.onError(code, msg)
//                                                    Tip.tip(msg)
//                                                }
//                                            })
//                                }
//                                .showDialog(mParentILayout)

                        teacherBean.updateObservable(p0!![0].objectId)
                                .subscribe(object : RLoadingSubscriber<Void>(mParentILayout) {

                                    override fun onSucceed(bean: Void?) {
                                        super.onSucceed(bean)
                                        Tip.tip("更新课表成功")
                                        finishIView()
                                    }

                                    override fun onError(code: Int, msg: String?) {
                                        super.onError(code, msg)
                                        Tip.tip(msg)
                                    }
                                })
                    } else {
                        teacherBean.saveObservable()
                                .subscribe(object : RLoadingSubscriber<String>(mParentILayout) {

                                    override fun onSucceed(bean: String?) {
                                        super.onSucceed(bean)
                                        Tip.tip("添加课表成功")
                                        finishIView()
                                    }

                                    override fun onError(code: Int, msg: String?) {
                                        super.onError(code, msg)
                                        Tip.tip(msg)
                                    }
                                })
                    }
                }
            })
        }
    }

    private fun saveStudent() {
        saveTeacher()
    }

    override fun createAdapter(): RExBaseAdapter<String, TeacherBean, String> = object : RExBaseAdapter<String, TeacherBean, String>(mActivity) {
        override fun getItemCount(): Int {
            return 66
        }

        override fun getItemLayoutId(viewType: Int): Int {
            return R.layout.item_add_teacher
        }

        override fun onBindCommonView(holder: RBaseViewHolder, position: Int, bean: TeacherBean?) {
            super.onBindCommonView(holder, position, bean)
            if (mRecyclerView.measuredHeight > 0) {
                UI.setViewHeight(holder.itemView, mRecyclerView.measuredHeight / 11)
            }

            holder.tv(R.id.text_view).setTextColor(getColor(R.color.base_text_color))
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)

            when (position) {
                0 -> {
                    holder.tv(R.id.text_view).text = if (teacherBean.name.isEmpty()) {
                        name
                    } else {
                        teacherBean.name
                    }
//                    holder.clickItem {
//                        startIView(UIInputExDialog().apply {
//                            inputDefaultString = teacherBean.name
//                            inputHintString = titleNameTip
//                            maxInputLength = 4
//                            onInputTextResult = {
//                                teacherBean.name = it
//                                notifyItemChanged(position)
//                            }
//                        })
//                    }
                }
                1 -> {
                    holder.tv(R.id.text_view).text = "一"
                }
                2 -> {
                    holder.tv(R.id.text_view).text = "二"
                }
                3 -> {
                    holder.tv(R.id.text_view).text = "三"
                }
                4 -> {
                    holder.tv(R.id.text_view).text = "四"
                }
                5 -> {
                    holder.tv(R.id.text_view).text = "五"
                }
                1 * 6 -> {
                    holder.tv(R.id.text_view).text = "${position / 6}"
                }
                2 * 6 -> {
                    holder.tv(R.id.text_view).text = "${position / 6}"
                }
                3 * 6 -> {
                    holder.tv(R.id.text_view).text = "${position / 6}"
                }
                4 * 6 -> {
                    holder.tv(R.id.text_view).text = "${position / 6}"
                }
                5 * 6 -> {
                    holder.tv(R.id.text_view).text = "${position / 6}"
                }
                6 * 6 -> {
                    holder.tv(R.id.text_view).text = "${position / 6}"
                }
                7 * 6 -> {
                    holder.tv(R.id.text_view).text = "${position / 6}"
                }
                8 * 6 -> {
                    holder.tv(R.id.text_view).text = "${position / 6}"
                }
                9 * 6 -> {
                    holder.tv(R.id.text_view).text = "${position / 6}"
                }
                10 * 6 -> {
                    holder.tv(R.id.text_view).text = "${position / 6}"
                }
                else -> {
                    if (!isTeacher) {
                        holder.clickItem {
                            if (RUtils.isListEmpty(lessonBeanList)) {
                                Tip.tip("需要先添加课程\n才能选择")
                                return@clickItem
                            }

                            startIView(UIItemSelectorDialog(lessonBeanList).apply {
                                onInitItemLayout = { holder, posInData, dataBean ->
                                    holder.tv(R.id.base_text_view).text = dataBean.name
                                }
                                onItemSelector = { _, bean ->
                                    holder.tv(R.id.text_view).text = bean.name
                                    holder.tv(R.id.text_view).setTextColor(Color.WHITE)
                                    holder.itemView.setBackgroundColor(SkinHelper.getSkin().themeSubColor)
                                }
                            })
                        }
                        return
                    }

                    val shl = 0b1.shl(position / 6 - 1)

                    fun setBg(w: Int) {
                        if (w.have(shl)) {
                            holder.itemView.setBackgroundColor(SkinHelper.getSkin().themeSubColor)
                            holder.tv(R.id.text_view).text = "√"
                            holder.tv(R.id.text_view).setTextColor(Color.WHITE)
                        } else {
                            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
                            holder.tv(R.id.text_view).text = ""
                        }
                    }

                    when (position.rem(6)) {
                        1 -> {//周一
                            setBg(teacherBean.w1)
                            holder.clickItem {
                                if (teacherBean.w1.have(shl)) {
                                    teacherBean.w1 = teacherBean.w1.remove(shl)
                                } else {
                                    teacherBean.w1 = teacherBean.w1.add(shl)
                                }
                                notifyItemChanged(position)
                            }
                        }
                        2 -> {
                            setBg(teacherBean.w2)
                            holder.clickItem {
                                if (teacherBean.w2.have(shl)) {
                                    teacherBean.w2 = teacherBean.w2.remove(shl)
                                } else {
                                    teacherBean.w2 = teacherBean.w2.add(shl)
                                }
                                notifyItemChanged(position)
                            }
                        }
                        3 -> {
                            setBg(teacherBean.w3)
                            holder.clickItem {
                                if (teacherBean.w3.have(shl)) {
                                    teacherBean.w3 = teacherBean.w3.remove(shl)
                                } else {
                                    teacherBean.w3 = teacherBean.w3.add(shl)
                                }
                                notifyItemChanged(position)
                            }
                        }
                        4 -> {
                            setBg(teacherBean.w4)
                            holder.clickItem {
                                if (teacherBean.w4.have(shl)) {
                                    teacherBean.w4 = teacherBean.w4.remove(shl)
                                } else {
                                    teacherBean.w4 = teacherBean.w4.add(shl)
                                }
                                notifyItemChanged(position)
                            }
                        }
                        5 -> {
                            setBg(teacherBean.w5)
                            holder.clickItem {
                                if (teacherBean.w5.have(shl)) {
                                    teacherBean.w5 = teacherBean.w5.remove(shl)
                                } else {
                                    teacherBean.w5 = teacherBean.w5.add(shl)
                                }
                                notifyItemChanged(position)
                            }
                        }
                    }
                }
            }


        }
    }

    override fun initRefreshLayout(refreshLayout: RefreshLayout?, baseContentLayout: ContentLayout?) {
        super.initRefreshLayout(refreshLayout, baseContentLayout)
        refreshLayout?.isEnabled = false
    }

    override fun initRecyclerView(recyclerView: RRecyclerView?, baseContentLayout: ContentLayout?) {
        super.initRecyclerView(recyclerView, baseContentLayout)
        recyclerView?.let {
            it.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            it.layoutManager = GridLayoutManager(mActivity, 6)
            it.onSizeChanged { w, h, oldw, oldh ->
                if (w > 0 && h > 0) {
                    it.adapter?.notifyDataSetChanged()
                }
            }
            it.addItemDecoration(RBaseItemDecoration(getDimensionPixelOffset(R.dimen.base_line), getColor(R.color.base_chat_bg_color)))
        }
    }

    override fun getDefaultRequestedOrientation(): Int {
        return ActivityInfo.SCREEN_ORIENTATION_USER
    }

    override fun getDefaultLayoutState(): LayoutState {
        return LayoutState.LOAD
    }

    private val lessonBeanList = mutableListOf<LessonBean>()
    override fun onViewShowFirst(bundle: Bundle?) {
        super.onViewShowFirst(bundle)
    }

    override fun onUILoadData(page: Int) {
        super.onUILoadData(page)
        RBmob.query<LessonBean>().apply {
            add(findObjects(object : FindListener<LessonBean>() {
                override fun done(p0: MutableList<LessonBean>?, p1: BmobException?) {
                    p0?.let {
                        lessonBeanList.addAll(it)
                    }
                }
            }))
        }

        if (isTeacher) {
            val query = BmobQuery<TeacherBean>()
            query.addWhereEqualTo("name", teacherBean.name)
            query.findObjects(object : FindListener<TeacherBean>() {
                override fun done(p0: MutableList<TeacherBean>?, p1: BmobException?) {
                    if (!RUtils.isListEmpty(p0)) {
                        teacherBean = p0!!.first()
                    }
                    showContentLayout()
                }
            })
        } else {

        }
    }
}