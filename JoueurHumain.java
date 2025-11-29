package modele;

import java.util.List;
import java.util.Scanner;

public class JoueurHumain extends Joueur {
    private final Scanner sc;

    public JoueurHumain(String nom, Scanner sc) {
        super(nom);
        this.sc = sc;
    }

    @Override
    public Position jouer(Grille g, Orientation o, int alignIndex) {
        List<Integer> positions = g.positionsLibresDansAlignement(o, alignIndex);
        if (positions.isEmpty()) return null;
        System.out.println("Coup (" + nom + ") - vous devez jouer dans " + (o==Orientation.LIGNE? "la ligne ":"la colonne ") + alignIndex);
        System.out.println("Positions possibles (index dans l'alignement) : " + positions);
        int chooseIndex = -1;
        while (true) {
            System.out.print("Saisissez la position (index dans l'alignement) : ");
            try {
                String line = sc.nextLine();
                chooseIndex = Integer.parseInt(line.trim());
                if (!positions.contains(chooseIndex)) {
                    System.out.println("Position non valide.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("Entrée invalide, recommencez.");
            }
        }
        // convertir index dans alignement vers (ligne,colonne)
        if (o == Orientation.LIGNE) {
            return new Position(alignIndex, chooseIndex);
        } else {
            return new Position(chooseIndex, alignIndex);
        }
    }
}
