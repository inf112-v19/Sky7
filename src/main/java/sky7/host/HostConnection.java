package sky7.host;

import com.badlogic.gdx.net.Socket;

public class HostConnection {

    Socket cSocket;
    IHost host;
    
    public HostConnection(IHost host, Socket cSocket) {
        this.cSocket = cSocket;
        this.host = host;
    }

}
