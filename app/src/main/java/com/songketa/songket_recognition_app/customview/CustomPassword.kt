package com.songketa.songket_recognition_app.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.songketa.songket_recognition_app.R
import com.songketa.songket_recognition_app.utils.validateMinLength

class CustomPassword : AppCompatEditText{
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
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (validateMinLength(text.toString())) {
                    this@CustomPassword.error = null
                } else {
                    this@CustomPassword.error = context.getString(R.string.invalid_password)
                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

}