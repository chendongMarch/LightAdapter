package com.zfy.component.basic.foundation.api.converts

import retrofit2.Converter

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * CreateAt : 2018/9/8
 * Describe :
 *
 * @author chendong
 */
class StringConvertFactory : Converter.Factory() {

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        return if (type === String::class.java) {
            Converter<ResponseBody, String> { value ->
                value.string()
            }
        } else null
    }

    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<Annotation>?, methodAnnotations: Array<Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
        return if (type === String::class.java) {
            Converter<String, RequestBody> { value -> RequestBody.create(MediaType.parse("text/plain"), value) }
        } else null
    }

    companion object {
        @JvmStatic
        fun create(): StringConvertFactory {
            return StringConvertFactory()
        }
    }
}