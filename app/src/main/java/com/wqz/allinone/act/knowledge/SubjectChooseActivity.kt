package com.wqz.allinone.act.knowledge

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wqz.allinone.R
import com.wqz.allinone.act.knowledge.ui.SelectItem
import com.wqz.allinone.ui.XBackground
import com.wqz.allinone.ui.XCard
import com.wqz.allinone.ui.property.BorderWidth

/**
 * 科目选择
 * Created by Wu Qizhen on 2024.11.30
 */
class SubjectChooseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            XBackground.BreathingBackground(R.string.smart_box) {
                SubjectChooseScreen()
            }
        }
    }

    @Composable
    fun SubjectChooseScreen() {
        XCard.SurfaceCard {
            SelectItem.SubjectItem(text = R.string.chinese) {
                startActivity(Intent(this@SubjectChooseActivity, ChineseActivity::class.java))
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = BorderWidth.DEFAULT_WIDTH,
                color = Color(54, 54, 54)
            )

            SelectItem.SubjectItem(text = R.string.english) {
                startActivity(Intent(this@SubjectChooseActivity, EnglishActivity::class.java))
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}