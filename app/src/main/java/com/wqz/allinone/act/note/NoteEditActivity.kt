package com.wqz.allinone.act.note

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.wqz.allinone.dao.NoteDao
import com.wqz.allinone.database.NoteDatabase
import com.wqz.allinone.entity.Note
import com.wqz.allinone.ui.CirclesBackground
import com.wqz.allinone.ui.theme.AllInOneTheme
import com.wqz.allinone.ui.theme.ThemeColor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NoteEditActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val noteId = intent.getIntExtra("NOTE_ID", -1)

            val noteDatabase =
                Room.databaseBuilder(this@NoteEditActivity, NoteDatabase::class.java, "Note")
                    .build()
            val noteDao = noteDatabase.noteDao()

            enableEdgeToEdge()
            setContent {
                AllInOneTheme {
                    CirclesBackground.RegularBackground {
                        NoteEditScreen(
                            noteId = noteId,
                            noteDao = noteDao
                            // onNavigateBack = { finish() }
                            // onBackButtonClick = ::finish
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("MainScreen", e.toString())
        }
    }

    private fun getDateTime(): String {
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm")
        return currentTime.format(formatter)
    }

    private fun onSaveClick(noteId: Int, title: String, content: String, noteDao: NoteDao) {
        val formattedUpdateTime = getDateTime()
        val note = Note(
            id = if (noteId != -1) noteId else null,
            title = title,
            content = content,
            createTime = if (noteId == -1) formattedUpdateTime else null,
            updateTime = formattedUpdateTime
        )

        // 在主线程之外执行数据库操作
        // GlobalScope.launch(Dispatchers.IO) {
        if (noteId == -1) {
            // 插入新笔记
            noteDao.insert(note)
        } else {
            // 更新现有笔记
            noteDao.update(note)
        }
        // 弹出消息
        Toast.makeText(this@NoteEditActivity, "保存成功", Toast.LENGTH_SHORT).show()
        // 返回上一个屏幕
        // onNavigateBack()
        /*val resultIntent = Intent().apply {
            // 可以在这里放置您想要返回的数据
            putExtra("NOTE_RESULT", "Note saved successfully")
        }
        setResult(RESULT_OK, resultIntent)
        finish()*/
        // }
    }

    @Composable
    fun NoteEditScreen(
        noteId: Int,
        noteDao: NoteDao
        // updateTime: String
        // onNavigateBack: () -> Unit
        // onBackButtonClick: () -> Unit
    ) {
        // val context = LocalContext.current
        val scrollState = rememberScrollState()
        val title = remember { mutableStateOf("") }
        val content = remember { mutableStateOf("") }
        var contentLength by remember { mutableIntStateOf(0) }

        val updateTime: String
        if (noteId != -1) {
            val note = noteDao.getById(noteId)!!
            updateTime = note.updateTime
            title.value = note.title
            content.value = note.content
        } else {
            updateTime = getDateTime()
        }

        LaunchedEffect(content.value) {
            contentLength = content.value.length // 在内容改变时计算字数
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 4.dp) // 输入框内部距默认 16
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Center
            ) {
                Row(modifier = Modifier.wrapContentSize()) {
                    /*Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "返回",
                        tint = Color.White,
                        modifier = Modifier
                            .clickVfx {
                                onNavigateBack
                                // onBackButtonClick
                            }
                            .size(25.dp)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "保存",
                        tint = Color.White,
                        modifier = Modifier
                            .clickVfx {

                            }
                            .size(25.dp)
                    )*/
                    IconButton(
                        onClick = {
                            // onNavigateBack() // 直接调用返回逻辑
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
                            onSaveClick(noteId, title.value, content.value, noteDao)
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
            }
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = title.value,
                onValueChange = { title.value = it },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent, // 背景颜色
                    focusedContainerColor = Color.Transparent, // 背景颜色
                    unfocusedIndicatorColor = Color.Transparent, // 下划线颜色
                    focusedIndicatorColor = Color.Transparent, // 下划线颜色
                    cursorColor = ThemeColor // 光标颜色
                ),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                placeholder = {
                    Text(
                        text = "标题",
                        color = Color.DarkGray,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
            Row {
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = updateTime,
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "${contentLength}字", color = Color.LightGray, fontSize = 12.sp)
            }
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = content.value,
                onValueChange = { content.value = it },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent, // 背景颜色
                    focusedContainerColor = Color.Transparent, // 背景颜色
                    unfocusedIndicatorColor = Color.Transparent, // 下划线颜色
                    focusedIndicatorColor = Color.Transparent, // 下划线颜色
                    cursorColor = ThemeColor // 光标颜色
                ),
                textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                placeholder = {
                    Text(
                        text = "记点什么吧",
                        color = Color.DarkGray,
                        fontSize = 14.sp
                    )
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    /*
    @Preview
    @Composable
    fun NoteEditScreenPreview() {
        AllInOneTheme {
            CirclesBackground.RegularBackground {
                NoteEditScreen()
            }
        }
    }*/
}