package com.wqz.allinone.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.ui.ModifierExtends.clickVfx

/**
 * 胶囊按钮
 * Created by Wu Qizhen on 2024.6.16
 */
object CapsuleButton {
    @SuppressLint("InvalidColorHexValue")
    private val defaultBackgroundColor = Color(0xFF8C262626)

    @SuppressLint("InvalidColorHexValue")
    private val pressedBackgroundColor = Color(0xFFD9262626)

    private val borderColors = listOf(
        Color(0xFF333333),
        defaultBackgroundColor
    )

    @Composable
    fun LogoButtonWithBorder(
        icon: Int,
        text: String,
        subText: String,
        onClick: () -> Unit,
    ) {
        // val context = LocalContext.current
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        // val borderColor = Color(0xFF333333)
        val backgroundColor =
            if (isPressed.value) pressedBackgroundColor else defaultBackgroundColor
        val borderColorsBrush = remember {
            Brush.linearGradient(
                borderColors
            )
        }

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
            modifier = Modifier.run {
                clickVfx(interactionSource, true, onClick)
                    // .height(50.dp)
                    .wrapContentHeight()
                    .background(backgroundColor, RoundedCornerShape(50.dp))
                    // .border(1.dp, color = borderColor, shape = RoundedCornerShape(50.dp))
                    .border(
                        BorderStroke(1.dp, borderColorsBrush),
                        RoundedCornerShape(50.dp)
                    )
                    .fillMaxWidth()
                    .padding(10.dp)
            },
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
                    fontSize = 12.sp,
                    maxLines = 1
                )
                // Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subText,
                    fontSize = 10.sp,
                    color = Color.Gray,
                    maxLines = 1
                    // modifier = Modifier.alpha(ContentAlpha.medium)
                )
            }
        }
        /*}*/
    }

    @Composable
    fun IconButtonWithBorder(
        icon: Int,
        text: String,
        subText: String,
        onClick: () -> Unit,
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) pressedBackgroundColor else defaultBackgroundColor
        val borderColorsBrush = remember {
            Brush.linearGradient(
                borderColors
            )
        }

        Row(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick)
                // .height(50.dp)
                .wrapContentHeight()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                // .border(1.dp, color = borderColor, shape = RoundedCornerShape(50.dp))
                .border(
                    BorderStroke(1.dp, borderColorsBrush),
                    RoundedCornerShape(50.dp)
                )
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = icon),
                modifier = Modifier
                    .padding(5.dp)
                    .size(20.dp),
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
                    fontSize = 12.sp,
                )
                Text(
                    text = subText,
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview
@Composable
fun CapsuleButtonPreview() {
    Column {
        CapsuleButton.LogoButtonWithBorder(
            icon = R.drawable.logo_wqz,
            text = "Wu Qizhen",
            subText = "Developer"
        ) { }
        Spacer(modifier = Modifier.height(10.dp))
        CapsuleButton.IconButtonWithBorder(
            icon = R.drawable.ic_version,
            text = "Released 1.0.0",
            subText = "Version"
        ) { }
    }
}