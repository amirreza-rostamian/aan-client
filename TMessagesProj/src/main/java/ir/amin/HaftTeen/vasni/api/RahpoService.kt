package ir.amin.HaftTeen.vasni.api

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import ir.amin.HaftTeen.vasni.utils.OksHttpClient
import ir.amin.HaftTeen.BuildConfig
import java.util.concurrent.TimeUnit


object RahpoService {
    val apiInterface: ApiInterface
    val gson = GsonBuilder().setLenient().create()

    var okHttpClient = OksHttpClient.getUnsafeOkHttpClient().newBuilder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.UPLOAD_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(JacksonConverterFactory.create(createObjectMapper()))
            .client(okHttpClient)
            .build()

    init {
        apiInterface = retrofit.create(ApiInterface::class.java)
    }

    fun createObjectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false)
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
        return objectMapper
    }

}