package ski.mashiro.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import ski.mashiro.common.Result

/**
 * @author MashiroT
 * @since 2023-07-27
 */
object JsonUtils {
    private val OBJECT_MAPPER = ObjectMapper().registerKotlinModule().registerModules(JavaTimeModule())

    fun trans2Json(obj: Any): String {
        return OBJECT_MAPPER.writeValueAsString(obj)
    }

    fun <T> trans2Obj(json: String, clazz: Class<T>): T {
        return OBJECT_MAPPER.readValue(json, clazz)
    }

    fun <K, V> trans2Map(json: String, kClazz: Class<K>, vClazz: Class<V>): Map<K, V> {
        val mapType = OBJECT_MAPPER.typeFactory.constructMapType(
            MutableMap::class.java, kClazz, vClazz
        )
        return OBJECT_MAPPER.readValue(json, mapType)
    }

    fun <T> trans2List(json: String, clazz: Class<T>): List<T> {
        val collectionType = OBJECT_MAPPER.typeFactory.constructCollectionType(
            MutableList::class.java, clazz
        )
        return OBJECT_MAPPER.readValue(json, collectionType)
    }

    fun <T> trans2ResultList(json: String, clazz: Class<T>): Result<List<T>?> {
        val parametricType =
            OBJECT_MAPPER.typeFactory.constructParametricType(
                Result::class.java,
                OBJECT_MAPPER.typeFactory.constructCollectionType(
                    MutableList::class.java, clazz
                )
            )
        return OBJECT_MAPPER.readValue(json, parametricType)
    }
}
