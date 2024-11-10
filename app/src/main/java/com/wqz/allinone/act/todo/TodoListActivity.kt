package com.wqz.allinone.act.todo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.wqz.allinone.R
import com.wqz.allinone.act.todo.viewmodel.TodoViewModel
import com.wqz.allinone.entity.Todo
import com.wqz.allinone.ui.AppBackground
import com.wqz.allinone.ui.ItemX
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.TitleBar
import com.wqz.allinone.ui.color.BackgroundColor
import com.wqz.allinone.ui.color.BorderColor
import com.wqz.allinone.ui.theme.AllInOneTheme
import com.wqz.allinone.ui.theme.ThemeColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 待办列表
 * Created by Wu Qizhen on 2024.10.1
 */
class TodoListActivity : ComponentActivity() {
    private lateinit var viewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = TodoViewModel(application)

        setContent {
            AllInOneTheme {
                AppBackground.BreathingBackground {
                    TodoListScreen()
                }
            }
        }
    }

    @Composable
    fun TodoListScreen() {
        val scrollState = rememberScrollState()
        val todos by viewModel.unCompletedTodos.observeAsState(listOf())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleBar.TextTitleBar(title = R.string.todo)

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ItemX.Button(
                    icon = R.drawable.ic_todo,
                    text = stringResource(id = R.string.completed)
                ) {
                    startActivity(Intent(this@TodoListActivity, TodoCompletedActivity::class.java))
                }
                Spacer(modifier = Modifier.width(10.dp))
                ItemX.Button(
                    icon = R.drawable.ic_add,
                    text = stringResource(id = R.string.add)
                ) {
                    startActivity(Intent(this@TodoListActivity, TodoAddActivity::class.java))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            if (todos.isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_no_data),
                    contentDescription = "无数据",
                    modifier = Modifier
                        .size(100.dp)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            BackgroundColor.DEFAULT_GRAY, RoundedCornerShape(10.dp)
                        )
                        .border(
                            width = 0.4f.dp,
                            shape = RoundedCornerShape(10.dp),
                            brush = Brush.linearGradient(
                                BorderColor.DEFAULT_GRAY,
                                start = Offset.Zero,
                                end = Offset.Infinite
                            )
                        )
                ) {
                    todos.forEach {
                        key(it.id) {
                            TodoItem(
                                todo = it
                            )
                            // 分如果不是最后一个绘制割线
                            if (it != todos.last()) {
                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    thickness = 0.5f.dp,
                                    color = Color(54, 54, 54)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(50.dp))
            }
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
        var isChecked by remember { mutableStateOf(false) }

        AnimatedVisibility(
            visible = !isChecked,
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
            /*enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + expandIn(
                animationSpec = tween(
                    durationMillis = 1000
                ), expandFrom = Alignment.TopStart
            ),
            exit = fadeOut(animationSpec = tween(durationMillis = 1000)) + shrinkOut(
                animationSpec = tween(
                    durationMillis = 1000
                ), shrinkTowards = Alignment.TopStart
            )*/
            /*enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(durationMillis = 1000)
            ) + fadeIn(animationSpec = tween(durationMillis = 1000)),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(durationMillis = 1000)
            ) + fadeOut(animationSpec = tween(durationMillis = 1000))*/
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
                                Intent(this@TodoListActivity, TodoDetailsActivity::class.java)
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