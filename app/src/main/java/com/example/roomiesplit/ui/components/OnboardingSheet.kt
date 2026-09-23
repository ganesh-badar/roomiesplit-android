package com.example.roomiesplit.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.TealContainer
import com.example.ui.theme.WarmIvory
import com.example.ui.theme.WarmMustard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onCreateFlat: (name: String, upiId: String, adminName: String) -> Unit,
    onJoinFlat: (code: String, userName: String, phone: String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Create, 1 = Join

    // Create Flat state
    var flatName by remember { mutableStateOf("") }
    var adminUpi by remember { mutableStateOf("") }
    var adminName by remember { mutableStateOf("") }

    // Join Flat state
    var inviteCode by remember { mutableStateOf("") }
    var roommateName by remember { mutableStateOf("") }
    var roommatePhone by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = WarmIvory
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(TealContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (selectedTab == 0) Icons.Filled.HomeWork else Icons.Filled.MeetingRoom,
                        contentDescription = "Flat",
                        tint = DeepTeal
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (selectedTab == 0) "Create a New Flat" else "Join an Existing Flat",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = if (selectedTab == 0) "You become Admin & get an invite code" else "Enter invite code shared by flatmates",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = DeepTeal
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Create Flat", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Join with Code", fontWeight = FontWeight.Bold) }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (selectedTab == 0) {
                // Create Flat Form
                OutlinedTextField(
                    value = flatName,
                    onValueChange = { flatName = it },
                    label = { Text("Flat Name (e.g. Flat 3C, The Bachelors)") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("create_flat_name_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = adminName,
                    onValueChange = { adminName = it },
                    label = { Text("Your Name (Admin)") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("create_flat_admin_name_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = adminUpi,
                    onValueChange = { adminUpi = it },
                    label = { Text("Your Receiving UPI ID (e.g. yourname@okhdfcbank)") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("create_flat_upi_input")
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (flatName.isNotBlank() && adminName.isNotBlank()) {
                            onCreateFlat(flatName.trim(), adminUpi.trim(), adminName.trim())
                            onDismiss()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_create_flat_button")
                ) {
                    Text("Create Flat & Generate Code", fontWeight = FontWeight.Bold, color = Color.White)
                }
            } else {
                // Join Flat Form
                OutlinedTextField(
                    value = inviteCode,
                    onValueChange = { inviteCode = it.uppercase() },
                    label = { Text("Invite Code (e.g. FLAT4B)") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("join_flat_code_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = roommateName,
                    onValueChange = { roommateName = it },
                    label = { Text("Your Name") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("join_flat_name_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = roommatePhone,
                    onValueChange = { roommatePhone = it },
                    label = { Text("Phone Number / OTP Contact") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("join_flat_phone_input")
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (inviteCode.isNotBlank() && roommateName.isNotBlank()) {
                            onJoinFlat(inviteCode.trim(), roommateName.trim(), roommatePhone.trim())
                            onDismiss()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WarmMustard),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_join_flat_button")
                ) {
                    Text("Join Flat & Sync Data", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
