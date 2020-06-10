package com.Memory.memory

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class Settings : AppCompatActivity(), CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener
{
    private val prefsFilename = "com.memory.settings.prefs"
    private val darkModeFile = "dark_mode"
    private val difficultyFile = "difficulty"

    private lateinit var prefs: SharedPreferences

    private var darkmode: Boolean = false
    private var difficulty: String = "medium"


    // the settings switch, button and radioButtons
    private lateinit var darkModeSwitch: Switch
    private lateinit var menuButton: Button
    private lateinit var difficultyButtons: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        prefs = getSharedPreferences(prefsFilename, 0)

        // 0: lightmode, 1: darkmode
        darkmode = prefs.getBoolean(darkModeFile, false)
        difficulty = prefs.getString(difficultyFile, "medium").toString()

        // the settings slider, switch and button
        darkModeSwitch = findViewById(R.id.dark_mode_switch)
        menuButton = findViewById(R.id.menu_button_settings)
        difficultyButtons = findViewById(R.id.difficultyRadioGroup)


        // update switch and textView to data from prefs
        darkModeSwitch.isChecked = darkmode

        when(difficulty) {
            "easy" -> findViewById<RadioButton>(R.id.radioButtonEasy).isChecked = true
            "medium" -> findViewById<RadioButton>(R.id.radioButtonMedium).isChecked = true
            "hard" -> findViewById<RadioButton>(R.id.radioButtonHard).isChecked = true
            else -> {findViewById<RadioButton>(R.id.radioButtonEasy).isChecked = true}
        }

        // add listeners to the switch and button
        darkModeSwitch.setOnCheckedChangeListener(this)
        menuButton.setOnClickListener {returnToMenu()}
        difficultyButtons.setOnCheckedChangeListener(this)

        colorMode()

        // force portrait mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(checkedId)
        {
            R.id.radioButtonEasy -> {
                difficulty = "easy"
            }
            R.id.radioButtonMedium -> {
                difficulty = "medium"
            }
            R.id.radioButtonHard -> {
                difficulty = "hard"
            }
        }
        prefs.edit().putString(difficultyFile, difficulty).apply()
    }
}
