package com.aasati.hiltdemoapp.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.setVisible(visible: Boolean, notVisibleType: Int? = null) {
    visibility = if (visible) View.VISIBLE else (notVisibleType ?: View.GONE)
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

/**
 * hideKeyboard
 *
 * Given any view, this will climb the view tree and close the attached keyboard if there is one
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * showKeyboard
 *
 * Given any view, this method will show a keyboard for View.
 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    requestFocus()
    imm.showSoftInput(this, 0)
}