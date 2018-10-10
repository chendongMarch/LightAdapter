package com.zfy.component.basic.foundation.api.transformers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.march.common.exts.StreamX
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.File


class RespToBmTransformer(private val opts: BitmapFactory.Options) : ObservableTransformer<ResponseBody, Bitmap?> {
    override fun apply(upstream: Observable<ResponseBody>): ObservableSource<Bitmap?> {
        return upstream.map<Bitmap> { resp ->
            val bytes = StreamX.saveStreamToBytes(resp.byteStream())
            if (bytes != null && bytes.isNotEmpty()) {
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size, opts)
            } else throw IllegalStateException("can not convert to bitmap")
        }
    }
}

class RespToFileTransformer(private val toFile: File) : ObservableTransformer<ResponseBody, File> {
    override fun apply(upstream: Observable<ResponseBody>): ObservableSource<File> {
        return upstream.map { resp -> StreamX.saveStreamToFile(toFile, resp.byteStream()) }
    }
}

class ApiSchedulerTransformer<T> : ObservableTransformer<T, T> {
    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}