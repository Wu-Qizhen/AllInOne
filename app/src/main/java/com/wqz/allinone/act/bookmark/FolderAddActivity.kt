package com.wqz.allinone.act.bookmark

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
import com.wqz.allinone.act.bookmark.viewmodel.BookmarkViewModel
import com.wqz.allinone.entity.Folder
import com.wqz.allinone.ui.AppBackground
import com.wqz.allinone.ui.XCard
import com.wqz.allinone.ui.XItem
import com.wqz.allinone.ui.color.TextFieldColor
import com.wqz.allinone.ui.theme.AllInOneTheme

/**
 * 文件夹添加
 * Created by Wu Qizhen on 2024.11.3
 * Refactored by Wu Qizhen on 2024.11.30
 */
class FolderAddActivity : ComponentActivity() {
    private lateinit var viewModel: BookmarkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = BookmarkViewModel(application)

        setContent {
            AllInOneTheme {
                AppBackground.BreathingBackground(title = R.string.add_folder) {
                    FolderAddScreen()
                }
            }
        }
    }

    @Composable
    fun FolderAddScreen() {
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
                        text = "文件夹名称",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        XItem.Button(icon = R.drawable.ic_add, text = "添加") {
            val name = content.trim().replace("\n", "")
            if (name.isNotEmpty()) {
                val folder = Folder(
                    id = null,
                    name = name
                )
                viewModel.insertFolder(folder)
                Toast.makeText(context, R.string.added, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(context, R.string.input_folder_name_empty, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}