package ir.amin.HaftTeen.vasni.teentaak.fragment.pay

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonObject
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.BottomDialog
import me.himanshusoni.chatmessageview.ui.JTextView
import me.himanshusoni.chatmessageview.ui.MButton
import me.himanshusoni.chatmessageview.ui.MEditText
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
import ir.amin.HaftTeen.vasni.model.teentaak.PlayerChart
import ir.amin.HaftTeen.vasni.utils.MSharePk
import ir.amin.HaftTeen.R

class PayFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate, Callback<JsonObject> {

    private var txt: String = ""
    private lateinit var tv_pay_detail: JTextView
    private lateinit var et_pay_active_code: MEditText
    private lateinit var et_pay_phone: MEditText
    private lateinit var btn_submit_pay: MButton
    private lateinit var pv_pay: View

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
        fragmentView = factory.inflate(R.layout.frg_pay, null)
        fragmentView.setOnTouchListener { v, event -> true }

        tv_pay_detail = fragmentView.findViewById(R.id.tv_pay_detail)
        et_pay_active_code = fragmentView.findViewById(R.id.et_pay_active_code)
        et_pay_phone = fragmentView.findViewById(R.id.et_pay_phone)
        btn_submit_pay = fragmentView.findViewById(R.id.btn_submit_pay)
        pv_pay = fragmentView.findViewById(R.id.pv_pay)

        tv_pay_detail.setText(fragmentView.context.getString(R.string.voucher_desc), true)
        btn_submit_pay.setOnClickListener {
            checkInput()
        }

        return fragmentView
    }

    private fun checkInput() {
        if (et_pay_active_code.text.length < 2) {
            VasniSchema.instance.showMessage(
                    fragmentView.context,
                    fragmentView.context.getString(R.string.active_code_error),
                    "",
                    fragmentView.context.getString(R.string.ok)
            )
            return
        }

        if (et_pay_phone.text.length < 11) {
            VasniSchema.instance.showMessage(
                    fragmentView.context,
                    fragmentView.context.getString(R.string.phone_pay_eror),
                    "",
                    fragmentView.context.getString(R.string.ok)
            )
            return
        }
        VasniSchema.instance.show(true, pv_pay)
        ApiService.apiInterface.checkVoucher(
                et_pay_active_code.text.toString().trim(),
                et_pay_phone.text.toString().trim()
        ).enqueue(this)

    }

    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
        try {
            VasniSchema.instance.show(false, pv_pay)
            if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                getUserStatus(fragmentView.context)
            } else {
                VasniSchema.instance.showMessage(
                        fragmentView.context,
                        getError(response.body()!!).message.toString(),
                        "",
                        fragmentView.context.getString(R.string.ok)
                )
            }

        } catch (e: Exception) {

        }

    }

    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
        VasniSchema.instance.showMessage(fragmentView.context,
                fragmentView.context.getString(R.string.server_error),
                "",
                fragmentView.context.getString(R.string.ok))
    }

    fun getUserStatus(context: Context) {
        ApiService.apiInterface.getSimpleUserDetail()
                .enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful && response.body() != null) {
                            if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                                val userDetails = Gson().fromJson(getData(response.body()!!), PlayerChart::class.java)
                                if (userDetails.isActive.equals(VasniSchema.instance.active)) {
                                    MSharePk.putBoolean(context, VasniSchema.instance.checkSimple, true)
                                    BottomDialog.Builder(fragmentView.context)
                                            .setContent(fragmentView.context.getString(R.string.voucher_active))
                                            .setNegativeText(fragmentView.context.getString(R.string.ok))
                                            .setNegativeTextColor(
                                                    ContextCompat.getColor(
                                                            fragmentView.context,
                                                            R.color.colorAccent
                                                    )
                                            )
                                            .autoDismiss(false)
                                            .setCancelable(false)
                                            .onNegative(object : BottomDialog.ButtonCallback {
                                                override fun onClick(dialog: BottomDialog) {
                                                    dialog.dismiss()
                                                    finishFragment()
                                                }
                                            })
                                            .show()
                                } else {
                                    MSharePk.putBoolean(context, VasniSchema.instance.checkSimple, false)
                                    BottomDialog.Builder(fragmentView.context)
                                            .setContent(fragmentView.context.getString(R.string.voucher_inactive))
                                            .setNegativeText(fragmentView.context.getString(R.string.ok))
                                            .setNegativeTextColor(
                                                    ContextCompat.getColor(
                                                            fragmentView.context,
                                                            R.color.colorAccent
                                                    )
                                            )
                                            .autoDismiss(false)
                                            .setCancelable(false)
                                            .onNegative(object : BottomDialog.ButtonCallback {
                                                override fun onClick(dialog: BottomDialog) {
                                                    dialog.dismiss()
                                                    finishFragment()
                                                }
                                            })
                                            .show()
                                }
                            } else {
                                VasniSchema.instance.showMessage(
                                        context,
                                        getError(response.body()!!).message.toString(),
                                        "",
                                        context.getString(R.string.ok)
                                )
                            }
                        }
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        VasniSchema.instance.showMessage(
                                context,
                                context.getString(R.string.server_error),
                                "",
                                context.getString(R.string.ok)
                        )
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