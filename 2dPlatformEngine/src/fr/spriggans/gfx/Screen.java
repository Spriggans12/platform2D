package fr.spriggans.gfx;

/** Classe d'affichage a l'ecran. */
public class Screen {
	public static final int MIRROR_HORIZONTAL = 1;
	public static final int MIRROR_VERTICAL = 2;

	/** Lorsque le player arrive a cette distance du bord de la map, la view se decale.<br/>
	 * TODO : Comportement a changer lorsque la classe Camera arrivera.<br/>
	 * TODO : Ajouter un sliderDistance vertical et horinzontal. */
	public int screenSlideDistance = 200;

	private int width;
	private int height;
	public int xOffs = 0;
	public int yOffs = 0;
	private int pixels[];

	// Parametres pour eviter un bug si affichage en dehors de l'ecran.
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

	/** Methode a appeler a chaque changement de level ! */
	public void calibrateScreenToLevel(int lvlWidth, int lvlHeight) {
		final int yDiff = height - lvlHeight;
		final int xDiff = width - lvlWidth;
		compHeight1 = yDiff > 0 ? yDiff / 2 : 0;
		compHeight2 = yDiff > 0 ? lvlHeight + 1 + yDiff / 2 : height;
		compWidth1 = xDiff > 0 ? xDiff / 2 : 0;
		compWidth2 = xDiff > 0 ? lvlWidth + 1 + xDiff / 2 : width;
	}

	/** x,y coin NW. */
	public void renderRectangle(int x, int y, int w, int h, int col) {
		renderRectangle(x, y, w, h, col, false, 0);
	}

	public void renderRectangle(int x, int y, int w, int h, int col, boolean outlineOnly, int outlineSize) {
		x -= xOffs;
		y -= yOffs;
		for (int j = 0; j < h; j++) {
			final int jPixel = j + y;
			if (jPixel < compHeight1 || jPixel >= compHeight2)
				continue;
			for (int i = 0; i < w; i++) {
				if (outlineOnly && j > outlineSize - 1 && j < h - outlineSize && i > outlineSize - 1 && i < w - outlineSize) {
					continue;
				}
				final int iPixel = i + x;
				if (iPixel < compWidth1 || iPixel >= compWidth2)
					continue;
				if (col == 0x00000000)
					continue;
				pixels[jPixel * width + iPixel] = col;
			}
		}
	}

	public void renderParallelogram(int x, int y, int w, int h, int dh, int col) {
		x -= xOffs;
		y -= yOffs;

		// On render les traits verticaux.
		for (int j = 0; j < h; j++) {
			final int jPixel = j + y;
			if (jPixel < compHeight1 || jPixel >= compHeight2)
				continue;
			if (x < compWidth1 || x > compWidth2)
				continue;
			pixels[jPixel * width + x] = col;
		}
		for (int j = 0; j < h; j++) {
			final int jPixel = j + y + dh;
			if (jPixel < compHeight1 || jPixel >= compHeight2)
				continue;
			final int iPixel = x + w;
			if (!(iPixel < compWidth1 || iPixel > compWidth2))
				pixels[jPixel * width + iPixel] = col;
		}

		// On render les traits diagonaux
		line(x, y, x + w, y + dh, col);
		line(x, y + h, x + w, y + dh + h, col);

	}

	/** Attention : il faut que x et y aient déjà comptés les offsets. */
	private void line(int x, int y, int x2, int y2, int color) {
		final int w = x2 - x;
		final int h = y2 - y;
		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
		if (w < 0)
			dx1 = -1;
		else if (w > 0)
			dx1 = 1;
		if (h < 0)
			dy1 = -1;
		else if (h > 0)
			dy1 = 1;
		if (w < 0)
			dx2 = -1;
		else if (w > 0)
			dx2 = 1;
		int longest = Math.abs(w);
		int shortest = Math.abs(h);
		if (!(longest > shortest)) {
			longest = Math.abs(h);
			shortest = Math.abs(w);
			if (h < 0)
				dy2 = -1;
			else if (h > 0)
				dy2 = 1;
			dx2 = 0;
		}
		int numerator = longest >> 1;
		for (int i = 0; i <= longest; i++) {
			if (!isOutsideScreen(x, y, 0, 0))
				pixels[y * width + x] = color;

			numerator += shortest;
			if (!(numerator < longest)) {
				numerator -= longest;
				x += dx1;
				y += dy1;
			} else {
				x += dx2;
				y += dy2;
			}
		}
	}

	/** @param character est une seule lettre. */
	public void renderCharacter(int x, int y, String character, int col) {
		Characters.renderCharacter(this, x, y, character, col);
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

	/** Les offsets doivent déjà avoir été fait. */
	public void renderPixelNoOffset(int p, int x, int y) {
		if (p == 0x00000000)
			return;
		if (isOutsideScreen(x, y, 0, 0))
			return;
		pixels[y * width + x] = p;
	}

	/** x,y coin NW. */
	public void renderPixels(int[] pxls, int w, int h, int x, int y, int mirrorBits) {
		x -= xOffs;
		y -= yOffs;
		if (isOutsideScreen(x, y, w, h))
			return;
		if ((mirrorBits & MIRROR_HORIZONTAL) == MIRROR_HORIZONTAL) {
			if ((mirrorBits & MIRROR_VERTICAL) == MIRROR_VERTICAL)
				for (int j = 0; j < h; j++)
					for (int i = 0; i < w; i++)
						renderPixelNoOffset(pxls[j * w + i], x + w - i, y + h - j);
			else
				for (int j = 0; j < h; j++)
					for (int i = 0; i < w; i++)
						renderPixelNoOffset(pxls[j * w + i], x + w - i, y + j);
			return;
		} else if ((mirrorBits & MIRROR_VERTICAL) == MIRROR_VERTICAL) {
			for (int j = 0; j < h; j++)
				for (int i = 0; i < w; i++)
					renderPixelNoOffset(pxls[j * w + i], x + i, y + h - j);
			return;
		}
		for (int j = 0; j < h; j++)
			for (int i = 0; i < w; i++)
				renderPixelNoOffset(pxls[j * w + i], x + i, y + j);
	}

	public void renderPixelsUnicolor(int[] pxls, int w, int h, int x, int y, int color) {
		x -= xOffs;
		y -= yOffs;
		if (isOutsideScreen(x, y, w, h))
			return;
		for (int j = 0; j < h; j++)
			for (int i = 0; i < w; i++) {
				int p = pxls[j * w + i];
				if (p == 0x00000000)
					continue;
				else
					p = color;
				renderPixelNoOffset(p, x + i, y + j);
			}
	}

	public void renderEllipse(int xNW, int yNW, int totalW, int totalH, int col) {
		xNW -= xOffs;
		yNW -= yOffs;
		final int xCenter = xNW + totalW / 2;
		final int yCenter = yNW + totalH / 2;
		final float rX2 = totalW * totalW / 4;
		final float rY2 = totalH * totalH / 4;

		for (int j = 0; j < totalH; j++) {
			final int jPixel = j + yNW;
			if (jPixel < compHeight1 || jPixel >= compHeight2)
				continue;
			int yDiff2 = jPixel - yCenter;
			yDiff2 *= yDiff2;
			for (int i = 0; i < totalW; i++) {
				final int iPixel = i + xNW;
				if (iPixel < compWidth1 || iPixel >= compWidth2)
					continue;
				if (col == 0x00000000)
					continue;
				final int xDiff = iPixel - xCenter;
				if (xDiff * xDiff / rX2 + yDiff2 / rY2 < 1)
					pixels[jPixel * width + iPixel] = col;
			}
		}

	}

	public boolean isOutsideScreen(int x, int y, int w, int h) {
		return x + w < compWidth1 || x >= compWidth2 || y + h < compHeight1 || y >= compHeight2;
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
