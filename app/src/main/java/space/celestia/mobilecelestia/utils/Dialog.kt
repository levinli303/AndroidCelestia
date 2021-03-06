/*
 * Dialog.kt
 *
 * Copyright (C) 2001-2020, Celestia Development Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 */

package space.celestia.mobilecelestia.utils

import android.app.Activity
import android.app.AlertDialog
import android.text.InputType
import android.view.LayoutInflater
import android.widget.EditText
import space.celestia.mobilecelestia.R
import java.text.SimpleDateFormat
import java.util.*

fun Activity.showTextInput(title: String, placeholder: String? = null, handler: (String) -> Unit) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    val customView = LayoutInflater.from(this).inflate(R.layout.dialog_text_input, findViewById(android.R.id.content), false)

    val editText = customView.findViewById<EditText>(R.id.input)
    editText.inputType = InputType.TYPE_CLASS_TEXT
    if (placeholder != null)
        editText.setText(placeholder)
    builder.setView(customView)

    builder.setPositiveButton(CelestiaString("OK", "")) { _, _ ->
        handler(editText.text.toString())
    }

    builder.setNegativeButton(CelestiaString("Cancel", "")) { dialog, _ ->
        dialog.cancel()
    }
    builder.show()
}

fun Activity.showDateInput(title: String, format: String, handler: (Date?) -> Unit) {
    val formatter = SimpleDateFormat(format, Locale.US)
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    val customView = LayoutInflater.from(this).inflate(R.layout.dialog_text_input, findViewById(android.R.id.content), false)

    val editText = customView.findViewById<EditText>(R.id.input)
    editText.hint = formatter.format(Date())
    editText.inputType = InputType.TYPE_CLASS_TEXT
    builder.setView(customView)

    builder.setPositiveButton(CelestiaString("OK", "")) { _, _ ->
        try {
            val date = formatter.parse(editText.text.toString())
            handler(date)
        } catch (_: Exception) {
            handler(null)
        }
    }

    builder.setNegativeButton(CelestiaString("Cancel", "")) { dialog, _ ->
        dialog.cancel()
    }
    builder.show()
}

fun Activity.showSingleSelection(title: String, selections: List<String>, checkedIndex: Int, handler: (Int) -> Unit) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setSingleChoiceItems(selections.toTypedArray(), checkedIndex) { dialog, index ->
        handler(index)
        dialog.dismiss()
    }

    builder.show()
}

fun Activity.showOptions(title: String, options: Array<String>, handler: (Int) -> Unit) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setItems(options) { _, index ->
        handler(index)
    }
    builder.show()
}

fun Activity.showLoading(title: String, cancelHandler: (() -> Unit)? = null): AlertDialog {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    if (cancelHandler != null) {
        builder.setCancelable(true)
        builder.setNegativeButton(CelestiaString("Cancel", "")) { dialog, _ ->
            dialog.cancel()
            cancelHandler()
        }
    } else {
        builder.setCancelable(false)
    }
    return builder.show()
}

fun Activity.showAlert(title: String, handler: (() -> Unit)? = null) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setPositiveButton(CelestiaString("OK", "")) { _, _ ->
        if (handler != null)
            handler()
    }
    if (handler != null) {
        builder.setNegativeButton(CelestiaString("Cancel", "")) { dialog, _ ->
            dialog.cancel()
        }
    }
    builder.show()
}

fun Activity.showError(error: Throwable) {
    var message = error.message
    if (message == null)
        message = "Unknown error"
    showAlert(message)
}