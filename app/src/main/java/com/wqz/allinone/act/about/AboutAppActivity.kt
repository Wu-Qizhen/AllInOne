package com.wqz.allinone.act.about

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wqz.allinone.act.about.ui.AboutAppScreen
import com.wqz.allinone.ui.AppBackground
import com.wqz.allinone.ui.theme.AllInOneTheme

/**
 * 关于应用
 * Created by Wu Qizhen on 2024.6.22
 */
class AboutAppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AllInOneTheme {
                AppBackground.CirclesBackground {
                    AboutAppScreen()
                }
            }
        }
    }
}