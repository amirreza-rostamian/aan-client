package ir.amin.HaftTeen.vasni.model.teentaak

import com.google.gson.JsonArray
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Poll : Serializable {
    @SerializedName("title")
    @Expose
    var title: String = ""
    @SerializedName("typeId")
    @Expose
    var typeId: Int = 0
    @SerializedName("category_id")
    @Expose
    var category_id: Int = 0
    @SerializedName("category_name")
    @Expose
    var category_name: String = ""
    @SerializedName("cat_Type")
    @Expose
    var cat_Type: Int = 0
    @SerializedName("cat_Type_name")
    @Expose
    var cat_Type_name: String = ""
    @SerializedName("questions")
    @Expose
    var questions: ArrayList<Question> = ArrayList()
    @SerializedName("type")
    @Expose
    var type: String = ""
}

class Question : Serializable {
    @SerializedName("question_id")
    @Expose
    var question_id: Int = 0
    @SerializedName("ques_title")
    @Expose
    var ques_title: String = ""
    @SerializedName("ques_type")
    @Expose
    var ques_type: Int = 0
    //    @SerializedName("ques_ans")
//    @Expose
//    var ques_ans: ArrayList<QuestionAnswer> = ArrayList()
    @SerializedName("ques_ans")
    @Expose
    var ques_ans: JsonArray? = null

}

class QuestionAnswer : Serializable {
    @SerializedName("answer_id")
    @Expose
    var answer_id: Int = 0
    @SerializedName("ans_title")
    @Expose
    var ans_title: String = ""
}

class PollAnswer : Serializable {
    @SerializedName("answer_id")
    @Expose
    var answer_id: Int = 0
    @SerializedName("question_id")
    @Expose
    var question_id: Int = 0
    @SerializedName("ans_title")
    @Expose
    var ans_title: String? = null

    constructor(answer_id: Int, question_id: Int) {
        this.answer_id = answer_id
        this.question_id = question_id
    }
}