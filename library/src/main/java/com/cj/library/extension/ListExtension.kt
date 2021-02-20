package com.cj.library.extension

import org.json.JSONArray

/**
 * 由 Harreke 创建于 2017/11/14.
 */
fun <T : Any> List<T>?.random() = if (this != null && this.isNotEmpty()) this[(Math.random() * this.size).toInt()] else null

private fun ensureChangePosition(list: List<*>, from: Int, to: Int) = from != to && from >= 0 && from < list.size && to >= 0 || to < list.size

fun <T : Any> MutableList<T>.swapItem(from: Int, to: Int) = if (ensureChangePosition(this, from, to)) {
    val fromItem = this[from]
    val toItem = this[to]
    this[from] = toItem
    this[to] = fromItem
    true
} else {
    false
}

fun <T : Any> MutableList<T>.moveItem(from: Int, to: Int) = if (ensureChangePosition(this, from, to)) {
    val item = this.removeAt(from)
    this.add(to, item)
    true
} else {
    false
}

/**
 * 找到第一个
 */
inline fun <T : Any> List<T>?.findFirstItemAndPosition(predicate: (T) -> Boolean): Pair<Int, T>? {
    if (this.isNullOrEmpty()) return null
    for ((index, item) in this.withIndex()) {
        if (predicate(item))
            return Pair(index, item)
    }
    return null
}

/**
 * 找到第一个
 */
inline fun <T : Any> List<T>?.findFirstPair(predicate: (index: Int, T) -> Boolean): Pair<Int, T>? {
    if (this.isNullOrEmpty()) return null
    for ((index, item) in this.withIndex()) {
        if (predicate(index, item))
            return Pair(index, item)
    }
    return null
}

/**
 * 找到最大的一个
 */
inline fun <T : Any> Collection<T>.findMax(body: (T) -> Long): T? {
    var target: T? = null
    var targetNumber: Long = Long.MIN_VALUE
    this.forEach {
        val number = body(it)
        if (number > targetNumber) {
            target = it
            targetNumber = number
        }
    }
    return target
}

/**
 * 找到最大的一个
 */
inline fun <T : Any> Collection<T>.findMin(body: (T) -> Long): T? {
    var target: T? = null
    var targetNumber: Long = Long.MAX_VALUE
    this.forEach {
        val number = body(it)
        if (number < targetNumber) {
            target = it
            targetNumber = number
        }
    }
    return target
}

// 倒序遍历
inline fun <T : Any> List<T>.forEachReverseIndexed(action: (index: Int, T) -> Unit): Unit {
    val listSize = size
    for (index in (listSize - 1) downTo 0)
        action(index, this[index])
}

fun <T : Any> List<T>?.toJson() = JsonUtil.toString(this)

fun <T : Any> String?.toList(clazz: Class<T>) = JsonUtil.toList(this, clazz)

fun <T : Any> List<T>?.toArrayList() = if (this.isNullOrEmpty()) {
    null
} else {
    this as? ArrayList<T> ?: ArrayList(this)
}

fun <T : Any> MutableList<T>?.removeFirst(predicate: (T) -> Boolean): Boolean {
    if (this.isNullOrEmpty()) return false
    val iterator = iterator()
    while (iterator.hasNext()) {
        val item = iterator.next()
        if (predicate(item)) {
            iterator.remove()
            return true
        }
    }
    return false
}

fun <T : Any> MutableList<T>?.remove(predicate: (T) -> Boolean): Int {
    if (this.isNullOrEmpty()) return 0
    var removed = 0
    val iterator = iterator()
    while (iterator.hasNext()) {
        val item = iterator.next()
        if (predicate(item)) {
            iterator.remove()
            removed++
        }
    }
    return removed
}

fun <T : Any> List<T>?.filterNoRepeat(identifier: (T) -> String): List<T>? {
    if (this.isNullOrEmpty()) return null
    val map = HashMap<String, T>()
    for (item in this) {
        map[identifier(item)] = item
    }
    return map.values.toList()
}

inline fun <reified T : Any> List<*>?.indexOfFirstWhen(predicate: (T) -> Boolean): Int {
    if (this.isNullOrEmpty()) return -1
    for ((index, item) in withIndex()) {
        if (item is T && predicate(item)) {
            return index
        }
    }
    return -1
}

inline fun <reified T : Any> List<*>?.indexOfFirst(): Int {
    if (this.isNullOrEmpty()) return -1
    for ((index, item) in withIndex()) {
        if (item is T) {
            return index
        }
    }
    return -1
}

inline fun <reified T : Any> List<*>?.getAt(position: Int): T? {
    if (this.isNullOrEmpty() || position !in 0 until size) return null
    return get(position) as? T
}

inline fun <reified T : Any> List<*>?.getFirst(): T? {
    if (this.isNullOrEmpty()) return null
    for (item in this) {
        if (item is T) {
            return item
        }
    }
    return null
}

inline fun <reified T : Any> List<*>?.getLast(): T? {
    if (this.isNullOrEmpty()) return null
    for (item in this.asReversed()) {
        if (item is T) {
            return item
        }
    }
    return null
}

inline fun <reified T : Any> List<*>?.getFirstWhen(predicate: (T) -> Boolean): T? {
    if (this.isNullOrEmpty()) return null
    for (item in this) {
        if (item is T && predicate(item)) {
            return item
        }
    }
    return null
}

inline fun <reified T : Any> List<*>?.filterFirst(predicate: (T) -> Boolean): T? {
    if (this.isNullOrEmpty()) return null
    for (item in this) {
        if (item is T && predicate(item)) {
            return item
        }
    }
    return null
}

inline fun <reified T : Any> MutableList<Any>?.replaceFirst(newValue: Any, addIfNotExist: Boolean = false, addToLast: Boolean = true) {
    if (this.isNullOrEmpty()) return
    val count = size
    var index = 0
    for (item in this) {
        if (item is T) {
            break
        }
        index++
    }
    if (index < count) {
        this[index] = newValue
    } else if (addIfNotExist) {
        if (addToLast) {
            add(newValue)
        } else {
            add(0, newValue)
        }
    }
}

inline fun <reified T : Any> MutableList<Any>?.replaceFirstWhen(predicate: (T) -> kotlin.Boolean, newValue: Any, addIfNotExist: Boolean = false,
        addToLast: Boolean = true) {
    if (this.isNullOrEmpty()) return
    var index = 0
    for (item in this) {
        if (item is T && predicate(item)) {
            break
        }
        index++
    }
    if (index < size) {
        this[index] = newValue
    } else if (addIfNotExist) {
        if (addToLast) {
            add(newValue)
        } else {
            add(0, newValue)
        }
    }
}

inline fun <reified T : Any> MutableList<Any>?.changeFirst(changeAction: (T) -> Unit): Boolean {
    if (this.isNullOrEmpty()) return false
    var index = 0
    for (item in this) {
        if (item is T) {
            break
        }
        index++
    }
    return if (index < size) {
        val item = this[index] as T
        changeAction(item)
        this[index] = item
        true
    } else {
        false
    }
}

inline fun <reified T : Any> MutableList<Any>?.changeFirstWhen(predicate: (T) -> Boolean, changeAction: (T) -> Unit): Boolean {
    if (this.isNullOrEmpty()) return false
    var index = 0
    for (item in this) {
        if (item is T && predicate(item)) {
            break
        }
        index++
    }
    return if (index < size) {
        val item = this[index] as T
        changeAction(item)
        this[index] = item
        true
    } else {
        false
    }
}

inline fun <reified T : Any> List<*>?.changeAt(position: Int, changeAction: (T) -> Unit): Boolean {
    if (this.isNullOrEmpty()) return false
    if (position !in 0 until size) return false
    val item = get(position)
    if (item is T) {
        changeAction(item)
        return true
    }
    return false
}

inline fun <reified T : Any> MutableList<*>?.removeWhen(predicate: (T) -> Boolean): Int {
    if (this.isNullOrEmpty()) return 0
    var count = 0
    val iterator = iterator()
    val pendingRemoval = ArrayList<Any>()
    while (iterator.hasNext()) {
        val item = iterator.next()
        if (item is T && predicate(item)) {
            pendingRemoval.add(item)
            count++
        }
    }
    removeAll(pendingRemoval)
    return count
}

inline fun <reified T : Any> MutableList<Any>?.removeFirst(): Boolean {
    if (this.isNullOrEmpty()) return false
    var index = 0
    val iterator = iterator()
    while (iterator.hasNext()) {
        val item = iterator.next()
        if (item is T) {
            break
        }
        index++
    }
    return if (index < size) {
        removeAt(index)
        true
    } else {
        false
    }
}

inline fun <reified T : Any> MutableList<*>?.removeFirstWhen(predicate: (T) -> Boolean): Boolean {
    if (this.isNullOrEmpty()) return false
    var index = 0
    val iterator = iterator()
    while (iterator.hasNext()) {
        val item = iterator.next()
        if (item is T && predicate(item)) {
            break
        }
        index++
    }
    return if (index < size) {
        removeAt(index)
        true
    } else {
        false
    }
}

inline fun <reified T : Any> List<*>?.containsWhen(predicate: (T) -> Boolean): Boolean {
    if (this.isNullOrEmpty()) return false
    for (item in this) {
        if (item is T && predicate(item)) return true
    }
    return false
}

fun MutableList<*>?.safeRemoveAt(index: Int): Boolean {
    if (this == null) return false
    if (index < 0 || index >= size) return false
    removeAt(index)
    return true
}

fun String?.jsonToStringList() = if (this.isNullOrEmpty()) {
    null
} else {
    try {
        val list = ArrayList<String>()
        val jArray = JSONArray(this)
        for (i in 0 until jArray.length()) {
            val obj = jArray.optString(i, null) ?: continue
            list.add(obj)
        }
        list
    } catch (e: Exception) {
        null
    }
}

fun List<String>?.stringListToJson() = if (this.isNullOrEmpty()) {
    null
} else {
    try {
        val jArray = JSONArray()
        for (obj in this) {
            jArray.put(obj)
        }
        jArray.toString()
    } catch (e: Exception) {
        null
    }
}

fun String?.jsonToStringArray() = if (this.isNullOrEmpty()) {
    null
} else {
    try {
        val jArray = JSONArray(this)
        val size = jArray.length()
        val array = Array<String>(size) {
            jArray.optString(it, null).orEmpty()
        }
        array
    } catch (e: Exception) {
        null
    }
}

fun Array<String>?.stringArrayToJson() = if (this.isNullOrEmpty()) {
    null
} else {
    try {
        val jArray = JSONArray()
        for (obj in this) {
            jArray.put(obj)
        }
        jArray.toString()
    } catch (e: Exception) {
        null
    }
}

fun <T : Any, R : Any> List<T>?.iterateSegmented(threshold: Int, mapper: (T) -> R, action: (tempList: List<R>) -> Unit) {
    if (this.isNullOrEmpty()) return
    if (size < threshold) {
        action.invoke(map(mapper))
        return
    }
    var count = 0
    val tempList = ArrayList<R>(threshold)
    for (element in this) {
        tempList.add(mapper.invoke(element))
        count++
        if (count >= threshold) {
            action.invoke(tempList)
            tempList.clear()
            count = 0
        }
    }
    if (count > 0) {
        action.invoke(tempList)
        tempList.clear()
    }
}

fun <T : Any> List<T>?.iterateSegmented(threshold: Int, action: (tempList: List<T>) -> Unit) {
    if (this.isNullOrEmpty()) return
    if (size < threshold) {
        action.invoke(this)
        return
    }
    var count = 0
    val tempList = ArrayList<T>(threshold)
    for (element in this) {
        tempList.add(element)
        count++
        if (count >= threshold) {
            action.invoke(tempList)
            tempList.clear()
            count = 0
        }
    }
    if (count > 0) {
        action.invoke(tempList)
        tempList.clear()
    }
}

fun <T : Any> List<T>?.removeDuplication(): List<T>? {
    if (this.isNullOrEmpty()) return null
    return associateBy { it }.keys.toList()
}