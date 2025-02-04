@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.wqz.allinone.act.main.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.act.anniversary.AnniversaryPreviewActivity
import com.wqz.allinone.act.bookmark.FolderListActivity
import com.wqz.allinone.act.knowledge.SubjectChooseActivity
import com.wqz.allinone.act.note.NoteListActivity
import com.wqz.allinone.act.password.PasswordCheckActivity
import com.wqz.allinone.act.setting.SettingActivity
import com.wqz.allinone.act.todo.TodoListActivity
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.XItem
import kotlinx.coroutines.delay

/**
 * 主界面
 * Created by Wu Qizhen on 2024.6.29
 * Refactored by Wu Qizhen on 2024.11.30
 */
@Composable
fun LaunchScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    var isVisible by remember {
        mutableStateOf(true)
    }
    val animatedBlur by animateDpAsState(
        targetValue = if (isVisible) 5.dp else 0.dp,
        label = "blur"
    )

    LaunchedEffect(key1 = Unit) {
        delay(100)
        isVisible = false
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            with(sharedTransitionScope) {
                Box(modifier = Modifier.wrapContentSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_background_frosted),
                        contentDescription = "Logo Background",
                        modifier = Modifier
                            .size(80.dp)
                            .blur(
                                radius = animatedBlur,
                                edgeTreatment = BlurredEdgeTreatment.Unbounded
                            )
                    )

                    Image(
                        painter = painterResource(id = R.drawable.logo_pen_white),
                        contentDescription = "White Pen Logo",
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState(key = "pen"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .size(40.dp)
                            .offset(x = 25.dp, y = (15).dp)
                            .blur(
                                radius = animatedBlur,
                                edgeTreatment = BlurredEdgeTreatment.Unbounded
                            )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Image(
                    painter = painterResource(id = R.drawable.logo_wxgy_golden),
                    contentDescription = "WXGY Text",
                    modifier = Modifier
                        .sharedElement(
                            rememberSharedContentState(key = "text"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .size(width = 100.dp, height = 30.dp)
                        .blur(
                            radius = animatedBlur,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded
                        )
                )
            }
        }

        Text(
            text = stringResource(id = R.string.slogan),
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.fzfengrusongti_regular)),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
                .blur(
                    radius = animatedBlur,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                )
        )
    }
}

@Composable
fun MainScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    isGirdLayout: Boolean
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val showBuild = remember { mutableStateOf(true) }
    val backgroundColor = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,  // 顶部颜色：全透明
            Color.Black.copy(alpha = 0.65f)  // 底部颜色：黑色
        )
    )
    var isVisible by remember {
        mutableStateOf(false)
    }
    val animatedBlur by animateDpAsState(
        targetValue = if (isVisible) 5.dp else 0.dp,
        label = "blur"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 25.dp, bottom = 10.dp)
                    .wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                with(sharedTransitionScope) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_pen_golden),
                        contentDescription = "Golden Pen Logo",
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState(key = "pen"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .size(25.dp)
                            .blur(
                                radius = animatedBlur,
                                edgeTreatment = BlurredEdgeTreatment.Unbounded
                            ),
                        contentScale = ContentScale.Fit
                    )

                    Image(
                        painter = painterResource(id = R.drawable.logo_wxgy_white),
                        contentDescription = "WXGY Text",
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState(key = "text"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .width(100.dp)
                            .height(23.dp)
                            .clickVfx { isVisible = !isVisible }
                            .blur(
                                radius = animatedBlur,
                                edgeTreatment = BlurredEdgeTreatment.Unbounded
                            ),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            if (!isGirdLayout) {
                ClassificationBar(icon = R.drawable.ic_person, text = R.string.personal_manage)

                Spacer(modifier = Modifier.height(10.dp))

                XItem.Capsule(
                    icon = R.drawable.ic_todo,
                    text = stringResource(id = R.string.todo)
                ) {
                    context.startActivity(Intent(context, TodoListActivity::class.java))
                }

                Spacer(modifier = Modifier.height(5.dp))

                XItem.Capsule(
                    icon = R.drawable.ic_note,
                    text = stringResource(id = R.string.note)
                ) {
                    context.startActivity(Intent(context, NoteListActivity::class.java))
                }

                Spacer(modifier = Modifier.height(5.dp))

                XItem.Capsule(
                    icon = R.drawable.ic_anniversary,
                    text = stringResource(id = R.string.anniversary)
                ) {
                    context.startActivity(Intent(context, AnniversaryPreviewActivity::class.java))
                }

                Spacer(modifier = Modifier.height(5.dp))

                XItem.Capsule(
                    icon = R.drawable.ic_diary,
                    text = stringResource(id = R.string.diary)
                ) {
                    val intent = Intent(context, PasswordCheckActivity::class.java)
                    intent.putExtra("REQUEST_CODE", 1)
                    context.startActivity(intent)
                }

                Spacer(modifier = Modifier.height(5.dp))

                XItem.Capsule(
                    icon = R.drawable.ic_clock_in,
                    text = stringResource(id = R.string.clock_in)
                ) {
                    Toast.makeText(context, "施工中", Toast.LENGTH_SHORT).show()
                }

                Spacer(modifier = Modifier.height(5.dp))

                XItem.Capsule(
                    icon = R.drawable.ic_schedule,
                    text = stringResource(id = R.string.schedule)
                ) {
                    openCalendarApp(context)
                }

                Spacer(modifier = Modifier.height(10.dp))

                ClassificationBar(
                    icon = R.drawable.ic_collect,
                    text = R.string.information_collection
                )

                Spacer(modifier = Modifier.height(10.dp))

                XItem.Capsule(
                    icon = R.drawable.ic_bookmark,
                    text = stringResource(id = R.string.bookmark)
                ) {
                    context.startActivity(Intent(context, FolderListActivity::class.java))
                }

                Spacer(modifier = Modifier.height(10.dp))

                ClassificationBar(icon = R.drawable.ic_jump, text = R.string.study_manage)

                Spacer(modifier = Modifier.height(10.dp))

                XItem.Capsule(
                    icon = R.drawable.ic_school_timetable,
                    text = stringResource(id = R.string.school_timetable)
                ) {
                    Toast.makeText(context, "施工中", Toast.LENGTH_SHORT).show()
                }

                Spacer(modifier = Modifier.height(5.dp))

                XItem.Capsule(
                    icon = R.drawable.ic_smart_box,
                    text = stringResource(id = R.string.smart_box)
                ) {
                    context.startActivity(Intent(context, SubjectChooseActivity::class.java))
                }

                Spacer(modifier = Modifier.height(10.dp))

                ClassificationBar(icon = R.drawable.ic_option, text = R.string.option)

                Spacer(modifier = Modifier.height(10.dp))

                XItem.Capsule(
                    icon = R.drawable.ic_setting,
                    text = stringResource(id = R.string.setting)
                ) {
                    context.startActivity(Intent(context, SettingActivity::class.java))

                }
            } else {
                ClassificationBar(icon = R.drawable.ic_person, text = R.string.personal_manage)

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    XItem.Card(
                        icon = R.drawable.ic_todo,
                        text = stringResource(id = R.string.todo)
                    ) {
                        context.startActivity(Intent(context, TodoListActivity::class.java))
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    XItem.Card(
                        icon = R.drawable.ic_note,
                        text = stringResource(id = R.string.note)
                    ) {
                        context.startActivity(Intent(context, NoteListActivity::class.java))
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    XItem.Card(
                        icon = R.drawable.ic_anniversary,
                        text = stringResource(id = R.string.anniversary)
                    ) {
                        context.startActivity(
                            Intent(
                                context,
                                AnniversaryPreviewActivity::class.java
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    XItem.Card(
                        icon = R.drawable.ic_diary,
                        text = stringResource(id = R.string.diary)
                    ) {
                        val intent = Intent(context, PasswordCheckActivity::class.java)
                        intent.putExtra("REQUEST_CODE", 1)
                        context.startActivity(intent)
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    XItem.Card(
                        icon = R.drawable.ic_clock_in,
                        text = stringResource(id = R.string.clock_in)
                    ) {
                        Toast.makeText(context, "施工中", Toast.LENGTH_SHORT).show()
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    XItem.Card(
                        icon = R.drawable.ic_schedule,
                        text = stringResource(id = R.string.schedule)
                    ) {
                        openCalendarApp(context)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                ClassificationBar(
                    icon = R.drawable.ic_collect,
                    text = R.string.information_collection
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    XItem.Card(
                        icon = R.drawable.ic_bookmark,
                        text = stringResource(id = R.string.bookmark)
                    ) {
                        context.startActivity(Intent(context, FolderListActivity::class.java))
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    Surface(
                        modifier = Modifier.size(85.dp),
                        color = Color.Transparent
                    ) {}
                }

                Spacer(modifier = Modifier.height(10.dp))

                ClassificationBar(icon = R.drawable.ic_jump, text = R.string.study_manage)

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    XItem.Card(
                        icon = R.drawable.ic_school_timetable,
                        text = stringResource(id = R.string.school_timetable)
                    ) {
                        Toast.makeText(context, "施工中", Toast.LENGTH_SHORT).show()
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    XItem.Card(
                        icon = R.drawable.ic_smart_box,
                        text = stringResource(id = R.string.smart_box)
                    ) {
                        context.startActivity(Intent(context, SubjectChooseActivity::class.java))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                ClassificationBar(icon = R.drawable.ic_option, text = R.string.option)

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    XItem.Card(
                        icon = R.drawable.ic_setting,
                        text = stringResource(id = R.string.setting)
                    ) {
                        context.startActivity(Intent(context, SettingActivity::class.java))
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    Surface(
                        modifier = Modifier.size(85.dp),
                        color = Color.Transparent
                    ) {}
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            AnimatedVisibility(
                visible = showBuild.value,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .clickVfx {
                            showBuild.value = false
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.logo_aethex_matrix),
                        contentDescription = null,
                        modifier = Modifier
                            .size(25.dp)
                            .padding(end = 5.dp),
                        tint = Color.White
                    )

                    Text(
                        text = stringResource(id = R.string.powered_by),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            AnimatedVisibility(
                visible = !showBuild.value,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .clickVfx {
                            showBuild.value = true
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = stringResource(id = R.string.copyright),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun ClassificationBar(icon: Int, text: Int) {
    Row(
        modifier = Modifier
            .clickVfx()
            .wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = stringResource(id = text),
            modifier = Modifier.size(20.dp),
            tint = Color.White
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = stringResource(id = text),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.fzfengrusongti_regular)),
            fontWeight = FontWeight.Bold
        )
    }
}

// @Preview
@Composable
fun LaunchAnimation(
    isGirdLayout: Boolean
) {
    var isTransitioned by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        delay(1500)
        isTransitioned = true
    }

    SharedTransitionLayout {
        AnimatedContent(
            isTransitioned,
            label = "basic_transition"
        ) { targetState ->
            if (!targetState) {
                LaunchScreen(
                    animatedVisibilityScope = this@AnimatedContent,
                    sharedTransitionScope = this@SharedTransitionLayout
                )
            } else {
                MainScreen(
                    animatedVisibilityScope = this@AnimatedContent,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    isGirdLayout = isGirdLayout
                )
            }
        }
    }
}

private fun openCalendarApp(
    context: Context
) {
    val calendarIntent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("content://com.samsung.android.calendar")
        // 如果从非 Activity 上下文中启动的话需要设置这个标志
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    try {
        context.startActivity(calendarIntent)
    } catch (e: Exception) {
        // 如果没有找到处理该意图的应用，则捕获异常并处理
        e.printStackTrace()
        // 在这里可以提示用户安装日历应用或选择其他操作
        Toast.makeText(context, "无法打开日历应用", Toast.LENGTH_SHORT).show()
    }
}