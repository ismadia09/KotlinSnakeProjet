package module


fun main(args: Array<String>) {
    startGame()
}

fun startGame() {
    GameConsole(GamePresenter()).initMenu()
}