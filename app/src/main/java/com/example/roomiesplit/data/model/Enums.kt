package com.example.roomiesplit.data.model

enum class Role(val label: String) {
    ADMIN("Admin"),
    CO_ADMIN("Co-Admin"),
    MEMBER("Roommate")
}

enum class ExpenseCategory(val label: String, val emoji: String) {
    RENT("Rent", "🏠"),
    ELECTRICITY("Electricity", "⚡"),
    WIFI("Wifi & Internet", "📶"),
    GROCERY("Groceries & Food", "🛒"),
    MAID_COOK("Maid & Cook", "🧹"),
    PARTY("Party & Outing", "🍕"),
    OTHER("Other Misc", "🧾")
}

enum class PaymentStatus(val label: String) {
    PENDING("Pending"),
    PAID_UNVERIFIED("Paid – unverified"),
    PAID_VERIFIED("Paid – verified")
}

enum class SplitType(val label: String) {
    EQUAL("Equal Split"),
    CUSTOM("Custom Share")
}
