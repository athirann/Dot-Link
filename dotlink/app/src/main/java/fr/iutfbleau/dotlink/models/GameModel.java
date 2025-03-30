package fr.iutfbleau.dotlink.models;

import java.util.ArrayList;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
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

    public boolean hasPathForPaire(Paire paire) {
        return drawnPaths.containsKey(paire);
    }

    public void removePath(Paire paire) {
        List<Point> path = drawnPaths.get(paire);
        if (path != null) {
            // Supprimer les points occupés par ce chemin, sauf les extrémités (points de la paire)
            for (Point point : path) {
                if (!isEndPoint(point, paire)) {
                    occupiedPoints.remove(point);
                }
            }
            drawnPaths.remove(paire);
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

    private boolean isEndPoint(Point point, Paire paire) {
        return point.equals(paire.getPoint1()) || point.equals(paire.getPoint2());
    }

    public boolean isComplete() {
        return getCompletedCount() == getTotalPairs();
    }
    public void chargerSauvegarde(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("DotLinkSave", Context.MODE_PRIVATE);
        String nomSauvegarde = prefs.getString("puzzleName", null);

        if (nomSauvegarde == null || !nomSauvegarde.equals(puzzle.getNom())) return;

        this.secondsElapsed = prefs.getInt("elapsedTime", 0);

        String pathData = prefs.getString("paths", "");
        if (!pathData.isEmpty()) {
            String[] chemins = pathData.split(";");
            for (String chemin : chemins) {
                String[] parts = chemin.split(":");
                if (parts.length != 2) continue;

                int index = Integer.parseInt(parts[0]);
                if (index < 0 || index >= puzzle.getPaires().size()) continue;

                Paire paire = puzzle.getPaires().get(index);
                String[] pointsStr = parts[1].split("-");
                List<Point> cheminPoints = new ArrayList<>();

                for (String pointStr : pointsStr) {
                    String[] coord = pointStr.split(",");
                    if (coord.length == 2) {
                        int col = Integer.parseInt(coord[0]);
                        int lig = Integer.parseInt(coord[1]);
                        Point pt = new Point(lig, col);
                        cheminPoints.add(pt);
                        if (!isEndPoint(pt, paire)) {
                            occupiedPoints.add(pt);
                        }
                    }
                }

                drawnPaths.put(paire, cheminPoints);
            }
        }
    }



    public void sauvegarder(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("DotLinkSave", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("puzzleName", puzzle.getNom());

        editor.putInt("elapsedTime", secondsElapsed);

        StringBuilder pathsBuilder = new StringBuilder();
        for (Paire paire : drawnPaths.keySet()) {
            int index = puzzle.getPaires().indexOf(paire);
            List<Point> chemin = drawnPaths.get(paire);
            if (chemin != null && !chemin.isEmpty()) {
                pathsBuilder.append(index).append(":");
                for (Point point : chemin) {
                    pathsBuilder.append(point.getColonne()).append(",").append(point.getLigne()).append("-");
                }
                pathsBuilder.deleteCharAt(pathsBuilder.length() - 1); // remove last dash
                pathsBuilder.append(";");
            }
        }
        editor.putString("paths", pathsBuilder.toString());

        editor.apply();
    }

}