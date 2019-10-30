package ir.amin.HaftTeen.vasni.teentaak.fragment.media

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonObject
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.VideoPlayer.Jzvd
import me.himanshusoni.chatmessageview.ui.MoreView.MoreAdapter
import me.himanshusoni.chatmessageview.ui.MoreView.action.MoreClickListener
import me.himanshusoni.chatmessageview.ui.MoreView.link.RegisterItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.messenger.ApplicationLoader
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.vasni.adapter.Media.MediaAdapter
import ir.amin.HaftTeen.vasni.adapter.MediaCategory.CategoryMediaAdapter
import ir.amin.HaftTeen.vasni.adapter.MediaCategory.SubCategoryAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.extention.getDataArray
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.model.teentaak.*
import ir.amin.HaftTeen.vasni.utils.grid.AsymmetricGridView
import ir.amin.HaftTeen.vasni.utils.grid.AsymmetricGridViewAdapter
import ir.amin.HaftTeen.R

class MediaBaseFragment : BaseFragment, Callback<JsonObject> {

    private var txt: String = ""
    var eventHandler = EventHandler()
    var pageType: String = ""
    var categoryList: List<Category> = ArrayList()
    private val adapter = MoreAdapter()
    var subCategoryList: ArrayList<SubCategory> = ArrayList()
    var solutionList: ArrayList<MultiMediasCategoryModel> = ArrayList()
    private lateinit var pv_media_base_loading: View
    private lateinit var rc_tab_media_Category: RecyclerView
    private lateinit var rc_cat_media: RecyclerView
    private lateinit var refresh_media: SwipeRefreshLayout
    private lateinit var gv_media: AsymmetricGridView
    private lateinit var pv_media_loading: View
    private lateinit var empty_media_data: View

    private val catAdapter = MoreAdapter()
    private var selectSubCategoryPosition: Int = 0
    var multiMedias = MultiMediasCategoryModel()
    lateinit var mediaAdapter: MediaAdapter

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
        fragmentView = factory.inflate(R.layout.frg_media_base, null)
        fragmentView.setOnTouchListener { v, event -> false }
        swipeBackEnabled = false

        pv_media_base_loading = fragmentView.findViewById(R.id.pv_media_base_loading)
        rc_tab_media_Category = fragmentView.findViewById(R.id.rc_tab_media_Category)
        rc_cat_media = fragmentView.findViewById(R.id.rc_cat_media)
        refresh_media = fragmentView.findViewById(R.id.refresh_media)
        gv_media = fragmentView.findViewById(R.id.gv_media)
        pv_media_loading = fragmentView.findViewById(R.id.pv_media_loading)
        empty_media_data = fragmentView.findViewById(R.id.empty_media_data)

        getMediaCategory()
        refresh_media.setOnRefreshListener {
            Jzvd.releaseAllVideos()
            adapter.removeAllData()
            refresh_media.isRefreshing = true
            getMediaCategory()
        }
        return fragmentView

    }


    override fun onFragmentCreate(): Boolean {
        return super.onFragmentCreate()
    }

    override fun onFragmentDestroy() {
        super.onFragmentDestroy()
    }

//    override fun didReceivedNotification(id: Int, account: Int, vararg args: Any?) {
//    }

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

    private fun getMediaCategory() {
        categoryList = emptyList()
        subCategoryList.clear()
        solutionList.clear()
        VasniSchema.instance.show(true, pv_media_base_loading)
        ApiService.apiInterface.getMediaCategory(eventHandler.program, eventHandler.source
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
        ApiService.apiInterface.getMedia(
                eventHandler.tileId,
                multiMedias.subCategoryList.get(selectSubCategoryPosition).id
        ).enqueue(this)
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

                    ApiService.apiInterface.getMedia(
                            eventHandler.tileId,
                            multiMedias.subCategoryList.get(selectSubCategoryPosition).id
                    ).enqueue(this@MediaBaseFragment)
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
                    VasniSchema.instance.show(true, gv_media)
                    VasniSchema.instance.show(false, empty_media_data)
                    refresh_media.isRefreshing = false
                    val mediaList: List<Media> =
                            Gson().fromJson(getDataArray(response.body()!!), Array<Media>::class.java)
                                    .toList()

                    counter1 = 4
                    counter2 = 8
                    swt = 0
                    index = 5
                    var noOfColumn = 3
                    val mediaLayout: ArrayList<MediaLayout> = ArrayList()
                    val items = getDataArray(response.body()!!).asJsonArray
                    for (i in 0 until items.size()) {
//                        val colSpan = if (Math.random() < 0.2f) 2 else 1
//                        val colSpan = if (i == 2 || i == 9 || i == 20 || i == 27) 2 else 1
                        val colSpan = Spanner(i)
                        var jsonObject = items.get(i).asJsonObject
                        var dynamicData = MediaLayout(
                                colSpan,
                                colSpan,
                                i,
                                jsonObject.get("id").asString,
                                jsonObject.get("thumbnail").asString,
                                jsonObject.get("media_type").asString,
                                jsonObject.get("file").asString
                        )
                        mediaLayout.add(dynamicData)
                    }
                    var adapter = MediaAdapter(fragmentView.context, mediaLayout)

                    adapter!!.notifyDataSetChanged()
                    gv_media.setRequestedColumnCount(noOfColumn)
                    gv_media.determineColumns()
                    gv_media.setRequestedHorizontalSpacing(VasniSchema.instance.dpToPx(fragmentView.context, noOfColumn.toFloat()))
                    gv_media.setDividerHeight(VasniSchema.instance.dpToPx(fragmentView.context, noOfColumn.toFloat()))
                    gv_media.adapter = AsymmetricGridViewAdapter(ApplicationLoader.applicationContext, gv_media, adapter)
                    gv_media.setPadding(
                            VasniSchema.instance.dpToPx(fragmentView.context, noOfColumn.toFloat()),
                            VasniSchema.instance.dpToPx(fragmentView.context, noOfColumn.toFloat()),
                            VasniSchema.instance.dpToPx(fragmentView.context, noOfColumn.toFloat()),
                            VasniSchema.instance.dpToPx(fragmentView.context, noOfColumn.toFloat())
                    )
                    gv_media.setOnItemClickListener(object : AdapterView.OnItemClickListener {
                        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            try {
                                presentFragment(MediaDetailFragment(eventHandler.title, eventHandler, mediaList, position))
                            } catch (e: JsonIOException) {

                            }
                        }
                    })
                } else {
                    VasniSchema.instance.show(true, empty_media_data)
                    VasniSchema.instance.show(false, gv_media)
//                    VasniSchema.instance.showMessage(
//                            fragmentView.context,
//                            getError(response.body()!!).message.toString(),
//                            "",
//                            fragmentView.context.getString(R.string.ok)
//                    )
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

    var counter1 = 4
    var counter2 = 8
    var swt = 0
    var index = 5
    fun Spanner(i: Int): Int {
        var colspan = 1
        if (i == 5) {
            colspan = 2
            return colspan
        } else if ((i - index) == counter1 && swt == 0) {
            colspan = 2
            index = i
            swt = 1
            return colspan
        } else if ((i - index) == counter2 && swt == 1) {
            colspan = 2
            index = i
            swt = 0
            return colspan
        } else {
            colspan = 1
            return colspan
        }
        return colspan
    }

}
