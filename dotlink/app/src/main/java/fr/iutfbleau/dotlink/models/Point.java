package fr.iutfbleau.dotlink.models;

import java.io.Serializable;

public class Point implements Serializable {
    private int ligne, colonne;

    public Point(int ligne, int colonne) {
        this.ligne = ligne;
        this.colonne = colonne;
    }

    public int getLigne() {
        return ligne;
    }

    public int getColonne() {
        return colonne;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point point = (Point) obj;
        return ligne == point.ligne && colonne == point.colonne;
    }

    @Override
    public int hashCode() {
        return 31 * ligne + colonne;
    }
}