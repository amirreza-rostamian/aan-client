package ir.amin.HaftTeen.vasni.model.teentaak.Vitrin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PackageCategory : Serializable {
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
    var items: ArrayList<Package>? = ArrayList()
}

class Package : Serializable {
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
    @SerializedName("description")
    @Expose
    var description: String? = ""
    @SerializedName("price")
    @Expose
    var price: Int? = 0
    @SerializedName("banner")
    @Expose
    var banner: String? = ""
    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = ""
    @SerializedName("wallet")
    @Expose
    var wallet: Wallet? = Wallet()
    @SerializedName("product_game")
    @Expose
    var game: ArrayList<Game> = ArrayList()
    @SerializedName("product_book")
    @Expose
    var book: ArrayList<Book> = ArrayList()
    @SerializedName("product_video")
    @Expose
    var video: ArrayList<Video> = ArrayList()
    @SerializedName("product_music")
    @Expose
    var music: ArrayList<Music> = ArrayList()
    @SerializedName("logs")
    @Expose
    var logs: Logs? = Logs()
    @SerializedName("comments")
    @Expose
    var comments: ArrayList<Comments>? = ArrayList()
}