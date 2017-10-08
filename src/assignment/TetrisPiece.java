package assignment;

import java.awt.*;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 * Each piece is defined by the blocks that make up its body.
 *
 * You need to implement this.
 */
public final class TetrisPiece extends Piece {

    private int x, y; // coordinate of bottom left
    private int height, width;
    private Point[] body;
    private int[] skirt, lSkirt, rSkirt;

    /**
     * Parse a "piece string" of the form "x1 y1 x2 y2 ..." into a TetrisPiece
     * where the corresponding (x1, y1), (x2, y2) positions have been filled in.
     *
     * Calculate height, width, store body, calculate skirt
     * Generate the rotations as well and link them
     */
    public static Piece getPiece(String pieceString) {
        TetrisPiece piece = new TetrisPiece();

        // TODO : handle rotations, maybe process the pieces differently?

        // place the piece at the top in the middle
        piece.setX(JTetris.WIDTH / 2);
        piece.setY(JTetris.HEIGHT);

        piece.setBody(parsePoints(pieceString));

        int maxRow = 0, maxCol = 0;
        // go through the Point array, find max row and col ==> height and width
        for (Point point : piece.getBody()) {
            if (maxCol < point.getX())
                maxCol = (int)point.getX();
            if (maxRow < point.getY())
                maxRow = (int)point.getY();
        }

        piece.setWidth(maxCol + 1);
        piece.setHeight(maxRow + 1);

        // getting skirt
        int[] temp = new int[piece.getWidth()];
        for (int i = 0 ; i < temp.length ; i++) temp[i] = 10;
        for (Point point : piece.getBody())
            if (point.getY() < temp[(int)point.getX()])
                temp[(int)point.getX()] = (int)point.getY();

        piece.setSkirt(temp);

        // getting left skirt
        temp = new int[piece.getHeight()];
        for (int i = 0 ; i < temp.length ; i++) temp[i] = 10;
        for (Point point : piece.getBody())
            if (point.getX() < temp[(int)point.getY()])
                temp[(int)point.getY()] = (int)point.getX();

        piece.setLSkirt(temp);

        // getting right skirt - *trickier*
        temp = new int[piece.getHeight()];
        for (int i = 0 ; i < temp.length ; i++) temp[i] = -1;
        for (Point point : piece.getBody())
            if (point.getX() > temp[(int)point.getY()])
                temp[(int)point.getY()] = (int)point.getX();
        for (int i = 0 ; i < temp.length; i++)
            temp[i] = piece.getWidth() - 1 - temp[i];

        piece.setRSkirt(temp);

        return piece;
    }

    /**
     * A helper method to create and return
     * a Piece that is 90 degrees rotated
     * counter-clockwise from the input
     *
     * @param piece
     * @return
     */
    private static TetrisPiece getRotation(TetrisPiece piece) {
        return null;
    }

    /**
     *
     * @return
     */
    @Override
    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    /**
     *
     * @return
     */
    @Override
    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     *
     * @return
     */
    @Override
    public Point[] getBody() {
        return this.body;
    }

    @Override
    public int[] getSkirt() {
        return skirt;
    }

    public int[] getLSkirt() { return lSkirt; }

    public int[] getRSkirt() { return rSkirt; }

    /**
     * Compares the Point[] bodies
     *
     * Assumes that the coordinates
     * are stored in the same order
     * (which is sometime we can control)
     *
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TetrisPiece))
            return false;
        Piece o = (TetrisPiece) other;
        for (int i = 0 ; i < this.getBody().length; i++)
            if (!this.getBody()[i].equals(o.getBody()[i]))
                return false;
        return true;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setBody(Point[] body) {
        this.body = body;
    }

    public void setSkirt(int[] skirt) { this.skirt = skirt; }

    public void setLSkirt(int[] lSkirt) { this.lSkirt = lSkirt; }

    public void setRSkirt(int[] rSkirt) { this.rSkirt = rSkirt; }

}
