package sky7.board.cellContents;

import sky7.board.ICell;

public interface IMoving extends ICell {


    DIRECTION getOrientation();

    /**
     * Rotate counterclockwise
     */
    void rotateCCW();

    /**
     * Rotate clockwise
     */
    void rotateCW();

    /**
     * Rotate 180 degree.
     */
    void rotate180();

}
