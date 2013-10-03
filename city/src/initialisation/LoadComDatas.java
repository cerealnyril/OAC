package initialisation;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tools.ParamsGlobals;


/** charge la premiere partie du fichier XML de commerce */
public class LoadComDatas {
	private Document com_doc;
	
	public ArrayList<String> load(int type){
		parseFile(ParamsGlobals.XML_COM_DATAS);
		return parseDocument(type);
	}
	private void parseFile(String file){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			com_doc = db.parse(file);
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	private ArrayList<String> parseDocument(int type){
		ArrayList<String> datas = new ArrayList<String>();
		NodeList commerces = com_doc.getElementsByTagName("commerce");
		int i = 0;
		while(i< commerces.getLength()){
			Element noeud = (Element) commerces.item(i);
			if(noeud.getAttribute("type").equalsIgnoreCase(""+type)){
				datas.add(noeud.getAttribute("nom"));
				datas.add(noeud.getElementsByTagName("vendeurs").item(0).getTextContent());
				datas.add(noeud.getElementsByTagName("artisants").item(0).getTextContent());
				datas.add(noeud.getElementsByTagName("revenus").item(0).getTextContent());
				datas.add(noeud.getElementsByTagName("energie").item(0).getTextContent());
				datas.add(noeud.getElementsByTagName("physique").item(0).getTextContent());
				i = commerces.getLength();
			}
			i++;
		}
		return datas;
	}
}
