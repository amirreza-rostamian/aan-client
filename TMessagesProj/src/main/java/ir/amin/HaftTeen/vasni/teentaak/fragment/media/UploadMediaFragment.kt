package ir.amin.HaftTeen.vasni.teentaak.fragment.media

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.widget.AppCompatImageView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zolad.videoslimmer.VideoSlimmer
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.tus.java.client.TusClient
import me.himanshusoni.chatmessageview.Vasni.Permission.MPermission
import me.himanshusoni.chatmessageview.Vasni.Permission.PermissionCallback
import me.himanshusoni.chatmessageview.Vasni.Permission.PermissionItem
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import me.himanshusoni.chatmessageview.ui.BottomPicker.TedBottomPicker
import me.himanshusoni.chatmessageview.ui.JTextView
import me.himanshusoni.chatmessageview.ui.MButton
import me.himanshusoni.chatmessageview.ui.MTextView
import me.himanshusoni.chatmessageview.ui.ProgressView
import me.himanshusoni.chatmessageview.ui.TagView.Tag
import me.himanshusoni.chatmessageview.ui.TagView.TagView
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.messenger.NotificationCenter
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.ui.LaunchActivity
import ir.amin.HaftTeen.vasni.api.AbrArvanService
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.api.RahpoService
import ir.amin.HaftTeen.vasni.core.Compressor
import ir.amin.HaftTeen.vasni.core.DataLoader
import ir.amin.HaftTeen.vasni.core.ProgressRequestBody
import ir.amin.HaftTeen.vasni.extention.*
import ir.amin.HaftTeen.vasni.model.teentaak.DialogContent
import ir.amin.HaftTeen.vasni.model.teentaak.Event
import ir.amin.HaftTeen.vasni.model.teentaak.EventHandler
import ir.amin.HaftTeen.vasni.model.teentaak.UserVideoCategory
import ir.amin.HaftTeen.vasni.utils.TusAndroidUpload
import ir.amin.HaftTeen.vasni.utils.UploadTask
import ir.amin.HaftTeen.R
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class UploadMediaFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate, Callback<JsonObject>, ProgressRequestBody.UploadCallbacks {

    private var txt: String = ""
    private var eventHandler = EventHandler()
    private var categoryList: List<UserVideoCategory> = ArrayList()
    private var subCategoryList: ArrayList<UserVideoCategory> = ArrayList()
    private var file: File? = null
    private val selectedUri: Uri? = null
    private var mediaTypeList = ArrayList<DialogContent>()
    private var fileType: String = ""
    private var bitmap: Bitmap? = null
    private var category_id: String = ""
    private lateinit var imv_tv_founders_icon: AppCompatImageView
    private lateinit var pv_tv_founder: ProgressView
    private lateinit var tv_upload_media_detail: JTextView
    private lateinit var et_upload_media_title: EditText
    private lateinit var tv_select_media_file: MTextView
    private lateinit var tagview_upload_media: TagView
    private lateinit var btn_submit_upload_media: MButton
    private lateinit var pv_media_upload: View
    private lateinit var tv_loading_desc: MTextView

    constructor(txt: String, data: EventHandler) {
        this.txt = txt
        this.eventHandler = data
    }

    override fun createView(context: Context?): View {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back)
        actionBar.setAllowOverlayTitle(true)
        actionBar.setTitle(txt)
        actionBar.setActionBarMenuOnItemClick(object : ActionBar.ActionBarMenuOnItemClick() {
            override fun onItemClick(id: Int) {
                if (id == -1) {
                    finishFragment()
                }
            }
        })
        val factory = LayoutInflater.from(context)
        fragmentView = factory.inflate(R.layout.frg_upload_media, null)
        fragmentView.setOnTouchListener { v, event -> true }

        imv_tv_founders_icon = fragmentView.findViewById(R.id.imv_tv_founders_icon)
        pv_tv_founder = fragmentView.findViewById(R.id.pv_tv_founder)
        tv_upload_media_detail = fragmentView.findViewById(R.id.tv_upload_media_detail)
        et_upload_media_title = fragmentView.findViewById(R.id.et_upload_media_title)
        tv_select_media_file = fragmentView.findViewById(R.id.tv_select_media_file)
        tagview_upload_media = fragmentView.findViewById(R.id.tagview_upload_media)
        btn_submit_upload_media = fragmentView.findViewById(R.id.btn_submit_upload_media)
        pv_media_upload = fragmentView.findViewById(R.id.pv_media_upload)
        tv_loading_desc = fragmentView.findViewById(R.id.tv_loading_desc)

        tv_upload_media_detail.setText(fragmentView.context.getString(R.string.upload_media_desc), true)
        ApiService.apiInterface.getMediaCategory(eventHandler.program, eventHandler.source
        ).enqueue(this)

        return fragmentView

    }


    override fun onFragmentCreate(): Boolean {
        return super.onFragmentCreate()
    }

    override fun onFragmentDestroy() {
        super.onFragmentDestroy()
    }

    override fun didReceivedNotification(id: Int, account: Int, vararg args: Any?) {
    }

    override fun getThemeDescriptions(): Array<ThemeDescription> {
        return arrayOf(ThemeDescription(fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null,
                null, Theme.key_windowBackgroundWhite), ThemeDescription(actionBar, ThemeDescription.FLAG_BACKGROUND,
                null, null, null, null,
                Theme.key_actionBarDefault), ThemeDescription(actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR,
                null, null, null, null,
                Theme.key_actionBarDefaultIcon), ThemeDescription(actionBar, ThemeDescription.FLAG_AB_TITLECOLOR,
                null, null, null, null,
                Theme.key_actionBarDefaultTitle), ThemeDescription(actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR,
                null, null, null, null,
                Theme.key_actionBarDefaultSelector))
    }

    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
        if (response.isSuccessful && response.body() != null) {
            try {
                if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                    VasniSchema.instance.show(false, pv_media_upload)
                    subCategoryList.clear()
                    categoryList = Gson().fromJson(
                            getDataArray(response.body()!!), Array<UserVideoCategory>::class.java
                    ).toList()
                    for (i in 0 until categoryList.size) {
                        for (j in 0 until categoryList.get(i).children.size) {
                            var userVideoCategory = UserVideoCategory()
                            userVideoCategory.title = categoryList.get(i).children.get(j).title
                            userVideoCategory.hash_id = categoryList.get(i).children.get(j).hash_id
                            subCategoryList.add(userVideoCategory)
                        }
                    }

                    initView()

                } else {
                    VasniSchema.instance.showMessage(
                            fragmentView.context,
                            getError(response.body()!!).message.toString(),
                            "",
                            fragmentView.context.getString(R.string.ok)
                    )
                }

            } catch (e: Exception) {

            }

        }
    }

    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
        VasniSchema.instance.showMessage(
                fragmentView.context,
                fragmentView.context.getString(R.string.server_error),
                "",
                fragmentView.context.getString(R.string.ok)
        )
    }

    private fun initView() {
        for (i in 0 until subCategoryList.size) {
            var tag = Tag(subCategoryList.get(i).title)
            tag.radius = 5.toFloat()
            tag.layoutColor = Color.parseColor(getRandomColor())
            tagview_upload_media.addTag(tag)
        }
        tagview_upload_media.setOnTagClickListener(object : TagView.OnTagClickListener {
            override fun onTagClick(tag: Tag?, position: Int) {
                category_id = subCategoryList.get(position).hash_id
                Toast.makeText(
                        fragmentView.context,
                        " دسته " + subCategoryList.get(position).title + " انتخاب شد. ",
                        Toast.LENGTH_LONG
                ).show()
            }

        })

        tv_select_media_file.setOnClickListener {
            mediaTypeList.clear()
            mediaTypeList.add(DialogContent(0, fragmentView.context.getString(R.string.picture_file)))
            mediaTypeList.add(DialogContent(1, fragmentView.context.getString(R.string.video_file)))
            DataLoader.instance.showDialog(
                    fragmentView.context,
                    mediaTypeList,
                    fragmentView.context.getString(R.string.select_file_type),
                    VasniSchema.instance.mediaFile
            )
        }

        btn_submit_upload_media.setOnClickListener {
            sendFile()
        }
    }

    private fun sendFile() {
        try {
            if (et_upload_media_title.text.isEmpty()) {
                VasniSchema.instance.showMessage(
                        fragmentView.context,
                        fragmentView.context.getString(R.string.select_name_media),
                        "",
                        fragmentView.context.getString(R.string.ok)
                )
                return
            } else if (file == null) {
                VasniSchema.instance.showMessage(
                        fragmentView.context,
                        fragmentView.context.getString(R.string.select_media),
                        "",
                        fragmentView.context.getString(R.string.ok)
                )
                return
            } else if (category_id.equals("")) {
                VasniSchema.instance.showMessage(
                        fragmentView.context,
                        fragmentView.context.getString(R.string.select_category_media),
                        "",
                        fragmentView.context.getString(R.string.ok)
                )
                return
            }
            VasniSchema.instance.show(true, pv_media_upload)

            if (fileType == "1") {
                convertVideo()
            } else {
                convertPic()
            }

        } catch (e: Exception) {
        }

    }

    private fun checkPermission() {
        val permissionItems = ArrayList<PermissionItem>()
        permissionItems.add(
                PermissionItem(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        fragmentView.context.getString(R.string.permission_storage),
                        R.drawable.permission_ic_storage
                )
        )
        MPermission.create(fragmentView.context)
                .title(fragmentView.context.getString(R.string.select_file_permission))
                .permissions(permissionItems)
                .msg(fragmentView.context.getString(R.string.file_permission_msg))
                .animStyle(R.style.PermissionAnimFade)
                .checkMutiPermission(object : PermissionCallback {
                    override fun onClose() {

                    }

                    override fun onFinish() {
                        if (VasniSchema.instance.createDirIfNotExists(VasniSchema.instance.convertVideo)) {
                        }
                        val bottomSheetDialogFragment = TedBottomPicker.Builder(fragmentView.context)
                                .setOnImageSelectedListener(object : TedBottomPicker.OnImageSelectedListener {
                                    override fun onImageSelected(uri: Uri) {
                                        file = File(uri.path)
                                        tv_select_media_file.text = file!!.name + " "
                                    }
                                })
                                .setPeekHeight(fragmentView.context.resources.displayMetrics.heightPixels / 2)
                                .setSelectedUri(selectedUri)
                                .setTitle(fragmentView.context.getString(R.string.select_pic))
                                .showCameraTile(false)
                                .create()
                        bottomSheetDialogFragment.show(LaunchActivity.getFragmenrManager())
                    }

                    override fun onDeny(permission: String, position: Int) {
                    }

                    override fun onGuarantee(permission: String, position: Int) {
                    }
                })
    }

    override fun onProgressUpdate(percentage: Int) {
        try {
            tv_loading_desc.setText(fragmentView.context.getString(R.string.send_file_to_server) + " " + percentage + "%")
        } catch (e: Exception) {

        }
    }

    override fun onError() {

    }

    override fun onFinish() {
    }

    private fun uploadPicture() {
        val body = ProgressRequestBody(file, this)
        val vFile = MultipartBody.Part.createFormData("file", file!!.getName(), body)
        val thumbnail = MultipartBody.Part.createFormData("thumbnail", file!!.getName(), body)
        val fileId = RequestBody.create(okhttp3.MultipartBody.FORM, "")
        val title = RequestBody.create(okhttp3.MultipartBody.FORM, et_upload_media_title.text.toString())
        val category = RequestBody.create(okhttp3.MultipartBody.FORM, category_id)
        val type = RequestBody.create(okhttp3.MultipartBody.FORM, VasniSchema.instance.MediaType_picture)
        ApiService.apiInterface.submitUploadMedia(fileId, category, type, title, vFile, thumbnail
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    VasniSchema.instance.show(false, pv_media_upload)
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        VasniSchema.instance.showMessage(
                                fragmentView.context,
                                fragmentView.context.getString(R.string.submit_send_pic),
                                "",
                                fragmentView.context.getString(R.string.ok)
                        )
                        et_upload_media_title.setText("")
                        category_id = ""
                    } else {
                        VasniSchema.instance.showMessage(
                                fragmentView.context,
                                getError(response.body()!!).message.toString(),
                                "",
                                fragmentView.context.getString(R.string.ok)
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.showMessage(
                        fragmentView.context,
                        fragmentView.context.getString(R.string.server_error),
                        "",
                        fragmentView.context.getString(R.string.ok)
                )
            }
        })

    }

    private fun convertPic() {
        try {
            Compressor(fragmentView.context)
                    .setMaxHeight(200)
                    .setMaxWidth(200)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(VasniSchema.instance.convertImage().toString())
                    .compressToFileAsFlowable(file)
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : Consumer<File> {
                        override fun accept(t: File) {
                            file = t
                            uploadPicture()
                        }

                    })

        } catch (e: Exception) {

        }
    }

    private fun checkPermissionVideo() {
        val permissionItems = ArrayList<PermissionItem>()
        permissionItems.add(
                PermissionItem(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        fragmentView.context.getString(R.string.permission_storage),
                        R.drawable.permission_ic_storage
                )
        )
        MPermission.create(fragmentView.context!!)
                .title(fragmentView.context.getString(R.string.select_file_permission))
                .permissions(permissionItems)
                .msg(fragmentView.context.getString(R.string.file_permission_msg))
                .animStyle(R.style.PermissionAnimFade)
                .checkMutiPermission(object : PermissionCallback {
                    override fun onClose() {

                    }

                    override fun onFinish() {
                        if (VasniSchema.instance.createDirIfNotExists(VasniSchema.instance.convertVideo)) {
                        }
                        val bottomSheetDialogFragment = TedBottomPicker.Builder(fragmentView.context)
                                .setOnImageSelectedListener(object : TedBottomPicker.OnImageSelectedListener {
                                    override fun onImageSelected(uri: Uri) {
                                        if (File(uri.path).length() > VasniSchema.instance.videoLimitSize) {
                                            VasniSchema.instance.showMessage(
                                                    fragmentView.context,
                                                    fragmentView.context.getString(R.string.size_video_error),
                                                    "",
                                                    fragmentView.context.getString(R.string.ok)
                                            )

                                        } else {
                                            file = File(uri.path)
                                            bitmap = ThumbnailUtils.createVideoThumbnail(uri.path, MediaStore.Images.Thumbnails.MINI_KIND)
                                            tv_select_media_file.text = file!!.name + " "
                                        }

                                    }
                                })
                                .setPeekHeight(fragmentView.context.resources.displayMetrics.heightPixels / 2)
                                .setSelectedUri(selectedUri)
                                .setTitle(fragmentView.context.getString(R.string.select_video))
                                .showCameraTile(false)
                                .showVideoMedia()
                                .create()
                        bottomSheetDialogFragment.show(LaunchActivity.getFragmenrManager())
                    }

                    override fun onDeny(permission: String, position: Int) {
                    }

                    override fun onGuarantee(permission: String, position: Int) {
                    }
                })
    }

    private fun convertVideo() {
        val destPath =
                "" + Environment.getExternalStorageDirectory() + VasniSchema.instance.convertVideo + File.separator + "Taak_" + SimpleDateFormat(
                        "yyyyMMdd_HHmmss"
                ).format(Date()) + ".mp4"
        VideoSlimmer.convertVideo(
                file.toString(),
                destPath,
                720,
                480,
                15000,
                object : VideoSlimmer.ProgressListener {
                    override fun onStart() {
                    }

                    override fun onProgress(progress: Float) {
                        try {
                            tv_loading_desc.setText(fragmentView.context.getString(R.string.process) + " " + progress.toInt() + "%")
                        } catch (e: Exception) {

                        }
                    }

                    override fun onFinish(result: Boolean) {
                        if (result) {
                            file = File(destPath)
                            tv_loading_desc.setText(fragmentView.context.getString(R.string.send_video_to_server))

                            if (VasniSchema.instance.video_service == VasniSchema.instance.video_service_rahpo) {
                                uploadVideo()
                            } else if (VasniSchema.instance.video_service == VasniSchema.instance.video_service_abr_arvan) {
                                patchVideoAbrArvan()
                            } else {
                                submitUpload("")
                            }

                        } else {
                            VasniSchema.instance.showMessage(
                                    fragmentView.context,
                                    fragmentView.context.getString(R.string.error_video_convert),
                                    "",
                                    fragmentView.context.getString(R.string.ok)
                            )
                        }

                    }
                })
    }

    private fun uploadVideo() {
        val videoBody = ProgressRequestBody(file, this)
        val vFile = MultipartBody.Part.createFormData("file_content", file!!.getName(), videoBody)
        val fileName = RequestBody.create(okhttp3.MultipartBody.FORM, et_upload_media_title.text.toString())
        val type = RequestBody.create(okhttp3.MultipartBody.FORM, "V")
        val access_mode = RequestBody.create(okhttp3.MultipartBody.FORM, "free")
        val provider_code = RequestBody.create(okhttp3.MultipartBody.FORM, "689725394165")
        val submit = RequestBody.create(okhttp3.MultipartBody.FORM, "Upload file")
        RahpoService.apiInterface.uploadMedia(
                vFile, fileName, fileName, type, access_mode, provider_code, submit
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
//                    submitUpload(response.body()!!.asJsonObject)
                    submitUpload(response.body()!!.asJsonObject.get("guid").asString)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                try {
                    VasniSchema.instance.showMessage(
                            fragmentView.context,
                            fragmentView.context.getString(R.string.server_error),
                            "",
                            fragmentView.context.getString(R.string.ok)
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

    }

    private fun submitUpload(file_Id: String) {
        val videoBody = ProgressRequestBody(file, this)
        val vFile = MultipartBody.Part.createFormData("file", "ANN" + tv_select_media_file.text.toString(), videoBody)
//        val vFile = MultipartBody.Part.createFormData("file", file!!.name, videoBody)
//        val fileId = RequestBody.create(okhttp3.MultipartBody.FORM, jsonObject.get("guid").asString)
        val fileId = RequestBody.create(okhttp3.MultipartBody.FORM, file_Id)
        val title = RequestBody.create(okhttp3.MultipartBody.FORM, et_upload_media_title.text.toString())
        val category = RequestBody.create(okhttp3.MultipartBody.FORM, category_id)
        val type = RequestBody.create(okhttp3.MultipartBody.FORM, VasniSchema.instance.MediaType_video)

        /**********/
//        val bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_luancher_taak)
        val imgFile =
                File("" + Environment.getExternalStorageDirectory() + VasniSchema.instance.convertImage, "ic_launcher.PNG")
        val outStream = FileOutputStream(imgFile)
        bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, outStream)
        outStream.flush()
        outStream.close()
        val thumbnailBody = ProgressRequestBody(imgFile, this)
        val thumbnail = MultipartBody.Part.createFormData("thumbnail", imgFile!!.getName(), thumbnailBody)
        /*********/

        ApiService.apiInterface.submitUploadMedia(fileId, category, type, title, vFile, thumbnail
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    VasniSchema.instance.show(false, pv_media_upload)
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        VasniSchema.instance.showMessage(
                                fragmentView.context,
                                fragmentView.context.getString(R.string.submit_send_video),
                                "",
                                fragmentView.context.getString(R.string.ok)
                        )
                        et_upload_media_title.setText("")
                        category_id = ""
                    } else {
                        VasniSchema.instance.showMessage(
                                fragmentView.context,
                                getError(response.body()!!).message.toString(),
                                "",
                                fragmentView.context.getString(R.string.ok)
                        )
                    }
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.showMessage(
                        fragmentView.context,
                        fragmentView.context.getString(R.string.server_error),
                        "",
                        fragmentView.context.getString(R.string.ok)
                )
            }
        })
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: Event) {
        val contents = event.contents
        val msg = event.message
        if (msg.equals(VasniSchema.instance.mediaFile)) {
            val parts = contents.toString().split(",")
            val fileTypeName = parts[0]
            val id = parts[1]
            fileType = id
            tv_select_media_file.text = fileTypeName
            if (fileType == "1") {
                checkPermissionVideo()
            } else {
                checkPermission()
            }
        }

    }

    private fun patchVideoAbrArvan() {
        var url = DataLoader.instance.getUploadAbrArvan() + "channels/" + VasniSchema.instance.channel_id_abr_arvan + "/files"
        var client = TusClient()
        client.uploadCreationURL = URL(url)
        client.headers = VasniSchema.instance.getAbrArvanToken(VasniSchema.instance.api_key_abr_arvan)

        try {
            var upload = TusAndroidUpload(file!!)
            val uploadTask = UploadTask(client, upload)
            uploadTask.getResult(object : UploadTask.Result {
                override fun onProgressUpdate(percent: Int) {
                    tv_loading_desc.setText(fragmentView.context.getString(R.string.process) + " " + percent + "%")
                }

                override fun onProgressFinished(uploadURL: URL?) {
                    Log.e("onProgressFinished", "Upload finished!\n" + uploadURL.toString())
                    val fileId = uploadURL.toString().substring(uploadURL.toString().lastIndexOf("/") + 1)
                    uploadVideoAbrArvan(fileId)
                }

                override fun onProgressFailed(error: String?) {
                    Log.e("onProgressFailed", error)
                    VasniSchema.instance.showMessage(
                            fragmentView.context!!,
                            error!!,
                            "",
                            fragmentView.context!!.getString(R.string.ok)
                    )
                }

            }).execute()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun uploadVideoAbrArvan(fileId: String) {
        AbrArvanService.apiInterface.uploadVideoAbrArvan(
                VasniSchema.instance.getAbrArvanToken(
                        VasniSchema.instance.api_key_abr_arvan
                ),
                VasniSchema.instance.channel_id_abr_arvan,
                DataLoader.instance.createVideo(file!!.name, file!!.name, fileId)
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    submitUpload(getData(response.body()!!).get("id").asString)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                try {
                    VasniSchema.instance.showMessage(
                            fragmentView.context!!,
                            fragmentView.context!!.getString(R.string.server_error),
                            "",
                            fragmentView.context!!.getString(R.string.ok)
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

    }

}
