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

    private TetrisPiece piece;
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
        if (piece == null)
            return Result.NO_PIECE;

        lastAction = act;
        rowsCleared = 0;

        switch (act) {
            case LEFT:
                // check if the piece is on the left border
                if (piece.getX() == 0)
                    return Result.OUT_BOUNDS;
                // check if anything in the way to the left
                for (int offset = 0; offset < piece.getHeight(); offset ++)
                    if (board[piece.getY() + offset][piece.getX() - 1 + piece.getLSkirt()[offset]])
                        return Result.OUT_BOUNDS;
                // otherwise, shift to left
                clearCurrentPos();
                piece.setX(piece.getX() - 1);
                placePos();
                return Result.SUCCESS;
            case RIGHT:
                // check if the piece is on the right border
                if (piece.getX() + piece.getWidth() == JTetris.WIDTH)
                    return Result.OUT_BOUNDS;
                // check if anything in the way to the right
                for (int offset = 0; offset < piece.getHeight(); offset ++)
                    if (board[piece.getY() + offset][piece.getX() + piece.getWidth() - piece.getRSkirt()[offset]])
                        return Result.OUT_BOUNDS;
                // otherwise, shift to the right
                clearCurrentPos();
                piece.setX(piece.getX() + 1);
                placePos();
                return Result.SUCCESS;
            case DOWN:
                // check if the piece has reached the ground
                if (piece.getY() == 0)
                    return Result.PLACE;
                // check if the piece has anything under it
                for (int offset = 0; offset < piece.getWidth(); offset ++)
                    if (board[piece.getY() - 1 + piece.getSkirt()[offset]][piece.getX() + offset])
                        return Result.PLACE;
                // otherwise, slide it down by one
                clearCurrentPos();
                piece.setY(piece.getY() - 1);
                placePos();
                return Result.SUCCESS;
            case DROP:

                break;
            case CLOCKWISE:

                break;
            case COUNTERCLOCKWISE:

                break;
            case NOTHING:
                break;
            case HOLD:
                // TODO implement for karma
                break;
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
        this.piece = (TetrisPiece)p;
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

    @Override
    public boolean getGrid(int x, int y) {
        return board[y][x];
    }

}
