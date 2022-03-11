package app.com.dogfood.ui.products

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.com.dogfood.R
import app.com.dogfood.api.onliner.KeyType
import app.com.dogfood.api.onliner.Query
import app.com.dogfood.core.BaseFragment
import app.com.dogfood.core.extension.getViewModelExt
import app.com.dogfood.core.extension.hideSoftKeyboardExt
import app.com.dogfood.core.extension.navigateExt
import app.com.dogfood.data.ProductsResult
import app.com.dogfood.databinding.FragmentSearchBinding

private const val DEFAULT_QUERY = "Royal Canin"

class SearchFragment : BaseFragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagedProductsViewModel: PagedProductsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val defaultQuery = Query(KeyType.PRODUCTS)
        defaultQuery.searchQuery = DEFAULT_QUERY
        pagedProductsViewModel = getViewModelExt { PagedProductsViewModel(defaultQuery) }
        addDividers()
        bindState()
        bindSearch()
        return binding.root
    }

    private fun bindState() {
        val adapter = ProductsAdapter()
        binding.list.adapter = adapter
        adapter.shotClickListener = { item, _ ->
            navigateExt(SearchFragmentDirections.actionNavSearchToNavProduct(item.key))
        }
        bindList(adapter)
    }

    private fun bindSearch() {
        binding.searchView.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateListFromInput()
                true
            } else {
                false
            }
        }
        binding.searchView.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateListFromInput()
                true
            } else {
                false
            }
        }
        pagedProductsViewModel.state.map { it.query.searchQuery }.distinctUntilChanged()
            .observe(viewLifecycleOwner, binding.searchView::setText)
    }

    private fun updateListFromInput() {
        binding.searchView.text.trim().let {
            if (it.isNotEmpty()) {
                binding.list.scrollToPosition(0)
                hideSoftKeyboardExt()
                val query = Query(KeyType.PRODUCTS)
                query.searchQuery = it.toString()
                showProgress()
                pagedProductsViewModel.accept(UiAction.Search(query))
            }
        }
    }

    private fun bindList(adapter: ProductsAdapter) {
        setupScrollListener(pagedProductsViewModel.accept)
        pagedProductsViewModel.state.map(UiState::productsResult).distinctUntilChanged()
            .observe(viewLifecycleOwner) { result ->
                dissmissProgress()
                when (result) {
                    is ProductsResult.Success -> {
                        showEmptyList(result.data.isEmpty())
                        adapter.submitList(result.data)
                    }
                    is ProductsResult.Error -> {
                        showErrorSnackbar(R.string.error_app, R.string.snackbar_retry)
                            {
                                pagedProductsViewModel.retry()
                                showProgress()
                            }
                    }
                }
            }
    }

    private fun showEmptyList(show: Boolean) {
        binding.emptyList.isVisible = show
        binding.list.isVisible = !show
    }

    private fun setupScrollListener(onScrollChanged: (UiAction.Scroll) -> Unit) {
        val layoutManager = binding.list.layoutManager as LinearLayoutManager
        binding.list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                onScrollChanged(
                    UiAction.Scroll(visibleItemCount, lastVisibleItem, totalItemCount)
                )
            }
        })
    }    private fun showProgress() {
        binding.progressView.visibility = View.VISIBLE
        binding.mainLayout.alpha = 0.3f
    }

    private fun dissmissProgress() {
        binding.progressView.visibility = View.GONE
        binding.mainLayout.alpha = 1.0f
    }

    private fun addDividers() {
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}