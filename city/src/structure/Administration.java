package structure;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;


import profils.ProfilMetier;
import tools.Identifiants;
import topographie.Cell;
import topographie.Zone;


/** Le quartier d'administration est unique dans la ville */
public class Administration extends Bloc{
	
	private Quartier this_quartier, quartier_haut, quartier_bas, quartier_gauche, quartier_droite;
	private ArrayList<Cell> contigue, non_contigue;
	
	public Administration(int id) {
		super(id);
		listeBatiments = new TreeMap<Integer, Batiment>();
		super.type = Identifiants.admininistrationBloc;
		super.type_txt = "Administration";
		super.MAX_BAT = 18;
		setTaille();
		this.contigue = new ArrayList<Cell>();
		this.non_contigue = new ArrayList<Cell>();
	}
	private void setTaille(){
		int taille = 64;
		super.profondeur = (int) Math.sqrt(taille);
		super.facade = (int) Math.sqrt(taille);
	}
	public void upJour(){
		getMairie().upJour();
		getStation().upJour();
	}
	public void setZoneQuartier(Quartier q){
		this.this_quartier = q;
	}
	public void setHaut(Quartier q){
		this.quartier_haut = q;
	}
	public void setBas(Quartier q){
		this.quartier_bas = q;
	}
	public void setGauche(Quartier q){
		this.quartier_gauche = q;
	}
	public void setDroite(Quartier q){
		this.quartier_droite = q;
	}
	public boolean hasNeighbours(){
		if((quartier_haut != null) || (quartier_bas != null) || (quartier_gauche != null) || (quartier_droite != null)){
			return true;
		}
		return false;
	}
/*-----------------------------------ACCESSEURS-----------------------------*/
	/** renvois le batiment banque */
	private Banque getBanque() {
		ArrayList<Integer> bats = super.getBatimentsFromType(Identifiants.banqueBat);
		if(bats.size() > 0){
			int id = bats.get(0);
			return (Banque) this.listeBatiments.get(id);
		}
		return null;
	}
	/** renvois le batiment refuge pour sans abris */
	private Refuge getRefuge(){
		ArrayList<Integer> bats = super.getBatimentsFromType(Identifiants.refugeBat);
		if(bats.size() > 0){
			int id = bats.get(0);
			return (Refuge) this.listeBatiments.get(id);
		}
		return null;
	}
	/** renvois le batiment gare pour chemins dans le quartier */
	public Station getStation(){
		ArrayList<Integer> bats = super.getBatimentsFromType(Identifiants.stationBat);
		if(bats.size() > 0){
			int id = bats.get(0);
			return (Station) this.listeBatiments.get(id);
		}
		return null;
	}
	/** renvois le batiment mairie pour chemins et frontieres dans le quartier */
	public Mairie getMairie(){
		ArrayList<Integer> bats = super.getBatimentsFromType(Identifiants.mairieBat);
		if(bats.size() > 0){
			int id = bats.get(0);
			return (Mairie) this.listeBatiments.get(id);
		}
		return null;
	}
	/** renvois le batiment stase*/
	public Stase getStase(){
		ArrayList<Integer> bats = super.getBatimentsFromType(Identifiants.staseBat);
		if(bats.size() > 0){
			int id = bats.get(0);
			return (Stase) this.listeBatiments.get(id);
		}
		return null;
	}
	/** renvois le type d'un batiment administratif */
	public int getAdminType(int id_bat){
		Batiment bat = (Batiment) this.listeBatiments.get(id_bat);
		return bat.getType();
	}
/*--------------------------------RLE - LINKED STRUCTURES----------------------------------*/
	public void updateLinkedStructures(boolean to_x){
		Zone zh = null, zb = null, zg = null, zd = null;
		if(quartier_haut != null){
			zh = quartier_haut.getZone();
		}
		if(quartier_bas != null){
			zb = quartier_bas.getZone();
		}
		if(quartier_gauche != null){
			zg = quartier_gauche.getZone();
		}
		if(quartier_droite != null){
			zd = quartier_droite.getZone();
		}
		//mis en place des rails entre les stations 
		Station station = getStation();
		//si c'est la premiere fois alors on a pas encore toutes les zones
		if(!station.asZone()){
			station.setCurrentZones(this_quartier.getZone(), zh, zb, zg, zd);
		}
		station.updateGraph();
		station.updateRails();
		//mis en place des limites et des rocades des quartiers dans la mairie
		Mairie mairie = getMairie();
		if(!mairie.asZone()){
			mairie.setCurrentZones(this_quartier.getZone(), zh, zb, zg, zd);
		}
		//extraction des adjacences
		extractAdjacence();
		mairie.updateFrontieres(this.non_contigue, to_x);
		mairie.updateRocades(this.contigue, to_x);
	}
	

	/** Identifie quelles cellules de limite sont contigues */
	private void extractAdjacence(){
		this.contigue.clear();
		this.non_contigue.clear();
		ArrayList<Cell> check = new ArrayList<Cell>();
		if(quartier_haut != null){
			check.clear();
			//si au final le voisin couvre tout pourquoi s'emmerder c'est que tous sera contigeant 
			if((this_quartier.getStartX() >= quartier_haut.getStartX()) && ((this_quartier.getEndX()) <= quartier_haut.getEndX())){
				this.contigue.addAll(this_quartier.getZone().getLimitHaut());
			}
			else{
				check.addAll(quartier_haut.getZone().getLimitBas());
				trierContigue(this_quartier.getZone().getLimitHaut(), check, getMaxStartX(quartier_haut), getMaxEndX(quartier_haut), true);
			}
		}
		else{
			non_contigue.addAll(this_quartier.getZone().getLimitHaut());
		}
		if(quartier_bas != null){
			check.clear();
			//si au final le voisin couvre tout pourquoi s'emmerder c'est que tous sera contigeant 
			if((this_quartier.getStartX() >= quartier_bas.getStartX()) && ((this_quartier.getEndX()) <= quartier_bas.getEndX())){
				this.contigue.addAll(this_quartier.getZone().getLimitBas());
			}
			else{
				check.addAll(quartier_bas.getZone().getLimitHaut());
				trierContigue(this_quartier.getZone().getLimitBas(), check, getMaxStartX(quartier_bas), getMaxEndX(quartier_bas), true);
			}
		}
		else{
			non_contigue.addAll(this_quartier.getZone().getLimitBas());
		}
		if(quartier_gauche != null){
			check.clear();
			//si au final le voisin couvre tout pourquoi s'emmerder c'est que tous sera contigeant 
			if((this_quartier.getStartY() >= quartier_gauche.getStartY()) && ((this_quartier.getEndY()) <= quartier_gauche.getEndY())){
				this.contigue.addAll(this_quartier.getZone().getLimitGauche());
			}
			else{
				check.addAll(quartier_gauche.getZone().getLimitDroite());
				trierContigue(this_quartier.getZone().getLimitGauche(), check, getMaxStartY(quartier_gauche), getMaxEndY(quartier_gauche), false);
			}
		}
		else{
			non_contigue.addAll(this_quartier.getZone().getLimitGauche());
		}
		if(quartier_droite != null){
			check.clear();
			//si au final le voisin couvre tout pourquoi s'emmerder c'est que tous sera contigeant 
			if((this_quartier.getStartY() >= quartier_droite.getStartY()) && ((this_quartier.getEndY()) <= quartier_droite.getEndY())){
				this.contigue.addAll(this_quartier.getZone().getLimitDroite());
			}
			else{
				check.addAll(quartier_droite.getZone().getLimitGauche());
				trierContigue(this_quartier.getZone().getLimitDroite(), check, getMaxStartY(quartier_droite), getMaxEndY(quartier_droite), false);
			}
		}
		else{
			non_contigue.addAll(this_quartier.getZone().getLimitDroite());
		}
	}
	/** Retourne le plus petit x d'un voisin soit en se basant sur le voisin soit en allant regarder à gauche si il a lui meme un truc*/
	private float getMaxStartX(Quartier quart){
		float start_x = quart.getStartX();
		if(quart.getVoisinGauche() != null){
			start_x = quart.getVoisinGauche().getStartX();
		}
		return start_x;
	}
	/** Retourne le plus grand x possible d'une voisin, soit en se basant sur le bvoisin lui meme soit en allant regarder à droite de ce voisin*/
	private float getMaxEndX(Quartier quart){
		float end_x = quart.getEndX();
		if(quart.getVoisinDroite() != null){
			end_x = quart.getVoisinDroite().getEndX();
		}
		return end_x;
	}
	private float getMaxStartY(Quartier quart){
		float start_y = quart.getStartY();
		if(quart.getVoisinBas() != null){
			start_y = quart.getVoisinBas().getStartY();
		}
		return start_y;
	}
	private float getMaxEndY(Quartier quart){
		float end_y = quart.getStartY();
		if(quart.getVoisinHaut() != null){
			end_y = quart.getVoisinHaut().getEndY();
		}
		return end_y;
	}
	
	private void trierContigue(ArrayList<Cell> source, ArrayList<Cell> cible, float start, float end, boolean on_x){
		boolean next = false;
		Iterator<Cell> iter = source.iterator();
		while(iter.hasNext()){
			Cell cell = iter.next();
			Iterator<Cell> iter_voisin;
			//test des voisins de droite
			iter_voisin = cible.iterator();
			while(iter_voisin.hasNext() && !next){
				Cell voisin = iter_voisin.next();
				next = isVoisin(cell, voisin);
			}
			if(next){
				cible.remove(cell);
				contigue.add(cell);
			}
			else{
				//si on test les limites sur X
				if(on_x){
					//System.out.println("on x "+start+", "+end+" pour cellule "+cell.getX());
					if((cell.getX() < start) || (cell.getX() > end)){
						non_contigue.add(cell);
					}
				}
				//sinon on test les limites sur Y
				else{
					if((cell.getY() < start) || (cell.getY() > end)){
						non_contigue.add(cell);
					}
				}
				//non_contigue.add(cell);
			}
			next = false;
		}
	}
	/** test si les cellules sont voisines */
	private boolean isVoisin(Cell cell, Cell voisin){
		if(cell.getX() == voisin.getX() && (cell.getY() == voisin.getY()+1 || cell.getY() == voisin.getY()-1 || cell.getY() == voisin.getY())){
			return true;
		}
		else if(cell.getY() == voisin.getY() && (cell.getX() == voisin.getX()+1 || cell.getX() == voisin.getX()-1 || cell.getX() == voisin.getX())){
			return true;
		}
		return false;
	}
/*-------------------------------------JUSTICE---------------------------------------------*/
	/** retourne le jugement */
	public int getJugement(double peine){
		return getTribunal().getJugement(peine);
	}
	/** renvois le batiment tribunal */
	private Tribunal getTribunal() {
		int id = super.getBatimentsFromType(Identifiants.tribunalBat).get(0);
		return (Tribunal) this.listeBatiments.get(id);
	}
	/** retourne le commissariat */
	public Commissariat getCommissariat(){
		int id = super.getBatimentsFromType(Identifiants.commissariatBat).get(0);
		return (Commissariat) this.listeBatiments.get(id);
	}
/*--------------------------------------MORGUE---------------------------------------------*/
	/** renvois le batiment morgue */
	private Morgue getMorgue(){
		int id = super.getBatimentsFromType(Identifiants.morgueBat).get(0);
		return (Morgue) this.listeBatiments.get(id);
	}
	/** ajout d'un corps dans la morgue */
	public void addCorps(int id_pers){
		getMorgue().addCorpse(id_pers);
	}
	/** mise à jour des corps dans la morgue */
	public void updateCorpses(ArrayList<Integer> corps){
		getMorgue().resetCorpses(corps);
	}
	/** renvois les corps de la morgue */
	public ArrayList<Integer> getCorpses(){
		return getMorgue().getCorpses();
	}
/*--------------------------------------ECOLE----------------------------------------------*/
	/** renvois les enfants de l'internat */
	public ArrayList<Integer> getEnfants(){
		return getPensionnat().getEnfants();
	}
	/** renvois les eleves de l'internat */
	public ArrayList<Integer> getEleves(){
		return getEcole().getEleves();
	}
	/** l'orphelinat */
	private Pensionnat getPensionnat(){
		int id = super.getBatimentsFromType(Identifiants.pensionnatBat).get(0);
		return (Pensionnat) this.listeBatiments.get(id);
	}
	/** les écoles */
	private Ecole getEcole(){
		int id = super.getBatimentsFromType(Identifiants.ecoleBat).get(0);
		return (Ecole) this.listeBatiments.get(id);
	}
	/** passage d'examen et augmentation du niveau d'étude */
	public TreeMap<Integer, Double> graduation(TreeMap<Integer, Double> qis){
		return getEcole().graduation(qis);
	}
	/** mise à jour des enfants dans l'école */
	public void updateEcole(){
		ArrayList<Integer> non_s = getNonScholarises();
		Iterator<Integer> iter = non_s.iterator();
		boolean build = true;
		Ecole eco = getEcole();
		while(iter.hasNext() && build){
			int id_enfant = iter.next();
			eco.addEleve(id_enfant);
			build = autorisation_construction(eco.getFrais());
		}
	}
	public void addEleve(int id){
		Ecole ec = (Ecole) listeBatiments.get(getEcole());
		int prev_classe_nb = ec.getClasses();
		ec.addEleve(id);
		if(ec.getClasses() != prev_classe_nb){
//			Transmission.updateBat(zone);
		}
	}
	/** recupere tout les enfants de l'internat et tout les enfant de l'ecole et renvois
	 * la différence soit les enfants non scholarisés 
	 * @return : arraylist des identifiants enfants non scholarisés*/
	public ArrayList<Integer> getNonScholarises(){
		ArrayList<Integer> result = new ArrayList<Integer>();
		ArrayList<Integer> eleves = this.getEcole().getEleves();
		ArrayList<Integer> enfants = this.getPensionnat().getEnfants();
		if(enfants.size() > 0 && enfants.size() != eleves.size()){
			result.addAll(enfants);
			result.removeAll(eleves);
		}
		return result;
	}
	/** gère toutes les actions en rapport avec le passage à l'age adulte */
	public void updateAdult(int id_pers){
		getPensionnat().sortieEnfants(id_pers);
		getEcole().removeEleve(id_pers);
		getInterim().addDemandeur(id_pers);
		getRefuge().addHobo(id_pers);
	}
	/** ajout d'un enfant dans l'internat */
	public void ajoutEnfant(int nouveau){
		getPensionnat().ajoutEnfant(nouveau);
	}
/*-----------------------------------EMPLOIS---------------------------------------------*/
	/** renvois le batiment interim */
	private Interim getInterim(){
		int id = super.getBatimentsFromType(Identifiants.interimBat).get(0);
		return (Interim) this.listeBatiments.get(id);
	}
	/** rajoute un travail dans l'agence d'interim */
	public void ajoutJobs(ArrayList<ProfilMetier> jobs){
		getInterim().ajoutJobs(jobs);
	}
	/** suppression d'un demandeur d'emplois */
	public void removeDemandeur(int id_pers){
		getInterim().removeDemandeur(id_pers);
	}
	/** renvois les demandeurs d'emplois */
	public ArrayList<Integer> getDemandeurs(){
		return getInterim().getDemandeurs();
	}
	/** renvois les offres d'emplois */
	public ArrayList<ProfilMetier> getEmplois(int pop){
		ArrayList<ProfilMetier> result = new ArrayList<ProfilMetier>();
		/** pour l'ecole */
		Ecole eco = getEcole();
		if(eco != null){
			ProfilMetier pm = eco.getProfilMetier();
			if(pm.getNombrePostes() > 0){
				pm.setBloc(this.getID());
				result.add(pm);
			}
		}
		/** pour le tribunal */
		Tribunal trib = getTribunal();
		if(trib != null){
			ProfilMetier pm = trib.getProfilMetier();
			if(pm.getNombrePostes() > 0){
				pm.setBloc(this.getID());
				result.add(pm);
			}
		}
		/** pour le commissariat */
		Commissariat com = getCommissariat();
		if(com != null){
			ProfilMetier pm = com.getProfilMetier(pop);
			if(pm.getNombrePostes() > 0){
				pm.setBloc(this.getID());
				result.add(pm);
			}
		}
		return result;
	}
	/** versement des salaires administratifs */
	public TreeMap<Integer, Double> versementSalaires() {
		TreeMap<Integer, Double> salaires = new TreeMap<Integer, Double>();
		/** on paie les profs */
		salaires.putAll(getEcole().versementSalaire());
		/** on paie les juristes */
		salaires.putAll(getTribunal().versementSalaire());
		/** on paie les policiers */
		salaires.putAll(getCommissariat().versementSalaire());
		return salaires;
	}
	/** envois des candidats en entretiens d'embauche dans les batiment administratifs et retourne
	 * les niveaux hierarchiques attribués*/
	public void recrutement(ArrayList<Integer> candidats, int id_bat){
		if(candidats.size() > 0){
			/** si ce sont des professeurs */
			Batiment bat = (Batiment) listeBatiments.get(id_bat);
			if(bat.getType() == Identifiants.ecoleBat){
				Ecole eco = (Ecole) bat;
				eco.recrutement(candidats);
			}
			/** si ils travaillent au tribunal */
			else if(bat.getType() == Identifiants.tribunalBat){
				Tribunal trib = (Tribunal) bat;
				trib.recrutement(candidats);
			}
			/** si ce sont des flics */
			else if(bat.getType() == Identifiants.commissariatBat){
				Commissariat com = (Commissariat) bat;
				com.recrutement(candidats);
			}
		}
	}
	/** vire quelqu'un sans indemnité de licenciement. Quand il meurt en général. Ce sont des fonctionnaires
	 * ils ont la sécurité de l'emplois */
	@SuppressWarnings("unchecked")
	public boolean licenciement(int fired){
		Iterator<Integer> iter = listeBatiments.keySet().iterator();
		boolean stop = false;
		while(iter.hasNext() && !stop){
			int id_bat = iter.next();
			Batiment bat = (Batiment) listeBatiments.get(id_bat);
			/** si c'est un professeur */
			if(bat.getType() == Identifiants.ecoleBat){
				Ecole eco = (Ecole) bat;
				stop = eco.licenciement(fired);
			}
			/** si c'est un juriste */
			else if(bat.getType() == Identifiants.tribunalBat){
				Tribunal trib = (Tribunal) bat;
				stop = trib.licenciement(fired);
			}
		}
		return stop;
	}
/*----------------------------------GESTION BANQUE-------------------------------------------*/
	/** met à jour la cagnotte dans la banque */
	public void updateCagnotte(double ajout){
		getBanque().updateCagnotte(ajout);
	}
	/** verifie si l'on est autorisé à construire */
	public boolean autorisation_construction(double prix){
		return getBanque().autorisation_construction(prix);
	}
	/** retourne la cagnotte de la banque */
	public double getCagnotte(){
		return getBanque().getCagnotte();
	}
	/** mise à jour du soilent dans la banque */
	public void updateSoilent(double soilent){
		getBanque().updateSoilent(soilent);
	}
	/** renvois la quantité de soilent dans la banque */
	public double getSoilent(){
		return getBanque().getSoilent();
	}
/*-----------------------------------------GESTION DU REFUGE--------------------------------------*/
	/** mise à jour des sans abris */
	public void updateHobos(ArrayList<Integer> h){
		getRefuge().resetHobos(h);
	}
	/** retire un sans abris du refuge */
	public void removeHobo(int pers){
		getRefuge().removeHobo(pers);
	}
	/** renvois le nombre de sans abris dans le batiment de refuge */
	public int getHobosNb(){
		return getRefuge().getHobos().size();
	}
	/** renvois les sans abris */
	public ArrayList<Integer> getHobos(){
		return getRefuge().getHobos();
	}
	/** selection du metier */
	public TreeMap<Integer, TreeMap<Integer, ArrayList<Integer>>> selectionMetier(TreeMap<Integer, ArrayList<String>> sans_emplois_id){
		return getInterim().selectionMetier(sans_emplois_id);
	}
}
