package com.example.roomiesplit.ui.screens

import android.content.Intent
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roomiesplit.data.model.Dare
import com.example.roomiesplit.data.model.Expense
import com.example.roomiesplit.data.model.Flat
import com.example.roomiesplit.data.model.LeaderboardEntry
import com.example.roomiesplit.data.model.Member
import com.example.roomiesplit.data.model.PaymentStatus
import com.example.roomiesplit.data.model.Role
import com.example.roomiesplit.ui.components.AvatarBadge
import com.example.roomiesplit.ui.components.PaymentStatusBadge
import com.example.roomiesplit.util.PdfReportGenerator
import com.example.ui.theme.CoralContainer
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MustardContainer
import com.example.ui.theme.OnCoralContainer
import com.example.ui.theme.OnMustardContainer
import com.example.ui.theme.OnTealContainer
import com.example.ui.theme.SoftCoral
import com.example.ui.theme.TealContainer
import com.example.ui.theme.WarmIvory
import com.example.ui.theme.WarmMustard

@Composable
fun HomeScreen(
    flat: Flat,
    members: List<Member>,
    activeMember: Member,
    expenses: List<Expense>,
    leaderboard: List<LeaderboardEntry>,
    currentDare: Dare?,
    onPayExpense: (Expense, Double) -> Unit,
    onVerifyPayment: (expenseId: String, memberId: String) -> Unit,
    onSpinDareAgain: () -> Unit,
    onToggleDareDone: () -> Unit,
    onOpenAddExpense: () -> Unit,
    onOpenLogScore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isAdmin = activeMember.role == Role.ADMIN || activeMember.role == Role.CO_ADMIN

    // Unverified payments across all expenses (for Admin review)
    val unverifiedList = mutableListOf<Pair<Expense, Member>>()
    expenses.forEach { exp ->
        exp.statusMap.forEach { (mId, status) ->
            if (status == PaymentStatus.PAID_UNVERIFIED) {
                val mem = members.find { it.id == mId }
                if (mem != null) {
                    unverifiedList.add(Pair(exp, mem))
                }
            }
        }
    }

    // Active dues for current user
    val myDues = expenses.filter { exp ->
        val status = exp.statusMap[activeMember.id]
        status == PaymentStatus.PENDING || status == PaymentStatus.PAID_UNVERIFIED
    }

    val myVerifiedExpenses = expenses.filter { exp ->
        exp.statusMap[activeMember.id] == PaymentStatus.PAID_VERIFIED
    }

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
            // Quick Financial Stats Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DeepTeal),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "MONTHLY FLAT POOL",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealContainer
                        )
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Admin: ${flat.upiId.take(16)}…",
                                fontSize = 11.sp,
                                color = Color.White
                            )
                        }
                    }

                    val totalBilled = expenses.sumOf { it.amount }
                    Text(
                        text = "₹${totalBilled.toInt()}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // My share status banner inside card
                    val myPendingTotal = myDues.sumOf { it.splitMap[activeMember.id] ?: 0.0 }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.12f))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Your Pending Dues",
                                fontSize = 11.sp,
                                color = TealContainer
                            )
                            Text(
                                text = if (myPendingTotal > 0) "₹${myPendingTotal.toInt()}" else "All Cleared! 🎉",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (myPendingTotal > 0) WarmMustard else Color.White
                            )
                        }

                        if (myVerifiedExpenses.isNotEmpty()) {
                            OutlinedButton(
                                onClick = {
                                    PdfReportGenerator.generateAndSharePersonalReceipt(
                                        context = context,
                                        flat = flat,
                                        member = activeMember,
                                        verifiedExpenses = myVerifiedExpenses
                                    )
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                modifier = Modifier.testTag("download_my_receipt_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Download,
                                    contentDescription = "Receipt",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("My Receipt", fontSize = 12.sp, color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // Admin Unverified Payments Alert (Two-step verification flow)
        if (isAdmin && unverifiedList.isNotEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MustardContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_verification_section")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Filled.NotificationsActive,
                                contentDescription = "Unverified alert",
                                tint = OnMustardContainer,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Awaiting Admin Verification (${unverifiedList.size})",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnMustardContainer
                            )
                        }

                        Text(
                            text = "Roommates reported paying via UPI. Tap 'Verify' to confirm receipt.",
                            fontSize = 12.sp,
                            color = OnMustardContainer.copy(alpha = 0.85f),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        unverifiedList.forEach { (exp, mem) ->
                            val amt = exp.splitMap[mem.id] ?: 0.0
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AvatarBadge(initial = mem.avatarInitial, size = 28.dp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(text = mem.name, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        Text(
                                            text = "${exp.title} • ₹${amt.toInt()}",
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Button(
                                    onClick = { onVerifyPayment(exp.id, mem.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.testTag("verify_payment_${mem.avatarInitial.lowercase()}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = "Verify",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Verify", fontSize = 12.sp, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }

        // "Chhota Bhai" Spotlight & Dare Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8E0D2)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("chhota_bhai_dare_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "👶", fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "This Week's 'Chhota Bhai'",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray
                                )
                                Text(
                                    text = chhotaBhai?.memberName ?: "TBD",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepTeal
                                )
                            }
                        }

                        // Points badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(CoralContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Lowest: ${chhotaBhai?.points ?: 0} pts",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnCoralContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Assigned Dare Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(WarmIvory)
                            .border(1.dp, Color(0xFFE2DDD3), RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "ASSIGNED DARE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WarmMustard
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

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = currentDare?.text ?: "Makes morning ginger chai for everyone ☕",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                textDecoration = if (currentDare?.isCompleted == true) TextDecoration.LineThrough else TextDecoration.None,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Action buttons: "Spin Again" and "Mark Done"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onSpinDareAgain,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("spin_dare_again_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Casino,
                                contentDescription = "Spin",
                                tint = DeepTeal,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Spin Again 🎲", fontSize = 12.sp, color = DeepTeal, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onToggleDareDone,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentDare?.isCompleted == true) Color.Gray else DeepTeal
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("mark_dare_done_button")
                        ) {
                            Text(
                                text = if (currentDare?.isCompleted == true) "Undo" else "Mark Done ✓",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Quick Actions Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Add Expense
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8E0D2)),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenAddExpense() }
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "New Bill",
                            tint = DeepTeal,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "+ New Bill",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepTeal
                        )
                    }
                }

                // Log Score
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8E0D2)),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenLogScore() }
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SportsEsports,
                            contentDescription = "Log Game",
                            tint = WarmMustard,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Log Game",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = WarmMustard
                        )
                    }
                }

                // Share Invite
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8E0D2)),
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Join '${flat.name}' on RoomieSplit! Invite Code: ${flat.inviteCode}"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Flat Invite"))
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share Invite",
                            tint = DeepTeal,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Invite",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepTeal
                        )
                    }
                }
            }
        }

        // "My Active Dues" Section
        item {
            Text(
                text = "My Active Dues (${myDues.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (myDues.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(TealContainer.copy(alpha = 0.5f))
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🎉", fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "No pending dues for you!",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepTeal
                        )
                        Text(
                            text = "All your shares are paid & verified. High five!",
                            fontSize = 12.sp,
                            color = DeepTeal.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        } else {
            items(myDues) { exp ->
                val myShare = exp.splitMap[activeMember.id] ?: 0.0
                val myStatus = exp.statusMap[activeMember.id] ?: PaymentStatus.PENDING

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8E0D2)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = exp.category.emoji, fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = exp.title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Due: ${exp.dueDate} • Total: ₹${exp.amount.toInt()}",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                            PaymentStatusBadge(status = myStatus)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Your Share",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "₹${myShare.toInt()}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepTeal
                                )
                            }

                            if (myStatus == PaymentStatus.PENDING) {
                                Button(
                                    onClick = { onPayExpense(exp, myShare) },
                                    colors = ButtonDefaults.buttonColors(containerColor = WarmMustard),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.testTag("pay_now_${exp.id}")
                                ) {
                                    Text(
                                        text = "Pay Now (UPI)",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }
                            } else {
                                Text(
                                    text = "Awaiting Admin ✓",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = WarmMustard
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
