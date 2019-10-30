package ir.amin.HaftTeen.vasni.adapter.Vitrin

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.row_media_music_more.*
import kotlinx.android.synthetic.main.view_wallet_dialog.view.*
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.BottomDialog
import me.himanshusoni.chatmessageview.ui.CenterDialog
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.ui.LaunchActivity.presentFragment
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.core.DataLoader
import ir.amin.HaftTeen.vasni.extention.getData
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.Event
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Music
import ir.amin.HaftTeen.vasni.teentaak.fragment.market.MusicPlayerFragment
import ir.amin.HaftTeen.R

class MusicMoreAdapter(containerView: View) : MoreViewHolder<Music>(containerView) {

    override fun bindData(data: Music, payloads: List<Any>) {

        imv_media_music_more.loadImage(containerView.context, data.thumbnail!!, pv_loading_pic_music_more)
        tv_media_music_more_name.text = data.title
//        tv_media_music_more_decs.text = data.singer

        if (data.price == 0)
            tv_media_music_more_price.text = containerView.context.getString(R.string.free)
        else
            tv_media_music_more_price.text =
                    data.price.toString() + " " + containerView.context.getString(R.string.currency)

        ll_music_more.setOnClickListener {
            DataLoader.instance.music = data
            if (VasniSchema.instance.buy_is_visible != "0") {
                if (data.wallet!!.bought == false && data.price != 0) {
                    getWalletBalance(data)
//                if (data.wallet!!.balance == "0") {
//                    activeWallet(data)
//                } else {
//                    buy(data)
//                }
                } else {
                    presentFragment(MusicPlayerFragment(""))
                }
            } else {
                VasniSchema.instance.showMessage(
                        containerView.context,
                        containerView.context.getString(R.string.message_buy_package),
                        "",
                        containerView.context.getString(R.string.ok)
                )
            }
        }

    }

    fun buy(data: Music) {
        BottomDialog.Builder(containerView.context!!)
                .setContent(containerView.context.getString(R.string.wallet_message_dialog))
                .setNegativeText(containerView.context.getString(R.string.yes))
                .setNegativeTextColor(ContextCompat.getColor(containerView.context!!, R.color.colorAccent))
                .setPositiveText(containerView.context.getString(R.string.no))
                .autoDismiss(false)
                .setCancelable(false)
                .onNegative(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        if (data.price!! <= data.wallet!!.balance!!.toInt()) {
                            getWalletReduce(data)
                        } else {
                            VasniSchema.instance.showMessage(
                                    containerView.context!!,
                                    containerView.context.getString(R.string.wallet_no_balance),
                                    "",
                                    containerView.context.getString(R.string.ok)
                            )
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

    fun getWalletReduce(data: Music) {
        EventBus.getDefault().post(Event(VasniSchema.instance.showLoading))
        ApiService.apiInterface.getWalletReduce(data.id!!, VasniSchema.instance.music_type!!).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    EventBus.getDefault().post(Event(VasniSchema.instance.hideLoading))
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        presentFragment(MusicPlayerFragment(""))
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

    fun activeWallet(data: Music) {
        BottomDialog.Builder(containerView.context!!)
                .setContent(containerView.context.getString(R.string.wallet_message_dialog_active))
                .setNegativeText(containerView.context.getString(R.string.ok))
                .setNegativeTextColor(ContextCompat.getColor(containerView.context!!, R.color.colorAccent))
                .setPositiveText(containerView.context.getString(R.string.skip))
                .autoDismiss(false)
                .setCancelable(false)
                .onNegative(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        activeWalletDialog(data)
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

    fun activeWalletDialog(data: Music) {
        val inflater = containerView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customView = inflater.inflate(R.layout.view_wallet_dialog, null)

        var dialog: CenterDialog = CenterDialog.Builder(containerView.context!!)
                .setCustomView(customView)
                .autoDismiss(false)
                .setCancelable(true)
                .show()

        customView.tv_dialog_wallet_detail.setText(containerView.context.getString(R.string.wallet_message_dialog_active_desc), true)
        customView.btn_submit_dialog_wallet.setOnClickListener {
            if (customView.et_dialog_wallet_active_code.text.trim().isEmpty()) {
                VasniSchema.instance.showMessage(
                        containerView.context!!,
                        containerView.context.getString(R.string.wallet_is_empty),
                        "",
                        containerView.context.getString(R.string.ok)
                )
            } else {
                customView.pv_loading_active_wallet!!.visibility = View.VISIBLE
                customView.rl_active_wallet!!.visibility = View.GONE
                ApiService.apiInterface.checkWalletVoucher(
                        customView.et_dialog_wallet_active_code.text.trim().toString()
                ).enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful && response.body() != null) {
                            dialog.dismiss()
                            if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                                buy(data)
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

    fun getWalletBalance(data: Music) {
        EventBus.getDefault().post(Event(VasniSchema.instance.showLoading))
        ApiService.apiInterface.getWalletBalance().enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    EventBus.getDefault().post(Event(VasniSchema.instance.hideLoading))
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        data.wallet!!.balance = getData(response.body()!!).get("balance").asString
                        if (data.wallet!!.balance == "0") {
                            activeWallet(data)
                        } else {
                            buy(data)
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
}