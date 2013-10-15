package initialisation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;


import monde.Interaction;
import monde.Ville;

import social.Femme;
import social.Homme;
import social.Personne;
import structure.Administration;
import structure.Banque;
import structure.Centrale;
import structure.Commerce;
import structure.Commissariat;
import structure.Decoration;
import structure.Ecole;
import structure.Ferme;
import structure.Mairie;
import structure.Stase;
import structure.Station;
import structure.Habitation;
import structure.Horloge;
import structure.Immeuble;
import structure.Interim;
import structure.Morgue;
import structure.Pensionnat;
import structure.Production;
import structure.Quartier;
import structure.Refuge;
import structure.Sanitarium;
import structure.Tribunal;
import structure.Usine;
import tools.ParamsGlobals;
import tools.SelectionNom;

/** création d'une ville de base pour le test */
public class Initialisation {
	Ville ville;
	private static int ratio_ferme = 70;
	private static int ratio_centrale = 80;
	private static int ratio_usine = 100;

	/** données pour une ville initiales pour faire nos tests */
	public Ville execute(){
		ville = new Ville("Sombre Ville");
		ville.setAge(0);
		ville.setHeure(0);
		setQuartiers();
//		ville.updateTransports();
		ParamsGlobals.MANAGER.executeManagement();
//		ParamsGlobals.MANAGER.executeTime(0);
		return ville;
	}
	/** creation de tout les quartiers de depars */
	private void setQuartiers(){
		Quartier citadelle = new Quartier(-1, ville.getID());
		citadelle.setCitadelle();
		ville.ajoutQuartier(citadelle);
		// placement initial des quartiers
		for(int i = 0; i < ParamsGlobals.NB_QUARTIERS; i++){
			Quartier quartier = new Quartier(i, ville.getID());
			ville.ajoutQuartier(quartier);
		}
		// mise en correspondance des quartiers
		ville.setVoisins();
		citadelle.setRelief();
		// chargement du relief des quartiers
		Iterator<Integer> iter_quartier = ville.getQuartiersID().iterator();
		while(iter_quartier.hasNext()){
			int id_quartier = iter_quartier.next();
			Quartier quartier = ville.getQuartier(id_quartier);
			quartier.setRelief();
		}
		//chargement du contenu des quartiers
		iter_quartier = ville.getQuartiersID().iterator();
		while(iter_quartier.hasNext()){
			int id_quartier = iter_quartier.next();
			Quartier quartier = ville.getQuartier(id_quartier);
			// mise en place de l'interaction et de la population pour le quartier
			TreeMap<Integer, Personne> population = createHabitants();
			Interaction interaction = new Interaction();
			interaction.initPopulation(population);
			quartier.setInteraction(interaction);
			// lancement création des blocs
			setBloc(ville, quartier, population);
		}
		//mise en place des voisins 
		iter_quartier = ville.getQuartiersID().iterator();
		while(iter_quartier.hasNext()){
			Quartier quartier = ville.getQuartier(iter_quartier.next());
			quartier.updateLinkedStructures();
		}
		//cloture des rocades et des rails
		//TODO
		ParamsGlobals.MANAGER.registerObject(ville);
	}
	
	/** creation d'habitants pour le quartier */
	private TreeMap<Integer, Personne> createHabitants(){
		TreeMap<Integer, Personne> population = new TreeMap<Integer, Personne>();
		int nb_pop = (int) (Math.random() * ParamsGlobals.BASE_POP);
		for(int p = 0; p< nb_pop; p++){
			Personne personne;
			int new_id = p;
			if(Math.random() < 0.5){
				personne = new Femme(new_id);
				personne.setNom(SelectionNom.getNomFille());
			}
			else{
				personne = new Homme(new_id);
				personne.setNom(SelectionNom.getNomGarcon());
			}
			personne.setEsperanceVie(500);
			int age = (int) (Math.random()*100);
			personne.setAge(age);
			personne.initQI(Math.random());
			population.put(p, personne);
		}
		return population;
	}
	/** creation de tout les blocs de depars */
	private void setBloc(Ville ville, Quartier quartier, TreeMap<Integer, Personne> habitants){
		int id_q = quartier.getID();
		/** creation du bloc d'administration */
		Administration administration = new Administration(id_q);
		ville.ajoutBloc(administration, id_q);
		/** creation des blocs d'habitation */
		//récupération de tout les adultes 
		ArrayList<Personne> adultes = new ArrayList<Personne>();
		Iterator<Integer> iter = habitants.keySet().iterator();
		while(iter.hasNext()){
			Personne personne = habitants.get(iter.next());
			if(personne.getAge() > 16){
				adultes.add(personne);
			}
		}
		TreeMap<Integer, Habitation> habitations = createHabitations(habitants, id_q);
		TreeMap<Integer, Production> productions = createProduction(habitants, id_q);
		TreeMap<Integer, Commerce> commerces = createCommerce(habitants, id_q);
		int total_blocs = habitations.size()+productions.size()+commerces.size();
		TreeMap<Integer, Decoration> decorations = createDecorations(total_blocs, id_q);
		randomDispatch(habitations, productions, commerces, decorations, id_q);
		// remplissage des blocs des blocs
		populateAdministration(habitants, administration, id_q);
		ArrayList<Integer> hobos = populateHabitations(habitants, habitations);
		administration.updateHobos(hobos);	
		populateProductions(habitants, productions);      
		//creation des blocs de loisirs
		administration.updateHobos(hobos);
    }
	/** disposition aléatoire 
	 * @param decorations */
	private void randomDispatch(TreeMap<Integer, Habitation> habitations, 
			TreeMap<Integer, Production> productions, 
			TreeMap<Integer, Commerce> commerces, 
			TreeMap<Integer, Decoration> decorations, 
			int id_q){
		int hab_nb = habitations.size();
		int prod_nb = productions.size();
		int com_nb = commerces.size();
		int deco_nb = decorations.size();
		int hab_fait = 0;
		int prod_fait = 0;
		int com_fait = 0;
		int deco_fait = 0;
		while (hab_fait < hab_nb || prod_fait < prod_nb || com_fait < com_nb || deco_fait < deco_nb){
			double hab_score = 0.0;
			double prod_score = 0.0;
			double com_score = 0.0;
			double deco_score = 0.0;
			if(hab_nb > 0){
				hab_score = Math.random()*(1.0-(hab_fait/hab_nb));
			}
			if(prod_nb > 0){
				prod_score = Math.random()*(1.0-(prod_fait/prod_nb));
			}
			if(com_nb > 0){
				com_score = Math.random()*(1.0-(com_fait/com_nb));
			}
			if(deco_nb > 0){
				deco_score = Math.random()*(1.0-(deco_fait/deco_nb));
			}
			if(hab_score > prod_score && hab_score > com_score && hab_score > deco_score){
				Habitation habitation = habitations.get(hab_fait);
				if(habitation != null){
					ville.ajoutBloc(habitation, id_q);
					hab_fait++;
				}
			}
			else if(prod_score > hab_score && prod_score > com_score && prod_score > deco_score){
				Production production = productions.get(prod_fait);
				if(production != null){
					ville.ajoutBloc(production, id_q);
					prod_fait++;
				}
			}
			else if(com_score > hab_score && com_score > prod_score && com_score > deco_score){
				Commerce commerce = commerces.get(com_fait);
				if(commerce != null){
					ville.ajoutBloc(commerce, id_q);
					commerce.initMagasins();
					com_fait++;
				}
			}
			else{
				Decoration decoration = decorations.get(deco_fait);
				if(decoration != null){
					ville.ajoutBloc(decoration, id_q);
					decoration.initMU();
					deco_fait++;
				}
			}
		}
	}
	/** creation des decorations */
	private TreeMap<Integer, Decoration> createDecorations(int total_blocs, int id_q) {
		int nb_deco = total_blocs/3;
		TreeMap<Integer, Decoration> decorations = new TreeMap<Integer, Decoration>();
		for(int d = 0; d < nb_deco; d++){
			Decoration decoration = new Decoration(id_q);
			decorations.put(d, decoration);
		}
		return decorations;
	}
	/** creation des commerces */
	private TreeMap<Integer, Commerce> createCommerce(TreeMap<Integer, Personne> population, int id_q){
		int nb_pop = population.size();
		int nb_com = nb_pop/30;
		TreeMap<Integer, Commerce> commerces = new TreeMap<Integer, Commerce>();
		for(int c = 0; c < nb_com; c++){
			Commerce commerce = new Commerce(id_q);
			commerces.put(c, commerce);
		}
		return commerces;
	}
	/** remplissage des blocs de production */
	private void populateProductions(TreeMap<Integer, Personne> population, TreeMap<Integer, Production> productions){
		int nb_pop = population.size();
		int nb_ferme = nb_pop/ratio_ferme;
		int nb_central = nb_pop/ratio_centrale;
		int nb_usine = nb_pop/ratio_usine;
		int cap = 0;
		Iterator<Integer> iter = productions.keySet().iterator();
		Production production = productions.get(iter.next());
		int id_b = production.getID();
		//construction centrales 
		for(int c = 0; c < nb_central; c ++){
			Centrale centrale = new Centrale(id_b, production.getIDQuartier());
			production.ajoutCentrale(centrale);
			cap++;
			if(cap == production.getCapaciteMax()){
				production = productions.get(iter.next());
				cap = 0;
			}
		}
		//construction fermes
		for(int f = 0; f < nb_ferme; f ++){
			Ferme ferme = new Ferme(id_b, production.getIDQuartier());
			production.ajoutFerme(ferme);
			cap++;
			if(cap == production.getCapaciteMax()){
				production = productions.get(iter.next());
				cap = 0;
			}
		}
		//construction usines
		for(int u = 0; u < nb_usine; u ++){
			Usine usine = new Usine(id_b, production.getIDQuartier());
			production.ajoutUsine(usine);
			cap++;
			if(cap == production.getCapaciteMax()){
				production = productions.get(iter.next());
				cap = 0;
			}
		}
	}
	/** creation de la production */
	private TreeMap<Integer, Production> createProduction(TreeMap<Integer, Personne> population, int id_q){
		int nb_pop = population.size();
		TreeMap<Integer, Production> productions = new TreeMap<Integer, Production>();
		int nb_ferme = nb_pop/ratio_ferme;
		int nb_central = nb_pop/ratio_centrale;
		int nb_usine = nb_pop/ratio_usine;
		int cap = 0;
		Production production = new Production(id_q);
		//construction centrales 
		for(int c = 0; c < nb_central; c ++){
			cap++;
			if(cap == production.getCapaciteMax()){
				productions.put(newKey(productions), production);
				production = new Production(id_q);
				cap = 0;
			}
		}
		//construction fermes
		for(int f = 0; f < nb_ferme; f ++){
			cap++;
			if(cap == production.getCapaciteMax()){
				productions.put(newKey(productions), production);
				production = new Production(id_q);
				cap = 0;
			}
		}
		//construction usines
		for(int u = 0; u < nb_usine; u ++){
			cap++;
			if(cap == production.getCapaciteMax()){
				productions.put(newKey(productions), production);
				production = new Production(id_q);
				cap = 0;
			}
		}
		if(!productions.entrySet().contains(production)){
			productions.put(newKey(productions), production);
		}
		return productions;
	}
	private int newKey(TreeMap<Integer, ?> test){
		if(test != null && test.size() > 0){
			return (test.lastKey()+1);
		}
		return 0;
	}
	/** creation des habitation */
	private TreeMap<Integer, Habitation> createHabitations(TreeMap<Integer, Personne> population, int id_q){
		TreeMap<Integer, Habitation> habitations = new TreeMap<Integer, Habitation>();
		Immeuble test_im = new Immeuble(-1, id_q);
		//création de tout les immeubles ainsi que des immeubles d'habitation
		int nb_im = population.size()/(test_im.getCapaciteMaxTotal());
		int h = 0;
		int current = 0;
		while(!stop(nb_im, current)){
			Habitation habitation = new Habitation(id_q);
			habitations.put(h, habitation);
			h++;
			current += habitation.getCapaciteMax();
		}
		return habitations;
	}
	/** fonction de test d'arret de la recursion */
	private boolean stop(int nb_im, int current){
		if(current < nb_im){
			return false;
		}
		return true;
	}
	/** remplissage des habitations */
	private ArrayList<Integer> populateHabitations(TreeMap<Integer, Personne> population, TreeMap<Integer, Habitation> habitations){
		ArrayList<Personne> locataires = new ArrayList<Personne>();
		locataires.addAll(population.values());
		//création de tout les immeubles ainsi que des immeubles d'habitation
		Iterator<Integer> iter_hab = habitations.keySet().iterator();
		while(iter_hab.hasNext()){
			Habitation habitation = habitations.get(iter_hab.next());
			int id_b = habitation.getID();
			for(int h = 0; h < habitation.getCapaciteMax(); h++){
				Immeuble immeuble = new Immeuble(id_b, habitation.getIDQuartier());
				habitation.ajoutBatiment(immeuble);
				Iterator<Personne> iter_personne = locataires.iterator();
				ArrayList<Personne> tmp = new ArrayList<Personne>();
				ArrayList<Integer> tmp2 = new ArrayList<Integer>();
				//on rajoute les gens dans l'immeuble
				for(int p = 0; p < immeuble.getCapaciteMaxTotal(); p++){
					if(iter_personne.hasNext()){
						Personne locataire = iter_personne.next();
						tmp.add(locataire);
						tmp2.add(locataire.getId());
					}
				}
				immeuble.addLocataires(tmp2);
				locataires.removeAll(tmp);
			}
		}
		// on extrait les ids des sans abris 
		Iterator<Personne> iter_hobo = locataires.iterator();
		ArrayList<Integer> result = new ArrayList<Integer>();
		while(iter_hobo.hasNext()){
			result.add(iter_hobo.next().getId());
		}
		return result;
	}
	/** creation de l'administration */
	private void populateAdministration(TreeMap<Integer, Personne> population, Administration admin, int id_q){
		int cagnotte = (int) (Math.random()*1000*Math.random()*1000);
		int soilent = (int) (Math.random()*1000);
		//creation horloge
		int id_b = admin.getID();
		Horloge horloge = new Horloge(id_b, id_q);
		admin.ajoutBatiment(horloge);
		//creation banque
		Banque banque = new Banque(id_b, id_q);
		banque.setSoilent(soilent);
		banque.setCagnotte(cagnotte);
		admin.ajoutBatiment(banque);
		//creation Commissariat
		Commissariat commissariat = new Commissariat(id_b, id_q);
		admin.ajoutBatiment(commissariat);
		//creation creation ecole
		Ecole ecole = new Ecole(id_b, id_q);
		admin.ajoutBatiment(ecole);
		//creation Interim
		Interim interim = new Interim(id_b, id_q);
		admin.ajoutBatiment(interim);
		//ajout de tout les chomeurs 
		Iterator<Integer> iter_chomeurs = population.keySet().iterator();
		while(iter_chomeurs.hasNext()){
			Personne personne = population.get(iter_chomeurs.next());
			if(personne.getAge() > 16){
				interim.addDemandeur(personne.getId());
			}
		}
		//creation Morgue
		Morgue morgue = new Morgue(id_b, id_q);
		admin.ajoutBatiment(morgue);
		//creation Pensionnat
		Pensionnat pensionnat = new Pensionnat(id_b, id_q);
		Iterator<Integer> iter_enfant = population.keySet().iterator();
		while(iter_enfant.hasNext()){
			Personne personne = population.get(iter_enfant.next());
			if(personne.getAge() < 16){
				pensionnat.ajoutEnfant(personne.getId());
			}
		}
		admin.ajoutBatiment(pensionnat);
		//creation Refuge
		Refuge refuge = new Refuge(id_b, id_q);
		admin.ajoutBatiment(refuge);
		//creation Sanitarium
		Sanitarium sanitarium = new Sanitarium(id_b, id_q);
		admin.ajoutBatiment(sanitarium);
		//creation Tribunal
		Tribunal tribunal = new Tribunal(id_b, id_q);
		admin.ajoutBatiment(tribunal);
		//creation de la gare
		Station gare = new Station(id_b, id_q);
		admin.ajoutBatiment(gare);
		//création de la mairie
		Mairie mairie = new Mairie(id_b, id_q);
		admin.ajoutBatiment(mairie);
		//création du centre de stase 
		Stase stase = new Stase(id_b, id_q);
		admin.ajoutBatiment(stase);
	}
}
