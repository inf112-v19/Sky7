package sky7.net.packets;

public class ClientConnectionAccepted {

    int playerID;
    String boardName;
    
    public ClientConnectionAccepted(int playerID, String boardName) {
        this.playerID = playerID;
        this.boardName = boardName;
    }

}
