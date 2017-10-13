package assignment;

import org.junit.Test;

import static org.junit.Assert.*;

public class TetrisBoardTest {

    public static final String stickPiece = "0 0  1 0  2 0  3 0";
    public static final String jPiece = "0 1  1 1  2 1  2 0";
    public static final String lPiece = "0 0  0 1  1 1  2 1";
    public static final String rdPiece = "0 0  1 0  1 1  2 1";
    public static final String ldPiece = "0 1  1 1  1 0  2 0";
    public static final String squarePiece = "0 0  0 1  1 0  1 1";
    public static final String tPiece = "0 1  1 0  1 1  2 1";

    @Test
    public void testMove() throws Exception {
        int myWidth = JTetris.WIDTH * 2, myHeight = JTetris.HEIGHT * 3;

        // create a board of arbitrary dimensions
        Board board = new TetrisBoard(myWidth, myHeight);

        // check the dimension getters
        assertEquals(myWidth, board.getWidth());
        assertEquals(myHeight, board.getHeight());

        // try to move without a piece, should have NO_PIECE return
        assertEquals(Board.Result.NO_PIECE, board.move(Board.Action.DOWN));

        // even though NO_PIECE, the lastAction and lastResult should match up
        assertEquals(Board.Action.DOWN, board.getLastAction());
        assertEquals(Board.Result.NO_PIECE, board.getLastResult());

        // give a piece
        board.nextPiece(TetrisPiece.getPiece(stickPiece));

        //  test each of the actions
        assertEquals(Board.Result.SUCCESS, board.move(Board.Action.LEFT));
        assertEquals(Board.Result.SUCCESS, board.move(Board.Action.RIGHT));
        assertEquals(Board.Result.SUCCESS, board.move(Board.Action.DOWN));
        assertEquals(Board.Result.SUCCESS, board.move(Board.Action.CLOCKWISE));
        assertEquals(Board.Result.SUCCESS, board.move(Board.Action.COUNTERCLOCKWISE));
        assertEquals(Board.Result.SUCCESS, board.move(Board.Action.NOTHING));
        assertEquals(Board.Result.SUCCESS, board.move(Board.Action.HOLD));

        // DROP should return PLACE
        assertEquals(Board.Result.PLACE, board.move(Board.Action.DROP));

        // there should be NO_PIECE after a PLACE
        assertEquals(Board.Result.NO_PIECE, board.move(Board.Action.DOWN));

        // did the piece actually fall down
        assertEquals(4, board.getRowWidth(0));
        for (int col = 0; col < board.getWidth(); col++) {
            if (col >= board.getWidth() / 2 && col < board.getWidth() / 2 + 4)
                assertEquals(1, board.getColumnHeight(col));
            else
                assertEquals(0, board.getColumnHeight(col));
        }
    }

    @Test
    public void testTestMove() throws Exception {
        //THIS SECTION TESTS THAT TESTMOVE RETURNS A SEPARATE BOARD THAT STILL FUNCTIONS
        // make a 'standard' board
        Board board = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
        // add a horizontal I-piece
        board.nextPiece(TetrisPiece.getPiece(stickPiece));
        // TESTMOVE a LEFT and put result in board2
        Board board2 = board.testMove(Board.Action.LEFT);
        assertNotEquals(board, board2);
        // now move the original LEFT
        board.move(Board.Action.LEFT);
        // the board states should be identical but separate
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
    public void testEquals() throws Exception {
        Board board = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT + JTetris.TOP_SPACE);

        //check random object
        assertNotEquals(board, new Object());

        //check different dimensions
        Board board2 = new TetrisBoard(1, 1);
        assertNotEquals(board, board2);

        //check obviously different boards
        board2 = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT + JTetris.TOP_SPACE);
        board2.nextPiece(TetrisPiece.getPiece(jPiece));
        board2.move(Board.Action.DROP);
        assertNotEquals(board, board2);

        //check different lastAction
        board = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT + JTetris.TOP_SPACE);
        board2 = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT + JTetris.TOP_SPACE);
        board.nextPiece((TetrisPiece.getPiece(rdPiece)));
        board2.nextPiece((TetrisPiece.getPiece(rdPiece)));
        board.move(Board.Action.CLOCKWISE);
        board.move(Board.Action.CLOCKWISE);
        board2.move(Board.Action.DOWN);
        //these series of moves should result in identical final positions, but different Actions
        assertNotEquals(board, board2);

        //another test for different lastActions
        board = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT + JTetris.TOP_SPACE);
        board2 = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT + JTetris.TOP_SPACE);
        board.nextPiece((TetrisPiece.getPiece(rdPiece)));
        board2.nextPiece((TetrisPiece.getPiece(rdPiece)));
        board.move(Board.Action.NOTHING);
        board2.move(Board.Action.HOLD);
        //both moves should do nothing and and result in SUCCESS, just different Actions
        assertNotEquals(board, board2);

        //check different lastResult
        board = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT + JTetris.TOP_SPACE);
        board2 = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT + JTetris.TOP_SPACE);
        board.nextPiece((TetrisPiece.getPiece(rdPiece)));
        board2.nextPiece((TetrisPiece.getPiece(rdPiece)));
        for (int x = JTetris.WIDTH / 2 ; x > 0 ; x-- ) {
            board.move(Board.Action.LEFT);
            board2.move(Board.Action.LEFT);
        }
        // both pieces are at the left-most edge
        board.move(Board.Action.LEFT); // should result in a OUTOFBOUNDS, and no change
        assertNotEquals(board, board2);
    }

    @Test
    public void testDropHeight() throws Exception {
        Board board = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT + JTetris.TOP_SPACE);

        // check that a piece will fall to the ground y = 0
        assertEquals(0, board.dropHeight(TetrisPiece.getPiece(stickPiece), board.getWidth() / 2));

        // place a horizontal stick piece in the way
        board.nextPiece(TetrisPiece.getPiece(stickPiece));
        board.move(Board.Action.DROP);

        // check that a horizontal stick piece will fall to the ground y = 0
        assertEquals(1, board.dropHeight(TetrisPiece.getPiece(stickPiece), board.getWidth() / 2));

        // check that a RD piece will be able to fall to the ground
        assertEquals(0, board.dropHeight(TetrisPiece.getPiece(rdPiece), board.getWidth() / 2 - 2));

        // stack another horizontal stick piece
        board.nextPiece(TetrisPiece.getPiece(stickPiece));
        board.move(Board.Action.DROP);

        // check that an inverted J piece will be able to fall to the ground
        assertEquals(0, board.dropHeight(
                TetrisPiece.getPiece(jPiece).nextRotation(),
                board.getWidth() / 2 - 1));

        // stack yet another horizontal stick piece
        board.nextPiece(TetrisPiece.getPiece(stickPiece));
        board.move(Board.Action.DROP);

        // check that an inverted J piece will not be able to fall to the ground (stack of sticks is now 3 high)
        assertEquals(1, board.dropHeight(
                TetrisPiece.getPiece(jPiece).nextRotation(),
                board.getWidth() / 2 - 1));

    }

    @Test
    public void testGetRowWidth() throws Exception {
        Board board = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT + JTetris.TOP_SPACE);

        //check that every row is empty on an empty board
        for (int row  = 0 ; row < board.getHeight() ; row ++ )
            assertEquals(0, board.getRowWidth(row));

        board.nextPiece(TetrisPiece.getPiece(stickPiece));
        board.move(Board.Action.CLOCKWISE);
        board.move(Board.Action.DROP);

        for (int row = 0 ; row < board.getHeight() ; row ++ ) {
            if (row <= 3) // the bottom 4 rows should have 1 block
                assertEquals(1, board.getRowWidth(row));
            else
                assertEquals(0, board.getRowWidth(row));
        }
    }

}