package assignment;

import java.awt.*;

/**
 * Represents a Tetris board -- essentially a 2-d grid of booleans. Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2-d board.
 */
public final class TetrisBoard implements Board {

    /**
     *
     * @param width
     * @param height
     */
    // JTetris will use this constructor
    public TetrisBoard(int width, int height) {

    }

    /**
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

    /**
     *
     * @param p
     */
    @Override
    public void nextPiece(Piece p) {

    }

    /**
     *
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other) {
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public Result getLastResult() {
        return Result.NO_PIECE;
    }

    /**
     *
     * @return
     */
    @Override
    public Action getLastAction() {
        return Action.NOTHING;
    }

    /**
     *
     * @return
     */
    @Override
    public int getRowsCleared() {
        return -1;
    }

    /**
     *
     * @return
     */
    @Override
    public int getWidth() {
        return -1;
    }

    /**
     *
     * @return
     */
    @Override
    public int getHeight() {
        return -1;
    }

    /**
     *
     * @return
     */
    @Override
    public int getMaxHeight() {
        return -1;
    }

    /**
     *
     * @param piece
     * @param x
     * @return
     */
    @Override
    public int dropHeight(Piece piece, int x) {
        return -1;
    }

    /**
     *
     * @param x
     * @return
     */
    @Override
    public int getColumnHeight(int x) {
        return -1;
    }

    /**
     *
     * @param y
     * @return
     */
    @Override
    public int getRowWidth(int y) {
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
        return false;
    }

}
