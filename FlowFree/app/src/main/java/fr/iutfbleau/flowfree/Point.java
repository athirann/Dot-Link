package fr.iutfbleau.flowfree;

public class Point {
    private int ligne, colonne;

    public Point(int ligne, int colonne){
        this.ligne = ligne;
        this.colonne = colonne;
    }

    public int getLigne() {
        return ligne;
    }

    public int getColonne() {
        return colonne;
    }
}
