package ir.amin.HaftTeen.vasni.model.teentaak

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Comment : Serializable {
    @SerializedName("user_id")
    @Expose
    var user_id: String? = ""
    @SerializedName("user_name")
    @Expose
    var user_name: String? = ""
    @SerializedName("comment")
    @Expose
    var comment: String? = ""
    @SerializedName("created")
    @Expose
    var created: String? = ""
    @SerializedName("created_at")
    @Expose
    var created_at: String? = ""
}