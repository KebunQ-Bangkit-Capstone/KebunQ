package com.md.kebunq.data.customUI

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class CustomPassword @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputEditText(context, attrs, defStyleAttr) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Validasi panjang password
                if (!s.isNullOrEmpty() && s.length < 8) {
                    val textInputLayout = parent.parent as? TextInputLayout
//                    (parent.parent as? com.google.android.material.textfield.TextInputLayout)?.error =
//                        "Password tidak boleh kurang dari 8 karakter"
                    error = "Password tidak boleh kurang dari 8 karakter"
                    textInputLayout?.isEndIconVisible = false

                } else {
                    val textInputLayout = parent.parent as? TextInputLayout
                    (parent.parent as? com.google.android.material.textfield.TextInputLayout)?.error = null
                    textInputLayout?.isEndIconVisible = true
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
}