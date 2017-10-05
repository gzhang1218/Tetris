package assignment;

/**
 * Brain interface for JTetris; has a single method which takes the current
 * board state and returns the desired action.
 */
public interface Brain {
    /**
     * Decide what the next move should be based on the state of the board.
     */
    Board.Action nextMove(Board currentBoard);
}
