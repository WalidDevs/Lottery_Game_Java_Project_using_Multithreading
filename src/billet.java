import java.util.Set;
import java.util.UUID;

public class billet {
    private final String Id_Ticket;
    private final Set<Integer> numéros;
    private final int Catégorie;

    public billet(Set<Integer> numéros, int Catégorie) {
        this.Id_Ticket = generateUniqueId();
        this.numéros = numéros;
        this.Catégorie = Catégorie;
    }

    private String generateUniqueId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 20);
    }

    public String getId_Ticket() {
        return Id_Ticket;
    }

    public Set<Integer> getnuméros() {
        return numéros;
    }

    public int getCatégorie() {
        return Catégorie;
    }

    @Override
    public String toString() {
        String CatégorieString = (Catégorie == 1) ? "Categorie une" : "Categorie deux";
        return " ID du Ticket ---> " + Id_Ticket + ", Categorie ---> " + CatégorieString + ", Numéros ---> " + numéros;
    }
}
