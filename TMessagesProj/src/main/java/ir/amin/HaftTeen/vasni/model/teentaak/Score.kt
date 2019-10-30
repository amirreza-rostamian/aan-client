package ir.amin.HaftTeen.vasni.model.teentaak

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ProgramScore : Serializable {
    @SerializedName("source_id")
    @Expose
    var source_id: String? = ""
    @SerializedName("source_type")
    @Expose
    var source_type: String? = ""
    @SerializedName("source_type_name")
    @Expose
    var source_type_name: String? = ""
    @SerializedName("score")
    @Expose
    var score: String? = ""
    @SerializedName("created_at")
    @Expose
    var created_at: String? = ""
    @SerializedName("created")
    @Expose
    var created: String? = ""
    @SerializedName("source_name")
    @Expose
    var source_name: String? = ""
    @SerializedName("source_category_id")
    @Expose
    var source_category_id: String? = ""
    @SerializedName("source_category_name")
    @Expose
    var source_category_name: String? = ""
    @SerializedName("source_program_id")
    @Expose
    var source_program_id: String? = ""
    @SerializedName("source_program")
    @Expose
    var source_program: String? = ""
}

class Score : Serializable {
    @SerializedName("program_id")
    @Expose
    var program_id: String? = ""
    @SerializedName("program_name")
    @Expose
    var program_name: String? = ""
    @SerializedName("user_point")
    @Expose
    var user_point: String? = ""
    @SerializedName("user_chance")
    @Expose
    var user_chance: String? = ""
}
