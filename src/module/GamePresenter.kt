package module

import data.LabelDefinition
import data.model.*
import misc.Utils
import java.io.*
import java.time.LocalDateTime
import java.util.*


enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class GamePresenter {

    companion object {
        const val TOPKEY = "z"
        const val LEFTKEY = "q"
        const val RIGHTKEY = "d"
        const val BOTOOMKEY = "s"
        const val FOOD = "8"
        const val EMPTYBLOCK = "_"
    }

    private val view: GameInterface
    private var gameDataSave = GameDataSave()

    private var snake = Snake(Position(0, 0))
    var lastDirection = Direction.DOWN
    var playground = arrayOf<Array<String>>()
    var foodBlocks = mutableListOf<Position>()
    var size = 10
    var currentScore = 0

    var menuItems = arrayOf<String>()

    init {
        view = GameConsole(this)
    }

    fun initGame() {
        askForloadSave()
        println("${LabelDefinition.initGameBeginMessage}")
        val input = Scanner(System.`in`)
        val lineString = input.nextLine()

        //Check if enter key
        if (lineString.equals("")) {
            initSnakeGame()
        }
    }

    private fun initSnakeGame() {
        preparePlayground(size)
        snake.initSnake(size)
        view.startLoopReader()
    }

    fun askForInitMenu() {
        gameDataSave.loadBackup()
        view.initMenu()
    }

    fun askQuitGame() {
        view.quitGame()
    }

    fun userInput(input: String) {

        resetPlayground()
        updatePlayground()

        if (foodBlocks.count() > 0) {
            setFood()
        } else {
            addFood(10)
        }

        handleUserInput(input)
        view.printPlayground(playground)
    }


    private fun resetPlayground() {

        gameDataSave.handlerBackup(playground, lastDirection)

        for (i in 0 until playground.size) {
            for (j in 0 until playground[i].size) {
                playground[i][j] = "_"
            }
        }
    }

    //Add Snake to playground
    private fun updatePlayground() {

        for (i in snake.body) {
            if (playground.getOrNull(i.x) != null) {
                if (playground[i.x].getOrNull(i.y) != null) {
                    playground[i.x][i.y] = i.pattern
                }
            }
        }
    }

    //Add Food to playground
    private fun setFood() {

        for (i in foodBlocks) {
            if (playground.getOrNull(i.x) != null && playground[i.x].getOrNull(i.y) != null) {
                playground[i.x][i.y] = FOOD
            }
        }
    }

    private fun addFood(size: Int) {

        val x = (0..size).shuffled().first()
        val y = (0..size).shuffled().first()
        foodBlocks.add(Position(x, y))
    }

    private fun handleUserInput(userInput: String) {
        var isDead: Boolean? = false

        when {
            userInput.contains(TOPKEY, true) -> if (lastDirection != Direction.DOWN) {
                lastDirection = Direction.UP
                isDead = snake.moveSnake(lastDirection)
            }
            userInput.contains(BOTOOMKEY, true) -> if (lastDirection != Direction.UP) {
                lastDirection = Direction.DOWN
                isDead = snake.moveSnake(lastDirection)
            }
            userInput.contains(LEFTKEY, true) -> if (lastDirection != Direction.RIGHT) {
                lastDirection = Direction.LEFT
                isDead = snake.moveSnake(lastDirection)
            }
            userInput.contains(RIGHTKEY, true) -> if (lastDirection != Direction.LEFT) {
                lastDirection = Direction.RIGHT
                isDead = snake.moveSnake(lastDirection)
            }
            else -> {
                isDead = snake.moveSnake(lastDirection)
            }
        }

        isDead?.let { isDead_ ->
            if (isDead_) {
                askForSaveInFile()
                view.userDied()
            } else {
                currentScore += 100
            }
        }

        foodBlocks = snake.checkAteFood(foodBlocks)
    }

    //Init playground
    private fun preparePlayground(size: Int) {
        for (i in 0..size) {
            var array = arrayOf<String>()
            for (j in 0..size) {
                array += EMPTYBLOCK
            }
            playground += array
        }
    }

    /***
     * Load GameSave From a file
     */
    fun askForloadSave() {
        gameDataSave.loadBackup()
    }

    /***
     * GameSave the SaveItem array in a file
     */
    fun askForSaveInFile() {
        gameDataSave.saveInFile(currentScore)
    }


    fun askForLeaderBoard() {
        view.printLeaderBoard(gameDataSave.save)
    }

    fun askForReplayGames() {
        view.replayGameMenu(gameDataSave.save)
    }

    fun resetGame() {
        snake = Snake(Position(0, 0))
        lastDirection = Direction.DOWN
        playground = arrayOf<Array<String>>()
        foodBlocks = mutableListOf<Position>()
        currentScore = 0
        foodBlocks = mutableListOf<Position>()
        size = 10

        gameDataSave.resetData()

        askForInitMenu()
    }

}