package com.cls.manager

import android.content.Intent
import com.angcyo.uiview.base.UILayoutActivity
import com.cls.manager.iview.MainUIView

class MainActivity : UILayoutActivity() {
    override fun onLoadView(intent: Intent?) {
        startIView(MainUIView(), false)
    }
}
