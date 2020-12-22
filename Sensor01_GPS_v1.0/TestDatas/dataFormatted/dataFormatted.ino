/*  PROJECT: MONGOL BAROTA.
*   Purpose: Code for Decoding GPS data.
*   Used module:
*   Micro Controller End: Arduino Mega 2560.
*   GPS end: UBlox Neo.
*   Coded By: Shad Showmmo, NAME-02, MIST.
*   Test Mode: Alpha Testing.
*/

#include <SoftwareSerial.h>




//SoftwareSerial (10, 11); //rx at 10, no tx for this case.

char dirLat[2];
char dirLon[2];


void dateTime(char *string){
  Serial.print("Time: ");
  
  for(int i=0; i<10; ++i){
    if(i<2){
      Serial.print(string[i]);
      if(i==1)
        Serial.print(":");
      }      
    else if(i>=2 && i<4){
      Serial.print(string[i]);
      if(i==3)
        Serial.print(":");
    }      
    else if(i>=4 && i<6){
      Serial.print(string[i]);
      if(i==5)
        Serial.print(", ");
    }
  }
}

/*this function will get value from dataParser and process and display the latitude and longitude value*/

void displayer(int a, char *string){
  float value;
  value = atof (string);
  /*Serial.println(value, 5);*/ //for debugging
  
  float subtract = value - int(value);
  int mainValue = int (value)/100;
  float remainder = int(value)%100;
  float totalValue = mainValue*1.00 + (remainder*1.00 + subtract)/60;
  //File dataFile = SD.open("datalog.txt", FILE_WRITE);

  if(a==1){
    Serial.print("Latitude: ");
    Serial.print(totalValue, 6);
    Serial.print(", ");
    Serial.print(dirLat);
    Serial.print(", ");
    }
   else{
    Serial.print("Longitude: ");
    Serial.print(totalValue, 6);
    Serial.print(", ");
    Serial.print(dirLon);
    Serial.print(".");
    Serial.println();
   }
   //dataFile.close();
 }



/*this function will pass latitude longitude datas to displayer for showing in the Serial Monitor*/

void dataParser(char *string) {
  int counter;
  char time[10];
  char lat[10];
  char lon[11];
  int i;

  //code for passing the value of time
  for(i=1; i<10; ++i){
    time[i-1] = string[i];
    counter = i+1;
  }
  time[counter] = NULL;
  dateTime(time);
  
  i=i+3;

  //code for passing the latitude value
  for(int j=0; j<10; ++i,++j){    //13-22
    lat[j] = string[i];
    counter = j+1;
  }
  lat[counter] = NULL;
  displayer(1, lat);
  /*Serial.write(latLong(lat));*/ //for debugging
  //For direction of latitude
  i = i+1;
  dirLat[0] = string[i];
  i+=2;
  
  //code for passing the longitude value
  for(int j=0; j<11; ++i,++j){
    lon[j] = string[i];
    counter = j+1;
  }
  lon[counter] = NULL;
  displayer(2, lon);

  //For direction of longitude
  i = i+1;
  dirLon[0] = string[i];

/*Commented codes below are only for Data Testing and Debugging
 * Serial.print(string);
 * Serial.print("\n");
 * Serial.print("---");
 * Serial.println();
 * Serial.write(lat);
 * Serial.print("---");
 * Serial.println();
 * Serial.print(lon);
 * Serial.print("---");
 * Serial.println();
 */
   
}



void setup() {
  //delay(60000);
  Serial.begin(9600); //baud rate for serial monitor is set at 9600 baud
  Serial2.begin(9600); //baud rate for gps is set at 9600 baud
  Serial.println("Setup Initialized !!");
}

int count=0; // count variable for checking if "RMC" is recieved
int dataCount=1; // dataCount variable for storing the GNRMC values in "data" string
char data[60];

void loop() {
  //delay(30000);
  if(Serial2.available()) {
    
  char c = Serial2.read();
  
    if(count == 3){      
    data[dataCount - 1] = c;
    dataCount++;
    
    if(dataCount == 60){
      data[dataCount] = NULL;
      //Serial.println(data);
      dataParser(data);
      dataCount = 1;
      count=0;
    }  
  }
  
  else if(c == 'R' )
   count++;
  else if(c == 'M' && count == 1)
    count++;
  else if(c == 'C' && count == 2)
    count++;
   
  }
  
}

