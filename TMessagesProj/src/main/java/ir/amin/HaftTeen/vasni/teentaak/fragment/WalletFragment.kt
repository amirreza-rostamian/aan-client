package ir.amin.HaftTeen.vasni.teentaak.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.gson.JsonObject
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.messenger.NotificationCenter
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.extention.getData
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.model.api.profile.ResponseProfileData
import ir.amin.HaftTeen.vasni.webservice.GetProfile
import ir.amin.HaftTeen.R

class WalletFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate {


    private var txt: String = ""
    private var tv_balance: TextView? = null
    private var pv_wallet: View? = null
    private var btn_submit_wallet: Button? = null
    private var et_wallet_active_code: EditText? = null

    constructor(txt: String) {
        this.txt = txt
    }

    override fun createView(context: Context?): View {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back)
        actionBar.setAllowOverlayTitle(true)
        actionBar.setTitle(txt)
        actionBar.setActionBarMenuOnItemClick(object : ActionBar.ActionBarMenuOnItemClick() {
            override fun onItemClick(id: Int) {
                if (id == -1) {
                    finishFragment()
                }
            }
        })
        val factory = LayoutInflater.from(context)
        fragmentView = factory.inflate(R.layout.frg_wallet, null)
        fragmentView.setOnTouchListener { v, event -> true }
        tv_balance = fragmentView.findViewById(R.id.tv_balance)
        pv_wallet = fragmentView.findViewById(R.id.pv_wallet)
        btn_submit_wallet = fragmentView.findViewById(R.id.btn_submit_wallet)
        et_wallet_active_code = fragmentView.findViewById(R.id.et_wallet_active_code)
        btn_submit_wallet!!.setOnClickListener {
            checkInput()
        }
        initview()
        return fragmentView
    }

    private fun initview() {
        var getProfile = GetProfile(object : GetProfile.OnResponse {
            override fun onSuccess(data: ResponseProfileData?) {
                tv_balance!!.setText("" + data!!.balance + " ریال ")
            }

            override fun onError(message: String?) {

            }
        })
        getProfile.getProfile()
    }

    private fun checkInput() {
        if (et_wallet_active_code!!.text.trim().isEmpty()) {
            VasniSchema.instance.showMessage(
                    fragmentView.context!!,
                    fragmentView.context.getString(R.string.wallet_is_empty),
                    "",
                    fragmentView.context.getString(R.string.ok)
            )
            return
        } else if (et_wallet_active_code!!.text.length < 2) {
            VasniSchema.instance.showMessage(
                    fragmentView.context!!,
                    fragmentView.context.getString(R.string.active_code_error),
                    "",
                    fragmentView.context.getString(R.string.ok)
            )
            return
        }

        VasniSchema.instance.show(true, pv_wallet!!)
        ApiService.apiInterface.checkWalletVoucher(
                et_wallet_active_code!!.text.toString().trim()
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    VasniSchema.instance.show(false, pv_wallet!!)
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        VasniSchema.instance.showMessage(
                                fragmentView.context!!,
                                getData(response.body()!!).get("message").asString,
                                "",
                                fragmentView.context!!.getString(R.string.ok)
                        )
                        initview()
                        et_wallet_active_code!!.setText("")
                    } else {
                        VasniSchema.instance.showMessage(
                                fragmentView.context!!,
                                getError(response.body()!!).message.toString(),
                                "",
                                fragmentView.context!!.getString(R.string.ok)
                        )
                        et_wallet_active_code!!.setText("")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                try {
                    VasniSchema.instance.showMessage(
                            fragmentView.context!!,
                            fragmentView.context!!.getString(R.string.server_error),
                            "",
                            fragmentView.context!!.getString(R.string.ok)
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

    }

    override fun onFragmentCreate(): Boolean {
        return super.onFragmentCreate()
    }

    override fun onFragmentDestroy() {
        super.onFragmentDestroy()
    }

    override fun didReceivedNotification(id: Int, account: Int, vararg args: Any?) {
    }

    override fun getThemeDescriptions(): Array<ThemeDescription> {
        return arrayOf(ThemeDescription(fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null,
                null, Theme.key_windowBackgroundWhite), ThemeDescription(actionBar, ThemeDescription.FLAG_BACKGROUND,
                null, null, null, null,
                Theme.key_actionBarDefault), ThemeDescription(actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR,
                null, null, null, null,
                Theme.key_actionBarDefaultIcon), ThemeDescription(actionBar, ThemeDescription.FLAG_AB_TITLECOLOR,
                null, null, null, null,
                Theme.key_actionBarDefaultTitle), ThemeDescription(actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR,
                null, null, null, null,
                Theme.key_actionBarDefaultSelector))
    }

}