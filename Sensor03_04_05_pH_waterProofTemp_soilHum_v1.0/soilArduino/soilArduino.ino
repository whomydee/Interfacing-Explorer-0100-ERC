#include <SPI.h>
#include <Ethernet.h>
#include <EthernetUdp.h>


byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
IPAddress ip(192, 168, 1, 40);    //Arduino er IP address set

unsigned int localPort = 8888;

EthernetUDP Udp;

#define hum A0            //humidity Analog output to Arduino Analog Input 0
#define pHpin A1            //pH meter Analog output to Arduino Analog Input 1
#define Offset 0.32            //deviation compensate
#define LED 13
#define samplingInterval 20
#define printInterval 800
#define ArrayLenth  40    //times of collection
int pHArray[ArrayLenth];   //Store the average value of the sensor feedback
int pHArrayIndex=0;    

// buffers for receiving and sending data
char replyBuffer1[14];       // a string to send back
char replyBuffer2[14];
char replyBuffer3[14];
char replyBuffer4[14];

char mergeBuffer[40];
char packetBuffer[UDP_TX_PACKET_MAX_SIZE]; //buffer to hold incoming packet,

void setup()
{
  Ethernet.begin(mac, ip);
  Udp.begin(localPort);
  pinMode(7, OUTPUT); //for the pH meter VCC
  
  Serial.begin(9600);
  Serial.println("pH meter experiment!");    //Test the serial monitor
}

void loop()
{  
    digitalWrite(7, HIGH);//vc    
   int packetSize = Udp.parsePacket();
   Serial.println(packetSize);
   
   if(packetSize > 0)
   {
       Udp.read(packetBuffer, UDP_TX_PACKET_MAX_SIZE);
       
       if(packetBuffer[0]=='P')
       {
          int err;
          float volt, pH, humidity;
          
          /**********************************/
          /**********************************/
          
          static unsigned long samplingTime = millis();
          static unsigned long printTime = millis();
          static float pHValue,voltage;
          if(millis()-samplingTime > samplingInterval)
          {
              pHArray[pHArrayIndex++]=analogRead(pHpin);
              if(pHArrayIndex==ArrayLenth)pHArrayIndex=0;
              voltage = avergearray(pHArray, ArrayLenth)*5.0/1024;
              pHValue = 3.5*voltage+Offset;
              samplingTime=millis();
          }
          if(millis() - printTime > printInterval)   //Every 800 milliseconds, print a numerical, convert the state of the LED indicator
          {
          Serial.print("Voltage:");
                Serial.print(voltage,2);
                Serial.print("    pH value: ");
          Serial.println(pHValue,2);
                digitalWrite(LED,digitalRead(LED)^1);
                printTime=millis();
          }
          
          volt= voltage;
          pH= pHValue;
          
          humidity=analogRead(A0); //A0 er sathe humidity sensor connect korlam!!
          
  /******************************8/
  /********************************/
          
              Udp.beginPacket(Udp.remoteIP(), Udp.remotePort());
              
              char BufferHum[33];
              char BufferPH[33];
              
              dtostrf(humidity,8,2,BufferHum);
              dtostrf(pH,8,2,BufferPH);
                                      
              strcat(BufferHum, "-");
              strcat(BufferHum, BufferPH);
              strcat(BufferHum,"-    0.00");
              Serial.println(BufferHum);
              Udp.write(BufferHum);
              
              Udp.endPacket();
       }   
   }
}
  
double avergearray(int* arr, int number){
  int i;
  int max,min;
  double avg;
  long amount=0;
  if(number<=0){
    Serial.println("Error number for the array to avraging!/n");
    return 0;
  }
  if(number<5){   //less than 5, calculated directly statistics
    for(i=0;i<number;i++){
      amount+=arr[i];
    }
    avg = amount/number;
    return avg;
  }else{
    if(arr[0]<arr[1]){
      min = arr[0];max=arr[1];
    }
    else{
      min=arr[1];max=arr[0];
    }
    for(i=2;i<number;i++){
      if(arr[i]<min){
        amount+=min;        //arr<min
        min=arr[i];
      }else {
        if(arr[i]>max){
          amount+=max;    //arr>max
          max=arr[i];
        }else{
          amount+=arr[i]; //min<=arr<=max
        }
      }//if
    }//for
    avg = (double)amount/(number-2);
  }//if
  return avg;
}



