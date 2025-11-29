package modele;

public class Case {
    private int valeur;
    private final Position pos;
    private boolean prise;

    public Case(int valeur, Position pos) {
        this.valeur = valeur;
        this.pos = pos;
        this.prise = false;
    }

    public int getValeur() { return valeur; }
    public Position getPosition() { return pos; }
    public boolean isPrise() { return prise; }
    public void prendre() { prise = true; }
    public void liberer() { prise = false; }

    public void setValeur(int v) { this.valeur = v; }

    @Override
    public String toString() {
        return (prise ? " X" : String.format("%2d", valeur));
    }
}
