package ir.amin.HaftTeen.vasni.teentaak.fragment.market

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.view_wallet_dialog.view.*
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
import ir.amin.HaftTeen.vasni.adapter.Vitrin.*
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.extention.*
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.*
import ir.amin.HaftTeen.R

class PackageFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate, Callback<JsonObject> {

    private var txt: String = ""
    private var packageId: String = ""
    private val adapter = MoreAdapter()
    private val commentAdapter = MoreAdapter()
    private val videoAdapter = MoreAdapter()
    private val bookAdapter = MoreAdapter()
    private val musicAdapter = MoreAdapter()
    private var pkgData = ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Package()
    private lateinit var refresh_package: SwipeRefreshLayout
    private lateinit var tv_buy_package: MTextView
    private lateinit var tv_package_desc: JTextView
    private lateinit var pv_loading_pic_package: ProgressView
    private lateinit var imv_banner_package: AppCompatImageView
    private lateinit var cv_package_game: CardView
    private lateinit var rc_package_game: RecyclerView
    private lateinit var cv_package_video: CardView
    private lateinit var rc_package_video: RecyclerView
    private lateinit var cv_package_book: CardView
    private lateinit var rc_package_book: RecyclerView
    private lateinit var cv_package_music: CardView
    private lateinit var rc_package_music: RecyclerView
    private lateinit var pv_package_loading: View
    private lateinit var tv_media_package_price: MTextView
    private lateinit var tv_media_package_category: MTextViewBold
    private lateinit var tv_media_package_rating: MTextViewBold
    private lateinit var tv_media_package_view: MTextViewBold
    private lateinit var ll_rating_media_package: LinearLayout
    private lateinit var rb_media_package: MRatingBar
    private lateinit var rc_media_package_comment: RecyclerView

    constructor(txt: String, id: String) {
        this.txt = txt
        this.packageId = id
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
        fragmentView = factory.inflate(R.layout.frg_package, null)
        fragmentView.setOnTouchListener { v, event -> true }

        refresh_package = fragmentView.findViewById(R.id.refresh_package)
        tv_buy_package = fragmentView.findViewById(R.id.tv_buy_package)
        tv_package_desc = fragmentView.findViewById(R.id.tv_package_desc)
        pv_loading_pic_package = fragmentView.findViewById(R.id.pv_loading_pic_package)
        imv_banner_package = fragmentView.findViewById(R.id.imv_banner_package)
        cv_package_game = fragmentView.findViewById(R.id.cv_package_game)
        rc_package_game = fragmentView.findViewById(R.id.rc_package_game)
        cv_package_video = fragmentView.findViewById(R.id.cv_package_video)
        rc_package_video = fragmentView.findViewById(R.id.rc_package_video)
        cv_package_book = fragmentView.findViewById(R.id.cv_package_book)
        rc_package_book = fragmentView.findViewById(R.id.rc_package_book)
        cv_package_music = fragmentView.findViewById(R.id.cv_package_music)
        rc_package_music = fragmentView.findViewById(R.id.rc_package_music)
        pv_package_loading = fragmentView.findViewById(R.id.pv_package_loading)
        tv_media_package_price = fragmentView.findViewById(R.id.tv_media_package_price)
        tv_media_package_category = fragmentView.findViewById(R.id.tv_media_package_category)
        tv_media_package_rating = fragmentView.findViewById(R.id.tv_media_package_rating)
        tv_media_package_view = fragmentView.findViewById(R.id.tv_media_package_view)
        ll_rating_media_package = fragmentView.findViewById(R.id.ll_rating_media_package)
        rb_media_package = fragmentView.findViewById(R.id.rb_media_package)
        rc_media_package_comment = fragmentView.findViewById(R.id.rc_media_package_comment)

        refresh_package.setOnRefreshListener {
            refresh_package.isRefreshing = true
            initView()
        }

        initView()

        tv_buy_package.setOnClickListener {
            getWalletBalance()
        }

        ll_rating_media_package.setOnClickListener {
            ratingDialog(fragmentView.context, pkgData.id.toString(), VasniSchema.instance.packageActivity)
        }

        return fragmentView
    }


    fun initView() {
        adapter.removeAllData()
        videoAdapter.removeAllData()
        bookAdapter.removeAllData()
        musicAdapter.removeAllData()
        VasniSchema.instance.show(true, pv_package_loading)
        ApiService.apiInterface.getVitrinPackageDetail(packageId!!).enqueue(this)
    }

    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
        if (response.isSuccessful && response.body() != null) {
            try {
                VasniSchema.instance.show(false, pv_package_loading)
                refresh_package.isRefreshing = false
                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {

                    pkgData = Gson().fromJson(getData(response.body()!!).get("items"), Package::class.java)
                    actionBar.setTitle(pkgData.title)
                    imv_banner_package.loadImage(fragmentView.context, pkgData.banner!!, pv_loading_pic_package)

                    tv_package_desc.setText(pkgData.description!!, true)

                    val gamesList: List<Game> = pkgData.game
                    rc_package_game.layoutManager = RtlGrid(fragmentView.context, 1, LinearLayoutManager.HORIZONTAL, false)
                    adapter.apply {
                        register(RegisterItem(R.layout.row_media_game_more, GameMoreAdapter::class.java))
                        startAnimPosition(1)
                    }
                    adapter.loadData(gamesList)
                    adapter.attachTo(rc_package_game)

                    val videoList: List<Video> = pkgData.video
                    rc_package_video.layoutManager = RtlGrid(fragmentView.context, 1, LinearLayoutManager.HORIZONTAL, false)
                    videoAdapter.apply {
                        register(RegisterItem(R.layout.row_media_video_more, VideoMoreAdapter::class.java))
                        startAnimPosition(1)
                    }
                    videoAdapter.loadData(videoList)
                    videoAdapter.attachTo(rc_package_video)


                    val booksList: List<Book> = pkgData.book
                    rc_package_book.layoutManager = RtlGrid(fragmentView.context, 1, LinearLayoutManager.HORIZONTAL, false)
                    bookAdapter.apply {
                        register(RegisterItem(R.layout.row_media_book_more, BookMoreAdapter::class.java))
                        startAnimPosition(1)
                    }
                    bookAdapter.loadData(booksList)
                    bookAdapter.attachTo(rc_package_book)


                    val musicList: List<Music> = pkgData.music
                    rc_package_music.layoutManager = RtlGrid(fragmentView.context, 1, LinearLayoutManager.HORIZONTAL, false)
                    musicAdapter.apply {
                        register(RegisterItem(R.layout.row_media_music_more, MusicMoreAdapter::class.java))
                        startAnimPosition(1)
                    }
                    musicAdapter.loadData(musicList)
                    musicAdapter.attachTo(rc_package_music)

                    if (gamesList.size == 0) {
                        VasniSchema.instance.show(false, cv_package_game)
                    } else {
                        VasniSchema.instance.show(true, cv_package_game)
                    }

                    if (videoList.size == 0) {
                        VasniSchema.instance.show(false, cv_package_video)
                    } else {
                        VasniSchema.instance.show(true, cv_package_video)
                    }

                    if (booksList.size == 0) {
                        VasniSchema.instance.show(false, cv_package_book)
                    } else {
                        VasniSchema.instance.show(true, cv_package_book)
                    }

                    if (musicList.size == 0) {
                        VasniSchema.instance.show(false, cv_package_music)
                    } else {
                        VasniSchema.instance.show(true, cv_package_music)
                    }

                    tv_media_package_category.text = pkgData.category
                    tv_media_package_rating.text = pkgData.logs!!.rate.toString()
                    tv_media_package_view.text = pkgData.logs!!.view.toString()
                    rb_media_package.rating = pkgData.logs!!.rate!!.toFloat()

                    if (pkgData.price == 0)
                        tv_media_package_price.text = fragmentView.context.getString(R.string.free)
                    else
                        tv_media_package_price.text = pkgData.price.toString() + " " + fragmentView.context.getString(R.string.currency)


                    if (pkgData.wallet!!.bought == true) {
                        VasniSchema.instance.show(false, tv_buy_package)
                        VasniSchema.instance.show(false, tv_media_package_price)
                        VasniSchema.instance.buy_is_visible = "1"
                    } else {
                        VasniSchema.instance.show(true, tv_buy_package)
                        VasniSchema.instance.show(true, tv_media_package_price)
                    }

                    val llComments = LinearLayoutManager(fragmentView.context)
                    llComments.orientation = LinearLayoutManager.VERTICAL
                    llComments.reverseLayout = true
                    commentAdapter.removeAllData()
                    rc_media_package_comment.adapter = commentAdapter
                    rc_media_package_comment.setLayoutManager(llComments)
                    commentAdapter.apply {
                        register(RegisterItem(R.layout.row_comment, GameCommentAdapter::class.java))
                        startAnimPosition(1)
                    }
                    commentAdapter.loadData(pkgData.comments!!)
                    commentAdapter.attachTo(rc_media_package_comment)


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
    }

    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
        VasniSchema.instance.showMessage(
                fragmentView.context,
                fragmentView.context.getString(R.string.server_error),
                "",
                fragmentView.context.getString(R.string.ok)
        )
    }

    fun getWalletBalance() {
        VasniSchema.instance.show(true, pv_package_loading)
        ApiService.apiInterface.getWalletBalance().enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                VasniSchema.instance.show(false, pv_package_loading)
                if (response.isSuccessful && response.body() != null) {
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        pkgData.wallet!!.balance = getData(response.body()!!).get("balance").asString
                        if (pkgData.wallet!!.balance == "0") {
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

        customView.tv_dialog_wallet_detail.text = fragmentView.context.getString(R.string.wallet_message_dialog_active_desc)
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
                VasniSchema.instance.show(true, pv_package_loading)
                ApiService.apiInterface.checkWalletVoucher(
                        customView.et_dialog_wallet_active_code.text.trim().toString()
                ).enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful && response.body() != null) {
                            VasniSchema.instance.show(false, pv_package_loading)
                            if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                                VasniSchema.instance.showMessage(
                                        fragmentView.context,
                                        getData(response.body()!!).get("message").asString,
                                        "",
                                        fragmentView.context.getString(R.string.ok)
                                )
//                                recreate()
                                initView()
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

    fun buy() {
        BottomDialog.Builder(fragmentView.context)
                .setContent(fragmentView.context.getString(R.string.wallet_message_dialog))
                .setNegativeText(fragmentView.context.getString(R.string.yes))
                .setNegativeTextColor(ContextCompat.getColor(fragmentView.context, R.color.colorAccent))
                .setPositiveText(fragmentView.context.getString(R.string.skip))
                .autoDismiss(false)
                .setCancelable(false)
                .onNegative(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        if (pkgData.price!! <= pkgData.wallet!!.balance!!.toInt()) {
                            VasniSchema.instance.show(true, pv_package_loading)
                            getWalletReduce()
                        } else {
                            VasniSchema.instance.showMessage(
                                    fragmentView.context,
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
        ApiService.apiInterface.getWalletReduce(pkgData.id!!, VasniSchema.instance.package_type!!
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    VasniSchema.instance.show(false, pv_package_loading)
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        VasniSchema.instance.buy_is_visible = "1"
//                        recreate()
                        initView()
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
