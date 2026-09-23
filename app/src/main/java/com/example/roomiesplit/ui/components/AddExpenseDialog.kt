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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.roomiesplit.data.model.ExpenseCategory
import com.example.roomiesplit.data.model.Member
import com.example.roomiesplit.data.model.SplitType
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MustardContainer
import com.example.ui.theme.OnMustardContainer
import com.example.ui.theme.TealContainer
import com.example.ui.theme.WarmIvory
import com.example.ui.theme.WarmMustard

@Composable
fun AddExpenseDialog(
    members: List<Member>,
    onDismiss: () -> Unit,
    onSubmit: (
        title: String,
        category: ExpenseCategory,
        amount: Double,
        dueDate: String,
        splitType: SplitType,
        customSplits: Map<String, Double>?,
        isGuessingOpen: Boolean
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ExpenseCategory.RENT) }
    var amountText by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("Oct 10, 2026") }
    var splitType by remember { mutableStateOf(SplitType.EQUAL) }
    var enableGuessContest by remember { mutableStateOf(false) }

    val activeMembers = members.filter { it.isActive }
    val customAmounts = remember {
        mutableStateMapOf<String, String>().apply {
            activeMembers.forEach { put(it.id, "") }
        }
    }

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
                // Top row with title & close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "New Expense Slip",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Category selector chip row
                Text(
                    text = "Category",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExpenseCategory.values().forEach { cat ->
                        val isSelected = cat == selectedCategory
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) DeepTeal else Color.White)
                                .border(
                                    1.dp,
                                    if (isSelected) DeepTeal else Color(0xFFDDD5C7),
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedCategory = cat }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${cat.emoji} ${cat.label}",
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color.Black
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Bill Title (e.g. October Rent, Wifi Bill)") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("expense_title_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Amount Input
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Total Bill Amount (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("expense_amount_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Due Date Input
                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Due Date") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = "Date",
                            tint = DeepTeal
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Split Type Selector (Equal vs Custom)
                Text(
                    text = "Split Among Roommates",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SplitType.values().forEach { st ->
                        val isSelected = st == splitType
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) TealContainer else Color.White)
                                .border(
                                    1.dp,
                                    if (isSelected) DeepTeal else Color(0xFFDDD5C7),
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { splitType = st }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = st.label,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) DeepTeal else Color.Black
                            )
                        }
                    }
                }

                // Custom splits breakdown if custom chosen
                if (splitType == SplitType.CUSTOM) {
                    Spacer(modifier = Modifier.height(10.dp))
                    activeMembers.forEach { mem ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${mem.name}:",
                                fontSize = 13.sp,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = customAmounts[mem.id] ?: "",
                                onValueChange = { customAmounts[mem.id] = it },
                                placeholder = { Text("₹") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.width(110.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // "Guess the Bill" Contest Toggle Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MustardContainer.copy(alpha = 0.6f))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SportsEsports,
                            contentDescription = "Guess the bill",
                            tint = OnMustardContainer,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Run 'Guess the Bill' Game?",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnMustardContainer
                            )
                            Text(
                                text = "Roommates guess total; closest earns +40 Fun Zone pts!",
                                fontSize = 11.sp,
                                color = OnMustardContainer.copy(alpha = 0.8f)
                            )
                        }
                    }
                    Switch(
                        checked = enableGuessContest,
                        onCheckedChange = { enableGuessContest = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = WarmMustard,
                            checkedTrackColor = DeepTeal
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Create Button
                Button(
                    onClick = {
                        val parsedAmount = amountText.toDoubleOrNull() ?: 0.0
                        if (parsedAmount > 0) {
                            val customMap = if (splitType == SplitType.CUSTOM) {
                                customAmounts.mapValues { it.value.toDoubleOrNull() ?: 0.0 }
                            } else null

                            val cleanTitle = if (title.isBlank()) "${selectedCategory.label} Bill" else title
                            onSubmit(
                                cleanTitle,
                                selectedCategory,
                                parsedAmount,
                                dueDate,
                                splitType,
                                customMap,
                                enableGuessContest
                            )
                            onDismiss()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_expense_button")
                ) {
                    Text(
                        text = "Publish Expense Slip",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
