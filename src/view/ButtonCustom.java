package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import Global.Configuration;

public class ButtonCustom extends JButton {
	
	private ImageIcon imgIconExited;
	private ImageIcon imgIconEnterred;
	
	public ButtonCustom(String imageRacine, Dimension dimension) {
		super();
		createImageIcon(imageRacine, dimension);
		initButton(dimension);
	}

	public ButtonCustom(Action a, String imageRacine, Dimension dimension) {
		super(a);
		createImageIcon(imageRacine, dimension);
		initButton(dimension);
	}

	public ButtonCustom(String text, String imageRacine, Dimension dimension) {
		super(text);
		createImageIcon(imageRacine, dimension);
		initButton(dimension);
	}
	
	public ButtonCustom(String text, String imageRacine, Dimension dimension, Font font) {
		super(text);
		createImageIcon(imageRacine, dimension);
		initButton(dimension);
		setFont(font);
	}
	
	public ButtonCustom(String text, ImageIcon imgExited, ImageIcon imgEnterred, Dimension dimension) {
		super(text);
		if (imgExited != null) {
			this.imgIconExited = imgExited;
		} 
		if (imgEnterred != null) {
			this.imgIconEnterred = imgEnterred;
		}
		initButton(dimension);
	}

	private void createImageIcon(String imageRacine, Dimension dimension) {
		System.out.println(dimension.width + " " + dimension.height);
		try {
			imgIconExited = new ImageIcon(ImageIO.read(Configuration.charge(imageRacine + ".png", Configuration.MENU)).getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH));
			imgIconEnterred = new ImageIcon(ImageIO.read(Configuration.charge(imageRacine + "_actif.png", Configuration.MENU)).getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void initButton(Dimension dimension) {
		setFont(new Font("Century", Font.PLAIN, 40));
		setHorizontalTextPosition(SwingConstants.CENTER);
		setFocusPainted(false);
		setBorderPainted(false);
		setContentAreaFilled(false);
		setOpaque(false);
		setIcon(imgIconExited);
		addMouseListener(new MouseButtonCustom());
		setPreferredSize(dimension);
	}
	
	
	private class MouseButtonCustom extends MouseAdapter {
		@Override
		public void mouseEntered(MouseEvent e) {
			setIcon(imgIconEnterred);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			setIcon(imgIconExited);
		}
	}
}
