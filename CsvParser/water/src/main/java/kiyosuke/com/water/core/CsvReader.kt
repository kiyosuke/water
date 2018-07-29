package kiyosuke.com.water.core

import android.content.Context
import android.support.annotation.RawRes
import kiyosuke.com.water.util.toIntOrString
import kotlin.concurrent.thread

internal class CsvReader(context: Context) {
    private val assetsManager = context.resources.assets
    private val res = context.resources

    fun readAssets(fileName: String): List<Map<String, Any?>> {
        val header: MutableList<String> = mutableListOf()
        val csvMapList: MutableList<Map<String, Any?>> = mutableListOf()

        try {
            assetsManager.open(fileName).use {
                it.bufferedReader().useLines {
                    val sequenceList = it.toList()

                    sequenceList.first()
                            .split(",")
                            .forEach { header.add(it.replace("\"", "")) }


                    sequenceList.takeLast(sequenceList.size - 1)
                            .map { it.split(",") }
                            .forEach {
                                val csvMap: MutableMap<String, Any> = mutableMapOf()
                                it.forEachIndexed { i1, s1 ->
                                    csvMap[header[i1]] = s1.replace("\"", "")//.toIntOrString()
                                }
                                csvMapList.add(csvMap)
                            }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return csvMapList
    }

    fun readRaw(@RawRes resId: Int): List<Map<String, Any>> {
        val header: MutableList<String> = mutableListOf()
        val csvMapList: MutableList<Map<String, Any>> = mutableListOf()
        try {
            res.openRawResource(resId).use {
                it.bufferedReader().useLines {
                    val sequenceList = it.toList()

                    sequenceList.first()
                            .split(",")
                            .forEach { header.add(it.replace("\"", "")) }


                    sequenceList.takeLast(sequenceList.size - 1)
                            .map { it.split(",") }
                            .forEach {
                                val csvMap: MutableMap<String, Any> = mutableMapOf()
                                it.forEachIndexed { index, column ->
                                    csvMap[header[index]] = column.replace("\"", "")//.toIntOrString()
                                }
                                csvMapList.add(csvMap)
                            }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return csvMapList
    }


    fun readAssets(fileName: String, onSuccess: (List<Map<String, Any?>>) -> Unit, onError: (t: Throwable) -> Unit) {
        val header: MutableList<String> = mutableListOf()
        val csvMapList: MutableList<Map<String, Any?>> = mutableListOf()

        thread {

            try {
                assetsManager.open(fileName).use {
                    it.bufferedReader().useLines {
                        val sequenceList = it.toList()
                        // ヘッダーを取得
                        sequenceList.first()
                                .split(",")
                                .forEach { header.add(it.replace("\"", "")) }

                        // ヘッダーを飛ばして取得
                        sequenceList.takeLast(sequenceList.size - 1)
                                .map { it.split(",") }
                                .forEach {
                                    val csvMap: MutableMap<String, Any> = mutableMapOf()
                                    it.forEachIndexed { i1, s1 ->
                                        csvMap[header[i1]] = s1.replace("\"", "").toIntOrString()
                                    }
                                    csvMapList.add(csvMap)
                                }
                    }
                    onSuccess.invoke(csvMapList)
                }
            } catch (e: Exception) {
                onError.invoke(e)
            }

        }
    }

    fun readRaw(@RawRes resId: Int, onSuccess: (List<Map<String, Any?>>) -> Unit, onError: (t: Throwable) -> Unit) {
        val header: MutableList<String> = mutableListOf()
        val csvMapList: MutableList<Map<String, Any>> = mutableListOf()

        thread {
            try {
                res.openRawResource(resId).use {
                    it.bufferedReader().useLines {
                        val sequenceList = it.toList()
                        // ヘッダーを取得
                        sequenceList.first()
                                .split(",")
                                .forEach { header.add(it.replace("\"", "")) }

                        // ヘッダーを飛ばして取得
                        sequenceList.takeLast(sequenceList.size - 1)
                                .map { it.split(",") }
                                .forEach {
                                    val csvMap: MutableMap<String, Any> = mutableMapOf()
                                    it.forEachIndexed { index, column ->
                                        csvMap[header[index]] = column.replace("\"", "").toIntOrString()
                                    }
                                    csvMapList.add(csvMap)
                                }
                    }
                    onSuccess.invoke(csvMapList)
                }
            } catch (e: Exception) {
                onError.invoke(e)
            }
        }
    }
}