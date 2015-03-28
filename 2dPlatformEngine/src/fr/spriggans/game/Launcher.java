package fr.spriggans.game;

public class Launcher {
	public static final boolean DEBUG = true;
	private static final int WIDTH = 1200;
	private static final int HEIGHT = WIDTH * 9 / 16;

	public static void main(String[] args) {
		final MainComponent g = new MainComponent();
		g.init(WIDTH, HEIGHT);
		g.start();
	}
}
