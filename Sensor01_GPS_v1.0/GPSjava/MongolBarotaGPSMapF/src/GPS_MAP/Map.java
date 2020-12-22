package GPS_MAP;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Time;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;


public class Map extends JPanel implements MouseMotionListener
{
    
    java.util.Timer time;
    
    Map.TimeLimitTask T;
    
    public Point rover;
    public Point controlStation;
    public Point map_points[];
    public int no_of_points;
    
    private float mouseX;
    private float mouseY;
    private double mouseLat;
    private double mouseLong;
    
    private Image background;
    private Image control_station;
    private Image rover_image;
    private Image pickup_point;
    private Image delivery_point;
    private Image other_point;
    private ImageIcon img;
    
    public java.util.List<String> combobox_names = new java.util.ArrayList<String>();
    
    public Queue<Point> Q;
    public UDPGps UDP;
    
    public final int averageLimit = 30;
    
    public Map() throws SocketException, UnknownHostException, IOException
    {
        Q = new LinkedList<Point>();
         
        addMouseMotionListener(this);
        setPreferredSize(new Dimension(980,700));
        
        no_of_points = 0;
        map_points = new Point[2000];
        
        rover = new Point();
        
        controlStation = new Point();
        controlStation.create_point(GPS_MAP.start.getLattitudeInSeconds(), GPS_MAP.start.getLongitudeInSeconds(),"Control Station","Control Station");
        
        double radius = 2 * GPS_MAP.start.getRadius();
        
        Point.setConstants(controlStation, radius);
        controlStation.create_point(GPS_MAP.start.getLattitudeInSeconds(), GPS_MAP.start.getLongitudeInSeconds(),"Control Station","Control Station");

                
        UDP = new UDPGps();
        UDP.initializeConnection();
        
      
        T=new Map.TimeLimitTask();
        time=new java.util.Timer();
        time.schedule(T,100,100);

        //img=new ImageIcon(this.getClass().getResource("mist.png"));

        img=new ImageIcon(this.getClass().getResource("background.png"));
        //img=new ImageIcon(this.getClass().getResource("9.jpg"));
        background = img.getImage();
                
        img=new ImageIcon(this.getClass().getResource("rover.png"));
        rover_image = img.getImage();
        
        img=new ImageIcon(this.getClass().getResource("tent.png"));
        control_station = img.getImage();
        
        img=new ImageIcon(this.getClass().getResource("pickup_point.png"));
        pickup_point = img.getImage();
        
        img=new ImageIcon(this.getClass().getResource("delivery_point.png"));
        delivery_point = img.getImage();
        
        img=new ImageIcon(this.getClass().getResource("other.png"));
        other_point = img.getImage();
       
        
        rover_point();
    }
    
    public void rover_point()
    {
        rover.create_point(85840, 325290, "Rover", "R");
    }
    
    
    public void add_new_map_point(double lat, double lon, String nm, String tp)
    {
        if(lon < Point.X_OFFSET)
        {
            JOptionPane.showMessageDialog(null, "Point is out of range.");
        }
        else if(lon > Point.X_MAX)
        {
            JOptionPane.showMessageDialog(null, "Point is out of range.");

        }
        else if(lat < Point.Y_OFFSET)
        {
            JOptionPane.showMessageDialog(null, "Point is out of range.");

        }
        else if(lat > Point.Y_MAX)
        {
            JOptionPane.showMessageDialog(null, "Point is out of range.");

        }
        else
        {
            if(no_of_points == 2000)
            {
                JOptionPane.showMessageDialog(null, "Unable to add more points.");
            }
            else
            {
                map_points[no_of_points]= new Point();
                map_points[no_of_points].create_point(lat, lon, nm, tp);
                //System.out.println("here!!!!!! "+no_of_points+" "+map_points[no_of_points].lattitude+" "+map_points[no_of_points].longitude);
                
                combobox_names.add(nm);
                no_of_points++;
                if(tp.equals("Pickup Point"))
                {
                    System.out.println("one pick up point added.");
                    GPS_MAP.InputPanel.pickup_point_no++;
                }
                else if(tp.equals("Delivery Point"))
                {
                    System.out.println("one delivery point added.");
                    GPS_MAP.InputPanel.delivery_point_no++;
                }
                else if(tp.equals("Other"))
                {
                    System.out.println("one other point added.");
                    GPS_MAP.InputPanel.other_point_no++;
                }
            }
        }
    }
    
    
    
     public void paint(Graphics g) 
     {
         System.out.println("In paint");
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        
        
        g2d.drawImage(background, 0, 0, null);
        
        //g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("TimesRoman", Font.BOLD, 16));

        g2d.drawImage(control_station, controlStation.x-45, controlStation.y-45, null);
        g2d.drawString(controlStation.name, controlStation.x-35, controlStation.y+40);
        
        
        /*if(Q.size()>= averageLimit)
        {
            System.out.println("doing average "+Q.size());
            Point AveragePosition = new Point();
            AveragePosition.create_point(getAverageLattitude(), getAverageLongitude(), "Rover", "R");
            
            g2d.drawImage(rover_image, AveragePosition.x, AveragePosition.y, null);
            g2d.drawString(AveragePosition.name, AveragePosition.x+10, rover.y+50);
        }

        else
        {
            System.out.println("not doing average "+Q.size());
            g2d.drawImage(rover_image, rover.x, rover.y, null);
            g2d.drawString(rover.name, rover.x+10, rover.y+50);
        }*/
        Point AveragePosition = new Point();
        AveragePosition.create_point(getAverageLattitude(), getAverageLongitude(), "Rover", "R");

        g2d.drawImage(rover_image, AveragePosition.x, AveragePosition.y, null);
        g2d.drawString(AveragePosition.name, AveragePosition.x+10, rover.y+50);
        
        //g2d.drawImage(rover_image, rover.x, rover.y, null);
        //g2d.drawString(rover.name, rover.x+10, rover.y+50);
        
        // mouse cursor position showing
        
        int degreeLat = (int) (mouseLat/3600);
        double temp = (mouseLat/3600) - degreeLat;
        temp = temp * 60;
        int minuteLat = (int) temp;
        temp = temp - minuteLat;
        temp = temp*60;
        int secondLat = (int) temp;

        int degreeLong = (int) (mouseLong/3600);
        temp = (mouseLong/3600) - degreeLong;
        temp = temp * 60;
        int minuteLong = (int) temp;
        temp = temp - minuteLong;
        temp = temp*60;
        int secondLong = (int) temp;
        
        
        String mousePositionLat = Integer.toString(degreeLat)+ "\u00B0 "+Integer.toString(minuteLat)+"' "+Integer.toString(secondLat)+"\"";
        String mousePositionLong = Integer.toString(degreeLong)+ "\u00B0 "+Integer.toString(minuteLong)+"' "+Integer.toString(secondLong)+ "\"";

        g2d.drawString(mousePositionLat, mouseX, mouseY);
        g2d.drawString(mousePositionLong, mouseX, mouseY+15);

        //draw map
        for(int i=0; i < no_of_points; i++)
        {
            if(map_points[i].valid == true)
            {
                if(map_points[i].type=="Pickup Point" )
                {

                    g2d.drawImage(pickup_point, map_points[i].x-5, map_points[i].y-10, null);
                    g2d.drawString(map_points[i].name, map_points[i].x-50, map_points[i].y+30);

                }
                else if(map_points[i].type=="Delivery Point")
                {

                    g2d.drawImage(delivery_point, map_points[i].x-15, map_points[i].y-10, null);
                    g2d.drawString(map_points[i].name, map_points[i].x-50, map_points[i].y+50);

                }
                else
                {

                    g2d.drawImage(other_point, map_points[i].x-5, map_points[i].y-10, null);
                    g2d.drawString(map_points[i].name, map_points[i].x-50, map_points[i].y+30);

                }
            }
            
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        eventOutput(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        eventOutput(e);
    }
     void eventOutput(MouseEvent e) {
       
        Point a = new Point();
        mouseLong = (e.getX() / a.PIXELS_PER_SECOND_X + a.X_OFFSET);
        mouseLat = (a.Y_MAX - (e.getY() / a.PIXELS_PER_SECOND_Y ) );
    
        mouseX = e.getX();
        mouseY = e.getY();
    }

    class TimeLimitTask extends TimerTask
    {
        

        @Override
        public void run() 
        {
            System.out.println("Queue size: "+Q.size());
            try 
            {
                UDP.recieveGpsData();                
            } 
            catch (SocketException ex) 
            {
                Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (UnknownHostException ex) 
            {
                Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
            }

            
            double lattitude = UDP.getLattitudeInSeconds();
            double longitude = UDP.getLongitudeInSeconds();
       
            
            rover.create_point(lattitude, longitude, "Rover", "R");
            
            if(Q.size() == averageLimit)
            {
                Q.remove();
            }
            Q.add(rover);
            
            if(GPS_MAP.InputPanel!=null)
            {
            
                distance();
                if(GPS_MAP.status == true)
                {
                    GPS_MAP.InputPanel.UpdateLog();
                }
                GPS_MAP.InputPanel.setStatus();
            }
            repaint();
   
        }

    }
    
    public double getAverageLattitude()
    {
        double sum = 0;
        for(int i=0; i< Q.size(); i++)
        {
            sum += Q.element().lattitude;
        }
        sum = sum/Q.size();
        return sum;
    }
    
    public double getAverageLongitude()
    {
        double sum = 0;
        for(int i=0; i< Q.size(); i++)
        {
            sum += Q.element().longitude;
        }
        sum = sum/Q.size();
        return sum;
    }
         
    public void distance()
    {
        String from = GPS_MAP.InputPanel.distancePointFrom;
        String to = GPS_MAP.InputPanel.distancePointTo;
        double fromLat = 0;
        double fromLon = 0;
        double toLat = 0;
        double toLon = 0;
        int found = 0;
        boolean foundFrom = false;
        boolean foundTo = false;
       
        
        
        if(from.equals(rover.name)==true)
        {
            fromLat = rover.lattitude;
            fromLon = rover.longitude;
            foundFrom = true;
        }
        else if(from.equals(controlStation.name)==true)
        {
            fromLat = controlStation.lattitude;
            fromLon = controlStation.longitude;
            foundFrom = true;
        }
        
        if(to.equals(rover.name)==true)
        {
            toLat = rover.lattitude;
            toLon = rover.longitude;
            foundTo = true;
        }
        else if(to.equals(controlStation.name)==true)
        {
            toLat = controlStation.lattitude;
            toLon = controlStation.longitude;
            foundTo = true;
        }
        
        if(foundFrom == false || foundTo == false)
        {
            for(int i=0; i< no_of_points; i++)
            {
                if(map_points[i].valid == true)
                {
                    if(foundFrom == false && from.equals(map_points[i].name) == true)
                    {
                        fromLat = map_points[i].lattitude;
                        fromLon = map_points[i].longitude;
                        found++;
                    }
                    if(foundTo == false && to.equals(map_points[i].name) == true)
                    {
                        toLat = map_points[i].lattitude;
                        toLon = map_points[i].longitude;
                        found++;
                    }
                }
                if(found == 2)  break;
            }
        }
                
        double distance = 1000 * ( calculateDistance(fromLat / 3600, fromLon / 3600, toLat / 3600, toLon / 3600) );
        GPS_MAP.InputPanel.showDistance.setText(Double.toString(distance));
        
    }
    public double calculateDistance(double lat1, double lng1, double lat2, double lng2) 
    {
        //haversine formula
        int r = 6371; // average radius of the earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + 
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * 
                Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = r * c;
        return d;
    }
    public void zoom(int difference)
    {
        Point.setConstants(controlStation , Point.distance_in_both_axis + difference);
        
        controlStation.create_point(controlStation.lattitude, controlStation.longitude, controlStation.name, controlStation.type);
        rover.create_point(rover.lattitude, rover.longitude, rover.name, rover.type);
        for(int i=0; i< no_of_points; i++)
        {
            if(map_points[i].valid == true)
            {
                map_points[i].create_point(map_points[i].lattitude, map_points[i].longitude, map_points[i].name, map_points[i].type);
            }
        }
    }
    
}
/*
class Coordinate
{
    public int degree;
    public int minute;
    public double second;

    
    public static Coordinate getCoordinateFromSeconds(double totalSecond)
    {
        Coordinate returnValue = new Coordinate();
        
        double degreeTotal = totalSecond / 3600;
        int degreeRounded = (int) degreeTotal;
        
        double minuteTotal = degreeTotal - degreeRounded;
        int minuteRounded = (int) minuteTotal;
        
        double secondTotal = minuteTotal - minuteRounded;
        
        returnValue.degree = degreeRounded;
        returnValue.minute = minuteRounded;
        returnValue.second = secondTotal;
        
        return returnValue;
        
    }
    
}
*/
class Point
{
    public double lattitude=0;
    public double longitude=0;
    public int x=0;
    public int y=0;
    public String name="";
    public String type="";
    public boolean valid;
    
    public final static int MAP_HEIGHT = 690;
    public final static int MAP_WIDTH = 970; 
    
    public final static double FEET_PER_SECOND_LATTITUDE = 101.2;
    public final static double FEET_PER_SECOND_LONGITUDE = 61.6; 

    public final static double FEET_PER_METER = 3.280839902; 

    
    public static double X_OFFSET ;
    public static double X_MAX ;
    
    public static double Y_OFFSET ;
    public static double Y_MAX ;
    
    public static int PIXELS_PER_SECOND_X ;
    public static int PIXELS_PER_SECOND_Y ;
    
    public static double distance_in_both_axis;
    
    public Point()
    {
        valid = true;
    }
    
    public static void setConstants(Point center,double radius)
    {
        System.out.println("R: "+radius);
        distance_in_both_axis = radius;

        PIXELS_PER_SECOND_X = (int)((FEET_PER_SECOND_LONGITUDE*MAP_WIDTH)/(distance_in_both_axis*FEET_PER_METER));
        
        PIXELS_PER_SECOND_Y = (int)((FEET_PER_SECOND_LATTITUDE*MAP_HEIGHT)/(distance_in_both_axis*FEET_PER_METER));
        
        double half_x = ( ( ((double)MAP_WIDTH/2)/PIXELS_PER_SECOND_X));
        X_OFFSET =  ( center.longitude - half_x);
        X_MAX =  ( center.longitude + half_x);
        
        double half_y = (( (double)(MAP_HEIGHT/2)/PIXELS_PER_SECOND_Y));
        Y_OFFSET = ( center.lattitude - half_y);
        Y_MAX = ( center.lattitude + half_y);

        System.out.println("PX: " +PIXELS_PER_SECOND_X+" PY: "+PIXELS_PER_SECOND_Y);
        
        System.out.println("X-offset: "+X_OFFSET+" "+X_MAX);
        System.out.println("Y-offset: "+Y_OFFSET+" "+Y_MAX);
        
        
    }
    
    public void create_point(double lat, double lon, String nm, String tp)
    {
        lattitude = lat; //in seconds
        longitude = lon;
        name = nm;
        type = tp;
        
       // double x_x = Math.abs( (lattitude - X_OFFSET) ) * PIXELS_PER_SECOND_X ;
        //double y_y = Math.abs( (longitude - Y_OFFSET) ) * PIXELS_PER_SECOND_Y ;
        
        double x_x = (longitude - X_OFFSET) * PIXELS_PER_SECOND_X ;
        double y_y = (Y_MAX - lattitude) * PIXELS_PER_SECOND_Y ;
 
        x = (int) x_x;
        y = (int) y_y;
        
    }
    
    
   

    
};