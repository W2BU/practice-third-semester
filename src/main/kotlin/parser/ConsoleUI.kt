package parser

class ConsoleUI(
    engine: WikiSearchEngine
) {

    private var request = ""

    init {
        getUserRequest()
        try {
            engine.getAnswerJson(request)
        } catch (e: Exception) {
            error("Something went wrong")
        }
    }

    private fun getUserRequest() {
        println("Enter your Wikipedia request:")
        try {
            request = readLine()!!
        } catch(e: NullPointerException) {
            error("Request cannot be empty")
        }
        println("Your request is \"$request\"")
    }


}