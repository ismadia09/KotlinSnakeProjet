package data.model

data class Position(var x: Int, var y: Int, val pattern: String = "#") {
    override fun equals(other: Any?): Boolean {

        return when (other) {
            is Position -> {
                (this.x == other.x && this.y == other.y)
            }
            else -> false
        }
    }
}
