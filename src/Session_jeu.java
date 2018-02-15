import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class Session_jeu extends Session {

	public Session_jeu(Grille j) {
		super(j);
		addMouseListener(new EcouteurSouris());
		addKeyListener(new EcouteurClavier());
	}

	public Session_jeu(int lar, int hau) {
		super(lar, hau);
		addMouseListener(new EcouteurSouris());
		addKeyListener(new EcouteurClavier());
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
