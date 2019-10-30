package ir.amin.HaftTeen.vasni.adapter.Media

import android.support.v4.content.ContextCompat
import android.view.View
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.row_my_media.*
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.VideoPlayer.Jzvd
import me.himanshusoni.chatmessageview.ui.BottomDialog
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import org.greenrobot.eventbus.EventBus

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.vasni.api.AbrArvanService
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.api.RahpoService
import ir.amin.HaftTeen.vasni.extention.*
import ir.amin.HaftTeen.vasni.model.teentaak.Event
import ir.amin.HaftTeen.vasni.model.teentaak.Media
import ir.amin.HaftTeen.R

class MyMediaAdapter(containerView: View) : MoreViewHolder<Media>(containerView) {
    var file = ""
    override fun bindData(data: Media, payloads: List<Any>) {
        try {
            tv_my_media_title.setText(data.title)
            tv_my_media_view_count.setText(data.views.toString() + " " + containerView.context.getString(R.string.views))
            tv_my_media_category.setText(data.category_title)
            imv_my_media_share.setOnClickListener {
                shareLink(containerView.context, data.file)
            }
            tv_my_media_like.setText(data.likes.toString())

            btn_more_my_media.setOnClickListener {
                deleteAlert(data.id)
            }

            if (data.media_type == VasniSchema.instance.MediaType_video) {
//                if (data.file.endsWith(".mp4"))
//                    player_my_media.setUp(data.file, "", Jzvd.SCREEN_WINDOW_LIST)
//                else
//                    getMediaFile(data.id)
                player_my_media.visibility = View.VISIBLE
                imv_my_media_pic.visibility = View.GONE
                player_my_media.thumbImageView.loadImage(containerView.context, data.thumbnail)

                if (data.service == VasniSchema.instance.rahpo_file_service) {
                    getVideoLinkRahpo(data.file)
                } else if (data.service == VasniSchema.instance.abr_arvan_file_service) {
                    getMediaFileAbrArvan(data.file)
                } else {
                    player_my_media.setUp(data.file, data.title, Jzvd.SCREEN_WINDOW_LIST)
                }

            } else {
                player_my_media.visibility = View.GONE
                imv_my_media_pic.visibility = View.VISIBLE
                imv_my_media_pic.loadImage(containerView.context, data.file, pv_loading_my_media)
            }


        } catch (e: Exception) {

        }
    }

    fun getVideoLinkRahpo(key: String) {
        RahpoService.apiInterface.getVitrinRahpoLink(
                "689725394165", key, "Free", "Free"
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        if (response.body()!!.get("error_code").asString == "0") {
                            file = response.body()!!.get("full_addr").asString
                            player_my_media.setUp(file, "", Jzvd.SCREEN_WINDOW_LIST)
                        } else {
                            VasniSchema.instance.showMessage(
                                    containerView.context,
                                    response.body()!!.get("error_desc").asString,
                                    "",
                                    containerView.context.getString(R.string.ok)
                            )
                        }
                    } catch (e: Exception) {

                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.showMessage(
                        containerView.context,
                        containerView.context.getString(R.string.server_error),
                        "",
                        containerView.context.getString(R.string.ok)
                )
            }
        })
    }

    fun getMediaFileAbrArvan(key: String) {
        AbrArvanService.apiInterface.getAbrArvanLink(
                VasniSchema.instance.getAbrArvanToken(
                        VasniSchema.instance.api_key_abr_arvan
                ), key).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        file = getData(response.body()!!).get("hls_playlist").asString
                        player_my_media.setUp(file, "", Jzvd.SCREEN_WINDOW_LIST)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.showMessage(
                        containerView.context!!,
                        containerView.context!!.getString(R.string.server_error),
                        "",
                        containerView.context!!.getString(R.string.ok)
                )
            }
        })
    }

    fun getMediaFile(id: String) {
        ApiService.apiInterface.getMediaFile(id).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        file = getData(response.body()!!).get("file").asString
                        player_my_media.setUp(file, "", Jzvd.SCREEN_WINDOW_LIST)
                    } else {
                        VasniSchema.instance.showMessage(
                                containerView.context!!,
                                getError(response.body()!!).message.toString(),
                                "",
                                containerView.context!!.getString(R.string.ok)
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.showMessage(
                        containerView.context!!,
                        containerView.context!!.getString(R.string.server_error),
                        "",
                        containerView.context!!.getString(R.string.ok)
                )
            }
        })
    }

    fun deleteAlert(id: String) {
        BottomDialog.Builder(containerView.context!!)
                .setContent(containerView.context.getString(R.string.delete_user_media))
                .setPositiveText(containerView.context.getString(R.string.no))
                .setNegativeText(containerView.context.getString(R.string.yes))
                .setPositiveTextColor(ContextCompat.getColor(containerView.context!!, R.color.colorBlack))
                .setNegativeTextColor(ContextCompat.getColor(containerView.context!!, R.color.colorAccent))
                .autoDismiss(false)
                .setCancelable(false)
                .onNegative(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                        deletePicture(id)
                    }
                })
                .onPositive(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                    }
                })
                .show()
    }

    fun deletePicture(id: String) {
        ApiService.apiInterface.deleteUsersMedia(id).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        EventBus.getDefault().post(Event(VasniSchema.instance.refresh))
                    } else {
                        VasniSchema.instance.showMessage(
                                containerView.context!!,
                                getError(response.body()!!).message.toString(),
                                "",
                                containerView.context!!.getString(R.string.ok)
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.showMessage(
                        containerView.context!!,
                        containerView.context!!.getString(R.string.server_error),
                        "",
                        containerView.context!!.getString(R.string.ok)
                )
            }
        })
    }

}