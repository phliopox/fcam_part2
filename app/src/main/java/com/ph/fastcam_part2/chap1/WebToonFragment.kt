package com.ph.fastcam_part2.chap1

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.ph.fastcam_part2.R
import com.ph.fastcam_part2.databinding.Chap1Binding

class WebToonFragment : Fragment() {
    private lateinit var binding : Chap1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Chap1Binding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webView = binding.webView

        webView.apply {
            webViewClient = WebToonWebViewClient(binding.progressBar)
            settings.javaScriptEnabled = true //웹뷰 내부 js 허용
            loadUrl("https://comic.naver.com/webtoon/detail?titleId=795297&no=54")
        }
    }

    fun canGoBack():Boolean{
        return binding.webView.canGoBack()
    }
    fun goBack(){
        binding.webView.goBack()
    }
}