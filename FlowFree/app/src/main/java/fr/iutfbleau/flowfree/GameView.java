package fr.iutfbleau.flowfree;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameView extends View {
    private Puzzle puzzle;
    private int cellSize;
    private Paint gridPaint, pointPaint, pathPaint;
    private Map<Paire, Path> paths;
    private Point startPoint;
    private boolean puzzleTermine = false;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        gridPaint = new Paint();
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStrokeWidth(5);
        gridPaint.setStyle(Paint.Style.STROKE);

        pointPaint = new Paint();
        pointPaint.setColor(Color.RED);
        pointPaint.setStyle(Paint.Style.FILL);

        pathPaint = new Paint();
        pathPaint.setColor(Color.BLUE);
        pathPaint.setStrokeWidth(10);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);

        paths = new HashMap<>();
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
        paths.clear();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (puzzle == null) return;

        int size = puzzle.getTaille();
        cellSize = getWidth() / size;

        // Dessiner la grille
        for (int i = 0; i <= size; i++) {
            canvas.drawLine(i * cellSize, 0, i * cellSize, getHeight(), gridPaint);
            canvas.drawLine(0, i * cellSize, getWidth(), i * cellSize, gridPaint);
        }

        // Dessiner les points
        for (Paire paire : puzzle.getPaires()) {
            drawPoint(canvas, paire.getPoint1());
            drawPoint(canvas, paire.getPoint2());
        }

        // Dessiner les chemins tracés
        for (Path path : paths.values()) {
            canvas.drawPath(path, pathPaint);
        }
    }

    private void drawPoint(Canvas canvas, Point point) {
        int cx = point.getColonne() * cellSize + cellSize / 2;
        int cy = point.getLigne() * cellSize + cellSize / 2;
        canvas.drawCircle(cx, cy, cellSize / 4, pointPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int col = (int) (event.getX() / cellSize);
        int row = (int) (event.getY() / cellSize);
        Point touchedPoint = new Point(row, col);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isPuzzlePoint(touchedPoint)) {
                    startPoint = touchedPoint;
                    paths.put(new Paire(startPoint, touchedPoint), new Path());
                    paths.get(new Paire(startPoint, touchedPoint)).moveTo(event.getX(), event.getY());
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (startPoint != null && paths.containsKey(new Paire(startPoint, touchedPoint))) {
                    paths.get(new Paire(startPoint, touchedPoint)).lineTo(event.getX(), event.getY());
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                if (startPoint != null) {
                    startPoint = null;
                }
                if (verifierSolution()) {
                    Toast.makeText(getContext(), "Bravo ! Puzzle complété 🎉", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return true;
    }

    private boolean isPuzzlePoint(Point point) {
        for (Paire paire : puzzle.getPaires()) {
            if (paire.getPoint1().equals(point) || paire.getPoint2().equals(point)) {
                return true;
            }
        }
        return false;
    }

    private boolean verifierSolution() {
        int taille = puzzle.getTaille();
        boolean[][] grilleCouverte = new boolean[taille][taille];

        // Vérifier chaque paire
        for (Paire paire : puzzle.getPaires()) {
            Point p1 = paire.getPoint1();
            Point p2 = paire.getPoint2();

            if (!paths.containsKey(paire)) return false; // Paire non reliée

            Path path = paths.get(paire);
            if (!verifierChemin(path, p1, p2, grilleCouverte)) return false; // Chemin incorrect
        }

        // Vérifier que toutes les cases sont couvertes
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                if (!grilleCouverte[i][j]) return false; // Case vide détectée
            }
        }



        return true; // Puzzle complété !
    }


    private boolean verifierChemin(Path path, Point p1, Point p2, boolean[][] grilleCouverte) {
        // Récupère les coordonnées de départ et d'arrivée
        int startX = p1.getColonne();
        int startY = p1.getLigne();
        int endX = p2.getColonne();
        int endY = p2.getLigne();

        // Créer un PathMeasure pour mesurer le Path
        PathMeasure pathMeasure = new PathMeasure(path, false);

        // Vérifiez si le path contient des segments
        float pathLength = pathMeasure.getLength();
        if (pathLength == 0) {
            return false; // Le chemin est vide
        }

        // Tableau pour les coordonnées (x, y) et la tangente
        float[] coords = new float[2];
        float[] tangent = new float[2]; // Tableau pour la tangente

        // Vérifier que le point de départ correspond à la première position du Path
        pathMeasure.getPosTan(0f, coords, tangent);  // Ajouter le tableau pour la tangente
        if (Math.round(coords[0]) != startX || Math.round(coords[1]) != startY) {
            return false; // Si le premier point du path n'est pas le même que le point de départ
        }

        // Utiliser une précision plus fine pour la distance parcourue (par exemple, chaque 0.1 unité)
        for (float dist = 0; dist < pathLength; dist += 0.1f) {
            pathMeasure.getPosTan(dist, coords, tangent);  // Ajouter la tangente ici

            // Vérifiez que nous avons bien des coordonnées valides
            if (coords[0] < 0 || coords[1] < 0) {
                continue; // Éviter les coordonnées invalides
            }

            int x = Math.round(coords[0]);
            int y = Math.round(coords[1]);

            // Vérifier si le point a déjà été couvert
            if (grilleCouverte[y][x]) return false; // Chevauchement détecté

            // Marquer le point comme couvert
            grilleCouverte[y][x] = true;
        }

        // Vérifier que le dernier point atteint correspond bien au point final
        pathMeasure.getPosTan(pathLength, coords, tangent);
        return Math.round(coords[0]) == endX && Math.round(coords[1]) == endY;
    }

}
