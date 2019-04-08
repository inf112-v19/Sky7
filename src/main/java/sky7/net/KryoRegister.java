package sky7.net;

import com.esotericsoftware.kryo.Kryo;

import sky7.net.packets.Hand;
import sky7.net.packets.RegistryDiscard;

public class KryoRegister {

    public KryoRegister(Kryo kryo) {
        kryo.register(Hand.class);
        kryo.register(RegistryDiscard.class);
    }

}
