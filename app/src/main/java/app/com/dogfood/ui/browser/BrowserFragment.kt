package app.com.dogfood.ui.browser

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import app.com.dogfood.R
import app.com.dogfood.core.BaseFragment
import app.com.dogfood.databinding.FragmentBrowserBinding

class BrowserFragment : BaseFragment() {

    private var _binding: FragmentBrowserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrowserBinding.inflate(inflater, container, false)
        arguments?.let {
            val args = BrowserFragmentArgs.fromBundle(it)
            initBrowser(args.link)
        }
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initBrowser(link: String) {
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                binding.progressView.visibility = View.VISIBLE
                return true
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                binding.progressView.visibility = View.GONE
            }

            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                showErrorSnackbar(R.string.error_app, R.string.snackbar_retry)
                { binding.webview.loadUrl(link) }
            }
        }
        binding.webview.loadUrl(link)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}