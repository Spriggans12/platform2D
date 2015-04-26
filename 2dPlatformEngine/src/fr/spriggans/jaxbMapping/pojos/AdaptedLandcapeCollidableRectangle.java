package fr.spriggans.jaxbMapping.pojos;

import javax.xml.bind.annotation.XmlAttribute;

public class AdaptedLandcapeCollidableRectangle {

	private int x;
	private int y;
	private int w;
	private int h;
	private int deltaH;
	private boolean skewed;

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

	@XmlAttribute
	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	@XmlAttribute
	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	@XmlAttribute
	public int getDeltaH() {
		return deltaH;
	}

	public void setDeltaH(int deltaH) {
		this.deltaH = deltaH;
	}

	@XmlAttribute
	public boolean getSkewed() {
		return skewed;
	}

	public void setSkewed(boolean skewed) {
		this.skewed = skewed;
	}
}
