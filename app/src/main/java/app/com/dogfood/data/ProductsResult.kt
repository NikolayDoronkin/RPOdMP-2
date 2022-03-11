package app.com.dogfood.data

import app.com.dogfood.api.onliner.Product

sealed class ProductsResult {
    data class Success(val data: List<Product>) : ProductsResult()
    data class Error(val error: Exception) : ProductsResult()
}

sealed class ProductResult {
    data class Success(val data: Product) : ProductResult()
    data class Error(val error: Exception) : ProductResult()
}
