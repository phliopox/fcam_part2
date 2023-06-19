package com.ph.fastcam_part2.chap1

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.edit
import com.ph.fastcam_part2.WEB_HISTORY
import com.ph.fastcam_part2.databinding.Chap1Binding

class WebToonFragment(private val position: Int, private val webViewUrl: String) : Fragment() {
    private lateinit var binding: Chap1Binding
    var listener: OnTabLayoutNameChanged? = null

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
                activity?.getSharedPreferences(WEB_HISTORY, Context.MODE_PRIVATE)?.edit {
                    putString("tab$position", url)
                }

            }
            settings.javaScriptEnabled = true //웹뷰 내부 js 허용
            loadUrl(webViewUrl)
        }

        binding.backToLastButton.setOnClickListener {
            val pref = activity?.getSharedPreferences(WEB_HISTORY, Context.MODE_PRIVATE)
            val url = pref?.getString("tab$position", "")
            if (url.isNullOrEmpty()) {
                Toast.makeText(context, "마지막 저장 시점이 없습니다", Toast.LENGTH_SHORT).show()
            } else {
                binding.webView.loadUrl(url)
            }
        }

        binding.changeTabNameButton.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
            val edittext = EditText(context)
            dialog.apply {
                setView(edittext)
                setPositiveButton("저장") { _, _ ->
                    activity?.getSharedPreferences(WEB_HISTORY, Context.MODE_PRIVATE)?.edit {
                        putString("tab${position}_name", edittext.text.toString())
                        listener?.nameChanged(position,edittext.text.toString())
                    }
                }
                setNegativeButton("취소") { dialogInterface, _ ->
                    dialogInterface.cancel()
                }
                show()
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

interface OnTabLayoutNameChanged {
    fun nameChanged(position: Int, name: String)
}