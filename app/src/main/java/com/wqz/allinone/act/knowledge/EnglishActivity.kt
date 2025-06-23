package com.wqz.allinone.act.knowledge

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
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
 * 英语
 * Created by Wu Qizhen on 2024.11.30
 */
class EnglishActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            XBackground.BreathingBackground(R.string.english) {
                EnglishScreen()
            }
        }
    }

    @Composable
    fun EnglishScreen() {
        XCard.SurfaceCard {
            SelectItem.SubjectItem(text = R.string.english_practical_writing) {
                val intent = Intent(this@EnglishActivity, SmartBoxActivity::class.java)
                intent.putExtra("KNOWLEDGE_CODE", KnowledgeCode.ENGLISH_PRACTICAL_WRITING)
                startActivity(intent)
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = BorderWidth.DEFAULT_WIDTH,
                color = Color(54, 54, 54)
            )

            SelectItem.SubjectItem(text = R.string.english_emotional_writing) {
                val intent = Intent(this@EnglishActivity, SmartBoxActivity::class.java)
                intent.putExtra("KNOWLEDGE_CODE", KnowledgeCode.ENGLISH_EMOTIONAL_WRITING)
                startActivity(intent)
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = BorderWidth.DEFAULT_WIDTH,
                color = Color(54, 54, 54)
            )

            SelectItem.SubjectItem(text = R.string.english_ending_writing) {
                val intent = Intent(this@EnglishActivity, SmartBoxActivity::class.java)
                intent.putExtra("KNOWLEDGE_CODE", KnowledgeCode.ENGLISH_ENDING_WRITING)
                startActivity(intent)
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}