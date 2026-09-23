package com.example.roomiesplit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
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
import com.example.roomiesplit.data.model.Dare
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MustardContainer
import com.example.ui.theme.OnMustardContainer
import com.example.ui.theme.SoftCoral
import com.example.ui.theme.WarmIvory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageDaresSheet(
    dares: List<Dare>,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onAddDare: (text: String, emoji: String) -> Unit,
    onDeleteDare: (dareId: String) -> Unit
) {
    var newDareText by remember { mutableStateOf("") }
    var selectedEmoji by remember { mutableStateOf("☕") }

    val emojiChoices = listOf("☕", "🍟", "🧼", "🍜", "🗑️", "🧹", "🍕", "🎲", "🛒")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = WarmIvory
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 36.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Casino,
                    contentDescription = "Dice",
                    tint = DeepTeal,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pre-Approved Group Dares",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Text(
                text = "Only pre-approved, friendly dares are rolled for the weekly 'Chhota Bhai'. Nothing humiliating!",
                fontSize = 12.sp,
                color = Color.Gray,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add new dare card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE2DDD3), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "Add New Pre-Approved Dare",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepTeal
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = newDareText,
                    onValueChange = { newDareText = it },
                    placeholder = { Text("e.g. Cleans the balcony plants, Buys chai...") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("new_dare_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        emojiChoices.take(6).forEach { emo ->
                            Text(
                                text = emo,
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (selectedEmoji == emo) MustardContainer else Color.Transparent)
                                    .padding(4.dp)
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (newDareText.isNotBlank()) {
                                onAddDare(newDareText.trim(), selectedEmoji)
                                newDareText = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("add_dare_button")
                    ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add", color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Existing Dares List
            Text(
                text = "Active Dare Pool (${dares.size})",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            dares.forEach { dare ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFEFE8DB), RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dare.text,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { onDeleteDare(dare.id) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete dare",
                            tint = SoftCoral.copy(alpha = 0.8f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
