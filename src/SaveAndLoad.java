import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
//import java.util.zip.GZIPInputStream;
//import java.util.zip.GZIPOutputStream;

/**
 *
 * @author juri bieler
 */
public class SaveAndLoad{
   
   public SaveAndLoad(){
       
   }
    
   public void saveArray(String filename, String[][] output_veld) {
     try {
        FileOutputStream fos = new FileOutputStream(filename);
        //GZIPOutputStream gzos = new GZIPOutputStream(fos);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(output_veld);
        out.flush();
        out.close();
     }
     catch (IOException e) {
         System.out.println(e); 
     }
  }

  public String[][] loadArray(String filename) {
      try {
        FileInputStream fis = new FileInputStream(filename);
        //GZIPInputStream gzis = new GZIPInputStream(fis);
        ObjectInputStream in = new ObjectInputStream(fis);
        String[][] gelezen_veld = (String[][])in.readObject();
        in.close();
        return gelezen_veld;
      }
      catch (Exception e) {
          System.out.println(e);
      }
      return null;
  }
}
