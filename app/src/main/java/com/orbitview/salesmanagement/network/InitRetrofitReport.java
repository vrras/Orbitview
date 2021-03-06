package com.orbitview.salesmanagement.network;

import com.orbitview.salesmanagement.config.Config;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InitRetrofitReport {
    public static Retrofit setInit() {
        return new Retrofit.Builder()
                .baseUrl(Config.REPORT_URL[Config.BUILD_TYPE])
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiInterface getInstance() {
        return setInit().create(ApiInterface.class);
    }
}
