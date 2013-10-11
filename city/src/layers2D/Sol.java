package layers2D;

import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import elements2D.Bloc;
import elements2D.Canal;
import elements2D.Quartier;
import elements2D.Rocade;
import elements2D.Route;

/** Groupe d'images qui composent le sol du monde */
public class Sol extends Group{
	
	private Array<Route> routes;
	private Array<Canal> canaux;
	private ObjectMap<Integer, Bloc> blocs;
	private ObjectMap<Integer, Array<Rocade>> rocades;
	
	public Sol(){
		this.blocs = new ObjectMap<Integer, Bloc>();
		this.routes = new Array<Route>();
		this.canaux = new Array<Canal>();
		this.rocades = new ObjectMap<Integer, Array<Rocade>>();
	}
/*----------------------------ACCESSEURS---------------------------*/	
	public Group getSol(){
		return this;
	}
	/** Retourne un bloc dans la map de blocs */
	private Bloc getBlocFromId(int id){
		return blocs.get(id);
	}
/*---------------------------SETTEURS ET AJOUT MAJ-------------------*/
	/** Ajoute une dalle de quartier au sol */
	public void upQuartier(Quartier quartier){
		this.addActor(quartier.getImg());
	}
	
	/** Ajoute une route sur le sol */
	public void upRoute(Route route) {
		this.routes.add(route);
		this.addActor(route.getImg());
	}
	
	/** Ajoute un canal sur le sol */
	public void upCanal(Canal canal){
		this.canaux.add(canal);
		this.addActor(canal.getImg());
	}

	/** Ajoute une rocade sur le sol */
	public void upRocade(Rocade rocade){
		//Si ce n'est pas le meme jour on vire tous le RLEs
		if(rocades.get(rocade.getID_q()) != null && rocades.get(rocade.getID_q()).get(0).getJour() != rocade.getJour()){
			rocades.remove(rocade.getID_q());
			//TODO : virer rocades 
		}
		//Si le rle n'est pas vide 
		Array<Rocade> mes_rocades = new Array<Rocade>();
		if(rocades.get(rocade.getID_q()) != null){
			mes_rocades.addAll(rocades.get(rocade.getID_q()));
		}
		//Dans tous les cas on rajoute le nouvel objet
		mes_rocades.add(rocade);
		rocades.put(rocade.getID_q(), mes_rocades);
		this.addActor(rocade.getImg());
	}
	
	/** Ajoute un bloc sur le sol */
	public void upBloc(Bloc bloc){
		//si le bloc existe deja
		if(getBlocFromId(bloc.getID()) != null){
			getBlocFromId(bloc.getID()).setPosition(bloc.getPosition());
		}
		//sinon on créé un nouveau
		else{
			this.blocs.put(bloc.getID(), bloc);
			this.addActor(bloc.getImg());
		}
	}
	
	/** Fonction qui translate tout sur la map quand la ville grandie */
	public void translation(int id_quartier, Vector2 translation){
		//on vas commencer par parcourir chaque liste et si ça colle en id alors on translate
		Iterator<Integer> iter = blocs.keys().iterator();
		Bloc bloc;
		while(iter.hasNext()){
			bloc = blocs.get(iter.next());
			if(bloc.getID_q() == id_quartier){
				bloc.setPosition(new Vector2((bloc.getPosition().x + translation.x),
						(bloc.getPosition().y + translation.y)));
			}
		}
		Iterator<Route> iter_route = routes.iterator();
		Route route;
		while(iter_route.hasNext()){
			route = iter_route.next();
			if(route.getID_q() == id_quartier){
				route.setPosition(new Vector2((route.getPosition().x + translation.x),
						(route.getPosition().y + translation.y)));
			}
		}
		Iterator<Canal> iter_canaux = canaux.iterator();
		Canal canal;
		while(iter_canaux.hasNext()){
			canal = iter_canaux.next();
			if(canal.getID_q() == id_quartier){
				canal.setPosition(new Vector2((canal.getPosition().x + translation.x),
						(canal.getPosition().y + translation.y)));
			}
		}
	}
}
