package com.wqz.allinone.act.bookmark

import android.content.Intent
import android.os.Bundle
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
import com.wqz.allinone.ui.XBackground
import com.wqz.allinone.ui.XCard
import com.wqz.allinone.ui.XItem
import com.wqz.allinone.ui.XToast
import com.wqz.allinone.ui.color.TextFieldColor
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
            XBackground.BreathingBackground(titleId = R.string.add_link) {
                LinkAddScreen(
                    folderId = folderId
                )
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
                colors = TextFieldColor.colors(),
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
                colors = TextFieldColor.colors(),
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
                        XToast.showText(context, R.string.added)
                        finish()
                    } else {
                        XToast.showText(context, R.string.invalid_url)
                    }
                } else {
                    XToast.showText(context, R.string.input_link_empty)
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