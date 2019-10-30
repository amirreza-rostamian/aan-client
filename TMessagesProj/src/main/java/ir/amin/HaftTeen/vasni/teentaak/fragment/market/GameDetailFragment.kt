package ir.amin.HaftTeen.vasni.teentaak.fragment.market

import android.Manifest
import android.content.Context
import android.graphics.Paint
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatSeekBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.widget.LinearLayout
import com.downloader.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.view_wallet_dialog.view.*
import me.himanshusoni.chatmessageview.Vasni.Permission.MPermission
import me.himanshusoni.chatmessageview.Vasni.Permission.PermissionCallback
import me.himanshusoni.chatmessageview.Vasni.Permission.PermissionItem
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.*
import me.himanshusoni.chatmessageview.ui.MoreView.MoreAdapter
import me.himanshusoni.chatmessageview.ui.MoreView.link.RegisterItem
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.messenger.NotificationCenter
import ir.amin.HaftTeen.messenger.browser.Browser
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.vasni.adapter.Vitrin.GameCommentAdapter
import ir.amin.HaftTeen.vasni.adapter.Vitrin.GameScreenShotAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.core.DataLoader
import ir.amin.HaftTeen.vasni.extention.*
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Game
import ir.amin.HaftTeen.R

class GameDetailFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate {

    private var txt: String = ""
    private val gameScreenShotAdapter = MoreAdapter()
    private val gameCommentAdapter = MoreAdapter()
    private var gameData = Game()
    private var downloadId1: Int = 0
    private var rate = 0
    private lateinit var tv_media_game_download_click: MTextView
    private lateinit var tv_media_game_download_percent: MTextView
    private lateinit var tv_media_game_name: MTextViewBold
    private lateinit var tv_media_game_programmer_name: MTextView
    private lateinit var tv_media_game_price: MTextView
    private lateinit var pb_download_app: AppCompatSeekBar
    private lateinit var pv_loading_pic_game: ProgressView
    private lateinit var imv_media_game: AppCompatImageView
    private lateinit var ll_media_game_volume: LinearLayout
    private lateinit var ll_media_game_category: LinearLayout
    private lateinit var ll_media_game_rating: LinearLayout
    private lateinit var ll_media_game_download: LinearLayout
    private lateinit var ll_media_game_view: LinearLayout
    private lateinit var tv_media_game_volume: MTextViewBold
    private lateinit var tv_media_game_category: MTextViewBold
    private lateinit var tv_media_game_rating: MTextViewBold
    private lateinit var tv_media_game_download: MTextViewBold
    private lateinit var tv_media_game_view: MTextViewBold
    private lateinit var rc_media_game_screenshot: RecyclerView
    private lateinit var tv_media_game_desc: JustifiedTextView
    private lateinit var tv_media_game_desc_more: MTextView
    private lateinit var ll_rating_media_game: LinearLayout
    private lateinit var rb_media_game: MRatingBar
    private lateinit var rc_media_game_comment: RecyclerView
    private lateinit var pv_game_loading: View
    private lateinit var web_view_game: WebView

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
        fragmentView = factory.inflate(R.layout.activity_game, null)
        fragmentView.setOnTouchListener { v, event -> true }

        tv_media_game_download_click = fragmentView.findViewById(R.id.tv_media_game_download_click)
        tv_media_game_download_percent = fragmentView.findViewById(R.id.tv_media_game_download_percent)
        tv_media_game_name = fragmentView.findViewById(R.id.tv_media_game_name)
        tv_media_game_programmer_name = fragmentView.findViewById(R.id.tv_media_game_programmer_name)
        tv_media_game_price = fragmentView.findViewById(R.id.tv_media_game_price)
        pb_download_app = fragmentView.findViewById(R.id.pb_download_app)
        pv_loading_pic_game = fragmentView.findViewById(R.id.pv_loading_pic_game)
        imv_media_game = fragmentView.findViewById(R.id.imv_media_game)
        ll_media_game_volume = fragmentView.findViewById(R.id.ll_media_game_volume)
        ll_media_game_category = fragmentView.findViewById(R.id.ll_media_game_category)
        ll_media_game_rating = fragmentView.findViewById(R.id.ll_media_game_rating)
        ll_media_game_download = fragmentView.findViewById(R.id.ll_media_game_download)
        ll_media_game_view = fragmentView.findViewById(R.id.ll_media_game_view)
        tv_media_game_volume = fragmentView.findViewById(R.id.tv_media_game_volume)
        tv_media_game_category = fragmentView.findViewById(R.id.tv_media_game_category)
        tv_media_game_rating = fragmentView.findViewById(R.id.tv_media_game_rating)
        tv_media_game_download = fragmentView.findViewById(R.id.tv_media_game_download)
        tv_media_game_view = fragmentView.findViewById(R.id.tv_media_game_view)
        rc_media_game_screenshot = fragmentView.findViewById(R.id.rc_media_game_screenshot)
        tv_media_game_desc = fragmentView.findViewById(R.id.tv_media_game_desc)
        tv_media_game_desc_more = fragmentView.findViewById(R.id.tv_media_game_desc_more)
        ll_rating_media_game = fragmentView.findViewById(R.id.ll_rating_media_game)
        rb_media_game = fragmentView.findViewById(R.id.rb_media_game)
        rc_media_game_comment = fragmentView.findViewById(R.id.rc_media_game_comment)
        pv_game_loading = fragmentView.findViewById(R.id.pv_game_loading)
        web_view_game = fragmentView.findViewById(R.id.web_view_game)

        gameData = DataLoader.instance.game
        getGameDetail()

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

    fun initView() {
        actionBar.setTitle(gameData.title)
        imv_media_game.loadImage(fragmentView.context, gameData.thumbnail!!, pv_loading_pic_game)
        tv_media_game_name.text = gameData.title
        //tv_media_game_programmer_name.text = gameData.namespace
        if (gameData.volume != null)
            tv_media_game_volume.text = getHumanReadableSize(fragmentView.context, gameData.volume!!.toLong())
        tv_media_game_category.text = gameData.category
        tv_media_game_rating.text = gameData.logs!!.rate.toString()
        tv_media_game_download.text = gameData.logs!!.download.toString()
        tv_media_game_view.text = gameData.logs!!.view.toString()

        if (gameData.price == 0)
            tv_media_game_price.text = fragmentView.context.getString(R.string.free)
        else
            tv_media_game_price.text = gameData.price.toString() + " " + fragmentView.context.getString(R.string.currency)

        tv_media_game_desc.text = gameData.description
        tv_media_game_desc.setAlignment(Paint.Align.RIGHT)
        tv_media_game_desc.setLineSpacing(20)
        tv_media_game_desc.setTextSize(1, 14.0f)

        val layoutManager = LinearLayoutManager(fragmentView.context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        layoutManager.reverseLayout = true
        rc_media_game_screenshot.adapter = gameScreenShotAdapter
        rc_media_game_screenshot.setLayoutManager(layoutManager)
        gameScreenShotAdapter.apply {
            register(RegisterItem(R.layout.row_screenshot, GameScreenShotAdapter::class.java))
            startAnimPosition(1)
        }
        gameScreenShotAdapter.loadData(gameData.screenShots!!)
        gameScreenShotAdapter.attachTo(rc_media_game_screenshot)


        val llComments = LinearLayoutManager(fragmentView.context)
        llComments.orientation = LinearLayoutManager.VERTICAL
        llComments.reverseLayout = true
        rc_media_game_comment.adapter = gameCommentAdapter
        rc_media_game_comment.setLayoutManager(llComments)
        gameCommentAdapter.apply {
            register(RegisterItem(R.layout.row_comment, GameCommentAdapter::class.java))
            startAnimPosition(1)
        }
        gameCommentAdapter.loadData(gameData.comments!!)
        gameCommentAdapter.attachTo(rc_media_game_comment)


        rb_media_game.rating = gameData.logs!!.rate!!.toFloat()
        ll_rating_media_game.setOnClickListener {
            if (gameData.format == VasniSchema.instance.html_game_format) {
                ratingDialog(fragmentView.context, gameData.id.toString(), VasniSchema.instance.gameActivity)
            } else {
                if (VasniSchema.instance.appInstalledOrNot(fragmentView.context, gameData.namespace!!)) {
                    ratingDialog(fragmentView.context, gameData.id.toString(), VasniSchema.instance.gameActivity)
                } else {
                    VasniSchema.instance.showMessage(
                            fragmentView.context,
                            fragmentView.context.getString(R.string.dont_submit_comment),
                            "",
                            fragmentView.context.getString(R.string.ok)
                    )
                }
            }

        }

        if (gameData.format == VasniSchema.instance.html_game_format) {
            VasniSchema.instance.show(false, ll_media_game_volume)
            VasniSchema.instance.show(false, ll_media_game_download)
            VasniSchema.instance.show(true, ll_media_game_view)
        } else {
            VasniSchema.instance.show(true, ll_media_game_volume)
            VasniSchema.instance.show(true, ll_media_game_download)
            VasniSchema.instance.show(false, ll_media_game_view)
        }

        if (gameData.wallet!!.bought == false && gameData.price != 0) {
            tv_media_game_download_click.text = fragmentView.context.getString(R.string.buy)
        } else {
            if (gameData.format == VasniSchema.instance.html_game_format) {
                tv_media_game_download_click.setText(fragmentView.context.getString(R.string.run))
            } else {
                if (VasniSchema.instance.appInstalledOrNot(fragmentView.context, gameData.namespace!!)) {
                    tv_media_game_download_click.setText(fragmentView.context.getString(R.string.run))
                } else if (VasniSchema.instance.getVitrinAppFolderPath(gameData.title!!).exists()) {
                    tv_media_game_download_click.setText(fragmentView.context.getString(R.string.install))
                } else {
                    tv_media_game_download_click.setText(fragmentView.context.getString(R.string.download))
                }
            }

        }

        tv_media_game_download_click.setOnClickListener {
            if (gameData.format == VasniSchema.instance.html_game_format) {
                generateLink()
            } else {
                if (tv_media_game_download_click.text.equals(fragmentView.context.getString(R.string.download))) {
                    //handle download click
                    tv_media_game_download_click.setText(fragmentView.context.getString(R.string.stop))
                    VasniSchema.instance.show(true, pb_download_app)
                    VasniSchema.instance.show(true, tv_media_game_download_percent)
                    checkPermission()
                } else if (tv_media_game_download_click.text.equals(fragmentView.context.getString(R.string.stop))) {
                    // handle stop click
                    tv_media_game_download_click.setText(fragmentView.context.getString(R.string.resume))
                    PRDownloader.pause(downloadId1)
                } else if (tv_media_game_download_click.text.equals(fragmentView.context.getString(R.string.resume))) {
                    // continue download
                    tv_media_game_download_click.setText(fragmentView.context.getString(R.string.stop))
                    PRDownloader.resume(downloadId1)

                } else if (tv_media_game_download_click.text.equals(fragmentView.context.getString(R.string.install))) {
                    // handle install click
                    VasniSchema.instance.show(false, pb_download_app)
                    VasniSchema.instance.show(false, tv_media_game_download_percent)
                    if (VasniSchema.instance.getVitrinAppFolderPath(gameData.title!!).exists()) {
                        try {
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                try {
                                    DataLoader.instance.installApk(VasniSchema.instance.getVitrinAppFolderPath(gameData.title!!).toString())
                                } catch (e: Exception) {

                                }
                            } else {
                                try {

                                    DataLoader.instance.instalApk(
                                            VasniSchema.instance.getVitrinAppFolderPath(gameData.title!!).toString(),
                                            fragmentView.context
                                    )

                                } catch (e: Exception) {

                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                } else if (tv_media_game_download_click.text.equals(fragmentView.context.getString(R.string.run))) {
                    // run app
                    VasniSchema.instance.runApp(fragmentView.context, gameData.namespace!!)
                } else if (tv_media_game_download_click.text.equals(fragmentView.context.getString(R.string.buy))) {
                    getWalletBalance()
//                    if (gameData.wallet!!.balance == "0") {
//                        activeWallet()
//                    } else {
//                        buy()
//                    }
                }
            }

        }

        if (VasniSchema.instance.buy_is_visible == "0") {
            VasniSchema.instance.show(false, tv_media_game_download_click)
        } else {
            VasniSchema.instance.show(true, tv_media_game_download_click)
        }
    }

    private fun download() {
        try {
            DataLoader.instance.certificate()
            downloadId1 = PRDownloader.download(
                    gameData.link,
                    VasniSchema.instance.getVitrinAppFolderPath().toString(),
                    gameData.title + ".apk")
                    .build()
                    .setOnStartOrResumeListener(object : OnStartOrResumeListener {
                        override fun onStartOrResume() {
                            try {
                                pb_download_app.setIndeterminate(false)
                            } catch (e: Exception) {

                            }
                        }
                    })
                    .setOnPauseListener(object : OnPauseListener {
                        override fun onPause() {

                        }
                    })
                    .setOnCancelListener(object : OnCancelListener {
                        override fun onCancel() {

                        }
                    })
                    .setOnProgressListener(object : OnProgressListener {
                        override fun onProgress(progress: Progress?) {
                            try {
                                var progressPercent: Long = progress!!.currentBytes * 100 / progress!!.totalBytes;
                                pb_download_app.setProgress(progressPercent.toInt())
                                tv_media_game_download_percent.setText("%" + progressPercent.toString())
                            } catch (e: Exception) {

                            }
                        }
                    })
                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                            try {
                                downloadCount()
                                tv_media_game_download_click.setText(fragmentView.context.getString(R.string.install))
                                VasniSchema.instance.show(false, pb_download_app)
                                VasniSchema.instance.show(false, tv_media_game_download_percent)
                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    try {
                                        DataLoader.instance.installApk(VasniSchema.instance.getVitrinAppFolderPath(gameData.title!!).toString())
                                    } catch (e: Exception) {

                                    }
                                } else {
                                    try {
                                        DataLoader.instance.instalApk(
                                                VasniSchema.instance.getVitrinAppFolderPath(gameData.title!!).toString(),
                                                fragmentView.context
                                        )
                                    } catch (e: Exception) {

                                    }
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        override fun onError(error: Error?) {
                        }
                    })
        } catch (e: Exception) {

        }
    }

    fun checkPermission() {
        val permissionItems = ArrayList<PermissionItem>()
        permissionItems.add(
                PermissionItem(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        fragmentView.context.getString(R.string.permission_storage),
                        R.drawable.permission_ic_storage
                )
        )
        MPermission.create(fragmentView.context)
                .title(fragmentView.context.getString(R.string.select_file_permission))
                .permissions(permissionItems)
                .msg(fragmentView.context.getString(R.string.login_permission_msg))
                .animStyle(R.style.PermissionAnimFade)
                .checkMutiPermission(object : PermissionCallback {
                    override fun onClose() {
                    }

                    override fun onFinish() {
                        download()
                    }

                    override fun onDeny(permission: String, position: Int) {
                    }

                    override fun onGuarantee(permission: String, position: Int) {
                    }
                })
    }

    override fun onResume() {
        super.onResume()
        if (gameData.wallet!!.bought == false && gameData.price != 0) {
            tv_media_game_download_click.text = fragmentView.context.getString(R.string.buy)
        } else {
            if (gameData.format == VasniSchema.instance.html_game_format) {
                tv_media_game_download_click.setText(fragmentView.context.getString(R.string.run))
            } else {
                if (gameData.namespace != null) {
                    if (VasniSchema.instance.appInstalledOrNot(fragmentView.context, gameData.namespace!!)) {
                        tv_media_game_download_click.setText(fragmentView.context.getString(R.string.run))
                    } else if (VasniSchema.instance.getVitrinAppFolderPath(gameData.title!!).exists()) {
                        tv_media_game_download_click.setText(fragmentView.context.getString(R.string.install))
                    } else {
                        tv_media_game_download_click.setText(fragmentView.context.getString(R.string.download))
                    }
                }

            }
        }
    }

    override fun finishFragment() {
        super.finishFragment()
        try {
            PRDownloader.pause(downloadId1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getGameDetail() {
        VasniSchema.instance.show(true, pv_game_loading)
        ApiService.apiInterface.getVitrinGameDetail(gameData.id!!)
                .enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful && response.body() != null) {
                            try {
                                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                                    VasniSchema.instance.show(false, pv_game_loading)
                                    val game = Gson().fromJson(
                                            getData(response.body()!!).get("items").asJsonObject,
                                            Game()::class.java
                                    )
                                    gameData = game
                                    initView()
                                } else {
                                    VasniSchema.instance.showMessage(
                                            fragmentView.context!!,
                                            getError(response.body()!!).message.toString(),
                                            "",
                                            fragmentView.context.getString(R.string.ok)
                                    )
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        VasniSchema.instance.showMessage(
                                fragmentView.context!!,
                                fragmentView.context.getString(R.string.server_error),
                                "",
                                fragmentView.context.getString(R.string.ok)
                        )
                    }
                })
    }

    fun downloadCount() {
        val id = RequestBody.create(okhttp3.MultipartBody.FORM, gameData.id)
        val value = RequestBody.create(okhttp3.MultipartBody.FORM, "1")
        ApiService.apiInterface.downloadVitrinGame(id, value)
                .enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful && response.body() != null) {
                            if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {

                            } else {
                                VasniSchema.instance.showMessage(
                                        fragmentView.context,
                                        getError(response.body()!!).message.toString(),
                                        "",
                                        fragmentView.context.getString(R.string.ok)
                                )
                            }
                        }
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        VasniSchema.instance.showMessage(
                                fragmentView.context,
                                fragmentView.context.getString(R.string.server_error),
                                "",
                                fragmentView.context.getString(R.string.ok)
                        )
                    }
                })
    }

    fun buy() {
        BottomDialog.Builder(fragmentView.context!!)
                .setContent(fragmentView.context.getString(R.string.wallet_message_dialog))
                .setNegativeText(fragmentView.context.getString(R.string.yes))
                .setNegativeTextColor(ContextCompat.getColor(fragmentView.context!!, R.color.colorAccent))
                .setPositiveText(fragmentView.context.getString(R.string.skip))
                .autoDismiss(false)
                .setCancelable(false)
                .onNegative(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        if (gameData.price <= gameData.wallet!!.balance!!.toInt()) {
                            VasniSchema.instance.show(true, pv_game_loading)
                            getWalletReduce()
                        } else {
                            VasniSchema.instance.showMessage(
                                    fragmentView.context!!,
                                    fragmentView.context.getString(R.string.wallet_no_balance),
                                    "",
                                    fragmentView.context.getString(R.string.ok)
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

    fun getWalletReduce() {
        ApiService.apiInterface.getWalletReduce(gameData.id!!, VasniSchema.instance.game_type!!
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    VasniSchema.instance.show(false, pv_game_loading)
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
//                        recreate()
                        getGameDetail()
                        tv_media_game_download_click.setText(fragmentView.context.getString(R.string.download))
                        tv_media_game_download_click.setText(fragmentView.context.getString(R.string.stop))
                        VasniSchema.instance.show(true, pb_download_app)
                        VasniSchema.instance.show(true, tv_media_game_download_percent)
                        checkPermission()
                    } else {
                        VasniSchema.instance.showMessage(
                                fragmentView.context,
                                getError(response.body()!!).message.toString(),
                                "",
                                fragmentView.context.getString(R.string.ok)
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.showMessage(
                        fragmentView.context,
                        fragmentView.context.getString(R.string.server_error),
                        "",
                        fragmentView.context.getString(R.string.ok)
                )
            }
        })
    }

    fun activeWallet() {
        BottomDialog.Builder(fragmentView.context)
                .setContent(fragmentView.context.getString(R.string.wallet_message_dialog_active))
                .setNegativeText(fragmentView.context.getString(R.string.ok))
                .setNegativeTextColor(ContextCompat.getColor(fragmentView.context, R.color.colorAccent))
                .setPositiveText(fragmentView.context.getString(R.string.skip))
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
        val inflater = fragmentView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customView = inflater.inflate(R.layout.view_wallet_dialog, null)

        var dialog: CenterDialog = CenterDialog.Builder(fragmentView.context)
                .setCustomView(customView)
                .autoDismiss(false)
                .setCancelable(true)
                .show()

        customView.tv_dialog_wallet_detail.setText(fragmentView.context.getString(R.string.wallet_message_dialog_active_desc), true)
        customView.btn_submit_dialog_wallet.setOnClickListener {
            if (customView.et_dialog_wallet_active_code.text.trim().isEmpty()) {
                VasniSchema.instance.showMessage(
                        fragmentView.context,
                        fragmentView.context.getString(R.string.wallet_is_empty),
                        "",
                        fragmentView.context.getString(R.string.ok)
                )
            } else {
                dialog.dismiss()
                VasniSchema.instance.show(true, pv_game_loading)
                ApiService.apiInterface.checkWalletVoucher(
                        customView.et_dialog_wallet_active_code.text.trim().toString()
                ).enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful && response.body() != null) {
                            VasniSchema.instance.show(false, pv_game_loading)
                            if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                                tv_media_game_download_click.setText(fragmentView.context.getString(R.string.buy))
                                VasniSchema.instance.showMessage(
                                        fragmentView.context,
                                        getData(response.body()!!).get("message").asString,
                                        "",
                                        fragmentView.context.getString(R.string.ok)
                                )
//                                recreate()
                                getGameDetail()
                            } else {
                                VasniSchema.instance.showMessage(
                                        fragmentView.context,
                                        getError(response.body()!!).message.toString(),
                                        "",
                                        fragmentView.context.getString(R.string.ok)
                                )
                            }
                        }
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        VasniSchema.instance.showMessage(
                                fragmentView.context,
                                fragmentView.context.getString(R.string.server_error),
                                "",
                                fragmentView.context.getString(R.string.ok)
                        )
                    }
                })
            }
        }

    }

    fun generateLink() {
        VasniSchema.instance.show(true, pv_game_loading)
        ApiService.apiInterface.HtmlGameGenerate(gameData.id!!).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            VasniSchema.instance.show(false, pv_game_loading)
                            var token = getData(response.body()!!).get("token").asString
                            var link = getData(response.body()!!).get("link").asString
//                            VasniSchema.instance.openUrlInChrome(
//                                    this@GameDetailActivity,
//                                    VasniSchema.instance.decrypt(link, DataLoader.instance.getKey()) + "?token=" +
//                                            VasniSchema.instance.decrypt(token, DataLoader.instance.getKey()) + "&league=" + VasniSchema.instance.leagueId
//
//                            )


                            Browser.openUrl(fragmentView.context, VasniSchema.instance.decrypt(link, DataLoader.instance.getKey()) + "?token=" +
                                    VasniSchema.instance.decrypt(token, DataLoader.instance.getKey()) + "&league=" + VasniSchema.instance.leagueId
                            )

//                            VasniSchema.instance.certificate()
//                            VasniSchema.instance.show(true, web_view_game)
//                            web_view_game.settings.javaScriptEnabled = true
//                            web_view_game.settings.domStorageEnabled = true
//                            web_view_game.loadUrl("https://hextris.github.io/hextris/")
//                            web_view_game.loadUrl(VasniSchema.instance.decrypt(link, DataLoader.instance.getKey()) + "?token=" +
//                                    VasniSchema.instance.decrypt(token, DataLoader.instance.getKey()) + "&league=" + VasniSchema.instance.leagueId)


                        } else {
                            VasniSchema.instance.showMessage(
                                    fragmentView.context,
                                    getError(response.body()!!).message.toString(),
                                    "",
                                    fragmentView.context.getString(R.string.ok)
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.showMessage(
                        fragmentView.context,
                        fragmentView.context.getString(R.string.server_error),
                        "",
                        fragmentView.context.getString(R.string.ok)
                )
            }
        })
    }

    fun getWalletBalance() {
        VasniSchema.instance.show(true, pv_game_loading)
        ApiService.apiInterface.getWalletBalance().enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    VasniSchema.instance.show(false, pv_game_loading)
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        gameData.wallet!!.balance = getData(response.body()!!).get("balance").asString
                        if (gameData.wallet!!.balance == "0") {
                            activeWallet()
                        } else {
                            buy()
                        }
                    } else {
                        VasniSchema.instance.showMessage(
                                fragmentView.context,
                                getError(response.body()!!).message.toString(),
                                "",
                                fragmentView.context.getString(R.string.ok)
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.showMessage(
                        fragmentView.context,
                        fragmentView.context.getString(R.string.server_error),
                        "",
                        fragmentView.context.getString(R.string.ok)
                )
            }
        })
    }


}
