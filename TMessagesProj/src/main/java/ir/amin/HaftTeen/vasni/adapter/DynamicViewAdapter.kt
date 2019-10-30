package ir.amin.HaftTeen.vasni.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import me.himanshusoni.chatmessageview.ui.ProgressView
import ir.amin.HaftTeen.messenger.ApplicationLoader
import ir.amin.HaftTeen.vasni.extention.loadGif
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.DynamicLayout
import ir.amin.HaftTeen.R


class DynamicViewAdapter(context: Context, private val listData: ArrayList<DynamicLayout>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = convertView
        if (view == null) {
            view = LayoutInflater.from(ApplicationLoader.applicationContext).inflate(R.layout.row_dynamic_view, null, false)
        }
        try {
            val item = listData[position]
            val img_dynamic_icon = view!!.findViewById<View>(R.id.img_dynamic_icon) as ImageView
            val pv_dynamic_layer = view.findViewById<View>(R.id.pv_dynamic_layer) as ProgressView

            if (item.background.endsWith(".gif")) {
                img_dynamic_icon.loadGif(ApplicationLoader.applicationContext, item.background)
            } else {
                img_dynamic_icon.loadImage(ApplicationLoader.applicationContext, item.background, pv_dynamic_layer)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return view!!

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
