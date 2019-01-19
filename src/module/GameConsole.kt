package module

import data.DataProvider
import data.LabelDefinition
import data.model.GameSave
import data.model.Save
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit


class GameConsole(private val presenter: GamePresenter) : GameInterface {
    override fun initMenu() {

        println()
        println("${LabelDefinition.menuHomeTitle}")
        loadSave()
        presenter.menuItems = DataProvider.prepareMenuHomeItem()
        displayMenuItems()
        //println("${LabelDefinition.menuItemQuitGame}")

        val input = Scanner(System.`in`)
        val lineString = input.nextLine()

        when (lineString) {
            "1" -> presenter.initGame()
            "2" -> presenter.askForLeaderBoard()
            "3" -> presenter.askForReplayGames()
            "4" -> presenter.askQuitGame()
            "q" -> presenter.askQuitGame()
        }
    }

    override fun loadSave() {
        val saveFileExists = presenter.file.exists()
        if (saveFileExists) {
            println("Lecture de la sauvegarde\n")
            ObjectInputStream(FileInputStream(presenter.file)).use { it ->
                presenter.save = it.readObject() as Save
            }


        }
    }

    override fun saveInFile() {
        val currentDate = LocalDateTime.now()
        var gameSave = GameSave(presenter.saveItemList, presenter.currentScore, currentDate)
        presenter.save.saves.add(gameSave)
        ObjectOutputStream(FileOutputStream(presenter.file)).use { it -> it.writeObject(presenter.save) }
        println("Partie Sauvegardée")
        println()
    }

    override fun printLeaderBoard() {
        val saves = presenter.save.saves
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

    override fun replayGames() {
        println(LabelDefinition.separatorString)
        println(LabelDefinition.replayMenuTitle)
        println()
        var position = 1
        val saves = presenter.save.saves
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
                printPlayground(item.playground)
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
        presenter.askForSaveInFile()
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


    override fun copyPlayGround(playground: Array<Array<String>>): Array<Array<String>> {
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

    override fun quitGame() {
        println("${LabelDefinition.quitGameString}")
        return
    }

}
