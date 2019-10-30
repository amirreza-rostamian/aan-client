package ir.amin.HaftTeen.vasni.teentaak.fragment.poll

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.BottomDialog
import me.himanshusoni.chatmessageview.ui.MTextViewBold

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.messenger.NotificationCenter
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.vasni.Interface.OptionClickListener
import ir.amin.HaftTeen.vasni.adapter.Match.OptionAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.core.DataLoader
import ir.amin.HaftTeen.vasni.extention.getDataArray
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.model.teentaak.EventHandler
import ir.amin.HaftTeen.vasni.model.teentaak.Match
import ir.amin.HaftTeen.vasni.model.teentaak.Poll
import ir.amin.HaftTeen.vasni.model.teentaak.PollAnswer
import ir.amin.HaftTeen.R

class PollFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate, Callback<JsonObject> {

    private var txt: String = ""
    private var eventHandler = EventHandler()
    private var pollResult: ArrayList<PollAnswer> = ArrayList()
    private var adapter: OptionAdapter? = null
    private var options: ArrayList<Match> = ArrayList()
    private var pollList: List<Poll> = ArrayList()
    private lateinit var tvPollQuestion: MTextViewBold
    private lateinit var rvPollOptions: RecyclerView
    private lateinit var pv_poll_loading: View

    constructor(txt: String, Data: EventHandler) {
        this.txt = txt
        this.eventHandler = Data
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
        fragmentView = factory.inflate(R.layout.frg_poll, null)
        fragmentView.setOnTouchListener { v, event -> true }

        tvPollQuestion = fragmentView.findViewById(R.id.tvPollQuestion)
        rvPollOptions = fragmentView.findViewById(R.id.rvPollOptions)
        pv_poll_loading = fragmentView.findViewById(R.id.pv_poll_loading)


        ApiService.apiInterface.getPollList(VasniSchema.instance.getPageData(eventHandler.category)
        ).enqueue(this)

        adapter = OptionAdapter(options!!, fragmentView.context!!, object : OptionClickListener {
            override fun onItemClick(view: View, position: Int) {

                for (i in 0 until options.size) {
                    DataLoader.instance.btnOptions.get(i).setClickable(false)
                    DataLoader.instance.btnOptions.get(i).setEnabled(false)
                    DataLoader.instance.btnOptions.get(position).setBackgroundResource(R.drawable.bgr_option)
                }

                DataLoader.instance.btnOptions.get(position).setBackgroundResource(R.drawable.bgr_option_correct)
                pollResult.clear()
                pollResult.add(
                        PollAnswer(
                                options.get(position).optionId,
                                pollList.get(0).questions.get(0).question_id
                        )
                )
                if (pollResult.size != 0) {
                    VasniSchema.instance.show(true, pv_poll_loading)
                    submitPollResult()
                }
            }

            override fun onMediaClick(view: View, position: Int) {

            }
        })

        rvPollOptions.layoutManager = LinearLayoutManager(fragmentView.context)
        rvPollOptions.adapter = adapter
        adapter!!.notifyDataSetChanged()

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
                VasniSchema.instance.show(false, pv_poll_loading)
                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                    pollList = Gson().fromJson(getDataArray(response.body()!!), Array<Poll>::class.java).toList()

                    tvPollQuestion.setText(pollList.get(0).questions.get(0).ques_title)

                    options.clear()
                    var optionArray: JsonArray = pollList.get(0).questions.get(0).ques_ans!!
                    for (i in 0 until optionArray.size()) {
                        var optJs = optionArray.get(i).asJsonObject
                        var match = Match()
                        match.optionId = optJs.get("answer_id").asInt
                        match.optionTitle = optJs.get("ans_title").asString.trim()
                        options.add(match)
                    }
                    adapter!!.notifyDataSetChanged()

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

    fun submitPollResult() {
        ApiService.apiInterface.sendpollAnswer(
                DataLoader.instance.voteAnswer(pollResult)
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        VasniSchema.instance.show(false, pv_poll_loading)
                        pollResult.clear()
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            BottomDialog.Builder(fragmentView.context!!)
                                    .setContent(fragmentView.context!!.getString(R.string.submit_answer_send))
                                    .setNegativeText(fragmentView.context!!.getString(R.string.ok))
                                    .setNegativeTextColor(
                                            ContextCompat.getColor(
                                                    fragmentView.context!!,
                                                    R.color.colorAccent
                                            )
                                    )
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
                            BottomDialog.Builder(fragmentView.context!!)
                                    .setContent(getError(response.body()!!).message.toString())
                                    .setNegativeText(fragmentView.context!!.getString(R.string.ok))
                                    .setNegativeTextColor(
                                            ContextCompat.getColor(
                                                    fragmentView.context!!,
                                                    R.color.colorAccent
                                            )
                                    )
                                    .autoDismiss(false)
                                    .setCancelable(false)
                                    .onNegative(object : BottomDialog.ButtonCallback {
                                        override fun onClick(dialog: BottomDialog) {
                                            dialog.dismiss()
                                            finishFragment()
                                        }
                                    })
                                    .show()
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


}
