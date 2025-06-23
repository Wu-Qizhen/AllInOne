package com.wqz.allinone.act.bookmark

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.act.bookmark.viewmodel.BookmarkViewModel
import com.wqz.allinone.entity.Link
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.XBackground
import com.wqz.allinone.ui.XCard
import com.wqz.allinone.ui.XItem
import com.wqz.allinone.ui.property.BorderWidth

/**
 * 链接列表
 * Created by Wu Qizhen on 2024.11.3
 * Refactored by Wu Qizhen on 2024.11.30
 */
class LinkListActivity : ComponentActivity() {
    private lateinit var viewModel: BookmarkViewModel

    /*private val linkDetailsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data?.getBooleanExtra("delete", false) == true) {
                    finish()
                }
            }
        }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = BookmarkViewModel(application)

        val folderId = intent.getIntExtra("FOLDER_ID", -1)
        val folderName = intent.getStringExtra("FOLDER_NAME")

        setContent {
            XBackground.BreathingBackground(title = folderName!!) {
                LinkListScreen(
                    folderId = folderId,
                    folderName = folderName
                )
            }
        }
    }

    @Composable
    fun LinkListScreen(
        folderId: Int,
        folderName: String
    ) {
        val links by viewModel.links.observeAsState(listOf())

        if (folderId != 0) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                XItem.Button(
                    icon = R.drawable.ic_edit,
                    text = stringResource(id = R.string.edit)
                ) {
                    val intent =
                        Intent(this@LinkListActivity, FolderDetailsActivity::class.java)
                    intent.putExtra("FOLDER_ID", folderId)
                    intent.putExtra("FOLDER_NAME", folderName)
                    // linkDetailsLauncher.launch(intent)
                    startActivity(intent)
                    finish()
                }

                Spacer(modifier = Modifier.width(10.dp))

                XItem.Button(
                    icon = R.drawable.ic_add,
                    text = stringResource(id = R.string.add)
                ) {
                    val intent = Intent(this@LinkListActivity, LinkAddActivity::class.java)
                    intent.putExtra("FOLDER_ID", folderId)
                    startActivity(intent)
                }
            }
        } else {
            XItem.Button(
                icon = R.drawable.ic_add,
                text = stringResource(id = R.string.add)
            ) {
                val intent = Intent(this@LinkListActivity, LinkAddActivity::class.java)
                intent.putExtra("FOLDER_ID", folderId)
                startActivity(intent)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        val filteredLinks = links.filter { it.folder == folderId }

        if (filteredLinks.isEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.ic_no_data),
                contentDescription = "无数据",
                modifier = Modifier
                    .size(100.dp)
            )
        } else {
            XCard.SurfaceCard {
                filteredLinks.forEach {
                    key(it.id) {
                        LinkItem(
                            link = it
                        )
                        // 分如果不是最后一个绘制割线
                        if (it != filteredLinks.last()) {
                            Divider(
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
    fun LinkItem(
        link: Link
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val contentColor =
            if (isPressed.value) Color.Gray else Color.White

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
                            Intent(this@LinkListActivity, LinkDetailsActivity::class.java)
                        intent.putExtra("LINK_ID", link.id)
                        intent.putExtra("LINK_TITLE", link.title)
                        intent.putExtra("LINK_URL", link.url)
                        intent.putExtra("FOLDER_ID", link.folder)
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
                    painter = painterResource(id = R.drawable.ic_link),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = link.title,
                        fontSize = 16.sp,
                        color = contentColor,
                        maxLines = 1
                    )

                    Text(
                        text = link.url,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
            }
        }
    }
}