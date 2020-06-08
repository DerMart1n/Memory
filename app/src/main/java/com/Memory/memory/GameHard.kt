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


class GameHard : AppCompatActivity() {

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
        setContentView(R.layout.activity_game_hard)

        // the cards to turn
        val bh1 = findViewById<Button>(R.id.bh1)
        val bh2 = findViewById<Button>(R.id.bh2)
        val bh3 = findViewById<Button>(R.id.bh3)
        val bh4 = findViewById<Button>(R.id.bh4)
        val bh5 = findViewById<Button>(R.id.bh5)
        val bh6 = findViewById<Button>(R.id.bh6)
        val bh7 = findViewById<Button>(R.id.bh7)
        val bh8 = findViewById<Button>(R.id.bh8)
        val bh9 = findViewById<Button>(R.id.bh9)
        val bh10 = findViewById<Button>(R.id.bh10)
        val bh11 = findViewById<Button>(R.id.bh11)
        val bh12 = findViewById<Button>(R.id.bh12)
        val bh13 = findViewById<Button>(R.id.bh13)
        val bh14 = findViewById<Button>(R.id.bh14)
        val bh15 = findViewById<Button>(R.id.bh15)
        val bh16 = findViewById<Button>(R.id.bh16)
        val bh17 = findViewById<Button>(R.id.bh17)
        val bh18 = findViewById<Button>(R.id.bh18)
        val bh19 = findViewById<Button>(R.id.bh19)
        val bh20 = findViewById<Button>(R.id.bh20)
        val bh21 = findViewById<Button>(R.id.bh21)
        val bh22 = findViewById<Button>(R.id.bh22)
        val bh23 = findViewById<Button>(R.id.bh23)
        val bh24 = findViewById<Button>(R.id.bh24)
        val bh25 = findViewById<Button>(R.id.bh25)
        val bh26 = findViewById<Button>(R.id.bh26)
        val bh27 = findViewById<Button>(R.id.bh27)
        val bh28 = findViewById<Button>(R.id.bh28)
        val bh29 = findViewById<Button>(R.id.bh29)
        val bh30 = findViewById<Button>(R.id.bh30)
        val bh31 = findViewById<Button>(R.id.bh31)
        val bh32 = findViewById<Button>(R.id.bh32)
        val bh33 = findViewById<Button>(R.id.bh33)
        val bh34 = findViewById<Button>(R.id.bh34)
        val bh35 = findViewById<Button>(R.id.bh35)
        val bh36 = findViewById<Button>(R.id.bh36)
        val bh37 = findViewById<Button>(R.id.bh37)
        val bh38 = findViewById<Button>(R.id.bh38)
        val bh39 = findViewById<Button>(R.id.bh39)
        val bh40 = findViewById<Button>(R.id.bh40)

        bh1.setOnClickListener { bFunc(1) }
        bh2.setOnClickListener { bFunc(2) }
        bh3.setOnClickListener { bFunc(3) }
        bh4.setOnClickListener { bFunc(4) }
        bh5.setOnClickListener { bFunc(5) }
        bh6.setOnClickListener { bFunc(6) }
        bh7.setOnClickListener { bFunc(7) }
        bh8.setOnClickListener { bFunc(8) }
        bh9.setOnClickListener { bFunc(9) }
        bh10.setOnClickListener { bFunc(10) }
        bh11.setOnClickListener { bFunc(11) }
        bh12.setOnClickListener { bFunc(12) }
        bh13.setOnClickListener { bFunc(13) }
        bh14.setOnClickListener { bFunc(14) }
        bh15.setOnClickListener { bFunc(15) }
        bh16.setOnClickListener { bFunc(16) }
        bh17.setOnClickListener { bFunc(17) }
        bh18.setOnClickListener { bFunc(18) }
        bh19.setOnClickListener { bFunc(19) }
        bh20.setOnClickListener { bFunc(20) }
        bh21.setOnClickListener { bFunc(21) }
        bh22.setOnClickListener { bFunc(22) }
        bh23.setOnClickListener { bFunc(23) }
        bh24.setOnClickListener { bFunc(24) }
        bh25.setOnClickListener { bFunc(25) }
        bh26.setOnClickListener { bFunc(26) }
        bh27.setOnClickListener { bFunc(27) }
        bh28.setOnClickListener { bFunc(28) }
        bh29.setOnClickListener { bFunc(29) }
        bh30.setOnClickListener { bFunc(30) }
        bh31.setOnClickListener { bFunc(31) }
        bh32.setOnClickListener { bFunc(32) }
        bh33.setOnClickListener { bFunc(33) }
        bh34.setOnClickListener { bFunc(34) }
        bh35.setOnClickListener { bFunc(35) }
        bh36.setOnClickListener { bFunc(36) }
        bh37.setOnClickListener { bFunc(37) }
        bh38.setOnClickListener { bFunc(38) }
        bh39.setOnClickListener { bFunc(39) }
        bh40.setOnClickListener { bFunc(40) }

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
        for (i in 1..40) {
            val b = buttonFromNum(i)
            b.setBackgroundColor(buttonCol)
        }
        // change layout background color
        val bgCol = if (darkmode) ContextCompat.getColor(this, R.color.bgColForDark) else ContextCompat.getColor(this, R.color.bgColForLight)
        val gameLayout: ConstraintLayout = findViewById(R.id.game_layout_hard)
        gameLayout.setBackgroundColor(bgCol)

        // change background color of the endTextView
        val endTextView: TextView = findViewById(R.id.endGameTextHard)
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

        val endTextView: TextView = findViewById(R.id.endGameTextHard)
        // make the endTextView visible and the cards invisible
        endTextView.visibility = VISIBLE
        for (i in 1..40)
        {
            val b: Button = buttonFromNum(i)
            b.visibility = INVISIBLE
        }
        // Set the text of the endTextView to display the msg, cards flipped and the tries/time left
        val pairsFound = (foundCards.size / 2).toInt()
        val triesNeeded = prefs?.getInt(triesFile, 40)!! - triesLeft
        when
        {
            secondsLeft == 0 -> endTextView.text = "$msg\nTries left: $triesLeft\nPairs found: $pairsFound/20"
            triesLeft == 0 -> endTextView.text = "$msg\nSeconds left: $secondsLeft\nPairs found: $pairsFound/20"
            else -> { endTextView.text = "$msg\nTries used: $triesNeeded" }
        }
    }

    private fun setUp()
    {
        // map the buttons to a randomized list of 12 pairs
        val valsList: MutableList<Char> = mutableListOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't')
            valsList.shuffle()

        for (i in 1..40) {
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
            1 -> findViewById(R.id.bh1)
            2 -> findViewById(R.id.bh2)
            3 -> findViewById(R.id.bh3)
            4 -> findViewById(R.id.bh4)
            5 -> findViewById(R.id.bh5)
            6 -> findViewById(R.id.bh6)
            7 -> findViewById(R.id.bh7)
            8 -> findViewById(R.id.bh8)
            9 -> findViewById(R.id.bh9)
            10 -> findViewById(R.id.bh10)
            11 -> findViewById(R.id.bh11)
            12 -> findViewById(R.id.bh12)
            13 -> findViewById(R.id.bh13)
            14 -> findViewById(R.id.bh14)
            15 -> findViewById(R.id.bh15)
            16 -> findViewById(R.id.bh16)
            17 -> findViewById(R.id.bh17)
            18 -> findViewById(R.id.bh18)
            19 -> findViewById(R.id.bh19)
            20 -> findViewById(R.id.bh20)
            21 -> findViewById(R.id.bh21)
            22 -> findViewById(R.id.bh22)
            23 -> findViewById(R.id.bh23)
            24 -> findViewById(R.id.bh24)
            25 -> findViewById(R.id.bh25)
            26 -> findViewById(R.id.bh26)
            27 -> findViewById(R.id.bh27)
            28 -> findViewById(R.id.bh28)
            29 -> findViewById(R.id.bh29)
            30 -> findViewById(R.id.bh30)
            31 -> findViewById(R.id.bh31)
            32 -> findViewById(R.id.bh32)
            33 -> findViewById(R.id.bh33)
            34 -> findViewById(R.id.bh34)
            35 -> findViewById(R.id.bh35)
            36 -> findViewById(R.id.bh36)
            37 -> findViewById(R.id.bh37)
            38 -> findViewById(R.id.bh38)
            39 -> findViewById(R.id.bh39)
            40 -> findViewById(R.id.bh40)
            else -> { findViewById(R.id.bh1) }
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
        if (foundCards.size == 40)
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