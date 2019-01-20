package module

import data.model.Save

interface GameInterface {
    fun initMenu()
    fun startLoopReader()
    fun userDied()
    fun printPlayground(playground: Array<Array<String>>)
    fun quitGame()
    fun printLeaderBoard(save: Save)
    fun replayGameMenu(save: Save)
    fun displayMenuItems()
}