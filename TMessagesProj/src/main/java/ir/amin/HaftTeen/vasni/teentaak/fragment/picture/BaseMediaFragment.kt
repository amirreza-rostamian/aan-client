package ir.amin.HaftTeen.vasni.teentaak.fragment.picture

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonObject
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.VideoPlayer.Jzvd
import me.himanshusoni.chatmessageview.ui.MoreView.MoreAdapter
import me.himanshusoni.chatmessageview.ui.MoreView.action.MoreClickListener
import me.himanshusoni.chatmessageview.ui.MoreView.link.RegisterItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.vasni.adapter.MediaCategory.CategoryMediaAdapter
import ir.amin.HaftTeen.vasni.adapter.MediaCategory.SubCategoryAdapter
import ir.amin.HaftTeen.vasni.adapter.Picture.PictureAdminAdapter
import ir.amin.HaftTeen.vasni.adapter.Picture.PictureUserAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.extention.consume
import ir.amin.HaftTeen.vasni.extention.getDataArray
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.model.teentaak.*
import ir.amin.HaftTeen.R

class BaseMediaFragment : BaseFragment, Callback<JsonObject> {

    private var txt: String = ""
    var eventHandler = EventHandler()
    var pageType: String = ""
    var categoryList: List<Category> = ArrayList()
    private val adapter = MoreAdapter()
    private val mediaAdapter = MoreAdapter()
    var subCategoryList: ArrayList<SubCategory> = ArrayList()
    var solutionList: ArrayList<MultiMediasCategoryModel> = ArrayList()
    private lateinit var pv_media_base_loading: View
    private lateinit var rc_tab_media_Category: RecyclerView
    private lateinit var rc_cat_media: RecyclerView
    private lateinit var refresh_media: SwipeRefreshLayout
    private lateinit var rc_media: RecyclerView
    private lateinit var pv_media_loading: View
    private lateinit var empty_media_data: View

    private val catAdapter = MoreAdapter()
    private var selectSubCategoryPosition: Int = 0
    var multiMedias = MultiMediasCategoryModel()

    constructor(txt: String, data: EventHandler, type: String = "") {
        this.txt = txt
        this.eventHandler = data
        this.pageType = type
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
        fragmentView = factory.inflate(R.layout.frg_base_media, null)
        fragmentView.setOnTouchListener { v, event -> false }
        swipeBackEnabled = false

        pv_media_base_loading = fragmentView.findViewById(R.id.pv_media_base_loading)
        rc_tab_media_Category = fragmentView.findViewById(R.id.rc_tab_media_Category)
        rc_cat_media = fragmentView.findViewById(R.id.rc_cat_media)
        refresh_media = fragmentView.findViewById(R.id.refresh_media)
        rc_media = fragmentView.findViewById(R.id.rc_media)
        pv_media_loading = fragmentView.findViewById(R.id.pv_media_loading)
        empty_media_data = fragmentView.findViewById(R.id.empty_media_data)

        getPictureCategory()
        refresh_media.setOnRefreshListener {
            Jzvd.releaseAllVideos()
            adapter.removeAllData()
            refresh_media.isRefreshing = true
            getPictureCategory()
        }
        return fragmentView

    }


    override fun onFragmentCreate(): Boolean {
        return super.onFragmentCreate()
    }

    override fun onFragmentDestroy() {
        super.onFragmentDestroy()
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

    private fun getPictureCategory() {
        categoryList = emptyList()
        subCategoryList.clear()
        solutionList.clear()
        var src = "0"
        if (eventHandler.source == "1")
            src = "1"
        eventHandler.source = src
        VasniSchema.instance.show(true, pv_media_base_loading)
        ApiService.apiInterface.getPictureCategory(eventHandler.program, eventHandler.source
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        refresh_media.isRefreshing = false
                        VasniSchema.instance.show(false, pv_media_base_loading)
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            categoryList =
                                    Gson().fromJson(getDataArray(response.body()!!), Array<Category>::class.java)
                                            .toList()

                            for (i in 0 until categoryList.size) {
                                if (categoryList.get(i).childrenList.size > 0) {
                                    var chilListData: List<SubCategory> = categoryList.get(i).childrenList
                                    for (j in 0 until chilListData.size) {
                                        subCategoryList.add(
                                                SubCategory(
                                                        chilListData.get(j).id,
                                                        categoryList.get(i).id,
                                                        chilListData.get(j).name
                                                )
                                        )
                                    }
                                }
                            }

                            categoryList.forEach {
                                var tempSubCategoryList: ArrayList<SubCategory> = ArrayList()
                                if (!it.id.equals("3")) {
                                    tempSubCategoryList = getSubCategoryListByCategoryId(it.id)
                                    solutionList.add(MultiMediasCategoryModel(it, tempSubCategoryList, false))
                                }
                            }

                            configTabs()

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

    private fun getSubCategoryListByCategoryId(categoryId: String): ArrayList<SubCategory> {
        var tempSubCategoryList: ArrayList<SubCategory> = ArrayList()
        subCategoryList.forEach {
            if (it.categoryId.equals(categoryId)) {
                tempSubCategoryList.add(SubCategory(it))
            }
        }
        return tempSubCategoryList

    }

    private fun configTabs() {
        val llayoutManager = LinearLayoutManager(fragmentView.context!!)
        llayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        llayoutManager.reverseLayout = true
        rc_tab_media_Category!!.setLayoutManager(llayoutManager)
        rc_tab_media_Category!!.setHasFixedSize(true)
        adapter.apply {
            register(
                    RegisterItem(
                            R.layout.row_category_media,
                            CategoryMediaAdapter::class.java,
                            onTabClick)
            )
            startAnimPosition(1)
        }
        adapter.loadData(solutionList)
        adapter.attachTo(rc_tab_media_Category!!)

        if (solutionList.size > 0) {
            solutionList.get(0).isSelected = true
            adapter.notifyDataSetChanged()
            multiMedias = solutionList.get(0)
            initView()
        }

    }

    private val onTabClick = object : MoreClickListener() {
        override fun onItemClick(view: View, position: Int) {
            when (view.id) {
                R.id.rl_cat_media -> {
                    for (i in 0 until solutionList.size) {
                        solutionList.get(i).isSelected = false
                    }
                    solutionList.get(position).isSelected = true
                    adapter.notifyDataSetChanged()
                    multiMedias = solutionList.get(position)
                    selectSubCategoryPosition = 0
                    initView()
                }
            }
        }

        override fun onItemTouch(view: View, event: MotionEvent, position: Int): Boolean {
            return true
        }

        override fun onItemLongClick(view: View, position: Int): Boolean {
            return false
        }
    }


    private fun initView() {
        catAdapter.removeAllData()
        mediaAdapter.removeAllData()
        val layoutManager = LinearLayoutManager(fragmentView.context)
        rc_cat_media.adapter = catAdapter
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        layoutManager.reverseLayout = true
        rc_cat_media.setLayoutManager(layoutManager)
        rc_cat_media.setHasFixedSize(true)
        for (i in 0 until multiMedias.subCategoryList.size) {
            multiMedias.subCategoryList.get(i).isSelected = false
        }
        multiMedias.subCategoryList.get(selectSubCategoryPosition).isSelected = true
        rc_cat_media.setNestedScrollingEnabled(false)
        catAdapter.apply {
            register(RegisterItem(R.layout.row_sub_category, SubCategoryAdapter::class.java, subCategoryClick))
            startAnimPosition(1)
        }
        catAdapter.loadData(multiMedias.subCategoryList)
        catAdapter.attachTo(rc_cat_media)
        ApiService.apiInterface.getPicture(
                "0",
                multiMedias.subCategoryList.get(selectSubCategoryPosition).id,
                eventHandler.source,
                "100",
                "0"
        ).enqueue(this@BaseMediaFragment)

    }

    private val subCategoryClick = object : MoreClickListener() {
        override fun onItemClick(view: View, position: Int) {
            when (view.id) {
                R.id.tv_subcategory_title -> {
                    for (i in 0 until multiMedias.subCategoryList.size) {
                        multiMedias.subCategoryList.get(i).isSelected = false
                    }
                    multiMedias.subCategoryList.get(position).isSelected = true
                    catAdapter.notifyDataSetChanged()
                    Jzvd.releaseAllVideos()
                    VasniSchema.instance.show(true, pv_media_loading)
                    VasniSchema.instance.show(false, empty_media_data)
                    selectSubCategoryPosition = position
                    mediaAdapter.removeAllData()
                    ApiService.apiInterface.getPicture(
                            "0",
                            multiMedias.subCategoryList.get(selectSubCategoryPosition).id,
                            eventHandler.source,
                            "100",
                            "0"
                    ).enqueue(this@BaseMediaFragment)

                }

            }
        }

        override fun onItemTouch(view: View, event: MotionEvent, position: Int): Boolean {
            return true
        }

        override fun onItemLongClick(view: View, position: Int): Boolean {
            return false
        }
    }

    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
        if (response.isSuccessful && response.body() != null) {
            try {
                VasniSchema.instance.show(false, pv_media_loading)
                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                    VasniSchema.instance.show(true, rc_media)
                    VasniSchema.instance.show(false, empty_media_data)
                    refresh_media.isRefreshing = false
                    val mediaList: List<MultiMedias> =
                            Gson().fromJson(getDataArray(response.body()!!), Array<MultiMedias>::class.java)
                                    .toList()

                    when (eventHandler.source) {
                        VasniSchema.instance.multiMediaType_admin -> consume {
                            rc_media.layoutManager = LinearLayoutManager(fragmentView.context!!)
                            mediaAdapter.apply {
                                register(RegisterItem(R.layout.row_picture_admin, PictureAdminAdapter::class.java))
                                startAnimPosition(1)
                            }
                            mediaAdapter.loadData(mediaList)
                            mediaAdapter.attachTo(rc_media)
                        }
                        VasniSchema.instance.multiMediaType_User -> consume {
                            rc_media.layoutManager = LinearLayoutManager(fragmentView.context!!)
                            mediaAdapter.apply {
                                register(RegisterItem(R.layout.row_picture_user, PictureUserAdapter::class.java))
                                startAnimPosition(1)
                            }
                            mediaAdapter.loadData(mediaList)
                            mediaAdapter.attachTo(rc_media)
                        }
                    }


                } else {
                    VasniSchema.instance.show(true, empty_media_data)
                    VasniSchema.instance.show(false, rc_media)
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

    override fun onPause() {
        super.onPause()
        Jzvd.releaseAllVideos()
    }


}
