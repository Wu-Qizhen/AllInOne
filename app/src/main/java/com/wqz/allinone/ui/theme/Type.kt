package com.wqz.allinone.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.misans_regular, FontWeight.Normal)
        ),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = Color.White,
        // lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)