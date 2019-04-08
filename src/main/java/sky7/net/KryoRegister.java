package sky7.net;

import com.esotericsoftware.kryo.Kryo;

import sky7.net.packets.Hand;

public class KryoRegister {

    public KryoRegister(Kryo kryo) {
        kryo.register(Hand.class);
    }

}
