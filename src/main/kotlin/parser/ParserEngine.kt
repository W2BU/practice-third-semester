//Считать введенные пользователем данные
//Сделать запрос к серверу
//Распарсить ответ
//Вывести результат поиска
//Открыть нужную страницу в браузере

package parser

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder

class ParserEngine(

) {
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

    private fun jsonToList(json: String): List<String> {

    }

}