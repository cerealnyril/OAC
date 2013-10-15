package structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import com.badlogic.gdx.math.Vector2;


import monde.Analyse;
import monde.Interaction;

import profils.ProfilMetier;

import social.Faction;
import social.Personne;
import tools.Identifiants;
import tools.ParamsGlobals;
import tools.SelectionNom;
import tools.Utils;
import topographie.Cell;
import topographie.Zone;

public class Quartier {
	/** liste de tout les blocs de batiments de la ville */
	private TreeMap<Integer, Bloc> listeBlocs;
	/** accesseur plus rapide avec les blocs rangés par type */
	private TreeMap<Integer, ArrayList<Integer>> blocsType;
	/** nom de la ville */
	private String nom;
	/** taux d'imposition */
	private static double IMPOSITION = 0.5;
	private boolean build;
	private Faction faction;
	private int identifiant;
	/** permet l'interaction des gens avec des composants du quartier */
	private Interaction interaction;
	//pour la cartographie
	private AffiniteBlocs affinite;
	private Croissance croissance;
	/** stock les cellules pour chaque bloc classé par identifiant de coordonées */
	private static int ROAD_WIDTH = 2;
	private int expanded;
	private Zone zone;
	private int id_ville;
	private Quartier voisin_gauche, voisin_droite, voisin_haut, voisin_bas;
	private Vector2 translation;
	
	/** constructeur. Par defaut le quartier a un nom */
	public Quartier(int pos, int id_v) {
		this.id_ville = id_v;
		this.listeBlocs = new TreeMap<Integer, Bloc>();
		this.blocsType = new TreeMap<Integer, ArrayList<Integer>>();
		this.nom = SelectionNom.getNomQuartier(pos);
		this.build = true;
		this.faction = new Faction();
		this.identifiant = -1;
		if(pos != -1){
			this.identifiant = ParamsGlobals.MANAGER.registerObject(this);
		}
		//pour la cartographie
		zone = new Zone();
		zone.setIdQuartier(this.identifiant);
		affinite = new AffiniteBlocs();
		expanded = 0;
		translation = new Vector2();
		translation.x = 0;
		translation.y = 0;
	}
/*-----------------------------------------ACCESSEURS-----------------------------------------------*/
/*-- General au quartier */
	/** renvois le nom du quartier */
	public String getNom(){
		return this.nom;
	}
	/** renvois la zone du quartier */
	public Zone getZone(){
		return this.zone;
	}
	/** renvois le quartier voisin de haut */
	public Quartier getVoisinHaut(){
		return this.voisin_haut;
	}
	/** renvois le quartier voisin du bas */
	public Quartier getVoisinBas(){
		return this.voisin_bas;
	}
	/** renvois le quartier voisin de gauche */
	public Quartier getVoisinGauche(){
		return this.voisin_gauche;
	}
	/** renvois le quartier voisin de droite */
	public Quartier getVoisinDroite(){
		return this.voisin_droite;
	}
	/** renvois l'identifiant de la ville associée */
	public int getIDVille(){
		return this.id_ville;
	}
	/** renvois les stocks de soilent du quartier */
	public double getSoilent(){
		return getAdministration().getSoilent();
	}
	/** renvois la cagnotte du quartier */
	public double getCagnotte(){
		Administration admin = this.getAdministration();
		return admin.getCagnotte();
	}
	/** renvois l'interaction pour l'acces au personnes */
	public Interaction getInteraction(){
		return this.interaction;
	}
	/** renvois l'identifiant du quartier */
	public int getID(){
		return this.identifiant;
	}
	/** renvois la faction du quartier */
	public Faction getFaction(){
		return this.faction;
	}
	/** renvois la taille sur x du quartier */
	public int getTailleX(){
		return zone.getTailleX();
	}
	/** renvois la taille sur y du quartier */
	public int getTailleY(){
		return zone.getTailleY();
	}
	/** renvois le centre x du quartier */
	public float getCentreX(){
		return zone.getCentreX();
	}
	/** renvois le centre y du quartier */
	public float getCentreY(){
		return zone.getCentreY();
	}
	/** identifiants des blocs */
	public ArrayList<Integer> getBlocsID(){
		return new ArrayList<Integer>(listeBlocs.keySet());
	}
	/** Retourne un bloc à partie de son id */
	public Bloc getBloc(int id){
		return this.listeBlocs.get(id);
	}
	/** Retourne le start X du quartier */
	public float getStartX(){
		return this.zone.getStartX();
	}
	/** Retourne le start Y du quartier */
	public float getStartY(){
		return this.zone.getStartY();
	}
	/** Retourne le start X du quartier */
	public float getEndX(){
		return this.zone.getEndX();
	}
	/** Retourne le end Y du quartier */
	public float getEndY(){
		return this.zone.getEndY();
	}
	/** Retourne les routes du quartier */
	public ArrayList<Cell> getRoads(){
		return zone.getRoads();
	}
	/** Retourne tous les RLE qui sont des objets à durée de vie limitée à 1 jour encodés sur la longueur et la largeur */
	public ArrayList<RLE> getRLEs(){
		ArrayList<RLE> results = new ArrayList<RLE>();
		if(getAdministration() != null){
			//pour la mairie qui contient 
			Mairie mairie = getAdministration().getMairie();
			if(mairie != null){
				results.addAll(mairie.getRLEs());
			}
		}
		return results;
	}
	/** On a mis les rails dans un truc a part donc c'est un peut comme les RLE */
	public ArrayList<Rail> getRails(){
		ArrayList<Rail> results = new ArrayList<Rail>();
		if(getAdministration() != null){
			//pour la station qui contient les chemins de fer
			Station station = getAdministration().getStation();
			if(station != null){
				results.addAll(station.getRails());
			}
		}
		return results;
	}
/*-- Centree sur les batiments des blocs */
	/** renvois les parametres du bloc au sens general */
	public int getBlocCoord(int id){
		Bloc bloc = this.listeBlocs.get(id);
		return bloc.getID();
	}
	/** renvois le type du bloc au sens general */
	public int getBlocType(int id){
		Bloc bloc = this.listeBlocs.get(id);
		return bloc.getType();
	}
/*-------------------------------------CHARGEMENT QUARTIER------------------------------------------*/
	public void setVoisinHaut(Quartier q){
		this.voisin_haut = q;
	}
	public void setVoisinBas(Quartier q){
		this.voisin_bas = q;
	}
	public void setVoisinGauche(Quartier q){
		this.voisin_gauche = q;
	}
	public void setVoisinDroite(Quartier q){
		this.voisin_droite = q;
	}
	/** créé la classe de croissance */
	public void setCroissance(Croissance crois){
		this.croissance = crois;
	}
	/** initialisation de l'objet interaction */
	public void setInteraction(Interaction inter){
		this.interaction = inter;
	}
	/** fonction de mise à jour de la liste des blocs du quartier */
	public void ajoutBloc(Bloc bloc){
		/** ajout dans le quartier */
		ArrayList<Integer> blocs = new ArrayList<Integer>();
		if(blocsType.containsKey(bloc.getType())){
			blocs.addAll(blocsType.get(bloc.getType()));
		}
		if(!blocs.contains(bloc.getID())){
			blocs.add(bloc.getID());
		}
		
		/** fonction de cartographie */
		if(bloc.getType() == Identifiants.admininistrationBloc){
			initBloc(bloc);
		}
		else{
			placeBloc(bloc);
		}
		listeBlocs.put(bloc.getID(), bloc);
		blocsType.put(bloc.getType(), blocs);
		zone.refreshTaille();
		ParamsGlobals.MANAGER.updateObject(bloc);
	}
	/** fonction de mise à jour de la liste des batiments d'un bloc */
/*--------------------------------------GESTION ECONOMIQUE--------------------------------------*/	
	/** La cagnotte de la ville est une part d'impots qui est prélevée sur les magasins et sur les ventes.
	 * Elle sert à financer la construction de nouveaux batiments et à payer les salaires de l'administration
	 * qui couvre également la police. Elle peut etre prélevée par les factions 
	 * @param coms */
	private void updateCagnotte(){
		Iterator<Integer> iter_com = getCommercesIDs().iterator();
		while(iter_com.hasNext()){
			int id_com = iter_com.next();
			Commerce com = getCommerce(id_com);
			getAdministration().updateCagnotte(com.impots(IMPOSITION));
		}
	}
	/** transfert du soilent fabriqué depuis les usines */
	private void updateSoilent(){
		Iterator<Integer> iter_prod = getProductionsIDs().iterator();
		while(iter_prod.hasNext()){
			int id_prod = iter_prod.next();
			Production prod = getProduction(id_prod);
			getAdministration().updateSoilent(prod.transfertSoilent());
		}
	}
	/** transfert les corps dans les usines de fabrication de soilent */
	private void transfertBodies(){
		/** récupère tout les corps de la morgue et met à jour */
		if(getAdministration().getCorpses().size() > 0){
			ArrayList<Integer> tmp = getAdministration().getCorpses();
			TreeMap<Integer, Double> corps = new TreeMap<Integer, Double>();
			Iterator<Integer> iter = tmp.iterator();
			while(iter.hasNext()){
				int id = iter.next();
				corps.put(id, interaction.getSoilentValue(id));
			}
			Iterator<Integer> iter_prod = getProductionsIDs().iterator();
			while(iter_prod.hasNext()){
				int id_prod = iter_prod.next();
				Production prod = getProduction(id_prod);
				ArrayList<Integer> left = prod.updateSource(corps);
				if(left.size() > 0){
					corps.keySet().removeAll(left);
				}
				interaction.removeIDs(new ArrayList<Integer>(corps.keySet()));
				getAdministration().updateCorpses(prod.updateSource(corps));
			}
		}
	}
	/** embauche les gens 
	 * @param coms */
	private void embauche() {
		Administration admin = getAdministration();
		/** récupère les sans emplois et les données utiles qui y sont associées */
		TreeMap<Integer, ArrayList<String>> demandeurs = interaction.getSpecificPersEmbauche(admin.getDemandeurs());
		/** lance la fonction de calcul de match entre les personnes et les postes */
		TreeMap<Integer, TreeMap<Integer, ArrayList<Integer>>> nouveaux_employes = admin.selectionMetier(demandeurs);
		Iterator<Integer> iter_bloc = nouveaux_employes.keySet().iterator();
		while(iter_bloc.hasNext()){
			int id_bloc = iter_bloc.next();
			/** récupération des employés pour l'itération */
			TreeMap<Integer, ArrayList<Integer>> employes = nouveaux_employes.get(id_bloc);
			/** si c'est un commerce iterer dans les magasins pour attribuer les nouveaux employés */
			if(getCommercesIDs().contains(id_bloc)){
				recrutementCommerces(employes, getCommerce(id_bloc));
			}
			/** si c'est une administration */
			else if(id_bloc == admin.getID()){
				recrutementAdministrations(employes, admin);
			}
			/** si c'est un batiment de loisir */
			//TODO : loisirs
		}
	}
	/** fonction spécifique de recrutement des employés de commerce 
	 * @param coms */
	private void recrutementCommerces(TreeMap<Integer, ArrayList<Integer>> employes_bloc, Commerce commerce){
		/** iteration sur les blocs de commerce */
		Iterator<Integer> iter_bat = employes_bloc.keySet().iterator();
		while(iter_bat.hasNext()){
			int id_bat = iter_bat.next();
			ArrayList<Integer> employes = employes_bloc.get(id_bat);
			commerce.recrutement(employes, id_bat);
			/** pour tout les gens initialisation de la profession */
			Iterator<Integer> iter_pers = employes.iterator();
			while(iter_pers.hasNext()){
				int id_pers = iter_pers.next();
				interaction.setProfession(id_pers, commerce.getMagType(id_bat));
			}
		}
	}
	/** fonction spécifique de recrutement des employés de commerce 
	 * @param coms */
	private void recrutementAdministrations(TreeMap<Integer, ArrayList<Integer>> employes_bloc, Administration admin){
		/** iteration sur les blocs de commerce */
		Iterator<Integer> iter_bat = employes_bloc.keySet().iterator();
		while(iter_bat.hasNext()){
			int id_bat = iter_bat.next();
			ArrayList<Integer> employes = employes_bloc.get(id_bat);
			admin.recrutement(employes, id_bat);
			/** pour tout les gens initialisation de la profession */
			Iterator<Integer> iter_pers = employes.iterator();
			while(iter_pers.hasNext()){
				int id_pers = iter_pers.next();
				interaction.setProfession(id_pers, admin.getAdminType(id_bat));
			}
		}
	}
	/** quand il y'a une campagne de recrutement envois les demandeurs dans les différents magasins */
	private void majPostes(){
		Administration admin = getAdministration();
		ArrayList<ProfilMetier> emplois = new ArrayList<ProfilMetier>();
		/** pour tout les commerces */
		Iterator<Integer> iter_com = getCommercesIDs().iterator();
		while(iter_com.hasNext()){
			int id_com = iter_com.next();
			Commerce com = getCommerce(id_com);
			emplois.addAll(com.getEmplois());
		}
		/** pour l'administration */
		emplois.addAll(getAdministration().getEmplois(interaction.getPopSize()));
		/** transmission à l'agence d'interim */
		admin.ajoutJobs(emplois);
	}
	/** gestion des salaires des gens 
	 * @param coms 
	 * @param admin 
	 * @param loss */
	private void versementSalaires(){
		/** salaires des commerces */
		TreeMap<Integer, Double> salaires = new TreeMap<Integer, Double>();
		Iterator<Integer> iter_coms = getCommercesIDs().iterator();
		while(iter_coms.hasNext()){
			int id_com = iter_coms.next();
			Commerce com = getCommerce(id_com);
			salaires.putAll(com.versementSalaires());
		}
		/** salaires des loisirs */
		Iterator<Integer> iter_loss = getLoisirsIDs().iterator();
		while(iter_loss.hasNext()){
			int id_loss = iter_loss.next();
			Loisir los = getLoisir(id_loss);
			salaires.putAll(los.versementSalaires());
		}
		/** salaires de l'administration */
		salaires.putAll(getAdministration().versementSalaires());
		/** versement des salaires */
		interaction.versementSalaires(salaires);
	}
/*--------------------------------ACCESSEURS ET GROUPEMENT BLOCS---------------------------------------*/	
	/** pour le moment on vas chercher dans tout les blocs le nb de batiments
	 * dedans et on somme */
	public int getNombreBlocs(){
		int resultats = 0;
		for(int i = 0; i < listeBlocs.size(); i++){
			resultats += listeBlocs.get(0).getListeBatiments().size();
		}
		return resultats;
	}

	/** retourne le nombre d'habitants placés */
	public int getNombrePopulation(Administration admin) {
		return (interaction.getPopulation().size()-admin.getHobosNb());
	}	
	/** accesseur de l'ensemble des blocs du quartier tout type confondu */
	public TreeMap<Integer, Bloc> getListBlocs(){
		return this.listeBlocs;
	}
	/** fonction qui retourne un bloc d'habitation */
	public Habitation getHabitation(int id){
		return (Habitation) listeBlocs.get(id);
	}
	/** fonction qui retourne un bloc de commerce */
	public Commerce getCommerce(int id){
		return (Commerce) listeBlocs.get(id);
	}
	/** fonction qui retourne un bloc de production */
	public Production getProduction(int id){
		return (Production) listeBlocs.get(id);
	}
	/** fonction qui retourne un bloc de decoration */
	public Decoration getDecoration(int id){
		return (Decoration) listeBlocs.get(id);
	}
	/** fonction qui retourne un bloc d'administration*/
	public Administration getAdministration(){
		if(getAdministrationID() != -1){
			return (Administration) listeBlocs.get(getAdministrationID());
		}
		return null;
	}
	/** fonction qui retourne un bloc de loisirs */
	public Loisir getLoisir(int id){
		return (Loisir) listeBlocs.get(id);
	}
	public ArrayList<Integer> getHabitationsIDs(){
		int type = Identifiants.habitationBloc;
		if(blocsType.get(type) != null){
			return blocsType.get(type);
		}
		return new ArrayList<Integer>();
	}
	public ArrayList<Integer> getCommercesIDs(){
		int type = Identifiants.commerceBloc;
		if(blocsType.get(type) != null){
			return blocsType.get(type);
		}
		return new ArrayList<Integer>();
	}
	/** fonction qui ne retourne que des blocs de production parmis la liste */
	public ArrayList<Integer> getProductionsIDs(){
		int type = Identifiants.productionBloc;
		if(blocsType.get(type) != null){
			return blocsType.get(type);
		}
		return new ArrayList<Integer>();
	}
	/** fonction qui ne retourne que des blocs de loisirs parmis la liste */
	public ArrayList<Integer> getLoisirsIDs(){
		int type = Identifiants.loisirBloc;
		if(blocsType.get(type) != null){
			return blocsType.get(type);
		}
		return new ArrayList<Integer>();
	}
	/** fonction qui ne retourne que des blocs d'administration parmis la liste */
	public int getAdministrationID(){
		int type = Identifiants.admininistrationBloc;
		if(blocsType.get(type) != null){
			return blocsType.get(type).get(0);
		}
		return -1;
	}
	/** fonction qui ne retourne que les identifiants des blocs de decoration parmis la liste */
	public ArrayList<Integer> getDecorationsIDs(){
		int type = Identifiants.decorationBloc;
		if(blocsType.get(type) != null){
			return blocsType.get(type);
		}
		return new ArrayList<Integer>();
	}
/*---------------------------------------ANALYSE-------------------------------------------*/	
	/** récupération depuis les habitants de la consommation en nourriture globale 
	 * sur la ville */
	private int getConsoFood(){
		return interaction.getConsommationNourritureTotale();
	}
	/** récupération depuis le quartier de production de la production alimentaire globale */
	private int getProdFood(){
		int prod_food = 1;
		Iterator<Integer> iter_prods = getProductionsIDs().iterator();
		while(iter_prods.hasNext()){
			int id_prod = iter_prods.next();
			Production prod = getProduction(id_prod);
			prod_food += prod.getFood();
			
		}
		return prod_food;
	}
	/** récupération depuis les quartiers d'habitations, de commerce et d'administration de la consommation
	 * energétique globale au niveau du quartier */
	private int getConsoEnergie(){
		int conso_energie = 0;
		Iterator<Integer> iter = listeBlocs.keySet().iterator();
		while(iter.hasNext()){
			 int id = iter.next();
			 Bloc bloc = (Bloc) listeBlocs.get(id);
			 conso_energie += bloc.getConsommationEnergie();
		}
		return conso_energie;
	}
	/** Récupération depuis les quartiers de production de la production énérgétique*/
	private int getProdEnergie(){
		int prod_energie = 0;
		Iterator<Integer> iter_prods = getProductionsIDs().iterator();
		while(iter_prods.hasNext()){
			int id_prod = iter_prods.next();
			Production prod = getProduction(id_prod);
			prod_energie += prod.getEnergie();			
		}
		return prod_energie;
	}
	/** renvois la capacité de process globale des usines de soilent */
	private int getSoilentProdCap(){
		int cap = 0;
		Iterator<Integer> iter_prods = getProductionsIDs().iterator();
		while(iter_prods.hasNext()){
			int id_prod = iter_prods.next();
			Production prod = getProduction(id_prod);
			cap += prod.getUsineCap();			
		}
		return cap;
	}
	/** fonction de construction de la map de décision 
	 * @param admin 
	 * @param loss 
	 * @param prods 
	 * @param coms 
	 * @param habs */
	private TreeMap<Integer, Double> collectRatiosStructure(){
		TreeMap<Integer, Double> result = new TreeMap<Integer, Double>();
		/** collecte */
		Administration admin = getAdministration();
		double ratio_food = Analyse.ratioFarm(getConsoFood(), getProdFood());
		double ratio_energie = Analyse.ratioCentrale(getConsoEnergie(), getProdEnergie());
		double ratio_usine = Analyse.ratioUsine(admin.getCorpses().size(), getSoilentProdCap());
		double ratio_habitation = Analyse.ratioHabitation(interaction.getPopSize(), admin.getHobosNb());
		double ratio_education = Analyse.ratioEducation(admin.getNonScholarises().size(), admin.getEnfants().size());
		double ratio_decoration = Analyse.rationDecoration(listeBlocs.size(), getDecorationsIDs().size());
		/** insertion */
		result.put(0, ratio_energie);
		result.put(1, ratio_food);
		result.put(2, ratio_usine);
		result.put(3, ratio_habitation);
		result.put(4, ratio_education);
		result.put(5, ratio_decoration);
		return result;
	}
	private TreeMap<Integer, Double> collectRatiosSocial(){
		TreeMap<Integer, Double> result = new TreeMap<Integer, Double>();
		/** collecte */
		Administration admin = getAdministration();
		double ratio_peineS = Analyse.ratioPeinalSoilent(admin.getSoilent(), admin.getHobosNb());
		double ratio_peineM = Analyse.ratioPeinalCagnotte(admin.getCagnotte(), build);
		/** insertion */
		result.put(1, ratio_peineS);
		result.put(2, ratio_peineM);
		return result;
	}
/*---------------------------------------CONSTRUCTION-------------------------------------------*/	

	private void constructionManager(Vector<Integer> order){
		Iterator<Integer> iter = order.iterator();
		System.out.println("- Debut des constructions ");
		clearLinkedStructures();
		while(iter.hasNext()){
			int type = iter.next();
			switch(type){
			case 0 :
				System.out.println("-- Construit centrale");
				buildCentrale();
			break;
			case 1 :
				System.out.println("-- Construit ferme");
				buildFerme();
			break;
			case 2:
				System.out.println("-- Construit usine");
				buildUsine();
			break;
			case 3 :
				System.out.println("-- Construit habitation");
				buildHabitation();
			break;
			case 4 :
				 System.out.println("-- Besoin d'ecoles");
				 updateEcole();
			break;
			case 5 : 
				System.out.println("-- Construit décoration");
				buildDeco();
			break;
			}
		}
		System.out.println("- fin des constructions ");
		updateLinkedStructures();
//		Transmission.executeUpdateMinimap();
	}
	
	/** gestion de la production energétique */
	private void buildCentrale(){
		/** si on a necessité la création d'une nouvelle centrale */
		int additional_centrale = 0;
		Iterator<Integer> iter_prods = getProductionsIDs().iterator();
		while(iter_prods.hasNext() && build){
			int id_prod = iter_prods.next();
			Production prod = getProduction(id_prod);
			if(!prod.buildCentrale()){
				additional_centrale++;
				build = getAdministration().autorisation_construction(prod.frais());
			}
		}
		/** de combien de fermes et de centrales avons nous besoin dans un nouveau bloc de production */
		if(additional_centrale > 0){
			Production new_prod = new Production(this.identifiant);
			/** lancement brutal de la poussée d'une nouveau bloc de production avec les carac manquantes */
			ajoutBloc(new_prod);
			new_prod.createCentrale(additional_centrale);
		}
	}
	/** gestion de la production alimentaire */
	private void buildFerme(){
		/** si on a necessité la création d'une nouvelle ferme */
		int additional_ferme = 0;
		Iterator<Integer> iter_prods = getProductionsIDs().iterator();
		while(iter_prods.hasNext() && build){
			int id_prod = iter_prods.next();
			Production prod = getProduction(id_prod);
			if(!prod.buildFarm()){
				additional_ferme++;
				build = getAdministration().autorisation_construction(prod.frais());
			}
		}
		/** de combien de fermes avons nous besoin dans un nouveau bloc de production */
		if(additional_ferme > 0){
			Production new_prod = new Production(this.identifiant);
			/** lancement brutal de la poussée d'une nouveau bloc de production avec les carac manquantes */
			ajoutBloc(new_prod);
			new_prod.createFerme(additional_ferme);
		}
	}
	/** gestion de la production de soilent 
	 * @param admin */
	private void buildUsine(){
		/** si on a necessité la création d'une nouvelle ferme */
		int additional_usine = 0;
		Iterator<Integer> iter_prods = getProductionsIDs().iterator();
		while(iter_prods.hasNext() && build){
			int id_prod = iter_prods.next();
			Production prod = getProduction(id_prod);
			if(!prod.buildUsine()){
				additional_usine++;
				build = getAdministration().autorisation_construction(prod.frais());
			}
		}
		/** de combien d'usines avons nous besoin dans un nouveau bloc de production */
		if(additional_usine > 0){
			Production new_prod = new Production(this.identifiant);
			/** lancement brutal de la poussée d'une nouveau bloc de production avec les carac manquantes */
			ajoutBloc(new_prod);
			new_prod.createFerme(additional_usine);
		}
	}
	/** gestion des surplus de population */
	private void buildHabitation(){
		Administration admin = getAdministration();
		ArrayList<Integer> hobos = new ArrayList<Integer>();
		hobos.addAll(classificationRichesse(admin.getHobos()));
		/** on commence par redistribuer la population dans les blocs existants si c'est possible */
		Iterator<Integer> iter = getHabitationsIDs().iterator();
		while(iter.hasNext() && build){
			Habitation hab = getHabitation(iter.next());
			admin.updateHobos(hab.migration(hobos));
			build = admin.autorisation_construction(hab.frais());
		}
		/** sinon on créé un nouveau bloc */
		while((admin.getHobosNb() > 0) && build){
			Habitation new_hab = new Habitation(this.identifiant);
			ajoutBloc(new_hab);
			admin.updateHobos(new_hab.migration(admin.getHobos()));
			build = admin.autorisation_construction(new_hab.frais());
		}
	}
	
	/** rajoute des enfants dans l'ecole. Créé des classes si besoin est */
	private void updateEcole(){
		getAdministration().updateEcole();
	}
	/** rajoute du mobilier urbain */
	private void buildDeco() {
		Decoration deco = new Decoration(this.identifiant);
		ajoutBloc(deco);
		deco.initMU();
	}
/*-----------------------------------GESTION DEMOGRAPHIQUE-------------------------------------*/
	/** Vas calculer pour chaque habitant tout batiment confondu ceux qui survivent et tue les
	 * autres pour transférer leurs corps dans les usines de reconversion*/
	public void gestionMort(int id_pers){
		Administration admin = getAdministration();
		admin.removeDemandeur(id_pers);
		admin.removeHobo(id_pers);
		/** supression des habitants dans les batiments */
		Iterator<Integer> iter_bat = getHabitationsIDs().iterator();
		boolean stop = false;
		while(iter_bat.hasNext() && !stop){
			int id_bat = iter_bat.next();
			Habitation hab = getHabitation(id_bat);
			if(hab.getListeGens().containsKey(id_pers)){
				hab.removePersonne(id_pers);
				stop = true;
			}

		}
		licencieDead(id_pers);
		/** prend l'argent et le met dans la banque de la ville */
		admin.updateCagnotte(interaction.getRichesse(id_pers));
		/** ajout à la morgue*/
		admin.addCorps(id_pers);
	}
	
	/** pour éviter le travail des enfants on ne place que les plus de 18 ans comme
	 * sans emplois */
	public void updateAdult(int id_pers){
		getAdministration().updateAdult(id_pers);
	}
	/** supprime les morts des employés ou les fonctionnaires
	 * @param admin */
	private void licencieDead(int dead){
		Iterator<Integer> iter_com = getCommercesIDs().iterator();
		boolean stop = false;
		/** tour dans les commerces */
		while(iter_com.hasNext() && !stop){
			int id_com = iter_com.next();
			Commerce com = getCommerce(id_com);
			int id_bat = com.licenciement(dead);
			if(id_bat != -1){
				stop = true;
				Vector<Integer> orga = classificationQI(new ArrayList<Integer>(com.getEmployees(id_bat).keySet()));
				com.promotionInterne(orga, id_bat);
			}
		}
		/** tour des loisirs */
		Iterator<Integer> iter_loss = getLoisirsIDs().iterator();
		while(iter_loss.hasNext() && !stop){
			int id_loss = iter_loss.next();
			Loisir loss = getLoisir(id_loss);
			int id_bat = loss.licenciement((dead));
			if(id_bat != -1){
				stop = true;
				Vector<Integer> orga = classificationQI(new ArrayList<Integer>(loss.getEmployees(id_bat).keySet()));
				loss.promotionInterne(orga, id_bat);
			}
		}
		/** tour dans l'administration */
		if(!stop){
			getAdministration().licenciement(dead);
		}
	}
	/** organisation de la liste en fonction du QI */
	public Vector<Integer> classificationQI(ArrayList<Integer> personnel){
		Vector<Integer> result = new Vector<Integer>();
		TreeMap<Double, Integer> tmp = new TreeMap<Double, Integer>();
		Iterator<Integer> iter = personnel.iterator();
		while(iter.hasNext()){
			int id = iter.next();
			double valeure = interaction.getIQ(id);
			while(tmp.containsKey(valeure)){
				valeure += 1.0/1000000;
			}
			tmp.put(valeure, id);
		}
		Iterator<Double> iter_tmp = tmp.keySet().iterator();
		while(iter_tmp.hasNext()){
			result.add(tmp.get(iter_tmp.next()));
		}
		Collections.reverse(result);
		return result;
		
	}
	/** organisation de la liste en fonction de la richesse */
	public Vector<Integer> classificationRichesse(ArrayList<Integer> hobos){
		Vector<Integer> result = new Vector<Integer>();
		TreeMap<Double, Integer> tmp = new TreeMap<Double, Integer>();
		Iterator<Integer> iter = hobos.iterator();
		while(iter.hasNext()){
			int id = iter.next();
			double valeure = interaction.getRichesse(id);
			while(tmp.containsKey(valeure)){
				valeure += 1.0/1000000;
			}
			tmp.put(valeure, id);
		}
		Iterator<Double> iter_tmp = tmp.keySet().iterator();
		while(iter_tmp.hasNext()){
			result.add(tmp.get(iter_tmp.next()));
		}
		Collections.reverse(result);
		return result;
		
	}
	/** augmentation du niveau d'étude */
	private void updateConnaissance() {
		Administration admin = getAdministration();
		TreeMap<Integer, Double> grad = admin.graduation(interaction.getSpecificIQs(admin.getEleves()));
		interaction.updateEducation(grad);
	}
/*----------------------------------------CYCLES----------------------------------------------*/
	/** gère le quartier par la construction de nouveau blocs */
	public void cycle(){
		// chargement des types de blocs de la ville
		getAdministration().upJour();
		System.out.println("-----------------VERIFICATION ETATS-------------------");
		System.out.println("cagnotte "+getAdministration().getCagnotte());
		System.out.println("soilent "+getAdministration().getSoilent());
		System.out.println("Enfants dans le pensionnat "+getAdministration().getEnfants());
		System.out.println("Enfants à l'ecole "+getAdministration().getEleves());
		System.out.println("Population "+interaction.getPopulation().keySet());
		@SuppressWarnings({ "rawtypes", "unchecked" })
		TreeMap<Integer, Personne> tmp = new TreeMap(interaction.getPopulation());
		tmp.keySet().removeAll(getAdministration().getEleves());
		System.out.println("Population Adulte "+tmp.keySet());
		System.out.println("Demandeurs d'emplois "+getAdministration().getDemandeurs());
		System.out.println("blocs de production "+getProductionsIDs().size()+" ids "+getProductionsIDs());
		System.out.println("blocs d'habitation "+getHabitationsIDs().size()+" ids "+getHabitationsIDs());
		System.out.println("------------------------------------------------------");
		// mise à jour de la cagnotte de la ville
		System.out.println(".");
		updateCagnotte();
		// mise à jour du soilent depuis les corps
		System.out.println("..");
		updateSoilent();
		System.out.println("...");
		transfertBodies();
		// gestion de la connaissance
		System.out.println("....");
		updateConnaissance();
		// récupération des données pour la politique d'embauche
		System.out.println(".....");
		majPostes();
		System.out.println("......");
		embauche();
		// gestion des emplois
		System.out.println(".......");
		versementSalaires();
		// recupération des données pour la construction du quartier
		System.out.println("........");
		TreeMap<Integer, Double> ratios = collectRatiosStructure();
		// decision constructions
		System.out.println(".........");
		Vector<Integer> order = faction.getDecisionConstruction(ratios);
		// lancement constructions
		System.out.println("..........");
		constructionManager(order);
		System.out.println("===========");
//		decisionJustice();
	}
/*---------------------------------------CARTOGRAPHIE--------------------------------------------*/
	/*===============================GESTION DES INFRANCHISSABLES=================================*/
	/** ce type de quartier est completement à part il contient un seul bloc completement vide
	 * et bloqué */
	public void setCitadelle(){
		Zone z = new Zone();
		z.setTailleX(ParamsGlobals.GAP);
		z.setTailleY(ParamsGlobals.GAP);
		z.setCentre(0, 0);
		ArrayList<Cell> new_cells = new ArrayList<Cell>();
		for(int y= z.getStartY(); y < z.getEndY(); y++){
	    	for(int x=z.getStartX(); x < z.getEndX(); x++){
    			Cell cell = new Cell(x, y);
    			cell.resetBlocType(Identifiants.citadelle);
    			zone.removeAvailable(cell);
    			zone.indexation(cell);
    			z.indexation(cell);
    			new_cells.add(cell);
	    	}
    	}
		Bloc bloc = new Bloc(this.identifiant);
		bloc.setZone(z);
		this.listeBlocs.put(this.identifiant, bloc);
		bloc.initCitadelle();
	}
//TODO:placement des navmesh : donner les centres des cases disponibles ou vides qui sont des intersections	
/*=====================================GESTION DES BLOCS=======================================*/
/*------------------------------MODIFIFCATION DE LA GRILLE-------------------------------------*/
	/** rajoute le bloc administratif initial */
	private void initBloc(Bloc bloc){
		Zone z = new Zone();
		z.setIdQuartier(this.identifiant);
		z.setTailleX(bloc.getFacade()+ROAD_WIDTH);
		z.setTailleY(bloc.getProfondeur()+ROAD_WIDTH);
		z.setCentre(zone.getCentreX(), zone.getCentreY());
		z.setIdQuartier(identifiant);
		z.setIdBloc(bloc.getID());
		ArrayList<Cell> new_cells = new ArrayList<Cell>();
		for(int y= z.getStartY(); y < z.getEndY(); y++){
	    	for(int x=z.getStartX(); x < z.getEndX(); x++){
	    		Cell cell = zone.getCellRelief(x, y);
    			cell.resetBlocType(bloc.getType());
    			zone.removeAvailable(cell);
    			zone.indexation(cell);
    			z.ajoutAvailable(cell);
    			z.indexation(cell);
    			new_cells.add(cell);
	    	}
    	}
		bloc.setZone(z);
		listeBlocs.put(bloc.getID(), bloc);
		zone.initAvailables(new_cells, bloc.getID(), z);
	}

	/** fonction de création des cellules de la grille */
	private void placeBloc(Bloc bloc){
		int taille_x = bloc.getFacade()+ROAD_WIDTH;
		int taille_y = bloc.getProfondeur()+ROAD_WIDTH;
		Forme forme = getForme(taille_x, taille_y, bloc.getType(), bloc.getID());
		Zone z = new Zone();
		z.setTailleX(forme.taille_x);
		z.setTailleY(forme.taille_y);
		z.setIdQuartier(this.identifiant);
		z.setIdBloc(bloc.getID());
		/** mis en place des cellules de contenu */
		Iterator<Integer> iter_facade = forme.getCellsBlock().keySet().iterator();
		while(iter_facade.hasNext()){
			int facade = iter_facade.next();
			Iterator<Integer> iter_profondeur = forme.getCellsBlock().get(facade).iterator();
			while(iter_profondeur.hasNext()){
				int profondeur = iter_profondeur.next();
				initContentCell(forme, facade, profondeur, z);
			}
		}
		/** mis en place des cellules de route */
		iter_facade = forme.getRoadBlock().keySet().iterator();
		while(iter_facade.hasNext()){
			int facade = iter_facade.next();
			Iterator<Integer> iter_profondeur = forme.getRoadBlock().get(facade).iterator();
			while(iter_profondeur.hasNext()){
				int profondeur = iter_profondeur.next();
				initRoadCell(forme, facade, profondeur, z);
			}
		}
		/** mis en place des cellules de canaux */
		boolean route_ajout = false;
//		int qte = 0;
//		double random = 0;
		iter_facade = forme.getCanalBlock().keySet().iterator();
		while(iter_facade.hasNext()){
			int facade = iter_facade.next();
			Iterator<Integer> iter_profondeur = forme.getCanalBlock().get(facade).iterator();
			while(iter_profondeur.hasNext()){
				int profondeur = iter_profondeur.next();
				route_ajout = initCanalCell(forme, facade, profondeur, z, route_ajout);
			}
/*			if(qte < 4 && random > Math.random()){
				route_ajout = false;
				qte++;
				random = 0;
			}
			else{
				random+=0.1;
			}*/
		}
		/** mis en place des cellules disponibles */
		iter_facade = forme.getBorderBlock().keySet().iterator();
		boolean canal_ajout = false;
		while(iter_facade.hasNext()){
			int facade = iter_facade.next();
			Iterator<Integer> iter_profondeur = forme.getBorderBlock().get(facade).iterator();
			while(iter_profondeur.hasNext()){
				int profondeur = iter_profondeur.next();
				canal_ajout = initBorderCell(forme, facade, profondeur, z, canal_ajout);
			}
		}
		bloc.setZone(z);
		listeBlocs.put(bloc.getID(), bloc);
		
		/** positionne les cellules de routes */
		this.zone.setIntersectionAndRotation(0);
		/** positionne les cellules de chemin */
//		this.zone.setIntersectionAndRotation(1);
		/** positionne les cellules de canaux */
		this.zone.setIntersectionAndRotation(2);
		
	}
	/** on vérifie si la cellule qu'on occupe etait une cellule dite disponible.
	 * si c'est le cas alors elle est transformée en route. Dans les cas ou la cellule n'existe pas
	 * alors on en créé une nouvelle 
	 */
	private boolean initCanalCell(Forme forme, int x, int y, Zone z, boolean route_ajout){
		/** on vérifie d'abord si la cellule est en mémoire, sinon on la créé  */
		if(!forme.facadeX()){
			int tmp = y;
			y = x;
			x = tmp;
		}
		Cell cell = zone.getCellRelief(x, y);
		if(!route_ajout){
			Cell haut = zone.getHaut(cell);
			Cell bas = zone.getBas(cell);
			Cell gauche = zone.getGauche(cell);
			Cell droite = zone.getDroite(cell);
			if(haut != null && bas != null && ((haut.isRoad() && bas.isRoad()) || (haut.isFreeBloc() && bas.isFreeBloc()))){
				zone.ajoutRoute(cell);
				route_ajout = true;
			}
			else if(gauche != null && droite != null && ((gauche.isRoad() && droite.isRoad()) || (gauche.isFreeBloc() && droite.isFreeBloc()))){
				zone.ajoutRoute(cell);
				route_ajout = true;
			}
			else{
				zone.ajoutCanal(cell);
			}
		}
		else{
			zone.ajoutCanal(cell);
		}
		zone.indexation(cell);
		z.indexation(cell);
		return route_ajout;
	}
	/** on vérifie si la cellule qu'on occupe etait une cellule dite disponible.
	 * si c'est le cas alors elle est transformée en route. Dans les cas ou la cellule n'existe pas
	 * alors on en créé une nouvelle 
	 */
	private void initRoadCell(Forme forme, int x, int y, Zone z){
		/** on vérifie d'abord si la cellule est en mémoire, sinon on la créé  */
		if(!forme.facadeX()){
			int tmp = y;
			y = x;
			x = tmp;
		}
		Cell cell = zone.getCellRelief(x, y);
		zone.ajoutRoute(cell);
		zone.indexation(cell);
		z.indexation(cell);
	}
	/** on vérifie si la cellule qu'on occupe etait une cellule dite disponible.
	 * si c'est le cas alors elle est transformée en route. Dans les cas ou la cellule n'existe pas
	 * alors on en créé une nouvelle 
	 * @return 
	 */
	private boolean initBorderCell(Forme forme, int x, int y, Zone z, boolean canal_ajout){
		/** on vérifie d'abord si la cellule est en mémoire, sinon on la créé  */
		if(!forme.facadeX()){
			int tmp = y;
			y = x;
			x = tmp;
		}
		Cell cell = zone.getCellRelief(x, y);
		zone.ajoutAvailable(cell);
		Cell test = zone.getCell(x, y);
		if(test != null && cell.isFreeBloc()){
			zone.removeAvailable(cell);
			/** test pour savoir si on rajoute un canal */
			if(!canal_ajout){
				Cell haut = zone.getHaut(cell);
				Cell bas = zone.getBas(cell);
				Cell gauche = zone.getGauche(cell);
				Cell droite = zone.getDroite(cell);
				if(haut != null && bas != null && haut.isCanal() && bas.isCanal()){
					zone.ajoutCanal(cell);
					canal_ajout = true;
				}
				else if(gauche != null && droite != null && gauche.isCanal() && droite.isCanal()){
					zone.ajoutCanal(cell);
					canal_ajout = true;
				}
				else{
					zone.ajoutRoute(cell);
				}
			}
			else{
				zone.ajoutRoute(cell);
			}
		}
		zone.indexation(cell);
		z.indexation(cell);
		return canal_ajout;
	}
	/** on vérifie si la cellule qu'on occupe etait une cellule dite disponible.
	 * si c'est le cas alors elle est transformée en route. Dans les cas ou la cellule n'existe pas
	 * alors on en créé une nouvelle 
	 */
	private void initContentCell(Forme forme, int x, int y, Zone z){
		/** on vérifie d'abord si la cellule est en mémoire, sinon on la créé  */
		if(!forme.facadeX()){
			int tmp = y;
			y = x;
			x = tmp;
		}
		Cell cell = zone.getCellRelief(x, y);
		cell.resetBlocType(forme.getType());
		zone.removeAvailable(cell);
		z.ajoutAvailable(cell);
		zone.indexation(cell);
		z.indexation(cell);
	}
/*-----------------------------------ACCESSEURS------------------------------------*/
	/** retourne toutes les cellules blocs */
	public ArrayList<Cell> getAllCells(){
		return zone.getCells();
	}
	/** retourne les cellules d'altitude */
	public ArrayList<Cell> getHeightCells(){
		return zone.getHeightCells();
	}
	/** retourne les cellules de canaux */
	public ArrayList<Cell> getCanaux(){
		return zone.getCanaux();
	}
	/** retourne les cellules libres à partir desquels on placera les nouveaux blocs */
	public ArrayList<Cell> getFreeCells(){
		return zone.getAvailables();
	}
/*----------------------------------------FORME------------------------------*/
	/** a partir d'une cellulue candidate on créé une forme qui nous dis de combien
	 * il faut reculer ou avancer sur x et y par rapport à cette cellule*/
	private class Forme{
		private int taille_x, taille_y;
		private boolean x_valid;
		private int type;
		private TreeMap<Integer, ArrayList<Integer>> cellsBloc, borderBloc, canalBloc, roadBloc;
		public Forme(int x, 
				TreeMap<Integer, ArrayList<Integer>> cBloc, 
				TreeMap<Integer, ArrayList<Integer>> bBloc, 
				TreeMap<Integer, ArrayList<Integer>> canBloc,
				TreeMap<Integer, ArrayList<Integer>> rBloc,
				int t, int x_size, int y_size){
			this.cellsBloc = new TreeMap<Integer, ArrayList<Integer>>();
			this.x_valid = false;
			if(x != 0){
				this.x_valid = true;
			}
			this.cellsBloc = cBloc;
			this.borderBloc = bBloc;
			this.canalBloc = canBloc;
			this.roadBloc = rBloc;
			transfertCellsInBloc();
			this.type = t;
			this.taille_x = x_size;
			this.taille_y = y_size;
		}
		/** fonction qui transfert une partie des cellules de contenu vers les bordures */
		private void transfertCellsInBloc(){
			boolean canal = false;
			if(canalBloc.size() > 0){
				canal = true;
			}
			TreeMap<Integer, ArrayList<Integer>> new_cellsBloc = new TreeMap<Integer, ArrayList<Integer>>();
			Iterator<Integer> iter = cellsBloc.keySet().iterator();
			int start = 0;
			int stop = cellsBloc.size()-1;
			while(iter.hasNext()){
				int clef  = iter.next();
				ArrayList<Integer> border_to_add = new ArrayList<Integer>();
				ArrayList<Integer> canal_to_add = new ArrayList<Integer>();
				ArrayList<Integer> road_to_add = new ArrayList<Integer>();
				ArrayList<Integer> tmp = cellsBloc.get(clef);
				if(tmp != null){
					border_to_add.add(tmp.get(0));
					border_to_add.add(tmp.get(tmp.size()-1));
					/** si il y'a des canaux */
					if(canal){
//						if(start == 1 || start == stop){
							canal_to_add.add(tmp.get(1));
							canal_to_add.add(tmp.get(tmp.size()-2));
//						}
//						else if(start == 2 || start == stop-1){
							road_to_add.add(tmp.get(2));
							road_to_add.add(tmp.get(tmp.size()-3));
//						}
					}
					tmp.removeAll(border_to_add);
					tmp.removeAll(canal_to_add);
					tmp.removeAll(road_to_add);
					if(tmp.size() > 0){
						new_cellsBloc.put(clef, tmp);
					}
					if(border_to_add.size() > 0){
						borderBloc.put(clef, border_to_add);
					}
					if(canal_to_add.size() > 0){
						canalBloc.put(clef, canal_to_add);
					}
					if(road_to_add.size() > 0){
						roadBloc.put(clef, road_to_add);
					}
				}
				start++;
			}
			cellsBloc = new_cellsBloc;
		}
		public boolean facadeX(){
			return this.x_valid;
		}
		public TreeMap<Integer, ArrayList<Integer>> getCellsBlock(){
			return this.cellsBloc;
		}
		public TreeMap<Integer, ArrayList<Integer>> getBorderBlock(){
			return this.borderBloc;
		}
		/** Dans le cas ou il y'a un canal il y'a une suite de cellules qui seront attribuées au canal */
		public TreeMap<Integer, ArrayList<Integer>> getCanalBlock(){
			return this.canalBloc;
		}
		/** Dans le cas ou il y'a un canal il y'a un premier tour de cellules assurées d'etre des routes */
		public TreeMap<Integer, ArrayList<Integer>> getRoadBlock(){
			return this.roadBloc;
		}
		public int getType(){
			return this.type;
		}
	}
/*----------------------------------------------------------------------------*/
	/** regarde parmis tout les candidats celui qui est le plus approprié */
	private Cell selectCandidatBloc(int x_size, int y_size, int type){
		TreeMap<Double, Cell> selected = new TreeMap<Double, Cell>();
		Iterator<Cell> iter = zone.getAvailables().iterator();
		while(iter.hasNext()){
			boolean valid = false;
			double score = 0.0;
			Cell cell = iter.next();
			int size_x = x_size;
			int size_y = y_size;
			/** test si le bloc vas etre dans une zone ou l'on vas placer des canaux par la suite */
			if(zone.lowEnought(cell)){
				size_x+=4;
				size_y+=4;
			}
			/** dit combien de cases sont occupées sur les axes */
			int direction_x = getXDirectionBloc(cell.getX(), cell.getY());
			int direction_y = getYDirectionBloc(cell.getX(), cell.getY());
			/** on met en place les parametres pour la promenade */
			int x_inc = 0;
			int y_inc = 0;
			if(Math.abs(direction_x) > Math.abs(direction_y)){
				x_inc = 1;
				if(direction_x < 0){
					x_inc = -1;
				}
			}
			else{
				y_inc = 1;
				if(direction_y < 0){
					y_inc = -1;
				}
			}
			/** si la facade est le long de l'axe y et la profondeure sur l'axe x */
			if(Math.abs(x_inc) > Math.abs(y_inc)){
				/** on prépare la facade */
				int start = cell.getY()- Utils.doubleToInt(Utils.moitie(size_y));
				int end = (start+size_y);
				/** on test en profondeur le long de chaques points de la façade */
				while(start < end){
					double score_pass = scoreXPath(cell.getX(), start, size_x, x_inc, 0, type);
					if(score_pass != -1){
						start++;
						score += score_pass;
						valid = true;
					}
					else{
						start = end;
						valid = false;
					}
				}
			}
			/** si la facade est le long de l'axe x et la profondeure sur l'axe y */
			else{
				/** on prépare la facade */
				int start = cell.getX()- Utils.doubleToInt(Utils.moitie(size_x));
				int end = (start+size_x);
				/** on test en profondeur le long de chaques points de la façade */
				while(start < end){
					double score_pass = scoreYPath(start, cell.getY(), size_y, y_inc, 0, type);
					if(score_pass != -1){
						start++;
						score += score_pass;
						valid = true;
					}
					else{
						start = end;
						valid = false;
					}
				}
			}
			/** si dispo et qu'en plus c'est plus oocupé que la normale on prend celle là */
			if(valid){
				selected.put(score, cell);
			}
		}
		Cell result = null;
		if(selected.size() > 0){
			result = selected.get(selected.lastKey());
			result.setScore(selected.lastKey());
		}
		return result;
	}
	/** extrait la forme du bloc */
	private Forme getForme(int x_size, int y_size, int type, int id_bloc){
		Forme forme = null;
		boolean canal = false;
		Cell cell = null;
		while(cell == null){
			cell = selectCandidatBloc(x_size,y_size, type);
			if(x_size != y_size){
				int tmp_x = y_size;
				int tmp_y = x_size;
				Cell tmp = selectCandidatBloc(tmp_x, tmp_y, type);
				if(tmp != null && cell != null){
					if(tmp.getScore() > cell.getScore()){
						cell = tmp;
						x_size = tmp_x;
						y_size = tmp_y;
					}
				}
			}
			if(cell == null){
				expand();
			}
		}
		/** test si le bloc vas etre dans une zone ou l'on vas placer des canaux par la suite */
		if(zone.lowEnought(cell)){
			canal = true;
			x_size+=4;
			y_size+=4;
		}
		TreeMap<Integer, ArrayList<Integer>> content_blocs = new TreeMap<Integer, ArrayList<Integer>>();
		TreeMap<Integer, ArrayList<Integer>> border_blocs = new TreeMap<Integer, ArrayList<Integer>>();
		TreeMap<Integer, ArrayList<Integer>> canal_blocs = new TreeMap<Integer, ArrayList<Integer>>();
		TreeMap<Integer, ArrayList<Integer>> road_blocs = new TreeMap<Integer, ArrayList<Integer>>();
		/** on choisi la direction qui nous interesse */
		int direction_x = getXDirectionBloc(cell.getX(), cell.getY());
		int direction_y = getYDirectionBloc(cell.getX(), cell.getY());
		/** on met en place les parametres pour la promenade */
		int x_inc = 0;
		int y_inc = 0;
		if(Math.abs(direction_x) > Math.abs(direction_y)){
			x_inc = 1;
			if(direction_x < 0){
				x_inc = -1;
			}
		}
		else{
			y_inc = 1;
			if(direction_y < 0){
				y_inc = -1;
			}
		}
		/** si la facade est le long de l'axe y et la profondeure sur l'axe x */
		if(Math.abs(x_inc) > Math.abs(y_inc)){
			/** on prépare la facade */
			int start = cell.getY()- Utils.doubleToInt(Utils.moitie(y_size));
			int end = (start+y_size);
			int init = start;
			/** on test en profondeur le long de chaques points de la façade */
			while(start < end){
				ArrayList<Integer> tmp = getXPathBloc(cell.getX(), start, new ArrayList<Integer>(), x_size, x_inc);
				/** si il n'y a pas de canal */
				if(!canal){
					if(start == init || start == (end-1)){
						border_blocs.put(start, tmp);
					}
					else{
						content_blocs.put(start, tmp);
					}
				}
				/** si il y'a canal */
				else{
					if(start == init || start == (end-1)){
						border_blocs.put(start, tmp);
					}
					else if(start == (init+1) || start == (end-2)){
						ArrayList<Integer> tmp2 = new ArrayList<Integer>();
						tmp2.add(tmp.get(0));
						tmp2.add(tmp.get(tmp.size()-1));
						tmp.removeAll(tmp2);
						border_blocs.put(start, tmp2);
						canal_blocs.put(start, tmp);
					}
					else if(start == (init+2) || start == (end-3)){
						ArrayList<Integer> tmp3 = new ArrayList<Integer>();
						tmp3.add(tmp.get(0));
						tmp3.add(tmp.get(tmp.size()-1));
						border_blocs.put(start, tmp3);
						
						ArrayList<Integer> tmp2 = new ArrayList<Integer>();
						tmp2.add(tmp.get(1));
						tmp2.add(tmp.get(tmp.size()-2));
						canal_blocs.put(start, tmp2);
						
						tmp.removeAll(tmp3);
						tmp.removeAll(tmp2);
						
						road_blocs.put(start, tmp);
					}
					else{
						content_blocs.put(start, tmp);
					}
				}
				start++;
			}
		}
		/** si la facade est le long de l'axe x et la profondeure sur l'axe y */
		else{
			/** on prépare la facade */
			int start = cell.getX()- Utils.doubleToInt(Utils.moitie(x_size));
			int end = (start+x_size);
			int init = start;
			/** on test en profondeur le long de chaques points de la façade */
			while(start < end){	
				ArrayList<Integer> tmp = getYPathBloc(start, cell.getY(), new ArrayList<Integer>(), y_size, y_inc);
				/** si il n'y a pas de canal */
				if(!canal){
					if(start == init || start == (end-1)){
						border_blocs.put(start, tmp);
					}
					else{
						content_blocs.put(start, tmp);
					}
				}
				/** si il y'a un canal */
				else{
					if(start == init || start == (end-1)){
						border_blocs.put(start, tmp);
					}
					else if(start == (init+1) || start == (end-2)){
						ArrayList<Integer> tmp2 = new ArrayList<Integer>();
						tmp2.add(tmp.get(0));
						tmp2.add(tmp.get(tmp.size()-1));
						tmp.removeAll(tmp2);
						border_blocs.put(start, tmp2);
						canal_blocs.put(start, tmp);
					}
					else if(start == (init+2) || start == (end-3)){
						ArrayList<Integer> tmp3 = new ArrayList<Integer>();
						tmp3.add(tmp.get(0));
						tmp3.add(tmp.get(tmp.size()-1));
						border_blocs.put(start, tmp3);
						
						ArrayList<Integer> tmp2 = new ArrayList<Integer>();
						tmp2.add(tmp.get(1));
						tmp2.add(tmp.get(tmp.size()-2));
						canal_blocs.put(start, tmp2);
						
						tmp.removeAll(tmp3);
						tmp.removeAll(tmp2);
						road_blocs.put(start, tmp);
					}
					else{
						content_blocs.put(start, tmp);
					}
				}
				start++;
			}
		}
		/** finalement on créé la forme */
		forme = new Forme(Math.abs(y_inc), content_blocs, border_blocs, canal_blocs, road_blocs, type, x_size, y_size);
		return forme;
	}
	/** retourne l'inverse de la direction la plus occupée sur X*/
	private int getXDirectionBloc(int x, int y){
		int droite = countDroiteBloc(x, y, 1);
		int gauche = countGaucheBloc(x, y, 1);
		if(droite > gauche){
			return -droite;
		}
		if(gauche > droite){
			return gauche;
		}
		if(gauche == droite && gauche != 0){
			return gauche;
		}
		return 0;
	}
	/** retourne l'inverse de la direction la plus occupée sur X*/
	private int getYDirectionBloc(int x, int y){
		int haut = countHautBloc(x, y, 1);
		int bas = countBasBloc(x, y, 1);
		if(haut > bas){
			return -haut;
		}
		if(bas > haut){
			return bas;
		}
		if(bas == haut && bas != 0){
			return bas;
		}
		return 0;
	}
	/** retourne le compte de cellules occupées à droite  */
	private int countDroiteBloc(int x, int y, int total){
		Cell voisin = zone.getCell(x+total, y);
		if(voisin != null && !voisin.isFreeBloc()){
			total++;
			return countDroiteBloc(x, y, total);
		}
		return total;
	}
	/** retourne le compte de cellules occupées à gauche  */
	private int countGaucheBloc(int x, int y, int total){
		Cell voisin = zone.getCell(x-total, y);
		if(voisin != null && !voisin.isFreeBloc()){
			total++;
			return countDroiteBloc(x, y, total);
		}
		return total;
	}
	/** retourne le compte de cellules occupées en haut  */
	private int countHautBloc(int x, int y, int total){
		Cell voisin = zone.getCell(x, y+total);
		if(voisin != null && !voisin.isFreeBloc()){
			total++;
			return countDroiteBloc(x, y, total);
		}
		return total;
	}
	/** retourne le compte de cellules occupées en bas  */
	private int countBasBloc(int x, int y, int total){
		Cell voisin = zone.getCell(x, y-total);
		if(voisin != null && !voisin.isFreeBloc()){
			total++;
			return countDroiteBloc(x, y, total);
		}
		return total;
	}
	/** renvoie les coordonées de disponible sur x */
	private ArrayList<Integer> getXPathBloc(int x, int y, ArrayList<Integer> results, int needed, int inc){
		Cell voisin = zone.getCellRelief(x, y);
		if(voisin != null && voisin.isFreeBloc()){
			needed = needed -1;
			results.add(x);
			if(needed == 0){
				return results;
			}
			return getXPathBloc(x+inc, y, results, needed, inc);
		}
		return null;
	}
	/** renvoie les coordonées de disponible sur y */
	private ArrayList<Integer> getYPathBloc(int x, int y, ArrayList<Integer> results, int needed, int inc){
//		Cell voisin = zone.getCell(x, y);
		Cell voisin = zone.getCellRelief(x, y);
		if(voisin != null && voisin.isFreeBloc()){
			needed = needed -1;
			results.add(y);
			if(needed == 0){
				return results;
			}
			return getYPathBloc(x, y+inc, results, needed, inc);
		}
		return null;
	}
	/** calcul le score en fonction du nombre de cellules de type vide et des voisins de 
	 * ces cellules vides pour une façade en X */
	private double scoreXPath(int x, int y, int needed, int inc, double score, int type){
		Cell voisin = zone.getCellRelief(x, y);
		if(voisin != null && voisin.isFreeBloc()){
			needed = needed -1;
			score += scoreVoisinageBloc(voisin, type);
			if(needed == 0){
				return score;
			}
			return scoreXPath(x+inc, y, needed, inc, score, type);
		}
		return -1;
	}
	/** calcul le score en fonction du nombre de cellules de type vide et des voisins de 
	 * ces cellules vides pour une façade en Y */
	private double scoreYPath(int x, int y, int needed, int inc, double score, int type){
		Cell voisin = zone.getCellRelief(x, y);
		if(voisin != null && voisin.isFreeBloc()){
			needed = needed -1;
			score += scoreVoisinageBloc(voisin, type);
			if(needed == 0){
				return score;
			}
			return scoreYPath(x, y+inc, needed, inc, score, type);
		}
		return -1;
	}
	/** Fonction de score de la cellule d'apres son voisinage */
	private double scoreVoisinageBloc(Cell cell, int type){
		double result = 0.0;
		if(cell != null){
			Cell haut = zone.getHaut(cell);
			Cell bas = zone.getBas(cell);
			Cell gauche = zone.getGauche(cell);
			Cell droite = zone.getDroite(cell);
			if(haut != null){
				result +=scoreAffiniteBloc(type, haut.getBlocType());
			}
			if(bas != null){
				result +=scoreAffiniteBloc(type, bas.getBlocType());
			}
			if(gauche != null){
				result +=scoreAffiniteBloc(type, gauche.getBlocType());
			}
			if(droite != null){
				result +=scoreAffiniteBloc(type, droite.getBlocType());
			}
		}
		return result;
	}
	/** fonction bidon qui en fonction du type de bloc vas orienter vers un calcul de score 
	 * d'affinité */
	private double scoreAffiniteBloc(int source, int cible){
		return affinite.getScore(source, cible);
	}
/*======================================CODE COMMUN=============================*/
	/** met à jour les indexes des blocs */
	private void moveBloc(int to_x, int to_y){
		Iterator<Integer> iter = listeBlocs.keySet().iterator();
		while(iter.hasNext()){
			int id = iter.next();
			Bloc bloc = listeBlocs.get(id);
			bloc.move(to_x, to_y);
//			ParamsGlobals.MANAGER.updateObject(bloc);
		}
	}
	/** deplacement du quartier */
	public void move(int to_x, int to_y){
		zone.updateIndexQuartier(to_x, to_y);
		translation.x = translation.x+to_x;
		translation.y = translation.y+to_y;
		moveBloc(to_x, to_y);
		ParamsGlobals.MANAGER.updateObject(this);
	}
	/** permet de récupérer le vecteur de translation */
	public Vector2 getTranslation(){
		Vector2 result = new Vector2();
		result.x = translation.x;
		result.y = translation.y;
		translation.x = 0;
		translation.y = 0;
		return result;
	}
	/** initialisation de la zone */
	public void initZone(int x, int y, int lim_x, int lim_y){
		//initialisation de la zone
		zone.setCentre(x, y);
		zone.setTailleX(lim_x+2);
		zone.setTailleY(lim_y+2);
		zone.initLimit();
		zone.refreshTaille();
	}
	/** initialisation du relief */
	public void setRelief(){
		zone.setRelief(treePointNhgHeight());
	}
	/** suppression des structures qui utilisent plusieurs quartiers*/
	public void clearLinkedStructures(){
		if(getAdministration() != null){
			//pour la mairie
			if(getAdministration().getMairie() != null){
				getAdministration().getMairie().clearStructures();
			}
			//pour la station
			if(getAdministration().getStation() != null){
				getAdministration().getStation().clearStructures();
			}
		
		}
	}
	/** mise à jour des structures en liens entre les quartiers */
	public void updateLinkedStructures(){
		if(getAdministration() != null){
			//etape préparatoire on vas tester si il y'a des voisins
			Administration admin = getAdministration();
			if(!admin.hasNeighbours()){
				admin.setZoneQuartier(this);
				if(voisin_gauche != null){
					admin.setGauche(voisin_gauche);
				}
				if(voisin_droite != null){
					admin.setDroite(voisin_droite);
				}
				if(voisin_haut != null){
					admin.setHaut(voisin_haut);
				}
				if(voisin_bas != null){
					admin.setBas(voisin_bas);
				}
			}
//			System.out.println("croissance x "+this.croissance.axeX());
//			System.out.println("voisins h"+voisin_haut+" b "+this.voisin_bas+", g "+this.voisin_gauche+", d "+this.voisin_droite);
			admin.updateLinkedStructures(this.croissance.axeX());
		}
	}
	/** renvois trois cellules de chaque quartier voisin pour permettre que la heightmap soit homogène */
	private TreeMap<Integer, ArrayList<Cell>> treePointNhgHeight(){
		TreeMap<Integer, ArrayList<Cell>> result = new TreeMap<Integer, ArrayList<Cell>>();
		if(this.voisin_haut != null){
			ArrayList<Cell> cellules = new ArrayList<Cell>();
			//cellule 1
			cellules.add(voisin_haut.getZone().getCellRelief(
					zone.getStartX()+1,
					zone.getEndY()
					));
			//cellule 2
			cellules.add(voisin_haut.getZone().getCellRelief(
					Utils.floatToInt(zone.getCentreX()),
					zone.getEndY()
					));
			//cellule 3
			cellules.add(voisin_haut.getZone().getCellRelief(
					zone.getEndX()-1,
					zone.getEndY()
					));
			result.put(this.voisin_haut.getID(), cellules);
		}
		if(this.voisin_bas != null){
			ArrayList<Cell> cellules = new ArrayList<Cell>();
			//cellule 1
			cellules.add(voisin_bas.getZone().getCellRelief(
					zone.getStartX()+1,
					zone.getStartY()
					));
			//cellule 2
			cellules.add(voisin_bas.getZone().getCellRelief(
					Utils.floatToInt(zone.getCentreX()),
					zone.getStartY()
					));
			//cellule 3
			cellules.add(voisin_bas.getZone().getCellRelief(
					zone.getEndX()-1,
					zone.getStartY()
					));
			result.put(this.voisin_bas.getID(), cellules);
		}
		if(this.voisin_droite != null){
			ArrayList<Cell> cellules = new ArrayList<Cell>();
			//cellule 1
			cellules.add(voisin_droite.getZone().getCellRelief(
					zone.getEndX(), 
					zone.getStartY()+1));
			//cellule 2
			cellules.add(voisin_droite.getZone().getCellRelief(
					zone.getEndX(), 
					Utils.doubleToInt(zone.getCentreY())));
			//cellule 3
			cellules.add(voisin_droite.getZone().getCellRelief(
					zone.getEndX(), 
					zone.getEndY()-1));
			
			result.put(this.voisin_droite.getID(), cellules);
		}
		if(this.voisin_gauche != null){
			ArrayList<Cell> cellules = new ArrayList<Cell>();
			//cellule 1
			cellules.add(voisin_gauche.getZone().getCellRelief(
					zone.getStartX(), 
					zone.getStartY()+1));
			//cellule 2
			cellules.add(voisin_gauche.getZone().getCellRelief(
					zone.getStartX(),
					Utils.floatToInt(zone.getCentreY())));
			//cellule 3
			cellules.add(voisin_gauche.getZone().getCellRelief(
					zone.getStartX(),
					zone.getEndY()-1));
			result.put(this.voisin_gauche.getID(), cellules);
		}
//		System.out.println("transmission "+result);
		return result;
	}
	/** renvois toutes les cellules de chaque quartier voisin pour permettre que la heightmap soit homogène */
	private TreeMap<Integer, ArrayList<Cell>> allPointNhgHeight(){
		TreeMap<Integer, ArrayList<Cell>> result = new TreeMap<Integer, ArrayList<Cell>>();
		if(this.voisin_haut != null){
			ArrayList<Cell> cellules = new ArrayList<Cell>();
			int start_x = voisin_haut.getZone().getStartX();
			int end_x = voisin_haut.getZone().getEndX()-1;
			int gap = Math.abs(end_x - start_x);
			if(gap > Math.abs(zone.getEndX() - zone.getStartX())){
				start_x = zone.getStartX();
				end_x = zone.getEndX()-1;
			}
			Cell start = voisin_haut.getZone().getCellRelief(
					start_x, 
					voisin_haut.getZone().getStartY());
			Cell end = voisin_haut.getZone().getCellRelief(
					end_x, 
					voisin_haut.getZone().getStartY());
			if(start != null && end != null){
				cellules = voisin_haut.getZone().getCellLine(start, end);
				result.put(this.voisin_haut.getID(), cellules);
			}
		}
		if(this.voisin_bas != null){
			ArrayList<Cell> cellules = new ArrayList<Cell>();
			int start_x = voisin_bas.getZone().getStartX();
			int end_x = voisin_bas.getZone().getEndX()-1;
			int gap = Math.abs(end_x - start_x);
			if(gap > Math.abs(zone.getEndX() - zone.getStartX())){
				start_x = zone.getStartX();
				end_x = zone.getEndX()-1;
			}
			Cell start = voisin_bas.getZone().getCellRelief(
					start_x, 
					voisin_bas.getZone().getEndY()-1);
			Cell end = voisin_bas.getZone().getCellRelief(
					end_x, 
					voisin_bas.getZone().getEndY()-1);
			if(start != null && end != null){
				cellules = voisin_bas.getZone().getCellLine(start, end);
				result.put(this.voisin_bas.getID(), cellules);
			}
		}
		if(this.voisin_droite != null){
			ArrayList<Cell> cellules = new ArrayList<Cell>();
			int start_y = voisin_droite.getZone().getStartY();
			int end_y = voisin_droite.getZone().getEndY()-1;
			int gap = Math.abs(end_y - start_y);
			if(gap > Math.abs(zone.getEndY() - zone.getStartY())){
				start_y = zone.getStartY();
				end_y = zone.getEndY()-1;
			}
			Cell start = voisin_droite.getZone().getCellRelief(
					voisin_droite.getZone().getStartX(), 
					start_y);
			Cell end = voisin_droite.getZone().getCellRelief(
					voisin_droite.getZone().getStartX(), 
					end_y);
			if(start != null && end != null){
				cellules = voisin_droite.getZone().getCellLine(start, end);
				result.put(this.voisin_droite.getID(), cellules);
			}
		}
		if(this.voisin_gauche != null){
			ArrayList<Cell> cellules = new ArrayList<Cell>();
			int start_y = voisin_gauche.getZone().getStartY();
			int end_y = voisin_gauche.getZone().getEndY()-1;
			int gap = Math.abs(end_y - start_y);
			if(gap > Math.abs(zone.getEndY() - zone.getStartY())){
				start_y = zone.getStartY();
				end_y = zone.getEndY()-1;
			}
			Cell start = voisin_gauche.getZone().getCellRelief(
					voisin_gauche.getZone().getEndX()-1, 
					start_y);
			Cell end = voisin_gauche.getZone().getCellRelief(
					voisin_gauche.getZone().getEndX()-1,
					end_y);
			if(start != null && end != null){
				cellules = voisin_gauche.getZone().getCellLine(start, end);
				result.put(this.voisin_gauche.getID(), cellules);
			}
		}
//		System.out.println(result);
		return result;
	}
	/** retourne une seule fois le fait que le quartier ai grandi */
	public int asExpanded(){
		int result = expanded;
		expanded = 0;
		return result;
	}
	/** ajout de voisins */
	public Croissance getCroissance(){
		return this.croissance;
	}
	/** croissance du quartier */
	public void expand(){
		expanded++;
		croissance.crois();
		int to_x = croissance.toX();
		int to_y = croissance.toY();
		zone.augmenteTaille(to_x*2, to_y*2);
		zone.updateIndexQuartier(to_x, to_y);
		zone.resizeLimit(allPointNhgHeight());
		this.translation.x = translation.x+to_x;
		this.translation.y = translation.y+to_y;
		moveBloc(to_x, to_y);
		ParamsGlobals.MANAGER.updateObject(this);
		//mis en place des structures dans la mairie
		//setLinkedStructures();
	}
}
