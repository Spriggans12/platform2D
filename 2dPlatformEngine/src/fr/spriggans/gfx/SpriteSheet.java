package fr.spriggans.gfx;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Classe permettant de charger des sous-images (int[]) depuis une sheet.
 */
public class SpriteSheet {
	public static final SpriteSheet[] spriteSheets = new SpriteSheet[128];
	public static final SpriteSheet TEST_SHEET = new SpriteSheet(0,
			"res/sheets/test_sheet.png", 48, 96);
	public static final SpriteSheet METALSLUG_TEST_SHEET = new SpriteSheet(1,
			"res/sheets/metalSlug.png", 33, 40);

	private final int id;
	private int width;
	private int height;
	/** Fréquence horizontale des sprites. */
	private final int spritesWidth;
	/** Fréquence verticale des sprites. */
	private final int spritesHeight;
	/** SpriteSheet. */
	private BufferedImage sheet;

	public SpriteSheet(int id, String path, int spritesWidth, int spritesHeight) {
		this.id = id;
		this.spritesWidth = spritesWidth;
		this.spritesHeight = spritesHeight;
		readPixels(path);
		if (spriteSheets[id] != null)
			throw new RuntimeException("SpriteSheet id " + id + " duplicated.");
		spriteSheets[id] = this;
	}

	/**
	 * Méthode de chargement du spriteSheet dans pixels.
	 */
	private void readPixels(String path) {
		try {
			setSheet(ImageIO.read(new File(path)));
			width = getSheet().getWidth();
			height = getSheet().getHeight();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/** Commence à 0. */
	public int[] getSpritePixelsFor(int gridXIndex, int gridYIndex) {
		final int[] res = new int[spritesWidth * spritesHeight];
		final BufferedImage img = getSheet().getSubimage(
				gridXIndex * spritesWidth, gridYIndex * spritesHeight,
				spritesWidth, spritesHeight);
		final PixelGrabber grabber = new PixelGrabber(img, 0, 0, spritesWidth,
				spritesHeight, res, 0, spritesWidth);
		try {
			grabber.grabPixels();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		return res;
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

	public int getId() {
		return id;
	}

	public int getSpritesWidth() {
		return spritesWidth;
	}

	public int getSpritesHeight() {
		return spritesHeight;
	}

	public BufferedImage getSheet() {
		return sheet;
	}

	public void setSheet(BufferedImage sheet) {
		this.sheet = sheet;
	}
}
