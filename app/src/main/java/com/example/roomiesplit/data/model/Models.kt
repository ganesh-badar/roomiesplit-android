package com.example.roomiesplit.data.model

data class Flat(
    val id: String,
    val name: String,
    val inviteCode: String,
    val upiId: String,
    val createdAt: Long = System.currentTimeMillis()
)

data class Member(
    val id: String,
    val name: String,
    val role: Role,
    val avatarInitial: String,
    val phone: String = "",
    val colorHex: Long = 0xFF1B4B43,
    val isActive: Boolean = true
)

data class Expense(
    val id: String,
    val title: String,
    val category: ExpenseCategory,
    val amount: Double,
    val dueDate: String,
    val splitType: SplitType = SplitType.EQUAL,
    val splitMap: Map<String, Double> = emptyMap(), // memberId -> amount
    val statusMap: Map<String, PaymentStatus> = emptyMap(), // memberId -> PaymentStatus
    val photoUrl: String? = null,
    val billGuesses: Map<String, Double> = emptyMap(), // memberId -> guessAmount
    val isGuessingOpen: Boolean = false,
    val actualTotalRevealed: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

data class Comment(
    val id: String,
    val expenseId: String,
    val senderId: String,
    val senderName: String,
    val senderInitial: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class ScoreLog(
    val id: String,
    val sourceOrGame: String,
    val points: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val winnerName: String,
    val details: String = ""
)

data class LeaderboardEntry(
    val memberId: String,
    val memberName: String,
    val avatarInitial: String,
    val points: Int,
    val onTimeStreak: Int = 0,
    val isChhotaBhai: Boolean = false,
    val history: List<ScoreLog> = emptyList()
)

data class Dare(
    val id: String,
    val text: String,
    val emoji: String = "🎲",
    val active: Boolean = true,
    val assignedToMemberId: String? = null,
    val isCompleted: Boolean = false
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val senderName: String,
    val avatarInitial: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isSystemAnnouncement: Boolean = false
)
