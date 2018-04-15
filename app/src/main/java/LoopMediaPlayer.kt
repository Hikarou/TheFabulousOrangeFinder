import android.content.Context
import android.media.MediaPlayer
import android.util.Log

class LoopMediaPlayer private constructor(context: Context, resId: Int, startImmediately: Boolean) {

    private var mContext: Context? = null
    private var mResId = 0
    private var mCounter = 1

    private var mCurrentPlayer: MediaPlayer? = null
    private var mNextPlayer: MediaPlayer? = null

    private val onCompletionListener = MediaPlayer.OnCompletionListener { mediaPlayer ->
        mediaPlayer.release()
        mCurrentPlayer = mNextPlayer

        createNextMediaPlayer()

        Log.d(TAG, String.format("Loop #%d", ++mCounter))
    }

    fun restart() {
        mCurrentPlayer!!.seekTo(0)
        mCurrentPlayer!!.start()
    }

    fun start() {
        mCurrentPlayer!!.start()
    }

    fun stop() {
        mCurrentPlayer!!.pause()
    }

    init {
        mContext = context
        mResId = resId

        mCurrentPlayer = MediaPlayer.create(mContext, mResId)
        mCurrentPlayer!!.setOnPreparedListener {
            if (startImmediately) {
                mCurrentPlayer!!.start()
            }
        }

        createNextMediaPlayer()
    }

    private fun createNextMediaPlayer() {
        mNextPlayer = MediaPlayer.create(mContext, mResId)
        mCurrentPlayer!!.setNextMediaPlayer(mNextPlayer)
        mCurrentPlayer!!.setOnCompletionListener(onCompletionListener)
    }

    companion object {

        val TAG = LoopMediaPlayer::class.java.simpleName

        fun create(context: Context, resId: Int, startImmediately: Boolean): LoopMediaPlayer {
            return LoopMediaPlayer(context, resId, startImmediately)
        }
    }
}
