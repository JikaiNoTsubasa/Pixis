package fr.triedge.pixis.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Map")
public class Map {

	private String name;
	private int id, height, width;
	public String getName() {
		return name;
	}
	@XmlElement(name="MapName")
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	@XmlAttribute(name="id")
	public void setId(int id) {
		this.id = id;
	}
	public int getHeight() {
		return height;
	}
	@XmlElement(name="MapHeight")
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	@XmlElement(name="MapWidth")
	public void setWidth(int width) {
		this.width = width;
	}
}
