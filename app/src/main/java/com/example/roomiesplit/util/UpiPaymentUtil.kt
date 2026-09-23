package com.example.roomiesplit.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object UpiPaymentUtil {

    /**
     * Builds the standard UPI deep link URI:
     * upi://pay?pa=upiId&pn=name&am=amount&tn=note&cu=INR
     */
    fun buildUpiUri(
        upiId: String,
        payeeName: String,
        amount: Double,
        note: String
    ): Uri {
        val encodedNote = Uri.encode("RoomieSplit - $note")
        val encodedName = Uri.encode(payeeName)
        val formattedAmount = String.format("%.2f", amount)
        val uriString = "upi://pay?pa=$upiId&pn=$encodedName&am=$formattedAmount&tn=$encodedNote&cu=INR"
        return Uri.parse(uriString)
    }

    /**
     * Attempts to launch an installed UPI application (GPay, PhonePe, Paytm, etc.).
     * Returns true if successfully launched, false if no UPI app was found.
     */
    fun launchUpiIntent(
        context: Context,
        upiId: String,
        payeeName: String,
        amount: Double,
        note: String
    ): Boolean {
        val uri = buildUpiUri(upiId, payeeName, amount, note)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        return try {
            val chooser = Intent.createChooser(intent, "Pay via UPI App")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun copyToClipboard(context: Context, label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "$label copied to clipboard!", Toast.LENGTH_SHORT).show()
    }
}
