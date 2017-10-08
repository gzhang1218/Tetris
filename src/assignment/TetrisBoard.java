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

    private int maxHeight; // initialized at 0
    private int[] colHeights;

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
        if (piece == null)
            return Result.NO_PIECE;

        lastAction = act;
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
                    lastResult = Result.PLACE;
                    return Result.PLACE;
                }
                // check if the piece has anything under it
                for (int offset = 0; offset < piece.getWidth(); offset ++) {
                    if (board[piece.getY() - 1 + piece.getSkirt()[offset]][piece.getX() + offset]) {
                        updateMaxHeight();
                        updateColHeights();
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
                updateMaxHeight();
                updateColHeights();
                lastResult = Result.PLACE;
                return Result.PLACE;
            case CLOCKWISE:
                // TODO
                break;
            case COUNTERCLOCKWISE:
                // TODO
                break;
            case NOTHING:
                lastResult = Result.SUCCESS;
                return Result.SUCCESS;
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

    @Override
    public Board testMove(Action act) {
        // TODO
        return null;
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
        return maxHeight;
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

    // TODO implement in constant time
    @Override
    public int getRowWidth(int y) {
        int count = 0;
        for (int col = 0; col < getWidth(); col++)
            if (board[y][col])
                count++;
        return -1;
    }

    @Override
    public boolean getGrid(int x, int y) {
        if (x < 0 || x > getWidth() || y < 0 || y > getHeight())
            return true;
        return board[y][x];
    }

}
