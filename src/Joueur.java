import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class Joueur implements Runnable {
    private final Serveur_Lotterie server;
    private final int n;
    private final int k;
    private final Scanner scanner;
    private final CountDownLatch latch;
    private String nom;

    public Joueur(Serveur_Lotterie server, int n, int k, Scanner scanner, CountDownLatch latch,String nom) {
        this.server = server;
        this.n = n;
        this.k = k;
        this.scanner = scanner;
        this.latch = latch;
        this.nom=nom;
    }

    @Override
    public void run() {
        try {
            synchronized (scanner) {
                try {

                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Bonjour "+this.nom+" :Combien de billets souhaitez-vous acheter?");
                int numberOfTickets = scanner.nextInt();

                for (int i = 0; i < numberOfTickets; i++) {
                    Set<Integer> numbers = new HashSet<>();
                    int category = getCategoryFromUser();

                    if (category == 1) {
                        while (numbers.size() < k) {
                            numbers.add((int) (Math.random() * n) + 1);
                        }
                    } else {
                        System.out.println("veuillez entrer  " + k + " nombre unique entre 1 et " + n + " pour billet " + (i + 1) + ":");
                        while (numbers.size() < k) {
                            int number = scanner.nextInt();
                            if (number > 0 && number <= n && !numbers.contains(number)) {
                                numbers.add(number);
                            } else {
                                System.out.println("Numéro invalide. Veuillez saisir un numéro unique compris entre 1 et " + n);
                            }
                        }
                    }
                    server.ajouter_ticket(new billet(numbers, category));
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            latch.countDown();
        }
    }

    private int getCategoryFromUser() {
        System.out.println("Choisissez la catégorie  pour ce billet 1 pour la catégorie une, 2 pour la catégorie deux :");
        int category;
        do {
            category = scanner.nextInt();
        } while (category != 1 && category != 2);
        return category;
    }
}

