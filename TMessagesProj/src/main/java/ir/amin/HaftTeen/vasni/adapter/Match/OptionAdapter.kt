package ir.amin.HaftTeen.vasni.adapter.Match

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.row_option.view.*
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.VideoPlayer.Jzvd
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.vasni.Interface.OptionClickListener
import ir.amin.HaftTeen.vasni.api.AbrArvanService
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.api.RahpoService
import ir.amin.HaftTeen.vasni.core.DataLoader
import ir.amin.HaftTeen.vasni.extention.getData
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.Match
import ir.amin.HaftTeen.R


class OptionAdapter(val items: List<Match>, val context: Context, val btnlistener: OptionClickListener) :
        RecyclerView.Adapter<OptionAdapter.OptionHolder>() {

    private var mediaPlayer: MediaPlayer? = null
    private val mHandler = Handler()

    companion object {
        var mClickListener: OptionClickListener? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder {
        return OptionHolder(LayoutInflater.from(context).inflate(R.layout.row_option, parent, false))
    }

    override fun onBindViewHolder(holder: OptionHolder, position: Int) {
        mClickListener = btnlistener
        holder.titleOption.setText(items.get(position).optionTitle)

        holder.layoutOption.setBackgroundResource(R.drawable.bgr_option)
        holder.layoutOption.setOnClickListener {
            if (mClickListener != null) {
                mClickListener?.onItemClick(it, position);
            }
        }
        if (DataLoader.instance.btnOptions.size === 0)
            DataLoader.instance.btnOptions = ArrayList()
        DataLoader.instance.btnOptions.add(holder.layoutOption)

        if (items.get(position).file_type == null || items.get(position).file_type == "") {
            VasniSchema.instance.show(false, holder.imgOption)
            VasniSchema.instance.show(false, holder.loadingPic)
            VasniSchema.instance.show(false, holder.videoOption)
            VasniSchema.instance.show(false, holder.voiceOption)
        } else if (items.get(position).file_type == VasniSchema.instance.match_image_type) {
            holder.imgOption.loadImage(context!!, items.get(position).file.toString())
            VasniSchema.instance.show(true, holder.imgOption)
            VasniSchema.instance.show(true, holder.loadingPic)
            VasniSchema.instance.show(false, holder.videoOption)
            VasniSchema.instance.show(false, holder.voiceOption)
        } else if (items.get(position).file_type == VasniSchema.instance.match_video_type) {
            holder.videoOption.thumbImageView.loadImage(context, items.get(position).thumbnail!!)
            if (items.get(position).file_service == VasniSchema.instance.rahpo_file_service) {
//                getMediaFile(holder, items.get(position).file!!, items.get(position).optionTitle!!)
                getMediaFileRahpo(holder, items.get(position).file!!, items.get(position).optionTitle!!)
            } else if (items.get(position).file_service == VasniSchema.instance.abr_arvan_file_service) {
                getMediaFileAbrArvan(holder, items.get(position).file!!, items.get(position).optionTitle!!)
            } else {
                holder.videoOption.setUp(
                        items.get(position).file,
                        items.get(position).optionTitle,
                        Jzvd.SCREEN_WINDOW_LIST
                )
            }
            VasniSchema.instance.show(false, holder.imgOption)
            VasniSchema.instance.show(false, holder.loadingPic)
            VasniSchema.instance.show(true, holder.videoOption)
            VasniSchema.instance.show(false, holder.voiceOption)
        } else if (items.get(position).file_type == VasniSchema.instance.match_voice_type) {
            VasniSchema.instance.show(false, holder.imgOption)
            VasniSchema.instance.show(false, holder.loadingPic)
            VasniSchema.instance.show(false, holder.videoOption)
            VasniSchema.instance.show(true, holder.voiceOption)
            if (DataLoader.instance.voiceOptions.size === 0)
                DataLoader.instance.voiceOptions = ArrayList()
            DataLoader.instance.voiceOptions.add(holder.voiceOption)
        } else {
            VasniSchema.instance.show(false, holder.imgOption)
            VasniSchema.instance.show(false, holder.loadingPic)
            VasniSchema.instance.show(false, holder.videoOption)
            VasniSchema.instance.show(false, holder.voiceOption)
        }

        holder.voiceOption.setOnClickListener {
            if (mClickListener != null) {
                mClickListener?.onMediaClick(it, position)
            }
        }


    }

    override fun getItemCount(): Int {
        return items.size
    }

    class OptionHolder(view: View) : RecyclerView.ViewHolder(view) {
        val btnOption = view.btn_match_option
        val titleOption = view.txt_match_option_title
        val layoutOption = view.ll_match_option
        val loadingPic = view.pv_loading_pic_option
        val imgOption = view.img_match_option
        val videoOption = view.video_match_option
        val voiceOption = view.imv_voice_match_option
    }

    fun getMediaFile(holder: OptionHolder, file: String, title: String) {
        ApiService.apiInterface.getMatchMediaFile(file).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        var videoFile = getData(response.body()!!).get("file").asString
                        holder.videoOption.setUp(videoFile, title, Jzvd.SCREEN_WINDOW_LIST)
                    } else {
                        VasniSchema.instance.showMessage(
                                context!!,
                                getError(response.body()!!).message.toString(),
                                "",
                                context!!.getString(R.string.ok)
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.showMessage(
                        context!!,
                        context!!.getString(R.string.server_error),
                        "",
                        context!!.getString(R.string.ok)
                )
            }
        })
    }

    fun getMediaFileRahpo(holder: OptionHolder, file: String, title: String) {
        RahpoService.apiInterface.getVitrinRahpoLink(
                "689725394165", file, "Free", "Free"
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        var videoFile = response.body()!!.get("full_addr").asString
                        holder.videoOption.setUp(videoFile, title, Jzvd.SCREEN_WINDOW_LIST)
                    } else {
                        VasniSchema.instance.showMessage(
                                context!!,
                                getError(response.body()!!).message.toString(),
                                "",
                                context!!.getString(R.string.ok)
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.showMessage(
                        context!!,
                        context!!.getString(R.string.server_error),
                        "",
                        context!!.getString(R.string.ok)
                )
            }
        })
    }

    fun getMediaFileAbrArvan(holder: OptionHolder, file: String, title: String) {
        AbrArvanService.apiInterface.getAbrArvanLink(
                VasniSchema.instance.getAbrArvanToken(
                        VasniSchema.instance.api_key_abr_arvan
                ), file).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    var videoFile = getData(response.body()!!).get("hls_playlist").asString
                    holder.videoOption.setUp(videoFile, title, Jzvd.SCREEN_WINDOW_LIST)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.showMessage(
                        context!!,
                        context!!.getString(R.string.server_error),
                        "",
                        context!!.getString(R.string.ok)
                )
            }
        })
    }


}