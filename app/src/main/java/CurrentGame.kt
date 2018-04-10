import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.widget.Toast
import ch.hepia.hikarou.thefabulousorangefinder.Game
import java.io.*

object CurrentGame {
    private var inited = false
    private lateinit var curGame: Game
    private var finished = false
    private const val gameFilename = "savedGames"
    private val gamesState: ArrayList<Game> = ArrayList(0)

    fun init(context: Context) {
        if (inited) return

        inited = true
        try {
            ObjectInputStream(context.openFileInput(gameFilename)).use { it ->
                val restedFamily = it.readObject()
                when (restedFamily) {
                    is ArrayList<*> -> if (restedFamily[0] is Game) gamesState.add(restedFamily[0] as Game)
                    else -> println("Deserialization failed")
                }
            } // close not needed with use of use{}
        } catch (ex: Exception) {
            when (ex) {
                is FileNotFoundException -> {
                    createNewGame("Toto", DifferentGames.firstGame, context)
                }
                else -> throw ex
            }
        }

        CurrentGame.setCurGame(gamesState.last())
    }

    private fun setCurGame(game: Game) {
        curGame = game
    }

    fun createNewGame(name: String, tags: Array<String>, context: Context) {
        inited = true
        finished = false
        curGame = Game(name, tags)
        gamesState.add(curGame)
        ObjectOutputStream(context.openFileOutput(gameFilename, Context.MODE_PRIVATE)).use { it -> it.writeObject(gamesState) }
    }

    fun getCurStep(): Int {
        return if (inited) curGame.currentStep else -1
    }

    private fun nextTag(): String {
        return curGame.gameTags[curGame.currentStep]
    }

    fun processIntent(checkIntent: Intent, context: Context) {
        // Check if intent has the action of a discovered NFC tag
        // with NDEF formatted contents
        if (inited && !finished && checkIntent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {

            val tag: Tag = checkIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG)

            if (byteArrayToHexString(tag.id) == nextTag()) {
                Toast.makeText(context,
                        "Bien joué! Tu as trouvé le bon tag pour cette étape !",
                        Toast.LENGTH_LONG).show()
                curGame.currentStep++

                finished = curGame.currentStep == curGame.gameTags.size

                if (finished)
                    Toast.makeText(context,
                            "Bravo! Tu as trouvé le dernier tag !",
                            Toast.LENGTH_LONG).show()

                gamesState[gamesState.size - 1] = curGame

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