package ir.amin.HaftTeen.vasni.teentaak.fragment.market

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.widget.*
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.adpdigital.push.AdpPushClient.packageName
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.messenger.NotificationCenter
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.vasni.adapter.Vitrin.GameCommentAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.core.DataLoader
import ir.amin.HaftTeen.vasni.extention.*
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Book
import ir.amin.HaftTeen.R
import java.io.File

class BookDetailFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate {

    private var txt: String = ""
    private val gameScreenShotAdapter = MoreAdapter()
    private val commentAdapter = MoreAdapter()
    private var bookData = Book()
    private var downloadId1: Int = 0
    private var rate = 0
    private lateinit var imv_banner_book: AppCompatImageView
    private lateinit var tv_media_book_download_click: MTextView
    private lateinit var tv_media_book_download_percent: MTextView
    private lateinit var pb_download_book: AppCompatSeekBar
    private lateinit var tv_price_book: MTextView
    private lateinit var ll_media_book_point: LinearLayout
    private lateinit var rb_rate_book: MRatingBar
    private lateinit var tv_point_book: MTextView
    private lateinit var tv_publisher_book: MTextView
    private lateinit var pv_loading_pic_book: ProgressView
    private lateinit var imv_thumbnail_book: AppCompatImageView
    private lateinit var tv_media_book_volume: MTextViewBold
    private lateinit var tv_media_book_category: MTextViewBold
    private lateinit var tv_media_book_rating: MTextViewBold
    private lateinit var tv_media_book_view: MTextViewBold
    private lateinit var tv_summary_book: JustifiedTextView
    private lateinit var cv_gift_book: CardView
    private lateinit var pv_book_detail_loading: View
    private lateinit var rc_media_book_comment: RecyclerView

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
        fragmentView = factory.inflate(R.layout.activity_book, null)
        fragmentView.setOnTouchListener { v, event -> true }

        imv_banner_book = fragmentView.findViewById(R.id.imv_banner_book)
        tv_media_book_download_click = fragmentView.findViewById(R.id.tv_media_book_download_click)
        tv_media_book_download_percent = fragmentView.findViewById(R.id.tv_media_book_download_percent)
        pb_download_book = fragmentView.findViewById(R.id.pb_download_book)
        tv_price_book = fragmentView.findViewById(R.id.tv_price_book)
        ll_media_book_point = fragmentView.findViewById(R.id.ll_media_book_point)
        rb_rate_book = fragmentView.findViewById(R.id.rb_rate_book)
        tv_point_book = fragmentView.findViewById(R.id.tv_point_book)
        tv_publisher_book = fragmentView.findViewById(R.id.tv_publisher_book)
        pv_loading_pic_book = fragmentView.findViewById(R.id.pv_loading_pic_book)
        imv_thumbnail_book = fragmentView.findViewById(R.id.imv_thumbnail_book)
        tv_media_book_volume = fragmentView.findViewById(R.id.tv_media_book_volume)
        tv_media_book_category = fragmentView.findViewById(R.id.tv_media_book_category)
        tv_media_book_rating = fragmentView.findViewById(R.id.tv_media_book_rating)
        tv_media_book_view = fragmentView.findViewById(R.id.tv_media_book_view)
        tv_summary_book = fragmentView.findViewById(R.id.tv_summary_book)
        cv_gift_book = fragmentView.findViewById(R.id.cv_gift_book)
        pv_book_detail_loading = fragmentView.findViewById(R.id.pv_book_detail_loading)
        rc_media_book_comment = fragmentView.findViewById(R.id.rc_media_book_comment)

        bookData = DataLoader.instance.book
        getBookDetail()

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

        actionBar.setTitle(bookData.title)
        cv_gift_book.setOnClickListener {
            shareLink(fragmentView.context, bookData.link!!)
        }

        if (bookData.wallet!!.bought == true || bookData.price == 0) {
            VasniSchema.instance.show(true, cv_gift_book)
        } else {
            VasniSchema.instance.show(false, cv_gift_book)
        }

        imv_banner_book.loadImage(fragmentView.context, bookData.banner!!)
        imv_thumbnail_book.loadImage(fragmentView.context, bookData.thumbnail!!)
        rb_rate_book.rating = bookData.logs!!.rate!!.toFloat()
        tv_point_book.text = bookData.logs!!.rate!!.toString()
        tv_publisher_book.text = fragmentView.context.getString(R.string.vitrin_book_author) + " " + bookData.author
        if (bookData.price == 0)
            tv_price_book.text = fragmentView.context.getString(R.string.free)
        else
            tv_price_book.text = bookData.price.toString() + " " + fragmentView.context.getString(R.string.currency)

        tv_summary_book.text = bookData.summary
        tv_summary_book.setAlignment(Paint.Align.RIGHT)
        tv_summary_book.setLineSpacing(20)
        tv_summary_book.setTextSize(1, 14.0f)

        if (bookData.volume != null)
            tv_media_book_volume.text = getHumanReadableSize(fragmentView.context, bookData.volume!!.toLong()) + ""
        tv_media_book_category.text = bookData.category
        tv_media_book_rating.text = bookData.logs!!.rate.toString()
        tv_media_book_view.text = bookData.logs!!.view.toString()

        ll_media_book_point.setOnClickListener {
            ratingDialog(fragmentView.context, bookData.id.toString(), VasniSchema.instance.bookActivity)
        }

        if (bookData.wallet!!.bought == false && bookData.price != 0) {
            tv_media_book_download_click.setText(fragmentView.context.getString(R.string.buy))
        } else {
            if (VasniSchema.instance.getVitrinBookFolderPath(bookData.title!!).exists()) {
                tv_media_book_download_click.setText(fragmentView.context.getString(R.string.show_book))
            } else {
                tv_media_book_download_click.setText(fragmentView.context.getString(R.string.get_book))
            }
        }

        tv_media_book_download_click.setOnClickListener {
            if (tv_media_book_download_click.text.equals(fragmentView.context.getString(R.string.get_book))) {
                //handle download click
                tv_media_book_download_click.setText(fragmentView.context.getString(R.string.stop))
                VasniSchema.instance.show(true, pb_download_book)
                VasniSchema.instance.show(true, tv_media_book_download_percent)
                checkPermission()
            } else if (tv_media_book_download_click.text.equals(fragmentView.context.getString(R.string.stop))) {
                // handle stop click
                tv_media_book_download_click.setText(fragmentView.context.getString(R.string.resume))
                PRDownloader.pause(downloadId1)
            } else if (tv_media_book_download_click.text.equals(fragmentView.context.getString(R.string.resume))) {
                // continue download
                tv_media_book_download_click.setText(fragmentView.context.getString(R.string.stop))
                PRDownloader.resume(downloadId1)

            } else if (tv_media_book_download_click.text.equals(fragmentView.context.getString(R.string.show_book))) {
                // handle install click
                pb_download_book.visibility = View.INVISIBLE
                tv_media_book_download_percent.visibility = View.INVISIBLE
                if (VasniSchema.instance.getVitrinBookFolderPath(bookData.title!!).exists()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        val file = File(VasniSchema.instance.getVitrinBookFolderPath(bookData.title!!).toString())
                        val uri = FileProvider.getUriForFile(fragmentView.context, "$packageName.provider", file)
                        var intent = Intent(Intent.ACTION_VIEW)
                        intent.data = uri
                        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        fragmentView.context.startActivity(intent)
                    } else {
                        var intent = Intent(Intent.ACTION_VIEW)
                        val file = File(VasniSchema.instance.getVitrinBookFolderPath(bookData.title!!).toString())
                        intent.setDataAndType(
                                Uri.fromFile(file),
                                "application/pdf"
                        )
                        intent = Intent.createChooser(intent, "Open File")
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        fragmentView.context.startActivity(intent)
                    }
                }

            } else if (tv_media_book_download_click.text.equals(fragmentView.context.getString(R.string.buy))) {
                getWalletBalance()
//                if (bookData.wallet!!.balance == "0") {
//                    activeWallet()
//                } else {
//                    buy()
//                }
            }
        }

        if (VasniSchema.instance.buy_is_visible == "0") {
            VasniSchema.instance.show(false, tv_media_book_download_click)
        } else {
            VasniSchema.instance.show(true, tv_media_book_download_click)
        }

        val llComments = LinearLayoutManager(fragmentView.context)
        llComments.orientation = LinearLayoutManager.VERTICAL
        llComments.reverseLayout = true
        rc_media_book_comment.adapter = commentAdapter
        rc_media_book_comment.setLayoutManager(llComments)
        commentAdapter.apply {
            register(RegisterItem(R.layout.row_comment, GameCommentAdapter::class.java))
            startAnimPosition(1)
        }
        commentAdapter.loadData(bookData.comments!!)
        commentAdapter.attachTo(rc_media_book_comment)
    }

    private fun download() {
        try {
            DataLoader.instance.certificate()
            downloadId1 = PRDownloader.download(
                    bookData.link,
                    VasniSchema.instance.getVitrinBookFolderPath().toString(),
                    bookData.title + ".pdf"
            )
                    .build()
                    .setOnStartOrResumeListener(object : OnStartOrResumeListener {
                        override fun onStartOrResume() {
                            try {
                                pb_download_book.setIndeterminate(false)
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
                                pb_download_book.setProgress(progressPercent.toInt())
                                tv_media_book_download_percent.setText("%" + progressPercent.toString())
                            } catch (e: Exception) {

                            }

                        }
                    })
                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                            try {
                                tv_media_book_download_click.setText(fragmentView.context.getString(R.string.show_book))
                                pb_download_book.visibility = View.INVISIBLE
                                tv_media_book_download_percent.visibility = View.INVISIBLE
//                            AppSchema.instance.show(false, pb_download_book)
//                            AppSchema.instance.show(false, tv_media_book_download_percent)


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
        if (VasniSchema.instance.getVitrinBookFolderPath(bookData.title!!).exists()) {
            tv_media_book_download_click.setText(fragmentView.context.getString(R.string.show_book))
        } else {
            tv_media_book_download_click.setText(fragmentView.context.getString(R.string.get_book))
        }
    }

    override fun finishFragment() {
        super.finishFragment()
        try {
            PRDownloader.pause(downloadId1)
        } catch (e: Exception) {

        }
    }

    fun getBookDetail() {
        VasniSchema.instance.show(true, pv_book_detail_loading)
        ApiService.apiInterface.getVitrinBookDetail(bookData.id!!)
                .enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful && response.body() != null) {
                            try {
                                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                                    VasniSchema.instance.show(false, pv_book_detail_loading)
                                    val book = Gson().fromJson(
                                            getData(response.body()!!).get("items").asJsonObject,
                                            Book()::class.java
                                    )
                                    bookData = book
                                    initView()
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

    fun buy() {
        BottomDialog.Builder(fragmentView.context)
                .setContent(fragmentView.context.getString(R.string.wallet_message_dialog))
                .setNegativeText(fragmentView.context.getString(R.string.yes))
                .setNegativeTextColor(ContextCompat.getColor(fragmentView.context, R.color.colorAccent))
                .setPositiveText(fragmentView.context.getString(R.string.no))
                .autoDismiss(false)
                .setCancelable(false)
                .onNegative(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        if (bookData.price!! <= bookData.wallet!!.balance!!.toInt()) {
                            VasniSchema.instance.show(true, pv_book_detail_loading)
                            getWalletReduce()
                        } else {
                            VasniSchema.instance.showMessage(
                                    fragmentView.context,
                                    fragmentView.context.getString(R.string.wallet_no_balance),
                                    "",
                                    fragmentView.context.getString(R.string.ok))
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
        ApiService.apiInterface.getWalletReduce(bookData.id!!, VasniSchema.instance.book_type!!).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    VasniSchema.instance.show(false, pv_book_detail_loading)
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
//                        recreate()
                        getBookDetail()
                        tv_media_book_download_click.text = fragmentView.context.getString(R.string.get_book)
                        tv_media_book_download_click.setText(fragmentView.context.getString(R.string.stop))
                        VasniSchema.instance.show(true, pb_download_book)
                        VasniSchema.instance.show(true, tv_media_book_download_percent)
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
                VasniSchema.instance.show(true, pv_book_detail_loading)
                ApiService.apiInterface.checkWalletVoucher(
                        customView.et_dialog_wallet_active_code.text.trim().toString()
                ).enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful && response.body() != null) {
                            VasniSchema.instance.show(false, pv_book_detail_loading)
                            if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                                tv_media_book_download_click.text = fragmentView.context.getString(R.string.buy)
                                VasniSchema.instance.showMessage(
                                        fragmentView.context,
                                        getData(response.body()!!).get("message").asString,
                                        "",
                                        fragmentView.context.getString(R.string.ok)
                                )
//                                recreate()
                                getBookDetail()
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

    fun getWalletBalance() {
        VasniSchema.instance.show(true, pv_book_detail_loading)
        ApiService.apiInterface.getWalletBalance().enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    VasniSchema.instance.show(false, pv_book_detail_loading)
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        bookData.wallet!!.balance = getData(response.body()!!).get("balance").asString
                        if (bookData.wallet!!.balance == "0") {
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
