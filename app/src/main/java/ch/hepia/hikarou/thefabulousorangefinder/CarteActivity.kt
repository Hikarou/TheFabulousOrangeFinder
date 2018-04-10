package ch.hepia.hikarou.thefabulousorangefinder

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import CurrentGame
import android.nfc.NfcAdapter
import android.os.Handler
import android.widget.Toast


class CarteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enigme)
        val img = findViewById<View>(R.id.imageView) as ImageView
        img.isClickable = true
        img.setOnClickListener({
            this.finish()
            val intent = Intent(this@CarteActivity, EnigmeActivity::class.java)
            startActivity(intent)
        })

        val carte = when (CurrentGame.getCurStep()) {
            0 -> R.drawable.carte1
            1 -> R.drawable.carte2
            2 -> R.drawable.carte3
            3 -> R.drawable.carte4
            4 -> R.drawable.carte5
            else -> R.drawable.carte1
        }

        img.setImageResource(carte)

        if (intent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
            CurrentGame.processIntent(intent, this@CarteActivity)

            startActivity(Intent(this@CarteActivity, EnigmeActivity::class.java))

            finish()
        }
    }

}
