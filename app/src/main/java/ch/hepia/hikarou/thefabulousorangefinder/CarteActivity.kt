package ch.hepia.hikarou.thefabulousorangefinder

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import currentGame


class CarteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enigme)
        val img = findViewById<View>(R.id.imageView) as ImageView
        img.isClickable = true
        img.setOnClickListener({
            val intent = Intent(this@CarteActivity, EnigmeActivity::class.java)
            startActivity(intent)
        })

        val carte = when (currentGame.getCurStep()) {
            0 -> R.drawable.carte1
            1 -> R.drawable.carte2
            2 -> R.drawable.carte3
            3 -> R.drawable.carte4
            4 -> R.drawable.carte5
            else -> R.drawable.carte1
        }

        img.setImageResource(carte)

        if (intent != null) {
            processIntent(intent)
        }
    }


    private fun processIntent(checkIntent: Intent) {
        // Check if intent has the action of a discovered NFC tag
        // with NDEF formatted contents
        if (checkIntent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {

            val tag: Tag = checkIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG)

            if (byteArrayToHexString(tag.id) == currentGame.nextTag()) {
                Toast.makeText(this@CarteActivity,
                        "Bien joué! Tu as trouvé le bon tag pour cette étape !",
                        Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@CarteActivity,
                        "Malheuresement, pas le bon tag !",
                        Toast.LENGTH_LONG).show()
            }
            //TODO Erase this
            Toast.makeText(this@CarteActivity,
                    "ID : ${byteArrayToHexString(tag.id)}",
                    Toast.LENGTH_LONG).show()
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
