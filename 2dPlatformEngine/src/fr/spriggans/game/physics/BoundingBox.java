package fr.spriggans.game.physics;

public class BoundingBox {
	private int x0;
	private int y0;
	private int x1;
	private int y1;
	private int w;
	private int h;

	public BoundingBox(int x0, int y0, int x1, int y1) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.w = x1 - x0;
		this.h = y1 - y0;
	}

	/** Renvoie vrai si la BB contient le point donné. Touché = contenu. */
	public boolean containsPoint(int xPoint, int yPoint) {
		return x0 <= xPoint && xPoint <= x1 && y0 <= yPoint && yPoint <= y1;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getX0() {
		return x0;
	}

	public void setX0(int x0) {
		this.x0 = x0;
	}

	public int getY0() {
		return y0;
	}

	public void setY0(int y0) {
		this.y0 = y0;
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}
}
