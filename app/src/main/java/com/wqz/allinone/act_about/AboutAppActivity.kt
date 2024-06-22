package com.wqz.allinone.act_about

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wqz.allinone.act_about.ui.AboutAppScreen
import com.wqz.allinone.ui.CirclesBackground
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
                CirclesBackground.RegularBackgroundWithNoTitle {
                    AboutAppScreen()
                }
            }
        }
    }
}