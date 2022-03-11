package app.com.dogfood.api.onliner

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface OnlinerService {

    @GET("search/{key}")
    suspend fun search(
        @Path("key") key: String,
        @QueryMap paramsMap: Map<String, String>
    ): Products

    @GET("products/{key}")
    suspend fun getProduct(
        @Path("key") key: String
    ): Product

    companion object {
        private const val BASE_URL = "https://catalog.api.onliner.by/"

        fun create(): OnlinerService {
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(OnlinerService::class.java)
        }
    }
}
