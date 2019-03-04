package sky7.board;

import java.io.FileNotFoundException;

public interface IBoardGenerator {

    /**
     * Reads from a json file that represents an encoded RoboRally board.
     *
     * @param file a String to the path of the file.
     * @return A Board object.
     * @throws FileNotFoundException    If file is not found.
     * @throws IllegalArgumentException If the file is not json or the format of the board is wrong.
     */
    Board getBoardFromFile(String file) throws FileNotFoundException, IllegalArgumentException;

}
