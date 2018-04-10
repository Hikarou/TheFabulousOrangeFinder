package ch.hepia.hikarou.thefabulousorangefinder

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import currentGame


class CarteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enigme)
        val img = findViewById<View>(R.id.imageView) as ImageView
        img.isClickable = true
        img.setOnClickListener({
            val intent = Intent(this@CarteActivity, EnigmeActivity::class.java)
            startActivity(intent)
        })

        val carte = when (currentGame.getCurStep()) {
            0    -> R.drawable.carte1
            1    -> R.drawable.carte2
            2    -> R.drawable.carte3
            3    -> R.drawable.carte4
            4    -> R.drawable.carte5
            else -> R.drawable.carte1
        }

        img.setImageResource(carte)

        if (intent != null) {
            currentGame.processIntent(intent, this@CarteActivity)

            startActivity(Intent(this@CarteActivity, EnigmeActivity::class.java))
        }
    }

}
