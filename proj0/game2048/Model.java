package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author ChainHuang
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }//返回的是Tile类型，不是int

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    /* 单独对一列进行NORTH方向的移动合并处理 ,若board状态改变返回true */
    //move()方法的作用：移动该tile到那个位置，若那里不为null则合并(不论值是否相等都合并）返回true，为null则直接移动返回false；已经帮我们完成了移动和合并操作，
    //所以我们需要找到每个不为null的tile应该移动到哪个位置记为j（见google quiz!!!2种情况！！)
    public boolean singleColHelper(int col){
        boolean[] merged  = new boolean[]{false, false, false, false};
        boolean changed = false;
        //🎉从上往下遍历，能保证每个tile只需移动一次，也才能使用初步思路
        for(int i = 2; i >= 0 ; i--){
            if(board.tile(col, i) == null)//从上往下找到不为null的tile
                continue;
            //初步思路：往上，找到该tile初步应该移到的位置j--往上第一个不为null位置的下一个（不太准确，因为有可能都是null)
            int j = i;
            while(j+1 <= 3 && board.tile(col, j+1) == null){//⚠️j+1<=3应先判断,且因第二个用到了j+1,所以应判断j+1
                j++;
            }//该循环结束后，j要么是3（即从i+1~3都为null)，要么是往上第一个不为null位置的下一个位置.
            Tile current = board.tile(col, i);
            if(j == 3) {//if j==3
                board.move(col, j, current);
                changed = true;
            }
            else {//or j为往上第一个不为null的下一个位置
                     //若j+1位置值 == i位置的值，且j+1位置还没有发生过合并，则j++, move到j位置(move会合并），并更新changed
                    if (current.value() == board.tile(col, j+1).value() && merged[j+1] == false) {
                        j++;
                        board.move(col, j, current);
                        merged[j] = true;
                        score += current.value() * 2;
                        changed = true;
                    }  //若j+1位置的值 ！= i位置的值，或者值相等但已经发生过合并，则直接move到j位置
                   else {
                         board.move(col, j, current);
                         changed = true;
                    }
            }
        }
        return changed;
    }
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.
        /* 切换side为NORTH */
        board.setViewingPerspective(side);//this method will change the behavior ......
       /* 以NORTH为主  一列一列*/
        for(int i = 0; i < 4; i++){
            if(singleColHelper(i) ){
                changed = true;
            }
        }
        board.setViewingPerspective(Side.NORTH);//
        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        for(int i = 0; i < b.size(); i++)
            for(int j = 0; j < b.size(); j++)
            {
                if(b.tile(j, i) == null)
                    return true;
            }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        for(int i = 0; i < b.size(); i++)
            for(int j = 0; j < b.size(); j++)
            {
                if(b.tile(j, i) == null)//必须先判断是否是null,否则会出现nullpointerError
                    continue;
                if(b.tile(j, i).value() == MAX_PIECE)
                    return true;
            }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.
        if(emptySpaceExists(b))
            return true;
        //there are two adjacent tiles with same values:
        for(int i = 0; i < b.size(); i++)
            for(int j = 0; j < b.size(); j++)
            {//原点是左下角,即b.tile(0,0）在左下角是标准直角坐标，所以要和top,right比较而不是和down，right
                Tile current = b.tile(j, i);
                if(i+1 < b.size()) {//要做该判断，因为要保证index不能越界
                    Tile right = b.tile(j, i + 1);
                    if(right != null && current.value() == right.value())//要用.value()，因为Tile还包含了col,row
                        return true;
                }
                if(j+1 < b.size()) {
                    Tile top = b.tile(j + 1, i);
                    if (top != null && current.value() == top.value())
                        return true;
                }
            }
        return false;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
