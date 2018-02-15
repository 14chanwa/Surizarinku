import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.*;
import java.util.Stack;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Session extends JPanel implements ActionListener {

	/*
	 * CARACT TERRAIN DE JEU
	 */
	Grille grille;

	/** Indicateur Solution Active */
	protected boolean solutionEstActive = false;

	// Coordonnées du point actif
	protected int pointActifX = 0;
	protected int pointActifY = 0;
	protected int keyEventNum = 0;

	// Caractéristiques graphiques
	protected final int MARGE = 50;
	
	/** Police de caractères utilisée pour les chiffres de la grille */
	private Font stringFont = new Font("SansSerif", Font.PLAIN, 20);
	
	private final int LARGEUR_JEU = 10;
	private final int TAILLE_ZONE_JEU = 40 * LARGEUR_JEU + 2 * MARGE;
	
	private final Color COULEUR_CHIFFRE = Color.BLACK;
	private final Color COULEUR_CHIFFRE_FAUX = Color.ORANGE;
	private final Color COULEUR_CHEMIN = Color.BLUE;
	private final Color COULEUR_CHEMIN_SOLUTION_TROUVE = Color.CYAN; // Vert ?
	private final Color COULEUR_CHEMIN_SOLUTION_NON_TROUVE = Color.MAGENTA;
	private final Color COULEUR_NOEUD_FAUX = Color.ORANGE;
	private final Color COULEUR_POINT = Color.RED;
	private final Stroke STROKE_CHEMIN = new BasicStroke(8);

	// Piles
	protected Stack<Action> pileActEffectuees;
	protected Stack<Action> pileActAnnulees;

	/*
	 * Constructeurs
	 */
	public Session(Grille j) {
		grille = j;
		setSize(new Dimension(TAILLE_ZONE_JEU, TAILLE_ZONE_JEU));
		pileActEffectuees = new Stack<Action>();
		pileActAnnulees = new Stack<Action>();
	}

	
	public Session(int lar, int hau) {
		this(new Grille(lar, hau));
	}

	// Dessin
	public void paint(Graphics g) {
		Dimension dimCanvas = getSize();
		// On peint le fond en blanc
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, dimCanvas.width, dimCanvas.height);
		
		// Grille de nombres
		g.setColor(COULEUR_CHIFFRE);
		g.setFont(stringFont);
		for (int i = 1; i <= RenvoyerLargeur(); i++) {
			for (int j = 1; j <= RenvoyerHauteur(); j++) {
				g.drawRect(MARGE + (i - 1) * 40, MARGE + (j - 1) * 40, 40, 40);
				if (grille.nbCotesPleins[i][j] != 9) {
					if (grille.celluleEstFausse[i][j]) {
						g.setColor(COULEUR_CHIFFRE_FAUX);
						g.drawString("" + grille.nbCotesPleins[i][j], MARGE
								+ (i - 1) * 40 + 15, MARGE + (j - 1) * 40 + 30);
						g.setColor(COULEUR_CHIFFRE);
					} else {
						g.drawString("" + grille.nbCotesPleins[i][j], MARGE
								+ (i - 1) * 40 + 15, MARGE + (j - 1) * 40 + 30);
					}
				}

			}
		}

		
		Graphics2D g2 = (Graphics2D) g;
		
		// Coloriage de différentes couleurs pour la solution
		g2.setStroke(STROKE_CHEMIN);
		if (solutionEstActive) {
			for (int i = 0; i <= RenvoyerLargeur(); i++) {
				for (int j = 0; j <= RenvoyerHauteur(); j++) {
					if (grille.coteBEstPlein[i][j]
							&& grille.SOLUTION_COTEB_EST_PLEIN[i][j]) {
						g2.setPaint(COULEUR_CHEMIN_SOLUTION_TROUVE);
						g.drawLine(MARGE + (i - 1) * 40, MARGE + j * 40, MARGE
								+ i * 40, MARGE + j * 40);
					} else if (!grille.coteBEstPlein[i][j]
							&& grille.SOLUTION_COTEB_EST_PLEIN[i][j]) {
						g2.setPaint(COULEUR_CHEMIN_SOLUTION_NON_TROUVE);
						g.drawLine(MARGE + (i - 1) * 40, MARGE + j * 40, MARGE
								+ i * 40, MARGE + j * 40);
					}
					if (grille.coteDEstPlein[i][j]
							&& grille.SOLUTION_COTED_EST_PLEIN[i][j]) {
						g2.setPaint(COULEUR_CHEMIN_SOLUTION_TROUVE);
						g.drawLine(MARGE + i * 40, MARGE + (j - 1) * 40, MARGE
								+ i * 40, MARGE + j * 40);
					} else if (!grille.coteDEstPlein[i][j]
							&& grille.SOLUTION_COTED_EST_PLEIN[i][j]) {
						g2.setPaint(COULEUR_CHEMIN_SOLUTION_NON_TROUVE);
						g.drawLine(MARGE + i * 40, MARGE + (j - 1) * 40, MARGE
								+ i * 40, MARGE + j * 40);
					}
				}
			}
			// Dessine le chemin
			g2.setPaint(COULEUR_CHEMIN);
			for (int i = 0; i <= RenvoyerLargeur(); i++) {
				for (int j = 0; j <= RenvoyerHauteur(); j++) {
					if (grille.coteBEstPlein[i][j] && !grille.SOLUTION_COTEB_EST_PLEIN[i][j]) {
						g.drawLine(MARGE + (i - 1) * 40, MARGE + j * 40, MARGE + i
								* 40, MARGE + j * 40);
					}
					if (grille.coteDEstPlein[i][j] && !grille.SOLUTION_COTED_EST_PLEIN[i][j]) {
						g.drawLine(MARGE + i * 40, MARGE + (j - 1) * 40, MARGE + i
								* 40, MARGE + j * 40);
					}
				}
			}
		} else {
			// Dessine le chemin
			g2.setPaint(COULEUR_CHEMIN);
			for (int i = 0; i <= RenvoyerLargeur(); i++) {
				for (int j = 0; j <= RenvoyerHauteur(); j++) {
					if (grille.coteBEstPlein[i][j]) {
						g.drawLine(MARGE + (i - 1) * 40, MARGE + j * 40, MARGE + i
								* 40, MARGE + j * 40);
					}
					if (grille.coteDEstPlein[i][j]) {
						g.drawLine(MARGE + i * 40, MARGE + (j - 1) * 40, MARGE + i
								* 40, MARGE + j * 40);
					}
				}
			}
		}

		// Colorie le noeud en orange s'il est faux
		g2.setPaint(COULEUR_NOEUD_FAUX);
		for (int i = 0; i <= RenvoyerLargeur(); i++) {
			for (int j = 0; j <= RenvoyerHauteur(); j++) {
				if (grille.cheminEstFaux[i][j]) {
					g.fillRect(MARGE + i * 40 - 10, MARGE + j * 40 - 10, 20, 20);
				}
			}
		}

		g2.setColor(COULEUR_POINT);
		int r = 15;
		int x = pointActifX * 40 - (r / 2) + MARGE;
		int y = pointActifY * 40 - (r / 2) + MARGE;
		g2.fillOval(x, y, r, r);
	}
	
	// Accès aux dimensions de la grille
	protected int RenvoyerLargeur() {
		return grille.TAILLEL;
	}
	
	protected int RenvoyerHauteur() {
		return grille.TAILLEH;
	}

	// Remise à zéro
	protected void Reset() {
		pointActifX = 0;
		pointActifY = 0;
		keyEventNum = 0;
		pileActEffectuees = new Stack<Action>();
		pileActAnnulees = new Stack<Action>();
		grille.ResetCotesPleins();
		repaint();
	}

	// Méthodes de la fonction annuler action/refaire action
	protected void AnnulerAction() {
		Action derniereAct;
		if (!pileActEffectuees.empty()) {
			derniereAct = pileActEffectuees.peek();
			pointActifX = derniereAct.x;
			pointActifY = derniereAct.y;
			switch (derniereAct.d) {
			case H:
				grille.ChangeCoteD(pointActifX, pointActifY);
				break;
			case B:
				grille.ChangeCoteD(pointActifX, pointActifY + 1);
				break;
			case G:
				grille.ChangeCoteB(pointActifX, pointActifY);
				break;
			case D:
				grille.ChangeCoteB(pointActifX + 1, pointActifY);
				break;
			default:
				// Rien
			}
			pileActAnnulees.push(derniereAct);
			pileActEffectuees.pop();
		}
	}

	protected void RefaireAction() {
		Action derniereAct;
		if (!pileActAnnulees.empty()) {
			derniereAct = pileActAnnulees.peek();
			pointActifX = derniereAct.x;
			pointActifY = derniereAct.y;
			switch (derniereAct.d) {
			case H:
				grille.ChangeCoteD(pointActifX, pointActifY);
				pointActifY--;
				break;
			case B:
				grille.ChangeCoteD(pointActifX, pointActifY + 1);
				pointActifY++;
				break;
			case G:
				grille.ChangeCoteB(pointActifX, pointActifY);
				pointActifX--;
				break;
			case D:
				grille.ChangeCoteB(pointActifX + 1, pointActifY);
				pointActifX++;
				break;
			default:
				// Rien
			}
			pileActEffectuees.push(derniereAct);
			pileActAnnulees.pop();
		}
	}

	// Change la visibilité de la solution : active ou non
	protected void ToggleSolutionEstActive() {
		if (solutionEstActive) {
			solutionEstActive = false;
		} else {
			solutionEstActive = true;
		}
	}

	public void actionPerformed(ActionEvent e) {

	}
}
