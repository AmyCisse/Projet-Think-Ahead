package modele;

public class Position{
  public final int ligne;
  public final int colone;

  pubic Position(int ligne, int colonne){
    this.ligne = ligne;
    this.colonne = colonne;
  }

  @Override
  pubic String toString(){
    return "[" +ligne+,"," + colone +"]";
  }
}
  
