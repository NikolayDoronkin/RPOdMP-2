package app.com.dogfood.ui.products

import androidx.lifecycle.*
import app.com.dogfood.api.onliner.Query
import app.com.dogfood.data.PagedOnlinerRepository
import app.com.dogfood.data.ProductsResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val VISIBLE_THRESHOLD = 8

class PagedProductsViewModel(defaultQuery: Query) : ViewModel() {

    val state: LiveData<UiState>
    val accept: (UiAction) -> Unit
    private val repository = PagedOnlinerRepository()
    private val queryLiveData = MutableLiveData(defaultQuery)

    init {
        state = queryLiveData.distinctUntilChanged().switchMap { query ->
            liveData {
                val uiState = repository.start(query)
                    .map { UiState(query = query, productsResult = it) }
                    .asLiveData(Dispatchers.Main)
                emitSource(uiState)
            }
        }

        accept = { action ->
            when (action) {
                is UiAction.Search -> queryLiveData.postValue(action.query)
                is UiAction.Scroll ->
                    if (action.shouldFetchMore) {
                        queryLiveData.value?.let {
                            viewModelScope.launch { repository.requestMore(it) }
                        }
                    }
                }
            }
        }

    fun retry() {
        queryLiveData.value?.let {
            viewModelScope.launch { repository.retry(it) }
        }
    }
}

private val UiAction.Scroll.shouldFetchMore
    get() = visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount


data class UiState(
    val query: Query,
    val productsResult: ProductsResult
)

sealed class UiAction {
    data class Search(val query: Query) : UiAction()
    data class Scroll(
        val visibleItemCount: Int,
        val lastVisibleItemPosition: Int,
        val totalItemCount: Int
    ) : UiAction()
}
