package com.example.roomiesplit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roomiesplit.data.model.Expense
import com.example.roomiesplit.util.UpiPaymentUtil
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MustardContainer
import com.example.ui.theme.OnMustardContainer
import com.example.ui.theme.TealContainer
import com.example.ui.theme.WarmIvory
import com.example.ui.theme.WarmMustard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpiPaymentSheet(
    expense: Expense,
    amountOwed: Double,
    adminUpiId: String,
    adminName: String,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onMarkPaid: () -> Unit
) {
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = WarmIvory
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MustardContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountBalanceWallet,
                    contentDescription = "UPI Wallet",
                    tint = WarmMustard,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Pay Your Share",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "${expense.title} (${expense.category.emoji} ${expense.category.label})",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Amount Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(TealContainer)
                    .padding(vertical = 18.dp, horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "AMOUNT DUE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepTeal.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "₹${String.format("%.2f", amountOwed)}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepTeal
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Payee UPI ID Card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE2DDD3), RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Admin's UPI ID ($adminName)",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = adminUpiId,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                IconButton(
                    onClick = {
                        UpiPaymentUtil.copyToClipboard(context, "UPI ID", adminUpiId)
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ContentCopy,
                        contentDescription = "Copy UPI ID",
                        tint = DeepTeal
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Two-step notice box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MustardContainer.copy(alpha = 0.6f))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Two step notice",
                    tint = OnMustardContainer,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "After transferring in your UPI app, tap 'I've Paid' below. Admin will verify receipt.",
                    fontSize = 12.sp,
                    color = OnMustardContainer,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Launch UPI Button
            Button(
                onClick = {
                    val launched = UpiPaymentUtil.launchUpiIntent(
                        context = context,
                        upiId = adminUpiId,
                        payeeName = adminName,
                        amount = amountOwed,
                        note = "${expense.category.label} - ${expense.title}"
                    )
                    if (!launched) {
                        UpiPaymentUtil.copyToClipboard(context, "Admin UPI ID", adminUpiId)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = WarmMustard),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("launch_upi_app_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.QrCode2,
                    contentDescription = "Pay via UPI",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Open UPI App (GPay / PhonePe / Paytm)",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // "I've Paid" Confirm Button
            Button(
                onClick = {
                    onMarkPaid()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("confirm_ive_paid_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "I've Paid",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "I've Paid (Submit for Verification)",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}
