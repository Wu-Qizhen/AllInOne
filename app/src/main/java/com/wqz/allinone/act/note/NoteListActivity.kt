package com.wqz.allinone.act.note

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.act.note.viewmodel.NoteViewModel
import com.wqz.allinone.entity.Note
import com.wqz.allinone.ui.CirclesBackground
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.TitleBar
import com.wqz.allinone.ui.color.BackgroundColor
import com.wqz.allinone.ui.color.BorderColor
import com.wqz.allinone.ui.theme.AllInOneTheme

/**
 * 笔记列表
 * Created by Wu Qizhen on 2024.6.30
 */
class NoteListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = NoteViewModel(application)

        enableEdgeToEdge()
        setContent {
            AllInOneTheme {
                CirclesBackground.RegularBackground {
                    NoteListScreen(viewModel)
                }
            }
        }
    }

    @Composable
    fun NoteListScreen(
        // notes: List<Note>
        // noteDao: NoteDao
        viewModel: NoteViewModel
    ) {
        val context = LocalContext.current
        val scrollState = rememberScrollState()
        val notes by viewModel.notes.observeAsState(emptyList())

        /*val notesState = remember { mutableStateListOf<Note>() }
        // 使用 LaunchedEffect 确保在 Composition 时订阅 Flow，并在退出时取消订阅
        LaunchedEffect(key1 = Unit) {
            noteDao.getAllAsFlow().collect { notes ->
                // 当数据库中的数据发生变化时，这个块会被调用
                // 更新 UI 状态
                notesState.clear()
                notesState.addAll(notes)
            }
        }*/

        /*val notesState = remember { mutableStateOf<List<Note>>(emptyList()) }
        // 使用 LaunchedEffect 确保在 Composition 时订阅 Flow，并在退出时取消订阅
        LaunchedEffect(key1 = Unit) {
            noteDao.getAllAsFlow().collect { notes ->
                // 当数据库中的数据发生变化时，这个块会被调用
                // 这里您可以更新 UI，例如使用状态提升
                notesState.value = notes
            }
        }*/

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleBar.TextTitleBar(title = R.string.btn_note)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickVfx {
                        val intent = Intent(context, NoteEditActivity::class.java)
                        intent.putExtra("NOTE_ID", -1)
                        startActivity(intent)
                    }
                    .wrapContentSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "新建笔记",
                    modifier = Modifier
                        .size(25.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = stringResource(R.string.btn_add_note),
                    fontSize = 16.sp
                )
            }
            if (notes.isEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_no_data),
                    contentDescription = "无数据",
                    modifier = Modifier
                        .size(100.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(7.dp))
                for (note in notes) {
                    Spacer(modifier = Modifier.height(3.dp))
                    NoteItem(note = note)
                    Spacer(modifier = Modifier.height(3.dp))
                }
                /*LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    items(notes.size) { index ->
                        val note = notes[index]
                        NoteItem(note)
                    }
                }*/
                Spacer(modifier = Modifier.height(37.dp))
            }
        }
    }

    @Composable
    fun NoteItem(note: Note) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BackgroundColor.PRESSED_GRAY else BackgroundColor.DEFAULT_GRAY
        val borderColors = BorderColor.DEFAULT_GRAY
        val borderWidth = 0.4f.dp

        Column(
            modifier = Modifier
                .clickVfx(
                    interactionSource,
                    true
                ) {
                    val intent = Intent(this@NoteListActivity, NoteEditActivity::class.java)
                    intent.putExtra("NOTE_ID", note.id)
                    intent.putExtra("NOTE_TITLE", note.title)
                    intent.putExtra("NOTE_CONTENT", note.content)
                    intent.putExtra("NOTE_CREATE_TIME", note.createTime)
                    intent.putExtra("NOTE_UPDATE_TIME", note.updateTime)
                    startActivity(intent)
                }
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
            Text(text = note.title.ifEmpty { "无标题" }, fontSize = 16.sp, maxLines = 1)
            Text(
                text = note.content.ifEmpty { "无附加文案" },
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = note.updateTime, fontSize = 12.sp, color = Color.Gray)
        }
    }

    /*@Preview
    @Composable
    fun NoteListScreenPreview() {
        AllInOneTheme {
            CirclesBackground.RegularBackground {
                NoteListScreen(listOf(Note(1, "标题", "", "2023年7月1日 12:00", "2023年7月1日 12:00")))
            }
        }
    }

    @Preview
    @Composable
    fun NoteItemPreview() {
        AllInOneTheme {
            NoteItem(
                Note(
                    id = 1,
                    title = "标题",
                    content = "",
                    createTime = "2023年7月1日 12:00",
                    updateTime = "2023年7月1日 12:00"
                )
            )
        }
    }*/
}