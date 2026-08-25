package com.example.androidapp2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/** Contact screen showing the same location, hours, email, classes, and maps action as iOS. */
class ContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        // Open the coffee shop in the user's installed map application.
        findViewById<Button>(R.id.openMapsButton).setOnClickListener {
            val location = Uri.parse("geo:43.4516,-80.4925?q=247+Roasters+Street,+Waterloo,+ON")
            val mapIntent = Intent(Intent.ACTION_VIEW, location)
            if (mapIntent.resolveActivity(packageManager) != null) startActivity(mapIntent)
        }

        // Navigation mirrors the three tabs available in the iOS version.
        findViewById<Button>(R.id.menuButton).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
        findViewById<Button>(R.id.aboutButton).setOnClickListener { startActivity(Intent(this, AboutActivity::class.java)) }
        findViewById<Button>(R.id.contactButton).isEnabled = false
    }
}
