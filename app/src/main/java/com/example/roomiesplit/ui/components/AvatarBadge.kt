package com.example.roomiesplit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WarmIvory

@Composable
fun AvatarBadge(
    initial: String,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    backgroundColor: Color = Color(0xFF1B4B43),
    textColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(1.5.dp, WarmIvory, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial.take(1).uppercase(),
            color = textColor,
            fontSize = (size.value * 0.44f).sp,
            fontWeight = FontWeight.Bold
        )
    }
}
