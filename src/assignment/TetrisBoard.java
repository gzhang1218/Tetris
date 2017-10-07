package assignment;

import java.awt.*;

/**
 * Represents a Tetris board -- essentially a 2-d grid of booleans. Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2-d board.
 */
public final class TetrisBoard implements Board {


    // (0, 0) is the bottom left corner
    private boolean[][] board;

    private Piece piece;
    private Action lastAction;
    private Result lastResult;
    private int rowsCleared; // initialized at 0

    // JTetris will use this constructor
    public TetrisBoard(int width, int height) {
        board = new boolean[height][width];
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
        return Result.NO_PIECE;
    }

    /**
     *
     * @param act
     * @return
     */
    @Override
    public Board testMove(Action act) {
        return null;
    }

    @Override
    public void nextPiece(Piece p) {
        this.piece = p;
    }

    /**
     * Need to compare 2-d arrays,
     *
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other) {
        return false;
    }

    @Override
    public Result getLastResult() {
        return lastResult;
    }

    @Override
    public Action getLastAction() {
        return lastAction;
    }

    @Override
    public int getRowsCleared() {
        return rowsCleared;
    }

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
        //TODO
        return -1;
    }

    @Override
    public int dropHeight(Piece piece, int x) {
        //TODO
        return -1;
    }

    @Override
    public int getColumnHeight(int x) {
        // TODO (does only count "settled" blocks?)
        return -1;
    }

    @Override
    public int getRowWidth(int y) {
        //TODO
        return -1;
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    @Override
    public boolean getGrid(int x, int y) {
        return board[y][x];
    }

}
