package modele;

public class Position {
    public final int ligne;
    public final int colonne;

    public Position(int ligne, int colonne) {
        this.ligne = ligne;
        this.colonne = colonne;
    }

    @Override
    public String toString() {
        return "[" + ligne + "," + colonne + "]";
    }
}
