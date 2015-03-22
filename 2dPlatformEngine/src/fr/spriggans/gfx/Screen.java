package fr.spriggans.gfx;

/**
 * Classe d'affichage à l'écran.
 *
 * @author Spriggans
 */
public class Screen {
	public static final int MIRROR_HORIZONTAL = 1;
	public static final int MIRROR_VERTICAL = 2;

	/**
	 * Lorsque le player arrive à cette distance du bord de la map, la view se
	 * décale. TODO : Comportement à changer lorsque la classe caméra arrivera.
	 * TODO : Ajouter un sliderDistance vertical et horinzontal.
	 */
	public int screenSlideDistance = 200;

	private int width;
	private int height;
	public int xOffs = 0;
	public int yOffs = 0;
	private int pixels[];

	// Paramètres pour éviter un bug si affichage en dehors de l'écran.
	private int compHeight1 = 0;
	private int compHeight2 = 0;
	private int compWidth1 = 0;
	private int compWidth2 = 0;

	public Screen(int w, int h) {
		width = w;
		height = h;
		pixels = new int[width * height];

		screenSlideDistance = (Math.min(w, h)) / 2;
	}

	/**
	 * Méthode à appeler à chaque changement de level !
	 */
	public void calibrateScreenToLevel(int lvlWidth, int lvlHeight) {
		final int yDiff = height - lvlHeight;
		final int xDiff = width - lvlWidth;
		compHeight1 = yDiff > 0 ? yDiff / 2 : 0;
		compHeight2 = yDiff > 0 ? lvlHeight + 1 + yDiff / 2 : height;
		compWidth1 = xDiff > 0 ? xDiff / 2 : 0;
		compWidth2 = xDiff > 0 ? lvlWidth + 1 + xDiff / 2 : width;
	}

	/**
	 * x,y coin NW.
	 */
	public void renderRectangle(int x, int y, int w, int h, int col) {
		x -= xOffs;
		y -= yOffs;
		for (int j = 0; j < h; j++) {
			final int jPixel = j + y;
			if (jPixel < compHeight1 || jPixel >= compHeight2)
				continue;
			for (int i = 0; i < w; i++) {
				final int iPixel = i + x;
				if (iPixel < compWidth1 || iPixel >= compWidth2)
					continue;
				if (col == 0x00000000)
					continue;
				pixels[jPixel * width + iPixel] = col;
			}
		}
	}

	public void renderSquare(int x, int y, int size, int col) {
		renderRectangle(x, y, size, size, col);
	}

	public void renderPixel(int p, int x, int y) {
		if (p == 0x00000000)
			return;
		final int xPxl = x - xOffs;
		final int yPxl = y - yOffs;
		if (isOutsideScreen(xPxl, yPxl, 0, 0))
			return;
		pixels[yPxl * width + xPxl] = p;
	}

	/**
	 * x,y coin NW.
	 */
	public void renderPixels(int[] pxls, int w, int h, int x, int y,
			int mirrorBits) {
		if (isOutsideScreen(x - xOffs, y - yOffs, w, h))
			return;
		if ((mirrorBits & MIRROR_HORIZONTAL) == MIRROR_HORIZONTAL) {
			if ((mirrorBits & MIRROR_VERTICAL) == MIRROR_VERTICAL)
				for (int j = 0; j < h; j++)
					for (int i = 0; i < w; i++)
						renderPixel(pxls[j * w + i], x + w - i, y + h - j);
			else
				for (int j = 0; j < h; j++)
					for (int i = 0; i < w; i++)
						renderPixel(pxls[j * w + i], x + w - i, y + j);
			return;
		} else if ((mirrorBits & MIRROR_VERTICAL) == MIRROR_VERTICAL) {
			for (int j = 0; j < h; j++)
				for (int i = 0; i < w; i++)
					renderPixel(pxls[j * w + i], x + i, y + h - j);
			return;
		}
		for (int j = 0; j < h; j++)
			for (int i = 0; i < w; i++)
				renderPixel(pxls[j * w + i], x + i, y + j);
	}

	public boolean isOutsideScreen(int x, int y, int w, int h) {
		return x + w < compWidth1 || x >= compWidth2 || y + h < compHeight1
				|| y >= compHeight2;
	}

	public void blackOut() {
		for (int i = 0; i < pixels.length; i++)
			pixels[i] = 0xff000000;
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

	public int[] getPixels() {
		return pixels;
	}

	public void setPixels(int pixels[]) {
		this.pixels = pixels;
	}

	public void setOffsets(int xOffs, int yOffs) {
		this.xOffs = xOffs;
		this.yOffs = yOffs;
	}
}
