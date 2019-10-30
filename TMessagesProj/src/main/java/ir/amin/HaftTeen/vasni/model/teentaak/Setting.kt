package ir.amin.HaftTeen.vasni.model.teentaak

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Setting : Serializable {
    @SerializedName("channel_id")
    @Expose
    var channel_id: String? = ""
    @SerializedName("api_key")
    @Expose
    var api_key: String? = ""
    @SerializedName("video")
    @Expose
    var video: String? = ""
    @SerializedName("audio")
    @Expose
    var audio: String? = ""
    @SerializedName("class")
    @Expose
    var has_class: Int? = 0
}