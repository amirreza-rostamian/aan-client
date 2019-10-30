package ir.amin.HaftTeen.vasni.model.teentaak

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class News : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = ""
    @SerializedName("title")
    @Expose
    var title: String? = ""
    @SerializedName("content")
    @Expose
    var content: String? = ""
    @SerializedName("pic")
    @Expose
    var pic: String? = ""
    @SerializedName("comments")
    @Expose
    var comments: ArrayList<Comment>? = ArrayList()
}