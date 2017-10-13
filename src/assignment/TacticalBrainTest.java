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
        // After that there should be
        // 7 translations x 4 rotations
        // = 28 different options
        Board.Action action = brain.nextMove(board);

        assertEquals(4 * 7, brain.getOptions().size());
        assertEquals(4 * 7, brain.getFirstMoves().size());

        // also check that the action is either LEFT, RIGHT, DROP, COUNTERCLOCKWISE, or CLOCKWISE
        assertTrue(action == Board.Action.LEFT || action == Board.Action.RIGHT
                || action == Board.Action.DROP || action == Board.Action.COUNTERCLOCKWISE
                || action == Board.Action.CLOCKWISE);

        // repeat checking the size of the options for different pieces
        // adding a vertical stick
        board.nextPiece(TetrisPiece.getPiece(TetrisPiece.I).nextRotation());
        brain.nextMove(board);
        // should be 10 translations and 4 rotations = 40 options
        assertEquals(4 * 10, brain.getOptions().size());
        assertEquals(4 * 10, brain.getFirstMoves().size());

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