package com.wqz.allinone.act.setting.ui

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wqz.allinone.R
import com.wqz.allinone.act.about.AboutAppActivity
import com.wqz.allinone.ui.CapsuleButton
import com.wqz.allinone.ui.TitleBar
import com.wqz.allinone.ui.theme.AllInOneTheme

/**
 * 设置
 * Created by Wu Qizhen on 2024.6.29
 */
@Composable
fun SettingScreen() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleBar.TextTitleBar(title = R.string.setting)
        CapsuleButton.IconButton(
            icon = R.drawable.ic_about,
            text = stringResource(R.string.btn_about)
        ) {
            context.startActivity(Intent(context, AboutAppActivity::class.java))
        }
        // Spacer(modifier = Modifier.height(40.dp))
    }
}

@Preview
@Composable
fun SettingScreenPreview() {
    AllInOneTheme {
        SettingScreen()
    }
}