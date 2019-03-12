package hu.ait.minesweeper

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_difficulty_select.*

class DifficultySelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_difficulty_select)

        easyButton.setOnClickListener{
            startDifficulty("easy")
        }
        mediumButton.setOnClickListener{
            startDifficulty("medium")
        }
        hardButton.setOnClickListener {
            startDifficulty("hard")
        }
    }

    private fun startDifficulty(difficulty:String) {
        var intentDetails = Intent()
        intentDetails.setClass(
            this@DifficultySelectActivity,
            MainActivity::class.java
        )

        intentDetails.putExtra("KEY_DIFF", difficulty)

        startActivity(intentDetails)
    }
}
