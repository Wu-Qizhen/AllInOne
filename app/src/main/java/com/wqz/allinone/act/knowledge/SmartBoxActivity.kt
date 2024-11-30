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
import com.wqz.allinone.act.knowledge.data.EnglishData
import com.wqz.allinone.act.knowledge.data.Outline
import com.wqz.allinone.act.knowledge.ui.SelectItem
import com.wqz.allinone.ui.AppBackground
import com.wqz.allinone.ui.theme.AllInOneTheme

/**
 * 智慧盒
 * Created by Wu Qizhen on 2024.11.30
 */
class SmartBoxActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val code = intent.getIntExtra("KNOWLEDGE_CODE", 0)
        val outlines: List<Outline> = when (code) {
            0 -> {
                EnglishData.englishPracticalWritingOutlines
            }

            1 -> {
                EnglishData.englishEmotionalWritingOutlines
            }

            2 -> {
                EnglishData.englishEndingWritingOutlines
            }

            else -> {
                EnglishData.englishPracticalWritingOutlines
            }
        }

        setContent {
            AllInOneTheme {
                AppBackground.BreathingBackground(title = R.string.smart_box) {
                    SmartBoxScreen(outlines = outlines)
                }
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