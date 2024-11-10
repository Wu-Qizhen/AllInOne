package com.wqz.allinone.act.anniversary

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.act.anniversary.viewmodel.AnniversaryViewModel
import com.wqz.allinone.entity.Anniversary
import com.wqz.allinone.ui.AppBackground
import com.wqz.allinone.ui.ItemX
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.TitleBar
import com.wqz.allinone.ui.color.BackgroundColor
import com.wqz.allinone.ui.color.BorderColor
import com.wqz.allinone.ui.theme.AllInOneTheme
import com.wqz.allinone.ui.theme.ThemeColor
import java.text.SimpleDateFormat
import java.time.LocalDate
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
                AppBackground.BreathingBackground {
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
        val anniversaries by viewModel.anniversaries.observeAsState(emptyList())
        var showJump by remember { mutableStateOf(false) }
        var changeDate by remember {
            mutableStateOf(
                LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
            )
        }
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleBar.TextTitleBar(title = R.string.anniversary)
            ItemX.Button(
                icon = R.drawable.ic_add,
                text = stringResource(R.string.add_anniversary)
            ) {
                startActivity(Intent(context, AnniversaryAddActivity::class.java))
            }
            /*Row(
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
            }*/

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
                    modifier = Modifier
                        .width(110.dp)
                        .clickVfx {
                            showJump = !showJump
                        },
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

            AnimatedVisibility(
                visible = showJump,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(
                        modifier = Modifier
                            .clickVfx()
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .background(
                                color = BackgroundColor.DEFAULT_GRAY,
                                shape = RoundedCornerShape(10.dp)
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
                            .padding(bottom = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = changeDate,
                            onValueChange = { changeDate = it },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent, // 背景颜色
                                focusedContainerColor = Color.Transparent, // 背景颜色
                                unfocusedIndicatorColor = Color.Transparent, // 下划线颜色
                                focusedIndicatorColor = Color.Transparent, // 下划线颜色
                                cursorColor = ThemeColor // 光标颜色
                            ),
                            textStyle = TextStyle(
                                color = Color.White,
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.misans_regular)),
                                textAlign = TextAlign.Center
                            ),
                            placeholder = {
                                Text(
                                    text = "2000.01.01",
                                    color = Color.DarkGray,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        )
                        Row {
                            ItemX.Button(icon = R.drawable.ic_locate, text = "今天") {
                                currentDate.value = Calendar.getInstance()
                                showJump = false
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            ItemX.Button(icon = R.drawable.ic_jump, text = "跳转") {
                                if (changeDate.matches(Regex("\\d{4}.\\d{2}.\\d{2}"))) {
                                    val date = changeDate.split(".")
                                    val newDateCalendar = Calendar.getInstance().apply {
                                        set(Calendar.YEAR, date[0].toInt())
                                        set(Calendar.MONTH, date[1].toInt() - 1)
                                        set(Calendar.DAY_OF_MONTH, date[2].toInt())
                                    }
                                    currentDate.value = newDateCalendar
                                    showJump = false
                                } else {
                                    Toast.makeText(context, "日期格式错误", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                    }
                }
            }

            if (anniversaries.isEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_no_data),
                    contentDescription = "无数据",
                    modifier = Modifier
                        .size(100.dp)
                )
                Spacer(modifier = Modifier.height(50.dp))
            } else {
                Spacer(modifier = Modifier.height(7.dp))
                anniversaries.forEach {
                    key(it.id) {
                        Spacer(modifier = Modifier.height(3.dp))
                        AnniversaryItem(anniversary = it, currentDate = currentDate)
                        Spacer(modifier = Modifier.height(3.dp))
                    }
                }
                Spacer(modifier = Modifier.height(47.dp))
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun AnniversaryItem(
        anniversary: Anniversary,
        currentDate: MutableState<Calendar>
    ) {
        val days = ChronoUnit.DAYS.between(
            anniversary.date,
            currentDate.value.time.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate()
        ).toInt() // .plus(1)
        val date = anniversary.date.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"))
        val dateType = if (days < 0) "倒数日" else "纪念日"

        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val onClick = {
            val intent =
                Intent(this@AnniversaryPreviewActivity, AnniversaryDetailsActivity::class.java)
            intent.putExtra("ANNIVERSARY_ID", anniversary.id)
            // 传递当前的年月日
            intent.putExtra("YEAR", currentDate.value.get(Calendar.YEAR))
            intent.putExtra("MONTH", currentDate.value.get(Calendar.MONTH) + 1)
            intent.putExtra("DAY", currentDate.value.get(Calendar.DAY_OF_MONTH))
            startActivity(intent)
        }

        // 判断是否为刚好满几周年
        val isAnniversary =
            anniversary.date.month == currentDate.value.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate().month &&
                    anniversary.date.dayOfMonth == currentDate.value.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate().dayOfMonth &&
                    days > 1
        if (isAnniversary) {
            val anniversaryCount = ChronoUnit.YEARS.between(
                anniversary.date,
                currentDate.value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            ).toInt()
            val backgroundColor = Brush.horizontalGradient( // 设置渐变色
                listOf(
                    Color(240, 175, 57),
                    Color(241, 126, 45),
                )
            )

            Column(
                modifier = Modifier
                    .clickVfx(
                        interactionSource = interactionSource,
                        enabled = true,
                        onClick = onClick
                    )
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .background(brush = backgroundColor, shape = RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween, // 两端对齐
                    verticalAlignment = Alignment.CenterVertically // 垂直居中对齐
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .basicMarquee(),
                        text = anniversary.content,
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        maxLines = 1
                    )
                    Text(
                        modifier = Modifier.padding(start = 10.dp),
                        text = "${anniversaryCount}周年",
                        textAlign = TextAlign.End,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                    )
                }
                Text(
                    text = "$dateType | $date",
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
        } else {
            val backgroundColor =
                if (isPressed.value) BackgroundColor.PRESSED_GRAY else BackgroundColor.DEFAULT_GRAY
            val borderColors = BorderColor.DEFAULT_GRAY
            val borderWidth = 0.4f.dp

            Column(
                modifier = Modifier
                    .clickVfx(
                        interactionSource = interactionSource,
                        enabled = true,
                        onClick = onClick
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween, // 两端对齐
                    verticalAlignment = Alignment.CenterVertically // 垂直居中对齐
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .basicMarquee(),
                        text = anniversary.content,
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        maxLines = 1
                    )
                    Text(
                        modifier = Modifier.padding(start = 10.dp),
                        text = if (days > 0) "${days.plus(1)}天" else "${days.absoluteValue}天后",
                        textAlign = TextAlign.End,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        color = if (days > 0) BackgroundColor.DEFAULT_YELLOW else Color(8, 116, 196)
                    )
                }
                Text(
                    text = "$dateType | $date",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
        }
    }

    /*@Composable
    fun JumpTo(
        currentDate: MutableState<Calendar>
    ) {}*/

    @Preview
    @Composable
    fun AnniversaryItemPreview() {
        AllInOneTheme {
            AnniversaryItem(
                anniversary = Anniversary(1, "Test", LocalDate.of(2020, 10, 1)),
                currentDate = remember { mutableStateOf(Calendar.getInstance()) }
            )
        }
    }
}