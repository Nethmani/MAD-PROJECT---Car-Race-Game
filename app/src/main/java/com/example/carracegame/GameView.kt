package com.example.carracegame

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

class GameView(var c: Context, var gameTask: GameTask) : View(c) {
    private var myPaint: Paint? = null
    private var speed = 1
    private var time = 0
    private var score = 0
    private var myCarPosition = 0
    private val otherCars = ArrayList<HashMap<String, Any>>()
    private lateinit var sharedPreferences: SharedPreferences // Used for storing high scores
    private var  isPaused = false

    var viewWidth = 0
    var viewHeight = 0

    init {
        myPaint = Paint()
        sharedPreferences = c.getSharedPreferences("highScoresLevel1", Context.MODE_PRIVATE) // Initialize SharedPreferences for storing high scores
    }

    // Function to draw the game elements on the canvas
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = measuredWidth
        viewHeight = measuredHeight

        if (!isPaused) {
            // Generate new cars at random intervals
            if (time % 700 < 10 + speed) {
                val map = HashMap<String, Any>()
                map["lane"] = (0..2).random() // Randomly select a lane for the new car
                map["startTime"] = time // Store the start time of the new car
                otherCars.add(map) // Add the new car to the list
            }
            time += 10 + speed // Increment time based on speed
        }

        // Calculate dimensions for the player's car
        val carWidth = viewWidth / 5
        val carHeight = carWidth + 10

        // Draw the player's car on the canvas
        myPaint!!.style = Paint.Style.FILL
        val d = resources.getDrawable(R.drawable.red_car, null)
        d.setBounds(
            myCarPosition * viewWidth / 3 + viewWidth / 15 + 25,
            viewHeight - 2 - carHeight,
            myCarPosition * viewWidth / 3 + viewWidth / 15 + carWidth - 25,
            viewHeight - 2
        )
        d.draw(canvas!!)

        // Draw other cars on the canvas and handle collisions
        myPaint!!.color = Color.GREEN
        var highScore = sharedPreferences.getInt("highScore", 0) // Get the high score from SharedPreferences
        for (i in otherCars.indices) {
            try {
                val carX = otherCars[i]["lane"] as Int * viewWidth / 3 + viewWidth / 15 // Calculate x-coordinate of the car
                var carY = time - (otherCars[i]["startTime"] as Int)
                val d2 = resources.getDrawable(R.drawable.yellow_car, null)

                // Draw the other car on the canvas
                d2.setBounds(
                    carX + 25, carY - carHeight, carX + carWidth - 25, carY
                )
                d2.draw(canvas)

                // Check for collisions with the player's car
                if (otherCars[i]["lane"] as Int == myCarPosition) {
                    if (carY > viewHeight - 2 - carHeight && carY < viewHeight - 2) {
                        gameTask.closeGame(score) // End the game if there is a collision
                    }
                }

                // Remove cars that have passed the player's car and update score and speed
                if (carY > viewHeight + carHeight) {
                    otherCars.removeAt(i)
                    score++
                    speed = 1 + Math.abs(score / 8)
                    if (score > highScore) {
                        highScore = score
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // display high score, current score, and speed on the canvas
        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 40f
        canvas.drawText("High Score: $highScore", 80f, 150f, myPaint!!)
        canvas.drawText("Score : $score", 80f, 200f, myPaint!!)
        canvas.drawText("Speed : $speed", 380f, 200f, myPaint!!)

        invalidate() // Invalidate the view to trigger a redraw
    }

    // Function to handle touch events
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isPaused) {
            when (event!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    val x1 = event.x
                    // Move the player's car based on the touch position
                    if (x1 < viewWidth / 2) {
                        if (myCarPosition > 0) {
                            myCarPosition--
                        }
                    }
                    if (x1 > viewWidth / 2) {
                        if (myCarPosition < 2) {
                            myCarPosition++
                        }
                    }
                    invalidate()
                }
                MotionEvent.ACTION_UP -> {
                }
            }
        }
        return true
    }

    // Function to pause the game
    fun pauseGame() {
        isPaused = true
    }

    // Function to resume the game
    fun resumeGame() {
        isPaused = false
    }
}
