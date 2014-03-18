import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;


public class FileSystemTree {

	/**
	 * @param args
	 * @throws IOException 
	 */
	static Noeud rac = new Noeud("/");
	public static int nbF=0;
	public static long tmin=Long.MAX_VALUE;
	public static long tmax=Long.MIN_VALUE;
	public static long ttot=0;
	
	public static void main(String[] args) throws IOException {
		rac.setRacine(rac);
		rac.parent=rac;
		

		try{
			if("locate".contentEquals(args[0]) && args[1].endsWith(".fstree")){
				locate2(args[1],args[2]);
			}else if("locate".contentEquals(args[0])){
				locate(args[1],args[2]);
			}else if("create".contentEquals(args[0])){
				if(!args[1].endsWith(".fstree"))  throw new
				AssertionError("Erreur , mauvaise extention de fichier");
				rac.setTexte(args[2]);
				create(args[1],args[2],rac);
			}else if("update".contentEquals(args[0])){
				update(args[1]);
			}else if("stat".contentEquals(args[0])){
				
				FSTree fs = new FSTree(null);
				try {
					fs.lire(args[1]);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				Noeud chemin = fs.getN();
				stat(chemin,args[2]);
				
				
				System.out.println("Nombre de fichiers trouvés: "+nbF);
				if(nbF !=0){
				System.out.println("Taille moyenner: "+ttot/nbF);
				}else{
					System.out.println("Taille moyenner: 0");
				}
				System.out.println("Taille du plus petit:: "+tmin);
				System.out.println("Taille du plus gros: "+tmax);
				System.out.println("Taille totale:  "+ttot);

			}
		}catch(ArrayIndexOutOfBoundsException a){
			System.out.println("Erreur , Aide : java FileSystemTree locate directory_path file_name \n java FileSystemTree create mytree.fstree directory_path \n java FileSystemTree locate mytree.fstree file_name \n java FileSystemTree update mytree.fstree \n java FileSystemTree stat mytree.fstree file_name");
		}catch(AssertionError e){
			System.out.println(e);
		}

	}
	public static void stat(Noeud n,String fn) throws FileNotFoundException, IOException{

		for(int i=0;i<n.Liste.size();i++){
			
			if(n.Liste.get(i).getClass().getName().contentEquals("FileInfo")){
				
				if(fn.charAt(0) == '*'){
					if(((FileInfo) n.Liste.get(i)).getNom().endsWith(fn.substring(1,fn.length()))){
						nbF++;
						ttot+=((FileInfo)n.Liste.get(i)).getTaille();
						if(((FileInfo)n.Liste.get(i)).getTaille()<tmin){
							tmin =((FileInfo)n.Liste.get(i)).getTaille();
						}
						if(((FileInfo)n.Liste.get(i)).getTaille()>tmax){
							tmax=((FileInfo)n.Liste.get(i)).getTaille();
						}




					}
				}else if(fn.charAt(fn.length()-1)=='*'){ 
					if(((FileInfo) n.Liste.get(i)).getNom().startsWith(fn.substring(0, fn.length()-1))){

						nbF++;
						ttot+=((FileInfo)n.Liste.get(i)).getTaille();
						if(((FileInfo)n.Liste.get(i)).getTaille()<tmin){
							tmin =((FileInfo)n.Liste.get(i)).getTaille();
						}
						if(((FileInfo)n.Liste.get(i)).getTaille()>tmax){
							tmax=((FileInfo)n.Liste.get(i)).getTaille();
						}

					}
				}else {
					if(((FileInfo) n.Liste.get(i)).getNom().contentEquals(fn)){

						nbF++;
						ttot+=((FileInfo)n.Liste.get(i)).getTaille();
						if(((FileInfo)n.Liste.get(i)).getTaille()<tmin){
							tmin =((FileInfo)n.Liste.get(i)).getTaille();
						}
						if(((FileInfo)n.Liste.get(i)).getTaille()>tmax){
							tmax=((FileInfo)n.Liste.get(i)).getTaille();
						}


					}

				}
			}
			if(n.Liste.get(i).getClass().getName().contentEquals("Noeud")){
				stat((Noeud) n.Liste.get(i),fn );
			}


		}




	}
	
	
	public static void locate(String dp,String fn) throws IOException{

		try{
			String cheminRepertoire = dp;
			File repertoire = new File(cheminRepertoire);
			String[] fichiers = repertoire.list();
			File [] fichier = repertoire.listFiles();

			for(int i=0;i<fichiers.length;i++){

				if(fn.charAt(0) == '*'){
					if(fichiers[i].endsWith(fn.substring(1,fn.length()))){


						System.out.println(fichier[i].getAbsolutePath() +" "+  fichier[i].length()+" "+ new Date(fichier[i].lastModified()));

					}
				}else if(fn.charAt(fn.length()-1)=='*'){ 
					if(fichiers[i].startsWith(fn.substring(0, fn.length()-1))){

						System.out.println(fichier[i].getAbsolutePath() +"  "+  fichier[i].length()+"  "+ new Date(fichier[i].lastModified()));

					}
				}else {
					if(fichiers[i].contentEquals(fn)){


					System.out.println(fichier[i].getAbsolutePath() +" "+  fichier[i].length()+" "+ new Date(fichier[i].lastModified()));

					}
					
				}
				if(!isSymlink(fichier[i]) && fichier[i].isDirectory()){
				locate(dp+"/"+fichiers[i],fn );
				}


			}
		}catch(NullPointerException a){
			System.out.println("Chemin introuvable");
		}



	}
	
	public static void locate2(String nm,String fn) throws IOException{

		
			FSTree fs = new FSTree(null);
			try {
				fs.lire(nm);
			} catch (ClassNotFoundException e) {
			
				e.printStackTrace();
			}
			Noeud chemin = fs.getN();
			
			chemin.trouve(fn,null);
			
			

	}
	

	public static void create(String treeName,String dp,Noeud noeud) throws IOException{
		try{
			
			String cheminRepertoire = dp;
			File repertoire = new File(cheminRepertoire);
			String[] fichiers = repertoire.list();
			File [] fichier = repertoire.listFiles();

			for(int i=0;i<fichiers.length;i++){

	
				if(!fichier[i].isDirectory()){
						FileInfo f = new FileInfo(fichiers[i],fichier[i].length(),fichier[i].lastModified());
						noeud.addFeuille(f);
				}
				
				if(!isSymlink(fichier[i]) && fichier[i].isDirectory()){
					Noeud n =new Noeud(fichiers[i]);
					noeud.addFils(n);
					n.parent=noeud;
					create(treeName,dp+"/"+fichiers[i],n);
				}

				
			}
		}catch(NullPointerException a){
			System.out.println("Chemin introuvable");
		}
		File race = new File(rac.Texte);
		
		rac.setTexte(race.getAbsolutePath());
		FSTree fs = new FSTree(rac);
		fs.ecrire(treeName);
		
	}
	
	public static void update(String nm) throws FileNotFoundException, IOException{
		FSTree fs = new FSTree(null);
		try {
			fs.lire(nm);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Noeud chemin = fs.getN();
		Noeud n = new Noeud(chemin.Texte);
		File race = new File(chemin.Texte);	
		rac.setTexte(race.getName());
		n.setTexte(race.getName());
		rac = n;
		create(nm,race.getName(),rac);
		n.montreDiff(fs.getN(),chemin.Texte);
	}
	
	
	public static boolean isSymlink(File file) throws IOException {
		  if (file == null)
		    throw new NullPointerException("File must not be null");
		  File canon;
		  if (file.getParent() == null) {
		    canon = file;
		  } else {
		    File canonDir = file.getParentFile().getCanonicalFile();
		    canon = new File(canonDir, file.getName());
		  }
		  return !canon.getCanonicalFile().equals(canon.getAbsoluteFile());
		}
}
/*
 * 
 * 
 */
