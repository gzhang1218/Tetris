package assignment;

import java.awt.*;
import java.util.Arrays;

/**
 * Represents a Tetris board -- essentially a 2-d grid of booleans. Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2-d board.
 */
public final class TetrisBoard implements Board {


    // (0, 0) is the bottom left corner
    private boolean[][] board;

    private TetrisPiece piece;
    private Action lastAction;
    private Result lastResult;
    private int rowsCleared; // initialized at 0

    private int maxHeight; // initialized at 0
    private int[] colHeights;

    // these hold the wall kick data
    // access the offsets by ARRAY_NAME[test# - 0based][character of current rotationState][0 or 1 for x or y]
    static final int[][][] CCW_WALL_KICK_DATA;
    static final int[][][] CW_WALL_KICK_DATA;
    static final int[][][] CCW_I_WALL_KICK_DATA;
    static final int[][][] CW_I_WALL_KICK_DATA;

    static {
        CCW_WALL_KICK_DATA = new int[4]['R' + 1][2];
        CW_WALL_KICK_DATA = new int[4]['R' + 1][2];
        CCW_I_WALL_KICK_DATA = new int[4]['R' + 1][2];
        CW_I_WALL_KICK_DATA = new int[4]['R' + 1][2];

        // counterclockwise - normal
        CCW_WALL_KICK_DATA[0]['0'][0] = 1;
        CCW_WALL_KICK_DATA[0]['0'][1] = 0;
        CCW_WALL_KICK_DATA[0]['L'][0] = -1;
        CCW_WALL_KICK_DATA[0]['L'][1] = 0;
        CCW_WALL_KICK_DATA[0]['2'][0] = -1;
        CCW_WALL_KICK_DATA[0]['2'][1] = 0;
        CCW_WALL_KICK_DATA[0]['R'][0] = 1;
        CCW_WALL_KICK_DATA[0]['R'][1] = 0;

        CCW_WALL_KICK_DATA[1]['0'][0] = 1;
        CCW_WALL_KICK_DATA[1]['0'][1] = 1;
        CCW_WALL_KICK_DATA[1]['L'][0] = -1;
        CCW_WALL_KICK_DATA[1]['L'][1] = -1;
        CCW_WALL_KICK_DATA[1]['2'][0] = -1;
        CCW_WALL_KICK_DATA[1]['2'][1] = 1;
        CCW_WALL_KICK_DATA[1]['R'][0] = 1;
        CCW_WALL_KICK_DATA[1]['R'][1] = -1;

        CCW_WALL_KICK_DATA[2]['0'][0] = 0;
        CCW_WALL_KICK_DATA[2]['0'][1] = -2;
        CCW_WALL_KICK_DATA[2]['L'][0] = 0;
        CCW_WALL_KICK_DATA[2]['L'][1] = 2;
        CCW_WALL_KICK_DATA[2]['2'][0] = 0;
        CCW_WALL_KICK_DATA[2]['2'][1] = -2;
        CCW_WALL_KICK_DATA[2]['R'][0] = 0;
        CCW_WALL_KICK_DATA[2]['R'][1] = 2;

        CCW_WALL_KICK_DATA[3]['0'][0] = 1;
        CCW_WALL_KICK_DATA[3]['0'][1] = -2;
        CCW_WALL_KICK_DATA[3]['L'][0] = -1;
        CCW_WALL_KICK_DATA[3]['L'][1] = 2;
        CCW_WALL_KICK_DATA[3]['2'][0] = -1;
        CCW_WALL_KICK_DATA[3]['2'][1] = -2;
        CCW_WALL_KICK_DATA[3]['R'][0] = 1;
        CCW_WALL_KICK_DATA[3]['R'][1] = 2;

        // clockwise - normal
        CW_WALL_KICK_DATA[0]['0'][0] = -1;
        CW_WALL_KICK_DATA[0]['0'][1] = 0;
        CW_WALL_KICK_DATA[0]['L'][0] = -1;
        CW_WALL_KICK_DATA[0]['L'][1] = 0;
        CW_WALL_KICK_DATA[0]['2'][0] = 1;
        CW_WALL_KICK_DATA[0]['2'][1] = 0;
        CW_WALL_KICK_DATA[0]['R'][0] = 1;
        CW_WALL_KICK_DATA[0]['R'][1] = 0;

        CW_WALL_KICK_DATA[1]['0'][0] = -1;
        CW_WALL_KICK_DATA[1]['0'][1] = 1;
        CW_WALL_KICK_DATA[1]['L'][0] = -1;
        CW_WALL_KICK_DATA[1]['L'][1] = -1;
        CW_WALL_KICK_DATA[1]['2'][0] = 1;
        CW_WALL_KICK_DATA[1]['2'][1] = 1;
        CW_WALL_KICK_DATA[1]['R'][0] = 1;
        CW_WALL_KICK_DATA[1]['R'][1] = -1;

        CW_WALL_KICK_DATA[2]['0'][0] = 0;
        CW_WALL_KICK_DATA[2]['0'][1] = -2;
        CW_WALL_KICK_DATA[2]['L'][0] = 0;
        CW_WALL_KICK_DATA[2]['L'][1] = 2;
        CW_WALL_KICK_DATA[2]['2'][0] = 0;
        CW_WALL_KICK_DATA[2]['2'][1] = -2;
        CW_WALL_KICK_DATA[2]['R'][0] = 0;
        CW_WALL_KICK_DATA[2]['R'][1] = 2;

        CW_WALL_KICK_DATA[3]['0'][0] = -1;
        CW_WALL_KICK_DATA[3]['0'][1] = -2;
        CW_WALL_KICK_DATA[3]['L'][0] = -1;
        CW_WALL_KICK_DATA[3]['L'][1] = 2;
        CW_WALL_KICK_DATA[3]['2'][0] = 1;
        CW_WALL_KICK_DATA[3]['2'][1] = -2;
        CW_WALL_KICK_DATA[3]['R'][0] = 1;
        CW_WALL_KICK_DATA[3]['R'][1] = 2;

        // counterclockwise - I piece
        CCW_I_WALL_KICK_DATA[0]['0'][0] = -1;
        CCW_I_WALL_KICK_DATA[0]['0'][1] = 0;
        CCW_I_WALL_KICK_DATA[0]['L'][0] = -2;
        CCW_I_WALL_KICK_DATA[0]['L'][1] = 0;
        CCW_I_WALL_KICK_DATA[0]['2'][0] = 1;
        CCW_I_WALL_KICK_DATA[0]['2'][1] = 0;
        CCW_I_WALL_KICK_DATA[0]['R'][0] = 2;
        CCW_I_WALL_KICK_DATA[0]['R'][1] = 0;

        CCW_I_WALL_KICK_DATA[1]['0'][0] = 2;
        CCW_I_WALL_KICK_DATA[1]['0'][1] = 0;
        CCW_I_WALL_KICK_DATA[1]['L'][0] = 1;
        CCW_I_WALL_KICK_DATA[1]['L'][1] = 0;
        CCW_I_WALL_KICK_DATA[1]['2'][0] = -2;
        CCW_I_WALL_KICK_DATA[1]['2'][1] = 0;
        CCW_I_WALL_KICK_DATA[1]['R'][0] = -1;
        CCW_I_WALL_KICK_DATA[1]['R'][1] = 0;

        CCW_I_WALL_KICK_DATA[2]['0'][0] = -1;
        CCW_I_WALL_KICK_DATA[2]['0'][1] = 2;
        CCW_I_WALL_KICK_DATA[2]['L'][0] = -2;
        CCW_I_WALL_KICK_DATA[2]['L'][1] = -1;
        CCW_I_WALL_KICK_DATA[2]['2'][0] = 1;
        CCW_I_WALL_KICK_DATA[2]['2'][1] = -2;
        CCW_I_WALL_KICK_DATA[2]['R'][0] = 2;
        CCW_I_WALL_KICK_DATA[2]['R'][1] = 1;

        CCW_I_WALL_KICK_DATA[3]['0'][0] = 2;
        CCW_I_WALL_KICK_DATA[3]['0'][1] = -1;
        CCW_I_WALL_KICK_DATA[3]['L'][0] = 1;
        CCW_I_WALL_KICK_DATA[3]['L'][1] = 2;
        CCW_I_WALL_KICK_DATA[3]['2'][0] = -2;
        CCW_I_WALL_KICK_DATA[3]['2'][1] = 1;
        CCW_I_WALL_KICK_DATA[3]['R'][0] = -1;
        CCW_I_WALL_KICK_DATA[3]['R'][1] = -2;

        // clockwise - I piece
        CW_I_WALL_KICK_DATA[0]['0'][0] = -2;
        CW_I_WALL_KICK_DATA[0]['0'][1] = 0;
        CW_I_WALL_KICK_DATA[0]['L'][0] = 1;
        CW_I_WALL_KICK_DATA[0]['L'][1] = 0;
        CW_I_WALL_KICK_DATA[0]['2'][0] = 2;
        CW_I_WALL_KICK_DATA[0]['2'][1] = 0;
        CW_I_WALL_KICK_DATA[0]['R'][0] = -1;
        CW_I_WALL_KICK_DATA[0]['R'][1] = 0;

        CW_I_WALL_KICK_DATA[1]['0'][0] = 1;
        CW_I_WALL_KICK_DATA[1]['0'][1] = 0;
        CW_I_WALL_KICK_DATA[1]['L'][0] = -2;
        CW_I_WALL_KICK_DATA[1]['L'][1] = 0;
        CW_I_WALL_KICK_DATA[1]['2'][0] = -1;
        CW_I_WALL_KICK_DATA[1]['2'][1] = 0;
        CW_I_WALL_KICK_DATA[1]['R'][0] = 2;
        CW_I_WALL_KICK_DATA[1]['R'][1] = 0;

        CW_I_WALL_KICK_DATA[2]['0'][0] = -2;
        CW_I_WALL_KICK_DATA[2]['0'][1] = -1;
        CW_I_WALL_KICK_DATA[2]['L'][0] = 1;
        CW_I_WALL_KICK_DATA[2]['L'][1] = -2;
        CW_I_WALL_KICK_DATA[2]['2'][0] = 2;
        CW_I_WALL_KICK_DATA[2]['2'][1] = 1;
        CW_I_WALL_KICK_DATA[2]['R'][0] = -1;
        CW_I_WALL_KICK_DATA[2]['R'][1] = 2;

        CW_I_WALL_KICK_DATA[3]['0'][0] = 1;
        CW_I_WALL_KICK_DATA[3]['0'][1] = 2;
        CW_I_WALL_KICK_DATA[3]['L'][0] = -2;
        CW_I_WALL_KICK_DATA[3]['L'][1] = 1;
        CW_I_WALL_KICK_DATA[3]['2'][0] = -1;
        CW_I_WALL_KICK_DATA[3]['2'][1] = -2;
        CW_I_WALL_KICK_DATA[3]['R'][0] = 2;
        CW_I_WALL_KICK_DATA[3]['R'][1] = -1;
    }



    // JTetris will use this constructor
    public TetrisBoard(int width, int height) {
        board = new boolean[height][width];
        colHeights = new int[width];
    }

    /**
     * This method needs to update the lastAction,
     * lastResult, rowsCleared
     *
     * @param act
     * @return
     */
    @Override
    public Result move(Action act) {
        lastAction = act;

        if (piece == null) {
            lastResult = Result.NO_PIECE;
            return Result.NO_PIECE;
        }

        rowsCleared = 0;

        switch (act) {
            case LEFT:
                // check if the piece is on the left border
                if (piece.getX() == 0) {
                    lastResult = Result.OUT_BOUNDS;
                    return Result.OUT_BOUNDS;
                }
                // check if anything in the way to the left
                for (int offset = 0; offset < piece.getHeight(); offset ++) {
                    if (board[piece.getY() + offset][piece.getX() - 1 + piece.getLSkirt()[offset]]) {
                        lastResult = Result.OUT_BOUNDS;
                        return Result.OUT_BOUNDS;
                    }
                }
                // otherwise, shift to left
                clearCurrentPos();
                piece.setX(piece.getX() - 1);
                placePos();
                lastResult = Result.SUCCESS;
                return Result.SUCCESS;
            case RIGHT:
                // check if the piece is on the right border
                if (piece.getX() + piece.getWidth() == JTetris.WIDTH) {
                    lastResult = Result.OUT_BOUNDS;
                    return Result.OUT_BOUNDS;
                }
                // check if anything in the way to the right
                for (int offset = 0; offset < piece.getHeight(); offset ++) {
                    if (board[piece.getY() + offset][piece.getX() + piece.getWidth() - piece.getRSkirt()[offset]]) {
                        lastResult = Result.OUT_BOUNDS;
                        return Result.OUT_BOUNDS;
                    }
                }
                // otherwise, shift to the right
                clearCurrentPos();
                piece.setX(piece.getX() + 1);
                placePos();
                lastResult = Result.SUCCESS;
                return Result.SUCCESS;
            case DOWN:
                // check if the piece has reached the ground
                if (piece.getY() == 0) {
                    updateMaxHeight();
                    updateColHeights();

                    clearRows();

                    piece = null;

                    lastResult = Result.PLACE;
                    return Result.PLACE;
                }
                // check if the piece has anything under it
                for (int offset = 0; offset < piece.getWidth(); offset ++) {
                    if (board[piece.getY() - 1 + piece.getSkirt()[offset]][piece.getX() + offset]) {
                        updateMaxHeight();
                        updateColHeights();

                        clearRows();

                        piece = null;

                        lastResult = Result.PLACE;
                        return Result.PLACE;
                    }
                }
                // otherwise, slide it down by one
                clearCurrentPos();
                piece.setY(piece.getY() - 1);
                placePos();
                lastResult = Result.SUCCESS;
                return Result.SUCCESS;
            case DROP:
                // should always be able to drop, assuming the current piece is in a valid position
                clearCurrentPos();
                piece.setY(piece.getY() - dropHeight(piece, piece.getX()));
                placePos();

                lastResult = Result.SUCCESS;
                return Result.SUCCESS;
            case CLOCKWISE:

                clearCurrentPos();

                // check if simple rotate is valid
                TetrisPiece temp = (TetrisPiece)piece.nextRotation().nextRotation().nextRotation();
                boolean simpleRotateWorks = isValid(temp);

                if (simpleRotateWorks) {
                    piece = (TetrisPiece)piece.nextRotation().nextRotation().nextRotation();
                    lastResult = Result.SUCCESS;
                } else { // need to check wall kicks..... :(
                    // FOUR TESTS TO CHECK, DEPENDS ON IDENTITY OF TETRONIMO, WHICH ORIENTATION IT'S IN AND GOING TO
                    TetrisPiece kicked;
                    for (int test = 0; test < 4; test++ ) {
                        int xOffset, yOffset;
                        if(piece.isI()) {
                            xOffset = CW_I_WALL_KICK_DATA[test][piece.getRotationState()][0];
                            yOffset = CW_I_WALL_KICK_DATA[test][piece.getRotationState()][1];
                        } else {
                            xOffset = CW_WALL_KICK_DATA[test][piece.getRotationState()][0];
                            yOffset = CW_WALL_KICK_DATA[test][piece.getRotationState()][1];
                        }

                        kicked = (TetrisPiece)piece.nextRotation().nextRotation().nextRotation();

                        kicked.setX(kicked.getX() + xOffset);
                        kicked.setY(kicked.getY() + yOffset);

                        if (isValid(kicked)) {
                            piece = (TetrisPiece)piece.nextRotation().nextRotation().nextRotation();
                            piece.setX(kicked.getX() + xOffset);
                            piece.setY(kicked.getY() + yOffset);
                            lastResult = Result.SUCCESS;
                            break;
                        }

                    }
                    // if we don't find a valid kick, then don't change 'piece' and have OUTOFBOUNDS return
                    lastResult = Result.OUT_BOUNDS;
                }

                // at this point, piece should either be reassigned or left untouched
                // last result should be updated (SUCCESS, OUTOFBOUNDS)
                placePos();
                return lastResult;

            case COUNTERCLOCKWISE:

                clearCurrentPos();

                // check if simple rotate is valid
                temp = (TetrisPiece)piece.nextRotation();
                simpleRotateWorks = isValid(temp);

                if (simpleRotateWorks) {
                    piece = (TetrisPiece)piece.nextRotation();
                    lastResult = Result.SUCCESS;
                } else { // need to check wall kicks..... :(
                    // FOUR TESTS TO CHECK, DEPENDS ON IDENTITY OF TETRONIMO, WHICH ORIENTATION IT'S IN AND GOING TO
                    TetrisPiece kicked;
                    for (int test = 0; test < 4; test++ ) {
                        int xOffset, yOffset;
                        if(piece.isI()) {
                            xOffset = CCW_I_WALL_KICK_DATA[test][piece.getRotationState()][0];
                            yOffset = CCW_I_WALL_KICK_DATA[test][piece.getRotationState()][1];
                        } else {
                            xOffset = CCW_WALL_KICK_DATA[test][piece.getRotationState()][0];
                            yOffset = CCW_WALL_KICK_DATA[test][piece.getRotationState()][1];
                        }

                        kicked = (TetrisPiece)piece.nextRotation();

                        kicked.setX(kicked.getX() + xOffset);
                        kicked.setY(kicked.getY() + yOffset);

                        if (isValid(kicked)) {
                            piece = (TetrisPiece)piece.nextRotation();
                            piece.setX(kicked.getX() + xOffset);
                            piece.setY(kicked.getY() + yOffset);
                            lastResult = Result.SUCCESS;
                            break;
                        }
                    }
                    // if we don't find a valid kick, then don't change 'piece' and have OUTOFBOUNDS return
                    lastResult = Result.OUT_BOUNDS;
                }

                // at this point, piece should either be reassigned or left untouched
                // last result should be updated (SUCCESS, OUTOFBOUNDS)
                placePos();
                return lastResult;

            case NOTHING:
                lastResult = Result.SUCCESS;
                return Result.SUCCESS;
            case HOLD:
                // TODO implement for karma
                lastResult = Result.SUCCESS;
                return Result.SUCCESS;
        }




        return Result.NO_PIECE;
    }

    private void clearCurrentPos() {
        for (Point offset : piece.getBody())
            board[piece.getY() + (int)offset.getY()][piece.getX() + (int)offset.getX()] = false;
    }

    private void placePos() {
        for (Point offset : piece.getBody())
            board[piece.getY() + (int)offset.getY()][piece.getX() + (int)offset.getX()] = true;
    }

    private void updateMaxHeight() {
        if (piece.getY() + piece.getHeight() > maxHeight)
            maxHeight = piece.getY() + piece.getHeight();
    }

    private void updateColHeights() {
        for (Point offset : piece.getBody()) {
            int x = piece.getX() + (int)offset.getX();
            int y = piece.getY() + (int)offset.getY() + 1;
            if (colHeights[x] < y)
                colHeights[x] = y;
        }
    }

    /**
     * Checks if the provided piece would fit on the board as is
     * -both in bounds and not on top of anything
     *
     * @param piece
     * @return
     */
    private boolean isValid(TetrisPiece piece) {
        for (Point offset : piece.getBody()) {
            if (piece.getX() + (int) offset.getX() < 0 || piece.getX() + (int) offset.getX() >= JTetris.WIDTH) {
                return false;
            }
            if (piece.getY() + (int) offset.getY() < 0 || piece.getY() + (int) offset.getY() >= JTetris.HEIGHT) {
                return false;
            }
            if /*occupied*/ (getGrid(piece.getX() + (int) offset.getX(), piece.getY() + (int) offset.getY())) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method will check if any rows are full, following a PLACE result.
     * It will update the rowsCleared attribute accordingly,
     * as well at the maxHeight and colHeights
     */
    private void clearRows() {
        int fullRowCount = 0;

        // moving from the bottom up, counting full rows and shifting non-full rows down by 'fullRowCount'
        for (int row = 0; row < JTetris.HEIGHT; row++ ) {
            if (isFull(row)) {
                fullRowCount++;
                continue;
            } else {
                if (fullRowCount > 0) // not a shift by 0
                    for (int col = 0; col < getWidth(); col ++)
                        board[row - fullRowCount][col] = board[row][col];
            }
        }

        // clear the top 'fullRowCount' rows
        for (int rowCount = 0; rowCount < fullRowCount; rowCount ++)
            Arrays.fill(board[JTetris.HEIGHT - 1 - rowCount], false);

        rowsCleared = fullRowCount;

        maxHeight -= fullRowCount;
        for (int col = 0 ; col < JTetris.WIDTH; col ++ )
            colHeights[col] = colHeights[col] - fullRowCount;

    }

    private boolean isFull(int row) {
        for (int col = 0; col < JTetris.WIDTH; col++) {
            if (!getGrid(col, row))
                return false;
        }
        return true;
    }

    // TODO need to figure out a way of simulating moves without actually changing anything
    @Override
    public Board testMove(Action act) {
        /*Board savedBoard = new TetrisBoard(this.getHeight(),this.getWidth());
        savedBoard = this;*/
        //TEMPORARY HACKY METHOD JUST TO GET BRAIN TO WORK
        /*boolean[][] savedBoard = board;
        int[] savedColHeights = colHeights;*/
        Board tempBoard = makeCopy();
        tempBoard.move(act);
        /*Board changedBoard = this;
        board = savedBoard;
        colHeights = savedColHeights;*/

        return tempBoard;
    }

    /**
     * Needs to copy board, piece, lastAction, lastResult, rowsCleared, maxHeight, colHeight
     * only board, piece, and colHeight are pointers
     */
    private TetrisBoard makeCopy() {
        TetrisBoard copy = new TetrisBoard(getWidth(), getHeight()); //initializes the board[][] and colHeights[]

        copy.setLastAction(this.getLastAction());
        copy.setLastResult(this.getLastResult());
        copy.setRowsCleared(this.getRowsCleared());
        copy.setMaxHeight(this.getMaxHeight());
        for (int col = 0; col < getWidth(); col++ )
            copy.setColumnHeight(col, this.getColumnHeight(col));
        for (int row = 0; row < getHeight(); row++ )
            for (int col = 0; col < getWidth(); col++ )
                copy.setGrid(row, col, this.getGrid(col, row));
        copy.nextPiece(this.piece);

        return copy;
    }

    @Override
    public void nextPiece(Piece p) {
        this.piece = (TetrisPiece)p;
    }

    // need to compare the 2-d arrays
    // TODO should we compare active pieces too? and previous actions, results, rows cleared?
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TetrisBoard))
            return false;
        TetrisBoard o = (TetrisBoard) other;

        // compare each element
        for (int row = 0; row < getHeight(); row++ ) {
            for (int col = 0; col < getWidth(); col++ ) {
                if (getGrid(col, row) != o.getGrid(col, row))
                    return false;
            }
        }

        return true;
    }

    @Override
    public Result getLastResult() {
        return lastResult;
    }

    public void setLastResult(Result result) { this.lastResult = result; }

    @Override
    public Action getLastAction() {
        return lastAction;
    }

    public void setLastAction(Action action) { this.lastAction = action; }

    @Override
    public int getRowsCleared() {
        return rowsCleared;
    }

    public void setRowsCleared(int rowsCleared) { this.rowsCleared = rowsCleared; }

    @Override
    public int getWidth() {
        return board[0].length;
    }

    @Override
    public int getHeight() {
        return board.length;
    }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    // TODO implement in constant time? This seems good enough though...?
    @Override
    public int dropHeight(Piece piece, int x) {
        int minDist = Integer.MAX_VALUE;
        for (int col = x; col < x + piece.getWidth(); col++) {
            int dist = ((TetrisPiece)piece).getY() + piece.getSkirt()[col - x] - getColumnHeight(col);
            if (dist < minDist)
                minDist = dist;
        }
        return minDist;
    }

    @Override
    public int getColumnHeight(int x) {
        return colHeights[x];
    }

    public void setColumnHeight(int col, int val) { this.colHeights[col] = val; }

    // TODO implement in constant time
    @Override
    public int getRowWidth(int y) {
        int count = 0;
        for (int col = 0; col < getWidth(); col++)
            if (board[y][col])
                count++;
        return count;
    }

    @Override
    public boolean getGrid(int x, int y) {
        if (x < 0 || x > getWidth() || y < 0 || y > getHeight())
            return true;
        return board[y][x];
    }

    public void setGrid(int row, int col, boolean val) { board[row][col] = val; }

}
