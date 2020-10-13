package com.example.game2048;

import java.sql.Date;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlayGameInterface {
    @FormUrlEncoded
    @POST("playUser/checkPlayUser")
    Call<String> checkResult(@Field("playUserId") Long playUserId,
                             @Field("playUserPassword") String playUserPassword);

    @GET("playUser/getUserNameById")
    Call<String> getUserNameById(@Query("userId") Long userId);

    @FormUrlEncoded
    @POST("playUser/add")
    Call<String> addPlayUser(@Field("userId") Long userId, @Field("userName") String userName,
                             @Field("userPassword") String userPassword, @Field("userGender") String userGender);

    @FormUrlEncoded
    @POST("play2048/addNewRecord")
    Call<String> addNew2048Record(@Field("userId") Long userId, @Field("score") int score,
                                  @Field("playTime") Date playTime, @Field("finialResult") String finialResult);
    @GET("play2048/getMaxScoreById?userId={userId}")
    Call<String> get2048MaxScoreById(@Query("userId") Long userId);

    @FormUrlEncoded
    @POST("playBoard/addNewBoardRecord")
    Call<String> addNewBoardRecord(@Field("userId") Long userId, @Field("takeTime") int takeTime,
                                   @Field("playTime") Date playTime, @Field("initialBoard") String initialBoard);

    @GET("playBoard/getMinTakeTimeById")
    Call<String> getBoardMinTakeTimeById(@Query("userId") Long userId);
}
