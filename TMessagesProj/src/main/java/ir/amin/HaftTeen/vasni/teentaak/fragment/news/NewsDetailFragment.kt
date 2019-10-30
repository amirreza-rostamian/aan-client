package ir.amin.HaftTeen.vasni.teentaak.fragment.news

import android.content.Context
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.view_comment.view.*
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.*
import me.himanshusoni.chatmessageview.ui.LikeView.LikeView
import me.himanshusoni.chatmessageview.ui.MoreView.MoreAdapter
import me.himanshusoni.chatmessageview.ui.MoreView.link.RegisterItem
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.messenger.NotificationCenter
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.vasni.adapter.CommentAdapter
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.extention.shareLink
import ir.amin.HaftTeen.vasni.model.teentaak.News
import ir.amin.HaftTeen.R

class NewsDetailFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate {

    private var txt: String = ""
    private var newsData = News()
    private lateinit var imv_news_detail_header_bgr: ImageView
    private lateinit var pv_news_detail: ProgressView
    private lateinit var tv_news_detail_desc: JustifiedTextView
    private lateinit var imv_news_like: LikeView
    private lateinit var imv_news_share: AppCompatImageView
    private lateinit var tv_news_view_count: MTextView
    private lateinit var rc_news_comment: RecyclerView
    private lateinit var imv_news_comment: AppCompatImageView
    private val adapter = MoreAdapter()


    constructor(txt: String, data: News) {
        this.txt = txt
        this.newsData = data
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
        fragmentView = factory.inflate(R.layout.frg_news_detail, null)
        fragmentView.setOnTouchListener { v, event -> true }

        imv_news_detail_header_bgr = fragmentView.findViewById(R.id.imv_news_detail_header_bgr)
        pv_news_detail = fragmentView.findViewById(R.id.pv_news_detail)
        tv_news_detail_desc = fragmentView.findViewById(R.id.tv_news_detail_desc)
        imv_news_like = fragmentView.findViewById(R.id.imv_news_like)
        rc_news_comment = fragmentView.findViewById(R.id.rc_news_comment)
        imv_news_comment = fragmentView.findViewById(R.id.imv_news_comment)
        imv_news_share = fragmentView.findViewById(R.id.imv_news_share)

        imv_news_detail_header_bgr.loadImage(fragmentView.context!!, newsData.pic!!, pv_news_detail)

        var text = Html.fromHtml(newsData.content.toString()).toString()
        tv_news_detail_desc.setAlignment(Paint.Align.RIGHT)
        tv_news_detail_desc.setLineSpacing(20)
        tv_news_detail_desc.setText(text)
        tv_news_detail_desc.setTextSize(1, 16.0f)

        imv_news_share.setOnClickListener {
            shareLink(fragmentView.context, tv_news_detail_desc.text.toString().trim())
        }

        imv_news_comment.setOnClickListener {
            commentDialog(fragmentView.context)
        }

        rc_news_comment.layoutManager = RtlGrid(fragmentView.context!!, 1)
        rc_news_comment.adapter = adapter
        adapter.apply {
            register(RegisterItem(R.layout.row_comment, CommentAdapter::class.java))
            startAnimPosition(1)
        }
        adapter.loadData(newsData.comments!!)
        adapter.attachTo(rc_news_comment)


        return fragmentView

    }


    fun commentDialog(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customView = inflater.inflate(R.layout.view_comment, null)
        var commentText = ""
        var dialog: BottomDialog = BottomDialog.Builder(context)
                .setContent(context.getString(R.string.title_comment_dialog))
                .setCustomView(customView)
                .setNegativeText(context.getString(R.string.submit_rating_dialog))
                .setPositiveText(context.getString(R.string.permission_cancel))
                .setNegativeTextColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setPositiveTextColor(ContextCompat.getColor(context, R.color.colorBlack))
                .autoDismiss(false)
                .setCancelable(true)
                .onPositive(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                    }
                })
                .onNegative(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        commentText = customView.et_comment_dialog.text.trim().toString()
                        saveComment(newsData.id!!, commentText)
                        dialog.dismiss()
                    }
                })
                .show()
    }

    fun saveComment(id: String, comment: String) {
        val commentId = RequestBody.create(okhttp3.MultipartBody.FORM, id)
        val commentText = RequestBody.create(okhttp3.MultipartBody.FORM, comment)
        ApiService.apiInterface.newsComment(commentId, commentText
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        VasniSchema.instance.showMessage(
                                fragmentView.context,
                                fragmentView.context.getString(R.string.submit_comment_send),
                                "",
                                fragmentView.context.getString(R.string.ok)
                        )
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
