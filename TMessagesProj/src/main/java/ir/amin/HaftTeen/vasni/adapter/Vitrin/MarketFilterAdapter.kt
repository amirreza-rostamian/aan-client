package ir.amin.HaftTeen.vasni.adapter.Vitrin

import android.support.v4.content.ContextCompat
import android.view.View
import kotlinx.android.synthetic.main.row_market_filter.*
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.PageFilter
import ir.amin.HaftTeen.R


class MarketFilterAdapter(containerView: View) : MoreViewHolder<PageFilter>(containerView) {

    override fun bindData(data: PageFilter, payloads: List<Any>) {
        tv_market_filter_title.setText(data.title)
        if (data.isSelected!!) {
            img_nav_filter.visibility = View.VISIBLE
            tv_market_filter_title.setTextColor(ContextCompat.getColor(containerView.context, R.color.colorGreen))
        } else {
            img_nav_filter.visibility = View.GONE
            tv_market_filter_title.setTextColor(ContextCompat.getColor(containerView.context, R.color.colorBlack))
        }
        addOnClickListener(tv_market_filter_title)
    }
}