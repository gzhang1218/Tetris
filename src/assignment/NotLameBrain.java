package assignment;

import java.util.*;

/**
 * LameBrain but with rotations
 */
public class NotLameBrain implements Brain {

    private ArrayList<Board> options;
    private ArrayList<Board.Action> firstMoves;

    /**
     * Decide what the next move should be based on the state of the board.
     */
    public Board.Action nextMove(Board currentBoard) {
        // Fill the our options array with versions of the new Board
        options = new ArrayList<>();
        firstMoves = new ArrayList<>();
        enumerateOptions(currentBoard);

        int best = 0;
        int bestIndex = 0;

        // Check all of the options and get the one with the highest score
        for (int i = 0; i < options.size(); i++) {
            int score = scoreBoard(options.get(i));
            if (score > best) {
                best = score;
                bestIndex = i;
            }
        }

        // We want to return the first move on the way to the best Board
        return firstMoves.get(bestIndex);
    }

    /**
     * Test all of the places we can put the current Piece, including rotations
     */
    private void enumerateOptions(Board currentBoard) {
        // Check out rotations in place
        Board center = currentBoard.testMove(Board.Action.NOTHING); // just copy the board
        options.add(center.testMove(Board.Action.DROP));
        firstMoves.add(Board.Action.DROP);
        for(int i = 0; i < 3; i++) {
            center.move(Board.Action.CLOCKWISE);
            options.add(center.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.CLOCKWISE);
        }


        // Now we'll add all the places to the left we can DROP
        Board left = currentBoard.testMove(Board.Action.LEFT);
        while (left.getLastResult() == Board.Result.SUCCESS) {
            options.add(left.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.LEFT);

            // before the left shift, test out rotations too
            Board leftCenter = left.testMove(Board.Action.NOTHING);
            for(int i = 0; i < 3; i++) {
                leftCenter.move(Board.Action.CLOCKWISE);
                options.add(leftCenter.testMove(Board.Action.DROP));
                firstMoves.add(Board.Action.LEFT);
            }

            left.move(Board.Action.LEFT);
        }

        // And then the same thing to the right
        Board right = currentBoard.testMove(Board.Action.RIGHT);
        while (right.getLastResult() == Board.Result.SUCCESS) {
            options.add(right.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.RIGHT);

            // before the left shift, test out rotations too
            Board rightCenter = right.testMove(Board.Action.NOTHING);
            for(int i = 0; i < 3; i++) {
                rightCenter.move(Board.Action.CLOCKWISE);
                options.add(rightCenter.testMove(Board.Action.DROP));
                firstMoves.add(Board.Action.RIGHT);
            }

            right.move(Board.Action.RIGHT);
        }
    }

    /**
     * Penalize: height, holes
     * Reward: clearRows, edges that touch blocks, walls, floor
     */
    private int scoreBoard(Board newBoard) {
        return 100 - (newBoard.getMaxHeight() * 5);
    }

}
