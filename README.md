# Tetris
A Java implementation of fully playable Tetris in addition to a self-playing AI optimized through genetic evolution.

Currently, the AI can play indefinitely, reaching scores of around 1 million rows cleared in about an hour while running at maximum speed.

### Brief Summary of AI

The AI considers every possible position and orientation that the current piece can be placed onto the board, and calculates a score for each of these. In addition, pieces can be held as is the case with most versions of the game today, and the AI considers all possible placements of the held piece as well. The score of a placement is determined by the sum of the heights of every block, the number of full rows which can be cleared, the number of holes in the board, the sum of the number of blocks above a hole (a blockade), the sum of the absolute height difference between every two adjacent columns, the number of blocks touching the edge, and the number of blocks touching the floor. These parameters are weighted according to their importance when assessing the state of a board. 

### Genetic Evolution
The weights are determined through a genetic algorithm, where 1000 sets of random weights are generated as a population and used to simulate 30-50 games of Tetris each. The scores (rows cleared) for each game are summed to produce a fitness score for the given set of weights. A random sample (10%) of the population is taken and the two sets of weights with the highest fitness scores are used to "reproduce" and create a "child" which is the weighted average of the two "parent" weight sets. Additionally, each child has a chance to mutate (10%), which will select a random weight and adjust it randomly in the range of -0.2 to +0.2. The sampling and reproduction continues until a critical mass of "children" is reached (30% of the population) and the weakest performing sets are replaced with the "children", while the population remains 1000. This cycle continues for 25-50 generations, eventually producing the highest performing weights that will yield the most optimal scoring function.
