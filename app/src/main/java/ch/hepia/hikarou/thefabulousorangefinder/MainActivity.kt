package ch.hepia.hikarou.thefabulousorangefinder


import CurrentGame
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get the game state
        CurrentGame.init(this@MainActivity)

        startActivity(Intent(this@MainActivity, CarteActivity::class.java))

        // Should not be opened again on back
        finish()
    }
}
