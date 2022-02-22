#include  <SPI.h>
#include  <RF24.h>

RF24 radio(9,10); 
uint8_t address[6] = {0x5C,0xCE,0xCC,0xCE,0xCC};   // Adresse du pipe
int rcv_val = 0;

void setup() {
  Serial.begin(115200);    // Initialiser la communication série 
  // Serial.println (F("Starting my first test")) ;
  
  radio.begin();
  radio.setChannel(0x5C);
  radio.setDataRate(RF24_2MBPS);

  radio.openReadingPipe(0,address); // Ouvrir le Pipe en lecture
  radio.startListening();
}

void loop(void) {
    unsigned long wait = micros();
    boolean timeout = false;
    
    while (radio.available()) 
      {
      radio.read(&rcv_val, sizeof(int));
      Serial.print(rcv_val);
      Serial.print(";");
      Serial.print(2);
      Serial.print(";");
      Serial.println();
      }
    delay(500);
}
