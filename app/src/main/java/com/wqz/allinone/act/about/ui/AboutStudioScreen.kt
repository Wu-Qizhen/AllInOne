package com.wqz.allinone.act.about.ui

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.XTitleBar

/**
 * 关于组织
 * Created by Wu Qizhen on 2024.6.22
 * Refactored by Wu Qizhen on 2024.11.30
 */
@Composable
fun AboutStudioScreen() {
    // val context = LocalContext.current
    val scrollState = rememberScrollState()
    var isVisible by remember {
        mutableStateOf(true)
    }
    val animatedBlur by animateDpAsState(
        targetValue = if (isVisible) 5.dp else 0.dp,
        label = "blur"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        XTitleBar.TextTitleBar(title = R.string.about_studio)

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
            modifier = Modifier
                .clickVfx { isVisible = !isVisible }
                .blur(radius = animatedBlur, edgeTreatment = BlurredEdgeTreatment.Unbounded),
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

        Spacer(modifier = Modifier.height(50.dp))
    }
}

/*@Preview(showBackground = true)
@Composable
fun AboutStudioScreenPreview() {
    AllInOneTheme {
        AboutStudioScreen()
    }
}*/