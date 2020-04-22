import java.util.*;
import java.io.*;

/**
 *
 * @author juri bieler
 */

public class Ini{
  private String     path;
  private Properties p;
  
  public Ini(String path){
     this.path = path;
     readIni();
  }
  
  public Ini(){
     this.path = "config.ini";
     readIni();
  }
  
  public void setPath(String path){
     this.path = path;
  }
  
  //post:liest die ini-Datei ein und speichert sie in der Klassenvariable p
  public boolean readIni(){
     try{
        if ((new File(path)).exists()){
           p = new Properties();
           p.load(new FileInputStream(path));
           return true;
        }
        return false;

     }
     catch (Exception e){
        System.out.println(e);
     }
     return false;
  }
  
  public void createIni(){
     write(path, "");
  }
  
  public void write(String path, String stg){
     try{
        BufferedWriter out = new BufferedWriter(
        new OutputStreamWriter(
        new FileOutputStream(path) ) );
        out.write(stg);
        out.newLine();
        out.close();
      }
      catch (IOException e) {  }
   }
  
  //post:liest den Wert der ini an der stelle (key) data ein
  public String getData(String data){
	 if(p == null) {
		 System.out.println("no config file");
		 return "";
	 }
     if (p.containsKey(data)){
        return p.getProperty(data);
     }
     return "";
  }
  
  //post:aendert den Eintraf an der stelle data zum Wert val, und ueberschreibt dies in der ini
  public void save(String data, String val){
    try{
       p.put(data,val);
       FileOutputStream out = new FileOutputStream(path);
       p.store(out, "[TimeKeeper 2.0]");
    }
    catch (Exception e){
       System.out.println(e);
    }
  }

}