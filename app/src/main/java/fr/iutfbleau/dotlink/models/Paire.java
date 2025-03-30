package fr.iutfbleau.dotlink.models;

import java.io.Serializable;

public class Paire implements Serializable {
    private Point point1;
    private Point point2;

    public Paire(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    public Point getPoint1() {
        return point1;
    }

    public Point getPoint2() {
        return point2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paire)) return false;
        Paire p = (Paire) o;
        return (point1.equals(p.point1) && point2.equals(p.point2)) ||
                (point1.equals(p.point2) && point2.equals(p.point1));
    }

    @Override
    public int hashCode() {
        return point1.hashCode() + point2.hashCode();
    }
}