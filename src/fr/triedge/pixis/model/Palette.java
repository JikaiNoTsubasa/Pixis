package fr.triedge.pixis.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Palette")
public class Palette {

	private String name;
	private int id;
	private ArrayList<Integer> colors = new ArrayList<>();
	
	public String getName() {
		return name;
	}
	@XmlElement(name="PaletteName")
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
	public ArrayList<Integer> getColors() {
		return colors;
	}
	
	@XmlElementWrapper(name="ColorList")
	@XmlElement(name="Color")
	public void setColors(ArrayList<Integer> colors) {
		this.colors = colors;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
