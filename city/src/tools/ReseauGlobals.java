package tools;

import java.util.logging.Logger;

public class ReseauGlobals {
	
	public static Logger log = Logger.getLogger("log");
	
	/** paquet pour demander une connection au serveur */
	public static byte[] PING = new byte[]{	0,0,0,0,0,0,0,0,0,0,
											0,0,0,0,0,0,0,0,0,0,
											0,0,0,0,0,0,0,0,0,0,
											0,0,0,0,0,0,0,0,0,0,
											0,0,0,0,0,0,0,0,0,0,
											0,0,0,0,0,0,0,0,0,0,
											0,0,1,0
											};
	/** paquet pour dire au client que c'est bon on a accepté sa connection */
	public static byte[] PONG = new byte[]{	0,0,0,0,0,0,0,0,0,0,
											0,0,0,0,0,0,0,0,0,0,
											0,0,0,0,0,0,0,0,0,0,
											0,0,0,0,0,0,0,0,0,0,
											0,0,0,0,0,0,0,0,0,0,
											0,0,0,0,0,0,0,0,0,0,
											0,0,0,1
											};
	/** paquet pour demander au serveur de balancer toute la ville */
	public static byte[] GIMMI = new byte[]{0,0,0,0,0,0,0,0,0,0,
											0,0,0,0,0,0,0,0,0,0,
											0,0,0,0,0,0,0,0,0,0,
											0,0,0,0,0,0,0,0,0,0,
											0,0,0,0,0,0,0,0,0,0,
											0,0,0,0,0,0,0,0,0,0,
											0,1,0,0
											};
	public static int SERVEUR_PORT = 1666;
}

