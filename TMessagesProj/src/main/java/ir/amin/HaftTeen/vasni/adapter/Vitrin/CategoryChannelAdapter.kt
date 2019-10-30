package ir.amin.HaftTeen.vasni.adapter.Vitrin

import android.view.View
import kotlinx.android.synthetic.main.row_channel.*
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.Channel
import ir.amin.HaftTeen.R


class CategoryChannelAdapter(containerView: View) : MoreViewHolder<Channel>(containerView) {

    override fun bindData(data: Channel, payloads: List<Any>) {

        if (data.pic.equals("") || data.pic.equals(null))
            imv_channel.setImageResource(R.drawable.ic_launcher)
        else
            imv_channel.loadImage(containerView.context, data.pic!!, pv_loading_pic_channel)
        tv_channel_name.text = data.title
        addOnClickListener(ll_channel)

    }

}