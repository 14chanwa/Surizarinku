# Surizarinku


Le Surizarinku (Slither Link) est un jeu dont le principe est de dessiner sur une grille un chemin continu et fermé passant par les arêtes et vérifiant certaines contraintes : le numéro inscrit dans chaque case (0, 1, 2, 3) est le nombre d'arêtes pleines adjacentes. Selon le nombre d'indications, la solution peut être unique ou pas... mais il y a au moins une solution !


Ce programme a été développé dans le cadre d'un projet d'informatique pendant mon cursus ingénieur. Voir 'Documentation.pdf' pour les détails de l'implémentation.


### Mode Jeu


Le joueur commence en haut à gauche et se déplace (en remplissant les arêtes sur son chemin) en haut, bas, droite, gauche avec les touches Z, S, D, Q. Le joueur peut utiliser un outil lui permettant de vérifier la justesse de sa solution, ou afficher une solution préalablement stockée.


<p align="center">
<img src="https://raw.githubusercontent.com/14chanwa/Surizarinku/master/wiki_resources/screen001.PNG" width="500">
</p>
<p align="center"><em>Mode jeu</em></p>


<p align="center">
<img src="https://raw.githubusercontent.com/14chanwa/Surizarinku/master/wiki_resources/screen003.PNG" width="500">
</p>
<p align="center"><em>Vérification de la solution proposée par le joueur. On voit les contraintes non remplies.</em></p>


<p align="center">
<img src="https://raw.githubusercontent.com/14chanwa/Surizarinku/master/wiki_resources/screen004.PNG" width="500">
</p>
<p align="center"><em>Affichage d'une solution possible en surimpression.</em></p>


Les fichiers de jeu sont stockés sous un certain format dans un fichier .txt situé dans le répertoire de lancement du programme (ou le même répertoire que le '.jar'). Ils doivent être répertoriés dans le fichier 'LISTEGRILLES.txt'.


### Mode Création


Le programme propose un mode dans lequel l'utilisateur dessine lui-même son chemin continu sur la grille, et décide des indications données au joueur (le programme peut aussi générer un set d'indications selon une proportion de cases remplies). L'utilisateur peut ensuite sauvegarder sa grille pour y jouer plus tard !


<p align="center">
<img src="https://raw.githubusercontent.com/14chanwa/Surizarinku/master/wiki_resources/screen002.PNG" width="500">
</p>
<p align="center"><em>Mode création</em></p>