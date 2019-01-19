package misc

class Utils {

    companion object {
        fun waitingThread() {
            for (i in 0..5) {
                print(". ")
                Thread.sleep(300)
            }
        }
    }

}