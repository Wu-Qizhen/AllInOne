package com.wqz.allinone.act.anniversary

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.wqz.allinone.ui.XCard
import com.wqz.allinone.ui.XItem
import com.wqz.allinone.ui.color.TextFieldColor
import com.wqz.allinone.ui.theme.AllInOneTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 添加纪念日
 * Created by Wu Qizhen on 2024.8.20
 * Refactored by Wu Qizhen on 2024.11.30
 */
class AnniversaryAddActivity : ComponentActivity() {
    private lateinit var viewModel: AnniversaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = AnniversaryViewModel(application)

        setContent {
            AllInOneTheme {
                AppBackground.BreathingBackground(title = R.string.add_anniversary) {
                    AnniversaryAddScreen()
                }
            }
        }
    }

    @Composable
    fun AnniversaryAddScreen() {
        val context = LocalContext.current
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

        XCard.SurfaceCard {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = content,
                onValueChange = { content = it },
                colors = TextFieldColor.colors(),
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

        XCard.SurfaceCard {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = date,
                onValueChange = { date = it },
                colors = TextFieldColor.colors(),
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

        XItem.Button(icon = R.drawable.ic_add, text = "添加") {
            if (content.trim().isNotEmpty() && date.matches(Regex("\\d{4}.\\d{2}.\\d{2}"))) {
                val anniversary = Anniversary(
                    id = null,
                    content = content.trim(),
                    date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                )
                viewModel.insertAnniversary(anniversary)
                Toast.makeText(context, R.string.added, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(context, R.string.input_anniversary_empty, Toast.LENGTH_SHORT).show()
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
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