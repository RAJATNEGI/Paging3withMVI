package com.rajat.paging3withmvi.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val TMDB_BASEURL = "https://api.themoviedb.org/"

    /**
     * Go to https://www.themoviedb.org/  and register and generate your api key
     * */
    private const val TMDB_API_KEY = "<YOUR API KEY>"

    private lateinit var INSTANCE: Retrofit
    private fun getInstance(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url

            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", TMDB_API_KEY)
                .build()

            chain.proceed(original.newBuilder().url(url).build())
        }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(TMDB_BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()

        INSTANCE = retrofit
        return INSTANCE
    }

    fun <T : Any> getService(clazz: Class<T>): T {
        return getInstance().create(clazz)
    }
}
