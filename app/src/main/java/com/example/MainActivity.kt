package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.roomiesplit.data.model.Expense
import com.example.roomiesplit.data.model.Role
import com.example.roomiesplit.data.repository.RoomieRepository
import com.example.roomiesplit.ui.components.AddExpenseDialog
import com.example.roomiesplit.ui.components.AdminSettingsSheet
import com.example.roomiesplit.ui.components.LogScoreDialog
import com.example.roomiesplit.ui.components.ManageDaresSheet
import com.example.roomiesplit.ui.components.OnboardingSheet
import com.example.roomiesplit.ui.components.RoomieTopBar
import com.example.roomiesplit.ui.components.UpiPaymentSheet
import com.example.roomiesplit.ui.navigation.NavigationItem
import com.example.roomiesplit.ui.screens.BillsScreen
import com.example.roomiesplit.ui.screens.ChatScreen
import com.example.roomiesplit.ui.screens.FunZoneScreen
import com.example.roomiesplit.ui.screens.HomeScreen
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.RoomieSplitTheme
import com.example.ui.theme.TealContainer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoomieSplitTheme {
                RoomieSplitApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomieSplitApp() {
    val context = LocalContext.current

    // State flows from RoomieRepository
    val flat by RoomieRepository.flat.collectAsState()
    val members by RoomieRepository.members.collectAsState()
    val activeUserId by RoomieRepository.activeUserId.collectAsState()
    val expenses by RoomieRepository.expenses.collectAsState()
    val leaderboard by RoomieRepository.leaderboard.collectAsState()
    val currentDare by RoomieRepository.currentDare.collectAsState()
    val scoreHistory by RoomieRepository.scoreHistory.collectAsState()
    val commentsMap by RoomieRepository.comments.collectAsState()
    val chatMessages by RoomieRepository.chatMessages.collectAsState()
    val daresList by RoomieRepository.dares.collectAsState()

    val activeMember = members.find { it.id == activeUserId } ?: members.firstOrNull()
        ?: com.example.roomiesplit.data.model.Member("m_temp", "Roommate", Role.ADMIN, "R")

    // Active bottom navigation item
    var currentTab by remember { mutableStateOf(NavigationItem.HOME) }

    // Dialog & Bottom Sheet States
    var showAddExpenseDialog by remember { mutableStateOf(false) }
    var showLogScoreDialog by remember { mutableStateOf(false) }
    var showAdminSettingsSheet by remember { mutableStateOf(false) }
    var showManageDaresSheet by remember { mutableStateOf(false) }
    var showOnboardingSheet by remember { mutableStateOf(false) }

    // UPI Payment Sheet state
    var payingExpense by remember { mutableStateOf<Expense?>(null) }
    var payingAmount by remember { mutableStateOf(0.0) }

    val adminSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val upiSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val daresSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val onboardingSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            RoomieTopBar(
                flat = flat,
                members = members,
                activeMember = activeMember,
                onSwitchUser = { memberId ->
                    RoomieRepository.switchActiveUser(memberId)
                },
                onOpenSettings = {
                    showAdminSettingsSheet = true
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 4.dp
            ) {
                NavigationItem.values().forEach { item ->
                    val isSelected = currentTab == item
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = item },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepTeal,
                            selectedTextColor = DeepTeal,
                            indicatorColor = TealContainer,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier.testTag(item.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                NavigationItem.HOME -> {
                    HomeScreen(
                        flat = flat,
                        members = members,
                        activeMember = activeMember,
                        expenses = expenses,
                        leaderboard = leaderboard,
                        currentDare = currentDare,
                        onPayExpense = { exp, amt ->
                            payingExpense = exp
                            payingAmount = amt
                        },
                        onVerifyPayment = { expId, memId ->
                            RoomieRepository.verifyPayment(expId, memId)
                            Toast.makeText(context, "Payment verified! +25 streak points awarded.", Toast.LENGTH_SHORT).show()
                        },
                        onSpinDareAgain = {
                            RoomieRepository.spinDareAgain()
                            Toast.makeText(context, "New dare assigned! 🎲", Toast.LENGTH_SHORT).show()
                        },
                        onToggleDareDone = {
                            RoomieRepository.toggleDareDone()
                        },
                        onOpenAddExpense = {
                            showAddExpenseDialog = true
                        },
                        onOpenLogScore = {
                            showLogScoreDialog = true
                        }
                    )
                }

                NavigationItem.BILLS -> {
                    BillsScreen(
                        flat = flat,
                        members = members,
                        activeMember = activeMember,
                        expenses = expenses,
                        commentsMap = commentsMap,
                        onPayExpense = { exp, amt ->
                            payingExpense = exp
                            payingAmount = amt
                        },
                        onVerifyPayment = { expId, memId ->
                            RoomieRepository.verifyPayment(expId, memId)
                            Toast.makeText(context, "Payment verified! ✓", Toast.LENGTH_SHORT).show()
                        },
                        onSubmitGuess = { expId, guess ->
                            RoomieRepository.submitBillGuess(expId, guess)
                            Toast.makeText(context, "Guess of ₹${guess.toInt()} submitted!", Toast.LENGTH_SHORT).show()
                        },
                        onRevealBill = { expId ->
                            RoomieRepository.revealBillAndAwardPoints(expId)
                            Toast.makeText(context, "Bill revealed & points awarded to closest guesser!", Toast.LENGTH_SHORT).show()
                        },
                        onAddComment = { expId, text ->
                            RoomieRepository.addComment(expId, text)
                        },
                        onOpenAddExpense = {
                            showAddExpenseDialog = true
                        }
                    )
                }

                NavigationItem.FUN_ZONE -> {
                    FunZoneScreen(
                        leaderboard = leaderboard,
                        currentDare = currentDare,
                        scoreHistory = scoreHistory,
                        onSpinDareAgain = {
                            RoomieRepository.spinDareAgain()
                            Toast.makeText(context, "Dare spun! 🎲", Toast.LENGTH_SHORT).show()
                        },
                        onToggleDareDone = {
                            RoomieRepository.toggleDareDone()
                        },
                        onOpenLogScore = {
                            showLogScoreDialog = true
                        },
                        onOpenManageDares = {
                            showManageDaresSheet = true
                        }
                    )
                }

                NavigationItem.CHAT -> {
                    ChatScreen(
                        flat = flat,
                        activeMember = activeMember,
                        messages = chatMessages,
                        onSendMessage = { msg ->
                            RoomieRepository.sendChatMessage(msg)
                        }
                    )
                }
            }
        }
    }

    // Modal Bottom Sheets & Dialogs

    // 1. UPI Payment Sheet
    payingExpense?.let { exp ->
        val adminMember = members.find { it.role == Role.ADMIN }
        UpiPaymentSheet(
            expense = exp,
            amountOwed = payingAmount,
            adminUpiId = flat.upiId,
            adminName = adminMember?.name ?: "Admin",
            sheetState = upiSheetState,
            onDismiss = { payingExpense = null },
            onMarkPaid = {
                RoomieRepository.markPaidUnverified(exp.id, activeMember.id)
                payingExpense = null
                Toast.makeText(context, "Marked as paid! Admin notified to verify.", Toast.LENGTH_LONG).show()
            }
        )
    }

    // 2. Add Expense Dialog
    if (showAddExpenseDialog) {
        AddExpenseDialog(
            members = members,
            onDismiss = { showAddExpenseDialog = false },
            onSubmit = { title, category, amount, dueDate, splitType, customSplits, isGuessingOpen ->
                RoomieRepository.addExpense(
                    title = title,
                    category = category,
                    amount = amount,
                    dueDate = dueDate,
                    splitType = splitType,
                    customSplits = customSplits,
                    isGuessingOpen = isGuessingOpen
                )
                showAddExpenseDialog = false
                Toast.makeText(context, "Expense slip published!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // 3. Log Game Score Dialog
    if (showLogScoreDialog) {
        LogScoreDialog(
            members = members,
            onDismiss = { showLogScoreDialog = false },
            onSubmit = { gameName, winnerId, points ->
                RoomieRepository.logGameScore(gameName, winnerId, points)
                showLogScoreDialog = false
                val winner = members.find { it.id == winnerId }
                Toast.makeText(context, "Score logged! +$points to ${winner?.name}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // 4. Admin Settings Sheet
    if (showAdminSettingsSheet) {
        AdminSettingsSheet(
            flat = flat,
            members = members,
            activeMember = activeMember,
            sheetState = adminSheetState,
            onDismiss = { showAdminSettingsSheet = false },
            onRegenerateCode = {
                RoomieRepository.regenerateInviteCode()
                Toast.makeText(context, "New invite code generated!", Toast.LENGTH_SHORT).show()
            },
            onUpdateUpiId = { newUpi ->
                RoomieRepository.updateUpiId(newUpi)
                Toast.makeText(context, "Admin UPI ID updated!", Toast.LENGTH_SHORT).show()
            },
            onPromoteToCoAdmin = { memId ->
                RoomieRepository.promoteToCoAdmin(memId)
                Toast.makeText(context, "Promoted to Co-Admin!", Toast.LENGTH_SHORT).show()
            },
            onRemoveMember = { memId ->
                RoomieRepository.removeMember(memId)
                Toast.makeText(context, "Member removed from active flat.", Toast.LENGTH_SHORT).show()
            },
            onOpenCreateOrJoinFlat = {
                showAdminSettingsSheet = false
                showOnboardingSheet = true
            }
        )
    }

    // 5. Manage Dares Sheet
    if (showManageDaresSheet) {
        ManageDaresSheet(
            dares = daresList,
            sheetState = daresSheetState,
            onDismiss = { showManageDaresSheet = false },
            onAddDare = { text, emoji ->
                RoomieRepository.addPreApprovedDare(text, emoji)
                Toast.makeText(context, "Approved dare added!", Toast.LENGTH_SHORT).show()
            },
            onDeleteDare = { dareId ->
                RoomieRepository.deleteDare(dareId)
            }
        )
    }

    // 6. Onboarding Sheet (Create / Join Flat)
    if (showOnboardingSheet) {
        OnboardingSheet(
            sheetState = onboardingSheetState,
            onDismiss = { showOnboardingSheet = false },
            onCreateFlat = { name, upi, adminName ->
                RoomieRepository.createNewFlat(name, upi, adminName)
                showOnboardingSheet = false
                Toast.makeText(context, "Created '$name'! You are Admin 👑", Toast.LENGTH_SHORT).show()
            },
            onJoinFlat = { code, userName, phone ->
                RoomieRepository.joinFlat(code, userName, phone)
                showOnboardingSheet = false
                Toast.makeText(context, "Joined flat! Data synced.", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
