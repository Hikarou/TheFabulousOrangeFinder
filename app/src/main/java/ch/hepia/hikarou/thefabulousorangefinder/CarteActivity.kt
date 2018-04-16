package ch.hepia.hikarou.thefabulousorangefinder

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import CurrentGame
import LoopMediaPlayer


class CarteActivity : AppCompatActivity() {
    lateinit var mediaPlayer: LoopMediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mediaPlayer = LoopMediaPlayer.create(this, R.raw.map, true)

        // Layout
        setContentView(R.layout.activity_enigme)

        //Picture handling and customisation
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
            else -> R.drawable.carte5
        }

        img.setImageResource(carte)
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
