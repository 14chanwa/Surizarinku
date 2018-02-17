# Surizarinku


Le Surizarinku (Slither Link) est un jeu dont le principe est de dessiner sur une grille un chemin continu et ferm� passant par les ar�tes et v�rifiant certaines contraintes : le num�ro inscrit dans chaque case (0, 1, 2, 3) est le nombre d'ar�tes pleines adjacentes. Selon le nombre d'indications, la solution peut �tre unique ou pas... mais il y a au moins une solution !


Ce programme a �t� d�velopp� dans le cadre d'un projet d'informatique pendant mon cursus ing�nieur. Voir 'Documentation.pdf' pour les d�tails de l'impl�mentation.


### Mode Jeu


Le joueur commence en haut � gauche et se d�place (en remplissant les ar�tes sur son chemin) en haut, bas, droite, gauche avec les touches Z, S, D, Q. Le joueur peut utiliser un outil lui permettant de v�rifier la justesse de sa solution, ou afficher une solution pr�alablement stock�e.


<p align="center">
<img src="https://raw.githubusercontent.com/14chanwa/Surizarinku/master/wiki_resources/screen001.PNG" width="500">
</p>
<p align="center"><em>Mode jeu</em></p>


<p align="center">
<img src="https://raw.githubusercontent.com/14chanwa/Surizarinku/master/wiki_resources/screen003.PNG" width="500">
</p>
<p align="center"><em>V�rification de la solution propos�e par le joueur. On voit les contraintes non remplies.</em></p>


<p align="center">
<img src="https://raw.githubusercontent.com/14chanwa/Surizarinku/master/wiki_resources/screen004.PNG" width="500">
</p>
<p align="center"><em>Affichage d'une solution possible en surimpression.</em></p>


Les fichiers de jeu sont stock�s sous un certain format dans un fichier .txt situ� dans le r�pertoire de lancement du programme (ou le m�me r�pertoire que le '.jar'). Ils doivent �tre r�pertori�s dans le fichier 'LISTEGRILLES.txt'.


### Mode Cr�ation


Le programme propose un mode dans lequel l'utilisateur dessine lui-m�me son chemin continu sur la grille, et d�cide des indications donn�es au joueur (le programme peut aussi g�n�rer un set d'indications selon une proportion de cases remplies). L'utilisateur peut ensuite sauvegarder sa grille pour y jouer plus tard !


<p align="center">
<img src="https://raw.githubusercontent.com/14chanwa/Surizarinku/master/wiki_resources/screen002.PNG" width="500">
</p>
<p align="center"><em>Mode cr�ation</em></p>