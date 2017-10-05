package assignment;

import java.awt.*;
import java.util.*;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 * Each piece is defined by the blocks that make up its body.
 *
 * This is the basis for a piece in Tetris.
 */
public abstract class Piece {

    /**
     * The next rotation of this piece. See {@link #nextRotation}.
     */
    protected Piece next;

    /**
     * Returns a piece that is 90 degrees counter-clockwise
     * rotated from the receiver.
     */
    public Piece nextRotation() {
        return next;
    }

    /**
     * Returns the width of the piece measured in blocks.
     */
    public abstract int getWidth();


    /**
     * Returns the height of the piece measured in blocks.
     */
    public abstract int getHeight();


    /**
     * Returns the points in the piece's body.
     */
    public abstract Point[] getBody();


    /**
     * Returns a pointer to the piece's skirt. For each x value across the
     * piece, the skirt gives the lowest y value in the body.
     */
    public abstract int[] getSkirt();


    /**
     * Returns true if two pieces are the same --
     * their bodies contain the same points.
     */
    public abstract boolean equals(Object other);


    /**
     * Given a string of x,y pairs., i.e. ("0 0  0 1  0 2  1 0"), parses
     * the points into a Point[] array.
     */
    protected static Point[] parsePoints(String string) {
        ArrayList<Point> points = new ArrayList<>();
        StringTokenizer tok = new StringTokenizer(string);
        try {
            while(tok.hasMoreTokens()) {
                int x = Integer.parseInt(tok.nextToken());
                int y = Integer.parseInt(tok.nextToken());

                points.add(new Point(x, y));
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("Could not parse x,y string:" + string);
        }

        Point[] array = new Point[0];
        array = points.toArray(array);
        return array;
    }
}
