import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Liste_des_Joueurs {


    public static void Joueurs(int nombre_de_joueurs, Scanner scanner, Serveur_Lotterie server, CountDownLatch latch,int n,int k){
        String[] nom_joueurs= new String[5];
        nom_joueurs[0]="walid";
        nom_joueurs[1]="adnane";
        nom_joueurs[2]="hamid";
        nom_joueurs[3]="sui";
        nom_joueurs[4]="deku";
        Thread[] threads = new Thread[nombre_de_joueurs];

        for (int i = 0; i < nombre_de_joueurs; i++) {
                threads[i] = new Thread(new Joueur(server, n, k, scanner, latch, nom_joueurs[i]));
                threads[i].start();

        }
    }
}
