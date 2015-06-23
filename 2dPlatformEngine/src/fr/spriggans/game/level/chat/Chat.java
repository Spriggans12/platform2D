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
	private static final int TIME_IN_FRAMES_NEW_MESSAGE_VISIBLE = 420;
	private static final int TIME_IN_FRAMES_DECAYING_MESSAGE = 60;
	
	/** Taille entre deux lettres consécutives. */
	private static final int LETTER_SPACE_WIDTH = 7;
	/** Taille du caractère espace seul. */
	private static final int LETTER_SPACE_SIZE = 3;
	/** Taille du retour à la ligne. */
	private static final int LETTER_LINE_RETURN_HEIGHT = 14;  

	/** Inputs */
	private KeyboardInput keyboard;

	/**
	 * Si vrai, alors on est dans le chat. Sinon, on est juste en train de
	 * jouer.
	 */
	private boolean insideChat;

	/** Derniers messages affichés. */
	private List<Message> lastMessages;

	/** String en cours d'écriture. */
	private StringBuilder buffer;
	
	
	// TODO Mettre dans le constructeur.
	/** coins NW de début de l'affichage des écritures. */
	private int xNWDebutChat = 25;
	private int yNWDebutChat = 480;
	/** Taille après laquelle il faut retourner à la ligne. */
	private int maximumSequenceWidthInPixels = 360;
	private int numberOfLettersPerLine;
	

	public Chat(KeyboardInput keyboardInput) {
		this.keyboard = keyboardInput;
		this.insideChat = false;
		this.buffer = new StringBuilder(STRING_BUFFER_SIZE);
		this.lastMessages = new ArrayList<Message>(MESSAGES_NUMBER);
		this.numberOfLettersPerLine = maximumSequenceWidthInPixels / LETTER_SPACE_WIDTH;
		
		
		lastMessages.add(new Message("Console", "Ceci est un test avec une phrase quand même assez longue. Est-ce que ça marche ?"));
		lastMessages.add(new Message("Debug", "Je confirme, ça marche !"));
		
		buffer.append("Hello World !");
	}

	@Override
	public void render(Screen screen) {
		int beginY = yNWDebutChat;
		if (insideChat) {
			// Affichage des messages déjà écrits.
			for (Message msg : lastMessages) {
				beginY = renderMessage(screen, msg, beginY);
			}	
		} else {
			for (Message msg : lastMessages) {
				if(msg.isRecent || !msg.isGone) {
					beginY = renderMessage(screen, msg, beginY);
					msg.tick();
				}
			}
		}
	}
	
	/** @return y du message suivant. */
	private int renderMessage(Screen screen, Message msg, int beginY) {
		int beginX = xNWDebutChat;
		
		String alpha = "ff";
		if(!msg.isRecent){
			// TODO make it decay.
			alpha = "55";
		}
		
		// Affichage du nom.
		beginX = screen.renderWord(beginX + screen.xOffs, beginY + screen.yOffs, msg.auteur + ":", Integer.parseUnsignedInt(alpha + "ff0000", 16), LETTER_SPACE_WIDTH, LETTER_SPACE_SIZE) - screen.xOffs;
		
		// Affichage du message.
		String[] words = msg.message.split(" ");
		for (String word : words) {
			// On regarde si le mot qu'on ajoute a la place. Si non, on saute une ligne.
			int pxlWordSize = word.length() * LETTER_SPACE_WIDTH;
			
			if(pxlWordSize > maximumSequenceWidthInPixels) {
				// si le mot ne rentre pas en une ligne => couper en autant de mots qu'il faut et les afficher.
				 List<String> wordInLines = cutWord(word);
				 for (String cutWord : wordInLines) {
					 beginY += LETTER_LINE_RETURN_HEIGHT;
					 beginX = xNWDebutChat;
					 beginX = screen.renderWord(beginX+screen.xOffs, beginY+screen.yOffs, cutWord, Integer.parseUnsignedInt(alpha + "ffffff", 16), LETTER_SPACE_WIDTH, LETTER_SPACE_SIZE) - screen.xOffs;
				}				
			} else {
				if(beginX + pxlWordSize > maximumSequenceWidthInPixels) {
					// Trop grand ! On saute une ligne.
					beginY += LETTER_LINE_RETURN_HEIGHT;
					beginX = xNWDebutChat;
				}
				// On affiche le mot.
				beginX = screen.renderWord(beginX+screen.xOffs, beginY+screen.yOffs, word, Integer.parseUnsignedInt(alpha + "ffffff", 16), LETTER_SPACE_WIDTH, LETTER_SPACE_SIZE) - screen.xOffs;
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
		String message = buffer.toString();
		leaveChat();
		if(message != null && !message.isEmpty())
			lastMessages.add(new Message(name, message));
		return message;
	}

	/** Méthode suivant le bouton de sortie du chat. */
	private void leaveChat() {
		insideChat = false;
		buffer = new StringBuilder(STRING_BUFFER_SIZE);
	}
	
	private List<String> cutWord(String word) {
		List<String> res = new ArrayList<String>();	
		while(word.length() > numberOfLettersPerLine) {
			String cut = word.substring(0, numberOfLettersPerLine); 
			word = word.substring(numberOfLettersPerLine, word.length());
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
			if(isRecent) {
				if(timeBeforeDecay-- < 0) {
					isRecent = false;
					timeBeforeDecay = TIME_IN_FRAMES_DECAYING_MESSAGE;
				}
			}
			else if(timeBeforeDecay-- < 0) {
				isGone = true;
			}
		}
	}	
}
