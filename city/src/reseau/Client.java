package reseau;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;

import elements2D.Ville;

import tools.ReseauGlobals;


/**
 * Classe gérante de la partie réseau cliente de l'application.
 * @author synoril
 *
 */
public class Client {
	
	private DatagramSocket clientSocket;
	private InetAddress IP;
	private int clientID;
	private int port_serveur;
	private int port_client;
	private boolean online;
	public boolean ping;
	private Ville ville;
//	private ModelTransmit mt;
	
	/**
	 * Instancie la partie réseau du client de l'application.
	 * @param port : port de connexion au serveur
	 * @param ip : adresse de connexion au serveur
	 */
	public Client(int port, InetAddress ip){
		this.online = false;
		this.port_serveur = port;
		this.IP = ip;
		this.clientID = -1;
	}
	
	public Client(int port, InetAddress ip, int id) {
		this.online = false;
		this.port_client = port;
		this.IP = ip;
		this.clientID = id;
	}

	public void setPortClient(int p){
		ReseauGlobals.log.log(Level.INFO,"nouveau port client : "+p);
		this.port_client = p;
	}
	
	/**
	 * Création d'un socket de connexion et identification auprès du serveur
	 */
	public void connect(){

		ReseauGlobals.log.info("Création du socket de connexion.");
		this.port_client = -1;
		try{
			clientSocket = new DatagramSocket();
		}
		catch(SocketException e){
			ReseauGlobals.log.log(Level.SEVERE,"Création du socket impossible : "+e);
			System.exit(1);
		}
		
		ReseauGlobals.log.info("Envoie d'un paquet PING au serveur.");
		DatagramPacket dp = new DatagramPacket(ReseauGlobals.PING, 64, this.IP, this.port_serveur);
		try {
			clientSocket.send(dp);
		} catch (IOException e) {
			ReseauGlobals.log.log(Level.SEVERE,"Envoi du paquet PING impossible : "+e);
			System.exit(1);
		}
		
		ReseauGlobals.log.info("Écoute de la réponse.");
		byte[] receiveData = new byte[64];
		dp = new DatagramPacket(receiveData, 64);
		try {
			clientSocket.receive(dp);
		} catch (IOException e) {
			ReseauGlobals.log.log(Level.SEVERE,"Réception du paquet PONG impossible : "+e);
			System.exit(1);
		}
		
		ReseauGlobals.log.info("Création d'un Thread d'écoute.");
		Thread t = new Thread() {
		   	public void run() {
		   			online = true;
		   			accept();
		   		}
		};
		long threadID = t.getId();
		ReseauGlobals.log.info("ID du Thread d'écoute :"+threadID);
		t.start();
		//envois un paquet pour avoir toute la ville
	}
	
	public void disconnect(){
		ReseauGlobals.log.info("Fermeture du Thread d'écoute.");
		online = false;
	}
	
	/**
	 * Création d'une boucle pour accepter les paquets en provenance du serveur.
	 */
	private void accept(){
		while(online){
			byte[] receiveData = new byte[64];
			DatagramPacket dp = new DatagramPacket(receiveData, 64);
   			ReseauGlobals.log.info("Attente d'un paquet.");
			try {
				clientSocket.receive(dp);
				compute(dp);
			} catch (IOException e) {
	   			ReseauGlobals.log.severe("Réception du paquet impossible : "+e);
			}
		}
	}
	/** Pour demander au serveur de tout filer histoire qu'on se construise la ville quand meme */
	public void askForAllCity(){
		//avant la boucle on vas demander le gimmi
		DatagramPacket dp = new DatagramPacket(ReseauGlobals.GIMMI, 64, this.IP, this.port_serveur);
		try {
			//System.out.println("Envois d'un paquet GIMMI");
			clientSocket.send(dp);
		} catch (IOException e) {
			ReseauGlobals.log.log(Level.SEVERE,"Envoi du paquet GIMMI impossible : "+e);
			System.exit(1);
		}
	}
	/** met en place la ville graphique en 2D. Il faudra voir plus tard si on fait une sorte de selecteur en fonction
	 * du type de ville (2D ou 3D) qui vas tomber */
	public void setVille(Ville ville){
		this.ville = ville;
	}
	/**
	 * Traite un paquet reçu.
	 * @param dp : Datagramme reçu.
	 */
	private void compute(DatagramPacket dp){
		if(ville != null){
			ville.getMsg(new Paquet(dp.getData()));
		}
		//mes tests pour le type de donnée refus
	}
	
	public InetAddress getAdress(){
		return this.IP;
	}
	
	public int getClientPort(){
		return this.port_client;
	}
	
	public int getClientID(){
		return this.clientID;
	}
}
