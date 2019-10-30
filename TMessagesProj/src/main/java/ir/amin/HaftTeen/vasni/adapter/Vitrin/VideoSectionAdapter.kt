package ir.amin.HaftTeen.vasni.adapter.Vitrin

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.row_section_video.*
import kotlinx.android.synthetic.main.view_wallet_dialog.view.*
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.VideoPlayer.Jzvd
import me.himanshusoni.chatmessageview.ui.BottomDialog
import me.himanshusoni.chatmessageview.ui.CenterDialog
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.vasni.api.AbrArvanService
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.api.RahpoService
import ir.amin.HaftTeen.vasni.extention.getData
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Video
import ir.amin.HaftTeen.R

class VideoSectionAdapter(containerView: View) : MoreViewHolder<Video>(containerView) {

    override fun bindData(data: Video, payloads: List<Any>) {
//        section_video.setUp(data.link, data.title, Jzvd.SCREEN_WINDOW_LIST)

        if (data.wallet!!.bought == false && data.price != 0) {
            if (data.wallet!!.balance == "0") {
                activeWallet()
            } else {
                buy(data)
            }
        } else {
            if (data.fileServiceProvider == VasniSchema.instance.rahpo_file_service) {
                getVideoLink(data.guid!!, data.title!!)
            } else if (data.fileServiceProvider == VasniSchema.instance.abr_arvan_file_service) {
                getMediaFileAbrArvan(data.guid!!, data.title!!)
            } else {
                section_video.setUp(data.link, data.title, Jzvd.SCREEN_WINDOW_LIST)
            }
        }


        section_video.thumbImageView.loadImage(containerView.context, data.banner!!)
        section_video.titleTextView.visibility = View.VISIBLE
        tv_section_video_name.text = data.title
    }


    fun getVideoLink(guid: String, title: String) {
        RahpoService.apiInterface.getVitrinRahpoLink(
                "689725394165", guid, "Free", "Free"
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        if (response.body()!!.get("error_code").asString == "0") {
                            var link = response.body()!!.get("full_addr").asString
                            section_video.setUp(link, title, Jzvd.SCREEN_WINDOW_LIST)
                            section_video.startButton.visibility = View.GONE
                        } else {
                            VasniSchema.instance.showMessage(
                                    containerView.context!!,
                                    response.body()!!.get("error_desc").asString,
                                    "",
                                    containerView.context.getString(R.string.ok)
                            )
                        }
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

    fun getMediaFileAbrArvan(guid: String, title: String) {
        AbrArvanService.apiInterface.getAbrArvanLink(
                VasniSchema.instance.getAbrArvanToken(
                        VasniSchema.instance.api_key_abr_arvan
                ), guid).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    var link = getData(response.body()!!).get("hls_playlist").asString
                    section_video.setUp(link, title, Jzvd.SCREEN_WINDOW_LIST)
                    section_video.startButton.visibility = View.GONE
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

    fun buy(data: Video) {
        BottomDialog.Builder(containerView.context!!)
                .setContent(containerView.context.getString(R.string.wallet_message_dialog))
                .setNegativeText(containerView.context.getString(R.string.yes))
                .setNegativeTextColor(ContextCompat.getColor(containerView.context!!, R.color.colorAccent))
                .setPositiveText(containerView.context.getString(R.string.skip))
                .autoDismiss(false)
                .setCancelable(false)
                .onNegative(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        if (data.price!! <= data.wallet!!.balance!!.toInt()) {
                            getWalletReduce(data)
                        }
                        dialog.dismiss()
                    }
                })
                .onPositive(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                    }
                })
                .show()
    }

    fun getWalletReduce(data: Video) {
        ApiService.apiInterface.getWalletReduce(data.id!!, VasniSchema.instance.video_type!!
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        if (data.fileServiceProvider == VasniSchema.instance.rahpo_file_service) {
                            getVideoLink(data.guid!!, data.title!!)
                        } else if (data.fileServiceProvider == VasniSchema.instance.abr_arvan_file_service) {
                            getMediaFileAbrArvan(data.guid!!, data.title!!)
                        } else {
                            section_video.setUp(data.link, data.title, Jzvd.SCREEN_WINDOW_LIST)
                        }
                    } else {
                        VasniSchema.instance.showMessage(
                                containerView.context,
                                getError(response.body()!!).message.toString(),
                                "",
                                containerView.context.getString(R.string.ok)
                        )
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

    fun activeWallet() {
        BottomDialog.Builder(containerView.context!!)
                .setContent(containerView.context.getString(R.string.wallet_message_dialog_active))
                .setNegativeText(containerView.context.getString(R.string.ok))
                .setNegativeTextColor(ContextCompat.getColor(containerView.context!!, R.color.colorAccent))
                .setPositiveText(containerView.context.getString(R.string.skip))
                .autoDismiss(false)
                .setCancelable(false)
                .onNegative(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        activeWalletDialog()
                        dialog.dismiss()
                    }
                })
                .onPositive(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                    }
                })
                .show()
    }

    fun activeWalletDialog() {
        val inflater = containerView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customView = inflater.inflate(R.layout.view_wallet_dialog, null)

        var dialog: CenterDialog = CenterDialog.Builder(containerView.context!!)
                .setCustomView(customView)
                .autoDismiss(false)
                .setCancelable(true)
                .show()

        customView.tv_dialog_wallet_detail.text = containerView.context.getString(R.string.wallet_message_dialog_active_desc)
        customView.btn_submit_dialog_wallet.setOnClickListener {
            dialog.dismiss()
            ApiService.apiInterface.checkWalletVoucher(
                    customView.et_dialog_wallet_active_code.text.trim().toString()
            ).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful && response.body() != null) {
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            VasniSchema.instance.showMessage(
                                    containerView.context!!,
                                    getData(response.body()!!).get("message").asString,
                                    "",
                                    containerView.context.getString(R.string.ok)
                            )
                        } else {
                            VasniSchema.instance.showMessage(
                                    containerView.context,
                                    getError(response.body()!!).message.toString(),
                                    "",
                                    containerView.context.getString(R.string.ok)
                            )
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

    }


}