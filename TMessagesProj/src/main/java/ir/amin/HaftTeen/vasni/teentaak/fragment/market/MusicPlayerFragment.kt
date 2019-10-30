package ir.amin.HaftTeen.vasni.teentaak.fragment.market

import android.content.Context
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Handler
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatSeekBar
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.google.gson.JsonObject
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.MTextView
import me.himanshusoni.chatmessageview.ui.MTextViewBold
import me.himanshusoni.chatmessageview.ui.ProgressView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.messenger.NotificationCenter
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.api.RahpoService
import ir.amin.HaftTeen.vasni.core.DataLoader
import ir.amin.HaftTeen.vasni.core.GaussianBlur
import ir.amin.HaftTeen.vasni.extention.*
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Music
import ir.amin.HaftTeen.R
import java.io.IOException

class MusicPlayerFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate {

    private var txt: String = ""
    private var musicData = Music()
    private var mediaPlayer: MediaPlayer? = null
    private val mHandler = Handler()
    private var rate = 0
    private lateinit var img_background_blur: ImageView
    private lateinit var pv_loading_media_music: ProgressView
    private lateinit var imv_media_music_banner: ImageView
    private lateinit var ll_media_music_fav: LinearLayout
    private lateinit var tv_media_music_title: MTextViewBold
    private lateinit var tv_media_music_singer: MTextView
    private lateinit var ll_media_music_more: LinearLayout
    private lateinit var tv_media_music_player: MTextView
    private lateinit var mp_seekbar: AppCompatSeekBar
    private lateinit var tv_media_music_play_time: MTextView
    private lateinit var imv_media_music_shuffle: LinearLayout
    private lateinit var ll_media_music_previous: LinearLayout
    private lateinit var imv_player_backward: AppCompatImageView
    private lateinit var imv_player_puase: AppCompatImageView
    private lateinit var imv_player_forward: AppCompatImageView
    private lateinit var pv_music_player_loading: View


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
        fragmentView = factory.inflate(R.layout.activity_music, null)
        fragmentView.setOnTouchListener { v, event -> true }

        img_background_blur = fragmentView.findViewById(R.id.img_background_blur)
        pv_loading_media_music = fragmentView.findViewById(R.id.pv_loading_media_music)
        imv_media_music_banner = fragmentView.findViewById(R.id.imv_media_music_banner)
        ll_media_music_fav = fragmentView.findViewById(R.id.ll_media_music_fav)
        tv_media_music_title = fragmentView.findViewById(R.id.tv_media_music_title)
        tv_media_music_singer = fragmentView.findViewById(R.id.tv_media_music_singer)
        ll_media_music_more = fragmentView.findViewById(R.id.ll_media_music_more)
        tv_media_music_player = fragmentView.findViewById(R.id.tv_media_music_player)
        mp_seekbar = fragmentView.findViewById(R.id.mp_seekbar)
        imv_media_music_shuffle = fragmentView.findViewById(R.id.imv_media_music_shuffle)
        ll_media_music_previous = fragmentView.findViewById(R.id.ll_media_music_previous)
        tv_media_music_play_time = fragmentView.findViewById(R.id.tv_media_music_play_time)
        imv_player_backward = fragmentView.findViewById(R.id.imv_player_backward)
        imv_player_puase = fragmentView.findViewById(R.id.imv_player_puase)
        imv_player_forward = fragmentView.findViewById(R.id.imv_player_forward)
        pv_music_player_loading = fragmentView.findViewById(R.id.pv_music_player_loading)

        musicData = DataLoader.instance.music
        VasniSchema.instance.show(true, pv_music_player_loading)
        getMusicDetail()

        return fragmentView
    }


    override fun onFragmentCreate(): Boolean {
        return super.onFragmentCreate()
    }

    override fun onFragmentDestroy() {
        super.onFragmentDestroy()
        if (mediaPlayer != null) {
            mediaPlayer!!.reset();
            mediaPlayer!!.release();
            mediaPlayer = null;
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
        actionBar.setTitle(musicData.title)
        imv_media_music_banner.loadImage(fragmentView.context, musicData.banner!!, pv_loading_media_music)
        tv_media_music_title.text = musicData.title
        tv_media_music_singer.text = musicData.logs!!.view.toString() + " " + fragmentView.context.getString(R.string.view)
//        tv_media_music_play_time.text = musicData.playbackTime

        ll_media_music_fav.setOnClickListener {
            ratingDialog(fragmentView.context, musicData.id.toString(), VasniSchema.instance.musicActivity)
        }
        VasniSchema.instance.certificate()
        Glide.with(fragmentView.context)
                .asDrawable()
                .load(musicData.banner)
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        GaussianBlur.with(fragmentView.context).put(resource, img_background_blur);
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        GaussianBlur.with(fragmentView.context).put(R.drawable.ic_launcher, img_background_blur);
                    }
                })
        imv_player_puase.setOnClickListener {
            if (imv_player_puase.tag.equals("puase")) {
                pause()
                imv_player_puase.tag = "play"
                imv_player_puase.setImageResource(R.drawable.ic_play_circle)
            } else if (imv_player_puase.tag.equals("play")) {
                play()
                imv_player_puase.tag = "puase"
                imv_player_puase.setImageResource(R.drawable.ic_pause)
            }
        }
        imv_player_forward.setOnClickListener {
            seekForward()
        }
        imv_player_backward.setOnClickListener {
            seekBackward()
        }

        if (musicData.fileServiceProvider == VasniSchema.instance.rahpo_file_service) {
            getMusicLink()
        } else {
            initMusicPlayer(musicData.link!!)
        }

    }

    private fun initMusicPlayer(link: String) {
        try {
            mediaPlayer = MediaPlayer()
//            mediaPlayer!!.setDataSource("https://www.niniban.com/files/fa/news/1397/7/10/339907_481.mp3")
            mediaPlayer!!.setDataSource(link)
            mediaPlayer!!.prepareAsync()
            mediaPlayer!!.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
                override fun onPrepared(mp: MediaPlayer?) {
                    mp!!.start()
                    mRunnable.run()
                }
            })
        } catch (E: IOException) {
            //Utils.instance.showMessage(this, getString(R.string.server_error), "", getString(R.string.ok))
        }

    }

    private val mRunnable = object : Runnable {
        override fun run() {
            if (mediaPlayer != null) {
                val mDuration = mediaPlayer!!.getDuration()
                mp_seekbar.max = mDuration
                tv_media_music_play_time.setText(getTimeString(mDuration.toLong()))
                val mCurrentPosition = mediaPlayer!!.getCurrentPosition()
                mp_seekbar.progress = mCurrentPosition
                tv_media_music_player.setText(getTimeString(mCurrentPosition.toLong()))
                mp_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onStopTrackingTouch(seekBar: SeekBar) {

                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {

                    }

                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        if (mediaPlayer != null && fromUser) {
                            mediaPlayer!!.seekTo(progress)
                        }
                    }
                })


            }
            mHandler.postDelayed(this, 10)
        }
    }

    private fun play() {
        if (mediaPlayer != null)
            mediaPlayer!!.start()
    }

    private fun pause() {
        if (mediaPlayer != null)
            mediaPlayer!!.pause()
    }

    private fun stop() {
        mediaPlayer!!.seekTo(0)
        mediaPlayer!!.pause()
    }

    private fun seekForward() {
        val seekForwardTime = 5000
        val currentPosition = mediaPlayer!!.getCurrentPosition()
        if (currentPosition + seekForwardTime <= mediaPlayer!!.getDuration()) {
            mediaPlayer!!.seekTo(currentPosition + seekForwardTime)
        } else {
            mediaPlayer!!.seekTo(mediaPlayer!!.getDuration())
        }

    }

    private fun seekBackward() {
        val seekBackwardTime = 5000
        val currentPosition = mediaPlayer!!.getCurrentPosition()
        if (currentPosition - seekBackwardTime >= 0) {
            mediaPlayer!!.seekTo(currentPosition - seekBackwardTime)
        } else {
            mediaPlayer!!.seekTo(0)
        }
    }

    override fun finishFragment() {
        if (mediaPlayer != null) {
            mediaPlayer!!.reset()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
        super.finishFragment()
    }

    fun getMusicDetail() {
        ApiService.apiInterface.getVitrinMusicDetail(musicData.id!!)
                .enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful && response.body() != null) {
                            try {
                                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                                    VasniSchema.instance.show(false, pv_music_player_loading)
                                    val music = Gson().fromJson(
                                            getData(response.body()!!).get("items").asJsonObject,
                                            Music()::class.java
                                    )
                                    musicData = music
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

    fun getMusicLink() {
        RahpoService.apiInterface.getVitrinRahpoLink(
                "689725394165", musicData.guid!!, "Free", "Free"
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        if (response.body()!!.get("error_code").asString == "0") {
                            var link = response.body()!!.get("full_addr").asString
                            initMusicPlayer(link)
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

}
