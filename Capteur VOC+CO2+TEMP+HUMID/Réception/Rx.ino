#include  <SPI.h>
#include  <RF24.h>

RF24 radio(9,10); 
uint8_t address[6] = {0x2C,0xCE,0xCC,0xCE,0xCC};   // Adresse du pipe
String rcv_val;

void setup() {
  Serial.begin(115200);    // Initialiser la communication sÃ©rie 
  
  radio.begin();
 radio.setChannel(0x2c);
  radio.setDataRate(RF24_2MBPS);

  radio.openReadingPipe(0,address); // Ouvrir le Pipe en lecture
  radio.startListening();
}

void loop(void) {
    unsigned long debut = micros();
    unsigned long wait = micros();
    boolean timeout = false;
    delay(3000);
    while (radio.available()) 
      {
     char text[32] = {0};
    radio.read(&text, sizeof(text));
    Serial.println(text);
      }
   unsigned long fin = micros;
  // Serial.print(fin-debut); 
}
