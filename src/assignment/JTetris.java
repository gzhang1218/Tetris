package assignment;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;


/**
 * JTetris presents a tetris game in a window.
 * It handles the GUI and the animation.
 * The Piece and Board classes handle the
 * lower-level computations.
 */
public class JTetris extends JComponent {
    private static final long serialVersionUID = 1L;
    // size of the board in blocks
    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;

    public static final int PIXELS = 16;

    // Extra blocks at the top for pieces to start.
    // If a piece is sticking up into this area
    // when it has landed -- game over!
    public static final int TOP_SPACE = 4;

    /**
     * Creates a Window,
     * installs the JTetris or JBrainTetris,
     * checks the testMode state,
     * install the controls in the WEST.
     */
    public static void createGUI(JTetris tetris) {
        JFrame frame = new JFrame("Tetris 2015");
        JComponent container = (JComponent)frame.getContentPane();
        container.setLayout(new BorderLayout());

        // Set the metal look and feel
        try {
            UIManager.
                setLookAndFeel(UIManager.
                    getCrossPlatformLookAndFeelClassName());
        }
        catch (Exception ignored) {}

        container.add(tetris, BorderLayout.CENTER);

        Container panel = tetris.createControlPanel();

        // Add the quit button last so it's at the bottom
        panel.add(Box.createVerticalStrut(12));
        JButton quit = new JButton("Quit");
        panel.add(quit);
        quit.addActionListener( new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

        container.add(panel, BorderLayout.EAST);
        frame.pack();
        frame.setVisible(true);

        // Quit on window close
        frame.addWindowListener( new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            System.exit(0);
          }
        });
    }

    public static void main(String[] args) {
        createGUI(new JTetris());
    }

    // Is drawing optimized
    protected boolean DRAW_OPTIMIZE = true;

    // These Strings represent the seven type of pieces that are used
    // to play Tetris.
    static protected String[] pieceStrings = { "0 0  1 0  2 0  3 0",
                                               "0 1  1 1  2 1  2 0",
                                               "0 0  0 1  1 1  2 1",
                                               "0 0  1 0  1 1  2 1",
                                               "0 1  1 1  1 0  2 0",
                                               "0 0  0 1  1 0  1 1",
                                               "0 1  1 0  1 1  2 1" };

    // Board data structure
    protected Board board;

    // State of the game
    protected boolean gameOn;    // true if we are playing
    protected int count;        // how many pieces played so far
    protected long startTime;    // used to measure elapsed time
    protected Random random;    // the random generator for new pieces

    // Controls
    protected JLabel countLabel;
    protected JLabel scoreLabel;
    protected JLabel timeLabel;
    protected JButton startButton;
    protected JButton stopButton;
    protected javax.swing.Timer timer;
    protected JSlider speed;

    public final int DELAY = 400;    // milliseconds per tick

    JTetris() {
        super();
        setPreferredSize(new Dimension(WIDTH*PIXELS+2,
                                       (HEIGHT+TOP_SPACE)*PIXELS+2));
        gameOn = false;

        board = new TetrisBoard(WIDTH, HEIGHT + TOP_SPACE);

        /**
         * Register key handlers that call
         * tick with the appropriate constant.
         */

        // LEFT
        registerKeyboardAction( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            tick(Board.Action.LEFT);
          }
        }, "left", KeyStroke.getKeyStroke('a'),
        WHEN_IN_FOCUSED_WINDOW
        );

        // RIGHT
        registerKeyboardAction( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            tick(Board.Action.DOWN);
          }
        }, "down", KeyStroke.getKeyStroke('s'),
        WHEN_IN_FOCUSED_WINDOW
        );

        // DOWN
        registerKeyboardAction( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            tick(Board.Action.RIGHT);
          }
        }, "right", KeyStroke.getKeyStroke('d'),
        WHEN_IN_FOCUSED_WINDOW
        );

        // ROTATE
        registerKeyboardAction( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            tick(Board.Action.COUNTERCLOCKWISE);
          }
        }, "counterclockwise", KeyStroke.getKeyStroke('q'),
        WHEN_IN_FOCUSED_WINDOW
        );

        // UNROTATE
        registerKeyboardAction( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            tick(Board.Action.CLOCKWISE);
          }
        }, "clockwise", KeyStroke.getKeyStroke('e'),
        WHEN_IN_FOCUSED_WINDOW
        );

        // DROP
        registerKeyboardAction( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            tick(Board.Action.DROP);
          }
        }, "drop", KeyStroke.getKeyStroke('w'),
        WHEN_IN_FOCUSED_WINDOW
        );

        // HOLD
        registerKeyboardAction( new ActionListener() {
            public void actionPerformed(ActionEvent e) { tick(Board.Action.HOLD); }
            }, "hold", KeyStroke.getKeyStroke('h'), WHEN_IN_FOCUSED_WINDOW
        );

        // Create the Timer object and have it send
        // tick(DOWN) periodically
        timer = new javax.swing.Timer(DELAY, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tick(Board.Action.DOWN);
                }
            });
    }

    /**
     * Sets the internal state and starts the timer
     * so the game is happening.
     */
    public void startGame() {
        // cheap way to reset the board state
        board = new TetrisBoard(WIDTH, HEIGHT + TOP_SPACE);

        // draw the new board state once
        repaint();

        count = 0;
        gameOn = true;

        random = new Random(); // diff seq each game

        enableButtons();
        timeLabel.setText(" ");
        addNewPiece();
        timer.start();
        startTime = System.currentTimeMillis();
    }

    /**
     * Sets the enabling of the start/stop buttons
     * based on the gameOn state.
     */
    private void enableButtons() {
        startButton.setEnabled(!gameOn);
        stopButton.setEnabled(gameOn);
    }

    /**
     * Stops the game.
     */
    public void stopGame() {
        gameOn = false;
        enableButtons();
        timer.stop();

        long delta = (System.currentTimeMillis() - startTime)/10;
        timeLabel.setText(Double.toString(delta/100.0) + " seconds");

    }

    /**
     * Selects the next piece to use using the random generator
     * set in startGame().
     */
    public Piece pickNextPiece() {
        int pieceNum = (int) (pieceStrings.length * random.nextDouble());
        Piece piece  = TetrisPiece.getPiece(pieceStrings[pieceNum]);
        return(piece);
    }

    /**
     * Adds a new random piece to the board.
     */
    public void addNewPiece() {
        count++;
        board.nextPiece(pickNextPiece());
        countLabel.setText(Integer.toString(count));
    }

    /**
     * Called to change the position of the current piece.
     * Each key press calls this once with a Board.Action
     * and the timer calls it with the verb DOWN to move
     * the piece down one square.
     */
    public void tick(Board.Action verb) {
        if (!gameOn) {
            return;
        }

        Board.Result result = board.move(verb);
        switch (result) {
          case SUCCESS:
          case OUT_BOUNDS:
            // The board is responsible for staying in a good state
            break;
          case PLACE:
            if (board.getMaxHeight() > HEIGHT) {
              stopGame();
            }
          case NO_PIECE:
            if (gameOn) {
              addNewPiece();
            }
            break;
        }

        repaint();
        scoreLabel.setText(Integer.toString(((TetrisBoard)board).getTotalRowsCleared()));
    }

    /**
     * Pixel helpers.
     * These centralize the translation of (x,y) coords
     * that refer to blocks in the board to (x,y) coords that
     * count pixels.
     *
     * The +1's and -2's are to account for the 1 pixel
     * rect around the perimeter.
     */

    // width in pixels of a block
    private final float dX() {
        return( ((float)(getWidth()-2)) / board.getWidth() );
    }

    // height in pixels of a block
    private final float dY() {
        return( ((float)(getHeight()-2)) / board.getHeight() );
    }

    // the x pixel coord of the left side of a block
    private final int xPixel(int x) {
        return(Math.round(1 + (x * dX())));
    }

    // the y pixel coord of the top of a block
    private final int yPixel(int y) {
        return(Math.round(getHeight() -1 - (y+1)*dY()));
    }

    /**
     * Draws the current board with a 1 pixel border
     * around the whole thing. Uses the pixel helpers
     * above to map board coords to pixel coords.
     * Draws rows that are filled all the way across in green.
     */
    public void paintComponent(Graphics g) {

        // Draw a rect around the whole thing
        g.drawRect(0, 0, getWidth()-1, getHeight()-1);

        // Draw the line separating the top
        int spacerY = yPixel(board.getHeight() - TOP_SPACE - 1);
        g.drawLine(0, spacerY, getWidth() - 1, spacerY);


        // check if we are drawing with clipping
        //Shape shape = g.getClip();
        Rectangle clip = null;
        if (DRAW_OPTIMIZE) {
            clip = g.getClipBounds();
        }

        // Factor a few things out to help the optimizer
        final int dx = Math.round(dX() - 2);
        final int dy = Math.round(dY() - 2);
        final int bWidth = board.getWidth();
        final int bHeight = board.getHeight();

        // Loop through and draw all the blocks
        // left-right, bottom-top
        for (int x = 0; x < bWidth; x++) {
            int left = xPixel(x);    // the left pixel

            // right pixel (useful for clip optimization)
            int right = xPixel(x + 1) -1;

            // skip this x if it is outside the clip rect
            if (DRAW_OPTIMIZE && clip != null) {
                if ((right < clip.x) || (left >= (clip.x + clip.width))) {
                    continue;
                }
            }

            // draw the HELD piece
            g.drawLine(getWidth()/3, 0, getWidth()/3, spacerY);
            g.setColor(Color.gray);
            Piece heldPiece = ((TetrisBoard)board).getHeldPiece();
            if (heldPiece != null) {
                for (Point point : heldPiece.getBody()) {
                    g.fillRect(xPixel((int) point.getX())*2/3 + (getWidth()/3-heldPiece.getWidth()*dx)/2 + 6, yPixel(HEIGHT + TOP_SPACE - (int) point.getY())*2/3 + (spacerY-heldPiece.getHeight()*dy)/2 + 15, dx*2/3, dy*2/3);
                }
            }
            g.setColor(Color.black);

            // draw from 0 up to the col height
            for (int y = 0; y < bHeight; y++) {
                if (board.getGrid(x, y)) {
                    final boolean filled = (board.getRowWidth(y) == bWidth);
                    if (filled) g.setColor(Color.green);
                    // +1 to leave a white border
                    g.fillRect(left + 1, yPixel(y) + 1, dx, dy);

                    if (filled) g.setColor(Color.BLACK);
                    //g.setColor(((TetrisPiece)((TetrisBoard)board).getPiece()).getColor());
                }
            }
        }
    }

    /**
     * Updates the timer to reflect the current setting of the
     * speed slider.
     */
    public void updateTimer() {
        double value = ((double)speed.getValue())/speed.getMaximum();
        timer.setDelay((int)(DELAY - value*DELAY));
    }

    /**
     * Creates the panel of UI controls.
     * This code is very repetitive -- the GUI/XML
     * extensions in Java 1.4 should make this sort
     * of ugly code less necessary.
     */
    public java.awt.Container createControlPanel() {
        java.awt.Container panel = Box.createVerticalBox();

        // COUNT
        countLabel = new JLabel("0");
        panel.add(countLabel);

        //SCORE (rows)
        scoreLabel = new JLabel("0");
        panel.add(scoreLabel);

        // TIME
        timeLabel = new JLabel(" ");
        panel.add(timeLabel);

        panel.add(Box.createVerticalStrut(12));

        // START button
        startButton = new JButton("Start");
        panel.add(startButton);
        startButton.addActionListener( new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    startGame();
                }
            });

        // STOP button
        stopButton = new JButton("Stop");
        panel.add(stopButton);
        stopButton.addActionListener( new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    stopGame();
                }
            });

        enableButtons();

        JPanel row = new JPanel();

        // SPEED slider
        panel.add(Box.createVerticalStrut(12));
        row.add(new JLabel("Speed:"));
        speed = new JSlider(0, 200, 75);    // min, max, current
        speed.setPreferredSize(new Dimension(100,15));

        updateTimer();
        row.add(speed);

        panel.add(row);
        speed.addChangeListener( new ChangeListener() {
                // when the slider changes, sync the timer to its value
                public void stateChanged(ChangeEvent e) {
                    updateTimer();
                }
            });

        return(panel);
    }

}
