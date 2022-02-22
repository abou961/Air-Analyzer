package fr.insalyon.p2i2.javaarduino;

import fr.insalyon.p2i2.javaarduino.tdtp.BDFlux;
import fr.insalyon.p2i2.javaarduino.usb.ArduinoManager;
import fr.insalyon.p2i2.javaarduino.util.Console;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class TestArduino 
{
    
    public static void main(String[] args) {
                
        // Objet matérialisant la console d'exécution (Affichage Écran / Lecture Clavier)
        final Console console = new Console();
        final BDFlux BD = new BDFlux( "G222_C_BD2", "G222_C", "G222_C");
        final TestArduino tA = new TestArduino();
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
        SimpleDateFormat formatJourSemaine = new SimpleDateFormat("EEEE");
        
        final Date date =  new java.sql.Date(System.currentTimeMillis());
        final Time heure = new java.sql.Time(System.currentTimeMillis());
        final String jour = formatJourSemaine.format(date);
        

        final ConteneurDonnees boite = new ConteneurDonnees(jour);
        boite.JourSemaine = jour;
        //SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
        //SimpleDateFormat formatJourSemaine = new SimpleDateFormat("EEEE");
        //Date date =  new java.sql.Date(System.currentTimeMillis());
        //Time heure = new java.sql.Time(System.currentTimeMillis());
        //String jour = formatJourSemaine.format(date);

        // Affichage sur la console
        console.log("DÉBUT du programme TestArduino");

        console.log("TOUS les Ports COM Virtuels:");
        for (String port : ArduinoManager.listVirtualComPorts()) {
            console.log(" - " + port);
        }
        console.log("----");

        // Recherche d'un port disponible (avec une liste d'exceptions si besoin)
        String myPort = ArduinoManager.searchVirtualComPort("COM0", "/dev/tty.usbserial-FTUS8LMO", "<autre-exception>");

        console.log("CONNEXION au port " + myPort);

        ArduinoManager arduino = new ArduinoManager(myPort) {
          
            @Override
            protected void onData(String line) {

                // Cette méthode est appelée AUTOMATIQUEMENT lorsque l'Arduino envoie des données
                // Affichage sur la Console de la ligne transmise par l'Arduino
                console.println("ARDUINO >> " + line);

                // À vous de jouer ;-)
                // Par exemple:
                try{
                    if(line.trim().length() > 0 ){
                        String[] data = line.split(";");
                        if(data[0].length() >0){
                            int Prediction1 = BD.recherchePrediction(Time.valueOf( "00:05:00" ), heure, boite);
                            int Prediction2 = BD.recherchePrediction(Time.valueOf( "00:10:00" ), heure, boite);
                            int Prediction3 = BD.recherchePrediction(Time.valueOf( "00:15:00" ), heure, boite);
                            int Prediction4 = BD.recherchePrediction(Time.valueOf( "00:20:00" ), heure, boite);
                            int Prediction5 = BD.recherchePrediction(Time.valueOf( "00:25:00" ), heure, boite);
                            switch (Integer.parseInt(data[0])) {
                                case 1: tA.traitementRideau(boite,data);
                                        BD.addResultats(date,boite.estimation,heure,jour);
                                        BD.addSite(boite.estimation,Prediction1,Prediction2,Prediction3,Prediction4,Prediction5);
                                        BD.addStats(date, heure, jour, boite.tempsSortie, boite.nbPersonnesEntrees, boite.nbPersonnesSorties);
                                    break;
                                case 2: tA.traitementSortie(boite,data);
                                        BD.addResultats(date,boite.estimation,heure,jour);
                                        BD.addSite(boite.estimation,BD.recherchePrediction(Time.valueOf( "00:05:00" ), heure, boite),BD.recherchePrediction(Time.valueOf( "00:10:00" ), heure, boite),BD.recherchePrediction(Time.valueOf( "00:15:00" ), heure, boite),BD.recherchePrediction(Time.valueOf( "00:20:00" ), heure, boite),BD.recherchePrediction(Time.valueOf( "00:25:00" ), heure, boite));
                                        BD.addStats(date, heure, jour, boite.tempsSortie, boite.nbPersonnesEntrees, boite.nbPersonnesSorties);
                                    break;
                                case 3: tA.traitementPalier(boite,data,3);
                                        BD.addResultats(date,boite.estimation,heure,jour);
                                        BD.addSite(boite.estimation,BD.recherchePrediction(Time.valueOf( "00:05:00" ), heure, boite),BD.recherchePrediction(Time.valueOf( "00:10:00" ), heure, boite),BD.recherchePrediction(Time.valueOf( "00:15:00" ), heure, boite),BD.recherchePrediction(Time.valueOf( "00:20:00" ), heure, boite),BD.recherchePrediction(Time.valueOf( "00:25:00" ), heure, boite));
                                        BD.addStats(date, heure, jour, boite.tempsSortie, boite.nbPersonnesEntrees, boite.nbPersonnesSorties);
                                    break;
                                case 4: tA.traitementPalier(boite,data,4);
                                        BD.addResultats(date,boite.estimation,heure,jour);
                                        BD.addSite(boite.estimation,BD.recherchePrediction(Time.valueOf( "00:05:00" ), heure, boite),BD.recherchePrediction(Time.valueOf( "00:10:00" ), heure, boite),BD.recherchePrediction(Time.valueOf( "00:15:00" ), heure, boite),BD.recherchePrediction(Time.valueOf( "00:20:00" ), heure, boite),BD.recherchePrediction(Time.valueOf( "00:25:00" ), heure, boite));
                                        BD.addStats(date, heure, jour, boite.tempsSortie, boite.nbPersonnesEntrees, boite.nbPersonnesSorties);
                                    break;
                                case 5: tA.traitementPalier(boite,data,5);
                                        BD.addResultats(date,boite.estimation,heure,jour);
                                        BD.addSite(boite.estimation,BD.recherchePrediction(Time.valueOf( "00:05:00" ), heure, boite),BD.recherchePrediction(Time.valueOf( "00:10:00" ), heure, boite),BD.recherchePrediction(Time.valueOf( "00:15:00" ), heure, boite),BD.recherchePrediction(Time.valueOf( "00:20:00" ), heure, boite),BD.recherchePrediction(Time.valueOf( "00:25:00" ), heure, boite));
                                        BD.addStats(date, heure, jour, boite.tempsSortie, boite.nbPersonnesEntrees, boite.nbPersonnesSorties);
                                    break;
                                default: 
                                    BD.addResultats(date, (double) (Integer.parseInt(data[1])), heure, jour);
                                    
                                
                                    break;
                            }
                        }
                    }
                }catch(SQLException e){
                    console.log(e);
                }
                
                try{
                
                    TimeUnit.SECONDS.sleep(10);
                
                } catch(InterruptedException e){
                    console.log(e);
                } 
            }
        };

        try {

            console.log("DÉMARRAGE de la connexion");
            // Connexion à l'Arduino
            arduino.start();

            console.log("BOUCLE infinie en attente du Clavier");
            // Boucle d'ecriture sur l'arduino (execution concurrente au thread)
            boolean exit = false;

            while (!exit) {

                // Lecture Clavier de la ligne saisie par l'Utilisateur
                String line = console.readLine("Envoyer une ligne (ou 'stop') > ");

                if (line.length() != 0) {

                    // Affichage sur l'écran
                    console.log("CLAVIER >> " + line);

                    // Test de sortie de boucle
                    exit = line.equalsIgnoreCase("stop");

                    if (!exit) {
                        // Envoi sur l'Arduino du texte saisi au Clavier
                        arduino.write(line);
                    }
                }
            }

            console.log("ARRÊT de la connexion");
            // Fin de la connexion à l'Arduino
            arduino.stop();

        } catch (IOException ex) {
            // Si un problème a eu lieu...
            console.log(ex);
        }

    
}

    public void traitementRideau (ConteneurDonnees boite, String[] data) {
        if(data.length==2){
            boite.nbPersonnesEntrees=Integer.parseInt(data[1]);
            System.out.println(boite.nbPersonnesEntrees);
            boite.calculEstimation();
        }
    }
    
    public void traitementPalier (ConteneurDonnees boite, String[] data, int i) {
        if (data.length==2) {
            switch (i) {
                case 3: boite.palier1Actif=Boolean.parseBoolean(data[1]);
                        boite.calculEstimation();
                    break;
                case 4: boite.palier2Actif=Boolean.parseBoolean(data[1]);
                        boite.calculEstimation();
                    break;
                case 5: boite.palier3Actif=Boolean.parseBoolean(data[1]);
                        boite.calculEstimation();
                    break;
            }
        }
    }
    
    public void traitementSortie (ConteneurDonnees boite, String[] data) {
        if (data.length==3) {
            boite.nbPersonnesSorties=Integer.parseInt(data[1]);
            boite.tempsSortie=Double.parseDouble(data[2]);
            boite.calculEstimation();
        }
    }

}
