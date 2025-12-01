package modele;

public abstract class Joueur {
    protected final String nom;
    protected int score;

    public Joueur(String nom) {
        this.nom = nom;
        this.score = 0;
    }

    public String getNom() { return nom; }
    public int getScore() { return score; }
    public void ajouterScore(int s) { score += s; }
    public void resetScore() { score = 0; }

    // retourne la position choisie (Position ligne,colonne)
    // paramètres: la grille, orientation en cours (l'alignement où le joueur doit jouer), index de l'alignement
    public abstract Position jouer(Grille g, Orientation o, int alignIndex);
}
