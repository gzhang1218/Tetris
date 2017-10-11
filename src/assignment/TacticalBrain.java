package assignment;

import java.util.*;

public class TacticalBrain implements Brain {

    private ArrayList<Board> options;
    private ArrayList<Board.Action> firstMoves;

    private static final int WEIGHTS = 4;
    private double[] weights = new double[WEIGHTS];

    public TacticalBrain() {
        weights = new double[]{2, 2, -2, -2};
    }

    public double[] getWeights(){
        return weights;
    }

    public void setWeights(double[] weights){
        this.weights = weights;
    }
    /**
     * Decide what the next move should be based on the state of the board.
     */
    public Board.Action nextMove(Board currentBoard) {
        // Fill the our options array with versions of the new Board
        options = new ArrayList<>();
        firstMoves = new ArrayList<>();
        enumerateOptions(currentBoard);

        double best = 0;
        int bestIndex = 0;

        // Check all of the options and get the one with the highest score
        for (int i = 0; i < options.size(); i++) {
            double score = scoreBoard(options.get(i));
            if (score > best) {
                best = score;
                bestIndex = i;
            }
        }

        // We want to return the first move on the way to the best Board
        return firstMoves.get(bestIndex);
    }

    /**
     * Test all of the places we can put the current Piece.
     * Since this is just a Lame Brain, we aren't going to do smart
     * things like rotating pieces.
     */
    private void enumerateOptions(Board currentBoard) {

        //TODO Check whether this is implemented correctly
        Board rotateBoard = currentBoard;
        for (int i = 0; i < 4; i++) {
            // We can always drop our current Piece
            options.add(rotateBoard.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.DROP);

            // Now we'll add all the places to the left we can DROP
            Board left = rotateBoard.testMove(Board.Action.LEFT);
            while (left.getLastResult() == Board.Result.SUCCESS) {
                options.add(left.testMove(Board.Action.DROP));
                firstMoves.add(Board.Action.LEFT);
                left.move(Board.Action.LEFT);
            }

            // And then the same thing to the right
            Board right = rotateBoard.testMove(Board.Action.RIGHT);
            while (right.getLastResult() == Board.Result.SUCCESS) {
                options.add(right.testMove(Board.Action.DROP));
                firstMoves.add(Board.Action.RIGHT);
                right.move(Board.Action.RIGHT);
            }

            //is there a difference between CR and CCR? do we also need to
            //rotate it in the CCR direction?
            rotateBoard.move(Board.Action.CLOCKWISE);
        }
        //TODO maybe instead of this, we test the left and right options with all possible rotation states
        /*Board rotateCR = currentBoard.testMove(Board.Action.CLOCKWISE);
        if (rotateCR.getLastResult() == Board.Result.SUCCESS) {
            options.add(rotateCR.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.CLOCKWISE);
            rotateCR.move(Board.Action.CLOCKWISE);
        }

        Board rotateCCR = currentBoard.testMove(Board.Action.COUNTERCLOCKWISE);
        if (rotateCCR.getLastResult() == Board.Result.SUCCESS) {
            options.add(rotateCCR.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.COUNTERCLOCKWISE);
            rotateCCR.move(Board.Action.COUNTERCLOCKWISE);
        }*/
    }

    private double scoreBoard(Board newBoard) {
        //sum of all column heights, want to minimize this
        int totalHeight = 0;
        for (int i = 0; i < newBoard.getWidth(); i++) {
            totalHeight = totalHeight + newBoard.getColumnHeight(i);
        }

        //TODO check if we are using rowsCleared correctly in TetrisBoard
        //TODO also check if we are catching the fullRows before they are cleared
        //number of rows that can be cleared, maximize this
        int fullRows = 0;
        for (int i = 0; i < newBoard.getHeight(); i++) {
            if (newBoard.getRowWidth(i) == newBoard.getWidth()) {
                fullRows++;
            }
        }
        System.out.println(fullRows);

        //TODO: implement hole function (empty space such
        //TODO: that there is at least one tile in column above it)
        //TODO: Constant time?
        //number of holes present in the board, minimize this
        int holes = 0;
        for (int x = 0; x < newBoard.getWidth(); x++) {
            for (int i = 0; i < newBoard.getColumnHeight(x); i++) {
                if (!newBoard.getGrid(x, i))
                    holes++;
            }
        }

        // "bumpiness" of the board, sum of the differences of all adjacent columns
        // minimize this
        int inconsistency = 0;
        for (int i = 0; i < newBoard.getWidth()-1; i++) {
            int diff = newBoard.getColumnHeight(i) - newBoard.getColumnHeight(i+1);
            inconsistency = inconsistency + Math.abs(diff);
        }

        double score = weights[0]*totalHeight + weights[1]*fullRows + weights[2]*holes + weights[3]*inconsistency;
        return score;
    }
}
