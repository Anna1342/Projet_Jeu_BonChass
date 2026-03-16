package entites;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class GestionnaireDonnees {
    private static final String NOM_FICHIER = "joueurs_stats.dat";

    // Sauvegarde la Map complète (Nom -> StatistiquesGlobales)
    public static void sauvegarder(Map<String, StatistiquesGlobales> map) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOM_FICHIER))) {
            oos.writeObject(map);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    // Charge la Map depuis le fichier
    @SuppressWarnings("unchecked")
    public static Map<String, StatistiquesGlobales> charger() {
        File fichier = new File(NOM_FICHIER);
        if (!fichier.exists()) return new HashMap<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NOM_FICHIER))) {
            return (Map<String, StatistiquesGlobales>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }
}
