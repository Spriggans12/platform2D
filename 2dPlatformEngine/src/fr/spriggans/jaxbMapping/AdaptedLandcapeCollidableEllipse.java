package fr.spriggans.jaxbMapping;

import javax.xml.bind.annotation.XmlAttribute;

public class AdaptedLandcapeCollidableEllipse {

	private int xNW;
	private int yNW;
	private int totalW;
	private int totalH;

	@XmlAttribute
	public int getxNW() {
		return xNW;
	}

	public void setxNW(int xNW) {
		this.xNW = xNW;
	}

	@XmlAttribute
	public int getyNW() {
		return yNW;
	}

	public void setyNW(int yNW) {
		this.yNW = yNW;
	}

	@XmlAttribute
	public int getTotalW() {
		return totalW;
	}

	public void setTotalW(int totalW) {
		this.totalW = totalW;
	}

	@XmlAttribute
	public int getTotalH() {
		return totalH;
	}

	public void setTotalH(int totalH) {
		this.totalH = totalH;
	}

}
