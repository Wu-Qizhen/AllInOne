package com.wqz.allinone.act.bookmark

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.act.bookmark.viewmodel.BookmarkViewModel
import com.wqz.allinone.entity.Folder
import com.wqz.allinone.ui.AppBackground
import com.wqz.allinone.ui.ItemX
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.TitleBar
import com.wqz.allinone.ui.color.BackgroundColor
import com.wqz.allinone.ui.color.BorderColor
import com.wqz.allinone.ui.theme.AllInOneTheme

/**
 * 文件夹列表
 * Created by Wu Qizhen on 2024.11.3
 */
class FolderListActivity : ComponentActivity() {
    private lateinit var viewModel: BookmarkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = BookmarkViewModel(application)

        setContent {
            AllInOneTheme {
                AppBackground.BreathingBackground {
                    FolderListScreen()
                }
            }
        }
    }

    @Composable
    fun FolderListScreen() {
        val scrollState = rememberScrollState()
        val folders by viewModel.folders.observeAsState(listOf())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleBar.TextTitleBar(title = R.string.bookmark)

            ItemX.Button(
                icon = R.drawable.ic_add_folder,
                text = stringResource(id = R.string.add_folder)
            ) {
                startActivity(Intent(this@FolderListActivity, FolderAddActivity::class.java))
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (folders.isEmpty()) {
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
                    folders.forEach {
                        key(it.id) {
                            FolderItem(
                                folder = it
                            )
                            // 分如果不是最后一个绘制割线
                            if (it != folders.last()) {
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
    fun FolderItem(
        folder: Folder
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val contentColor =
            if (isPressed.value) Color.Gray else Color.White

        var linkCount by remember {
            mutableIntStateOf(0)
        }

        LaunchedEffect(folder.id) {
            linkCount = viewModel.getLinkCount(folder.id!!)
        }

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(10.dp)
                .clickVfx(
                    interactionSource = interactionSource,
                    enabled = true,
                    onClick = {
                        val intent =
                            Intent(this@FolderListActivity, LinkListActivity::class.java)
                        intent.putExtra("FOLDER_ID", folder.id)
                        intent.putExtra("FOLDER_NAME", folder.name)
                        startActivity(intent)
                    }
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_folder),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = folder.name,
                        fontSize = 16.sp,
                        color = contentColor,
                        maxLines = 1
                    )
                    Text(
                        text = "$linkCount 个书签",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
            }
        }
    }
}