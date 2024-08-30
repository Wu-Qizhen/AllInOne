package com.wqz.allinone.act.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.wqz.allinone.act.main.ui.LaunchAnimation
import com.wqz.allinone.ui.AppBackground
import com.wqz.allinone.ui.theme.AllInOneTheme

/**
 * 主界面
 * Created by Wu Qizhen on 2024.6.29
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContent {
            AllInOneTheme {
                AppBackground.CirclesBackground {
                    LaunchAnimation()
                }
            }
        }
    }
}