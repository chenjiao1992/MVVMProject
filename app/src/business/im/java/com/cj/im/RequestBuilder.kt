package com.cj.im


import android.util.Log.d
import org.json.JSONObject
import java.io.File
import java.net.URLEncoder
import java.util.*
import java.util.logging.Logger

class RequestBuilder(val url: String) {
    val headers = TreeMap<String, String?>()
    val queries = TreeMap<String, String?>()
    val bodies = TreeMap<String, String?>()
    val uploadPairs = TreeMap<String, UploadPair>()
    val formDatas = LinkedList<Pair<String, String?>>()
    var rawBody = false
    var autoRetry = true
    var cacheMode = 0
    var autoReport = true
    var cacheExpiredTime = 5 * 60 * 1000L
    val hasHeader
        get() = headers.isNotEmpty()
    val hasQuery
        get() = queries.isNotEmpty()
    val hasBody
        get() = bodies.isNotEmpty()

    constructor(host: String, path: String) : this(String.format("%s%s", host, path))

    fun postUseRawBody(): RequestBuilder {
        rawBody = true
        return this
    }

    fun <T : Any> add(value: T?, transform: RequestBuilder.(value: T) -> Unit): RequestBuilder {
        if (value != null) {
            transform(value)
        }
        return this
    }

    fun add(transform: RequestBuilder.() -> Unit): RequestBuilder {
        transform()
        return this
    }

    fun addHeader(key: String, value: Any?): RequestBuilder {
        val header = value?.toString()
        if (header.isNullOrEmpty()) {
            headers[key] = header
        } else {
            var encodedHeader = header
            for (char in header) {
                if (char <= '\u001f' || char >= '\u007f') {
                    encodedHeader = URLEncoder.encode(header, "utf-8")
                    break
                }
            }
            headers[key] = encodedHeader
        }
        return this
    }

    fun addHeaders(headers: Map<String, String>?): RequestBuilder {
        if (headers.isNullOrEmpty()) return this
        this.headers.putAll(headers)
        return this
    }

    fun addHeadersCombined(key: String, values: Collection<Any>?, combineFormatter: String = ","): RequestBuilder {
        if (values.isNullOrEmpty()) return this
        val builder = StringBuilder()
        for (value in values) {
            builder.append(value.toString()).append(combineFormatter)
        }
        builder.deleteCharAt(builder.length - 1)
        addHeader(key, builder.toString())
        return this
    }

    fun addQuery(key: String, value: Any?): RequestBuilder {
        queries[key] = value?.toString()
        return this
    }

    fun addQueries(queries: Map<String, String>?): RequestBuilder {
        if (queries.isNullOrEmpty()) return this
        this.queries.putAll(queries)
        return this
    }

    fun addQueriesCombined(key: String, values: Collection<Any>?, combineFormatter: String = ","): RequestBuilder {
        if (values.isNullOrEmpty()) return this
        val builder = StringBuilder()
        for (value in values) {
            builder.append(value.toString()).append(combineFormatter)
        }
        builder.deleteCharAt(builder.length - 1)
        addQuery(key, builder.toString())
        return this
    }

    fun addBody(key: String, value: Any?): RequestBuilder {
        bodies[key] = value?.toString()
        return this
    }

    fun addBodies(bodies: Map<String, String>?): RequestBuilder {
        if (bodies.isNullOrEmpty()) return this
        this.bodies.putAll(bodies)
        return this
    }

    fun addBodies(json: JSONObject): RequestBuilder {
        for (key in json.keys()) {
            addBody(key, json.opt(key))
        }
        return this
    }

    fun addBodiesCombined(key: String, values: Collection<Any>?, combineFormatter: String = ","): RequestBuilder {
        if (values.isNullOrEmpty()) return this
        val builder = StringBuilder()
        for (value in values) {
            builder.append(value.toString()).append(combineFormatter)
        }
        builder.deleteCharAt(builder.length - 1)
        addBody(key, builder.toString())
        return this
    }

    fun addUploadPair(name: String, contentType: String, file: File): RequestBuilder {
        uploadPairs[name] = UploadPair(contentType, file)
        return this
    }

    fun addFormData(name: String, value:Any?):RequestBuilder {
        formDatas.add(Pair(name, value?.toString()))
        return this
    }

    fun setAutoRetry(autoRetry: Boolean): RequestBuilder {
        this.autoRetry = autoRetry
        return this
    }

    fun setCacheMode(mode: Int): RequestBuilder {
        this.cacheMode = mode
        return this
    }

    fun setCacheExpiredTime(time: Long): RequestBuilder {
        this.cacheExpiredTime = time
        return this
    }

    fun setAutoReport(autoReport: Boolean): RequestBuilder {
        this.autoReport = autoReport
        return this
    }

    val fullUrl
        get() = "$url$queryStr"
    val queryStr
        get() = if (queries.isNotEmpty()) {
            val builder = StringBuilder()
            builder.append('?')
            for ((key, value) in queries) {
                builder.append(key).append('=').append(value.orEmpty()).append('&')
            }
            builder.deleteCharAt(builder.length - 1)
            builder.toString()
        } else {
            ""
        }
    val bodyStr
        get() = if (bodies.isNotEmpty()) {
            val builder = StringBuilder()
            for ((key, value) in bodies) {
                builder.append(key).append('=').append(value.orEmpty()).append('&')
            }
            builder.deleteCharAt(builder.length - 1)
            builder.toString()
        } else {
            ""
        }
    val headerStr
        get() = if (headers.isNotEmpty()) {
            val builder = StringBuilder()
            for ((key, value) in headers) {
                builder.append(key).append('=').append(value.orEmpty()).append('&')
            }
            builder.deleteCharAt(builder.length - 1)
            builder.toString()
        } else {
            ""
        }

    private fun newLine(input: String) = input.replace("&", "\n")

    val mode
        get() = hasBody.trueOrFalse("POST", "GET")

    private fun getFullRequestUrl() = "$url${if (queryStr.isNotEmpty()) "&" else "?"}$bodyStr"

    override fun toString() =
            "Request: $mode $url\nHeaders:\n${newLine(headerStr)}\nQueries:\n${newLine(queryStr)}\nBodies:\n${
                newLine(
                        bodyStr)
            }\nFull Request:${getFullRequestUrl()}"


    fun <T : Any> addIndexed(values: Collection<T>?, transform: RequestBuilder.(index: Int, value: T) -> Unit): RequestBuilder {
        if (values.isNullOrEmpty()) return this
        values.forEachIndexed { index, value ->
            transform(this, index, value)
        }
        return this
    }

    fun <K : Any, V : Any> addMap(value: Map<K, V>?, transform: RequestBuilder.(key: K, value: V) -> Unit): RequestBuilder {
        if (value.isNullOrEmpty()) return this
        value.forEach {
            transform(this, it.key, it.value)
        }
        return this
    }
}