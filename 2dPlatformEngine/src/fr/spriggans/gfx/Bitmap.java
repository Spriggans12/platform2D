package fr.spriggans.gfx;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/** Un Bitmap est utilisé pour charger une image qui n'est pas animée (ex: backgrounds). */
public class Bitmap {
	public static final Bitmap[] bitmaps = new Bitmap[128];
	public static final Bitmap GASPARD = new Bitmap(0, "res/bitmaps/gaspard.png", 0, 0);
	public static final Bitmap BG_TEST = new Bitmap(1, "res/bitmaps/bg_test.jpg", 0, 0);

	public static final Bitmap CHARACTER_SET = new Bitmap(2, "res/bitmaps/curses_640x300.png", 0, 0);

	private int id;
	/** Si l'objet est en x0, on commence a dessiner le bitmap a x0 - xCenter */
	private int xCenter;
	/** Si l'objet est en y0, on commence a dessiner le bitmap a y0 - yCenter */
	private int yCenter;
	private int width;
	private int height;
	private int[] pixels;

	public Bitmap(int id, String path, int xCenter, int yCenter) {
		this.setId(id);
		this.setxCenter(xCenter);
		this.setyCenter(yCenter);
		readPixels(path);
		if (bitmaps[id] != null)
			throw new RuntimeException("Bitmap id " + id + " duplicated.");
		bitmaps[id] = this;
	}

	/** Methode de chargement du bitmap dans pixels. */
	private void readPixels(String path) {
		try {
			final BufferedImage img = ImageIO.read(new File(path));
			width = img.getWidth();
			height = img.getHeight();
			pixels = new int[width * height];
			final PixelGrabber grabber = new PixelGrabber(img, 0, 0, width, height, pixels, 0, width);
			grabber.grabPixels();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	/** Render no mirroring. */
	public void render(Screen screen, int x, int y) {
		render(screen, x, y, 0);
	}

	/** Render. Bits de mirrorBits stockés dans Screen. */
	public void render(Screen screen, int x, int y, int mirrorBits) {
		screen.renderPixels(pixels, width, height, x - xCenter, y - yCenter, mirrorBits);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getxCenter() {
		return xCenter;
	}

	public void setxCenter(int xCenter) {
		this.xCenter = xCenter;
	}

	public int getyCenter() {
		return yCenter;
	}

	public void setyCenter(int yCenter) {
		this.yCenter = yCenter;
	}

	public int[] getPixels() {
		return pixels;
	}

	public void setPixels(int[] pixels) {
		this.pixels = pixels;
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
