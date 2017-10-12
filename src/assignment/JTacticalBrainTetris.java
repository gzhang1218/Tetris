package assignment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import assignment.Board.Action;

/**
 * An extension of JTetris which runs an implementation of a Brain.
 */
public class JTacticalBrainTetris extends JTetris {
    /**
     * The brain being used.
     */
    private Brain brain;

    /**
     * Creates a new JTetris running the given Brain.
     * @param brain The brain to use.
     */
    public JTacticalBrainTetris(Brain brain) {
        super();

        this.brain = brain;
        this.timer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Action act = brain.nextMove(JTacticalBrainTetris.this.board);
                tick(act);
            }
        });
    }

    public static void main(String[] args) {
        // Replace LameBrain with your own Brain implementation.
        createGUI(new JTacticalBrainTetris(new TacticalBrain()));
    }
}