package ir.amin.HaftTeen.vasni.model.teentaak.Vitrin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MenuContent : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = ""
    @SerializedName("title")
    @Expose
    var title: String? = ""
    @SerializedName("display")
    @Expose
    var display: String? = "0"
    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = ""
    @SerializedName("categories")
    @Expose
    var categories: ArrayList<CategoryFilter> = ArrayList()
}

class CategoryFilter : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = ""
    @SerializedName("categoryId")
    @Expose
    var categoryId: String? = ""
    @SerializedName("category")
    @Expose
    var category: String? = ""
    @SerializedName("isSelected")
    @Expose
    var isSelected: Boolean? = false
    @SerializedName("tags")
    @Expose
    var tags: ArrayList<Tag> = ArrayList()
}

class PageFilter : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = ""
    @SerializedName("title")
    @Expose
    var title: String? = ""
    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = ""
    @SerializedName("category")
    @Expose
    var category: String? = ""
    @SerializedName("isSelected")
    @Expose
    var isSelected: Boolean? = false
}

class Tag : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = ""
    @SerializedName("tagId")
    @Expose
    var tagId: String? = ""
    @SerializedName("tag")
    @Expose
    var tag: String? = ""
    @SerializedName("title")
    @Expose
    var title: String? = ""
    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = ""
    @SerializedName("display")
    @Expose
    var display: String? = "0"
    @SerializedName("type")
    @Expose
    var type: String? = "1"
    @SerializedName("categoryId")
    @Expose
    var categoryId: String? = ""
    @SerializedName("game")
    @Expose
    var game: ArrayList<Game> = ArrayList()
    @SerializedName("book")
    @Expose
    var book: ArrayList<Book> = ArrayList()
    @SerializedName("video")
    @Expose
    var video: ArrayList<Video> = ArrayList()
    @SerializedName("music")
    @Expose
    var music: ArrayList<Music> = ArrayList()
    @SerializedName("package")
    @Expose
    var pkg: ArrayList<Package> = ArrayList()
    @SerializedName("banners")
    @Expose
    var banners: ArrayList<Banners> = ArrayList()
}

class Banners : Serializable {
    @SerializedName("bannerId")
    @Expose
    var bannerId: String? = ""
    @SerializedName("banner")
    @Expose
    var banner: String? = ""
    @SerializedName("source_type")
    @Expose
    var source_type: String? = ""
    @SerializedName("source_id")
    @Expose
    var source_id: String? = ""
}