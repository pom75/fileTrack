import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



public class FSTree {

	private Noeud n;
	private String nomF;
	
	public FSTree(Noeud n){
		this.n=n;
	}
	
	
	public Noeud getN(){
		return n;
	}
	
	public void setN(Noeud n){
		this.n=n;
	}
	
	public void ecrire(String s) throws IOException{
		try{
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(s));
		out.writeObject(n);
		out.close();
		}catch(NotSerializableException e){
			System.out.println(e);
		}catch(NullPointerException e){
			System.out.println(e);
		}
		
	}
	
	public void lire(String s) throws FileNotFoundException, IOException, ClassNotFoundException{	
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(s));
			n = (Noeud) in.readObject();
			in.close();
			}catch(NotSerializableException e){
				System.out.println(e);
			}
			
	}
}
