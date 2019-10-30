package ir.amin.HaftTeen.vasni.model.teentaak

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DialogContent : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = 0
    @SerializedName("name")
    @Expose
    var name: String? = ""
    @SerializedName("ostanId")
    @Expose
    var ostanId: Int? = 0

    constructor(id: Int, name: String) {
        this.id = id
        this.name = name
    }

    constructor()
    constructor(id: Int, name: String, ostanId: Int) {
        this.id = id
        this.name = name
        this.ostanId = ostanId
    }


}