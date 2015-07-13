package fr.spriggans.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {

	private static final int KEY_COUNT = 1024;

	private boolean isLockedInChat = false;

	private enum KeyState {
		RELEASED, PRESSED, ONCE
	}

	// Current state of the keyboard
	private boolean[] currentKeys = null;

	// Polled keyboard state
	private KeyState[] keys = null;

	public KeyboardInput(MainComponent component) {
		currentKeys = new boolean[KEY_COUNT];
		keys = new KeyState[KEY_COUNT];
		for (int i = 0; i < KEY_COUNT; ++i) {
			keys[i] = KeyState.RELEASED;
		}
		component.addKeyListener(this);
	}

	public synchronized void poll() {
		for (int i = 0; i < KEY_COUNT; ++i) {
			if (currentKeys[i]) {
				if (keys[i] == KeyState.RELEASED)
					keys[i] = KeyState.ONCE;
				else
					keys[i] = KeyState.PRESSED;
			} else {
				keys[i] = KeyState.RELEASED;
			}
		}
	}

	public boolean keyDown(int keyCode) {
		return keys[keyCode] == KeyState.ONCE || keys[keyCode] == KeyState.PRESSED;
	}

	public boolean keyDownOnce(int keyCode) {
		return keys[keyCode] == KeyState.ONCE;
	}

	@Override
	public synchronized void keyPressed(KeyEvent e) {
		final int keyCode = e.getKeyCode();
		if (keyCode >= 0 && keyCode < KEY_COUNT) {
			currentKeys[keyCode] = true;
		}
	}

	@Override
	public synchronized void keyReleased(KeyEvent e) {
		final int keyCode = e.getKeyCode();
		if (keyCode >= 0 && keyCode < KEY_COUNT) {
			currentKeys[keyCode] = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Not needed
	}

	public boolean isLockedInChat() {
		return isLockedInChat;
	}

	public void setLockedInChat(boolean isLockedInChat) {
		this.isLockedInChat = isLockedInChat;
	}
}