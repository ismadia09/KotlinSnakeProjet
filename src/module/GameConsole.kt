package module

import data.DataProvider
import data.LabelDefinition
import data.model.GameSave
import data.model.Save
import misc.Utils.Companion.framePerSecond
import java.util.*
import java.util.concurrent.TimeUnit

class GameConsole(private val presenter: GamePresenter) : GameInterface {

    private var isPlaying = false


    override fun initMenu() {
        println()
        println("${LabelDefinition.menuHomeTitle}")

        presenter.menuItems = DataProvider.prepareMenuHomeItem()
        displayMenuItems()

        startLoop@ do {
            val input = Scanner(System.`in`)
            val lineString = input.nextLine()
            when (lineString) {
                "1" -> {
                    presenter.initGame()
                    break@startLoop
                }
                "2" -> {
                    presenter.askForLeaderBoard()
                    break@startLoop
                }
                "3" -> {
                    presenter.askForReplayGames()
                    break@startLoop
                }
                "4" -> {
                    presenter.askQuitGame()
                    break@startLoop
                }
                "q" -> {
                    presenter.askQuitGame()
                    break@startLoop
                }
                else -> println(message = "${LabelDefinition.userSelectionError}")
            }
        } while (true)

    }


    override fun printLeaderBoard(save: Save) {
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

    override fun replayGameMenu(save: Save) {
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
                val userChoice = userChoiceString.toIntOrNull()

                if (userChoice != null && userChoice > 0) {

                    index = userChoice!! - 1
                    canLaunchReplayInt = index.compareTo(saves.count() - 1)
                    if ((canLaunchReplayInt > 0).not()) {
                        canLaunchReplayBool = true
                        finalUserChoice = index
                    }
                }

            } while (canLaunchReplayBool.not())

            val save = saves.get(finalUserChoice)
            printBackupGame(save)

        } else {
            println("${LabelDefinition.replayMenuSaveNotFoundString}")
        }

        println()
        initMenu()
    }


    private fun printBackupGame(save: GameSave) {
        val saveItems = save.saveItemList
        val directions = mutableListOf<Direction>()
        for (item in saveItems) {
            directions.add(item.direction)
            println("Direction : ${item.direction}")
            printPlayground(item.playground)
            println()
            framePerSecond()
        }
        println(LabelDefinition.separatorString)
    }

    override fun displayMenuItems() {
        var position = 1
        val menuItems = presenter.menuItems
        if (menuItems.count() > 0) {
            for (item in menuItems) {
                println("$position\t-\t$item")
                position++
            }
        }
    }

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

    override fun quitGame() {
        println("${LabelDefinition.quitGameString}")
        return
    }

}
