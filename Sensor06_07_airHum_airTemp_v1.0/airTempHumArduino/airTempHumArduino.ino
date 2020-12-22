#include <dht.h>
#include <Ethernet.h>
#include<EthernetUdp.h>

byte mac[] = {0x12,0x1A,0x4A,0x4D,0x8B,0xBF};
IPAddress ip(192, 168, 1, 11);
unsigned int localPort = 8888;

char packetBuffer[UDP_TX_PACKET_MAX_SIZE];

EthernetUDP Udp;
dht DHT;
char bufferTemp[20], bufferHum[10];

#define DHT_PIN A2

void setup(){
  Serial.begin(9600);
  Ethernet.begin(mac, ip);
  Udp.begin(localPort);
}

boolean sendData = false;

void loop()
{
  int packetSize = Udp.parsePacket();
  if(packetSize>0){
    Udp.read(packetBuffer, UDP_TX_PACKET_MAX_SIZE);
    

    if(packetBuffer[0]=='P')
      sendData = true;
    }

   if(sendData){
  
      DHT.read11(DHT_PIN);
  
      dtostrf(DHT.temperature, 8 , 2, bufferTemp);
      dtostrf(DHT.humidity, 8 , 2, bufferHum);

      strcat(bufferTemp, "-");
      strcat(bufferTemp, bufferHum);

      Udp.beginPacket(Udp.remoteIP(), Udp.remotePort());
      Udp.write(bufferTemp);
      Udp.endPacket();
  
      //Serial.print("Temperature = ");
      //Serial.println(DHT.temperature);
      //Serial.print("Humidity = ");
      //Serial.println(DHT.humidity);
  
      delay(1000);
   }
}

