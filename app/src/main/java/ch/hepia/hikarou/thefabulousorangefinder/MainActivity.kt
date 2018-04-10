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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Layout
        val txt = findViewById<TextView>(R.id.textView)
        txt.typeface = ResourcesCompat.getFont(this.applicationContext, R.font.font)

        val btNewGame = findViewById<Button>(R.id.button)
        btNewGame.visibility = View.INVISIBLE
        btNewGame?.setOnClickListener {
            // TODO créer une nouvelle partie :)
            startActivity(Intent(this@MainActivity, CarteActivity::class.java))
        }

        val btContinue = findViewById<Button>(R.id.button2)
        btContinue?.setOnClickListener {
            // get the game state
            CurrentGame.init(this@MainActivity)
            startActivity (Intent(this@MainActivity, CarteActivity::class.java))
        }



        if (intent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
            CurrentGame.processIntent(intent, this@MainActivity)

            startActivity(Intent(this@MainActivity, CarteActivity::class.java))
            startActivity(Intent(this@MainActivity, EnigmeActivity::class.java))
        }
    }

    private var exit = false
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
