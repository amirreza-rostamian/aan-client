package ir.amin.HaftTeen.vasni.teentaak.fragment

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import com.google.gson.JsonObject
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.BottomDialog
import me.himanshusoni.chatmessageview.ui.MButton
import me.himanshusoni.chatmessageview.ui.MEditText
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.messenger.NotificationCenter
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.model.teentaak.EventHandler
import ir.amin.HaftTeen.R

class SendCommentFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate, Callback<JsonObject> {

    private var txt: String = ""
    private var eventHandler = EventHandler()
    private lateinit var et_comment_content: MEditText
    private lateinit var btn_submit_comment: MButton
    private lateinit var pv_send_comment: View

    constructor(txt: String, data: EventHandler) {
        this.txt = txt
        this.eventHandler = data
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
        fragmentView = factory.inflate(R.layout.frg_send_comment, null)
        fragmentView.setOnTouchListener { v, event -> true }

        et_comment_content = fragmentView.findViewById(R.id.et_comment_content)
        btn_submit_comment = fragmentView.findViewById(R.id.btn_submit_comment)
        pv_send_comment = fragmentView.findViewById(R.id.pv_send_comment)

        et_comment_content.setHint(eventHandler.description)
        if (eventHandler.button.length > 1)
            btn_submit_comment.setText(eventHandler.button)

        btn_submit_comment.setOnClickListener {
            if (et_comment_content.text.toString().trim().isEmpty()) {
                VasniSchema.instance.showMessage(
                        context!!,
                        context.getString(R.string.input_comment_empty),
                        "",
                        context.getString(R.string.ok)
                )
            } else if (et_comment_content.text.length > eventHandler.characters.toInt()) {
                VasniSchema.instance.showMessage(
                        context!!,
                        context.getString(R.string.input_comment_invalid),
                        "",
                        context.getString(R.string.ok)
                )
            } else {
                VasniSchema.instance.show(true, pv_send_comment)
                ApiService.apiInterface.complain(
                        RequestBody.create(okhttp3.MultipartBody.FORM, et_comment_content.text.toString().trim()),
                        RequestBody.create(okhttp3.MultipartBody.FORM, eventHandler.tileId)
                ).enqueue(this)
            }
        }

        return fragmentView

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

    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
        if (response.isSuccessful && response.body() != null) {
            VasniSchema.instance.show(false, pv_send_comment)
            if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                et_comment_content.setText("")
                BottomDialog.Builder(fragmentView.context!!)
                        .setContent(fragmentView.context!!.getString(R.string.submit_commite_send))
                        .setNegativeText(fragmentView.context!!.getString(R.string.ok))
                        .setNegativeTextColor(ContextCompat.getColor(fragmentView.context!!, R.color.colorAccent))
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
                VasniSchema.instance.showMessage(
                        fragmentView.context!!,
                        getError(response.body()!!).message.toString(),
                        "",
                        fragmentView.context!!.getString(R.string.ok)
                )
            }
        }

    }

    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
        VasniSchema.instance.showMessage(
                fragmentView.context!!,
                fragmentView.context!!.getString(R.string.server_error),
                "",
                fragmentView.context!!.getString(R.string.ok)
        )
    }


}
