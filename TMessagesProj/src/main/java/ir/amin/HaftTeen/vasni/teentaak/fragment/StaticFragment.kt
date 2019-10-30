package ir.amin.HaftTeen.vasni.teentaak.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
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
import ir.amin.HaftTeen.vasni.model.teentaak.EventHandler
import ir.amin.HaftTeen.R

class StaticFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate, Callback<JsonObject> {

    private var txt: String = ""
    private var eventHandler = EventHandler()
    private lateinit var tv_static_desc: WebView
    private lateinit var pv_static: View

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
        fragmentView = factory.inflate(R.layout.frg_static, null)
        fragmentView.setOnTouchListener { v, event -> true }

        tv_static_desc = fragmentView.findViewById(R.id.tv_static_desc)
        pv_static = fragmentView.findViewById(R.id.pv_static)

        ApiService.apiInterface.getStaticPage(eventHandler.pages
        ).enqueue(this)

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
            try {
                VasniSchema.instance.show(false, pv_static)
                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
//                    tv_static_desc.setText(Html.fromHtml(getData(response.body()!!).get("content").asString))

//                    var text = Html.fromHtml(getData(response.body()!!).get("content").asString).toString()
//                    tv_static_desc.setAlignment(Paint.Align.RIGHT)
//                    tv_static_desc.setLineSpacing(22)
//                    tv_static_desc.setText(text)
//                    tv_static_desc.setTextSize(1, 16.0f)

                    var prefix = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/IRANSansMobile.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;direction:rtl}</style></head><body>"
                    var postfix = "</body></html>"
                    var htmlString = prefix + getData(response.body()!!).get("content").asString + postfix
                    tv_static_desc.loadDataWithBaseURL("", htmlString, "text/html", "UTF-8", "")


                } else {
                    VasniSchema.instance.showMessage(
                            fragmentView.context!!,
                            getError(response.body()!!).message.toString(),
                            "",
                            fragmentView.context!!.getString(R.string.ok)
                    )
                }
            } catch (e: Exception) {

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
