package assignment;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class JTacticalBrainTetris extends JTetris {
    protected Brain brain;
    protected boolean brainActive = true;
    protected Board.Action nextMove;


    JTacticalBrainTetris() {
        super();
        brain = new TacticalBrain();
    }

    /*public void stopGame() {
        super.stopGame();
    }*/

    public void tick(Board.Action verb) {
        if (!gameOn) {
            return;
        }
        if (verb == Board.Action.DOWN && brainActive) {
            nextMove = brain.nextMove(board);
        }
        if (nextMove != null) {
            super.tick(nextMove);
        }
        super.tick(verb);
    }

    public static void main(String[] args) {
        createGUI(new JTacticalBrainTetris());
    }

}
