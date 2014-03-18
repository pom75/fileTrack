import java.io.Serializable;


public class FileInfo implements Serializable {
	
	private static final long serialVersionUID = -4785089487478827044L;
	private String nom;
	private long taille;
	private long DateModif;
	
	public FileInfo(String nom,long taille,long DateModif){
		this.nom=nom;
		this.setTaille(taille);
		this.DateModif=DateModif;
		
	}
	
	public boolean egal(String s){
		return this.toString().contentEquals(s);
	}

	public long getTaille() {
		return taille;
	}

	public void setTaille(long taille) {
		this.taille = taille;
	}
	
	
	
	public String toString(){
		return nom+" "+taille+" "+DateModif;
	}
	
	public String getNom(){
		return nom;
	}
	
	
}
