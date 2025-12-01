package modeTexte;

import modele.*;

import java.util.Random;
import java.util.Scanner;

public class MainConsole {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== ThinkAhead - Mode texte ===");
        System.out.print("Taille de la grille (m) ? ");
        int m = Integer.parseInt(sc.nextLine().trim());

        Grille g = new Grille(m);
        // choix d'initialisation
        System.out.println("Choisir initialisation : 1=0..9 aléatoire, 2=distribution, 3=tableau T, 4=T puis distribution");
        int init = Integer.parseInt(sc.nextLine().trim());
        if (init==1) g.initAleatoire0a9();
        else if (init==2) g.initDistributionProgressive();
        else if (init==3) {
            // exemple T via saisie simple: nombre d'éléments puis pairs
            System.out.print("Combien de paires (x,y) ? ");
            int k = Integer.parseInt(sc.nextLine().trim());
            int[][] T = new int[k][2];
            for (int i=0;i<k;i++) {
                System.out.print("x y (ex: 5 3) : ");
                String[] sp = sc.nextLine().trim().split("\\s+");
                T[i][0] = Integer.parseInt(sp[0]);
                T[i][1] = Integer.parseInt(sp[1]);
            }
            g.initParTableau(T);
        } else {
            System.out.print("Combien de paires (x,y) ? ");
            int k = Integer.parseInt(sc.nextLine().trim());
            int[][] T = new int[k][2];
            for (int i=0;i<k;i++) {
                System.out.print("x y (ex: 5 3) : ");
                String[] sp = sc.nextLine().trim().split("\\s+");
                T[i][0] = Integer.parseInt(sp[0]);
                T[i][1] = Integer.parseInt(sp[1]);
            }
            g.initParTableauPuisDistribution(T);
        }

        System.out.print("Nom joueur 1 (humain) : ");
        String nom1 = sc.nextLine().trim();
        Joueur j1 = new JoueurHumain(nom1, sc);

        System.out.print("Joueur 2: 1=humain, 2=ordi : ");
        int t2 = Integer.parseInt(sc.nextLine().trim());
        Joueur j2;
        if (t2 == 1) {
            System.out.print("Nom joueur 2 : ");
            j2 = new JoueurHumain(sc.nextLine().trim(), sc);
        } else {
            System.out.print("Nom ordi : ");
            String nomOrdi = sc.nextLine().trim();
            System.out.print("Choisir stratégie ordi (1 alea, 2 max, 3 ecart) : ");
            int strat = Integer.parseInt(sc.nextLine().trim());
            j2 = new JoueurOrdinateur(nomOrdi, strat);
        }

        // choix ligne/colonne initiale aléatoire
        Random rnd = new Random();
        Orientation startOrient = rnd.nextBoolean() ? Orientation.LIGNE : Orientation.COLONNE;
        int startIndex = rnd.nextInt(m);
        System.out.println("Orientation de départ: " + startOrient + " index=" + startIndex);

        Partie partie = new Partie(g, j1, j2, startOrient, startIndex);

        // boucle de jeu
        while (true) {
            System.out.println("\n=== Etat du plateau ===");
            g.afficherConsole();
            System.out.println(j1.getNom() + " score=" + j1.getScore() + " | " + j2.getNom() + " score=" + j2.getScore());
            System.out.println("Coup n°" + (partie.getHistorique().size()+1) + " (" + partie.getJoueurCourant().getNom() + ")");
            System.out.println("Vous devez jouer dans " + (partie.getOrientationCourante()==Orientation.LIGNE? "ligne ":"colonne ") + partie.getIndexAlignementCourant());
            // possibilité d'annuler si humain commande
            if (partie.getJoueurCourant() instanceof JoueurHumain) {
                System.out.print("Entrez 'u' pour annuler dernier coup, sinon Entrée pour continuer: ");
                String line = sc.nextLine().trim();
                if (line.equalsIgnoreCase("u")) {
                    partie.annulerDernierCoup();
                    continue;
                }
            }

            if (!partie.peutJouerJoueurCourant()) {
                System.out.println("Le joueur " + partie.getJoueurCourant().getNom() + " ne peut pas jouer -> fin de la partie.");
                break;
            }
            Joueur courant = partie.getJoueurCourant();
            Position choix = courant.jouer(g, partie.getOrientationCourante(), partie.getIndexAlignementCourant());
            if (choix == null) {
                System.out.println("Aucun choix possible pour " + courant.getNom());
                break;
            }
            boolean ok = partie.jouerCoup(choix);
            if (!ok) {
                System.out.println("Coup invalide, recommencez.");
            }
        }

        System.out.println("\n=== Fin de partie ===");
        g.afficherConsole();
        System.out.println(j1.getNom() + " score=" + j1.getScore() + " | " + j2.getNom() + " score=" + j2.getScore());
        if (j1.getScore() > j2.getScore()) System.out.println(j1.getNom() + " gagne !");
        else if (j2.getScore() > j1.getScore()) System.out.println(j2.getNom() + " gagne !");
        else System.out.println("Egalité !");
        sc.close();
    }
}
