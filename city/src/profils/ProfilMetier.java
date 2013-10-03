package profils;

import initialisation.LoadJobDatas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;



public class ProfilMetier {
	private double education;
	private double qi;
	private double ratiohf;
	private double age;
	private TreeMap<Integer, Double> factions;
	private int type;
	private String nom;
	private int nombre_postes;
	private double desirabilite;
	private int id_bat;
	private int id_bloc;
	
	public ProfilMetier(int id_bat){
		this.education = 0.0;
		this.qi = 0.0;
		this.ratiohf = 0.0;
		this.factions = new TreeMap<Integer, Double>();
		this.type = 10;
		this.nombre_postes = 0;
		this.nom = "vide";
		this.desirabilite = 0.0;
		this.id_bat = id_bat;
	}
	/** indique le nombre de postes disponibles */
	public void setNombrePostes(int nb){
		this.nombre_postes = nb;
	}
	/** rajoute des postes par dessus */
	public void addPostes(int nb){
		this.nombre_postes += nb;
	}
	/** enleve des postes */
	public void reducePostes(){
		this.nombre_postes = (nombre_postes-1);
	}
	/** indique dans quel bloc le poste est disponible */
	public void setBloc(int id){
		this.id_bloc = id;
	}
	/** indique dans quel batiment le poste est disponible */
	public void setBat(int id){
		this.id_bat = id;
	}
/*-----------------------------CHARGEMENT------------------------*/
	public void loadProfil(int t){
		if(this.type != t){
			LoadJobDatas jd = new LoadJobDatas(t);
			ArrayList<Double> param_job = jd.getJobParams();
			factions = jd.getJobFaction();
			Iterator<Double> iter = param_job.iterator();
			this.nom = jd.getNom();
			this.education = iter.next();
			this.qi = iter.next();
			this.ratiohf = iter.next();
			this.age = iter.next();
			this.desirabilite = iter.next();
			this.type = t;
		}
	}
/*----------------------------ACCESSEURS--------------------------*/
	public double getEducation(){
		return this.education;
	}
	public double getQI(){
		return this.qi;
	}
	public double getRatio(){
		return this.ratiohf;
	}
	public TreeMap<Integer, Double> getFaction(){
		return this.factions;
	}
	public String getNom(){
		return this.nom;
	}
	public int getNombrePostes(){
		return this.nombre_postes;
	}
	public double getAge(){
		return this.age;
	}
	public int getType(){
		return this.type;
	}
	public double getDesirabilite(){
		return this.desirabilite;
	}
	public int getIDBloc(){
		return this.id_bloc;
	}
	public int getIDBat(){
		return this.id_bat;
	}
}
