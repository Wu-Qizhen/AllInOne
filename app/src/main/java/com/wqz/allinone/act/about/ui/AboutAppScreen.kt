package com.wqz.allinone.act.about.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.wqz.allinone.act.about.AboutDeveloperActivity
import com.wqz.allinone.act.about.AboutStudioActivity
import com.wqz.allinone.ui.CapsuleButton
import com.wqz.allinone.ui.theme.AllInOneTheme

/**
 * 关于应用
 * Created by Wu Qizhen on 2024.6.22
 */
@Composable
fun AboutAppScreen() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val developerNames = listOf(
        R.string.wqz,
        R.string.chatglm,
        R.string.tylm,
        R.string.codegeex,
        R.string.sd
    )
    val developerDesc = listOf(
        R.string.wqz_desc,
        R.string.chatglm_desc,
        R.string.tylm_desc,
        R.string.codegeex_desc,
        R.string.sd_desc
    )
    val developerImages = listOf(
        R.drawable.logo_wqz,
        R.drawable.logo_chatglm,
        R.drawable.logo_tylm,
        R.drawable.logo_codegeex,
        R.drawable.logo_sd
    )
    val developerDetails = listOf(
        R.string.text_about_wqz,
        R.string.text_about_chatglm,
        R.string.text_about_tylm,
        R.string.text_about_codegeex,
        R.string.text_about_sd
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        // horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = stringResource(id = R.string.title_about_us),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(10.dp))
        Image(
            painter = painterResource(id = R.drawable.logo_wxgy_for_wear_os),
            contentDescription = stringResource(id = R.string.app_name),
            modifier = Modifier.height(50.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.introduction),
            color = Color.White,
            fontSize = 14.sp,
            fontFamily = FontFamily(
                Font(R.font.fzfengrusongti_regular, FontWeight.Normal)
            ),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(5.dp))
        IconButtonWithTopSpacer(
            icon = R.drawable.ic_version,
            text = R.string.version,
            subText = R.string.version_desc
        ) {
            Toast.makeText(context, "施工中", Toast.LENGTH_SHORT).show()
        }
        IconButtonWithTopSpacer(
            icon = R.drawable.ic_github,
            text = R.string.open_source,
            subText = R.string.open_source_desc
        ) {
            Toast.makeText(context, "施工中", Toast.LENGTH_SHORT).show()
            /* AnimatedVisibility(
                 visible = toastContent.isNotEmpty(),
                 enter = fadeIn() + slideInVertically { it / 2 },
                 exit = fadeOut() + slideOutVertically { it / 2 },
                 modifier = Modifier
                     .align(Alignment.BottomCenter)
             ) {
                 ToastUtils.ToastContent(content = toastContent)
             }*/
        }
        ClassificationBar(icon = R.drawable.ic_team, text = R.string.participating_team)
        Spacer(modifier = Modifier.height(5.dp))
        CapsuleButton.LogoButton(
            icon = R.drawable.logo_code_intellix,
            text = stringResource(id = R.string.studio),
            subText = stringResource(id = R.string.studio_desc)
        ) {
            context.startActivity(Intent(context, AboutStudioActivity::class.java))
        }
        ClassificationBar(icon = R.drawable.ic_member, text = R.string.participating_individuals)
        for ((index) in developerNames.withIndex()) {
            Spacer(modifier = Modifier.height(5.dp))
            CapsuleButton.LogoButton(
                icon = developerImages[index],
                text = stringResource(developerNames[index]),
                subText = stringResource(developerDesc[index])
            ) {
                val intent = Intent(context, AboutDeveloperActivity::class.java)
                intent.putExtra("logo", developerImages[index])
                intent.putExtra("name", developerNames[index])
                intent.putExtra("description", developerDesc[index])
                intent.putExtra("details", developerDetails[index])
                context.startActivity(intent)
            }
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
        /*VerticalScrollbar(
            adapter = rememberScrollbarAdapter(scrollState),
            modifier = Modifier // .align(Alignment.Horizontal)
                .fillMaxHeight()
        )*/
    }
}

@Composable
fun IconButtonWithTopSpacer(icon: Int, text: Int, subText: Int, onClick: () -> Unit) {
    Spacer(modifier = Modifier.height(5.dp))
    CapsuleButton.IconButton(
        icon = icon,
        iconSize = 20,
        text = stringResource(text),
        subText = stringResource(subText)
    ) {
        onClick()
    }
}

@Composable
fun ClassificationBar(icon: Int, text: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = stringResource(id = text),
            modifier = Modifier.size(25.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = stringResource(id = text),
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview
@Composable
fun AboutAppScreenPreview() {
    AllInOneTheme {
        AboutAppScreen()
    }
}