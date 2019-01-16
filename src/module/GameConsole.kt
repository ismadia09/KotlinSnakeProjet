package module

import java.util.concurrent.*


class GameConsole(private val presenter: GamePresenter) : GameInterface {

    private var isPlaying = false


    override fun startLoopReader() {
        val i = ConsoleInput(1,500, TimeUnit.MILLISECONDS)
        isPlaying = true

        do {
            val userInput = i.readLine() ?: ""
            presenter.userInput(userInput)
        } while (isPlaying)
    }

    override fun userDied() {
        isPlaying = false
        println("Dead bro")
    }

    override fun printPlayground(playground: Array<Array<String>>){

        for (array in playground) {
            for (value in array) {
                print("$value ")
            }
            println()
        }
        println()
    }

}
