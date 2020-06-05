package com.Memory.memory

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class Settings : AppCompatActivity(), OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener
{
    private val prefsFilename = "com.memory.settings.prefs"
    private val timeFile = "time"
    private val triesFile = "tries"
    private val darkModeFile = "dark_mode"

    private lateinit var prefs: SharedPreferences

    private var time: Int = 0
    private var tries: Int = 0
    private var darkmode: Boolean = false

    // the settings slider, switch and button
    private lateinit var timeSlider: SeekBar
    private lateinit var triesSlider: SeekBar
    private lateinit var darkModeSwitch: Switch
    private lateinit var menuButton: Button

    // the textView above the sliders
    private lateinit var timeText: TextView
    private lateinit var triesText: TextView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        prefs = getSharedPreferences(prefsFilename, 0)

        time = prefs.getInt(timeFile, 130)
        tries = prefs.getInt(triesFile, 30)
        // 0: lightmode, 1: darkmode
        darkmode = prefs.getBoolean(darkModeFile, false)

        // the settings slider, switch and button
        timeSlider = findViewById(R.id.time_slider)
        triesSlider = findViewById(R.id.tries_slider)
        darkModeSwitch = findViewById(R.id.dark_mode_switch)
        menuButton = findViewById(R.id.menu_button_settings)

        // the textView above the sliders
        timeText = findViewById(R.id.time_slider_text)
        triesText = findViewById(R.id.tries_slider_text)

        // update sliders, switch and textView to data from prefs
        timeSlider.progress = time
        triesSlider.progress = tries
        timeText.text = "Time: $time"
        triesText.text = "Tries: $tries"
        darkModeSwitch.isChecked = darkmode

        // add listeners to the sliders, switch and button
        timeSlider.setOnSeekBarChangeListener(this)
        triesSlider.setOnSeekBarChangeListener(this)
        darkModeSwitch.setOnCheckedChangeListener(this)
        menuButton.setOnClickListener {returnToMenu()}

        colorMode()
    }
    // write change to file when one of the sliders change
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean)
    {
        if (seekBar != null) when (seekBar.id) {
            R.id.time_slider -> {
                time = seekBar.progress
                timeText.text = "Time: $time"
                prefs.edit().putInt(timeFile, time).apply()
            }
            R.id.tries_slider -> {
                tries = seekBar.progress
                triesText.text = "Tries: $tries"
                prefs.edit().putInt(triesFile, tries).apply()
            }
        }
    }
    // write changes of the switch to file
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean)
    {
        darkmode = darkModeSwitch.isChecked
        prefs.edit().putBoolean(darkModeFile, darkmode).apply()
        colorMode()
    }

    private fun colorMode()
    {
        val buttonCol = if (darkmode) ContextCompat.getColor(this, R.color.colorPrimaryDark) else ContextCompat.getColor(this, R.color.buttonColForLight)
        // change the menu buttons background color
        menuButton.setBackgroundColor(buttonCol)

        // change layout background color
        val bgCol = if (darkmode) ContextCompat.getColor(this, R.color.bgColForDark) else ContextCompat.getColor(this, R.color.bgColForLight)
        val settingsLayout: ConstraintLayout = findViewById(R.id.settings_layout)
        settingsLayout.setBackgroundColor(bgCol)
    }

    private fun returnToMenu()
    {
        val menuIntent = Intent(this, MainActivity::class.java)
        this.startActivity(menuIntent)
    }

    // these functions have to implemented for the seekbars
    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}
