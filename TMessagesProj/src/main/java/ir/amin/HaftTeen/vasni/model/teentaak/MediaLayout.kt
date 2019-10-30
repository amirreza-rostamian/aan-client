package ir.amin.HaftTeen.vasni.model.teentaak

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ir.amin.HaftTeen.vasni.utils.grid.AsymmetricItem
import java.io.Serializable


data class MediaLayout(
        @SerializedName("columnspan")
        @Expose
        var columnspan: Int?,
        @SerializedName("rowspan")
        @Expose
        var rowspan: Int?,
        @SerializedName("position")
        @Expose
        var position: Int?,
        @SerializedName("id")
        @Expose
        var id: String? = "",
        @SerializedName("thumbnail")
        @Expose
        var thumbnail: String? = "",
        @SerializedName("media_type")
        @Expose
        var media_type: String? = "",
        @SerializedName("file")
        @Expose
        var file: String? = ""
) : AsymmetricItem, Parcelable, Serializable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeInt(columnspan!!)
        dest!!.writeInt(rowspan!!)
        dest!!.writeInt(position!!)
    }

    override fun getColumnSpan(): Int {
        return columnspan!!
    }

    override fun getRowSpan(): Int {
        return rowspan!!
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MediaLayout> {
        override fun createFromParcel(parcel: Parcel): MediaLayout {
            return MediaLayout(parcel)
        }

        override fun newArray(size: Int): Array<MediaLayout?> {
            return arrayOfNulls(size)
        }
    }

}


class Media : Serializable {
    @SerializedName("id")
    @Expose
    var id: String = ""
    @SerializedName("title")
    @Expose
    var title: String = ""
    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String = ""
    @SerializedName("service")
    @Expose
    var service: String = ""
    @SerializedName("media_type")
    @Expose
    var media_type: String = ""
    @SerializedName("file")
    @Expose
    var file: String = ""
    @SerializedName("category_id")
    @Expose
    var category_id: String = ""
    @SerializedName("category_title")
    @Expose
    var category_title: String = ""
    @SerializedName("parent_category_id")
    @Expose
    var parent_category_id: String = ""
    @SerializedName("description")
    @Expose
    var description: String = ""
    @SerializedName("created_timestamp")
    @Expose
    var created_timestamp: String = ""
    @SerializedName("created")
    @Expose
    var created: String = ""
    @SerializedName("likes")
    @Expose
    var likes: Int = 0
    @SerializedName("views")
    @Expose
    var views: Int = 0
    @SerializedName("user_name")
    @Expose
    var user_name: String = ""
    @SerializedName("user_pic")
    @Expose
    var user_pic: String = ""
    @SerializedName("user_liked")
    @Expose
    var user_liked: Boolean = false
    @SerializedName("user_score")
    @Expose
    var user_score: String = ""
    @SerializedName("comments")
    @Expose
    var comments: ArrayList<Comment>? = ArrayList()
}
