package ch.hepia.hikarou.thefabulousorangefinder

import CurrentGame
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
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

        // NFC support
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "Cet appareil ne supporte pas la technologie NFC",
                    Toast.LENGTH_SHORT).show()
            finish()
        } else {

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

            if (!nfcAdapter.isEnabled) {
                Toast.makeText(this, "Il faut activer le NFC avant de pouvoir jouer",
                        Toast.LENGTH_SHORT).show()
                val buttons = arrayOf(btNewGame, btContinue)
                buttons.forEach { it.visibility = View.INVISIBLE }
                btNewGame.visibility = View.INVISIBLE
                btContinue.visibility = View.INVISIBLE
                checkNfcBack(nfcAdapter, buttons)
            }


            // NFC tag handling
            if (CurrentGame.isInited() && intent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
                CurrentGame.processIntent(intent, this@MainActivity)

                startActivity(Intent(this@MainActivity, EnigmeActivity::class.java))
            }
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
            Toast.makeText(this, "Appuyer une nouvelle fois sur Retour pour sortir",
                    Toast.LENGTH_SHORT).show()
            exit = true
            Handler().postDelayed({ exit = false }, 3 * 1000)
        }

    }

    /**
     * Functions that check every 500 milliseconds if the user activated the NFC
     */
    private fun checkNfcBack(nfcAdapter: NfcAdapter, buttons: Array<Button>) {
        if (nfcAdapter.isEnabled) {
            buttons.forEach { it.visibility = View.VISIBLE }
        }
        Handler().postDelayed({ checkNfcBack(nfcAdapter, buttons) }, 5 * 100)
    }
}
