package com.example.roomiesplit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.roomiesplit.data.model.Member
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MustardContainer
import com.example.ui.theme.OnMustardContainer
import com.example.ui.theme.TealContainer
import com.example.ui.theme.WarmIvory
import com.example.ui.theme.WarmMustard

@Composable
fun LogScoreDialog(
    members: List<Member>,
    onDismiss: () -> Unit,
    onSubmit: (gameName: String, winnerMemberId: String, points: Int) -> Unit
) {
    val commonGames = listOf("Ludo King 🎲", "FIFA 24 ⚽", "Uno / Cards 🃏", "Foosball ⚽", "Carrom 🎯", "Chess ♟️", "8-Ball Pool 🎱")
    var selectedGame by remember { mutableStateOf(commonGames.first()) }
    var customGame by remember { mutableStateOf("") }
    val activeMembers = members.filter { it.isActive }
    var selectedWinnerId by remember { mutableStateOf(activeMembers.firstOrNull()?.id ?: "") }
    var points by remember { mutableStateOf(50) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = WarmIvory),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
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
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Log Game Score",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close", tint = Color.Gray)
                    }
                }

                Text(
                    text = "Winner earns points on the bachelor leaderboard!",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Select Game
                Text(text = "Choose Game", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    commonGames.forEach { game ->
                        val isSelected = game == selectedGame && customGame.isBlank()
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) DeepTeal else Color.White)
                                .border(1.dp, if (isSelected) DeepTeal else Color(0xFFDDD5C7), RoundedCornerShape(10.dp))
                                .clickable {
                                    selectedGame = game
                                    customGame = ""
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = game,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color.Black
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = customGame,
                    onValueChange = { customGame = it },
                    placeholder = { Text("Or enter custom game / bet title") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Select Winner
                Text(text = "Who Won?", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    activeMembers.forEach { mem ->
                        val isSelected = mem.id == selectedWinnerId
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) TealContainer else Color.White)
                                .border(1.dp, if (isSelected) DeepTeal else Color(0xFFE2DDD3), RoundedCornerShape(10.dp))
                                .clickable { selectedWinnerId = mem.id }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AvatarBadge(initial = mem.avatarInitial, size = 26.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = mem.name,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) DeepTeal else Color.Black
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            if (isSelected) {
                                Text(text = "🏆 Winner", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepTeal)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Points selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MustardContainer.copy(alpha = 0.5f))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Points Awarded:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = OnMustardContainer)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(25, 50, 75).forEach { p ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (points == p) WarmMustard else Color.White)
                                    .clickable { points = p }
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "+$p",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (points == p) Color.White else Color.Black
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val finalGame = if (customGame.isNotBlank()) customGame.trim() else selectedGame
                        if (selectedWinnerId.isNotBlank()) {
                            onSubmit(finalGame, selectedWinnerId, points)
                            onDismiss()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_game_score_button")
                ) {
                    Text(text = "Award Points & Update Leaderboard", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
