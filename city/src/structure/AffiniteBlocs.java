package structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import tools.Identifiants;


/** classe qui gère les affinités entre les différents ty*/
public class AffiniteBlocs {
	ArrayList<Lien> liens;
	public AffiniteBlocs(){
		liens = new ArrayList<Lien>();
		ArrayList<Type> types = setNodes();
		setEdges(types);
	}
	private ArrayList<Type> setNodes(){
		ArrayList<Type> types = new ArrayList<Type>();
		Type admin = new Type(Identifiants.admininistrationBloc);
		types.add(admin);
		Type free = new Type(Identifiants.vide);
		types.add(free);
		Type com = new Type(Identifiants.commerceBloc);
		types.add(com);
		Type prod = new Type(Identifiants.productionBloc);
		types.add(prod);
		Type lois = new Type(Identifiants.loisirBloc);
		types.add(lois);
		Type hab = new Type(Identifiants.habitationBloc);
		types.add(hab);
		Type road = new Type(Identifiants.roadBloc);
		types.add(road);
		Type deco = new Type(Identifiants.decorationBloc);
		types.add(deco);
		return types;
	}
	/** initialise les arcs */
	private void setEdges(ArrayList<Type> nodes){
		Iterator<Type> iter_pere = nodes.iterator();
		while(iter_pere.hasNext()){
			Type pere = iter_pere.next();
			Iterator<Type> iter_fils = nodes.iterator();
			while(iter_fils.hasNext()){
				Type fils = iter_fils.next();
				double score = setScore(pere.getId(), fils.getId());
				Lien lien_1 = new Lien(pere, fils, score);
				Lien lien_2 = new Lien(fils, pere, score);
				liens.add(lien_1);
				liens.add(lien_2);
			}
		}
	}
	/** A mettre dans une base de donnée */
	private double setScore(int source, int cible){
		if(isIn(source, cible, Identifiants.vide) || isIn(source, cible, Identifiants.roadBloc)){
			return 1.0;
		}
		if(isIn(source, cible, Identifiants.admininistrationBloc)){
			//administration avec lui meme -> pas possible 
			if(source == cible){
				return 0.0;
			}
			//administration avec loisirs
			else if(isIn(source, cible, Identifiants.loisirBloc)){
				return 0.7;
			}
			//administration avec commerces
			else if(isIn(source, cible, Identifiants.commerceBloc)){
				return 1.0;
			}
			//administration avec habitation
			else if(isIn(source, cible, Identifiants.habitationBloc)){
				return 1.0;
			}
			//administration avec production
			else if(isIn(source, cible, Identifiants.productionBloc)){
				return 0.1;
			}
			//administration avec decoration
			else if(isIn(source, cible, Identifiants.decorationBloc)){
				return 1.0;
			}
		}
		if(isIn(source, cible, Identifiants.commerceBloc)){
			//commerce avec lui meme 
			if(source == cible){
				return 0.7;
			}
			//commerces avec habitation
			else if(isIn(source, cible, Identifiants.habitationBloc)){
				return 0.6;
			}
			//commerce avec production
			else if(isIn(source, cible, Identifiants.productionBloc)){
				return 0.4;
			}
			//commerce avec loisirs
			else if(isIn(source, cible, Identifiants.loisirBloc)){
				return 0.7;
			}
			//commerce avec decoration
			else if(isIn(source, cible, Identifiants.decorationBloc)){
				return 1.0;
			}
		}
		if(isIn(source, cible, Identifiants.habitationBloc)){
			//habitation avec lui meme
			if(source == cible){
				return 0.5;
			}
			//habitation avec loisirs
			if(isIn(source, cible, Identifiants.loisirBloc)){
				return 0.4;
			}
			//habitation avec production
			else if(isIn(source, cible, Identifiants.productionBloc)){
				return 0.0;
			}
			//habitation avec decoration
			else if(isIn(source, cible, Identifiants.decorationBloc)){
				return 0.7;
			}
		}
		if(isIn(source, cible, Identifiants.productionBloc)){
			//production avec lui meme
			if(source == cible){
				return 1.0;
			}
			//production avec loisirs
			if(isIn(source, cible, Identifiants.loisirBloc)){
				return 0.6;
			}
			//production avec decoration
			else if(isIn(source, cible, Identifiants.decorationBloc)){
				return 0.2;
			}
		}
		if(isIn(source, cible, Identifiants.loisirBloc)){
			if(source == cible){
				return 0.3;
			}
		}
		return -1;
	}
	/** test si un des identifiants est dans une des relations */
	private boolean isIn(int id_1, int id_2, int seek){
		if(id_1 == seek || id_2 == seek){
			return true;
		}
		return false;
	}
	/** récupère le score entre un noeud source et un noeud cible */
	public double getScore(int source, int cible){
		double score = 0;
		Iterator<Lien> iter = liens.iterator();
		while(iter.hasNext() && score == 0){
			Lien lien = iter.next();
			if(lien.getSource() == source && lien.getCible() == cible){
				score = lien.getScore();
			}
		}
		return score;
	}
/** classe qui contient les types de blocs */
	private class Type{
		int id;
		public Type(int i){
			this.id = i;
		}
		public int getId(){
			return this.id;
		}
	}
/** Classe de liens pondérés entre les types de blocs */
	private class Lien{
		Type source;
		Type cible;
		double score;
		public Lien(Type s, Type c, double aff){
			this.source = s;
			this.cible = c;
			this.score = aff;
		}
		public double getScore(){
			return this.score;
		}
		public int getSource(){
			return this.source.getId();
		}
		public int getCible(){
			return this.cible.getId();
		}
	}
}
