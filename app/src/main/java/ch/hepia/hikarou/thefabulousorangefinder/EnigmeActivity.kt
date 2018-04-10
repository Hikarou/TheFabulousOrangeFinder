package ch.hepia.hikarou.thefabulousorangefinder

import CurrentGame
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView

class EnigmeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enigme2)
        val img = findViewById<View>(R.id.imageView2) as ImageView

        img.isClickable = true
        img.setOnClickListener({
            this.finish()
            startActivity(Intent(this@EnigmeActivity, CarteActivity::class.java))
        })

        val enigme = when (CurrentGame.getCurStep()) {
            0 -> R.drawable.enigme1
            1 -> R.drawable.enigme2
            2 -> R.drawable.enigme3
            3 -> R.drawable.enigme4
            4 -> R.drawable.enigme5
            else -> R.drawable.enigme1
        }

        img.setImageResource(enigme)
    }

    override fun onBackPressed() {
        this.finish()
        startActivity(Intent(this@EnigmeActivity, CarteActivity::class.java))
    }
}
