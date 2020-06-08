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


class GameMedium : AppCompatActivity() {

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
        setContentView(R.layout.activity_game_medium)

        // the cards to turn
        val bm1 = findViewById<Button>(R.id.bm1)
        val bm2 = findViewById<Button>(R.id.bm2)
        val bm3 = findViewById<Button>(R.id.bm3)
        val bm4 = findViewById<Button>(R.id.bm4)
        val bm5 = findViewById<Button>(R.id.bm5)
        val bm6 = findViewById<Button>(R.id.bm6)
        val bm7 = findViewById<Button>(R.id.bm7)
        val bm8 = findViewById<Button>(R.id.bm8)
        val bm9 = findViewById<Button>(R.id.bm9)
        val bm10 = findViewById<Button>(R.id.bm10)
        val bm11 = findViewById<Button>(R.id.bm11)
        val bm12 = findViewById<Button>(R.id.bm12)
        val bm13 = findViewById<Button>(R.id.bm13)
        val bm14 = findViewById<Button>(R.id.bm14)
        val bm15 = findViewById<Button>(R.id.bm15)
        val bm16 = findViewById<Button>(R.id.bm16)
        val bm17 = findViewById<Button>(R.id.bm17)
        val bm18 = findViewById<Button>(R.id.bm18)
        val bm19 = findViewById<Button>(R.id.bm19)
        val bm20 = findViewById<Button>(R.id.bm20)
        val bm21 = findViewById<Button>(R.id.bm21)
        val bm22 = findViewById<Button>(R.id.bm22)
        val bm23 = findViewById<Button>(R.id.bm23)
        val bm24 = findViewById<Button>(R.id.bm24)
        val bm25 = findViewById<Button>(R.id.bm25)
        val bm26 = findViewById<Button>(R.id.bm26)
        val bm27 = findViewById<Button>(R.id.bm27)
        val bm28 = findViewById<Button>(R.id.bm28)

        bm1.setOnClickListener { bFunc(1) }
        bm2.setOnClickListener { bFunc(2) }
        bm3.setOnClickListener { bFunc(3) }
        bm4.setOnClickListener { bFunc(4) }
        bm5.setOnClickListener { bFunc(5) }
        bm6.setOnClickListener { bFunc(6) }
        bm7.setOnClickListener { bFunc(7) }
        bm8.setOnClickListener { bFunc(8) }
        bm9.setOnClickListener { bFunc(9) }
        bm10.setOnClickListener { bFunc(10) }
        bm11.setOnClickListener { bFunc(11) }
        bm12.setOnClickListener { bFunc(12) }
        bm13.setOnClickListener { bFunc(13) }
        bm14.setOnClickListener { bFunc(14) }
        bm15.setOnClickListener { bFunc(15) }
        bm16.setOnClickListener { bFunc(16) }
        bm17.setOnClickListener { bFunc(17) }
        bm18.setOnClickListener { bFunc(18) }
        bm19.setOnClickListener { bFunc(19) }
        bm20.setOnClickListener { bFunc(20) }
        bm21.setOnClickListener { bFunc(21) }
        bm22.setOnClickListener { bFunc(22) }
        bm23.setOnClickListener { bFunc(23) }
        bm24.setOnClickListener { bFunc(24) }
        bm25.setOnClickListener { bFunc(25) }
        bm26.setOnClickListener { bFunc(26) }
        bm27.setOnClickListener { bFunc(27) }
        bm28.setOnClickListener { bFunc(28) }

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
        for (i in 1..28) {
            val b = buttonFromNum(i)
            b.setBackgroundColor(buttonCol)
        }
        // change layout background color
        val bgCol = if (darkmode) ContextCompat.getColor(this, R.color.bgColForDark) else ContextCompat.getColor(this, R.color.bgColForLight)
        val gameLayout: ConstraintLayout = findViewById(R.id.game_layout_medium)
        gameLayout.setBackgroundColor(bgCol)

        // change background color of the endTextView
        val endTextView: TextView = findViewById(R.id.endGameTextMedium)
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

        val endTextView: TextView = findViewById(R.id.endGameTextMedium)
        // make the endTextView visible and the cards invisible
        endTextView.visibility = VISIBLE
        for (i in 1..28)
        {
            val b: Button = buttonFromNum(i)
            b.visibility = INVISIBLE
        }
        // Set the text of the endTextView to display the msg, cards flipped and the tries/time left
        val pairsFound = (foundCards.size / 2).toInt()
        val triesNeeded = prefs?.getInt(triesFile, 40)!! - triesLeft
        when
        {
            secondsLeft == 0 -> endTextView.text = "$msg\nTries left: $triesLeft\nPairs found: $pairsFound/14"
            triesLeft == 0 -> endTextView.text = "$msg\nSeconds left: $secondsLeft\nPairs found: $pairsFound/14"
            else -> { endTextView.text = "$msg\nTries used: $triesNeeded" }
        }
    }

    private fun setUp()
    {
        // map the buttons to a randomized list of 12 pairs
        val valsList: MutableList<Char> = mutableListOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n')
            valsList.shuffle()

        for (i in 1..28) {
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
            1 -> findViewById(R.id.bm1)
            2 -> findViewById(R.id.bm2)
            3 -> findViewById(R.id.bm3)
            4 -> findViewById(R.id.bm4)
            5 -> findViewById(R.id.bm5)
            6 -> findViewById(R.id.bm6)
            7 -> findViewById(R.id.bm7)
            8 -> findViewById(R.id.bm8)
            9 -> findViewById(R.id.bm9)
            10 -> findViewById(R.id.bm10)
            11 -> findViewById(R.id.bm11)
            12 -> findViewById(R.id.bm12)
            13 -> findViewById(R.id.bm13)
            14 -> findViewById(R.id.bm14)
            15 -> findViewById(R.id.bm15)
            16 -> findViewById(R.id.bm16)
            17 -> findViewById(R.id.bm17)
            18 -> findViewById(R.id.bm18)
            19 -> findViewById(R.id.bm19)
            20 -> findViewById(R.id.bm20)
            21 -> findViewById(R.id.bm21)
            22 -> findViewById(R.id.bm22)
            23 -> findViewById(R.id.bm23)
            24 -> findViewById(R.id.bm24)
            25 -> findViewById(R.id.bm25)
            26 -> findViewById(R.id.bm26)
            27 -> findViewById(R.id.bm27)
            28 -> findViewById(R.id.bm28)
            else -> { findViewById(R.id.bm1) }
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
        if (foundCards.size == 28)
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