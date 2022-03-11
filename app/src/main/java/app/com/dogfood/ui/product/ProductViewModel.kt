package app.com.dogfood.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.com.dogfood.App
import app.com.dogfood.data.OnlinerRepository
import app.com.dogfood.data.ProductResult
import app.com.dogfood.db.entity.Favorites
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ProductViewModel(val productKey: String) : ViewModel() {

    private val onlinerRepository = OnlinerRepository()
    private val favoritesRepository = App.instance.favoritesRepository

    private val _product = MutableLiveData<ProductResult>().apply { load() }
    val product: LiveData<ProductResult> = _product

    private val _isFavorites = MutableLiveData<Boolean>()
    val isFavorites: LiveData<Boolean> = _isFavorites

    fun load() {
        viewModelScope.launch {
            val value = try {
                ProductResult.Success(onlinerRepository.getProductAsync(productKey))
            } catch (e: IOException) {
                ProductResult.Error(e)
            } catch (e: HttpException) {
                ProductResult.Error(e)
            }
            _product.postValue(value)
        }
    }

    fun saveFavorites(favorites: Favorites) {
        viewModelScope.launch {
            favoritesRepository.insert(favorites)
        }
    }

    fun deleteFavorites(userId: String) {
        viewModelScope.launch {
            favoritesRepository.delete(Favorites(userId, productKey))
        }
    }

    fun existFavorites(userId: String?) {
        userId?.let { u ->
            viewModelScope.launch {
                _isFavorites.value = favoritesRepository.existFavorites(u, productKey)
            }
        }
    }

}