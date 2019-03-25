package sky7.host;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;

public class HostNetListener {

    ServerSocket sSocket;
    ServerSocketHints servHints;
    
    public HostNetListener(final IHost host) {
        servHints = new ServerSocketHints();
        servHints.acceptTimeout = 8000;
        sSocket = Gdx.net.newServerSocket(Protocol.TCP, 6767, servHints);
        while (true) {
            final Socket cSocket = sSocket.accept(null);
            
            new Thread(new Runnable(){

                @Override
                public void run() {
                    new HostConnection(host, cSocket);
                }
            }).start();
        }
    }
}
