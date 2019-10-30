package ir.amin.HaftTeen.vasni.webservice;

import android.content.Context;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ir.amin.HaftTeen.messenger.ApplicationLoader;
import ir.amin.HaftTeen.vasni.api.Api;
import ir.amin.HaftTeen.vasni.model.api.splash.ResponseModelSplashApi;


public class SplashWebService {

    private Api api;
    private OnResponse onResponse;
    private Call<ResponseModelSplashApi> call;
    private Context context;


    public SplashWebService(Context context, OnResponse onResponse) {
        this.onResponse = onResponse;
        this.context = context;
        api = ApplicationLoader.getRetrofit(Api.class);
    }

    public void getplash() {
        cancelRequestGetQuestion();
        call = api.getSetting();
        Callback<ResponseModelSplashApi> callback = new Callback<ResponseModelSplashApi>() {
            @Override
            public void onResponse(Call<ResponseModelSplashApi> call, Response<ResponseModelSplashApi> response) {
                try {
                    if (response.isSuccessful()) {
                        ResponseModelSplashApi setting = response.body();
                        if (setting != null) {
                            if (setting.getSuccess() == 1) {
                                onResponse.onSuccess(setting);
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
            public void onFailure(Call<ResponseModelSplashApi> call, Throwable t) {
                if (call.isCanceled()) {
                    Log.i("LOG", "User Is Cancel Request Reagent Code");
                } else {
                    onResponse.onError("خطا در برقراری ارتباط با سرور");
                }
            }
        };

        call.enqueue(callback);

    }

    private void cancelRequestGetQuestion() {
        if (call != null && call.isExecuted()) {
            call.cancel();
        }
    }

    public interface OnResponse {
        void onSuccess(ResponseModelSplashApi setting);

        void onError(String message);
    }

}
