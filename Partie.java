package modele;

import java.util.LinkedList;
import java.util.List;

public class Partie {
    private final Grille grille;
    private final Joueur joueur1;
    private final Joueur joueur2;
    private final LinkedList<Coup> historique;
    private Orientation orientationCourante; // où doit jouer le joueur actuel
    private int indexAlignementCourant; // index de la ligne ou colonne
    private Joueur joueurCourant;

    public Partie(Grille g, Joueur j1, Joueur j2, Orientation startOrientation, int startIndex) {
        this.grille = g;
        this.joueur1 = j1;
        this.joueur2 = j2;
        this.historique = new LinkedList<>();
        this.orientationCourante = startOrientation;
        this.indexAlignementCourant = startIndex;
        this.joueurCourant = j1;
    }

    public Grille getGrille() { return grille; }
    public Joueur getJoueurCourant() { return joueurCourant; }
    public Orientation getOrientationCourante() { return orientationCourante; }
    public int getIndexAlignementCourant() { return indexAlignementCourant; }
    public List<Coup> getHistorique() { return historique; }

    // joue un coup à la position (ligne,colonne). Retourne true si coup valide et joué.
    public boolean jouerCoup(Position pos) {
        Case c = grille.getCase(pos.ligne, pos.colonne);
        if (c == null || c.isPrise()) return false;
        // vérifier que la position est bien dans l'alignement demandé
        if (orientationCourante == Orientation.LIGNE && pos.ligne != indexAlignementCourant) return false;
        if (orientationCourante == Orientation.COLONNE && pos.colonne != indexAlignementCourant) return false;
        // appliquer
        c.prendre();
        joueurCourant.ajouterScore(c.getValeur());
        Coup coup = new Coup(joueurCourant, c, orientationCourante, indexAlignementCourant);
        historique.add(coup);
        // préparation du prochain joueur : orientation change (ligne<->colonne), index devient position pivot
        if (orientationCourante == Orientation.LIGNE) {
            orientationCourante = Orientation.COLONNE;
            indexAlignementCourant = pos.colonne;
        } else {
            orientationCourante = Orientation.LIGNE;
            indexAlignementCourant = pos.ligne;
        }
        // changer joueur
        joueurCourant = (joueurCourant == joueur1) ? joueur2 : joueur1;
        // si l'alignement suivant n'a plus de cases libres => fin immédiate (on traite en boucle de jeu)
        return true;
    }

    public boolean peutJouerJoueurCourant() {
        List<Integer> pos = grille.positionsLibresDansAlignement(orientationCourante, indexAlignementCourant);
        return !pos.isEmpty();
    }

    public void annulerDernierCoup() {
        if (historique.isEmpty()) return;
        Coup last = historique.removeLast();
        // restaurer : liberer la case, retirer le score du joueur qui avait joué
        last.getLaCase().liberer();
        last.getJoueur().ajouterScore(- last.getLaCase().getValeur()); // retirer le score (attention : getValeur est inchangé)
        // reconstituer l'état : orientation et index = orientation et alignIndex du coup précédent (avant qu'il soit joué)
        // On peut recalculer : si historique vide => remettre à état initial n/a (on ne le stocke pas ici)
        if (!historique.isEmpty()) {
            Coup prev = historique.getLast();
            // état courant doit être le joueur qui avait joué prev's opponent
            // recompute orientationCourante et indexAlignementCourant à partir du dernier coup restant
            // after last remaining coup, next player to move is opponent of prev.getJoueur()
            // orientationCourante should be opposite of prev.getOrientation() and index = pivot of prev
            Orientation prevOrient = prev.getOrientation();
            Position pivot = prev.getLaCase().getPosition();
            if (prevOrient == Orientation.LIGNE) {
                orientationCourante = Orientation.COLONNE;
                indexAlignementCourant = pivot.colonne;
            } else {
                orientationCourante = Orientation.LIGNE;
                indexAlignementCourant = pivot.ligne;
            }
            joueurCourant = (prev.getJoueur() == joueur1) ? joueur2 : jogador1;
        } else {
            // si plus de coups, on laisse l'état à une valeur par défaut (à gérer selon appelant)
            // ici on remet joueurCourant à joueur1, orientation et index restent les mêmes (ou à définir par appelant)
            joueurCourant = joueur1;
        }
    }
}
