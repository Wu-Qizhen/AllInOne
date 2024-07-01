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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.room.Room
import com.wqz.allinone.R
import com.wqz.allinone.dao.NoteDao
import com.wqz.allinone.database.NoteDatabase
import com.wqz.allinone.entity.Note
import com.wqz.allinone.ui.CirclesBackground
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.TitleBar
import com.wqz.allinone.ui.theme.AllInOneTheme
import com.wqz.allinone.ui.theme.BorderColors
import com.wqz.allinone.ui.theme.DefaultBackgroundColor
import com.wqz.allinone.ui.theme.PressedBackgroundColor

class NoteListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val noteDatabase = Room.databaseBuilder(this, NoteDatabase::class.java, "Note").build()
        val noteDao = noteDatabase.noteDao()

        enableEdgeToEdge()
        setContent {
            AllInOneTheme {
                CirclesBackground.RegularBackground {
                    NoteListScreen(noteDao)
                }
            }
        }
    }

    @Composable
    fun NoteListScreen(/*notes: List<Note>*/ noteDao: NoteDao) {
        val context = LocalContext.current
        // val scrollState = rememberScrollState()

        /*val noteDatabase = Room.databaseBuilder(this, NoteDatabase::class.java, "Note").build()
        val noteDao = noteDatabase.noteDao()
        val notes = noteDao.getAll()*/

        val notesFlow = noteDao.getAllAsFlow()
        val notes by notesFlow.collectAsState(initial = emptyList())

        /*// 使用 rememberLauncherForActivityResult 来处理 Activity 的结果
        val noteEditLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                // 从结果中获取数据
                val data = result.data
                if (data != null&& data.hasExtra("NOTE_RESULT")) {
                    // 更新列表
                }
            }
        }*/

        Column(
            modifier = Modifier
                .fillMaxWidth()
                // .verticalScroll(scrollState)
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
                        // noteEditLauncher.launch(intent)
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
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                Modifier
                    .fillMaxSize()
            ) {
                items(notes.size) { index ->
                    val note = notes[index]
                    NoteItem(note)
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    @Composable
    fun NoteItem(note: Note) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) PressedBackgroundColor else DefaultBackgroundColor
        val borderColors = BorderColors
        val borderWidth = 0.4f.dp

        Column(modifier = Modifier
            .clickVfx(interactionSource, true) {}
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
            .padding(10.dp)) {
            Text(text = note.title.ifEmpty { "无标题" }, fontSize = 16.sp)
            Text(text = note.content.ifEmpty { "无附加文案" }, fontSize = 14.sp, color = Color.Gray)
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