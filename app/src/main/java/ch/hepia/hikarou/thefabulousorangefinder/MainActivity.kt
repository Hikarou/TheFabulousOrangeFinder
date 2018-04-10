package ch.hepia.hikarou.thefabulousorangefinder

import CurrentGame
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    /**
     * Will be called everytime the activity is called
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Layout
        val txt = findViewById<TextView>(R.id.textView)
        txt.typeface = ResourcesCompat.getFont(this.applicationContext, R.font.font)

        // New game button
        val btNewGame = findViewById<Button>(R.id.button)
        btNewGame?.setOnClickListener {
            CurrentGame.createNewGame("Toto", DifferentGames.firstGame, this@MainActivity)
            startActivity(Intent(this@MainActivity, CarteActivity::class.java))
        }

        // continue button
        val btContinue = findViewById<Button>(R.id.button2)
        btContinue?.setOnClickListener {
            CurrentGame.init(this@MainActivity)
            startActivity(Intent(this@MainActivity, CarteActivity::class.java))
        }

        // NFC tag handling
        if (intent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
            CurrentGame.processIntent(intent, this@MainActivity)

            startActivity(Intent(this@MainActivity, EnigmeActivity::class.java))
        }
    }

    private var exit = false
    /**
     * Handles the back button press, asks confirmation by repressing the button in the next 3 seconds
     */
    override fun onBackPressed() {
        if (exit) {
            finish()
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show()
            exit = true
            Handler().postDelayed({ exit = false }, 3 * 1000)
        }

    }
}
