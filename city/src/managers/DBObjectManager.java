package managers;

import java.sql.Connection;
import java.util.Iterator;
import java.util.TreeMap;

import bdd.DBLoad;
import bdd.DBSave;
import bdd.Database;

import monde.Ville;

import structure.Batiment;
import structure.Bloc;
import structure.Quartier;

/** cette classe stock temporairement les objets à mettre à jour dans la base de donnée */
public class DBObjectManager {
	private TreeMap<Integer, Quartier> insert_quartiers, update_quartiers;
	private TreeMap<Integer, Bloc> insert_blocs, update_blocs;
	private TreeMap<Integer, Batiment> insert_batiments, update_batiments;
	private Ville ville;
	private int max_id;
	
	/** constructeur avec initialisations */
	public DBObjectManager(){
		insert_quartiers = new TreeMap<Integer, Quartier>();
		insert_blocs = new TreeMap<Integer, Bloc>();
		insert_batiments = new TreeMap<Integer, Batiment>();
		update_quartiers = new TreeMap<Integer, Quartier>();
		update_blocs = new TreeMap<Integer, Bloc>();
		update_batiments = new TreeMap<Integer, Batiment>();
		Connection connect = Database.connect();
		int max_id_ville = DBLoad.getMaxIDVille(connect);
		int max_id_quartier = DBLoad.getMaxIDQuartier(connect);
		int max_id_bloc = DBLoad.getMaxIDBatiment(connect);
		int max_id_batiment = DBLoad.getMaxIDBatiment(connect);
		max_id = (Math.max(
				Math.max(max_id_ville, max_id_quartier), 
				Math.max(max_id_bloc, max_id_batiment)
				)+100);
		Database.closeConnection(connect);
	}
	/** ajout d'une ville */
	public int storeVille(Ville v){
		max_id++;
		ville = v;
		return max_id;
	}
	/** ajout d'un quartier à rajouter */
	public int storeQuartier(Quartier quartier){
		max_id++;
		insert_quartiers.put(max_id, quartier);
		return max_id;
	}
	/** ajout d'un bloc à rajouter */
	public int storeBloc(Bloc bloc){
		max_id++;
		insert_blocs.put(max_id, bloc);
		return max_id;
	}
	/** ajout d'un batiment à rajouter */
	public int storeBat(Batiment bat){
		max_id++;
		insert_batiments.put(max_id, bat);
		return max_id;
	}
	/** ajout d'un quartier à modifier */
	public void updateQuartier(Quartier quartier){
		if(insert_quartiers.get(quartier.getID()) == null){
			update_quartiers.put(quartier.getID(), quartier);
		}
	}
	/** ajout d'un bloc à modifier */
	public void updateBloc(Bloc bloc){
		if(insert_blocs.get(bloc.getID()) == null){
			update_blocs.put(bloc.getID(), bloc);
		}
	}
	/** ajout d'un batiment à modifier */
	public void updateBat(Batiment bat){
		if(insert_batiments.get(bat.getID()) == null){
			update_batiments.put(bat.getID(), bat);
		}
	}
	/** sauvegarde et met à jour les nouvelles données */
	public void save(){
		//insertion si nouvelle ville, mise à jour de la date sinon 
		Connection connect = Database.connect();
		if(connect != null){
			if(!DBLoad.existVille(connect, ville.getID())){
				DBSave.insertVille(connect, ville.getNom(), ville.getAge(), ville.getID());
			}
			else{
				DBSave.updateVille(connect, ville.getID(), ville.getAge());
			}
			insert(connect);
			update(connect);
			Database.closeConnection(connect);
		}
	}
	/** insere tout les nouveaux elements dans la base de donnée */
	private void insert(Connection connect){
		Iterator<Integer> iter_quartier = insert_quartiers.keySet().iterator();
		while(iter_quartier.hasNext()){
			Quartier quartier = insert_quartiers.get(iter_quartier.next());
			DBSave.insertQuartier(connect, quartier);
		}
		insert_quartiers = new TreeMap<Integer, Quartier>();
		Iterator<Integer> iter_bloc = insert_blocs.keySet().iterator();
		while(iter_bloc.hasNext()){
			Bloc bloc = insert_blocs.get(iter_bloc.next());
			DBSave.insertBloc(connect, bloc);
		}
		insert_blocs = new TreeMap<Integer, Bloc>();
		Iterator<Integer> iter_batiment = insert_batiments.keySet().iterator();
		while(iter_batiment.hasNext()){
			Batiment batiment = insert_batiments.get(iter_batiment.next());
			DBSave.insertBat(connect, batiment);
		}
		insert_batiments = new TreeMap<Integer, Batiment>();
	}
	/** met à jour tout les elements dans la base de donnée lorsqu'ils ont subit une modification */
	private void update(Connection connect){
		Iterator<Integer> iter_quartier = update_quartiers.keySet().iterator();
		while(iter_quartier.hasNext()){
			Quartier quartier = update_quartiers.get(iter_quartier.next());
			DBSave.updateQuartier(connect, quartier);
		}
		update_quartiers = new TreeMap<Integer, Quartier>();
		Iterator<Integer> iter_bloc = update_blocs.keySet().iterator();
		while(iter_quartier.hasNext()){
			Bloc bloc = update_blocs.get(iter_bloc.next());
			DBSave.updateBloc(connect, bloc);
		}
		update_blocs = new TreeMap<Integer, Bloc>();
		Iterator<Integer> iter_batiment = update_batiments.keySet().iterator();
		while(iter_batiment.hasNext()){
			Batiment batiment = update_batiments.get(iter_batiment.next());
			DBSave.updateBatiment(connect, batiment);
		}
		update_batiments = new TreeMap<Integer, Batiment>();
	}
}
