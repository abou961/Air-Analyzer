#include  <SPI.h>
#include  <RF24.h>

RF24 radio(9,10);
uint8_t address[6] = {0x5C,0xCE,0xCC,0xCE,0xCC};   // Adresse du pipe

double temps1 = 0;
double temps2 = 0;
int tempsDelai = 0;
boolean passage = false;
boolean passagePrecedent = false;
boolean changementEtat = false;
boolean actif = false;
String id = "3;"; // A CHANGER SELON LE PALIER : 3 / 4 / 5
String val;

void setup() {
  Serial.begin(115200);    // Initialiser la communication série 
  Serial.println (F("Starting my first test")) ;
  
  radio.begin();
  radio.setChannel(0x5C);
  radio.setDataRate(RF24_2MBPS);
  
  radio.openWritingPipe(address);    // Ouvrir le Pipe en écriture
  radio.stopListening();             
}

void loop(void) {
  
  int laser = analogRead(A0); // On actualise la situation du capteur (faisceau coupé ou non)
  passagePrecedent=passage;
  if (laser >110){
    passage=true;
  }else{
    passage=false;
  }
  if (passagePrecedent!=passage){ // On teste si la situation du capteur a changé
    temps2 = millis();
    temps1=temps2;
    changementEtat = true;   
  }else{
    temps2 = millis();
    tempsDelai = temps2-temps1;
    if (tempsDelai>=4000){
      if (changementEtat==true){
        changementEtat = false;
        if (actif==false){
          actif=true;
        }else{
          actif=false;
        }
      }
    }
  }
  val = actif+";";
  
  Serial.println(tempsDelai);
 
  Serial.print(F("\n Now sending Packet "));
  Serial.print(actif);
  if (radio.write(&id,sizeof(id)))
    { if (radio.write(&val,sizeof(val))) 
        { Serial.print(F(" ... Ok ... "));     
        }
        else
        { Serial.print(F(" ... failed ... ")); 
        }
    } else
    { Serial.print(F(" ... failed ... "));
    }
  delay(100);
  
}
