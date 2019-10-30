package ir.amin.HaftTeen.vasni.adapter.Picture

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.row_picture_user.*
import kotlinx.android.synthetic.main.view_comment.view.*
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
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
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.extention.shareLink
import ir.amin.HaftTeen.vasni.model.teentaak.MultiMedias
import ir.amin.HaftTeen.R


class PictureUserAdapter(containerView: View) : MoreViewHolder<MultiMedias>(containerView) {
    val adapter = MoreAdapter()
    override fun bindData(data: MultiMedias, payloads: List<Any>) {
        try {
            imv_pic_user.loadImage(containerView.context, data.pic, pv_loading_pic_user)
            tv_pic_user_view_count.setText(data.views.toString() + " " + containerView.context.getString(R.string.views))
            tv_pic_user_user_name.setText(data.user_name)
            tv_pic_user_score.setText(data.user_score.toString() + " " + containerView.context.getString(R.string.scores))
            if (!data.user_pic.isEmpty())
                imv_pic_user_avatar.loadImage(containerView.context, data.user_pic)
            else
                imv_pic_user_avatar.setImageResource(R.drawable.amir)
            tv_pic_user_title.setText(data.title)
            tv_pic_user_category.setText(data.category_title)
            imv_pic_user_like.setHasLike(data.is_liked)
            imv_pic_user_like.setLikeCount(data.likes)
            imv_pic_user_like.setOnLikeListeners(object : LikeView.OnLikeListeners {
                override fun like(isCancel: Boolean) {
                    data.is_liked = !isCancel
                    if (isCancel) {
                        disLikeUserPicture(data.hash_id)
                    } else {
                        likeUserPicture(data.hash_id)
                    }
                }
            })

            imv_pic_user_share.setOnClickListener {
                shareLink(containerView.context, data.url)
            }

            viewPicture(data.hash_id)

            imv_pic_user_comment.setOnClickListener {
                commentDialog(containerView.context, data.hash_id)
            }

            adapter.removeAllData()
            rc_pic_user_comment.layoutManager = RtlGrid(containerView.context!!, 1)
            rc_pic_user_comment.adapter = adapter
            adapter.apply {
                register(RegisterItem(R.layout.row_comment, CommentAdapter::class.java))
                startAnimPosition(1)
            }

            if (data.comments!!.size == 0) {
                VasniSchema.instance.show(false, tv_pic_user_more)
            } else if (data.comments!!.size > 0) {
                adapter.loadData(data.comments!!.get(0))
                adapter.attachTo(rc_pic_user_comment)
                if (data.comments!!.size == 1) {
                    VasniSchema.instance.show(false, tv_pic_user_more)
                } else {
                    VasniSchema.instance.show(true, tv_pic_user_more)
                    tv_pic_user_more.text = containerView.context.getString(R.string.view_more_comment)
                }
            }

            tv_pic_user_more.setOnClickListener {
                if (tv_pic_user_more.text == containerView.context.getString(R.string.view_more_comment)) {
                    tv_pic_user_more.text = containerView.context.getString(R.string.view_more_comment_close)
                    adapter.removeAllData()
                    adapter.loadData(data.comments!!)
                    adapter.attachTo(rc_pic_user_comment)
                } else {
                    tv_pic_user_more.text = containerView.context.getString(R.string.view_more_comment)
                    adapter.removeAllData()
                    if (data.comments!!.size > 0) {
                        adapter.loadData(data.comments!!.get(0))
                        adapter.attachTo(rc_pic_user_comment)
                    }
                }
            }


        } catch (e: Exception) {

        }
    }

    fun likeUserPicture(hashId: String) {
        ApiService.apiInterface.likeUsersPicture(hashId.toInt()).enqueue(object : Callback<JsonObject> {
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

    fun disLikeUserPicture(hashId: String) {
        ApiService.apiInterface.dislikeUsersPicture(hashId.toInt()).enqueue(object : Callback<JsonObject> {
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

    fun viewPicture(hashId: String) {
        ApiService.apiInterface.viewUsersPicture(hashId.toInt()).enqueue(object : Callback<JsonObject> {
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
        ApiService.apiInterface.pictureComment(commentId, commentText).enqueue(object : Callback<JsonObject> {
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