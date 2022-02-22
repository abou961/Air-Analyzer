#include "payload_out.h";
#include  <SPI.h>
#include  <RF24.h>

RF24 radio(9,10);
uint8_t address[6] = {0x1E,0xCE,0xCC,0xCE,0xCC};   // Adresse du pipe

double temps1 =0;
double temps2 = 0;
boolean passage = false;
double intervalletemps = 0;
int nbPersonnes =0;
struct payload_out val_out;

void setup() {
  Serial.begin(115200);    // Initialiser la communication série 
  Serial.println (F("Starting my first test")) ;
  
  radio.begin();
  radio.setChannel(100);
  radio.setDataRate(RF24_2MBPS);
  
  radio.openWritingPipe(address);    // Ouvrir le Pipe en écriture
  radio.stopListening();

  pinMode(2, INPUT);
  double temps1 = millis();          
}

void loop() {
  
  int laser = analogRead(A0);
  if ((laser >300)&&(passage==false)){ // Laser coupé 
    temps2 = millis();
    intervalletemps=temps2-temps1;
    temps1=temps2;
    passage=true;
    nbPersonnes +=1;
  } 
  if ((laser <300)&&(passage==true)){
    passage=false;
  }
  Serial.print(F("\n Now sending Packet "));
  Serial.println(intervalletemps);
  Serial.println(nbPersonnes);
  val_out.intervalletemps = (int) (intervalletemps);
  val_out.persOut = nbPersonnes;
  /*if (radio.write(&id,sizeof(id))) 
        {*/  if (radio.write(&val_out,sizeof(payload_out))) 
            { Serial.print(F(" ... Ok ... "));     
            }
            else
            { Serial.print(F(" ... failed ... ")); 
            } 
       /* }
        else
        { Serial.print(F(" ... failed ... ")); 
        }*/

  delay(100);
}
