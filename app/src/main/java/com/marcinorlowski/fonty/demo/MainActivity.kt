package com.marcinorlowski.fonty.demo

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast

import com.google.android.material.textfield.TextInputLayout
import com.marcinorlowski.fonty.Fonty

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder

/**
 ******************************************************************************
 *
 * Copyright 2013-2019 Marcin Orlowski <github@MarcinOrlowski.com>
 *
 * Licensed under the Apache License 2.0
 *
 ******************************************************************************
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mDrawerToggle: ActionBarDrawerToggle

    private lateinit var mButterKnifeUnbinder: Unbinder

    private var mHandler = Handler()

    // Runnable that hides TIL's error messages
    private var mRunnable: Runnable = Runnable {
        val ids = intArrayOf(R.id.til1, R.id.til2)
        for (id in ids) {
            (findViewById<View>(id) as TextInputLayout).error = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.toolbar_title)
        toolbar.subtitle = getString(R.string.toolbar_subtitle)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar

        if (actionBar != null) {
            val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

            actionBar.setDisplayHomeAsUpEnabled(true)
            mDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.toggle_opend, R.string.toggle_closed) {
                override fun onDrawerClosed(drawerView: View) {
                    super.onDrawerClosed(drawerView)
                    invalidateOptionsMenu()
                }

                override fun onDrawerOpened(drawerView: View) {
                    super.onDrawerOpened(drawerView)
                    invalidateOptionsMenu()
                }
            }
            mDrawerToggle.isDrawerIndicatorEnabled = true

            drawerLayout.addDrawerListener(mDrawerToggle)
            mDrawerToggle.syncState()
        }

        // apply our fonts now
        Fonty.setFonts(this)

        mButterKnifeUnbinder = ButterKnife.bind(this)
    }

    override fun onDestroy() {
        mButterKnifeUnbinder.unbind()
        super.onDestroy()
    }

    @OnClick(R.id.github)
    fun clickGithub() {
        try {
            startActivity(
                    Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://github.com/MarcinOrlowski/fonty/")
                    )
            )
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this@MainActivity, R.string.error_browser_failed, Toast.LENGTH_SHORT).show()
        }
    }

    @OnClick(R.id.button)
    fun clickButton() {
        val ids = intArrayOf(R.id.til1, R.id.til2)
        for (id in ids) {
            val til = findViewById<TextInputLayout>(id)
            til.isErrorEnabled = true
            til.error = getString(R.string.til_error)
        }

        mHandler.removeCallbacks(mRunnable)
        mHandler.postDelayed(mRunnable, (6 * 1000).toLong())
    }
}
