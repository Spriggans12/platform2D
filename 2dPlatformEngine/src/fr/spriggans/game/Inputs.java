package fr.spriggans.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Inputs implements KeyListener {

	public Inputs(MainComponent mainComponent) {
		mainComponent.addKeyListener(this);
	}

	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();

	public void toggleKey(int keyCode, boolean pressed) {
		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_Z)
			up.toggle(pressed);
		if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S)
			down.toggle(pressed);
		if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_Q)
			left.toggle(pressed);
		if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D)
			right.toggle(pressed);
	}

	public class Key {
		private int nbTimePressed = 0;
		private boolean pressed = false;

		public int getNbTimePressed() {
			return nbTimePressed;
		}

		public boolean isPressed() {
			return pressed;
		}

		public void toggle(boolean pressed) {
			this.pressed = pressed;
			if (pressed)
				nbTimePressed++;
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		toggleKey(arg0.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		toggleKey(arg0.getKeyCode(), false);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
