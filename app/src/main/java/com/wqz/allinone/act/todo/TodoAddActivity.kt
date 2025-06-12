package com.wqz.allinone.act.todo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.act.todo.viewmodel.TodoViewModel
import com.wqz.allinone.entity.Todo
import com.wqz.allinone.ui.AppBackground
import com.wqz.allinone.ui.XCard
import com.wqz.allinone.ui.XItem
import com.wqz.allinone.ui.color.TextFieldColor
import com.wqz.allinone.ui.theme.AllInOneTheme

/**
 * 待办添加
 * Created by Wu Qizhen on 2024.10.1
 * Refactored by Wu Qizhen on 2024.11.30
 */
class TodoAddActivity : ComponentActivity() {
    private lateinit var viewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = TodoViewModel(application)

        setContent {
            AllInOneTheme {
                AppBackground.BreathingBackground(title = R.string.add_todo) {
                    TodoAddScreen()
                }
            }
        }
    }

    @Composable
    fun TodoAddScreen() {
        val context = LocalContext.current
        var content by remember { mutableStateOf("") }

        XCard.SurfaceCard {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = content,
                onValueChange = { content = it },
                colors = TextFieldColor.colors(),
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
        }

        Spacer(modifier = Modifier.height(10.dp))

        XItem.Button(icon = R.drawable.ic_add, text = "添加") {
            if (content.trim().isNotEmpty()) {
                val todo = Todo(
                    id = null,
                    title = content.trim(),
                    completed = false
                )
                viewModel.insertTodo(todo)
                Toast.makeText(context, R.string.added, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(context, R.string.input_todo_empty, Toast.LENGTH_SHORT).show()
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}