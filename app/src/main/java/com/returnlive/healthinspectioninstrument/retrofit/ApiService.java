package com.returnlive.healthinspectioninstrument.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by 张梓彬 on 2017/8/24 0024.
 */

public interface ApiService {
    @GET("/")
    Call<ResponseBody> getMessage();
}
