package hu.ait.minesweeper.model

object MinesweeperModel{

    /*
    SIZE and NUM_MINES are vars ONLY so that they can be given different values depending on difficulty.
    This was the easiest approach given that kotlin objects cannot have constructors.
    Do not modify these once the game has been initialized! Or do, and see what happens.
    */
    public var SIZE = 5
    public var NUM_MINES = 3

    public const val CLEAR = 0
    public const val BOMB = 1

    public var gameState = "alive"





    private var model = Array(SIZE) {
        Array(SIZE) {
            Cell(CLEAR, 0, isFlagged = false, wasClicked = false) } }

    public fun initGame(){
        placeMines()
        calculateAdjacencies()
    }

    private fun calculateAdjacencies() {
        for (row in 0..SIZE-1){
            for (col in 0..SIZE-1){
                model[row][col].minesAround = countMines(row, col)

            }
        }
    }

    private fun countMines(row: Int, col: Int): Int {
        var count: Int = 0
        for (i in row-1..row+1){
            for (j in col-1..col+1){
                if (isMine(i,j)) count += 1
            }
        }
        if (isMine(row, col)) count -= 1

        return count
    }

    public fun isMine(i: Int, j: Int): Boolean {
        return if (i < 0 || i >= SIZE || j < 0 || j >= SIZE){
            false
        } else {
            model[i][j].type == BOMB
        }
    }

    private fun placeMines() {
        var coordinateSet = calculateMineLocations()
        for (mineCoord in coordinateSet){
            model[mineCoord.first][mineCoord.second].type = BOMB

        }
    }

    private fun calculateMineLocations() : MutableSet<Pair<Int,Int>> {
        //Note: This function will be extremely slow if NUM_MINES is close to SIZE^2
        var coordinateSet = mutableSetOf<Pair<Int, Int>>()
        while(coordinateSet.size < NUM_MINES){
            val mineLocation = Pair<Int, Int>((0..SIZE-1).random(), (0..SIZE-1).random())
            coordinateSet.add(mineLocation)
        }
        return coordinateSet
    }

    public fun toggleFlag(row: Int, col: Int){
        model[row][col].isFlagged = !model[row][col].isFlagged
    }

    public fun checkWin(){
        /* The game is won if:
            1. There are as many flags on the board as there are bombs
            2. Each bomb has a flag
            3. All other cells have been clicked
         */
        var flagCount: Int = 0
        var bombsFlagged: Boolean = true
        var cellsClicked: Boolean = true
        for (row in model) {
            for (cell in row) {
                if (cell.isFlagged) {
                    flagCount += 1
                    bombsFlagged = bombsFlagged && ((cell.isFlagged && cell.type == BOMB) ||
                            (!cell.isFlagged && cell.type == CLEAR))
                } else {
                    cellsClicked = cellsClicked && cell.wasClicked
                }

            }
        }
        if (flagCount == NUM_MINES && bombsFlagged && cellsClicked) gameState = "win"
    }

    public fun gameOver(){
        gameState = "dead"
    }

    public fun resetModel(){
        model = Array(SIZE) {
            Array(SIZE) {
                Cell(CLEAR, 0, isFlagged = false, wasClicked = false) } }
        initGame()
        gameState = "alive"
    }

    public fun getCell(i:Int, j:Int) = model[i][j]


}

data class Cell(var type: Int, var minesAround: Int,
                 var isFlagged: Boolean, var wasClicked: Boolean)