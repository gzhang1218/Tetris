package assignment;

import java.awt.*;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 * Each piece is defined by the blocks that make up its body.
 *
 * You need to implement this.
 */
public final class TetrisPiece extends Piece {

    private int height, width;
    private Point[] body;
    private Point anchor;
    private int[] skirt, lSkirt, rSkirt;
    private char rotationState;
    private boolean isI;

    // A S S U M I N G that the JTetris pieceStrings will not change
    static final String I = "0 0  1 0  2 0  3 0";
    static final String J = "0 1  1 1  2 1  2 0";
    static final String L = "0 0  0 1  1 1  2 1";
    static final String RD = "0 0  1 0  1 1  2 1";
    static final String LD = "0 1  1 1  1 0  2 0";
    static final String S = "0 0  0 1  1 0  1 1";
    static final String T = "0 1  1 0  1 1  2 1";

    /**
     * Parse a "piece string" of the form "x1 y1 x2 y2 ..." into a TetrisPiece
     * where the corresponding (x1, y1), (x2, y2) positions have been filled in.
     *
     * ***IMPORTANT ASSUMPTION***
     * In order to choose an "anchor point" about which to rotate
     * we assumed that only the 7 pieceStrings specified in JTetris.java
     * would be passed in.
     *
     * Generate the rotations and link them
     */
    public static Piece getPiece(String pieceString) {
        // generate the given piece
        TetrisPiece piece = generatePiece(parsePoints(pieceString));
        piece.setRotationState('0');

        // generate the other 3 rotations
        // their coordinates will be both 0 (un-initialized)
        // but will be updated whenever nextRotation() is called

        // first rotation (90 degrees counterclockwise)
        Point[] oneRotation = new Point[piece.getBody().length];
        // find the top left corner of the previous - used to find next coordinates
        int x = 0, y = piece.getHeight();
        for (int i = 0; i < oneRotation.length; i ++ )
            oneRotation[i] = new Point(y - 1 - (int)(piece.getBody()[i].getY()), (int)piece.getBody()[i].getX() - x);

        TetrisPiece one = generatePiece(oneRotation);
        one.setRotationState('L');

        // second rotation (180 degrees counterclockwise)
        Point[] twoRotation = new Point[piece.getBody().length];
        // find the top left corner of the previous
        x = 0; y = one.getHeight();
        for (int i = 0; i < twoRotation.length; i ++ )
            twoRotation[i] = new Point(y - 1 - (int)(one.getBody()[i].getY()), (int)one.getBody()[i].getX() - x);

        TetrisPiece two = generatePiece(twoRotation);
        two.setRotationState('2');

        // third rotation (270 degrees counterclockwise)
        Point[] threeRotation = new Point[piece.getBody().length];
        // find the top left corner of the previous
        x = 0; y = two.getHeight();
        for (int i = 0; i < threeRotation.length; i ++ )
            threeRotation[i] = new Point(y - 1 - (int)(two.getBody()[i].getY()), (int)two.getBody()[i].getX() - x);

        TetrisPiece three = generatePiece(threeRotation);
        three.setRotationState('R');

        // link the pieces all together
        piece.setNext(one);
        one.setNext(two);
        two.setNext(three);
        three.setNext(piece);

        // find the anchor points for each piece...
        int anchorIndex = -1;
        boolean isSquare = false, isLine = false;
        // ASSUMES THAT THERE ARE ONLY 7 VALID INPUTS, READ ABOVE
        switch (pieceString) {
            case J:
                // reassign the rotationStates to align with the website for J, L, T
                piece.setRotationState('2');
                one.setRotationState('R');
                two.setRotationState('0');
                three.setRotationState('L');
            case RD:
                anchorIndex = 1; // the 2nd point is the anchor
                break;
            case L:
            case T:
                // reassign the rotationStates to align with the website for J, L, T
                piece.setRotationState('2');
                one.setRotationState('R');
                two.setRotationState('0');
                three.setRotationState('L');
            case LD:
                anchorIndex = 2; // the 3rd point is the anchor
                break;
            case S:
                isSquare = true;
                break;
            case I:
                isLine = true;
                break;
        }

        // manually assign anchor points for squares and lines, these are tricky
        if (isSquare) {
            piece.setAnchor(new Point(1,1));
            one.setAnchor(new Point(1,1));
            two.setAnchor(new Point(1,1));
            three.setAnchor(new Point(1,1));
        }
        else if (isLine) {
            piece.setAnchor(new Point(2,0));
            one.setAnchor(new Point(1,2));
            two.setAnchor(new Point(2,1));
            three.setAnchor(new Point(0,2));

            // mark these as "I" pieces, for later - wall kick data
            piece.setI(true);
            one.setI(true);
            two.setI(true);
            three.setI(true);
        }
        else {
            piece.setAnchor(piece.getBody()[anchorIndex]);
            one.setAnchor(one.getBody()[anchorIndex]);
            two.setAnchor(two.getBody()[anchorIndex]);
            three.setAnchor(three.getBody()[anchorIndex]);
        }

        return piece;
    }

    /**
     * A helper method to create and return
     * a Piece that is 90 degrees rotated
     * counter-clockwise from the input
     *
     * Calculate store body, height, width, calculate skirts
     *
     * @param points the points that comprise the body
     * @return a TetrisPiece with instance variables all initialized
     */
    private static TetrisPiece generatePiece(Point[] points) {

        TetrisPiece piece = new TetrisPiece();

        piece.setBody(points);

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

    @Override
    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

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
     * Compares the Point[] bodies to determine equality
     *
     * We cannot assume the Point[]
     * is in the same order
     *
     * @param other
     * @return
     */ //TODO testing
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TetrisPiece))
            return false;
        Piece o = (TetrisPiece) other;
        for (int i = 0 ; i < this.getBody().length; i++) {
            Point p = getBody()[i];
            boolean found = false;
            for (int j = 0; j < o.getBody().length; j++) {
                if (p.equals(o.getBody()[j]))
                    found = true;
            }
            if (!found)
                return false; // there is a coordinate in this that is not in other
        }
        return true;
    }

    private void setBody(Point[] body) {
        this.body = body;
    }

    private void setSkirt(int[] skirt) { this.skirt = skirt; }

    private void setLSkirt(int[] lSkirt) { this.lSkirt = lSkirt; }

    private void setRSkirt(int[] rSkirt) { this.rSkirt = rSkirt; }

    private void setNext(Piece next) { this.next = next; }

    public char getRotationState() {
        return rotationState;
    }

    private void setRotationState(char rotationState) {
        this.rotationState = rotationState;
    }

    public Point getAnchor() {
        return anchor;
    }

    private void setAnchor(Point anchor) {
        this.anchor = anchor;
    }

    public boolean isI() {
        return isI;
    }

    private void setI(boolean i) {
        isI = i;
    }

}
