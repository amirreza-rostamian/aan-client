package ir.amin.HaftTeen.vasni.model.teentaak

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CheckTci : Serializable {
    @SerializedName("status")
    @Expose
    var status: String? = ""
}

class CheckError : Serializable {
    @SerializedName("name")
    @Expose
    var name: String? = ""
    @SerializedName("code")
    @Expose
    var code: String? = ""
    @SerializedName("message")
    @Expose
    var message: String? = ""
    @SerializedName("status")
    @Expose
    var status: String? = ""

    @SerializedName("title")
    @Expose
    var title: String? = ""
    @SerializedName("details")
    @Expose
    var details: String? = ""
    @SerializedName("href")
    @Expose
    var href: String? = ""
}

class CheckStatus : Serializable {
    @SerializedName("status")
    @Expose
    var status: Int? = 0
}

class CheckSuccess : Serializable {
    @SerializedName("success")
    @Expose
    var success: Int? = 0
}

class CheckErrors : Serializable {

}
