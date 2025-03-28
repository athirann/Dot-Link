package fr.iutfbleau.dotlink;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.*;

public class GameView extends View {

    private Puzzle puzzle;
    private int cellSize;
    private Paint gridPaint;
    private Map<Paire, List<Point>> drawnPaths = new HashMap<>();
    private Map<Paire, Paint> pathPaints = new HashMap<>();
    private Set<Point> occupiedPoints = new HashSet<>();

    private Point currentStart;
    private Paire currentPaire;
    private List<Point> currentPath;

    private OnPuzzleCompleteListener puzzleCompleteListener;
    private OnScoreChangeListener scoreChangeListener;

    private boolean achromate = false;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.parseColor("#222222")); // Fond gris foncé
        init();
    }

    private void init() {
        gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#AAAAAA"));
        gridPaint.setStrokeWidth(4);
        gridPaint.setStyle(Paint.Style.STROKE);
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
        drawnPaths.clear();
        pathPaints.clear();
        occupiedPoints.clear();
        assignColorsToPaires();
        invalidate();
        notifyScoreChange();
    }

    public void setAchromate(boolean achromate) {
        this.achromate = achromate;
        assignColorsToPaires();
        invalidate();
    }

    private void assignColorsToPaires() {
        String[] fluoHex = {"#00FF00", "#00FFFF", "#FF00FF", "#FFFF00", "#FF6600", "#FF0033", "#9900FF"};
        String[] grayHex = {"#CCCCCC", "#AAAAAA", "#888888", "#666666", "#444444", "#EEEEEE", "#999999"};
        int i = 0;

        for (Paire paire : puzzle.getPaires()) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(55);
            paint.setStrokeCap(Paint.Cap.ROUND);

            String colorStr = achromate ? grayHex[i % grayHex.length] : fluoHex[i % fluoHex.length];
            paint.setColor(Color.parseColor(colorStr));

            pathPaints.put(paire, paint);
            i++;
        }
    }

    public void setOnPuzzleCompleteListener(OnPuzzleCompleteListener listener) {
        this.puzzleCompleteListener = listener;
    }

    public void setOnScoreChangeListener(OnScoreChangeListener listener) {
        this.scoreChangeListener = listener;
    }

    private void notifyScoreChange() {
        if (scoreChangeListener != null && puzzle != null) {
            int completed = drawnPaths.size();
            int total = puzzle.getPaires().size();
            scoreChangeListener.onScoreChanged(completed, total);
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

        // Grille
        Paint cellPaint = new Paint();
        cellPaint.setColor(Color.parseColor("#333333")); // Gris plus clair
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

        // Points
        for (Paire paire : puzzle.getPaires()) {
            drawPoint(canvas, paire.getPoint1());
            drawPoint(canvas, paire.getPoint2());
        }

        // Tracés
        for (Map.Entry<Paire, List<Point>> entry : drawnPaths.entrySet()) {
            drawPath(canvas, entry.getValue(), pathPaints.get(entry.getKey()));
        }

        if (currentPath != null && currentPaire != null) {
            drawPath(canvas, currentPath, pathPaints.get(currentPaire));
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
        if (path.size() < 2) return;

        Paint glowPaint = new Paint(paint);
        glowPaint.setStyle(Paint.Style.STROKE);
        glowPaint.setStrokeJoin(Paint.Join.ROUND);
        glowPaint.setStrokeCap(Paint.Cap.ROUND);
        glowPaint.setStrokeWidth(paint.getStrokeWidth() * 2.5f);
        glowPaint.setAlpha(100);
        glowPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.NORMAL));

        for (int i = 0; i < path.size() - 1; i++) {
            PointF start = getCellCenter(path.get(i));
            PointF end = getCellCenter(path.get(i + 1));
            canvas.drawLine(start.x, start.y, end.x, end.y, glowPaint);
            canvas.drawLine(start.x, start.y, end.x, end.y, paint);
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

    public int getCompletedCount() {
        return drawnPaths.size();
    }

    public Puzzle getPuzzle() {
        return this.puzzle;
    }

    public void reset() {
        drawnPaths.clear();
        occupiedPoints.clear();
        currentPath = null;
        currentPaire = null;
        currentStart = null;
        invalidate();
        notifyScoreChange();
    }

    private Paire getPaireForPoint(Point p) {
        for (Paire paire : puzzle.getPaires()) {
            if (paire.getPoint1().equals(p) || paire.getPoint2().equals(p)) return paire;
        }
        return null;
    }

    private boolean isAdjacent(Point a, Point b) {
        int dx = Math.abs(a.getColonne() - b.getColonne());
        int dy = Math.abs(a.getLigne() - b.getLigne());
        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        Point touched = getTouchedPoint(x, y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (touched == null) break;
                Paire paire = getPaireForPoint(touched);
                if (paire == null) break;

                currentStart = touched;
                currentPaire = paire;
                currentPath = new ArrayList<>();
                currentPath.add(touched);
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                if (currentPath != null && touched != null) {
                    Point last = currentPath.get(currentPath.size() - 1);
                    if (!touched.equals(last) && isAdjacent(last, touched) && !occupiedPoints.contains(touched)) {
                        currentPath.add(touched);
                        invalidate();
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (currentStart != null && touched != null && currentPaire != null) {
                    Point end = touched;
                    if ((currentPaire.getPoint1().equals(currentStart) && currentPaire.getPoint2().equals(end)) ||
                            (currentPaire.getPoint2().equals(currentStart) && currentPaire.getPoint1().equals(end))) {

                        drawnPaths.put(currentPaire, new ArrayList<>(currentPath));
                        occupiedPoints.addAll(currentPath);

                        notifyScoreChange();

                        if (drawnPaths.size() == puzzle.getPaires().size() && puzzleCompleteListener != null) {
                            puzzleCompleteListener.onPuzzleCompleted();
                        }

                    } else {
                        Toast.makeText(getContext(), "Paire incorrecte ou incomplète", Toast.LENGTH_SHORT).show();
                    }
                }

                currentStart = null;
                currentPaire = null;
                currentPath = null;
                invalidate();
                break;
        }

        return true;
    }
}
