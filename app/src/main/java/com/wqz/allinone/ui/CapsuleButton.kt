package com.wqz.allinone.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.wqz.allinone.ui.theme.AllInOneTheme
import com.wqz.allinone.ui.theme.BorderColors
import com.wqz.allinone.ui.theme.DefaultBackgroundColor
import com.wqz.allinone.ui.theme.PressedBackgroundColor

/**
 * 胶囊按钮
 * Created by Wu Qizhen on 2024.6.16
 */
object CapsuleButton {
    /*private val defaultBackgroundColor = Color(38, 38, 38, 115)
    private val pressedBackgroundColor = Color(38, 38, 38, 153)
    private val borderColors = listOf(
        Color(54, 54, 54, 255),
        Color.Transparent
    )*/
    private val defaultBackgroundColor = DefaultBackgroundColor
    private val pressedBackgroundColor = PressedBackgroundColor
    private val borderColors = BorderColors
    private val borderWidth = 0.4f.dp

    @Composable
    fun LogoButton(
        icon: Int,
        text: String,
        subText: String,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) pressedBackgroundColor else defaultBackgroundColor
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
                    width = borderWidth,
                    shape = RoundedCornerShape(50.dp),
                    brush = Brush.linearGradient(
                        borderColors,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = icon),
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
    fun IconButton(
        icon: Int,
        iconSize: Int = 30,
        text: String,
        subText: String,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) pressedBackgroundColor else defaultBackgroundColor

        Row(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick)
                .wrapContentHeight()
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                // .border(1.dp, color = borderColor, shape = RoundedCornerShape(50.dp))
                .border(
                    width = borderWidth,
                    shape = RoundedCornerShape(50.dp),
                    brush = Brush.linearGradient(
                        borderColors,
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
    fun IconButton(
        icon: Int,
        iconSize: Int = 30,
        text: String,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) pressedBackgroundColor else defaultBackgroundColor

        Row(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick)
                .wrapContentHeight()
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                // .border(1.dp, color = borderColor, shape = RoundedCornerShape(50.dp))
                .border(
                    width = borderWidth,
                    shape = RoundedCornerShape(50.dp),
                    brush = Brush.linearGradient(
                        borderColors,
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
fun CapsuleButtonPreview() {
    AllInOneTheme {
        Column {
            CapsuleButton.LogoButton(
                icon = R.drawable.logo_wqz,
                text = "Wu Qizhen",
                subText = "Developer"
            ) { }
            Spacer(modifier = Modifier.height(10.dp))
            CapsuleButton.IconButton(
                icon = R.drawable.ic_version,
                iconSize = 20,
                text = "Released 1.0.0",
                subText = "Version"
            ) { }
        }
    }
}