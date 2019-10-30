package ir.amin.HaftTeen.vasni.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import ir.amin.HaftTeen.vasni.model.api.ResponseGetBots;
import ir.amin.HaftTeen.vasni.model.api.ResponseGetQuestion;
import ir.amin.HaftTeen.vasni.model.api.getstar.ResponseGetStar;
import ir.amin.HaftTeen.vasni.model.api.listquestionmatchoffline.ResponseListQuestionMatchOffline;
import ir.amin.HaftTeen.vasni.model.api.login.ResponseLogin;
import ir.amin.HaftTeen.vasni.model.api.matchoffline.sendanswer.ModelResponseSendAnswer;
import ir.amin.HaftTeen.vasni.model.api.playerchart.ResponseChart;
import ir.amin.HaftTeen.vasni.model.api.profile.ResponseProfile;
import ir.amin.HaftTeen.vasni.model.api.register.ResponseRegister;
import ir.amin.HaftTeen.vasni.model.api.splash.ResponseModelSplashApi;

public interface Api {

    @GET("api/matches2")
    Call<ResponseGetQuestion> getQuestionOffline();

    @Multipart
    @POST("user/profile")
    Call<ResponseRegister> register(
            @Part("name") RequestBody name,
            @Part("phone") RequestBody phone
    );

    @GET("api/check-match")
    Call<ResponseGetQuestion> checkAllowMatch(@Query("id") String idMatch);


    @GET("api/user-star3")
    Call<ResponseGetStar> getStar(@Query("id") String idMatch);

    @GET("api/scoreboard")
    Call<ResponseChart> getChart(@Query("program") int idMatch);


    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("api/answer3")
    Call<ModelResponseSendAnswer> sendAnswerOffline(
            @Query("match") String matchID,
            @Query("repeat") boolean repeat,
            @Body JsonArray answer

    );

    ///api/version
    @GET("app/version")
    Call<ResponseModelSplashApi> getSetting();

    @GET("api/questions2")
    Call<ResponseListQuestionMatchOffline> getListQuestionMatchOffline(@Query("id") String matchID);

    @GET("api/channels")
    Call<ResponseGetBots> getBots(@Query("category") String category);

    @GET("api/pages")
    Call<JsonObject> getTiles();

    // New Api
//    @GET("user/login")
//    Call<ResponseLogin> login();

    @Multipart
    @POST("authenticate/login")
    Call<ResponseLogin> login(@Part("mobile") RequestBody mobile, @Part("serviceId") RequestBody serviceId);

    @Multipart
    @POST("user/verify")
    Call<JsonObject> verifyCode(@Part("code") RequestBody code);

    @GET("user/getprofile")
    Call<ResponseProfile> getProfile();


}
