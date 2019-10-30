package me.himanshusoni.chatmessageview.ui

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.annotation.UiThread
import android.support.v4.content.res.ResourcesCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import me.himanshusoni.chatmessageview.R
import me.himanshusoni.chatmessageview.Vasni.VasniSchema


class CenterDialog protected constructor(val builder: Builder?) {
    lateinit var iconImageView: ImageView
    lateinit var titleTextView: TextView
    lateinit var contentTextView: TextView
    protected lateinit var vCustomView: FrameLayout
    lateinit var negativeButton: Button
    lateinit var positiveButton: Button

    init {
        this.builder!!.bottomDialog = initBottomDialog(builder)
    }

    @UiThread
    fun show() {
        if (builder != null && builder.bottomDialog != null)
            builder.bottomDialog!!.show()
    }

    @UiThread
    fun dismiss() {
        if (builder != null && builder.bottomDialog != null)
            builder.bottomDialog!!.dismiss()
    }

    @UiThread
    private fun initBottomDialog(builder: Builder): Dialog {
        val bottomDialog = Dialog(builder.context, R.style.BottomDialogs)
        val view = LayoutInflater.from(builder.context).inflate(R.layout.library_center_dialog, null)

        iconImageView = view.findViewById(R.id.centerDialog_icon)
        titleTextView = view.findViewById(R.id.centerDialog_title)
        contentTextView = view.findViewById(R.id.centerDialog_content)
        vCustomView = view.findViewById(R.id.centerDialog_custom_view)
        negativeButton = view.findViewById(R.id.centerDialog_cancel)
        positiveButton = view.findViewById(R.id.centerDialog_ok)

        if (builder.icon != null) {
            iconImageView.visibility = View.VISIBLE
            iconImageView.setImageDrawable(builder.icon)
        }

        if (builder.title != null) {
            titleTextView.text = builder.title
        }

        if (builder.content != null) {
            contentTextView.text = builder.content
        }

        if (builder.customView != null) {
            if (builder.customView!!.parent != null)
                (builder.customView!!.parent as ViewGroup).removeAllViews()
            vCustomView.addView(builder.customView)
            vCustomView.setPadding(
                    builder.customViewPaddingLeft,
                    builder.customViewPaddingTop,
                    builder.customViewPaddingRight,
                    builder.customViewPaddingBottom
            )
        }

        if (builder.btn_positive != null) {
            positiveButton.visibility = View.VISIBLE
            positiveButton.text = builder.btn_positive
            positiveButton.setOnClickListener {
                if (builder.btn_positive_callback != null)
                    builder.btn_positive_callback!!.onClick(this@CenterDialog)
                if (builder.isAutoDismiss)
                    bottomDialog.dismiss()
            }

            if (builder.btn_colorPositive != 0) {
                positiveButton.setTextColor(builder.btn_colorPositive)
            }

            if (builder.btn_colorPositiveBackground == 0) {
                //val variable = TypedValue()
                //val hasColorPrimary = builder.context.theme.resolveAttribute(R.attr.colorPrimary, v, true)
                //builder.btn_colorPositiveBackground = !hasColorPrimary ? v.data : ContextCompat.getColor(builder.context, R.color.bottom_dialog);
            }

            val buttonBackground =
                    VasniSchema.instance.createButtonBackgroundDrawable(builder.context, builder.btn_colorPositiveBackground)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                positiveButton.background = buttonBackground
            } else {

                //positiveButton.setBackgroundDrawable(buttonBackground)
            }
        }

        if (builder.btn_negative != null) {
            negativeButton.visibility = View.VISIBLE
            negativeButton.text = builder.btn_negative
            negativeButton.setOnClickListener {
                if (builder.btn_negative_callback != null)
                    builder.btn_negative_callback!!.onClick(this@CenterDialog)
                if (builder.isAutoDismiss)
                    bottomDialog.dismiss()
            }

            if (builder.btn_colorNegative != 0) {
                negativeButton.setTextColor(builder.btn_colorNegative)
            }
        }

        bottomDialog.setContentView(view)
        bottomDialog.setCancelable(builder.isCancelable)
        bottomDialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        bottomDialog.window!!.setGravity(Gravity.CENTER)

        return bottomDialog
    }

    interface ButtonCallback {

        fun onClick(dialog: CenterDialog)
    }

    class Builder(var context: Context) {

        // Bottom Dialog
        var bottomDialog: Dialog? = null

        // Icon, Title and Content
        var icon: Drawable? = null
        var title: CharSequence? = null
        var content: CharSequence? = null

        // Buttons
        var btn_negative: CharSequence? = null
        var btn_positive: CharSequence? = null
        var btn_negative_callback: ButtonCallback? = null
        var btn_positive_callback: ButtonCallback? = null
        var isAutoDismiss: Boolean = false

        // Button text colors
        var btn_colorNegative: Int = 0
        var btn_colorPositive: Int = 0

        // Button background colors
        var btn_colorPositiveBackground: Int = 0

        // Custom View
        var customView: View? = null
        var customViewPaddingLeft: Int = 0
        var customViewPaddingTop: Int = 0
        var customViewPaddingRight: Int = 0
        var customViewPaddingBottom: Int = 0

        // Other options
        var isCancelable: Boolean = false

        init {
            this.isCancelable = true
            this.isAutoDismiss = true
        }

        fun setTitle(@StringRes titleRes: Int): Builder {
            setTitle(this.context.getString(titleRes))
            return this
        }

        fun setTitle(title: CharSequence): Builder {
            this.title = title
            return this
        }

        fun setContent(@StringRes contentRes: Int): Builder {
            setContent(this.context.getString(contentRes))
            return this
        }

        fun setContent(content: CharSequence): Builder {
            this.content = content
            return this
        }

        fun setIcon(icon: Drawable): Builder {
            this.icon = icon
            return this
        }

        fun setIcon(@DrawableRes iconRes: Int): Builder {
            this.icon = ResourcesCompat.getDrawable(context.resources, iconRes, null)
            return this
        }

        fun setPositiveBackgroundColorResource(@ColorRes buttonColorRes: Int): Builder {
            this.btn_colorPositiveBackground = ResourcesCompat.getColor(context.resources, buttonColorRes, null)
            return this
        }

        fun setPositiveBackgroundColor(color: Int): Builder {
            this.btn_colorPositiveBackground = color
            return this
        }

        fun setPositiveTextColorResource(@ColorRes textColorRes: Int): Builder {
            this.btn_colorPositive = ResourcesCompat.getColor(context.resources, textColorRes, null)
            return this
        }

        fun setPositiveTextColor(color: Int): Builder {
            this.btn_colorPositive = color
            return this
        }

        fun setPositiveText(@StringRes buttonTextRes: Int): Builder {
            setPositiveText(this.context.getString(buttonTextRes))
            return this
        }

        fun setPositiveText(buttonText: CharSequence): Builder {
            this.btn_positive = buttonText
            return this
        }

        fun onPositive(buttonCallback: ButtonCallback): Builder {
            this.btn_positive_callback = buttonCallback
            return this
        }

        fun setNegativeTextColorResource(@ColorRes textColorRes: Int): Builder {
            this.btn_colorNegative = ResourcesCompat.getColor(context.resources, textColorRes, null)
            return this
        }

        fun setNegativeTextColor(color: Int): Builder {
            this.btn_colorNegative = color
            return this
        }

        fun setNegativeText(@StringRes buttonTextRes: Int): Builder {
            setNegativeText(this.context.getString(buttonTextRes))
            return this
        }

        fun setNegativeText(buttonText: CharSequence): Builder {
            this.btn_negative = buttonText
            return this
        }

        fun onNegative(buttonCallback: ButtonCallback): Builder {
            this.btn_negative_callback = buttonCallback
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            this.isCancelable = cancelable
            return this
        }

        fun autoDismiss(autodismiss: Boolean): Builder {
            this.isAutoDismiss = autodismiss
            return this
        }

        fun setCustomView(customView: View): Builder {
            this.customView = customView
            this.customViewPaddingLeft = 0
            this.customViewPaddingRight = 0
            this.customViewPaddingTop = 0
            this.customViewPaddingBottom = 0
            return this
        }

        fun setCustomView(customView: View, left: Int, top: Int, right: Int, bottom: Int): Builder {
            this.customView = customView
            this.customViewPaddingLeft = VasniSchema.instance.dpToPixels(context, left)
            this.customViewPaddingRight = VasniSchema.instance.dpToPixels(context, right)
            this.customViewPaddingTop = VasniSchema.instance.dpToPixels(context, top)
            this.customViewPaddingBottom = VasniSchema.instance.dpToPixels(context, bottom)
            return this
        }

        @UiThread
        fun build(): CenterDialog {
            return CenterDialog(this)
        }

        @UiThread
        fun show(): CenterDialog {
            val bottomDialog = build()
            bottomDialog.show()
            return bottomDialog
        }

    }

}
