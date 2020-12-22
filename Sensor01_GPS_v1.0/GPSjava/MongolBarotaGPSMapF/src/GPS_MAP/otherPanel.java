/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GPS_MAP;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.RoundingMode;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import java.text.SimpleDateFormat; //new added
import java.util.Calendar; //new added
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.DefaultCaret;


    
/**
 *
 * @author flora
 */
public class otherPanel extends JPanel {
    
    double lattitudeInSeconds;
    double longitudeInSeconds;
    
    static JTextField lattitudeDegree = new JTextField("23");     
    static JTextField lattitudeMinute = new JTextField("50"); 
    static JTextField lattitudeSecond = new JTextField("15");     
    static JTextField longitudeDegree = new JTextField("90");     
    static JTextField longitudeMinute = new JTextField("21");     
    static JTextField longitudeSecond = new JTextField("33");     

    static JLabel longitudeDegreeLabel = new JLabel("Longitude Degree:");
    static JLabel longitudeMinuteLabel = new JLabel("Longitude Minute:");
    static JLabel longitudeSecondLabel = new JLabel("Longitude Second:");
    static JLabel lattitudeDegreeLabel = new JLabel("Lattitude Degree:");
    static JLabel lattitudeMinuteLabel = new JLabel("Lattitude Minute:");
    static JLabel lattitudeSecondLabel = new JLabel("Lattitude Second:");
    static JLabel typeLabel = new JLabel("Point Type:");
    static JLabel removePointLabel = new JLabel("Remove Point:");
    
    static JButton AddPoint = new JButton("ADD POINT");
    static JTextField Name = new JTextField("");
    static JLabel NameLabel = new JLabel("Point Name:");
    
    static JLabel headingAddPoint = new JLabel("Add New Point");
    static JLabel headingCalculateDistance = new JLabel("Calculate Distance");
    static JLabel distanceFrom = new JLabel("From: ");
    static JLabel distanceTo = new JLabel("To: ");
    
    static JLabel status = new JLabel("");
    
    String[] pointTypes = { "Pickup Point", "Delivery Point", "Other" };
    String[] combo_list = new String[200];
    
    DefaultComboBoxModel model;
    

    JComboBox type = new JComboBox(pointTypes);
    
    JComboBox removePoint = new JComboBox();
    JComboBox pointFrom = new JComboBox();
    JComboBox pointTo = new JComboBox();
    
    static JButton removePointButton = new JButton("REMOVE");
    
    public String distancePointFrom = "Rover";
    public String distancePointTo = "Control Station";
    
    static JButton distanceGoButton = new JButton("GO");
    static JLabel showDistanceLabel = new JLabel("Distance: ");
    static JLabel distanceUnit = new JLabel("Meter");

    public JTextField showDistance = new JTextField("");
    
    static JTextArea log = new JTextArea();
    static JLabel logLabel = new JLabel("Current Rover Position");
    static JScrollPane scrollPane;// = new JScrollPane(log); //new added
    
    int getLongitudeMinute,getLongitudeDegree,getLatitudeMinute,getLatitudeDegree;
    double getLatitudeSecond, getLongitudeSecond;
    
    String currentLatitude, currentLongitude;
    String  currentLog; //new added
    
    public JButton zoomIn = new JButton("Zoom In");
    public JButton zoomOut = new JButton("Zoom Out");
    
    public ImageIcon red_light;
    public ImageIcon green_light;
    
    //Calendar time = Calendar.getInstance();
    
    public int pickup_point_no = 0;
    public int delivery_point_no = 0;
    public int other_point_no = 0;
    public int zoom = 0;
    
    public JLabel amountOfZoom = new JLabel();


    public otherPanel() {
        
        
        ImageIcon red_light =new ImageIcon(this.getClass().getResource("red.png"));
        ImageIcon green_light =new ImageIcon(this.getClass().getResource("green.png"));
        
        setLayout(null);
        setPreferredSize(new Dimension(350,700));
        setBackground(Color.LIGHT_GRAY);
        
        Font F2 = new Font("Serif", Font.PLAIN, 16);
        Font F = new Font("Serif", Font.PLAIN, 18);
        Font headingFont = new Font("Serif", Font.BOLD, 22);
        Font zoomFont = new Font("Serif", Font.BOLD, 20);
        
        
        headingAddPoint.setBounds(100, -20, 500, 100);
        headingAddPoint.setFont(headingFont);
                
        lattitudeDegreeLabel.setBounds(35 , 50 , 150, 25);
        lattitudeDegreeLabel.setFont(F);
        lattitudeDegree.setBounds(180 , 50 , 130, 20);
        
        lattitudeMinuteLabel.setBounds(35 , 80 , 150, 25);
        lattitudeMinuteLabel.setFont(F);
        lattitudeMinute.setBounds(180 , 80 , 130, 20);
        
        lattitudeSecondLabel.setBounds(35 , 110 , 150, 25);
        lattitudeSecondLabel.setFont(F);
        lattitudeSecond.setBounds(180 , 110 , 130, 20);
        
        longitudeDegreeLabel.setBounds(35 , 140 , 150, 25);
        longitudeDegreeLabel.setFont(F);
        longitudeDegree.setBounds(180 , 140 , 130, 20);
        
        longitudeMinuteLabel.setBounds(35 , 170 , 150, 25);
        longitudeMinuteLabel.setFont(F);
        longitudeMinute.setBounds(180 , 170 , 130, 20);
        
        longitudeSecondLabel.setBounds(35 , 200 , 150, 25);
        longitudeSecondLabel.setFont(F);
        longitudeSecond.setBounds(180 , 200 , 130, 20);
        
        NameLabel.setBounds(35 , 230 , 150, 25);
        NameLabel.setFont(F);
        Name.setBounds(180, 230, 130, 20);
        
        typeLabel.setBounds(35,260,150,25);
        typeLabel.setFont(F);
        type.setBounds(180, 260, 130, 25);
        
        AddPoint.setBounds(120, 290, 100, 35);
        
        removePointLabel.setBounds(5, 340, 150, 25);
        removePointLabel.setFont(headingFont);
        removePoint.setBounds(150, 340, 100, 25);
        
        status.setBounds(10, 0, 50, 50);
        //status.setIcon(green_light);
        
        
        
        removePointButton.setBounds(255, 335, 90, 35);
        
        headingCalculateDistance.setBounds(100, 375, 200, 40);
        headingCalculateDistance.setFont(headingFont);
        
        distanceFrom.setBounds(5, 415, 150, 25);
        distanceFrom.setFont(F);
        
        pointFrom.setBounds(50, 415, 110, 25);
        
        distanceTo.setBounds(170, 415, 90, 25);
        distanceTo.setFont(F);
        
        pointTo.setBounds(200, 415, 90, 25);
        distanceGoButton.setBounds(295, 410, 60, 30);
        
        showDistanceLabel.setBounds(30, 460, 100, 25);
        showDistanceLabel.setFont(F);
        showDistance.setBounds(100, 455, 150, 40);
        showDistance.setFont(new Font("Serif", Font.PLAIN, 16));
        showDistance.setEditable(false);
        
        distanceUnit.setBounds(260,460,100,25);
        distanceUnit.setFont(F);
        
        //log.setBounds(20,550,380,50);
        //log.setFont(F);
        //log.setEditable(false);
     
        log.setFont(F2);
        log.setEditable(false);
        //log.setLineWrap(true);
        //log.setAutoscrolls(true);
        DefaultCaret caret = (DefaultCaret)log.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        scrollPane = new JScrollPane(log); //new added
        scrollPane.setViewportView(log);
        add(scrollPane, BorderLayout.CENTER);

        //log.setPreferredSize(new Dimension(380,50));
        scrollPane.setPreferredSize(new Dimension(380,48));
        scrollPane.setBounds(15,545,320,90);
        //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        //scrollPane.setViewportView(log); //new added
        
        logLabel.setBounds(80,510,400, 40);
        logLabel.setFont(headingFont);
        
        zoomIn.setBounds(20, 650, 150, 40);
        zoomIn.setFont(headingFont);
        zoomOut.setBounds(195, 650, 150, 40);
        zoomOut.setFont(headingFont);
        
        amountOfZoom.setFont(zoomFont);
        amountOfZoom.setBounds(255,-10,200,50);
        amountOfZoom.setForeground(Color.red);
        amountOfZoom.setText("Zoom: "+Integer.toString(zoom)+"x");
        

        setDistanceComponents();
        
        
        if(GPS_MAP.M.no_of_points <= 0)
        {
            removePoint.setEnabled(false);
            removePointLabel.setEnabled(false);
            removePointButton.setEnabled(false);
        }
        else
        {
            combo_list = GPS_MAP.M.combobox_names.toArray(new String[GPS_MAP.M.combobox_names.size()]);
            model = new DefaultComboBoxModel( combo_list );
            removePoint.setModel(model);
          
        }
        
        if(type.getSelectedItem().equals("Pickup Point"))
        {
            Name.setText("Pickup_Point_"+Integer.toString(pickup_point_no+1));
        }
        else if(type.getSelectedItem().equals("Delivery Point"))
        {
            Name.setText("Delivery_Point_"+Integer.toString(delivery_point_no+1));
            
        }
        else if(type.getSelectedItem().equals("Other"))
        {
            
            Name.setText("Other_Point_"+Integer.toString(other_point_no+1));
        }
        
        type.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) 
                {
                    if(type.getSelectedItem().equals("Pickup Point"))
                    {
                        Name.setText("Pickup_Point_"+Integer.toString(pickup_point_no+1));
                    }
                    else if(type.getSelectedItem().equals("Delivery Point"))
                    {
                        Name.setText("Delivery_Point_"+Integer.toString(delivery_point_no+1));

                    }
                    else if(type.getSelectedItem().equals("Other"))
                    {

                        Name.setText("Other_Point_"+Integer.toString(other_point_no+1));
                    }
                }
            });
        
        addingComponentToOtherPanel();
        
        AddPoint.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    AddPointFromUser(evt);
                }
            });
        
        distanceGoButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    
                    distancePointFrom = (String) pointFrom.getSelectedItem();
                    distancePointTo = (String) pointTo.getSelectedItem();
                    setDistanceComponents();
                    
                }
            });
        
        
        removePointButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) 
                {
                    String nameToRemove = (String) removePoint.getSelectedItem();
                    
                    for(int i=0; i< GPS_MAP.M.no_of_points; i++)
                    {
                        System.out.println(nameToRemove);
                        if(nameToRemove.equals(GPS_MAP.M.map_points[i].name))
                        {
                            GPS_MAP.M.map_points[i].valid=false;  
                            GPS_MAP.M.combobox_names.remove(GPS_MAP.M.map_points[i].name);
                        }
                    }
                    
                    combo_list = new String[200];
                    combo_list = GPS_MAP.M.combobox_names.toArray(new String[GPS_MAP.M.combobox_names.size()]);
                    model = new DefaultComboBoxModel( combo_list );
                    removePoint.setModel(model);
                    
                    
                    if(combo_list.length <= 0)
                    {
                        removePoint.setEnabled(false);
                        removePointLabel.setEnabled(false);
                        removePointButton.setEnabled(false);
                    }
                    else
                    {
                        removePoint.setEnabled(true);
                        removePointLabel.setEnabled(true);
                        removePointButton.setEnabled(true);   
                    }
                    
                    if(nameToRemove.equals(distancePointFrom))
                    {
                        distancePointFrom = "Rover";
                    }
                    if(nameToRemove.equals(distancePointTo))
                    {
                        distancePointTo = "Control Station";
                    }
                    setDistanceComponents();
                    
                }
            });
        zoomIn.addActionListener(new java.awt.event.ActionListener() 
        {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) 
                {
                    
                    if(Point.distance_in_both_axis <= 200)
                    {
                        JOptionPane.showMessageDialog(null, "Zoom in is not possible.");
                    }
                    else
                    {
                        zoom++;
                        amountOfZoom.setText("Zoom: "+Integer.toString(zoom)+"x");
                        //System.out.println("Amount of zoom: "+zoom);
                        GPS_MAP.M.zoom(-200);
                    }
                }
            });
        zoomOut.addActionListener(new java.awt.event.ActionListener() 
        {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) 
                {
                    if(Point.distance_in_both_axis >= 5000)
                    {
                        JOptionPane.showMessageDialog(null, "Zoom out is not possible.");
                    }
                    else
                    {
                        zoom--;
                        amountOfZoom.setText("Zoom: "+Integer.toString(zoom)+"x");
                        System.out.println("Amount of zoom: "+zoom);
                        GPS_MAP.M.zoom(+200);
                    }
                }
            });
        
    }
    private void AddPointFromUser(java.awt.event.ActionEvent evt)
    {
        System.out.println("Add button");
        
        String nm = Name.getText();
        
        if(nm.equals("") == true)
        {
            JOptionPane.showMessageDialog(null, "Please enter a name for the point.");
        }
        else
        {
            boolean namePreviouslyUsed = false;

            for(int i=0;i< GPS_MAP.M.no_of_points ; i++)
            {
                if(nm.equals(GPS_MAP.M.map_points[i].name)&&GPS_MAP.M.map_points[i].valid == true)
                {
                    namePreviouslyUsed = true;
                    break;
                }
            }


            if(namePreviouslyUsed == true)
            {
                JOptionPane.showMessageDialog(null, "Point name is already used. Please use another name.");
            }
            else
            {
                double lat_degree = 0; 
                double lat_minute = 0;
                double lat_second = 0;
                double lon_degree = 0; 
                double lon_minute = 0; 
                double lon_second = 0;

                String lattitudeDeg = lattitudeDegree.getText();
                String lattitudeMin = lattitudeMinute.getText();
                String lattitudeSec = lattitudeSecond.getText();

                String longitudeDeg = longitudeDegree.getText();
                String longitudeMin = longitudeMinute.getText();
                String longitudeSec = longitudeSecond.getText();


                int type_index = type.getSelectedIndex();
                boolean exceptionHappened = false;
                
                try
                {
                    lat_degree = Double.parseDouble(lattitudeDeg);
                    lat_minute = Double.parseDouble(lattitudeMin);
                    lat_second = Double.parseDouble(lattitudeSec);

                    lon_degree = Double.parseDouble(longitudeDeg);
                    lon_minute = Double.parseDouble(longitudeMin);
                    lon_second = Double.parseDouble(longitudeSec);
                   
                }
                catch(NullPointerException e)
                {
                    exceptionHappened = true;
                    JOptionPane.showMessageDialog(null, "Please fill up every field correctly.", "Invalid Input.", JOptionPane.WARNING_MESSAGE);

                }
                catch(NumberFormatException e)
                {
                    exceptionHappened = true;
                    JOptionPane.showMessageDialog(null, "Please fill up every field correctly.", "Invalid Input.", JOptionPane.WARNING_MESSAGE);
                }
                
                
                if(exceptionHappened == false)
                {
                    if(lat_degree <0 || lat_degree >= 360 || lat_minute < 0 || lat_minute >= 60 || lat_second < 0 || lat_second >= 60
                       || lon_degree < 0 || lon_degree >= 360 || lon_minute < 0 || lon_minute >= 60 || lon_second < 0 || lon_second >= 60)
                    {
                        JOptionPane.showMessageDialog(null, "Invalid GPS data.", "Invalid Input.", JOptionPane.WARNING_MESSAGE);

                    }
                    else
                    {
                        lattitudeInSeconds = (lat_degree*3600) + (lat_minute*60) + lat_second;
                        longitudeInSeconds = (lon_degree*3600) + (lon_minute*60) + lon_second;

                        GPS_MAP.M.add_new_map_point(lattitudeInSeconds, longitudeInSeconds, nm, pointTypes[type_index] );


                        String[] combo_list = GPS_MAP.M.combobox_names.toArray(new String[GPS_MAP.M.combobox_names.size()]);

                        model = new DefaultComboBoxModel( combo_list );
                        removePoint.setModel(model);

                        if(combo_list.length <= 0)
                        {
                            removePoint.setEnabled(false);
                            removePointLabel.setEnabled(false);
                            removePointButton.setEnabled(false);
                        }
                        else
                        {
                            removePoint.setEnabled(true);
                            removePointLabel.setEnabled(true);
                            removePointButton.setEnabled(true);   
                        }
                        if(type.getSelectedItem().equals("Pickup Point"))
                        {
                            Name.setText("Pickup_Point_"+Integer.toString(pickup_point_no+1));
                        }
                        else if(type.getSelectedItem().equals("Delivery Point"))
                        {
                            Name.setText("Delivery_Point_"+Integer.toString(delivery_point_no+1));

                        }
                        else if(type.getSelectedItem().equals("Other"))
                        {

                            Name.setText("Other_Point_"+Integer.toString(other_point_no+1));
                        }
                    }
                }
                
            }
        }
        
        setDistanceComponents();
    }
       
    
    public void setDistanceComponents()
    {
        GPS_MAP.M.combobox_names.add("Rover");
        GPS_MAP.M.combobox_names.add("Control Station");
        combo_list = new String[200];
        combo_list = GPS_MAP.M.combobox_names.toArray(new String[GPS_MAP.M.combobox_names.size()]);
        model = new DefaultComboBoxModel( combo_list );
        
        pointFrom.setModel(model);
        pointFrom.setSelectedItem(distancePointFrom);
        
        model = new DefaultComboBoxModel( combo_list );
        pointTo.setModel(model);
        pointTo.setSelectedItem(distancePointTo);
        GPS_MAP.M.combobox_names.remove("Rover");
        GPS_MAP.M.combobox_names.remove("Control Station");
    }
    
    
   /* public void updatetDistanceStrings()
    {
        distancePointFrom = (String) pointFrom.getSelectedItem();
        distancePointTo = (String) pointTo.getSelectedItem();
        setDistanceComponents();
    }*/
    public String getDistanceFrom()
    {
        return distancePointFrom;
    }
    
    public String getDistanceTo()
    {
        return distancePointTo;
    }

    public void setStatus()
    {
        //System.out.println("in set status");
        if(GPS_MAP.status == false)
        {

            //status.setBounds(10, 0, 50, 50);
            ImageIcon red_light =new ImageIcon(this.getClass().getResource("red_light_1.png"));
            System.out.println("status: "+GPS_MAP.status);
            status.setIcon(red_light);
            //status.setText("Red");
            status.setVisible(true);
            add(status);
        }
        else if(GPS_MAP.status == true)
        {
            //status.setBounds(10, 0, 50, 50);
            ImageIcon green_light =new ImageIcon(this.getClass().getResource("green_light_1.png"));
            System.out.println("status: "+GPS_MAP.status);
            status.setIcon(green_light);
            //status.setText("Green");
            status.setVisible(true);
            add(status);
        }
        
    }
    
    
    
    
    private void addingComponentToOtherPanel(){
        
        add(headingAddPoint);
        add(longitudeDegreeLabel);        
        add(longitudeDegree); 
        add(longitudeMinuteLabel);        
        add(longitudeMinute);        
        add(longitudeSecondLabel);        
        add(longitudeSecond);        
        add(lattitudeDegreeLabel);        
        add(lattitudeDegree);                
        add(lattitudeMinuteLabel);        
        add(lattitudeMinute);                
        add(lattitudeSecondLabel);
        add(lattitudeSecond);
        add(AddPoint);
        add(NameLabel);
        add(Name);
        add(status);
        add(typeLabel);
        add(type);
        add(removePoint);
        add(removePointLabel);
        add(removePointButton);
        add(headingCalculateDistance);
        add(distanceFrom);
        add(distanceTo);
        add(pointFrom);
        add(pointTo);
        add(distanceGoButton);
        add(showDistanceLabel);
        add(showDistance);
        add(distanceUnit);
        //add(log);
        add(scrollPane); //new added
        add(logLabel);
        add(zoomIn);
        add(zoomOut);
        add(amountOfZoom);
    }
    
    public void UpdateLog()
    {
        //if(GPS_MAP.status == true)
        {
        
            DecimalFormat df = new DecimalFormat("##.#####");
            df.setRoundingMode(RoundingMode.DOWN);
            System.out.println(df.format(getLatitudeSecond));

            getLatitudeDegree=(int) GPS_MAP.M.UDP.lat_d;
            getLatitudeMinute=(int) GPS_MAP.M.UDP.lat_m;
            getLatitudeSecond= GPS_MAP.M.UDP.lat_s;
            String LatSecond = df.format(getLatitudeSecond);
            getLongitudeDegree=(int) GPS_MAP.M.UDP.longi_d;
            getLongitudeMinute=(int) GPS_MAP.M.UDP.longi_m;
            getLongitudeSecond=GPS_MAP.M.UDP.longi_s;
            String LonSecond = df.format(getLongitudeSecond);

            //System.out.println(getLatitudeDegree + " deg "+ lat_m +" min " + lat_s +" sec ");

            currentLatitude=Integer.toString(getLatitudeDegree)+"\u00B0 "+Integer.toString(getLatitudeMinute)+"' "+LatSecond+"\"";
            currentLongitude=Integer.toString(getLongitudeDegree)+"\u00B0 "+Integer.toString(getLongitudeMinute)+"' "+LonSecond+"\"";    

            currentLog=("Current Lattitude:  "+currentLatitude+"\n"+"Current Longitude: "+currentLongitude+"\n"); //log.SetText er bodole edited
            Calendar time = Calendar.getInstance();
            time.getTime(); //new added
            SimpleDateFormat hourMinuteSecond = new SimpleDateFormat("HH:mm:ss");
            log.append( "\n\nTime: "+hourMinuteSecond.format(time.getTime()) );
            log.append(" hrs\n");
            log.append(currentLog); //new added
        }
        
    }

    
    
}
