package ir.amin.HaftTeen.vasni.teentaak.fragment.match

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.Vasni.Wave.MultiWaveHeader
import me.himanshusoni.chatmessageview.ui.BottomDialog
import me.himanshusoni.chatmessageview.ui.CircularCountDownBar
import me.himanshusoni.chatmessageview.ui.MTextView
import me.himanshusoni.chatmessageview.ui.MTextViewBold
import me.himanshusoni.chatmessageview.ui.RoundProgress.RoundCornerProgressBar
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import ir.amin.HaftTeen.messenger.NotificationCenter
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.vasni.extention.consume
import ir.amin.HaftTeen.vasni.utils.Function
import ir.amin.HaftTeen.R
import java.net.URI
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.math.round

class OnlineMatchFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate {

    private var txt: String = ""
    private lateinit var tv_match_totaluser: MTextViewBold
    private lateinit var tv_match_score: MTextViewBold
    private lateinit var wave_match: MultiWaveHeader
    private lateinit var rl_start_match: RelativeLayout
    private lateinit var pb_match_online: CircularCountDownBar
    private lateinit var tv_time_out: MTextViewBold
    private lateinit var tv_match_quize: MTextViewBold
    private lateinit var rl_option1: RelativeLayout
    private lateinit var pb_option1: RoundCornerProgressBar
    private lateinit var tv_match_option1: MTextView
    private lateinit var tv_count_1: MTextView
    private lateinit var rl_option2: RelativeLayout
    private lateinit var pb_option2: RoundCornerProgressBar
    private lateinit var tv_match_option2: MTextView
    private lateinit var tv_count_2: MTextView
    private lateinit var rl_option3: RelativeLayout
    private lateinit var pb_option3: RoundCornerProgressBar
    private lateinit var tv_match_option3: MTextView
    private lateinit var tv_count_3: MTextView
    private lateinit var rl_end_match: RelativeLayout
    private lateinit var rl_winner_match: RelativeLayout
    private lateinit var tv_lose_txt: MTextViewBold
    private lateinit var rl_counter: RelativeLayout
    private lateinit var tv_question_number: MTextViewBold
    private lateinit var pv_loading_match: View
    private lateinit var tv_loading_desc: MTextView
    private lateinit var img_background_loading: AppCompatImageView
    private var mWebSocketClient: WebSocketClient? = null
    private var socketReconnect: Boolean = false
    private var uri: URI? = null
    private var closeType: String = ""
    private var selectedAnswer: Int = 0
    private var questionNumber: Int = 0

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
                    try {
                        selectedAnswer = 0
                        if (mWebSocketClient != null) {
                            if (mWebSocketClient!!.isOpen()) {
                                val jsonObject = JsonObject()
                                jsonObject.addProperty("type", "disconnect")
                                mWebSocketClient!!.send(jsonObject.toString())
                                socketReconnect = false
                                mWebSocketClient!!.close()
                                finishFragment()
                            } else {
                                finishFragment()
                            }
                        }
                    } catch (e: Exception) {

                    }
                }
            }
        })
        val factory = LayoutInflater.from(context)
        fragmentView = factory.inflate(R.layout.activity_online_match, null)
        fragmentView.setOnTouchListener { v, event -> true }

        tv_match_totaluser = fragmentView.findViewById(R.id.tv_match_totaluser)
        tv_match_score = fragmentView.findViewById(R.id.tv_match_score)
        wave_match = fragmentView.findViewById(R.id.wave_match)
        rl_start_match = fragmentView.findViewById(R.id.rl_start_match)
        pb_match_online = fragmentView.findViewById(R.id.pb_match_online)
        tv_time_out = fragmentView.findViewById(R.id.tv_time_out)
        tv_match_quize = fragmentView.findViewById(R.id.tv_match_quize)
        rl_option1 = fragmentView.findViewById(R.id.rl_option1)
        pb_option1 = fragmentView.findViewById(R.id.pb_option1)
        tv_match_option1 = fragmentView.findViewById(R.id.tv_match_option1)
        tv_count_1 = fragmentView.findViewById(R.id.tv_count_1)
        rl_option2 = fragmentView.findViewById(R.id.rl_option2)
        pb_option2 = fragmentView.findViewById(R.id.pb_option2)
        tv_match_option2 = fragmentView.findViewById(R.id.tv_match_option2)
        tv_count_2 = fragmentView.findViewById(R.id.tv_count_2)
        rl_option3 = fragmentView.findViewById(R.id.rl_option3)
        pb_option3 = fragmentView.findViewById(R.id.pb_option3)
        tv_match_option3 = fragmentView.findViewById(R.id.tv_match_option3)
        tv_count_3 = fragmentView.findViewById(R.id.tv_count_3)
        rl_end_match = fragmentView.findViewById(R.id.rl_end_match)
        rl_winner_match = fragmentView.findViewById(R.id.rl_winner_match)
        tv_lose_txt = fragmentView.findViewById(R.id.tv_lose_txt)
        rl_counter = fragmentView.findViewById(R.id.rl_counter)
        tv_question_number = fragmentView.findViewById(R.id.tv_question_number)
        pv_loading_match = fragmentView.findViewById(R.id.pv_loading_match)
        tv_loading_desc = fragmentView.findViewById(R.id.tv_loading_desc)
        img_background_loading = fragmentView.findViewById(R.id.img_background_loading)

        val color = context!!.resources.getColor(R.color.dark_blue)

        tv_loading_desc.setTextColor(context!!.resources.getColor(R.color.white))
        tv_loading_desc.alpha = 0.9f
        img_background_loading.setBackgroundResource(R.color.dark_blue)

        parentActivity.runOnUiThread {
            connectWebSocket()
        }

        rl_option1.setOnClickListener {
            selectedAnswer = 1
            pb_option1.setProgressBackgroundColor(context!!.getResources().getColor(R.color.colorLight))
            rl_option1.isClickable = false
            rl_option2.isClickable = false
            rl_option3.isClickable = false
            val jsonObject = JsonObject()
            jsonObject.addProperty("type", "answer")
            jsonObject.addProperty("answer", selectedAnswer)
            mWebSocketClient!!.send(jsonObject.toString())
        }


        rl_option2.setOnClickListener {
            selectedAnswer = 2
            pb_option2.setProgressBackgroundColor(context!!.getResources().getColor(R.color.colorLight))
            rl_option2.isClickable = false
            rl_option1.isClickable = false
            rl_option3.isClickable = false
            val jsonObject = JsonObject()
            jsonObject.addProperty("type", "answer")
            jsonObject.addProperty("answer", selectedAnswer)
            mWebSocketClient!!.send(jsonObject.toString())
        }

        rl_option3.setOnClickListener {
            selectedAnswer = 3
            pb_option3.setProgressBackgroundColor(context!!.getResources().getColor(R.color.colorLight))
            rl_option3.isClickable = false
            rl_option2.isClickable = false
            rl_option1.isClickable = false
            val jsonObject = JsonObject()
            jsonObject.addProperty("type", "answer")
            jsonObject.addProperty("answer", selectedAnswer)
            mWebSocketClient!!.send(jsonObject.toString())
        }

        return fragmentView

    }

    private fun closeSocket() {
        try {
            if (mWebSocketClient != null) {
                if (mWebSocketClient!!.isOpen) {
                    socketReconnect = false
                    mWebSocketClient!!.close()
                }
            }
        } catch (e: Exception) {

        }
    }

    override fun onBackPressed(): Boolean {
        try {
            selectedAnswer = 0
            if (mWebSocketClient != null) {
                if (mWebSocketClient!!.isOpen()) {
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("type", "disconnect")
                    mWebSocketClient!!.send(jsonObject.toString())
                    socketReconnect = false
                    mWebSocketClient!!.close()
                    finishFragment()
                } else {
                    finishFragment()
                }
            }
        } catch (e: Exception) {

        }
        return super.onBackPressed()
    }

    private fun connectWebSocket() {
        val map = HashMap<String, String>()
        map.put("user_id", Function.getUserId(parentActivity))
        uri = URI("wss://chat31-asi.10d.ir:1337")
        mWebSocketClient = object : WebSocketClient(uri, map) {
            override fun onOpen(serverHandshake: ServerHandshake) {
                parentActivity.runOnUiThread {
                    VasniSchema.instance.show(false, pv_loading_match)
                }
            }

            override fun onMessage(s: String) {
                val resultJson = Gson().fromJson(s, JsonObject::class.java)
                val type = resultJson.get("type").getAsString()
                val jsonObject = Gson().fromJson(s, JsonObject::class.java)
                when (type) {
                    VasniSchema.instance.CONNECT -> consume {
                        parentActivity.runOnUiThread {
                            VasniSchema.instance.show(true, pv_loading_match)
                            tv_loading_desc.setText(fragmentView.context.getString(R.string.waite_for_question))
                            tv_loading_desc.setTextColor(fragmentView.context.resources.getColor(R.color.white))
                            tv_loading_desc.alpha = 0.9f
                            img_background_loading.setBackgroundResource(R.color.dark_blue)
                        }
                    }
                    VasniSchema.instance.QUESTION -> consume {
                        parentActivity.runOnUiThread {
                            startTimer()
                            rl_option3.isClickable = true
                            rl_option2.isClickable = true
                            rl_option1.isClickable = true
                            rl_start_match.visibility = View.VISIBLE
                            rl_counter.visibility = View.VISIBLE
                            questionNumber = questionNumber.inc()
                            tv_question_number.text = questionNumber.toString()
                            VasniSchema.instance.show(false, pv_loading_match)
                            val question = jsonObject.get("data").asJsonObject.get("question").asString
                            val total = jsonObject.get("data").asJsonObject.get("total").asInt
                            val star = jsonObject.get("data").asJsonObject.get("star").asInt
                            val score = jsonObject.get("data").asJsonObject.get("score").asInt
                            val answer1 =
                                    jsonObject.get("data").asJsonObject.get("answer").asJsonObject.get("1").asString
                            val answer2 =
                                    jsonObject.get("data").asJsonObject.get("answer").asJsonObject.get("2").asString
                            val answer3 =
                                    jsonObject.get("data").asJsonObject.get("answer").asJsonObject.get("3").asString
                            tv_match_totaluser.setText("")
                            tv_match_score.setText("")
                            tv_match_quize.setText("")
                            tv_match_option3.setText("")
                            tv_match_option2.setText("")
                            tv_match_option1.setText("")
                            tv_count_1.setText("")
                            tv_count_2.setText("")
                            tv_count_3.setText("")
                            pb_option1.progress = 0f
                            pb_option2.progress = 0f
                            pb_option3.progress = 0f
                            pb_option1.setProgressBackgroundColor(fragmentView.context.getResources().getColor(R.color.colorDarkBlue1))
                            pb_option2.setProgressBackgroundColor(fragmentView.context.getResources().getColor(R.color.colorDarkBlue1))
                            pb_option3.setProgressBackgroundColor(fragmentView.context.getResources().getColor(R.color.colorDarkBlue1))
                            tv_match_totaluser.setText("" + " نفر " + total)
                            tv_match_score.setText("" + score + " امتیاز ")
                            tv_match_quize.setText(question)
                            tv_match_option3.setText(answer3)
                            tv_match_option2.setText(answer2)
                            tv_match_option1.setText(answer1)
                        }
                    }
                    VasniSchema.instance.ANSWER -> consume {
                        parentActivity.runOnUiThread {
                            var prgOne = 0
                            var prgTwo = 0
                            var prgThree = 0
                            val correct = jsonObject.get("correct").asInt
                            val countUserAnswerOne = jsonObject.get("data").asJsonObject.get("1").asInt
                            val countUserAnswerTwo = jsonObject.get("data").asJsonObject.get("2").asInt
                            val countUserAnswerThree = jsonObject.get("data").asJsonObject.get("3").asInt
                            val allpercent = countUserAnswerOne + countUserAnswerTwo + countUserAnswerThree
                            try {
                                prgOne =
                                        round((countUserAnswerOne.toDouble().div(allpercent)).times(100).toDouble()).toInt()
                                prgTwo =
                                        round((countUserAnswerTwo.toDouble().div(allpercent)).times(100).toDouble()).toInt()
                                prgThree =
                                        round((countUserAnswerThree.toDouble().div(allpercent)).times(100).toDouble()).toInt()
                            } catch (e: Exception) {

                            }

                            pb_option1.progress = prgOne.toFloat()
                            pb_option2.progress = prgTwo.toFloat()
                            pb_option3.progress = prgThree.toFloat()

                            tv_count_1.setText("")
                            tv_count_2.setText("")
                            tv_count_3.setText("")

                            pb_option1.setProgressBackgroundColor(fragmentView.context.getResources().getColor(R.color.colorDarkBlue1))
                            pb_option2.setProgressBackgroundColor(fragmentView.context.getResources().getColor(R.color.colorDarkBlue1))
                            pb_option3.setProgressBackgroundColor(fragmentView.context.getResources().getColor(R.color.colorDarkBlue1))

                            tv_count_1.setText(" " + countUserAnswerOne + " نفر ")
                            tv_count_2.setText(" " + countUserAnswerTwo + " نفر ")
                            tv_count_3.setText(" " + countUserAnswerThree + " نفر ")
                            if (correct == selectedAnswer) {
                                if (correct == 1) {
                                    pb_option1.progressColor = fragmentView.context.getResources().getColor(R.color.colorAccent)
                                } else if (correct == 2) {
                                    pb_option2.progressColor = fragmentView.context.getResources().getColor(R.color.colorAccent)
                                } else if (correct == 3) {
                                    pb_option3.progressColor = fragmentView.context.getResources().getColor(R.color.colorAccent)
                                }
                            } else if (correct != selectedAnswer) {
                                when (correct) {
                                    1 -> consume {
                                        if (selectedAnswer == 2) {
                                            pb_option1.progressColor = fragmentView.context.getResources().getColor(R.color.colorAccent)
                                            pb_option2.progressColor = fragmentView.context.getResources().getColor(R.color.colorRed)
                                            pb_option3.progressColor = fragmentView.context.getResources().getColor(R.color.colorLight)
                                        } else if (selectedAnswer == 3) {
                                            pb_option3.progressColor = fragmentView.context.getResources().getColor(R.color.colorRed)
                                            pb_option1.progressColor = fragmentView.context.getResources().getColor(R.color.colorAccent)
                                            pb_option2.progressColor = fragmentView.context.getResources().getColor(R.color.colorLight)
                                        }
                                    }

                                    2 -> consume {
                                        if (selectedAnswer == 1) {
                                            pb_option1.progressColor = fragmentView.context.getResources().getColor(R.color.colorRed)
                                            pb_option2.progressColor = fragmentView.context.getResources().getColor(R.color.colorAccent)
                                            pb_option3.progressColor = fragmentView.context.getResources().getColor(R.color.colorLight)
                                        } else if (selectedAnswer == 3) {
                                            pb_option1.progressColor = fragmentView.context.getResources().getColor(R.color.colorLight)
                                            pb_option2.progressColor = fragmentView.context.getResources().getColor(R.color.colorAccent)
                                            pb_option3.progressColor = fragmentView.context.getResources().getColor(R.color.colorRed)
                                        }
                                    }

                                    3 -> consume {
                                        if (selectedAnswer == 1) {
                                            pb_option1.progressColor = fragmentView.context.getResources().getColor(R.color.colorRed)
                                            pb_option2.progressColor = fragmentView.context.getResources().getColor(R.color.colorLight)
                                            pb_option3.progressColor = fragmentView.context.getResources().getColor(R.color.colorAccent)
                                        } else if (selectedAnswer == 2) {
                                            pb_option1.progressColor = fragmentView.context.getResources().getColor(R.color.colorLight)
                                            pb_option2.progressColor = fragmentView.context.getResources().getColor(R.color.colorRed)
                                            pb_option3.progressColor = fragmentView.context.getResources().getColor(R.color.colorAccent)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    VasniSchema.instance.REJECT -> consume {
                        parentActivity.runOnUiThread {
                            VasniSchema.instance.show(true, pv_loading_match)
                            dialogloseMsg()
                        }
                    }
                    VasniSchema.instance.END -> consume {
                        parentActivity.runOnUiThread {
                            rl_start_match.visibility = View.INVISIBLE
                            rl_end_match.visibility = View.VISIBLE
                            rl_counter.visibility = View.INVISIBLE
                        }
                    }
                    VasniSchema.instance.WINNER -> consume {
                        parentActivity.runOnUiThread {
                            val isWin = resultJson.get("data").asJsonObject.get("winner").asBoolean
                            if (isWin) {
                                rl_start_match.visibility = View.INVISIBLE
                                rl_end_match.visibility = View.INVISIBLE
                                rl_winner_match.visibility = View.VISIBLE
                                rl_counter.visibility = View.INVISIBLE
                            } else {
                                rl_start_match.visibility = View.INVISIBLE
                                rl_end_match.visibility = View.INVISIBLE
                                rl_winner_match.visibility = View.VISIBLE
                                rl_counter.visibility = View.INVISIBLE
                                tv_lose_txt.setText("برنده به زودی اعلام میشود")
                            }
                            closeSocket()
                        }
                    }
                    VasniSchema.instance.EXIT -> consume {
                        socketReconnect = false;
                        mWebSocketClient!!.reconnect();
                    }
                    VasniSchema.instance.RESTART -> consume {
                        closeSocket()
                    }
                }
            }

            override fun onClose(i: Int, s: String, b: Boolean) {
                closeType = s
                if (closeType.contains(VasniSchema.instance.status404)) {
                    parentActivity.runOnUiThread {
                        VasniSchema.instance.show(true, pv_loading_match)
                        dialogMatchNotStart()
                    }
                } else if (closeType.contains(VasniSchema.instance.status401)) {

                } else if (socketReconnect) {
                    mWebSocketClient!!.reconnect()
                }

            }

            override fun onError(e: Exception) {
                try {
                    parentActivity.runOnUiThread {
                        VasniSchema.instance.show(false, pv_loading_match)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(
                        chain: Array<X509Certificate>, authType: String
                ) {
                }

                override fun checkServerTrusted(
                        chain: Array<X509Certificate>, authType: String
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory
            mWebSocketClient!!.setSocket(sslSocketFactory.createSocket())
            mWebSocketClient!!.connect()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }


    private fun dialogMatchNotStart() {
        BottomDialog.Builder(fragmentView.context)
                .setContent(fragmentView.context.getString(R.string.match_not_start))
                .setNegativeText(fragmentView.context.getString(R.string.ok))
                .setNegativeTextColor(ContextCompat.getColor(fragmentView.context, R.color.colorAccent))
                .autoDismiss(false)
                .setCancelable(false)
                .onNegative(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                        closeSocket()
                        finishFragment()
                    }
                })
                .onPositive(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                    }
                })
                .show()
    }

    private fun dialogloseMsg() {
        BottomDialog.Builder(fragmentView.context)
                .setContent(fragmentView.context.getString(R.string.lose_msg))
                .setNegativeText(fragmentView.context.getString(R.string.ok))
                .setNegativeTextColor(ContextCompat.getColor(fragmentView.context, R.color.colorAccent))
                .autoDismiss(false)
                .setCancelable(false)
                .onNegative(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                        finishFragment()
                    }
                })
                .onPositive(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                    }
                })
                .show()
    }

    private fun startTimer() {
        tv_time_out.visibility = View.INVISIBLE
        pb_match_online.visibility = View.VISIBLE
        pb_match_online.start()
        pb_match_online!!.setOnLoadingFinishListener(object : CircularCountDownBar.OnLoadingFinishListener {
            override fun finish() {
                try {
                    rl_option2.isClickable = false
                    rl_option1.isClickable = false
                    rl_option3.isClickable = false
                    parentActivity.runOnUiThread {
                        tv_time_out.visibility = View.VISIBLE
                        pb_match_online.visibility = View.INVISIBLE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    override fun onFragmentCreate(): Boolean {
        return super.onFragmentCreate()
    }

    override fun onFragmentDestroy() {
        try {
            socketReconnect = true
            selectedAnswer = 0
            wave_match.stop()
            closeSocket()
        } catch (e: Exception) {

        }
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
