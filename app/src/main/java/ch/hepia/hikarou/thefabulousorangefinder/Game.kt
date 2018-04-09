package ch.hepia.hikarou.thefabulousorangefinder

import java.io.Serializable

class Game(val name: String, val gameTags : Array<String>) : Serializable {
    var currentStep = 0
}