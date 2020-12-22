
package GPS_MAP;

import java.io.IOException;
import java.net.*;
import java.util.TimerTask;
import javax.swing.JOptionPane;



public class UDPGps {
    
    
    public String p,ok,n_s,e_w,mode,f1,f2,f3,gps="$GNRMC",temporaryLatitude,temporaryLongitude;
    public float longi,lat,speed,heading_degree,d_o_U,m_v,v,time;
    public double longi2=0,lat2=0;
    public double longi_d=0,longi_m=0,longi_s=0;
    public double lat_d=0,lat_m=0,lat_s=0,degreeMinuteLatitude,degreeMinuteLongitude;
    public double ts1=0,ts2=0;
    
    public String sendData = "g";
    public String stopData = "k";
    private DatagramSocket datagramSocket = null;
    
    String ip;
    int port;
    int myPort = 4444;
    
    public UDPGps() throws SocketException
    {
        ip = GPS_MAP.start.ipAddress;
        port = GPS_MAP.start.portArduino;
        
        datagramSocket = new DatagramSocket(myPort);
    }

    
    public void initializeConnection() throws UnknownHostException, IOException
    {
        
        byte [] buffer = sendData.getBytes();
        
        InetAddress address = InetAddress.getByName(ip);
                        
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
         
        datagramSocket.send(packet);
    }
    
    public void endConnection() throws UnknownHostException, IOException
    {
        byte [] buffer = stopData.getBytes();
        
        InetAddress address = InetAddress.getByName(ip);
                        
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
         
        datagramSocket.send(packet);
        
        datagramSocket.close();
        datagramSocket.disconnect();
    }
    
    public void recieveGpsData() throws SocketException, UnknownHostException, IOException
    {     
        
        try
        {

            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            datagramSocket.setSoTimeout(4000);

            try
            {

                datagramSocket.receive(receivePacket);
                GPS_MAP.status = true;
                if(GPS_MAP.InputPanel != null)
                {
                    GPS_MAP.InputPanel.setStatus();
                }

            }
            catch(SocketTimeoutException e)
            {
                GPS_MAP.status = false;
                if(GPS_MAP.InputPanel != null)
                {
                    GPS_MAP.InputPanel.setStatus();
                }
                Object[] options = { "TRY AGAIN", "EXIT" };
                int answer = JOptionPane.showOptionDialog(null, "Connection to the rover is lost.\nCheck ethernet cable.", "Connection Lost",
                                                            JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
                                                            null, options, options[0]);

                if(answer == 0)
                {
                    initializeConnection();

                }
                else if(answer == 1)
                {
                    GPS_MAP.F.setVisible(false);
                    System.exit(0);
                }
            }                 

            String str = new String(receivePacket.getData());
            System.out.println("REcieved: "+str+"*");

            p=(str_piece(str, ',', 1));

            if(true)
            {
                System.err.println("recie "+str); 
                parseString(str);
                GPS_MAP.status = true;
                if(GPS_MAP.InputPanel != null)
                {

                    GPS_MAP.InputPanel.setStatus();
                }
                System.out.println("string parse");

            }
           
        }
        catch (Exception e)
        {
            System.out.println("in exception "+GPS_MAP.status);
            GPS_MAP.status = false;
            if(GPS_MAP.InputPanel != null)
            {
                GPS_MAP.InputPanel.setStatus();
            }

            /*Object[] options = { "TRY AGAIN", "EXIT" };
            int answer = JOptionPane.showOptionDialog(null, "Recieving invalid GPS Data.\nCheck GPS antenna and try again after some time", "Invalid GPS Data",
                                                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                                        null, options, options[0]);


            if(answer == 1)
            {
                GPS_MAP.F.setVisible(false);
                System.exit(0);
            }*/
        }

    }
    
    public void parseString(String str)
    {

        time=Float.parseFloat(str_piece(str, ',', 2));

        ok=str_piece(str, ',', 3);
        temporaryLatitude=str_piece(str, ',', 4);
        degreeMinuteLatitude=Double.parseDouble(str_piece(temporaryLatitude, '.', 1));
        //System.out.print("3.Current Latitude: ");
        lat2=Double.parseDouble(temporaryLatitude);
        lat2=lat2-degreeMinuteLatitude;
        //System.out.println("temp latitude" + lat2);
        lat_s=(lat2*60);
        lat_m=degreeMinuteLatitude%100;
        degreeMinuteLatitude=degreeMinuteLatitude-lat_m;
        lat_d=degreeMinuteLatitude/100;
        System.out.println(lat_d + " deg "+ lat_m +" min " + lat_s +" sec ");
        ts1=(lat_d*3600)+(lat_m*60)+lat_s;
        //System.out.println("Latitude total in sec: "+ts1);

        n_s=str_piece(str, ',', 5);
        //System.out.println("4.Latitude side: " + n_s);

        temporaryLongitude=str_piece(str, ',', 6);
        degreeMinuteLongitude=Double.parseDouble(str_piece(temporaryLongitude, '.', 1));
        //System.out.print("3.Current Longitude: ");
        longi2=Double.parseDouble(temporaryLongitude);
        longi2=longi2-degreeMinuteLongitude;
        //System.out.println("temp longitude" + longi2);
        longi_s=(longi2*60);
        longi_m=degreeMinuteLongitude%100;
        degreeMinuteLongitude=degreeMinuteLongitude-longi_m;
        longi_d=degreeMinuteLongitude/100;
        System.out.println(longi_d + " deg "+ longi_m +" min " + longi_s +" sec ");
        ts2=(longi_d*3600)+(longi_m*60)+longi_s;
        //System.out.println("Latitude total in sec: "+ts1);

        e_w=str_piece(str, ',', 7);
        //System.out.println("6.Longitude side: " + e_w);

        speed=Float.parseFloat(str_piece(str, ',', 8));
        //System.out.println("7.Speed in Knots: " + speed);

        d_o_U=Float.parseFloat(str_piece(str, ',', 10));
        //System.out.println("9.Date in UTC (DdMmAa): " + d_o_U);

        mode=str_piece(str, ',', 13);
        //System.out.println("12.Mode: " + mode);
    }
    public double getLattitudeInSeconds()
    {
        return ts1;
    }
    
    public double getLongitudeInSeconds()
    {
        return ts2;
    }
    
    private static String str_piece(String str, char separator, int index) 
    {
        String str_result = "";
        int count = 0;
        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) == separator) {
                count++;
                if(count == index) {
                    break;
                }
            }
            else {
                if(count == index-1) {
                    str_result += str.charAt(i);
                    }
            }
        }
        return str_result;
    }
   

}
           
