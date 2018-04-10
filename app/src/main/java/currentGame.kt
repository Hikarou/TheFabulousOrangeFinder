import ch.hepia.hikarou.thefabulousorangefinder.Game

object currentGame {
    private lateinit var curGame : Game

    fun setCurGame(game: Game) {
        curGame = game
    }

    fun getCurStep(): Int {
        return curGame.currentStep
    }

    fun nextTag(): String {
        return curGame.gameTags[curGame.currentStep]
    }
}