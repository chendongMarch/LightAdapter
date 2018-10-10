package com.zfy.component.basic.foundation.api.interceptors;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * CreateAt : 2017/7/1
 * Describe : 自定义日志打印拦截器，扩展自 HttpLoggingInterceptor
 * REQ_BODY
 * REQ_HEADERS
 * RESP_BODY
 * RESP_HEADERS
 *
 * @author chendong
 */
public final class LogInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final String TAG = LogInterceptor.class.getSimpleName();

    public static final int REQ_BODY = 0;
    public static final int REQ_HEADERS = 1;
    public static final int RESP_BODY = 2;
    public static final int RESP_HEADERS = 3;

    private Set<Integer> mLogPartList;

    public LogInterceptor() {
        mLogPartList = new HashSet<>();
        mLogPartList.add(REQ_BODY);
        mLogPartList.add(REQ_HEADERS);
        mLogPartList.add(RESP_BODY);
        mLogPartList.add(RESP_HEADERS);
    }

    public LogInterceptor exclude(Integer... parts) {
        for (Integer part : parts) {
            mLogPartList.remove(part);
        }
        return this;
    }

    private void log(String msg) {
        breakLog4K(msg, resp -> Log.i(TAG + "|network", resp));
    }

    private boolean isLogPart(int part) {
        return mLogPartList.contains(part);
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        return proceedResponse(chain.proceed(proceedRequest(chain.request())));
    }


    protected Request proceedRequest(Request request) {
        try {
            logRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }

    protected Response proceedResponse(Response response) {
        try {
            logResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    // 打印 response
    private void logResponse(Response response) throws IOException {
        if (response == null || !isLogPart(RESP_BODY) && !isLogPart(RESP_HEADERS)) {
            return;
        }
        log("\n=============================== Response Start =======================================================\n");

        StringBuilder sb = new StringBuilder();
        long tookMs = 0;//TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        sb.append("[ ").append(response.request().method()).append(" ]  ").append(response.request().url()).append("\n");
        sb.append("Cost:").append(tookMs).append("ms ,code = ").append(response.code())
                .append(" ,msg = ").append(response.message()).append("\n");
        if (isLogPart(RESP_HEADERS)) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                sb.append("headers:[ ").append(headers.name(i)).append(": ").append(headers.value(i)).append(" ]").append("\n");
            }
        }
        if (response.request().url().toString().endsWith("js")) {
            log("请求 js 文件 =>" + response.request().url() +" 结束");
            log("\n=============================== Request End =======================================================\n\n");
            return;
        }
        if (isLogPart(RESP_BODY)) {
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                sb.append("无");
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }
                if (!isPlaintext(buffer)) {
                    sb.append("binary size = ").append(buffer.size()).append(" -byte");
                } else if (responseBody.contentLength() != 0) {
                    String respStr = buffer.clone().readString(charset == null ? Charset.forName("utf-8") : charset);
                    try {
                        if (respStr.startsWith("{")) {
                            JSONObject jsonObject = new JSONObject(respStr);
                            respStr = jsonObject.toString(2);
                        } else if (respStr.startsWith("[")) {
                            JSONArray jsonArray = new JSONArray(respStr);
                            respStr = jsonArray.toString(2);
                        } else {
                            respStr = "无法转换 JsonObj+\n" + respStr;
                        }
                        if (respStr.length() > 500) {
                            respStr = respStr.substring(0, 490) + "...";
                        }
                    } catch (Exception e) {
                       // e.printStackTrace();
                    }
                    sb.append(respStr);
                }
            }
        }
        log(sb.toString());
        log("\n=============================== Response End =======================================================\n\n");
    }


    // 打印 request
    private void logRequest(Request request) throws Exception {
        if (request == null || !isLogPart(REQ_HEADERS) && !isLogPart(REQ_BODY)) {
            return;
        }
        log("\n=============================== Request Start =======================================================\n");

        StringBuilder sb = new StringBuilder();
        sb.append("[ ").append(request.method()).append(" ], url = ").append(request.url()).append("\n");
        Headers headers = request.headers();
        if (isLogPart(REQ_HEADERS)) {
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                sb.append("headers:[ ").append(name).append(": ").append(headers.value(i)).append(" ]").append("\n");
            }
        }

        if (isLogPart(REQ_BODY)) {
            sb.append("Request Body: ");
            RequestBody requestBody = request.body();
            if (requestBody == null) {
                sb.append("无");
            } else {
                if (requestBody.contentType() != null) {
                    sb.append("Content-Type: ").append(requestBody.contentType()).append("\n");
                }
                if (requestBody.contentLength() != -1) {
                    sb.append("Content-Length: ").append(requestBody.contentLength()).append("\n");
                }
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }
                if (isPlaintext(buffer) && charset != null) {
                    sb.append(buffer.readString(charset));
                } else {
                    sb.append("不可读的 body");
                }
            }
        }
        String respStr = sb.toString();
        log(respStr);
        log("\n=============================== Request End =======================================================\n\n");
    }


    public interface Consumer<T> {
        void accept(T resp);
    }

    private static void breakLog4K(String msg, Consumer<String> consumer) {
        if (msg.length() < 3800) {
            consumer.accept(msg);
            return;
        }
        int index = 0; // 当前位置
        int max = 3800;// 需要截取的最大长度,别用4000
        String sub;    // 进行截取操作的string
        while (index < msg.length()) {
            if (msg.length() < max) { // 如果长度比最大长度小
                max = msg.length();   // 最大长度设为length,全部截取完成.
                sub = msg.substring(index, max);
            } else {
                sub = msg.substring(index, max);
            }
            consumer.accept(sub);      // 进行输出
            index = max;
            max += 3800;
        }
    }


    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

}
