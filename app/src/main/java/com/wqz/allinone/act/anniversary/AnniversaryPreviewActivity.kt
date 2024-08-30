package com.wqz.allinone.act.anniversary

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.act.anniversary.viewmodel.AnniversaryViewModel
import com.wqz.allinone.entity.Anniversary
import com.wqz.allinone.ui.AppBackground
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.TitleBar
import com.wqz.allinone.ui.color.BackgroundColor
import com.wqz.allinone.ui.color.BorderColor
import com.wqz.allinone.ui.theme.AllInOneTheme
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale
import kotlin.math.absoluteValue

/**
 * 纪念日预览
 * Created by Wu Qizhen on 2024.8.20
 */
class AnniversaryPreviewActivity : ComponentActivity() {
    private lateinit var viewModel: AnniversaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = AnniversaryViewModel(application)

        setContent {
            AllInOneTheme {
                AppBackground.CirclesBackground {
                    AnniversaryPreviewScreen(viewModel)
                }
            }
        }
    }

    @Composable
    fun AnniversaryPreviewScreen(viewModel: AnniversaryViewModel) {
        val context = LocalContext.current
        val currentDate = remember { mutableStateOf(Calendar.getInstance()) }
        val dateFormat = remember { SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault()) }
        val scrollState = rememberScrollState()
        val anniversaries by viewModel.anniversaries.observeAsState(emptyList())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleBar.TextTitleBar(title = R.string.anniversary)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickVfx {
                        startActivity(Intent(context, AnniversaryAddActivity::class.java))
                    }
                    .wrapContentSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "添加纪念日",
                    modifier = Modifier
                        .size(25.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = stringResource(R.string.add_anniversary),
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {
                        // 更新 Calendar 实例中的日期
                        val newDate = Calendar.getInstance().apply {
                            time = currentDate.value.time
                            add(Calendar.DAY_OF_MONTH, -1)
                        }
                        currentDate.value = newDate
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowLeft,
                            contentDescription = "前一天",
                            tint = Color.White
                        )
                    },
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = dateFormat.format(currentDate.value.time),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(110.dp),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.width(5.dp))
                IconButton(
                    onClick = {
                        // 更新 Calendar 实例中的日期
                        val newDate = Calendar.getInstance().apply {
                            time = currentDate.value.time
                            add(Calendar.DAY_OF_MONTH, 1)
                        }
                        currentDate.value = newDate
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowRight,
                            contentDescription = "后一天",
                            tint = Color.White
                        )
                    },
                    modifier = Modifier.size(25.dp)
                )
            }
            if (anniversaries.isEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_no_data),
                    contentDescription = "无数据",
                    modifier = Modifier
                        .size(100.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(7.dp))
                for (anniversary in anniversaries) {
                    Spacer(modifier = Modifier.height(3.dp))
                    AnniversaryItem(anniversary = anniversary, currentDate = currentDate)
                    Spacer(modifier = Modifier.height(3.dp))
                }
                Spacer(modifier = Modifier.height(47.dp))
            }
        }
    }

    @Composable
    fun AnniversaryItem(
        anniversary: Anniversary,
        currentDate: MutableState<Calendar>
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BackgroundColor.PRESSED_GRAY else BackgroundColor.DEFAULT_GRAY
        val borderColors = BorderColor.DEFAULT_GRAY
        val borderWidth = 0.4f.dp
        // val mutableDate = remember { mutableStateOf(currentDate) }

        Column(
            modifier = Modifier
                .clickVfx(
                    interactionSource = interactionSource,
                    enabled = true,
                    onClick = {}
                )
                .wrapContentHeight()
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(10.dp))
                .border(
                    width = borderWidth,
                    shape = RoundedCornerShape(10.dp),
                    brush = Brush.linearGradient(
                        borderColors,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(10.dp)
        ) {
            Text(
                text = "${anniversary.content} · ${
                    ChronoUnit.DAYS.between(
                        anniversary.date,
                        currentDate.value.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        // mutableDate.value
                    ).absoluteValue.plus(1)
                }天", fontSize = 14.sp, maxLines = 1
            )
            Text(
                text = "纪念日 | ${anniversary.date.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"))}",
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}