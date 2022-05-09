package com.epam.harrypotterspells.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

const val TAG = "WhatIsGoingOn"

fun EditText.focusAndShowKeyboard() {

    requestFocus()
    if (hasWindowFocus() && isFocused) {
        post {
            // We still post the call, just in case we are being notified of the windows focus
            // but InputMethodManager didn't get properly setup yet.
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
    }
    setSelection(text.length)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

