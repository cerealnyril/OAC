package topographie;

import tools.Identifiants;
import tools.ParamsGlobals;

//import static org.lwjgl.opengl.GL11.*;



public class Cell{
	private int bloc_type, bat_type;
	private int x_cord, y_cord;
	private double z_cord;
	private double score;
	private int rotation;
	private int id_q;
	private int nb_voisins_route, nb_voisins_canaux;
	public Cell(int x, int y){
		this.bloc_type = Identifiants.vide;
		this.bat_type = Identifiants.vide;
		this.x_cord = x;
		this.y_cord = y;
		this.z_cord = -1;
		this.rotation = 0;
		this.nb_voisins_route = 0;
		this.nb_voisins_canaux = 0;
	}
/*-------------------------------ACCESSEURS---------------------------*/
	public int getX(){
		return this.x_cord;
	}
	public int getY(){
		return this.y_cord;
	}
	public double getZ(){
		return this.z_cord;
	}
	public float getCentreX(){
		return (float) (x_cord+0.5);
	}
	public float getCentreY(){
		return (float) (y_cord+0.5);
	}
	public double getScore(){
		return this.score;
	}
	public int getRotation(){
		return this.rotation;
	}
	public int getNbRoutes(){
		return this.nb_voisins_route;
	}
	public int getNbCanaux(){
		return this.nb_voisins_canaux;
	}
/*-------------------------------SETTEURS------------------------------*/
	public void setScore(double s){
		this.score = s;
	}
	/** change la coordonée sur X*/
	public void resetX(int x){
		this.x_cord = x;
	}
	/** change la coordonée sur Y*/
	public void resetY(int y){
		this.y_cord = y;
	}
	/** change la coordonée sur Z */
	public void resetZ(double z){
		if(/*z_cord == -1 &&*/ z != -1){
			this.z_cord = z;
		}
	}
	/** met en place la rotation dans le cas ou c'est une cellule genre route ou frontiere ou canal */
	public void setRotation(int rotation){
		this.rotation = rotation;
	}
	/** associe a un quartier */
	public void setIDQuartier(int id_q){
		this.id_q = id_q;
	}
	/** indique le nombre de voisins qui sont des routes */
	public void setVoisinRoutes(int i){
		this.nb_voisins_route = i;
	}
	/** indique le nombre de voisins qui sont des canaux */
	public void setVoisinCanaux(int i){
		this.nb_voisins_canaux = i;
	}
/*-------------POUR LES BATIMENTS--------------*/
	/** change le type du batiment */
	public void resetBatType(int t){
		this.bat_type = t;
	}
	/** renvois le type du batiment */
	public int getBatType(){
		return this.bat_type;
	}
	/** augmente la hauteur ou le niveau du batiment */
	public void incrementHeight(){
		this.z_cord++;
	}
	/** donne l'identifiant du quartier associé */
	public int getIDQuartier(){
		return this.id_q;
	}
/*---------------POUR LES BLOCS----------------*/
	public void resetBlocType(int t){
		this.bloc_type = t;
	}
	public int getBlocType(){
		return this.bloc_type;
	}
/*-----------------UTILITAIRE-------------------*/
	/** dit si le type est considéré comme libre pour le cas d'un bloc */
	public boolean isFreeBloc(){
		if(bloc_type==Identifiants.roadBloc || bloc_type == Identifiants.vide){
			return true;
		}
		return false;
	}
	/** dit si le type est considéré comme libre pour le cas d'un batiment */
	public boolean isFreeBat(){
		if(bat_type==Identifiants.roadBloc || bloc_type == Identifiants.vide){
			return true;
		}
		return false;
	}
	/** Dit si la cell est un bat */
	public boolean isBat(){
		if (bat_type != -1) return true;
		else return false;
	}
	/** Dit si la celle est un bloc */
	public boolean isBloc(){
		if (bloc_type == -1) return false;
		else if (bat_type == -1 && bloc_type != -1){
			if (bloc_type == Identifiants.roadBloc) return false;
			if (bloc_type == Identifiants.canal) return false;
//			if(bloc_type == Identifiants.frontiereBloc) return false;
			else return true;
		} else return false;
	}
	/** Dit si la cell est une route */
	public boolean isRoad(){
		if (bloc_type == -1) return false;
		else if (bat_type == -1 && bloc_type != -1){
			if (bloc_type == Identifiants.roadBloc) return true;
			else return false;
		} else return false;
	}
	/** Dit si la cell est un canal */
	public boolean isCanal(){
		if (bloc_type == -1) return false;
		else if (bat_type == -1 && bloc_type != -1){
			if (bloc_type == Identifiants.canal) return true;
			else return false;
		} else return false;
	}
	/** Dit si la cell est une frontiere */
	public boolean isFrontiere(){
//		System.out.println("identifiant de bat "+bat_type+", de bloc "+bloc_type+" sachant que l'identifiant de frontiere est "+Identifiants.frontiereBat);
		if (bat_type == Identifiants.frontiereBat){ 
			return true;
		}
		return false;
	}
	/** indique si c'est un noeud potentiel */
	public boolean isNode(){
		if(this.nb_voisins_canaux > 0 || this.nb_voisins_route > 0){
			return true;
		}
		return false;
	}
/*-----------------------------------OPENGL-------------------*/
	/** fonction de test pour verifier si les cellules sont disponibles */
	public void renderDispos(){
		if(this.bloc_type == Identifiants.vide){
			//glColor4f(1f,1f,1f,0.5f);
		}
		//render();
	}
	

	public void renderCanal(){
    	if(this.bloc_type == Identifiants.canal){
    		//glColor4f(0.5f,0,0.5f,0.5f);

    	}
    }
	
	public void renderFrontiere(){
    	if(this.bat_type == Identifiants.frontiereBat){
    		//glColor4f(0.5f,0.5f,0.5f,0.5f);

    	}
    }
	
	public float[] getCellColor4f (){
		float[] color = new float[]{0,0,0,0};
			if (isBat()&&!isBloc()&&!(isCanal()&&!isRoad())){
				if(this.getBatType() == 51){
		    		color[0]=1f;
		    		color[1]=1f;
		    		color[2]=1f;
		    		color[3]=1f;
		    	}
		    	//administration 
		    	if(this.getBatType() == Identifiants.horlogeBat || this.getBatType() == Identifiants.pensionnatBat || 
		    			this.getBatType() == Identifiants.ecoleBat || this.getBatType() == Identifiants.morgueBat ||
		    			this.getBatType() == Identifiants.banqueBat || this.getBatType() == Identifiants.interimBat ||
		    			this.getBatType() == Identifiants.tribunalBat || this.getBatType() == Identifiants.refugeBat ||
		    			this.getBatType() == Identifiants.commissariatBat || this.getBatType() == Identifiants.sanitariumBat ||
		    			this.getBatType() == Identifiants.stationBat || this.getBatType() == Identifiants.mairieBat ||
		    			this.getBatType() == Identifiants.staseBat){
		    		color[0]=0f;
		    		color[1]=1f;
		    		color[2]=0f;
		    		color[3]=1f;
		    	}
		    	//habitation
		    	else if(this.getBatType()== Identifiants.immeubleBat){
		    		color[0]=0f;
		    		color[1]=0f;
		    		color[2]=1f;
		    		color[3]=1f;
		    	}
		    	//production
		    	else if(this.getBatType() == Identifiants.usineBat || this.getBatType() == Identifiants.fermeBat || 
		    			this.getBatType() == Identifiants.centraleBat){
		    		color[0]=1f;
		    		color[1]=0f;
		    		color[2]=0f;
		    		color[3]=1f;
		    	}
		    	//commerce
		    	else if(this.getBatType() == Identifiants.magasinBat){
		    		color[0]=1f;
		    		color[1]=0.5f;
		    		color[2]=0f;
		    		color[3]=1f;
		    	}
		    	//citadelle 
		    	else if(this.getBatType() == Identifiants.citadelle){
		    		color[0]=0f;
		    		color[1]=0f;
		    		color[2]=0f;
		    		color[3]=1f;
		    	}
		    	//decoration
		    	if(this.getBatType() == Identifiants.treeBat || this.getBatType() == Identifiants.statueBat ||
		    			this.getBatType() == Identifiants.placeBat || this.getBatType() == Identifiants.parcBat
		    			){
		    		color[0]=0.74f;
		    		color[1]=0.71f;
		    		color[2]=0.42f;
		    		color[3]=1f;
		    	}
		    	//frontiere
		    	if(this.getBatType() == Identifiants.frontiereBat){
		    		color[0]=0.9f;
		    		color[1]=0.5f;
		    		color[2]=0.9f;
		    		color[3]=1f;
		    	}
			}else if (!isBat()&&isBloc()&&!isCanal()&&!isRoad()){
				if(this.bloc_type == 51){
		    		color[0]=1f;
		    		color[1]=1f;
		    		color[2]=1f;
		    		color[3]=1f;
		    	}
		    	else if(this.bloc_type == Identifiants.vide){
		    		color[0]=0.5f;
		    		color[1]=0.5f;
		    		color[2]=0.5f;
		    		color[3]=1f;
		    	}
		    	else if(this.bloc_type == Identifiants.productionBloc){
		    		color[0]=0.69f;
		    		color[1]=0.13f;
		    		color[2]=0.13f;
		    		color[3]=1f;
		    	}
		    	else if(this.bloc_type == Identifiants.habitationBloc){
		    		color[0]=0f;
		    		color[1]=0f;
		    		color[2]=0.5f;
		    		color[3]=1f;
		    	}
		    	else if(this.bloc_type == Identifiants.commerceBloc){
		    		color[0]=1f;
		    		color[1]=1f;
		    		color[2]=0f;
		    		color[3]=1f;
		    	}
		    	else if(this.bloc_type == Identifiants.admininistrationBloc){
		    		color[0]=0f;
		    		color[1]=0.39f;
		    		color[2]=0f;
		    		color[3]=1f;
		    	}
		    	else if(this.bloc_type == Identifiants.decorationBloc){
		    		color[0]=0.94f;
		    		color[1]=0.9f;
		    		color[2]=0.55f;
		    		color[3]=1f;
		    	}
		    	else if(this.bloc_type == Identifiants.loisirBloc){
		    		color[0]=1f;
		    		color[1]=0.5f;
		    		color[2]=0f;
		    		color[3]=1f;
		    	}
		    	else if(this.bloc_type == Identifiants.border){
		    		color[0]=1f;
		    		color[1]=0f;
		    		color[2]=0f;
		    		color[3]=1f;
		    	}
		    	//citadelle 
		    	else if(this.bloc_type == Identifiants.citadelle){
		    		color[0]=0.8f;
		    		color[1]=0.8f;
		    		color[2]=0.8f;
		    		color[3]=1f;
		    	}
			} else if(!isBat()&&!isBloc()&&isCanal()&&!isRoad()){
		    	if(this.bloc_type == Identifiants.canal){
		    		color[0]=0.5f;
		    		color[1]=0f;
		    		color[2]=0.5f;
		    		color[3]=1f;
		    	}
			} else if(!isBat()&&!isBloc()&&!isCanal()&&isRoad()){
		    	if(this.bloc_type == Identifiants.roadBloc){
		    		color[0]=0.1f;
		    		color[1]=0.1f;
		    		color[2]=0.1f;
		    		color[3]=1f;
		    	}
			}/*if(!isBat()&&!isBloc()&&!isCanal()&&isFrontiere()){
	    		color[0]=0.5f;
	    		color[1]=0.5f;
	    		color[2]=0.5f;
	    		color[3]=1f;
	    	}*/
			
		return color;
	}

	public float[] getHeightCellColor4f() {
		float[] color = new float[]{0,0,0,1f};
		double palier = ParamsGlobals.HEIGHT/3.0;
		float valeur = (float) (z_cord/ParamsGlobals.HEIGHT);
		if(this.z_cord >= 0){
			//nuances de bleu
			if(z_cord < palier){
				float assign = (float) ((valeur*z_cord)/(1*ParamsGlobals.HEIGHT/3f));
				color[2]=assign;
			}
			//nuances de vert
			else if(z_cord < palier*2){
				float assign = (float) ((valeur*z_cord)/(2*ParamsGlobals.HEIGHT/3f));
				color[1]=assign;
			}
			//nuances de rouge
			else{
				float assign = (float) ((valeur*z_cord)/(3*ParamsGlobals.HEIGHT/3f));
				color[0]=assign;
			}
			
		}
		else{
			color[0]=0.5f;
			color[1]=0.5f;
			color[2]=0.5f;
		}
		return color;
	}
}
	