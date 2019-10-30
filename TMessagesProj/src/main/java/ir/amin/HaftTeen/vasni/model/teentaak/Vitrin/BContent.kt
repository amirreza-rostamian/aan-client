package ir.amin.HaftTeen.vasni.model.teentaak.Vitrin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BookCategory : Serializable {
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
    var items: ArrayList<Book>? = ArrayList()
}

class Book : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = ""
    @SerializedName("title")
    @Expose
    var title: String? = ""
    @SerializedName("category")
    @Expose
    var category: String? = ""
    @SerializedName("author")
    @Expose
    var author: String? = ""
    @SerializedName("translator")
    @Expose
    var translator: String? = ""
    @SerializedName("editor")
    @Expose
    var editor: String? = ""
    @SerializedName("subject")
    @Expose
    var subject: String? = ""
    @SerializedName("publisher")
    @Expose
    var publisher: String? = ""
    @SerializedName("type")
    @Expose
    var type: Int? = 0
    @SerializedName("volume")
    @Expose
    var volume: String? = ""
    @SerializedName("price")
    @Expose
    var price: Int? = 0
    @SerializedName("format")
    @Expose
    var format: String? = ""
    @SerializedName("published")
    @Expose
    var published: String? = ""
    @SerializedName("pageNumber")
    @Expose
    var pageNumber: Int? = 0
    @SerializedName("circulation")
    @Expose
    var circulation: Int? = 0
    @SerializedName("ages")
    @Expose
    var ages: String? = ""
    @SerializedName("language")
    @Expose
    var language: String? = ""
    @SerializedName("banner")
    @Expose
    var banner: String? = ""
    @SerializedName("link")
    @Expose
    var link: String? = ""
    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = ""
    @SerializedName("summary")
    @Expose
    var summary: String? = ""
    @SerializedName("logs")
    @Expose
    var logs: Logs? = Logs()
    @SerializedName("wallet")
    @Expose
    var wallet: Wallet? = Wallet()
    @SerializedName("comments")
    @Expose
    var comments: ArrayList<Comments>? = ArrayList()
}