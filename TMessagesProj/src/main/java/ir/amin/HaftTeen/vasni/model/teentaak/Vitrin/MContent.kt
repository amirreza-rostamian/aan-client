package ir.amin.HaftTeen.vasni.model.teentaak.Vitrin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MusicCategory : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = ""
    @SerializedName("name")
    @Expose
    var name: String? = ""
    @SerializedName("display")
    @Expose
    var display: String? = "0"
    @SerializedName("catId")
    @Expose
    var catId: String? = ""
    @SerializedName("items")
    @Expose
    var items: ArrayList<Music>? = ArrayList()
}

class Music : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = ""
    @SerializedName("title")
    @Expose
    var title: String? = ""
    @SerializedName("category")
    @Expose
    var category: String? = ""
    @SerializedName("type")
    @Expose
    var type: String? = ""
    @SerializedName("singer")
    @Expose
    var singer: String? = ""
    @SerializedName("composer")
    @Expose
    var composer: String? = ""
    @SerializedName("musicians")
    @Expose
    var musicians: String? = ""
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
    @SerializedName("contentId")
    @Expose
    var contentId: Int? = 0
    @SerializedName("fileId")
    @Expose
    var fileId: Int? = 0
    @SerializedName("categoryHash")
    @Expose
    var categoryHash: Int? = 0
    @SerializedName("banner")
    @Expose
    var banner: String? = ""
    @SerializedName("link")
    @Expose
    var link: String? = ""
    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = ""
    @SerializedName("programs")
    @Expose
    var programs: ArrayList<String>? = ArrayList()
    @SerializedName("comments")
    @Expose
    var comments: ArrayList<Comments>? = ArrayList()
    @SerializedName("screenShots")
    @Expose
    var screenShots: ArrayList<ScreenShot>? = ArrayList()
    @SerializedName("logs")
    @Expose
    var logs: Logs? = Logs()
    @SerializedName("guid")
    @Expose
    var guid: String? = ""
    @SerializedName("wallet")
    @Expose
    var wallet: Wallet? = Wallet()
    @SerializedName("fileServiceProvider")
    @Expose
    var fileServiceProvider: String? = ""

}