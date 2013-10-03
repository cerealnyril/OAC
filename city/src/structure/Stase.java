package structure;

import java.util.ArrayList;

import tools.Identifiants;


/** Batiment de stase. Permet de mettre son personnage en attente pendant une longue
 * durée contre ressources. A l'expiration du contrat le personnage est decongelé et laché dans
 * nature. */
public class Stase extends Batiment{

	private ArrayList<Integer> clients;
	public Stase(int id_b, int id_q) {
		super(id_b, id_q);
		clients = new ArrayList<Integer>();
		super.type = Identifiants.staseBat;
		setTaille();
	}
	private void setTaille(){
		super.profondeur = 2;
		super.facade = 2;
	}
	/** renvois les identifiants de tout les clients en etat de stase */
	public ArrayList<Integer> getClients(){
		return clients;
	}
	/** rajoute un client dans la liste des clients */
	public void addClient(int id){
		clients.add(id);
	}
	/** fonction qui indique si le personnage demandé est en stase */
	public boolean isClient(int id){
		if(clients.contains(id)){
			return true;
		}
		return false;
	}
	@Override
	public int getConsommationEnergie() {
		return clients.size();
	}

}
