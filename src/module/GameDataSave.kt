package module

import data.LabelDefinition
import data.model.GameSave
import data.model.Save
import data.model.SaveItem
import java.io.*
import java.time.LocalDateTime


class GameDataSave {
    val fileName = "src/data/save/savedGame.txt"
    val file = File(fileName)
    var saveFileExists = false
    var save = Save(mutableListOf<GameSave>())
    val saveItemList = mutableListOf<SaveItem>()

    fun resetData() {
        saveFileExists = false
        save = Save(mutableListOf<GameSave>())
    }

    fun handlerBackup(playground: Array<Array<String>>, lastDirection: Direction){
        val playgroungToSave = copyPlayGround(playground)
        saveGameStep(playgroungToSave, lastDirection)
    }

    private fun saveGameStep(playground: Array<Array<String>>, lastDirection: Direction) {
        val saveItem = SaveItem(lastDirection, playground)
        saveItemList.add(saveItem)
    }

    private fun copyPlayGround(playground: Array<Array<String>>): Array<Array<String>> {
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

    fun loadBackup() {
        val saveFileExists = file.exists()
        if (saveFileExists) {
            println("${LabelDefinition.readBackup}")
            ObjectInputStream(FileInputStream(file)).use { it ->
                save = it.readObject() as Save
            }
        }
    }

    fun saveInFile(currentScore: Int) {
        val currentDate = LocalDateTime.now()
        var gameSave = GameSave(saveItemList, currentScore, currentDate)
        save.saves.add(gameSave)
        ObjectOutputStream(FileOutputStream(file)).use { it -> it.writeObject(save) }
        println("${LabelDefinition.gameSaved}")
        println()
    }
}