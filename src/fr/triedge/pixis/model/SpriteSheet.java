package fr.triedge.pixis.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SpriteSheet")
public class SpriteSheet {

	private String name;
	private int characterHeight,characterWidth;
	private ArrayList<SpriteSerie> layers = new ArrayList<>();

	public String getName() {
		return name;
	}

	@XmlElement(name="SpriteName")
	public void setName(String name) {
		this.name = name;
	}

	public int getCharacterWidth() {
		return characterWidth;
	}

	@XmlElement(name="CharacterWidth")
	public void setCharacterWidth(int characterWidth) {
		this.characterWidth = characterWidth;
	}

	public int getCharacterHeight() {
		return characterHeight;
	}

	@XmlElement(name="CharacterHeight")
	public void setCharacterHeight(int characterHeight) {
		this.characterHeight = characterHeight;
	}

	public ArrayList<SpriteSerie> getLayers() {
		return layers;
	}

	public void setLayers(ArrayList<SpriteSerie> layers) {
		this.layers = layers;
	}
}
