package com.zfy.component.basic.foundation.extend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.march.common.exts.EmptyX;
import com.march.common.exts.StreamX;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * CreateAt : 2018/3/8
 * Describe :
 *
 * @author chendongeiyo
 */
public class Transformers {

    /**
     * 切换线程，上游为 io 线程，下游为 主 线程
     *
     * @param <T> Origin
     * @return 切换线程的 ObservableTransformer
     */
    public static <T> ObservableTransformer<T, T> checkOutApiThread() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static ObservableTransformer<ResponseBody, File> toFile(final File file) {
        return new ObservableTransformer<ResponseBody, File>() {
            @Override
            public ObservableSource<File> apply(Observable<ResponseBody> upstream) {
                return upstream.map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody resp) throws Exception {
                        return StreamX.saveStreamToFile(file, resp.byteStream());
                    }
                });
            }
        };
    }


    public static ObservableTransformer<ResponseBody, Bitmap> toBitmap(final BitmapFactory.Options options) {
        return new ObservableTransformer<ResponseBody, Bitmap>() {
            @Override
            public ObservableSource<Bitmap> apply(Observable<ResponseBody> upstream) {
                return upstream.map(new Function<ResponseBody, Bitmap>() {
                    @Override
                    public Bitmap apply(ResponseBody resp) throws Exception {
                        byte[] bytes = StreamX.saveStreamToBytes(resp.byteStream());
                        if (!EmptyX.isEmpty(bytes)) {
                            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                        }
                        return null;
                    }
                });
            }
        };
    }

}
