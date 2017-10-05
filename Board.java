package assignment;

/**
 * An abstraction for a Tetris board, which allows for querying it's state and
 * applying actions.
 */
public interface Board {

    /**
     * Possible results of applying a board action.
     */
    public enum Result {
        /**
         * The action was a success (eg, applied successfuly).
         */
        SUCCESS,

        /**
         * The action would cause the piece to go out of bounds, or collide with
         * another piece.
         */
        OUT_BOUNDS,

        /**
         * There is no piece on the board to apply an action to.
         */
        NO_PIECE,

        /**
         * The last move caused a new piece to be placed.
         */
        PLACE
    }

    /**
     * The valid actions that can be taken on the board.
     */
    public enum Action {
        /**
         * Attempt to move the piece one position to the left.
         */
        LEFT,

        /**
         * Attempt to move the piece one position to the right.
         */
        RIGHT,

        /**
         * Attempt to move the piece one position down.
         */
        DOWN,

        /**
         * Attempt to drop the piece all the way, placing it wheverever it
         * lands.
         */
        DROP,

        /**
         * Attempt to rotate the piece clockwise, applying wall-kicks if
         * neccessary.
         */
        CLOCKWISE,

        /**
         * Attempt to rtate the piece counter-clockwise, applying wall-kicks if
         * neccessary.
         */
        COUNTERCLOCKWISE,

        /**
         * Do nothing.
         */
        NOTHING,

        /**
         * "Hold" a piece until a later time, or "unhold" the piece so it
         * returns to play.
         *
         * Used only in karma.
         */
        HOLD
    }

    /**
     * Allows the client to interact with the active piece on the board.
     * The above enums are used for the inputs and outputs of the function
     */
    Result move(Action act);

    /**
     * Returns a new board whose state is equal to what the state of this
     * board would be after the input Action.
     * This is useful for any AI using the board.
     */
    Board testMove(Action act);

    /**
     * Give a piece to the board to use as its next piece
     */
    void nextPiece(Piece p);

    /**
     * Every board should be able to tell if another Board is in the same
     * state as itself
     */
    boolean equals(Object other);

    /**
     * Returns the result of the last action given to the board.
     */
    Result getLastResult();

    /**
     * Returns the last action given to the board.
     */
    Action getLastAction();

    /**
     * Returns the number of rows cleared by the last action.
     */
    int getRowsCleared();

    /**
     * Returns the width of the board in blocks.
     */
    int getWidth();

    /**
     * Returns the height of the board in blocks.
     */
    int getHeight();

    /**
     * Returns the max column height present in the board.  For an empty board
     * this is 0.
     */
    int getMaxHeight();

    /**
     * Given a piece and an x, returns the y value where the piece would come to
     * rest if it were dropped straight down at that x.
     */
    int dropHeight(Piece piece, int x);

    /**
     * Returns the height of the given column -- i.e. the y value of the highest
     * block + 1.  The height is 0 if the column contains no blocks.
     */
    int getColumnHeight(int x);

    /**
     * Returns the number of filled blocks in the given row.
     */
    int getRowWidth(int y);

    /**
     * Returns true if the given block is filled in the board.  Blocks outside
     * of the valid width/height area always return true.
     */
    boolean getGrid(int x, int y);
}
