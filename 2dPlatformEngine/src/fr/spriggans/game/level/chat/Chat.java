package fr.spriggans.game.level.chat;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import fr.spriggans.game.KeyboardInput;
import fr.spriggans.gfx.Screen;

public class Chat implements IChat {
	private static final int KEY_ENTER_CHAT = KeyEvent.VK_ENTER;
	private static final int KEY_LEAVE_CHAT = KeyEvent.VK_ESCAPE;
	private static final int KEY_SUBMIT_CHAT = KeyEvent.VK_ENTER;
	private static final int KEY_DELETE_CHAT = KeyEvent.VK_BACK_SPACE;
	private static final int STRING_BUFFER_SIZE = 100;
	private static final int MESSAGES_NUMBER = 5;
	private static final int TIME_IN_FRAMES_NEW_MESSAGE_VISIBLE = 480;
	private static final int TIME_IN_FRAMES_DECAYING_MESSAGE = 60;
	private static final int MINIMUM_ALPHA_DISPLAY_VALUE = 25;

	/** Taille entre deux lettres consécutives. */
	private static final int LETTER_SPACE_WIDTH = 7;
	/** Taille du caractère espace seul. */
	private static final int LETTER_SPACE_SIZE = 3;
	/** Taille du retour à la ligne. */
	private static final int LETTER_LINE_RETURN_HEIGHT = 14;

	/** coins NW de début de l'affichage des écritures. */
	private static final int X_NW_DEBUT_CHAT = 25;
	private static final int Y_NW_DEBUT_CHAT = 500;

	/** Taille après laquelle il faut retourner à la ligne. */
	private static final int MAXIMUM_SEQUENCE_WIDTH_IN_PIXELS = 360;
	private static final int NUMBER_OF_LETTERS_PER_LINE = MAXIMUM_SEQUENCE_WIDTH_IN_PIXELS / LETTER_SPACE_WIDTH;
	/** Taille entre le chat et le cadre du chat. */
	private static final int CHAT_MARGIN = 10;
	/** Pixels séparant la zone de saisie des bords du chat. */
	private static final int CHAT_BUFER_INSET = 4;
	private static final int CHAT_HEIGHT = 170;

	/** Inputs */
	private final KeyboardInput keyboard;

	/** Si vrai, alors on est dans le chat. Sinon, on est juste en train de jouer. */
	private boolean insideChat;

	/** Derniers messages affichés. */
	private final List<Message> lastMessages;

	/** String en cours d'écriture. */
	private StringBuilder buffer;

	public Chat(KeyboardInput keyboardInput) {
		this.keyboard = keyboardInput;
		this.insideChat = false;
		this.buffer = new StringBuilder(STRING_BUFFER_SIZE);
		this.lastMessages = new ArrayList<Message>(MESSAGES_NUMBER);

		lastMessages.add(new Message("Console", "Welcome to this alpha version of an unnamed game !"));
	}

	@Override
	public void render(Screen screen) {
		// Cadre autour du chat.

		// TODO déployer le chat.
		// TODO améliorer affichage historiques.
		screen.renderRectangle(X_NW_DEBUT_CHAT + screen.xOffs - CHAT_MARGIN, Y_NW_DEBUT_CHAT + screen.yOffs - CHAT_MARGIN / 2,
				MAXIMUM_SEQUENCE_WIDTH_IN_PIXELS + CHAT_MARGIN, CHAT_HEIGHT + CHAT_MARGIN, 0x22ffffff, true);
	
		// Cadre pour la saisie du texte
		if(insideChat) {
			screen.renderRectangle(X_NW_DEBUT_CHAT + screen.xOffs - CHAT_MARGIN + CHAT_BUFER_INSET, Y_NW_DEBUT_CHAT + screen.yOffs + CHAT_HEIGHT + CHAT_MARGIN/2 - LETTER_LINE_RETURN_HEIGHT * 2 - CHAT_BUFER_INSET - 1,
					MAXIMUM_SEQUENCE_WIDTH_IN_PIXELS + CHAT_MARGIN - CHAT_BUFER_INSET * 2, LETTER_LINE_RETURN_HEIGHT * 2 + 2, 0x33ffffff, true);
		}

		int beginY = Y_NW_DEBUT_CHAT;
		// Affichage des MESSAGES_NUMBER derniers messages écrits.
		if (insideChat) {
			for (int i = lastMessages.size() - MESSAGES_NUMBER; i < lastMessages.size(); i++) {
				if(i >= 0)
					beginY = renderMessage(screen, lastMessages.get(i), beginY, true);
			}			
			// Affichage du message dans le buffer.
			renderMessageBuffer(screen, X_NW_DEBUT_CHAT + CHAT_BUFER_INSET, Y_NW_DEBUT_CHAT  + CHAT_HEIGHT + CHAT_MARGIN/2 - LETTER_LINE_RETURN_HEIGHT * 2 - CHAT_BUFER_INSET);
		} else {
			for (int i = lastMessages.size() - MESSAGES_NUMBER; i < lastMessages.size(); i++) {
				if (i >= 0 && (lastMessages.get(i).isRecent || !lastMessages.get(i).isGone)) {
					beginY = renderMessage(screen, lastMessages.get(i), beginY, false);
				}
			}
			for (Message msg : lastMessages) {
				msg.tick();				
			}
		}
	}

	/** @return y du message suivant. */
	private int renderMessage(Screen screen, Message msg, int beginY, boolean renderNotRecentMessages) {
		int beginX = X_NW_DEBUT_CHAT;

		String alpha = "ff";
		if (!renderNotRecentMessages && !msg.isRecent) {
			int alphaInt = 255 - 255 * (TIME_IN_FRAMES_DECAYING_MESSAGE - msg.timeBeforeDecay) / TIME_IN_FRAMES_DECAYING_MESSAGE;
			alphaInt = alphaInt > 255 ? 255 : (alphaInt < MINIMUM_ALPHA_DISPLAY_VALUE ? 0 : alphaInt);
			if (alphaInt == 0)
				return beginY;
			alpha = Integer.toHexString(alphaInt);
		}

		// Affichage du nom.
		beginX = screen.renderWord(beginX + screen.xOffs, beginY + screen.yOffs, msg.auteur + ":",
				Integer.parseUnsignedInt(alpha + "ff0000", 16), LETTER_SPACE_WIDTH, LETTER_SPACE_SIZE)
				- screen.xOffs;

		// Affichage du message.
		final String[] words = msg.message.split(" ");
		for (final String word : words) {
			// On regarde si le mot qu'on ajoute a la place. Si non, on saute une ligne.
			final int pxlWordSize = word.length() * LETTER_SPACE_WIDTH;

			if (pxlWordSize > MAXIMUM_SEQUENCE_WIDTH_IN_PIXELS) {
				// si le mot ne rentre pas en une ligne => couper en autant de mots qu'il faut et les afficher.
				final List<String> wordInLines = cutWord(word);
				for (final String cutWord : wordInLines) {
					beginY += LETTER_LINE_RETURN_HEIGHT;
					beginX = X_NW_DEBUT_CHAT;
					beginX = screen.renderWord(beginX + screen.xOffs, beginY + screen.yOffs, cutWord,
							Integer.parseUnsignedInt(alpha + "ffffff", 16), LETTER_SPACE_WIDTH, LETTER_SPACE_SIZE)
							- screen.xOffs;
				}
			} else {
				if (beginX + pxlWordSize > MAXIMUM_SEQUENCE_WIDTH_IN_PIXELS) {
					// Trop grand ! On saute une ligne.
					beginY += LETTER_LINE_RETURN_HEIGHT;
					beginX = X_NW_DEBUT_CHAT;
				}
				// On affiche le mot.
				beginX = screen.renderWord(beginX + screen.xOffs, beginY + screen.yOffs, word,
						Integer.parseUnsignedInt(alpha + "ffffff", 16), LETTER_SPACE_WIDTH, LETTER_SPACE_SIZE)
						- screen.xOffs;
			}
		}

		return beginY + LETTER_LINE_RETURN_HEIGHT;
	}
	
	private void renderMessageBuffer(Screen screen, int startX, int beginY) {
		int beginX = startX;
		String messageToDisplay = "> " + buffer.toString();
		// Affichage du message.
		final String[] words = messageToDisplay.split(" ");
		for (final String word : words) {
			// On regarde si le mot qu'on ajoute a la place. Si non, on saute une ligne.
			final int pxlWordSize = word.length() * LETTER_SPACE_WIDTH;

			if (pxlWordSize > MAXIMUM_SEQUENCE_WIDTH_IN_PIXELS) {
				// si le mot ne rentre pas en une ligne => couper en autant de mots qu'il faut et les afficher.
				final List<String> wordInLines = cutWord(word);
				for (final String cutWord : wordInLines) {
					beginY += LETTER_LINE_RETURN_HEIGHT;
					beginX = startX;
					beginX = screen.renderWord(beginX + screen.xOffs, beginY + screen.yOffs, cutWord,
							0xFFFFFFFF, LETTER_SPACE_WIDTH, LETTER_SPACE_SIZE)
							- screen.xOffs;
				}
			} else {
				if (beginX + pxlWordSize > MAXIMUM_SEQUENCE_WIDTH_IN_PIXELS) {
					// Trop grand ! On saute une ligne.
					beginY += LETTER_LINE_RETURN_HEIGHT;
					beginX = startX;
				}
				// On affiche le mot.
				beginX = screen.renderWord(beginX + screen.xOffs, beginY + screen.yOffs, word,
						0xFFFFFFFF, LETTER_SPACE_WIDTH, LETTER_SPACE_SIZE)
						- screen.xOffs;
			}
		}
	}

	@Override
	public String tick(String name) {
		if (!insideChat) {
			// Bouton d'entrée dans le chat.
			if (keyboard.keyDownOnce(KEY_ENTER_CHAT)) {
				insideChat = true;
			}
			return null;
		}

		// Quitter le chat.
		if (keyboard.keyDownOnce(KEY_LEAVE_CHAT)) {
			leaveChat();
			return null;
		}

		// Submiter le chat.
		if (keyboard.keyDownOnce(KEY_SUBMIT_CHAT)) {
			return submitChat(name);
		}

		// Ecrire dans le chat.
		handleInput();

		return null;
	}

	/** Méthode d'écriture dans le buffer. */
	private void handleInput() {
		// TODO Hold to delete.
		if(keyboard.keyDownOnce(KEY_DELETE_CHAT)) {
			// On remove le dernier character.
			buffer.setLength(Math.max(buffer.length() - 1, 0));
			return;
		}
		
		// Ecriture du caractère.
		if(buffer.length() >= STRING_BUFFER_SIZE) {
			return;
		}
		
		boolean shifted = keyboard.keyDown(KeyEvent.VK_SHIFT);

		for (Key k : keyMap)
			if(keyboard.keyDownOnce(k.keyCode)) {
				buffer.append(shifted ? k.majuscule : k.minuscule);
				return;
			}
		
		
		
		
	}

	/** Méthode suivant le bouton de submit du chat. */
	private String submitChat(String name) {
		final String message = buffer.toString();
		leaveChat();
		if (message != null && !message.isEmpty())
			lastMessages.add(new Message(name, message));
		return message;
	}

	/** Méthode suivant le bouton de sortie du chat. */
	private void leaveChat() {
		insideChat = false;
		buffer = new StringBuilder(STRING_BUFFER_SIZE);
	}

	private List<String> cutWord(String word) {
		final List<String> res = new ArrayList<String>();
		while (word.length() > NUMBER_OF_LETTERS_PER_LINE) {
			final String cut = word.substring(0, NUMBER_OF_LETTERS_PER_LINE);
			word = word.substring(NUMBER_OF_LETTERS_PER_LINE, word.length());
			res.add(cut);
		}
		res.add(word);
		return res;
	}

	private class Message {
		String message;
		String auteur;
		boolean isRecent;
		boolean isGone;
		int timeBeforeDecay;

		public Message(String auteur, String message) {
			this.message = message;
			this.auteur = auteur;
			this.isRecent = true;
			this.isGone = false;
			this.timeBeforeDecay = TIME_IN_FRAMES_NEW_MESSAGE_VISIBLE;
		}

		public void tick() {
			if (isRecent) {
				if (timeBeforeDecay-- < 0) {
					isRecent = false;
					timeBeforeDecay = TIME_IN_FRAMES_DECAYING_MESSAGE;
				}
			} else if (!isGone && timeBeforeDecay-- < 0) {
				isGone = true;
			}
		}
	}
	
	private final Key[] keyMap = { new Key(KeyEvent.VK_A, "a", "A"),
		new Key(KeyEvent.VK_B, "b", "B"),
		new Key(KeyEvent.VK_C, "c", "C"),
		new Key(KeyEvent.VK_D, "d", "D"),
		new Key(KeyEvent.VK_E, "e", "E"),
		new Key(KeyEvent.VK_F, "f", "F"),
		new Key(KeyEvent.VK_G, "g", "G"),
		new Key(KeyEvent.VK_H, "h", "H"),
		new Key(KeyEvent.VK_I, "i", "I"),
		new Key(KeyEvent.VK_J, "j", "J"),
		new Key(KeyEvent.VK_K, "k", "K"),
		new Key(KeyEvent.VK_L, "l", "L"),
		new Key(KeyEvent.VK_M, "m", "M"),
		new Key(KeyEvent.VK_N, "n", "N"),
		new Key(KeyEvent.VK_O, "o", "O"),
		new Key(KeyEvent.VK_P, "p", "P"),
		new Key(KeyEvent.VK_Q, "q", "Q"),
		new Key(KeyEvent.VK_R, "r", "R"),
		new Key(KeyEvent.VK_S, "s", "S"),
		new Key(KeyEvent.VK_T, "t", "T"),
		new Key(KeyEvent.VK_U, "u", "U"),
		new Key(KeyEvent.VK_V, "v", "V"),
		new Key(KeyEvent.VK_W, "w", "W"),
		new Key(KeyEvent.VK_X, "x", "X"),
		new Key(KeyEvent.VK_Y, "y", "Y"),
		new Key(KeyEvent.VK_Z, "z", "Z"),
		new Key(KeyEvent.VK_DEAD_TILDE, "~", ""),
		new Key(KeyEvent.VK_DEAD_CIRCUMFLEX, "^", ""),
		new Key(KeyEvent.VK_DEAD_GRAVE, "`", ""),
		new Key(KeyEvent.VK_EXCLAMATION_MARK, "!", "§"),
		new Key(KeyEvent.VK_0, "à", "0"),
		new Key(KeyEvent.VK_1, "&", "1"),
		new Key(KeyEvent.VK_2, "é", "2"),
		new Key(KeyEvent.VK_3, "\"", "3"),
		new Key(KeyEvent.VK_4, "'", "4"),
		new Key(KeyEvent.VK_5, "(", "5"),
		new Key(KeyEvent.VK_6, "-", "6"),
		new Key(KeyEvent.VK_7, "è", "7"),
		new Key(KeyEvent.VK_8, "_", "8"),
		new Key(KeyEvent.VK_9, "ç", "9"),
		new Key(KeyEvent.VK_NUMPAD0, "0", ""),
		new Key(KeyEvent.VK_NUMPAD1, "1", ""),
		new Key(KeyEvent.VK_NUMPAD2, "2", ""),
		new Key(KeyEvent.VK_NUMPAD3, "3", ""),
		new Key(KeyEvent.VK_NUMPAD4, "4", ""),
		new Key(KeyEvent.VK_NUMPAD5, "5", ""),
		new Key(KeyEvent.VK_NUMPAD6, "6", ""),
		new Key(KeyEvent.VK_NUMPAD7, "7", ""),
		new Key(KeyEvent.VK_NUMPAD8, "8", ""),
		new Key(KeyEvent.VK_NUMPAD9, "9", ""),		
		new Key(KeyEvent.VK_SPACE, " ", " "),
		};

	private class Key {
		int keyCode;
		String minuscule;
		String majuscule;
		public Key(int keyCode, String minuscule, String majuscule) {
			this.keyCode = keyCode;
			this.minuscule = minuscule;
			this.majuscule = majuscule;
		}
	}
}
