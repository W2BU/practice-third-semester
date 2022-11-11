package csvxmlparser

import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.system.measureTimeMillis


class CsvXmlParser() {

    private val entriesCount: HashMap<List<String>, Int> = hashMapOf()

    var lastPath: String = ""
        private set
    val repeatedMoreThanOneTime: HashMap<List<String>, Int> = hashMapOf()
    val floorsCountForEachCity: HashMap<String, MutableList<Int>> = hashMapOf()
    var executionTime: Long = 0
        private set

    fun result(pathToFile: String) {
        lastPath = pathToFile
        when (pathToFile.substringAfter('.')) {
            "csv" -> parseCsv()
            "xml" -> parseXml()
            else -> throw IllegalArgumentException("Unknown file extension")
        }
    }

    private fun parseCsv() {
        executionTime = measureTimeMillis {
            val result = Files.lines(Paths.get(lastPath))
                .skip(1)
                .flatMap { s: String ->
                    Stream.of(
                        s.split(";")
                    )
                }.collect(Collectors.toList())
            countDuplicatesAndFloors(result)
        }
    }

    private fun parseXml() {
        executionTime = measureTimeMillis {
            val xmlreg =
                "^<item city=\"([А-Яа-я -]+)\" street=\"([А-Яа-яё0-9 -,-]+)\" house=\"([0-9]|[1-9][0-9]|[1-9][0-9][0-9])\" floor=\"([1-5])+\" />\$".toRegex()
            val result = Files.lines(Paths.get(lastPath))
                .flatMap { s: String ->
                    if (s.matches(xmlreg)) {
                        Stream.of(xmlreg.find(s)!!.groupValues.subList(1, 5))
                    } else {
                        Stream.empty()
                    }
                }.collect(Collectors.toList())
            countDuplicatesAndFloors(result)
        }
    }

    private fun countDuplicatesAndFloors(result: List<List<String>>) {
        for (fullAddress in result) {
            //  entry data - times repeated
            if (entriesCount.containsKey(fullAddress)) {
                entriesCount.replace(fullAddress, entriesCount.getValue(fullAddress) + 1)
            } else {
                entriesCount[fullAddress] = 1
            }
            //  cityName - floorsCount
            val cityName = fullAddress[0]
            val floorsCount = fullAddress[3].toInt() - 1
            if (floorsCountForEachCity.containsKey(cityName)) {
                floorsCountForEachCity.getValue(cityName)[floorsCount] += 1
            } else {
                floorsCountForEachCity[cityName] = mutableListOf(0, 0, 0, 0, 0)
                floorsCountForEachCity.getValue(cityName)[floorsCount] += 1
            }
        }
        repeatedMoreThanOneTime.putAll(entriesCount.filter { it.value != 1 })
    }
}


