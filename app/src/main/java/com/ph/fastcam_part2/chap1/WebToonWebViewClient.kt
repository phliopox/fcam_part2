package com.ph.fastcam_part2.chap1

import android.graphics.Bitmap
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar

class WebToonWebViewClient(private val progressBar : ProgressBar) :WebViewClient(){
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        progressBar.visibility = View.VISIBLE
    }
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        progressBar.visibility = View.GONE
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
    // null 이거나 naver 웹툰 페이지가 아니면 true 를 반환한다 -> true 반환시 url loading 작업이 막힘.
     return !(request != null && request.url.toString().contains("comic.naver.com"))
    }
}