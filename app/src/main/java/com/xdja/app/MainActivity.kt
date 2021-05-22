package com.xdja.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xdja.app.mvp.ui.activity.TestActivity
import com.xdja.app.mvp.ui.activity.UserActivity
import com.xdja.easymvp.utils.onClick
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnClick.onClick {
            startActivity<TestActivity>()
        }
    }
}