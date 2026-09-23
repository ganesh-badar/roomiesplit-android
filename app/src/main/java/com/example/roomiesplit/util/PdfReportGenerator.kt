package com.example.roomiesplit.util

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.roomiesplit.data.model.Expense
import com.example.roomiesplit.data.model.Flat
import com.example.roomiesplit.data.model.Member
import com.example.roomiesplit.data.model.PaymentStatus
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfReportGenerator {

    fun generateAndShareMonthlyReport(
        context: Context,
        flat: Flat,
        members: List<Member>,
        expenses: List<Expense>,
        monthName: String = "October 2026"
    ) {
        try {
            val pdfDoc = PdfDocument()
            val pageWidth = 595 // A4 standard width in points
            val pageHeight = 842 // A4 standard height in points
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
            val page = pdfDoc.startPage(pageInfo)
            val canvas = page.canvas

            val paint = Paint().apply {
                isAntiAlias = true
            }

            // Background
            paint.color = Color.rgb(250, 247, 242) // Warm Ivory
            canvas.drawRect(0f, 0f, pageWidth.toFloat(), pageHeight.toFloat(), paint)

            // Header Banner (Deep Teal)
            paint.color = Color.rgb(27, 75, 67)
            canvas.drawRect(0f, 0f, pageWidth.toFloat(), 110f, paint)

            // Header Texts
            paint.color = Color.rgb(232, 163, 61) // Warm Mustard accent
            paint.textSize = 14f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("ROOMIESPLIT • FLAT EXPENSE REPORT", 32f, 38f, paint)

            paint.color = Color.WHITE
            paint.textSize = 24f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(flat.name, 32f, 70f, paint)

            paint.color = Color.rgb(210, 235, 228) // Teal container tone
            paint.textSize = 13f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText("Period: $monthName • Generated on ${SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())}", 32f, 92f, paint)

            // Calculations
            var totalBilled = 0.0
            var totalCollected = 0.0
            var totalPending = 0.0

            expenses.forEach { exp ->
                totalBilled += exp.amount
                exp.splitMap.forEach { (mId, share) ->
                    val st = exp.statusMap[mId] ?: PaymentStatus.PENDING
                    if (st == PaymentStatus.PAID_VERIFIED) {
                        totalCollected += share
                    } else {
                        totalPending += share
                    }
                }
            }

            // 3 Summary Cards
            val cardY = 130f
            val cardHeight = 60f
            val cardWidth = 162f
            val margin = 32f
            val gap = 20f

            // Card 1: Total Billed
            drawSummaryCard(canvas, margin, cardY, cardWidth, cardHeight, "TOTAL BILLED", "₹${totalBilled.toInt()}", Color.rgb(27, 75, 67))

            // Card 2: Total Collected
            drawSummaryCard(canvas, margin + cardWidth + gap, cardY, cardWidth, cardHeight, "TOTAL COLLECTED", "₹${totalCollected.toInt()}", Color.rgb(40, 109, 98))

            // Card 3: Still Pending
            drawSummaryCard(canvas, margin + (cardWidth + gap) * 2, cardY, cardWidth, cardHeight, "STILL PENDING", "₹${totalPending.toInt()}", Color.rgb(216, 84, 79))

            // Table Header
            val tableTopY = 220f
            paint.color = Color.rgb(242, 236, 225)
            canvas.drawRect(margin, tableTopY, pageWidth - margin, tableTopY + 28f, paint)

            paint.color = Color.rgb(27, 75, 67)
            paint.textSize = 11f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

            canvas.drawText("BILL / CATEGORY", margin + 12f, tableTopY + 18f, paint)
            canvas.drawText("DUE DATE", margin + 180f, tableTopY + 18f, paint)
            canvas.drawText("TOTAL", margin + 270f, tableTopY + 18f, paint)
            canvas.drawText("ROOMMATE STATUS", margin + 350f, tableTopY + 18f, paint)

            // Table Rows
            var rowY = tableTopY + 36f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

            expenses.forEachIndexed { idx, exp ->
                if (rowY > pageHeight - 80f) return@forEachIndexed

                // Alternating row background
                if (idx % 2 == 1) {
                    paint.color = Color.rgb(247, 243, 235)
                    canvas.drawRect(margin, rowY - 10f, pageWidth - margin, rowY + 22f, paint)
                }

                // Title & Category
                paint.color = Color.rgb(24, 34, 31)
                paint.textSize = 11f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                val safeTitle = if (exp.title.length > 22) exp.title.take(20) + "…" else exp.title
                canvas.drawText(safeTitle, margin + 12f, rowY + 6f, paint)

                paint.color = Color.rgb(91, 110, 104)
                paint.textSize = 9f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                canvas.drawText(exp.category.label, margin + 12f, rowY + 17f, paint)

                // Due Date
                paint.color = Color.rgb(24, 34, 31)
                paint.textSize = 10f
                canvas.drawText(exp.dueDate, margin + 180f, rowY + 10f, paint)

                // Total
                paint.color = Color.rgb(27, 75, 67)
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText("₹${exp.amount.toInt()}", margin + 270f, rowY + 10f, paint)

                // Roommate status summary
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                var statusX = margin + 350f
                members.forEach { mem ->
                    val st = exp.statusMap[mem.id] ?: PaymentStatus.PENDING
                    val circlePaint = Paint().apply { isAntiAlias = true }
                    circlePaint.color = when (st) {
                        PaymentStatus.PAID_VERIFIED -> Color.rgb(27, 75, 67)
                        PaymentStatus.PAID_UNVERIFIED -> Color.rgb(232, 163, 61)
                        PaymentStatus.PENDING -> Color.rgb(216, 84, 79)
                    }
                    canvas.drawCircle(statusX + 6f, rowY + 6f, 5f, circlePaint)

                    paint.color = Color.rgb(24, 34, 31)
                    paint.textSize = 9f
                    canvas.drawText(mem.avatarInitial, statusX + 16f, rowY + 9f, paint)

                    statusX += 36f
                }

                // Row divider
                paint.color = Color.rgb(221, 213, 199)
                canvas.drawLine(margin, rowY + 22f, pageWidth - margin, rowY + 22f, paint)

                rowY += 34f
            }

            // Legend & Footer
            val footerY = pageHeight - 50f
            paint.color = Color.rgb(91, 110, 104)
            paint.textSize = 9f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText("Legend: 🟢 Paid (Verified)  🟡 Paid (Unverified)  🔴 Pending", margin + 12f, footerY - 14f, paint)
            canvas.drawText("RoomieSplit • Shared transparently with all flatmates • Admin UPI: ${flat.upiId}", margin + 12f, footerY, paint)

            pdfDoc.finishPage(page)

            // Save to cache dir
            val dir = File(context.cacheDir, "reports")
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, "RoomieSplit_${flat.name.replace(" ", "_")}_Report.pdf")
            val outputStream = FileOutputStream(file)
            pdfDoc.writeTo(outputStream)
            outputStream.flush()
            outputStream.close()
            pdfDoc.close()

            // Share via Android Intent
            sharePdfFile(context, file, "Share RoomieSplit Monthly Report")
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Could not generate PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun generateAndSharePersonalReceipt(
        context: Context,
        flat: Flat,
        member: Member,
        verifiedExpenses: List<Expense>
    ) {
        try {
            val pdfDoc = PdfDocument()
            val pageWidth = 420
            val pageHeight = 595
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
            val page = pdfDoc.startPage(pageInfo)
            val canvas = page.canvas

            val paint = Paint().apply { isAntiAlias = true }

            // Background
            paint.color = Color.rgb(250, 247, 242)
            canvas.drawRect(0f, 0f, pageWidth.toFloat(), pageHeight.toFloat(), paint)

            // Header Banner
            paint.color = Color.rgb(27, 75, 67)
            canvas.drawRect(0f, 0f, pageWidth.toFloat(), 95f, paint)

            paint.color = Color.rgb(232, 163, 61)
            paint.textSize = 12f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("OFFICIAL ROOMMATE PAYMENT RECEIPT", 24f, 32f, paint)

            paint.color = Color.WHITE
            paint.textSize = 18f
            canvas.drawText(flat.name, 24f, 58f, paint)

            paint.color = Color.rgb(210, 235, 228)
            paint.textSize = 11f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText("Issued to: ${member.name} (${member.role.label})", 24f, 78f, paint)

            // Receipt Details Box
            var totalPaid = 0.0
            verifiedExpenses.forEach { exp ->
                totalPaid += exp.splitMap[member.id] ?: 0.0
            }

            val boxY = 115f
            paint.color = Color.WHITE
            canvas.drawRoundRect(24f, boxY, pageWidth - 24f, boxY + 70f, 12f, 12f, paint)

            paint.color = Color.rgb(91, 110, 104)
            paint.textSize = 10f
            canvas.drawText("TOTAL AMOUNT PAID & VERIFIED", 38f, boxY + 28f, paint)

            paint.color = Color.rgb(27, 75, 67)
            paint.textSize = 22f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("₹${String.format("%.2f", totalPaid)}", 38f, boxY + 54f, paint)

            // Itemized list
            var itemY = boxY + 95f
            paint.color = Color.rgb(24, 34, 31)
            paint.textSize = 12f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("Verified Bill Items:", 24f, itemY, paint)

            itemY += 20f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

            verifiedExpenses.forEach { exp ->
                val myShare = exp.splitMap[member.id] ?: 0.0
                paint.color = Color.rgb(24, 34, 31)
                paint.textSize = 11f
                canvas.drawText("• ${exp.title} (${exp.category.label})", 28f, itemY, paint)

                paint.color = Color.rgb(27, 75, 67)
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText("₹${myShare.toInt()} [Verified ✓]", pageWidth - 140f, itemY, paint)

                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                itemY += 24f
            }

            // Stamp seal
            val footerY = pageHeight - 50f
            paint.color = Color.rgb(91, 110, 104)
            paint.textSize = 9f
            canvas.drawText("Payment verified by Flat Admin • UPI ID: ${flat.upiId}", 24f, footerY - 12f, paint)
            canvas.drawText("RoomieSplit Digital Payment Record • ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())}", 24f, footerY, paint)

            pdfDoc.finishPage(page)

            val dir = File(context.cacheDir, "receipts")
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, "Receipt_${member.name.replace(" ", "_")}.pdf")
            val outputStream = FileOutputStream(file)
            pdfDoc.writeTo(outputStream)
            outputStream.flush()
            outputStream.close()
            pdfDoc.close()

            sharePdfFile(context, file, "Share Payment Receipt")
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Could not generate receipt: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun drawSummaryCard(
        canvas: android.graphics.Canvas,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        label: String,
        amount: String,
        textColor: Int
    ) {
        val bgPaint = Paint().apply {
            isAntiAlias = true
            color = Color.WHITE
        }
        canvas.drawRoundRect(x, y, x + width, y + height, 8f, 8f, bgPaint)

        val borderPaint = Paint().apply {
            isAntiAlias = true
            color = Color.rgb(221, 213, 199)
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }
        canvas.drawRoundRect(x, y, x + width, y + height, 8f, 8f, borderPaint)

        val textPaint = Paint().apply {
            isAntiAlias = true
            color = Color.rgb(91, 110, 104)
            textSize = 9f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText(label, x + 10f, y + 20f, textPaint)

        textPaint.color = textColor
        textPaint.textSize = 17f
        canvas.drawText(amount, x + 10f, y + 46f, textPaint)
    }

    private fun sharePdfFile(context: Context, file: File, title: String) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(shareIntent, title).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
}
