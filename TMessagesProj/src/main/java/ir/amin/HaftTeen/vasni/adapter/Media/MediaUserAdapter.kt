package ir.amin.HaftTeen.vasni.adapter.Media

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.row_media_user.*
import kotlinx.android.synthetic.main.view_comment.view.*
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.VideoPlayer.Jzvd
import me.himanshusoni.chatmessageview.ui.BottomDialog
import me.himanshusoni.chatmessageview.ui.LikeView.LikeView
import me.himanshusoni.chatmessageview.ui.MoreView.MoreAdapter
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import me.himanshusoni.chatmessageview.ui.MoreView.link.RegisterItem
import me.himanshusoni.chatmessageview.ui.RtlGrid
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.vasni.adapter.CommentAdapter
import ir.amin.HaftTeen.vasni.api.AbrArvanService
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.api.RahpoService
import ir.amin.HaftTeen.vasni.extention.*
import ir.amin.HaftTeen.vasni.model.teentaak.Media
import ir.amin.HaftTeen.R


class MediaUserAdapter(containerView: View) : MoreViewHolder<Media>(containerView) {
    var file = ""
    val adapter = MoreAdapter()

    override fun bindData(data: Media, payloads: List<Any>) {
        try {
            tv_media_user_title.setText(data.title)
            tv_media_user_view_count.setText(data.views.toString() + " " + containerView.context.getString(R.string.views))
            tv_media_user_user_name.setText(data.user_name)
            tv_media_user_score.setText(data.user_score.toString() + " " + containerView.context.getString(R.string.scores))
            if (!data.user_pic.isEmpty())
                imv_media_user_avatar.loadImage(containerView.context, data.user_pic)
            else
                imv_media_user_avatar.setImageResource(R.drawable.amir)
            tv_media_user_category.setText(data.category_title)
            imv_media_user_share.setOnClickListener {
                shareLink(containerView.context, data.file)
            }
            viewPicture(data.id)
            if (data.media_type == VasniSchema.instance.MediaType_video) {
                player_media_user.visibility = View.VISIBLE
                imv_media_user.visibility = View.GONE
                player_media_user.thumbImageView.loadImage(containerView.context, data.thumbnail)

                if (data.service == VasniSchema.instance.rahpo_file_service) {
                    getVideoLinkRahpo(data.file)
                } else if (data.service == VasniSchema.instance.abr_arvan_file_service) {
                    getMediaFileAbrArvan(data.file)
                } else {
                    player_media_user.setUp(data.file, data.title, Jzvd.SCREEN_WINDOW_LIST)
                }

            } else {
                player_media_user.visibility = View.GONE
                imv_media_user.visibility = View.VISIBLE
                imv_media_user.loadImage(containerView.context, data.file, pv_loading_media_user)
            }

            imv_media_user_like.setHasLike(data.user_liked)
            imv_media_user_like.setLikeCount(data.likes)
            imv_media_user_like.setOnLikeListeners(object : LikeView.OnLikeListeners {
                override fun like(isCancel: Boolean) {
                    data.user_liked = !isCancel
                    if (isCancel) {
                        disLikeUser(data.id)
                    } else {
                        likeUser(data.id)
                    }
                }
            })

            imv_media_user_comment.setOnClickListener {
                commentDialog(containerView.context, data.id)
            }

            adapter.removeAllData()
            rc_media_user_comment.layoutManager = RtlGrid(containerView.context!!, 1)
            rc_media_user_comment.adapter = adapter
            adapter.apply {
                register(RegisterItem(R.layout.row_comment, CommentAdapter::class.java))
                startAnimPosition(1)
            }

            if (data.comments!!.size == 0) {
                VasniSchema.instance.show(false, tv_media_user_more)
            } else if (data.comments!!.size > 0) {
                adapter.loadData(data.comments!!.get(0))
                adapter.attachTo(rc_media_user_comment)
                if (data.comments!!.size == 1) {
                    VasniSchema.instance.show(false, tv_media_user_more)
                } else {
                    VasniSchema.instance.show(true, tv_media_user_more)
                    tv_media_user_more.text = containerView.context.getString(R.string.view_more_comment)
                }
            }

            tv_media_user_more.setOnClickListener {
                if (tv_media_user_more.text == containerView.context.getString(R.string.view_more_comment)) {
                    tv_media_user_more.text = containerView.context.getString(R.string.view_more_comment_close)
                    adapter.removeAllData()
                    adapter.loadData(data.comments!!)
                    adapter.attachTo(rc_media_user_comment)
                } else {
                    tv_media_user_more.text = containerView.context.getString(R.string.view_more_comment)
                    adapter.removeAllData()
                    if (data.comments!!.size > 0) {
                        adapter.loadData(data.comments!!.get(0))
                        adapter.attachTo(rc_media_user_comment)
                    }
                }
            }


        } catch (e: Exception) {

        }
    }

    fun viewPicture(id: String) {
        ApiService.apiInterface.mediaView(id).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                    } else {
                        VasniSchema.instance.showMessage(
                                containerView.context!!,
                                getError(response.body()!!).message.toString(),
                                "",
                                containerView.context!!.getString(R.string.ok)
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.showMessage(
                        containerView.context!!,
                        containerView.context!!.getString(R.string.server_error),
                        "",
                        containerView.context!!.getString(R.string.ok)
                )
            }
        })
    }

    fun getMediaFile(id: String) {
        ApiService.apiInterface.getMediaFile(id).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        file = getData(response.body()!!).get("file").asString
                        player_media_user.setUp(file, "", Jzvd.SCREEN_WINDOW_LIST)
                    } else {
                        VasniSchema.instance.showMessage(
                                containerView.context!!,
                                getError(response.body()!!).message.toString(),
                                "",
                                containerView.context!!.getString(R.string.ok)
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.showMessage(
                        containerView.context!!,
                        containerView.context!!.getString(R.string.server_error),
                        "",
                        containerView.context!!.getString(R.string.ok)
                )
            }
        })
    }

    fun likeUser(hashId: String) {
        ApiService.apiInterface.likeMedia(hashId).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                    } else {
                        VasniSchema.instance.showMessage(
                                containerView.context!!,
                                getError(response.body()!!).message.toString(),
                                "",
                                containerView.context!!.getString(R.string.ok)
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.showMessage(
                        containerView.context!!,
                        containerView.context!!.getString(R.string.server_error),
                        "",
                        containerView.context!!.getString(R.string.ok)
                )
            }
        })
    }

    fun disLikeUser(hashId: String) {
        ApiService.apiInterface.dislikeMedia(hashId).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                    } else {
                        VasniSchema.instance.showMessage(
                                containerView.context!!,
                                getError(response.body()!!).message.toString(),
                                "",
                                containerView.context!!.getString(R.string.ok)
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.showMessage(
                        containerView.context!!,
                        containerView.context!!.getString(R.string.server_error),
                        "",
                        containerView.context!!.getString(R.string.ok)
                )
            }
        })
    }

    fun getMediaFileAbrArvan(key: String) {
        AbrArvanService.apiInterface.getAbrArvanLink(
                VasniSchema.instance.getAbrArvanToken(
                        VasniSchema.instance.api_key_abr_arvan
                ), key).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    file = getData(response.body()!!).get("hls_playlist").asString
                    player_media_user.setUp(file, "", Jzvd.SCREEN_WINDOW_LIST)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.showMessage(
                        containerView.context!!,
                        containerView.context!!.getString(R.string.server_error),
                        "",
                        containerView.context!!.getString(R.string.ok)
                )
            }
        })
    }

    fun getVideoLinkRahpo(key: String) {
        RahpoService.apiInterface.getVitrinRahpoLink(
                "689725394165", key, "Free", "Free"
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        if (response.body()!!.get("error_code").asString == "0") {
                            var file = response.body()!!.get("full_addr").asString
                            player_media_user.setUp(file, "", Jzvd.SCREEN_WINDOW_LIST)
                        } else {
                            VasniSchema.instance.showMessage(
                                    containerView.context,
                                    response.body()!!.get("error_desc").asString,
                                    "",
                                    containerView.context.getString(R.string.ok)
                            )
                        }
                    } catch (e: Exception) {

                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.showMessage(
                        containerView.context,
                        containerView.context.getString(R.string.server_error),
                        "",
                        containerView.context.getString(R.string.ok)
                )
            }
        })
    }

    fun commentDialog(context: Context, id: String) {
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
                        saveComment(id, commentText)
                        dialog.dismiss()
                    }
                })
                .show()
    }

    fun saveComment(id: String, comment: String) {
        val commentId = RequestBody.create(okhttp3.MultipartBody.FORM, id)
        val commentText = RequestBody.create(okhttp3.MultipartBody.FORM, comment)
        ApiService.apiInterface.mediaComment(commentId, commentText
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        VasniSchema.instance.showMessage(
                                containerView.context!!,
                                containerView.context.getString(R.string.submit_comment_send),
                                "",
                                containerView.context.getString(R.string.ok)
                        )
                    } else {
                        VasniSchema.instance.showMessage(
                                containerView.context!!,
                                getError(response.body()!!).message.toString(),
                                "",
                                containerView.context.getString(R.string.ok)
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.showMessage(
                        containerView.context!!,
                        containerView.context!!.getString(R.string.server_error),
                        "",
                        containerView.context!!.getString(R.string.ok)
                )
            }
        })
    }

}