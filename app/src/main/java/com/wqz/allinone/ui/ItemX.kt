package com.wqz.allinone.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.color.BackgroundColor
import com.wqz.allinone.ui.color.BorderColor
import com.wqz.allinone.ui.color.ContentColor
import com.wqz.allinone.ui.theme.AllInOneTheme

/**
 * 胶囊按钮 -> 项目按钮
 * Created by Wu Qizhen on 2024.6.16
 * Updated by Wu Qizhen on 2024.8.31
 */
object ItemX {
    private val BACKGROUND_DEFAULT_GRAY = BackgroundColor.DEFAULT_GRAY
    private val BACKGROUND_PRESSED_GRAY = BackgroundColor.PRESSED_GRAY
    private val BACKGROUND_DEFAULT_YELLOW = BackgroundColor.DEFAULT_YELLOW
    private val BACKGROUND_PRESSED_YELLOW = BackgroundColor.PRESSED_YELLOW
    private val BORDER_DEFAULT_GRAY = BorderColor.DEFAULT_GRAY
    private val BORDER_Width = 0.4f.dp

    @Composable
    fun Button(
        text: String,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BACKGROUND_PRESSED_YELLOW else BACKGROUND_DEFAULT_YELLOW

        Box(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick)
                .wrapContentSize()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = ContentColor.DEFAULT_BROWN
            )
        }
    }

    @Composable
    fun Button(
        icon: Int,
        text: String,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BACKGROUND_PRESSED_YELLOW else BACKGROUND_DEFAULT_YELLOW

        Row(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick)
                .wrapContentSize()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                .padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                tint = ContentColor.DEFAULT_BROWN,
                modifier = Modifier.size(20.dp),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = ContentColor.DEFAULT_BROWN
            )
        }
    }

    @Composable
    fun Capsule(
        image: Int,
        text: String,
        subText: String,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BACKGROUND_PRESSED_GRAY else BACKGROUND_DEFAULT_GRAY
        /*val borderColorsBrush = remember {
            Brush.linearGradient(
                borderColors
            )
        }*/

        /*Button(
            onClick = { },
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor
            ),
            border = BorderStroke(1.dp, color = borderColor),
            modifier = Modifier
                .height(50.dp),
            interactionSource = interactionSource
        ) {*/
        Row(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick)
                .wrapContentHeight()
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                // .border(1.dp, color = borderColor, shape = RoundedCornerShape(50.dp))
                .border(
                    width = BORDER_Width,
                    shape = RoundedCornerShape(50.dp),
                    brush = Brush.linearGradient(
                        BORDER_DEFAULT_GRAY,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = image),
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape),
                contentDescription = null,
            )
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = text,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1
                )
                // Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subText,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1
                    // modifier = Modifier.alpha(ContentAlpha.medium)
                )
            }
        }
        /*}*/
    }

    @Composable
    fun Capsule(
        icon: Int,
        iconSize: Int = 30,
        text: String,
        subText: String,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BACKGROUND_PRESSED_GRAY else BACKGROUND_DEFAULT_GRAY

        Row(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick)
                .wrapContentHeight()
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                // .border(1.dp, color = borderColor, shape = RoundedCornerShape(50.dp))
                .border(
                    width = BORDER_Width,
                    shape = RoundedCornerShape(50.dp),
                    brush = Brush.linearGradient(
                        BORDER_DEFAULT_GRAY,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconPadding: Dp = if (iconSize >= 30) {
                0.dp
            } else if (iconSize == 20) {
                5.dp
            } else {
                ((30 - iconSize) / 2).dp
            }

            Image(
                painter = painterResource(id = icon),
                modifier = Modifier
                    .size(30.dp)
                    .padding(iconPadding),
                contentDescription = null,
            )
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = text,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1
                )
                Text(
                    text = subText,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
        }
    }

    @Composable
    fun Capsule(
        icon: Int,
        iconSize: Int = 30,
        text: String,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BACKGROUND_PRESSED_GRAY else BACKGROUND_DEFAULT_GRAY

        Row(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick)
                .wrapContentHeight()
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                // .border(1.dp, color = borderColor, shape = RoundedCornerShape(50.dp))
                .border(
                    width = BORDER_Width,
                    shape = RoundedCornerShape(50.dp),
                    brush = Brush.linearGradient(
                        BORDER_DEFAULT_GRAY,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconPadding: Dp = if (iconSize >= 30) {
                0.dp
            } else if (iconSize == 20) {
                5.dp
            } else {
                ((30 - iconSize) / 2).dp
            }

            Image(
                painter = painterResource(id = icon),
                modifier = Modifier
                    .size(30.dp)
                    .padding(iconPadding),
                contentDescription = null,
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically),
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1
            )
        }
    }
}

@Preview
@Composable
fun ItemXPreview() {
    AllInOneTheme {
        Column {
            ItemX.Capsule(
                image = R.drawable.logo_wqz,
                text = "Wu Qizhen",
                subText = "Developer"
            ) { }
            Spacer(modifier = Modifier.height(10.dp))
            ItemX.Capsule(
                icon = R.drawable.ic_version,
                iconSize = 20,
                text = "Released 1.0.0",
                subText = "Version"
            ) { }
            Spacer(modifier = Modifier.height(10.dp))
            ItemX.Button(text = "Text Button") { }
        }
    }
}