package sky7.host;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;

public class HostNetHandler {

    ServerSocket sSocket;
    ServerSocketHints servHints;
    ArrayList<HostConnection> connections;
    
    public HostNetHandler(final IHost host) {
        servHints = new ServerSocketHints();
        servHints.acceptTimeout = 8000;
        sSocket = Gdx.net.newServerSocket(Protocol.TCP, 6767, servHints);
        while (true) {
            final Socket cSocket = sSocket.accept(null);
            
            new Thread(new Runnable(){

                @Override
                public void run() {
                    connections.add(new HostConnection(host, cSocket));
                }
            }).start();
        }
    }
}
