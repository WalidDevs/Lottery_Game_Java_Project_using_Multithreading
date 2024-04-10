import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Serveur_Lotterie extends Thread {
    private final int n;
    private final int k;
    private final int t;
    private final BlockingQueue<billet> ticketQueue;
    private final List<billet> billet_vendu;
    private final Random random = new Random();
    private Set<Integer> nb_gagnants;
    private volatile boolean running = true;

    public Serveur_Lotterie(int n, int k, int t) {
        this.n = n;
        this.k = k;
        this.t = t;
        this.ticketQueue = new ArrayBlockingQueue<>(100);
        this.billet_vendu = new ArrayList<>();
    }

    public void run() {
        try {
            while (running || !ticketQueue.isEmpty()) {
                billet ticket = ticketQueue.poll(100, TimeUnit.MILLISECONDS);
                if (ticket != null) {
                    fournir_ticket(ticket);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Nombre_gagnants();
        annoncer_vainqueur();
    }


    private void fournir_ticket(billet ticket) {
        billet_vendu.add(ticket);
        System.out.println("Billet fourni:" + ticket);
        ecrire_ticket(ticket);
    }


    public void Nombre_gagnants() {

        do {
            nb_gagnants = new HashSet<>();
            while (nb_gagnants.size() < k) {
                int randomNumber = random.nextInt(n) + 1;
                nb_gagnants.add(randomNumber);
            }

            System.out.println("Les Nombres gagnants: " + nb_gagnants);
        } while (!ticket_gagnant());
    }

    private boolean ticket_gagnant() {
        for (billet ticket : billet_vendu) {
            Set<Integer> ticketNumbers = new HashSet<>(ticket.getnuméros());
            ticketNumbers.retainAll(nb_gagnants);
            if (ticketNumbers.size() >= t) {
                return true;
            }
        }
        return false;
    }




    public void annoncer_vainqueur() {
        if (nb_gagnants == null || nb_gagnants.isEmpty()) {
            System.out.println("Aucun numéro gagnant n'est tiré.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("gagnant_lotterie.txt"))) {
            for (billet ticket : billet_vendu) {
                Set<Integer> ticketNumbers = new HashSet<>(ticket.getnuméros());
                ticketNumbers.retainAll(nb_gagnants);
                if (ticketNumbers.size() >= t) {
                    String info_gagnant = "Les tickets gagnants " + ticket.getId_Ticket() + ", Numéros correspondants: " + ticketNumbers;
                    System.out.println(info_gagnant);
                    writer.write(info_gagnant);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture des gagnants dans le fichier: " + e.getMessage());
        }
    }

    public void ajouter_ticket(billet ticket) throws InterruptedException {
        ticketQueue.put(ticket);
    }

    public void Arreter_serveur() {
        running = false; // Signal pour arreter le serveur
    }

    private void ecrire_ticket(billet ticket) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("billets_vendu.txt", true))) {
            writer.write("ID du Ticket---> " + ticket.getId_Ticket() + ", Numéros ---> " + ticket.getnuméros() + ", Categorie---> " + ticket.getCatégorie());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture des gagnants dans le fichier: " + e.getMessage());
        }
    }
}
