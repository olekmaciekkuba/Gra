
package Gra;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

// This class is a convenient place to keep things common to both the client and server.
public class Network {
	static public final int port = 54555;

	// This registers objects that are going to be sent over the network.
	static public void register (EndPoint endPoshort) {
		Kryo kryo = endPoshort.getKryo();
		kryo.register(Register.class);
		kryo.register(AddCharacter.class);
		kryo.register(UpdateCharacter.class);
		kryo.register(RemoveCharacter.class);
		kryo.register(Character.class);
		kryo.register(MoveCharacter.class);
                kryo.register(CharacterID.class);
                kryo.register(SendChat.class);
                kryo.register(SetMap.class);
                kryo.register(Combat.class);
                kryo.register(Dead.class);
                kryo.register(NewPosition.class);
                kryo.register(Kick.class);
                kryo.register(Immortal.class);
                kryo.register(Klatka.class);
                kryo.register(Frag.class);
                kryo.register(SetGM.class);
                kryo.register(NameList.class);
                kryo.register(GetNameList.class);
                kryo.register(String[].class);
                kryo.register(RegisterOK.class);
                kryo.register(RegisterAgain.class);
	}
        static public class GetNameList{
            
        }
        static public class NameList{
                public String[] name;
        }
        static public class SetGM{
                public short id;
        }
        static public class Klatka{
                public short id, a;
        }
        static public class Frag{
                public short id;
        }
        static public class Immortal{
                public short id;
                public boolean immortal;
        }
        static public class Kick{
                public String name;
        }
        static public class NewPosition{
                public short x,y,hp;
        }
        static public class Dead{
                public short characterID;
        }
        static public class Combat{
                public boolean attack;
        }
        
        static public class SetMap{
                public String name;
        }
        
        static public class SendChat{
                public String napis;
        }
        static public class CharacterID{
                public short id;
                public boolean admin;
        }
	static public class Register {
		public String name;
                public String password;
                public short image;
	}
        static public class RegisterOK{
            
        }
        static public class RegisterAgain{
            
        }

	static public class UpdateCharacter {
		public short id, x, y;
                public boolean attack;
	}

	static public class AddCharacter {
		public Character character;
	}

	static public class RemoveCharacter {
		public short id;
	}

	static public class MoveCharacter {
		public short x, y;
                public boolean attack;
	}
}
