package com.wqz.allinone.act.about

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wqz.allinone.act.about.ui.AboutStudioScreen
import com.wqz.allinone.ui.AppBackground
import com.wqz.allinone.ui.theme.AllInOneTheme

/**
 * 关于组织
 * Created by Wu Qizhen on 2024.6.22
 */
class AboutStudioActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AllInOneTheme {
                AppBackground.CirclesBackground {
                    AboutStudioScreen()
                }
            }
        }
    }
}