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
        } while (isPlaying)
    }

    override fun userDied() {
        presenter.saveInFile()
        isPlaying = false
        println("You have died ${presenter.currentScore}")
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


    override fun printLeaderBoard() {
        presenter.loadSave()

    }


}
