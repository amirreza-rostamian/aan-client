package ir.amin.HaftTeen.vasni.adapter.Media

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.ProgressView
import ir.amin.HaftTeen.messenger.ApplicationLoader
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.MediaLayout
import ir.amin.HaftTeen.R


class MediaAdapter(context: Context, private val listData: List<MediaLayout>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = convertView
        if (view == null) {
            view = LayoutInflater.from(ApplicationLoader.applicationContext).inflate(R.layout.row_media, null, false)
        }
        val item = listData[position]
        val imv_videocam = view!!.findViewById<View>(R.id.imv_videocam) as ImageView
        val imv_media = view!!.findViewById<View>(R.id.imv_media) as ImageView
        val pv_loading_media = view!!.findViewById<View>(R.id.pv_loading_media) as ProgressView
//        imv_media.loadImage(MApp.applicationContext(), item.thumbnail!!, pv_loading_media)
        if (item.media_type == VasniSchema.instance.MediaType_video) {
            imv_videocam.visibility = View.VISIBLE
            imv_media.loadImage(ApplicationLoader.applicationContext, item.thumbnail!!, pv_loading_media)
        } else {
            imv_videocam.visibility = View.GONE
            imv_media.loadImage(ApplicationLoader.applicationContext, item.file!!, pv_loading_media)
        }

        return view

    }

    override fun getItem(position: Int): Any {
        return listData.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return listData.size
    }
}
