package ir.amin.HaftTeen.vasni.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.row_dialog.view.*
import ir.amin.HaftTeen.vasni.model.teentaak.DialogContent
import ir.amin.HaftTeen.R


class DialogContentAdapter(val items: ArrayList<DialogContent>, val context: Context) :
        RecyclerView.Adapter<ViewHolder>() {
    private var lastCheckedPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_dialog, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.tv_dialog_content?.text = items.get(position).name
        if (items.get(position).id === lastCheckedPosition) {
            holder?.rb_dialog.setChecked(true)
        } else {
            holder?.rb_dialog.setChecked(false)
        }
        holder?.rb_dialog.setOnClickListener {
            lastCheckedPosition = items.get(position).id!!
            notifyItemRangeChanged(0, items.size)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getSelectedItem(): DialogContent {
        return items.get(lastCheckedPosition)
    }

    fun selectedPosition(): Int {
        return lastCheckedPosition
    }

}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tv_dialog_content = view.tv_dialog_content
    val rb_dialog = view.rb_dialog
}
