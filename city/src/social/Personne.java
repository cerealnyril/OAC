package social;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import monde.Interaction;



public abstract class Personne {
	/** âge de la personne */
	protected int age;
	/** Réputation de la personne */
	protected int reputation;
	/** esperance de vie de la personne */
	protected int esperance_vie;
	/** nom et prenom */
	protected String nom;
	protected String prenom;
	/** classe de profession associée */
	protected int profession;
	/** identifiant unique de la personne */
	protected int identifiant;
	/** quotien intellectuel de la personne compris entre 0 et 200 */
	protected double QI;
	/** niveau d'éducation cumulée */
	protected double education;
	/** salaire */
	protected double cagnotte;
	/** si la personne est vivante */
	private boolean alive;	
	/** temporairement les besoins */
	private double faim;
	private double sommeil;
	private double physique;
	/** pointeur quartier */
	protected Interaction interaction;
	/** marqueur intelligence artificielle */
	private boolean AI;
	/** horloge interne */
	protected final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	public Personne(int id){
		this.age = 0;
		this.reputation = 0;
		this.profession = 10;
		this.identifiant = id;
		this.education = 0;
		this.QI = 0.5;
		this.alive = true;
		this.faim = 0.0;
		this.sommeil = 0.0;
		this.cagnotte = 0.0;
		this.physique = 0.0;
		this.AI = true;
	}

	public abstract void setEsperanceVie(int esp);
/*----------------------------------ACCESSEURS---------------------------*/
	/** retourne l'identifiant du quartier associé à la personne */
	public int getIDQuartier(){
		return this.interaction.getQuartier().getID();
	}
	/** retourne l'age de la personne */
	public int getAge() {
		return this.age;
	}
	/** retourne l'esperance de vie de la personne */
	public int getEsperanceVie(){
		return this.esperance_vie;
	}
	
	public int getReputation() {
		return this.reputation;
	}

	public void setReputation(int reputation) {
		this.reputation = reputation;
	}
	public double getEducation(){
		return this.education;
	}
	public int getProfession() {
		return this.profession;
	}
	public int getId(){
		return this.identifiant;
	}
	public String getNom(){
		return this.nom;
	}
	public String getPrenom(){
		return this.prenom;
	}
	public double getQI(){
		return this.QI;
	}
	/** renvois la quantité de nourriture que la personne mange */
	public int getConsommationMiam(){
		if(alive){
			return 5;
		}
		return 0;
	}
	/** dit si c'est une intelligence artificielle */
	public boolean isAI(){
		return this.AI;
	}
	/** renvois l'argent personnel de la personne */
	public double getCagnotte(){
		return this.cagnotte;
	}
/*------------------------------------SETTEURS------------------------------*/
	public void resetID(int id){
		this.identifiant = id;
	}
	public void setProfession(int type) {
		this.profession = type;
	}
	public void initEsperance(int esp){
		this.esperance_vie = esp;
	}
	/** la depense physique est un ratio pour la faim et la fatique */
	public void setPhysique(double phy){
		this.physique = phy;
	}
	/** augmente l'etat en fonction des taux de croissance */
	protected void gestionEtat(){
		double base_faim = 0.025;
		double base_sommeil = 0.0125;
		double croissance_faim = base_faim+(base_faim*this.physique);
		double croissance_sommeil = base_sommeil+(base_sommeil*this.physique);
		faim += croissance_faim;
		sommeil += croissance_sommeil;
	}
	public void setInteraction(Interaction interaction){
		this.interaction = interaction;
	}
	/** le quotien intellectuel de l'enfant dépend de celui de sa mere
	 * et de l'éducation de cette derniere */
	public void initQI(double qi){
		double new_qi = (qi+Math.random())/2.0;
		this.QI = new_qi;
	}
	/** versement du salaire */
	public void versementSalaire(double paie){
		this.cagnotte += paie;
	}
/*-------------------------------Fonctionnement interne-------------------------------*/
	public abstract String getSexe();
	/** methode abstraite du lancement de l'horloge interne */
	public abstract void timerStart();
	/** rajoute une année à la personne */
	public void viellissement(){
		this.age++;
	}
	/** vérifie si l'habitant survit. 
	 * @return false si il ne meurt pas; true si il meurt */
	protected void gestionMort(){
		double rand = Math.random();
		/** pour le moment j'ai juste mis que si son age divisé par son espérance + rand > 1 et beh couic */
		double couic = (age/esperance_vie)+rand;
		if(couic > 1.0){
			scheduler.shutdown();
			alive = false;
			interaction.getQuartier().gestionMort(this.identifiant);
		}
	}
	public boolean isAlive(){
		return alive;
	}
	/** Vas augmenter l'age de tout le monde de 1 */
	protected void augmentationAge(){
		this.age++;
		if(this.age == 18){
			interaction.getQuartier().updateAdult(this.identifiant);
		}
	}
	
/*-------------------------INITIALISATION SI CHARGEMENT------------------------ */	
	/** initialisation du nom */
	public void setNom(String n){
		this.nom = n;
	}
	/** initialisation du prénom */
	public void setPrenom(String p){
		this.prenom = p;
	}
	/** initialisation de l'âge */
	public void setAge(int a){
		this.age = a;
	}
	/** initialisation du quotien intellectuel */
	public void setQI(double qi){
		this.QI = qi;
	}
	/** initialisation de l'éducation */
	public void setEducation(double edu){
		this.education = edu;
	}
	/** initialisation vivant */
	public void setVivant(boolean vie){
		this.alive = vie;
	}
}
