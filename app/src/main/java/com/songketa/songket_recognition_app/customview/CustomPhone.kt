package com.songketa.songket_recognition_app.customview

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.songketa.songket_recognition_app.R

class CustomPhone : AppCompatEditText {

    private val phoneRegex = Regex("^\\d{11,12}\$")

    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialize()
    }

    private fun initialize() {
        inputType = InputType.TYPE_CLASS_PHONE
        filters = arrayOf(InputFilter.LengthFilter(12)) // 12 characters: XXXX-XXXX-XXXX

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed before text changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed on text changed
            }

            override fun afterTextChanged(s: Editable?) {
                validatePhoneNumber(s.toString())
            }
        })
    }

    private fun validatePhoneNumber(phoneNumber: String) {
        if (!phoneRegex.matches(phoneNumber)) {
            setError(resources.getString(R.string.invalid_phone_number))
        } else {
            setError(null)
        }
    }
}
