package profils;

import initialisation.LoadComDatas;

import java.util.ArrayList;
import java.util.Iterator;



public class ProfilMagasin {
	private String nom;
	private int type;
	private int max_art;
	private int max_vend;
	private int conso_ene;
	private int revenus;
	private double physique;
	
	public ProfilMagasin(){
		this.nom = "vide";
		this.max_vend = 0;
		this.max_art = 0;
		this.conso_ene = 0;
		this.type = 10;
		this.revenus = 0;
		this.physique = 0.0;
	}
/*------------------------------CHARGEMENT---------------------------*/	
	public void loadProfil(int t){
		if(this.type != t){
			ArrayList<String> params = new LoadComDatas().load(t);
			Iterator<String> iter = params.iterator();
			this.nom = iter.next();
			this.max_vend = Integer.parseInt(iter.next());
			this.max_art = Integer.parseInt(iter.next());
			this.conso_ene = Integer.parseInt(iter.next());
			this.revenus = Integer.parseInt(iter.next());
			this.physique = Double.parseDouble(iter.next());
			this.type = t;
		}
	}
/*------------------------------ACCESSEURS----------------------------*/
	public String getNom(){
		return this.nom;
	}
	public int getMaxVend(){
		return this.max_vend;
	}
	public int getMaxArt(){
		return this.max_art;
	}
	public int getConso(){
		return this.conso_ene;
	}
	public int getRevenus(){
		return this.revenus;
	}
	public int getType(){
		return this.type;
	}
	public double getPhysique(){
		return this.physique;
	}
}
