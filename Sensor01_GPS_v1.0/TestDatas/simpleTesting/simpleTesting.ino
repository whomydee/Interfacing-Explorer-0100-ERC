#include <SPI.h>       
#include <Ethernet.h>
#include <EthernetUdp.h> 
#include<String.h>

byte mac[] = {0xED,0x42,0x4A,0x4D,0x8B,0xBF};
IPAddress ip(192, 168, 1, 7);
unsigned int localPort = 8888;  

int count = 0;
int dataCount = 1;
char data[60];


char packetBuffer[UDP_TX_PACKET_MAX_SIZE];

EthernetUDP Udp;

void setup() 
{
  Serial.begin(9600);
  Serial2.begin(9600);
  Ethernet.begin(mac,ip);
  Udp.begin(localPort);
 }


boolean sendData = true;


void loop()
{   
  int packetSize =  Udp.parsePacket();
  Serial.println("Shad Showmmo Initializing");
  if(packetSize>0)
  {
    Serial.println("TRUE-----TRUE---TRUE----TRUE");
    Serial.println("recieved");
    Udp.read(packetBuffer,UDP_TX_PACKET_MAX_SIZE);
    
    if(packetBuffer[0]=='g')
      sendData=true;
    if(packetBuffer[0]=='k')
       sendData=false;
  }
  
  if(sendData==true)
  {
    if (Serial2.available())
    {
      char c = Serial2.read();
      if(count == 3){      
        data[dataCount - 1] = c;
        dataCount++;
    
        if(dataCount == 60){
          data[dataCount] = NULL;
          Serial.println(data);
          //Udp.beginPacket(Udp.remoteIP(), Udp.remotePort());
          //Udp.write(data);
          //Udp.endPacket();
          dataCount = 1;
          count = 0;
        }  
      }

     
  
    else if(c == 'R')
      count++;
    else if(c == 'M' && count == 1)
      count++;
    else if(c == 'C' && count == 2)
      count++;

    
    }
  }
}
