package com.Memory.memory

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat


class Game : AppCompatActivity() {

    private val prefsFilename = "com.memory.settings.prefs"
    private val timeFile = "time"
    private val triesFile = "tries"
    private val darkModeFile = "dark_mode"

    private var prefs: SharedPreferences? = null


    private var secondsLeft: Int = 120
    // flipping two cards counts as one try
    private var triesLeft: Int = 40

    private var darkmode: Boolean = false

    // timer and tries-counter text views
    private lateinit var timeText: TextView
    private lateinit var triesText: TextView

    // maps values to the cards/buttons, for every value there are two buttons
    private var cardsValMap: HashMap<Int, Char> = HashMap()
    // the number of cards that are currently flipped up
    private var flippedNum: Int = 0
    // the IDs of the currently flipped up cards. 0 means not flipped up
    private var flippedCard1: Int = 0
    private var flippedCard2: Int = 0
    // a list of the found pairs
    private var foundCards: MutableSet<Int> = mutableSetOf()
    // this is set to false when the game ends
    private var runCounter: Boolean = true
    // the button color will be changed according to dark/lightmode
    private var buttonCol: Int = 0x000000

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // the cards to turn
        val b1 = findViewById<Button>(R.id.b1)
        val b2 = findViewById<Button>(R.id.b2)
        val b3 = findViewById<Button>(R.id.b3)
        val b4 = findViewById<Button>(R.id.b4)
        val b5 = findViewById<Button>(R.id.b5)
        val b6 = findViewById<Button>(R.id.b6)
        val b7 = findViewById<Button>(R.id.b7)
        val b8 = findViewById<Button>(R.id.b8)
        val b9 = findViewById<Button>(R.id.b9)
        val b10 = findViewById<Button>(R.id.b10)
        val b11 = findViewById<Button>(R.id.b11)
        val b12 = findViewById<Button>(R.id.b12)
        val b13 = findViewById<Button>(R.id.b13)
        val b14 = findViewById<Button>(R.id.b14)
        val b15 = findViewById<Button>(R.id.b15)
        val b16 = findViewById<Button>(R.id.b16)
        val b17 = findViewById<Button>(R.id.b17)
        val b18 = findViewById<Button>(R.id.b18)
        val b19 = findViewById<Button>(R.id.b19)
        val b20 = findViewById<Button>(R.id.b20)
        val b21 = findViewById<Button>(R.id.b21)
        val b22 = findViewById<Button>(R.id.b22)
        val b23 = findViewById<Button>(R.id.b23)
        val b24 = findViewById<Button>(R.id.b24)

        b1.setOnClickListener { bFunc(1) }
        b2.setOnClickListener { bFunc(2) }
        b3.setOnClickListener { bFunc(3) }
        b4.setOnClickListener { bFunc(4) }
        b5.setOnClickListener { bFunc(5) }
        b6.setOnClickListener { bFunc(6) }
        b7.setOnClickListener { bFunc(7) }
        b8.setOnClickListener { bFunc(8) }
        b9.setOnClickListener { bFunc(9) }
        b10.setOnClickListener { bFunc(10) }
        b11.setOnClickListener { bFunc(11) }
        b12.setOnClickListener { bFunc(12) }
        b13.setOnClickListener { bFunc(13) }
        b14.setOnClickListener { bFunc(14) }
        b15.setOnClickListener { bFunc(15) }
        b16.setOnClickListener { bFunc(16) }
        b17.setOnClickListener { bFunc(17) }
        b18.setOnClickListener { bFunc(18) }
        b19.setOnClickListener { bFunc(19) }
        b20.setOnClickListener { bFunc(20) }
        b21.setOnClickListener { bFunc(21) }
        b22.setOnClickListener { bFunc(22) }
        b23.setOnClickListener { bFunc(23) }
        b24.setOnClickListener { bFunc(24) }

        // get settings from preferences
        prefs = getSharedPreferences(prefsFilename, 0)

        secondsLeft = prefs?.getInt(timeFile, 120)!! + 1
        // flipping two cards counts as one try
        triesLeft = prefs?.getInt(triesFile, 40)!!

        darkmode = prefs?.getBoolean(darkModeFile, false)!!

        // menu button
        val menuButton = findViewById<Button>(R.id.menu_button)
        menuButton.setOnClickListener { returnToMenu() }

        // assign textViews to the timer and tries variables
        timeText = findViewById(R.id.time_left_text)
        triesText = findViewById(R.id.tries_left_text)

        // change button and layout background color according to dark/lightmode
        colorMode()

        // give the cards their values
        setUp()

        // start timer
        startTimer()
    }

    private fun colorMode()
    {
        buttonCol = if (darkmode) ContextCompat.getColor(this, R.color.colorPrimaryDark) else ContextCompat.getColor(this, R.color.buttonColForLight)
        // change button color
        for (i in 1..24) {
            val b = buttonFromNum(i)
            b.setBackgroundColor(buttonCol)
        }
        // change layout background color
        val bgCol = if (darkmode) ContextCompat.getColor(this, R.color.bgColForDark) else ContextCompat.getColor(this, R.color.bgColForLight)
        val gameLayout: ConstraintLayout = findViewById(R.id.game_layout)
        gameLayout.setBackgroundColor(bgCol)

        // change background color of the endTextView
        val endTextView: TextView = findViewById(R.id.endGameText)
        endTextView.setBackgroundColor(buttonCol)

    }

    private fun startTimer()
    {
        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run()
            {
                secondsLeft -= 1
                timeText.text = secondsLeft.toString()
                if (secondsLeft <= 0)
                {
                    endGame("Time is up.")
                }
                if (runCounter)
                {
                    mainHandler.postDelayed(this, 999)
                }
            }
        })
    }

    private fun endGame(msg: String)
    {
        // prevent the counter from continuing
        runCounter = false

        val endTextView: TextView = findViewById(R.id.endGameText)
        // make the endTextView visible and the cards invisible
        endTextView.visibility = VISIBLE
        for (i in 1..24)
        {
            val b: Button = buttonFromNum(i)
            b.visibility = INVISIBLE
        }
        // Set the text of the endTextView to display the msg, cards flipped and the tries/time left
        val pairsFound = (foundCards.size / 2).toInt()
        val triesNeeded = prefs?.getInt(triesFile, 40)!! - triesLeft
        when
        {
            secondsLeft == 0 -> endTextView.text = "$msg\nTries left: $triesLeft\nPairs found: $pairsFound/12"
            triesLeft == 0 -> endTextView.text = "$msg\nSeconds left: $secondsLeft\nPairs found: $pairsFound/12"
            else -> { endTextView.text = "$msg\nTries used: $triesNeeded" }
        }
    }

    private fun setUp()
    {
        // map the buttons to a randomized list of 12 pairs
        val valsList: MutableList<Char> = mutableListOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l')
        valsList.shuffle()

        for (i in 1..24) {
            cardsValMap[i] = valsList[i-1]
        }

        // initialise the text for the triesText View
        triesText.text = triesLeft.toString()
    }

    private fun returnToMenu()
    {
        // return to the activity_main (the menu)
        val menuIntent = Intent(this, MainActivity::class.java)
        this.startActivity(menuIntent)
    }

    private fun decreaseTries()
    {
        triesLeft -= 1
        // update textView
        triesText.text = triesLeft.toString()
        // end the game if there are no tries left
        if (triesLeft <= 0)
        {
            endGame("You have no tries left.")
        }
    }

    private fun buttonFromNum(n: Int): Button
    {
        return when(n)
        {
            1 -> findViewById(R.id.b1)
            2 -> findViewById(R.id.b2)
            3 -> findViewById(R.id.b3)
            4 -> findViewById(R.id.b4)
            5 -> findViewById(R.id.b5)
            6 -> findViewById(R.id.b6)
            7 -> findViewById(R.id.b7)
            8 -> findViewById(R.id.b8)
            9 -> findViewById(R.id.b9)
            10 -> findViewById(R.id.b10)
            11 -> findViewById(R.id.b11)
            12 -> findViewById(R.id.b12)
            13 -> findViewById(R.id.b13)
            14 -> findViewById(R.id.b14)
            15 -> findViewById(R.id.b15)
            16 -> findViewById(R.id.b16)
            17 -> findViewById(R.id.b17)
            18 -> findViewById(R.id.b18)
            19 -> findViewById(R.id.b19)
            20 -> findViewById(R.id.b20)
            21 -> findViewById(R.id.b21)
            22 -> findViewById(R.id.b22)
            23 -> findViewById(R.id.b23)
            24 -> findViewById(R.id.b24)
            else -> { findViewById(R.id.b1) }
        }
    }

    private fun bFunc(n: Int)
    {   // if the clicked card is not yet flipped up
        if (!foundCards.contains(n) && flippedCard1 != n && flippedCard2 != n)
        {
            val b: Button = buttonFromNum(n)
            val b1: Button = buttonFromNum(flippedCard1)
            val b2: Button = buttonFromNum(flippedCard2)

            // if this is the first card to be flipped: flip it.
            when (flippedNum)
            {
                0 -> {
                    b.text = cardsValMap[n].toString()
                    // aRGB light green
                    b.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                    flippedCard1 = n
                    flippedNum = 1
                }
                1 -> {
                    b.text = cardsValMap[n].toString()
                    b.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                    flippedCard2 = n
                    flippedNum = 2

                    // if we found a pair: add those cards to foundCards
                    if (cardsValMap[flippedCard1] == cardsValMap[flippedCard2])
                    {
                        println(foundCards.size)
                        foundCards.add(flippedCard1)
                        foundCards.add(flippedCard2)
                        disappearButtons(flippedCard1, flippedCard2)
                    }
                    // having flipped two cards now, the player has one try less
                    decreaseTries()
                }
                2 -> { // if the last two flipped cards were not a pair: flip them back down
                    if (cardsValMap[flippedCard1] != cardsValMap[flippedCard2])
                    {
                        b1.text = ""
                        b2.text = ""
                        b1.setBackgroundColor(buttonCol)
                        b2.setBackgroundColor(buttonCol)
                    }
                    // now deal with the clicked card
                    flippedCard1 = n
                    flippedCard2 = 0
                    flippedNum = 1
                    b.text = cardsValMap[n].toString()
                    b.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                }
            }
        }
        else
        {
            println("This card is already flipped up.")
        }
        // if all 12 pairs were found
        if (foundCards.size == 24)
        {
            endGame("Congratulations,\nyou won!")
        }
    }

    private fun disappearButtons(buttonId1: Int, buttonId2: Int)
    {
        val button1: Button = buttonFromNum(buttonId1)
        val button2: Button = buttonFromNum(buttonId2)

        val vanishDuration: Long = 600

        val animatorVanish1: ObjectAnimator = ObjectAnimator.ofFloat(button1, "alpha", 1f, 0f)
        animatorVanish1.duration = vanishDuration

        val animatorVanish2: ObjectAnimator = ObjectAnimator.ofFloat(button2, "alpha", 1f, 0f)
        animatorVanish2.duration = vanishDuration

        val animatorSetVanish = AnimatorSet()
        animatorSetVanish.playTogether(animatorVanish1, animatorVanish2)
        animatorSetVanish.interpolator = LinearInterpolator()
        animatorSetVanish.start()
    }
}