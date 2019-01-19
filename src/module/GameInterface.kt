package module

interface GameInterface {
    fun initMenu()
    fun startLoopReader()
    fun userDied()
    fun printPlayground(playground: Array<Array<String>>)
    fun copyPlayGround(playground: Array<Array<String>>): Array<Array<String>>
    fun quitGame()
    fun loadSave()
    fun saveInFile()
    fun printLeaderBoard()
    fun replayGames()
    fun displayMenuItems()


}