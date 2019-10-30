package me.himanshusoni.chatmessageview.Vasni

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.NonNull
import android.support.annotation.RawRes
import android.support.v4.content.ContextCompat
import android.util.Base64
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.*
import me.himanshusoni.chatmessageview.R
import me.himanshusoni.chatmessageview.Vasni.Core.MSharePk
import me.himanshusoni.chatmessageview.Vasni.StackFragment.FragNavController
import me.himanshusoni.chatmessageview.ui.Bottom.BottomBar
import me.himanshusoni.chatmessageview.ui.BottomDialog
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.InputStream
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.net.ssl.*

class VasniSchema {
    var appicationName = "HaftTeen"
    var FONT_PATH = "Fonts/sh_normal.ttf"
    var FONT_PATH_BOLD = "Fonts/sh_bold.ttf"
    val randoms = "qwertyuioplkjhgfdsazxcvbnm1234567890"
    val appName: String = "HaftTeen"
    val mobile: String = "mobile"
    val activeCode: String = "activeCode"
    val isNewRecord: String = "isNewRecord"
    val reciveCode: String = "200002020"
    val reciveCode2: String = "3000846101"
    val isTci: String = "isTci"
    var et_active_code: EditText? = null
    var active: String? = "active"
    var inactive: String? = "inactive"
    var pending: String? = "pending"
    var checkTci: String = "checkTci"
    var province = "province"
    val appFolder: String = "/HaftTeen"/*TakTeen  DigiBoom  SchoolJam TiTi KidToon HaftTeen HaftTeen_Test*/
    val convertImage: String = "/HaftTeen/Image/"
    val downloadAppFolder: String = "/HaftTeen/Apps/"
    val downloadVersionFolder: String = "/HaftTeen/Apps/Version/"
    var convertVideo: String = "/HaftTeen/Video/"
    var vitrinApps: String = "/HaftTeen/Vitrin/Apps/"
    var vitrinBooks: String = "/HaftTeen/Vitrin/Books/"
    //Messenger Path
    var VideoPath :String = "Video"
    var MusicPath :String = "Audio"
    var ImagePath :String = "Images"
    var DocumentPath:String = "Documents"
    var gender = "gender"
    var mediaFile = "mediaFile"
    var index_setting = FragNavController.TAB1
    var index_home = FragNavController.TAB2
    var index_media = FragNavController.TAB3
    var grade: String = ""
    var ev_live: String = "liveStreamVideo"
    // version app
    var App: String = "44"
    //  app grade 0 or 1 for questions, 0 for digiboom
    var appGrade: Int = 0
    var img: String? = "pic"
    var provinceId: String = "provinceId"
    var LoginCompeleted: String = "loginCompeleted"
    var waitingFragment: String = "waitingFragment"
    var loginStatus: String = "loginStatus"
    var addAccount: String = "addAccount"
    var width: Int = 0
    var height: Int = 0
    var statusOk: Int = 1
    var ev_show_messenger: String = "messenger"
    var success: Int = 1
    var statusError: Int = 0
    //    var videoLimitSize: Int = 17988946
    var videoLimitSize: Int = 41943040
    var imv_home_notification: ImageView? = null
    var imv_home_back: ImageView? = null
    var imv_main_help: ImageView? = null
    var imv_main_search: ImageView? = null
    //var home_id: String = ""
    var ev_Simple: String = "simple"
    var ev_News: String = "news"
    var ev_video: String = "videos"
    var ev_picture: String = "pictures"
    var ev_media: String = "media"
    var ev_channel: String = "channel"
    var multiMediaType_User: String = "0"
    var multiMediaType_admin: String = "1"
    var multiMediaType_users: String = "2"
    var multiMediaType_all: String = "3"
    var MediaType_all: String = "0"
    var MediaType_picture: String = "1"
    var MediaType_video: String = "2"
    var MediaType_voice: String = "3"
    var ev_match: String = "match"
    var ev_change_chart: String = "competition"
    var gradeId: String = "gradeId"
    var playerMore: String = "playerMore"
    var genderId: String = "genderId"
    var birthday: String = "birthday"
    var news_detail: String = "news_detail"
    var user_profile: String = "user_profile"
    var league_detail: String = "league_detail"
    var games_detail: String = "games_detail"
    var ev_msg: String = ""
    var ev_url: String = "link"
    var ev_contactus: String = "complains"
    var ev_telegram: String = "telegram"
    var ev_browser: String = "browser"
    var ev_founderstv: String = "lottery"
    var ev_poll: String = "polls"
    var ev_keyword: String = "keyword"
    var ev_vitrin: String = "vitrine"
    var ev_static_pages: String = "static_pages"
    var ev_wallet: String = "wallet"
    var checkSimple: String = "checkSimple"
    var homePageId: Int = 0
    var program: String = ""
    var faq: String? = ""
    var showProfileDialog: String = "profileDialog"
    var gameActivity: String = "gameActivity"
    var bookActivity: String = "bookActivity"
    var musicActivity: String = "musicActivity"
    var videoActivity: String = "videoActivity"
    var packageActivity: String = "packageActivity"
    var score_toolbar: RelativeLayout? = null
    var tv_program_score: TextView? = null
    var tv_program_chance: TextView? = null
    var tagType: String? = "1"
    var bannerType: String? = "2"
    var bannerView: String? = "1"
    var multiBannerView: String? = "2"
    var horizentalView: String? = "3"
    var verticalView: String? = "4"
    var direct: String? = "direct"
    var category: String? = "cat"
    var game_type: String? = "1"
    var book_type: String? = "2"
    var video_type: String? = "3"
    var music_type: String? = "4"
    var software_type: String? = "5"
    var package_type: String? = "7"
    var bottomBar: BottomBar? = null
    var mediaType: String? = "1" // "3"
    var match_type_league: String? = "3"
    var match_type_online: String? = "4"
    var internal_file_service: String? = "1"
    var rahpo_file_service: String? = "3"
    var abr_arvan_file_service: String? = "5"
    var streaming_file_service: String? = "4"
    var all: String? = "all"
    var game_category_id: String? = all
    var book_category_id: String? = all
    var video_category_id: String? = all
    var music_category_id: String? = all
    var showLoading = "showLoading"
    var hideLoading = "hideLoading"
    var refresh = "refresh"
    var competitionTable = "competitionTable"
    var scoreBoard = "scoreBoard"
    var match_image_type: String? = "1"
    var match_video_type: String? = "2"
    var match_voice_type: String? = "3"
    var CONNECT: String = "connect"
    var QUESTION: String = "question"
    var ANSWER: String = "answer"
    var REJECT: String = "reject"
    var END: String = "end"
    var WINNER: String = "winner"
    var EXIT: String = "exit"
    var RESTART: String = "restart"
    var status404: String = "Invalid status code received: 404 Status line: HTTP/1.1 404 Not Found"
    var status401: String = "Invalid status code received: 401 Status line: HTTP/1.1 401 Unauthorized"
    var android_format: Int = 2
    var html_game_format: Int = 5
    var match_option_answer: String? = "1"
    var match_text_answer: String? = "2"
    var vitrin_game: String? = "1"
    var vitrin_book: String? = "2"
    var vitrin_video: String? = "3"
    var vitrin_music: String? = "4"
    var vitrin_package: String? = "7"
    var vitrin_character: String? = "8"
    var vitrin_story: String? = "9"
    var vitrin_search_type: String? = "1"
    var vitrin_is_banner: String? = "0"
    var buy_is_visible: String = "1"
    var leagueId = "0"
    var video_service: String = "1"
    var audio_service: String = "1"
    var video_service_rahpo: String = "1"
    var video_service_abr_arvan: String = "2"
    var video_service_internal: String = "4"
    var api_key_abr_arvan: String = ""
    var channel_id_abr_arvan: String = ""
    // Dialog Type
    var chat: String = "chat"
    var service: String = "service"
    var profile: String = "profile"


    @Suppress("DEPRECATION")
    val deviceID: String
        get() {
            val m_szDevIDShort =
                    "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10
            var serial: String?
            try {
                serial = Build::class.java.getField("SERIAL").get(null).toString()
                return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
            } catch (exception: Exception) {
                serial = "serial"
            }

            return UUID(m_szDevIDShort.hashCode().toLong(), serial!!.hashCode().toLong()).toString()
        }


    fun Base64(value: String): String {
        val encodeValue = Base64.encode(value.toByteArray(), Base64.NO_WRAP)
        return String(encodeValue)
    }

    fun formatIndex(position: Int): String {
        return if (position < 10) "" + position else position.toString() + ""
    }

    @Throws(Exception::class)
    fun encrypt(key: String): String {
        val plainText = deviceID.trim { it <= ' ' } + randomString(32)
        val skeySpec = SecretKeySpec(key.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
        val encrypted = cipher.doFinal(plainText.toByteArray())
        return Base64.encodeToString(encrypted, Base64.NO_WRAP)
    }

    @Throws(Exception::class)
    fun decrypt(textToDecrypt: String, key: String): String {
        val skeySpec = SecretKeySpec(key.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, skeySpec)
        val decrypted = cipher.doFinal(Base64.decode(textToDecrypt, Base64.NO_WRAP))
        return decrypted.toString(charset("UTF_8"))
    }


    fun randomString(length: Int): String {
        val rand = Random()
        val buf = StringBuilder()
        for (i in 0 until length) {
            buf.append(randoms[rand.nextInt(randoms.length)])
        }
        return buf.toString()
    }

    @Throws(Exception::class)
    fun encryptMobile(key: String, mobile: String): String {
        val plainText = mobile + randomString(32)
        val skeySpec = SecretKeySpec(key.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
        val encrypted = cipher.doFinal(plainText.toByteArray())
        return Base64.encodeToString(encrypted, Base64.NO_WRAP)
    }

    @Throws(Exception::class)
    fun getuserToken(mobile: String, key: String): Map<String, String> {
        val map = HashMap<String, String>()
        map.put("User-Token", encrypt(key))
        map.put("Device-Id", deviceID)
        map.put("Mobile", encryptMobile(key, mobile))
        map.put("App", App)
        return map
    }

    @Throws(Exception::class)
    fun getAbrArvanToken(apiKey: String): Map<String, String> {
        val map = HashMap<String, String>()
        map.put("Authorization", apiKey)
        map.put("Accept-Language", "en")
        map.put("Accept", "application/json")
        map.put("Content-Type", "application/json")
        return map
    }


    fun getFont(context: Context): Typeface {
        return Typeface.createFromAsset(context.getAssets(), VasniSchema.instance.FONT_PATH)
    }

    fun getBoldFont(context: Context): Typeface {
        return Typeface.createFromAsset(context.getAssets(), VasniSchema.instance.FONT_PATH_BOLD)
    }

    fun dpToPixels(context: Context, dp: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    fun createButtonBackgroundDrawable(@NonNull context: Context, fillColor: Int): Drawable {
        val buttonCornerRadius = dpToPixels(context, 2)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val v = TypedValue()
            val hasAttribute = context.theme.resolveAttribute(R.attr.colorControlHighlight, v, true)
            val rippleColor = if (hasAttribute) v.data else Color.parseColor("#88CCCCCC")
            return createButtonBackgroundDrawableLollipop(fillColor, rippleColor, buttonCornerRadius)
        }
        return createButtonBackgroundDrawableBase(fillColor, buttonCornerRadius)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun createButtonBackgroundDrawableLollipop(fillColor: Int, rippleColor: Int, cornerRadius: Int): Drawable {
        val d = createButtonBackgroundDrawableBase(fillColor, cornerRadius)
        return RippleDrawable(ColorStateList.valueOf(rippleColor), d, null)
    }

    fun createButtonBackgroundDrawableBase(color: Int, cornerRadius: Int): Drawable {
        val d = GradientDrawable()
        d.shape = GradientDrawable.RECTANGLE
        d.cornerRadius = cornerRadius.toFloat()
        d.setColor(color)
        return d
    }

    fun showMessage(context: Context, content: String, positiveTxt: String, negetiveTxt: String) {
        try {
            BottomDialog.Builder(context)
                    .setContent(content)
                    .setPositiveText(positiveTxt)
                    .setNegativeText(negetiveTxt)
                    .setPositiveTextColor(ContextCompat.getColor(context, R.color.colorBlack))
                    .setNegativeTextColor(ContextCompat.getColor(context, R.color.colorAccent))
                    .autoDismiss(false)
                    .setCancelable(false)
                    .onNegative(object : BottomDialog.ButtonCallback {
                        override fun onClick(dialog: BottomDialog) {
                            dialog.dismiss()
                        }
                    })
                    .show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Suppress("DEPRECATION")
    @ColorInt
    fun getColor(context: Context, @ColorRes colorId: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getColor(colorId)
        } else context.resources.getColor(colorId)

    }

    fun readRawResource(context: Context, @RawRes rawRes: Int): String {
        return readStream(context.resources.openRawResource(rawRes))
    }

    fun readStream(`is`: InputStream): String {
        // http://stackoverflow.com/a/5445161
        val s = Scanner(`is`).useDelimiter("\\A")
        return if (s.hasNext()) s.next() else ""
    }

    fun dp2px(dpVal: Float): Int {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpVal, Resources.getSystem().getDisplayMetrics()
        ).toInt()
    }

    fun show(isShow: Boolean, view: View): Boolean {
        var isViewToshow = isShow
        if (isViewToshow) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
        return isViewToshow
    }


    fun saveMobile(mobileNum: String, context: Context) {
        MSharePk.putString(context, mobile, mobileNum).toString()
        if (mobileNum.startsWith("091") || mobileNum.startsWith("099")) {
            MSharePk.putBoolean(context, isTci, true)
        } else {
            MSharePk.putBoolean(context, isTci, false)
        }
    }

    fun getMobile(context: Context): String {
        var mobile: String = MSharePk.getString(context, mobile, "").toString()
        return mobile
    }

    fun saveActiveCode(activCode: String, context: Context) {
        MSharePk.putString(context, activeCode, activCode)
    }

    fun getAciveCode(context: Context): String {
        var code: String = MSharePk.getString(context, activeCode, "").toString()
        return code
    }

    fun isNewRecord(newRecord: Boolean, context: Context) {
        MSharePk.putBoolean(context, isNewRecord, newRecord)
    }

    fun getNewRecord(context: Context): Boolean {
        var newRecord: Boolean = MSharePk.getBoolean(context, isNewRecord, false)
        return newRecord
    }

    fun isTci(context: Context): Boolean {
        var check: Boolean = MSharePk.getBoolean(context, isTci, false)
        return check
    }

    fun getTciStatus(context: Context): Boolean {
        var check: Boolean = MSharePk.getBoolean(context, checkTci, false)
        return check
    }

    fun getSimpleStatus(context: Context): Boolean {
        var check: Boolean = MSharePk.getBoolean(context, checkSimple, false)
        return check
    }

    fun convertImage(): File {
        return File("" + Environment.getExternalStorageDirectory() + convertImage)
    }

    fun createDirIfNotExists(path: String): Boolean {
        var rect: Boolean = true
        var file = File(Environment.getExternalStorageDirectory(), path)
        if (!file.exists()) {
            if (!file.mkdirs()) {
                rect = false
            }
        }
        return rect

    }


    fun dpToPx(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    fun getScreenWidth(context: Context?): Int {
        return if (context == null) {
            0
        } else getDisplayMetrics(context).widthPixels
    }

    fun getDisplayMetrics(context: Context): DisplayMetrics {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        return metrics
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

    fun getPageFromId(id: String): HashMap<String, String> {
        val map = HashMap<String, String>()
        map.put("id", Base64(getId(id)))
        return map
    }

    fun getPageParentCategory(id: String, category: String): HashMap<String, String> {
        val map = HashMap<String, String>()
        map.put("q", Base64(categoryId(id, category)))
        return map
    }


    fun getPageCategory(id: String, category: String): HashMap<String, String> {
        val map = HashMap<String, String>()
        map.put("q", Base64(matchCategoryId(id, category)))
        return map
    }

    fun getId(id: String): String {
        var data: String = ""
        try {
            var json = JSONObject()
            var item = JSONObject()
            item.put("id", id)
            json.put("where", item);
            data = json.toString();
        } catch (e: JSONException) {
        }
        return data

    }

    fun categoryId(id: String, category: String): String {
        var data: String = ""
        try {
            var json = JSONObject()
            var item = JSONObject()
            item.put("id", id)
            item.put("category_id", category)
            json.put("where", item);
            data = json.toString();
        } catch (e: JSONException) {
        }
        return data

    }


    fun matchCategoryId(parent: String, category: String): String {
        var data: String = ""
        try {
            var json = JSONObject()
            var item = JSONObject()
            item.put("parent", parent)
            item.put("category_id", category)
            json.put("where", item);
            data = json.toString();
        } catch (e: JSONException) {
        }
        return data

    }


    fun getPageData(parent: String): HashMap<String, String> {
        val map = HashMap<String, String>()
//        map.put("category", Base64(getParent(parent)))
        map.put("category", parent)
        return map
    }

    fun getParent(parent: String): String {
        var data: String = ""
        try {
            var json = JSONObject()
            var item = JSONObject()
            item.put("parent", parent)
            json.put("where", item)
            data = json.toString()
        } catch (e: JSONException) {
        }
        return data
    }

    fun limitData(per_page: Int, page: Int, offset: Int): String {
        var message = "DefileValue"
        try {
            val json = JSONObject()
            val item = JSONObject()
            json.put("where", item)
            json.put("per_page", per_page)
            json.put("page", page)
            json.put("offset", offset)
            message = json.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return message
    }

    fun getDownloadAppFolderPath(name: String): File {
        val apk_path = File("" + Environment.getExternalStorageDirectory() + downloadVersionFolder + name + ".apk")
        return apk_path
    }

    fun getVitrinAppFolderPath(name: String): File {
        val apk_path = File("" + Environment.getExternalStorageDirectory() + vitrinApps + name + ".apk")
        return apk_path
    }

    fun getVitrinBookFolderPath(name: String): File {
        val apk_path = File("" + Environment.getExternalStorageDirectory() + vitrinBooks + name + ".pdf")
        return apk_path
    }


    fun instalApk(path: String, context: Context) {
        var packageURI = Uri.parse(context.packageName.toString())
        var apkfile: File = File(path)
        val intent: Intent = Intent(Intent.ACTION_VIEW, packageURI)
        intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive")
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)

    }

    fun getFileSize(file: File): String {
        val size = getFolderSize(file) / 1024 // Get size and convert bytes into Kb.
        return if (size >= 1024) {
            (size / 1024).toString() + " Mb"
        } else {
            size.toString() + " Kb"
        }
    }


    fun getFolderSize(file: File): Long {
        var size: Long = 0
        if (file.isDirectory) {
            for (child in file.listFiles()!!) {
                size += getFolderSize(child)
            }
        } else {
            size = file.length()
        }
        return size
    }

    fun getDownloadAppFolderPath(): File {
        val apk_path = File("" + Environment.getExternalStorageDirectory() + downloadVersionFolder)
        return apk_path
    }

    fun getVitrinAppFolderPath(): File {
        val apk_path = File("" + Environment.getExternalStorageDirectory() + vitrinApps)
        return apk_path
    }

    fun getVitrinBookFolderPath(): File {
        val apk_path = File("" + Environment.getExternalStorageDirectory() + vitrinBooks)
        return apk_path
    }

    fun openUrlInChrome(context: Context, url: String) {
        try {
            try {
                var uri: Uri = Uri.parse("googlechrome://navigate?url=" + url)
                var intent: Intent = Intent(Intent.ACTION_VIEW, uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: Exception) {
                var uri: Uri = Uri.parse(url)
                var intent: Intent = Intent(Intent.ACTION_VIEW, uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

        } catch (e: Exception) {

        }

    }

    fun intentMessageTelegram(context: Context, msg: String) {
        try {
            var telgramPkg: String = "ir.amin.HaftTeen"
            var intent: Intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(msg))
            if (isAppAvailable(context.getApplicationContext(), appName)) {
                intent.setPackage(appName);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "تلگرام روی گوشی شما نصب نمی باشد.", Toast.LENGTH_SHORT).show();
            }
        } catch (e: Exception) {
            Toast.makeText(context, "لطفا منتظر بمانید...", Toast.LENGTH_SHORT).show();

        }

    }


    fun isAppAvailable(context: Context, pkg: String): Boolean {
        var pm: PackageManager = context.getPackageManager()
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;

        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }

    fun appInstalledOrNot(context: Context, uri: String): Boolean {
        var pm: PackageManager = context.getPackageManager()
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }

        return false
    }

    fun runApp(context: Context, pkg: String) {
        context.startActivity(
                Intent(
                        context.getPackageManager()
                                .getLaunchIntentForPackage(pkg)
                )
        )
    }


    companion object {
        private var ourInstance: VasniSchema? = VasniSchema()
        val instance: VasniSchema
            get() {
                if (ourInstance == null) {
                    ourInstance = VasniSchema()
                }
                return ourInstance!!
            }

        fun clear() {
            ourInstance = null
        }
    }

}