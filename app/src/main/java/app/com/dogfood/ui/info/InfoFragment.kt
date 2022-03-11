package app.com.dogfood.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.com.dogfood.R
import app.com.dogfood.core.BaseFragment
import app.com.dogfood.databinding.FragmentInfoBinding

class InfoFragment : BaseFragment() {

    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        initPages()
        return binding.root
    }

    private fun initPages() {
        val pages: Array<String> = resources.getStringArray(R.array.help_pages_array)
        val adapter = InfoPagerAdapter(pages)
        binding.viewPager.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}