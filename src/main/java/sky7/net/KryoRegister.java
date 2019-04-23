package sky7.net;

import java.util.ArrayList;
import java.util.HashMap;

import com.esotericsoftware.kryo.Kryo;

import sky7.card.ICard;
import sky7.card.ProgramCard;
import sky7.net.packets.*;

public class KryoRegister {

    public KryoRegister(Kryo kryo) {
        
        //packet classes
        kryo.register(Hand.class);
        kryo.register(RegistryDiscard.class);
        kryo.register(ClientConnectionAccepted.class);
        kryo.register(ProcessRound.class);
        kryo.register(PlaceRobot.class);
        
        //other necessary resources
        kryo.register(ICard.class);
        kryo.register(ProgramCard.class);
        kryo.register(ArrayList.class);
        kryo.register(HashMap.class);
    }

}
