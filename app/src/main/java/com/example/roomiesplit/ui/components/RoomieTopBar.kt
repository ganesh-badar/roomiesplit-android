package com.example.roomiesplit.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MustardContainer
import com.example.ui.theme.OnMustardContainer
import com.example.ui.theme.OnTealContainer
import com.example.ui.theme.TealContainer
import com.example.ui.theme.WarmIvory
import com.example.ui.theme.WarmMustard

@Composable
fun RoomieTopBar(
    flat: Flat,
    members: List<Member>,
    activeMember: Member,
    onSwitchUser: (String) -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var userMenuExpanded by remember { mutableStateOf(false) }

    Surface(
        color = WarmIvory,
        shadowElevation = 1.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Top row: Flat title + Invite Code + Settings
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(TealContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Flat Icon",
                            tint = DeepTeal,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = flat.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        // Invite code pill
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(TealContainer.copy(alpha = 0.6f))
                                .clickable {
                                    UpiPaymentUtil.copyToClipboard(context, "Invite Code", flat.inviteCode)
                                }
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Code: ${flat.inviteCode}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnTealContainer
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Filled.ContentCopy,
                                contentDescription = "Copy invite code",
                                tint = OnTealContainer,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }
                }

                // Settings icon button
                IconButton(
                    onClick = onOpenSettings,
                    modifier = Modifier.testTag("admin_settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Admin & Flat Settings",
                        tint = DeepTeal
                    )
                }
            }

            Spacer(modifier = Modifier.size(6.dp))

            // Switcher Bar: "Viewing as: [Name] (Role) ▼"
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MustardContainer.copy(alpha = 0.5f))
                        .clickable { userMenuExpanded = true }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AvatarBadge(
                            initial = activeMember.avatarInitial,
                            size = 24.dp,
                            backgroundColor = when (activeMember.role) {
                                Role.ADMIN -> DeepTeal
                                Role.CO_ADMIN -> Color(0xFF286D62)
                                Role.MEMBER -> WarmMustard
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Viewing as: ",
                            fontSize = 12.sp,
                            color = OnMustardContainer.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "${activeMember.name} (${activeMember.role.label})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnMustardContainer
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Switch",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DeepTeal
                        )
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Switch Roommate",
                            tint = DeepTeal,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                DropdownMenu(
                    expanded = userMenuExpanded,
                    onDismissRequest = { userMenuExpanded = false }
                ) {
                    Text(
                        text = "Switch Roommate Perspective",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    members.filter { it.isActive }.forEach { member ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AvatarBadge(
                                        initial = member.avatarInitial,
                                        size = 26.dp,
                                        backgroundColor = when (member.role) {
                                            Role.ADMIN -> DeepTeal
                                            Role.CO_ADMIN -> Color(0xFF286D62)
                                            Role.MEMBER -> WarmMustard
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = member.name,
                                            fontWeight = if (member.id == activeMember.id) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = member.role.label,
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            },
                            onClick = {
                                onSwitchUser(member.id)
                                userMenuExpanded = false
                            },
                            modifier = Modifier.testTag("switch_to_${member.avatarInitial.lowercase()}")
                        )
                    }
                }
            }
        }
    }
}
