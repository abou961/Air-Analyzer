#include  <SPI.h>
#include  <RF24.h>
#include <Adafruit_Sensor.h>
#include <DHT.h>
#include <DHT_U.h>
#include <Wire.h>
#include "MutichannelGasSensor.h"
#include "CO2Sensor.h"

#define DHTPIN 2     // Digital pin connected to the DHT sensor 
// Feather HUZZAH ESP8266 note: use pins 3, 4, 5, 12, 13 or 14 --
// Pin 15 can work but DHT must be disconnected during program upload.

// Uncomment the type of sensor in use:
#define DHTTYPE    DHT11     // DHT 117
unsigned long int tempe;
unsigned long int humi;

CO2Sensor co2Sensor(A0, 0.99, 100);
DHT_Unified dht(DHTPIN, DHTTYPE);

uint32_t delayMS;
RF24 radio(9,10);
uint8_t address[6] = {0x2C,0xCE,0xCC,0xCE,0xCC};   // Adresse du pipe

void setup() {
  //Serial.begin(115200);    // Initialiser la communication série 
  //Serial.println (F("Starting my first test")) ;
  //aDejaMesure = false;
  co2Sensor.calibrate();
  
  radio.begin();
  radio.setChannel(0x2c);
  radio.setDataRate(RF24_2MBPS);
  
  radio.openWritingPipe(address);    // Ouvrir le Pipe en écriture
  radio.stopListening();             
  // Initialize device.
  dht.begin();
  Serial.println(F("DHTxx Unified Sensor Example"));
  // Print temperature sensor details.
  sensor_t sensor;
  dht.temperature().getSensor(&sensor);
  dht.humidity().getSensor(&sensor);
  delayMS = 30000;//sensor.min_delay / 1000;
  delay(120000);
  gas.begin(0x04);//the default I2C address of the slave is 0x04
  gas.powerOn();
}

void loop() {
  // put your main code here, to run repeatedly:
 
  float c;
  c = gas.measure_NH3();
  float d;
  d= gas.measure_CO();
  float e;
  e=gas.measure_NO2();
  int f = co2Sensor.read();
  
  sensors_event_t event;
  dht.temperature().getEvent(&event);
  tempe=event.temperature;
  dht.humidity().getEvent(&event);
  humi=event.relative_humidity;
  //Serial.print(F("\n Now sending Packet "));
  //Serial.print(tempe);
/*const char text[] = "";
String temp = tempe+"oui"; //Store result in a String*/
 String dataToSend= String(humi)+";"+String(tempe)+";"+ String(e)+";"+ String(d)+";"+ String(c)+";"+String(f)+";";
  char inputChar[dataToSend.length()+1] ;
  dataToSend.toCharArray(inputChar,dataToSend.length()+1);
    if (radio.write(&inputChar,sizeof(inputChar))) 
        { //Serial.print(F(" ... Ok ... "));     
        }
        else
        { //Serial.print(F(" ... failed ... ")); 
        }
        //aDejaMesure=false;

   delay(delayMS);
}

/*

boolean aDejaMesure;

unsigned long temps;
#define PIR_MOTION_SENSOR 2
RF24 radio(9,10);
uint8_t address[6] = {0x2C,0xCE,0xCC,0xCE,0xCC};   // Adresse du pipe
int val=0;  // Valeur à envoyer

void setup() {
  Serial.begin(115200);    // Initialiser la communication série 
  Serial.println (F("Starting my first test")) ;
  pinMode(PIR_MOTION_SENSOR, INPUT);
  aDejaMesure = false;
  
  radio.begin();
  radio.setChannel(0x2c);
  radio.setDataRate(RF24_2MBPS);
  
  radio.openWritingPipe(address);    // Ouvrir le Pipe en écriture
  radio.stopListening();             
}

void loop(void) {
  int chk = DHT.read11(DHT11_PIN);
  if(digitalRead(PIR_MOTION_SENSOR) && aDejaMesure==false){
      humi=DHT.humidity;
      tempe=DHT.temperature;
      delay(3000);
      temps=millis();
      aDejaMesure=true;
  }

  if(digitalRead(PIR_MOTION_SENSOR)&& aDejaMesure==true)
  {
      humi=DHT.humidity;
      tempe=DHT.temperature;
      delay(3000);
  }

  if(millis()-temps>5000 && aDejaMesure==true)
  {
    Serial.print(F("\n Now sending Packet "));
    Serial.print(val-1);
    if (radio.write(&val,sizeof(val))) 
        { Serial.print(F(" ... Ok ... "));     
        }
        else
        { Serial.print(F(" ... failed ... ")); 
        }
        aDejaMesure=false;
        val=0;
  }
  }*/
