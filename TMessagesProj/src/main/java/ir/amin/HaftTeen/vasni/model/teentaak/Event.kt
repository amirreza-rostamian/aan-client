package ir.amin.HaftTeen.vasni.model.teentaak

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Game
import java.io.Serializable


class Event : Serializable {
    @SerializedName("message")
    @Expose
    var message: String? = ""
    @SerializedName("contents")
    @Expose
    var contents: String? = ""
    @SerializedName("news")
    @Expose
    var news: News? = null
    @SerializedName("userProfile")
    @Expose
    var userProfile: UserProfile? = null
    @SerializedName("games")
    @Expose
    var games: Game? = null
    @SerializedName("league")
    @Expose
    var league: League? = null
    @SerializedName("player")
    @Expose
    var player: ArrayList<Player>? = null


    constructor(message: String, contents: String) {
        this.message = message
        this.contents = contents
    }


    constructor(message: String) {
        this.message = message
    }

    constructor(message: String?, news: News?) {
        this.message = message
        this.news = news
    }

    constructor(message: String?, userProfile: UserProfile?) {
        this.message = message
        this.userProfile = userProfile
    }

    constructor(message: String?, contents: String?, player: ArrayList<Player>?) {
        this.message = message
        this.contents = contents
        this.player = player
    }

    constructor(message: String?, games: Game?) {
        this.message = message
        this.games = games
    }

    constructor(message: String?, league: League?) {
        this.message = message
        this.league = league
    }


}

data class EventHandler
(
        @SerializedName("event_name")
        @Expose
        var event_name: String = "",
        @SerializedName("id")
        @Expose
        var id: Int = 0,
        @SerializedName("parent")
        @Expose
        var parent: Int = 0,
        @SerializedName("title")
        @Expose
        var title: String = "",
        @SerializedName("is_free")
        @Expose
        var is_free: String = "",
        @SerializedName("category")
        @Expose
        var category: String = "",
        @SerializedName("program")
        @Expose
        var program: String = "",
        @SerializedName("type")
        @Expose
        var type: String = "",
        @SerializedName("source")
        @Expose
        var source: String = "",
        @SerializedName("tileId")
        @Expose
        var tileId: String = "0",
        @SerializedName("sub_category")
        @Expose
        var sub_category: String = "",
        @SerializedName("url")
        @Expose
        var url: String = "",
        @SerializedName("match")
        @Expose
        var match: String = "",
        @SerializedName("page")
        @Expose
        var page: Int = 0,
        @SerializedName("product_type")
        @Expose
        var product_type: String = "",
        @SerializedName("product")
        @Expose
        var product: String = "",
        @SerializedName("faq")
        @Expose
        var faq: String = "",
        @SerializedName("match_type")
        @Expose
        var match_type: String = "",
        @SerializedName("pages")
        @Expose
        var pages: String = "",
        @SerializedName("link")
        @Expose
        var link: String = "",
        @SerializedName("text")
        @Expose
        var text: String = "",
        @SerializedName("channel")
        @Expose
        var channel: String = "",
        @SerializedName("button")
        @Expose
        var button: String = "",
        @SerializedName("description")
        @Expose
        var description: String = "",
        @SerializedName("characters")
        @Expose
        var characters: String = "",
        @SerializedName("keyword")
        @Expose
        var keyword: String = ""


) : Serializable
