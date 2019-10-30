package ir.amin.HaftTeen.vasni.model.teentaak


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ir.amin.HaftTeen.vasni.utils.grid.AsymmetricItem
import java.io.Serializable

data class DynamicLayout(
        @SerializedName("columnspan")
        @Expose
        var columnspan: Int,
        @SerializedName("rowspan")
        @Expose
        var rowspan: Int,
        @SerializedName("position")
        @Expose
        var position: Int,
        @SerializedName("background")
        @Expose
        var background: String,
        @SerializedName("columns")
        @Expose
        var columns: Int,
        @SerializedName("rows")
        @Expose
        var rows: Int,
        @SerializedName("itemId")
        @Expose
        var itemId: Int,
        @SerializedName("event")
        @Expose
        var event: String,
        @SerializedName("eventData")
        @Expose
        var eventData: String,
        @SerializedName("clickable")
        @Expose
        var clickable: Int,
        @SerializedName("need_profile")
        @Expose
        var need_profile: Int,
        @SerializedName("is_free")
        @Expose
        var is_free: Int,
        @SerializedName("title")
        @Expose
        var title: String = "",
        @SerializedName("faq")
        @Expose
        var faq: String = ""
) : AsymmetricItem, Parcelable, Serializable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeInt(columnspan)
        dest!!.writeInt(rowspan)
        dest!!.writeInt(position)
    }

    override fun getColumnSpan(): Int {
        return columnspan
    }

    override fun getRowSpan(): Int {
        return rowspan
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<DynamicLayout> {
        override fun createFromParcel(parcel: Parcel): DynamicLayout {
            return DynamicLayout(parcel)
        }

        override fun newArray(size: Int): Array<DynamicLayout?> {
            return arrayOfNulls(size)
        }
    }

}

