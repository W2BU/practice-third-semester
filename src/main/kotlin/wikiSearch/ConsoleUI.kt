package wikiSearch

const val TITLES_ON_PAGE = 8

class ConsoleUI (
    private val engine: WikiSearchEngine
) {

    private var request = ""

    init {
        readUserRequestFromConsole()
        var isSuccessful = true
        try {
            engine.search(request)
        } catch (e: Exception) {
            println("Nothing was found with \"$request\"")
            isSuccessful = confirmExitDialog()
        }

        if (isSuccessful) {
            showHitsList()
        }
    }


    private fun readUserRequestFromConsole() {
        println("Enter your Wikipedia request:")
        request = readLine()!! // Console input cannot be null
        println("Your request is \"$request\"")
    }

    /*
        Confirmation dialog that also returns its own result
        true - exit confirmed
        false - exit declined
     */
    private fun confirmExitDialog(): Boolean {
        println("Try again?")
        println("1 - yes, 2 - no")
        var isActive = true
        var confirmed = true
        var chosenOption: String
        while(isActive) {
            chosenOption = readLine()!!
            when(chosenOption) {
                "1" -> {
                    isActive = false
                    confirmed = true
                }
                "2" -> {
                    isActive = false
                    confirmed = false
                }
                else -> println("Unknown option")
            }
        }
        return confirmed
    }

    /*
        Prints a list of related to request wikipedia pages in amount of TITLES_ON_PAGE per page
        and scrolling options
     */
    private fun showHitsList() {
        var page = 0
        val totalPages = engine.hits.div(TITLES_ON_PAGE)
        val exitCode = TITLES_ON_PAGE + 3
        var userInput: Int
        var isActive = true
        println("Result found with \"$request\": ${engine.hits}\n")
        println("Choose option to open in browser")

        while(isActive) {
            //  print titles
            for (i in 0 until TITLES_ON_PAGE) {
                try {
                    println("${i + 1} - ${engine.searchResults[i].first}")
                } catch(e: IndexOutOfBoundsException) {
                    println("")
                }
            }
            println("${TITLES_ON_PAGE + 1} - Previous    ${page+1} of ${totalPages+1}    ${TITLES_ON_PAGE + 2} - Next")
            println("$exitCode - Exit")

            //  choice handling
            userInput = readLine()?.toInt() ?: -1
            when (userInput) {
                in 1..TITLES_ON_PAGE ->
                    try {
                        engine.openInBrowser(
                            engine.searchResults[userInput - 1 + page * TITLES_ON_PAGE].second
                        )
                    } catch (e: IndexOutOfBoundsException) {
                        println("Unknown option")
                    } catch (e: Exception) {
                        println("Something went wrong")
                    }
                TITLES_ON_PAGE -> if (page != 0) page -= 1
                TITLES_ON_PAGE + 1 -> if (page < totalPages) page += 1
                exitCode -> isActive = false
                else -> println("Unknown option")
            }
        }
    }

}