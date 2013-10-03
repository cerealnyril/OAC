package initialisation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tools.ParamsGlobals;

public class LoadJobDatas {
	private Document com_doc;
	ArrayList<Double> params_job;
	TreeMap<Integer, Double> param_factions;
	private String type_nom;
	
	public LoadJobDatas(int type){
		this.params_job = new ArrayList<Double>();
		this.param_factions = new TreeMap<Integer, Double>();
		this.type_nom = "vide";
		parseFile(ParamsGlobals.XML_JOB_DATAS);
		parseJobParams(type);
		parseJobFaction(type);
	}
/*--------------------------------ACCESSEURS------------------------------*/
	public ArrayList<Double> getJobParams(){
		return this.params_job;
	}
	public TreeMap<Integer, Double> getJobFaction(){
		return this.param_factions;
	}
	public String getNom(){
		return this.type_nom;
	}
/*--------------------------------CHARGEMENT------------------------------*/
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
	private void parseJobParams(int type){
		NodeList commerces = com_doc.getElementsByTagName("metier");
		int i = 0;
		while(i< commerces.getLength()){
			Element noeud = (Element) commerces.item(i);
			if(noeud.getAttribute("type").equalsIgnoreCase(""+type)){
				this.type_nom = noeud.getAttribute("nom");
				this.params_job.add(Double.parseDouble(noeud.getElementsByTagName("education").item(0).getTextContent()));
				this.params_job.add(Double.parseDouble(noeud.getElementsByTagName("qi").item(0).getTextContent()));
				this.params_job.add(Double.parseDouble(noeud.getElementsByTagName("ratiohf").item(0).getTextContent()));
				this.params_job.add(Double.parseDouble(noeud.getElementsByTagName("age").item(0).getTextContent()));
				this.params_job.add(Double.parseDouble(noeud.getElementsByTagName("desirabilite").item(0).getTextContent()));
				i = commerces.getLength();
			}
			i++;
		}
	}
	private void parseJobFaction(int type){
		NodeList commerces = com_doc.getElementsByTagName("metier");
		int i = 0;
		while(i< commerces.getLength()){
			Element noeud = (Element) commerces.item(i);
			if(noeud.getAttribute("type").equalsIgnoreCase(""+type)){
				NodeList factions = noeud.getElementsByTagName("faction");
				if(factions != null){
					for(int f = 0; f < factions.getLength(); f++){
						Element faction = (Element) factions.item(f);
						int id = Integer.parseInt(faction.getAttribute("id"));
						this.param_factions.put(id, Double.parseDouble(faction.getTextContent()));
					}
				}
				i = commerces.getLength();
			}
			i++;
		}
	}
}
