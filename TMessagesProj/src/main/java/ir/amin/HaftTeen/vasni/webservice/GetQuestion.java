package ir.amin.HaftTeen.vasni.webservice;

import android.util.Log;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ir.amin.HaftTeen.messenger.ApplicationLoader;
import ir.amin.HaftTeen.vasni.api.Api;
import ir.amin.HaftTeen.vasni.model.api.ResponseGetQuestion;
import ir.amin.HaftTeen.vasni.model.api.ResponseGetQuestionData;

public class GetQuestion {

    private Api api;
    private OnResponse onResponse;
    private Call<ResponseGetQuestion> callQuestion;

    public GetQuestion(OnResponse onResponse) {
        this.onResponse = onResponse;
        api = ApplicationLoader.getRetrofit(Api.class);
    }


    public void getQuestionAtServer() {
        cancelRequestGetQuestion();
        callQuestion = api.getQuestionOffline();
        Callback<ResponseGetQuestion> callback = new Callback<ResponseGetQuestion>() {
            @Override
            public void onResponse(Call<ResponseGetQuestion> call, Response<ResponseGetQuestion> response) {
                try {
                    if (response.isSuccessful()) {
                        ResponseGetQuestion question = response.body();
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
            public void onFailure(Call<ResponseGetQuestion> call, Throwable t) {
                if (call.isCanceled()) {
                    onResponse.onError("");
                    Log.i("LOG", "User Is Cancel Request Reagent Code");
                } else {
                    onResponse.onError("خطا در برقراری ارتباط با سرور");
                }
            }
        };
        callQuestion.enqueue(callback);
    }

    public void cancelRequestGetQuestion() {
        if (callQuestion != null && callQuestion.isExecuted()) {
            callQuestion.cancel();
        }
    }


    public interface OnResponse {
        void onSuccess(ArrayList<ResponseGetQuestionData> data);

        void onError(String message);
    }

}
