package ir.amin.HaftTeen.vasni.webservice;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ir.amin.HaftTeen.messenger.ApplicationLoader;
import ir.amin.HaftTeen.vasni.api.Api;

public class GetTiles {

    private Api api;
    private OnResponse onResponse;
    private Call<JsonObject> callTiles;

    public GetTiles(OnResponse onResponse) {
        this.onResponse = onResponse;
        api = ApplicationLoader.getRetrofit(Api.class);
    }


    public void getTilesAtServer() {
        cancelRequestGetQuestion();
        callTiles = api.getTiles();
        Callback<JsonObject> callback = new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().get("success").getAsInt() == 1) {
                                onResponse.onSuccess(response.body());
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
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (call.isCanceled()) {
                    onResponse.onError("");
                } else {
                    onResponse.onError("خطا در برقراری ارتباط با سرور");
                }
            }
        };
        callTiles.enqueue(callback);
    }

    public void cancelRequestGetQuestion() {
        if (callTiles != null && callTiles.isExecuted()) {
            callTiles.cancel();
        }
    }


    public interface OnResponse {
        void onSuccess(JsonObject jsonObject);

        void onError(String message);
    }
}
