
package Gra;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

// This class is a convenient place to keep things common to both the client and server.
public class Network {
	static public final int port = 54555;

	// This registers objects that are going to be sent over the network.
	static public void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
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
	}
        static public class Kick{
                public String name;
        }
        static public class NewPosition{
                public int x,y,hp;
        }
        static public class Dead{
                public int characterID;
        }
        static public class Combat{
                public boolean attack;
        }
        
        static public class SetMap{
                public int id;
            
        }
        
        static public class SendChat{
                public String napis;
        }
        static public class CharacterID{
                public int id;
                public boolean admin;
        }
	static public class Register {
		public String   name;
                public int      image;
	}

	static public class UpdateCharacter {
		public int id, x, y;
//                public boolean right_move;
//                public boolean move;
                public boolean attack;
                public int a, b;
                public int frags;
                public int dead;
	}

	static public class AddCharacter {
		public Character character;
	}

	static public class RemoveCharacter {
		public int id;
	}

	static public class MoveCharacter {
		public int x, y;
                public int a, b;
                public boolean attack;
                MoveCharacter(){
                    x=0; y=0;
                }
	}
}
