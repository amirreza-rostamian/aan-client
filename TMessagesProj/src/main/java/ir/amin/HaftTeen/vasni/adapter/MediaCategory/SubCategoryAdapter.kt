package ir.amin.HaftTeen.vasni.adapter.MediaCategory

import android.support.v4.content.ContextCompat
import android.view.View
import kotlinx.android.synthetic.main.row_sub_category.*
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import ir.amin.HaftTeen.vasni.model.teentaak.SubCategory
import ir.amin.HaftTeen.R


class SubCategoryAdapter(containerView: View) : MoreViewHolder<SubCategory>(containerView) {

    override fun bindData(data: SubCategory, payloads: List<Any>) {
        tv_subcategory_title.setText(data.name)
        if (data.isSelected) {
            tv_subcategory_title.setBackgroundResource(R.drawable.selector_sub_category);
            tv_subcategory_title.setTextColor(ContextCompat.getColor(containerView.context, R.color.white))
        } else {
            tv_subcategory_title.setBackgroundResource(R.drawable.selector_unselected_sub_category)
            tv_subcategory_title.setTextColor(ContextCompat.getColor(containerView.context, R.color.colorBlack))
        }
        addOnClickListener(tv_subcategory_title)
    }
}