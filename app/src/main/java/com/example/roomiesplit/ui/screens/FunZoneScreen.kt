package com.example.roomiesplit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roomiesplit.data.model.Dare
import com.example.roomiesplit.data.model.LeaderboardEntry
import com.example.roomiesplit.data.model.ScoreLog
import com.example.roomiesplit.ui.components.AvatarBadge
import com.example.ui.theme.CoralContainer
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MustardContainer
import com.example.ui.theme.OnCoralContainer
import com.example.ui.theme.OnMustardContainer
import com.example.ui.theme.SoftCoral
import com.example.ui.theme.TealContainer
import com.example.ui.theme.WarmIvory
import com.example.ui.theme.WarmMustard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FunZoneScreen(
    leaderboard: List<LeaderboardEntry>,
    currentDare: Dare?,
    scoreHistory: List<ScoreLog>,
    onSpinDareAgain: () -> Unit,
    onToggleDareDone: () -> Unit,
    onOpenLogScore: () -> Unit,
    onOpenManageDares: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedPeriodTab by remember { mutableIntStateOf(0) } // 0 = Weekly, 1 = Monthly
    val chhotaBhai = leaderboard.find { it.isChhotaBhai }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvory)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Fun Zone & Dares",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Leaderboard, game scores & friendly flat dares",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                // Log Score CTA
                Button(
                    onClick = onOpenLogScore,
                    colors = ButtonDefaults.buttonColors(containerColor = WarmMustard),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("log_score_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.SportsEsports,
                        contentDescription = "Log",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Log Game",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Chhota Bhai & Live Dare Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8E0D2)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "👶", fontSize = 26.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "THIS WEEK'S 'CHHOTA BHAI'",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WarmMustard
                                )
                                Text(
                                    text = chhotaBhai?.memberName ?: "Dev Mehta",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepTeal
                                )
                            }
                        }

                        // Lowest points indicator
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(CoralContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Lowest (${chhotaBhai?.points ?: 0} pts)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnCoralContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Dare content container
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(WarmIvory)
                            .border(1.dp, Color(0xFFE2DDD3), RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "ASSIGNED FLAT DARE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepTeal
                                )
                                if (currentDare?.isCompleted == true) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Filled.CheckCircle,
                                            contentDescription = "Done",
                                            tint = DeepTeal,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Completed!",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DeepTeal
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = currentDare?.text ?: "Makes morning ginger chai for everyone ☕",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                textDecoration = if (currentDare?.isCompleted == true) TextDecoration.LineThrough else TextDecoration.None,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onSpinDareAgain,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("funzone_spin_dare_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Casino,
                                contentDescription = "Spin",
                                tint = DeepTeal,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Spin Again 🎲", fontSize = 12.sp, color = DeepTeal, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onToggleDareDone,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentDare?.isCompleted == true) Color.Gray else DeepTeal
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = if (currentDare?.isCompleted == true) "Undo" else "Mark Done ✓",
                                fontSize = 12.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenManageDares() }
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Edit dares",
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Edit Pre-Approved Dares List",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Leaderboard Section
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8E0D2)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("leaderboard_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.EmojiEvents,
                                contentDescription = "Trophy",
                                tint = WarmMustard,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Bachelor Leaderboard",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        // Period selector
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf("Weekly", "Monthly").forEachIndexed { idx, label ->
                                val isSel = selectedPeriodTab == idx
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSel) DeepTeal else Color(0xFFEFE8DB))
                                        .clickable { selectedPeriodTab = idx }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSel) Color.White else Color.Black
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    leaderboard.forEachIndexed { rankIndex, entry ->
                        val medalEmoji = when (rankIndex) {
                            0 -> "🥇"
                            1 -> "🥈"
                            2 -> "🥉"
                            else -> if (entry.isChhotaBhai) "👶" else "${rankIndex + 1}"
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (entry.isChhotaBhai) CoralContainer.copy(alpha = 0.35f) else WarmIvory)
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = medalEmoji,
                                    fontSize = 20.sp,
                                    modifier = Modifier.width(28.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                AvatarBadge(initial = entry.avatarInitial, size = 30.dp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = entry.memberName,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (entry.isChhotaBhai) {
                                            Text(
                                                text = " (Chhota Bhai)",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = SoftCoral
                                            )
                                        }
                                    }
                                    Text(
                                        text = "⚡ Streak: ${entry.onTimeStreak} on-time",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                            }

                            Text(
                                text = "${entry.points} pts",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepTeal
                            )
                        }
                    }
                }
            }
        }

        // How Points Work info card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(TealContainer.copy(alpha = 0.6f))
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        text = "🎯 How Fun Zone Points Work",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepTeal
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• 🎮 Win real-world games (Ludo, FIFA, Cards) → +50 pts\n• 🎯 Closest guess on 'Guess the Bill' → +40 pts\n• ⚡ Pay share before due date → +25 pts\n• Lowest score becomes 'Chhota Bhai' & gets a fun dare!",
                        fontSize = 11.sp,
                        color = DeepTeal,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Points History Logs
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = "History",
                    tint = DeepTeal,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Recent Points Log (${scoreHistory.size})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        items(scoreHistory) { log ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFEFE8DB), RoundedCornerShape(10.dp))
                .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${log.winnerName} • ${log.sourceOrGame}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "${log.details} • ${SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(log.timestamp))}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(MustardContainer)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "+${log.points}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnMustardContainer
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
