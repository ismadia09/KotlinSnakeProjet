package module

import java.util.concurrent.TimeUnit


class GameConsole(private val presenter: GamePresenter) : GameInterface {

    private var isPlaying = false


    override fun startLoopReader() {
        val i = ConsoleInput(1, 500, TimeUnit.MILLISECONDS)
        isPlaying = true

        do {
            val userInput = i.readLine() ?: ""
            presenter.userInput(userInput)
            println("Current score ${presenter.currentScore}")
            val playgroungToSave = copyPlayGround(presenter.playground)
            presenter.saveGameStep(playgroungToSave)
        } while (isPlaying)
    }

    override fun userDied() {
        presenter.saveInFile()
        isPlaying = false
        println("You have died ${presenter.currentScore}")
        presenter.resetGame()

    }

    override fun printPlayground(playground: Array<Array<String>>) {

        for (array in playground) {
            for (value in array) {
                print("$value ")
            }
            println()
        }
        println()
    }


    fun copyPlayGround(playground: Array<Array<String>>): Array<Array<String>> {
        val copy = mutableListOf<Array<String>>()

        for (array in playground) {
            var values = mutableListOf<String>()
            for (value in array) {
                values.add(value)
            }
            copy.add(values.toTypedArray())
        }
        return copy.toTypedArray()
    }
}
