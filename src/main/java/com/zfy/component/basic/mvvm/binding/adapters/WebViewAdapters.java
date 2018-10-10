package com.zfy.component.basic.mvvm.binding.adapters;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.webkit.WebView;

/**
 * CreateAt : 2018/9/10
 * Describe : ViewPager
 * bindHtml
 * bindUrl
 *
 * @author chendong
 */
public class WebViewAdapters {

    @BindingAdapter(value = {"bindHtml"})
    public static void bindHtml(
            final WebView webView,
            final String html) {
        if (!TextUtils.isEmpty(html)) {
            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        }
    }

    @BindingAdapter(value = {"bindUrl"})
    public static void bindUrl(
            final WebView webView,
            final String url) {
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
    }
}
