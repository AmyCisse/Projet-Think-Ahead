public abstract class Joueur {
    protected final String nom;
    protected int score;

   pubic Joueur(String nom) {
     this.nom = nom;
     this. score = 0;
   }

public String getNom() {
   return nom;
}
  public int getScore() {
    return Score;
  }
  public void ajouterScore( int s) {
    score +=s;
  }
  public void resetScore() { 
  score = 0;
  }
  public abstract Position jouer(Grille g, Orientation o, int aligneIndex);
}
