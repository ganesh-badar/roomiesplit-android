package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.roomiesplit.data.model.ExpenseCategory
import com.example.roomiesplit.data.model.PaymentStatus
import com.example.roomiesplit.data.model.SplitType
import com.example.roomiesplit.data.repository.RoomieRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("RoomieSplit", appName)
    }

    @Test
    fun `test initial flat and members setup`() {
        val flat = RoomieRepository.flat.value
        assertEquals("Flat 4B - Silicon Babs", flat.name)
        assertEquals("FLAT4B", flat.inviteCode)

        val members = RoomieRepository.members.value
        assertEquals(4, members.size)
        assertEquals("Alex Mercer", members[0].name)
    }

    @Test
    fun `test two-step payment verification flow`() {
        // Mark as paid unverified
        RoomieRepository.markPaidUnverified("exp_wifi_oct", "m_dev")
        val expAfterUnverified = RoomieRepository.expenses.value.find { it.id == "exp_wifi_oct" }
        assertEquals(PaymentStatus.PAID_UNVERIFIED, expAfterUnverified?.statusMap?.get("m_dev"))

        // Admin verifies payment
        RoomieRepository.verifyPayment("exp_wifi_oct", "m_dev")
        val expAfterVerified = RoomieRepository.expenses.value.find { it.id == "exp_wifi_oct" }
        assertEquals(PaymentStatus.PAID_VERIFIED, expAfterVerified?.statusMap?.get("m_dev"))
    }

    @Test
    fun `test expense creation with equal split`() {
        RoomieRepository.addExpense(
            title = "Cook & Maid Salary",
            category = ExpenseCategory.MAID_COOK,
            amount = 4000.0,
            dueDate = "Oct 10, 2026",
            splitType = SplitType.EQUAL
        )
        val latest = RoomieRepository.expenses.value.first()
        assertEquals("Cook & Maid Salary", latest.title)
        assertEquals(1000.0, latest.splitMap["m_alex"] ?: 0.0, 0.01)
    }

    @Test
    fun `test dare spin again logic`() {
        val dare = RoomieRepository.spinDareAgain()
        assertNotNull(dare)
        val current = RoomieRepository.currentDare.value
        assertNotNull(current)
    }
}
