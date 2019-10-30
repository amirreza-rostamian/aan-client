package ir.amin.HaftTeen.vasni.extention

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.AnimRes
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.view_help.view.*
import kotlinx.android.synthetic.main.view_rating.view.*
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.BottomDialog
import me.himanshusoni.chatmessageview.ui.ProgressView
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.messenger.ApplicationLoader
import ir.amin.HaftTeen.ui.LaunchActivity.presentFragment
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.core.DataLoader
import ir.amin.HaftTeen.vasni.model.teentaak.*
import ir.amin.HaftTeen.vasni.teentaak.HomeFragment
import ir.amin.HaftTeen.vasni.teentaak.LinkFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.FounderTvFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.SendCommentFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.StaticFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.WalletFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.channel.CategoryChannelFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.market.*
import ir.amin.HaftTeen.vasni.teentaak.fragment.match.LeagueCategoryFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.match.LeagueFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.match.MatchFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.match.OnlineMatchFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.media.MediaBaseFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.media.MyMediaFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.news.NewsFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.pay.PayFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.pay.TciPayFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.picture.BaseMediaFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.picture.MyPictureFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.playerChart.ChallengeChartFragment
import ir.amin.HaftTeen.vasni.teentaak.fragment.poll.PollFragment
import ir.amin.HaftTeen.vasni.utils.MSharePk
import ir.amin.HaftTeen.R
import java.util.*


fun getTimeString(millis: Long): String {
    val buf = StringBuffer()
    val hours = millis / (1000 * 60 * 60)
    val minutes = millis % (1000 * 60 * 60) / (1000 * 60)
    val seconds = millis % (1000 * 60 * 60) % (1000 * 60) / 1000
    buf.append(String.format("%02d", hours))
            .append(":")
            .append(String.format("%02d", minutes))
            .append(":")
            .append(String.format("%02d", seconds))

    return buf.toString()
}

fun getStatus(jsonObject: JsonObject): CheckStatus {
    val check = Gson().fromJson(jsonObject, CheckStatus::class.java)
    return check
}

fun getSuccess(jsonObject: JsonObject): CheckSuccess {
    val check = Gson().fromJson(jsonObject, CheckSuccess::class.java)
    return check
}

fun getData(jsonObject: JsonObject): JsonObject {
    val data = jsonObject.get("data").asJsonObject
    return data
}

fun getDataArray(jsonObject: JsonObject): JsonArray {
    val data = Gson().fromJson(jsonObject.get("data").asJsonArray, JsonArray::class.java)
    return data
}

fun getError(jsonObject: JsonObject): CheckError {
    val checkError = Gson().fromJson(jsonObject.get("errors").asJsonObject, CheckError::class.java)
    return checkError
}

fun haveData(jsonObject: JsonObject): Boolean {
    var isChecked: Boolean? = false
    if (jsonObject.get("status").asInt == 1) {
        isChecked = true
    } else {
        isChecked = false
    }
    return isChecked
}

fun getSimpleUserStatus(context: Context) {
    ApiService.apiInterface.getSimpleUserDetail()
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful && response.body() != null) {
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            val userDetails = Gson().fromJson(getData(response.body()!!), PlayerChart::class.java)
                            if (userDetails.isActive.equals(VasniSchema.instance.active)) {
                                MSharePk.putBoolean(context, VasniSchema.instance.checkSimple, true)
                            } else {
                                MSharePk.putBoolean(context, VasniSchema.instance.checkSimple, false)
                            }
                        } else {
                            VasniSchema.instance.showMessage(
                                    context,
                                    getError(response.body()!!).message.toString(),
                                    "",
                                    context.getString(R.string.ok)
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    VasniSchema.instance.showMessage(
                            context,
                            context.getString(R.string.server_error),
                            "",
                            context.getString(R.string.ok)
                    )
                }
            })
}

fun <T> getFirst(list: ArrayList<T>?): T? {
    return if (list != null && !list.isEmpty()) list[0] else null
}

fun <T> getLast(list: ArrayList<T>?): T? {
    return if (list != null && !list.isEmpty()) list[list.size - 2] else null
}


fun showHelp(context: Context, content: String, positiveTxt: String, negetiveTxt: String) {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val customView = inflater.inflate(R.layout.view_help, null)

    customView.tv_help.setAlignment(Paint.Align.RIGHT)
    customView.tv_help.setLineSpacing(20)
    customView.tv_help.setText(content)
    customView.tv_help.setTextSize(1, 16.0f)

    var dialog: BottomDialog = BottomDialog.Builder(context)
            .setContent(context.getString(R.string.help))
            .setCustomView(customView)
            .setPositiveText(positiveTxt)
            .setNegativeText(negetiveTxt)
            .setPositiveTextColor(ContextCompat.getColor(context, R.color.colorBlack))
            .setNegativeTextColor(ContextCompat.getColor(context, R.color.colorAccent))
            .autoDismiss(false)
            .setCancelable(true)
            .onNegative(object : BottomDialog.ButtonCallback {
                override fun onClick(dialog: BottomDialog) {
                    dialog.dismiss()
                }
            })
            .onPositive(object : BottomDialog.ButtonCallback {
                override fun onClick(dialog: BottomDialog) {
                    dialog.dismiss()
                }
            })
            .show()
    customView.btn_help_ok.setOnClickListener { dialog.dismiss() }
}

fun ImageView.loadImage(
        context: Context,
        url: String,
        progressBar: ProgressBar,
        loadOnlyFromCache: Boolean = false,
        onLoadingFinished: () -> Unit = {}
) {
    VasniSchema.instance.certificate()
    val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished()
            progressBar.visibility = View.GONE
            return false
        }

        override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished()
            progressBar.visibility = View.GONE
            return false
        }
    }
    Glide.with(context)
            .load(url)
            .listener(listener)
            .into(this)
}

fun ImageView.loadImage(
        context: Context,
        url: String,
        loadOnlyFromCache: Boolean = false,
        onLoadingFinished: () -> Unit = {}
) {
    VasniSchema.instance.certificate()

    val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished()
            return false
        }

        override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished()
            return false
        }
    }
    Glide.with(context)
            .load(url)
            .listener(listener)
            .into(this)
}

fun ImageView.loadImage(
        context: Context,
        url: String,
        progressBar: ProgressView,
        loadOnlyFromCache: Boolean = false,
        onLoadingFinished: () -> Unit = {}
) {
    VasniSchema.instance.certificate()
    val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished()
            progressBar.visibility = View.GONE
            return false
        }

        override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished()
            progressBar.visibility = View.GONE
            return false
        }
    }
    Glide.with(context)
            .load(url)
            .listener(listener)
            .into(this)
}


fun ImageView.loadGif(
        context: Context,
        url: String
) {
    VasniSchema.instance.certificate()
    Glide.with(context)
            .asGif()
            .load(url)
            .into(this)
}

fun isProfileComplete(): Boolean {
    var userDetail: List<UserProfile> = ArrayList()
//    userDetail = DataLoader.instance.getProfile()
//    if (userDetail.get(0).name!!.isEmpty())
//        return false
//    if (userDetail.get(0).grade!!.isEmpty())
//        return false
//    if (userDetail.get(0).province!!.isEmpty())
//        return false
    return true
}

fun getUserPoint(context: Context, program: String) {
    if (program.isEmpty()) {
        VasniSchema.instance.score_toolbar!!.visibility = View.GONE
    } else {
        VasniSchema.instance.score_toolbar!!.visibility = View.VISIBLE
        ApiService.apiInterface.getUserPoint(program.toInt()
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {

                            VasniSchema.instance.tv_program_score!!.text =
                                    getData(response.body()!!).get("user_point").asString
                            VasniSchema.instance.tv_program_chance!!.text =
                                    getData(response.body()!!).get("user_chance").asString

                        } else {
                            VasniSchema.instance.showMessage(
                                    context!!,
                                    getError(response.body()!!).message.toString(),
                                    "",
                                    context!!.getString(R.string.ok)
                            )
                        }
                    } catch (e: Exception) {

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
}

fun consume(f: () -> Unit): Boolean {
    f()
    return true
}

fun shareLink(context: Context, link: String) {
    var intent: Intent = Intent(Intent.ACTION_SEND)
    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_TEXT, link);
    intent.putExtra(
            Intent.EXTRA_SUBJECT,
            context.getString(R.string.app_name) + " " + context.getString(R.string.splash_desc)
    )
    context.startActivity(Intent.createChooser(intent, "اشتراک گذاری با :"))
}


fun goBack() {
    if (DataLoader.instance.itemBack.size >= 2) {
        var eventDataList: ArrayList<EventHandler> = DataLoader.instance.itemBack
        var backItem = getLast(DataLoader.instance.itemBack)
        VasniSchema.instance.homePageId = backItem!!.page
        //MSharePk.putString(MApp.applicationContext(), AppSchema.instance.home_id, backItem!!.category)
        eventDataList.removeAt(DataLoader.instance.itemBack.size - 1)
        DataLoader.instance.tv_main_header_title!!.setText(backItem.title)
    }
}

fun getRandomColor(): String {
    var colors: ArrayList<String> = ArrayList()
    colors.add("#FF0E52")
    colors.add("#29C034")
    colors.add("#F39B03")
    colors.add("#00B0F2")
    return colors.get(Random().nextInt(colors.size))
}


fun addEventStack(title: String) {
    var eventData = EventHandler()
    eventData.title = title
    DataLoader.instance.itemBack.add(eventData)
    DataLoader.instance.tv_main_header_title!!.setText(title)
}

/**
 * Method to add the fragment. The [fragment] is added to the container view with id
 * [containerViewId] and a [tag]. The operation is performed by the childFragmentManager.
 * This method checks if fragment exists and it is added.
 * @return the fragment added.
 */
fun <T : Fragment> Fragment.addFragmentSafely(
        fragment: T,
        tag: String,
        allowStateLoss: Boolean = false,
        @IdRes containerViewId: Int,
        @AnimRes enterAnimation: Int = 0,
        @AnimRes exitAnimation: Int = 0,
        @AnimRes popEnterAnimation: Int = 0,
        @AnimRes popExitAnimation: Int = 0
): T {
    if (isAdded && !existsFragmentByTag(tag)) {
        val ft = childFragmentManager.beginTransaction()
        ft.setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        ft.add(containerViewId, fragment, tag)
        if (!childFragmentManager.isStateSaved) {
            ft.commit()
        } else if (allowStateLoss) {
            ft.commitAllowingStateLoss()
        }
        return fragment
    }
    return findFragmentByTag(tag) as T
}

/**
 * Method to check if fragment exists. The operation is performed by the childFragmentManager.
 */
fun Fragment.existsFragmentByTag(tag: String): Boolean {
    return childFragmentManager.findFragmentByTag(tag) != null
}

/**
 * Method to get fragment by tag. The operation is performed by the childFragmentManager.
 */
fun Fragment.findFragmentByTag(tag: String): Fragment? {
    return childFragmentManager.findFragmentByTag(tag)
}


fun Fragment.replaceFragmentSafely(
        fragment: Fragment,
        tag: String,
        allowStateLoss: Boolean = false,
        @IdRes containerViewId: Int,
        @AnimRes enterAnimation: Int = 0,
        @AnimRes exitAnimation: Int = 0,
        @AnimRes popEnterAnimation: Int = 0,
        @AnimRes popExitAnimation: Int = 0
) {
    if (isAdded) {
        val ft = childFragmentManager.beginTransaction()
        ft.setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        ft.replace(containerViewId, fragment, tag)
        if (!childFragmentManager.isStateSaved) {
            ft.commit()
        } else if (allowStateLoss) {
            ft.commitAllowingStateLoss()
        }
    }
}

fun getHumanReadableSize(context: Context, apkSize: Long): String {
    val humanReadableSize: String
    if (apkSize < 1024) {
        humanReadableSize = String.format(
                context.getString(R.string.app_size_b),
                apkSize.toDouble()
        )
    } else if (apkSize < Math.pow(1024.0, 2.0)) {
        humanReadableSize = String.format(
                context.getString(R.string.app_size_kib),
                (apkSize / 1024).toDouble()
        )
    } else if (apkSize < Math.pow(1024.0, 3.0)) {
        humanReadableSize = String.format(
                context.getString(R.string.app_size_mib),
                apkSize / Math.pow(1024.0, 2.0)
        )
    } else {
        humanReadableSize = String.format(
                context.getString(R.string.app_size_gib),
                apkSize / Math.pow(1024.0, 3.0)
        )
    }
    return humanReadableSize
}


fun ratingDialog(context: Context, id: String, type: String) {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val customView = inflater.inflate(R.layout.view_rating, null)
    var rate = "0"
    var commentText = ""

    var dialog: BottomDialog = BottomDialog.Builder(context)
            .setContent(context.getString(R.string.title_rating_dialog))
            .setCustomView(customView)
            .setNegativeText(context.getString(R.string.submit_rating_dialog))
            .setPositiveText(context.getString(R.string.permission_cancel))
            .setNegativeTextColor(ContextCompat.getColor(context, R.color.colorAccent))
            .setPositiveTextColor(ContextCompat.getColor(context, R.color.colorBlack))
            .autoDismiss(false)
            .setCancelable(true)
            .onPositive(object : BottomDialog.ButtonCallback {
                override fun onClick(dialog: BottomDialog) {
                    dialog.dismiss()
                }
            })
            .onNegative(object : BottomDialog.ButtonCallback {
                override fun onClick(dialog: BottomDialog) {
                    commentText = customView.et_comment_dialog.text.trim().toString()
                    if (rate != "0") {
                        if (type.equals(VasniSchema.instance.gameActivity))
                            ratingGame(context, id, rate)
                        else if (type.equals(VasniSchema.instance.bookActivity))
                            ratingBook(context, id, rate)
                        else if (type.equals(VasniSchema.instance.musicActivity))
                            ratingMusic(context, id, rate)
                        else if (type.equals(VasniSchema.instance.videoActivity))
                            ratingVideo(context, id, rate)
                        else if (type.equals(VasniSchema.instance.packageActivity))
                            ratingPackage(context, id, rate)
                    }
                    if (!commentText.isEmpty()) {
                        if (type.equals(VasniSchema.instance.gameActivity))
                            commentGame(context, id, commentText)
                        else if (type.equals(VasniSchema.instance.bookActivity))
                            commentBook(context, id, commentText)
                        else if (type.equals(VasniSchema.instance.musicActivity))
                            commentMusic(context, id, commentText)
                        else if (type.equals(VasniSchema.instance.videoActivity))
                            commentVideo(context, id, commentText)
                        else if (type.equals(VasniSchema.instance.packageActivity))
                            commentPackage(context, id, commentText)
                    }

                    dialog.dismiss()
                }
            })
            .show()
    customView.rb_rating_dialog.setOnRatingBarChangeListener { ratingBar, fl, b ->
        rate = fl.toInt().toString()
    }

}


private fun ratingGame(context: Context, idRate: String, valuRate: String) {
    val id = RequestBody.create(okhttp3.MultipartBody.FORM, idRate)
    val value = RequestBody.create(okhttp3.MultipartBody.FORM, valuRate)
    ApiService.apiInterface.ratingVitrinGame(id, value)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful && response.body() != null) {
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            VasniSchema.instance.showMessage(
                                    context!!,
                                    context.getString(R.string.submit_rate_send),
                                    "",
                                    context.getString(R.string.ok)
                            )
                        } else {
                            VasniSchema.instance.showMessage(
                                    context,
                                    getError(response.body()!!).message.toString(),
                                    "",
                                    context.getString(R.string.ok)
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    VasniSchema.instance.showMessage(
                            context,
                            context.getString(R.string.server_error),
                            "",
                            context.getString(R.string.ok)
                    )
                }
            })
}

private fun ratingBook(context: Context, idRate: String, valuRate: String) {
    val id = RequestBody.create(okhttp3.MultipartBody.FORM, idRate)
    val value = RequestBody.create(okhttp3.MultipartBody.FORM, valuRate)
    ApiService.apiInterface.ratingVitrinBook(id, value)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful && response.body() != null) {
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            VasniSchema.instance.showMessage(
                                    context!!,
                                    context.getString(R.string.submit_rate_send),
                                    "",
                                    context.getString(R.string.ok)
                            )
                        } else {
                            VasniSchema.instance.showMessage(
                                    context,
                                    getError(response.body()!!).message.toString(),
                                    "",
                                    context.getString(R.string.ok)
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    VasniSchema.instance.showMessage(
                            context,
                            context.getString(R.string.server_error),
                            "",
                            context.getString(R.string.ok)
                    )
                }
            })
}

private fun ratingMusic(context: Context, idRate: String, valuRate: String) {
    val id = RequestBody.create(okhttp3.MultipartBody.FORM, idRate)
    val value = RequestBody.create(okhttp3.MultipartBody.FORM, valuRate)
    ApiService.apiInterface.ratingVitrinMusic(id, value)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful && response.body() != null) {
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            VasniSchema.instance.showMessage(
                                    context!!,
                                    context.getString(R.string.submit_rate_send),
                                    "",
                                    context.getString(R.string.ok)
                            )
                        } else {
                            VasniSchema.instance.showMessage(
                                    context,
                                    getError(response.body()!!).message.toString(),
                                    "",
                                    context.getString(R.string.ok)
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    VasniSchema.instance.showMessage(
                            context,
                            context.getString(R.string.server_error),
                            "",
                            context.getString(R.string.ok)
                    )
                }
            })
}

private fun ratingVideo(context: Context, idRate: String, valuRate: String) {
    val id = RequestBody.create(okhttp3.MultipartBody.FORM, idRate)
    val value = RequestBody.create(okhttp3.MultipartBody.FORM, valuRate)
    ApiService.apiInterface.ratingVitrinVideo(id, value)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful && response.body() != null) {
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            VasniSchema.instance.showMessage(
                                    context!!,
                                    context.getString(R.string.submit_rate_send),
                                    "",
                                    context.getString(R.string.ok)
                            )
                        } else {
                            VasniSchema.instance.showMessage(
                                    context,
                                    getError(response.body()!!).message.toString(),
                                    "",
                                    context.getString(R.string.ok)
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    VasniSchema.instance.showMessage(
                            context,
                            context.getString(R.string.server_error),
                            "",
                            context.getString(R.string.ok)
                    )
                }
            })
}

private fun ratingPackage(context: Context, idRate: String, valuRate: String) {
    val id = RequestBody.create(okhttp3.MultipartBody.FORM, idRate)
    val value = RequestBody.create(okhttp3.MultipartBody.FORM, valuRate)
    ApiService.apiInterface.ratingVitrinPackage(id, value
    ).enqueue(object : Callback<JsonObject> {
        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
            if (response.isSuccessful && response.body() != null) {
                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                    VasniSchema.instance.showMessage(
                            context!!,
                            context.getString(R.string.submit_rate_send),
                            "",
                            context.getString(R.string.ok)
                    )
                } else {
                    VasniSchema.instance.showMessage(
                            context,
                            getError(response.body()!!).message.toString(),
                            "",
                            context.getString(R.string.ok)
                    )
                }
            }
        }

        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            VasniSchema.instance.showMessage(
                    context,
                    context.getString(R.string.server_error),
                    "",
                    context.getString(R.string.ok)
            )
        }
    })
}

private fun commentGame(context: Context, idComment: String, valuRate: String) {
    val id = RequestBody.create(okhttp3.MultipartBody.FORM, idComment)
    val value = RequestBody.create(okhttp3.MultipartBody.FORM, valuRate)
    ApiService.apiInterface.commentVitrinGame(id, value
    ).enqueue(object : Callback<JsonObject> {
        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
            if (response.isSuccessful && response.body() != null) {
                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {

                } else {
                    VasniSchema.instance.showMessage(
                            context,
                            getError(response.body()!!).message.toString(),
                            "",
                            context.getString(R.string.ok)
                    )
                }
            }
        }

        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            VasniSchema.instance.showMessage(
                    context,
                    context.getString(R.string.server_error),
                    "",
                    context.getString(R.string.ok)
            )
        }
    })
}


private fun commentBook(context: Context, idComment: String, valuRate: String) {
    val id = RequestBody.create(okhttp3.MultipartBody.FORM, idComment)
    val value = RequestBody.create(okhttp3.MultipartBody.FORM, valuRate)
    ApiService.apiInterface.commentVitrinBook(id, value)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful && response.body() != null) {
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            VasniSchema.instance.showMessage(
                                    context!!,
                                    context.getString(R.string.submit_comment_send),
                                    "",
                                    context.getString(R.string.ok)
                            )
                        } else {
                            VasniSchema.instance.showMessage(
                                    context,
                                    getError(response.body()!!).message.toString(),
                                    "",
                                    context.getString(R.string.ok)
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    VasniSchema.instance.showMessage(
                            context,
                            context.getString(R.string.server_error),
                            "",
                            context.getString(R.string.ok)
                    )
                }
            })
}

private fun commentMusic(context: Context, idComment: String, valuRate: String) {
    val id = RequestBody.create(okhttp3.MultipartBody.FORM, idComment)
    val value = RequestBody.create(okhttp3.MultipartBody.FORM, valuRate)
    ApiService.apiInterface.commentVitrinMusic(id, value)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful && response.body() != null) {
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            VasniSchema.instance.showMessage(
                                    context!!,
                                    context.getString(R.string.submit_comment_send),
                                    "",
                                    context.getString(R.string.ok)
                            )
                        } else {
                            VasniSchema.instance.showMessage(
                                    context,
                                    getError(response.body()!!).message.toString(),
                                    "",
                                    context.getString(R.string.ok)
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    VasniSchema.instance.showMessage(
                            context,
                            context.getString(R.string.server_error),
                            "",
                            context.getString(R.string.ok)
                    )
                }
            })
}

private fun commentVideo(context: Context, idComment: String, valuRate: String) {
    val id = RequestBody.create(okhttp3.MultipartBody.FORM, idComment)
    val value = RequestBody.create(okhttp3.MultipartBody.FORM, valuRate)
    ApiService.apiInterface.commentVitrinVideo(id, value)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful && response.body() != null) {
                        if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                            VasniSchema.instance.showMessage(
                                    context!!,
                                    context.getString(R.string.submit_comment_send),
                                    "",
                                    context.getString(R.string.ok)
                            )
                        } else {
                            VasniSchema.instance.showMessage(
                                    context,
                                    getError(response.body()!!).message.toString(),
                                    "",
                                    context.getString(R.string.ok)
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    VasniSchema.instance.showMessage(
                            context,
                            context.getString(R.string.server_error),
                            "",
                            context.getString(R.string.ok)
                    )
                }
            })
}

private fun commentPackage(context: Context, idComment: String, valuRate: String) {
    val id = RequestBody.create(okhttp3.MultipartBody.FORM, idComment)
    val value = RequestBody.create(okhttp3.MultipartBody.FORM, valuRate)
    ApiService.apiInterface.commentVitrinPackage(id, value).enqueue(object : Callback<JsonObject> {
        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
            if (response.isSuccessful && response.body() != null) {
                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                    VasniSchema.instance.showMessage(
                            context!!,
                            context.getString(R.string.submit_comment_send),
                            "",
                            context.getString(R.string.ok)
                    )
                } else {
                    VasniSchema.instance.showMessage(
                            context,
                            getError(response.body()!!).message.toString(),
                            "",
                            context.getString(R.string.ok)
                    )
                }
            }
        }

        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            VasniSchema.instance.showMessage(
                    context,
                    context.getString(R.string.server_error),
                    "",
                    context.getString(R.string.ok)
            )
        }
    })
}

fun teenTaakEventClick(dynamicLayout: DynamicLayout, activity: Context) {
    var event: String = dynamicLayout.event
    var eventData: EventHandler = Gson().fromJson(dynamicLayout.eventData, EventHandler::class.java)
    eventData.title = dynamicLayout.title
    eventData.event_name = dynamicLayout.event

    if (dynamicLayout.clickable == 1 && dynamicLayout.is_free == 1) {
        if (dynamicLayout.need_profile == 1 && !isProfileComplete()) {
            VasniSchema.instance.showMessage(
                    activity!!,
                    activity!!.getString(R.string.need_profile),
                    "",
                    activity!!.getString(R.string.ok)
            )
        } else {
            when (event) {
                VasniSchema.instance.ev_Simple -> consume {
                    presentFragment(HomeFragment(dynamicLayout.title, eventData.page, eventData.program))
                }

                VasniSchema.instance.ev_url -> consume {
                    if (eventData.type.equals(VasniSchema.instance.ev_live)) {
                        var pkg: String = ApplicationLoader.applicationContext.packageName
                        try {
                            ApplicationLoader.applicationContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pkg)))

                        } catch (e: android.content.ActivityNotFoundException) {
                            ApplicationLoader.applicationContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pkg)))
                        }
                    } else {
                        presentFragment(LinkFragment(eventData.title, eventData))
                    }
                }

                VasniSchema.instance.ev_vitrin -> consume {
                    mediaMarket(activity, eventData)
                }

                VasniSchema.instance.ev_match -> consume {
                    if (eventData.type.equals(VasniSchema.instance.category) && eventData.match_type.equals(VasniSchema.instance.match_type_league)) {
                        if (eventData.match.isEmpty()) {
                            presentFragment(LeagueCategoryFragment(eventData.title, eventData))
                        } else {
                            presentFragment(LeagueFragment(eventData.title, eventData, eventData.match))
                        }
                    } else if (eventData.match_type.equals(VasniSchema.instance.match_type_online)) {
                        presentFragment(OnlineMatchFragment(eventData.title))
                    } else {
                        if (eventData.match_type.equals(VasniSchema.instance.match_type_league)) {
                            presentFragment(LeagueFragment(eventData.title, eventData, eventData.match))
                        } else {
                            presentFragment(MatchFragment(eventData.title, eventData))
                        }
                    }
                }

                VasniSchema.instance.ev_poll -> consume {
                    presentFragment(PollFragment(eventData.title, eventData))
                }

                VasniSchema.instance.ev_News -> consume {
                    presentFragment(NewsFragment(eventData.title, eventData))
                }

                VasniSchema.instance.ev_change_chart -> consume {
                    presentFragment(ChallengeChartFragment(eventData.title, eventData))
                }

                VasniSchema.instance.ev_static_pages -> consume {
                    presentFragment(StaticFragment(eventData.title, eventData))
                }

                VasniSchema.instance.ev_contactus -> consume {
                    presentFragment(SendCommentFragment(eventData.title, eventData))
                }

                VasniSchema.instance.ev_founderstv -> consume {
                    presentFragment(FounderTvFragment(eventData.title, eventData, dynamicLayout.background))
                }

                VasniSchema.instance.ev_wallet -> consume {
                    presentFragment(WalletFragment(eventData.title))
                }

                VasniSchema.instance.ev_media -> consume {
                    if (eventData.source.equals(VasniSchema.instance.multiMediaType_User)) {
                        presentFragment(MyMediaFragment(eventData.title, eventData))
                    } else {
                        presentFragment(MediaBaseFragment(eventData.title, eventData, VasniSchema.instance.ev_media))
                    }
                }

                VasniSchema.instance.ev_channel -> consume {
                    if (eventData.type.equals(VasniSchema.instance.category)) {
                        presentFragment(CategoryChannelFragment(eventData.title, eventData.category))
                    }
                }

                VasniSchema.instance.ev_picture -> consume {
                    if (eventData.source.equals(VasniSchema.instance.multiMediaType_User)) {
                        presentFragment(MyPictureFragment(eventData.title, eventData))
                    } else {
                        presentFragment(BaseMediaFragment(eventData.title, eventData, VasniSchema.instance.ev_picture))
                    }
                }


            }
        }
    } else if (dynamicLayout.clickable == 1 && dynamicLayout.is_free == 0) {
        if (dynamicLayout.need_profile == 1 && !isProfileComplete()) {
            VasniSchema.instance.showMessage(
                    activity!!,
                    activity!!.getString(R.string.need_profile),
                    "",
                    activity!!.getString(R.string.ok)
            )
        } else {
            if (VasniSchema.instance.getSimpleStatus(activity)) {
                when (event) {
                    VasniSchema.instance.ev_Simple -> consume {
                        //                        VasniSchema.instance.program = eventData.program
                        presentFragment(HomeFragment(dynamicLayout.title, eventData.page, eventData.program))
                    }

                    VasniSchema.instance.ev_url -> consume {
                        //VasniSchema.instance.openUrlInChrome(ApplicationLoader.applicationContext, eventData.link)
                        presentFragment(LinkFragment(eventData.title, eventData))

                    }

                    VasniSchema.instance.ev_vitrin -> consume {
                        mediaMarket(activity, eventData)
                    }

                    VasniSchema.instance.ev_match -> consume {
                        if (eventData.type.equals(VasniSchema.instance.category) && eventData.match_type.equals(VasniSchema.instance.match_type_league)) {
                            if (eventData.match.isEmpty()) {
                                presentFragment(LeagueCategoryFragment(eventData.title, eventData))
                            } else {
                                presentFragment(LeagueFragment(eventData.title, eventData, eventData.match))
                            }
                        } else if (eventData.match_type.equals(VasniSchema.instance.match_type_online)) {
                            presentFragment(OnlineMatchFragment(eventData.title))
                        } else {
                            if (eventData.match_type.equals(VasniSchema.instance.match_type_league)) {
                                presentFragment(LeagueFragment(eventData.title, eventData, eventData.match))
                            } else {
                                presentFragment(MatchFragment(eventData.title, eventData))
                            }
                        }
                    }

                    VasniSchema.instance.ev_poll -> consume {
                        presentFragment(PollFragment(eventData.title, eventData))
                    }

                    VasniSchema.instance.ev_News -> consume {
                        presentFragment(NewsFragment(eventData.title, eventData))
                    }

                    VasniSchema.instance.ev_change_chart -> consume {
                        presentFragment(ChallengeChartFragment(eventData.title, eventData))
                    }

                    VasniSchema.instance.ev_static_pages -> consume {
                        presentFragment(StaticFragment(eventData.title, eventData))
                    }

                    VasniSchema.instance.ev_contactus -> consume {
                        presentFragment(SendCommentFragment(eventData.title, eventData))
                    }

                    VasniSchema.instance.ev_founderstv -> consume {
                        presentFragment(FounderTvFragment(eventData.title, eventData, dynamicLayout.background))
                    }

                    VasniSchema.instance.ev_wallet -> consume {
                        presentFragment(WalletFragment(eventData.title))
                    }

                    VasniSchema.instance.ev_media -> consume {
                        if (eventData.source.equals(VasniSchema.instance.multiMediaType_User)) {
                            presentFragment(MyMediaFragment(eventData.title, eventData))
                        } else {
                            presentFragment(MediaBaseFragment(eventData.title, eventData, VasniSchema.instance.ev_media))
                        }
                    }

                    VasniSchema.instance.ev_channel -> consume {
                        if (eventData.type.equals(VasniSchema.instance.category)) {
                            presentFragment(CategoryChannelFragment(eventData.title, eventData.category))
                        }
                    }

                    VasniSchema.instance.ev_picture -> consume {
                        if (eventData.source.equals(VasniSchema.instance.multiMediaType_User)) {
                            presentFragment(MyPictureFragment(eventData.title, eventData))
                        } else {
                            presentFragment(BaseMediaFragment(eventData.title, eventData, VasniSchema.instance.ev_picture))
                        }
                    }
                }
            } else if (VasniSchema.instance.isTci(activity)) {
                presentFragment(TciPayFragment(activity.getString(R.string.activetion_tile)))
            } else {
                presentFragment(PayFragment(activity.getString(R.string.activetion_tile)))
            }
        }
    }
}

fun mediaMarket(context: Context, eventData: EventHandler) {
    /* Game */
    if (eventData.product_type.equals(VasniSchema.instance.game_type) || eventData.product_type.equals(VasniSchema.instance.software_type)) {
        VasniSchema.instance.buy_is_visible = "1"
        if (eventData.type.equals(VasniSchema.instance.direct)) {
            DataLoader.instance.game.id = eventData.product
            presentFragment(GameDetailFragment(""))
        } else if (eventData.type.equals(VasniSchema.instance.category)) {
            VasniSchema.instance.mediaType = VasniSchema.instance.game_type
            VasniSchema.instance.game_category_id = eventData.category
            presentFragment(GameMarketFragment(eventData.title))
        }
    }
    /* Book */
    else if (eventData.product_type.equals(VasniSchema.instance.book_type)) {
        VasniSchema.instance.buy_is_visible = "1"
        if (eventData.type.equals(VasniSchema.instance.direct)) {
            DataLoader.instance.book.id = eventData.product
            presentFragment(BookDetailFragment(""))
        } else if (eventData.type.equals(VasniSchema.instance.category)) {
            VasniSchema.instance.mediaType = VasniSchema.instance.book_type
            VasniSchema.instance.book_category_id = eventData.category
            presentFragment(BookMarketFragment(eventData.title))
        }
    }
    /* Video */
    else if (eventData.product_type.equals(VasniSchema.instance.video_type)) {
        VasniSchema.instance.buy_is_visible = "1"
        if (eventData.type.equals(VasniSchema.instance.direct)) {
            DataLoader.instance.video.id = eventData.product
            presentFragment(VideoDetailFragment(""))
        } else if (eventData.type.equals(VasniSchema.instance.category)) {
            VasniSchema.instance.mediaType = VasniSchema.instance.video_type
            VasniSchema.instance.video_category_id = eventData.category
            presentFragment(VideoMarketFragment(eventData.title))
        }
    }
    /* Music */
    else if (eventData.product_type.equals(VasniSchema.instance.music_type)) {
        VasniSchema.instance.buy_is_visible = "1"
        if (eventData.type.equals(VasniSchema.instance.direct)) {
            DataLoader.instance.music.id = eventData.product
            presentFragment(MusicPlayerFragment(""))
        } else if (eventData.type.equals(VasniSchema.instance.category)) {
            VasniSchema.instance.mediaType = VasniSchema.instance.music_type
            VasniSchema.instance.music_category_id = eventData.category
            presentFragment(MusicMarketFragment(eventData.title))
        }
    }
    /* Package */
    else if (eventData.product_type.equals(VasniSchema.instance.package_type)) {
        if (eventData.type.equals(VasniSchema.instance.direct)) {
            VasniSchema.instance.buy_is_visible = "0"
            presentFragment(PackageFragment("", eventData.product))
        } else if (eventData.type.equals(VasniSchema.instance.category)) {
            VasniSchema.instance.buy_is_visible = "1"
        }
    }
}




