package com.wqz.allinone.act.bookmark

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.wqz.allinone.entity.Link
import com.wqz.allinone.ui.AppBackground
import com.wqz.allinone.ui.XCard
import com.wqz.allinone.ui.XItem
import com.wqz.allinone.ui.theme.AllInOneTheme
import com.wqz.allinone.ui.theme.ThemeColor
import java.util.regex.Pattern

/**
 * 链接添加
 * Created by Wu Qizhen on 2024.11.3
 * Refactored by Wu Qizhen on 2024.11.30
 */
class LinkAddActivity : ComponentActivity() {
    private lateinit var viewModel: BookmarkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = BookmarkViewModel(application)

        val folderId = intent.getIntExtra("FOLDER_ID", 0)

        setContent {
            AllInOneTheme {
                AppBackground.BreathingBackground(title = R.string.add_link) {
                    LinkAddScreen(
                        folderId = folderId
                    )
                }
            }
        }
    }

    @Composable
    fun LinkAddScreen(
        folderId: Int = 0
    ) {
        val context = LocalContext.current
        var link by remember { mutableStateOf("") }
        var content by remember { mutableStateOf("") }

        XCard.SurfaceCard {
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
                        text = "书签名",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        XCard.SurfaceCard {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = link,
                onValueChange = { link = it },
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
                        text = "URL",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            XItem.Button(icon = R.drawable.ic_visit, text = "批量") {
                val intent = Intent(this@LinkAddActivity, LinkImportActivity::class.java)
                intent.putExtra("FOLDER_ID", folderId)
                startActivity(intent)
                finish()
            }

            Spacer(modifier = Modifier.width(10.dp))

            XItem.Button(icon = R.drawable.ic_add, text = "添加") {
                val title = content.trim().replace("\n", "")
                val url = link.trim().replace("\n", "")
                if (title.isNotEmpty() && url.isNotEmpty()) {
                    if (isValidUrl(url)) {
                        val newLink = Link(
                            id = null,
                            title = title,
                            url = url,
                            folder = folderId
                        )
                        viewModel.insertLink(newLink)
                        Toast.makeText(context, R.string.added, Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(context, R.string.invalid_url, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, R.string.input_link_empty, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }

    private fun isValidUrl(url: String): Boolean {
        val regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
        val pattern = Pattern.compile(regex)
        return pattern.matcher(url).matches()
    }
}