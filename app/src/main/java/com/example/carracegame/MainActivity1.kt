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
import com.example.carracegame.GameTask
import com.example.carracegame.GameView2
import com.example.carracegame.R

class MainActivity1 : AppCompatActivity(), GameTask {

    private lateinit var rootLayout: LinearLayout
    private lateinit var startBtn: Button
    private lateinit var pauseBtn: Button
    private lateinit var scoreTextView: TextView
    private lateinit var mGameView: GameView1
    private lateinit var sharedPreferences: SharedPreferences
    private var isPaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("highScoresLevel2", Context.MODE_PRIVATE)

        startBtn = findViewById(R.id.startbtn3)
        pauseBtn = findViewById(R.id.pauseBtn)
        rootLayout = findViewById(R.id.main)
        scoreTextView = findViewById(R.id.score)

        val highScore = sharedPreferences.getInt("highScore", 0)
        scoreTextView.text = "High Score: $highScore"

        mGameView = GameView1(this, this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        startBtn.setOnClickListener {
            mGameView.setBackgroundResource(R.drawable.road2)
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
