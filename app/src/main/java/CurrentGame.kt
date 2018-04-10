import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.widget.Toast
import ch.hepia.hikarou.thefabulousorangefinder.Game

object CurrentGame {
    private lateinit var curGame : Game
    private var finished: Boolean = false

    fun setCurGame(game: Game) {
        curGame = game
    }

    fun getCurStep(): Int {
        return curGame.currentStep
    }

    fun nextTag(): String {
        return curGame.gameTags[curGame.currentStep]
    }



    fun processIntent(checkIntent: Intent, context: Context) {
        // Check if intent has the action of a discovered NFC tag
        // with NDEF formatted contents
        if (!finished && checkIntent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {

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