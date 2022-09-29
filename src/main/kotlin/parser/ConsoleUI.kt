package parser

class ConsoleUI(
    engine: ParserEngine
) {

    private var request = ""


    init {
        getUserRequest()
        engine.getAnswerJson(request)
    }


    private fun getUserRequest() {
        println("Enter your Wikipedia request:")
        request = readLine()!!
        println("Your request is \"$request\"")
    }


}