package ir.amin.HaftTeen.vasni.webservice;

import android.util.Log;

import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ir.amin.HaftTeen.messenger.ApplicationLoader;
import ir.amin.HaftTeen.vasni.api.Api;
import ir.amin.HaftTeen.vasni.model.api.register.ResponseRegister;
import ir.amin.HaftTeen.vasni.model.api.register.ResponseRegisterData;


public class RegisterWebService {
    private Api api;
    private OnResponse onResponse;
    private Call<ResponseRegister> registerCall;

    public RegisterWebService(OnResponse onResponse) {
        this.onResponse = onResponse;
        api = ApplicationLoader.getRetrofit(Api.class);
    }


    public void registerUser(RequestBody name, RequestBody first_name) {
        cancelRequestRegister();
        registerCall = api.register(name, first_name);
        Callback<ResponseRegister> callback = new Callback<ResponseRegister>() {
            @Override
            public void onResponse(Call<ResponseRegister> call, Response<ResponseRegister> response) {
                try {
                    if (response.isSuccessful()) {
                        ResponseRegister register = response.body();
                        if (register != null) {
                            if (register.getSuccess() == 1) {
                                onResponse.onSuccess(register);
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
            public void onFailure(Call<ResponseRegister> call, Throwable t) {
                if (call.isCanceled()) {
                    Log.i("LOG", "User Is Cancel Request Reagent Code");
                } else {
                    onResponse.onError("خطا در برقراری ارتباط با سرور");
                }
            }
        };

        registerCall.enqueue(callback);

    }

    private void cancelRequestRegister() {
        if (registerCall != null && registerCall.isExecuted()) {
            registerCall.cancel();
        }
    }


    public interface OnResponse {
        void onSuccess(ResponseRegister register);

        void onError(String message);
    }

}
