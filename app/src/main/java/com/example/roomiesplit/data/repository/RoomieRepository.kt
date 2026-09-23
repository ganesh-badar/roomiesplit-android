package com.example.roomiesplit.data.repository

import com.example.roomiesplit.data.model.ChatMessage
import com.example.roomiesplit.data.model.Comment
import com.example.roomiesplit.data.model.Dare
import com.example.roomiesplit.data.model.Expense
import com.example.roomiesplit.data.model.ExpenseCategory
import com.example.roomiesplit.data.model.Flat
import com.example.roomiesplit.data.model.LeaderboardEntry
import com.example.roomiesplit.data.model.Member
import com.example.roomiesplit.data.model.PaymentStatus
import com.example.roomiesplit.data.model.Role
import com.example.roomiesplit.data.model.ScoreLog
import com.example.roomiesplit.data.model.SplitType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import kotlin.math.abs
import kotlin.random.Random

object RoomieRepository {

    // Initial Flat
    private val _flat = MutableStateFlow(
        Flat(
            id = "flat_4b_blr",
            name = "Flat 4B - Silicon Babs",
            inviteCode = "FLAT4B",
            upiId = "alex.flat4b@okhdfcbank"
        )
    )
    val flat: StateFlow<Flat> = _flat.asStateFlow()

    // Members of Flat 4B
    private val initialMembers = listOf(
        Member("m_alex", "Alex Mercer", Role.ADMIN, "A", "+91 98765 43210", 0xFF1B4B43),
        Member("m_kabir", "Kabir Roy", Role.CO_ADMIN, "K", "+91 98220 11223", 0xFF286D62),
        Member("m_rohan", "Rohan Verma", Role.MEMBER, "R", "+91 97110 44556", 0xFFE8A33D),
        Member("m_dev", "Dev Mehta", Role.MEMBER, "D", "+91 99001 88990", 0xFFD8544F)
    )
    private val _members = MutableStateFlow(initialMembers)
    val members: StateFlow<List<Member>> = _members.asStateFlow()

    // Currently logged-in / active perspective user (default Alex - Admin)
    private val _activeUserId = MutableStateFlow("m_alex")
    val activeUserId: StateFlow<String> = _activeUserId.asStateFlow()

    // Pre-approved friendly group dares
    private val initialDares = listOf(
        Dare("d_1", "Makes morning ginger chai for everyone ☕", "☕", active = true),
        Dare("d_2", "Buys Friday evening samosas & snacks for the house 🍟", "🍟", active = true),
        Dare("d_3", "Does all the kitchen dishes for 2 full days 🧼", "🧼", active = true),
        Dare("d_4", "Prepares 2 AM midnight special Maggi with cheese 🍜", "🍜", active = true),
        Dare("d_5", "Takes out all flat trash & recycling for a week 🗑️", "🗑️", active = true),
        Dare("d_6", "Cleans and tidies the living room after game night 🧹", "🧹", active = true),
        Dare("d_7", "Treats everyone to weekend filter coffee ☕", "☕", active = true),
        Dare("d_8", "Waters balcony plants and wipes dining table for 3 days 🌱", "🌱", active = true)
    )
    private val _dares = MutableStateFlow(initialDares)
    val dares: StateFlow<List<Dare>> = _dares.asStateFlow()

    private val _currentDare = MutableStateFlow<Dare?>(initialDares[0])
    val currentDare: StateFlow<Dare?> = _currentDare.asStateFlow()

    // Initial Expenses
    private val _expenses = MutableStateFlow(
        listOf(
            Expense(
                id = "exp_rent_oct",
                title = "Monthly Flat Rent",
                category = ExpenseCategory.RENT,
                amount = 32000.0,
                dueDate = "Oct 05, 2026",
                splitType = SplitType.EQUAL,
                splitMap = mapOf(
                    "m_alex" to 8000.0,
                    "m_kabir" to 8000.0,
                    "m_rohan" to 8000.0,
                    "m_dev" to 8000.0
                ),
                statusMap = mapOf(
                    "m_alex" to PaymentStatus.PAID_VERIFIED,
                    "m_kabir" to PaymentStatus.PAID_UNVERIFIED,
                    "m_rohan" to PaymentStatus.PENDING,
                    "m_dev" to PaymentStatus.PENDING
                ),
                isGuessingOpen = false,
                actualTotalRevealed = true
            ),
            Expense(
                id = "exp_wifi_oct",
                title = "Airtel Fiber 300 Mbps Internet",
                category = ExpenseCategory.WIFI,
                amount = 1499.0,
                dueDate = "Oct 02, 2026",
                splitType = SplitType.EQUAL,
                splitMap = mapOf(
                    "m_alex" to 374.75,
                    "m_kabir" to 374.75,
                    "m_rohan" to 374.75,
                    "m_dev" to 374.75
                ),
                statusMap = mapOf(
                    "m_alex" to PaymentStatus.PAID_VERIFIED,
                    "m_kabir" to PaymentStatus.PAID_VERIFIED,
                    "m_rohan" to PaymentStatus.PAID_UNVERIFIED,
                    "m_dev" to PaymentStatus.PENDING
                ),
                isGuessingOpen = false,
                actualTotalRevealed = true
            ),
            Expense(
                id = "exp_eb_oct",
                title = "BESCOM Electricity (Guess the Bill!)",
                category = ExpenseCategory.ELECTRICITY,
                amount = 2640.0,
                dueDate = "Oct 10, 2026",
                splitType = SplitType.EQUAL,
                splitMap = mapOf(
                    "m_alex" to 660.0,
                    "m_kabir" to 660.0,
                    "m_rohan" to 660.0,
                    "m_dev" to 660.0
                ),
                statusMap = mapOf(
                    "m_alex" to PaymentStatus.PENDING,
                    "m_kabir" to PaymentStatus.PENDING,
                    "m_rohan" to PaymentStatus.PENDING,
                    "m_dev" to PaymentStatus.PENDING
                ),
                billGuesses = mapOf(
                    "m_kabir" to 2500.0,
                    "m_rohan" to 2800.0
                ),
                isGuessingOpen = true,
                actualTotalRevealed = false
            ),
            Expense(
                id = "exp_grocery_sept",
                title = "Blinkit Big Grocery Restock",
                category = ExpenseCategory.GROCERY,
                amount = 3480.0,
                dueDate = "Sep 20, 2026",
                splitType = SplitType.EQUAL,
                splitMap = mapOf(
                    "m_alex" to 870.0,
                    "m_kabir" to 870.0,
                    "m_rohan" to 870.0,
                    "m_dev" to 870.0
                ),
                statusMap = mapOf(
                    "m_alex" to PaymentStatus.PAID_VERIFIED,
                    "m_kabir" to PaymentStatus.PAID_VERIFIED,
                    "m_rohan" to PaymentStatus.PAID_VERIFIED,
                    "m_dev" to PaymentStatus.PAID_VERIFIED
                ),
                isGuessingOpen = false,
                actualTotalRevealed = true
            )
        )
    )
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    // Bill comments
    private val _comments = MutableStateFlow<Map<String, List<Comment>>>(
        mapOf(
            "exp_rent_oct" to listOf(
                Comment("c_1", "exp_rent_oct", "m_kabir", "Kabir Roy", "K", "Transferred ₹8,000 via GPay! Check your HDFC statement bro.", System.currentTimeMillis() - 7200000),
                Comment("c_2", "exp_rent_oct", "m_alex", "Alex Mercer", "A", "Got it Kabir! Will verify on the dashboard.", System.currentTimeMillis() - 3600000)
            ),
            "exp_eb_oct" to listOf(
                Comment("c_3", "exp_eb_oct", "m_rohan", "Rohan Verma", "R", "Submitted my guess as ₹2,800. AC was running 8 hours every night 😂", System.currentTimeMillis() - 5400000),
                Comment("c_4", "exp_eb_oct", "m_dev", "Dev Mehta", "D", "No way, it was raining last week. I guess around ₹2,600.", System.currentTimeMillis() - 1800000)
            )
        )
    )
    val comments: StateFlow<Map<String, List<Comment>>> = _comments.asStateFlow()

    // Game Scores & Points
    private val _memberPoints = MutableStateFlow<Map<String, Int>>(
        mapOf(
            "m_alex" to 220,
            "m_kabir" to 195,
            "m_rohan" to 140,
            "m_dev" to 85 // Lowest scorer = Chhota Bhai!
        )
    )

    private val _scoreHistory = MutableStateFlow(
        listOf(
            ScoreLog("sl_1", "Ludo King Tournament", 50, System.currentTimeMillis() - 86400000, "Alex Mercer", "Won 4-player final round"),
            ScoreLog("sl_2", "On-Time Payment Streak", 25, System.currentTimeMillis() - 64800000, "Kabir Roy", "Paid Sept Wifi before due date"),
            ScoreLog("sl_3", "FIFA 24 Weekend Clash", 50, System.currentTimeMillis() - 43200000, "Alex Mercer", "Real Madrid vs Bayern 3-1"),
            ScoreLog("sl_4", "Guess the Bill (Grocery)", 40, System.currentTimeMillis() - 21600000, "Kabir Roy", "Guessed ₹3,500 (Actual: ₹3,480)")
        )
    )
    val scoreHistory: StateFlow<List<ScoreLog>> = _scoreHistory.asStateFlow()

    // Group Chat Messages
    private val _chatMessages = MutableStateFlow(
        listOf(
            ChatMessage("msg_1", "system", "RoomieBot", "🤖", "🏠 Welcome to Flat 4B - Silicon Babs! Real-time group chat active.", System.currentTimeMillis() - 86400000, isSystemAnnouncement = true),
            ChatMessage("msg_2", "m_alex", "Alex Mercer", "A", "Rent slip for this month has been created. Please clear before 5th Oct!", System.currentTimeMillis() - 36000000),
            ChatMessage("msg_3", "m_kabir", "Kabir Roy", "K", "I just sent ₹8,000 on your UPI. Marked as 'Paid – unverified'.", System.currentTimeMillis() - 25000000),
            ChatMessage("msg_4", "m_rohan", "Rohan Verma", "R", "Who has the spare flat keys? Left mine inside the bedroom 🔑", System.currentTimeMillis() - 14000000),
            ChatMessage("msg_5", "m_dev", "Dev Mehta", "D", "Under the shoe rack outside bro! Don't lose it.", System.currentTimeMillis() - 10000000),
            ChatMessage("msg_6", "system", "RoomieBot", "👶", "🎲 Weekly Update: Dev has been tagged as this week's Chhota Bhai! Dare: Makes morning ginger chai for everyone ☕", System.currentTimeMillis() - 7200000, isSystemAnnouncement = true)
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    // Leaderboard calculation
    val leaderboard = MutableStateFlow<List<LeaderboardEntry>>(emptyList())

    init {
        recalculateLeaderboard()
    }

    private fun recalculateLeaderboard() {
        val currentPts = _memberPoints.value
        val activeMems = _members.value.filter { it.isActive }
        if (activeMems.isEmpty()) return

        // Sort by points descending
        val sortedMembers = activeMems.sortedByDescending { currentPts[it.id] ?: 0 }
        val lowestScoreMember = sortedMembers.lastOrNull()

        val list = sortedMembers.mapIndexed { index, mem ->
            val pts = currentPts[mem.id] ?: 0
            val isChhota = (mem.id == lowestScoreMember?.id)
            val history = _scoreHistory.value.filter { it.winnerName == mem.name }
            LeaderboardEntry(
                memberId = mem.id,
                memberName = mem.name,
                avatarInitial = mem.avatarInitial,
                points = pts,
                onTimeStreak = if (index == 0) 3 else (2 - index).coerceAtLeast(0),
                isChhotaBhai = isChhota,
                history = history
            )
        }
        leaderboard.value = list

        // Update assigned dare to lowest member
        if (lowestScoreMember != null) {
            val curr = _currentDare.value
            if (curr != null && curr.assignedToMemberId != lowestScoreMember.id) {
                _currentDare.value = curr.copy(assignedToMemberId = lowestScoreMember.id)
            }
        }
    }

    // Active User Switcher (for testing and multi-roommate UX)
    fun switchActiveUser(memberId: String) {
        _activeUserId.value = memberId
    }

    fun getActiveMember(): Member {
        val currentId = _activeUserId.value
        return _members.value.find { it.id == currentId } ?: _members.value.first()
    }

    // Reroll dare ("Spin Again")
    fun spinDareAgain(): Dare? {
        val activeDares = _dares.value.filter { it.active }
        if (activeDares.isEmpty()) return null
        val current = _currentDare.value
        val candidates = activeDares.filter { it.id != current?.id }
        val picked = if (candidates.isNotEmpty()) candidates.random() else activeDares.first()
        val lowestMember = leaderboard.value.find { it.isChhotaBhai }
        val newDare = picked.copy(
            assignedToMemberId = lowestMember?.memberId,
            isCompleted = false
        )
        _currentDare.value = newDare

        // Post to chat
        val activeUser = getActiveMember()
        sendChatMessage(
            text = "🎲 ${activeUser.name} spun the wheel! New Dare for Chhota Bhai (${lowestMember?.memberName ?: "Roommate"}): \"${newDare.text}\"",
            isSystem = true
        )
        return newDare
    }

    fun toggleDareDone() {
        val curr = _currentDare.value ?: return
        val updated = curr.copy(isCompleted = !curr.isCompleted)
        _currentDare.value = updated
        if (updated.isCompleted) {
            val mem = _members.value.find { it.id == curr.assignedToMemberId }
            sendChatMessage(
                text = "👏 Chhota Bhai ${mem?.name ?: ""} successfully completed the dare: \"${curr.text}\"!",
                isSystem = true
            )
        }
    }

    fun addPreApprovedDare(text: String, emoji: String) {
        val newDare = Dare(
            id = "d_" + UUID.randomUUID().toString().take(6),
            text = text,
            emoji = emoji,
            active = true
        )
        _dares.value = _dares.value + newDare
    }

    fun deleteDare(dareId: String) {
        _dares.value = _dares.value.filter { it.id != dareId }
    }

    // Payment Flow
    fun markPaidUnverified(expenseId: String, memberId: String) {
        val expList = _expenses.value.toMutableList()
        val index = expList.indexOfFirst { it.id == expenseId }
        if (index != -1) {
            val expense = expList[index]
            val updatedStatusMap = expense.statusMap.toMutableMap()
            updatedStatusMap[memberId] = PaymentStatus.PAID_UNVERIFIED
            expList[index] = expense.copy(statusMap = updatedStatusMap)
            _expenses.value = expList

            val member = _members.value.find { it.id == memberId }
            sendChatMessage(
                text = "💸 ${member?.name ?: "Roommate"} marked their share of ₹${expense.splitMap[memberId] ?: 0.0} for '${expense.title}' as 'Paid – unverified'.",
                isSystem = true
            )
        }
    }

    fun verifyPayment(expenseId: String, memberId: String) {
        val expList = _expenses.value.toMutableList()
        val index = expList.indexOfFirst { it.id == expenseId }
        if (index != -1) {
            val expense = expList[index]
            val updatedStatusMap = expense.statusMap.toMutableMap()
            updatedStatusMap[memberId] = PaymentStatus.PAID_VERIFIED
            expList[index] = expense.copy(statusMap = updatedStatusMap)
            _expenses.value = expList

            // Award on-time payment bonus points
            val member = _members.value.find { it.id == memberId }
            val currentPts = _memberPoints.value.toMutableMap()
            val oldPts = currentPts[memberId] ?: 0
            currentPts[memberId] = oldPts + 25 // +25 bonus points for prompt payment
            _memberPoints.value = currentPts

            val log = ScoreLog(
                id = UUID.randomUUID().toString(),
                sourceOrGame = "On-time Payment Streak",
                points = 25,
                timestamp = System.currentTimeMillis(),
                winnerName = member?.name ?: "Roommate",
                details = "Verified for ${expense.title}"
            )
            _scoreHistory.value = listOf(log) + _scoreHistory.value
            recalculateLeaderboard()

            val admin = getActiveMember()
            sendChatMessage(
                text = "✅ ${admin.name} verified payment of ₹${expense.splitMap[memberId] ?: 0.0} from ${member?.name ?: "Roommate"} for '${expense.title}' (+25 points awarded)!",
                isSystem = true
            )
        }
    }

    // Expense Management
    fun addExpense(
        title: String,
        category: ExpenseCategory,
        amount: Double,
        dueDate: String,
        splitType: SplitType,
        customSplits: Map<String, Double>? = null,
        isGuessingOpen: Boolean = false
    ) {
        val activeMems = _members.value.filter { it.isActive }
        val splitMap = mutableMapOf<String, Double>()
        val statusMap = mutableMapOf<String, PaymentStatus>()

        if (splitType == SplitType.EQUAL && activeMems.isNotEmpty()) {
            val share = (amount / activeMems.size * 100).toLong() / 100.0
            activeMems.forEach {
                splitMap[it.id] = share
                statusMap[it.id] = PaymentStatus.PENDING
            }
        } else if (customSplits != null) {
            customSplits.forEach { (mId, amt) ->
                splitMap[mId] = amt
                statusMap[mId] = PaymentStatus.PENDING
            }
        }

        val newExpense = Expense(
            id = "exp_" + UUID.randomUUID().toString().take(8),
            title = title,
            category = category,
            amount = amount,
            dueDate = dueDate,
            splitType = splitType,
            splitMap = splitMap,
            statusMap = statusMap,
            isGuessingOpen = isGuessingOpen,
            actualTotalRevealed = !isGuessingOpen
        )

        _expenses.value = listOf(newExpense) + _expenses.value

        val guessNotice = if (isGuessingOpen) " 🎯 'Guess the Bill' contest is OPEN! Guess closest to win +40 pts!" else ""
        sendChatMessage(
            text = "⚡ New Expense Slip: '${title}' (${category.emoji} ${category.label}) total ₹${amount.toInt()}. Due: $dueDate.$guessNotice",
            isSystem = true
        )
    }

    // Guess the Bill Flow
    fun submitBillGuess(expenseId: String, guessAmount: Double) {
        val expList = _expenses.value.toMutableList()
        val index = expList.indexOfFirst { it.id == expenseId }
        val currentUserId = _activeUserId.value
        if (index != -1) {
            val expense = expList[index]
            val updatedGuesses = expense.billGuesses.toMutableMap()
            updatedGuesses[currentUserId] = guessAmount
            expList[index] = expense.copy(billGuesses = updatedGuesses)
            _expenses.value = expList

            val member = getActiveMember()
            sendChatMessage(
                text = "🎯 ${member.name} submitted their guess of ₹${guessAmount.toInt()} for '${expense.title}'!",
                isSystem = true
            )
        }
    }

    fun revealBillAndAwardPoints(expenseId: String) {
        val expList = _expenses.value.toMutableList()
        val index = expList.indexOfFirst { it.id == expenseId }
        if (index != -1) {
            val expense = expList[index]
            expList[index] = expense.copy(actualTotalRevealed = true, isGuessingOpen = false)
            _expenses.value = expList

            // Find closest guess
            if (expense.billGuesses.isNotEmpty()) {
                var bestMemberId: String? = null
                var minDiff = Double.MAX_VALUE

                expense.billGuesses.forEach { (mId, guess) ->
                    val diff = abs(expense.amount - guess)
                    if (diff < minDiff) {
                        minDiff = diff
                        bestMemberId = mId
                    }
                }

                if (bestMemberId != null) {
                    val winner = _members.value.find { it.id == bestMemberId }
                    val currentPts = _memberPoints.value.toMutableMap()
                    val old = currentPts[bestMemberId!!] ?: 0
                    currentPts[bestMemberId!!] = old + 40
                    _memberPoints.value = currentPts

                    val log = ScoreLog(
                        id = UUID.randomUUID().toString(),
                        sourceOrGame = "Guess the Bill Winner",
                        points = 40,
                        timestamp = System.currentTimeMillis(),
                        winnerName = winner?.name ?: "Roommate",
                        details = "Guessed ₹${expense.billGuesses[bestMemberId]?.toInt()} on ₹${expense.amount.toInt()} bill"
                    )
                    _scoreHistory.value = listOf(log) + _scoreHistory.value
                    recalculateLeaderboard()

                    sendChatMessage(
                        text = "🏆 Bill revealed for '${expense.title}' (₹${expense.amount.toInt()})! Closest guess: ${winner?.name ?: "Roommate"} (₹${expense.billGuesses[bestMemberId]?.toInt()}) won +40 Fun Zone points!",
                        isSystem = true
                    )
                }
            }
        }
    }

    // Bill comments
    fun addComment(expenseId: String, text: String) {
        if (text.isBlank()) return
        val currentSender = getActiveMember()
        val newComment = Comment(
            id = "c_" + UUID.randomUUID().toString().take(6),
            expenseId = expenseId,
            senderId = currentSender.id,
            senderName = currentSender.name,
            senderInitial = currentSender.avatarInitial,
            text = text.trim()
        )
        val currentMap = _comments.value.toMutableMap()
        val existingList = currentMap[expenseId] ?: emptyList()
        currentMap[expenseId] = existingList + newComment
        _comments.value = currentMap
    }

    // Log Game Score
    fun logGameScore(gameName: String, winnerMemberId: String, points: Int = 50) {
        val winner = _members.value.find { it.id == winnerMemberId } ?: return
        val currentPts = _memberPoints.value.toMutableMap()
        val oldPts = currentPts[winnerMemberId] ?: 0
        currentPts[winnerMemberId] = oldPts + points
        _memberPoints.value = currentPts

        val log = ScoreLog(
            id = UUID.randomUUID().toString(),
            sourceOrGame = gameName,
            points = points,
            timestamp = System.currentTimeMillis(),
            winnerName = winner.name,
            details = "Logged by ${getActiveMember().name}"
        )
        _scoreHistory.value = listOf(log) + _scoreHistory.value
        recalculateLeaderboard()

        sendChatMessage(
            text = "🎮 Game Result: ${winner.name} won at '$gameName' and scored +$points points!",
            isSystem = true
        )
    }

    // Chat
    fun sendChatMessage(text: String, isSystem: Boolean = false) {
        if (text.isBlank()) return
        val active = getActiveMember()
        val msg = ChatMessage(
            id = "msg_" + UUID.randomUUID().toString().take(8),
            senderId = if (isSystem) "system" else active.id,
            senderName = if (isSystem) "RoomieBot" else active.name,
            avatarInitial = if (isSystem) "🤖" else active.avatarInitial,
            text = text.trim(),
            timestamp = System.currentTimeMillis(),
            isSystemAnnouncement = isSystem
        )
        _chatMessages.value = _chatMessages.value + msg
    }

    // Admin & Flat Management
    fun createNewFlat(flatName: String, adminUpiId: String, adminName: String) {
        val newCode = "FLAT" + Random.nextInt(100, 999)
        val newFlat = Flat(
            id = "flat_" + UUID.randomUUID().toString().take(6),
            name = flatName.ifBlank { "My Flat" },
            inviteCode = newCode,
            upiId = adminUpiId.ifBlank { "admin@upi" }
        )
        _flat.value = newFlat
        val adminMember = Member(
            id = "m_admin",
            name = adminName.ifBlank { "Flat Admin" },
            role = Role.ADMIN,
            avatarInitial = (adminName.firstOrNull() ?: 'A').uppercase(),
            colorHex = 0xFF1B4B43
        )
        _members.value = listOf(adminMember)
        _activeUserId.value = adminMember.id
        _expenses.value = emptyList()
        _comments.value = emptyMap()
        _memberPoints.value = mapOf(adminMember.id to 100)
        recalculateLeaderboard()

        sendChatMessage(
            text = "🎉 Welcome to '${newFlat.name}'! Invite code is ${newFlat.inviteCode}. Share it with your flatmates.",
            isSystem = true
        )
    }

    fun joinFlat(inviteCode: String, newMemberName: String, phone: String) {
        val code = inviteCode.trim().uppercase()
        val newId = "m_" + UUID.randomUUID().toString().take(6)
        val newMember = Member(
            id = newId,
            name = newMemberName.ifBlank { "New Roomie" },
            role = Role.MEMBER,
            avatarInitial = (newMemberName.firstOrNull() ?: 'R').uppercase(),
            phone = phone,
            colorHex = 0xFFE8A33D
        )
        _members.value = _members.value + newMember
        _activeUserId.value = newId

        val pts = _memberPoints.value.toMutableMap()
        pts[newId] = 50
        _memberPoints.value = pts
        recalculateLeaderboard()

        sendChatMessage(
            text = "👋 Everyone say hi! ${newMember.name} just joined the flat using code $code.",
            isSystem = true
        )
    }

    fun promoteToCoAdmin(memberId: String) {
        _members.value = _members.value.map {
            if (it.id == memberId) it.copy(role = Role.CO_ADMIN) else it
        }
        val mem = _members.value.find { it.id == memberId }
        sendChatMessage(
            text = "🎖️ ${mem?.name ?: "Roommate"} has been promoted to Co-Admin by ${getActiveMember().name}!",
            isSystem = true
        )
    }

    fun removeMember(memberId: String) {
        val mem = _members.value.find { it.id == memberId }
        // Keep historical records, mark inactive
        _members.value = _members.value.map {
            if (it.id == memberId) it.copy(isActive = false) else it
        }
        recalculateLeaderboard()
        sendChatMessage(
            text = "🚪 ${mem?.name ?: "Roommate"} has moved out. Their past expense records remain archived.",
            isSystem = true
        )
    }

    fun regenerateInviteCode() {
        val newCode = "RM" + Random.nextInt(100, 999)
        _flat.value = _flat.value.copy(inviteCode = newCode)
        sendChatMessage(
            text = "🔑 New flat invite code generated: $newCode",
            isSystem = true
        )
    }

    fun updateUpiId(newUpiId: String) {
        _flat.value = _flat.value.copy(upiId = newUpiId.trim())
    }
}
