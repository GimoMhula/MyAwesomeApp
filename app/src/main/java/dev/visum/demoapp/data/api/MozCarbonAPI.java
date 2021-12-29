package dev.visum.demoapp.data.api;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import dev.visum.demoapp.data.local.KeyStoreLocal;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MozCarbonAPI {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://3.10.223.89/api/v1/";
//    private static final String BASE_URL = "http://34.237.238.49/api/v1/";
//    private static final String BASE_URL = "http://192.168.43.200:8000/api/v1/";

    public static Retrofit getRetrofit(@NotNull Context context) {
        OkHttpClient httpClient = new OkHttpClient().newBuilder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder requestBuilder = chain.request().newBuilder();
                // NOTE: Legacy support until production release
                if (KeyStoreLocal.getInstance(context).getUser() != null || KeyStoreLocal.getInstance(context).getToken() != null) {
                    requestBuilder.header("Authorization", KeyStoreLocal.getInstance(context).getUser() != null ? KeyStoreLocal.getInstance(context).getUser().getToken() : KeyStoreLocal.getInstance(context).getToken());
                }
                return chain.proceed(requestBuilder.build());
            }
        }).build();

        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

