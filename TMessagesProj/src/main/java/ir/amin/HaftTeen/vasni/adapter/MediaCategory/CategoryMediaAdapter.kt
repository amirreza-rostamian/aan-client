package ir.amin.HaftTeen.vasni.adapter.MediaCategory

import android.view.View
import kotlinx.android.synthetic.main.row_category_media.*
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.MultiMediasCategoryModel


class CategoryMediaAdapter(containerView: View) : MoreViewHolder<MultiMediasCategoryModel>(containerView) {

    override fun bindData(data: MultiMediasCategoryModel, payloads: List<Any>) {
        imgCategoryIcon.loadImage(containerView.context, data.category.picture!!)
        if (data.isSelected) {
            VasniSchema.instance.show(true, img_nav_tab_media)
        } else {
            VasniSchema.instance.show(false, img_nav_tab_media)
        }
        addOnClickListener(rl_cat_media)
    }
}