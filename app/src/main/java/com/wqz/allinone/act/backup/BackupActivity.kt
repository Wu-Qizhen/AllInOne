package com.wqz.allinone.act.backup

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream

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
        val (diaryExport, onDiaryStateChange) = remember { mutableStateOf(true) }
        val (bookmarkExport, onBookmarkStateChange) = remember { mutableStateOf(true) }
        val parentState = remember(diaryExport, bookmarkExport) {
            if (diaryExport && bookmarkExport) ToggleableState.On
            else if (!diaryExport && !bookmarkExport) ToggleableState.Off
            else ToggleableState.Indeterminate
        }
        val onParentClick = {
            val s = parentState != ToggleableState.On
            onDiaryStateChange(s)
            onBookmarkStateChange(s)
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
                    .padding(start = 50.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    modifier = Modifier.size(20.dp),
                    checked = diaryExport,
                    onCheckedChange = onDiaryStateChange,
                    colors = CheckboxDefaults.colors(checkedColor = ThemeColor)
                )

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    text = "日记",
                    fontSize = 16.sp,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 50.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    modifier = Modifier.size(20.dp),
                    checked = bookmarkExport,
                    onCheckedChange = onBookmarkStateChange,
                    colors = CheckboxDefaults.colors(checkedColor = ThemeColor)
                )

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    text = "书签",
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
                    .padding(15.dp),
                text = "* 文件将导出到系统下载目录",
                color = ThemeColor,
                fontSize = 12.sp,
                maxLines = 1,
                textAlign = TextAlign.Start
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (enabled) {
            XItem.Button(
                icon = R.drawable.ic_todo,
                text = stringResource(id = R.string.execute)
            ) {
                if (diaryExport && bookmarkExport) {
                    onPermissionGranted = {
                        exportDiaries()
                        viewModel.viewModelScope.launch { viewModel.exportBookmarks() }
                    }
                } else if (diaryExport) {
                    onPermissionGranted = ::exportDiaries
                } else if (bookmarkExport) {
                    onPermissionGranted = {
                        viewModel.viewModelScope.launch { viewModel.exportBookmarks() }
                    }
                }
                (context as BackupActivity).requestStoragePermission()
                enabled = false
            }
        } else {
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .background(Color.DarkGray, RoundedCornerShape(50.dp))
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

    private fun exportDiaries() {
        val sourceFile = getDatabasePath("Diary") // 替换为你的数据库名称
        val backupFileName = "Diary.db"

        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, backupFileName)
            put(MediaStore.Downloads.MIME_TYPE, "application/x-sqlite3")
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val uri: Uri? = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
        uri?.let { targetUri ->
            try {
                contentResolver.openOutputStream(targetUri)?.use { outputStream ->
                    sourceFile.inputStream().use { inputStream ->
                        copyFile(inputStream, outputStream)
                    }
                }
                Toast.makeText(this, R.string.backup_diary_success, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, R.string.backup_diary_failed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun copyFile(inputStream: InputStream, outputStream: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        while (inputStream.read(buffer).also { read = it } != -1) {
            outputStream.write(buffer, 0, read)
        }
    }
}