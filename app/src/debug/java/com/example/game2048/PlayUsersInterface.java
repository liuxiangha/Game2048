package com.example.game2048;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PlayUsersInterface {
    @FormUrlEncoded
    @POST("playUser/checkPlayUser")
    Call<String> checkResult(@Field("playUserId") Long playUserId,
                             @Field("playUserPassword") String playUserPassword);

    @FormUrlEncoded
    @POST("playUser/add")
    Call<String> addPlayUser(@Field("userId") Long userId, @Field("userName") String userName,
                             @Field("userPassword") String userPassword, @Field("userGender") String userGender);
}
