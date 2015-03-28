package fr.spriggans.gfx;

/** Une frame est un objet contenant une image et une durée d'affichage. <br/>
 * La durée est définie sur les ticks, pas les renders. Il y a 60 ticks/s peut importe les fps. */
public class Frame {
	private int[] pixels;
	private int width;
	private int height;
	private int duration;

	public Frame(SpriteSheet spriteSheet, int indexSpriteX, int indexSpriteY, int duration) {
		this.pixels = spriteSheet.getSpritePixelsFor(indexSpriteX, indexSpriteY);
		this.duration = duration;
		this.width = spriteSheet.getSpritesWidth();
		this.height = spriteSheet.getSpritesHeight();
	}

	public int[] getPixels() {
		return pixels;
	}

	// TODO : Render ?

	public void setPixels(int[] pixels) {
		this.pixels = pixels;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
