package fr.triedge.pixis.model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SpriteAnimation {

	private ArrayList<BufferedImage> images = new ArrayList<>();

	public ArrayList<BufferedImage> getImages() {
		return images;
	}

	public void setImages(ArrayList<BufferedImage> images) {
		this.images = images;
	}
	
}
