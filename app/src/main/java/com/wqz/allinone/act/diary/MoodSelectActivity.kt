package com.wqz.allinone.act.diary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.act.diary.data.Option
import com.wqz.allinone.act.diary.data.OptionData
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.XBackground
import com.wqz.allinone.ui.XCard
import com.wqz.allinone.ui.property.BorderWidth

/**
 * 心情选择
 * Created by Wu Qizhen on 2024.10.13
 * Refactored by Wu Qizhen on 2024.11.30
 */
class MoodSelectActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            XBackground.BreathingBackground(titleId = R.string.mood_select) {
                MoodSelectScreen()
            }
        }
    }

    @Composable
    fun MoodSelectScreen() {
        val moodOptions = OptionData.moodOptions

        XCard.SurfaceCard {
            moodOptions.forEach {
                key(it.id) {
                    SelectItem(option = it)
                    // 分如果不是最后一个绘制割线
                    if (it != moodOptions.last()) {
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth(),
                            thickness = BorderWidth.DEFAULT_WIDTH,
                            color = Color(54, 54, 54)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }

    @Composable
    fun SelectItem(
        option: Option
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val contentColor =
            if (isPressed.value) Color.Gray else Color.White

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(10.dp)
                .clickVfx(
                    interactionSource = interactionSource,
                    enabled = true,
                    onClick = {
                        val result = Intent().putExtra("selected_mood", option.id)
                        setResult(Activity.RESULT_OK, result)
                        finish()
                    }
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = option.icon),
                    modifier = Modifier.size(30.dp),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = option.text,
                    fontSize = 16.sp,
                    color = contentColor,
                    maxLines = 1
                )
            }
        }
    }
}