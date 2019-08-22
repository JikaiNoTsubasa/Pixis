package fr.triedge.pixis.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SpriteSheet")
public class SpriteSheet {

	private String name;
	private int characterHeight,characterWidth;
	private ArrayList<SpriteLayer> layers = new ArrayList<>();
	private int imageType;

	public String getName() {
		return name;
	}

	@XmlElement(name="SpriteSheetName")
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

	public ArrayList<SpriteLayer> getLayers() {
		return layers;
	}

	@XmlElementWrapper(name="LayerList")
	@XmlElement(name="SpriteLayer")
	public void setLayers(ArrayList<SpriteLayer> layers) {
		this.layers = layers;
	}

	public int getImageType() {
		return imageType;
	}

	@XmlElement(name="ImageType")
	public void setImageType(int imageType) {
		this.imageType = imageType;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
