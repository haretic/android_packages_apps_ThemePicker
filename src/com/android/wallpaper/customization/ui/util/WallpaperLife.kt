/*
 * SPDX-FileCopyrightText: The haretic Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.android.wallpaper.customization.ui.util

import android.app.WallpaperManager
import android.content.Context
import android.provider.Settings
import android.view.View
import com.android.themepicker.R
import com.google.android.material.materialswitch.MaterialSwitch

/**
 * haretic's living wallpaper, the still image's hares drawn alive, and the one setting it reads:
 * at 0 it shows the picture without its life. The row for it is there while that wallpaper is.
 */
object WallpaperLife {
    private const val PACKAGE = "org.haretic.wallpaper"
    private const val SETTING = "haretic_wallpaper_life"

    /** Whether the living wallpaper is the one that shows: its switch is for nothing else. */
    fun isAvailable(context: Context): Boolean =
        WallpaperManager.getInstance(context).wallpaperInfo?.packageName == PACKAGE

    /** The row toggles the setting when pressed; its switch shows where it stands. */
    fun bind(row: View) {
        val resolver = row.context.contentResolver
        val toggle: MaterialSwitch = row.requireViewById(R.id.option_entry_switch)
        toggle.isChecked = Settings.Secure.getInt(resolver, SETTING, 1) != 0
        row.stateDescription = stateOf(row.context, toggle.isChecked)
        row.setOnClickListener {
            val on = !toggle.isChecked
            Settings.Secure.putInt(resolver, SETTING, if (on) 1 else 0)
            toggle.isChecked = on
            row.stateDescription = stateOf(row.context, on)
        }
    }

    private fun stateOf(context: Context, on: Boolean): CharSequence =
        context.getString(if (on) R.string.wallpaper_life_on else R.string.wallpaper_life_off)
}
