package com.example.testdemo.http

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitFactory {
    companion object {

        private const val TIME_OUT = 3L;
        var httpLog: HttpLog? = null
        var httpLoggingInterceptor: HttpLoggingInterceptor? = null

        private val httpClient by lazy {
            createHttpClient()
        }

        private fun createHttpClient(): OkHttpClient {
            val builder = OkHttpClient.Builder()
                    .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .addInterceptor(initInterceptor())
                    .addInterceptor(HeaderInterceptor())
            return builder.build()
        }

        private fun initInterceptor(): HttpLoggingInterceptor {
            if (null == httpLoggingInterceptor) {
                if (null == httpLog) {
                    httpLog = HttpLog()
                }
                httpLoggingInterceptor = HttpLoggingInterceptor(httpLog!!)
                httpLoggingInterceptor?.level = HttpLoggingInterceptor.Level.BODY
            }
            return httpLoggingInterceptor!!
        }

        fun createRetrofit(): Retrofit {

            return Retrofit.Builder()
                    .client(httpClient)
                    .baseUrl("http://test.m.muyuhuajiaoyu.com/app/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

    }


}

class HeaderInterceptor:Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("token","799634bc-ea1e-4b2c-a55e-e114145f86ea")
        builder.addHeader("App-Version","1.10.95")
        builder.addHeader("accept","application/vnd.ipad.lshy-2.3+json")
        builder.addHeader("lsbcVersion","1.10.95+1.10.95")
        builder.addHeader("locale","zh")
        builder.addHeader("serialNumber","Y101191200192")
        builder.addHeader("ts",System.currentTimeMillis().toString())

        val request = builder.build()
        val response = chain.proceed(request)
        val responseBuilder = response.newBuilder()

        return responseBuilder.build()
    }

}

