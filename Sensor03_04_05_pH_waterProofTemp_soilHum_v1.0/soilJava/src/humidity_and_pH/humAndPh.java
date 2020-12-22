
package humidity_and_pH;

/**
 *
 * @author Samanta
 */

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import static java.awt.PageAttributes.MediaType.C;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DialShape;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.chart.plot.PiePlotState;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.Range;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.text.TextUtilities;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;



public class humAndPh extends ApplicationFrame implements ActionListener{

    private static DefaultValueDataset dataset1;
    private static DefaultValueDataset dataset2;
    private Timer timer = new Timer(250, (ActionListener) this);/** Timer to refresh graph after every 1/4th of a second */
    public static String clientIP = "192.168.1.40";
    public static int clientPort = 8888;
    public static DataOutputStream outToclient;
    public static BufferedReader inFromServer;
    public static double pH = 0.0;
    public static double humidity = 0.0;
    public static boolean connection = false;
    public static String given_ip;
    public static InetAddress  address = null;
    public static DatagramSocket datagramSocket = null;
    public static JTextArea log;
    public static JScrollPane text_scroll;
    

    public humAndPh(String title) throws IOException {
        
        
        super(title);
        setBackground(Color.yellow);
        
        final JPanel content = new JPanel(new BorderLayout());              //Created JPanel to show graph on screen
        
        dataset1 = new DefaultValueDataset(0);
        JFreeChart chart = createChart(dataset1);
        
        dataset2 = new DefaultValueDataset(0);
        JFreeChart chart1 = createChart1(dataset2);
        
        
        ChartPanel chartpanel = new ChartPanel(chart);
        chartpanel.setMouseWheelEnabled(true);
        chartpanel.setPreferredSize(new Dimension(500, 200));
        chartpanel.setBackground(Color.red);
    
        ChartPanel chartpanel1 = new ChartPanel(chart1);
        chartpanel1.setMouseWheelEnabled(true);
        chartpanel1.setPreferredSize(new Dimension(500, 300));
    
        
         // Contents for IP input and Connect Button
        
        final JPanel input_ip = new JPanel();
        
        final JLabel enter_ip = new JLabel("IP Address: ");
        input_ip.add(enter_ip, BorderLayout.EAST);
        enter_ip.setBackground(Color.yellow);
         
        final JTextField ip = new JTextField("192.168.1.40");
        ip.setPreferredSize(new Dimension(100, 30));
        input_ip.add(ip, BorderLayout.CENTER);
        
        final JButton connect = new JButton("  Connect  ");
        input_ip.add(connect, BorderLayout.WEST);
        connect.setBackground(Color.green);
        input_ip.setBackground(new Color(217, 217, 240));
        
    //Contents for Showing Log
        
        final JPanel show_log = new JPanel();
       
     
        log = new JTextArea();
        log.setEditable(false);
        log.setAutoscrolls(true);
        
        final JScrollPane scroller = new JScrollPane(log);
        scroller.setPreferredSize(new Dimension(400, 80));
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        input_ip.add(scroller, BorderLayout.AFTER_LINE_ENDS);
        
        
     //Logo Show
        
        final JPanel show_logo = new JPanel();
        
        BufferedImage image = ImageIO.read( getClass().getResourceAsStream("logo.png"));
        JLabel back_label = new JLabel(new ImageIcon(image));
        show_logo.add(back_label, BorderLayout.CENTER);        
        

        content.setBackground(Color.ORANGE);
        content.add(show_logo, BorderLayout.NORTH);
        content.add(chartpanel, BorderLayout.WEST);
        content.add(chartpanel1, BorderLayout.EAST);
        content.add(input_ip, BorderLayout.SOUTH);
        
        
        setContentPane(content);
         
        
        connect.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
               
                if(connection == false){
                    
                    connection = true;
                    connect.setText("Disconnect");
                    connect.setBackground(new Color(252, 131, 114));
                    ip.setEditable(false); 
                    
                    
                    try {
     
                        given_ip = ip.getText();
                         
                        byte [] buffer = "P".getBytes();
                        byte [] IP = given_ip.getBytes();
    
                        InetAddress address = InetAddress.getByName(given_ip);
                        
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, clientPort);
                        datagramSocket = new DatagramSocket();
                        
                        datagramSocket.send(packet);
                        datagramSocket.setSoTimeout(3000);
                        
                        
                        log.setText(log.getText().toString() + "Connection Has Been Established with IP: " + given_ip + "\n");
                        
                        
                        } catch (IOException ex) {
                            
                            log.setText(log.getText().toString() + "Connection Could Not Be Established with IP: " + given_ip + "\n");
                            
                            Logger.getLogger(humAndPh.class.getName()).log(Level.SEVERE, null, ex);
                     }
                    
                     timer.start();
                                     
                 
                }
                else{
                    
                    connection = false;
                    connect.setText("  Connect  ");
                    connect.setBackground(Color.LIGHT_GRAY);
                    ip.setEditable(true);
                    
                    pH = 0.0;
                    humidity = 0.0;
                    update();
                    
                    timer.stop();
                    
                    if(datagramSocket != null) datagramSocket.close();
                    
                    log.setText(log.getText().toString() + "Connection Has Been Broken\n");

                }
            }
        });
    }

  private static JFreeChart createChart(ValueDataset valuedataset) {
        
    MeterPlot meterplot = new MeterPlot(valuedataset);
    
  //set minimum and maximum value
    
    double value_next;
    if(pH == 0.0) value_next = pH;
    else value_next = pH + 1;
    
    meterplot.setRange(new Range(0.0D, 14D));

    meterplot.addInterval(new MeterInterval("Current Humidity", new Range(0.0D, pH), Color.blue, new BasicStroke(2.0F), new Color(255, 0, 0, 128)));

    meterplot.addInterval(new MeterInterval("High Humidity", new Range(value_next, 7.0D), Color.blue, new BasicStroke(2.0F), new Color(255, 255, 0, 64)));

    //meterplot.addInterval(new MeterInterval("High 2 Humidity", new Range(100, 150D), Color.blue, new BasicStroke(2.0F), new Color(255, 0, 0, 128)));


    meterplot.setNeedlePaint(Color.darkGray);
    meterplot.setDialBackgroundPaint(Color.cyan);
    meterplot.setDialOutlinePaint(Color.BLACK);
    
    meterplot.setDialShape(DialShape.CHORD);
    
    meterplot.setMeterAngle(180);
    
    meterplot.setTickLabelsVisible(true);
    meterplot.setTickLabelFont(new Font("Arial", 1, 20));
    meterplot.setTickLabelPaint(Color.black);
    meterplot.setTickSize(10D);
    
 
    meterplot.setTickPaint(Color.gray);
    meterplot.setValuePaint(Color.black);
    
    meterplot.setValueFont(new Font("Arial", 1, 18));
    meterplot.setUnits("");
   
    
    JFreeChart jfreechart = new JFreeChart("pH Value Reading", JFreeChart.DEFAULT_TITLE_FONT, meterplot, true);
    
    return jfreechart;
    
}

  
  private static JFreeChart createChart1(ValueDataset valuedataset) {
        
    MeterPlot meterplot = new MeterPlot(valuedataset);
    
  //set minimum and maximum value
    
    double value_nexth;
    if(humidity == 0.0) value_nexth = humidity;
    else value_nexth = humidity + 1;
    
    meterplot.setRange(new Range(0.0D, 1030D));

    meterplot.addInterval(new MeterInterval("Current Humidity", new Range(0.0D, humidity), Color.blue, new BasicStroke(2.0F), new Color(255, 0, 0, 128)));

    meterplot.addInterval(new MeterInterval("High Humidity", new Range(value_nexth, 100D), Color.blue, new BasicStroke(2.0F), new Color(255, 255, 0, 64)));


    meterplot.setNeedlePaint(Color.darkGray);
    meterplot.setDialBackgroundPaint(Color.cyan);
    meterplot.setDialOutlinePaint(Color.BLACK);
    
    meterplot.setDialShape(DialShape.CHORD);
    
    meterplot.setMeterAngle(180);
    
    meterplot.setTickLabelsVisible(true);
    meterplot.setTickLabelFont(new Font("Arial", 1, 12));
    meterplot.setTickLabelPaint(Color.black);
    meterplot.setTickSize(5D);
 
    meterplot.setTickPaint(Color.gray);
    meterplot.setValuePaint(Color.black);
    
    meterplot.setValueFont(new Font("Arial", 1, 18));
    meterplot.setUnits("");
   
    
    JFreeChart jfreechart = new JFreeChart("Soil Humidity Reading", JFreeChart.DEFAULT_TITLE_FONT, meterplot, true);
    
    return jfreechart;
    
}


public void update(){
    
    dataset1.setValue(pH);
    dataset2.setValue(humidity);
    
}


    @Override
    public void actionPerformed(ActionEvent e) {
        
         //Data Fetch from Arduino & DHT11
        
          byte [] buffer = "P".getBytes();

            
          try {
              
                 address = InetAddress.getByName(given_ip);
                    
            } catch (UnknownHostException ex) {
                
                log.setText(log.getText().toString() + "IP not Found! \n");
                
                Logger.getLogger(humAndPh.class.getName()).log(Level.SEVERE, null, ex);
           }
                        
             DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, clientPort);
          
          
          try {
                
              datagramSocket.send(packet);
                
            } catch (IOException ex) {
                
                log.setText(log.getText().toString() + "Data Packet Could Not Be Sent\n");
                
                Logger.getLogger(humAndPh.class.getName()).log(Level.SEVERE, null, ex);
            }
          

          
        
       //Receive Data Packet
          
       
          
          try{
             
                 byte[] receiveData = new byte[1024]; 
                 String[] data = new String[3];
                 
                 DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                 datagramSocket.receive(receivePacket);
                         
                 String sentence = new String(receivePacket.getData());
                
                 data = sentence.split("-");
                 
                 humidity = Float.valueOf(data[0]);
                 pH = Float.valueOf(data[1]);
                       
                 update();
                 
                 log.setText(log.getText().toString() + "pH: " + pH + "      Humidity: " + humidity + "\n");
                 
                    
                            
            }catch (IOException ex){
                
                log.setText(log.getText().toString() + "Data Packet Could Not Be Received\n");
      
        }
         
    }
    
 
    
    
    public static void main(String[] args) throws IOException {
        
        humAndPh demo = new humAndPh("Team Mongol Barota 2015");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}
