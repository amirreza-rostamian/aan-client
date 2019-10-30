package ir.amin.HaftTeen.vasni.webservice;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ir.amin.HaftTeen.messenger.ApplicationLoader;
import ir.amin.HaftTeen.vasni.api.Api;
import ir.amin.HaftTeen.vasni.model.api.profile.ResponseProfile;
import ir.amin.HaftTeen.vasni.model.api.profile.ResponseProfileData;

public class GetProfile {

    private Api api;
    private OnResponse onResponse;
    private Call<ResponseProfile> profileCall;

    public GetProfile(OnResponse onResponse) {
        this.onResponse = onResponse;
        api = ApplicationLoader.getRetrofit(Api.class);
    }


    public void getProfile() {
        cancelRequestGetProfile();
        profileCall = api.getProfile();
        Callback<ResponseProfile> callback = new Callback<ResponseProfile>() {
            @Override
            public void onResponse(Call<ResponseProfile> call, Response<ResponseProfile> response) {
                try {
                    if (response.isSuccessful()) {
                        ResponseProfile question = response.body();
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
            public void onFailure(Call<ResponseProfile> call, Throwable t) {
                if (call.isCanceled()) {
                    onResponse.onError("");
                } else {
                    onResponse.onError("خطا در برقراری ارتباط با سرور");
                }
            }
        };
        profileCall.enqueue(callback);
    }

    public void cancelRequestGetProfile() {
        if (profileCall != null && profileCall.isExecuted()) {
            profileCall.cancel();
        }
    }


    public interface OnResponse {
        void onSuccess(ResponseProfileData data);

        void onError(String message);
    }
}
