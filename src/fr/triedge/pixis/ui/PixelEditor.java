package fr.triedge.pixis.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import fr.triedge.pixis.ctrl.Controller;
import fr.triedge.pixis.model.SpriteSheet;

public class PixelEditor extends JPanel{
	//https://stackoverflow.com/questions/2900801/wanting-a-type-of-grid-for-a-pixel-editor/2901472#2901472
	//https://stackoverflow.com/questions/34344309/create-a-swing-gui-to-manipulate-png-pixel-by-pixel
	private static final long serialVersionUID = 6821220201585858378L;

	private Controller controller;
	private SpriteSheet sprite;
	
	BufferedImage img;
	
	public PixelEditor(Controller controller, SpriteSheet sprite) {
		setController(controller);
		setSprite(sprite);
	}
	
	public void build() {
		setBackground(Color.DARK_GRAY);
		this.setPreferredSize(new Dimension(16 * 10, 16 * 10));
		try {
			img = ImageIO.read(new File("img/icon/o_file.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}//new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        //Graphics2D g2d = (Graphics2D) img.getGraphics();
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        int paneW = this.getWidth();
        int paneH = this.getHeight();
        //g.drawImage(img, 0, 0, paneW, paneH, null);
        g.drawImage(img,0,0,160,160,null);
    }

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public SpriteSheet getSprite() {
		return sprite;
	}

	public void setSprite(SpriteSheet sprite) {
		this.sprite = sprite;
	}
}
