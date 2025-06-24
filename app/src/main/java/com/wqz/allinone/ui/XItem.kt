package com.wqz.allinone.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
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
import com.wqz.allinone.ui.property.BorderWidth
import com.wqz.allinone.ui.theme.AllInOneTheme
import com.wqz.allinone.ui.theme.ThemeColor

/**
 * 胶囊按钮 -> 项目按钮
 * Created by Wu Qizhen on 2024.6.16 (V0)
 * Updated by Wu Qizhen on 2024.8.31 (V0)
 * Updated by Wu Qizhen on 2024.11.30 (V1)
 * Updated by Wu Qizhen on 2024.12.31 (V1)
 */
object XItem {
    private val BACKGROUND_DEFAULT_GRAY = BackgroundColor.DEFAULT_GRAY
    private val BACKGROUND_PRESSED_GRAY = BackgroundColor.PRESSED_GRAY
    private val BACKGROUND_DEFAULT_YELLOW = BackgroundColor.DEFAULT_YELLOW
    private val BACKGROUND_PRESSED_YELLOW = BackgroundColor.PRESSED_YELLOW
    private val BACKGROUND_DEFAULT_BROWN = BackgroundColor.DEFAULT_BROWN
    private val BORDER_DEFAULT_GRAY = BorderColor.DEFAULT_GRAY
    private val BORDER_WIDTH = BorderWidth.DEFAULT_WIDTH

    /**
     * 文本按钮
     * @param text 按钮文字
     * @param onClick 点击事件
     */
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

    /**
     * 文本按钮
     * @param text 按钮文字
     * @param color 颜色
     * @param onClick 点击事件
     */
    @Composable
    fun Button(
        text: String,
        color: List<Color>,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) color[1] else color[0]

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
                color = color[2]
            )
        }
    }

    /**
     * 图标按钮
     * @param icon 图标
     * @param text 按钮文字
     * @param onClick 点击事件
     */
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
                contentDescription = null
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

    /**
     * 图标按钮
     * @param icon 图标
     * @param text 按钮文字
     * @param color 颜色
     * @param onClick 点击事件
     */
    @Composable
    fun Button(
        icon: Int,
        text: String,
        color: List<Color>,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) color[1] else color[0]

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
                tint = color[2],
                modifier = Modifier.size(20.dp),
                contentDescription = null
            )

            Spacer(modifier = Modifier.size(5.dp))

            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = color[2]
            )
        }
    }

    /*
     * 带动态模糊背景的文本按钮
     * @param text 按钮文字
     * @param blurRadius 模糊半径
     * @param onClick 点击事件
     */
    /*@Composable
    fun BlurButton(
        text: String,
        blurRadius: Float = 15f,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()

        // 动态模糊效果
        val blurModifier =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                Modifier.graphicsLayer {
                    renderEffect = RenderEffect
                        .createBlurEffect(blurRadius, blurRadius, Shader.TileMode.DECAL)
                        .asComposeRenderEffect()
                }
            } else {
                Modifier // Android 12 以下不应用模糊
            }

        // 按钮背景颜色（根据按压状态变化）
        val backgroundColor = when {
            isPressed -> BACKGROUND_PRESSED_YELLOW
            else -> Color.Transparent // 使用透明背景以显示模糊效果
        }

        Box(
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null, // 禁用默认波纹效果
                    onClick = onClick
                )
                .wrapContentSize()
                .then(blurModifier) // 应用模糊效果
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(50.dp)
                )
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = ContentColor.DEFAULT_BROWN
            )
        }
    }*/

    /**
     * 胶囊按钮
     * @param image 图标
     * @param text 按钮文字
     * @param subText 副标题
     * @param onClick 点击事件
     */
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

        Row(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick)
                .wrapContentHeight()
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                .border(
                    width = BORDER_WIDTH,
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
                contentDescription = null
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

    /**
     * 胶囊按钮
     * @param icon 图标
     * @param iconSize 图标大小
     * @param text 按钮文字
     * @param subText 副标题
     * @param onClick 点击事件
     */
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
                .border(
                    width = BORDER_WIDTH,
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
                contentDescription = null
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

    /**
     * 胶囊按钮
     * @param icon 图标
     * @param iconSize 图标大小
     * @param text 按钮文字
     * @param onClick 点击事件
     */
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
                .border(
                    width = BORDER_WIDTH,
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
                contentDescription = null
            )

            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically),
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                maxLines = 1
            )
        }
    }

    /**
     * 卡片按钮
     * @param icon 图标
     * @param text 按钮文字
     * @param cardSize 卡片大小
     * @param iconSize 图标大小
     * @param onClick 点击事件
     */
    @Composable
    fun Card(
        icon: Int,
        text: String,
        cardSize: Int = 85,
        iconSize: Int = 30,
        onClick: () -> Unit,
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BACKGROUND_PRESSED_GRAY else BACKGROUND_DEFAULT_GRAY

        Box(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick)
                .size(cardSize.dp)
                .background(backgroundColor, RoundedCornerShape(15.dp))
                .border(
                    width = BORDER_WIDTH,
                    shape = RoundedCornerShape(15.dp),
                    brush = Brush.linearGradient(
                        BORDER_DEFAULT_GRAY,
                        start = Offset.Infinite,
                        end = Offset.Zero
                    )
                )
                .padding(10.dp)
        ) {
            Image(
                painter = painterResource(id = icon),
                modifier = Modifier.size(iconSize.dp),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1,
                modifier = Modifier
                    .align(Alignment.BottomStart)
            )
        }
    }

    /**
     * 切换按钮
     * @param icon 图标
     * @param iconSize 图标大小
     * @param text 按钮文字
     * @param subText 副文本
     * @param status 状态
     * @param onClick 点击事件
     */
    @Composable
    fun Switch(
        icon: Int,
        iconSize: Int = 30,
        text: String,
        subText: String,
        status: State<Boolean>,
        onClick: () -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        // val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (status.value) BACKGROUND_DEFAULT_BROWN else BACKGROUND_DEFAULT_GRAY
        val borderColor by animateColorAsState(
            targetValue = if (status.value) ThemeColor else Color(
                23,
                23,
                23,
                255
            ), label = ""
        )
        val borderWidth by animateDpAsState(
            targetValue = if (status.value) 2.0f.dp else BORDER_WIDTH,
            label = ""
        )

        Row(
            modifier = Modifier
                .clickVfx(interactionSource, true, onClick)
                .wrapContentHeight()
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(50.dp))
                .border(
                    width = borderWidth,
                    shape = RoundedCornerShape(50.dp),
                    color = borderColor
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
                contentDescription = null
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
}

@Preview
@Composable
fun XItemPreview() {
    AllInOneTheme {
        Column {
            Text(text = "普通按钮")
            Spacer(modifier = Modifier.height(10.dp))
            XItem.Button(text = "Text Button") { }
            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "胶囊按钮")
            Spacer(modifier = Modifier.height(10.dp))
            XItem.Capsule(
                image = R.drawable.logo_wqz,
                text = "Wu Qizhen",
                subText = "Developer"
            ) { }
            Spacer(modifier = Modifier.height(10.dp))
            XItem.Capsule(
                icon = R.drawable.ic_version,
                iconSize = 20,
                text = "Released 1.0.0",
                subText = "Version"
            ) { }
            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "卡片按钮")
            Spacer(modifier = Modifier.height(10.dp))
            XItem.Card(icon = R.drawable.ic_version, text = "Option") {}
        }
    }
}