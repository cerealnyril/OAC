package reseau;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import managers.CommunicationManager;

import structure.Batiment;
import tools.ParamsGlobals;
import tools.ReseauGlobals;


/**
 * Classe gérante de la partie serveur réseau de l'application.
 * @author synoril
 *
 */
public class Serveur {
	
	private boolean online;
	private DatagramSocket serverSocket;
	private int port;
	private ConcurrentHashMap<Integer, Message> receivePile = new ConcurrentHashMap<Integer,Message>();
	private ConcurrentHashMap<Integer, Message> sendPile = new ConcurrentHashMap<Integer,Message>();
	private ConcurrentHashMap<Integer, Client> identifiants = new ConcurrentHashMap<Integer, Client>();
	private long sendThreadID;
	private long receiveThreadID;
	private long pileThreadID;
	private int pingCount;
	private CommunicationManager cm;
	
	/**
	 * Constructeur de la classe Serveur.
	 * @param port : port d'écoute du serveur.
	 */
	public Serveur(int port){
		this.online = false;
		this.port = port;
	}
	
	/**
	 * Ouvre le port d'écoute du serveur et attend une connexion.
	 * @throws Exception
	 */
	public void connect(CommunicationManager cm) throws Exception{
		this.online = true;
		this.cm = cm;
		this.serverSocket = new DatagramSocket(this.port);
		ReseauGlobals.log.info("Ouverture du Thread d'écoute sur le port : "+port+".");
		Thread r = new Thread() {
		   	public void run() {
		   			while(online){
		   				pushReceivePile();
		   			}
		   		}
		};
		receiveThreadID = r.getId();
		ReseauGlobals.log.info("ID du Thread de réception :"+receiveThreadID);
		r.start();
		
		Thread s = new Thread() {
		   	public void run() {
		   			while(online){
		   				pushSendPile();
		   				pingCount++ ;
	   					//System.out.println(pingCount);
		   				if (pingCount == 100){
		   					pingCount = 0;
		   					pingClients();
		   				}
		   			}
		   		}
		};
		sendThreadID = s.getId();
		ReseauGlobals.log.info("ID du Thread d'envoi :"+sendThreadID);
		s.start();
	}
	
	protected void pingClients() {
		for (int i=0 ; i<identifiants.size(); i++){
			Client client = identifiants.get(i);
			client.ping = false;
			send(new Message(client.getClientID(),new Paquet(ReseauGlobals.PING),client.getClientPort(),client.getAdress()));
		}
	}

	/**
	 * Traite un message en réception et un message en envoi.
	 */
	private synchronized void traiteReceivePile() {
   		if (receivePile.size() != 0){
   			receive(receivePile.get(0));
	   		if(receivePile.size() < 1)
	   			for (int i=0 ; i<receivePile.size()-1 ; i++){
		   			receivePile.replace(i,receivePile.get(i+1));
		   		}
   			receivePile.remove(receivePile.size()-1);
   		} 
		
	}

	/**
	 * Créé la pile d'envoi dans un nouveau Thread et traite le premier élément de la pile.
	 */
	private synchronized void pushSendPile() {
		if (sendPile.size() != 0){
			send(sendPile.get(0));
	   		if(sendPile.size() < 1){
	   			for (int i=0 ; i<sendPile.size()-1 ; i++){
		   			sendPile.replace(i,sendPile.get(i+1));
		   		}
   			sendPile.remove(sendPile.size()-1);
	   		}
		}
	}

	/**
	 * Crée la pile de réception dans un nouveau Thread et ajoute les paquets reçus à la pile de réception.
	 */
	private synchronized void pushReceivePile() {
   		byte[] receivedData = new byte[64];
		DatagramPacket rp = new DatagramPacket(receivedData, 64);
		try {
			serverSocket.receive(rp);
		} catch (IOException e) {
			ReseauGlobals.log.info("Réception de paquet impossible :"+e);
		}
		Paquet p = new Paquet (rp.getData());
		int id = isKnown(rp);
		System.out.println("identifiant !!! "+id);
		Message m = new Message(id,p,rp.getPort(), rp.getAddress());
		receivePile.put(receivePile.size(), m);
		traiteReceivePile();
	}

	/**
	 * Identifie un paquet reçu et le redirige vers le client de destination.
	 * Crée un nouveau client si le destinataire est inconnu.
	 * @param dp : données reçues
	 */
	private void receive(Message msg){
		if(msg.getID() != -1){
			compute(msg);
		}
		else{
			createClient(msg);
		}
	}
	
	/** permet de traiter un packet venant d'un client connu*/
	private void compute(Message msg){
		ReseauGlobals.log.info("Traitement d'un paquet connu.");
		byte[] data = new byte[64];
		data = msg.getPack().getDatagram();
		//si le datagramm c'est pas un ping ou un pong
		if(!isSame(data, ReseauGlobals.PING) && !isSame(data, ReseauGlobals.PONG)){
			cm.broadcastFull(msg.getID(), msg.getPort(), msg.getIP());
		}
		//sinon on sait pas encore mais ça vas surrement etre des commandes de joueur
		else{
			ReseauGlobals.log.info("Je ne sais pas traiter ce paquet.");
		}
	} 
	
	/**
	 * Crée un client à la suite de la réception d'un paquet d'un auteur inconnu.
	 * @param dp
	 */
	private void createClient(Message msg){
		byte[] data = new byte[64];
		data = msg.getPack().getDatagram();
		ReseauGlobals.log.info("Paquet inconnu : est-ce un PING?");
		if(isSame(data, ReseauGlobals.PING)){
			ReseauGlobals.log.info("Oui, envoi d'un PONG et création d'un client : "+identifiants.size()+" - "+msg.getIP()+" - "+msg.getPort());
			Client client = new Client(msg.getPort(), msg.getIP(),identifiants.size());
			identifiants.put((identifiants.size()), client);
			send(new Message(client.getClientID(),new Paquet(ReseauGlobals.PONG),client.getClientPort(),client.getAdress()));
		}
		else{
			ReseauGlobals.log.info("J'aime pas trop les voleurs et les fils de putes.");
		}
	}
	
	/**
	 * Envoie un paquet à un client.
	 * @param client : destinataire
	 * @param tab : données à envoyer
	 */
	public void send(Message msg){
		ReseauGlobals.log.info("Envoi d'un paquet.");
		try {
			serverSocket.send(new DatagramPacket(msg.getPack().getDatagram(), 64, msg.getIP(), msg.getPort()));
		} catch (IOException e) {
			ReseauGlobals.log.info("Envoi du paquet impossible : "+e);
		}
		
	}
	
	/**
	 * Envoie un paquet à tous les clients si il y'a des clients
	 * @param pack : packete
	 */
	public void sendToAll(Paquet pack){
		if(identifiants.size() > 0){
			ReseauGlobals.log.info("Envoi d'un paquet global.");
			for (int i=0 ; i<identifiants.size(); i++){
				Client client = identifiants.get(i);
				client.ping = false;
				send(new Message(client.getClientID(), pack, client.getClientPort(), client.getAdress()));
			}
		}
	}
	
	public void disconnect(){
		this.online = false;
	}
	
	/**
	 * Parcourt la liste des clients connus pour déterminer si l'auteur d'un paquet est connu.
	 * @param ia : adresse de l'auteur
	 * @param port : port de l'auteur
	 * @return : ID de l'auteur, -1 si inconnu.
	 */
	private int isKnown(DatagramPacket dp){
		String ia = dp.getAddress().getCanonicalHostName();
		int port = dp.getPort();
		ReseauGlobals.log.info("Identification d'un paquet.");
		Iterator<Integer> iter = identifiants.keySet().iterator();
		while(iter.hasNext()){
			int test = iter.next();
			String test_addr = identifiants.get(test).getAdress().getCanonicalHostName();
			if(test_addr.equalsIgnoreCase(ia)){
				if(port == identifiants.get(test).getClientPort()){
					return test;
				}
			}
		}
		return -1;
	}
	
	/**
	 * Compare un 2 paquets.
	 * @param test_1 : paquet 1
	 * @param test_2 : paquet 2
	 * @return
	 */
	private boolean isSame(byte[] test_1, byte[] test_2){
//		ReseauGlobals.log.info("Comparaison de paquets.");
		if(test_1.length == test_2.length){
			for(int i = 0; i < test_1.length; i++){
				if(test_1[i] != test_2[i]){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/** Renvois une chaine de caract�re avec les infos des joueurs connect�s */
	public String getClientInfos(){
		String result = "";
		Iterator<Integer> iter = identifiants.keySet().iterator();
		while(iter.hasNext()){
			int clef = iter.next();
			Client tmp = identifiants.get(clef);
			result = result+""+clef+"---"+tmp.getAdress().toString()+"---"+tmp.getClientPort()+"\r";
		}
		return result;
	}
	
	/** retourne le nombre de clients enregistr�s */
	public int registeredCount(){
		return identifiants.size();
	}
}
