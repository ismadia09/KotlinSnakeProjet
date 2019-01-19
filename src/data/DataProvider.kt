package data

object DataProvider {

    fun prepareMenuHomeItem(): Array<String> {
        val menuItems = mutableListOf<String>()
        menuItems.add(LabelDefinition.menuItemPlayGame)
        menuItems.add(LabelDefinition.menuItemSeeLeaderBoard)
        menuItems.add(LabelDefinition.menuItemReplayGames)
        menuItems.add(LabelDefinition.menuItemQuitGame)

        return menuItems.toTypedArray()
    }


}