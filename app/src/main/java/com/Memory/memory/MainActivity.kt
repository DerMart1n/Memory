package com.Memory.memory

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() 
{
    private var buttonCol: Int = 0

    private val prefsFilename = "com.memory.settings.prefs"
    private val darkModeFile = "dark_mode"
    private val difficultyFile = "difficulty"

    private var prefs: SharedPreferences? = null
    private var darkMode: Boolean = false
    private var difficulty: String = "medium"

    private lateinit var newGameButton: Button
    private lateinit var settingsButton: Button
    
    override fun onCreate(savedInstanceState: Bundle?) 
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newGameButton = findViewById(R.id.new_game_button)
        settingsButton = findViewById(R.id.settings_button)

        newGameButton.setOnClickListener { newGameFunc() }
        settingsButton.setOnClickListener { settingsFunc() }

        prefs = getSharedPreferences(prefsFilename, 0)
        darkMode = prefs?.getBoolean(darkModeFile, false)!!
        difficulty = prefs?.getString(difficultyFile, "medium")!!

        colorMode()

        // force portrait mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
    // starts a new game by starting the game activity
    private fun newGameFunc()
    {
        //val gameIntent: Intent = when(difficulty)
        this.startActivity (
            when(difficulty)
            {
                "easy" -> Intent(this, GameEasy::class.java)
                "medium" -> Intent(this, GameMedium::class.java)
                "hard" -> Intent(this, GameHard::class.java)
                else -> { Intent(this, GameMedium::class.java) }
            }
        )
    }
    // shows the settings page
    private fun settingsFunc()
    {
        val settingsIntent = Intent(this, Settings::class.java)
        this.startActivity(settingsIntent)
    }

    private fun colorMode()
    {
        buttonCol = if (darkMode) ContextCompat.getColor(this, R.color.colorPrimaryDark) else ContextCompat.getColor(this, R.color.buttonColForLight)
        // change button color
        newGameButton.setBackgroundColor(buttonCol)
        settingsButton.setBackgroundColor(buttonCol)

        // change layout background color
        val bgCol = if (darkMode) ContextCompat.getColor(this, R.color.bgColForDark) else ContextCompat.getColor(this, R.color.bgColForLight)
        val mainLayout: ConstraintLayout = findViewById(R.id.main_layout)
        mainLayout.setBackgroundColor(bgCol)

    }
}