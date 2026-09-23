package com.example.roomiesplit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roomiesplit.data.model.PaymentStatus
import com.example.ui.theme.CoralContainer
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MustardContainer
import com.example.ui.theme.OnCoralContainer
import com.example.ui.theme.OnMustardContainer
import com.example.ui.theme.OnTealContainer
import com.example.ui.theme.SoftCoral
import com.example.ui.theme.TealContainer
import com.example.ui.theme.WarmMustard

@Composable
fun PaymentStatusBadge(
    status: PaymentStatus,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val (bgColor, textColor, icon, label) = when (status) {
        PaymentStatus.PAID_VERIFIED -> Quadruple(
            TealContainer,
            DeepTeal,
            Icons.Filled.Check,
            "Paid – verified"
        )
        PaymentStatus.PAID_UNVERIFIED -> Quadruple(
            MustardContainer,
            OnMustardContainer,
            Icons.Filled.HourglassTop,
            "Paid – unverified"
        )
        PaymentStatus.PENDING -> Quadruple(
            CoralContainer,
            OnCoralContainer,
            Icons.Filled.Schedule,
            "Pending"
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = if (compact) 6.dp else 10.dp, vertical = if (compact) 3.dp else 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = textColor,
                modifier = Modifier.size(if (compact) 12.dp else 14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (compact) label.split(" – ").first() else label,
                color = textColor,
                fontSize = if (compact) 11.sp else 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
