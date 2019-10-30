package ir.amin.HaftTeen.vasni.webservice;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ir.amin.HaftTeen.messenger.ApplicationLoader;
import ir.amin.HaftTeen.vasni.api.Api;
import ir.amin.HaftTeen.vasni.model.api.ResponseGetQuestion;
import ir.amin.HaftTeen.vasni.model.api.getstar.ResponseGetStar;
import ir.amin.HaftTeen.vasni.model.api.getstar.ResponseGetStarData;
import ir.amin.HaftTeen.vasni.model.api.listquestionmatchoffline.ResponseListQuestionMatchOffline;
import ir.amin.HaftTeen.vasni.model.api.listquestionmatchoffline.ResponseListQuestionMatchOfflineData;
import ir.amin.HaftTeen.vasni.model.api.matchoffline.sendanswer.ModelResponseSendAnswer;
import ir.amin.HaftTeen.vasni.model.api.matchoffline.sendanswer.ModelResponseSendAnswerData;


public class MatchOfflineWebService {

    private Api api;
    private OnResponse onResponse;
    private Call<ResponseGetQuestion> callIsAllowMatch;
    private Call<ResponseGetStar> callGetStar;
    private Call<ModelResponseSendAnswer> callSendAnswer;
    private Call<ResponseListQuestionMatchOffline> callGetListQuestion;


    public MatchOfflineWebService(OnResponse onResponse) {
        this.onResponse = onResponse;
        api = ApplicationLoader.getRetrofit(Api.class);
    }

    public void getListQuestion(String matchId) {
        cancelGetListQuestion();
        callGetListQuestion = api.getListQuestionMatchOffline(matchId);
        Callback<ResponseListQuestionMatchOffline> callback = new Callback<ResponseListQuestionMatchOffline>() {
            @Override
            public void onResponse(Call<ResponseListQuestionMatchOffline> call, Response<ResponseListQuestionMatchOffline> response) {
                try {
                    if (response.isSuccessful()) {
                        ResponseListQuestionMatchOffline listQuestion = response.body();
                        if (listQuestion != null) {
                            if (listQuestion.getSuccess() == 1) {
                                onResponse.onSuccessGetListQuestionMatchOffline(listQuestion.getData());
                            } else {
                                onResponse.onErrorGetListQuestionMatchOffline(listQuestion.getErrors().getMessage(), listQuestion.getErrors().getName());
                            }
                        } else {
                            onResponse.onErrorGetListQuestionMatchOffline("خطا در برقراری ارتباط با سرور", "");
                        }

                    } else {
                        switch (response.code()) {
                            case 404:
                                onResponse.onErrorGetListQuestionMatchOffline("خطا در برقراری ارتباط با سرور", "");
                                break;
                            case 500:
                                onResponse.onErrorGetListQuestionMatchOffline("خطا در برقراری ارتباط با سرور", "");
                                break;
                            default:
                                onResponse.onErrorGetListQuestionMatchOffline("خطا در برقراری ارتباط با سرور", "");
                                break;
                        }
                    }
                } catch (Exception e) {
                    onResponse.onErrorGetListQuestionMatchOffline("خطا در برقراری ارتباط با سرور", "");
                }
            }

            @Override
            public void onFailure(Call<ResponseListQuestionMatchOffline> call, Throwable t) {
                if (call.isCanceled()) {
                } else {
                    onResponse.onErrorGetListQuestionMatchOffline("خطا در برقراری ارتباط با سرور", "");
                }
            }
        };

        callGetListQuestion.enqueue(callback);

    }

    private void cancelGetListQuestion() {
        if (callGetListQuestion != null && callGetListQuestion.isExecuted()) {
            callGetListQuestion.cancel();
        }

    }

    public void getStar(String idMatch) {
        cancelgetStar();
        callGetStar = api.getStar(idMatch);
        Callback<ResponseGetStar> callback = new Callback<ResponseGetStar>() {
            @Override
            public void onResponse(Call<ResponseGetStar> call, Response<ResponseGetStar> response) {
                try {
                    if (response.isSuccessful()) {
                        ResponseGetStar star = response.body();
                        if (star != null) {
                            if (star.getSuccess() == 1) {
                                onResponse.onSuccessGetStar(star.getData());
                            } else {
                                onResponse.onErrorGetStar("خطا در برقراری ارتباط با سرور");
                            }
                        } else {
                            onResponse.onErrorGetStar("خطا در برقراری ارتباط با سرور");
                        }

                    } else {
                        switch (response.code()) {
                            case 404:
                                onResponse.onErrorGetStar("خطا در برقراری ارتباط با سرور");
                                break;
                            case 500:
                                onResponse.onErrorGetStar("خطا در برقراری ارتباط با سرور");
                                break;
                            default:
                                onResponse.onErrorGetStar("خطا در برقراری ارتباط با سرور");
                                break;
                        }
                    }
                } catch (Exception e) {
                    onResponse.onErrorGetStar("خطا در برقراری ارتباط با سرور");
                }
            }

            @Override
            public void onFailure(Call<ResponseGetStar> call, Throwable t) {
                if (call.isCanceled()) {
                } else {
                    onResponse.onErrorGetStar("خطا در برقراری ارتباط با سرور");
                }
            }
        };

        callGetStar.enqueue(callback);

    }

    private void cancelgetStar() {
        if (callGetStar != null && callGetStar.isExecuted()) {
            callGetStar.cancel();
        }
    }

    public void sendAnswerAtServer(String matchId, boolean repeat, JsonArray answer) {
        cancelAendAnswerAtServer();
        callSendAnswer = api.sendAnswerOffline(matchId, repeat, answer);
        Callback<ModelResponseSendAnswer> callback = new Callback<ModelResponseSendAnswer>() {
            @Override
            public void onResponse(Call<ModelResponseSendAnswer> call, Response<ModelResponseSendAnswer> response) {
                try {
                    if (response.isSuccessful()) {
                        ModelResponseSendAnswer sendAnswer = response.body();
                        if (sendAnswer != null) {
                            if (sendAnswer.getSuccess() == 1) {
                                onResponse.onSuccessSendAnswer(sendAnswer.getData());
                            } else {
                                onResponse.onErrorSendAnswer("خطا در برقراری ارتباط با سرور");
                            }
                        } else {
                            onResponse.onErrorSendAnswer("خطا در برقراری ارتباط با سرور");
                        }

                    } else {
                        switch (response.code()) {
                            case 404:
                                onResponse.onErrorSendAnswer("خطا در برقراری ارتباط با سرور");
                                break;
                            case 500:
                                onResponse.onErrorSendAnswer("خطا در برقراری ارتباط با سرور");
                                break;
                            default:
                                onResponse.onErrorSendAnswer("خطا در برقراری ارتباط با سرور");
                                break;
                        }
                    }
                } catch (Exception e) {
                    onResponse.onErrorSendAnswer("خطا در برقراری ارتباط با سرور");
                }
            }

            @Override
            public void onFailure(Call<ModelResponseSendAnswer> call, Throwable t) {
                if (call.isCanceled()) {
                } else {
                    onResponse.onErrorSendAnswer("خطا در برقراری ارتباط با سرور");
                }
            }
        };

        callSendAnswer.enqueue(callback);

    }

    private void cancelAendAnswerAtServer() {
        if (callSendAnswer != null && callSendAnswer.isExecuted()) {
            callSendAnswer.cancel();
        }
    }


    public interface OnResponse {
        void onSuccessSendIdMatch(boolean isAllowMatch);

        void onErrorSendIdMatch(String message);

        void onSuccessGetStar(ResponseGetStarData data);

        void onErrorGetStar(String message);

        void onSuccessSendAnswer(ModelResponseSendAnswerData data);

        void onErrorSendAnswer(String message);

        void onSuccessGetListQuestionMatchOffline(ResponseListQuestionMatchOfflineData data);

        void onErrorGetListQuestionMatchOffline(String message, String name);

    }
}
