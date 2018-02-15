import java.util.Arrays;
import javax.swing.JOptionPane;

public class Grille {

	protected enum Diff {
		F, M, D;
	}

	protected final Diff DIFFICULTE;

	protected final int TAILLEL;
	protected final int TAILLEH;

	protected int[][] nbCotesPleins;
	protected boolean[][] coteBEstPlein;
	protected boolean[][] coteDEstPlein;
	protected boolean[][] cheminEstFaux;
	protected boolean[][] celluleEstFausse;

	protected final boolean[][] SOLUTION_COTEB_EST_PLEIN;
	protected final boolean[][] SOLUTION_COTED_EST_PLEIN;

	/*
	 * Constructeurs
	 */
	public Grille(int tL, int tH, int[][] nbCP, boolean[][] cBEP,
			boolean[][] cDEP, boolean[][] sCBEP, boolean[][] sCDEP, Diff d) {
		TAILLEL = tL;
		TAILLEH = tH;
		nbCotesPleins = nbCP;
		coteBEstPlein = cBEP;
		coteDEstPlein = cDEP;
		cheminEstFaux = new boolean[TAILLEL + 1][TAILLEH + 1];
		celluleEstFausse = new boolean[TAILLEL + 1][TAILLEH + 1];
		SOLUTION_COTEB_EST_PLEIN = sCBEP;
		SOLUTION_COTED_EST_PLEIN = sCDEP;
		DIFFICULTE = d;
	}

	public Grille(int tL, int tH, int[][] nbCP, boolean[][] cBEP,
			boolean[][] cDEP, boolean[][] sCBEP, boolean[][] sCDEP) {
		this(tL, tH, nbCP, cBEP, cDEP, sCBEP, sCDEP, null);
	}

	public Grille(int tL, int tH, int[][] nbCP) {
		this(tL, tH, nbCP, new boolean[tL + 1][tH + 1],
				new boolean[tL + 1][tH + 1], new boolean[tL + 1][tH + 1],
				new boolean[tL + 1][tH + 1], null);
	}

	public Grille(int tL, int tH, int[][] nbCP, boolean[][] sCBEP,
			boolean[][] sCDEP) {
		this(tL, tH, nbCP, new boolean[tL + 1][tH + 1],
				new boolean[tL + 1][tH + 1], sCBEP, sCDEP, null);
	}

	public Grille(int tL, int tH) {
		this(tL, tH, new int[tL + 1][tH + 1], new boolean[tL + 1][tH + 1],
				new boolean[tL + 1][tH + 1], new boolean[tL + 1][tH + 1],
				new boolean[tL + 1][tH + 1], null);
		int[][] grilleEntiers = new int[tL + 1][tH + 1];
		for (int i = 1; i <= tL; i++) {
			Arrays.fill(grilleEntiers[i], 9);
		}
		nbCotesPleins = grilleEntiers;
	}

	/**
	 * Vérifie que le chemin proposé par l'utilisateur est cohérent,
	 * c'est-à-dire qu'il n'y a pas de croisement et que c'est bien une boucle.
	 * Cela revient en fait à vérifier que pour chaque noeud, il y a exactement
	 * zéro ou deux attributs parmi les cotés haut, bas, gauche, droit marqués
	 * ``true''. Si une erreur est détectée, on met en évidence le noeud posant
	 * problème (via le tableau cheminEstFaux). Si les chemins sont fermés sur
	 * eux-mêmes, on vérifie s'il n'y a qu'un seul chemin. Pour cela, on choisit
	 * un point de départ puis on suit le chemin qui passe par ce point jusqu'à
	 * revenir au point de départ. Par la suite, on vérifie qu'il n'y a aucun
	 * autre tracé enregistré.
	 * 
	 * @return Chemin faux ou non
	 */
	protected boolean CheminEstFaux() {
		boolean problemeDetecte = false;
		int compteur = 0;
		// Vérification intérieur de la grille sans côtés droit et bas
		for (int i = 0; i < TAILLEL; i++) {
			for (int j = 0; j < TAILLEH; j++) {
				compteur = 0;
				if (coteBEstPlein[i][j])
					compteur++;
				if (coteDEstPlein[i][j])
					compteur++;
				if (coteBEstPlein[i + 1][j])
					compteur++;
				if (coteDEstPlein[i][j + 1])
					compteur++;
				if (compteur != 2 && compteur != 0) {
					cheminEstFaux[i][j] = true;
					problemeDetecte = true;
				} else {
					cheminEstFaux[i][j] = false;
				}
			}
		}
		// Vérification bord droit
		for (int j = 0; j < TAILLEH; j++) {
			compteur = 0;
			if (coteBEstPlein[TAILLEL][j])
				compteur++;
			if (coteDEstPlein[TAILLEL][j])
				compteur++;
			if (coteDEstPlein[TAILLEL][j + 1])
				compteur++;
			if (compteur != 2 && compteur != 0) {
				cheminEstFaux[TAILLEL][j] = true;
				problemeDetecte = true;
			} else {
				cheminEstFaux[TAILLEL][j] = false;
			}
		}
		// Vérification bord bas
		for (int i = 0; i < TAILLEL; i++) {
			compteur = 0;
			if (coteBEstPlein[i][TAILLEH])
				compteur++;
			if (coteDEstPlein[i][TAILLEH])
				compteur++;
			if (coteBEstPlein[i + 1][TAILLEH])
				compteur++;
			if (compteur != 2 && compteur != 0) {
				cheminEstFaux[i][TAILLEH] = true;
				problemeDetecte = true;
			} else {
				cheminEstFaux[i][TAILLEH] = false;
			}
		}
		// Coin inférieur droit
		if (!((coteBEstPlein[TAILLEL][TAILLEH] && coteDEstPlein[TAILLEL][TAILLEH]) || (!coteBEstPlein[TAILLEL][TAILLEH] && !coteDEstPlein[TAILLEL][TAILLEH]))) {
			cheminEstFaux[TAILLEL][TAILLEH] = true;
			problemeDetecte = true;
		} else {
			cheminEstFaux[TAILLEL][TAILLEH] = false;
		}

		if (problemeDetecte)
			return true;

		// On va vérifier qu'il n'y a qu'un seul chemin tracé
		boolean[][] premierCheminCoteB = new boolean[TAILLEL + 1][TAILLEH + 1];
		boolean[][] premierCheminCoteD = new boolean[TAILLEL + 1][TAILLEH + 1];
		int pointDeparti = -1;
		int pointDepartj = -1;
		int pointCouranti = -1;
		int pointCourantj = -1;
		Action actionRealisee = null;
		// Etape 1 : repérer un point où commencer
		int i = 0, j = 0;
		for (j = 0; j <= TAILLEH; j++) {
			for (i = 0; i <= TAILLEL; i++) {
				if (coteBEstPlein[i][j] || coteDEstPlein[i][j]) {
					if (coteBEstPlein[i][j]) {
						pointDeparti = i - 1;
						pointDepartj = j;
						pointCouranti = i;
						pointCourantj = j;
						actionRealisee = new Action(i - 1, j,
								Action.Direction.D);
						premierCheminCoteB[i][j] = true;
					} else {
						pointDeparti = i;
						pointDepartj = j - 1;
						pointCouranti = i;
						pointCourantj = j;
						actionRealisee = new Action(i, j - 1,
								Action.Direction.B);
						premierCheminCoteD[i][j] = true;
					}
				}
				if (!(actionRealisee == null))
					break;
			}
			if (!(actionRealisee == null))
				break;
		}

		if (!(pointDeparti == -1)) {
			// Etape 2 : suivre un chemin
			while (pointCouranti != pointDeparti
					|| pointCourantj != pointDepartj) {
				switch (actionRealisee.d) {
				case D:
					if (coteDEstPlein[pointCouranti][pointCourantj]) {
						actionRealisee = new Action(pointCouranti,
								pointCourantj, Action.Direction.H);
						premierCheminCoteD[pointCouranti][pointCourantj] = true;
						pointCourantj = pointCourantj - 1;
					} else if (pointCouranti < TAILLEL
							&& coteBEstPlein[pointCouranti + 1][pointCourantj]) {
						actionRealisee = new Action(pointCouranti,
								pointCourantj, Action.Direction.D);
						premierCheminCoteB[pointCouranti + 1][pointCourantj] = true;
						pointCouranti = pointCouranti + 1;
					} else if (pointCourantj < TAILLEH
							&& coteDEstPlein[pointCouranti][pointCourantj + 1]) {
						actionRealisee = new Action(pointCouranti,
								pointCourantj, Action.Direction.B);
						premierCheminCoteD[pointCouranti][pointCourantj + 1] = true;
						pointCourantj = pointCourantj + 1;
					}
					break;
				case H:
					if (coteDEstPlein[pointCouranti][pointCourantj]) {
						actionRealisee = new Action(pointCouranti,
								pointCourantj, Action.Direction.H);
						premierCheminCoteD[pointCouranti][pointCourantj] = true;
						pointCourantj = pointCourantj - 1;
					} else if (coteBEstPlein[pointCouranti][pointCourantj]) {
						actionRealisee = new Action(pointCouranti,
								pointCourantj, Action.Direction.G);
						premierCheminCoteB[pointCouranti][pointCourantj] = true;
						pointCouranti = pointCouranti - 1;
					} else if (pointCourantj < TAILLEL
							&& coteBEstPlein[pointCouranti + 1][pointCourantj]) {
						actionRealisee = new Action(pointCouranti,
								pointCourantj, Action.Direction.D);
						premierCheminCoteB[pointCouranti + 1][pointCourantj] = true;
						pointCouranti = pointCouranti + 1;
					}
					break;
				case B:
					if (pointCourantj < TAILLEH
							&& coteDEstPlein[pointCouranti][pointCourantj + 1]) {
						actionRealisee = new Action(pointCouranti,
								pointCourantj, Action.Direction.B);
						premierCheminCoteD[pointCouranti][pointCourantj + 1] = true;
						pointCourantj = pointCourantj + 1;
					} else if (coteBEstPlein[pointCouranti][pointCourantj]) {
						actionRealisee = new Action(pointCouranti,
								pointCourantj, Action.Direction.G);
						premierCheminCoteB[pointCouranti][pointCourantj] = true;
						pointCouranti = pointCouranti - 1;
					} else if (pointCouranti < TAILLEL
							&& coteBEstPlein[pointCouranti + 1][pointCourantj]) {
						actionRealisee = new Action(pointCouranti,
								pointCourantj, Action.Direction.D);
						premierCheminCoteB[pointCouranti + 1][pointCourantj] = true;
						pointCouranti = pointCouranti + 1;
					}
					break;
				case G:
					if (coteBEstPlein[pointCouranti][pointCourantj]) {
						actionRealisee = new Action(pointCouranti,
								pointCourantj, Action.Direction.G);
						premierCheminCoteB[pointCouranti][pointCourantj] = true;
						pointCouranti = pointCouranti - 1;
					} else if (coteDEstPlein[pointCouranti][pointCourantj]) {
						actionRealisee = new Action(pointCouranti,
								pointCourantj, Action.Direction.H);
						premierCheminCoteD[pointCouranti][pointCourantj] = true;
						pointCourantj = pointCourantj - 1;
					} else if (pointCourantj < TAILLEH
							&& coteDEstPlein[pointCouranti][pointCourantj + 1]) {
						actionRealisee = new Action(pointCouranti,
								pointCourantj, Action.Direction.B);
						premierCheminCoteD[pointCouranti][pointCourantj + 1] = true;
						pointCourantj = pointCourantj + 1;
					}
					break;
				default:
					// Rien
				}
			}

			// Etape 3 : revue des points marqués true
			boolean erreurDetectee = false;
			for (i = 0; i <= TAILLEL; i++) {
				for (j = 0; j <= TAILLEH; j++) {
					if (coteBEstPlein[i][j] != premierCheminCoteB[i][j]) {
						erreurDetectee = true;
					}
					if (coteDEstPlein[i][j] != premierCheminCoteD[i][j]) {
						erreurDetectee = true;
					}
				}
				if (erreurDetectee) {
					JOptionPane.showMessageDialog(null,
							"Il y a plusieurs boucles dans le chemin !");
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Vérifie que le chemin proposé par l'utilisateur vérifie bien la
	 * contrainte du nombre de côtés adjacents aux cases contenant un chiffre
	 * différent de 9. Pour cela, on prend les cellules une par une. Si elle
	 * contient un chiffre différent de 9, on compte les côtés pleins (via la
	 * méthode RenvoyerCotes. Si une erreur est détectée, on met en évidence la
	 * cellule posant problème (via l'attribut celluleEstFausse).
	 * 
	 * @return Côtés faux ou non
	 */
	protected boolean CotesSontFaux() {
		boolean erreurDetectee = false;
		for (int i = 1; i <= TAILLEL; i++) {
			for (int j = 1; j <= TAILLEH; j++) {
				if (nbCotesPleins[i][j] != 9) {
					if (RenvoyerCotes(i, j) != nbCotesPleins[i][j]) {
						celluleEstFausse[i][j] = true;
						erreurDetectee = true;
					} else {
						celluleEstFausse[i][j] = false;
					}
				} else {
					celluleEstFausse[i][j] = false;
				}
			}
		}
		return erreurDetectee;
	}

	/**
	 * Vide les tableaux relatifs aux vérifications des côtés et des chiffres,
	 * pour que la grille soit dessinée "normalement".
	 */
	protected void EffacerVerifier() {
		for (int i = 1; i <= TAILLEL; i++) {
			for (int j = 1; j <= TAILLEH; j++) {
				celluleEstFausse[i][j] = false;
				cheminEstFaux[i][j] = false;
			}
		}
	}

	/**
	 * Change l'état du côté droit de la case concernée i,j
	 * 
	 * @param i Ligne
	 * @param j Colonne
	 */
	protected void ChangeCoteD(int i, int j) {
		if (coteDEstPlein[i][j]) {
			coteDEstPlein[i][j] = false;
		} else {
			coteDEstPlein[i][j] = true;
		}
	}

	/**
	 * Change l'état du côté bas de la case concernée i,j
	 * 
	 * @param i Ligne
	 * @param j Colonne
	 */
	protected void ChangeCoteB(int i, int j) {
		if (coteBEstPlein[i][j]) {
			coteBEstPlein[i][j] = false;
		} else {
			coteBEstPlein[i][j] = true;
		}
	}

	/**
	 * Renvoie le nombre de côtés pleins bordant la case i,j de la grille
	 * 
	 * @param i Ligne
	 * @param j Colonne
	 * @return Nombre de côtés pleins
	 */
	protected int RenvoyerCotes(int i, int j) {
		int compteur = 0;
		if (coteBEstPlein[i][j])
			compteur++;
		if (coteDEstPlein[i][j])
			compteur++;
		if (coteBEstPlein[i][j - 1])
			compteur++;
		if (coteDEstPlein[i - 1][j])
			compteur++;
		return compteur;
	}

	/**
	 * Rafraîchit le nombre de côtés pleins de toutes les cases de la grille
	 */
	protected void RafraichirCotes() {
		int i, j;
		for (i = 1; i <= TAILLEL; i++) {
			for (j = 1; j <= TAILLEH; j++) {
				if (nbCotesPleins[i][j] != 9) {
					nbCotesPleins[i][j] = RenvoyerCotes(i, j);
				}
			}
		}
	}

	/**
	 * Détermine aléatoirement (probabilité d) si la chaque case du tableau
	 * donnera une restriction au chemin de l'utilisateur
	 * 
	 * @param d Proportion de cases remplies
	 */
	protected void RemplirNbCotesAlea(double d) {
		int i, j;
		for (i = 1; i <= TAILLEL; i++) {
			for (j = 1; j <= TAILLEH; j++) {
				if (Math.random() < d) {
					nbCotesPleins[i][j] = RenvoyerCotes(i, j);
				} else {
					nbCotesPleins[i][j] = 9;
				}
			}
		}
	}

	/**
	 * Bascule le chemin solution enregistré vers le chemin tracé par
	 * l'utilisateur.
	 */
	protected void TracerCheminSolution() {
		int i, j;
		for (i = 0; i <= TAILLEL; i++) {
			for (j = 0; j <= TAILLEH; j++) {
				coteBEstPlein[i][j] = SOLUTION_COTEB_EST_PLEIN[i][j];
				coteDEstPlein[i][j] = SOLUTION_COTED_EST_PLEIN[i][j];
			}
		}
		RafraichirCotes();
	}

	/**
	 * Remet à zéro l'état des côtés des cases
	 */
	protected void ResetCotesPleins() {
		coteDEstPlein = new boolean[TAILLEL + 1][TAILLEH + 1];
		coteBEstPlein = new boolean[TAILLEL + 1][TAILLEH + 1];
	}

}
