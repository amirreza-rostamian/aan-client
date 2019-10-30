package ir.amin.HaftTeen.vasni.model.teentaak.Vitrin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class VideoCategory : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = ""
    @SerializedName("name")
    @Expose
    var name: String? = ""
    @SerializedName("display")
    @Expose
    var display: String? = "0"
    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = ""
    @SerializedName("catId")
    @Expose
    var catId: String? = ""
    @SerializedName("items")
    @Expose
    var items: ArrayList<Video>? = ArrayList()
    @SerializedName("episodes")
    @Expose
    var episodes: ArrayList<Video>? = ArrayList()
}

class Video : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = ""
    @SerializedName("title")
    @Expose
    var title: String? = ""
    @SerializedName("category")
    @Expose
    var category: String? = ""
    @SerializedName("director")
    @Expose
    var director: String? = ""
    @SerializedName("actors")
    @Expose
    var actors: String? = ""
    @SerializedName("composers")
    @Expose
    var composers: String? = ""
    @SerializedName("writer")
    @Expose
    var writer: String? = ""
    @SerializedName("productionYear")
    @Expose
    var productionYear: String? = ""
    @SerializedName("playbackTime")
    @Expose
    var playbackTime: String? = ""
    @SerializedName("volume")
    @Expose
    var volume: String? = ""
    @SerializedName("price")
    @Expose
    var price: Int? = 0
    @SerializedName("format")
    @Expose
    var format: String? = ""
    @SerializedName("quality")
    @Expose
    var quality: String? = ""
    @SerializedName("ages")
    @Expose
    var ages: String? = ""
    @SerializedName("producingCountry")
    @Expose
    var producingCountry: String? = ""
    @SerializedName("producingCompany")
    @Expose
    var producingCompany: String? = ""
    @SerializedName("language")
    @Expose
    var language: String? = ""
    @SerializedName("honors")
    @Expose
    var honors: String? = ""
    @SerializedName("summary")
    @Expose
    var summary: String? = ""
    @SerializedName("banner")
    @Expose
    var banner: String? = ""
    @SerializedName("link")
    @Expose
    var link: String? = ""
    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = ""
    @SerializedName("logs")
    @Expose
    var logs: Logs? = Logs()
    @SerializedName("name")
    @Expose
    var name: String? = ""
    @SerializedName("parentId")
    @Expose
    var parentId: String? = ""
    @SerializedName("categoryId")
    @Expose
    var categoryId: String? = ""
    @SerializedName("isMemberOfSubCategory")
    @Expose
    var isMemberOfSubCategory: Boolean? = false
    @SerializedName("guid")
    @Expose
    var guid: String? = ""
    @SerializedName("wallet")
    @Expose
    var wallet: Wallet? = Wallet()
    @SerializedName("fileServiceProvider")
    @Expose
    var fileServiceProvider: String? = ""
    @SerializedName("comments")
    @Expose
    var comments: ArrayList<Comments>? = ArrayList()
}

class VideoInfo : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = ""
    @SerializedName("name")
    @Expose
    var name: String? = ""
    @SerializedName("display")
    @Expose
    var display: String? = "0"
    @SerializedName("items")
    @Expose
    var items: Video? = Video()
    @SerializedName("episodes")
    @Expose
    var episodes: ArrayList<Video>? = ArrayList()
}