package ir.amin.HaftTeen.vasni.model.teentaak.Vitrin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Filter : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = "0"
    @SerializedName("name")
    @Expose
    var name: String? = ""
    @SerializedName("isSelected")
    @Expose
    var isSelected: Boolean? = false
}

