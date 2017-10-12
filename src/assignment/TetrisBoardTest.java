package assignment;

import org.junit.Test;

import static org.junit.Assert.*;

public class TetrisBoardTest {

    /**
     * A piece string representing the horizontal stick.
     */
    public static final String stickPiece = "0 0  1 0  2 0  3 0";

    @Test
    public void move() throws Exception {
    }

    @Test
    public void testMove() throws Exception {
        // make a 'standard' board
        Board board = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);

        // add a horizontal I-piece
        board.nextPiece(TetrisPiece.getPiece(stickPiece));

        // TESTMOVE a LEFT and put result in board2
        Board board2 = board.testMove(Board.Action.LEFT);

        assertNotEquals(board, board2);

        // now move the original LEFT
        board.move(Board.Action.LEFT);

        assertEquals(board, board2);

        // now have a testMove off of the testMove
        Board board3 = board2.testMove(Board.Action.RIGHT);

        // move the original back to starting point
        board.move(Board.Action.RIGHT);

        assertEquals(board, board3);

        // DROP the original
        board.move(Board.Action.DROP);

        assertNotEquals(board, board3);
    }

    @Test
    public void nextPiece() throws Exception {
    }

    @Test
    public void equals() throws Exception {
    }

    @Test
    public void dropHeight() throws Exception {
    }

    @Test
    public void getRowWidth() throws Exception {
    }

}