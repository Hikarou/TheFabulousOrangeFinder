package ch.hepia.hikarou.thefabulousorangefinder


import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.Spanned
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import kotlin.experimental.and

class MainActivity : AppCompatActivity() {
    private val _keyLogText = "logText"
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
                //We can't use <String, String> because of type erasure
                    is Game -> gamesState.add(gamesState.size,restedFamily)
                    else -> println("Deserialization failed")
                }
            }
        } else {
            gameFile.createNewFile()
            val testGame = Game("Toto", DifferentGames.firstGame)
            ObjectOutputStream(FileOutputStream("$filesDir$_gameFilename")).use { it -> it.writeObject(testGame) }
        }


        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        logMessage("NFC supported", (nfcAdapter != null).toString())
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC not supported", Toast.LENGTH_LONG).show()
        }
        logMessage("NFC enabled", (nfcAdapter?.isEnabled).toString())
        if (!nfcAdapter.isEnabled) {
            Toast.makeText(this, "NFC not enabled", Toast.LENGTH_LONG).show()
        }

        if (intent != null) {
            processIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (intent != null) {
            processIntent(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putCharSequence(_keyLogText, tv_messages.text)
        super.onSaveInstanceState(outState)
    }

    private fun logMessage(header: String, text: String?) {
        tv_messages.append(if (text.isNullOrBlank()) fromHtml("<b>$header</b><br>") else fromHtml("<b>$header</b>: $text<br>"))
        scrollDown()
    }

    private fun fromHtml(html: String): Spanned {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(html)
        }
    }

    private fun scrollDown() {
        sv_messages.post({ sv_messages.smoothScrollTo(0, sv_messages.bottom) })
    }

    private fun processIntent(checkIntent: Intent) {
        // Check if intent has the action of a discovered NFC tag
        // with NDEF formatted contents
        if (checkIntent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {

            val tag: Tag = checkIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG)

            // Retrieve the raw NDEF message from the tag
            val rawMessages = checkIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)

            val ndefMsg = rawMessages[0] as NdefMessage

            val ndefRecord = ndefMsg.records[0]

            val mime = ndefRecord.toMimeType()

            if (mime != null) {
                //https://dzone.com/articles/nfc-android-read-ndef-tag
                logMessage("Text detected", mime.toString())

                val status = ndefRecord.payload[0]

                val ianaLength = status and 0x3F // Bit mask bit 5..0
                try {
                    val content = String(ndefRecord.payload, ianaLength + 1, ndefRecord.payload.size - 1 - ianaLength)
                    logMessage("Content", content)
                } catch (t: Throwable) {
                    t.printStackTrace()
                }


            } else {
                // Other NFC Tags
                logMessage("Payload", ndefRecord.payload!!.contentToString())
            }
            logMessage("ID", ByteArrayToHexString(tag.id))
        }




    }

    private fun ByteArrayToHexString(inarray: ByteArray): String {
        var i: Int
        var j: Int
        var `in`: Int
        val hex = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")
        var out = ""

        j = 0
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
