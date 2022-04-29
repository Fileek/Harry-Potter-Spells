package com.epam.harrypotterspells.ext

import android.content.Context
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import com.epam.harrypotterspells.entities.JsonSpell
import com.epam.harrypotterspells.entities.Spell

private const val INCANTATION_STUB = "Unknown"
private const val CAN_BE_VERBAL = "Yes"
private const val CAN_NOT_BE_VERBAL = "No"
private const val CAN_BE_VERBAL_STUB = "Unknown"
private const val CREATOR_STUB = "Unknown"

fun View.focusAndShowKeyboard() {
    /**
     * This is to be called when the window already has focus.
     */
    fun View.showTheKeyboardNow() {
        if (isFocused) {
            post {
                // We still post the call, just in case we are being notified of the windows focus
                // but InputMethodManager didn't get properly setup yet.
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

    requestFocus()
    if (hasWindowFocus()) {
        // No need to wait for the window to get focus.
        showTheKeyboardNow()
    } else {
        // We need to wait until the window gets focus.
        viewTreeObserver.addOnWindowFocusChangeListener(
            object : ViewTreeObserver.OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    // This notification will arrive just before the InputMethodManager gets set up.
                    if (hasFocus) {
                        this@focusAndShowKeyboard.showTheKeyboardNow()
                        // It’s very important to remove this listener once we are done.
                        viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
    }
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun JsonSpell.toSpell(): Spell {
    return Spell(
        id = this.id,
        name = this.name,
        incantation = this.incantation ?: INCANTATION_STUB,
        effect = this.effect,
        canBeVerbal = when (this.canBeVerbal) {
            true -> CAN_BE_VERBAL
            false -> CAN_NOT_BE_VERBAL
            null -> CAN_BE_VERBAL_STUB
        },
        type = this.type,
        light = this.light,
        creator = this.creator ?: CREATOR_STUB
    )
}

fun Spell.toJsonSpell(): JsonSpell {
    return JsonSpell(
        id = this.id,
        name = this.name,
        incantation = this.incantation,
        effect = this.effect,
        canBeVerbal = when (this.canBeVerbal) {
            CAN_BE_VERBAL -> true
            CAN_NOT_BE_VERBAL -> false
            else -> null
        },
        type = this.type,
        light = this.light,
        creator = this.creator
    )
}