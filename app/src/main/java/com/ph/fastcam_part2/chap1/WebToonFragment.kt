package com.ph.fastcam_part2.chap1

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.content.edit
import com.ph.fastcam_part2.R
import com.ph.fastcam_part2.databinding.Chap1Binding

class WebToonFragment(private val position: Int) : Fragment() {
    private lateinit var binding: Chap1Binding

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
            webViewClient = WebToonWebViewClient(binding.progressBar) { url ->
                activity?.getSharedPreferences("WEB_HISTORY", Context.MODE_PRIVATE)?.edit {
                    putString("tab$position",url)
                }

            }
            settings.javaScriptEnabled = true //웹뷰 내부 js 허용
            loadUrl("https://comic.naver.com/webtoon/detail?titleId=795297&no=54")
        }

        binding.backToLastButton.setOnClickListener {
            val pref = activity?.getSharedPreferences("WEB_HISTORY", Context.MODE_PRIVATE)
            /*(pref?.getString("tab$position", ""))?.let {
                binding.webView.loadUrl(it)
            }*/
            val url = pref?.getString("tab$position", "")
            if (url.isNullOrEmpty()) {
                Toast.makeText(context, "마지막 저장 시점이 없습니다", Toast.LENGTH_SHORT).show()
            } else {
                binding.webView.loadUrl(url)
            }
        }
    }

    fun canGoBack(): Boolean {
        return binding.webView.canGoBack()
    }

    fun goBack() {
        binding.webView.goBack()
    }
}