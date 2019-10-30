package ir.amin.HaftTeen.vasni.teentaak.fragment

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonObject
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.JTextView
import me.himanshusoni.chatmessageview.ui.MButton
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
import ir.amin.HaftTeen.vasni.extention.getData
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.EventHandler
import ir.amin.HaftTeen.vasni.model.teentaak.PlayerChart
import ir.amin.HaftTeen.R

class FounderTvFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate {

    private var txt: String = ""
    private var eventHandler = EventHandler()
    private var imageUrl: String = ""
    private lateinit var tv_total_score: MTextViewBold
    private lateinit var tv_total_chance: MTextViewBold
    private lateinit var tv_tv_founder_detail: JTextView
    private lateinit var btn_submit_tv_founder: MButton
    private lateinit var pv_loading_tv_founder: View
    private lateinit var imv_tv_founders_icon: AppCompatImageView
    private lateinit var pv_tv_founder: ProgressView

    constructor(txt: String, data: EventHandler, image: String) {
        this.txt = txt
        this.eventHandler = data
        this.imageUrl = image
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
        fragmentView = factory.inflate(R.layout.frg_tv_founder, null)
        fragmentView.setOnTouchListener { v, event -> true }

        tv_total_score = fragmentView.findViewById(R.id.tv_total_score)
        tv_total_chance = fragmentView.findViewById(R.id.tv_total_chance)
        tv_tv_founder_detail = fragmentView.findViewById(R.id.tv_tv_founder_detail)
        btn_submit_tv_founder = fragmentView.findViewById(R.id.btn_submit_tv_founder)
        pv_loading_tv_founder = fragmentView.findViewById(R.id.pv_loading_tv_founder)
        imv_tv_founders_icon = fragmentView.findViewById(R.id.imv_tv_founders_icon)
        pv_tv_founder = fragmentView.findViewById(R.id.pv_tv_founder)

        tv_tv_founder_detail.setText(context!!.getString(R.string.convert_score), true)
        imv_tv_founders_icon.loadImage(fragmentView.context!!, imageUrl, pv_tv_founder)

        getProfile()
        getUserPoint()
        btn_submit_tv_founder.setOnClickListener {
            VasniSchema.instance.show(true, pv_loading_tv_founder)
            convertScore()
        }

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

    fun getProfile() {
        ApiService.apiInterface.getSimpleUserDetail().enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        VasniSchema.instance.show(false, pv_loading_tv_founder)
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            val userDetails = Gson().fromJson(
                                    getData(response.body()!!),
                                    PlayerChart::class.java
                            )
//                            tv_total_score.setText(userDetails.userTotalRank.toString())
//                            tv_total_chance.setText(userDetails.userChance.toString())
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

    /*TODO check try catch*/
    fun convertScore() {
        try {
            ApiService.apiInterface.convertScore(eventHandler.program.toInt()
            ).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful && response.body() != null) {
                        try {
                            VasniSchema.instance.show(false, pv_loading_tv_founder)
                            if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                                getProfile()
                                getUserPoint()
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
                    VasniSchema.instance.show(false, pv_loading_tv_founder)
                    VasniSchema.instance.showMessage(
                            fragmentView.context!!,
                            fragmentView.context!!.getString(R.string.server_error),
                            "",
                            fragmentView.context!!.getString(R.string.ok)
                    )
                }
            })
        } catch (e: Exception) {

        }
    }

    /*TODO check try catch*/
    fun getUserPoint() {
        try {
            if (!eventHandler.program.isEmpty()) {
                ApiService.apiInterface.getUserPoint(eventHandler.program.toInt()
                ).enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful && response.body() != null) {
                            try {
                                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                                    tv_total_score.text = getData(response.body()!!).get("user_point").asString
                                    tv_total_chance.text = getData(response.body()!!).get("user_chance").asString
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
        } catch (e: Exception) {

        }
    }

}
