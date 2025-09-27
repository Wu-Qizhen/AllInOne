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
import com.wqz.allinone.act.knowledge.data.KnowledgeCode
import com.wqz.allinone.act.knowledge.ui.SelectItem
import com.wqz.allinone.ui.XBackground
import com.wqz.allinone.ui.XCard
import com.wqz.allinone.ui.property.BorderWidth

/**
 * 语文
 * Created by Wu Qizhen on 2024.12.8
 */
class ChineseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            XBackground.BreathingBackground(R.string.chinese) {
                ChineseScreen()
            }
        }
    }

    @Composable
    fun ChineseScreen() {
        XCard.SurfaceCard {
            SelectItem.SubjectItem(text = R.string.chinese_ending_writing) {
                val intent = Intent(this@ChineseActivity, SmartBoxActivity::class.java)
                intent.putExtra("KNOWLEDGE_CODE", KnowledgeCode.CHINESE_ENDING_WRITING)
                startActivity(intent)
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = BorderWidth.DEFAULT_WIDTH,
                color = Color(54, 54, 54)
            )

            SelectItem.SubjectItem(text = R.string.chinese_common_expressions) {
                val intent = Intent(this@ChineseActivity, SmartBoxActivity::class.java)
                intent.putExtra("KNOWLEDGE_CODE", KnowledgeCode.CHINESE_COMMON_EXPRESSION)
                startActivity(intent)
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}