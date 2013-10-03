package tools;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLTools {
	/**
	 * Prend un élément XML et le nom de son tag et retourne le contenu texte correspondant à ce 
	 * tag  
	 * @param ele le document XML
	 * @param tagName le nom du tag
	 * @return textVal le noeud feuille texte
	 */
	public static String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}
		return textVal;
	}
	
	/**
	 * Appel la fonction d'extraction texte et retourne un int
	 * @param ele
	 * @param tagName
	 * @return result l'élément converti en int
	 */
	public static int getIntValue(Element ele, String tagName) {
		int result = -1;
		try{
			result = Integer.parseInt(getTextValue(ele,tagName));
		}
		catch(Exception e){}
		return result;
	}
	public static double getDoubleValue(Element ele, String tagName) {
		double result = Double.parseDouble(getTextValue(ele,tagName));
		return result;
	}
}
