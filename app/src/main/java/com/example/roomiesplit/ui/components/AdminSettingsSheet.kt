package com.example.roomiesplit.ui.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roomiesplit.data.model.Flat
import com.example.roomiesplit.data.model.Member
import com.example.roomiesplit.data.model.Role
import com.example.roomiesplit.util.UpiPaymentUtil
import com.example.ui.theme.CoralContainer
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MustardContainer
import com.example.ui.theme.OnCoralContainer
import com.example.ui.theme.OnMustardContainer
import com.example.ui.theme.SoftCoral
import com.example.ui.theme.TealContainer
import com.example.ui.theme.WarmIvory
import com.example.ui.theme.WarmMustard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSettingsSheet(
    flat: Flat,
    members: List<Member>,
    activeMember: Member,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onRegenerateCode: () -> Unit,
    onUpdateUpiId: (String) -> Unit,
    onPromoteToCoAdmin: (String) -> Unit,
    onRemoveMember: (String) -> Unit,
    onOpenCreateOrJoinFlat: () -> Unit
) {
    val context = LocalContext.current
    var editingUpi by remember { mutableStateOf(false) }
    var upiInput by remember { mutableStateOf(flat.upiId) }
    val isAdminOrCoAdmin = activeMember.role == Role.ADMIN || activeMember.role == Role.CO_ADMIN

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
            Text(
                text = "Flat & Admin Settings",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = flat.name,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Invite Code Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(TealContainer)
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "FLAT INVITE CODE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepTeal.copy(alpha = 0.8f)
                            )
                            Text(
                                text = flat.inviteCode,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepTeal
                            )
                        }

                        Row {
                            IconButton(
                                onClick = {
                                    UpiPaymentUtil.copyToClipboard(context, "Invite Code", flat.inviteCode)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ContentCopy,
                                    contentDescription = "Copy code",
                                    tint = DeepTeal
                                )
                            }
                            IconButton(
                                onClick = {
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "Join our flat '${flat.name}' on RoomieSplit! Use Invite Code: ${flat.inviteCode} or open: roomiesplit://join?code=${flat.inviteCode}"
                                        )
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Share Flat Invite Code"))
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Share,
                                    contentDescription = "Share code",
                                    tint = DeepTeal
                                )
                            }
                        }
                    }

                    if (isAdminOrCoAdmin) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onRegenerateCode() }
                                .padding(vertical = 4.dp, horizontal = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "Regenerate",
                                tint = DeepTeal,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Regenerate code",
                                fontSize = 12.sp,
                                color = DeepTeal,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Admin UPI ID Configuration
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE2DDD3), RoundedCornerShape(14.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (editingUpi) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Admin's UPI ID for payments:", fontSize = 11.sp, color = Color.Gray)
                        OutlinedTextField(
                            value = upiInput,
                            onValueChange = { upiInput = it },
                            placeholder = { Text("e.g. name@upi") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    IconButton(
                        onClick = {
                            onUpdateUpiId(upiInput)
                            editingUpi = false
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = "Save", tint = DeepTeal)
                    }
                } else {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Admin's Receiving UPI ID", fontSize = 11.sp, color = Color.Gray)
                        Text(
                            text = flat.upiId,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    if (isAdminOrCoAdmin) {
                        IconButton(onClick = { editingUpi = true }) {
                            Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit UPI", tint = DeepTeal)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Roommates List
            Text(
                text = "Flatmates (${members.count { it.isActive }})",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            members.filter { it.isActive }.forEach { member ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFEFE8DB), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AvatarBadge(
                            initial = member.avatarInitial,
                            size = 32.dp,
                            backgroundColor = when (member.role) {
                                Role.ADMIN -> DeepTeal
                                Role.CO_ADMIN -> Color(0xFF286D62)
                                Role.MEMBER -> WarmMustard
                            }
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = member.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                if (member.id == activeMember.id) {
                                    Text(
                                        text = " (You)",
                                        fontSize = 12.sp,
                                        color = DeepTeal,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Text(
                                text = "${member.role.label} • ${member.phone}",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    // Admin Actions on roommate
                    if (isAdminOrCoAdmin && member.id != activeMember.id && member.role != Role.ADMIN) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (member.role == Role.MEMBER) {
                                IconButton(
                                    onClick = { onPromoteToCoAdmin(member.id) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = "Promote to Co-Admin",
                                        tint = WarmMustard,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            IconButton(
                                onClick = { onRemoveMember(member.id) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Remove roommate",
                                    tint = SoftCoral,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Switch / Create New Flat Button
            OutlinedButton(
                onClick = {
                    onDismiss()
                    onOpenCreateOrJoinFlat()
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("create_or_join_flat_button")
            ) {
                Icon(imageVector = Icons.Filled.GroupAdd, contentDescription = "Add flat", tint = DeepTeal)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Create New Flat / Join Another", color = DeepTeal, fontWeight = FontWeight.Bold)
            }
        }
    }
}
