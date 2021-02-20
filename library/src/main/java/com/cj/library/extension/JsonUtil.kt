package com.cj.library.extension

import android.util.Log
import android.util.Log.e
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream
import java.io.OutputStream
import java.util.logging.Logger

/**
 * 由 Harreke 创建于 2017/9/25.
 */
object JsonUtil {
    val TAG="JsonUtil"
    val objectMapper = ObjectMapper()
    private val EMPTY_OBJECT = ObjectNode(objectMapper.nodeFactory)
    private val EMPTY_ARRAY = ArrayNode(objectMapper.nodeFactory)

    init {
        val objectMapper = objectMapper
        objectMapper.factory.configure(JsonFactory.Feature.USE_THREAD_LOCAL_FOR_BUFFER_RECYCLING, false)
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    fun toJSONArray(json: String?) = if (json.isNullOrEmpty()) {
        null
    } else {
        try {
            JSONArray(json)
        } catch (e: JSONException) {
            Log.e(TAG,"${json}非法的json数组格式")
            null
        }
    }

    fun toJSONObject(json: String?) = if (json.isNullOrEmpty()) {
        null
    } else {
        try {
            JSONObject(json)
        } catch (e: JSONException) {
            Log.e(TAG,"${json}非法的json格式")
            null
        }
    }

    fun <T : Any> toList(node: JsonNode, key: String? = null, clazz: Class<T>): List<T> {
        val actualNode = node.get(key.orEmpty()) ?: node
        return objectMapper.readValue<List<T>>(actualNode.toString(), objectMapper.typeFactory.constructParametricType(ArrayList::class.java, clazz))
    }

    fun <T : Any> toArrayList(json: String?, clazz: Class<T>) = if (json.isNullOrEmpty()) {
        null
    } else {
        try {
            objectMapper.readValue<ArrayList<T>>(json, objectMapper.typeFactory.constructParametricType(ArrayList::class.java, clazz))
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG,"${json}无法被反序列化为列表${clazz.canonicalName}")
            null
        }
    }

    fun <T : Any> toList(json: String?, clazz: Class<T>) = if (json.isNullOrEmpty()) {
        null
    } else {
        try {
            objectMapper.readValue<List<T>>(json, objectMapper.typeFactory.constructParametricType(ArrayList::class.java, clazz))
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG,"${json}无法被反序列化为列表${clazz.canonicalName}")
            null
        }
    }

    fun <K : Any, V : Any> toMap(json: String?, keyClass: Class<K>, valueClass: Class<V>) = if (json.isNullOrEmpty()) {
        null
    } else {
        try {
            objectMapper.readValue<Map<K, V>>(json, objectMapper.typeFactory.constructMapType(HashMap::class.java, keyClass, valueClass))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.e(TAG,"${json}无法被反序列化为集合${keyClass.canonicalName}/${valueClass.canonicalName}")
            null
        }
    }

    /**
    反序列化json列表，如果json字符串为null或empty，则直接创建一个新的列表对象；否则正常解析
     */
    fun <T : Any> toListOrEmpty(json: String?, clazz: Class<T>) = if (json.isNullOrEmpty()) {
        ArrayList()
    } else {
        try {
            objectMapper.readValue<List<T>>(json, objectMapper.typeFactory.constructParametricType(ArrayList::class.java, clazz))
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG,"${json}无法被反序列化为列表${clazz.canonicalName}")
            null
        }
    }

    fun <T : Any> toArray(json: String?, clazz: Class<T>) = if (json.isNullOrEmpty()) {
        null
    } else {
        try {
            objectMapper.readValue<Array<out T>>(json, objectMapper.typeFactory.constructArrayType(clazz))
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG,"${json}无法被反序列化为数组${clazz.canonicalName}")
            null
        }
    }

    fun <T : Any> toObject(json: String?, typeReference: TypeReference<T>) = if (json.isNullOrEmpty()) {
        null
    } else {
        try {
            objectMapper.readValue<T>(json, typeReference)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG,"${json}无法被反序列化为${typeReference.type}")
            null
        }
    }

    fun <T : Any> toObject(json: String?, clazz: Class<T>) = if (json.isNullOrEmpty()) {
        null
    } else {
        try {
            objectMapper.readValue(json, clazz)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG,"${json}无法被反序列化为${clazz.canonicalName}")
            null
        }
    }

    /**
    反序列化json，如果json字符串为null或empty，则直接创建一个新的对象；否则正常解析
     */
    fun <T : Any> toObjectOrEmpty(json: String?, clazz: Class<T>) = if (json.isNullOrEmpty()) {
        try {
            clazz.getDeclaredConstructor().newInstance()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    } else {
        try {
            objectMapper.readValue(json, clazz)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG,"${json}无法被反序列化为${clazz.canonicalName}")
            null
        }
    }

    fun <T : Any> toObject(input: InputStream?, clazz: Class<T>) = if (input == null) {
        null
    } else {
        try {
            objectMapper.readValue(input, clazz)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG,"输入流无法被反序列化为${clazz.canonicalName}")
            null
        }
    }

    fun <T : Any> toObject(input: InputStream?, typeReference: TypeReference<T>) = if (input == null) {
        null
    } else {
        try {
            objectMapper.readValue<T>(input, typeReference)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG,"输入流无法被反序列化为${typeReference.type}")
            null
        }
    }

    fun toString(target: Any?) = if (target == null) {
        null
    } else {
        try {
            objectMapper.writeValueAsString(target)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG,"${target::class.java.canonicalName}无法被序列化为json")
            e.printStackTrace()
            null
        }
    }



    fun toStream(target: Any?, output: OutputStream) {
        if (target == null) return
        try {
            objectMapper.writeValue(output, target)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG,"${target::class.java.canonicalName}无法被写入输出流")
        }
    }

    fun <T : Any> transform(source: Any?, targetClass: Class<T>) = toObject(toString(source), targetClass)

    fun writeTo(output: OutputStream?, target: Any?) {
        try {
            objectMapper.writeValue(output, target)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG,"输出流无法被序列化为json")
        }
    }
}