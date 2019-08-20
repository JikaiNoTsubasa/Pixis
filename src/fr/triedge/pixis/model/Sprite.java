package fr.triedge.pixis.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Sprite")
public class Sprite {

	private String name;
	private int characterHeight,characterWidth;

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
}
