//Считать введенные пользователем данные
//Сделать запрос к серверу
//Распарсить ответ
//Вывести результат поиска
//Открыть нужную страницу в браузере

package wikiSearch

import com.google.gson.JsonParser
import java.awt.Desktop
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder

class WikiSearchEngine {

    var hits: Int = 0
        private set

    // list of pairs page title/page id
    val searchResults: MutableList<Pair<String, String>> = mutableListOf()

    /*
        Accesses Wikipedia page search API with stated request
        Can throw exception

            request - request to perform a Wikipedia search with
     */
    fun search(request: String) {
        val encodedRequest: String = URLEncoder.encode(request, "UTF-8")
        val connection = URL(
            "https://ru.wikipedia.org/w/api.php?action=query&list=search&utf8=&format=json&srsearch=$encodedRequest"
        ).openConnection()
        var json = ""
        BufferedReader(InputStreamReader(connection.getInputStream())).use { input ->
            var line: String?
            while (input.readLine().also { line = it } != null) {
                json += line
            }
        }
        jsonToList(json)
    }

    /*
        Opens Wikipedia in browser on page with @pageid
        Can throw exception

            pageid - id of Wikipedia page
     */
    fun openInBrowser(pageid: String) {
        Desktop.getDesktop().browse(URL("https://ru.wikipedia.org/w/index.php?curid=$pageid").toURI())
    }

    /*
      Parses JSON string singling out related page titles and their ID's into list
     */
    private fun jsonToList(json: String) {
        val article = object {
            var title: String = ""
        }
        val queryField = JsonParser.parseString(json).asJsonObject.get("query")
        for (jsonElement in queryField.asJsonObject.get("search").asJsonArray) {
            val pageid: String = jsonElement.asJsonObject.get("pageid").asString
            val title: String = jsonElement.asJsonObject.get("title").asString
            searchResults.add(Pair(title, pageid))
        }
        hits = searchResults.size
    }
}