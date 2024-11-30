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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
 * 批量导入链接
 * Created by Wu Qizhen on 2024.11.29
 */
class LinkImportActivity : ComponentActivity() {
    private lateinit var viewModel: BookmarkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = BookmarkViewModel(application)

        val folderId = intent.getIntExtra("FOLDER_ID", 0)

        setContent {
            AllInOneTheme {
                AppBackground.BreathingBackground(
                    R.string.batch_import
                ) {
                    LinkImportScreen(
                        folderId = folderId
                    )
                }
            }
        }
    }

    @Composable
    fun LinkImportScreen(
        folderId: Int = 0
    ) {
        var importLinks by remember { mutableStateOf("") }

        XCard.SurfaceCard {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = importLinks,
                onValueChange = { importLinks = it },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = ThemeColor
                ),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.misans_regular))
                ),
                placeholder = {
                    Text(
                        text = "名称@https://xxx.com",
                        color = Color.DarkGray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        XItem.Button(icon = R.drawable.ic_add, text = "导入") {
            val links = mutableListOf<Link>()
            val lines = importLinks.trim().split("\n")

            for (line in lines) {
                val parts = line.split("@")
                if (parts.size == 2) {
                    val name = parts[0].trim()
                    val url = parts[1].trim()
                    if (isValidUrl(url)) {
                        links.add(
                            Link(
                                id = null,
                                title = name,
                                url = url,
                                folder = folderId
                            )
                        )
                    }
                }
            }

            if (links.isNotEmpty()) {
                viewModel.insertLinks(links)
                Toast.makeText(this, R.string.imported, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, R.string.invalid_content, Toast.LENGTH_SHORT).show()
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }

    private fun isValidUrl(url: String): Boolean {
        val regex = "^(http|https|ftp|file)://" + // 协议
                "([a-zA-Z0-9.-]+\\.)*" + // 域名主体
                "[a-zA-Z0-9.-]+" + // 域名
                "(:[0-9]{1,5})?" + // 端口号
                "(/[!$&-~]*" + // 路径
                "(\\?[!$&-~]+)?" + // 查询字符串
                "(#[!$&-~]+)?)?$" // 锚点
        val pattern = Pattern.compile(regex)
        return pattern.matcher(url).matches()
    }
}