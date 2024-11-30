package com.wqz.allinone.act.knowledge.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.act.knowledge.data.Outline
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.color.BackgroundColor
import com.wqz.allinone.ui.color.BorderColor
import com.wqz.allinone.ui.property.BorderWidth

object SelectItem {
    @Composable
    fun SubjectItem(
        text: Int,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val contentColor =
            if (isPressed.value) Color.Gray else Color.White

        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(10.dp)
                .clickVfx(
                    interactionSource = interactionSource,
                    enabled = true,
                    onClick = onClick
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_book),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = stringResource(id = text),
                fontSize = 16.sp,
                color = contentColor,
                maxLines = 1
            )
        }
    }

    @Composable
    fun OutlineItem(
        outline: Outline
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BackgroundColor.PRESSED_GRAY else BackgroundColor.DEFAULT_GRAY
        val borderColors = BorderColor.DEFAULT_GRAY
        val borderWidth = BorderWidth.DEFAULT_WIDTH
        var showDetails by remember { mutableStateOf(false) }

        Column {
            Spacer(modifier = Modifier.height(3.dp))

            Column(
                modifier = Modifier
                    .clickVfx(
                        interactionSource = interactionSource,
                        enabled = true,
                        onClick = {
                            showDetails = !showDetails
                        }
                    )
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .background(backgroundColor, RoundedCornerShape(10.dp))
                    .border(
                        width = borderWidth,
                        shape = RoundedCornerShape(10.dp),
                        brush = Brush.linearGradient(
                            borderColors,
                            start = Offset.Zero,
                            end = Offset.Infinite
                        )
                    )
                    .padding(10.dp)
            ) {
                Text(
                    text = outline.title,
                    fontSize = 16.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold
                )

                AnimatedVisibility(
                    visible = showDetails,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = outline.content,
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}