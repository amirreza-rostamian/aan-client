package ir.amin.HaftTeen.vasni.model.teentaak.Vitrin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ir.amin.HaftTeen.vasni.model.teentaak.LeagueUser
import java.io.Serializable

class GameCategory : Serializable {
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
    var items: ArrayList<Game> = ArrayList()
}

class Game : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = ""
    @SerializedName("title")
    @Expose
    var title: String? = ""
    @SerializedName("category")
    @Expose
    var category: String? = ""
    @SerializedName("format")
    @Expose
    var format: Int? = 0
    @SerializedName("ages")
    @Expose
    var ages: String? = ""
    @SerializedName("volume")
    @Expose
    var volume: String? = ""
    @SerializedName("version")
    @Expose
    var version: String? = ""
    @SerializedName("namespace")
    @Expose
    var namespace: String? = ""
    @SerializedName("price")
    @Expose
    var price: Int = 0
    @SerializedName("description")
    @Expose
    var description: String? = ""
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
    @SerializedName("top_score")
    @Expose
    var top_score: ArrayList<LeagueUser>? = ArrayList()
    @SerializedName("wallet")
    @Expose
    var wallet: Wallet? = Wallet()
}

class ScreenShot : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = 0
    @SerializedName("link")
    @Expose
    var link: String? = ""
}

class Logs : Serializable {
    @SerializedName("download")
    @Expose
    var download: Int? = 0
    @SerializedName("like")
    @Expose
    var like: Int? = 0
    @SerializedName("rate")
    @Expose
    var rate: String? = "0"
    @SerializedName("view")
    @Expose
    var view: Int? = 0
    @SerializedName("share")
    @Expose
    var share: Int? = 0
}

class Comments : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = ""
    @SerializedName("userId")
    @Expose
    var userId: String? = ""
    @SerializedName("fullName")
    @Expose
    var fullName: String? = ""
    @SerializedName("sourceId")
    @Expose
    var sourceId: String? = ""
    @SerializedName("message")
    @Expose
    var message: String? = ""
}

class Wallet : Serializable {
    @SerializedName("bought")
    @Expose
    var bought: Boolean? = false
    @SerializedName("balance")
    @Expose
    var balance: String? = "0"
}
