import java.util.Scanner;
import java.util.concurrent.*;

public class Main{
    public static void main(String[] args) {
        System.out.println(" Début de vente des billets");
        int n = 90;
        int k = 5;
        int t =2;
        int nombre_de_joueurs = 5;
        int durée = 40; // la Durée

        Scanner scanner = new Scanner(System.in);
        Serveur_Lotterie server = new Serveur_Lotterie(n, k, t);
        server.start();

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(server::Arreter_serveur, durée, TimeUnit.SECONDS);

        CountDownLatch latch = new CountDownLatch(nombre_de_joueurs);


        Liste_des_Joueurs.Joueurs(nombre_de_joueurs,scanner,server,latch,n,k);


        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            scanner.close();
            scheduler.shutdown();
        }
    }
}

