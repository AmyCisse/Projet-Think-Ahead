package modele;

public class Coup {
    private final Joueur joueur;
    private final Case laCase;
    private final Orientation orientation; // orientation de l'alignement joué (ligne ou colonne)
    private final int alignIndex; // quelle ligne ou colonne (0..n-1)

    public Coup(Joueur joueur, Case laCase, Orientation orientation, int alignIndex) {
        this.joueur = joueur;
        this.laCase = laCase;
        this.orientation = orientation;
        this.alignIndex = alignIndex;
    }

    public Joueur getJoueur() { return joueur; }
    public Case getLaCase() { return laCase; }
    public Orientation getOrientation() { return orientation; }
    public int getAlignIndex() { return alignIndex; }

    @Override
    public String toString() {
        return joueur.getNom() + " -> " + laCase.getPosition() + " (" + laCase.getValeur() + ")";
    }
}
