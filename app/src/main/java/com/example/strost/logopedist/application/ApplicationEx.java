package com.example.strost.logopedist.application;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.strost.logopedist.Netwerk.Requests;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by strost on 20-3-2017.
 */

public class ApplicationEx extends Application{
    public static Requests restAdapter;
    public static ApplicationEx appContext;

    private Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();
            if(isOnline()){
                int maxAge = 86400;
                request = request.newBuilder()
                        .addHeader("Cache-Control", "public, max-age=" + maxAge)
                        .addHeader("application-id", "E5A95319-DFEE-9344-FF32-50448355EC00")
                        .addHeader("secret-key", "56EA686C-C995-2F8C-FF28-953EE7423800")
                        .build();
            }else{
                int maxStale = 86400;
                request = request.newBuilder()
                        .addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .addHeader("application-id", "E5A95319-DFEE-9344-FF32-50448355EC00")
                        .addHeader("secret-key", "56EA686C-C995-2F8C-FF28-953EE7423800")
                        .build();
            }
            return chain.proceed(request);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        setupRetroFit();
    }

    private void setupRetroFit(){
        File httpCacheDir;
        Cache cache = null;
        try{
            httpCacheDir = new File(getApplicationContext().getCacheDir(), "httpResponses");
            long httpCacheSize = 10 * 1024 * 1024; //10 MiB
            cache = new Cache(httpCacheDir, httpCacheSize);
        }catch(Exception ex){
            String RETROFIT = "retrofit";
            Log.e(RETROFIT, "could not create http cache", ex);
        }

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.interceptors().add(interceptor);
        int TIMEOUT = 5000;
        httpClient.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
        httpClient.readTimeout(TIMEOUT, TimeUnit.SECONDS);
        httpClient.cache(cache);

        //logging interceptor
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(httpLoggingInterceptor);

        OkHttpClient okHttpClient = httpClient.build();
        restAdapter = new Retrofit.Builder()
                .baseUrl(Requests.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Requests.class);
    }

    public boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

}
