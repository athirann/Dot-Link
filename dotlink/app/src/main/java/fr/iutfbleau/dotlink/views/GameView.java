package fr.iutfbleau.dotlink.views;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import fr.iutfbleau.dotlink.controllers.GameController;
import fr.iutfbleau.dotlink.models.Paire;
import fr.iutfbleau.dotlink.models.Point;
import fr.iutfbleau.dotlink.models.Puzzle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameView extends View {
    private Puzzle puzzle;
    private int cellSize;
    private Paint gridPaint;
    private Map<Paire, List<Point>> drawnPaths = new HashMap<>();
    private Map<Paire, Paint> pathPaints = new HashMap<>();
    private GameController controller;
    private Point currentStart;
    private Paire currentPaire;
    private List<Point> currentPath;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.parseColor("#222222"));
        init();
    }

    private void init() {
        gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#AAAAAA"));
        gridPaint.setStrokeWidth(4);
        gridPaint.setStyle(Paint.Style.STROKE);
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
        drawnPaths.clear();
        pathPaints.clear();
        assignColorsToPaires();
        invalidate();
    }

    public void updatePaths(Map<Paire, List<Point>> paths) {
        this.drawnPaths = paths;
        invalidate();
    }

    public void setCurrentPath(List<Point> path, Paire paire) {
        this.currentPath = path;
        this.currentPaire = paire;
        invalidate();
    }

    private void assignColorsToPaires() {
        String[] fluoHex = {"#00FF00", "#00FFFF", "#FF00FF", "#FFFF00", "#FF6600", "#FF0033", "#9900FF"};
        int i = 0;
        for (Paire paire : puzzle.getPaires()) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(55);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(Color.parseColor(fluoHex[i % fluoHex.length]));
            pathPaints.put(paire, paint);
            i++;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (puzzle == null) return;

        int size = puzzle.getTaille();
        cellSize = getWidth() / size;

        // Dessiner la grille
        Paint cellPaint = new Paint();
        cellPaint.setColor(Color.parseColor("#333333"));
        int spacing = cellSize / 8;
        float radius = cellSize / 10f;

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                float left = col * cellSize + spacing;
                float top = row * cellSize + spacing;
                float right = (col + 1) * cellSize - spacing;
                float bottom = (row + 1) * cellSize - spacing;
                canvas.drawRoundRect(left, top, right, bottom, radius, radius, cellPaint);
            }
        }

        // Dessiner les points
        for (Paire paire : puzzle.getPaires()) {
            drawPoint(canvas, paire.getPoint1());
            drawPoint(canvas, paire.getPoint2());
        }

        // Dessiner les chemins validés (drawnPaths)
        for (Map.Entry<Paire, List<Point>> entry : drawnPaths.entrySet()) {
            Paire paire = entry.getKey();
            List<Point> path = entry.getValue();
            Paint paint = pathPaints.get(paire);
            if (paint != null && path != null && !path.isEmpty()) {
                drawPath(canvas, path, paint);
            }
        }

        // Dessiner le chemin en cours (currentPath)
        if (currentPath != null && currentPaire != null && !currentPath.isEmpty()) {
            Paint paint = pathPaints.get(currentPaire);
            if (paint != null) {
                drawPath(canvas, currentPath, paint);
            }
        }
    }

    private void drawPoint(Canvas canvas, Point point) {
        for (Map.Entry<Paire, Paint> entry : pathPaints.entrySet()) {
            Paire paire = entry.getKey();
            if (paire.getPoint1().equals(point) || paire.getPoint2().equals(point)) {
                Paint pointPaint = new Paint(entry.getValue());
                pointPaint.setStyle(Paint.Style.FILL);
                int spacing = cellSize / 8;
                float radius = cellSize / 10f;
                float left = point.getColonne() * cellSize + spacing;
                float top = point.getLigne() * cellSize + spacing;
                float right = (point.getColonne() + 1) * cellSize - spacing;
                float bottom = (point.getLigne() + 1) * cellSize - spacing;
                canvas.drawRoundRect(left, top, right, bottom, radius, radius, pointPaint);
                break;
            }
        }
    }

    private void drawPath(Canvas canvas, List<Point> path, Paint paint) {
        if (path == null || path.size() < 2) return;
        for (int i = 0; i < path.size() - 1; i++) {
            Point start = path.get(i);
            Point end = path.get(i + 1);
            PointF startCenter = getCellCenter(start);
            PointF endCenter = getCellCenter(end);
            canvas.drawLine(startCenter.x, startCenter.y, endCenter.x, endCenter.y, paint);
        }
    }

    private PointF getCellCenter(Point point) {
        float x = point.getColonne() * cellSize + cellSize / 2f;
        float y = point.getLigne() * cellSize + cellSize / 2f;
        return new PointF(x, y);
    }

    private Point getTouchedPoint(float x, float y) {
        int col = (int) (x / cellSize);
        int row = (int) (y / cellSize);
        if (row >= 0 && row < puzzle.getTaille() && col >= 0 && col < puzzle.getTaille()) {
            return new Point(row, col);
        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        Point touched = getTouchedPoint(x, y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (touched != null) {
                    controller.startPath(touched);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (touched != null) {
                    controller.updatePath(touched);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (touched != null) {
                    controller.endPath(touched);
                }
                break;
        }
        return true;
    }
}