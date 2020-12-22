#include <SoftwareSerial.h>

void setup() {
  Serial.begin(9600);
  Serial2.begin(9600);
}

void loop() {
  if(Serial2.available()){
    char c = Serial2.read();
    Serial.print(c);
  }
}
