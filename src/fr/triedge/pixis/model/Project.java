package fr.triedge.pixis.model;

import java.util.ArrayList;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import fr.triedge.pixis.utils.Storage;

@XmlRootElement(name="Project")
public class Project {

	private String name;
	
	private ArrayList<SpriteSheet> sprites = new ArrayList<>();
	private ArrayList<Map> maps = new ArrayList<>();
	private ArrayList<Palette> palettes = new ArrayList<>();

	public String getName() {
		return name;
	}
	
	public void save() throws JAXBException {
		Storage.storeProjectToDefault(this, getName());
	}

	@XmlElement(name="ProjectName")
	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<SpriteSheet> getSprites() {
		return sprites;
	}

	@XmlElementWrapper(name="SpriteList")
	@XmlElement(name="Sprite")
	public void setSprites(ArrayList<SpriteSheet> sprites) {
		this.sprites = sprites;
	}

	public ArrayList<Map> getMaps() {
		return maps;
	}

	@XmlElementWrapper(name="MapList")
	@XmlElement(name="Map")
	public void setMaps(ArrayList<Map> maps) {
		this.maps = maps;
	}

	public ArrayList<Palette> getPalettes() {
		return palettes;
	}

	@XmlElementWrapper(name="PaletteList")
	@XmlElement(name="Palette")
	public void setPalettes(ArrayList<Palette> palettes) {
		this.palettes = palettes;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
