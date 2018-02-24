package com.cls.manager

import android.content.Intent
import com.angcyo.uiview.base.UILayoutActivity
import com.cls.manager.control.UserControl
import com.cls.manager.iview.LoginUIView
import com.cls.manager.iview.MainUIView
import com.orhanobut.hawk.Hawk

class MainActivity : UILayoutActivity() {
    override fun onLoadView(intent: Intent?) {
        //startIView(MainUIView(), false)
        if (Hawk.get("AUTO_LOGIN", false)) {
            UserControl.loginUser(Hawk.get("NAME", ""), Hawk.get("PW", "")) {
                if (it == null) {
                    startIView(LoginUIView(), false)
                } else {
                    startIView(MainUIView(), false)
                }
            }
        } else {
            startIView(LoginUIView(), false)
        }
    }
}
