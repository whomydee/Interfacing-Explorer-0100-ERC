#include <Ethernet.h>
#include<EthernetUdp.h>


byte mac[] = {0x13,0x1B,0x4A,0x4D,0x8B,0xBF};
IPAddress ip(192, 168, 1, 12);
unsigned int localPort = 8888;

char packetBuffer[UDP_TX_PACKET_MAX_SIZE];

EthernetUDP Udp;

char bufferVolt[10];
boolean sendData = false;

void setup() {
  Serial.begin(9600);
  Ethernet.begin(mac, ip);
  Udp.begin(localPort);
}

void loop() {

   int packetSize = Udp.parsePacket();
    if(packetSize>0){
      Udp.read(packetBuffer, UDP_TX_PACKET_MAX_SIZE);
      if(packetBuffer[0]=='P')
        sendData = true;
    }

  if(sendData){
    int sensorValue = analogRead(A0);
    float voltage = sensorValue * (5.0 / 1023.0);
    //Serial.println(voltage);
    dtostrf(voltage, 8, 2, bufferVolt);

    strcat(bufferVolt, "-");
    //strcat(bufferVolt, "220.0");

    Udp.beginPacket(Udp.remoteIP(), Udp.remotePort());
    Udp.write(bufferVolt);
    Udp.endPacket();

    delay(1000);
  }
}
