/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.p2i2.javaarduino;

/**
 *
 * @author lucastissier
 */
public class ConteneurDonnees {
		// Variables d'entrée (résultat des données brutes traitées)
		
		boolean palier1Actif; // Palier coupé ou non : Si false = personne
		boolean palier2Actif;
		boolean palier3Actif; //Sort de l'arduino "Palier"
		int nbPersonnesEntrees; // Sort du Traitement "Rideau" 
		int nbPersonnesSorties; // Sort de l'arduino "Sortie"
		double tempsSortie; // Sort de l'arduino "Sortie"
		
		// Variables à calculer 
		
		double estimation; // LE coeur du problème 
		int nbPersonnesSysteme; // Nombre de personnes dans le système
		double tempsPaliers; // Estimation de temps venant du traitement des paliers coupés
                public String JourSemaine;
                
                
    public ConteneurDonnees(String jour){
        tempsPaliers =0;
	nbPersonnesEntrees=0;
	nbPersonnesSorties=0;
	tempsSortie=0;
	estimation=0;
        this.JourSemaine = jour;
    }
    
    public double calculTempsPaliers(){
	if (palier1Actif == false){
            this.tempsPaliers = 0;             // temps d'attente à modifier 
	}
	if ((palier1Actif==true )&& (palier2Actif == false )){
	    this.tempsPaliers = 850;
	}
	if ((palier1Actif==true )&& (palier2Actif == true )&& (palier3Actif == false )){		
            this.tempsPaliers = 1000;
	}
    return this.tempsPaliers;
    }
	
    public void calculEstimation(){
        // calcul Estimation
        double estimationAncienne = this.estimation;
	this.nbPersonnesSysteme = this.nbPersonnesEntrees-this.nbPersonnesSorties;
        this.estimation = this.nbPersonnesSysteme*this.tempsSortie;
	//Vérification
	//double erreur = Math.abs(this.estimation-this.calculTempsPaliers());
	/*if (erreur>600){ // 10 min
        this.estimation=estimationAncienne;
	}*/		
    }
    
}


