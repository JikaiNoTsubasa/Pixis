package fr.triedge.pixis.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Sprite")
public class Sprite {

	private int posXonSheet,posYonSheet;
	private byte[] imageData;

	public int getPosXonSheet() {
		return posXonSheet;
	}

	@XmlAttribute(name="posXonSheet")
	public void setPosXonSheet(int posXonSheet) {
		this.posXonSheet = posXonSheet;
	}

	public int getPosYonSheet() {
		return posYonSheet;
	}

	@XmlAttribute(name="posYonSheet")
	public void setPosYonSheet(int posYonSheet) {
		this.posYonSheet = posYonSheet;
	}

	public byte[] getImageData() {
		return imageData;
	}

	@XmlElement(name="ImageData")
	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}
}
