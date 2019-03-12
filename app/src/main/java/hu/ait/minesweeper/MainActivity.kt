package hu.ait.minesweeper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import hu.ait.minesweeper.model.MinesweeperModel
import hu.ait.minesweeper.view.MinesweeperView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDifficulty()

        setContentView(R.layout.activity_main)
        MinesweeperModel.resetModel()

        btnRestart.setOnClickListener {
            minesweeperView.resetGame()
        }

        assert(switch1 != null)
    }

    private fun setDifficulty() {
        val SIZE: Int
        val NUM_MINES: Int

        if (intent.extras.containsKey("KEY_DIFF")) {
            when (intent.getStringExtra("KEY_DIFF")) {
                "easy" -> {
                    SIZE = 5
                    NUM_MINES = 3
                }
                "medium" -> {
                    SIZE = 7
                    NUM_MINES = 9
                }
                "hard" -> {
                    SIZE = 9
                    NUM_MINES = 20
                }
                else -> {
                    throw Exception("Invalid difficulty setting")
                }
            }
            MinesweeperModel.SIZE = SIZE
            MinesweeperModel.NUM_MINES = NUM_MINES
            //Don't change these!

        }
    }

    public fun isSwitchOn() : Boolean{
        return switch1.isChecked
    }


}
