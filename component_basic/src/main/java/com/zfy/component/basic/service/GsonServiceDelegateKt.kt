package com.zfy.component.basic.service

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * CreateAt : 2018/9/8
 * Describe : JSON 解析服务
 *
 * @author chendong
 */
open class GsonServiceDelegateKt : IJsonService {

    override fun init(context: Context?) {

    }

    private val gson by lazy { Gson() }

    private fun throwError(msg: String) {
        throw IllegalArgumentException("check args please ! $msg")
    }

    override fun <T : Any?> json2Object(input: String?, clazz: Class<T>?): T? {
        return toObj(input, clazz)
    }

    override fun object2Json(instance: Any?): String? {
        return toJson(instance)
    }

    override fun <T : Any?> parseObject(json: String?, type: Type?): T? {
        json ?: throwError("json is null")
        type ?: throwError("type is null")
        return gson.fromJson<T>(json, type)
    }

    override fun toJson(obj: Any?): String? {
        obj ?: throwError("obj is null")
        return gson.toJson(obj)
    }

    override fun <T> toObj(json: String?, clazz: Class<T>?): T? {
        json ?: throwError("json is null")
        clazz ?: throwError("class is null")
        return gson.fromJson(json, clazz)
    }

    override fun <T> toList(json: String?, clazz: Class<T>?): MutableList<T>? {
        json ?: throwError("json is null")
        clazz ?: throwError("class is null")
        val type = object : TypeToken<List<T>>() {}.type
        return parseObject(json, type) ?: mutableListOf()
    }

    override fun <K, V> toMap(json: String?, kClazz: Class<K>?, vClazz: Class<V>?): MutableMap<K, V>? {
        json ?: throwError("json is null")
        kClazz ?: throwError("key class is null")
        vClazz ?: throwError("value class is null")
        val type = object : TypeToken<Map<K, V>>() {}.type
        return parseObject(json, type)
    }
}
