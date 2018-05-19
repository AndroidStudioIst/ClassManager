package com.cls.manager.control

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import com.angcyo.bmob.RBmob
import com.angcyo.uiview.RApplication
import com.angcyo.uiview.utils.NotifyUtil
import com.angcyo.uiview.utils.RUtils
import com.cls.manager.MainActivity
import com.cls.manager.R
import com.cls.manager.bean.RequestClassBean

/**
 * Created by angcyo on 2018/05/19 09:12
 */
object NotifyControl {
    const val KEY_NOTIFY = "key_notify"

    fun checkNotify() {
        RBmob.query<RequestClassBean>(RequestClassBean::class.java, "name:${UserControl.loginUserBean!!.name}") {
            if (!RUtils.isListEmpty(it)) {

                it.firstOrNull()?.let {

                    if (it.isNeedNotify) {
                        debug()

                        it.isNeedNotify = false

                        RBmob.update(RequestClassBean::class.java, it, "name:${UserControl.loginUserBean!!.name}") {

                        }
                    }
                }

            }
        }
    }

    fun debug() {
        val intent = Intent(RApplication.getApp(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(KEY_NOTIFY, true)
        val pendingIntent = PendingIntent.getActivity(RApplication.getApp(), 10000, intent, FLAG_UPDATE_CURRENT)

        NotifyUtil.build(200)
                .notify_normal_singline(pendingIntent, R.mipmap.logo, "申请审核通过",
                        "通知", "您申请的软件实验课室, 审核通过.", true, true, true)
    }
}