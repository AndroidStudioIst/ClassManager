package com.cls.manager

import android.content.Intent
import com.angcyo.uiview.base.UILayoutActivity
import com.cls.manager.iview.LoginUIView

class MainActivity : UILayoutActivity() {
    override fun onLoadView(intent: Intent?) {
        //startIView(MainUIView(), false)
        startIView(LoginUIView(), false)
    }
}
