package assignment;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TetrisPieceTest {

    public static final String stickPiece = "0 0  1 0  2 0  3 0";
    public static final String jPiece = "0 1  1 1  2 1  2 0";
    public static final String lPiece = "0 0  0 1  1 1  2 1";
    public static final String rdPiece = "0 0  1 0  1 1  2 1";
    public static final String ldPiece = "0 1  1 1  1 0  2 0";
    public static final String squarePiece = "0 0  0 1  1 0  1 1";
    public static final String tPiece = "0 1  1 0  1 1  2 1";

    @Test
    public void getPiece() throws Exception {
        // testing that the pieces are actually circularly linked
        Piece piece = TetrisPiece.getPiece(stickPiece);
        Piece piece2 = piece.nextRotation().nextRotation().nextRotation().nextRotation();
        assertTrue(piece == piece2);
        piece = piece.nextRotation();
        piece2 = piece2.nextRotation();
        assertTrue(piece == piece2);

        piece = TetrisPiece.getPiece(rdPiece);
        piece2 = piece.nextRotation().nextRotation();
        assertFalse(piece == piece2); //same orientation, but not same object
        piece2 = piece2.nextRotation().nextRotation();
        assertTrue(piece == piece2);

    }

    @Test
    public void testEquals() throws Exception {
        //check obviously different pieces
        Piece piece = TetrisPiece.getPiece(stickPiece);
        Piece piece2 = TetrisPiece.getPiece(jPiece);
        assertFalse(piece.equals(piece2));

        //check the same piece, but different orientations
        piece = TetrisPiece.getPiece(stickPiece);
        piece2 = piece.nextRotation();
        assertFalse(piece.equals(piece2));
        piece = TetrisPiece.getPiece(tPiece);
        piece2 = piece.nextRotation();
        assertFalse(piece.equals(piece2));
        piece = TetrisPiece.getPiece(jPiece);
        piece2 = piece.nextRotation();
        assertFalse(piece.equals(piece2));
        piece = TetrisPiece.getPiece(lPiece);
        piece2 = piece.nextRotation();
        assertFalse(piece.equals(piece2));


        //check the same piece, different rotation state but same orientation
        piece = TetrisPiece.getPiece(stickPiece);
        piece2 = piece.nextRotation().nextRotation();
        assertTrue(piece.equals(piece2));
        piece = TetrisPiece.getPiece(rdPiece);
        piece2 = piece.nextRotation().nextRotation();
        assertTrue(piece.equals(piece2));
        piece = TetrisPiece.getPiece(ldPiece);
        piece2 = piece.nextRotation().nextRotation();
        assertTrue(piece.equals(piece2));

        //check square, all 4 rotations
        piece = TetrisPiece.getPiece(squarePiece);
        piece2 = piece.nextRotation();
        assertTrue(piece.equals(piece2));
        piece2 = piece2.nextRotation();
        assertTrue(piece.equals(piece2));
        piece2 = piece2.nextRotation();
        assertTrue(piece.equals(piece2));

        //check every piece with itself
        piece = TetrisPiece.getPiece(stickPiece);
        piece2 = TetrisPiece.getPiece(stickPiece);
        assertTrue(piece.equals(piece2));
        assertFalse(piece == piece2);
        piece = TetrisPiece.getPiece(jPiece);
        piece2 = TetrisPiece.getPiece(jPiece);
        assertTrue(piece.equals(piece2));
        assertFalse(piece == piece2);
        piece = TetrisPiece.getPiece(lPiece);
        piece2 = TetrisPiece.getPiece(lPiece);
        assertTrue(piece.equals(piece2));
        assertFalse(piece == piece2);
        piece = TetrisPiece.getPiece(ldPiece);
        piece2 = TetrisPiece.getPiece(ldPiece);
        assertTrue(piece.equals(piece2));
        assertFalse(piece == piece2);
        piece = TetrisPiece.getPiece(rdPiece);
        piece2 = TetrisPiece.getPiece(rdPiece);
        assertTrue(piece.equals(piece2));
        assertFalse(piece == piece2);
        piece = TetrisPiece.getPiece(squarePiece);
        piece2 = TetrisPiece.getPiece(squarePiece);
        assertTrue(piece.equals(piece2));
        assertFalse(piece == piece2);
        piece = TetrisPiece.getPiece(tPiece);
        piece2 = TetrisPiece.getPiece(tPiece);
        assertTrue(piece.equals(piece2));
        assertFalse(piece == piece2);

    }

    @Test
    public void testGetSkirt() throws Exception {
        //just test the skirts for the 7 canonical pieces
        Piece piece = TetrisPiece.getPiece(stickPiece);
        assertTrue(Arrays.equals(new int[]{0, 0, 0, 0}, piece.getSkirt()));


        piece = TetrisPiece.getPiece(jPiece);
        assertTrue(Arrays.equals(new int[]{1, 1, 0}, piece.getSkirt()));

        piece = TetrisPiece.getPiece(lPiece);
        assertTrue(Arrays.equals(new int[]{0, 1, 1}, piece.getSkirt()));

        piece = TetrisPiece.getPiece(rdPiece);
        assertTrue(Arrays.equals(new int[]{0, 0, 1}, piece.getSkirt()));

        piece = TetrisPiece.getPiece(ldPiece);
        assertTrue(Arrays.equals(new int[]{1, 0, 0}, piece.getSkirt()));

        piece = TetrisPiece.getPiece(squarePiece);
        assertTrue(Arrays.equals(new int[]{0, 0}, piece.getSkirt()));

        piece = TetrisPiece.getPiece(tPiece);
        assertTrue(Arrays.equals(new int[]{1, 0, 1}, piece.getSkirt()));
    }
}