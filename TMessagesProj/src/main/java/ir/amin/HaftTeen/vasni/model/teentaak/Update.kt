package ir.amin.HaftTeen.vasni.model.teentaak

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Update(
        @SerializedName("appName")
        @Expose
        var appName: String? = "",
        @SerializedName("changeLog")
        @Expose
        var changeLog: String? = "",
        @SerializedName("apkUrl")
        @Expose
        var apkUrl: String? = "",
        @SerializedName("updateTips")
        @Expose
        var updateTips: String? = "",
        @SerializedName("forceUpgrade")
        @Expose
        var forceUpgrade: Boolean? = false,
        @SerializedName("versionCode")
        @Expose
        var versionCode: String? = "",
        @SerializedName("versionName")
        @Expose
        var versionName: String? = ""
) : Serializable
