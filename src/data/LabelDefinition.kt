package data

class LabelDefinition {
    companion object {

        val separatorString = "###########################"
        val leaderBoardTitle = "Les meilleurs scores"
        val leaderBoardScoreNotFoundString = "Veuillez terminer une partie afin d'apparaitre dans le classement"
        val menuItemPlayGame = "Jouer au Snake Kotlin Edition"
        val menuItemSeeLeaderBoard = "Voir le classement des 10 meilleurs scores"
        val menuItemReplayGames = "Rejouer aux anciennes parties"
        val menuItemQuitGame = "q\t-\tAppuyer sur q pour quitter le jeu"
        val menuHomeTitle = "Menu Principal"
        val quitGameString = "Au revoir et à la prochaine"
        val replayMenuTitle = "Veuillez choisir une sauvegarde"
        val replayMenuSaveNotFoundString = "Veuillez terminer une partie afin de pouvoir rejouer"


        fun displayMenuItems() {
            val menuItems = mutableListOf<String>()
            menuItems.add(menuItemPlayGame)
            menuItems.add(menuItemSeeLeaderBoard)
            menuItems.add(menuItemReplayGames)
            var position = 1

            for (item in menuItems) {
                println("$position\t-\t$item")
                position++
            }

        }
    }
}