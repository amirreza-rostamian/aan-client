package ir.amin.HaftTeen.vasni.teentaak

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.frg_link.*
import me.himanshusoni.chatmessageview.Vasni.AdvancedWebView
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.util.AppSchema
import retrofit2.Callback
import ir.amin.HaftTeen.messenger.NotificationCenter
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.model.teentaak.EventHandler
import ir.amin.HaftTeen.R


class LinkFragment : BaseFragment,NotificationCenter.NotificationCenterDelegate, AdvancedWebView.Listener {
    private var txt: String = ""
    private var eventHandler = EventHandler()
    private lateinit var webview: WebView
    private lateinit var pv_link: View

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPageFinished(url: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDownloadRequested(url: String?, suggestedFilename: String?, mimeType: String?, contentLength: Long, contentDisposition: String?, userAgent: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onExternalPageRequest(url: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


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
        fragmentView = factory.inflate(R.layout.frg_link, null)
        fragmentView.setOnTouchListener { v, event -> true }
        webview = fragmentView.findViewById(R.id.webview)
        pv_link = fragmentView.findViewById(R.id.pv_link)
        if (eventHandler.link == "") {
            webview.loadUrl("https://www.google.com/")
        } else {
            webview.loadUrl(eventHandler.link)
        }
        webview.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                if (pv_link != null)
                    VasniSchema.instance.show(false, pv_link)
            }
        })
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

    override fun onActivityResultFragment(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResultFragment(requestCode, resultCode, data)

    }



}