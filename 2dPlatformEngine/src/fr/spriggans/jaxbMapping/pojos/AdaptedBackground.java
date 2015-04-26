package fr.spriggans.jaxbMapping.pojos;

import javax.xml.bind.annotation.XmlAttribute;

public class AdaptedBackground {

	private int x;
	private int y;

	@XmlAttribute
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	@XmlAttribute
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
