# Simulation Chasseur-Proie : Architecture Orientée Objet & Design Patterns

Ce projet Java est une simulation dynamique mettant en scène un **Chasseur** et un **Bonhomme** (proie). L'objectif est de démontrer l'application de principes d'ingénierie logicielle avancés pour créer un système flexible, robuste et évolutif.

## Support de Présentation
**[Lien vers la présentation Canva](https://www.canva.com/design/DAHC5VC0kTI/taXxW9ZdCwPgsD7WEmsjDQ/edit?utm_content=DAHC5VC0kTI&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton)**

## Structure du projet
- Projet_Minimal : logique du jeu
- Projet_Interface : interface graphique

## Architecture & Design Patterns

Pour ce projet, nous avons dépassé la simple programmation procédurale en implémentant plusieurs patterns structurels et comportementaux :

### 1. Pattern Adaptateur (Adapter)
Ce pattern a été utilisé pour uniformiser la gestion des éléments interactifs de la carte. Nous avons créé un adaptateur pour transformer des entités hétérogènes (**Fruits**, **Feu**, **Prédateurs**) en un type unique : **Item**.
* **Fonctionnement :** L'adaptateur permet au moteur de jeu de manipuler chaque élément (qu'il donne de l'énergie comme un fruit ou qu'il en retire comme le feu) via une interface commune sans modifier leur code source respectif.
  
### 2. Pattern Stratégie (Strategy)
Nous avons appliqué ce pattern pour gérer l'autonomie du **Bonhomme** (la proie). Son intelligence de déplacement est encapsulée dans une stratégie évolutive :
* **Stratégie Exploratoire :** Le Bonhomme analyse son propre état interne pour décider de son mouvement. 
* **Variable "High Value" :** Lorsque son énergie est supérieure à 15, le Bonhomme passe en mode "haute intensité" et avance automatiquement de manière proactive.
* *Avantage :* Cela permet de simuler un comportement de fuite intelligent et autonome qui s'adapte en temps réel aux ressources restantes de l'entité.
  
### 3. Pattern Observateur (Observer)
Le moteur graphique (la Vue) "écoute" les entités (le Modèle). Chaque changement de position ou de niveau d'énergie déclenche une notification automatique qui actualise l'interface en temps réel.

## Fonctionnalités du Jeu
* **Gestion d'Énergie :** Le chasseur consomme des ressources à chaque pas. Une barre d'énergie (ProgressBar) suit son état.
* **Mode Automatique :** Simulation gérée par une `Timeline` JavaFX avec des tours de jeu cadencés.
* **Logs Temps Réel :** Sortie console détaillée affichant les coordonnées `(x, y)` et les statistiques d'énergie à chaque mouvement.
* **Correction Graphique :** Intégration d'un correctif pour le pipeline Direct3D afin d'assurer la stabilité sur tous les environnements Windows.

## Installation et Exécution

### Prérequis
* **JDK 17** ou version supérieure.
* **JavaFX SDK** configuré dans votre IDE.

### Lancement
1. Clonez le repository.
2. **Important :** Pour éviter l'erreur `NullPointerException` liée au rendu graphique (D3D), configurez vos **VM Options** comme suit :
   ```text
   -Dprism.order=sw

## Auteurs
Anna Szwarcbart
Antonin Biette
   
