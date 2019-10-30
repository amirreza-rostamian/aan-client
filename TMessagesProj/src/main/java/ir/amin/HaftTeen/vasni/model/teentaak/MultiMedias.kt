package ir.amin.HaftTeen.vasni.model.teentaak

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class MultiMedias : Serializable {
    @SerializedName("title")
    @Expose
    var title: String = ""
    @SerializedName("id")
    @Expose
    var hash_id: String = ""
    @SerializedName("category_title")
    @Expose
    var category_title: String = ""
    @SerializedName("parent_category_id")
    @Expose
    var category_hash_id: Int = 0
    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String = ""
    @SerializedName("link")
    @Expose
    var link: String = ""
    @SerializedName("likes")
    @Expose
    var likes: Int = 0
    @SerializedName("user_liked")
    @Expose
    var is_liked: Boolean = false
    @SerializedName("my_rate")
    @Expose
    var my_rate: Int = 0
    @SerializedName("views")
    @Expose
    var views: Int = 0
    @SerializedName("rate")
    @Expose
    var rate: Int = 0
    @SerializedName("pic")
    @Expose
    var pic: String = ""
    @SerializedName("user_name")
    @Expose
    var user_name: String = ""
    @SerializedName("user_score")
    @Expose
    var user_score: String = ""
    @SerializedName("user_pic")
    @Expose
    var user_pic: String = ""
    @SerializedName("url")
    @Expose
    var url: String = ""
    @SerializedName("description")
    @Expose
    var description: String = ""
    @SerializedName("comments")
    @Expose
    var comments: ArrayList<Comment>? = ArrayList()
}


data class Banner(
        @SerializedName("name")
        @Expose
        var name: String = "",
        @SerializedName("url")
        @Expose
        var url: String = "",
        @SerializedName("action")
        @Expose
        var action: String = "",
        @SerializedName("content_action")
        @Expose
        var content_action: String = ""
) : Serializable