package fr.spriggans.gfx;

import java.util.ArrayList;

public class Characters {

	private static final String[] CHARACTERS = { //
	"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",//
	"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",//
	" ", "!", "\"", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/",//
	"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ";", "<", "=", ">", "?",//
	"@", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",//
	"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "[", "\\", "]", "^", "_",//
	"`", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",//
	"p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "{", "|", "}", "~", "TODO",//
	"TODO", "ü", "é", "â", "ä", "à", "TODO", "ç", "ê", "ë", "è", "ï", "î", "ì", "Ä", "Â",//
	"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",//
	"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",//
	};

	private static ArrayList<String> charactersList;

	static {
		charactersList = new ArrayList<String>();
		for (final String string : CHARACTERS) {
			charactersList.add(string);
		}
	}

	/** Ecart en pixels entre deux lettres dans le Bitmap. */
	private static final int GRID_WIDTH = 8;
	private static final int GRID_HEIGHT = 12;

	/** Nombre de lettres par lignes et colonnes dans le Bitmap. */
	private static final int GRID_NB_ELEMENTS_W = 16;
	private static final int GRID_NB_ELEMENTS_H = 16;

	/** Les Offsets ont déjà été appliqués. */
	public static void renderCharacter(Screen screen, int x, int y, String character, int color) {
		try {
			final int characterIndex = charactersList.indexOf(character);
			final int iChar = characterIndex % GRID_NB_ELEMENTS_H;
			final int jChar = characterIndex / GRID_NB_ELEMENTS_W;
			final int[] characterPixels = new int[GRID_HEIGHT * GRID_WIDTH];
			final int iBitmapFirstPixel = iChar * GRID_WIDTH;
			final int jBitmapFirstPixel = jChar * GRID_HEIGHT;

			for (int j = 0; j < GRID_HEIGHT; j++) {
				final int jCurrentPixel = (jBitmapFirstPixel + j) * Bitmap.CHARACTER_SET.getWidth();
				final int jj = j * GRID_WIDTH;
				for (int i = 0; i < GRID_WIDTH; i++) {
					characterPixels[jj + i] = Bitmap.CHARACTER_SET.getPixels()[jCurrentPixel + iBitmapFirstPixel + i];
				}
			}

			screen.renderPixelsUnicolor(characterPixels, GRID_WIDTH, GRID_HEIGHT, x - Bitmap.CHARACTER_SET.getxCenter(), y
					- Bitmap.CHARACTER_SET.getyCenter(), color, true);
		} catch (final Exception e) {
			System.out.println("Unknown character : " + character);
		}
	}
}
