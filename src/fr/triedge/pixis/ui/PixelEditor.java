package fr.triedge.pixis.ui;

import java.awt.Color;

import javax.swing.JPanel;

import fr.triedge.pixis.ctrl.Controller;
import fr.triedge.pixis.model.Sprite;

public class PixelEditor extends JPanel{

	private static final long serialVersionUID = 6821220201585858378L;

	private Controller controller;
	private Sprite sprite;
	
	public PixelEditor(Controller controller, Sprite sprite) {
		setController(controller);
		setSprite(sprite);
	}
	
	public void build() {
		setBackground(Color.DARK_GRAY);
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
}
