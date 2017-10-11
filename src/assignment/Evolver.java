package assignment;

import java.util.*;
import java.io.*;

public class Evolver {

    private int population = 1000;
    private TacticalBrainFitness[] brains = new TacticalBrainFitness[population];
    //private int[] fitnesses = new int[population];

    //choose 10% of population for bestBrains
    private int sample = population/10;
    private TacticalBrain[] randomBrains = new TacticalBrain[sample];
    private int[] scores = new int[sample];
    private int highScoreIndex1;
    private int highScoreIndex2;

    private int maxChildren = 3*population/10;
    private TacticalBrain[] children = new TacticalBrain[maxChildren];

    private int generations = 100;
    private int trials = 100;
    private int pieceLimit = 750;

    private Random rand = new Random();


    public Evolver() {
        //populate brains with TacticalBrains
        //TODO FIX NULLPOINTEREXCEPTION
        for(int i = 0; i < population; i++) {
            brains[i].brain = new TacticalBrain();
        }
        readWeights();
    }

    public static void main(String[] args) throws IOException {
        Evolver evolver = new Evolver();
        evolver.evolve();
    }

    public void readWeights() {
        /*try (Scanner scan = new Scanner(new File("weights.txt"))) {
            for(int i = 0; i < champs.length; i++){
                double[] weights = new double[BetterBrain.WEIGHTS];
                for(int j = 0; j < BetterBrain.WEIGHTS; j++){
                    weights[j] = scan.nextDouble();
                }
                champs[i] = weights;
            }
        }catch(IOException e){
        }*/
    }

    public void evolve() {

        for (int a = 0; a < generations; a++) {
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
            averageWeights[i] = (scaledWeight1 + scaledWeight2);
        }
        averageWeights = mutate(averageWeights);
        child.setWeights(averageWeights);
        return child;
    }


    public double[] mutate(double[] weights) {
        if (rand.nextInt(100) < 5) {
            int weightIndex = rand.nextInt(weights.length);
            weights[weightIndex] = weights[weightIndex] + (rand.nextDouble() * 0.4 - 0.2);
        }
        return weights;
    }

    public int gameSimulator(Brain brain) {
        int score = 0;
        while (score < 750)
        {
            break;
        }
        //return score;
        return rand.nextInt(100);
    }
}

//needed for sorting
class TacticalBrainFitness implements Comparable<TacticalBrainFitness>{

    TacticalBrain brain;
    int fitness;

    @Override
    public int compareTo(TacticalBrainFitness x) {
        return (this.fitness - x.fitness);
    }
}
