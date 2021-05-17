package com.example.tic_tac_toe

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.textfield.TextInputEditText
class MainActivity : AppCompatActivity() {
    private var gameStatus: Boolean = true //true if someone has not won yet
    private var count: Int = 0 // No of steps
    private var xScore: Int = 0 //Score of player1
    private var oScore: Int = 0 //score of player2
    private var firstActive: Int = 1
    private lateinit var lastCheckedBox: ImageView
    var undo: Boolean = true
    private var playerX: String = "Player1"
    private var playerO: String = "Player2"

    // 1 - x
    // 0 - 0
    private var activePlayer: Int = 1
    private var gameState = Array(9) { 2 }
    private val winState1 = arrayOf(0, 1, 2)
    private val winState2 = arrayOf(3, 4, 5)
    private val winState3 = arrayOf(6, 7, 8)
    private val winState4 = arrayOf(0, 3, 6)
    private val winState5 = arrayOf(1, 4, 7)
    private val winState6 = arrayOf(2, 5, 8)
    private val winState7 = arrayOf(0, 4, 8)
    private val winState8 = arrayOf(2, 4, 6)
    private val winState = arrayOf(
        winState1,
        winState2,
        winState3,
        winState4,
        winState5,
        winState6,
        winState7,
        winState8
    )

    fun clicked(view: View) {
        val img: ImageView = view as ImageView
        val clickedImageNo: Int = Integer.parseInt(img.tag.toString())
        if (!gameStatus || count == 9) {
            if (count == 9)
                findViewById<TextView>(R.id.status).text = "Game Tie"
            resetGame()
        } else {
            if (gameState[clickedImageNo] == 2) {
                undo = false
                gameState[clickedImageNo] = activePlayer
                lastCheckedBox = img

                if (activePlayer == 1) {
                    activePlayer = 0
                    view.translationX = -1000f
                    view.setImageResource(R.drawable.x)
                    view.animate().translationXBy(1000f).duration = 150

                    findViewById<TextView>(R.id.status).text = getString(R.string.Turn, playerO)

                    count++
                    if (checkWin()) {
                        displayWin(playerX)
                        xScore++
                        findViewById<TextView>(R.id.Xscore).text =
                            getString(R.string.Score, xScore.toString())
                    }

                } else {
                    view.translationX = -1000f
                    view.setImageResource(R.drawable.o)
                    view.animate().translationXBy(1000f).duration = 150
                    findViewById<TextView>(R.id.status).text = getString(R.string.Turn, playerX)
                    activePlayer = 1
                    count++
                    if (checkWin()) {
                        displayWin(playerO)
                        oScore++
                        findViewById<TextView>(R.id.Oscore).text =
                            getString(R.string.Score, oScore.toString())
                    }
                }
                if (count == 9 && !checkWin()) {
                    val msg = "Game Tie"
                    findViewById<TextView>(R.id.status).text = msg
                    undo = true
                    firstActive = activePlayer

                }
            }
        }

    }

    private fun displayWin(player: String) {
        val msg: String = getString(R.string.Win, player)
        findViewById<TextView>(R.id.status).text = msg
        firstActive = activePlayer
        undo = true
    }

    private fun checkWin(): Boolean {
        for (i in 0..7) {
            if (gameState[(winState[i][1])] == gameState[(winState[i][0])] && gameState[(winState[i][1])] == gameState[(winState[i][2])] && gameState[(winState[i][0])] != 2) {
                gameStatus = false
                return true
            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        findViewById<TextView>(R.id.status).text = getString(R.string.Turn, playerX)
        findViewById<TextView>(R.id.Xscore).text = getString(R.string.Score, xScore.toString())
        findViewById<TextView>(R.id.Oscore).text = getString(R.string.Score, oScore.toString())

        findViewById<TextInputEditText>(R.id.OName).setOnKeyListener { view, keyCode, _ ->
            handleKey(
                view,
                keyCode
            )
        }

        findViewById<TextInputEditText>(R.id.XName).setOnKeyListener { view, keyCode, _ ->
            handleKey(
                view,
                keyCode
            )
        }

        findViewById<Button>(R.id.undoBtn).setOnClickListener {
            undo()
        }
        findViewById<Button>(R.id.resetBtn).setOnClickListener {
            activePlayer = firstActive
            xScore = 0
            oScore = 0
            findViewById<TextView>(R.id.Xscore).text = getString(R.string.Score, xScore.toString())
            findViewById<TextView>(R.id.Oscore).text = getString(R.string.Score, oScore.toString())
            resetGame()
        }
    }

    fun resetGame() {
        getNames()
        count = 0

        gameStatus = true
        gameState.fill(2, 0, 9)
        if (activePlayer == 1)
            findViewById<TextView>(R.id.status).text = getString(R.string.Turn, playerX)
        else
            findViewById<TextView>(R.id.status).text = getString(R.string.Turn, playerO)
        findViewById<ImageView>(R.id.image0).setImageResource(0)
        findViewById<ImageView>(R.id.image1).setImageResource(0)
        findViewById<ImageView>(R.id.image2).setImageResource(0)
        findViewById<ImageView>(R.id.image3).setImageResource(0)
        findViewById<ImageView>(R.id.image4).setImageResource(0)
        findViewById<ImageView>(R.id.image5).setImageResource(0)
        findViewById<ImageView>(R.id.image6).setImageResource(0)
        findViewById<ImageView>(R.id.image7).setImageResource(0)
        findViewById<ImageView>(R.id.image8).setImageResource(0)
    }

    private fun undo() {
        if (undo) {
            return
        }
        count--
        lastCheckedBox.setImageResource(0)
        gameState[Integer.parseInt(lastCheckedBox.tag.toString())] = 2
        if (activePlayer == 1) {
            activePlayer = 0
            findViewById<TextView>(R.id.status).text = getString(R.string.Turn, playerO)
        } else {
            activePlayer = 1
            findViewById<TextView>(R.id.status).text = getString(R.string.Turn, playerX)
        }
        undo = true

    }

    private fun getNames() {
        val p1: TextInputEditText = findViewById(R.id.XName)
        val p2: TextInputEditText = findViewById(R.id.OName)
        if (p1.text.toString() == "") {
            p1.setText("Player1")
        }
        if (p2.text.toString() == "") {
            p2.setText("Player2")
        }
        playerX = p1.text.toString()
        playerO = p2.text.toString()

        if (activePlayer == 1)
            findViewById<TextView>(R.id.status).text = getString(R.string.Turn, playerX)
        else
            findViewById<TextView>(R.id.status).text = getString(R.string.Turn, playerO)
    }

    private fun handleKey(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            val input: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            input.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
            getNames()
            return true
        }
        return false
    }
}