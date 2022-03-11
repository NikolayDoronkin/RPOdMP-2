package app.com.dogfood.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.com.dogfood.R
import app.com.dogfood.api.onliner.Product
import app.com.dogfood.core.BaseFragment
import app.com.dogfood.core.extension.getActivityViewModelExt
import app.com.dogfood.core.extension.getViewModelExt
import app.com.dogfood.core.extension.navigateExt
import app.com.dogfood.core.extension.showExt
import app.com.dogfood.data.ProductResult
import app.com.dogfood.databinding.FragmentProductBinding
import app.com.dogfood.db.entity.Favorites
import app.com.dogfood.ui.activity.MainViewModel


class ProductFragment : BaseFragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var productViewModel: ProductViewModel
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        arguments?.let {
            val args = ProductFragmentArgs.fromBundle(it)
            val key = args.key
            productViewModel = getViewModelExt { ProductViewModel(key) }
            userId = getActivityViewModelExt { MainViewModel() }.account?.id
            initObservers()
            productViewModel.existFavorites(userId)
        }
        return binding.root
    }

    private fun initObservers() {
        productViewModel.product.observe(viewLifecycleOwner) {
            when (it) {
                is ProductResult.Success -> {
                    with(it.data) {
                        binding.nameView.text = fullName
                        binding.descriptionView.text = description
                        showPrice(this)
                        showImage(this)
                        initListeners(this)
                    }
                }
                is ProductResult.Error -> {
                    showErrorSnackbar(R.string.error_app, R.string.snackbar_retry)
                    { productViewModel.load() }
                }
            }
        }
        productViewModel.isFavorites.observe(viewLifecycleOwner) {
            it?.let { b -> binding.favoritesChip.isChecked = b }
        }
    }

    private fun showPrice(product: Product) {
        product.prices?.let {
            var priceStr = ""
            it.priceMin?.let { p -> priceStr += p.amount + " " + p.currency }
            it.priceMax?.let { p ->
                if (p.amount != it.priceMin?.amount) {
                    priceStr += " - " + p.amount + " " + p.currency
                }
            }
            if (priceStr.isNotEmpty()) {
                it.offers?.count?.let { c ->
                    val amountStr = getString(R.string.offers)
                    priceStr += " ($c $amountStr)"
                }
                binding.priceView.text = priceStr
            }
        }
    }

    private fun showImage(product: Product) {
        val imageUrl = product.images?.header
        if (imageUrl != null && imageUrl.isNotEmpty()) {
            binding.imageView.showExt("https://$imageUrl")
        } else {
            binding.imageView.setImageResource(R.drawable.ic_image_24)
        }
    }

    private fun initListeners(product: Product) {
        binding.btnLink.setOnClickListener {
            navigateExt(ProductFragmentDirections.actionNavProductToNavBrowser(product.htmlUrl))
        }

        binding.favoritesChip.setOnCheckedChangeListener { _, b ->
            userId?.let {
                if (b) {
                    val favorites = Favorites(it, product.key)
                    favorites.name = product.name
                    favorites.description = product.description
                    favorites.imageUrl = product.images?.header
                    productViewModel.saveFavorites(favorites)
                } else {
                    productViewModel.deleteFavorites(it)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}