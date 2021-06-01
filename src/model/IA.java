package model;

public interface IA {

	public int choixCarteIA(Coup coup);
	
	public int[] actionIA( Coup coup );
	
	public boolean estBonCoup (Escrimeur e, int valeurCarte, int action);
	
}
