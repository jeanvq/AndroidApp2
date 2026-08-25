package com.example.androidapp2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/** About screen containing the same story, craft, and brand values as the iOS app. */
class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // Navigation keeps the same three primary destinations available on every screen.
        findViewById<Button>(R.id.menuButton).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
        findViewById<Button>(R.id.aboutButton).isEnabled = false
        findViewById<Button>(R.id.contactButton).setOnClickListener {
            startActivity(Intent(this, ContactActivity::class.java))
        }
    }
}
