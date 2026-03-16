package entites;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;

import java.util.Map;
import java.util.Optional;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.geometry.Pos; 
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;

public class MainFX extends Application implements Observateur { 

    private static final int TAILLE_CASE = 30;

    private GridPane grilleVisuelle;
    private Grille grilleJeu;
    private Bonhomme bonhomme;
    private Chasseur chasseur;
    private Label labelBonhomme;
    private Label labelChasseur;
    private ProgressBar barBonhomme;
    private ProgressBar barChasseur;
    private BorderPane root;
    private HBox topBar;
    private final int ENERGIE_MAX_B = 70; // On définit un max pour calculer le %
    private final int ENERGIE_MAX_C = 70;
    private int taille = 20;
    private boolean gameOver = false;
    private javafx.animation.Timeline timelineAuto; // Le "moteur" du mode auto
    private Map<String, StatistiquesGlobales> dictionnaireJoueurs;
    private StatistiquesGlobales stats;
    private long debutPartie; // Pour calculer la durée de la partie
    private int compteurCoups = 0;

    // Méthode utilitaire pour le formatage demandée
    private String formaterTemps(long totalSecondes) {
        java.time.Duration d = java.time.Duration.ofSeconds(totalSecondes);
        return String.format("%02d:%02d:%02d", d.toHours(), d.toMinutesPart(), d.toSecondsPart());
    }

    @Override
    public void start(Stage stage) {
    	
        // Demander le nom et comparer avec les joueurs existants
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nom du joueur");
        dialog.setHeaderText("Choisissez votre nom");
        dialog.setContentText("Nom :");
        Optional<String> result = dialog.showAndWait();
        String nom = result.orElse("Joueur");
        
        dictionnaireJoueurs = GestionnaireDonnees.charger();
        if (dictionnaireJoueurs.containsKey(nom)) {
            this.stats = dictionnaireJoueurs.get(nom);
        } else {
            this.stats = new StatistiquesGlobales();
            dictionnaireJoueurs.put(nom, stats);
        }

        // Initialisation 
        grilleJeu = new Grille(taille);
        bonhomme = new Bonhomme(nom, 17, null);
        chasseur = new Chasseur(40, null, bonhomme); 
        
        bonhomme.ajouterObservateur(this); 
        chasseur.ajouterObservateur(this);
        
        grilleJeu.placerElements(bonhomme, chasseur);
        
        // Initialisation du Label
        double largeurGrille = taille * TAILLE_CASE;
        
        // Création des barres
        barBonhomme = new ProgressBar(0);
        barBonhomme.setPrefWidth(170); 
        barBonhomme.setPrefHeight(25); 
        barBonhomme.setStyle("-fx-accent: green;");
        
        Node iconB = chargerImage("bonhomme.png");
        if (iconB instanceof ImageView) {
            ((ImageView) iconB).setFitWidth(50);
            ((ImageView) iconB).setFitHeight(50);
        }

        barChasseur = new ProgressBar(0);
        barChasseur.setPrefWidth(170);
        barChasseur.setPrefHeight(25);
        barChasseur.setStyle("-fx-accent: green;"); 
        
        Node iconC = chargerImage("chasseur.png");
        if (iconC instanceof ImageView) {
            ((ImageView) iconC).setFitWidth(50);
            ((ImageView) iconC).setFitHeight(50);
        }

        labelBonhomme = new Label();
        labelBonhomme.setStyle("-fx-font-size: 16px; -fx-font-family: 'Arial Black'; -fx-text-fill: #000000;");
        
        labelChasseur = new Label();
        labelChasseur.setStyle("-fx-font-size: 16px; -fx-font-family: 'Arial Black'; -fx-text-fill: #000000;");

        // Conteneur Gauche (Bonhomme)
        VBox boxB = new VBox(10, labelBonhomme, barBonhomme);
        HBox headerB = new HBox(10, iconB, boxB); // 15px d'espace entre l'image et les infos
        headerB.setAlignment(Pos.CENTER_LEFT);
        headerB.setPadding(new Insets(10));
        boxB.setPadding(new Insets(10)); 
        boxB.setPrefWidth(largeurGrille / 2); 

        // Conteneur Droite (Chasseur)
        VBox boxC = new VBox(10, labelChasseur, barChasseur);
        HBox headerC = new HBox(10, boxC, iconC);
        headerC.setAlignment(Pos.CENTER_RIGHT);
        headerC.setPadding(new Insets(10));
        boxC.setAlignment(Pos.TOP_RIGHT);
        boxC.setPadding(new Insets(10));
        boxC.setPrefWidth(largeurGrille / 2);

        // Layout de l'en-tête
        topBar = new HBox();
        topBar.getChildren().addAll(headerB, headerC);
        topBar.setAlignment(Pos.CENTER);
        
        grilleVisuelle = new GridPane();
        grilleVisuelle.setAlignment(Pos.CENTER);

        rafraichirGrille();
        
        // --- LA CORRECTION EST ICI ---
        root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setTop(topBar); // On met les barres en haut
        root.setCenter(grilleVisuelle); // On met la grille au milieu
        
        // On donne "root" à la scène et on agrandit la fenêtre (850x900)
        Scene scene = new Scene(root, 850, 900); 
        stage.setTitle("Jeu de Grille");
        stage.setScene(scene);
        stage.show();
        
        preparerModeAuto();
        
    	// On demarre le timer de la partie une fois toute l'initialisation terminée
    	this.debutPartie = System.currentTimeMillis() ;
    }
    
    private void initialiserNouvellePartie() {
        // 1. Réinitialisation des états
    	if (timelineAuto != null) {
            timelineAuto.stop();
            timelineAuto = null; // On force le nettoyage
        }
    	
        this.gameOver = false;
        this.compteurCoups = 0;
        
        // 2. Recréation des entités (en gardant le même nom)
        String nomActuel = (bonhomme != null) ? bonhomme.getNom() : "Joueur";
        this.grilleJeu = new Grille(taille);
        this.bonhomme = new Bonhomme(nomActuel, 17, null);
        this.chasseur = new Chasseur(40, null, bonhomme);
        
        // 3. Réenregistrement des observateurs
        this.bonhomme.ajouterObservateur(this);
        this.chasseur.ajouterObservateur(this);
        
        // 4. Placement et affichage
        this.grilleJeu.placerElements(bonhomme, chasseur);
        this.root.setTop(topBar); // On remet la barre standard (sans les boutons de fin)
        
        rafraichirGrille();
        preparerModeAuto();
        
        // 5. Relance du chronomètre
        this.debutPartie = System.currentTimeMillis();
    }
    
    @Override
    public void actualiser() {
        // Platform.runLater est crucial ici pour que JavaFX mette à jour 
        // l'interface dès que le calcul du thread moteur est fini
        javafx.application.Platform.runLater(() -> {
            rafraichirGrille();
            verifierFinJeu();
        });
    }

    private void rafraichirGrille() {
        grilleVisuelle.getChildren().clear();
        // Mise à jour Bonhomme
        labelBonhomme.setText(bonhomme.getNom() + " : " + bonhomme.getEnergie());
        double prgB = (double) bonhomme.getEnergie() / ENERGIE_MAX_B;
        barBonhomme.setProgress(Math.min(1.0, Math.max(0.0, prgB)));

        // Mise à jour Chasseur
        labelChasseur.setText("Chasseur : " + chasseur.getEnergie());
        double prgC = (double) chasseur.getEnergie() / ENERGIE_MAX_C;
        barChasseur.setProgress(Math.min(1.0, Math.max(0.0, prgC)));
        
        Case posB = bonhomme.getCaseActuelle();
        Case posC = chasseur.getCaseActuelle();

        for(int y = 0; y < taille; y++) {
            for(int x = 0; x < taille; x++) {
                Case c = grilleJeu.getCases(x, y);
                Node affichageCase = null;
                
                // Calcul visibilité => case visible si à 1de dist du bonhomme ou du chasseur
                int distB = Math.abs(x - posB.getX()) + Math.abs(y - posB.getY());
                int distC = Math.abs(x - posC.getX()) + Math.abs(y - posC.getY());
                boolean estVisible = (distB <= 1) || (distC <=1);
                		
                if (estVisible) {
                    // SI VISIBLE : On affiche le contenu normalement (ton code actuel)
                    if(bonhomme.getCaseActuelle() == c && bonhomme.estVivant()) {
                        affichageCase = chargerImage("bonhomme.png");
                    } 
                    else if(chasseur.getCaseActuelle() == c && chasseur.estVivant()) {
                        affichageCase = chargerImage("chasseur.png");
                    } 
                    else if(c == grilleJeu.getCible()) {
                        Rectangle rCible = new Rectangle(TAILLE_CASE, TAILLE_CASE);
                        rCible.setFill(Color.GOLD);
                        rCible.setStroke(Color.BLACK);
                        affichageCase = rCible;
                    } 
                    else if(c.getItem() != null) {
                        Item item = c.getItem();                    
                        if(item instanceof AdaptateurFruit) affichageCase = chargerImage("fruit.png");
                        else if(item instanceof AdaptateurFeu) affichageCase = chargerImage("feu.png");
                        else if(item instanceof AdaptateurPredateur) affichageCase = chargerImage("predateur.png");
                    }

                    if(affichageCase == null) {
                        Rectangle vide = new Rectangle(TAILLE_CASE, TAILLE_CASE);
                        vide.setFill(Color.WHITE);
                        vide.setStroke(Color.LIGHTGRAY);
                        affichageCase = vide;
                    }
                } else {
                    // SI NON VISIBLE : On affiche une case "Brouillard" (Gris foncé)
                    Rectangle brouillard = new Rectangle(TAILLE_CASE, TAILLE_CASE);
                    brouillard.setFill(Color.DARKGRAY);
                    brouillard.setStroke(Color.web("#333333")); // Contour encore plus sombre
                    affichageCase = brouillard;
                }

                // Gestion du clic (reste identique)
                int finalX = x; int finalY = y;
                affichageCase.setOnMouseClicked(e -> {
                    Case dest = grilleJeu.getCases(finalX, finalY);
                    if (!gameOver && bonhomme.getEnergie() <= 15) {
                        if (bonhomme.estAdjacent(bonhomme.getCaseActuelle(), dest)) {
                            faireUnTourDeJeu(dest);
                        }
                    }
                });


                grilleVisuelle.add(affichageCase, x, y);
            }
        }
    }
    
    private void preparerModeAuto() {
        // On crée une boucle qui s'exécute toutes les 0.5 secondes
    	if (timelineAuto != null) {
            timelineAuto.stop();
        }
        timelineAuto = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(2.5), e -> {
                if (!gameOver && bonhomme.getEnergie() > 15) {
                    faireUnTourDeJeu(null); // null car c'est l'IA qui choisit
                } else {
                    timelineAuto.stop(); // On arrête si on repasse en manuel ou si c'est fini
                }
            })
        );
        timelineAuto.setCycleCount(javafx.animation.Timeline.INDEFINITE);
        
        if (bonhomme.getEnergie() > 15) {
            timelineAuto.play();
        }
    }

    private void faireUnTourDeJeu(Case destinationManuelle) {
        if (gameOver) return;

        compteurCoups++;
        
        // 1. Tour du Bonhomme
        if (bonhomme.getEnergie() > 15) {
            bonhomme.seDeplacer(grilleJeu); // Utilise la stratégie
        } else if (destinationManuelle != null) {
            // Logique manuelle que tu avais
            bonhomme.setCaseActuelle(destinationManuelle);
            destinationManuelle.appliquerEffet(bonhomme);
            destinationManuelle.setItem(null);
            bonhomme.setEnergie(bonhomme.getEnergie() - 1);
            if (!bonhomme.getPapier().contains(destinationManuelle)) 
            	bonhomme.getPapier().add(destinationManuelle);
        	}
        	actualiser();

        // 2. Tour du Chasseur
        if (chasseur.estVivant() && !gameOver) {
            chasseur.seDeplacer(grilleJeu);
        }

        
        verifierFinJeu();
        

        // 4. Si on vient de passer en auto via un fruit, on lance la timeline
        if (bonhomme.getEnergie() > 15 && timelineAuto.getStatus() != javafx.animation.Animation.Status.RUNNING) {
            timelineAuto.play();
        }
    }


    private Node chargerImage(String nomFichier) {
        try {
            Image img = new Image("file:src/images/" + nomFichier);
            ImageView iv = new ImageView(img);
            iv.setFitWidth(TAILLE_CASE);
            iv.setFitHeight(TAILLE_CASE);
            return iv;
        } catch (Exception e) {
            Rectangle r = new Rectangle(TAILLE_CASE, TAILLE_CASE);
            r.setFill(Color.MAGENTA);
            return r;
        }
    }

    private void verifierFinJeu() {
        String message = "";
        boolean fini = false;

        // 1. VICTOIRE DU BONHOMME
        if (bonhomme.getCaseActuelle() == grilleJeu.getCible()) {
            message = "Félicitations " + bonhomme.getNom() + " ! Vous avez atteint la cible !";
            fini = true;
        } 
        // 2. LE CHASSEUR ATTEINT LA CIBLE (DÉFAITE)
        else if (chasseur.getCaseActuelle() == grilleJeu.getCible()) {
            message = "Dommage... Le Chasseur a atteint la cible avant vous !";
            fini = true;
        }
        // 3. LE CHASSEUR ATTRAPE LE BONHOMME (DÉFAITE)
        else if (chasseur.getCaseActuelle() == bonhomme.getCaseActuelle()) {
            message = "Aïe ! Le Chasseur vous a attrapé !";
            fini = true;
        }
        // 4. PLUS D'ÉNERGIE
        else if (bonhomme.getEnergie() <= 0) {
            message = "GAME OVER : Vous n'avez plus d'énergie pour avancer.";
            fini = true;
        }

        if (fini && !gameOver) {
            gameOver = true;
            if (timelineAuto != null) timelineAuto.stop();
            
            long dureeSecondes = (System.currentTimeMillis() - debutPartie) / 1000;
            boolean victoireBonhomme = (bonhomme.getCaseActuelle() == grilleJeu.getCible());
            stats.statistiquePartie(compteurCoups, victoireBonhomme, dureeSecondes);

            final String messageFinal = message;

            	javafx.application.Platform.runLater(() -> {

                javafx.scene.control.Alert alert =
                    new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);

                alert.setTitle("Fin de la partie");
                alert.setHeaderText(null);
                alert.setContentText(messageFinal);

                // Action exécutée lorsque l'alerte est fermée
                alert.setOnHidden(ev -> {

                    javafx.application.Platform.runLater(() -> {

                        // Création des boutons
                        javafx.scene.control.Button boutonQuitter = new javafx.scene.control.Button("✖");
                        boutonQuitter.setStyle(
                        	"-fx-font-size: 18px;" +
                        	"-fx-background-color: transparent;" +
                        	"-fx-text-fill: red;"
                        );
                        boutonQuitter.setOnAction(e -> {
                            javafx.stage.Stage stage =
                                (javafx.stage.Stage) boutonQuitter.getScene().getWindow();
                            stage.close();
                        });

                        javafx.scene.control.Button btnStats = new javafx.scene.control.Button("Voir Statistiques");
                        btnStats.setStyle(
                        	"-fx-background-color: #4CAF50; " +
                        	"-fx-text-fill: white; " +
                        	"-fx-font-weight: bold;"
                        );
                        btnStats.setOnAction(e -> afficherFenetreStats());
                        
                        javafx.scene.control.Button btnRejouer = new javafx.scene.control.Button("Rejouer");
                        btnRejouer.setStyle(
                            "-fx-background-color: #3498db; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold;"
                        );
                        btnRejouer.setOnAction(e -> initialiserNouvellePartie());
                        
                        javafx.scene.control.Button btnClassement = new javafx.scene.control.Button("Classement");
                        btnClassement.setStyle(
                            "-fx-background-color: #9b59b6; " + // Couleur violette
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold;"
                        );
                        btnClassement.setOnAction(e -> afficherClassement());

                        // Assemblage de la barre de boutons
                        HBox barreBoutonsFin = new HBox(15, btnRejouer, btnStats, btnClassement, boutonQuitter);
                        barreBoutonsFin.setAlignment(Pos.TOP_RIGHT);
                        barreBoutonsFin.setPadding(new Insets(5));

                        VBox nouveauTop = new VBox(barreBoutonsFin, topBar);
                        root.setTop(nouveauTop);
                        
                        GestionnaireDonnees.sauvegarder(dictionnaireJoueurs);
                    });
                });
                alert.show();
            });
        }
    }

    private void afficherFenetreStats() {
        Stage stageStats = new Stage();
        stageStats.setTitle("Statistiques Globales");

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER_LEFT);

        // Titre
        Label titre = new Label("--- TABLEAU DES SCORES ---");
        titre.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        
        // Labels des statistiques
        Label l1 = new Label();
        Label l2 = new Label();
        Label l3 = new Label();
        Label l4 = new Label();

        // Fonction interne pour mettre à jour le texte des labels
        // On l'appelle à l'ouverture et après une réinitialisation
        Runnable mettreAJourLabels = () -> {
            long totalSec = stats.getTempsJouer();
            String tempsFormate = String.format("%02d:%02d:%02d", 
                                  totalSec / 3600, (totalSec % 3600) / 60, totalSec % 60);
            
            l1.setText("Nombre de parties : " + stats.getNombrePartie());
            l2.setText("Taux de victoire : " + String.format("%.1f", stats.getTauxVictoire()) + "%");
            l3.setText("Meilleur score (coups) : " + (stats.getBestScore() == Integer.MAX_VALUE ? "N/A" : stats.getBestScore()));
            l4.setText("Temps total de jeu : " + tempsFormate);
        };

        // Initialisation du texte
        mettreAJourLabels.run();

        // Bouton de réinitialisation
        javafx.scene.control.Button btnReset = new javafx.scene.control.Button("Réinitialisation");
        btnReset.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;"); // Rouge pour la suppression
        btnReset.setOnAction(e -> {
            stats.resetStats();
            mettreAJourLabels.run(); // On rafraîchit l'affichage immédiatement
        });

        // Bouton Fermer
        javafx.scene.control.Button btnFermer = new javafx.scene.control.Button("Retour au jeu");
        btnFermer.setOnAction(e -> stageStats.close());

        // Ajout des éléments au layout (HBox pour mettre les boutons côte à côte)
        HBox boxBoutons = new HBox(10, btnReset, btnFermer);
        boxBoutons.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(titre, l1, l2, l3, l4, boxBoutons);

        Scene scene = new Scene(layout, 350, 300);
        stageStats.setScene(scene);
        stageStats.show();
    }
    
    private void afficherClassement() {
        Stage stageRank = new Stage();
        stageRank.setTitle("Top 5 - Meilleurs Scores");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Label titre = new Label("CLASSEMENT GÉNÉRAL");
        titre.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2c3e50;");

        GridPane tableau = new GridPane();
        tableau.setHgap(20);
        tableau.setVgap(10);
        tableau.setAlignment(Pos.CENTER);

        // En-têtes
        tableau.add(new Label("Rang"), 0, 0);
        tableau.add(new Label("Joueur"), 1, 0);
        tableau.add(new Label("Coups"), 2, 0);

        // Tri des joueurs :
        // 1. On transforme la Map en liste d'entrées
        // 2. On filtre ceux qui ont un score valide (différent de MAX_VALUE)
        // 3. On trie par score ascendant
        // 4. On prend les 5 premiers
        var top5 = dictionnaireJoueurs.entrySet().stream()
                .filter(e -> e.getValue().getBestScore() < Integer.MAX_VALUE)
                .sorted((e1, e2) -> Integer.compare(e1.getValue().getBestScore(), e2.getValue().getBestScore()))
                .limit(5)
                .toList();

        int ligne = 1;
        for (var entry : top5) {
            String nom = entry.getKey();
            int score = entry.getValue().getBestScore();

            Label lblRang = new Label("#" + ligne);
            Label lblNom = new Label(nom);
            Label lblScore = new Label(String.valueOf(score));

            // Style pour le top 1
            if(ligne == 1) {
                lblNom.setStyle("-fx-text-fill: #f1c40f; -fx-font-weight: bold;");
            }

            tableau.add(lblRang, 0, ligne);
            tableau.add(lblNom, 1, ligne);
            tableau.add(lblScore, 2, ligne);
            ligne++;
        }

        if (top5.isEmpty()) {
            layout.getChildren().add(new Label("Aucun score enregistré pour le moment."));
        }

        javafx.scene.control.Button btnFermer = new javafx.scene.control.Button("Fermer");
        btnFermer.setOnAction(e -> stageRank.close());

        layout.getChildren().addAll(titre, tableau, btnFermer);
        
        Scene scene = new Scene(layout, 300, 400);
        stageRank.setScene(scene);
        stageRank.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}