package com.wqz.allinone.act.setting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wqz.allinone.act.setting.ui.SettingScreen
import com.wqz.allinone.ui.CirclesBackground
import com.wqz.allinone.ui.theme.AllInOneTheme

/**
 * 设置
 * Created by Wu Qizhen on 2024.6.29
 */
class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AllInOneTheme {
                CirclesBackground.RegularBackground {
                    SettingScreen()
                }
            }
        }
    }
}