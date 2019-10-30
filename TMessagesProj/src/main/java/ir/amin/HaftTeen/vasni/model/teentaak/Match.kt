package ir.amin.HaftTeen.vasni.model.teentaak

import com.google.gson.JsonArray
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Match : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int = 0
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("correct_answer_id")
    @Expose
    var answer_id: Int = 0
    @SerializedName("score")
    @Expose
    var score: Int = 0
    @SerializedName("time")
    @Expose
    var time: Int = 0
    @SerializedName("options")
    @Expose
    var options: JsonArray? = null
    @SerializedName("option_id")
    @Expose
    var optionId: Int = 0
    @SerializedName("option_title")
    @Expose
    var optionTitle: String? = null
    @SerializedName("userAnswerId")
    @Expose
    var userAnswerId = 0
    @SerializedName("file")
    @Expose
    var file: String? = null
    @SerializedName("file_type")
    @Expose
    var file_type: String? = null
    @SerializedName("file_service")
    @Expose
    var file_service: String? = ""
    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = ""
    @SerializedName("type")
    @Expose
    var type: String? = ""
}
