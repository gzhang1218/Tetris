package assignment;

import java.awt.*;
import java.util.Arrays;

/**
 * Represents a Tetris board -- essentially a 2-d grid of booleans. Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2-d board.
 */
public final class TetrisBoard implements Board {


    // (0, 0) is the bottom left corner
    private boolean[][] board;

    private TetrisPiece piece;
    private Piece tempPiece; //TODO - done
    private int x, y; // coordinate of the current piece //TODO - done
    private int tempX, tempY; // temporary x and y values for testing //TODO - done

    private Action lastAction;
    private Result lastResult;
    private int rowsCleared; // initialized at 0

    private int maxHeight; // initialized at 0
    private int[] colHeights;

    // these hold the wall kick data
    // access the offsets by ARRAY_NAME[test# - 0based][character of current rotationState][0 or 1 for x or y]
    static final int[][][] CCW_WALL_KICK_DATA;
    static final int[][][] CW_WALL_KICK_DATA;
    static final int[][][] CCW_I_WALL_KICK_DATA;
    static final int[][][] CW_I_WALL_KICK_DATA;

    // adding SRS wallkick data
    static {
        CCW_WALL_KICK_DATA = new int[4]['R' + 1][2];
        CW_WALL_KICK_DATA = new int[4]['R' + 1][2];
        CCW_I_WALL_KICK_DATA = new int[4]['R' + 1][2];
        CW_I_WALL_KICK_DATA = new int[4]['R' + 1][2];

        // counterclockwise - normal
        CCW_WALL_KICK_DATA[0]['0'][0] = 1;
        CCW_WALL_KICK_DATA[0]['0'][1] = 0;
        CCW_WALL_KICK_DATA[0]['L'][0] = -1;
        CCW_WALL_KICK_DATA[0]['L'][1] = 0;
        CCW_WALL_KICK_DATA[0]['2'][0] = -1;
        CCW_WALL_KICK_DATA[0]['2'][1] = 0;
        CCW_WALL_KICK_DATA[0]['R'][0] = 1;
        CCW_WALL_KICK_DATA[0]['R'][1] = 0;

        CCW_WALL_KICK_DATA[1]['0'][0] = 1;
        CCW_WALL_KICK_DATA[1]['0'][1] = 1;
        CCW_WALL_KICK_DATA[1]['L'][0] = -1;
        CCW_WALL_KICK_DATA[1]['L'][1] = -1;
        CCW_WALL_KICK_DATA[1]['2'][0] = -1;
        CCW_WALL_KICK_DATA[1]['2'][1] = 1;
        CCW_WALL_KICK_DATA[1]['R'][0] = 1;
        CCW_WALL_KICK_DATA[1]['R'][1] = -1;

        CCW_WALL_KICK_DATA[2]['0'][0] = 0;
        CCW_WALL_KICK_DATA[2]['0'][1] = -2;
        CCW_WALL_KICK_DATA[2]['L'][0] = 0;
        CCW_WALL_KICK_DATA[2]['L'][1] = 2;
        CCW_WALL_KICK_DATA[2]['2'][0] = 0;
        CCW_WALL_KICK_DATA[2]['2'][1] = -2;
        CCW_WALL_KICK_DATA[2]['R'][0] = 0;
        CCW_WALL_KICK_DATA[2]['R'][1] = 2;

        CCW_WALL_KICK_DATA[3]['0'][0] = 1;
        CCW_WALL_KICK_DATA[3]['0'][1] = -2;
        CCW_WALL_KICK_DATA[3]['L'][0] = -1;
        CCW_WALL_KICK_DATA[3]['L'][1] = 2;
        CCW_WALL_KICK_DATA[3]['2'][0] = -1;
        CCW_WALL_KICK_DATA[3]['2'][1] = -2;
        CCW_WALL_KICK_DATA[3]['R'][0] = 1;
        CCW_WALL_KICK_DATA[3]['R'][1] = 2;

        // clockwise - normal
        CW_WALL_KICK_DATA[0]['0'][0] = -1;
        CW_WALL_KICK_DATA[0]['0'][1] = 0;
        CW_WALL_KICK_DATA[0]['L'][0] = -1;
        CW_WALL_KICK_DATA[0]['L'][1] = 0;
        CW_WALL_KICK_DATA[0]['2'][0] = 1;
        CW_WALL_KICK_DATA[0]['2'][1] = 0;
        CW_WALL_KICK_DATA[0]['R'][0] = 1;
        CW_WALL_KICK_DATA[0]['R'][1] = 0;

        CW_WALL_KICK_DATA[1]['0'][0] = -1;
        CW_WALL_KICK_DATA[1]['0'][1] = 1;
        CW_WALL_KICK_DATA[1]['L'][0] = -1;
        CW_WALL_KICK_DATA[1]['L'][1] = -1;
        CW_WALL_KICK_DATA[1]['2'][0] = 1;
        CW_WALL_KICK_DATA[1]['2'][1] = 1;
        CW_WALL_KICK_DATA[1]['R'][0] = 1;
        CW_WALL_KICK_DATA[1]['R'][1] = -1;

        CW_WALL_KICK_DATA[2]['0'][0] = 0;
        CW_WALL_KICK_DATA[2]['0'][1] = -2;
        CW_WALL_KICK_DATA[2]['L'][0] = 0;
        CW_WALL_KICK_DATA[2]['L'][1] = 2;
        CW_WALL_KICK_DATA[2]['2'][0] = 0;
        CW_WALL_KICK_DATA[2]['2'][1] = -2;
        CW_WALL_KICK_DATA[2]['R'][0] = 0;
        CW_WALL_KICK_DATA[2]['R'][1] = 2;

        CW_WALL_KICK_DATA[3]['0'][0] = -1;
        CW_WALL_KICK_DATA[3]['0'][1] = -2;
        CW_WALL_KICK_DATA[3]['L'][0] = -1;
        CW_WALL_KICK_DATA[3]['L'][1] = 2;
        CW_WALL_KICK_DATA[3]['2'][0] = 1;
        CW_WALL_KICK_DATA[3]['2'][1] = -2;
        CW_WALL_KICK_DATA[3]['R'][0] = 1;
        CW_WALL_KICK_DATA[3]['R'][1] = 2;

        // counterclockwise - I piece
        CCW_I_WALL_KICK_DATA[0]['0'][0] = -1;
        CCW_I_WALL_KICK_DATA[0]['0'][1] = 0;
        CCW_I_WALL_KICK_DATA[0]['L'][0] = -2;
        CCW_I_WALL_KICK_DATA[0]['L'][1] = 0;
        CCW_I_WALL_KICK_DATA[0]['2'][0] = 1;
        CCW_I_WALL_KICK_DATA[0]['2'][1] = 0;
        CCW_I_WALL_KICK_DATA[0]['R'][0] = 2;
        CCW_I_WALL_KICK_DATA[0]['R'][1] = 0;

        CCW_I_WALL_KICK_DATA[1]['0'][0] = 2;
        CCW_I_WALL_KICK_DATA[1]['0'][1] = 0;
        CCW_I_WALL_KICK_DATA[1]['L'][0] = 1;
        CCW_I_WALL_KICK_DATA[1]['L'][1] = 0;
        CCW_I_WALL_KICK_DATA[1]['2'][0] = -2;
        CCW_I_WALL_KICK_DATA[1]['2'][1] = 0;
        CCW_I_WALL_KICK_DATA[1]['R'][0] = -1;
        CCW_I_WALL_KICK_DATA[1]['R'][1] = 0;

        CCW_I_WALL_KICK_DATA[2]['0'][0] = -1;
        CCW_I_WALL_KICK_DATA[2]['0'][1] = 2;
        CCW_I_WALL_KICK_DATA[2]['L'][0] = -2;
        CCW_I_WALL_KICK_DATA[2]['L'][1] = -1;
        CCW_I_WALL_KICK_DATA[2]['2'][0] = 1;
        CCW_I_WALL_KICK_DATA[2]['2'][1] = -2;
        CCW_I_WALL_KICK_DATA[2]['R'][0] = 2;
        CCW_I_WALL_KICK_DATA[2]['R'][1] = 1;

        CCW_I_WALL_KICK_DATA[3]['0'][0] = 2;
        CCW_I_WALL_KICK_DATA[3]['0'][1] = -1;
        CCW_I_WALL_KICK_DATA[3]['L'][0] = 1;
        CCW_I_WALL_KICK_DATA[3]['L'][1] = 2;
        CCW_I_WALL_KICK_DATA[3]['2'][0] = -2;
        CCW_I_WALL_KICK_DATA[3]['2'][1] = 1;
        CCW_I_WALL_KICK_DATA[3]['R'][0] = -1;
        CCW_I_WALL_KICK_DATA[3]['R'][1] = -2;

        // clockwise - I piece
        CW_I_WALL_KICK_DATA[0]['0'][0] = -2;
        CW_I_WALL_KICK_DATA[0]['0'][1] = 0;
        CW_I_WALL_KICK_DATA[0]['L'][0] = 1;
        CW_I_WALL_KICK_DATA[0]['L'][1] = 0;
        CW_I_WALL_KICK_DATA[0]['2'][0] = 2;
        CW_I_WALL_KICK_DATA[0]['2'][1] = 0;
        CW_I_WALL_KICK_DATA[0]['R'][0] = -1;
        CW_I_WALL_KICK_DATA[0]['R'][1] = 0;

        CW_I_WALL_KICK_DATA[1]['0'][0] = 1;
        CW_I_WALL_KICK_DATA[1]['0'][1] = 0;
        CW_I_WALL_KICK_DATA[1]['L'][0] = -2;
        CW_I_WALL_KICK_DATA[1]['L'][1] = 0;
        CW_I_WALL_KICK_DATA[1]['2'][0] = -1;
        CW_I_WALL_KICK_DATA[1]['2'][1] = 0;
        CW_I_WALL_KICK_DATA[1]['R'][0] = 2;
        CW_I_WALL_KICK_DATA[1]['R'][1] = 0;

        CW_I_WALL_KICK_DATA[2]['0'][0] = -2;
        CW_I_WALL_KICK_DATA[2]['0'][1] = -1;
        CW_I_WALL_KICK_DATA[2]['L'][0] = 1;
        CW_I_WALL_KICK_DATA[2]['L'][1] = -2;
        CW_I_WALL_KICK_DATA[2]['2'][0] = 2;
        CW_I_WALL_KICK_DATA[2]['2'][1] = 1;
        CW_I_WALL_KICK_DATA[2]['R'][0] = -1;
        CW_I_WALL_KICK_DATA[2]['R'][1] = 2;

        CW_I_WALL_KICK_DATA[3]['0'][0] = 1;
        CW_I_WALL_KICK_DATA[3]['0'][1] = 2;
        CW_I_WALL_KICK_DATA[3]['L'][0] = -2;
        CW_I_WALL_KICK_DATA[3]['L'][1] = 1;
        CW_I_WALL_KICK_DATA[3]['2'][0] = -1;
        CW_I_WALL_KICK_DATA[3]['2'][1] = -2;
        CW_I_WALL_KICK_DATA[3]['R'][0] = 2;
        CW_I_WALL_KICK_DATA[3]['R'][1] = -1;
    }



    // JTetris will use this constructor
    public TetrisBoard(int width, int height) {
        board = new boolean[height][width];
        colHeights = new int[width];
    }

    /**
     * This method handles each action and returns the following result
     * It will update lastAction, lastResult, and rowsCleared before it returns
     */
    @Override
    public Result move(Action act) {
        lastAction = act;

        rowsCleared = 0;

        if (piece == null) {
            lastResult = Result.NO_PIECE;
            return Result.NO_PIECE;
        }

        switch (act) {
            case LEFT:
                // check if the piece is on the left border
                if (x == 0) { //TODO - done
                    lastResult = Result.OUT_BOUNDS;
                    return Result.OUT_BOUNDS;
                }
                // check if anything in the way to the left
                for (int offset = 0; offset < piece.getHeight(); offset ++) {
                    if (board[y + offset][x - 1 + piece.getLSkirt()[offset]]) { //TODO - done
                        lastResult = Result.OUT_BOUNDS;
                        return Result.OUT_BOUNDS;
                    }
                }
                // otherwise, shift to left
                clearCurrentPos();
                x--; //TODO - done
                placePos();
                lastResult = Result.SUCCESS;
                return Result.SUCCESS;
            case RIGHT:
                // check if the piece is on the right border
                if (x + piece.getWidth() == JTetris.WIDTH) { //TODO - done
                    lastResult = Result.OUT_BOUNDS;
                    return Result.OUT_BOUNDS;
                }
                // check if anything in the way to the right
                for (int offset = 0; offset < piece.getHeight(); offset ++) {
                    if (board[y + offset][x + piece.getWidth() - piece.getRSkirt()[offset]]) { //TODO - done
                        lastResult = Result.OUT_BOUNDS;
                        return Result.OUT_BOUNDS;
                    }
                }
                // otherwise, shift to the right
                clearCurrentPos();
                x++; //TODO - done
                placePos();
                lastResult = Result.SUCCESS;
                return Result.SUCCESS;
            case DOWN:
                // check if the piece has reached the ground
                if (y == 0) { //TODO - done
                    updateMaxHeight();
                    updateColHeights();

                    clearRows();

                    piece = null; // after a PLACE, piece should be null

                    lastResult = Result.PLACE;
                    return Result.PLACE;
                }
                // check if the piece has anything under it
                for (int offset = 0; offset < piece.getWidth(); offset ++) {
                    if (board[y - 1 + piece.getSkirt()[offset]][x + offset]) { //TODO - done
                        updateMaxHeight();
                        updateColHeights();

                        clearRows();

                        piece = null; // after a PLACE, piece should be null

                        lastResult = Result.PLACE;
                        return Result.PLACE;
                    }
                }
                // otherwise, slide it down by one
                clearCurrentPos();
                y--; //TODO - done
                placePos();
                lastResult = Result.SUCCESS;
                return Result.SUCCESS;
            case DROP:
                // should always be able to drop, assuming the current piece is in a valid position
                clearCurrentPos();
                y = dropHeight(piece, x); //TODO - done
                placePos();

                lastResult = Result.SUCCESS;
                return Result.SUCCESS;
            case CLOCKWISE:

                clearCurrentPos();

                // check if simple rotate is valid
                testCW(); //TODO - done
                boolean simpleRotateWorks = isValid(tempPiece);

                if (simpleRotateWorks) {
                    rotateCW(); //TODO - done
                    lastResult = Result.SUCCESS;
                } else { // need to check wall kicks..... :(
                    // FOUR TESTS TO CHECK, DEPENDS ON IDENTITY OF TETRONIMO, WHICH ORIENTATION IT'S IN AND GOING TO
                    for (int test = 0; test < 4; test++ ) {
                        int xOffset, yOffset;

                        // retrieve the correct data
                        if(piece.isI()) {
                            xOffset = CW_I_WALL_KICK_DATA[test][piece.getRotationState()][0];
                            yOffset = CW_I_WALL_KICK_DATA[test][piece.getRotationState()][1];
                        } else {
                            xOffset = CW_WALL_KICK_DATA[test][piece.getRotationState()][0];
                            yOffset = CW_WALL_KICK_DATA[test][piece.getRotationState()][1];
                        }

                        testCW(xOffset, yOffset); //TODO - done

                        // if we find a valid wall kick, take it, modify piece accordingly, and return SUCCESS
                        if (isValid(tempPiece)) {
                            rotateCW(xOffset, yOffset); // TODO - done
                            lastResult = Result.SUCCESS;
                            placePos();
                            return Result.SUCCESS;
                        }

                    }
                    // if we don't find a valid kick, then don't change 'piece' and have OUTOFBOUNDS return
                    lastResult = Result.OUT_BOUNDS;
                }

                // at this point, piece should either be reassigned or left untouched
                // last result should be updated (SUCCESS, OUTOFBOUNDS)
                placePos();
                return lastResult;

            case COUNTERCLOCKWISE:

                clearCurrentPos();

                // check if simple rotate is valid
                testCCW(); //TODO - done
                simpleRotateWorks = isValid(tempPiece);

                if (simpleRotateWorks) {
                    rotateCCW(); //TODO - done
                    lastResult = Result.SUCCESS;
                } else { // need to check wall kicks..... :(
                    // FOUR TESTS TO CHECK, DEPENDS ON IDENTITY OF TETRONIMO, WHICH ORIENTATION IT'S IN AND GOING TO
                    for (int test = 0; test < 4; test++ ) {
                        int xOffset, yOffset;

                        // retrieve correct wall kick data
                        if(piece.isI()) {
                            xOffset = CCW_I_WALL_KICK_DATA[test][piece.getRotationState()][0];
                            yOffset = CCW_I_WALL_KICK_DATA[test][piece.getRotationState()][1];
                        } else {
                            xOffset = CCW_WALL_KICK_DATA[test][piece.getRotationState()][0];
                            yOffset = CCW_WALL_KICK_DATA[test][piece.getRotationState()][1];
                        }

                        testCCW(xOffset, yOffset); //TODO - done

                        // if we find a valid wall kick, take it, modify piece accordingly, and return SUCCESS
                        if (isValid(tempPiece)) {
                            rotateCCW(xOffset, yOffset); //TODO - done
                            lastResult = Result.SUCCESS;
                            placePos();
                            return Result.SUCCESS;
                        }
                    }
                    // if we don't find a valid kick, then don't change 'piece' and have OUTOFBOUNDS return
                    lastResult = Result.OUT_BOUNDS;
                }

                // at this point, piece should either be reassigned or left untouched
                // last result should be updated (SUCCESS, OUTOFBOUNDS)
                placePos();
                return lastResult;

            case NOTHING:
                lastResult = Result.SUCCESS;
                return Result.SUCCESS;
            case HOLD:
                // TODO implement for karma
                lastResult = Result.SUCCESS;
                return Result.SUCCESS;
        }
        return Result.NO_PIECE;
    }

    /**
     * This method sets the coordinates occupied by the current piece to false
     */
    private void clearCurrentPos() {
        for (Point offset : piece.getBody())
            board[y + (int)offset.getY()][x + (int)offset.getX()] = false; //TODO - done
    }

    /**
     * This method sets the coordinates occupied by the current piece to true
     */
    private void placePos() {
        for (Point offset : piece.getBody())
            board[y + (int)offset.getY()][x + (int)offset.getX()] = true; //TODO - done
    }

    /**
     * This method updates maxHeight whenever a piece is placed
     * Compares the maxHeight of the piece with the current maxHeight
     */
    private void updateMaxHeight() {
        if (y + piece.getHeight() > maxHeight) //TODO - done
            maxHeight = y + piece.getHeight();
    }

    /**
     * This method updates the colHeights for each column
     * The colHeight for a given column is simply the
     * index (1 - based) of the higheset occupied block in that column
     */
    private void updateColHeights() {
        for (Point offset : piece.getBody()) {
            int col = x + (int)offset.getX(); //TODO - done
            int height = y + (int)offset.getY() + 1; //TODO - done
            if (colHeights[col] < height)
                colHeights[col] = height;
        }
    }

    /**
     * This method changes tempX, tempY according to a CW rotation
     * Also assigns the CW rotated piece to tempPiece
     *
     * Does not modify x, y, or piece
     */
    private void testCW(int xOffset, int yOffset) { // TODO - done
        tempPiece = piece.nextRotation().nextRotation().nextRotation();
        int anchorAdjustX, anchorAdjustY;
        anchorAdjustX = (int)(piece.getAnchor().getX() - ((TetrisPiece)tempPiece).getAnchor().getX());
        anchorAdjustY = (int)(piece.getAnchor().getY() - ((TetrisPiece)tempPiece).getAnchor().getY());

        tempX = x + xOffset + anchorAdjustX;
        tempY = y + yOffset + anchorAdjustY;
    }

    private void testCW() {
        testCW(0, 0);
    }

    /**
     * This method changes tempX, tempY according to a CCW rotation
     * Also assigns the CCW rotated piece to tempPiece
     *
     * Does not modify x, y, or piece
     */
    private void testCCW(int xOffset, int yOffset) { // TODO - done
        tempPiece = piece.nextRotation();
        int anchorAdjustX, anchorAdjustY;
        anchorAdjustX = (int)(piece.getAnchor().getX() - ((TetrisPiece)tempPiece).getAnchor().getX());
        anchorAdjustY = (int)(piece.getAnchor().getY() - ((TetrisPiece)tempPiece).getAnchor().getY());

        tempX = x + xOffset + anchorAdjustX;
        tempY = y + yOffset + anchorAdjustY;
    }

    private void testCCW() {
        testCCW(0 ,0);
    }

    /**
     * This method updates x and y according to a CW rotation
     * Also assigns the CW rotated piece to piece
     */
    private void rotateCW(int xOffset, int yOffset) { // TODO - done
        tempPiece = piece.nextRotation().nextRotation().nextRotation();
        int anchorAdjustX, anchorAdjustY;
        anchorAdjustX = (int)(piece.getAnchor().getX() - ((TetrisPiece)tempPiece).getAnchor().getX());
        anchorAdjustY = (int)(piece.getAnchor().getY() - ((TetrisPiece)tempPiece).getAnchor().getY());

        x += xOffset + anchorAdjustX;
        y += yOffset + anchorAdjustY;
        piece = (TetrisPiece)tempPiece;
    }

    private void rotateCW() {
        rotateCW(0, 0);
    }

    /**
     * This method updates x and y according to a CCW rotation
     * Also assigns the CCW rotated piece to piece
     */
    private void rotateCCW(int xOffset, int yOffset) { // TODO - done
        tempPiece = piece.nextRotation();
        int anchorAdjustX, anchorAdjustY;
        anchorAdjustX = (int)(piece.getAnchor().getX() - ((TetrisPiece)tempPiece).getAnchor().getX());
        anchorAdjustY = (int)(piece.getAnchor().getY() - ((TetrisPiece)tempPiece).getAnchor().getY());

        x += xOffset + anchorAdjustX;
        y += yOffset + anchorAdjustY;
        piece = (TetrisPiece)tempPiece;
    }

    private void rotateCCW() {
        rotateCCW(0, 0);
    }

    /**
     * Checks if the provided piece would fit on the board as is
     * -both in bounds and not on top of anything
     *
     * Will pass in tempPiece and use tempX, tempY as it coordinates
     */
    private boolean isValid(Piece piece) {
        for (Point offset : piece.getBody()) {
            if (tempX + (int) offset.getX() < 0 || tempX + (int) offset.getX() >= getWidth()) { //TODO - done
                return false;
            }
            if (tempY + (int) offset.getY() < 0 || tempY + (int) offset.getY() >= getHeight()) { //TODO - done
                return false;
            }
            if /*occupied*/ (getGrid(tempX + (int) offset.getX(), tempY + (int) offset.getY())) { //TODO - done
                return false;
            }
        }
        return true;
    }

    /**
     * This method will check if any rows are full, following a PLACE result.
     * It will update the rowsCleared attribute accordingly,
     * as well at the maxHeight and colHeights
     */
    private void clearRows() {
        int fullRowCount = 0;

        // moving from the bottom up, counting full rows and shifting non-full rows down by 'fullRowCount'
        for (int row = 0; row < JTetris.HEIGHT; row++ ) {
            if (isFull(row)) {
                fullRowCount++;
            } else {
                if (fullRowCount > 0) // not a shift by 0
                    System.arraycopy(board[row], 0, board[row - fullRowCount], 0, getWidth());
            }
        }

        // clear the top 'fullRowCount' rows
        for (int rowCount = 0; rowCount < fullRowCount; rowCount ++)
            Arrays.fill(board[JTetris.HEIGHT - 1 - rowCount], false);

        rowsCleared = fullRowCount;

        // adjusting maxHeight, colHeights
        maxHeight -= fullRowCount;
        for (int col = 0; col < JTetris.WIDTH; col ++ )
            colHeights[col] = colHeights[col] - fullRowCount >= 0 ? colHeights[col] - fullRowCount : 0; // making sure colHeights stay at 0 or above
    }

    /**
     * This method returns true if the entire row is populated by blocks
     */
    private boolean isFull(int row) {
        for (int col = 0; col < JTetris.WIDTH; col++) {
            if (!getGrid(col, row))
                return false;
        }
        return true;
    }

    /**
     * This method should return a
     */
    @Override
    public Board testMove(Action act) {
        /*Board savedBoard = new TetrisBoard(this.getHeight(),this.getWidth());
        savedBoard = this;*/
        //TEMPORARY HACKY METHOD JUST TO GET BRAIN TO WORK
        /*boolean[][] savedBoard = board;
        int[] savedColHeights = colHeights;*/
        Board tempBoard = makeCopy();
        tempBoard.move(act);
        /*Board changedBoard = this;
        board = savedBoard;
        colHeights = savedColHeights;*/

        return tempBoard;
    }

    /**
     * This method clones the current TetrisBoard and return a copy that is completely separate
     * -needs to copy board, piece, lastAction, lastResult, rowsCleared, maxHeight, colHeight, *x, y*X
     * -only board, piece, and colHeight are pointers
     */
    private TetrisBoard makeCopy() {
        TetrisBoard copy = new TetrisBoard(getWidth(), getHeight()); //initializes the board[][] and colHeights[]

        copy.setLastAction(this.getLastAction());
        copy.setLastResult(this.getLastResult());
        copy.setRowsCleared(this.getRowsCleared());
        copy.setMaxHeight(this.getMaxHeight());
        for (int col = 0; col < getWidth(); col++ )
            copy.setColumnHeight(col, this.getColumnHeight(col));
        for (int row = 0; row < getHeight(); row++ )
            for (int col = 0; col < getWidth(); col++ )
                copy.setGrid(row, col, this.getGrid(col, row));
        copy.nextPiece(this.piece);
        copy.setX(this.x); // TODO - done
        copy.setY((this.y));// TODO - done

        return copy;
    }

    /**
     * This method reassigns the current piece to the provided piece
     */
    @Override
    public void nextPiece(Piece p) {
        this.piece = (TetrisPiece)p;
        x = getWidth() / 2; //TODO - done
        y = getHeight() - 4; //TODO - done
    }

    /**
     * This method compares two TetrisBoards
     *
     * Currently only checks if the contents of the boards are the same
     */
    // TODO should we compare active pieces too? and previous actions, results, rows cleared?
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TetrisBoard))
            return false;
        TetrisBoard o = (TetrisBoard) other;

        // compare each element
        for (int row = 0; row < getHeight(); row++ ) {
            for (int col = 0; col < getWidth(); col++ ) {
                if (getGrid(col, row) != o.getGrid(col, row))
                    return false;
            }
        }

        return true;
    }

    @Override
    public Result getLastResult() {
        return lastResult;
    }

    public void setLastResult(Result result) { this.lastResult = result; }

    @Override
    public Action getLastAction() {
        return lastAction;
    }

    public void setLastAction(Action action) { this.lastAction = action; }

    @Override
    public int getRowsCleared() {
        return rowsCleared;
    }

    public void setRowsCleared(int rowsCleared) { this.rowsCleared = rowsCleared; }

    @Override
    public int getWidth() {
        return board[0].length;
    }

    @Override
    public int getHeight() {
        return board.length;
    }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    /**
     * This method returns the y-height the piece would end up
     * if it were to be dropped
     */
    // TODO implement in constant time? This seems good enough though...?
    @Override
    public int dropHeight(Piece piece, int xVal) {
        int minDist = Integer.MAX_VALUE;
        for (int col = xVal; col < xVal + piece.getWidth(); col++) {
            int dist = y + piece.getSkirt()[col - xVal] - getColumnHeight(col); //TODO - done
            if (dist < minDist)
                minDist = dist;
        }
        return y - minDist; //TODO - done
    }

    @Override
    public int getColumnHeight(int x) {
        return colHeights[x];
    }

    public void setColumnHeight(int col, int val) { this.colHeights[col] = val; }

    /**
     * This method counts and returns the number of filled blocks in a row
     */
    // TODO implement in constant time
    @Override
    public int getRowWidth(int y) {
        int count = 0;
        for (int col = 0; col < getWidth(); col++)
            if (board[y][col])
                count++;
        return count;
    }

    @Override
    public boolean getGrid(int x, int y) {
        if (x < 0 || x > getWidth() || y < 0 || y > getHeight())
            return true;
        return board[y][x];
    }
    public void setGrid(int row, int col, boolean val) { board[row][col] = val; }

    public void setX(int x) { this.x = x; }

    public int getX() { return this.x; }

    public void setY(int y) { this.y = y; }

    public int getY() { return this.y; }

}
