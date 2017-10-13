package assignment;

import java.util.*;

public class TacticalBrain implements Brain {

    private ArrayList<Board> options;
    private ArrayList<Board.Action> firstMoves;

    private static final int WEIGHTS = 7;
    private double[] weights = new double[WEIGHTS];

    private Random rand = new Random();

    public TacticalBrain() {
        //weights = new double[]{-0.6102501489040298, 0.714374500112446, -0.9015603583671786, -0.4715603583671786, -0.52544587956874866};
        weights = new double[]{-1.020828988488132, -0.9292577207656729, -1.3586823994937924, -0.20814941564555897, -0.6152158288454252, 0.11026280385014185, -0.3805911191245738};
        //genRandomWeights();
    }

    public double[] getWeights(){
        return weights;
    }

    public void setWeights(double[] weights){
        this.weights = weights;
    }

    public void genRandomWeights() {
        for (int i = 0; i < WEIGHTS; i++) {
            weights[i] = (rand.nextDouble() * 2.0) - 2.0;
        }
    }

    /**
     * Decide what the next move should be based on the state of the board.
     */
    public Board.Action nextMove(Board currentBoard) {
        // Fill the our options array with versions of the new Board
        options = new ArrayList<>();
        firstMoves = new ArrayList<>();
        enumerateOptions(currentBoard);

        double best = Integer.MIN_VALUE;
        int bestIndex = 0;

        // Check all of the options and get the one with the highest score
        for (int i = 0; i < options.size(); i++) {
            double score = scoreBoard(options.get(i));
            if (score > best) {
                best = score;
                bestIndex = i;
            }
        }

/*        System.out.println("I chose the " + bestIndex + "th move out of " + options.size());

        System.out.println("I'm " + firstMoves.get(bestIndex));

        System.out.println("Score is: " + scoreBoard(options.get(bestIndex)));*/

        // We want to return the first move on the way to the best Board
        return firstMoves.get(bestIndex);
    }

    /**
     * Test all of the places we can put the current Piece.
     * Since this is just a Lame Brain, we aren't going to do smart
     * things like rotating pieces.
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

    private double scoreBoard(Board newBoard) {
        //TODO add more variables??? Ex: edges touching another block, edges touching wall, edges touching floor
        //sum of all column heights, want to minimize this
        int totalHeight = 0;
        for (int i = 0; i < newBoard.getWidth(); i++) {
            totalHeight = totalHeight + newBoard.getColumnHeight(i);
        }

        //TODO check if we are using rowsCleared correctly in TetrisBoard
        //TODO also check if we are catching the fullRows before they are cleared
        //number of rows that can be cleared, maximize this
        int fullRows = newBoard.getRowsCleared();
        //System.out.println(fullRows);

        //TODO: implement hole function (empty space such
        //TODO: that there is at least one tile in column above it)
        //TODO: Constant time?
        //number of holes present in the board, minimize this
        int holes = 0, blockades = 0;
        for (int x = 0; x < newBoard.getWidth(); x++) {
            int holesInCol = 0;
            for (int y = 0; y < newBoard.getColumnHeight(x); y++) {
                if (!newBoard.getGrid(x, y)) // if empty
                    holesInCol++;
                if (holesInCol != 0 && newBoard.getGrid(x, y)) // a hole has been found
                    blockades++;
            }
            holes += holesInCol;
        }

        // "bumpiness" of the board, sum of the differences of all adjacent columns
        // minimize this
        int inconsistency = 0;
        for (int i = 0; i < newBoard.getWidth()-1; i++) {
            int diff = newBoard.getColumnHeight(i) - newBoard.getColumnHeight(i+1);
            inconsistency = inconsistency + Math.abs(diff);
        }

        int edgeBlocks = 0;
        for (int y = 0; y < newBoard.getHeight(); y++)
        {
            if (newBoard.getGrid(0,y))
                edgeBlocks++;
            if (newBoard.getGrid((newBoard.getWidth()-1),y))
                edgeBlocks++;
        }
        int floorBlocks = 0;
        for (int x = 0; x < newBoard.getWidth(); x++) {
            if (newBoard.getGrid(x,0))
                floorBlocks++;
        }

        double score = weights[0]*totalHeight + weights[1]*fullRows + weights[2]*holes + weights[3]*blockades + weights[4]*inconsistency + weights[5]*edgeBlocks + weights[6]*floorBlocks;
        return score;
    }
}
