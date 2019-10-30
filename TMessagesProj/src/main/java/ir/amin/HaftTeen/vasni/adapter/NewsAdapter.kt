package ir.amin.HaftTeen.vasni.adapter

import android.text.Html
import android.view.View
import kotlinx.android.synthetic.main.row_news.*
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.News

class NewsAdapter(containerView: View) : MoreViewHolder<News>(containerView) {

    override fun bindData(data: News, payloads: List<Any>) {
        tv_news_title.setText(data.title)
        tv_news_desc.setText(Html.fromHtml(data.content))
        imv_news_icon.loadImage(containerView.context, data.pic!!, pv_news_content)
        addOnClickListener(rl_news_row)
    }

}