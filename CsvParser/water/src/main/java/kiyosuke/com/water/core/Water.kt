package kiyosuke.com.water.core

import android.content.Context
import android.support.annotation.RawRes
import kiyosuke.com.water.annotation.Header
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.jvmErasure

class Water private constructor(context: Context) {

    private val csvReader = CsvReader(context)

    private var fileName: String? = null

    @RawRes
    private var rawId: Int? = null

    /**
     * Read csv file of assets folder
     *
     * @param fileName csv file name.
     */
    fun load(fileName: String): Water {
        if (this.fileName != null || this.rawId != null) {
            throw IllegalArgumentException("This function can be called only once.")
        }
        this.fileName = fileName
        return this
    }

    /**
     * Read csv file of raw folder
     */
    fun load(@RawRes rawId: Int): Water {
        if (this.fileName != null || this.rawId != null) {
            throw IllegalArgumentException("This function can be called only once.")
        }
        this.rawId = rawId
        return this
    }

    /**
     * Do action
     *
     * @param clazz POJO
     */
    fun <T : Any> parse(clazz: KClass<T>): List<T> {
        val list = mutableListOf<T>()
        if (fileName == null && rawId == null) throw IllegalArgumentException("A file to be read is not specified.")
        fileName?.let {
            csvReader.readAssets(it).forEach {
                list.add(mapping(it, clazz))
            }
            return list
        }
        rawId?.let {
            csvReader.readRaw(it).forEach {
                list.add(mapping(it, clazz))
            }
            return list
        }
        return emptyList()
    }

    private fun <T : Any> mapping(map: Map<String, Any?>, clazz: KClass<T>): T {
        val instance = clazz.createInstance()
        clazz.declaredMemberProperties.forEach { property ->
            val mutableProperty = property as KMutableProperty<*>
            val header = property.annotations.firstOrNull { it is Header } as? Header
                    ?: throw IllegalArgumentException("Header annotation is not defined in mapping class.")
            val typeKClass = mutableProperty.returnType.jvmErasure
            var value = map[header.name]
            value = when (typeKClass) {
                Int::class -> value.toString().toInt()
                String::class -> value.toString()
                Float::class -> value.toString().toFloat()
                Double::class -> value.toString().toDouble()
                else -> throw IllegalStateException(typeKClass.simpleName + " this parameter type is not supported.")
            }

            mutableProperty.setter.call(instance, value)
        }
        return instance
    }

    companion object {
        fun with(context: Context) = Water(context)
    }
}