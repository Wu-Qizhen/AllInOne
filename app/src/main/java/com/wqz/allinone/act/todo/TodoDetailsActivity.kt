package com.wqz.allinone.act.todo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.wqz.allinone.act.todo.viewmodel.TodoViewModel
import com.wqz.allinone.entity.Todo
import com.wqz.allinone.ui.AppBackground
import com.wqz.allinone.ui.XCard
import com.wqz.allinone.ui.XItem
import com.wqz.allinone.ui.property.BorderWidth
import com.wqz.allinone.ui.theme.AllInOneTheme
import com.wqz.allinone.ui.theme.ThemeColor
import kotlinx.coroutines.launch

/**
 * 待办详情
 * Created by Wu Qizhen on 2024.10.2
 * Refactored by Wu Qizhen on 2024.11.30
 */
class TodoDetailsActivity : ComponentActivity() {
    private lateinit var viewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val todoId = intent.getIntExtra("TODO_ID", -1)
        val todoContent = intent.getStringExtra("TODO_CONTENT")
        val todoCompleted = intent.getBooleanExtra("TODO_COMPLETED", false)

        viewModel = TodoViewModel(application)

        setContent {
            AllInOneTheme {
                AppBackground.BreathingBackground(title = R.string.todo_details) {
                    TodoDetailsScreen(
                        todo = Todo(
                            id = todoId,
                            title = todoContent.toString(),
                            completed = todoCompleted
                        )
                    )
                }
            }
        }
    }

    @Composable
    fun TodoDetailsScreen(
        todo: Todo
    ) {
        var content by remember { mutableStateOf(todo.title) }
        var deleteConfirm by remember { mutableIntStateOf(0) }

        XCard.LivelyCard {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = content,
                onValueChange = { content = it },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent, // 背景颜色
                    focusedContainerColor = Color.Transparent, // 背景颜色
                    unfocusedIndicatorColor = Color.Transparent, // 下划线颜色
                    focusedIndicatorColor = Color.Transparent, // 下划线颜色
                    cursorColor = ThemeColor, // 光标颜色
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.Gray
                ),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.misans_regular))
                ),
                label = {
                    Text(
                        text = "待办内容",
                        fontWeight = FontWeight.Bold
                    )
                }
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = BorderWidth.DEFAULT_WIDTH,
                color = Color(54, 54, 54)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp)
            ) {
                Text(
                    text = "待办状态：",
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                if (todo.completed) {
                    Text(
                        text = "已完成",
                        color = Color(87, 150, 92),
                        maxLines = 1
                    )
                } else {
                    Text(
                        text = "未完成",
                        color = Color(201, 79, 79),
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp),
                text = "* 这是积累第 ${todo.id} 条待办",
                color = ThemeColor,
                fontSize = 12.sp,
                maxLines = 1,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            XItem.Button(
                icon = R.drawable.ic_delete,
                text = stringResource(id = R.string.delete)
            ) {
                deleteConfirm++
                if (deleteConfirm > 2) {
                    viewModel.deleteTodo(todo.id)
                    Toast.makeText(
                        this@TodoDetailsActivity,
                        R.string.deleted,
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@TodoDetailsActivity,
                        "再按 ${3 - deleteConfirm} 次即可删除",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            XItem.Button(
                icon = R.drawable.ic_todo,
                text = stringResource(id = R.string.save)
            ) {
                if (content.trim().isNotEmpty()) {
                    todo.title = content.trim()
                    viewModel.viewModelScope.launch {
                        viewModel.updateTodo(todo)
                    }
                    Toast.makeText(
                        this@TodoDetailsActivity,
                        R.string.saved,
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@TodoDetailsActivity,
                        R.string.input_todo_empty,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}