package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
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
public class InterfaceGraphiqueTutoriel implements Runnable {
	
    private JFrame frame;
    private ButtonCustom plus;
    private ButtonCustom moins;
    private JPanel center;
    private final CardLayout c1 = new CardLayout();
    private final int widthImageTuto = 1600;
    private final int heightImageTuto = 900;
    private static Boolean exists = false;
    
    public static void demarrer() {
    	if (!exists) {
    		exists = true;
    		SwingUtilities.invokeLater(new InterfaceGraphiqueTutoriel());
    	} else {
    		System.out.println("La fenetre existe déjà");
    	}
    }
   
    private InterfaceGraphiqueTutoriel() {
        frame = new JFrame("EN GARDE ! - Tutoriel");
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
        frame.setSize(1619,1048);
        frame.setResizable(false);
        try {
			frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(ImageIO.read(Configuration.charge("curseur.png", Configuration.AUTRES)),new Point(0,0),"Mon curseur"));
		} catch (HeadlessException | IndexOutOfBoundsException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        JPanel panelBoutons = new JPanel(new GridLayout(1,2));
        panelBoutons.setPreferredSize(new Dimension(frame.getWidth(),100));
        frame.add(panelBoutons,BorderLayout.NORTH);
        //Dimension et taille du textes des boutons
        final Dimension dim =new Dimension(280,60);
        final Font f = new Font("Century",Font.PLAIN,dim.height/3);
        //boutons mois et plus
        JPanel panelMoins = new JPanel();
        panelMoins.setBackground(Color.white);
        panelBoutons.add(panelMoins);
        JPanel panelPlus= new JPanel();
        panelPlus.setBackground(Color.white);
        panelBoutons.add(panelPlus );
        plus = new ButtonCustom("Suivant >>", "cadre4", dim, f);
        plus.setForeground(Color.white);
        plus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c1.next(center);
            }
        });
        moins = new ButtonCustom("<< Précedent", "cadre4", dim, f);
        moins.setForeground(Color.white);;
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
	        for (int i = 1; i <= 13; i++){
	            center.add(new ScrollPanelImage(widthImageTuto, heightImageTuto, Configuration.charge("tuto" + i + ".png", Configuration.TUTORIEL)), i - 1);
	        }
        } catch(Exception e) {
        	e.printStackTrace();
        }
        frame.add(center);
        frame.repaint();
        frame.setVisible(true);
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
}