package assignment;

import java.awt.*;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 * Each piece is defined by the blocks that make up its body.
 *
 * You need to implement this.
 */
public final class TetrisPiece extends Piece {

    /**
     * Parse a "piece string" of the form "x1 y1 x2 y2 ..." into a TetrisPiece
     * where the corresponding (x1, y1), (x2, y2) positions have been filled in.
     */
    public static Piece getPiece(String pieceString) { return null; }

    @Override
    public int getWidth() { return -1; }

    @Override
    public int getHeight() { return -1; }

    @Override
    public Point[] getBody() { return null; }

    @Override
    public int[] getSkirt() { return null; }

    @Override
    public boolean equals(Object other) { return false; }
}
