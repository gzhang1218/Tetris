package assignment;

import java.awt.*;

/**
 * Represents a Tetris board -- essentially a 2-d grid of booleans. Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2-d board.
 */
public final class TetrisBoard implements Board {

    // JTetris will use this constructor
    public TetrisBoard(int width, int height) {}

    @Override
    public Result move(Action act) { return Result.NO_PIECE; }

    @Override
    public Board testMove(Action act) { return null; }

    @Override
    public void nextPiece(Piece p) {}

    @Override
    public boolean equals(Object other) { return false; }

    @Override
    public Result getLastResult() { return Result.NO_PIECE; }

    @Override
    public Action getLastAction() { return Action.NOTHING; }

    @Override
    public int getRowsCleared() { return -1; }

    @Override
    public int getWidth() { return -1; }

    @Override
    public int getHeight() { return -1; }

    @Override
    public int getMaxHeight() { return -1; }

    @Override
    public int dropHeight(Piece piece, int x) { return -1; }

    @Override
    public int getColumnHeight(int x) { return -1; }

    @Override
    public int getRowWidth(int y) { return -1; }

    @Override
    public boolean getGrid(int x, int y) { return false; }

}
