package com.wqz.allinone.act.diary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.act.diary.data.Option
import com.wqz.allinone.act.diary.data.OptionData
import com.wqz.allinone.ui.AppBackground
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.TitleBar
import com.wqz.allinone.ui.color.BackgroundColor
import com.wqz.allinone.ui.color.BorderColor
import com.wqz.allinone.ui.theme.AllInOneTheme

class WeatherSelectActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AllInOneTheme {
                AppBackground.BreathingBackground {
                    WeatherSelectScreen()
                }
            }
        }
    }

    @Composable
    fun WeatherSelectScreen() {
        val scrollState = rememberScrollState()
        val weatherOptions = OptionData.weatherOptions

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 50.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleBar.TextTitleBar(title = R.string.weather_select)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        BackgroundColor.DEFAULT_GRAY, RoundedCornerShape(10.dp)
                    )
                    .border(
                        width = 0.4f.dp,
                        shape = RoundedCornerShape(10.dp),
                        brush = Brush.linearGradient(
                            BorderColor.DEFAULT_GRAY,
                            start = Offset.Zero,
                            end = Offset.Infinite
                        )
                    )
            ) {
                weatherOptions.forEach {
                    key(it.id) {
                        SelectItem(option = it)
                        // 分如果不是最后一个绘制割线
                        if (it != weatherOptions.last()) {
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                thickness = 0.5f.dp,
                                color = Color(54, 54, 54)
                            )
                        }
                    }
                }
            }
        }
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
                        val result = Intent().putExtra("selected_weather", option.id)
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