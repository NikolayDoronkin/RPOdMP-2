package app.com.dogfood.data

import app.com.dogfood.api.onliner.OnlinerService

class OnlinerRepository {

    private val service = OnlinerService.create()

    suspend fun getProductAsync(key: String) =
        service.getProduct(key)
}