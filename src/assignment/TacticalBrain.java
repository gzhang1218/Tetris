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
        //weights = new double[]{-1.020828988488132, 0.9292577207656729, -1.3586823994937924, -0.20814941564555897, -0.6152158288454252, 0.11026280385014185, -0.3805911191245738}; //goodone
        //weights = new double[]{-1.3067101996001897, -0.5776263525445752, -1.3240709146729546, -0.31311290018089394, -0.7883146132012633, -0.29163742419386496, -0.6307497961281868};
        //weights = new double[]{-0.510066, 0.760666, -0.35663, 0, -0.184483, 0, 0};
        //weights = new double[]{-1.5406684363464582, -0.7509873596334373, -1.4781405702056236, -0.3194835404635906, -0.6745877009088455, 0.01867967152179941, -0.565265239315107};
        weights = new double[]{-1.020828988488132, 0.9292577207656729, -1.3586823994937922, -0.11175037697890922, -0.38568284549465454, 0.12050772756889917, 0}; //bestone
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
     * Test all drops with rotations
     *
     * UPDATE: needed to rotate before translating -- I pieces were not working!
     */
    void enumerateOptions(Board currentBoard) {

        // get copies of the board in the 4 rotation states
        Board zero = currentBoard.testMove(Board.Action.NOTHING); // just copy the board
        Board left = zero.testMove(Board.Action.COUNTERCLOCKWISE);
        Board two = left.testMove(Board.Action.COUNTERCLOCKWISE);
        Board right = two.testMove(Board.Action.COUNTERCLOCKWISE);

        // get plain drops, only if the original rotation was successful
        if (zero.getLastResult() == Board.Result.SUCCESS) {
            options.add(zero.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.DROP);
        }
        if (left.getLastResult() == Board.Result.SUCCESS) {
            options.add(left.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.COUNTERCLOCKWISE);
        }
        if (two.getLastResult() == Board.Result.SUCCESS) {
            options.add(two.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.COUNTERCLOCKWISE);
        }
        if (right.getLastResult() == Board.Result.SUCCESS) {
            options.add(right.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.CLOCKWISE);
        }

        // CHECKING LEFT TRANSLATIONS

        // go through left translations of the spawn state, only if the original rotation was successful
        if (zero.getLastResult() == Board.Result.SUCCESS) {
            Board left0 = zero.testMove(Board.Action.LEFT);
            while (left0.getLastResult() == Board.Result.SUCCESS) {
                options.add(left0.testMove(Board.Action.DROP));
                firstMoves.add(Board.Action.LEFT);
                left0.move(Board.Action.LEFT);
            }
        }

        // go through left translations of the left state, only if the original rotation was successful
        if (left.getLastResult() == Board.Result.SUCCESS) {
            Board leftL = left.testMove(Board.Action.LEFT);
            while (leftL.getLastResult() == Board.Result.SUCCESS) {
                options.add(leftL.testMove(Board.Action.DROP));
                firstMoves.add(Board.Action.COUNTERCLOCKWISE);
                leftL.move(Board.Action.LEFT);
            }
        }

        // go through left translations of the 180 state, only if the original rotation was successful
        if (two.getLastResult() == Board.Result.SUCCESS) {
            Board left2 = two.testMove(Board.Action.LEFT);
            while (left2.getLastResult() == Board.Result.SUCCESS) {
                options.add(left2.testMove(Board.Action.DROP));
                firstMoves.add(Board.Action.COUNTERCLOCKWISE);
                left2.move(Board.Action.LEFT);
            }
        }

        // go through left translations of the right state, only if the original rotation was successful
        if (right.getLastResult() == Board.Result.SUCCESS) {
            Board leftR = right.testMove(Board.Action.LEFT);
            while (leftR.getLastResult() == Board.Result.SUCCESS) {
                options.add(leftR.testMove(Board.Action.DROP));
                firstMoves.add(Board.Action.CLOCKWISE);
                leftR.move(Board.Action.LEFT);
            }
        }

        // CHECKING RIGHT TRANSLATIONS

        // go through right translations of the spawn state, only if the original rotation was successful
        if (zero.getLastResult() == Board.Result.SUCCESS) {
            Board right0 = zero.testMove(Board.Action.RIGHT);
            while (right0.getLastResult() == Board.Result.SUCCESS) {
                options.add(right0.testMove(Board.Action.DROP));
                firstMoves.add(Board.Action.RIGHT);
                right0.move(Board.Action.RIGHT);
            }
        }

        // go through right translations of the left state, only if the original rotation was successful
        if (left.getLastResult() == Board.Result.SUCCESS) {
            Board rightL = left.testMove(Board.Action.RIGHT);
            while (rightL.getLastResult() == Board.Result.SUCCESS) {
                options.add(rightL.testMove(Board.Action.DROP));
                firstMoves.add(Board.Action.COUNTERCLOCKWISE);
                rightL.move(Board.Action.RIGHT);
            }
        }

        // go through right translations of the 180 state, only if the original rotation was successful
        if (two.getLastResult() == Board.Result.SUCCESS) {
            Board right2 = two.testMove(Board.Action.RIGHT);
            while (right2.getLastResult() == Board.Result.SUCCESS) {
                options.add(right2.testMove(Board.Action.DROP));
                firstMoves.add(Board.Action.COUNTERCLOCKWISE);
                right2.move(Board.Action.RIGHT);
            }
        }

        // go through right translations of the right state, only if the original rotation was successful
        if (right.getLastResult() == Board.Result.SUCCESS) {
            Board rightR = right.testMove(Board.Action.RIGHT);
            while (rightR.getLastResult() == Board.Result.SUCCESS) {
                options.add(rightR.testMove(Board.Action.DROP));
                firstMoves.add(Board.Action.CLOCKWISE);
                rightR.move(Board.Action.RIGHT);
            }
        }

    }

    double scoreBoard(Board newBoard) {
        //TODO add more variables??? Ex: edges touching another block, edges touching wall, edges touching floor
        // get the summed height - each block has value of its height
        int totalHeight = 0;
        for (int row = 0; row < newBoard.getHeight(); row++) {
            totalHeight += (row + 1) * newBoard.getRowWidth(row);
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

    public ArrayList<Board.Action> getFirstMoves() {
        return firstMoves;
    }

    public ArrayList<Board> getOptions() {
        return options;
    }
}
