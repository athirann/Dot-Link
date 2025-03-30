package fr.iutfbleau.dotlink.models;

import java.io.Serializable;
import java.util.List;

public class Puzzle implements Serializable {
    private String nom;
    private int taille;
    private List<Paire> paires;
    private boolean isValid;

    public Puzzle(String nom, int taille, List<Paire> paires, boolean isValid) {
        this.nom = nom;
        this.taille = taille;
        this.paires = paires;
        this.isValid = isValid;
    }

    public String getNom() {
        return nom;
    }

    public int getTaille() {
        return taille;
    }

    public List<Paire> getPaires() {
        return paires;
    }

    public boolean isValid() {
        return isValid;
    }
}