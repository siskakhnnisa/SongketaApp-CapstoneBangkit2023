package com.songketa.songket_recognition_app.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.songketa.songket_recognition_app.R

class CustomButton : MaterialButton{
    private lateinit var enabledBackground: Drawable
    private lateinit var disabledBackground: Drawable
    private var txtColor: Int = 0
    private var txtColor2: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if(isEnabled) enabledBackground  else disabledBackground
        if(isEnabled) setTextColor(txtColor) else setTextColor(txtColor2)
        textSize = 18f
        gravity = Gravity.CENTER
//        text = if(isEnabled) "Sign In" else "Sign In"
        textAlignment = TEXT_ALIGNMENT_CENTER
    }
    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.white)
        txtColor2 = ContextCompat.getColor(context, R.color.primary)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_enabled) as Drawable
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_disabled) as Drawable
    }
}