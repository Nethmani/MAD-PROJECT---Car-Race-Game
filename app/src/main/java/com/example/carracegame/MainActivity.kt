package com.example.carracegame

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), GameTask {

    // Declare UI elements
    private lateinit var rootLayout: LinearLayout
    private lateinit var startBtn: Button
    private lateinit var pauseBtn: Button
    private lateinit var scoreTextView: TextView
    private lateinit var mGameView: GameView
    private lateinit var sharedPreferences: SharedPreferences
    private var isPaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize SharedPreferences for storing high scores
        sharedPreferences = getSharedPreferences("highScoresLevel1", Context.MODE_PRIVATE)

        // Initialize UI elements
        startBtn = findViewById(R.id.startbtn3)
        pauseBtn = findViewById(R.id.pauseBtn)
        rootLayout = findViewById(R.id.main)
        scoreTextView = findViewById(R.id.score)

        // Set initial high score text
        val highScore = sharedPreferences.getInt("highScore", 0)
        scoreTextView.text = "High Score: $highScore"

        // Create instance of GameView1
        mGameView = GameView(this, this)

        // Set padding to handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Start Button Click Listener
        startBtn.setOnClickListener {
            // Set background resource and add GameView to root layout
            mGameView.setBackgroundResource(R.drawable.road)
            rootLayout.addView(mGameView)
            // Update visibility of UI elements
            startBtn.visibility = View.GONE
            pauseBtn.visibility = View.VISIBLE
            scoreTextView.visibility = View.GONE
        }

        // Pause Button Click Listener
        pauseBtn.setOnClickListener {
            if (!isPaused) {
                pauseGame()
            } else {
                resumeGame()
            }
        }


    }

    // Function to pause the game
    private fun pauseGame() {
        mGameView.pauseGame()
        isPaused = true
        pauseBtn.text = "Resume"
    }

    // Function to resume the game
    private fun resumeGame() {
        mGameView.resumeGame()
        isPaused = false
        pauseBtn.text = "Pause"
    }

    // Function to handle game closure
    override fun closeGame(mScore: Int) {
        // Update high score if the current score is higher
        val currentHighScore = sharedPreferences.getInt("highScore", 0)
        if (mScore > currentHighScore) {
            sharedPreferences.edit().putInt("highScore", mScore).apply()
            scoreTextView.text = "High Score: $mScore"
        }

        // Update score text
        scoreTextView.text = "Score : $mScore"

        // Remove GameView from root layout and update UI element visibility
        rootLayout.removeView(mGameView)
        startBtn.visibility = View.GONE
        pauseBtn.visibility = View.GONE
        scoreTextView.visibility = View.VISIBLE

        // Navigate to HomePage Button Click Listener
        var homePageButton = findViewById<Button>(R.id.homePageButton)
        homePageButton.setOnClickListener() {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
            finish()
        }
    }
}
