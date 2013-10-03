package tools;

import java.util.ArrayList;

public class Utils {
	
	public static ArrayList<Integer> removeInArrayList(ArrayList<Integer> base, int id){
		int result = -1;
		for(int i = 0; i < base.size(); i++){
			if(base.get(i) == id){
				result = i;
			}
		}
		if(result != -1){
			base.remove(result);
		}
		return base;
	}
	public static int stringToInt(String s){
		char[] tmp = s.toCharArray();
		int base = -1;
		for(int i = 0; i < tmp.length; i++){
			int tmp_int = getInt(tmp[i]);
			if(tmp_int != -1){
				base = concat(base, tmp_int);
			}
		}
		return base;
	}
	private static int getInt(char c){
		int result = -1;
		switch(c){
			case '0' :
				result = 0;
			break;
			case '1' :
				result = 1;
			break;
			case '2' :
				result = 2;
			break;
			case '3' :
				result = 3;
			break;
			case '4' :
				result = 4;
			break;
			case '5' :
				result = 5;
			break;
			case '6' :
				result = 6;
			break;
			case '7' :
				result = 7;
			break;
			case '8' :
				result = 8;
			break;
			case '9' :
				result = 9;
			break;
		}
		return result;
	}
	private static int concat(int base, int add){
		int mult = 10;
		int result = 0;
		/** si c'est un nouveau */
		if(base == -1){
			result = add;
		}
		/** sinon */
		else{
			result += (add+base*mult);
		}
		return result;
	}
	public static int moitiePos(int num){
		if(num % 2 == 0){
			return (num/2);
		}
		return (num/2)+1;
	}
	public static int moitieNeg(int num){
		if(num % 2 == 0){
			return (num/2);
		}
		return (num/2)-1;
	}
	public static float moitie(int num){
		return (float) ((float) num/2.0);
	}
	/** dit si il y'a des chiffres aprés la virgule */
	public static boolean checkVirguleFloat(float num){
		String test = ""+num;
		String[] tab = test.split("\\.");
		if(tab.length > 1 && (!tab[1].equals("0") || tab.length > 2)){
			return true;
		}
		return false;
	}
	/** dit si il y'a des chiffres aprés la virgule */
	public static boolean checkVirguleDouble(double num){
		String test = ""+num;
		String[] tab = test.split("\\.");
		if(tab.length > 1 && (!tab[1].equals("0") || tab.length > 2)){
			return true;
		}
		return false;
	}
//TODO a corriger et repenser
	/** fait un random jusqu'a un palier en suivant une courbe logarithmique */
	public int getRandTo(int to){
		double rand = Math.log(Double.parseDouble(to+".0")*Math.random());
		double palier = Math.log(to)/to;
		int resultat = 0;
		while(resultat*palier < rand){
			resultat++;
		}
		return resultat;
	}
	/** fait la moyenne */
	public static double avg(double a, double b){
		return (a+b)/2.0;
	}
	/** transforme un double pour avoir une valeur entiere */
	public static int doubleToInt(double num){
		if(Utils.checkVirguleDouble(num)){
			if(num < 0){
				return (int) (num-0.5);
			}
			else{
				return (int) (num+0.5);
			}
		}
		return (int) num;
	}
	/** transforme un float pour avoir une valeur entiere */
	public static int floatToInt(float num){
		if(Utils.checkVirguleFloat(num)){
			if(num < 0){
				return (int) (num-0.5);
			}
			else{
				return (int) (num+0.5);
			}
		}
		return (int) num;
	}
}
