package ir.amin.HaftTeen.vasni.teentaak

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.RelativeLayout
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonObject
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.MTextViewBold
import org.json.JSONException


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.messenger.ApplicationLoader
import ir.amin.HaftTeen.messenger.MessagesController
import ir.amin.HaftTeen.messenger.NotificationCenter
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.vasni.adapter.DynamicViewAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.extention.*
import ir.amin.HaftTeen.vasni.model.teentaak.*
import ir.amin.HaftTeen.vasni.utils.Function
import ir.amin.HaftTeen.vasni.utils.grid.AsymmetricGridView
import ir.amin.HaftTeen.vasni.utils.grid.AsymmetricGridViewAdapter
import ir.amin.HaftTeen.BuildConfig
import ir.amin.HaftTeen.R

class HomeFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate, Callback<JsonObject> {

    private var txt: String = ""
    private var id: Int = 0
    private var program: String = ""
    private var adapter: DynamicViewAdapter? = null
    private var noOfColumn: Int = 1
    private val itemModels: ArrayList<DynamicLayout> = ArrayList()
    private var refresh_my_home_page: SwipeRefreshLayout? = null
    private var gv_main: AsymmetricGridView? = null
    private var pv_loading_main: View? = null
    private var tv_program_score: MTextViewBold? = null
    private var rl_score_program: RelativeLayout? = null

    constructor(txt: String, data: Int, program: String) {
        this.txt = txt
        this.id = data
        this.program = program
    }

    override fun createView(context: Context?): View {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back)
        actionBar.setAllowOverlayTitle(true)
        actionBar.setTitle(txt)
        actionBar.setActionBarMenuOnItemClick(object : ActionBar.ActionBarMenuOnItemClick() {
            override fun onItemClick(id: Int) {
                if (id == -1) {
                    VasniSchema.instance.show(false, rl_score_program!!)
                    finishFragment()
                }
            }
        })
        val factory = LayoutInflater.from(context)
        fragmentView = factory.inflate(R.layout.frg_home, null)
        fragmentView.setOnTouchListener { v, event -> true }

        refresh_my_home_page = fragmentView.findViewById(R.id.refresh_my_home_page)
        gv_main = fragmentView.findViewById(R.id.gv_main)
        pv_loading_main = fragmentView.findViewById(R.id.pv_loading_main)
        tv_program_score = fragmentView.findViewById(R.id.tv_program_score)
        rl_score_program = fragmentView.findViewById(R.id.rl_score_program)

        adapter = DynamicViewAdapter(context!!, itemModels)
        getSetting()
        getProfile()
        VasniSchema.instance.show(false, rl_score_program!!)
        getUserPoint(context!!, program)

        ApiService.apiInterface.getPages(id).enqueue(this)

        refresh_my_home_page!!.setOnRefreshListener {
            itemModels.clear()
            refresh_my_home_page!!.isRefreshing = true
            ApiService.apiInterface.getPages(
                    id
            ).enqueue(this)
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

    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
        if (response.isSuccessful && response.body() != null) {
            if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                setData(response.body()!!)
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
        } catch (e: Exception
        ) {
            e.printStackTrace()
        }
    }

    private fun setData(results: JsonObject) {
        try {
            refresh_my_home_page!!.isRefreshing = false
            val jsonData = results.get("data").asJsonObject
            noOfColumn = jsonData.get("columns").asInt
            VasniSchema.instance.faq = jsonData.get("faq").asString
            itemModels.clear()
            val items = jsonData.get("items").asJsonArray
            for (i in 0 until items.size()) {
                var jsonObject = items.get(i).asJsonObject
                var dynamicData = DynamicLayout(
                        jsonObject.get("columnSpan").asInt,
                        jsonObject.get("rowSpan").asInt,
                        jsonObject.get("position").asInt,
                        jsonObject.get("background").asString,
                        noOfColumn,
                        jsonData.get("rows").asInt,
                        jsonObject.get("itemId").asInt,
                        jsonObject.get("event").asString,
                        jsonObject.get("eventData").asJsonObject.toString(),
                        jsonObject.get("clickable").asInt,
                        jsonObject.get("need_profile").asInt,
                        jsonObject.get("is_free").asInt,
                        jsonObject.get("title").asString,
                        jsonData.get("faq").toString()
                )
                itemModels.add(dynamicData)
            }
            adapter!!.notifyDataSetChanged()
            gv_main!!.setRequestedColumnCount(noOfColumn)
            gv_main!!.determineColumns();
            gv_main!!.setRequestedHorizontalSpacing(VasniSchema.instance.dpToPx(fragmentView.context!!, noOfColumn.toFloat()))
            gv_main!!.setDividerHeight(VasniSchema.instance.dpToPx(fragmentView.context!!, noOfColumn.toFloat()))
            gv_main!!.adapter = AsymmetricGridViewAdapter(ApplicationLoader.applicationContext, gv_main, adapter)
            gv_main!!.setPadding(
                    VasniSchema.instance.dpToPx(fragmentView.context!!, noOfColumn.toFloat()),
                    VasniSchema.instance.dpToPx(fragmentView.context!!, noOfColumn.toFloat()),
                    VasniSchema.instance.dpToPx(fragmentView.context!!, noOfColumn.toFloat()),
                    VasniSchema.instance.dpToPx(fragmentView.context!!, noOfColumn.toFloat())
            )
            VasniSchema.instance.show(false, pv_loading_main!!)
            gv_main!!.setOnItemClickListener(object : AdapterView.OnItemClickListener {
                override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    try {
                        var dynamicLayout: DynamicLayout = itemModels.get(position)
                        var event: String = dynamicLayout.event
                        var eventData: EventHandler = Gson().fromJson(dynamicLayout.eventData, EventHandler::class.java)
                        if (event.equals(VasniSchema.instance.ev_channel) && eventData.type.equals(VasniSchema.instance.direct)) {
                            getChannelInfo(eventData.channel)
//                            MessagesController.getInstance(currentAccount).openByUserName("7teenbot", this@HomeFragment, 0);
                        } else {
                            teenTaakEventClick(dynamicLayout, fragmentView.context!!)
                        }
                    } catch (e: JsonIOException) {

                    }
                }
            })

        } catch (e: JSONException) {

        }
    }

    fun getChannelInfo(id: String) {
        VasniSchema.instance.show(true, pv_loading_main!!)
        ApiService.apiInterface.getChannelInfo(id).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        VasniSchema.instance.show(false, pv_loading_main!!)
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            val channelList: List<Channel> =
                                    Gson().fromJson(
                                            getDataArray(response.body()!!),
                                            Array<Channel>::class.java
                                    ).toList()
                            MessagesController.getInstance(currentAccount).openByBotName(channelList.get(0).channelId, this@HomeFragment, 0)
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

    fun getProfile() {
        ApiService.apiInterface.getSimpleUserDetail().enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    if (response.isSuccessful && response.body() != null) {
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            var userProfileList = ArrayList<UserProfile>()
                            val profile = Gson().fromJson(getData(response.body()!!), UserProfile::class.java)
                            userProfileList.add(profile)
                            Function.setUserId(parentActivity, "" + profile.id.toString())
                        } else {
                            VasniSchema.instance.showMessage(
                                    fragmentView.context,
                                    getError(response.body()!!).message.toString(),
                                    "",
                                    fragmentView.context.getString(R.string.ok)
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
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

    private fun getSetting() {
        ApiService.apiInterface.getSetting(BuildConfig.APP_ID).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        var setting = Gson().fromJson(getData(response.body()!!), Setting::class.java)
                        VasniSchema.instance.appGrade = setting.has_class!!
                        VasniSchema.instance.video_service = setting.video!!
                        VasniSchema.instance.audio_service = setting.audio!!
                        VasniSchema.instance.api_key_abr_arvan = setting.api_key!!
                        VasniSchema.instance.channel_id_abr_arvan = setting.channel_id!!

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

    fun getUserPoint(context: Context, program: String) {
        if (!program.isEmpty()) {
            ApiService.apiInterface.getUserPoint(program.toInt()).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful && response.body() != null) {
                        try {
                            if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                                tv_program_score!!.text = getData(response.body()!!).get("user_point").asString
                                VasniSchema.instance.show(true, rl_score_program!!)
                            } else {
                                VasniSchema.instance.showMessage(
                                        context,
                                        getError(response.body()!!).message.toString(),
                                        "",
                                        context.getString(R.string.ok)
                                )
                            }
                        } catch (e: Exception) {

                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    try {
                        VasniSchema.instance.showMessage(
                                fragmentView.context,
                                fragmentView.context.getString(R.string.server_error),
                                "",
                                fragmentView.context.getString(R.string.ok)
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
        }
    }

}
