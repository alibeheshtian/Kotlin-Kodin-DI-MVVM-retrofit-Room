package com.shenoto.app.customView

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.shenoto.domain.R
import kotlinx.android.synthetic.main.custom_loading.view.*


class CustomLoading : ConstraintLayout {


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = 8f
        }

        View.inflate(context, R.layout.custom_loading, this)

        hide()

    }

    fun hide() {
        this.visibility = GONE

        pb_loading.visibility = GONE

        tv_description.visibility = GONE

        iv_image.visibility = GONE

        btn.visibility = GONE
    }

    fun show(
        onClickListener: OnClickListener? = null,
        btnText: String? = null,
        message: String? = null,
        image: Int = 0,
        isShowLoading: Boolean = false
    ) {
        this.visibility = VISIBLE

        pb_loading.visibility = if (isShowLoading) VISIBLE else GONE
        btn.visibility = if (onClickListener != null) VISIBLE else GONE
        tv_description.visibility = if (!message.isNullOrEmpty()) VISIBLE else GONE
        iv_image.visibility = if (image != 0) VISIBLE else GONE



        btn.text = btnText ?: "تلاش مجدد"

        btn.setOnClickListener(onClickListener)

        tv_description.text = message


        if (image != 0) {
            iv_image.setImageResource(image)
        }

    }

    fun show() {
        show(isShowLoading = true)
    }

    fun toggel(isShow: Boolean = true) {
        if (isShow) {
            show(isShowLoading = true)
        } else {
            hide()
        }
    }
}




