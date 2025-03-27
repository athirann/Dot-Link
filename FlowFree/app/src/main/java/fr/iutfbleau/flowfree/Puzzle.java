package fr.iutfbleau.flowfree;

import java.util.List;

public class Puzzle {
    private String nom;
    private int taille;
    private List<Paire> paires;

    public Puzzle(String nom, int taille, List<Paire> paires){
        this.nom = nom;
        this.taille = taille;
        this.paires = paires;
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
}
