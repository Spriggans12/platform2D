package fr.spriggans.game;

public class Launcher {
	private static final int WIDTH = 900;
	private static final int HEIGHT = WIDTH * 9 / 16;

	public static void main(String[] args) {
		final MainComponent g = new MainComponent();
		g.init(WIDTH, HEIGHT);
		g.start();
	}
}
