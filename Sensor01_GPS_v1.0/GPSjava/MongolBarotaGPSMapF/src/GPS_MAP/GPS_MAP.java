

package GPS_MAP;


import java.net.SocketException;
import javax.swing.*;

public class GPS_MAP {
    
    public static MapSettings start;
    public static JFrame F;
    public static JPanel MasterPanel;
    public static otherPanel InputPanel;
    public static Map M;
    public static ImageIcon img;
    public static boolean status;


    public static void main(String[] args) throws SocketException {
        status = true;
        start = new MapSettings();
        start.setVisible(true);        
    }

    
       
    
}
