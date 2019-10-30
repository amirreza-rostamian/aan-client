package ir.amin.HaftTeen.vasni.model.teentaak

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Game
import java.io.Serializable

class League : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = ""
    @SerializedName("title")
    @Expose
    var title: String? = ""
    @SerializedName("description")
    @Expose
    var description: String? = ""
    @SerializedName("category_id")
    @Expose
    var category_id: String? = ""
    @SerializedName("category_name")
    @Expose
    var category_name: String? = ""
    @SerializedName("start")
    @Expose
    var start: String? = ""
    @SerializedName("end")
    @Expose
    var end: String? = ""
    @SerializedName("icon")
    @Expose
    var icon: String? = ""
    @SerializedName("pic")
    @Expose
    var pic: String? = ""
    @SerializedName("games")
    @Expose
    var games: ArrayList<Game> = ArrayList()
    @SerializedName("dateDiff")
    @Expose
    var dateDiff: JsonObject? = null
    @SerializedName("users")
    @Expose
    var users: ArrayList<LeagueUser> = ArrayList()
    @SerializedName("top_scores")
    @Expose
    var top_scores: ArrayList<LeagueUser> = ArrayList()
    @SerializedName("panel_winner")
    @Expose
    var panel_winner: ArrayList<LeagueUser> = ArrayList()
    @SerializedName("user_download")
    @Expose
    var user_download: ArrayList<LeagueUser> = ArrayList()
    @SerializedName("user_participate")
    @Expose
    var user_participate: Boolean = true
    @SerializedName("isSelected")
    @Expose
    var isSelected: Boolean = false
}

class LeagueUser : Serializable {
    @SerializedName("user_id")
    @Expose
    var user_id: String? = ""
    @SerializedName("user_name")
    @Expose
    var user_name: String? = ""
    @SerializedName("user_mobile")
    @Expose
    var user_mobile: String? = ""
    @SerializedName("user_birthday")
    @Expose
    var user_birthday: String? = ""
    @SerializedName("user_pic")
    @Expose
    var user_pic: String? = ""
    @SerializedName("user_grade")
    @Expose
    var user_grade: String? = ""
    @SerializedName("user_province")
    @Expose
    var user_province: String? = ""
    @SerializedName("user_province_name")
    @Expose
    var user_province_name: String? = ""
    @SerializedName("league_score")
    @Expose
    var league_score: String? = ""
    @SerializedName("score")
    @Expose
    var score: String? = ""
}

class LeagueSubmit : Serializable {
    @SerializedName("league_id")
    @Expose
    var league_id: String? = ""
    @SerializedName("score")
    @Expose
    var score: String? = ""

    constructor(league_id: String, score: String) {
        this.league_id = league_id
        this.score = score
    }
}