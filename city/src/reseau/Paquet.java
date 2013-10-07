package reseau;

import java.nio.ByteBuffer;

import com.badlogic.gdx.math.Vector2;

/**
 * Classe gérant la transition Datagramme<->Paquet.
 * @author synril
 *
 */
public class Paquet {
	private byte[] datagramme = new byte[64];
	private int l, L, texX, texY, sens;
	private int type, id, id_q, classe;
	private float x, y, z;
	private boolean gimmi, ackbit, ping, pong;
	
	/**
	 * Constructeur de la classe Paquet dans le sens Paquet->Datagramme.
	 * @param L : longeur
	 * @param l : largeur
	 * @param x : coordonnée x
	 * @param y : coordonnée y
	 * @param z : coordonnée z
	 * @param texX : coordonnée de texture x
	 * @param texY : coordonnées de texture y
	 * @param sens : sens de la texture
	 * @param type : le type de batiments
	 * @param ID : l'ID du batiment
	 * @param classe : la classe de l'élément (batiment, bloc ou elements)
	 * @param id_q : l'identifiant du quartier
	 * @param gimmi : bit de demande d'envois de tout
	 * @param ackbit : bit d'acquittement
	 * @param ping : bit de ping
	 * @param pong : bit de pong
	 */
	public Paquet(int L, int l, float x, float y, float z, int texX, int texY, int sens, int type, int id, int classe, int id_q,
			boolean gimmi, boolean ackbit, boolean ping, boolean pong){
		this.l = l;
		this.L = L;
		this.x = x;
		this.y = y;
		this.z = z;
		this.texX = texX;
		this.texY = texY;
		this.sens = sens;
		this.ackbit = ackbit;
		this.ping = ping;
		this.pong = pong;
		this.type = type;
		this.id = id;
		this.id_q = id_q;
		this.classe = classe;
		//conversion
		byte[] res;
		//0-3 L
//		System.out.println(L);
		res = intToByte(this.L);
		datagramme[0] = res[0];
		datagramme[1] = res[1];
		datagramme[2] = res[2];
		datagramme[3] = res[3];
		//4-7 l
		res = intToByte(this.l);
		datagramme[4] = res[0];
		datagramme[5] = res[1];
		datagramme[6] = res[2];
		datagramme[7] = res[3];
		//8-11 x
		res = floatToByte(this.x);
		datagramme[8] = res[0];
		datagramme[9] = res[1];
		datagramme[10] = res[2];
		datagramme[11] = res[3];
		//12-15 y
		res = floatToByte(this.y);
		datagramme[12] = res[0];
		datagramme[13] = res[1];
		datagramme[14] = res[2];
		datagramme[15] = res[3];
		//16-19 z
		res = floatToByte(this.z);
		datagramme[16] = res[0];
		datagramme[17] = res[1];
		datagramme[18] = res[2];
		datagramme[19] = res[3];
		//20-23 texX
		res = intToByte(this.texX);
		datagramme[20] = res[0];
		datagramme[21] = res[1];
		datagramme[22] = res[2];
		datagramme[23] = res[3];
		//24-27 texY
		res = intToByte(this.texY);
		datagramme[24] = res[0];
		datagramme[25] = res[1];
		datagramme[26] = res[2];
		datagramme[27] = res[3];
		//28-31 sens
		res = intToByte(this.sens);
		datagramme[28] = res[0];
		datagramme[29] = res[1];
		datagramme[30] = res[2];
		datagramme[31] = res[3];
		//32-35 type
		res = intToByte(this.type);
		datagramme[32] = res[0];
		datagramme[33] = res[1];
		datagramme[34] = res[2];
		datagramme[35] = res[3];
		//36-39 id
		res = intToByte(this.id);
		datagramme[36] = res[0];
		datagramme[37] = res[1];
		datagramme[38] = res[2];
		datagramme[39] = res[3];
		//40-43 classe
		res = intToByte(this.classe);
		datagramme[40] = res[0];
		datagramme[41] = res[1];
		datagramme[42] = res[2];
		datagramme[43] = res[3];
		//46-49 id du quartier
		res = intToByte(this.id_q);
		datagramme[44] = res[0];
		datagramme[45] = res[1];
		datagramme[46] = res[2];
		datagramme[47] = res[3];
		//le reste à 0
		for(int t = 48; t < 60; t++){
			datagramme[t] = 0;
		}
		//booleens
		if(gimmi){
			datagramme[60] = 1;
		}
		if(ackbit){
			datagramme[61] = 1;
		}
		if(ping){
			datagramme[62] = 1;
		}
		if(pong){
			datagramme[63] = 1;
		}
	}
	
	/**
	 * Constructeur de la classe Paquet dans le sens Datagramme->Paquet.
	 * @param dat : Données reçues
	 */
	public Paquet(byte[] dat){
		this.L = byteToInt(new byte[]{dat[0], dat[1], dat[2], dat[3]});
		this.l = byteToInt(new byte[]{dat[4], dat[5], dat[6], dat[7]});
		this.x = byteToFloat(new byte[]{dat[8], dat[9], dat[10], dat[11]});
		this.y = byteToFloat(new byte[]{dat[12], dat[13], dat[14], dat[15]});
		this.z = byteToFloat(new byte[]{dat[16], dat[17], dat[18], dat[19]});
		this.texX = byteToInt(new byte[]{dat[20], dat[21], dat[22], dat[23]});
		this.texY = byteToInt(new byte[]{dat[24], dat[25], dat[26], dat[27]});
		this.sens = byteToInt(new byte[]{dat[28], dat[29], dat[30], dat[31]});
		this.type = byteToInt(new byte[]{dat[32], dat[33], dat[34], dat[35]});
		this.id = byteToInt(new byte[]{dat[36], dat[37], dat[38], dat[39]});
		this.classe = byteToInt(new byte[]{dat[40], dat[41], dat[42], dat[43]});
		this.id_q = byteToInt(new byte[]{dat[44], dat[45], dat[46], dat[47]});
		this.gimmi = false;
		if(dat[60] == 1){
			this.gimmi = true;
		}
		this.ackbit = false;
		if(dat[61] == 1){
			this.ackbit = true;
		}
		this.ping = false;
		if(dat[62] == 1){
			this.ping = true;
		}
		this.pong = false;
		if(dat[63] == 1){
			this.pong = true;
		}
		for (int i=0 ; i<64 ; i++){
			datagramme[i] = dat[i];
		}
	}
	
	public byte[] getDatagram(){
		return datagramme;
	}
	
	/** conversion d'un chiffre en tableau de byte*/
	private byte[] intToByte(int i){
		return ByteBuffer.allocate(4).putInt(i).array();
	}
	
	/** conversion d'un float en tableau de byte*/
	private byte[] floatToByte(float f){
		return ByteBuffer.allocate(4).putFloat(f).array();
	}
	
	private int byteToInt(byte[] tab){
		ByteBuffer b = ByteBuffer.wrap(tab);
		return b.getInt();
	}
	
	private float byteToFloat(byte[] tab){
		ByteBuffer b = ByteBuffer.wrap(tab);
		return b.getFloat();
	}
/*----------------------ACCESSEURS----------------------*/
	public int getL(){
		return this.L;
	}
	public int getl(){
		return this.l;
	}
	public float getX(){
		return this.x;
	}
	public float getY(){
		return this.y;
	}
	public float getZ(){
		return this.z;
	}
	public int getTexX(){
		return this.texX;
	}
	public int getTexY(){
		return this.texY;
	}
	public int getSens(){
		return this.sens;
	}
	public int getType(){
		return this.type;
	}
	public int getId(){
		return this.id;
	}
	public int getClasse(){
		return this.classe;
	}
	public int getId_q(){
		return this.id_q;
	}
	public Vector2 getVectorTranslate(){
		Vector2 vecteur = new Vector2();
		vecteur.x = this.x;
		vecteur.y = this.y;
		return vecteur;
	}
	public int getJour(){
		return this.id;
	}
	public int getHeure(){
		return this.L;
	}
	public int getAge(){
		return this.l;
	}
	public int getNbTotal(){
		return this.id_q;
	}
	public int getNext(){
		return this.texY;
	}
	public int getIDQn(){
		return this.texX;
	}
	public int getIDRail(){
		return this.L;
	}
	public int getClos(){
		return this.sens;
	}
}