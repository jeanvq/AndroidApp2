package com.example.androidapp2

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

/** Main menu screen. Recreates the iOS menu, quantity controls, total, and order confirmation. */
class MainActivity : AppCompatActivity() {
    /** Simple model for one Origin & Ember menu item. */
    data class MenuItem(val name: String, val description: String, val price: Double, val category: String)

    private val menuItems = listOf(
        MenuItem("Ember Blend", "Signature medium roast with notes of dark chocolate, caramel, and a hint of smokiness.", 4.50, "Featured Roasts"),
        MenuItem("Origin Guatemala", "Single-origin from Huehuetenango with citrus and floral undertones.", 5.25, "Featured Roasts"),
        MenuItem("Cold Brew Reserve", "Cold-brewed for 18 hours; smooth and naturally sweet.", 5.75, "Cold Drinks"),
        MenuItem("French Vanilla Latte", "Smooth espresso with steamed milk and sweet vanilla.", 5.00, "Hot Drinks"),
        MenuItem("Iced Caramel Macchiato", "Espresso over cold milk and ice with caramel drizzle.", 5.50, "Cold Drinks")
    )
    private val quantities = MutableList(menuItems.size) { 0 }
    private lateinit var menuContainer: LinearLayout
    private lateinit var totalSection: View
    private lateinit var totalText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        menuContainer = findViewById(R.id.menuContainer)
        totalSection = findViewById(R.id.totalSection)
        totalText = findViewById(R.id.totalText)
        buildMenu()
        updateTotal()

        // Bottom navigation mirrors the Menu, About, and Contact tabs from iOS.
        findViewById<Button>(R.id.menuButton).isEnabled = false
        findViewById<Button>(R.id.aboutButton).setOnClickListener { startActivity(Intent(this, AboutActivity::class.java)) }
        findViewById<Button>(R.id.contactButton).setOnClickListener { startActivity(Intent(this, ContactActivity::class.java)) }

        // Confirm the order and reset all quantities afterward.
        findViewById<Button>(R.id.placeOrderButton).setOnClickListener {
            AlertDialog.Builder(this).setTitle("Order Placed!")
                .setMessage("Your order has been placed. See you soon! ☕")
                .setPositiveButton("OK") { _, _ ->
                    quantities.indices.forEach { quantities[it] = 0 }
                    buildMenu()
                    updateTotal()
                }.show()
        }
    }

    /** Builds category headings and product cards with standard Android Views. */
    private fun buildMenu() {
        menuContainer.removeAllViews()
        menuItems.withIndex().groupBy { it.value.category }.toSortedMap().forEach { (category, items) ->
            menuContainer.addView(TextView(this).apply {
                text = category; textSize = 23f; setTypeface(null, Typeface.BOLD)
                setTextColor(Color.rgb(187, 112, 59)); setPadding(dp(16), dp(24), dp(16), dp(10))
            })
            items.forEach { menuContainer.addView(createMenuCard(it.index, it.value)) }
        }
    }

    /** Creates one product card with description, price, and 0–10 quantity controls. */
    private fun createMenuCard(index: Int, item: MenuItem): View {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL; setPadding(dp(16), dp(16), dp(16), dp(16))
            background = GradientDrawable().apply { setColor(Color.rgb(250, 248, 245)); cornerRadius = dp(12).toFloat(); setStroke(dp(1), Color.rgb(225, 218, 210)) }
            layoutParams = LinearLayout.LayoutParams(-1, -2).apply { setMargins(dp(16), dp(6), dp(16), dp(8)) }
        }
        card.addView(TextView(this).apply { text = "${item.name}     $${"%.2f".format(item.price)}"; textSize = 18f; setTypeface(null, Typeface.BOLD); setTextColor(Color.rgb(45, 30, 20)) })
        card.addView(TextView(this).apply { text = item.description; textSize = 14f; setTextColor(Color.DKGRAY); setPadding(0, dp(6), 0, dp(10)) })
        val row = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL }
        row.addView(TextView(this).apply { text = "Quantity:"; textSize = 15f; layoutParams = LinearLayout.LayoutParams(0, -2, 1f) })
        val minus = Button(this).apply { text = "−" }
        val count = TextView(this).apply { text = quantities[index].toString(); textSize = 18f; gravity = Gravity.CENTER; layoutParams = LinearLayout.LayoutParams(dp(44), dp(48)) }
        val plus = Button(this).apply { text = "+" }
        // Keep the same 0...10 range as the iOS Stepper.
        minus.setOnClickListener { if (quantities[index] > 0) { quantities[index]--; count.text = quantities[index].toString(); updateTotal() } }
        plus.setOnClickListener { if (quantities[index] < 10) { quantities[index]++; count.text = quantities[index].toString(); updateTotal() } }
        row.addView(minus); row.addView(count); row.addView(plus); card.addView(row)
        return card
    }

    /** Recalculates the order total and only shows checkout when an item is selected. */
    private fun updateTotal() {
        val total = menuItems.indices.sumOf { menuItems[it].price * quantities[it] }
        totalText.text = "$${"%.2f".format(total)}"
        totalSection.visibility = if (total > 0) View.VISIBLE else View.GONE
    }

    /** Converts dp values to pixels for dynamically created views. */
    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
