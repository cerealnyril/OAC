package managers;


/*import grafx.Camera;
import grafx.MapVBO;
import grafx.Model;
import grafx.Quad;
import grafx.Vertex;
*/
import initialisation.Initialisation;

/*import java.util.ArrayList;
import java.util.Iterator;
*/
import bdd.DBCreate;

//import com.badlogic.gdx.utils.Logger;

import monde.Ville;
import reseau.Serveur;
import structure.Batiment;
import structure.Bloc;
import structure.Quartier;
import structure.Rail;
import structure.RLE;
//import tools.Identifiants;
import tools.ParamsGlobals;
import topographie.Cell;

/** Le meta manager est une classe qui permet de gérer tout les objets de la ville
 * qui necessite un stockage ou un envois à un instant T*/
public class MetaManager {
	private DBObjectManager db;
	private CommunicationManager com;
//	private MinimapManager minimap;
//	private Old_TimeManager time;
	private int id_rail;
//	private Old_DisplayManager display;
//	private Old_TextureManager texture;
//	private Old_ModelManager model;
/*	private MapVBO map;
	private MapVBO height;
	private MapVBO rails;
	private MapVBO blocs;
	private MapVBO roads;
	private Camera camera;*/
//	private Old_RenderManager render;
//	private Old_MultiverseManager multiverse;

	/** création et enregistrement des instance des autres managers */
	public MetaManager(){
		new DBCreate().createTables();
		db = new DBObjectManager();
		com = new CommunicationManager();
//		minimap = new MinimapManager();
		id_rail = -2;
		switch(ParamsGlobals.DISPLAY){
			//affichage pour la vue en 2D du dessus
			case 1:
				init2DDisplay();
			break;
			case 2 :
			break;
			//affichage pour la vue en 3D
			case 3 : 
			break;
		}
        if(ParamsGlobals.MANAGER == null){
        	ParamsGlobals.registerManager(this);
        }
        ParamsGlobals.VILLE = new Initialisation().execute();
        ParamsGlobals.VILLE.setTimer();
        
        //gestion serveur

        //a rajouter
        
        
        /*if (ParamsGlobals.DISPLAY > 0)
        {
        	ParamsGlobals.MANAGER.makeDisplay();
        }*/
	}

	/** Cette fonction a pour role de charger les sprites des objets 2D ou du moins leur coordonées puis elle
	 * transmettra ces infos*/
	private void init2DDisplay(){

		
	}	
	
/*	private void initDisplay() {
		multiverse = new MultiverseManager();
		model = new ModelManager();
		model.addModel("assets/Meca/kub.obj");
		map = new MapVBO();
		height = new MapVBO();
		rails = new MapVBO();
		roads = new MapVBO();
		blocs = new MapVBO();
		render = new RenderManager();
		time = new TimeManager();
		//texture = new TextureManager();
		display = new DisplayManager();
	}*/

/*-----------------------------FONCTIONS PRINCIPALES DE GESTION DES OBJETS-------------------------*/
	/** lancement de l'enregistrement dans tout les managers */
	public void executeManagement(){
		//gestionnaire de base de donnee
//		db.save();
		com.broadcastUpdate();
	}
/*--------------------------------------MISE A JOUR--------------------------------------------------*/
	/** stockage d'un objet mis a jour 
	 * @param test: un objet de n'importe quel type qui sera testé pour etre redirigé vers le 
	 * bon manager*/
	public void updateObject(Object test){
		//si l'objet est un quartier 
		if(test instanceof Quartier){
			Quartier quartier = (Quartier) test;
			updateQuartier(quartier);
		}
		//si l'objet est un bloc
		else if(test instanceof Bloc){
			Bloc bloc = (Bloc) test;
			updateBloc(bloc);
		}
		//si l'objet est un batiment
		else if(test instanceof Batiment){
			Batiment batiment = (Batiment) test;
			updateBatiment(batiment);
		}
		//si l'objet est une cellule
		else if(test instanceof Cell){
			Cell cell = (Cell) test;
			//si c'est une route
			if((cell).isRoad()){
				updateRoute(cell);
			}
			//si c'est un chemin
/*			if((cell).isRoad()){
				updateRoute(cell);
			}*/
			//si c'est un canal
			else if((cell).isCanal()){
				updateCanal(cell);
			}
		}
		//si c'est un RLE
		else if(test instanceof RLE){
			RLE rle = (RLE) test;
			updateRLE(rle);
		}
		//si c'est un rail
		else if(test instanceof Rail){
			Rail rail = (Rail) test;
			updateRail(rail);
		}
	}
	/** met à jour un quartier dans les différents managers */
	private void updateQuartier(Quartier quartier){
		db.updateQuartier(quartier);
		if(quartier.getID() != -1){
			com.updateQuartier(quartier.getID(), quartier);
		}
	}
	/** met à jour un bloc dans les différents managers */
	private void updateBloc(Bloc bloc){
		db.updateBloc(bloc);
		com.updateBloc(bloc.getID(), bloc);
	}
	/** met à jour un batiment dans les différents managers */
	private void updateBatiment(Batiment bat){
		db.updateBat(bat);
		com.updateBat(bat.getID(), bat);
	}
	/** met à jour un RLE dans les différents managers */
	private void updateRLE(RLE rle){
		com.updateRLE(rle);
	}
	/** met à jour une portion de route */
	private void updateRoute(Cell cell){
		com.updateRoute(cell);
	}
	/** met à jour une portion de chemin */
	private void updateChemin(Cell cell){
		com.updateChemin(cell);
	}
	/** met à jour une portion de canal */
	private void updateCanal(Cell cell){
		com.updateCanal(cell);
	}
	/** met à jour une portion de chemin */
	private void updateRail(Rail rail){
		com.updateRail(rail);
	}
/*------------------------------------------AJOUT--------------------------------------------------*/
	/** stockage d'une nouvel objet 
	 * @param test: un objet de n'importe quel type qui sera testé pour etre redirigé vers le 
	 * bon manager*/
	public int registerObject(Object test){
		int id = -1;
		if(test instanceof Ville){
			Ville ville = (Ville) test;
			id = addVille(ville);
		}
		//si l'objet est un quartier 
		else if(test instanceof Quartier){
			Quartier quartier = (Quartier) test;
			id = addQuartier(quartier);
		}
		//si l'objet est un bloc
		else if(test instanceof Bloc){
			Bloc bloc = (Bloc) test;
			id = addBloc(bloc);
		}
		//si l'objet est un batiment
		else if(test instanceof Batiment){
			Batiment batiment = (Batiment) test;
			id = addBatiment(batiment);
		}
		else if(test instanceof Rail){
			Rail rail = (Rail) test;
			id = addRail(rail);
		}
		return id;
	}
	/** ajoute une ville dans les différents managers */
	private int addVille(Ville ville){
		//appel à la base de donnée 
		int id = db.storeVille(ville);
		return id;
	}
	/** ajoute un quartier dans les différents managers */
	private int addQuartier(Quartier quartier){
		//appel à la base de donnée 
		int id = db.storeQuartier(quartier);
		return id;
	}
	/** ajoute un bloc dans les différents managers */
	private int addBloc(Bloc bloc){
		//appel à la base de donnée 
		int id = db.storeBloc(bloc);
		return id;
	}
	/** ajoute un batiment dans les différents managers */
	private int addBatiment(Batiment batiment){
		//appel à la base de donnée 
		int id = db.storeBat(batiment);
		return id;
	}
	/** ajoute un rail dans les différents managers */
	private int addRail(Rail rail){
		//appel à la base de donnée 
		id_rail--;
		return id_rail;
	}
/*---------------------------COMMUNICATION---------------------------------*/
	public Serveur getServeur(){
		return this.com.getServeur();
	}
/*-----------------------------AFFICHAGE-----------------------------------*/
	
	/**Initialise l'affichage dans un nouveau Thread*/
/*	public void makeDisplay(){
		new Thread(display).start();
	}*/
	
	/** cette fonction est appelée à chaque frame*/
/*	public void update() {
		time.updateFPS();
		camera.update();
		//multiverse.logiCycle();
	}*/

	/**donne le temps écoulé entre 2 frames*/
/*	public float getdelta() {
		return time.getDelta();
	}*/
			
/*--------------------------VBOMAP---------------------------------*/
	
	/**Initialise la VboMap*/
/*	public void setupMap() {
		minimap.setupVboMap();
	}
*/
	/**Ajoute une Cell à la VboMap*/
/*	public void addBatToVboMap(Cell cell, float[] color) {
		map.addCellToVboMap(cell, color);
	}

*/
	
	/**déclenche le rendu de la VboMap*/
/*	public void renderMap() {
		if (ParamsGlobals.MAP_RENDER_BATS) map.render();
		if (ParamsGlobals.MAP_RENDER_HEIGHT) height.render();
		if (ParamsGlobals.MAP_RENDER_RAILS) rails.render();
		if (ParamsGlobals.MAP_RENDER_BLOCS) blocs.render();
		if (ParamsGlobals.MAP_RENDER_ROADS) roads.render();
	}
*/	
/*	public void addRailToVboMap(Cell cell, float[] color) {
		rails.addCellToVboMap(cell, color);
	}

	public void addHeightToVboMap(Cell cell, float[] color) {
		height.addCellToVboMap(cell, color);
	}


	public void setupCamera() {
		camera = new Camera ();
	}

	public void addBlocToVboMap(Cell cell, float[] color) {
		blocs.addCellToVboMap(cell, color);	
	
	}

	public void addRoadToVboMap(Cell cell, float[] color) {
		roads.addCellToVboMap(cell, color);			
	}
*/
	/**déclenche la mise en mémoire tampon de la VboMap*/
/*	public void bufferVboMap() {
		height.setHeight();
		blocs.buffer();
		roads.buffer();
		rails.buffer();
		map.buffer();
		height.buffer();
	}

	public void setupMultiverse() {
		multiverse.setup();
		multiverse.buffer();
	}

	public Model getModel(int type) {
		Model m = model.getModel(type);
		return m;
	}
*/
	/**l'affichage est déjà initialisé, on met en place des trucs utilisés par tout le monde (shader et matrices*/
/*	public void setupDisplay() {
		render.setupShaders();
		render.setupMatrices();	
	}
	
	public Old_RenderManager getRenderer(){
		return render;
	}

	public void cleanup() {
		map.cleanup();
		height.cleanup();
		rails.cleanup();
		roads.cleanup();
		multiverse.cleanup();
	}

	public void renderMultiverse() {
		multiverse.render();
	}*/
}

