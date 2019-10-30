package ir.amin.HaftTeen.vasni.core

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.support.annotation.RawRes
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.dialog_content.view.*
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.BottomDialog
import org.greenrobot.eventbus.EventBus
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import ir.amin.HaftTeen.messenger.ApplicationLoader
import ir.amin.HaftTeen.vasni.adapter.DialogContentAdapter
import ir.amin.HaftTeen.vasni.model.teentaak.*
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.*
import ir.amin.HaftTeen.BuildConfig
import ir.amin.HaftTeen.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.net.ssl.*

class DataLoader {

    val itemBack: ArrayList<EventHandler> = ArrayList()
    var tv_main_header_title: TextView? = null
    //    var btnOptions: ArrayList<Button> = ArrayList()
    var btnOptions: ArrayList<LinearLayout> = ArrayList()
    var voiceOptions: ArrayList<ImageView> = ArrayList()
    var result: JsonArray = JsonArray()

    var game = Game()
    var book = Book()
    var music = Music()
    var video = Video()
    var gameCategory = GameCategory()
    var bookCategory = BookCategory()
    var musicCategory = MusicCategory()
    var videoCategory = VideoCategory()
    var vitrinTag = Tag()

    companion object {
        private var ourInstance: DataLoader? = DataLoader()
        val instance: DataLoader
            get() {
                if (ourInstance == null) {
                    ourInstance = DataLoader()
                }
                return ourInstance!!
            }

        init {
//            System.loadLibrary("native-lib")
        }
    }

    fun getVersionName(): String {
        return ApplicationLoader.applicationContext.packageManager.getPackageInfo(ApplicationLoader.applicationContext.packageName, 0)
                .versionName
    }

    fun getVersionCode(): Int {
        return ApplicationLoader.applicationContext.packageManager.getPackageInfo(ApplicationLoader.applicationContext.packageName, 0)
                .versionCode
    }

    fun getKey(): String {
        return BuildConfig.SECURE_KEY
    }

    fun getUploadMedia(): String {
        return BuildConfig.UPLOAD_URL
    }

    fun getUploadAbrArvan(): String {
        return BuildConfig.SERVER_ABR
    }

    fun showDialog(context: Context, list: ArrayList<DialogContent>, title: String, mode: String) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customView = inflater.inflate(R.layout.dialog_content, null)
        var dialogContentList = ArrayList<DialogContent>()
        dialogContentList = list
        val dialogAdapter = DialogContentAdapter(dialogContentList, context)
        customView.rc_dialog.layoutManager = LinearLayoutManager(context)
        customView.rc_dialog.adapter = dialogAdapter
        BottomDialog.Builder(context)
                .setContent(title)
                .setCustomView(customView)
                .setPositiveText(context.getString(R.string.skip))
                .setNegativeText(context.getString(R.string.select))
                .setPositiveTextColor(ContextCompat.getColor(context, R.color.colorBlack))
                .setNegativeTextColor(ContextCompat.getColor(context, R.color.colorAccent))
                .autoDismiss(false)
                .setCancelable(false)
                .onPositive(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                    }
                })
                .onNegative(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                        try {
                            if (dialogAdapter.selectedPosition() !== -1) {
                                val Model = dialogAdapter.getSelectedItem()
                                if (mode.equals(VasniSchema.instance.gender)) {
                                    var gender: Int = 0
                                    var genderName: String = ""
                                    if (Model.id == 0) {
                                        gender = 0
                                        genderName = context.getString(R.string.gril)
                                    } else if (Model.id == 1) {
                                        gender = 1
                                        genderName = context.getString(R.string.boy)
                                    }
                                    EventBus.getDefault().post(Event(VasniSchema.instance.gender, genderName + "," + gender))
                                } else if (mode.equals(VasniSchema.instance.province)) {
                                    EventBus.getDefault().post(
                                            Event(
                                                    VasniSchema.instance.province,
                                                    Model.name + "," + Model.id + "," + Model.ostanId
                                            )
                                    )
                                } else if (mode.equals(VasniSchema.instance.grade)) {
                                    EventBus.getDefault().post(
                                            Event(
                                                    VasniSchema.instance.grade,
                                                    Model.name + "," + Model.ostanId
                                            )
                                    )
                                } else if (mode.equals(VasniSchema.instance.mediaFile)) {
                                    var fileId: Int = 0
                                    var fileType: String = ""
                                    if (Model.id == 0) {
                                        fileId = 0
                                        fileType = context.getString(R.string.picture_file)
                                    } else if (Model.id == 1) {
                                        fileId = 1
                                        fileType = context.getString(R.string.video_file)
                                    }
                                    EventBus.getDefault().post(Event(VasniSchema.instance.mediaFile, fileType + "," + fileId))
                                }
                            } else {
                                Toast.makeText(context, context.getText(R.string.dont_select), Toast.LENGTH_SHORT).show()
                            }
                            dialogContentList.clear()
                        } catch (e: Exception) {
                        }
                    }
                })
                .show()
    }

    fun getprovince(context: Context): ArrayList<DialogContent> {
        var dialogContentList = ArrayList<DialogContent>()
        try {
            var provinceJson: JSONArray = JSONObject(readRawResource(context, R.raw.province)).getJSONArray("province")
            for (i in 0 until provinceJson.length()) {
                var jo: JSONObject = provinceJson.getJSONObject(i)
                var id: Int = i
                var ostanId: Int = jo.getInt("ostan_id")
                var ostanName: String = jo.getString("ostan_name")
                dialogContentList.add(DialogContent(id, ostanName, ostanId))
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return dialogContentList

    }

    fun getGrade(context: Context): ArrayList<DialogContent> {
        var dialogContentList = ArrayList<DialogContent>()
        try {
            var provinceJson: JSONArray = JSONObject(readRawResource(context, R.raw.grade)).getJSONArray("grade")
            for (i in 0 until provinceJson.length()) {
                var jo: JSONObject = provinceJson.getJSONObject(i)
                var id: Int = i
                var ostanId: Int = jo.getInt("grade_id")
                var ostanName: String = jo.getString("grade_name")
                dialogContentList.add(DialogContent(id, ostanName, ostanId))
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return dialogContentList
    }

    fun readRawResource(context: Context, @RawRes rawRes: Int): String {
        return readStream(context.resources.openRawResource(rawRes))
    }

    fun readStream(`is`: InputStream): String {
        // http://stackoverflow.com/a/5445161
        val s = Scanner(`is`).useDelimiter("\\A")
        return if (s.hasNext()) s.next() else ""
    }

    fun getSmallBitMap(path: String, width: Int, height: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        options.inSampleSize = calculateInSampleSize(options, width, height)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(path, options)
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())

            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }

        return inSampleSize
    }

    fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap? {

        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_NORMAL -> return bitmap
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
            else -> return bitmap
        }
        try {
            val bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            bitmap.recycle()
            return bmRotated
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            return null
        }

    }

    fun copySdcard(context: Context) {
        var bm: Bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.amir);
        val extStorageDirectory = VasniSchema.instance.convertImage()
        val file = File(extStorageDirectory, "amir.png")
        val outStream = FileOutputStream(file)
        bm.compress(Bitmap.CompressFormat.PNG, 100, outStream)
        outStream.flush()
        outStream.close()
    }

    fun voteAnswer(voteAnswerArrayList: ArrayList<PollAnswer>): JsonArray {
        val arr = JsonArray()
        for (i in 0 until voteAnswerArrayList.size) {
            val ob = JsonObject()
            ob.addProperty("question_id", "" + voteAnswerArrayList[i].question_id.toString())
            ob.addProperty("answer_id", "" + voteAnswerArrayList[i].answer_id.toString())
            arr.add(ob)
        }
        return arr
    }

    fun certificate() {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}

            override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return arrayOf()
            }
        })
        var sc: SSLContext? = null
        try {
            sc = SSLContext.getInstance("SSL")
            sc!!.init(null, trustAllCerts, java.security.SecureRandom())
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(sc!!.getSocketFactory())
        val allHostsValid = object : HostnameVerifier {
            override fun verify(hostname: String, session: SSLSession): Boolean {
                return true
            }
        }
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid)
    }

    fun installApk(path: String) {
        try {
            var intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(
                    getUriFromFile(path),
                    "application/vnd.android.package-archive"
            )
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            ApplicationLoader.applicationContext.startActivity(intent);
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun instalApk(path: String, context: Context) {
        try {
            var apkfile: File = File(path)
            val intent: Intent = Intent(Intent.ACTION_VIEW)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive")
            context.startActivity(intent);
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getUriFromFile(path: String): Uri {
        if (Build.VERSION.SDK_INT < 24) {
            return Uri.fromFile(File(path));
        } else {
            return FileProvider.getUriForFile(
                    ApplicationLoader.applicationContext,
                    ApplicationLoader.applicationContext.getApplicationContext().getPackageName() + ".provider",
                    File(path)
            );
        }
    }


    fun leagueSubmit(leagueSubmit: LeagueSubmit): JsonObject {
        val ob = JsonObject()
        ob.addProperty("league_id", "" + leagueSubmit.league_id)
        ob.addProperty("score", "" + leagueSubmit.score)
        return ob
    }

    fun loadImage(
            context: Context,
            imageView: ImageView,
            url: String
    ) {
        VasniSchema.instance.certificate()

        val listener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
            ): Boolean {
                return false
            }
        }
        Glide.with(context)
                .load(url)
                .listener(listener)
                .into(imageView)
    }

    fun createVideo(title: String, description: String, file: String): JsonObject {
        val ob = JsonObject()
        ob.addProperty("title", title)
        ob.addProperty("description", description)
        ob.addProperty("file_id", file)
        ob.addProperty("convert_mode", "auto")
        return ob
    }
}