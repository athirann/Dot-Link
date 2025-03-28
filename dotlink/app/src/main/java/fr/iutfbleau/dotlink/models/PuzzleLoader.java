package fr.iutfbleau.dotlink.models;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PuzzleLoader {
    private static final String TAG = "PuzzleLoader";

    public static List<Puzzle> chargerPuzzles(Context context) {
        List<Puzzle> puzzles = new ArrayList<>();
        AssetManager assetManager = context.getAssets();

        try {
            String[] fichiers = assetManager.list("puzzles");
            if (fichiers != null) {
                for (String fichier : fichiers) {
                    if (fichier.endsWith(".xml")) {
                        Puzzle puzzle = chargerPuzzle(context, "puzzles/" + fichier);
                        if (puzzle != null) {
                            puzzles.add(puzzle);
                        } else {
                            Log.e(TAG, "Puzzle invalide : " + fichier);
                        }
                    }
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Erreur lors de la lecture des fichiers puzzles", e);
        }

        return puzzles;
    }

    private static Puzzle chargerPuzzle(Context context, String chemin) {
        try {
            InputStream inputStream = context.getAssets().open(chemin);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, "UTF-8");

            int eventType = parser.getEventType();
            String nom = null;
            int taille = -1;
            List<Paire> paires = new ArrayList<>();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tag = parser.getName();

                    if ("puzzle".equals(tag)) {
                        nom = parser.getAttributeValue(null, "nom");
                        String tailleStr = parser.getAttributeValue(null, "taille");
                        if (tailleStr != null) {
                            taille = Integer.parseInt(tailleStr);
                            if (taille < 5 || taille > 14) {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    } else if ("paire".equals(tag)) {
                        Point point1 = null;
                        Point point2 = null;

                        parser.next();
                        while (!(parser.getEventType() == XmlPullParser.END_TAG && "paire".equals(parser.getName()))) {
                            if (parser.getEventType() == XmlPullParser.START_TAG && "point".equals(parser.getName())) {
                                int ligne = Integer.parseInt(parser.getAttributeValue(null, "ligne"));
                                int colonne = Integer.parseInt(parser.getAttributeValue(null, "colonne"));
                                if (ligne < 0 || colonne < 0 || ligne >= taille || colonne >= taille) {
                                    return null;
                                }

                                if (point1 == null) {
                                    point1 = new Point(ligne, colonne);
                                } else {
                                    point2 = new Point(ligne, colonne);
                                }
                            }
                            parser.next();
                        }

                        if (point1 != null && point2 != null) {
                            paires.add(new Paire(point1, point2));
                        } else {
                            return null;
                        }
                    }
                }
                eventType = parser.next();
            }

            if (nom == null) {
                nom = chemin.substring(chemin.lastIndexOf("/") + 1, chemin.lastIndexOf("."));
            }

            return new Puzzle(nom, taille, paires);
        } catch (IOException | XmlPullParserException e) {
            Log.e(TAG, "Erreur lors du parsing du puzzle : " + chemin, e);
            return null;
        }
    }
}