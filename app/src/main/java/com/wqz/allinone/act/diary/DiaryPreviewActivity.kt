package com.wqz.allinone.act.diary

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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.act.diary.data.OptionData
import com.wqz.allinone.act.diary.viewmodel.DiaryViewModel
import com.wqz.allinone.entity.Diary
import com.wqz.allinone.ui.AppBackground
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.XCard
import com.wqz.allinone.ui.XItem
import com.wqz.allinone.ui.color.BackgroundColor
import com.wqz.allinone.ui.color.BorderColor
import com.wqz.allinone.ui.property.BorderWidth
import com.wqz.allinone.ui.property.ButtonCategory
import com.wqz.allinone.ui.theme.AllInOneTheme
import com.wqz.allinone.ui.theme.ThemeColor
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * 日记预览
 * Created by Wu Qizhen on 2024.10.13
 * Refactored by Wu Qizhen on 2024.11.30
 */
class DiaryPreviewActivity : ComponentActivity() {
    private lateinit var viewModel: DiaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = DiaryViewModel(application)

        setContent {
            AllInOneTheme {
                AppBackground.BreathingBackground(title = R.string.diary) {
                    DiaryPreviewScreen()
                }
            }
        }
    }

    @Composable
    fun DiaryPreviewScreen() {
        val context = LocalContext.current

        val currentDate = remember { mutableStateOf(LocalDate.now()) }
        val dateFormat = DateTimeFormatter.ofPattern("yyyy年MM月", Locale.CHINA)
        var showJump by remember { mutableStateOf(false) }
        var inputMonth by remember {
            mutableStateOf(
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM"))
            )
        }

        val diaries by viewModel.diaries.observeAsState(emptyList())

        /*Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            XItem.Button(
                icon = R.drawable.ic_security,
                text = stringResource(id = R.string.password)
            ) {
                startActivity(Intent(context, PasswordChangeActivity::class.java))
            }

            Spacer(modifier = Modifier.width(10.dp))

            XItem.Button(
                icon = R.drawable.ic_edit,
                text = stringResource(R.string.record)
            ) {
                startActivity(Intent(context, DiaryEditActivity::class.java))
            }
        }*/

        XItem.Button(
            icon = R.drawable.ic_edit,
            text = stringResource(R.string.record)
        ) {
            startActivity(Intent(context, DiaryEditActivity::class.java))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = {
                    currentDate.value = currentDate.value.minusMonths(1)
                    viewModel.setDate(currentDate.value)
                },
                content = {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowLeft,
                        contentDescription = "上个月",
                        tint = Color.White
                    )
                },
                modifier = Modifier.size(25.dp)
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = dateFormat.format(currentDate.value),
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
                    currentDate.value = currentDate.value.plusMonths(1)
                    viewModel.setDate(currentDate.value)
                },
                content = {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowRight,
                        contentDescription = "下个月",
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

                XCard.LivelyCard {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = inputMonth,
                        onValueChange = { inputMonth = it },
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
                                text = "2000.01",
                                color = Color.Gray,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    )

                    Row {
                        XItem.Button(icon = R.drawable.ic_locate, text = "本月") {
                            currentDate.value = LocalDate.now()
                            viewModel.setDate(currentDate.value)
                            showJump = false
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        XItem.Button(icon = R.drawable.ic_jump, text = "跳转") {
                            if (isValidYearMonth(inputMonth)) {
                                val year = inputMonth.substring(0, 4).toInt()
                                val month = inputMonth.substring(5, 7).toInt()
                                currentDate.value = LocalDate.of(year, month, 1)
                                viewModel.setDate(currentDate.value)
                                showJump = false
                            } else {
                                Toast.makeText(
                                    context,
                                    R.string.invalid_date,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        if (diaries.isEmpty()) {
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

            diaries.forEach {
                key(it.id) {
                    DiaryItem(diary = it)
                }
            }

            Spacer(modifier = Modifier.height(47.dp))
        }
    }

    @Composable
    fun DiaryItem(
        diary: Diary
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BackgroundColor.PRESSED_GRAY else BackgroundColor.DEFAULT_GRAY
        val borderColors = BorderColor.DEFAULT_GRAY
        val borderWidth = BorderWidth.DEFAULT_WIDTH

        var showDialog by remember { mutableIntStateOf(0) }

        AnimatedVisibility(
            visible = showDialog == 0,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                Spacer(modifier = Modifier.height(3.dp))
                Row(
                    modifier = Modifier
                        .clickVfx(
                            interactionSource = interactionSource,
                            enabled = true,
                            onClick = {
                                val intent =
                                    Intent(this@DiaryPreviewActivity, DiaryEditActivity::class.java)
                                intent.putExtra("DIARY_ID", diary.id)
                                startActivity(intent)
                            },
                            onLongClick = {
                                showDialog = 1
                            }
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
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.wrapContentWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = diary.date.format(DateTimeFormatter.ofPattern("dd")),
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.din_bold)),
                            maxLines = 1
                        )

                        Text(
                            text = diary.date.dayOfWeek.getDisplayName(
                                java.time.format.TextStyle.FULL,
                                Locale.CHINA
                            ),
                            fontSize = 14.sp,
                            color = Color.Gray,
                            maxLines = 1
                        )

                        Spacer(modifier = Modifier.height(5.dp))
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    Column(
                        modifier = Modifier.wrapContentWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = painterResource(id = OptionData.weatherOptions[diary.weather - 1].icon),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )

                        Image(
                            painter = painterResource(id = OptionData.moodOptions[diary.mood - 1].icon),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = diary.content,
                            fontSize = 12.sp,
                            maxLines = 4
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))
            }
        }

        AnimatedVisibility(
            visible = showDialog == 1,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                Spacer(modifier = Modifier.height(3.dp))

                Column(
                    modifier = Modifier
                        .clickVfx()
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
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.delete_confirm_hint),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        XItem.Button(
                            text = stringResource(R.string.delete),
                            color = ButtonCategory.WARNING_BUTTON
                        ) {
                            showDialog = 2
                            Toast.makeText(
                                this@DiaryPreviewActivity,
                                R.string.deleted,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            viewModel.deleteDiaryWithDelay(diary.id)
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        XItem.Button(
                            text = stringResource(R.string.cancel),
                            color = ButtonCategory.SAFE_BUTTON
                        ) { showDialog = 0 }
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))
            }
        }
    }

    private fun isValidYearMonth(input: String): Boolean {
        val regex = Regex("""^\d{4}\.\d{2}$""")
        if (!regex.matches(input)) return false

        val parts = input.split(".")
        val year = parts[0].toIntOrNull()
        val month = parts[1].toIntOrNull()

        return year != null && month != null && month in 1..12
    }
}