import java.io.Serializable;
import java.util.*;
public class Noeud implements Serializable
{

	private static final long serialVersionUID = -5103847058702187594L;
	public Noeud parent;
	public String Texte;
	public static Noeud Racine;
	public ArrayList<Object> Liste=new ArrayList<Object>();



	Noeud(String txt)
	{
		this.Texte=txt;
	}
	public static Noeud getRacine()
	{
		return Noeud.Racine;
	}
	public static void setRacine(Noeud r)
	{
		Noeud.Racine=r;
	}
	public void addFils(Noeud n)
	{
		Liste.add(n);
	}
	public void addFeuille(FileInfo f)
	{
		Liste.add(f);
		
	}
	public void setTexte(String s){
		Texte= s;
	}
	
	public String toString(){
		return Texte;
	}

	public void affiche(){
		for(int i=0;i<Liste.size();i++){
			if(Liste.get(i).getClass().getName().contentEquals("Noeud")){
				System.out.println(Liste.get(i).toString());
				((Noeud) Liste.get(i)).affiche();
			}else{
				System.out.println(Liste.get(i).toString());
			}

		}
	}

	public void trouve(String s,String chemin){
		for(int i=0;i<Liste.size();i++){
			if(Liste.get(i).getClass().getName().contentEquals("Noeud")){

				((Noeud) Liste.get(i)).trouve(s,chemin+Texte+"/");
			}else if(s.charAt(0) == '*'){
				if(((FileInfo) Liste.get(i)).getNom().endsWith(s.substring(1,s.length()))){

					System.out.println(chemin+Texte+"/"+(((FileInfo) Liste.get(i)).getNom())+" "+Liste.get(i).toString());

				}
			}else if(s.charAt(s.length()-1)=='*'){ 
				if(((FileInfo) Liste.get(i)).getNom().startsWith(s.substring(0, s.length()-1))){

					System.out.println(chemin+Texte+"/"+(((FileInfo) Liste.get(i)).getNom())+" "+Liste.get(i).toString());

				}
			}else{
				if(((FileInfo) Liste.get(i)).getNom().contentEquals(s)){
					System.out.println(chemin+Texte+"/"+(((FileInfo) Liste.get(i)).getNom())+" "+Liste.get(i).toString());
				}

			}

		}
	}

	public void montreDiff(Noeud n,String chemin){
		int t = 0;
		String buff="";
		//this nouveau chemin // n ancien chemin

		
		//Dossier et fichier Modifier ou ajoutŽ
		for(int i=0;i<Liste.size();i++){
			if(Liste.get(i).getClass().getName().contentEquals("Noeud")){
				for(int f=0;f<n.Liste.size();f++){
					if(n.Liste.get(f).getClass().getName().contentEquals("Noeud")){
						if(((Noeud) Liste.get(i)).Texte.contentEquals(((Noeud) n.Liste.get(f)).Texte)){
							t=1;
							//ancien dossier
							((Noeud) Liste.get(i)).montreDiff((Noeud) n.Liste.get(f),chemin+"/"+((Noeud) n.Liste.get(f)).Texte);
						}
					}
				}
				if(t==0){
					System.out.println("Nouveau Dossier "+chemin+"/"+( Liste.get(i)).toString());
					afficheAjout((Noeud) Liste.get(i),chemin+"/"+((Noeud) Liste.get(i)).Texte);
					//Nouveau DossiŽ affiche tous en tan que nouveau

				}	
			}else{
				for(int j=0;j < n.Liste.size();j++){
					if(n.Liste.get(j).getClass().getName().contentEquals("FileInfo") && Liste.get(i).getClass().getName().contentEquals("FileInfo")){ 
						//Compart tous
						if( ((FileInfo) n.Liste.get(j)).egal(Liste.get(i).toString())){
							t=1;

						//compart modification 
						}else if(((FileInfo) n.Liste.get(j)).getNom().contentEquals(((FileInfo) Liste.get(i)).getNom())){
							t=1;
							System.out.println("FichiŽ ModifiŽ "+chemin+" "+(Liste.get(i)).toString());
						}
					}
				}
				if(t==0){
					System.out.println("FichiŽ AjoutŽ "+chemin+" "+(Liste.get(i)).toString());
					//Nouveau Fichier
				}

			}
			t=0;
		}
		 
		//Dossier et fichier suprimŽ
		
		for(int i=0;i<n.Liste.size();i++){
			if(n.Liste.get(i).getClass().getName().contentEquals("Noeud")){
				for(int f=0;f<Liste.size();f++){
					if(Liste.get(f).getClass().getName().contentEquals("Noeud")){
						if(((Noeud) n.Liste.get(i)).Texte.contentEquals(((Noeud) Liste.get(f)).Texte)){
							t=1;
							
							((Noeud) Liste.get(f)).montreDiff((Noeud) Liste.get(f),chemin+"/"+((Noeud) Liste.get(f)).Texte);
						}
					}
				}
				if(t==0){
					System.out.println("Dossier SuprimŽ"+chemin+"/"+( n.Liste.get(i)).toString());
					afficheSupp((Noeud) n.Liste.get(i),chemin+"/"+((Noeud) n.Liste.get(i)).Texte);
					

				}	
			}else{
				for(int j=0;j < Liste.size();j++){
					if(Liste.get(j).getClass().getName().contentEquals("FileInfo") && n.Liste.get(i).getClass().getName().contentEquals("FileInfo")){ 
						
						if( ((FileInfo) Liste.get(j)).egal(n.Liste.get(i).toString())){
							t=1;

						
						}else if(((FileInfo) Liste.get(j)).getNom().contentEquals(((FileInfo) n.Liste.get(i)).getNom())){
							t=1;
							
						}
					}
				}
				if(t==0){
					System.out.println("FichiŽ SuprimŽ "+chemin+" "+(n.Liste.get(i)).toString());
					
				}

			}
			t=0;
		}
		
		
	}
	
	public void afficheAjout(Noeud n,String chemin){
		for(int i=0;i<n.Liste.size();i++){
			if(n.Liste.get(i).getClass().getName().contentEquals("Noeud")){
				System.out.println("Nouveau Dossier "+chemin+"/"+ n.Liste.get(i).toString());
				((Noeud) n.Liste.get(i)).afficheAjout((Noeud) n.Liste.get(i),chemin+"/"+((Noeud) n.Liste.get(i)).Texte);
			}else{
				System.out.println("FichiŽ AjoutŽ "+chemin+" "+( n.Liste.get(i)).toString());			}

		}
	}
	
	public void afficheSupp(Noeud n,String chemin){
		for(int i=0;i<n.Liste.size();i++){
			if(n.Liste.get(i).getClass().getName().contentEquals("Noeud")){
				System.out.println("Dossier SupprimŽ"+chemin+"/"+ n.Liste.get(i).toString());
				((Noeud) n.Liste.get(i)).afficheSupp((Noeud) n.Liste.get(i),chemin+"/"+((Noeud) n.Liste.get(i)).Texte);
			}else{
				System.out.println("FichiŽ SuprimŽ "+chemin+" "+( n.Liste.get(i)).toString());			}

		}
	}

}