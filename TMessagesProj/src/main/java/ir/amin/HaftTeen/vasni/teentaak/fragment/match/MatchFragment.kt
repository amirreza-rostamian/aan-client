package ir.amin.HaftTeen.vasni.teentaak.fragment.match

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatSeekBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.VideoPlayer.Jzvd
import me.himanshusoni.chatmessageview.VideoPlayer.JzvdStd
import me.himanshusoni.chatmessageview.ui.*
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.messenger.ApplicationLoader
import ir.amin.HaftTeen.messenger.NotificationCenter
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.vasni.Interface.OptionClickListener
import ir.amin.HaftTeen.vasni.adapter.Match.OptionAdapter
import ir.amin.HaftTeen.vasni.api.AbrArvanService
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.api.RahpoService
import ir.amin.HaftTeen.vasni.core.DataLoader
import ir.amin.HaftTeen.vasni.extention.*
import ir.amin.HaftTeen.vasni.model.teentaak.EventHandler
import ir.amin.HaftTeen.vasni.model.teentaak.Match
import ir.amin.HaftTeen.vasni.utils.DownTimer
import ir.amin.HaftTeen.R
import java.io.IOException

class MatchFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate {

    private var txt: String = ""
    private lateinit var eventHandler: EventHandler
    var questions: List<Match> = ArrayList()
    var questionOpt: ArrayList<Match> = ArrayList()
    var downTimer = DownTimer()
    var matchAdapter: OptionAdapter? = null
    var userSelectedItemId: Int = 0
    var score = 0
    var step: String = "0"
    private var mediaPlayer: MediaPlayer? = null
    private val mHandler = Handler()
    private var tvQuestion: MTextViewBold? = null
    private var pv_loading_pic_question: ProgressView? = null
    private var img_question: AppCompatImageView? = null
    private var video_question: JzvdStd? = null
    private var tv_player_voice_question: MTextView? = null
    private var seekbar_voice_question: AppCompatSeekBar? = null
    private var tv_player_time_voice_question: MTextView? = null
    private var imv_voice_question: AppCompatImageView? = null
    private var rvOptions: RecyclerView? = null
    private var et_text_answer: MEditText? = null
    private var tv_match_score: MTextView? = null
    private var tv_match_number: MTextView? = null
    private var match_step_indicator: StepIndicator? = null
    private var pv_match: View? = null
    private var rl_quize_result: RelativeLayout? = null
    private var img_match_emo: AppCompatImageView? = null
    private var tv_match_point: MTextViewBold? = null
    private var tv_match_desc: MTextViewBold? = null
    private var btn_continue_quiz: MButton? = null
    private var btn_cancel_quize: MButton? = null
    private var ll_voice_question: LinearLayout? = null


    constructor(txt: String, Data: EventHandler) {
        this.txt = txt
        this.eventHandler = Data
        this.step = "0"
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
        fragmentView = factory.inflate(R.layout.frg_match, null)
        fragmentView.setOnTouchListener { v, event -> true }

        tvQuestion = fragmentView.findViewById(R.id.tvQuestion)
        pv_loading_pic_question = fragmentView.findViewById(R.id.pv_loading_pic_question)
        img_question = fragmentView.findViewById(R.id.img_question)
        video_question = fragmentView.findViewById(R.id.video_question)
        tv_player_voice_question = fragmentView.findViewById(R.id.tv_player_voice_question)
        seekbar_voice_question = fragmentView.findViewById(R.id.seekbar_voice_question)
        tv_player_time_voice_question = fragmentView.findViewById(R.id.tv_player_time_voice_question)
        imv_voice_question = fragmentView.findViewById(R.id.imv_voice_question)
        rvOptions = fragmentView.findViewById(R.id.rvOptions)
        et_text_answer = fragmentView.findViewById(R.id.et_text_answer)
        tv_match_score = fragmentView.findViewById(R.id.tv_match_score)
        tv_match_number = fragmentView.findViewById(R.id.tv_match_number)
        match_step_indicator = fragmentView.findViewById(R.id.match_step_indicator)
        pv_match = fragmentView.findViewById(R.id.pv_match)
        rl_quize_result = fragmentView.findViewById(R.id.rl_quize_result)
        img_match_emo = fragmentView.findViewById(R.id.img_match_emo)
        tv_match_point = fragmentView.findViewById(R.id.tv_match_point)
        tv_match_desc = fragmentView.findViewById(R.id.tv_match_desc)
        btn_continue_quiz = fragmentView.findViewById(R.id.btn_continue_quiz)
        btn_cancel_quize = fragmentView.findViewById(R.id.btn_cancel_quize)
        ll_voice_question = fragmentView.findViewById(R.id.ll_voice_question)

        matchAdapter = OptionAdapter(questionOpt!!, context!!, object : OptionClickListener {
            override fun onItemClick(view: View, position: Int) {
                view.setClickable(false)
                match_step_indicator!!.isClickable = false
                userSelectedItemId = questionOpt.get(position).optionId
                questions.get(step.toInt()).userAnswerId = userSelectedItemId
                match_step_indicator!!.currentStepPosition = step.toInt()
                DataLoader.instance.btnOptions.get(position).setBackgroundResource(R.drawable.bgr_option)
//                DataLoader.instance.btnOptions.get(position).setTextColor(Color.BLACK)
                for (i in 0 until questionOpt.size) {
                    DataLoader.instance.btnOptions.get(i).setClickable(false)
                    DataLoader.instance.btnOptions.get(i).setEnabled(false)
                }
                ApplicationLoader.mDelayHandler!!.postDelayed(object : Runnable {
                    override fun run() {
                        if (questions.get(step.toInt()).userAnswerId == questions.get(
                                        step.toInt()
                                ).answer_id
                        ) {
                            DataLoader.instance.btnOptions.get(position)
                                    .setBackgroundResource(R.drawable.bgr_option_correct)
                            score = score + questions.get(step.toInt()).score
                            if (tv_match_score != null)
                                tv_match_score!!.setText(score.toString() + " " + context.getString(R.string.scores))
                            /* DataLoader.instance.btnOptions.get(position)
                                 .setTextColor(ContextCompat.getColor(activity!!, R.color.white))*/
                        } else {
                            DataLoader.instance.btnOptions.get(position)
                                    .setBackgroundResource(R.drawable.bgr_option_wrong)
                            /*DataLoader.instance.btnOptions.get(position)
                                .setTextColor(ContextCompat.getColor(activity!!, R.color.white))*/
                            for (i in 0 until questionOpt.size) {
                                if (questionOpt.get(i).optionId == questions.get(step.toInt()).answer_id) {
                                    DataLoader.instance.btnOptions.get(i)
                                            .setBackgroundResource(R.drawable.bgr_option_correct)
                                }
                            }

                        }

                    }
                }, 100)

                ApplicationLoader.mDelayHandler!!.postDelayed(object : Runnable {
                    override fun run() {
                        try {
                            downTimer.cancel()
                            addResult()
                            resetOptions()
                            step = step.toInt().inc().toString()
                            match_step_indicator!!.currentStepPosition = step.toInt()
                            setData()
                        } catch (e: Exception) {
                        }

                    }

                }, 1000)
            }

            override fun onMediaClick(view: View, position: Int) {
                imv_voice_question!!.tag = "play"
                imv_voice_question!!.setImageResource(R.drawable.ic_play_circle)
                for (i in 0 until questionOpt.size) {
                    if (i != position) {
                        DataLoader.instance.voiceOptions.get(i).tag = "play"
                        DataLoader.instance.voiceOptions.get(i).setImageResource(R.drawable.ic_play_circle)
                    }
                }
                if (DataLoader.instance.voiceOptions.get(position).tag.equals("pause")) {
                    pause()
                    DataLoader.instance.voiceOptions.get(position).tag = "play"
                    DataLoader.instance.voiceOptions.get(position).setImageResource(R.drawable.ic_play_circle)
                } else if (DataLoader.instance.voiceOptions.get(position).tag.equals("play")) {
                    if (questionOpt.get(position).file_service == VasniSchema.instance.rahpo_file_service) {
//                        getMediaFileMusic(questionOpt.get(position).file!!)
                        getMediaFileRahpoMusic(questionOpt.get(position).file!!)
                    } else {
                        initMusicPlayer(questionOpt.get(position).file.toString())
                    }
                    DataLoader.instance.voiceOptions.get(position).tag = "pause"
                    DataLoader.instance.voiceOptions.get(position).setImageResource(R.drawable.ic_pause)
                }
            }
        })

        if (questions.size != 0) {
            if (questions.get(step.toInt()).type == VasniSchema.instance.match_text_answer) {
                VasniSchema.instance.show(false, rvOptions!!)
                VasniSchema.instance.show(true, et_text_answer!!)
                et_text_answer!!.setText("")
            } else {
                VasniSchema.instance.show(true, rvOptions!!)
                VasniSchema.instance.show(false, et_text_answer!!)
            }
        }

        if (questionOpt.size != 0) {
            if (questionOpt.get(0).file_type == VasniSchema.instance.match_image_type || questionOpt.get(0).file_type == VasniSchema.instance.match_video_type) {
                rvOptions!!.layoutManager = RtlGrid(context!!, 2)
                rvOptions!!.adapter = matchAdapter
            } else {
                rvOptions!!.layoutManager = LinearLayoutManager(context)
                rvOptions!!.adapter = matchAdapter
            }
        } else {
            rvOptions!!.layoutManager = LinearLayoutManager(context)
            rvOptions!!.adapter = matchAdapter
        }

        matchAdapter!!.notifyDataSetChanged()
        DataLoader.instance.btnOptions = ArrayList()
        DataLoader.instance.result = JsonArray()
        getQuestions()
        btn_continue_quiz!!.setOnClickListener {
            VasniSchema.instance.show(false, rl_quize_result!!)
            VasniSchema.instance.show(true, pv_match!!)
            DataLoader.instance.result = JsonArray()
            getQuestions()
            match_step_indicator!!.currentStepPosition = 0
        }

        btn_cancel_quize!!.setOnClickListener {
            finishFragment()
        }

        imv_voice_question!!.setOnClickListener {
            if (questionOpt.get(0).file_type == VasniSchema.instance.match_voice_type) {
                for (i in 0 until questionOpt.size) {
                    DataLoader.instance.voiceOptions.get(i).tag = "play"
                    DataLoader.instance.voiceOptions.get(i).setImageResource(R.drawable.ic_play_circle)
                }
            }
            if (imv_voice_question!!.tag.equals("pause")) {
                pause()
                imv_voice_question!!.tag = "play"
                imv_voice_question!!.setImageResource(R.drawable.ic_play_circle)
            } else if (imv_voice_question!!.tag.equals("play")) {
                initMusicPlayer(questions.get(step.toInt()).file.toString())
                imv_voice_question!!.tag = "pause"
                imv_voice_question!!.setImageResource(R.drawable.ic_pause)
            }
        }

        et_text_answer!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                et_text_answer!!.setTextColor(context.resources.getColor(R.color.Black))
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (questionOpt.size >= 2) {
                    if (et_text_answer!!.text.toString().trim().length == questionOpt.get(0).optionTitle!!.trim().length && questionOpt.get(
                                    0
                            ).optionTitle!!.contains(et_text_answer!!.text.toString().trim())
                    ) {
                        et_text_answer!!.setTextColor(context.resources.getColor(R.color.Green))
                        questions.get(step.toInt()).userAnswerId = questionOpt.get(0).optionId
                        ApplicationLoader.mDelayHandler!!.postDelayed(object : Runnable {
                            override fun run() {
                                try {
                                    downTimer.cancel()
                                    addResult()
                                    resetOptions()
                                    step = step.toInt().inc().toString()
                                    match_step_indicator!!.currentStepPosition = step.toInt()
                                    setData()
                                } catch (e: Exception) {
                                }

                            }

                        }, 2000)
                    } else {
                        questions.get(step.toInt()).userAnswerId = questionOpt.get(1).optionId
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {

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


    private fun getQuestions() {
//        var gradeId = MSharePk.getString(activity!!, AppSchema.instance.gradeId, "0")

        ApiService.apiInterface.getMatch(eventHandler.match, VasniSchema.instance.appGrade
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            try {
                                questions =
                                        Gson().fromJson(
                                                getDataArray(response.body()!!),
                                                Array<Match>::class.java
                                        ).toList()
                                setData()
                                VasniSchema.instance.show(false, pv_match!!)
                                match_step_indicator!!.stepsCount = questions.size
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else {
//                            fragmentView.context!!.onBackPressed()
                            finishFragment()
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

    private fun setData() {

        if (mediaPlayer != null) {
            mediaPlayer!!.reset()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
        Jzvd.releaseAllVideos()

        if (step.toInt() < questions.size) {
            if (tv_match_number != null)
                tv_match_number!!.text = " سوال شماره " + (step.toInt().inc().toString())
            if (tvQuestion != null)
                tvQuestion!!.text = questions.get(step.toInt()).title

            if (questions.get(step.toInt()).file_type == null || questions.get(step.toInt()
                    ).file_type == ""
            ) {
                VasniSchema.instance.show(false, img_question!!)
                VasniSchema.instance.show(false, pv_loading_pic_question!!)
                VasniSchema.instance.show(false, video_question!!)
                ll_voice_question!!.visibility = View.GONE
            } else if (questions.get(step.toInt()).file_type == VasniSchema.instance.match_image_type) {
                img_question!!.loadImage(fragmentView.context!!, questions.get(step.toInt()).file.toString())
                VasniSchema.instance.show(true, img_question!!)
                VasniSchema.instance.show(true, pv_loading_pic_question!!)
                VasniSchema.instance.show(false, video_question!!)
                ll_voice_question!!.visibility = View.GONE
            } else if (questions.get(step.toInt()).file_type == VasniSchema.instance.match_video_type) {
                video_question!!.thumbImageView.loadImage(fragmentView.context!!, questions.get(step.toInt()).thumbnail!!)
                if (questions.get(step.toInt()).file_service == VasniSchema.instance.rahpo_file_service) {
//                    getMediaFile(questions.get(step.toInt()))
                    getMediaFileRahpo(questions.get(step.toInt()))
                } else if (questions.get(step.toInt()).file_service == VasniSchema.instance.abr_arvan_file_service) {
                    getMediaFileAbrArvan(questions.get(step.toInt()))
                } else {
                    video_question!!.setUp(
                            questions.get(step.toInt()).file,
                            questions.get(step.toInt()).title,
                            Jzvd.SCREEN_WINDOW_LIST
                    )
                }

                VasniSchema.instance.show(false, img_question!!)
                VasniSchema.instance.show(false, pv_loading_pic_question!!)
                VasniSchema.instance.show(true, video_question!!)
                ll_voice_question!!.visibility = View.GONE
            } else if (questions.get(step.toInt()).file_type == VasniSchema.instance.match_voice_type) {
                VasniSchema.instance.show(false, img_question!!)
                VasniSchema.instance.show(false, pv_loading_pic_question!!)
                VasniSchema.instance.show(false, video_question!!)
                ll_voice_question!!.visibility = View.VISIBLE
                if (questions.get(step.toInt()).file_service == VasniSchema.instance.rahpo_file_service) {
//                    getMediaFile(questions.get(step.toInt()))
                    getMediaFileRahpo(questions.get(step.toInt()))
                } else {
                    initMusicPlayer(questions.get(step.toInt()).file.toString())
                }
            }

            DataLoader.instance.btnOptions = ArrayList()
            questionOpt.clear()
            var optionArray: JsonArray = questions.get(step.toInt()).options!!
            for (i in 0 until optionArray.size()) {
                var optJs = optionArray.get(i).asJsonObject
                var match = Match()
                match.optionId = optJs.get("option_id").asInt
                match.optionTitle = optJs.get("option_title").asString.trim()
                match.file = optJs.get("file").asString.trim()
                match.file_type = optJs.get("file_type")!!.asString.trim()
                match.file_service = optJs.get("file_service")!!.asString
                match.thumbnail = optJs.get("thumbnail")!!.asString
                questionOpt.add(match)
            }

            if (questions.size != 0) {
                if (questions.get(step.toInt()).type == VasniSchema.instance.match_text_answer) {
                    VasniSchema.instance.show(false, rvOptions!!)
                    VasniSchema.instance.show(true, et_text_answer!!)
                    et_text_answer!!.setText("")
                } else {
                    VasniSchema.instance.show(true, rvOptions!!)
                    VasniSchema.instance.show(false, et_text_answer!!)
                }
            }

            if (questionOpt.size != 0) {
                if (questionOpt.get(0).file_type == VasniSchema.instance.match_image_type || questionOpt.get(0).file_type == VasniSchema.instance.match_video_type) {
                    rvOptions!!.layoutManager = RtlGrid(fragmentView.context!!, 2)
                    rvOptions!!.adapter = matchAdapter
                } else {
                    rvOptions!!.layoutManager = LinearLayoutManager(fragmentView.context)
                    rvOptions!!.adapter = matchAdapter
                }
            } else {
                rvOptions!!.layoutManager = LinearLayoutManager(fragmentView.context)
                rvOptions!!.adapter = matchAdapter
            }
            matchAdapter!!.notifyDataSetChanged()
            runTimer()

        } else {
            step = "0"
            questions = emptyList()
            questionOpt.clear()
            tv_match_number!!.text = ""
            tvQuestion!!.text = ""
            matchAdapter!!.notifyDataSetChanged()
            VasniSchema.instance.show(true, pv_match!!)
            var answerMatch = JsonObject()
            answerMatch.add("data", DataLoader.instance.result)
//            sendAnswerMatch(answerMatch)
            sendAnswerMatch(DataLoader.instance.result)
        }


    }

    private fun runTimer() {
        var time = questions.get(step.toInt()).time * 1000
        //pb_match_timer.max = questions.get(step.toInt()).time
        downTimer = DownTimer()
        downTimer.setTotalTime(time.toLong())
        downTimer.setIntervalTime(1000)
        downTimer.start()
        downTimer.setTimerLiener(object : DownTimer.TimeListener {
            override fun onFinish() {
                try {
                    downTimer.cancel()
                    addResult()
                    resetOptions()
                    step = step.toInt().inc().toString()
                    match_step_indicator!!.currentStepPosition = step.toInt()
                    setData()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onInterval(remainTime: Long) {
                try {
                    val number = java.lang.Long.toString(remainTime / 1000)

                    // pb_match_timer.setProgress(number.toInt())
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })

    }

    private fun addResult() {
        try {
            if (questions.size > 0)
                if (questions.get(step.toInt()!!).userAnswerId != 0) {
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("question_id", questions.get(step.toInt()!!).id.toString())
                    jsonObject.addProperty("answer_id", questions.get(step.toInt()!!).userAnswerId.toString())
//                jsonObject.addProperty("correct_answer_id", questions.get(step.toInt()!!).answer_id)
//                jsonObject.addProperty("category_id", eventHandler.category)
                    DataLoader.instance.result.add(jsonObject)
                }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun resetOptions() {
        if (DataLoader.instance.btnOptions.size !== 0) {
            for (i in 0 until questionOpt.size) {
                DataLoader.instance.btnOptions.get(i).setClickable(true)
                DataLoader.instance.btnOptions.get(i).setEnabled(true)
                DataLoader.instance.btnOptions.get(i).setBackgroundResource(R.drawable.bgr_option)
            }
        }

    }

    private fun sendAnswerMatch(jsonArray: JsonArray) {
        ApiService.apiInterface.sendAnswer(eventHandler.match, jsonArray
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            VasniSchema.instance.show(true, rl_quize_result!!)
                            img_match_emo!!.loadImage(
                                    fragmentView.context!!,
                                    getData(response.body()!!).get("icon").asString
                            )
                            tv_match_desc!!.setText(getData(response.body()!!).get("text").asString.trim())
                            tv_match_point!!.setText("امتیاز کسب شده : " + getData(response.body()!!).get("point").asString.trim())

                        } else {
//                            fragmentView.context!!.onBackPressed()
                            finishFragment()
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
        })
    }

    private fun initMusicPlayer(file: String) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer!!.reset()
                mediaPlayer!!.release()
                mediaPlayer = null
            }

            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setDataSource(file)
            mediaPlayer!!.prepareAsync()
            mediaPlayer!!.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
                override fun onPrepared(mp: MediaPlayer?) {
                    mp!!.start()
                    mRunnable.run()
                }
            })
        } catch (E: IOException) {
        }
    }

    private val mRunnable = object : Runnable {
        override fun run() {
            if (mediaPlayer != null) {
                val mDuration = mediaPlayer!!.getDuration()
                seekbar_voice_question!!.max = mDuration
                tv_player_time_voice_question!!.setText(getTimeString(mDuration.toLong()))
                val mCurrentPosition = mediaPlayer!!.getCurrentPosition()
                seekbar_voice_question!!.progress = mCurrentPosition
                tv_player_voice_question!!.setText(getTimeString(mCurrentPosition.toLong()))
                seekbar_voice_question!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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
        mediaPlayer!!.start()
    }

    private fun pause() {
        mediaPlayer!!.pause()
    }

    private fun stop() {
        mediaPlayer!!.seekTo(0)
        mediaPlayer!!.pause()
    }

    fun getMediaFile(match: Match) {
        ApiService.apiInterface.getMatchMediaFile(match.file!!).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        var mediaFile = getData(response.body()!!).get("file").asString

                        if (match.file_type == VasniSchema.instance.match_video_type) {
                            video_question!!.setUp(
                                    mediaFile,
                                    match.title,
                                    Jzvd.SCREEN_WINDOW_LIST
                            )
                        } else if (match.file_type == VasniSchema.instance.match_voice_type) {
                            questions.get(step.toInt()).file = mediaFile
                            initMusicPlayer(mediaFile)
                        }
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
        })
    }

    fun getMediaFileMusic(file: String) {
        ApiService.apiInterface.getMatchMediaFile(file).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        var musicFile = getData(response.body()!!).get("file").asString
                        initMusicPlayer(musicFile)
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

    fun getMediaFileRahpo(match: Match) {
        RahpoService.apiInterface.getVitrinRahpoLink(
                "689725394165", match.file!!, "Free", "Free"
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        if (response.body()!!.get("error_code").asString == "0") {
                            var mediaFile = response.body()!!.get("full_addr").asString

                            if (match.file_type == VasniSchema.instance.match_video_type) {
                                video_question!!.setUp(
                                        mediaFile,
                                        match.title,
                                        Jzvd.SCREEN_WINDOW_LIST
                                )
                            } else if (match.file_type == VasniSchema.instance.match_voice_type) {
                                questions.get(step.toInt()).file = mediaFile
                                initMusicPlayer(mediaFile)
                            }

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
        })
    }

    fun getMediaFileRahpoMusic(file: String) {
        RahpoService.apiInterface.getVitrinRahpoLink(
                "689725394165", file, "Free", "Free"
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        if (response.body()!!.get("error_code").asString == "0") {
                            var musicFile = response.body()!!.get("full_addr").asString
                            initMusicPlayer(musicFile)
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
        })
    }

    fun getMediaFileAbrArvan(match: Match) {
        AbrArvanService.apiInterface.getAbrArvanLink(
                VasniSchema.instance.getAbrArvanToken(
                        VasniSchema.instance.api_key_abr_arvan
                ), match.file!!
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    var mediaFile = getData(response.body()!!).get("hls_playlist").asString
                    video_question!!.setUp(
                            mediaFile,
                            match.title,
                            Jzvd.SCREEN_WINDOW_LIST
                    )
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
}
