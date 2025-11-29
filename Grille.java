package modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Grille {
    private final int n;
    private final Case[][] grille;
    private final Random rnd = new Random();

    public Grille(int n) {
        this.n = n;
        grille = new Case[n][n];
        // par défaut initialiser à 0
        for (int i=0;i<n;i++) for (int j=0;j<n;j++) grille[i][j] = new Case(0, new Position(i,j));
    }

    public int getN() { return n; }

    public Case getCase(int ligne, int colonne) {
        if (ligne<0||ligne>=n||colonne<0||colonne>=n) return null;
        return grille[ligne][colonne];
    }

    // Méthode pour extraire une ligne/colonne en AlignementCases
    public AlignementCases extraireAlignement(Orientation orientation, int index) {
        List<Case> list = new ArrayList<>();
        if (orientation == Orientation.LIGNE) {
            for (int c = 0; c < n; c++) list.add(grille[index][c]);
        } else {
            for (int r = 0; r < n; r++) list.add(grille[r][index]);
        }
        return new AlignementCases(list, orientation, index);
    }

    // INITIALISATIONS demandées

    // 1) valeurs aléatoires 0..9
    public void initAleatoire0a9() {
        for (int i=0;i<n;i++) for (int j=0;j<n;j++) {
            grille[i][j].setValeur(rnd.nextInt(10));
            grille[i][j].liberer();
        }
    }

    // 2) quart, quart... (distribution progressive)
    public void initDistributionProgressive() {
        int total = n * n;
        // on remplit une liste de valeurs puis on shuffle
        List<Integer> vals = new ArrayList<>();
        int value = 1;
        int remaining = total;
        while (remaining>0) {
            int count = (int) Math.ceil(total / Math.pow(2, value)); // heuristique alternative
            // pour coller à l'énoncé on peut utiliser: count = (int) Math.ceil((double) total/4.0) ... mais on fait une répartition décroissante simple
            // simplifions: répartition par quart successif
            count = Math.max(1, (int)Math.ceil((double) total / (4 * value)));
            for (int k=0;k<count && vals.size()<total;k++) vals.add(value);
            value++;
        }
        // si pas assez, compléter avec 1
        while (vals.size()<total) vals.add(1);
        Collections.shuffle(vals);
        int p=0;
        for (int i=0;i<n;i++) for (int j=0;j<n;j++) {
            grille[i][j].setValeur(vals.get(p++));
            grille[i][j].liberer();
        }
    }

    // 3) T = {{x1,y1},{x2,y2},...} : valeurs x répétés y fois, reste 0
    public void initParTableau(int[][] T) {
        List<Integer> vals = new ArrayList<>();
        for (int[] xy : T) {
            int x = xy[0], y = xy[1];
            for (int k=0;k<y;k++) vals.add(x);
        }
        int total = n*n;
        // remplir tableau par vals puis zeros
        Collections.shuffle(vals);
        int p=0;
        for (int i=0;i<n;i++) for (int j=0;j<n;j++) {
            if (p < vals.size()) grille[i][j].setValeur(vals.get(p++));
            else grille[i][j].setValeur(0);
            grille[i][j].liberer();
        }
    }

    // 4) variante : après T, remplir non affectées en utilisant méthode 2 (on réapplique initDistributionProgressive sur les cases à 0)
    public void initParTableauPuisDistribution(int[][] T) {
        initParTableau(T);
        // collect positions à 0
        List<Case> zeros = new ArrayList<>();
        for (int i=0;i<n;i++) for (int j=0;j<n;j++) if (grille[i][j].getValeur()==0) zeros.add(grille[i][j]);
        // créer une liste de valeurs issues de distribution (taille zeros.size())
        List<Integer> pool = new ArrayList<>();
        int val = 1;
        while (pool.size() < zeros.size()) {
            int cnt = Math.max(1, (int)Math.ceil((double) zeros.size() / (4 * val)));
            for (int k=0;k<cnt && pool.size()<zeros.size();k++) pool.add(val);
            val++;
        }
        Collections.shuffle(pool);
        for (int k=0;k<zeros.size();k++) zeros.get(k).setValeur(pool.get(k));
    }

    public void prendreCase(int ligne, int colonne) {
        getCase(ligne, colonne).prendre();
    }

    public void libererCase(int ligne, int colonne) {
        getCase(ligne, colonne).liberer();
    }

    public boolean resteCasesLibreDansAlignement(Orientation orientation, int index) {
        AlignementCases a = extraireAlignement(orientation, index);
        for (Case c : a.getCases()) if (!c.isPrise()) return true;
        return false;
    }

    public void afficherConsole() {
        // entete colonnes
        System.out.print("    ");
        for (int c = 0; c < n; c++) System.out.print(String.format("%3d", c));
        System.out.println();
        for (int r = 0; r < n; r++) {
            System.out.print("   +");
            for (int c = 0; c < n; c++) System.out.print("--+");
            System.out.println();
            System.out.print(String.format("%2d |", r));
            for (int c = 0; c < n; c++) {
                Case cc = grille[r][c];
                String s = cc.isPrise() ? " X" : String.format("%2d", cc.getValeur());
                System.out.print(s + "|");
            }
            System.out.println();
        }
        System.out.print("   +");
        for (int c = 0; c < n; c++) System.out.print("--+");
        System.out.println();
    }

    // renvoie toutes les positions libres dans un alignement (index dans l'alignement -> colonne si LIGNE, ligne si COLONNE)
    public List<Integer> positionsLibresDansAlignement(Orientation orientation, int index) {
        AlignementCases a = extraireAlignement(orientation, index);
        return a.positionsPossibles();
    }

    // utilitaire pour récupérer la valeur d'une case libre
    public List<Case> toutesCasesLibres() {
        List<Case> res = new ArrayList<>();
        for (int i=0;i<n;i++) for (int j=0;j<n;j++) if (!grille[i][j].isPrise()) res.add(grille[i][j]);
        return res;
    }
}
