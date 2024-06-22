package com.wqz.allinone.act_about.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
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
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.theme.AllInOneTheme

/**
 * 关于组织
 * Created by Wu Qizhen on 2024.6.22
 */
@Composable
fun AboutStudioScreen() {
    // val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = stringResource(id = R.string.title_about_studio),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(
                Font(R.font.fzfengrusongti_regular, FontWeight.Normal)
            ),
            color = colorResource(id = R.color.theme_yellow),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .align(Alignment.CenterHorizontally)
        )

        Image(
            painter = painterResource(id = R.drawable.logo_code_intellix_poster),
            contentDescription = null,
            modifier = Modifier
                .clickVfx { }
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(15.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(id = R.string.text_studio_describe),
            fontSize = 12.sp
        )

        ClassificationBar(icon = R.drawable.ic_develop, text = R.string.dept_dev)

        Spacer(modifier = Modifier.height(5.dp))

        Image(
            painter = painterResource(id = R.drawable.logo_intellic_lab_poster),
            contentDescription = null,
            modifier = Modifier
                .clickVfx { }
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(15.dp)),
            contentScale = ContentScale.Crop
        )

        ClassificationBar(icon = R.drawable.ic_draw, text = R.string.dept_design)

        Spacer(modifier = Modifier.height(5.dp))

        Image(
            painter = painterResource(id = R.drawable.logo_intellid_studio_poster),
            contentDescription = null,
            modifier = Modifier
                .clickVfx { }
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(15.dp)),
            contentScale = ContentScale.Crop
        )

        ClassificationBar(icon = R.drawable.ic_light, text = R.string.dept_create)

        Spacer(modifier = Modifier.height(5.dp))

        Image(
            painter = painterResource(id = R.drawable.logo_intellia_visual_poster),
            contentDescription = null,
            modifier = Modifier
                .clickVfx { }
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(15.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun AboutStudioScreenPreview() {
    AllInOneTheme {
        AboutStudioScreen()
    }
}