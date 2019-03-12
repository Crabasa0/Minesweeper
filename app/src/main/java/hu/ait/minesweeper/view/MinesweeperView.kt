package hu.ait.minesweeper.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import hu.ait.minesweeper.model.MinesweeperModel
import android.support.design.widget.Snackbar
import hu.ait.minesweeper.MainActivity
import kotlinx.android.synthetic.main.activity_main.view.*

class MinesweeperView(context: Context?, attrs: AttributeSet?) : View(context, attrs)
{
    private val paintBackground = Paint()
    private val paintLine = Paint()
    private val paintText = Paint()
    private val textOffset = 10

    private val SIZE = MinesweeperModel.SIZE

    init {
        paintBackground.color = Color.BLACK
        paintBackground.style = Paint.Style.FILL

        paintLine.color = Color.WHITE
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 8f

        paintText.color = Color.GREEN
        paintText.textSize = 50f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        paintText.textSize = height/ (SIZE.toFloat())
    }

    override fun onDraw(canvas: Canvas?){
        canvas?.drawRect(0f,0f,width.toFloat(),height.toFloat(),paintBackground)

        drawGameBoard(canvas)
        drawNumsAndFlags(canvas)
    }


    private fun drawGameBoard(canvas: Canvas?) {
        drawGameBorder(canvas)
        drawHorizontalGridlines(canvas)
        drawVerticalGridlines(canvas)
    }

    private fun drawGameBorder(canvas: Canvas?) {
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)
    }

    private fun drawVerticalGridlines(canvas: Canvas?) {
        for (i in 1 until SIZE) {
            canvas?.drawLine(
                (i * width / SIZE).toFloat(), 0f, (i * width / SIZE).toFloat(), height.toFloat(),
                paintLine
            )
        }
    }

    private fun drawHorizontalGridlines(canvas: Canvas?) {
        for (i in 1 until SIZE) {
            canvas?.drawLine(
                0f, (i * height / SIZE).toFloat(), width.toFloat(), (i * height / SIZE).toFloat(),
                paintLine
            )
        }
    }

    private fun drawNumsAndFlags(canvas: Canvas?) { //ALSO DRAWS MINES IF DEAD
        for (i in 0 until SIZE) {
            for (j in 0 until SIZE) {
                val cell = MinesweeperModel.getCell(i, j)
                if (cell.wasClicked && cell.type == MinesweeperModel.CLEAR) {
                    canvas?.drawText(
                        cell.minesAround.toString(),
                        i * width / SIZE.toFloat() + textOffset,
                        (j + 1) * height / SIZE.toFloat() - textOffset,
                        paintText
                    )
                } else if (MinesweeperModel.gameState == "dead" && cell.type == MinesweeperModel.BOMB){
                    drawBomb(canvas, i, j)

                } else if (cell.isFlagged) {
                    drawFlag(canvas, i, j)
                }
            }
        }
    }

    private fun drawFlag(canvas: Canvas?, i: Int, j: Int) {
        paintText.color = Color.WHITE
        canvas?.drawText("F", i * width / SIZE.toFloat() + textOffset,
                        (j + 1) * height / SIZE.toFloat() - textOffset, paintText)
        paintText.color = Color.GREEN
    }

    private fun drawBomb(canvas: Canvas?, i: Int, j: Int) {
        paintText.color = Color.RED
        canvas?.drawText("B", i * width / SIZE.toFloat() + textOffset,
                        (j + 1) * height / SIZE.toFloat() - textOffset, paintText)
        paintText.color = Color.GREEN
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (MinesweeperModel.gameState == "dead"
            || MinesweeperModel.gameState == "win") return super.onTouchEvent(event)

        if (event?.action == MotionEvent.ACTION_DOWN){
            val tX = event.x.toInt()/ (width/SIZE)
            val tY = event.y.toInt()/ (height/SIZE)

            if (tX < SIZE && tY < SIZE) {

                if(!(context as MainActivity).isSwitchOn()){
                    if (!MinesweeperModel.getCell(tX,tY).isFlagged){
                        clickCell(tX, tY)
                    }
                } else {
                    flagCell(tX, tY)
                }
                MinesweeperModel.checkWin()
                if (MinesweeperModel.gameState == "win"){
                    displayWinMessage()
                }
            }
            invalidate()
        }
        return super.onTouchEvent(event)
    }

    private fun displayWinMessage() {
        val snackbar = Snackbar
            .make(minesweeperView, "You Win!", Snackbar.LENGTH_LONG)
        snackbar.show()
    }


    private fun flagCell(tX: Int, tY: Int) {//toggles flagged status
        MinesweeperModel.toggleFlag(tX, tY)
    }

    private fun clickCell(tX: Int, tY: Int) {
        if (MinesweeperModel.getCell(tX, tY).wasClicked) return

        MinesweeperModel.getCell(tX, tY).wasClicked = true

        if (MinesweeperModel.isMine(tX, tY)) {
            MinesweeperModel.gameOver()
            val snackbar = Snackbar
                .make(minesweeperView, "Game Over", Snackbar.LENGTH_LONG)
            snackbar.show()
        } else if (MinesweeperModel.getCell(tX, tY).minesAround == 0){
            clickSurroundingCells(tX, tY)
        }
    }

    private fun clickSurroundingCells(tX: Int, tY: Int) {
        for (i in tX-1..tX+1){
            for (j in tY-1..tY+1){
                if (i in 0..(SIZE - 1) && j >= 0 && j < SIZE)
                clickCell(i,j)
            }
        }
    }

    public fun resetGame(){
        MinesweeperModel.resetModel()
        invalidate()
        val snackbar = Snackbar
            .make(minesweeperView, "Game Reset", Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = View.MeasureSpec.getSize(widthMeasureSpec)
        val h = View.MeasureSpec.getSize(heightMeasureSpec)
        val d = if (w == 0) h else if (h == 0) w else if (w < h) w else h

        setMeasuredDimension(d,d)
    }

    public fun updateStatus(newStatus: String){
        tvStatus.text = newStatus
    }


}