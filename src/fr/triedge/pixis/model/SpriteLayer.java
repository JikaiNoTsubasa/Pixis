package fr.triedge.pixis.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SpriteLayer")
public class SpriteLayer {

	private ArrayList<Sprite> sprites = new ArrayList<>();
	private int id;

	public ArrayList<Sprite> getSprites() {
		return sprites;
	}

	@XmlElementWrapper(name="SpriteList")
	@XmlElement(name="Sprite")
	public void setSprites(ArrayList<Sprite> sprites) {
		this.sprites = sprites;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
