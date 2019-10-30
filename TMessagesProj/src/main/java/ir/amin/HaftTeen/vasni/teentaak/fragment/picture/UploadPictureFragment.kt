package ir.amin.HaftTeen.vasni.teentaak.fragment.picture

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.support.v7.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.schedulers.Schedulers
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ir.amin.HaftTeen.messenger.NotificationCenter
import ir.amin.HaftTeen.ui.ActionBar.ActionBar
import ir.amin.HaftTeen.ui.ActionBar.BaseFragment
import ir.amin.HaftTeen.ui.ActionBar.Theme
import ir.amin.HaftTeen.ui.ActionBar.ThemeDescription
import ir.amin.HaftTeen.ui.LaunchActivity
import ir.amin.HaftTeen.vasni.api.ApiService
import ir.amin.HaftTeen.vasni.core.Compressor
import ir.amin.HaftTeen.vasni.core.ProgressRequestBody
import ir.amin.HaftTeen.vasni.extention.getDataArray
import ir.amin.HaftTeen.vasni.extention.getError
import ir.amin.HaftTeen.vasni.extention.getRandomColor
import ir.amin.HaftTeen.vasni.extention.getSuccess
import ir.amin.HaftTeen.vasni.model.teentaak.EventHandler
import ir.amin.HaftTeen.vasni.model.teentaak.UserVideoCategory
import ir.amin.HaftTeen.R
import java.io.File


class UploadPictureFragment : BaseFragment, NotificationCenter.NotificationCenterDelegate, Callback<JsonObject>, ProgressRequestBody.UploadCallbacks {

    private var txt: String = ""
    private var eventHandler = EventHandler()
    private var categoryList: List<UserVideoCategory> = ArrayList()
    private var subCategoryList: ArrayList<UserVideoCategory> = ArrayList()
    private var picFile: File? = null
    private val selectedUri: Uri? = null
    private var pic_category_id: String = ""
    private lateinit var imv_tv_founders_icon: AppCompatImageView
    private lateinit var pv_tv_founder: ProgressView
    private lateinit var tv_upload_pic_detail: JTextView
    private lateinit var et_upload_pic_title: EditText
    private lateinit var tv_select_pic_file: MTextView
    private lateinit var tagview_upload_pic: TagView
    private lateinit var btn_submit_upload_pic: MButton
    private lateinit var pv_pic_upload: View
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
        fragmentView = factory.inflate(R.layout.frg_upload_pic, null)
        fragmentView.setOnTouchListener { v, event -> true }

        imv_tv_founders_icon = fragmentView.findViewById(R.id.imv_tv_founders_icon)
        pv_tv_founder = fragmentView.findViewById(R.id.pv_tv_founder)
        tv_upload_pic_detail = fragmentView.findViewById(R.id.tv_upload_pic_detail)
        et_upload_pic_title = fragmentView.findViewById(R.id.et_upload_pic_title)
        tv_select_pic_file = fragmentView.findViewById(R.id.tv_select_pic_file)
        tagview_upload_pic = fragmentView.findViewById(R.id.tagview_upload_pic)
        btn_submit_upload_pic = fragmentView.findViewById(R.id.btn_submit_upload_pic)
        pv_pic_upload = fragmentView.findViewById(R.id.pv_pic_upload)
        tv_loading_desc = fragmentView.findViewById(R.id.tv_loading_desc)

        tv_upload_pic_detail.setText(fragmentView.context.getString(R.string.upload_pic_desc), true)
        ApiService.apiInterface.getPictureCategory(eventHandler.program, eventHandler.source).enqueue(this)

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
                    VasniSchema.instance.show(false, pv_pic_upload)
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
            tagview_upload_pic.addTag(tag)
        }
        tagview_upload_pic.setOnTagClickListener(object : TagView.OnTagClickListener {
            override fun onTagClick(tag: Tag?, position: Int) {
                pic_category_id = subCategoryList.get(position).hash_id
                Toast.makeText(
                        fragmentView.context!!,
                        " دسته " + subCategoryList.get(position).title + " انتخاب شد. ",
                        Toast.LENGTH_LONG
                ).show()
            }

        })
        tv_select_pic_file.setOnClickListener {
            checkPermission()
        }

        btn_submit_upload_pic.setOnClickListener {
            sendPicture()
        }
    }

    private fun sendPicture() {
        try {
            if (et_upload_pic_title.text.isEmpty()) {
                VasniSchema.instance.showMessage(
                        fragmentView.context!!,
                        fragmentView.context!!.getString(R.string.select_name_pic),
                        "",
                        fragmentView.context!!.getString(R.string.ok)
                )
                return
            } else if (picFile == null) {
                VasniSchema.instance.showMessage(
                        fragmentView.context!!,
                        fragmentView.context!!.getString(R.string.select_pic),
                        "",
                        fragmentView.context!!.getString(R.string.ok)
                )
                return
            } else if (pic_category_id.equals("")) {
                VasniSchema.instance.showMessage(
                        fragmentView.context!!,
                        fragmentView.context!!.getString(R.string.select_picture_category),
                        "",
                        fragmentView.context!!.getString(R.string.ok)
                )
                return
            }
            VasniSchema.instance.show(true, pv_pic_upload)
            convertPic()
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
        MPermission.create(fragmentView.context!!)
                .title(fragmentView.context.getString(R.string.select_file_permission))
                .permissions(permissionItems)
                .msg(fragmentView.context.getString(R.string.login_permission_msg))
                .animStyle(R.style.PermissionAnimFade)
                .checkMutiPermission(object : PermissionCallback {
                    override fun onClose() {

                    }

                    override fun onFinish() {
                        if (VasniSchema.instance.createDirIfNotExists(VasniSchema.instance.convertVideo)) {
                        }
                        val bottomSheetDialogFragment = TedBottomPicker.Builder(fragmentView.context!!)
                                .setOnImageSelectedListener(object : TedBottomPicker.OnImageSelectedListener {
                                    override fun onImageSelected(uri: Uri) {
                                        picFile = File(uri.path)
                                        tv_select_pic_file.text = picFile!!.name + " "
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
            tv_loading_desc.setText(fragmentView.context.getString(R.string.send_picture_to_server) + " " + percentage + "%")
        } catch (e: Exception) {

        }
    }

    override fun onError() {

    }

    override fun onFinish() {
    }

    private fun uploadPicture() {
        val picBody = ProgressRequestBody(picFile, this)
        val vFile = MultipartBody.Part.createFormData("pic", picFile!!.getName(), picBody)
        val title = RequestBody.create(MultipartBody.FORM, et_upload_pic_title.text.toString())
        val id = RequestBody.create(MultipartBody.FORM, pic_category_id)
        ApiService.apiInterface.uploadPic(title, id, vFile).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    VasniSchema.instance.show(false, pv_pic_upload)
                    if (getSuccess(response.body()!!).success == VasniSchema.instance.success) {
                        VasniSchema.instance.showMessage(
                                fragmentView.context!!,
                                fragmentView.context!!.getString(R.string.submit_send_pic),
                                "",
                                fragmentView.context!!.getString(R.string.ok)
                        )
                        et_upload_pic_title.setText("")
                        pic_category_id = ""
                        pic_category_id = ""
                    } else {
                        VasniSchema.instance.showMessage(
                                fragmentView.context!!,
                                getError(response.body()!!).message.toString(),
                                "",
                                fragmentView.context!!.getString(R.string.ok)
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                VasniSchema.instance.showMessage(
                        fragmentView.context!!,
                        fragmentView.context!!.getString(R.string.server_error),
                        "",
                        fragmentView.context!!.getString(R.string.ok)
                )
            }
        })

    }

    private fun convertPic() {
        try {
            Compressor(fragmentView.context!!)
                    .setMaxHeight(200)
                    .setMaxWidth(200)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(VasniSchema.instance.convertImage().toString())
                    .compressToFileAsFlowable(picFile)
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : io.reactivex.functions.Consumer<File> {
                        override fun accept(t: File) {
                            picFile = t
                            uploadPicture()
                        }

                    })

        } catch (e: Exception) {

        }
    }


}
