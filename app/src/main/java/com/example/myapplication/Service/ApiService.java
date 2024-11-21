package com.example.myapplication.Service;

import com.example.myapplication.models.Distributor;
import com.example.myapplication.models.Respone;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    String BASE_URL = "http://192.168.1.4:3000/";

    @GET("api/get-list-distributor")
    Call<Respone<ArrayList<Distributor>>> getDistributors();

    @POST("api/add-distributor")
    Call<Respone<Distributor>> addDistributor(@Body Distributor distributor);

    @PUT("api/update-distributor-by-id/{id}")
    Call<Respone<Distributor>> updateDistributor(@Path("id") String id, @Body Distributor distributor);

    @DELETE("api/destroy-distributor-by-id/{id}")
    Call<Respone<Distributor>> deleteDistributor(@Path("id") String id);

    @GET("api/search-distributor")
    Call<Respone<ArrayList<Distributor>>> searchDistributors(@Query("key") String key );
}
