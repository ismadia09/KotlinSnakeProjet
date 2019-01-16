package module

import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class ConsoleInput(private val tries: Int, private val timeout: Int, private val unit: TimeUnit) {

    @Throws(InterruptedException::class)
    fun readLine(): String? {
        val ex = Executors.newSingleThreadExecutor()
        var input: String? = null
        try {
            // start working
            for (i in 0 until tries) {
                val result = ex.submit(
                        ConsoleInputReadTask())
                try {
                    input = result.get(timeout.toLong(), unit)
                    break
                } catch (e: ExecutionException) {
                    //e.cause.printStackTrace()
                } catch (e: TimeoutException) {
                    result.cancel(true)
                }

            }
        } finally {
            ex.shutdownNow()
        }
        return input
    }
}