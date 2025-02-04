package com.wqz.allinone.act.diary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.wqz.allinone.R
import com.wqz.allinone.act.diary.data.OptionData
import com.wqz.allinone.act.diary.viewmodel.DiaryViewModel
import com.wqz.allinone.entity.Diary
import com.wqz.allinone.ui.AppBackground
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.XCard
import com.wqz.allinone.ui.XItem
import com.wqz.allinone.ui.property.BorderWidth
import com.wqz.allinone.ui.theme.AllInOneTheme
import com.wqz.allinone.ui.theme.ThemeColor
import kotlinx.coroutines.launch
import java.time.DateTimeException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * 编辑日记
 * Created by Wu Qizhen on 2024.10.13
 * Refactored by Wu Qizhen on 2024.11.30
 */
class DiaryEditActivity : ComponentActivity() {
    private lateinit var viewModel: DiaryViewModel
    private val weatherSelectLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getIntExtra("selected_weather", -1)?.let { selectedWeatherId ->
                    // 找到对应的 Option 并通过 ViewModel 设置
                    OptionData.weatherOptions.firstOrNull { it.id == selectedWeatherId }
                        ?.let { option ->
                            viewModel.setSelectedWeather(option)
                        }
                }
            }
        }
    private val moodSelectLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getIntExtra("selected_mood", -1)?.let { selectedMoodId ->
                    // 找到对应的 Option 并通过 ViewModel 设置
                    OptionData.moodOptions.firstOrNull { it.id == selectedMoodId }
                        ?.let { option ->
                            viewModel.setSelectedMood(option)
                        }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = DiaryViewModel(application)

        val diaryId = intent.getLongExtra("DIARY_ID", -1)

        var diary = Diary(
            id = -1,
            content = "",
            date = LocalDate.now(),
            weather = -1,
            mood = -1
        )

        viewModel.viewModelScope.launch {
            if (diaryId != (-1).toLong()) {
                diary = viewModel.getDiary(diaryId)
            }

            setContent {
                AllInOneTheme {
                    AppBackground.BreathingBackground {
                        DiaryEditScreen(diary)
                    }
                }
            }
        }
    }

    @Composable
    fun DiaryEditScreen(
        currentDiary: Diary
    ) {
        val context = LocalContext.current
        val scrollState = rememberScrollState()

        var content by remember { mutableStateOf(if (currentDiary.id >= 0) currentDiary.content else "") }
        var contentLength by remember { mutableIntStateOf(content.length) }

        var selectedDate by remember { mutableStateOf(if (currentDiary.id >= 0) currentDiary.date else LocalDate.now()) }
        val formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日", Locale.CHINA)
        var showDateSelect by remember { mutableStateOf(false) }
        // var inputDate by remember { mutableStateOf(selectedDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))) }
        var inputDate by remember { mutableStateOf("") }

        val selectedWeather by viewModel.selectedWeather.collectAsStateWithLifecycle(/*if (currentDiary.id >= 0) OptionData.weatherOptions[currentDiary.weather] else */
            null
        )
        val selectedMood by viewModel.selectedMood.collectAsStateWithLifecycle(/*if (currentDiary.id >= 0) OptionData.moodOptions[currentDiary.mood] else */
            null
        )

        /*if (currentDiary.id >= 0){
            OptionData.weatherOptions[currentDiary.weather].let {
                viewModel.setSelectedWeather(it)
            }
            OptionData.moodOptions[currentDiary.mood].let {
                viewModel.setSelectedMood(it)
            }
        }*/

        LaunchedEffect(key1 = currentDiary.id) {
            // 检查是否已经设置了天气，如果没有，则设置初始值
            if (viewModel.selectedWeather.value == null && currentDiary.id >= 0) {
                OptionData.weatherOptions.firstOrNull { it.id == currentDiary.weather }.let {
                    viewModel.setSelectedWeather(it)
                }
            }
            // 检查是否已经设置了心情，如果没有，则设置初始值
            if (viewModel.selectedMood.value == null && currentDiary.id >= 0) {
                OptionData.moodOptions.firstOrNull { it.id == currentDiary.mood }.let {
                    viewModel.setSelectedMood(it)
                }
            }
        }

        LaunchedEffect(content) {
            contentLength = content.length // 在内容改变时计算字数
        }

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
            Spacer(modifier = Modifier.height(15.dp))

            Row(modifier = Modifier.wrapContentSize()) {
                IconButton(
                    onClick = {
                        finish()
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "返回",
                            tint = Color.White
                        )
                    },
                    modifier = Modifier.size(25.dp)
                )

                Spacer(modifier = Modifier.width(20.dp))

                IconButton(
                    onClick = {
                        if (content.isEmpty()) {
                            Toast.makeText(
                                this@DiaryEditActivity,
                                R.string.input_empty,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@IconButton
                        }
                        viewModel.viewModelScope.launch {
                            if (currentDiary.id < 0) {
                                viewModel.insertDiary(
                                    Diary(
                                        content = content,
                                        date = selectedDate,
                                        weather = selectedWeather?.id ?: 1,
                                        mood = selectedMood?.id ?: 1
                                    )
                                )
                            } else {
                                viewModel.updateDiary(
                                    Diary(
                                        id = currentDiary.id,
                                        content = content,
                                        date = selectedDate,
                                        weather = selectedWeather?.id ?: 1,
                                        mood = selectedMood?.id ?: 1
                                    )
                                )
                            }
                        }
                        Toast.makeText(
                            this@DiaryEditActivity,
                            R.string.saved,
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = "保存",
                            tint = Color.White
                        )
                    },
                    modifier = Modifier.size(25.dp)
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            XCard.LivelyCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "日期：",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Text(
                        text = "${selectedDate.format(formatter)}\n${
                            selectedDate.dayOfWeek.getDisplayName(
                                java.time.format.TextStyle.FULL,
                                Locale.CHINA
                            )
                        }",
                        fontSize = 14.sp,
                        modifier = Modifier.clickVfx {
                            showDateSelect = !showDateSelect
                        }
                    )
                }

                AnimatedVisibility(
                    visible = showDateSelect,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth(),
                            thickness = BorderWidth.DEFAULT_WIDTH,
                            color = Color(54, 54, 54)
                        )

                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = inputDate,
                            onValueChange = { inputDate = it },
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
                                    color = Color.Gray,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        )

                        Row {
                            XItem.Button(icon = R.drawable.ic_locate, text = "今天") {
                                selectedDate = LocalDate.now()
                                showDateSelect = false
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            XItem.Button(icon = R.drawable.ic_jump, text = "跳转") {
                                if (inputDate.matches(Regex("\\d{4}.\\d{2}.\\d{2}"))) {
                                    val parts = inputDate.split(".")
                                    val year = parts[0].toInt()
                                    val month = parts[1].toInt()
                                    val day = parts[2].toInt()
                                    try {
                                        selectedDate = LocalDate.of(year, month, day)
                                    } catch (e: DateTimeException) {
                                        Toast.makeText(
                                            context,
                                            R.string.invalid_date,
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                    showDateSelect = false
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

                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = BorderWidth.DEFAULT_WIDTH,
                    color = Color(54, 54, 54)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "天气：",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    // 根据 selectedWeather 的值显示相应的天气图标或文本
                    if (selectedWeather == null) {
                        Text(
                            text = "选择天气",
                            fontSize = 16.sp,
                            modifier = Modifier.clickVfx {
                                weatherSelectLauncher.launch(
                                    Intent(
                                        context,
                                        WeatherSelectActivity::class.java
                                    )
                                )
                            }
                        )
                    } else {
                        val option = selectedWeather
                        if (option != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickVfx {
                                    weatherSelectLauncher.launch(
                                        Intent(
                                            context,
                                            WeatherSelectActivity::class.java
                                        )
                                    )
                                }
                            ) {
                                Image(
                                    painter = painterResource(id = option.icon),
                                    contentDescription = option.text,
                                    modifier = Modifier.size(30.dp)
                                )

                                Spacer(modifier = Modifier.width(5.dp))

                                Text(text = option.text, fontSize = 16.sp)
                            }
                        }
                    }
                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = BorderWidth.DEFAULT_WIDTH,
                    color = Color(54, 54, 54)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "心情：",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    if (selectedMood == null) {
                        Text(
                            text = "选择心情",
                            fontSize = 16.sp,
                            modifier = Modifier.clickVfx {
                                moodSelectLauncher.launch(
                                    Intent(
                                        context,
                                        MoodSelectActivity::class.java
                                    )
                                )
                            }
                        )
                    } else {
                        val option = selectedMood
                        if (option != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickVfx {
                                    moodSelectLauncher.launch(
                                        Intent(
                                            context,
                                            MoodSelectActivity::class.java
                                        )
                                    )
                                }
                            ) {
                                Image(
                                    painter = painterResource(id = option.icon),
                                    contentDescription = option.text,
                                    modifier = Modifier.size(30.dp)
                                )

                                Spacer(modifier = Modifier.width(5.dp))

                                Text(text = option.text, fontSize = 16.sp)
                            }
                        }
                    }
                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = BorderWidth.DEFAULT_WIDTH,
                    color = Color(54, 54, 54)
                )

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
                            text = "内容",
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
        }
    }
}