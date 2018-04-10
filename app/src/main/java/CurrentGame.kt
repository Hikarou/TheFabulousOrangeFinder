import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.widget.Toast
import ch.hepia.hikarou.thefabulousorangefinder.Game
import java.io.*

/**
 * Object handling the data during the game
 * Current and older games are stored here
 */
object CurrentGame {
    private var inited = false /// Either the object is initialized
    private lateinit var curGame: Game /// The current game
    private var finished = false  /// Either this game is finished
    private const val gameFilename = "savedGames" /// Filename where the games are saved
    private val gamesState: ArrayList<Game> = ArrayList(0) /// The games retrieved from the file

    /**
     * Initializes the object
     */
    fun init(context: Context) {
        if (inited) return

        inited = true
        // Read the file
        try {
            ObjectInputStream(context.openFileInput(gameFilename)).use { it ->
                val restedFamily = it.readObject()
                when (restedFamily) {
                // If the objects read from the file corresponds
                    is ArrayList<*> -> if (restedFamily[0] is Game) gamesState.addAll(restedFamily as ArrayList<Game>) // Add them to the list
                    else -> throw FileNotFoundException()
                }
            } // close not needed with use of use{}
        } catch (ex: Exception) { // If fails, create new file
            when (ex) {
                is FileNotFoundException -> {
                    createNewGame("Toto", DifferentGames.firstGame, context)
                }
                else -> throw ex
            }
        }

        CurrentGame.setCurGame(gamesState.last())
    }

    /**
     * Set the current game
     */
    private fun setCurGame(game: Game) {
        curGame = game
    }

    /**
     * Create a new game and set it as the current game
     */
    fun createNewGame(name: String, tags: Array<String>, context: Context) {
        inited = true
        finished = false
        curGame = Game(name, tags)
        gamesState.add(curGame)
        ObjectOutputStream(context.openFileOutput(gameFilename, Context.MODE_PRIVATE)).use { it -> it.writeObject(gamesState) }
    }

    /**
     * Get the current step
     */
    fun getCurStep(): Int {
        return if (inited) curGame.currentStep else -1
    }

    /**
     * Get the next tag
     */
    private fun nextTag(): String {
        return curGame.gameTags[curGame.currentStep]
    }

    /**
     * processes the intent when called for NFC tag
     */
    fun processIntent(checkIntent: Intent, context: Context) {
        // Check if intent has the action of a discovered NFC tag
        // with NDEF formatted contents
        if (inited && !finished && checkIntent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {

            val tag: Tag = checkIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG)

            // Check if the ID of the given tag is the next step
            if (byteArrayToHexString(tag.id) == nextTag()) {
                Toast.makeText(context,
                        "Bien joué! Tu as trouvé le bon tag pour cette étape !",
                        Toast.LENGTH_LONG).show()
                curGame.currentStep++

                // Check if it is the last step
                finished = curGame.currentStep == curGame.gameTags.size

                if (finished)
                    Toast.makeText(context,
                            "Bravo! Tu as trouvé le dernier tag !",
                            Toast.LENGTH_LONG).show()


                // Update the data locally ...
                gamesState[gamesState.size - 1] = curGame

                // ... and in the file
                ObjectOutputStream(context.openFileOutput(gameFilename, Context.MODE_PRIVATE)).use { it ->
                    it.writeObject(gamesState)
                } // close not needed with use of use{}
            } else {
                Toast.makeText(context,
                        "Malheuresement, pas le bon tag !",
                        Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Helper to convert the given ByteArray in a readable String to compare with the ones in local
     */
    private fun byteArrayToHexString(inarray: ByteArray): String {
        var i: Int
        var j = 0
        var `in`: Int
        val hex = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")
        var out = ""

        while (j < inarray.size) {
            `in` = inarray[j].toInt() and 0xff
            i = `in` shr 4 and 0x0f
            out += hex[i]
            i = `in` and 0x0f
            out += hex[i]
            ++j
        }
        return out
    }
}