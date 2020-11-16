package com.qingeng.apilibrary.http;

import com.qingeng.apilibrary.config.AppPreferences;
import com.qingeng.apilibrary.contact.URLConstant;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpServiceManager {

    private static HttpServiceManager sInstance;


    private static final int DEFAULT_TIMEOUT = 60; //连接 超时的时间，单位：秒

    private static OkHttpClient client;
    private static OkHttpClient clientNoToken;

    private static Retrofit retrofit;
    private static Retrofit retrofitNoToken;
    private static RetrofitInterface retrofitInterface;
    private static RetrofitInterface retrofitInterfaceNoToken;

    protected static HttpServiceManager getInstance() {
        if (null == sInstance) {
            sInstance = new HttpServiceManager();
        }
        return sInstance;
    }


    public synchronized static RetrofitInterface getRetrofit(boolean withToken) {
        //初始化retrofit的配置

        if (withToken){
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(URLConstant.URL_BASE)
                        .client(initOkHttpClient(withToken))
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
                retrofitInterface = retrofit.create(RetrofitInterface.class);
            }
            return retrofitInterface;
        }else {
            if (retrofitNoToken == null) {
                retrofitNoToken = new Retrofit.Builder()
                        .baseUrl(URLConstant.URL_BASE)
                        .client(initOkHttpClient(withToken))
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
                retrofitInterfaceNoToken = retrofitNoToken.create(RetrofitInterface.class);
            }
            return retrofitInterfaceNoToken;
        }
    }

    public synchronized static RetrofitInterface getRetrofit() {
       return getRetrofit(true);
    }


    private static OkHttpClient initOkHttpClient(final boolean withToken) {
        if (withToken){
            if (client == null) {
                try {
                    client = new OkHttpClient
                            .Builder()
                            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .addInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request original = chain.request();
                                    Request request = null;
                                    if (AppPreferences.getHttpToken() != null) {
                                        request = original.newBuilder()
                                                .addHeader("token", AppPreferences.getHttpToken())
                                                .method(original.method(), original.body())
                                                .build();
                                        System.out.println("token---" + AppPreferences.getHttpToken());
                                    } else {
                                        request = original.newBuilder()
                                                .method(original.method(), original.body())
                                                .build();
                                    }
                                    return chain.proceed(request);
                                }
                            })
                            .build();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
            return client;
        }else {
            if (clientNoToken == null) {
                try {
                    clientNoToken = new OkHttpClient
                            .Builder()
                            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .addInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request original = chain.request();
                                    Request request =  original.newBuilder()
                                                .method(original.method(), original.body())
                                                .build();
                                    return chain.proceed(request);
                                }
                            })
                            .build();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
            return clientNoToken;
        }

    }


}
