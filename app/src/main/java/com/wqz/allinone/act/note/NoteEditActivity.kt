package com.wqz.allinone.act.note

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.wqz.allinone.R
import com.wqz.allinone.act.note.viewmodel.NoteViewModel
import com.wqz.allinone.entity.Note
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.XBackground
import com.wqz.allinone.ui.XToast
import com.wqz.allinone.ui.color.ContentColor
import com.wqz.allinone.ui.color.TextFieldColor
import com.wqz.allinone.ui.theme.ThemeColor
import kotlinx.coroutines.launch

/**
 * 笔记编辑
 * Created by Wu Qizhen on 2024.7.1
 * Refactored by Wu Qizhen on 2024.11.30
 */
class NoteEditActivity : ComponentActivity() {
    private lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = NoteViewModel(application)
        val noteId = intent.getIntExtra("NOTE_ID", -1)

        viewModel.viewModelScope.launch {
            val note = if (noteId != -1) viewModel.getNote(noteId) else Note(
                id = -1,
                title = "",
                content = "",
                createTime = viewModel.getDateTime(),
                updateTime = viewModel.getDateTime(),
                isLocked = false
            )

            setContent {
                XBackground.BreathingBackground {
                    NoteEditScreen(
                        currentNote = note
                    )
                }
            }
        }
    }

    @Composable
    fun NoteEditScreen(
        currentNote: Note
    ) {
        val scrollState = rememberScrollState()
        var note by remember { mutableStateOf(currentNote) }
        var isLocked by remember { mutableStateOf(note.isLocked) }
        val title = remember { mutableStateOf(note.title) }
        val content = remember { mutableStateOf(note.content) }
        var updateTime by remember { mutableStateOf(note.updateTime) }
        var contentLength by remember { mutableIntStateOf(0) }

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
                            finish()
                        },
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "返回",
                                tint = Color.White
                            )
                        },
                        modifier = Modifier.size(25.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    if (note.id != -1) {
                        Image(
                            painter = painterResource(id = if (isLocked) R.drawable.ic_lock else R.drawable.ic_unlock),
                            contentDescription = "锁定",
                            modifier = Modifier
                                .size(20.dp)
                                .clickVfx {
                                    isLocked = !isLocked
                                    note.isLocked = isLocked
                                    note.id?.let { viewModel.updateLockStatus(it, isLocked) }
                                    XToast.showText("已${if (isLocked) "锁定" else "解锁"}笔记")
                                }
                        )

                        /*IconButton(
                            onClick = {
                                note.isLocked = !note.isLocked
                                note.id?.let { viewModel.updateLockStatus(it, note.isLocked) }
                            },
                            content = {
                                Icon(
                                    painter = painterResource(id = if (note.isLocked) R.drawable.ic_lock else R.drawable.ic_unlock),
                                    contentDescription = "锁定",
                                    tint = Color.White
                                )
                            },
                            modifier = Modifier.size(25.dp)
                        )*/

                        Spacer(modifier = Modifier.width(10.dp))
                    }

                    IconButton(
                        onClick = {
                            // onSaveClick(noteId, title.value, content.value, noteDao)
                            // 校验内容是否为空
                            if (content.value.isEmpty() && title.value.isEmpty()) {
                                XToast.showText(
                                    this@NoteEditActivity,
                                    R.string.input_empty
                                )
                                return@IconButton
                            }
                            viewModel.viewModelScope.launch {
                                note.title = title.value
                                note.content = content.value
                                note = viewModel.saveNote(note)
                                updateTime = note.updateTime
                            }
                            XToast.showText(
                                this@NoteEditActivity,
                                R.string.saved
                            )
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
                colors = TextFieldColor.colors(),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.misans_regular))
                ),
                placeholder = {
                    Text(
                        text = "标题",
                        color = Color.Gray,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = updateTime,
                    color = Color.LightGray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(text = "${contentLength}字", color = Color.LightGray, fontSize = 12.sp)

                Spacer(modifier = Modifier.width(5.dp))

                Row(
                    modifier = Modifier
                        .width(25.dp)
                        .background(ThemeColor, RoundedCornerShape(10.dp)),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (note.id == -1) "NEW" else note.id.toString(),
                        fontSize = 10.sp,
                        color = ContentColor.DEFAULT_BROWN
                    )
                }
            }

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = content.value,
                onValueChange = { content.value = it },
                colors = TextFieldColor.colors(),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.misans_regular))
                ),
                placeholder = {
                    Text(
                        text = "记点什么吧 (●'◡'●)",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            )

            Spacer(modifier = Modifier.height(34.dp))
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