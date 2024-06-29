@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.wqz.allinone.act.main.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.wqz.allinone.act.bookmark.BookmarkFoldersActivity
import com.wqz.allinone.act.setting.SettingActivity
import com.wqz.allinone.act.todo.TodoListActivity
import com.wqz.allinone.ui.CapsuleButton
import kotlinx.coroutines.delay

/**
 * 主界面
 * Created by Wu Qizhen on 2024.6.29
 */
@Composable
fun LaunchScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
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
                        modifier = Modifier.size(80.dp)
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
        )
    }
}

@Composable
fun MainScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TitleBar.LogoTitleBar()
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
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
                            .size(25.dp),
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
                            .height(23.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
        ClassificationBar(icon = R.drawable.ic_person, text = R.string.text_personal_manage)
        Spacer(modifier = Modifier.height(10.dp))
        CapsuleButton.IconButton(
            icon = R.drawable.ic_diary,
            text = stringResource(id = R.string.btn_diary)
        ) {
            Toast.makeText(context, "施工中", Toast.LENGTH_SHORT).show()
        }
        Spacer(modifier = Modifier.height(5.dp))
        CapsuleButton.IconButton(
            icon = R.drawable.ic_note,
            text = stringResource(id = R.string.btn_note)
        ) {
            Toast.makeText(context, "施工中", Toast.LENGTH_SHORT).show()
        }
        Spacer(modifier = Modifier.height(5.dp))
        CapsuleButton.IconButton(
            icon = R.drawable.ic_todo,
            text = stringResource(id = R.string.btn_todo)
        ) {
            context.startActivity(Intent(context, TodoListActivity::class.java))
        }
        Spacer(modifier = Modifier.height(5.dp))
        CapsuleButton.IconButton(
            icon = R.drawable.ic_clock_in,
            text = stringResource(id = R.string.btn_clock_in)
        ) {
            Toast.makeText(context, "施工中", Toast.LENGTH_SHORT).show()
        }
        Spacer(modifier = Modifier.height(5.dp))
        CapsuleButton.IconButton(
            icon = R.drawable.ic_schedule,
            text = stringResource(id = R.string.btn_schedule)
        ) {
            Toast.makeText(context, "施工中", Toast.LENGTH_SHORT).show()
        }
        Spacer(modifier = Modifier.height(10.dp))
        ClassificationBar(icon = R.drawable.ic_collect, text = R.string.text_information_collection)
        Spacer(modifier = Modifier.height(10.dp))
        CapsuleButton.IconButton(
            icon = R.drawable.ic_bookmark,
            text = stringResource(id = R.string.btn_bookmark)
        ) {
            context.startActivity(Intent(context, BookmarkFoldersActivity::class.java))
        }
        Spacer(modifier = Modifier.height(10.dp))
        ClassificationBar(icon = R.drawable.ic_study, text = R.string.text_study)
        Spacer(modifier = Modifier.height(10.dp))
        CapsuleButton.IconButton(
            icon = R.drawable.ic_school_timetable,
            text = stringResource(id = R.string.btn_school_timetable)
        ) {
            Toast.makeText(context, "施工中", Toast.LENGTH_SHORT).show()
        }
        Spacer(modifier = Modifier.height(10.dp))
        ClassificationBar(icon = R.drawable.ic_option, text = R.string.text_option)
        Spacer(modifier = Modifier.height(10.dp))
        CapsuleButton.IconButton(
            icon = R.drawable.ic_setting,
            text = stringResource(id = R.string.setting)
        ) {
            context.startActivity(Intent(context, SettingActivity::class.java))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.copyright),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ClassificationBar(icon: Int, text: Int) {
    Row(
        modifier = Modifier.wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = stringResource(id = text),
            modifier = Modifier.size(20.dp)
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
fun LaunchAnimation() {
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
                    sharedTransitionScope = this@SharedTransitionLayout
                )
            }
        }
    }
}

/*
@Preview
@Composable
fun LaunchScreenPreview() {
    AllInOneTheme {
        CirclesBackground.RegularBackground {
            LaunchScreen()
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    AllInOneTheme {
        CirclesBackground.RegularBackground {
            MainScreen()
        }
    }
}*/