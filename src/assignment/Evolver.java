package assignment;

import java.util.*;
import java.io.*;

public class Evolver {

    private Board board;
    private int population = 1000;
    private TacticalBrainFitness[] brains = new TacticalBrainFitness[population];
    //private TacticalBrain[] testBrains = new TacticalBrain[population];

    //private int[] fitnesses = new int[population];

    //choose 10% of population for bestBrains
    private double samplePercentage = 0.1;
    private int sample = (int)(population*samplePercentage);
    private TacticalBrain[] randomBrains = new TacticalBrain[sample];
    private int[] scores = new int[sample];
    private int highScoreIndex1;
    private int highScoreIndex2;

    private double childPercentage = 0.3;
    private int maxChildren = (int)(population*childPercentage);
    private TacticalBrain[] children = new TacticalBrain[maxChildren];

    private int generations = 100;
    private int trials = 100;
    private int pieceLimit = 750;

    private Random rand = new Random();


    public Evolver() {
        //populate brains with TacticalBrains
        for(int i = 0; i < population; i++) {
            brains[i] = new TacticalBrainFitness();
            brains[i].brain = new TacticalBrain();
        }
        readWeights();
    }

    public static void main(String[] args) throws IOException {
        Evolver evolver = new Evolver();
        evolver.evolve();
    }

    public void readWeights() {
    }

    public void evolve() {

        for (int a = 0; a < generations; a++) {
            //DEBUG time taken
            long startTime = System.nanoTime();
            //reset fitness scores at the beginning of every generation
            for (int i = 0; i < population; i++) {
                brains[i].fitness = 0;
            }

            for (int i = 0; i < population; i++) {
                for (int j = 0; j < trials; j++) {
                    brains[i].fitness = brains[i].fitness + gameSimulator(brains[i].brain);
                }
            }

            Arrays.sort(brains);

            //produces maxChildren # of children into children array
            for (int j = 0; j < maxChildren; j++) {
                //takes a random sample (size = 100) of the population
                for (int i = 0; i < sample; i++) {
                    int randomBrainIndex = (int) (rand.nextInt(population));
                    randomBrains[i] = brains[randomBrainIndex].brain;
                    scores[i] = brains[randomBrainIndex].fitness;
                }

                //TODO implement tournament selection? pair up brains within the sample, pit them against each other
                //finds the two highest scores from the random sample
                int highScore1 = Integer.MIN_VALUE;
                int highScore2 = Integer.MIN_VALUE;

                for (int i = 0; i < sample; i++) {
                    if (scores[i] > highScore1) {
                        highScore2 = highScore1;
                        highScore1 = scores[i];
                        highScoreIndex2 = highScoreIndex1;
                        highScoreIndex1 = i;

                    } else if (scores[i] > highScore2 && scores[i] < highScore1) {
                        highScore2 = scores[i];
                        highScoreIndex2 = i;
                    }
                }
                children[j] = reproduce();
            }

            //culling the 30 worst and replacing them with children
            for (int i = 0; i < 30; i++) {
                brains[i].brain = children[i];
            }

            //DEBUG time taken
            System.out.printf("Generation %d finished. Time: %f seconds\n", a + 1, (System.nanoTime() - startTime) /
                    1000000000.0);
        }
        //DEBUG
        /*for (int i = 0; i < brains.length; i++) {
            double[] debugWeights = new double[brains[i].brain.getWeights().length];
            debugWeights = brains[i].brain.getWeights();
            for (int j = 0; j < debugWeights.length; j++) {
                System.out.print(debugWeights[j]+" ");
            }
            System.out.println();
        }*/
        Arrays.sort(brains);

        //final weights
        for (int i = 0; i < 4; i++) {
            System.out.println(brains[brains.length-1].brain.getWeights()[i]);
        }
    }

    //takes 2 brains and produces an offspring
    public TacticalBrain reproduce() {
        TacticalBrain child = new TacticalBrain();
        double[] averageWeights = new double[child.getWeights().length];
        double scaledWeight1;
        double scaledWeight2;

        //scaled average
        for (int i = 0; i < averageWeights.length; i++) {

            scaledWeight1 = randomBrains[highScoreIndex1].getWeights()[i] * scores[highScoreIndex1];
            scaledWeight2 = randomBrains[highScoreIndex2].getWeights()[i] * scores[highScoreIndex2];
            averageWeights[i] = (scaledWeight1 + scaledWeight2)/(scores[highScoreIndex1]+scores[highScoreIndex2]);
        }
        averageWeights = mutate(averageWeights);
        child.setWeights(averageWeights);
        return child;
    }


    public double[] mutate(double[] weights) {
        if (rand.nextInt(100) < 15) {
            int weightIndex = rand.nextInt(weights.length);
            weights[weightIndex] = weights[weightIndex] + (rand.nextDouble() * 0.5 - 0.25);
        }
        return weights;
    }

    public int gameSimulator(Brain simBrain) {
        int score = 0;

        boolean gameOn;
        Board.Action act;
        board = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT+JTetris.TOP_SPACE);
        gameOn = true;

        while (score < 750 && gameOn) {
            act = simBrain.nextMove(board);
            Board.Result result = board.move(act);
            switch (result) {
                case SUCCESS:
                case OUT_BOUNDS:
                    // The board is responsible for staying in a good state
                    break;
                case PLACE:
                    if (board.getMaxHeight() > JTetris.HEIGHT) {
                        gameOn = false;
                    }
                case NO_PIECE:
                    if (gameOn) {
                        board.nextPiece(pickNextPiece());
                    }
                    break;
            }
            score = score + board.getRowsCleared();
        }

        System.out.println(score);
        return score;
    }

    public Piece pickNextPiece() {
        int pieceNum = (int) (JTetris.pieceStrings.length * rand.nextDouble());
        Piece piece  = TetrisPiece.getPiece(JTetris.pieceStrings[pieceNum]);
        return(piece);
    }
}

//needed for sorting
class TacticalBrainFitness implements Comparable<TacticalBrainFitness>{

    TacticalBrain brain;
    int fitness;

    public TacticalBrainFitness() {

    }

    @Override
    public int compareTo(TacticalBrainFitness x) {
        return (this.fitness - x.fitness);
    }
}
