package controller;

import java.io.IOException;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import Database.SauvegarderPartie_DAO;
import model.Carte;
import model.Coup;
import model.DeckDefausse;
import model.DeckPioche;
import model.Escrimeur;
import model.Historique;
import model.IncorrectCarteException;
import model.Jeu;
import model.Jeu.Action;
import model.Plateau;
import model.TypeEscrimeur;
import view.Animation;
import view.InterfaceGraphiqueActionAnnexe;
import view.InterfaceGraphiqueJeu;
import view.InterfaceGraphiqueMenu;

public class ControlerJeu extends Controler {
	private Jeu jeu;
	private LinkedList<Animation> animations;
	boolean animationsActives;
	private SauvegarderPartie_DAO partieSauvegardee;
	
	public ControlerJeu(Jeu jeu) {
		this.jeu = jeu;
		this.animations = new LinkedList<>();
		animationsActives = false;
		partieSauvegardee = new SauvegarderPartie_DAO();
		InterfaceGraphiqueJeu.demarrer(this, jeu);
	}

	public void piocher(Escrimeur e) {
		jeu.piocher(e);
	}
	
	@Override
	public boolean clickCase(int x, int nbCartesAUtiliser) {
		jeu.afficherEtatJeu();
		int nbCartesAttaque = nbCartesAUtiliser;
		Coup dernierCoup = jeu.getHistorique().voirDernierCoup();
		Escrimeur currentEscrimeur = jeu.getCurrentEscrimeur();
		int indiceCurrentEscrimeur = jeu.getIndiceCurrentEscrimeur();
		Plateau plateau = jeu.getPlateau();
		int positionCurrentEscrimeur = plateau.getPosition(indiceCurrentEscrimeur);
		int positionNotCurrentEscrimeur = plateau.getPosition(((indiceCurrentEscrimeur + 1) % 2));
		Coup coupAJouer;
		Carte[] cartesAJouer;

		if (positionCurrentEscrimeur == x) {
			//il faut defendre
			int puissanceAttaque = jeu.getHistorique().voirDernierCoup().getCartes().length;
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
						//fin de partie, winner gagne
					}
				} else {
					commenceMancheSuivante(winner.getIndice());
				}
			}
		} else {
			winner.addMancheGagnee();
			if(winner.getMancheGagner() != jeu.getNbManchesPourVictoire()) {
				commenceMancheSuivante(winner.getIndice());
			}else {
				//fin de partie, winner gagne
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
				jeu.changerTour();
				return true;
			case "Menu":
				InterfaceGraphiqueActionAnnexe.close();
				InterfaceGraphiqueJeu.close();
				InterfaceGraphiqueMenu.demarrer(new ControlerAutre());
				return true;
			case "QuitterJeu":
				System.exit(0);
				return false;
			case "nouvellePartie":
				nouvellePartie();
				return true;
			case "sauvPartie":
				partieSauvegardee.sauvegardeJeu(jeu);
				return false;
			case "chargePartie":
				
				return true;
			case "annuleCoup":
				jeu.getHistorique().annulerCoup();
				return false;
			case "refaireCoup":
				jeu.getHistorique().rejouerCoupAnnule();
				return true;
			case "close":
				InterfaceGraphiqueActionAnnexe.close(); 
				return true;
			case "PageInitialiser":
				nouvellePartie();
				return true;
			case "ChangeModeAnimation":
				jeu.toggleAnimationAutoriser();
				return true;
			default:
				System.out.println("Commande pas traitee : " + c);
				return false;
		}
	}
	
	public boolean finirAction() {
		HashSet<Integer> cases = jeu.casesJouables();
		if(cases.size() == 1 && cases.contains(-1)) {
			jeu.changerTour();
		} else {
			jeu.modifieVue(Action.ACTUALISER_PLATEAU);
		}
		if (jeu.isDernierTour()) {
			finDeManche(null);
		} else {
			HashSet<Integer> cj = jeu.casesJouables();
			if ((!jeu.getDeckPioche().deckVide() && cj.isEmpty()) || (cj.isEmpty() && (jeu.getHistorique().voirDernierCoup().getAction() == Coup.ATTAQUEDIRECTE || jeu.getHistorique().voirDernierCoup().getAction() == Coup.ATTAQUEINDIRECTE))) {
				System.out.println("aucune case jouable");
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
		jeu.nouvellePartie();
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
		InterfaceGraphiqueJeu.close();
		this.jeu = jeu;
		this.animations.clear();
		animationsActives = false;
		InterfaceGraphiqueJeu.demarrer(this, jeu);
	}
	
}
