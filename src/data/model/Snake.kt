package data.model

import com.sun.org.apache.xpath.internal.operations.Bool
import module.Direction

class Snake (var coord: Position){

    var body = mutableListOf<Position>()
    var sizeGround = 0
    var lastDirection: Direction? = null

    fun initSnake(sizeGround: Int) {
        body.add(Position(0, 0, "@"))
        body.add(Position(-1, 0))
        body.add(Position(-2, 0))
        this.sizeGround = sizeGround
    }

    private fun addBodyBlock(dir: Direction) {

        var lastPosition = body.last()

        when(dir) {
            Direction.DOWN -> {
                lastPosition.x
                body.add(lastPosition)
            }
            Direction.UP -> {
                lastPosition.x
                body.add(lastPosition)
            }
            Direction.LEFT -> {
                lastPosition.y
                body.add(lastPosition)
            }
            Direction.RIGHT -> {
                lastPosition.y
                body.add(lastPosition)
            }
        }
    }

    fun moveSnake(dir: Direction) :Boolean? {

        lastDirection = dir

        var tmp = mutableListOf<Position>()
        tmp.add(Position(body[0].x, body[0].y, body[0].pattern))

        for (i in 0 until body.size) {
            if (i>0){
                if (body.size - 1 > i - 1) {
                    tmp.add(Position(body[i - 1].x, body[i - 1].y, body[i].pattern))
                }
            }
        }

        body = tmp.toMutableList()

        when(dir) {
            Direction.DOWN -> body[0].x++
            Direction.UP -> body[0].x--
            Direction.LEFT -> body[0].y--
            Direction.RIGHT -> body[0].y++
        }

        checkInfinityWall()
        return  checkAteYourself()
    }

    private fun checkInfinityWall() {

        if ( body[0].x > sizeGround ) {
            body[0].x = 0
        } else if ( body[0].x < 0 ) {
            body[0].x = sizeGround
        } else if ( body[0].y < 0 ) {
            body[0].y = sizeGround
        } else if ( body[0].y > sizeGround) {
            body[0].y = 0
        }
    }

    private fun checkAteYourself() : Boolean {

        val tmp = body.filter { it.pattern != "@" }

        if ( tmp.contains(body[0]) ) {
           // throw Exception("ok")
            return true
        }

        return false
    }


    fun checkAteFood(food: MutableList<Position>) :MutableList<Position> {

        if ( food.contains(body[0]) ) {
            addBodyBlock(lastDirection!!)
            food.remove(body[0])
        }

        return  food
    }

}