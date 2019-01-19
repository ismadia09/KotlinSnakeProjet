package module

import data.LabelDefinition
import data.model.*
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

    private var snake = Snake(Position(0, 0))
    var lastDirection = Direction.DOWN
    var playground = arrayOf<Array<String>>()
    var foodBlocks = mutableListOf<Position>()
    var size = 10
    val saveItemList = mutableListOf<SaveItem>()
    var currentScore = 0
    val fileName = "src/data/save/savedGame.txt"
    val file = File(fileName)
    var saveFileExists = false
    var save = Save(mutableListOf<GameSave>())

    init {
        view = GameConsole(this)
    }

    fun initGame() {
        loadSave()
        println("Press enter key to start the game : ")
        val input = Scanner(System.`in`)
        val lineString = input.nextLine()

        //Check if enter key
        if (lineString.equals("")) {
            initSnakeGame()
        }


    }

    fun initSnakeGame() {
        preparePlayground(size)
        snake.initSnake(size)
        view.startLoopReader()
    }

    fun initMenu() {
        println()
        println("${LabelDefinition.menuHomeTitle}")
        loadSave()
        LabelDefinition.displayMenuItems()
        println("${LabelDefinition.menuItemQuitGame}")

        val input = Scanner(System.`in`)
        val lineString = input.nextLine()

        when (lineString) {
            "1" -> initGame()
            "2" -> printLeaderBoard()
            "3" -> replayGames()
            "q" -> quitGame()
        }


    }

    fun quitGame() {
        println("${LabelDefinition.quitGameString}")
        return
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
                view.userDied()
            }
            updateScore(isDead)

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
    fun loadSave() {
        saveFileExists = file.exists()
        if (saveFileExists) {
            println("Lecture de la sauvegarde\n")
            ObjectInputStream(FileInputStream(file)).use { it ->
                save = it.readObject() as Save
            }


        }
    }

    /***
     * GameSave the SaveItem array in a file
     */
    fun saveInFile() {
        val currentDate = LocalDateTime.now()
        var gameSave = GameSave(saveItemList, currentScore, currentDate)
        save.saves.add(gameSave)
        ObjectOutputStream(FileOutputStream(file)).use { it -> it.writeObject(save) }
        println("Partie Sauvegardée $file")
        println()
    }

    /**
     * Add SaveItem in SaveItem Array
     */
    fun saveGameStep(playground: Array<Array<String>>) {
        val saveItem = SaveItem(lastDirection, playground)
        saveItemList.add(saveItem)

    }


    /**
     * Update score
     */
    fun updateScore(isDead: Boolean) {
        if (isDead == false) {
            currentScore += 100
        }
    }

    fun printLeaderBoard() {
        val saves = save.saves
        if (saves.count() > 0) {
            var ascendingSortedScoreList = saves.sortedWith(compareBy({ it.score }))
            val descendingSortedScoreList = ascendingSortedScoreList.reversed()
            println(LabelDefinition.separatorString)
            println(LabelDefinition.leaderBoardTitle)
            println()

            var position = 1
            for (item in descendingSortedScoreList) {
                val saveDateString = item.saveDate.toLocalDate()
                println("$position\t-\t Score : ${item.score} \tDate : $saveDateString")
                position++

            }
        } else {
            println("${LabelDefinition.leaderBoardScoreNotFoundString}")
        }
        println()
        println(LabelDefinition.separatorString)
        println()
        initMenu()
    }

    fun replayGames() {
        println(LabelDefinition.separatorString)
        println(LabelDefinition.replayMenuTitle)
        println()
        var position = 1
        val saves = save.saves
        var canLaunchReplayInt = 0
        var canLaunchReplayBool = false
        var index = 0
        var finalUserChoice = 0
        if (saves.count() > 0) {

            do {
                for (save in saves) {
                    val saveDateString = save.saveDate.toLocalDate()
                    println("$position\t-\t Score : ${save.score} \tDate : $saveDateString")
                    position++
                }
                position = 1
                println()
                val input = Scanner(System.`in`)
                val userChoiceString = input.nextLine()
                val userChoice = userChoiceString.toInt()
                index = userChoice - 1
                canLaunchReplayInt = index.compareTo(saves.count() - 1)
                if ((canLaunchReplayInt > 0).not()) {
                    canLaunchReplayBool = true
                    finalUserChoice = index
                }
            } while (canLaunchReplayBool.not())

            val save = saves.get(finalUserChoice)
            val saveItems = save.saveItemList
            val directions = mutableListOf<Direction>()
            for (item in saveItems) {
                directions.add(item.direction)
                println("Direction : ${item.direction}")
                view.printPlayground(item.playground)
                println()
            }
            println(LabelDefinition.separatorString)
            println()
            initMenu()
        } else {
            println("${LabelDefinition.replayMenuSaveNotFoundString}")
            println()
            initMenu()
        }

    }

    fun resetGame() {
        snake = Snake(Position(0, 0))
        lastDirection = Direction.DOWN
        playground = arrayOf<Array<String>>()
        foodBlocks = mutableListOf<Position>()
        currentScore = 0
        saveFileExists = false
        foodBlocks = mutableListOf<Position>()
        size = 10
        save = Save(mutableListOf<GameSave>())
        initMenu()
    }
}