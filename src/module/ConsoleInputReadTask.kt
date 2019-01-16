package module

import java.awt.AWTException
import java.awt.Robot
import java.awt.event.KeyEvent
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.Callable

class ConsoleInputReadTask : Callable<String> {
    @Throws(IOException::class)
    override fun call(): String? {
        val br = BufferedReader(
                InputStreamReader(System.`in`))
        var input: String
        do {
            try {
                while (!br.ready()) {

                    try {
                    val robot = Robot()
                    robot.keyPress(KeyEvent.VK_ENTER)
                    robot.keyRelease(KeyEvent.VK_ENTER)
                    } catch (e: AWTException) {
                        // e.printStackTrace()
                    }

                    Thread.sleep(200)
                }



                input = br.readLine()

            } catch (e: InterruptedException) {
                return null
            }

        } while ("" == input)
        return input
    }
}