package ir.amin.HaftTeen.vasni.model.teentaak

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserProfile : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int = 0
    @SerializedName("name")
    @Expose
    var name: String? = ""
    @SerializedName("family")
    @Expose
    var family: String? = ""
    @SerializedName("mobile")
    @Expose
    var mobile: String? = ""
    @SerializedName("status")
    @Expose
    var status: Int? = 0
    @SerializedName("genderId")
    @Expose
    var genderId: Int? = 0
    @SerializedName("gender")
    @Expose
    var gender: String? = ""
    @SerializedName("province")
    @Expose
    var province: String? = ""
    @SerializedName("provinceId")
    @Expose
    var provinceId: String? = ""
    @SerializedName("pic")
    @Expose
    var pic: String? = ""
    @SerializedName("phone")
    @Expose
    var phone: String? = ""
    @SerializedName("age")
    @Expose
    var age: Int? = 0
    @SerializedName("grade")
    @Expose
    var grade: String? = ""
    @SerializedName("gradeId")
    @Expose
    var gradeId: Int? = 0
    @SerializedName("birthday")
    @Expose
    var birthday: String? = ""
}

data class PlayerChart(
        @SerializedName("id")
        @Expose
        var id: Int = 0,
        @SerializedName("title")
        @Expose
        var title: String = "",
        @SerializedName("description")
        @Expose
        var description: String = "",
        @SerializedName("length_data")
        @Expose
        var length_data: Int = 0,
        @SerializedName("have_banners")
        @Expose
        var have_banners: Boolean = false,
        @SerializedName("banners")
        @Expose
        var banners: ArrayList<Banner> = ArrayList(),
        @SerializedName("data")
        @Expose
        var data: ArrayList<Player> = ArrayList(),
        @SerializedName("user_total_score")
        @Expose
        var user_total_score: Int = 0,
        @SerializedName("user_total_rank")
        @Expose
        var user_total_rank: Int = 0,
        @SerializedName("user_today_rank")
        @Expose
        var user_today_rank: Int = 0,
        @SerializedName("user_total_match_number")
        @Expose
        var user_total_match_number: Int = 0,
        @SerializedName("user_today_match_number")
        @Expose
        var user_today_match_number: Int = 0,
        @SerializedName("user_status")
        @Expose
        var user_status: String = "",
        @SerializedName("isActive")
        @Expose
        var isActive: String = "",
        @SerializedName("user_chance")
        @Expose
        var user_chance: Int = 0,
        @SerializedName("userChance")
        @Expose
        var userChance: Int = 0,
        @SerializedName("userPoint")
        @Expose
        var userPoint: Int = 0,
        @SerializedName("userTodayRank")
        @Expose
        var userTodayRank: Int = 0,
        @SerializedName("userTotalRank")
        @Expose
        var userTotalRank: Int = 0,
        @SerializedName("profile")
        @Expose
        var profile: Boolean = false,
        @SerializedName("birthday")
        @Expose
        var birthday: String? = "",
        @SerializedName("voucher")
        @Expose
        var voucher: Boolean? = false,
        @SerializedName("balance")
        @Expose
        var balance: String? = ""

) : Serializable

data class Player(
        @SerializedName("date")
        @Expose
        var date: String = "",
        @SerializedName("timestamp")
        @Expose
        var timestamp: String = "",
        @SerializedName("user_id")
        @Expose
        var user_id: String = "",
        @SerializedName("user_grade")
        @Expose
        var grade: String = "",
        @SerializedName("user_province_name")
        @Expose
        var province: String = "",
        @SerializedName("user_name")
        @Expose
        var name: String = "",
        @SerializedName("family")
        @Expose
        var family: String = "",
        @SerializedName("user_pic")
        @Expose
        var pic: String = "",
        @SerializedName("score")
        @Expose
        var sum: String = "",
        @SerializedName("userPoint")
        @Expose
        var userPoint: String = "",
        @SerializedName("userTodayRank")
        @Expose
        var userTodayRank: String = "",
        @SerializedName("userTotalRank")
        @Expose
        var userTotalRank: String = ""
) : Serializable

data class Competition(
        @SerializedName("users")
        @Expose
        var users: ArrayList<PlayerChart> = ArrayList(),
        @SerializedName("user")
        @Expose
        var user: PlayerChart = PlayerChart()
) : Serializable