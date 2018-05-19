package com.cls.manager.iview

import android.graphics.Color
import android.widget.TextView
import cn.bmob.v3.BmobQuery
import com.angcyo.bmob.RBmob
import com.angcyo.uiview.dialog.UIItemSelectorDialog
import com.angcyo.uiview.dialog.UILoading
import com.angcyo.uiview.model.TitleBarPattern
import com.angcyo.uiview.net.RException
import com.angcyo.uiview.net.RSubscriber
import com.angcyo.uiview.net.Rx
import com.angcyo.uiview.recycler.RBaseViewHolder
import com.angcyo.uiview.utils.RUtils
import com.angcyo.uiview.utils.T_
import com.cls.manager.R
import com.cls.manager.bean.RequestClassBean
import com.cls.manager.bean.TeacherBean
import rx.schedulers.Schedulers
import java.util.*

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2018/02/26 13:45
 * 修改人员：Robi
 * 修改时间：2018/02/26 13:45
 * 修改备注：
 * Version: 1.0.0
 */
open class VerifyClassUIView : RequestClassUIView() {

    /*所有老师的请求*/
    protected var requestClassList = mutableListOf<RequestClassBean>()


    override fun getTitleBar(): TitleBarPattern {
        return super.getTitleBar().setTitleString(if (isSeeClass) {
            "班级课表"
        } else {
            "审核课室"
        })
    }

    override fun onShowContentData() {
        if (isSeeClass) {
            showContentLayout()
            uiTitleBarContainer.hideRightItem(0)
            return
        }

        RBmob.query<RequestClassBean>(RequestClassBean::class.java, "") {

            fun onEnd() {
                if (requestClassList.isEmpty()) {
                    T_.error("暂无老师申请课室")
                    uiTitleBarContainer.hideRightItem(0)
                } else if (this.javaClass.simpleName.contains("SeeVerifyClassUIView")) {
                    uiTitleBarContainer.hideRightItem(0)
                } else {
                    uiTitleBarContainer.getRightView<TextView>(0).text = "批准"
                }
            }

            if (RUtils.isListEmpty(it)) {
                showContentLayout()
                onEnd()
            } else {
                requestClassList.addAll(it)
                //requestClassList
                Rx.from(requestClassList)
                        .observeOn(Schedulers.io())
                        .flatMap({ requestBean ->
                            val query = BmobQuery<TeacherBean>()
                            query.setLimit(500)//最大返回500条, 请查看文档的分页查询
                            query.addWhereEqualTo("name", requestBean.name)
                            query.findObjectsObservable(TeacherBean::class.java)
                                    .observeOn(Schedulers.io())
                                    .map {
                                        if (RUtils.isListEmpty(it)) {
                                            "${requestBean.name}:${it.size}:fail"
                                        } else {
                                            requestBean.mTeacherBean = it.first().apply {
                                                allNum = calcAllNum()
                                            }
                                            "${requestBean.mTeacherBean.name}:${it.size}:ok"
                                        }
                                    }
                        })
                        .compose(Rx.transformer())
                        .subscribe(object : RSubscriber<String>() {
                            override fun onStart() {
                                super.onStart()
                                if (!this@VerifyClassUIView.javaClass.simpleName.contains("SeeVerifyClassUIView")) {
                                    UILoading.flow(mParentILayout).setLoadingTipText("正在计算中...")
                                }
                            }

                            override fun onCompleted() {
                                requestClassList.sortWith(Comparator { class1, class2 ->
                                    //老师不能存在, 或者 课程比较多的, 放后面, 自然序, 从大到小
                                    var result = if (class1.mTeacherBean == null && class2.mTeacherBean == null) {
                                        0
                                    } else if (class1.mTeacherBean == null) {
                                        1
                                    } else if (class2.mTeacherBean == null) {
                                        -1
                                    } else if (class1.mTeacherBean.allNum == class2.mTeacherBean.allNum) {
                                        0
                                    } else if (class1.mTeacherBean.allNum > class2.mTeacherBean.allNum) {
                                        1
                                    } else {
                                        -1
                                    }
                                    result
                                })
                                super.onCompleted()
                            }

                            override fun onEnd(isError: Boolean, isNoNetwork: Boolean, e: RException?) {
                                super.onEnd(isError, isNoNetwork, e)
                                UILoading.hide()
                                showContentLayout()
                                onEnd()
                            }
                        })
            }
        }
    }

    override fun initTeacher(holder: RBaseViewHolder, position: Int) {
        //super.initTeacher(holder, position)
        val row = position / 6  //第几行
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
        UILoading.progress(mParentILayout).setLoadingTipText("正在审批...")
        val list = mutableListOf<RequestClassBean>()
        for (bean in mExBaseAdapter.allDatas) {
            if (!RUtils.isListEmpty(bean.requestList)) {
                list.addAll(bean.requestList)
            }

//            bean.selectorRequest?.let {
//                RBmob.update(RequestClassBean::class.java, it, "name:${it.name}") {
//                    if (it.isEmpty()) {
//                        T_.error("操作失败")
//                    } else {
//                        T_.error("审批成功")
//                        finishIView()
//                    }
//                    UILoading.hide()
//                }
//            }
        }
        RBmob.update(list) {
            if (it.isEmpty()) {
                T_.error("操作失败")
            } else {
                T_.error("审批成功")
                finishIView()
            }
            UILoading.hide()
        }
    }

    override fun setTeacherItem(holder: RBaseViewHolder, w: Int, position: Int) {
        //super.setTeacherItem(holder, w, position)
        val row = position / 6  //第几行
        val column = position.rem(6) //第几列

        var count = 0
        var rBeanList = mutableListOf<RequestClassBean>()
        requestClassList.mapIndexed { _, requestClassBean ->
            if (requestClassBean.request.contains("$row:$column") || requestClassBean.success.contains("$row:$column")) {
                count++
                rBeanList.add(requestClassBean)
            }
        }

        if (RUtils.isListEmpty(mExBaseAdapter.allDatas)) {
            mExBaseAdapter.allDatas = MutableList(mExBaseAdapter.itemCount) {
                TeacherBean()
            }
        }

        val teacherBean = mExBaseAdapter.allDatas[position]

        if (teacherBean.requestList == null) {
            teacherBean.requestList = mutableListOf()
            teacherBean.requestList.addAll(rBeanList)
        }

        for (bean in rBeanList) {
            if (bean.success.contains("$row:$column")) {
                teacherBean.selectorRequest = bean
                break
            }
        }

        holder.clickItem { }

        if (count == 0) {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            holder.tv(R.id.text_view).text = ""
        } else {
            if (count == 1) {
                holder.itemView.setBackgroundColor(getColor(R.color.base_chat_bg_color))
                if (teacherBean.isFirst) {
                    teacherBean.selectorRequest = rBeanList[0]
                    teacherBean.selectorRequest.addSuccess("$row:$column")
                }

                if (teacherBean.selectorRequest == null) {
                    holder.tv(R.id.text_view).text = "未定"
                } else {
                    holder.tv(R.id.text_view).text = teacherBean.selectorRequest.name
                }
            } else {
                holder.itemView.setBackgroundColor(getColor(R.color.base_text_color_dark))

                if (teacherBean.selectorRequest == null) {
                    if (teacherBean.isFirst) {
                        holder.tv(R.id.text_view).text = "${count}个"
                    } else {
                        holder.tv(R.id.text_view).text = "未定"
                    }
                } else {
                    holder.tv(R.id.text_view).text = teacherBean.selectorRequest.name
                }
            }

            holder.clickItem {
                val datas = mutableListOf<RequestClassBean>()
                datas.addAll(teacherBean.requestList)
                datas.add(RequestClassBean().apply {
                    name = "(不指定)"
                })
                startIView(UIItemSelectorDialog(datas).apply {
                    itemLayoutId = R.layout.item_text3_layout
                    onInitItemLayout = { holder, pos, dataBean ->
                        if (dataBean.mTeacherBean == null) {
                            holder.tv(R.id.base_text_view).text = dataBean.name
                            if (pos == datas.size - 1) {
                                holder.tv(R.id.base_text_view2).text = ""
                            } else {
                                holder.tv(R.id.base_text_view2).text = "老师已被删"
                            }
                            holder.tv(R.id.base_text_view3).text = ""
                        } else {
                            holder.tv(R.id.base_text_view).text = dataBean.name
                            holder.tv(R.id.base_text_view2).text = "已有课时:${dataBean.mTeacherBean.allNum}节"
                            holder.tv(R.id.base_text_view3).text = "空余:${50 - dataBean.mTeacherBean.allNum}节"
                        }
                    }
                    onItemSelector = { pos, bean ->
                        if (pos == datas.size - 1) {
                            for (request in teacherBean.requestList) {
                                teacherBean.selectorRequest = null
                                request.addRequest("$row:$column")
                                request.isNeedNotify = false
                            }
                        } else {
                            for (request in teacherBean.requestList) {
                                if (request == bean) {
                                    teacherBean.selectorRequest = request
                                    teacherBean.selectorRequest.addSuccess("$row:$column")
                                    request.isNeedNotify = true
                                } else {
                                    request.addRequest("$row:$column")
                                    request.isNeedNotify = false
                                }
                            }
                        }
                        mExBaseAdapter.notifyItemChanged(position)
                    }
                })
            }
        }
        teacherBean.isFirst = false
    }
}
