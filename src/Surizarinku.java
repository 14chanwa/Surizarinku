import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Surizarinku extends JFrame implements ActionListener, ItemListener {

	private Session_jeu sessionJeu;
	private Session_creation sessionCreation;

	private enum TypeActif {
		J, C;
	}

	private TypeActif sessionActive;

	private JPanel panelPrincipal = new JPanel();
	private JPanel panelDroit = new JPanel(new BorderLayout());
	private JPanel panelHaut = new JPanel(new BorderLayout());
	private JPanel panelAR = new JPanel(new FlowLayout());
	private JPanel panelLireSupprimer = new JPanel(new GridLayout(3, 1));
	private JPanel panelInstructions = new JPanel(new FlowLayout());
	private JPanel panelBoutonsVerifierOk = new JPanel(new FlowLayout());
	private JPanel panelCards = new JPanel(new CardLayout());
	private JPanel panelBoutonsJeu = new JPanel(new BorderLayout());
	private JPanel panelBoutonsCreation = new JPanel(new GridLayout(7, 1));

	private String[] comboBoxItems = { "Mode Jeu", "Mode Création" };
	private JComboBoxItemListener comboBoxMode = new JComboBoxItemListener(
			comboBoxItems);

	private JButton boutonReset = new JButton("Effacer le tracé");
	private JButton boutonResetNbCotesPleins = new JButton(
			"Effacer les indications");
	private JButton boutonAnnuler = new JButton("Annuler");
	private JButton boutonRefaire = new JButton("Refaire");
	private JButton boutonLireFichier = new JButton("Lire la grille");
	private JButton boutonModifierFichier = new JButton("Modifier la grille");
	private JButton boutonSupprimerFichier = new JButton("Supprimer la grille");
	private JButton boutonRemplirAlea = new JButton("Indications aléatoires");
	private JButton boutonEnregistrer = new JButton("Enregistrer");
	private JButton boutonVerifierGrille = new JButton("Vérifier la grille");
	private JButton boutonToggleSolution = new JButton("Montrer une solution");
	private JButton boutonInstructions = new JButton("Instructions");

	private List listeGrilles = new List();

	private JTextField champTexte = new JTextField();
	private JTextField champProba = new JTextField("0.8");

	private JLabel labelInformations = new JLabel(
			"Bienvenue dans le module Surizarinku !");
	private JLabel labelProba = new JLabel("Probabilité d'indication");
	private JLabel labelTexte = new JLabel("Nom de fichier");

	public Surizarinku(Session_jeu sj, Session_creation sc) {
		setTitle("Surizarinku");
		setLayout(new BorderLayout());

		panelDroit.setSize(new Dimension(200, 550));

		sessionJeu = sj;
		sessionCreation = sc;
		sessionActive = TypeActif.J;

		comboBoxMode.addActionListener(this);

		LireListeGrilles();
		listeGrilles.addActionListener(this);

		boutonReset.addActionListener(this);
		boutonAnnuler.addActionListener(this);
		boutonRefaire.addActionListener(this);
		boutonLireFichier.addActionListener(this);
		boutonModifierFichier.addActionListener(this);
		boutonSupprimerFichier.addActionListener(this);
		boutonRemplirAlea.addActionListener(this);
		boutonEnregistrer.addActionListener(this);
		boutonVerifierGrille.addActionListener(this);
		boutonToggleSolution.addActionListener(this);
		boutonResetNbCotesPleins.addActionListener(this);
		boutonInstructions.addActionListener(this);
		
		listeGrilles.addItemListener(this);

		sessionJeu.setLayout(new BoxLayout(sessionJeu, BoxLayout.Y_AXIS));
		sessionCreation.setLayout(new BoxLayout(sessionCreation,
				BoxLayout.Y_AXIS));
		sessionJeu.setPreferredSize(new Dimension(500, 500));
		sessionCreation.setPreferredSize(new Dimension(500, 500));
		sessionJeu.setFocusable(true);
		sessionCreation.setFocusable(true);

		panelPrincipal.add(sessionJeu);

		panelHaut.add(comboBoxMode, BorderLayout.NORTH);
		panelHaut.add(panelAR, BorderLayout.SOUTH);

		panelAR.add(boutonAnnuler);
		panelAR.add(boutonRefaire);

		panelLireSupprimer.add(boutonLireFichier);
		panelLireSupprimer.add(boutonModifierFichier);
		panelLireSupprimer.add(boutonSupprimerFichier);

		panelBoutonsVerifierOk.add(boutonVerifierGrille);

		panelBoutonsJeu.add(boutonToggleSolution, BorderLayout.NORTH);
		panelBoutonsJeu.add(listeGrilles, BorderLayout.WEST);
		panelBoutonsJeu.add(panelLireSupprimer, BorderLayout.EAST);
		panelBoutonsJeu.add(panelBoutonsVerifierOk, BorderLayout.SOUTH);

		panelBoutonsCreation.add(labelProba);
		panelBoutonsCreation.add(champProba);
		panelBoutonsCreation.add(boutonRemplirAlea);
		panelBoutonsCreation.add(boutonResetNbCotesPleins);
		panelBoutonsCreation.add(labelTexte);
		panelBoutonsCreation.add(champTexte);
		panelBoutonsCreation.add(boutonEnregistrer);

		panelCards.add(panelBoutonsJeu, comboBoxItems[0]);
		panelCards.add(panelBoutonsCreation, comboBoxItems[1]);

		panelDroit.add(panelHaut, BorderLayout.NORTH);
		panelDroit.add(panelCards, BorderLayout.CENTER);
		panelDroit.add(boutonReset, BorderLayout.SOUTH);

		panelInstructions.add(boutonInstructions);
		add(labelInformations, BorderLayout.SOUTH);

		add(panelPrincipal, BorderLayout.CENTER);
		add(panelDroit, BorderLayout.EAST);
		add(panelInstructions, BorderLayout.NORTH);
		addWindowListener(new EcouteurFenetre());
	}

	/**
	 * Met à jour la liste des grilles depuis le fichier ad-hoc
	 */
	private void LireListeGrilles() {
		try {
			listeGrilles.removeAll();
			FileReader in = new FileReader("LISTEGRILLES.txt");
			Scanner sc = new Scanner(in);
			while (sc.hasNextLine()) {
				listeGrilles.add(sc.nextLine());
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.err.println("Fichier non trouvé");
		}
	}

	@SuppressWarnings("resource")
	/**
	 * Renvoie une grille lue depuis le fichier correspondant
	 */
	private Grille LireGrille(String s) {
		String l;
		Scanner sc2;
		char c;
		int TAILLEL = 0;
		int TAILLEH = 0;
		int[][] nbCotesPleins = new int[1][1];
		boolean[][] solutionCoteBEstPlein = new boolean[1][1];
		boolean[][] solutionCoteDEstPlein = new boolean[1][1];
		int ligneEcoutee;
		int colonneEcoutee;

		try {
			FileReader in = new FileReader(s);
			Scanner sc = new Scanner(in);

			// Lecture de la taille du fichier. On vérifie la présence du
			// caractère 'T' pour le formatage du fichier
			while (sc.hasNextLine() && TAILLEL == 0) {
				l = sc.nextLine();
				if (l != "") {
					sc2 = new Scanner(l);
					if (sc2.hasNext()) {
						c = l.charAt(0);
						switch (c) {
						case '/':
							continue;
						case 'T':
							sc2.next();
							if (sc2.hasNextInt()) {
								TAILLEL = sc2.nextInt();
								if (sc2.hasNextInt()) {
									TAILLEH = sc2.nextInt();
									nbCotesPleins = new int[TAILLEL + 1][TAILLEH + 1];
									solutionCoteBEstPlein = new boolean[TAILLEL + 1][TAILLEH + 1];
									solutionCoteDEstPlein = new boolean[TAILLEL + 1][TAILLEH + 1];
								}
							}
							break;
						default:
							continue;
						}
					}
					sc2.close();
				}
			}

			// Si le formatage n'était pas bon, on arrête
			if (TAILLEL == 0)
				throw (new IOException());

			// Lecture de la grille nombre côtés pleins
			ligneEcoutee = 1;
			while (sc.hasNextLine() && ligneEcoutee <= TAILLEH) {
				l = sc.nextLine();
				sc2 = new Scanner(l);
				if (!sc2.hasNextInt()) {
					continue;
				} else {
					colonneEcoutee = 1;
					while (sc2.hasNextInt() && colonneEcoutee <= TAILLEL) {
						nbCotesPleins[colonneEcoutee][ligneEcoutee] = sc2
								.nextInt();
						colonneEcoutee++;
					}
					colonneEcoutee = 1;
					ligneEcoutee++;
				}
			}

			// Lecture de la grille côtés bas
			ligneEcoutee = 0;
			while (sc.hasNextLine() && ligneEcoutee <= TAILLEH) {
				l = sc.nextLine();
				sc2 = new Scanner(l);
				if (!sc2.hasNextInt()) {
					continue;
				} else {
					colonneEcoutee = 0;
					while (sc2.hasNextInt() && colonneEcoutee <= TAILLEL) {
						if (sc2.nextInt() == 1) {
							solutionCoteBEstPlein[colonneEcoutee][ligneEcoutee] = true;
						} else {
							solutionCoteBEstPlein[colonneEcoutee][ligneEcoutee] = false;
						}
						colonneEcoutee++;
					}
					ligneEcoutee++;
				}
			}

			// Lecture de la grille côtés droits
			ligneEcoutee = 0;
			while (sc.hasNextLine() && ligneEcoutee <= TAILLEH) {
				l = sc.nextLine();
				sc2 = new Scanner(l);
				if (!sc2.hasNextInt()) {
					continue;
				} else {
					colonneEcoutee = 0;
					while (sc2.hasNextInt() && colonneEcoutee <= TAILLEL) {
						if (sc2.nextInt() == 1) {
							solutionCoteDEstPlein[colonneEcoutee][ligneEcoutee] = true;
						} else {
							solutionCoteDEstPlein[colonneEcoutee][ligneEcoutee] = false;
						}
						colonneEcoutee++;
					}
					ligneEcoutee++;
				}
			}

			sc.close();
			return (new Grille(TAILLEL, TAILLEH, nbCotesPleins,
					solutionCoteBEstPlein, solutionCoteDEstPlein));

		} catch (FileNotFoundException e) {
			System.err.println("Fichier non trouvé");
		} catch (IOException e) {
			System.err.println("Erreur d'IO");
		}
		return null;
	}

	/**
	 * Enregistre la grille de la Session_creation dans un fichier portant le
	 * nom dans le champ texte.
	 * 
	 * @param s Nom de fichier
	 */
	private void EnregistrerGrille(String s) {
		try {
			// On supprime la ligne correspondante dans la liste si elle existe
			// déjà
			SupprimerGrille(s);

			int i, j;
			String ligne;
			PrintWriter out;

			// En-tête du fichier
			out = new PrintWriter(s);
			out.println("// ---------------------------------------------------");
			out.println("// Fichier d'enregistrement de grille de Surizarinku");
			out.println("// Nom : " + s);
			out.println("// Date : " + Dates.date());
			out.println("// ---------------------------------------------------");
			out.println("// Taille :");
			out.println("T " + sessionCreation.RenvoyerLargeur() + " "
					+ sessionCreation.RenvoyerHauteur());

			// Grille nombre côtés pleins
			out.println("// Grille");
			for (j = 1; j <= sessionCreation.RenvoyerHauteur(); j++) {
				ligne = "";
				for (i = 1; i <= sessionCreation.RenvoyerLargeur(); i++) {
					ligne = ligne + sessionCreation.grille.nbCotesPleins[i][j]
							+ " ";
				}
				out.println(ligne);
			}

			// Grille solution côté bas
			out.println("// Solution CoteBas");
			for (j = 0; j <= sessionCreation.RenvoyerHauteur(); j++) {
				ligne = "";
				for (i = 0; i <= sessionCreation.RenvoyerLargeur(); i++) {
					if (sessionCreation.grille.coteBEstPlein[i][j]) {
						ligne = ligne + 1 + " ";
					} else {
						ligne = ligne + 0 + " ";
					}
				}
				out.println(ligne);
			}

			// Grille solution côté droit
			out.println("// Solution CoteDroit");
			for (j = 0; j <= sessionCreation.RenvoyerHauteur(); j++) {
				ligne = "";
				for (i = 0; i <= sessionCreation.RenvoyerLargeur(); i++) {
					if (sessionCreation.grille.coteDEstPlein[i][j]) {
						ligne = ligne + 1 + " ";
					} else {
						ligne = ligne + 0 + " ";
					}
				}
				out.println(ligne);
			}

			out.close();

			// Mise à jour du fichier liste des grilles
			out = new PrintWriter(
					new FileOutputStream("LISTEGRILLES.txt", true));
			out.println(s);
			out.close();

		} catch (IOException e) {
			System.err.println("Erreur d'IO");
		}
	}

	/**
	 * Supprime la ligne s dans le fichier de la liste des grilles
	 * 
	 * @param s Nom de fichier
	 */
	private void SupprimerGrille(String s) {
		try {
			Vector<String> monVector = new Vector<String>();
			FileReader in = new FileReader("LISTEGRILLES.txt");
			Scanner sc = new Scanner(in);
			String ligne;
			while (sc.hasNextLine()) {
				ligne = sc.nextLine();
				if (!s.equals(ligne)) {
					monVector.addElement(ligne);
				}
			}
			sc.close();
			in.close();
			PrintWriter out = new PrintWriter("LISTEGRILLES.txt");
			for (int i = 0; i < monVector.size(); i++) {
				out.println(monVector.get(i));
			}
			out.close();
		} catch (FileNotFoundException e) {
			System.err.println("Fichier non trouvé");
		} catch (IOException e) {
			System.err.println("Erreur d'IO");
		}
	}

	/**
	 * Comportement actionPerformed
	 */
	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == boutonReset) {
			switch (sessionActive) {
			case J:
				sessionJeu.Reset();
				break;
			case C:
				sessionCreation.Reset();
				break;
			}
			labelInformations.setText("Chemin effacé");
		}

		if (evt.getSource() == boutonAnnuler) {
			switch (sessionActive) {
			case J:
				sessionJeu.AnnulerAction();
				break;
			case C:
				sessionCreation.AnnulerAction();
				break;
			}
		}

		if (evt.getSource() == boutonRefaire) {
			switch (sessionActive) {
			case J:
				sessionJeu.RefaireAction();
				break;
			case C:
				sessionCreation.RefaireAction();
				break;
			}
		}

		if (evt.getSource() == boutonLireFichier) {
			try {
				sessionJeu.grille = LireGrille(listeGrilles.getSelectedItem());
				labelInformations.setText("Grille lue : "
						+ listeGrilles.getSelectedItem());
			} catch (NullPointerException e) {
				System.err.println("Grille non conforme");
			}
		}

		if (evt.getSource() == boutonModifierFichier) {
			champTexte.setText(listeGrilles.getSelectedItem());
			sessionCreation.grille = LireGrille(listeGrilles.getSelectedItem());
			sessionCreation.grille.TracerCheminSolution();
			comboBoxMode.setSelectedItem(comboBoxItems[1]);
			labelInformations.setText("Grille lue : "
					+ listeGrilles.getSelectedItem());
		}

		if (evt.getSource() == boutonSupprimerFichier) {
			try {
				SupprimerGrille(listeGrilles.getSelectedItem());
				// Mise à jour de la liste des grilles
				listeGrilles.removeAll();
				LireListeGrilles();
				sessionJeu.grille = new Grille(10, 10);
				labelInformations.setText("La grille a bien été supprimée.");
			} catch (NullPointerException e) {
				System.err.println("Aucun item sélectionné");
			}
		}

		if (evt.getSource() == boutonEnregistrer) {
			EnregistrerGrille(champTexte.getText());
			// Mise à jour de la liste des grilles
			listeGrilles.removeAll();
			LireListeGrilles();
			labelInformations.setText("Grille enregistrée sous le nom : "
					+ champTexte.getText() + ".");
		}

		if (evt.getSource() == boutonVerifierGrille) {
			if (boutonVerifierGrille.getText().equals("Vérifier la grille")) {
				sessionJeu.grille.CotesSontFaux();
				sessionJeu.grille.CheminEstFaux();
				if (!sessionJeu.grille.CotesSontFaux() && !sessionJeu.grille.CheminEstFaux()) {
					JOptionPane.showMessageDialog(null, "La grille est juste, félicitations ! :)");
				} else {
					boutonVerifierGrille.setText("J'ai compris !");
				}
			} else if (boutonVerifierGrille.getText().equals("J'ai compris !")) {
				sessionJeu.grille.EffacerVerifier();
				boutonVerifierGrille.setText("Vérifier la grille");
			}

		}

		if (evt.getSource() == boutonToggleSolution) {
			sessionJeu.ToggleSolutionEstActive();
			if (boutonToggleSolution.getText().equals("Montrer une solution")) {
				boutonToggleSolution.setText("Masquer la solution");
			} else {
				boutonToggleSolution.setText("Montrer une solution");
			}
		}

		if (evt.getSource() == boutonRemplirAlea) {
			try {
				sessionCreation.grille.RemplirNbCotesAlea(Double
						.parseDouble(champProba.getText()));
			} catch (NumberFormatException e) {
				System.err
						.println("Entrer un double entre 0 et 1 avec un point");
			}
		}

		if (evt.getSource() == boutonResetNbCotesPleins) {
			sessionCreation.ResetNbCotesPleins();
		}

		if (evt.getSource() == boutonInstructions) {
			URL url = Surizarinku.class.getResource("/Instructions.jpg");
			JOptionPane.showMessageDialog(null, "", "Instructions",
					JOptionPane.INFORMATION_MESSAGE, new ImageIcon(
							url));
		}

		sessionJeu.repaint();
		sessionCreation.repaint();

		// Déplacement du focus dans la grille
		switch (sessionActive) {
		case J:
			sessionJeu.grabFocus();
			break;
		case C:
			sessionCreation.grabFocus();
			break;
		}
	}
	
	public void itemStateChanged(ItemEvent evt) {
		switch (sessionActive) {
		case J:
			sessionJeu.grabFocus();
			break;
		case C:
			sessionCreation.grabFocus();
			break;
		}
	}

	/**
	 * Bascule vers la session de jeu
	 */
	private void AfficheSessionJeu() {
		// Afficher session jeu
		sessionActive = TypeActif.J;
		panelPrincipal.remove(sessionCreation);
		panelPrincipal.add(sessionJeu);
		CardLayout cl = (CardLayout) (panelCards.getLayout());
		cl.show(panelCards, comboBoxItems[0]);
		pack();
		labelInformations.setText("Passage en mode Jeu");
	}

	/**
	 * Bascule vers la session création
	 */
	private void AfficheSessionCreation() {
		// Afficher session création
		sessionActive = TypeActif.C;
		panelPrincipal.remove(sessionJeu);
		panelPrincipal.add(sessionCreation);
		CardLayout cl = (CardLayout) (panelCards.getLayout());
		cl.show(panelCards, comboBoxItems[1]);
		pack();
		labelInformations.setText("Passage en mode Création");
	}

	/*
	 * Classes auxiliaires
	 */

	/**
	 * Ecouteur de la fenêtre
	 * 
	 * @author Quentin
	 *
	 */
	private class EcouteurFenetre extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}


	private class JComboBoxItemListener extends JComboBox<String> implements
			ItemListener {

		public JComboBoxItemListener(String[] s) {
			super(s);
			addItemListener(this);
		}

		public void itemStateChanged(ItemEvent evt) {
			if (comboBoxItems[0].compareTo((String) evt.getItem()) == 0) {
				AfficheSessionJeu();
			}
			if (comboBoxItems[1].compareTo((String) evt.getItem()) == 0) {
				AfficheSessionCreation();
			}
			switch (sessionActive) {
			case J:
				sessionJeu.grabFocus();
				break;
			case C:
				sessionCreation.grabFocus();
				break;
			}
		}
	}

	/**
	 * Lanceur
	 * 
	 * @param args Rien
	 */
	public static void main(String[] args) {
		Surizarinku suri = new Surizarinku(new Session_jeu(10, 10),
				new Session_creation(10, 10));
		suri.setResizable(false);
		suri.setLocation(50, 50);
		suri.pack();
		suri.setVisible(true);
	}
}
