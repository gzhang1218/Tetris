package assignment;

import org.junit.Test;

import static org.junit.Assert.*;

public class TacticalBrainTest {

    @Test
    public void testNextMove() throws Exception {
        TacticalBrain brain = new TacticalBrain();
        Board board = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT + JTetris.TOP_SPACE);
        board.nextPiece(TetrisPiece.getPiece(TetrisPiece.I));

        // nextMove calls enumerateOptions
        Board.Action action = brain.nextMove(board);

        // after enumerate options, should be as many elements as possible rotations + translations
        assertEquals(2 * 7 + 2 * 10, brain.getOptions().size());
        assertEquals(2 * 7 + 2 * 10, brain.getFirstMoves().size());

        // also check that the action is either LEFT, RIGHT, DROP, COUNTERCLOCKWISE, or CLOCKWISE
        assertTrue(action == Board.Action.LEFT || action == Board.Action.RIGHT
                || action == Board.Action.DROP || action == Board.Action.COUNTERCLOCKWISE
                || action == Board.Action.CLOCKWISE);

        // repeat checking the size of the options for different pieces

        // adding a T piece
        board.nextPiece(TetrisPiece.getPiece(TetrisPiece.T));
        brain.nextMove(board);
        assertEquals(2 * 8 + 2 * 9, brain.getOptions().size());
        assertEquals(2 * 8 + 2 * 9, brain.getFirstMoves().size());

        // adding a RD piece
        board.nextPiece(TetrisPiece.getPiece(TetrisPiece.RD));
        brain.nextMove(board);
        assertEquals(2 * 8 + 2 * 9, brain.getOptions().size());
        assertEquals(2 * 8 + 2 * 9, brain.getFirstMoves().size());

        // adding a LD piece
        board.nextPiece(TetrisPiece.getPiece(TetrisPiece.LD));
        brain.nextMove(board);
        assertEquals(2 * 8 + 2 * 9, brain.getOptions().size());
        assertEquals(2 * 8 + 2 * 9, brain.getFirstMoves().size());

        // adding a square piece
        board.nextPiece(TetrisPiece.getPiece(TetrisPiece.S));
        brain.nextMove(board);
        assertEquals(4 * 9, brain.getOptions().size());
        assertEquals(4 * 9, brain.getFirstMoves().size());

    }

    @Test
    public void testScoreBoard() throws Exception {
        TacticalBrain brain = new TacticalBrain();

        // varying board states to control each heuristic is difficult
        // I'll just test the simplest case -- when everyhing is 0
        Board board = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT + JTetris.TOP_SPACE);
        double score = brain.scoreBoard(board);
        assertTrue(score == 0);
    }

}