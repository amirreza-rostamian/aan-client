package ir.amin.HaftTeen.vasni.teentaak.fragment.market

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.view_wallet_dialog.view.*
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.VideoPlayer.Jzvd
import me.himanshusoni.chatmessageview.VideoPlayer.JzvdStd
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
import ir.amin.HaftTeen.vasni.adapter.Vitrin.VideoEpisodeAdapter
import ir.amin.HaftTeen.vasni.api.AbrArvanService
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.api.RahpoService
import ir.amin.HaftTeen.vasni.core.DataLoader
import ir.amin.HaftTeen.vasni.extention.*
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Video
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.VideoCategory
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.VideoInfo
import ir.amin.HaftTeen.R

class VideoDetailFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate {

    private var txt: String = ""
    private var videoData = Video()
    private var videoInfo = VideoInfo()
    private var rate = 0
    private var episodes = VideoCategory()
    private val adapter = MoreAdapter()
    private val commentAdapter = MoreAdapter()
    private lateinit var media_video: JzvdStd
    private lateinit var btn_video_play: MTextView
    private lateinit var tv_price_video: MTextView
    private lateinit var ll_media_video_point: LinearLayout
    private lateinit var rb_rate_video: MRatingBar
    private lateinit var tv_point_video: MTextView
    private lateinit var tv_director_video: MTextView
    private lateinit var pv_loading_pic_video: ProgressView
    private lateinit var imv_thumbnail_video: AppCompatImageView
    private lateinit var tv_media_video_volume: MTextViewBold
    private lateinit var tv_media_video_category: MTextViewBold
    private lateinit var tv_media_video_rating: MTextViewBold
    private lateinit var tv_media_video_view: MTextViewBold
    private lateinit var tv_summary_video: JustifiedTextView
    private lateinit var tv_session_video: LinearLayout
    private lateinit var rc_video_detail_more: RecyclerView
    private lateinit var pv_video_detail_loading: View
    private lateinit var rc_media_video_comment: RecyclerView


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
        fragmentView = factory.inflate(R.layout.activity_video, null)
        fragmentView.setOnTouchListener { v, event -> true }

        media_video = fragmentView.findViewById(R.id.media_video)
        btn_video_play = fragmentView.findViewById(R.id.btn_video_play)
        tv_price_video = fragmentView.findViewById(R.id.tv_price_video)
        ll_media_video_point = fragmentView.findViewById(R.id.ll_media_video_point)
        rb_rate_video = fragmentView.findViewById(R.id.rb_rate_video)
        tv_point_video = fragmentView.findViewById(R.id.tv_point_video)
        tv_director_video = fragmentView.findViewById(R.id.tv_director_video)
        pv_loading_pic_video = fragmentView.findViewById(R.id.pv_loading_pic_video)
        imv_thumbnail_video = fragmentView.findViewById(R.id.imv_thumbnail_video)
        tv_media_video_volume = fragmentView.findViewById(R.id.tv_media_video_volume)
        tv_media_video_category = fragmentView.findViewById(R.id.tv_media_video_category)
        tv_media_video_rating = fragmentView.findViewById(R.id.tv_media_video_rating)
        tv_media_video_view = fragmentView.findViewById(R.id.tv_media_video_view)
        tv_summary_video = fragmentView.findViewById(R.id.tv_summary_video)
        tv_session_video = fragmentView.findViewById(R.id.tv_session_video)
        rc_video_detail_more = fragmentView.findViewById(R.id.rc_video_detail_more)
        pv_video_detail_loading = fragmentView.findViewById(R.id.pv_video_detail_loading)
        rc_media_video_comment = fragmentView.findViewById(R.id.rc_media_video_comment)

        videoData = DataLoader.instance.video
        getVideoDetail()
        return fragmentView
    }


    override fun onFragmentCreate(): Boolean {
        return super.onFragmentCreate()
    }

    override fun onFragmentDestroy() {
        super.onFragmentDestroy()
        try {
            Jzvd.releaseAllVideos()
        } catch (e: Exception) {

        }
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
        actionBar.setTitle(videoData.title)
        if (videoData.wallet!!.bought == false && videoData.price != 0) {
            btn_video_play.setText(R.string.buy)
        } else {
            btn_video_play.setText(R.string.show_video)
        }

//        media_video.setUp(videoData.link, videoData.title, Jzvd.SCREEN_WINDOW_LIST)

        media_video.thumbImageView.loadImage(fragmentView.context, videoData.banner!!)
        media_video.thumbImageView.alpha = 0.5f
        media_video.startButton.visibility = View.GONE


        imv_thumbnail_video.loadImage(fragmentView.context, videoData.thumbnail!!)
        rb_rate_video.rating = videoData.logs!!.rate!!.toFloat()
        tv_point_video.text = videoData.logs!!.rate!!.toString()
        if (!videoData.director!!.isEmpty())
            tv_director_video.text = fragmentView.context.getString(R.string.vitrin_video_director) + " " + videoData.director

        if (videoData.price == 0)
            tv_price_video.text = fragmentView.context.getString(R.string.free)
        else
            tv_price_video.text = videoData.price.toString() + " " + fragmentView.context.getString(R.string.currency)

        tv_summary_video.text = videoData.summary
        tv_summary_video.setAlignment(Paint.Align.RIGHT)
        tv_summary_video.setLineSpacing(20)
        tv_summary_video.setTextSize(1, 14.0f)

        if (videoData.volume != null)
            tv_media_video_volume.text = getHumanReadableSize(fragmentView.context, videoData.volume!!.toLong()) + ""
        tv_media_video_category.text = videoData.category
        tv_media_video_rating.text = videoData.logs!!.rate.toString()
        tv_media_video_view.text = videoData.logs!!.view.toString()

        ll_media_video_point.setOnClickListener {
            ratingDialog(fragmentView.context, videoData.id.toString(), VasniSchema.instance.videoActivity)
        }


        btn_video_play.setOnClickListener {
            if (btn_video_play.text.equals(fragmentView.context.getString(R.string.buy))) {
                getWalletBalance()
//                if (videoData.wallet!!.balance == "0") {
//                    activeWallet()
//                } else {
//                    buy()
//                }
            } else {
                if (videoData.fileServiceProvider == VasniSchema.instance.rahpo_file_service) {
                    getVideoLink()
                } else if (videoData.fileServiceProvider == VasniSchema.instance.abr_arvan_file_service) {
                    getMediaFileAbrArvan()
                } else {
                    media_video.setUp(videoData.link, videoData.title, Jzvd.SCREEN_WINDOW_LIST)
                }
            }

        }

        if (videoData.isMemberOfSubCategory!!) {
            tv_session_video.visibility = View.VISIBLE
        } else {
            tv_session_video.visibility = View.GONE
        }

        tv_session_video.setOnClickListener {
            presentFragment(VideoSeasonFragment(videoData.title!!, videoData.parentId!!))
        }

//        getVideoEpisodes(videoData.id!!)
        getVideoEpisodes()

        if (VasniSchema.instance.buy_is_visible == "0") {
            VasniSchema.instance.show(false, btn_video_play)
        } else {
            VasniSchema.instance.show(true, btn_video_play)
        }

        val llComments = LinearLayoutManager(fragmentView.context)
        llComments.orientation = LinearLayoutManager.VERTICAL
        llComments.reverseLayout = true
        rc_media_video_comment.adapter = commentAdapter
        rc_media_video_comment.setLayoutManager(llComments)
        commentAdapter.apply {
            register(RegisterItem(R.layout.row_comment, GameCommentAdapter::class.java))
            startAnimPosition(1)
        }
        commentAdapter.loadData(videoData.comments!!)
        commentAdapter.attachTo(rc_media_video_comment)
    }

    override fun onResume() {
        super.onResume()
        try {
            Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (e: Exception) {

        }
    }

    override fun finishFragment() {
        try {
            if (Jzvd.backPress()) {
                return
            } else {

            }
        } catch (e: Exception) {

        }
        super.finishFragment()
    }

    override fun onPause() {
        super.onPause()
        try {
            Jzvd.releaseAllVideos()
            Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (e: Exception) {

        }
    }

    fun getVideoDetail() {
        VasniSchema.instance.show(true, pv_video_detail_loading)
        ApiService.apiInterface.getVitrinVideoDetail(videoData.id!!
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            VasniSchema.instance.show(false, pv_video_detail_loading)
                            val video = Gson().fromJson(
                                    getData(response.body()!!).get("items").asJsonObject,
                                    Video()::class.java
                            )

                            videoInfo = Gson().fromJson(
                                    getData(response.body()!!),
                                    VideoInfo()::class.java
                            )

                            videoData = video
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

    fun getVideoEpisodes() {
        if (videoInfo.episodes!!.size!! > 0) {
            rc_video_detail_more.layoutManager = LinearLayoutManager(fragmentView.context)
            adapter.apply {
                register(RegisterItem(R.layout.row_cat, VideoEpisodeAdapter::class.java))
                startAnimPosition(1)
            }
            adapter.loadData(videoInfo)
            adapter.attachTo(rc_video_detail_more)
        }
    }

    fun getVideoLink() {
        RahpoService.apiInterface.getVitrinRahpoLink(
                "689725394165", videoData.guid!!, "Free", "Free"
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        if (response.body()!!.get("error_code").asString == "0") {
                            var link = response.body()!!.get("full_addr").asString
                            media_video.setUp(link, videoData.title, Jzvd.SCREEN_WINDOW_LIST)
                            media_video.startButton.visibility = View.GONE
                            media_video.startVideo()
                        } else {
                            VasniSchema.instance.showMessage(
                                    fragmentView.context,
                                    response.body()!!.get("error_desc").asString,
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
        })
    }

    fun getMediaFileAbrArvan() {
        AbrArvanService.apiInterface.getAbrArvanLink(
                VasniSchema.instance.getAbrArvanToken(
                        VasniSchema.instance.api_key_abr_arvan
                ), videoData.guid!!).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    var link = getData(response.body()!!).get("hls_playlist").asString
                    media_video.setUp(link, videoData.title, Jzvd.SCREEN_WINDOW_LIST)
                    media_video.startButton.visibility = View.GONE
                    media_video.startVideo()
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
                        if (videoData.price!! <= videoData.wallet!!.balance!!.toInt()) {
                            VasniSchema.instance.show(true, pv_video_detail_loading)
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
        ApiService.apiInterface.getWalletReduce(videoData.id!!, VasniSchema.instance.video_type!!).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    VasniSchema.instance.show(false, pv_video_detail_loading)
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        btn_video_play.setText(fragmentView.context.getString(R.string.show_video))
                        if (videoData.fileServiceProvider == VasniSchema.instance.rahpo_file_service) {
                            getVideoLink()
                        } else if (videoData.fileServiceProvider == VasniSchema.instance.abr_arvan_file_service) {
                            getMediaFileAbrArvan()
                        } else {
                            media_video.setUp(videoData.link, videoData.title, Jzvd.SCREEN_WINDOW_LIST)
                        }
//                        recreate()
                        getVideoDetail()
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
                VasniSchema.instance.show(true, pv_video_detail_loading)
                ApiService.apiInterface.checkWalletVoucher(
                        customView.et_dialog_wallet_active_code.text.trim().toString()
                ).enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful && response.body() != null) {
                            VasniSchema.instance.show(false, pv_video_detail_loading)
                            if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                                btn_video_play.setText(fragmentView.context.getString(R.string.buy))
                                VasniSchema.instance.showMessage(
                                        fragmentView.context,
                                        getData(response.body()!!).get("message").asString,
                                        "",
                                        fragmentView.context.getString(R.string.ok)
                                )
//                                recreate()
                                getVideoDetail()
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
        VasniSchema.instance.show(true, pv_video_detail_loading)
        ApiService.apiInterface.getWalletBalance().enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    VasniSchema.instance.show(false, pv_video_detail_loading)
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        videoData.wallet!!.balance = getData(response.body()!!).get("balance").asString
                        if (videoData.wallet!!.balance == "0") {
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
