package controller;

import java.util.HashSet;
import java.util.LinkedList;
import Database.Classement_DAO;
import Database.SauvegarderPartie_DAO;
import model.Carte;
import model.Coup;
import model.Escrimeur;
import model.IA;
import model.IA_Difficile;
import model.IA_Moyenne;
import model.Jeu;
import model.Jeu.Action;
import model.Plateau;
import model.TypeEscrimeur;
import view.Animation;
import view.InterfaceGraphiqueActionAnnexe;
import view.InterfaceGraphiqueChargerPartie;
import view.InterfaceGraphiqueFin;
import view.InterfaceGraphiqueJeu;
import view.InterfaceGraphiqueMenu;

public class ControlerJeu extends Controler {
	protected Jeu jeu;
	protected LinkedList<Animation> animations;
	protected boolean animationsActives;
	protected final SauvegarderPartie_DAO partieSauvegardee = new SauvegarderPartie_DAO();
	protected final Classement_DAO classDAO = new Classement_DAO();

	protected boolean lancerNouvellePartie;
	protected IA IA_conseil;
	
	public ControlerJeu() {}
	
	public ControlerJeu(Jeu jeu) {
		initControler(jeu, true, true);
	}
	
	public ControlerJeu(Jeu jeu, boolean lancerNouvellePartie, boolean showGraphique) {
		initControler(jeu, lancerNouvellePartie, showGraphique);
	}
	


	public ControlerJeu(Jeu jeu, boolean lancerNouvellePartie) {
		initControler(jeu, lancerNouvellePartie, true);
	}
	
	
	private void initControler(Jeu jeu, boolean lancerNouvellePartie, boolean showGraphique) {
		this.lancerNouvellePartie = lancerNouvellePartie;
		this.jeu = jeu;
		this.jeu.setShowGraphique(showGraphique);
		this.animations = new LinkedList<>();
		animationsActives = false;
		initIA();
		if (showGraphique) {
			InterfaceGraphiqueJeu.demarrer(this, jeu);
		} else {
			System.out.println("Interface graphique non lancé");
			jeu.nouvellePartie();
		}
		InsertJoueursBD();
	}
	
	private void InsertJoueursBD() {
		classDAO.insertJoueur(jeu.getEscrimeurGaucher().getNom());
		classDAO.insertJoueur(jeu.getEscrimeurDroitier().getNom());
	}
	
	protected void initIA() {
		IA_conseil = new IA_Difficile(jeu);
	}
	
	public void piocher(Escrimeur e) {
		jeu.piocher(e);
	}
	
	@Override
	public boolean clickCase(int x, int nbCartesAUtiliser) {
		//jeu.afficherEtatJeu();
		int nbCartesAttaque = nbCartesAUtiliser;
		Coup dernierCoup = jeu.getHistorique().voirDernierCoup();
		Escrimeur currentEscrimeur = jeu.getCurrentEscrimeur();
		int indiceCurrentEscrimeur = jeu.getIndiceCurrentEscrimeur();
		Plateau plateau = jeu.getPlateau();
		int positionCurrentEscrimeur = plateau.getPosition(indiceCurrentEscrimeur);
		int positionNotCurrentEscrimeur = plateau.getPosition(((indiceCurrentEscrimeur + 1) % 2));
		Coup coupAJouer;
		Carte[] cartesAJouer;

		if (x == -1) {
			if(dernierCoup.getEscrimeur() == jeu.getCurrentEscrimeur()) {
				jeu.changerTour();
				return finirAction();
			}
			coupAJouer = null;
		} else if(positionCurrentEscrimeur == x) {
			//il faut defendre
			int puissanceAttaque = dernierCoup.getCartes().length;
			cartesAJouer = new Carte[puissanceAttaque];
			int i = 0;
			int nbCartesCurrentEscrimeur = currentEscrimeur.getNbCartes();
			int distanceAttaque = plateau.getPosition(Escrimeur.DROITIER) - plateau.getPosition(Escrimeur.GAUCHER);
			while (i < nbCartesCurrentEscrimeur && puissanceAttaque > 0) {
				if (currentEscrimeur.getCarte(i) != null && currentEscrimeur.getCarte(i).getDistance() == distanceAttaque) {
					cartesAJouer[puissanceAttaque-1] = currentEscrimeur.getCarte(i);
					puissanceAttaque --;
				}
				i++;
			}
			coupAJouer = new Coup(currentEscrimeur, cartesAJouer, Coup.PARER);
		} else if(positionNotCurrentEscrimeur == x){
			//attaquer
			cartesAJouer = new Carte[nbCartesAttaque];
			int i = 0;
			int nbCartesCurrentEscrimeur = currentEscrimeur.getNbCartes();
			int distanceAttaque = plateau.getPosition(Escrimeur.DROITIER) - plateau.getPosition(Escrimeur.GAUCHER);
			while (i < nbCartesCurrentEscrimeur && nbCartesAttaque != 0) {
				if (currentEscrimeur.getCarte(i) != null && currentEscrimeur.getCarte(i).getDistance() == distanceAttaque) {
					cartesAJouer[nbCartesAttaque-1] = currentEscrimeur.getCarte(i);
					nbCartesAttaque--;
				}
				i++;
			}	
			int typeAttaque;
			if (dernierCoup != null && dernierCoup.getEscrimeur() == currentEscrimeur && dernierCoup.getAction() == Coup.AVANCER) {
				typeAttaque = Coup.ATTAQUEINDIRECTE;
			} else {
				typeAttaque = Coup.ATTAQUEDIRECTE;
			}
			coupAJouer = new Coup(currentEscrimeur, cartesAJouer, typeAttaque);
		} else if((currentEscrimeur.getIsGaucher() && x > positionCurrentEscrimeur) || (!currentEscrimeur.getIsGaucher() && x < positionCurrentEscrimeur)) {
			//avancer
			cartesAJouer = new Carte[1];
			int distanceClick = Math.abs(x - positionCurrentEscrimeur);
			int i = 0;
			int nbCartesCurrentEscrimeur = currentEscrimeur.getNbCartes();
			while(i < nbCartesCurrentEscrimeur && cartesAJouer[0] == null) {
				if(currentEscrimeur.getCarte(i) != null && currentEscrimeur.getCarte(i).getDistance() == distanceClick) {
					cartesAJouer[0] = currentEscrimeur.getCarte(i);
				}
				i++;
			}
			coupAJouer = new Coup(currentEscrimeur, cartesAJouer, Coup.AVANCER);
		} else {
			//reculer ou esquiver
			cartesAJouer = new Carte[1];
			int distanceClick = Math.abs(x - positionCurrentEscrimeur);
			int i = 0;
			int nbCartesCurrentEscrimeur = currentEscrimeur.getNbCartes();
			while (i < nbCartesCurrentEscrimeur && cartesAJouer[0] == null) {
				if (currentEscrimeur.getCarte(i) != null && currentEscrimeur.getCarte(i).getDistance() == distanceClick) {
					cartesAJouer[0] = currentEscrimeur.getCarte(i);
				}
				i++;
			}
			if (dernierCoup != null && dernierCoup.getAction() == Coup.ATTAQUEINDIRECTE) {
				coupAJouer = new Coup(currentEscrimeur, cartesAJouer, Coup.ESQUIVER);
			} else {
				coupAJouer = new Coup(currentEscrimeur, cartesAJouer, Coup.RECULER);
			}
		}
		if (jeu.jouer(coupAJouer, false)) {
			//jeu.modifieVue(Action.ACTUALISER_PLATEAU);
			return finirAction();
		} else {
			jeu.modifieVue(Action.ACTUALISER_PLATEAU);
			jeu.modifieVue(Action.ACTUALISER_ESCRIMEUR);
			return true;
		}
	}
	
	public void finDeManche(Escrimeur w) {
		Escrimeur winner = w;
		if (winner == null) {
			int action = jeu.getHistorique().voirDernierCoup().getAction();
			Escrimeur gaucher = jeu.getEscrimeurGaucher();
			Escrimeur droitier = jeu.getEscrimeurDroitier();
			Plateau plateau = jeu.getPlateau();
			if ((action == Coup.ATTAQUEDIRECTE  || action == Coup.ATTAQUEINDIRECTE) && !jeu.casesJouables().isEmpty()) {
					jeu.setDernierTour(true);
			} else {
				if (action == Coup.ATTAQUEDIRECTE  || action == Coup.ATTAQUEINDIRECTE) {
					winner = jeu.getHistorique().voirDernierCoup().getEscrimeur();
				} else {
					int avantageGaucher = 0;
					int nbCartes = gaucher.getNbCartes();
					int distanceAttaque = plateau.getPosition(Escrimeur.DROITIER) - plateau.getPosition(Escrimeur.GAUCHER);
					Carte[] cartesGaucher = gaucher.getCartes();
					//checker le joueur qui a le plus de carte permettant une attaque directe en main
					for (int i = 0; i < nbCartes; i++) {
						if (cartesGaucher[i] != null && cartesGaucher[i].getDistance() == distanceAttaque) {
							avantageGaucher ++;
						}
					}
					nbCartes = droitier.getNbCartes();
					Carte[] cartesDroitier = droitier.getCartes();
					for (int i = 0; i < nbCartes; i++) {
						if (cartesDroitier[i] != null && cartesDroitier[i].getDistance() == distanceAttaque) {
							avantageGaucher --;
						}
					}
					if (avantageGaucher == 0) {
						//checker le joueur qui a le plus avance
						avantageGaucher = (plateau.getPosition(Escrimeur.GAUCHER)-1) - (plateau.getNbCase() - plateau.getPosition(Escrimeur.DROITIER));
					}
					if (avantageGaucher > 0) {
						winner = gaucher;
					} else if (avantageGaucher < 0) {
						winner = droitier;
					}
				}
				if (winner != null) {
					winner.addMancheGagnee();
					if(winner.getMancheGagner() != jeu.getNbManchesPourVictoire()) {
						commenceMancheSuivante(winner.getIndice());
					}else {
						classDAO.insertMatch(jeu.getEscrimeurGaucher().getNom(), jeu.getEscrimeurDroitier().getNom(), jeu.getEscrimeurGaucher().getMancheGagner(),  jeu.getEscrimeurDroitier().getMancheGagner(), (winner.getIsGaucher()) ? "Gaucher" : "Droitier");
						closeAll();
						InterfaceGraphiqueFin.demarrer(this, winner);
					}
				} else {
					commenceMancheSuivante(Jeu.EGALITE);
				}
			}
		} else {
			winner.addMancheGagnee();
			if (winner.getMancheGagner() != jeu.getNbManchesPourVictoire()) {
				commenceMancheSuivante(winner.getIndice());
			} else {
				classDAO.insertMatch(jeu.getEscrimeurGaucher().getNom(), jeu.getEscrimeurDroitier().getNom(), jeu.getEscrimeurGaucher().getMancheGagner(),  jeu.getEscrimeurDroitier().getMancheGagner(), (winner.getIsGaucher()) ? "Gaucher" : "Droitier");
				closeAll();
				InterfaceGraphiqueFin.demarrer(this, winner);
			}
		}
	}
	
	@Override
	public boolean animation(String commande, Animation anim) {
		switch (commande) {
			case "Ajouter":
				animations.add(anim);
				commande("ActionTerminer");
				return true;
			case "Terminer":
				animations.remove(anim);
				animationsActives = false;
				commande("ActionTerminer");
				return true;
			case "Lancer":
				if (!animations.isEmpty() && !animationsActives) {
					animationsActives = true;
					animations.peek().demarre();
				}
				return true;
			default:
				System.out.println("Animation pas traité : " + commande);
				break;
			}
		return false;
	}

	@Override
	public boolean commande(String c) {
		switch (c) {
			case "ActionTerminer":
				jeu.actionTerminer();
				return true;
			case "ActionLancer":
				jeu.demarreActionSuivante();
				return true;
			case "PasserTour":
				clickCase(-1, 0);
				return true;
			case "Menu":
				closeAll();
				InterfaceGraphiqueMenu.demarrer(new ControlerAutre());
				return true;
			case "QuitterJeu":
				System.exit(0);
				return false;
			case "nouveauMatch":
				nouveauMatch();
				return true;
			case "nouvellePartie":
				nouvellePartie();
				return true;
			case "sauvPartie":
				int idJeu = partieSauvegardee.sauvegardeJeu(jeu);
				jeu.setIdJeu(idJeu);
				return false;
			case "chargePartie":
				InterfaceGraphiqueChargerPartie.demarrer(this);
				return true;
			case "annuleCoup":
				jeu.getHistorique().annulerCoup();
				return false;
			case "refaireCoup":
				jeu.getHistorique().rejouerCoupAnnule();
				return true;
			case "closeActionAnnexe":
				InterfaceGraphiqueActionAnnexe.close(); 
				return true;
			case "PageInitialiser":
				if (lancerNouvellePartie) {
					lancerNouvellePartie = true;
					nouvellePartie();
				} else {
					jeu.modifieVue(Action.ACTUALISER_JEU);
				}
				return true;
			case "ChangeModeAnimation":
				desactiverAnimation();
				return true;
			case "montrerCartes":
				jeu.toggleShowAllCartes();
				return true;
			case "montrerAide":
				if (jeu.aideEstMontrer()) {
					jeu.setCaseAide(IA_conseil.getChoixCoup()[0]);
				}
				return true;
			default:
				System.out.println("Commande pas traitee : " + c);
				return false;
		}
	}
	
	private void desactiverAnimation() {
		animations.clear();
		jeu.toggleAnimationAutoriser();
	}

	public boolean finirAction() {
		HashSet<Integer> cases = jeu.casesJouables();
		if(cases.size() == 1 && cases.contains(-1)) {
			jeu.changerTour();
		} else {
			if (jeu.getCurrentEscrimeur().getType() == TypeEscrimeur.HUMAIN) {
				jeu.modifieVue(Action.ACTUALISER_PLATEAU);
			} else {
				jeu.IADoitJouer();
			}
		}
		if (jeu.isDernierTour()) {
			finDeManche(null);
		} else {
			HashSet<Integer> cj = jeu.casesJouables();
			if ((!jeu.getDeckPioche().deckVide() && cj.isEmpty()) || (cj.isEmpty() && (jeu.getHistorique().voirDernierCoup().getAction() == Coup.ATTAQUEDIRECTE || jeu.getHistorique().voirDernierCoup().getAction() == Coup.ATTAQUEINDIRECTE))) {
				System.out.println("defense impossible ou aucun coup jouable");
				finDeManche(jeu.getNotCurrentEscrimeur());
				return true;
			} else if (jeu.getDeckPioche().deckVide()) {
				finDeManche(null);
				return true;
			}
		}
		return true;
	}
	
	public void nouvellePartie() {
		animationsActives = false;
		if (animations.size() > 0) {
			animations.getFirst().termine();
		}
		animations.clear();
		jeu.setIdJeu(-1);
		jeu.nouvellePartie();
	}
	
	public void nouveauMatch() {
		animations = new LinkedList<>();
		jeu.echangeEscrimeurs();
		jeu.resetAction();
		closeAll();
		InterfaceGraphiqueJeu.demarrer(this);
	}
	
	public void commenceMancheSuivante(int indiceWinner) {
		animationsActives = false;
		if (animations.size() > 0) {
			animations.getFirst().termine();
		}
		animations.clear();
		jeu.resetAction();
		jeu.setIndiceWinnerManche(indiceWinner);
		jeu.modifieVue(Action.ACTUALISER_PLATEAU_SANS_CASE);
		jeu.modifieVueAnimation(Action.ANIMATION_FIN_MANCHE);
		jeu.nouvelleManche();
	}
	
	
	@Override
	public void SuiteChargerPartie(Jeu jeu) {
		// TODO Auto-generated method stub
		closeAll();
		this.jeu = jeu;
		this.animations.clear();
		animationsActives = false;
		lancerNouvellePartie = false;
		InterfaceGraphiqueJeu.demarrer(this, jeu);
	}
	
	public ControlerJeu copySimple() {
		return new ControlerJeu(jeu.copySimple());
	}
	
	public Jeu getJeu() {
		return jeu;
	}
	
	public void closeAll() {
		InterfaceGraphiqueActionAnnexe.close();
		InterfaceGraphiqueFin.close();
		InterfaceGraphiqueJeu.close();
	}
}
