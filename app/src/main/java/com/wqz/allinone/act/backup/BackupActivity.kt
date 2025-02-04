package com.wqz.allinone.act.backup

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.wqz.allinone.R
import com.wqz.allinone.act.backup.viewmodel.BackupViewModel
import com.wqz.allinone.ui.AppBackground
import com.wqz.allinone.ui.XCard
import com.wqz.allinone.ui.XItem
import com.wqz.allinone.ui.property.BorderWidth
import com.wqz.allinone.ui.theme.AllInOneTheme
import com.wqz.allinone.ui.theme.ThemeColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 数据导出
 * Created by Wu Qizhen on 2024.11.17
 */
class BackupActivity : ComponentActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var viewModel: BackupViewModel
    private var onPermissionGranted: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化权限请求
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    onPermissionGranted()
                } else {
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show()
                }
            }

        viewModel = BackupViewModel(application)

        setContent {
            AllInOneTheme {
                AppBackground.BreathingBackground(
                    R.string.backup
                ) {
                    BackupScreen()
                }
            }
        }
    }

    @Composable
    fun BackupScreen() {
        val context = LocalContext.current
        var enabled by remember { mutableStateOf(true) }
        val (todoExport, onTodoStateChange) = remember { mutableStateOf(true) }
        val (noteExport, onNoteStateChange) = remember { mutableStateOf(true) }
        val (diaryExport, onDiaryStateChange) = remember { mutableStateOf(true) }
        val (bookmarkExport, onBookmarkStateChange) = remember { mutableStateOf(true) }
        val parentState = remember(todoExport, noteExport, diaryExport, bookmarkExport) {
            if (todoExport && noteExport && diaryExport && bookmarkExport) ToggleableState.On
            else if (!todoExport && !noteExport && !diaryExport && !bookmarkExport) ToggleableState.Off
            else ToggleableState.Indeterminate
        }
        val onParentClick = {
            val s = parentState != ToggleableState.On
            onTodoStateChange(s)
            onNoteStateChange(s)
            onDiaryStateChange(s)
            onBookmarkStateChange(s)
        }
        val exportStatus by viewModel.exportStatus.collectAsState(initial = listOf("* 导出结果：")) //.observeAsState(emptyList())
        var exportStatusString by remember { mutableStateOf("* 导出结果：") }
        val pendingOperations = mutableListOf<() -> Unit>()

        LaunchedEffect(exportStatus) {
            exportStatusString = exportStatus.joinToString(separator = "\n")
        }

        XCard.LivelyCard {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TriStateCheckbox(
                    modifier = Modifier.size(20.dp),
                    state = parentState, onClick = onParentClick,
                    colors = CheckboxDefaults.colors(checkedColor = ThemeColor)
                )

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    text = "全选",
                    fontSize = 16.sp,
                    maxLines = 1
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "├",
                    fontSize = 16.sp,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.width(10.dp))

                Checkbox(
                    modifier = Modifier.size(20.dp),
                    checked = todoExport,
                    onCheckedChange = onTodoStateChange,
                    colors = CheckboxDefaults.colors(checkedColor = ThemeColor)
                )

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    text = "待办箱",
                    fontSize = 16.sp,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "├",
                    fontSize = 16.sp,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.width(10.dp))

                Checkbox(
                    modifier = Modifier.size(20.dp),
                    checked = noteExport,
                    onCheckedChange = onNoteStateChange,
                    colors = CheckboxDefaults.colors(checkedColor = ThemeColor)
                )

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    text = "随手记",
                    fontSize = 16.sp,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "├",
                    fontSize = 16.sp,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.width(10.dp))

                Checkbox(
                    modifier = Modifier.size(20.dp),
                    checked = diaryExport,
                    onCheckedChange = onDiaryStateChange,
                    colors = CheckboxDefaults.colors(checkedColor = ThemeColor)
                )

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    text = "生活书",
                    fontSize = 16.sp,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "└",
                    fontSize = 16.sp,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.width(10.dp))

                Checkbox(
                    modifier = Modifier.size(20.dp),
                    checked = bookmarkExport,
                    onCheckedChange = onBookmarkStateChange,
                    colors = CheckboxDefaults.colors(checkedColor = ThemeColor)
                )

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    text = "书签宝",
                    fontSize = 16.sp,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Divider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = BorderWidth.DEFAULT_WIDTH,
                color = Color(54, 54, 54)
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 15.dp, 15.dp, 0.dp),
                text = "* 文件将导出到系统下载目录",
                color = ThemeColor,
                fontSize = 12.sp,
                textAlign = TextAlign.Start
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 0.dp, 15.dp, 15.dp),
                text = exportStatusString,
                color = ThemeColor,
                fontSize = 12.sp,
                textAlign = TextAlign.Start
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (enabled) {
            XItem.Button(
                icon = R.drawable.ic_todo,
                text = stringResource(id = R.string.execute)
            ) {
                if (todoExport) {
                    pendingOperations.add { viewModel.exportDatabase(context, "Todo", "待办箱") }
                }
                if (noteExport) {
                    pendingOperations.add { viewModel.exportDatabase(context, "Note", "随手记") }
                }
                if (diaryExport) {
                    pendingOperations.add { viewModel.exportDatabase(context, "Diary", "生活书") }
                }
                if (bookmarkExport) {
                    pendingOperations.add {
                        viewModel.viewModelScope.launch { viewModel.exportBookmarks() }
                    }
                }

                if (pendingOperations.isNotEmpty()) {
                    if (ContextCompat.checkSelfPermission(
                            this@BackupActivity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        // 权限已经授予，立即执行导出操作
                        pendingOperations.forEach { it() }
                        pendingOperations.clear()
                    } else {
                        // 权限未授予，触发权限请求
                        onPermissionGranted = {
                            pendingOperations.forEach { it() }
                            pendingOperations.clear()
                        }
                        requestStoragePermission()
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

        Spacer(modifier = Modifier.height(50.dp))
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun requestStoragePermission(/*onPermissionGranted: () -> Unit*/) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            onPermissionGranted()
        }
    }
}