package com.wqz.allinone.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.ui.theme.ThemeColor

/**
 * 标题栏
 * Created by Wu Qizhen on 2024.6.23
 */
object TitleBar {
    @Composable
    fun TextTitleBar(title: Int) {
        Text(
            text = stringResource(id = title),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(
                Font(R.font.fzfengrusongti_regular, FontWeight.Normal)
            ),
            maxLines = 1,
            color = ThemeColor,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        )
    }

    @Composable
    fun LogoTitleBar() {
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
                Image(
                    painter = painterResource(id = R.drawable.logo_pen_golden),
                    contentDescription = "Golden Pen Logo",
                    modifier = Modifier.size(25.dp),
                    contentScale = ContentScale.Fit
                )

                Image(
                    painter = painterResource(id = R.drawable.logo_wxgy_white),
                    contentDescription = "WXGY White Logo",
                    modifier = Modifier
                        .width(100.dp)
                        .height(23.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Preview
@Composable
fun TextTitleBarPreview() {
    TitleBar.TextTitleBar(R.string.app_name)
}

@Preview
@Composable
fun LogoTitleBarPreview() {
    TitleBar.LogoTitleBar()
}