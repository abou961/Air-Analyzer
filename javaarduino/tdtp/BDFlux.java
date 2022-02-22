package fr.insalyon.p2i2.javaarduino.tdtp;

import fr.insalyon.p2i2.javaarduino.ConteneurDonnees;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class BDFlux {

    private Connection conn;
    private PreparedStatement addEstimation;
    private PreparedStatement selectMesuresStatement;
    SimpleDateFormat formatJour = new SimpleDateFormat("dd/mm/yyyy"); 
    Calendar monJour = new GregorianCalendar(2019,01,01);

    public static void main(String[] args) {

        //BDFlux bdFlux = new BDFlux("<base-de-donnees>", "<login>", "<mot-de-passe>");
        //BDFlux bdFlux = new BDFlux( "G222_C_BD3", "G222_C", "G222_C");        
//bdFlux.lireMesuresDepuisFichier("L:\\TDTP3\\mesures-input.txt");

//        bdFlux.ecrireMesures(
//                new PrintWriter(System.out, true),
//                1,
//                new GregorianCalendar(2016, Calendar.MAY, 8).getTime(),
//                new GregorianCalendar(2016, Calendar.MAY, 9).getTime()
//            );
//        
//        bdFlux.ecrireMesuresDansFichier(
//                "L:\\TDTP3\\output",
//                1,
//                new GregorianCalendar(2016, Calendar.MAY, 8).getTime(),
//                new GregorianCalendar(2016, Calendar.MAY, 9).getTime()
//            );

    }

    public BDFlux(String bd, String compte, String motDePasse) {
        try {

            //Enregistrement de la classe du driver par le driverManager
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver trouvé...");

            //Création d'une connexion sur la base de donnée
            this.conn = DriverManager.getConnection("jdbc:mysql://PC-TP-MYSQL.insa-lyon.fr:3306/" + "G222_C_BD3", "G222_C", "G222_C");
            //this.conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/" + bd, compte, motDePasse);
            System.out.println("Connexion établie...");

            // Prepared Statement
            //this.insertMesureStatement = this.conn.prepareStatement("INSERT INTO Mesure (valeur,idCapteur,dateMesure) VALUES (?,?,?) ;");
            //this.selectMesuresStatement = this.conn.prepareStatement("SELECT valeur,idCapteur numInventaire,dateMesure FROM Mesure WHERE idCapteur = ? AND dateMesure >= ? AND dateMesure < ? ;");

        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }
    }
    
    public void addResultats(java.sql.Date date, double estimation, Time heure, String jour) throws SQLException{
    
    String insertSQL = "INSERT INTO Resultats(Date, Estimation,Heure,JourSemaine) VALUES(?,?,?,?)";
    this.addEstimation = (com.mysql.jdbc.PreparedStatement) this.conn.prepareStatement(insertSQL);
           
    try{
        // affectation des valeurs des parametres 
        
        //this.addEstimation.setDouble(1, heure); 
        this.addEstimation.setDate(1,date);
        this.addEstimation.setInt(2, (int) estimation);
        this.addEstimation.setTime(3, heure);
        this.addEstimation.setString(4, jour);
        
        // execution de la requete 
        
        this.addEstimation.executeUpdate(); 
        System.out.println("Estimation ajoutee !");
        } catch(SQLException ex){
            ex.printStackTrace(System.err);
            System.exit(-1); 
        }
    }
    
    public void addSite(double derniereEstimation,int prevision1, int prevision2,int prevision3, int prevision4, int prevision5) throws SQLException{
        
        String truncate = "TRUNCATE TABLE Site ";
        String insertSQL = "INSERT INTO Site(DerniereEstimation,Prevision1,Prevision2,Prevision3,Prevision4,Prevision5) VALUES(?,?,?,?,?)";
        this.addEstimation = (com.mysql.jdbc.PreparedStatement) this.conn.prepareStatement(truncate);
        this.addEstimation.executeUpdate();  
        this.addEstimation = (com.mysql.jdbc.PreparedStatement) this.conn.prepareStatement(insertSQL);
           
        try{
       
            this.addEstimation.setInt(1,(int)derniereEstimation);
            this.addEstimation.setInt(2, prevision1);
            this.addEstimation.setInt(3, prevision2);
            this.addEstimation.setInt(4, prevision3);
            this.addEstimation.setInt(5, prevision4);
            this.addEstimation.setInt(6, prevision5);
            
            // execution de la requete 
            this.addEstimation.executeUpdate(); 
            System.out.println("Site mis a jour !");
            } catch(SQLException ex){
                ex.printStackTrace(System.err);
                System.exit(-1);
        }
    }
    public int recherchePrediction(Time ecart, Time tempsActuel, ConteneurDonnees boite) throws SQLException{ //recherche l'estimation à mettre
        int prediction = 0;
        String sql = "SELECT Prediction FROM Prevision WHERE JourSemaine =  "+boite.JourSemaine+" AND (SELECT TIMEDIFF(Heure,?))=?";
        PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
        try{
            preparedStatement.setTime(1,tempsActuel);
            preparedStatement.setTime(2,ecart);
            ResultSet resultat = preparedStatement.executeQuery();
            prediction = resultat.getInt(1);
            
        } catch(SQLException ex){
          ex.printStackTrace(System.err);
          System.exit(-1);}
        return prediction;
    }
     public void addStats(Date date, Time heure, String JourSemaine, double TempsSortie,int NbPersIn, int NbPersOut) throws SQLException{
        String insertSQL = "INSERT INTO Statistiques(Date,Heure,JourSemaine,TempsSortie,NbPersIn,NbPersOut) VALUES(?,?,?,?,?)"; 
        this.addEstimation = (com.mysql.jdbc.PreparedStatement) this.conn.prepareStatement(insertSQL);
           
        try{
       
            this.addEstimation.setDate(1, (java.sql.Date) date);
            this.addEstimation.setTime(2, heure);
            this.addEstimation.setString(3, JourSemaine);
            this.addEstimation.setInt(4, NbPersIn);
            this.addEstimation.setInt(5, NbPersOut);
            
            // execution de la requete 
            this.addEstimation.executeUpdate(); 
            System.out.println("Statistiques mis a jour !");
            } catch(SQLException ex){
                ex.printStackTrace(System.err);
                System.exit(-1);
        }  
     }

/* /// CODE A RECUP SI JAMAIS
    public void lireMesuresDepuisFichier(String cheminVersFichier) {
        try {
            // À compléter
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    new FileInputStream(cheminVersFichier)
            ));

            lireMesures(input);

            input.close();

        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }

    }

    public void lireMesures(BufferedReader input) {
        try {

            String line;

            while ((line = input.readLine()) != null) {
                String[] valeurs = line.split(";");
                if (valeurs.length > 1) {

                    // À compléter
                    Integer numInventaire = Integer.parseInt(valeurs[0]);
                    Double valeur = Double.parseDouble(valeurs[1]);
                    System.out.println("Le Capteur n°" + numInventaire + " a mesuré: " + valeur);

                    ajouterMesure(numInventaire, valeur, new Date());
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }

    }

    public int ajouterMesure(int numInventaire, double valeur, Date datetime) {
        try {
            this.insertMesureStatement.setDouble(1, valeur);
            this.insertMesureStatement.setInt(2, numInventaire);
            this.insertMesureStatement.setTimestamp(3, new Timestamp(datetime.getTime())); // DATETIME
            return this.insertMesureStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
            return -1;
        }
    }

    public void ecrireMesures(PrintWriter output, int numInventaire, Date dateDebut, Date dateFin) {

        try {

            this.selectMesuresStatement.setInt(1, numInventaire);
            this.selectMesuresStatement.setTimestamp(2, new Timestamp(dateDebut.getTime())); // DATETIME
            this.selectMesuresStatement.setTimestamp(3, new Timestamp(dateFin.getTime())); // DATETIME
            ResultSet result = this.selectMesuresStatement.executeQuery();

            SimpleDateFormat formatDatePourCSV = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DecimalFormat formatNombreDecimal = new DecimalFormat("0.00");
            formatNombreDecimal.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ROOT));

            while (result.next()) {

                output.println(
                        result.getInt("numInventaire") + ";"
                        + formatDatePourCSV.format(new Date(result.getTimestamp("dateMesure").getTime())) + ";"
                        + formatNombreDecimal.format(result.getDouble("valeur")) + ";"
                );

            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }
    }

    public void ecrireMesuresDansFichier(String cheminVersDossier, int numInventaire, Date dateDebut, Date dateFin) {

        try {
            SimpleDateFormat formatDatePourNomFichier = new SimpleDateFormat("yyyyMMdd-HHmmss");
            String datePourNomFichier = formatDatePourNomFichier.format(new Date());
            String nomFichier = "mesures-output_" + datePourNomFichier + ".csv";

            PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(cheminVersDossier + "\\" + nomFichier)
            ));

            ecrireMesures(writer, numInventaire, dateDebut, dateFin);

            writer.close();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }
    }

*/ 
}
