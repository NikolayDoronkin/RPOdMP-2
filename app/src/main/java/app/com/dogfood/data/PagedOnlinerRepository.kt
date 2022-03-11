package app.com.dogfood.data

import android.util.Log
import app.com.dogfood.api.onliner.OnlinerService
import app.com.dogfood.api.onliner.Product
import app.com.dogfood.api.onliner.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.HttpException
import java.io.IOException

class PagedOnlinerRepository {

    private val service = OnlinerService.create()
    private val inMemoryCache = mutableListOf<Product>()
    private val results = MutableSharedFlow<ProductsResult>(replay = 1)
    private var lastRequestedPage = 1
    private var lastPage = 1
    private var isRequestInProgress = false

    suspend fun start(query: Query): Flow<ProductsResult> {
        lastRequestedPage = 1
        inMemoryCache.clear()
        requestAndSaveData(query)
        return results
    }

    suspend fun requestMore(query: Query) {
        if (isRequestInProgress || lastRequestedPage > lastPage) return
        val successful = requestAndSaveData(query)
        if (successful) {
            lastRequestedPage++
        }
    }

    suspend fun retry(query: Query) {
        if (isRequestInProgress) return
        requestAndSaveData(query)
    }

    private suspend fun requestAndSaveData(query: Query): Boolean {
        isRequestInProgress = true
        var successful = false
        try {
            query.page = lastRequestedPage
            val response = service.search(query.key(), query.map())
            
            lastPage = response.page.last
            Log.d("CatfoodRepository", "response $response")
            inMemoryCache.addAll(response.products)

            val newList = mutableListOf<Product>()
            newList.addAll(inMemoryCache)

            results.emit(ProductsResult.Success(newList))
            successful = true
        } catch (e: IOException) {
            results.emit(ProductsResult.Error(e))
        } catch (e: HttpException) {
            results.emit(ProductsResult.Error(e))
        }
        isRequestInProgress = false
        return successful
    }

}