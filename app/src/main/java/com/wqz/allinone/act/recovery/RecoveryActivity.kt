package com.wqz.allinone.act.recovery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.wqz.allinone.R
import com.wqz.allinone.act.recovery.viewmodel.RecoveryViewModel
import com.wqz.allinone.ui.XBackground
import com.wqz.allinone.ui.XCard
import com.wqz.allinone.ui.XItem
import com.wqz.allinone.ui.XToast
import com.wqz.allinone.ui.color.TextFieldColor
import com.wqz.allinone.ui.property.BorderWidth
import com.wqz.allinone.ui.theme.ThemeColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 数据导入
 * Created by Wu Qizhen on 2025.2.4
 */
class RecoveryActivity : ComponentActivity() {
    private lateinit var viewModel: RecoveryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = RecoveryViewModel(application)

        setContent {
            XBackground.BreathingBackground(
                R.string.recovery
            ) {
                RecoveryScreen()
            }
        }
    }

    @Composable
    fun RecoveryScreen() {
        // val context = LocalContext.current
        var enabled by remember { mutableStateOf(true) }
        val importObjects = listOf("待办箱", "随手记", "生活书", "书签宝")
        val (selectedImportObject, setSelectedImportObject) = remember {
            mutableStateOf(
                importObjects[0]
            )
        }
        var importContent by remember { mutableStateOf("") }
        val importStatus by viewModel.importStatus.collectAsState(initial = listOf("* 导入结果："))
        var importStatusString by remember { mutableStateOf("* 导入结果：") }

        LaunchedEffect(importStatus) {
            importStatusString = importStatus.joinToString(separator = "\n")
        }

        XCard.LivelyCard {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 15.dp, 15.dp, 0.dp),
                text = "导入对象",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(5.dp))

            // 导入对象选择器
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                importObjects.forEach { obj ->
                    Row(
                        modifier = Modifier.height(30.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (obj == selectedImportObject),
                            onClick = { setSelectedImportObject(obj) },
                            colors = RadioButtonDefaults.colors(selectedColor = ThemeColor)
                        )
                        Text(
                            text = obj,
                            fontSize = 16.sp,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = BorderWidth.DEFAULT_WIDTH,
                color = Color(54, 54, 54)
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = importContent,
                onValueChange = { importContent = it },
                colors = TextFieldColor.colors(),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.misans_regular)),
                ),
                placeholder = {
                    Text(
                        text = "JSON/TXT",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = BorderWidth.DEFAULT_WIDTH,
                color = Color(54, 54, 54)
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 15.dp, 15.dp, 0.dp),
                text = "* 仅对 JSON/TXT 支持导入",
                color = ThemeColor,
                fontSize = 12.sp,
                textAlign = TextAlign.Start
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 0.dp, 15.dp, 15.dp),
                text = importStatusString,
                color = ThemeColor,
                fontSize = 12.sp,
                textAlign = TextAlign.Start
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            XItem.Button(
                icon = R.drawable.ic_delete,
                text = stringResource(id = R.string.clear)
            ) {
                importContent = ""
                XToast.showText("已清空")
            }

            Spacer(modifier = Modifier.width(10.dp))

            if (enabled) {
                XItem.Button(
                    icon = R.drawable.ic_todo,
                    text = stringResource(id = R.string.execute)
                ) {
                    if (importContent.isNotEmpty()) {
                        when (selectedImportObject) {
                            "待办箱" -> {
                                viewModel.importTodos(importContent)
                            }

                            "随手记" -> {
                                viewModel.importNotes(importContent)
                            }

                            "生活书" -> {
                                viewModel.importDiaries(importContent)
                            }

                            "书签宝" -> {
                                viewModel.viewModelScope.launch {
                                    viewModel.importBookmarks(importContent)
                                }
                            }
                        }
                    }

                    enabled = false

                    // 启动协程延迟启用按钮
                    viewModel.viewModelScope.launch {
                        delay(3000) // 延迟 3 秒
                        enabled = true
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(Color.Gray, RoundedCornerShape(50.dp))
                        .padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_todo),
                        tint = Color.LightGray,
                        modifier = Modifier.size(20.dp),
                        contentDescription = null,
                    )

                    Spacer(modifier = Modifier.size(5.dp))

                    Text(
                        text = stringResource(id = R.string.execute),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}