package app.com.dogfood.api.onliner

private const val PARAM_QUERY = "query"
private const val PARAM_TYPE_FOOD = "typefood4dog"
private const val PARAM_BRAND = "mfr"
private const val PARAM_PAGE = "page"
private const val PARAM_LIMIT = "limit"

private const val LIMIT = 30

class Query(val key: KeyType) {

    var searchQuery: String? = null
    var typefood: DogFoodType? = null
    var brand: BrandType? = null
    var page: Int = 1
    var limit: Int = LIMIT

    fun key() = key.type

    fun map(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        map[PARAM_PAGE] = page.toString()
        map[PARAM_LIMIT] = limit.toString()

        searchQuery?.let {
            map[PARAM_QUERY] = it
        }
        typefood?.let {
            map[PARAM_TYPE_FOOD] = it.type
        }
        brand?.let {
            map[PARAM_BRAND] = it.type
        }
        return map
    }
}