package fr.iutfbleau.dotlink.controllers;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;
import fr.iutfbleau.dotlink.models.GameModel;
import fr.iutfbleau.dotlink.models.Paire;
import fr.iutfbleau.dotlink.models.Point;
import fr.iutfbleau.dotlink.views.GameView;
import fr.iutfbleau.dotlink.views.OnPuzzleCompleteListener;
import fr.iutfbleau.dotlink.views.OnScoreChangeListener;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private GameModel model;
    private GameView view;
    private Context context;
    private Handler chronoHandler = new Handler();
    private List<OnPuzzleCompleteListener> completeListeners = new ArrayList<>();
    private List<OnScoreChangeListener> scoreListeners = new ArrayList<>();
    private Point currentStart;
    private Paire currentPaire;
    private List<Point> currentPath;

    public GameController(GameModel model, GameView view, Context context) {
        this.model = model;
        this.view = view;
        this.context = context;
        startChrono();
    }

    private void startChrono() {
        chronoHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                model.incrementTime();
                notifyScoreChange();
                chronoHandler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    public void startPath(Point start) {
        currentPaire = getPaireForPoint(start);
        if (currentPaire != null) {
            currentStart = start;
            currentPath = new ArrayList<>();
            currentPath.add(start);
            view.setCurrentPath(currentPath, currentPaire);
        }
    }

    public void updatePath(Point touched) {
        if (currentPath != null && touched != null && isAdjacent(currentPath.get(currentPath.size() - 1), touched)) {
            // Éviter les doublons dans le chemin
            if (!currentPath.contains(touched)) {
                // Vérifier si le point appartient à une autre paire
                if (model.isPointInOtherPaire(touched, currentPaire)) {
                    Toast.makeText(context, "Impossible de passer à travers un autre point !", Toast.LENGTH_SHORT).show();
                    currentStart = null;
                    currentPaire = null;
                    currentPath = null;
                    view.setCurrentPath(null, null);
                    return;
                }
                // Vérifier si le point est déjà occupé par un chemin validé
                if (model.isPointOccupied(touched) && !isEndPoint(touched, currentPaire)) {
                    Toast.makeText(context, "Impossible de passer à travers un chemin existant !", Toast.LENGTH_SHORT).show();
                    currentStart = null;
                    currentPaire = null;
                    currentPath = null;
                    view.setCurrentPath(null, null);
                    return;
                }
                currentPath.add(touched);
                view.setCurrentPath(currentPath, currentPaire);
            }
        }
    }

    public void endPath(Point end) {
        if (currentPaire != null && currentPath != null && end != null) {
            // Ajouter le point final au chemin s'il est adjacent et non déjà présent
            if (isAdjacent(currentPath.get(currentPath.size() - 1), end) && !currentPath.contains(end)) {
                // Vérifier si le point final est valide (ne doit pas appartenir à une autre paire sauf si c'est le point d'arrivée)
                if (end.equals(currentPaire.getPoint1()) || end.equals(currentPaire.getPoint2())) {
                    currentPath.add(end);
                } else if (model.isPointInOtherPaire(end, currentPaire)) {
                    Toast.makeText(context, "Point final invalide : appartient à une autre paire !", Toast.LENGTH_SHORT).show();
                    currentStart = null;
                    currentPaire = null;
                    currentPath = null;
                    view.setCurrentPath(null, null);
                    return;
                } else if (model.isPointOccupied(end)) {
                    Toast.makeText(context, "Point final invalide : déjà occupé par un chemin !", Toast.LENGTH_SHORT).show();
                    currentStart = null;
                    currentPaire = null;
                    currentPath = null;
                    view.setCurrentPath(null, null);
                    return;
                }
            }

            if (isValidPath(currentPaire, currentPath, end)) {
                model.addPath(currentPaire, currentPath);
                view.updatePaths(model.getDrawnPaths());
                notifyScoreChange();
                if (model.getCompletedCount() == model.getTotalPairs()) {
                    chronoHandler.removeCallbacksAndMessages(null);
                    notifyPuzzleComplete();
                }
            } else {
                Toast.makeText(context, "Paire incorrecte ou incomplète", Toast.LENGTH_SHORT).show();
            }
        }
        currentStart = null;
        currentPaire = null;
        currentPath = null;
        view.setCurrentPath(null, null);
    }

    public void resetGame() {
        model.reset();
        view.updatePaths(model.getDrawnPaths());
        chronoHandler.removeCallbacksAndMessages(null);
        startChrono();
        notifyScoreChange();
    }

    public void addPuzzleCompleteListener(OnPuzzleCompleteListener listener) {
        completeListeners.add(listener);
    }

    public void addScoreChangeListener(OnScoreChangeListener listener) {
        scoreListeners.add(listener);
    }

    private void notifyPuzzleComplete() {
        for (OnPuzzleCompleteListener listener : completeListeners) {
            listener.onPuzzleCompleted();
        }
    }

    private void notifyScoreChange() {
        for (OnScoreChangeListener listener : scoreListeners) {
            listener.onScoreChanged(model.getCompletedCount(), model.getTotalPairs());
        }
    }

    private Paire getPaireForPoint(Point p) {
        for (Paire paire : model.getPuzzle().getPaires()) {
            if (paire.getPoint1().equals(p) || paire.getPoint2().equals(p)) return paire;
        }
        return null;
    }

    private boolean isAdjacent(Point a, Point b) {
        int dx = Math.abs(a.getColonne() - b.getColonne());
        int dy = Math.abs(a.getLigne() - b.getLigne());
        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
    }

    private boolean isValidPath(Paire paire, List<Point> path, Point end) {
        if (path.size() < 2) return false;
        Point start = path.get(0);
        return (paire.getPoint1().equals(start) && paire.getPoint2().equals(end)) ||
                (paire.getPoint2().equals(start) && paire.getPoint1().equals(end));
    }

    private boolean isEndPoint(Point point, Paire paire) {
        return point.equals(paire.getPoint1()) || point.equals(paire.getPoint2());
    }

    public GameModel getModel() {
        return model;
    }
}