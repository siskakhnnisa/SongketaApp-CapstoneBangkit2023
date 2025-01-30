package com.songketa.songket_recognition_app.customview

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import com.songketa.songket_recognition_app.R


class CustomTextInput : TextInputEditText, TextWatcher {

    var isValid: Boolean? = null
        private set

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(this)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(p0: Editable?) {
        if (text != null && text?.isNotEmpty() == true) {
            isValid = true
            error = null
        } else {
            error = context.getString(R.string.required_field)
            isValid = false
        }
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        error = null
    }
}
