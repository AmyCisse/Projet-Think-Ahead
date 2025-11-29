package modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * strategies:
 * 1 -> Aleatoire
 * 2 -> Maximal (choisir la valeur max)
 * 3 -> Ecart maximal: suppose que l'adversaire utilise strategie 2
 * 4 -> profondeur (non implémenté complet)
 */
public class JoueurOrdinateur extends Joueur {
    private final int strategie;
    private final Random rnd = new Random();

    public JoueurOrdinateur(String nom, int strategie) {
        super(nom);
        this.strategie = strategie;
    }

    @Override
    public Position jouer(Grille g, Orientation o, int alignIndex) {
        List<Integer> positions = g.positionsLibresDansAlignement(o, alignIndex);
        if (positions.isEmpty()) return null;
        if (strategie == 1) return jouerAleatoire(g, o, alignIndex, positions);
        if (strategie == 2) return jouerMax(g, o, alignIndex, positions);
        if (strategie == 3) return jouerEcartMax(g, o, alignIndex, positions);
        return jouerMax(g, o, alignIndex, positions); // fallback
    }

    private Position jouerAleatoire(Grille g, Orientation o, int alignIndex, List<Integer> positions) {
        int choice = positions.get(rnd.nextInt(positions.size()));
        if (o==Orientation.LIGNE) return new Position(alignIndex, choice);
        else return new Position(choice, alignIndex);
    }

    private Position jouerMax(Grille g, Orientation o, int alignIndex, List<Integer> positions) {
        int bestPos = positions.get(0);
        int bestVal = Integer.MIN_VALUE;
        for (int pos : positions) {
            Case c = (o==Orientation.LIGNE) ? g.getCase(alignIndex,pos) : g.getCase(pos,alignIndex);
            if (c.getValeur() > bestVal) {
                bestVal = c.getValeur();
                bestPos = pos;
            }
        }
        if (o==Orientation.LIGNE) return new Position(alignIndex, bestPos);
        else return new Position(bestPos, alignIndex);
    }

    // stratégie 3 : pour chaque coup possible, simuler que l'adversaire prendra la meilleure valeur (strat 2)
    private Position jouerEcartMax(Grille g, Orientation o, int alignIndex, List<Integer> positions) {
        int bestPos = positions.get(0);
        int bestScoreDiff = Integer.MIN_VALUE;
        for (int pos : positions) {
            // valeur prise par nous
            Case ourCase = (o==Orientation.LIGNE)? g.getCase(alignIndex,pos) : g.getCase(pos,alignIndex);
            int ourVal = ourCase.getValeur();
            // simuler prise
            ourCase.prendre();
            // orientation suivant : si on a joué dans une ligne alors l'adversaire devra jouer dans la colonne pos
            Orientation oppOrient = (o==Orientation.LIGNE) ? Orientation.COLONNE : Orientation.LIGNE;
            int oppIndex = (o==Orientation.LIGNE) ? pos : (o==Orientation.COLONNE ? pos : 0);
            // récupérer positions libres pour l'adversaire
            List<Integer> oppPositions = g.positionsLibresDansAlignement(oppOrient, oppIndex);
            int oppBest = 0;
            if (!oppPositions.isEmpty()) {
                // adversaire utilise stratégie 2 => prend la valeur max
                oppBest = Integer.MIN_VALUE;
                for (int p2 : oppPositions) {
                    Case cc = (oppOrient==Orientation.LIGNE)? g.getCase(oppIndex,p2) : g.getCase(p2,oppIndex);
                    if (!cc.isPrise() && cc.getValeur() > oppBest) oppBest = cc.getValeur();
                }
            } else {
                // si pas de position pour l'adversaire => il ne joue plus (fin de partie)
                oppBest = 0;
            }
            // restaurer la case
            ourCase.liberer();
            int diff = ourVal - oppBest;
            if (diff > bestScoreDiff) {
                bestScoreDiff = diff;
                bestPos = pos;
            }
        }
        if (o==Orientation.LIGNE) return new Position(alignIndex, bestPos);
        else return new Position(bestPos, alignIndex);
    }
}
