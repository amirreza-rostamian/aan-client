package ir.amin.HaftTeen.vasni.adapter.Vitrin

import android.view.View
import kotlinx.android.synthetic.main.row_screenshot.*
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.ScreenShot


class GameScreenShotAdapter(containerView: View) : MoreViewHolder<ScreenShot>(containerView) {
    override fun bindData(data: ScreenShot, payloads: List<Any>) {
        try {
            imv_screenshot.loadImage(containerView.context, data.link!!, pv_screenshot)
        } catch (e: Exception) {

        }
    }

}