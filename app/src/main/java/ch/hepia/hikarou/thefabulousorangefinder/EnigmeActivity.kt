package ch.hepia.hikarou.thefabulousorangefinder

import CurrentGame
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import LoopMediaPlayer

class EnigmeActivity : AppCompatActivity() {

    lateinit var mediaPlayer: LoopMediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enigme2)

        mediaPlayer = LoopMediaPlayer.create(this, R.raw.engime, true)

        // Layout
        val img = findViewById<View>(R.id.imageView2) as ImageView

        // Picture handling and customisation
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
            else -> R.drawable.enigme_final
        }

        img.setImageResource(enigme)
    }


    /**
     * Handles the back button press, get back to carte
     */
    override fun onBackPressed() {
        startActivity(Intent(this@EnigmeActivity, CarteActivity::class.java))
        this.finish()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.stop()

    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
    }
}
