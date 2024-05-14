
package com.example.carracegame

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity2 : AppCompatActivity(), GameTask {

    private lateinit var rootLayout: LinearLayout
    private lateinit var startBtn: Button
    private lateinit var pauseBtn: Button
    private lateinit var scoreTextView: TextView
    private lateinit var mGameView: GameView2
    private lateinit var sharedPreferences: SharedPreferences
    private var isPaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        sharedPreferences = getSharedPreferences("highScores", Context.MODE_PRIVATE)

        startBtn = findViewById(R.id.startbtn3)
        pauseBtn = findViewById(R.id.pauseBtn)
        rootLayout = findViewById(R.id.main)
        scoreTextView = findViewById(R.id.score3)

        val highScore = sharedPreferences.getInt("highScore", 0)
        scoreTextView.text = "High Score: $highScore"

        mGameView = GameView2(this, this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        startBtn.setOnClickListener {
            mGameView.setBackgroundResource(R.drawable.road3)
            rootLayout.addView(mGameView)
            startBtn.visibility = View.GONE
            pauseBtn.visibility = View.VISIBLE
            scoreTextView.visibility = View.GONE
        }

        pauseBtn.setOnClickListener {
            if (!isPaused) {
                pauseGame()
            } else {
                resumeGame()
            }
        }
        // Navigate to HomePage

        var homePageButton = findViewById<Button>(R.id.homePageButton)

        homePageButton.setOnClickListener() {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun pauseGame() {
        mGameView.pauseGame()
        isPaused = true
        pauseBtn.text = "Resume"
    }

    private fun resumeGame() {
        mGameView.resumeGame()
        isPaused = false
        pauseBtn.text = "Pause"
    }

    override fun closeGame(mScore: Int) {
        val currentHighScore = sharedPreferences.getInt("highScore", 0)
        if (mScore > currentHighScore) {
            sharedPreferences.edit().putInt("highScore", mScore).apply()
            scoreTextView.text = "High Score: $mScore"
        }

        scoreTextView.text = "Score : $mScore"
        rootLayout.removeView(mGameView)
        startBtn.visibility = View.GONE
        pauseBtn.visibility = View.GONE
        scoreTextView.visibility = View.VISIBLE


    }
}
