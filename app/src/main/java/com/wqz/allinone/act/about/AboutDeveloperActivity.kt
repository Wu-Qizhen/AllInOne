package com.wqz.allinone.act.about

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.wqz.allinone.R
import com.wqz.allinone.act.about.ui.AboutDeveloperScreen
import com.wqz.allinone.ui.XBackground

/**
 * 关于开发者
 * Created by Wu Qizhen on 2024.6.22
 */
class AboutDeveloperActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        val logo = intent.getIntExtra("logo", R.drawable.logo_wqz)
        val name = intent.getIntExtra("name", R.string.wqz)
        val description =
            intent.getIntExtra("description", R.string.wqz_desc)
        val details = intent.getIntExtra("details", R.string.text_about_wqz)
        setContent {
            XBackground.BreathingBackground {
                AboutDeveloperScreen(
                    logo = logo,
                    name = name,
                    description = description,
                    details = details
                )
            }
        }
    }
}