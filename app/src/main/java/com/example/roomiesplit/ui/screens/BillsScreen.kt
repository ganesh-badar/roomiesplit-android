package com.example.roomiesplit.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roomiesplit.data.model.Comment
import com.example.roomiesplit.data.model.Expense
import com.example.roomiesplit.data.model.Flat
import com.example.roomiesplit.data.model.Member
import com.example.roomiesplit.data.model.PaymentStatus
import com.example.roomiesplit.data.model.Role
import com.example.roomiesplit.ui.components.AvatarBadge
import com.example.roomiesplit.ui.components.PaymentStatusBadge
import com.example.roomiesplit.util.PdfReportGenerator
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MustardContainer
import com.example.ui.theme.OnMustardContainer
import com.example.ui.theme.SoftCoral
import com.example.ui.theme.TealContainer
import com.example.ui.theme.WarmIvory
import com.example.ui.theme.WarmMustard

@Composable
fun BillsScreen(
    flat: Flat,
    members: List<Member>,
    activeMember: Member,
    expenses: List<Expense>,
    commentsMap: Map<String, List<Comment>>,
    onPayExpense: (Expense, Double) -> Unit,
    onVerifyPayment: (expenseId: String, memberId: String) -> Unit,
    onSubmitGuess: (expenseId: String, guess: Double) -> Unit,
    onRevealBill: (expenseId: String) -> Unit,
    onAddComment: (expenseId: String, text: String) -> Unit,
    onOpenAddExpense: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("All") } // All, Pending, Settled
    val expandedExpenseIds = remember { mutableStateMapOf<String, Boolean>() }
    val commentInputs = remember { mutableStateMapOf<String, String>() }
    val guessInputs = remember { mutableStateMapOf<String, String>() }

    val isAdmin = activeMember.role == Role.ADMIN || activeMember.role == Role.CO_ADMIN

    val filteredExpenses = expenses.filter { exp ->
        when (selectedFilter) {
            "Pending" -> exp.statusMap.values.any { it != PaymentStatus.PAID_VERIFIED }
            "Settled" -> exp.statusMap.values.all { it == PaymentStatus.PAID_VERIFIED }
            else -> true
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmIvory)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                // Top Header Row with Title and "Download Monthly Report (PDF)" button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Expense Slips",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "${expenses.size} bills logged for ${flat.name}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    // PDF Export Button
                    Button(
                        onClick = {
                            PdfReportGenerator.generateAndShareMonthlyReport(
                                context = context,
                                flat = flat,
                                members = members,
                                expenses = expenses,
                                monthName = "October 2026"
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("download_monthly_report_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Download,
                            contentDescription = "PDF",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Monthly PDF",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Filter Chips
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("All", "Pending", "Settled").forEach { filter ->
                        val isSelected = filter == selectedFilter
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) DeepTeal else Color.White)
                                .border(1.dp, if (isSelected) DeepTeal else Color(0xFFDDD5C7), RoundedCornerShape(20.dp))
                                .clickable { selectedFilter = filter }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = filter,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color.Black
                            )
                        }
                    }
                }
            }

            if (filteredExpenses.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No bills match this filter", color = Color.Gray)
                    }
                }
            } else {
                items(filteredExpenses, key = { it.id }) { exp ->
                    val isExpanded = expandedExpenseIds[exp.id] ?: false
                    val comments = commentsMap[exp.id] ?: emptyList()
                    val myShare = exp.splitMap[activeMember.id] ?: 0.0
                    val myStatus = exp.statusMap[activeMember.id] ?: PaymentStatus.PENDING

                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8E0D2)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("expense_card_${exp.id}")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Header Row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        expandedExpenseIds[exp.id] = !isExpanded
                                    },
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(TealContainer.copy(alpha = 0.7f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = exp.category.emoji, fontSize = 22.sp)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = exp.title,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                        Text(
                                            text = "${exp.category.label} • Due ${exp.dueDate}",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "₹${exp.amount.toInt()}",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepTeal
                                    )
                                    Icon(
                                        imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                        contentDescription = "Expand",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Roommate Payment Transparency Matrix
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(WarmIvory)
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Status:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    members.filter { it.isActive }.forEach { mem ->
                                        val st = exp.statusMap[mem.id] ?: PaymentStatus.PENDING
                                        val statusColor = when (st) {
                                            PaymentStatus.PAID_VERIFIED -> DeepTeal
                                            PaymentStatus.PAID_UNVERIFIED -> WarmMustard
                                            PaymentStatus.PENDING -> SoftCoral
                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color.White)
                                                .border(1.dp, Color(0xFFDDD5C7), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(7.dp)
                                                    .clip(CircleShape)
                                                    .background(statusColor)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = mem.avatarInitial,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }

                            // Expandable Details (My Share, Payment Actions, Guess Contest, Comments)
                            AnimatedVisibility(visible = isExpanded) {
                                Column(modifier = Modifier.padding(top = 12.dp)) {
                                    HorizontalDivider(color = Color(0xFFEFE8DB))
                                    Spacer(modifier = Modifier.height(10.dp))

                                    // My Share & Quick Pay
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(text = "Your Share", fontSize = 11.sp, color = Color.Gray)
                                            Text(
                                                text = "₹${myShare.toInt()}",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = DeepTeal
                                            )
                                        }

                                        PaymentStatusBadge(status = myStatus)
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (myStatus == PaymentStatus.PENDING) {
                                        Button(
                                            onClick = { onPayExpense(exp, myShare) },
                                            colors = ButtonDefaults.buttonColors(containerColor = WarmMustard),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("bill_pay_button_${exp.id}")
                                        ) {
                                            Text(
                                                text = "Pay Share via UPI (₹${myShare.toInt()})",
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }

                                    // Admin verification row for other flatmates
                                    if (isAdmin) {
                                        val pendingVerifications = exp.statusMap.filter { it.value == PaymentStatus.PAID_UNVERIFIED }
                                        if (pendingVerifications.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = "Unverified Payments on this bill:",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = WarmMustard
                                            )
                                            pendingVerifications.forEach { (mId, _) ->
                                                val mem = members.find { it.id == mId }
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 4.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(text = "${mem?.name ?: "Roomie"} (₹${exp.splitMap[mId]?.toInt()})", fontSize = 13.sp)
                                                    Button(
                                                        onClick = { onVerifyPayment(exp.id, mId) },
                                                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                                                        shape = RoundedCornerShape(8.dp)
                                                    ) {
                                                        Text("Verify ✓", fontSize = 11.sp, color = Color.White)
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // "Guess the Bill" section
                                    if (exp.isGuessingOpen || !exp.actualTotalRevealed) {
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(MustardContainer.copy(alpha = 0.6f))
                                                .padding(12.dp)
                                        ) {
                                            Column {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(
                                                            imageVector = Icons.Filled.SportsEsports,
                                                            contentDescription = "Guess",
                                                            tint = OnMustardContainer,
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Text(
                                                            text = "Guess the Bill Contest!",
                                                            fontSize = 13.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = OnMustardContainer
                                                        )
                                                    }
                                                    Text(text = "+40 pts", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepTeal)
                                                }

                                                Spacer(modifier = Modifier.height(6.dp))

                                                val myGuess = exp.billGuesses[activeMember.id]
                                                if (myGuess != null) {
                                                    Text(
                                                        text = "Your guess: ₹${myGuess.toInt()} (Awaiting total reveal)",
                                                        fontSize = 12.sp,
                                                        color = OnMustardContainer
                                                    )
                                                } else {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        OutlinedTextField(
                                                            value = guessInputs[exp.id] ?: "",
                                                            onValueChange = { guessInputs[exp.id] = it },
                                                            placeholder = { Text("Your ₹ guess") },
                                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                            shape = RoundedCornerShape(8.dp),
                                                            modifier = Modifier
                                                                .weight(1f)
                                                                .height(50.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Button(
                                                            onClick = {
                                                                val g = guessInputs[exp.id]?.toDoubleOrNull()
                                                                if (g != null && g > 0) {
                                                                    onSubmitGuess(exp.id, g)
                                                                    guessInputs[exp.id] = ""
                                                                }
                                                            },
                                                            colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                                                            shape = RoundedCornerShape(8.dp)
                                                        ) {
                                                            Text("Submit", fontSize = 12.sp, color = Color.White)
                                                        }
                                                    }
                                                }

                                                // Admin reveal button
                                                if (isAdmin && !exp.actualTotalRevealed) {
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    Button(
                                                        onClick = { onRevealBill(exp.id) },
                                                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                                                        shape = RoundedCornerShape(8.dp),
                                                        modifier = Modifier.fillMaxWidth()
                                                    ) {
                                                        Text("Reveal Actual Bill & Award Points", fontSize = 12.sp, color = Color.White)
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // Lightweight Comment Thread
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Filled.ChatBubbleOutline,
                                            contentDescription = "Comments",
                                            tint = DeepTeal,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Bill Comments (${comments.size})",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    comments.forEach { c ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            AvatarBadge(initial = c.senderInitial, size = 22.dp)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text(
                                                    text = c.senderName,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = DeepTeal
                                                )
                                                Text(
                                                    text = c.text,
                                                    fontSize = 12.sp,
                                                    color = MaterialTheme.colorScheme.onBackground
                                                )
                                            }
                                        }
                                    }

                                    // Add comment input
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        OutlinedTextField(
                                            value = commentInputs[exp.id] ?: "",
                                            onValueChange = { commentInputs[exp.id] = it },
                                            placeholder = { Text("Ask or update about this bill...") },
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(50.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        IconButton(
                                            onClick = {
                                                val txt = commentInputs[exp.id]?.trim()
                                                if (!txt.isNullOrBlank()) {
                                                    onAddComment(exp.id, txt)
                                                    commentInputs[exp.id] = ""
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Send,
                                                contentDescription = "Send",
                                                tint = DeepTeal
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }

        // Floating Action Button for "+ New Bill"
        FloatingActionButton(
            onClick = onOpenAddExpense,
            containerColor = WarmMustard,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("fab_add_expense")
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Bill")
        }
    }
}
