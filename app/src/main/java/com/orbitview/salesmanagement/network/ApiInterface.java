package com.orbitview.salesmanagement.network;

import com.orbitview.salesmanagement.model.LoginResponse;
import com.orbitview.salesmanagement.model.ReportResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> doLogin(@Field("username") String username,
                                @Field("password") String password);

    @FormUrlEncoded
    @POST("report")
    Call<ReportResponse> getReport(@Field("startdate") String startdate,
                                   @Field("enddate") String enddate);

    @GET("image")
    @Streaming
    Call<ResponseBody> getImageDetails(@Query("key") String key,
                                       @Query("id") String imageUrl);
}
