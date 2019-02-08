package me.example.vacancies.repository.network

import android.content.Context
import me.example.vacancies.models.Constants
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class CachedRetrofitProvider(context: Context) : RetrofitProvider {
    private val cacheSize = (10 * 1024 * 1024).toLong()
    private val cache = Cache(context.cacheDir, cacheSize)

    override fun getRetrofit(): Retrofit {
        val networkCacheInterceptor = Interceptor { chain ->
            val response = chain.proceed(chain.request())

            val cacheControl = CacheControl.Builder()
                .maxAge(1, TimeUnit.MINUTES)
                .build()

            response.newBuilder()
                .header("Cache-Control", cacheControl.toString())
                .build()
        }
        val okHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor(networkCacheInterceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl(Constants.BaseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}