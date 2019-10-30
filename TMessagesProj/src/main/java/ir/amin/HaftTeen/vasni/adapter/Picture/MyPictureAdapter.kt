package ir.amin.HaftTeen.vasni.adapter.Picture

import android.support.v4.content.ContextCompat
import android.view.View
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.row_my_picture.*
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.BottomDialog
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.extention.shareLink
import ir.amin.HaftTeen.vasni.model.teentaak.MultiMedias
import ir.amin.HaftTeen.R


class MyPictureAdapter(containerView: View) : MoreViewHolder<MultiMedias>(containerView) {
    override fun bindData(data: MultiMedias, payloads: List<Any>) {
        try {
            imv_my_pic.loadImage(containerView.context, data.pic, pv_loading_my_pic)
            tv_my_pic_title.setText(data.title)
            tv_my_pic_view_count.setText(data.views.toString() + " " + containerView.context.getString(R.string.views))
            tv_my_pic_category.setText(data.category_title)
            imv_my_pic_share.setOnClickListener {
                shareLink(containerView.context, data.pic)
            }
            tv_my_pic_like.setText(data.likes.toString())

            btn_more_my_pic.setOnClickListener {
                deleteAlert(data.hash_id)
            }

        } catch (e: Exception) {

        }
    }

    fun deleteAlert(id: String) {
        BottomDialog.Builder(containerView.context!!)
                .setContent(containerView.context.getString(R.string.delete_user_pic))
                .setPositiveText(containerView.context.getString(R.string.no))
                .setNegativeText(containerView.context.getString(R.string.yes))
                .setPositiveTextColor(ContextCompat.getColor(containerView.context!!, R.color.colorBlack))
                .setNegativeTextColor(ContextCompat.getColor(containerView.context!!, R.color.colorAccent))
                .autoDismiss(false)
                .setCancelable(false)
                .onNegative(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                        deletePicture(id)
                    }
                })
                .onPositive(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                    }
                })
                .show()
    }

    fun deletePicture(id: String) {
        ApiService.apiInterface.deleteUsersPicture(id.toInt()).enqueue(object : Callback<JsonObject> {
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

}