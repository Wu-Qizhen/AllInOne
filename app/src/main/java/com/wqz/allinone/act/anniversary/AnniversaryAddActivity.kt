package com.wqz.allinone.act.anniversary

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 添加纪念日
 * Created by Wu Qizhen on 2024.8.20
 */
class AnniversaryAddActivity : ComponentActivity() {
    private lateinit var viewModel: AnniversaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = AnniversaryViewModel(application)

        setContent {
            AllInOneTheme {
                AppBackground.CirclesBackground {
                    AnniversaryAddScreen(viewModel)
                }
            }
        }
    }

    @Composable
    fun AnniversaryAddScreen(viewModel: AnniversaryViewModel) {
        val context = LocalContext.current
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BackgroundColor.PRESSED_GRAY else BackgroundColor.DEFAULT_GRAY
        val borderColors = BorderColor.DEFAULT_GRAY
        val borderWidth = 0.4f.dp
        val scrollState = rememberScrollState()
        var content by remember { mutableStateOf("") }
        var date by remember {
            mutableStateOf(
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
            )
        }
        // var showDatePicker by remember { mutableStateOf(false) }
        // var selectedDate by remember { mutableStateOf(LocalDate.now()) }
        // val datePickerState = rememberDatePickerState()
        // val keyboardController = LocalSoftwareKeyboardController.current

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
            TitleBar.TextTitleBar(title = R.string.add_anniversary)
            Column(
                modifier = Modifier
                    .clickVfx(
                        interactionSource = interactionSource,
                        enabled = true,
                        onClick = { }
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
                // .padding(10.dp)
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = content,
                    onValueChange = { content = it },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent, // 背景颜色
                        focusedContainerColor = Color.Transparent, // 背景颜色
                        unfocusedIndicatorColor = Color.Transparent, // 下划线颜色
                        focusedIndicatorColor = Color.Transparent, // 下划线颜色
                        cursorColor = ThemeColor, // 光标颜色
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.Gray
                    ),
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.misans_regular))
                    ),
                    label = {
                        Text(
                            text = "纪念内容",
                            fontWeight = FontWeight.Bold
                        )
                    }
                    /*placeholder = {
                        Text(
                            text = "纪念内容",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }*/
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .clickVfx(
                        interactionSource = interactionSource,
                        enabled = true,
                        onClick = { }
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
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = date,
                    onValueChange = { date = it },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent, // 背景颜色
                        focusedContainerColor = Color.Transparent, // 背景颜色
                        unfocusedIndicatorColor = Color.Transparent, // 下划线颜色
                        focusedIndicatorColor = Color.Transparent, // 下划线颜色
                        cursorColor = ThemeColor, // 光标颜色
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.Gray
                    ),
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.misans_regular))
                    ),
                    maxLines = 1,
                    label = {
                        Text(
                            text = "纪念日期",
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            ItemX.Button(icon = R.drawable.ic_add, text = "添加") {
                if (content.isNotEmpty() && date.matches(Regex("\\d{4}.\\d{2}.\\d{2}"))) {
                    val anniversary = Anniversary(
                        id = null,
                        content = content,
                        date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                    )
                    viewModel.insertAnniversary(anniversary)
                    Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(context, "请输入纪念内容和日期", Toast.LENGTH_SHORT).show()
                }
            }
            /*Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "日期",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickVfx {
                            showDatePicker = true
                        },
                    text = selectedDate.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")),
                    color = Color.Gray,
                    fontSize = 16.sp,
                    textAlign = TextAlign.End
                )
            }*/
            // DatePicker(state = datePickerState)
            // Text("当前选中日期的时间戳：${datePickerState.selectedDateMillis ?: "没有选择"}")
        }
    }
}