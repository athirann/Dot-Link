package fr.iutfbleau.dotlink.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameModel {
    private Puzzle puzzle;
    private Map<Paire, List<Point>> drawnPaths = new HashMap<>();
    private Set<Point> occupiedPoints = new HashSet<>();
    private int secondsElapsed = 0;

    public GameModel(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public void addPath(Paire paire, List<Point> path) {
        if (path != null && !path.isEmpty()) {
            drawnPaths.put(paire, new ArrayList<>(path));
            occupiedPoints.addAll(path);
        }
    }

    public int getCompletedCount() {
        return drawnPaths.size();
    }

    public int getTotalPairs() {
        return puzzle.getPaires().size();
    }

    public void reset() {
        drawnPaths.clear();
        occupiedPoints.clear();
        secondsElapsed = 0;
    }

    public void incrementTime() {
        secondsElapsed++;
    }

    public int getSecondsElapsed() {
        return secondsElapsed;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public Map<Paire, List<Point>> getDrawnPaths() {
        return drawnPaths;
    }

    public boolean isPointInOtherPaire(Point point, Paire currentPaire) {
        for (Paire paire : puzzle.getPaires()) {
            if (!paire.equals(currentPaire)) { // Exclure la paire courante
                if (paire.getPoint1().equals(point) || paire.getPoint2().equals(point)) {
                    return true; // Le point appartient à une autre paire
                }
            }
        }
        return false;
    }

    public boolean isPointOccupied(Point point) {
        return occupiedPoints.contains(point);
    }
}