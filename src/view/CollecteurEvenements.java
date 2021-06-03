package view;

import java.util.ArrayList;

public interface CollecteurEvenements {
	boolean commande(String c);
	boolean clickCase(int x);
	boolean animation(String typeAnimation, Animation anim);
}
