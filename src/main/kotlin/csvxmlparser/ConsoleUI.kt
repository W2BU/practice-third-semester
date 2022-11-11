package csvxmlparser

/*
    Console UI class that controls console interface of program
*/
class ConsoleUI (
    private val parser: CsvXmlParser
) {
     init {
         var isActive = true
         while(isActive) {
             println("Enter full path to file or \"exit\" to exit:")
             when (val pathToFile = readLine()!!) { // Console input cannot be null
                 "exit" -> isActive = false
                 else -> {
                     try {
                         parser.result(pathToFile)
                         // Pretty hashmap formatting
                         println(buildString {
                             for (entry in parser.repeatedMoreThanOneTime) {
                                 append(entry.key)
                                 append(entry.value)
                                 appendLine()
                             }
                             for (entry in parser.floorsCountForEachCity) {
                                 append(entry.key)
                                 append(entry.value)
                                 appendLine()
                             }
                         })
                         println("Execution time = ${parser.executionTime} milliseconds")
                     } catch (e: Exception) {
                         println(e.message)
                     }
                 }
             }
         }
     }
}