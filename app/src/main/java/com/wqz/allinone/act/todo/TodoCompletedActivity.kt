package com.wqz.allinone.act.todo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.wqz.allinone.R
import com.wqz.allinone.act.todo.viewmodel.TodoViewModel
import com.wqz.allinone.entity.Todo
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.XBackground
import com.wqz.allinone.ui.XCard
import com.wqz.allinone.ui.XToast
import com.wqz.allinone.ui.color.BackgroundColor
import com.wqz.allinone.ui.color.ContentColor
import com.wqz.allinone.ui.property.BorderWidth
import com.wqz.allinone.ui.theme.ThemeColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 待办完成列表
 * Created by Wu Qizhen on 2024.10.1
 * Refactored by Wu Qizhen on 2024.11.30
 */
class TodoCompletedActivity : ComponentActivity() {
    private lateinit var viewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = TodoViewModel(application)

        setContent {
            XBackground.BreathingBackground(titleId = R.string.completed) {
                TodoCompletedListScreen()
            }
        }
    }

    @Composable
    fun TodoCompletedListScreen() {
        val todos by viewModel.completedTodos.observeAsState(listOf())
        var deleteConfirm by remember { mutableStateOf(false) }
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BackgroundColor.PRESSED_YELLOW else BackgroundColor.DEFAULT_YELLOW

        Row(
            modifier = Modifier
                .clickVfx(
                    interactionSource = interactionSource,
                    enabled = true,
                    onClick = {
                        if (deleteConfirm) {
                            viewModel.viewModelScope.launch {
                                viewModel.clearCompleted()
                                XToast.showText(
                                    this@TodoCompletedActivity,
                                    R.string.cleared
                                )
                                finish()
                            }
                        } else {
                            deleteConfirm = true
                        }
                    },
                    onLongClick = {
                        deleteConfirm = false
                    }
                )
                .wrapContentSize()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                .padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_delete),
                tint = ContentColor.DEFAULT_BROWN,
                modifier = Modifier.size(20.dp),
                contentDescription = null
            )

            Spacer(modifier = Modifier.size(5.dp))

            AnimatedVisibility(
                visible = deleteConfirm,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                Text(
                    text = stringResource(id = R.string.confirm),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ContentColor.DEFAULT_BROWN
                )
            }

            Text(
                text = stringResource(id = R.string.clear),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = ContentColor.DEFAULT_BROWN
            )
        }

        /*XItem.Button(
            icon = R.drawable.ic_delete,
            text = if (deleteConfirm) "确认操作" else stringResource(id = R.string.clear)
        ) {
            if (deleteConfirm) {
                viewModel.viewModelScope.launch {
                    viewModel.clearCompleted()
                    Toast.makeText(this@TodoCompletedActivity, R.string.cleared, Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            } else {
                deleteConfirm = true
            }
        }*/

        Spacer(modifier = Modifier.height(10.dp))

        if (todos.isEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.ic_no_data),
                contentDescription = "无数据",
                modifier = Modifier
                    .size(100.dp)
            )
        } else {
            XCard.SurfaceCard {
                todos.forEach {
                    key(it.id) {
                        TodoItem(
                            todo = it
                        )
                        // 分如果不是最后一个绘制割线
                        if (it != todos.last()) {
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                thickness = BorderWidth.DEFAULT_WIDTH,
                                color = Color(54, 54, 54)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }

    @Composable
    fun TodoItem(
        todo: Todo
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val contentColor =
            if (isPressed.value) Color.Gray else Color.White
        var isChecked by remember { mutableStateOf(true) }

        AnimatedVisibility(
            visible = isChecked,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + expandVertically(
                animationSpec = tween(
                    durationMillis = 1000
                )
            ),
            exit = fadeOut(animationSpec = tween(durationMillis = 1000)) + shrinkVertically(
                animationSpec = tween(
                    durationMillis = 1000
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(15.dp)
                    .clickVfx(
                        interactionSource = interactionSource,
                        enabled = true,
                        onClick = {
                            val intent =
                                Intent(this@TodoCompletedActivity, TodoDetailsActivity::class.java)
                            intent.putExtra("TODO_ID", todo.id)
                            intent.putExtra("TODO_CONTENT", todo.title)
                            intent.putExtra("TODO_COMPLETED", todo.completed)
                            startActivity(intent)
                        }
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        modifier = Modifier.size(20.dp),
                        colors = CheckboxDefaults.colors(
                            checkedColor = ThemeColor
                        ),
                        checked = isChecked,
                        onCheckedChange = {
                            viewModel.viewModelScope.launch {
                                isChecked = !isChecked
                                todo.completed = !todo.completed
                                delay(1000)
                                viewModel.updateTodo(todo)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.width(15.dp))

                    Text(
                        text = todo.title.ifEmpty { "无附加内容" },
                        fontSize = 16.sp,
                        color = contentColor,
                        textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
                        maxLines = 1
                    )
                }
            }
        }
    }
}