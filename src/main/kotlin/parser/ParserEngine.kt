//Считать введенные пользователем данные
//Сделать запрос к серверу
//Распарсить ответ
//Вывести результат поиска
//Открыть нужную страницу в браузере

package parser

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder


class WikiSearchEngine {

    var hits: Int = 0
        private set

    val searchResults: MutableList<Pair<String, Int>> = mutableListOf()

    fun getAnswerJson(request: String) {
        val encodedRequest: String = URLEncoder.encode(request, "UTF-8")
        val url = URL(
            "https://ru.wikipedia.org/w/api.php?action=query&list=search&utf8=&format=json&srsearch=$encodedRequest"
        )
        val connection = url.openConnection()
        var json = ""
        
        BufferedReader(InputStreamReader(connection.getInputStream())).use { inp ->
            var line: String?
            while (inp.readLine().also { line = it } != null) {
                json += line
            }
        }
        jsonToList(json)
    }

    private fun jsonToList(json: String) {
        val article = object {
            var title: String = ""
        }
        val queryField = JsonParser.parseString(json).asJsonObject.get("query")
        hits = queryField.asJsonObject.get("searchinfo").asJsonObject.get("totalhits").asInt
        for (jsonElement in queryField.asJsonObject.get("search").asJsonArray) {
            val pageid: Int = jsonElement.asJsonObject.get("pageid").asInt
            val title: String = jsonElement.asJsonObject.get("title").asString
            searchResults.add(Pair(title, pageid))
        }
    }
}