package fr.spriggans.game.level.chat;

import java.util.Arrays;

import fr.spriggans.game.level.Level;
import fr.spriggans.game.level.levelObjects.landscape.LandscapeCollidableRectangle;

public class ChatCommandInterpretor {

	private static final String COMMAND_SYMBOL = "/";

	private static final String CONSOLE = "Console";

	/** @param chatString doit être non null. */
	public static void interpretString(Level level, String player, String chatString) {
		if (chatString.startsWith(COMMAND_SYMBOL))
			executeCommand(level, player, chatString);
	}

	/** @param chatString doit être non null. */
	public static void executeCommand(Level level, String player, String chatString) {
		final String[] words = chatString.split("\\s+");
		final String command = words[0].substring(1);
		final String[] args = Arrays.copyOfRange(words, 1, words.length);
		if (command == null || "".equals(command) || COMMAND_SYMBOL.equals(command)) {
			tellMessageToLevel(level, "Invalid command.");
		} else if ("addRectangle".equals(command) || "aR".equals(command)) {
			if (args.length != 4) {
				tellMessageToLevel(level, "Invalid arguments. Expected x y w h.");
				return;
			}
			try {
				level.getCollisionLayer().add(
						new LandscapeCollidableRectangle(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]),
								Integer.parseInt(args[3])));
			} catch (final NumberFormatException e) {
				tellMessageToLevel(level, "Invalid arguments types. Expected integers.");
				return;
			}
		} else if ("exit".equals(command) || "quit".equals(command)) {
			if (args.length != 0) {
				tellMessageToLevel(level, "Invalid arguments. Expected none.");
				return;
			}
			level.setOver(true);
		} else {
			tellMessageToLevel(level, "Invalid command.");
		}

	}

	public static void tellMessageToLevel(Level level, String message) {
		tellMessageToLevel(level, CONSOLE, message);
	}

	public static void tellMessageToLevel(Level level, String autor, String message) {
		level.addMessageToChat(autor, message);
	}

}
