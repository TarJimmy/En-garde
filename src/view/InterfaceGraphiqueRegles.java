package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import Global.Configuration;

/**
 *
 * @author tardyj
 */
public class InterfaceGraphiqueRegles implements Runnable {
	
    private JFrame frame;
    private BtnNavigation plus;
    private BtnNavigation moins;
    private JPanel center;
    private final CardLayout c1 = new CardLayout();
    private final int widthImageRegle = 866;
    private final int heightImageRegle = 1200;
    private static Boolean exists = false;
    
    public static void demarrer() {
    	if (!exists) {
    		exists = true;
    		SwingUtilities.invokeLater(new InterfaceGraphiqueRegles());
    	} else {
    		System.out.println("La fenetre existe déjà");
    	}
    }
   
    private InterfaceGraphiqueRegles() {
        frame = new JFrame("Règle");
        frame.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				exists = false;
			}
			
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
		});
    }
    
    @Override
    public void run() {
    	frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBackground(Color.white);
        frame.setSize(906, 900);
        frame.setResizable(false);
        JPanel panelBoutons = new JPanel(new GridLayout(1,2));
        panelBoutons.setPreferredSize(new Dimension(frame.getWidth(),100));
        frame.add(panelBoutons,BorderLayout.NORTH);
        //Dimension et taille du textes des boutons
        final Dimension dim =new Dimension(200,80);
        final Font f = new Font(Font.SERIF,Font.BOLD,dim.height/4);
        //boutons mois et plus
        JPanel panelMoins = new JPanel();
        panelMoins.setBackground(Color.white);
        panelBoutons.add(panelMoins);
        JPanel panelPlus= new JPanel();
        panelPlus.setBackground(Color.white);
        panelBoutons.add(panelPlus );
        plus = new BtnNavigation("Suivant >>");
        plus.setFont(f);
        plus.setPreferredSize(dim);
        plus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c1.next(center);
            }
        });
        moins = new BtnNavigation("<< Précedent");
        moins.setPreferredSize(dim);
        moins.setFont(f);
        moins.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c1.previous(center);
            }
        });
        panelMoins.add(moins);
        panelPlus.add(plus);
        center = new JPanel(c1);
        try {
	        for (int i = 1; i <= 4; i++){
	            center.add(new ScrollPanelImage(widthImageRegle, heightImageRegle, Configuration.charge("regle" + i + ".png", Configuration.REGLES)), i - 1);
	        }
        } catch(Exception e) {
        	e.printStackTrace();
        }
        frame.add(center);
        frame.repaint();
        frame.setVisible(true);
    }
       
    //Permet la customisations des Boutons
    private class BtnNavigation extends JButton implements MouseListener {
        private final Color couleur = Color.lightGray;
        
        public BtnNavigation(String nom){
            super(nom);
            setFocusable(false);
            setBackground(couleur);
        }
        @Override
        public void paintComponent(Graphics g) {
	        Graphics2D g2 = (Graphics2D) g;
	        super.paintComponent(g);
	        g2.setColor(Color.BLACK);
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g2.setStroke(new BasicStroke(1.2f));
	        g2.draw(new RoundRectangle2D.Double(1, 1, (getWidth() - 3), (getHeight() - 3), 12, 8));
	        g2.setStroke(new BasicStroke(1.5f));
	        g2.drawLine(4, getHeight() - 3, getWidth() - 4, getHeight() - 3);
	        g2.dispose();
        } 

        @Override
        public void mouseClicked(MouseEvent arg0) {}

        @Override
        public void mousePressed(MouseEvent arg0) {
            setBackground(Color.white);
        }

        @Override
        public void mouseReleased(MouseEvent arg0) {
            setBackground(couleur);
        }

        @Override
        public void mouseEntered(MouseEvent arg0) {}

        @Override
        public void mouseExited(MouseEvent arg0) {}
    }
    
    public class ScrollPanelImage extends JPanel {
        private static final long serialVersionUID = 1L;
        private BufferedImage image;
        private PanelImage canvas;

        public ScrollPanelImage(int width, int height, InputStream in) {
            try {
                this.image = ImageIO.read(in);
                canvas = new PanelImage(width, height, this.image);
            } catch(IOException ex) {
                ex.printStackTrace();
            }
            setSize(width, height);
            JScrollPane sp = new JScrollPane(canvas);
            sp.getVerticalScrollBar().setUnitIncrement(50);
            setLayout(new BorderLayout());
            add(sp, BorderLayout.CENTER);
        }
        
        private class PanelImage extends JComponent {
			
			public int width, height;
            public BufferedImage img;
             
            public PanelImage(int width, int height, BufferedImage image) {
            	this.img = image;
                this.width = width;
                this.height = height;
                setPreferredSize(new Dimension(width, height));
            }
             
            @Override
            protected void paintComponent(Graphics gd) {
                Graphics2D g = (Graphics2D) gd;
                //Et j'affiche en utilisant la transformation
                g.drawImage(img, 0, 0, width, height, null);
                //On libère un peu de mémoire histoire de laisser le GC tranquille un peu plus longtemps
                g.dispose();
            }
        }
    }
	public static void main(String[] args) {
    	System.out.println("coucou");
        new InterfaceGraphiqueRegles();
    }
}