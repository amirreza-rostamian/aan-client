package ir.amin.HaftTeen.vasni.webservice;

import android.util.Log;

import me.himanshusoni.chatmessageview.Vasni.VasniSchema;
import me.himanshusoni.chatmessageview.util.AppSchema;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ir.amin.HaftTeen.messenger.ApplicationLoader;
import ir.amin.HaftTeen.vasni.api.Api;
import ir.amin.HaftTeen.vasni.model.api.login.ResponseLogin;
import ir.amin.HaftTeen.vasni.model.api.login.ResponseLoginData;
import ir.amin.HaftTeen.vasni.utils.MSharePk;
import ir.amin.HaftTeen.BuildConfig;

public class LoginUser {
    private Api api;
    private OnResponse onResponse;
    private Call<ResponseLogin> loginCall;

    public LoginUser(OnResponse onResponse) {
        this.onResponse = onResponse;
        api = ApplicationLoader.getRetrofit(Api.class);
    }


    public void loginUser() {
        cancelLoginRequest();
        RequestBody mobile = RequestBody.create(MultipartBody.FORM, MSharePk.getString(ApplicationLoader.applicationContext, AppSchema.phoneNumber, "").replace("98", "0").toString());
        RequestBody ServiceId = RequestBody.create(MultipartBody.FORM, BuildConfig.APP_ID);
        loginCall = api.login(mobile, ServiceId);
        Callback<ResponseLogin> callback = new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                try {
                    if (response.isSuccessful()) {
                        ResponseLogin question = response.body();
                        if (question != null) {
                            if (question.getSuccess() == 1) {
                                onResponse.onSuccess(question.getData());
                            } else {
                                onResponse.onError("خطا در برقراری ارتباط با سرور");
                            }
                        } else {

                            onResponse.onError("خطا در برقراری ارتباط با سرور");
                        }
                    } else {
                        switch (response.code()) {
                            case 404:
                                onResponse.onError("خطا در برقراری ارتباط با سرور");
                                break;
                            case 500:
                                onResponse.onError("خطا در برقراری ارتباط با سرور");
                                break;
                            default:
                                onResponse.onError("خطا در برقراری ارتباط با سرور");
                                break;
                        }
                    }
                } catch (Exception e) {
                    onResponse.onError("خطا در برقراری ارتباط با سرور");
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                if (call.isCanceled()) {
                    onResponse.onError("");
                    Log.i("LOG", "User Is Cancel Request Reagent Code");
                } else {
                    onResponse.onError("خطا در برقراری ارتباط با سرور");
                }
            }
        };
        loginCall.enqueue(callback);
    }

    public void cancelLoginRequest() {
        if (loginCall != null && loginCall.isExecuted()) {
            loginCall.cancel();
        }
    }


    public interface OnResponse {
        void onSuccess(ResponseLoginData data);

        void onError(String message);
    }
}
