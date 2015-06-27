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
	private static final int STRING_BUFFER_SIZE = 64;
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
	private static final int Y_NW_DEBUT_CHAT = 480;

	/** Taille après laquelle il faut retourner à la ligne. */
	private static final int MAXIMUM_SEQUENCE_WIDTH_IN_PIXELS = 360;
	private static final int NUMBER_OF_LETTERS_PER_LINE = MAXIMUM_SEQUENCE_WIDTH_IN_PIXELS / LETTER_SPACE_WIDTH;
	/** Taille entre le chat et le cadre du chat. */
	private static final int CHAT_MARGIN = 10;

	private static final int CHAT_HEIGHT = 200;

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

		lastMessages.add(new Message("Console", "Ceci est un test avec une phrase quand même assez longue. Est-ce que ça marche ?"));
		lastMessages.add(new Message("Debug", "Je confirme, ça marche !"));

		buffer.append("Hello World !");
	}

	@Override
	public void render(Screen screen) {
		// Cadre autour du chat.
		screen.renderRectangle(X_NW_DEBUT_CHAT + screen.xOffs - CHAT_MARGIN, Y_NW_DEBUT_CHAT + screen.yOffs - CHAT_MARGIN / 2,
				MAXIMUM_SEQUENCE_WIDTH_IN_PIXELS + CHAT_MARGIN, CHAT_HEIGHT + CHAT_MARGIN, 0x22FFFFFF, true);

		int beginY = Y_NW_DEBUT_CHAT;
		if (insideChat) {
			// Affichage des messages déjà écrits.
			for (final Message msg : lastMessages) {
				beginY = renderMessage(screen, msg, beginY, true);
			}
		} else {
			for (final Message msg : lastMessages) {
				if (msg.isRecent || !msg.isGone) {
					beginY = renderMessage(screen, msg, beginY, false);
					msg.tick();
				}
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

		// TODO ecrire

		return null;
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
}
