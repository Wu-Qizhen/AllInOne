package com.wqz.allinone.act.note

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.act.note.viewmodel.NoteViewModel
import com.wqz.allinone.entity.Note
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.XBackground
import com.wqz.allinone.ui.XCard
import com.wqz.allinone.ui.XItem
import com.wqz.allinone.ui.XToast
import com.wqz.allinone.ui.color.BackgroundColor
import com.wqz.allinone.ui.color.BorderColor
import com.wqz.allinone.ui.color.ContentColor
import com.wqz.allinone.ui.property.BorderWidth
import com.wqz.allinone.ui.property.ButtonCategory
import com.wqz.allinone.ui.theme.ThemeColor

/**
 * 笔记列表
 * Created by Wu Qizhen on 2024.6.30
 * Refactored by Wu Qizhen on 2024.11.30
 */
class NoteListActivity : ComponentActivity() {
    private lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = NoteViewModel(application)

        setContent {
            XBackground.BreathingBackground(titleId = R.string.note) {
                NoteListScreen()
            }
        }
    }

    @Composable
    fun NoteListScreen() {
        val context = LocalContext.current
        val notes by viewModel.notes.observeAsState(listOf())

        XItem.Button(
            icon = R.drawable.ic_add,
            text = stringResource(id = R.string.add_note)
        ) {
            val intent = Intent(context, NoteEditActivity::class.java)
            intent.putExtra("NOTE_ID", -1)
            startActivity(intent)
        }
        /*Row(
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
                text = stringResource(R.string.add_note),
                fontSize = 16.sp
            )
        }*/
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

            notes.forEach {
                key(it.id) {
                    // Spacer(modifier = Modifier.height(3.dp))
                    NoteItem(note = it)
                    // Spacer(modifier = Modifier.height(3.dp))
                }
            }

            Spacer(modifier = Modifier.height(47.dp))
        }
    }

    @Composable
    fun NoteItem(
        note: Note
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

                Column(
                    modifier = Modifier
                        .clickVfx(
                            interactionSource = interactionSource,
                            enabled = true,
                            onClick = {
                                val intent =
                                    Intent(this@NoteListActivity, NoteEditActivity::class.java)
                                intent.putExtra("NOTE_ID", note.id)
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = note.updateTime, fontSize = 12.sp, color = Color.Gray)

                        Row(
                            modifier = Modifier
                                .width(25.dp)
                                .background(ThemeColor, RoundedCornerShape(10.dp)),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = note.id.toString(),
                                fontSize = 10.sp,
                                color = ContentColor.DEFAULT_BROWN
                            )
                        }
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

                XCard.LivelyCard(10) {
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
                            if (!note.isLocked) {
                                showDialog = 2
                                XToast.showText(
                                    this@NoteListActivity,
                                    R.string.deleted
                                )
                                viewModel.deleteNoteWithDelay(note.id)
                            } else {
                                XToast.showText(
                                    this@NoteListActivity,
                                    R.string.note_locked
                                )
                            }
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