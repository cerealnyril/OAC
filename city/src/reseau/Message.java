package reseau;

import java.net.InetAddress;

/**
 * Un message est un Paquet lié à un Client
 * @author synoril
 *
 */
public class Message {
	private int ID = -1; //ID du client
	private int port = -1;
	private InetAddress IP;
	private Paquet pack;
	
	public Message(int ID, Paquet pack, int port, InetAddress ip){
		this.ID = ID;
		this.pack = pack;
		this.port = port;
		this.IP= ip;
	}
	
	public int getID(){
		return this.ID;
	}
	
	public Paquet getPack(){
		return this.pack;
	}
	
	public int getPort(){
		return this.port;
	}
	
	public InetAddress getIP(){
		return this.IP;
	}
}
