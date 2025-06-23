package com.wqz.allinone.act.knowledge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wqz.allinone.R
import com.wqz.allinone.act.knowledge.data.ChineseData
import com.wqz.allinone.act.knowledge.data.EnglishData
import com.wqz.allinone.act.knowledge.data.KnowledgeCode
import com.wqz.allinone.act.knowledge.data.Outline
import com.wqz.allinone.act.knowledge.ui.SelectItem
import com.wqz.allinone.ui.XBackground

/**
 * 智慧盒
 * Created by Wu Qizhen on 2024.11.30
 */
class SmartBoxActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val code = intent.getIntExtra("KNOWLEDGE_CODE", 0)
        val outlines: List<Outline> = when (code) {
            KnowledgeCode.ENGLISH_PRACTICAL_WRITING -> {
                EnglishData.englishPracticalWritingOutlines
            }

            KnowledgeCode.ENGLISH_EMOTIONAL_WRITING -> {
                EnglishData.englishEmotionalWritingOutlines
            }

            KnowledgeCode.ENGLISH_ENDING_WRITING -> {
                EnglishData.englishEndingWritingOutlines
            }

            KnowledgeCode.CHINESE_ENDING_WRITING -> {
                ChineseData.chineseEndingWritingOutlines
            }

            KnowledgeCode.CHINESE_COMMON_EXPRESSION -> {
                ChineseData.chineseCommonExpressionOutlines
            }

            else -> {
                EnglishData.englishPracticalWritingOutlines
            }
        }

        setContent {
            XBackground.BreathingBackground(titleId = R.string.smart_box) {
                SmartBoxScreen(outlines = outlines)
            }
        }
    }

    @Composable
    fun SmartBoxScreen(
        outlines: List<Outline>
    ) {
        outlines.forEach {
            key(it.title) {
                SelectItem.OutlineItem(outline = it)
            }
        }

        Spacer(modifier = Modifier.height(47.dp))
    }
}