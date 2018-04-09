package ch.hepia.hikarou.thefabulousorangefinder

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast

class EnigmeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enigme2)
        val img = findViewById<View>(R.id.imageView2) as ImageView

        img.isClickable = true
        img.setOnClickListener(View.OnClickListener {
            Toast.makeText(this@EnigmeActivity,
                    "The favorite list would appear on clicking this icon",
                    Toast.LENGTH_LONG).show()
        })
        img.setImageResource(R.drawable.enigme1)
    }
}
