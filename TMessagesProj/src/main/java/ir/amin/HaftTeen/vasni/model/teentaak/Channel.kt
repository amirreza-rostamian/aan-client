package ir.amin.HaftTeen.vasni.model.teentaak

import com.google.gson.annotations.SerializedName


data class Channel(
        @SerializedName("channel_id")
        var channelId: String,
        @SerializedName("id")
        var id: Int,
        @SerializedName("pic")
        var pic: String,
        @SerializedName("title")
        var title: String
)

