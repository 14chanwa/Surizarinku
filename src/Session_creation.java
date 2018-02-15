import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

@SuppressWarnings("serial")
public class Session_creation extends Session {

	public Session_creation(Grille j) {
		super(j);
		addMouseListener(new EcouteurSouris());
		addKeyListener(new EcouteurClavier());
	}

	public Session_creation(int lar, int hau) {
		super(lar, hau);
		addMouseListener(new EcouteurSouris());
		addKeyListener(new EcouteurClavier());
	}

	public void paint(Graphics g) {
		grille.RafraichirCotes();
		super.paint(g);
	}
	
	public void ResetNbCotesPleins() {
		int[][] grilleEntiers = new int[RenvoyerLargeur() + 1][RenvoyerHauteur() + 1];
		for (int i = 1; i <= RenvoyerLargeur(); i++) {
			Arrays.fill(grilleEntiers[i], 9);
		}
		grille.nbCotesPleins = grilleEntiers;
		repaint();
	}

	private class EcouteurSouris extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			int coordSourisX = e.getX() - MARGE;
			int coordSourisY = e.getY() - MARGE;
			int coordX = 999;
			int coordY = 999;
			if ((coordSourisX) % 40 < 10)
				coordX = coordSourisX / 40;
			if ((coordSourisX / 40 + 1) * 40 - coordSourisX < 10)
				coordX = coordSourisX / 40 + 1;
			if ((coordSourisY) % 40 < 10)
				coordY = (coordSourisY - coordSourisY % 40) / 40;
			if ((coordSourisY / 40 + 1) * 40 - coordSourisY < 10)
				coordY = coordSourisY / 40 + 1;
			// Ajouter ici le cas "je clique sur le centre d'une case" et je
			// modifie ou non...
			if ((coordSourisX) % 40 > 10 && 40 - (coordSourisX) % 40 > 10
					&& (coordSourisY) % 40 > 10
					&& 40 - (coordSourisY) % 40 > 10) {
				if (grille.nbCotesPleins[coordSourisX / 40 + 1][coordSourisY / 40 + 1] == 9) {
					grille.nbCotesPleins[coordSourisX / 40 + 1][coordSourisY / 40 + 1] = grille
							.RenvoyerCotes(coordSourisX / 40 + 1,
									coordSourisY / 40 + 1);
				} else {
					grille.nbCotesPleins[coordSourisX / 40 + 1][coordSourisY / 40 + 1] = 9;
				}
			}

			if (0 <= coordX && coordX <= RenvoyerLargeur() && 0 <= coordY
					&& coordY <= RenvoyerHauteur()) {
				pointActifX = coordX;
				pointActifY = coordY;
			}
			repaint();
			grabFocus();
		}
	}

	private class EcouteurClavier extends KeyAdapter {
		public void keyTyped(KeyEvent e) {
			keyEventNum = e.getKeyChar();

			switch (keyEventNum) {
			case 122:
				if (pointActifY > 0) {
					pileActEffectuees.push(new Action(pointActifX, pointActifY,
							Action.Direction.H));
					grille.ChangeCoteD(pointActifX, pointActifY);
					pointActifY--;
				}
				break;
			case 115:
				if (pointActifY < RenvoyerHauteur()) {
					pileActEffectuees.push(new Action(pointActifX, pointActifY,
							Action.Direction.B));
					pointActifY++;
					grille.ChangeCoteD(pointActifX, pointActifY);
				}
				break;
			case 113:
				if (pointActifX > 0) {
					pileActEffectuees.push(new Action(pointActifX, pointActifY,
							Action.Direction.G));
					grille.ChangeCoteB(pointActifX, pointActifY);
					pointActifX--;
				}
				break;
			case 100:
				if (pointActifX < RenvoyerLargeur()) {
					pileActEffectuees.push(new Action(pointActifX, pointActifY,
							Action.Direction.D));
					pointActifX++;
					grille.ChangeCoteB(pointActifX, pointActifY);
				}
				break;
			}
			repaint();
		}
	}
}
