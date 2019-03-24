package sky7.board.cellContents;

import sky7.board.ICell;

public interface IMoving extends ICell {

    DIRECTION getOrientation();

    void rotateCCW();

    void rotateCW();

    void rotate180();

}
