package ch.hepia.hikarou.thefabulousorangefinder


import DifferentGames
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import currentGame
import java.io.*

class MainActivity : AppCompatActivity() {
    private val _gameFilename = "savedGames"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get the game state
        val gamesState: ArrayList<Game> = ArrayList(0)
        val gameFile = File(filesDir, _gameFilename)

        if (gameFile.exists() && gameFile.isFile && gameFile.canRead() && gameFile.canWrite()) {
            ObjectInputStream(FileInputStream("$filesDir$_gameFilename")).use { it ->
                val restedFamily = it.readObject()
                when (restedFamily) {
                    is Game -> gamesState.add(gamesState.size, restedFamily)
                    else -> println("Deserialization failed")
                }
            }
        } else {
            gameFile.createNewFile()
            val testGame = Game("Toto", DifferentGames.firstGame)
            ObjectOutputStream(FileOutputStream("$filesDir$_gameFilename")).use { it -> it.writeObject(testGame) }
            gamesState.add(testGame)
        }

        currentGame.setCurGame(gamesState.last())

        val intent = Intent(this@MainActivity, CarteActivity::class.java)
        startActivity(intent)

        // Should not be opened again on back
        finish()
    }
}
